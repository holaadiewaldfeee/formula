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
package org.spldev.formula.structure.atomic;

import java.util.*;

import org.spldev.util.data.*;

public interface Assignment {

	default void setAll(Collection<Pair<Integer, Object>> assignments) {
		for (final Pair<Integer, Object> pair : assignments) {
			set(pair.getKey(), pair.getValue());
		}
	}

	default void unsetAll(Collection<Pair<Integer, Object>> assignments) {
		for (final Pair<Integer, Object> pair : assignments) {
			set(pair.getKey(), null);
		}
	}

	void unsetAll();

	default void unset(int index) {
		set(index, null);
	}

	void set(int index, Object assignment);

	Optional<Object> get(int index);

	List<Pair<Integer, Object>> getAll();

}
