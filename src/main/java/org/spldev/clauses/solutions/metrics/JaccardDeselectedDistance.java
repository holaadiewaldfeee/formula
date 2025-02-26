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
package org.spldev.clauses.solutions.metrics;

/**
 * Computes the Jaccard distance between two literal arrays. Considers only
 * negative literals.
 * 
 * @author Sebastian Krieter
 */
public class JaccardDeselectedDistance implements DistanceFunction {

	@Override
	public double computeDistance(final int[] literals1, final int[] literals2) {
		double sum = 0;
		double sumA = 0;
		double sumB = 0;
		for (int k = 0; k < literals1.length; k++) {
			final int a = literals1[k] >>> (Integer.SIZE - 1);
			final int b = literals2[k] >>> (Integer.SIZE - 1);
			sumA += a;
			sumB += b;
			sum += a & b;
		}
		final double similarity = sum / ((sumA + sumB) - sum);
		return 1 - similarity;
	}

	@Override
	public String getName() {
		return "JaccardDeselected";
	}

}
