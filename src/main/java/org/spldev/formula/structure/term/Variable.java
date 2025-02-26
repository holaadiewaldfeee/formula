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
package org.spldev.formula.structure.term;

import java.util.*;

import org.spldev.formula.structure.*;
import org.spldev.formula.structure.atomic.literal.*;

public abstract class Variable<T> extends Terminal implements Term<T> {

	protected int index;
	protected VariableMap map;

	public Variable(int index, VariableMap map) {
		this.map = Objects.requireNonNull(map);
		this.index = index;
	}

	protected Variable(Variable<T> oldVariable) {
		this.index = oldVariable.index;
		this.map = oldVariable.map;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public String getName() {
		return map.getName(index).orElse("??");
	}

	@Override
	public VariableMap getVariableMap() {
		return map;
	}

	@Override
	public List<Term<T>> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public void setVariableMap(VariableMap map) {
		this.map = Objects.requireNonNull(map);
	}

	@Override
	public void adaptVariableMap(VariableMap newMap) {
		index = newMap.getIndex(getName()).orElse(0);
		this.map = newMap;
	}

	@Override
	public int hashCode() {
		return Objects.hash(index);
	}

	@Override
	public boolean equalsNode(Object other) {
		if (getClass() != other.getClass()) {
			return false;
		}
		return index == ((Variable<?>) other).index;
	}

}
