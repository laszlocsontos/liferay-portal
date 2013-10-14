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

package com.liferay.portal.dao.db;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author László Csontos
 * @author Miguel Pastor
 */
public class InformixDBTest extends BaseDBTestCase {

	@Test
	public void testReplaceTemplate() throws IOException {
		String actualCreateTableQuery = StringUtil.toLowerCase(
			buildSQL(_CREATE_TABLE_QUERY));

		Assert.assertEquals(
			"create table test (" +
				"c1 varchar(200), c2 varchar(255), c3 lvarchar(256))\n",
			actualCreateTableQuery);
	}

	@Test
	public void testRewordRenameTable() throws IOException {
		Assert.assertEquals(
			"rename table a to b;\n", buildSQL(RENAME_TABLE_QUERY));
	}

	@Override
	protected DB getDB() {
		return InformixDB.getInstance();
	}

	private static final String _CREATE_TABLE_QUERY =
		"CREATE TABLE test (c1 VARCHAR(200), c2 VARCHAR(255), c3 VARCHAR(256))";

}