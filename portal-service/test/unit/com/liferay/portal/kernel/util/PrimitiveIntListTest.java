/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.kernel.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Michael C. Han
 */
public class PrimitiveIntListTest {

	@Test
	public void testAdd() {
		PrimitiveIntList primitiveIntList = new PrimitiveIntList();

		for (int i = 0; i < _EXPECTED.length; i++) {
			primitiveIntList.add(_EXPECTED[i]);
		}

		Assert.assertEquals(_EXPECTED.length, primitiveIntList.size());

		int[] actual = primitiveIntList.getArray();

		Assert.assertEquals(_EXPECTED.length, actual.length);

		for (int i = 0; i < actual.length; i++) {
			Assert.assertEquals(_EXPECTED[i], actual[i]);
		}
	}

	@Test
	public void testAddAll() {
		PrimitiveIntList primitiveIntList = new PrimitiveIntList();

		primitiveIntList.addAll(_EXPECTED);

		Assert.assertEquals(_EXPECTED.length, primitiveIntList.size());

		int[] actual = primitiveIntList.getArray();

		Assert.assertEquals(_EXPECTED.length, actual.length);

		for (int i = 0; i < actual.length; i++) {
			Assert.assertEquals(_EXPECTED[i], actual[i]);
		}
	}

	@Test
	public void testToString() {
		PrimitiveIntList primitiveIntList = new PrimitiveIntList();

		Assert.assertEquals("[]", primitiveIntList.toString());

		primitiveIntList.addAll(_EXPECTED);

		Assert.assertEquals("[10, 11, 12]", primitiveIntList.toString());

		primitiveIntList.addAll(_EXPECTED);

		Assert.assertEquals(
			"[10, 11, 12, 10, 11, 12]", primitiveIntList.toString());
	}

	private final int[] _EXPECTED = new int[] {10, 11, 12};

}