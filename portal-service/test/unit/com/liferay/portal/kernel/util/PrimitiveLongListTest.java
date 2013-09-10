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
public class PrimitiveLongListTest {

	@Test
	public void testAdd() {
		PrimitiveLongList primitiveLongList = new PrimitiveLongList();

		for (int i = 0; i < _EXPECTED.length; i++) {
			primitiveLongList.add(_EXPECTED[i]);
		}

		Assert.assertEquals(_EXPECTED.length, primitiveLongList.size());

		long[] actual = primitiveLongList.getArray();

		Assert.assertEquals(_EXPECTED.length, actual.length);

		for (int i = 0; i < actual.length; i++) {
			Assert.assertEquals(_EXPECTED[i], actual[i]);
		}
	}

	@Test
	public void testAddAll() {
		PrimitiveLongList primitiveLongList = new PrimitiveLongList();

		primitiveLongList.addAll(_EXPECTED);

		Assert.assertEquals(_EXPECTED.length, primitiveLongList.size());

		long[] actual = primitiveLongList.getArray();

		Assert.assertEquals(_EXPECTED.length, actual.length);

		for (int i = 0; i < actual.length; i++) {
			Assert.assertEquals(_EXPECTED[i], actual[i]);
		}
	}

	@Test
	public void testToString() {
		PrimitiveLongList primitiveLongList = new PrimitiveLongList();

		Assert.assertEquals("[]", primitiveLongList.toString());

		primitiveLongList.addAll(_EXPECTED);

		Assert.assertEquals("[10, 11, 12]", primitiveLongList.toString());

		primitiveLongList.addAll(_EXPECTED);

		Assert.assertEquals(
			"[10, 11, 12, 10, 11, 12]", primitiveLongList.toString());
	}

	private final long[] _EXPECTED = new long[] {10l, 11l, 12l};

}