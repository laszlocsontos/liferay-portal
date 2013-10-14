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

package com.liferay.portal.dao.orm.hibernate;

import org.hibernate.dialect.Dialect;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author László Csontos
 */
public class InformixDialectTest {

	@Test(expected = IllegalArgumentException.class)
	public void testGetLimitStringWithInvalidSql() {
		doTestGetLimitString("TEST", null, 0, 0);
	}

	@Test
	public void testGetLimitStringWithOffset() {
		doTestGetLimitString(
			"SELECT DISTINCT 1 FROM SYSTABLES",
			"SELECT SKIP 5 FIRST 5 DISTINCT 1 FROM SYSTABLES", 5, 5);
	}

	@Test
	public void testGetLimitStringWithoutOffset() {
		doTestGetLimitString(
			"SELECT DISTINCT 1 FROM SYSTABLES",
			"SELECT FIRST 5 DISTINCT 1 FROM SYSTABLES", 0, 5);
	}

	protected void doTestGetLimitString(
		String sql, String expectedSql, int offset, int limit) {

		String actualSql = _dialect.getLimitString(sql, offset, limit);

		Assert.assertEquals(expectedSql, actualSql);
	}

	private static Dialect _dialect = new InformixDialect();

}