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
package org.spldev.formula.structure.term.real;

import java.util.*;

import org.spldev.formula.structure.term.*;

public class RealAdd extends Add<Double> {

	public RealAdd(Term<Double> leftArgument, Term<Double> rightArgument) {
		super(leftArgument, rightArgument);
	}

	private RealAdd() {
		super();
	}

	@Override
	public Class<Double> getType() {
		return Double.class;
	}

	@Override
	public RealAdd cloneNode() {
		return new RealAdd();
	}

	@Override
	public Optional<Double> eval(List<Double> values) {
		if (values.stream().anyMatch(value -> value == null)) {
			return Optional.empty();
		}
		return values.stream().reduce(Double::sum);
	}

}
