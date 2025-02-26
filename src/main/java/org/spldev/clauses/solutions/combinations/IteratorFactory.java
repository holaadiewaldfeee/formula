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
package org.spldev.clauses.solutions.combinations;

/**
 * Instantiates an implementation of {@link CombinationIterator}.
 *
 * @author Sebastian Krieter
 */
public class IteratorFactory {

	public enum IteratorID {
		InverseDefault, Default, Lexicographic, InverseLexicographic, RandomPartition, Partition
	}

	public static CombinationIterator getIterator(IteratorID id, int size, int t) {
		switch (id) {
		case Default:
			return new InverseDefaultIterator(t, size);
		case InverseDefault:
			return new DefaultIterator(t, size);
		case InverseLexicographic:
			return new InverseLexicographicIterator(t, size);
		case Lexicographic:
			return new LexicographicIterator(t, size);
		case Partition:
			return new PartitionIterator(t, size);
		case RandomPartition:
			return new RandomPartitionIterator(t, size);
		default:
			return null;
		}
	}
}
