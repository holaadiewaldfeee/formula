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
package org.spldev.formula.structure.atomic.literal;

import java.util.*;

/**
 * Compares two literals based on their {@code var} object and {@code positive}
 * state.
 *
 * @author Sebastian Krieter
 */
public class LiteralComparator implements Comparator<Literal> {

	@Override
	public int compare(Literal arg0, Literal arg1) {
		if (arg0.isPositive() != arg1.isPositive()) {
			return arg0.isPositive() ? -1 : 1;
		}
		final int nameCompare = arg0.getName().compareTo(arg1.getName());
		if (nameCompare != 0) {
			return nameCompare;
		}
		return arg0.getClass().getCanonicalName().compareTo(arg1.getClass().getCanonicalName());
	}

}
