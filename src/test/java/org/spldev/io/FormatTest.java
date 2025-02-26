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
package org.spldev.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

import org.spldev.formula.structure.*;
import org.spldev.util.data.*;
import org.spldev.util.io.*;
import org.spldev.util.io.format.*;
import org.spldev.util.tree.*;
import org.spldev.util.tree.visitor.*;

/**
 * Tests formats.
 *
 * @author Sebastian Krieter
 */
public class FormatTest {

	private final static Path rootDirectory = Paths.get("src/test/resources");
	private final static Path formatsDirectory = rootDirectory.resolve("formats");

	public static void testLoad(Formula formula1, String name, Format<Formula> format) {
		assertEquals(format.getClass().getCanonicalName(), format.getIdentifier());
		assertTrue(format.supportsParse());
		assertFalse(format.supportsSerialize());

		for (final Path file : getFileList(name, format)) {
			System.out.println(file);
			final Formula formula2 = load(format, file);
			compareFormulas(formula1, formula2);
		}
	}

	public static void testLoadAndSave(Formula formula1, String name, Format<Formula> format) {
		assertEquals(format.getClass().getCanonicalName(), format.getIdentifier());
		assertTrue(format.supportsParse());
		assertTrue(format.supportsSerialize());
		final Formula formula3 = saveAndLoad(formula1, format);
		for (final Path file : getFileList(name, format)) {
			System.out.println(file);
			final Formula formula2 = load(format, file);
			final Formula formula4 = saveAndLoad(formula2, format);
			compareFormulas(formula1, formula2);
			compareFormulas(formula1, formula3);
			compareFormulas(formula1, formula4);
			compareFormulas(formula2, formula3);
			compareFormulas(formula2, formula4);
			compareFormulas(formula3, formula4);
		}
	}

	private static <T> T load(Format<T> format, Path path) {
		return FileHandler.load(path, format).get();
	}

	private static List<Path> getFileList(String name, Format<Formula> format) {
		final String namePattern = Pattern.quote(name) + "_\\d\\d[.]" + format.getFileExtension();
		try {
			final List<Path> fileList = Files.walk(formatsDirectory.resolve(format.getName()))
				.filter(Files::isRegularFile)
				.filter(f -> f.getFileName().toString().matches(namePattern))
				.sorted()
				.collect(Collectors.toList());
			assertNotNull(fileList);
			assertFalse(fileList.isEmpty());
			return fileList;
		} catch (final IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
			return null;
		}
	}

	private static void compareFormulas(final Formula formula1, final Formula formula2) {
		assertEquals(formula1, formula2, "Formulas are different");
		if (formula1 != null) {
			assertEquals(formula1.getVariableMap(), formula2.getVariableMap(), "Variables are different");
		}
	}

	private static <T> T saveAndLoad(T object, Format<T> format) {
		if (object == null) {
			return null;
		}
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			FileHandler.save(object, out, format);
			out.flush();
		} catch (final IOException e) {
			e.printStackTrace();
			fail("Failed saving object: " + e.getMessage());
		}
		final byte[] memory = out.toByteArray();
		assertNotNull(memory);
		assertTrue(memory.length > 0, "Saved object is empty");

		final Result<T> result = FileHandler.load(new ByteArrayInputStream(memory), format);
		assertTrue(result.isPresent(), "Failed loading saved object");
		return result.get();
	}

}
