/* -----------------------------------------------------------------------------
 * Formula Lib - Library to represent and edit propositional formulas.
 * Copyright (C) 2021-2022  Sebastian Krieter
 * 
 * This file is part of Formula Lib.
 * 
 * Formula Lib is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Formula Lib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Formula Lib.  If not, see <https://www.gnu.org/licenses/>.
 * 
 * See <https://github.com/skrieter/formula> for further information.
 * -----------------------------------------------------------------------------
 */
package org.spldev.formula.structure.transform;

import java.util.*;

import org.spldev.formula.structure.*;
import org.spldev.formula.structure.atomic.literal.*;
import org.spldev.formula.structure.compound.*;
import org.spldev.formula.structure.term.bool.*;
import org.spldev.formula.structure.transform.DistributiveLawTransformer.*;
import org.spldev.formula.structure.transform.NormalForms.*;
import org.spldev.formula.structure.transform.TseytinTransformer.*;
import org.spldev.util.job.*;
import org.spldev.util.tree.*;

public class CNFTransformer implements Transformer {

	public final boolean useMultipleThreads = false;

	protected final List<Formula> distributiveClauses;
	protected final List<Substitute> tseytinClauses;
	protected boolean useDistributive;
	protected int maximumNumberOfLiterals = Integer.MAX_VALUE;

	protected VariableMap variableMap = null;

	public CNFTransformer() {
		if (useMultipleThreads) {
			distributiveClauses = Collections.synchronizedList(new ArrayList<>());
			tseytinClauses = Collections.synchronizedList(new ArrayList<>());
		} else {
			distributiveClauses = new ArrayList<>();
			tseytinClauses = new ArrayList<>();
		}
	}

	public void setMaximumNumberOfLiterals(int maximumNumberOfLiterals) {
		this.maximumNumberOfLiterals = maximumNumberOfLiterals;
	}

	@Override
	public Formula execute(Formula orgFormula, InternalMonitor monitor) {
		useDistributive = (maximumNumberOfLiterals > 0);
		final NFTester nfTester = NormalForms.getNFTester(orgFormula, NormalForm.CNF);
		if (nfTester.isNf) {
			if (!nfTester.isClausalNf()) {
				return NormalForms.toClausalNF(Trees.cloneTree(orgFormula), NormalForm.CNF);
			} else {
				return Trees.cloneTree(orgFormula);
			}
		}
		variableMap = VariableMap.fromExpression(orgFormula).clone();
		Formula formula = NormalForms.simplifyForNF(Trees.cloneTree(orgFormula));
		if (formula instanceof And) {
			final List<Formula> children = ((And) formula).getChildren();
			if (useMultipleThreads) {
				children.parallelStream().forEach(this::transform);
			} else {
				children.forEach(this::transform);
			}
		} else {
			transform(formula);
		}

		formula = new And(getTransformedClauses());
		formula = NormalForms.toClausalNF(formula, NormalForm.CNF);
		formula.setVariableMap(variableMap);
		return formula;
	}

	protected Collection<? extends Formula> getTransformedClauses() {
		final List<Formula> transformedClauses = new ArrayList<>();

		transformedClauses.addAll(distributiveClauses);

		if (!tseytinClauses.isEmpty()) {
			variableMap = variableMap.clone();
			final HashMap<Substitute, Substitute> combinedTseytinClauses = new HashMap<>();
			int count = 0;
			for (final Substitute tseytinClause : tseytinClauses) {
				Substitute substitute = combinedTseytinClauses.get(tseytinClause);
				if (substitute == null) {
					substitute = tseytinClause;
					combinedTseytinClauses.put(substitute, substitute);
					final BoolVariable variable = substitute.getVariable();
					if (variable != null) {
						Optional<BoolVariable> addBooleanVariable;
						do {
							addBooleanVariable = variableMap.addBooleanVariable("__temp__" + count++);
						} while (addBooleanVariable.isEmpty());
						variable.getVariableMap().renameVariable(variable.getIndex(), addBooleanVariable.get()
							.getName());
					}
				} else {
					final BoolVariable variable = substitute.getVariable();
					if (variable != null) {
						final BoolVariable otherVariable = tseytinClause.getVariable();
						otherVariable.getVariableMap().renameVariable(otherVariable.getIndex(), variable.getName());
					}
				}
			}
			for (final Substitute tseytinClause : combinedTseytinClauses.keySet()) {
				for (final Formula formula : tseytinClause.getClauses()) {
					formula.adaptVariableMap(variableMap);
					transformedClauses.add(formula);
				}
			}
		}
		return transformedClauses;
	}

	private void transform(Formula child) {
		final Formula clonedChild = Trees.cloneTree(child);
		if (Formulas.isCNF(clonedChild)) {
			if (clonedChild instanceof And) {
				distributiveClauses.addAll(((And) clonedChild).getChildren());
			} else {
				distributiveClauses.add(clonedChild);
			}
		} else {
			if (useDistributive) {
				try {
					distributiveClauses.addAll(distributive(clonedChild, new NullMonitor()).getChildren());
					return;
				} catch (final MaximumNumberOfLiteralsExceededException e) {
				}
			}
			tseytinClauses.addAll(tseytin(clonedChild, new NullMonitor()));
		}
	}

	protected Compound distributive(Formula child, InternalMonitor monitor)
		throws MaximumNumberOfLiteralsExceededException {
		final CNFDistributiveLawTransformer cnfDistributiveLawTransformer = new CNFDistributiveLawTransformer();
		cnfDistributiveLawTransformer.setMaximumNumberOfLiterals(maximumNumberOfLiterals);
		return cnfDistributiveLawTransformer.execute(child, monitor);

	}

	protected List<Substitute> tseytin(Formula child, InternalMonitor monitor) {
		final TseytinTransformer tseytinTransformer = new TseytinTransformer();
		tseytinTransformer.setVariableMap(VariableMap.emptyMap());
		return tseytinTransformer.execute(child, monitor);
	}

}
