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

import org.spldev.formula.structure.*;
import org.spldev.util.tree.visitor.*;

public class NFTester implements TreeVisitor<Boolean, Expression> {

	protected boolean isNf;
	protected boolean isClausalNf;

	@Override
	public void reset() {
		isNf = true;
		isClausalNf = true;
	}

	@Override
	public Boolean getResult() {
		return isNf;
	}

	public boolean isNf() {
		return isNf;
	}

	public boolean isClausalNf() {
		return isClausalNf;
	}

}
