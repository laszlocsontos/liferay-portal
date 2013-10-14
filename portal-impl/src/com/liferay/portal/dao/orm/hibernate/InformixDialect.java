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

import com.liferay.portal.kernel.util.KMPSearch;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.sql.Types;

/**
 * @author László Csontos
 */
public class InformixDialect extends org.hibernate.dialect.InformixDialect {

	public InformixDialect() {
		registerColumnType(Types.BOOLEAN, "smallint");

		registerKeyword("first");
		registerKeyword("skip");
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		boolean hasOffset = false;

		if ((offset > 0) || forceLimitUsage()) {
			hasOffset = true;
		}

		sql = StringUtil.toUpperCase(sql);

		int pos = KMPSearch.search(sql, _SELECT, _SELECT_NEXTS);

		if (pos == -1) {
			throw new IllegalArgumentException(
				"Query {" + sql + "} contains no SELECT statement");
		}

		StringBundler sb = null;

		if (hasOffset) {
			sb = new StringBundler(6);
		}
		else {
			sb = new StringBundler(3);
		}

		// Offset

		if (hasOffset) {
			sb.append("SKIP ");
			sb.append(offset);
			sb.append(StringPool.SPACE);
		}

		// Limit

		sb.append("FIRST ");
		sb.append(limit);
		sb.append(StringPool.SPACE);

		return StringUtil.insert(sql, sb.toString(), pos + 7);
	}

	@Override
	public boolean supportsLimitOffset() {
		return _SUPPORTS_LIMIT_OFFSET;
	}

	@Override
	public boolean supportsVariableLimit() {
		return _SUPPORTS_VARIABLE_LIMIT;
	}

	@Override
	public boolean useMaxForLimit() {
		return _USE_MAX_FOR_LIMIT;
	}

	private static final String _SELECT = "SELECT";

	private static final int[] _SELECT_NEXTS = KMPSearch.generateNexts(_SELECT);

	private static final boolean _SUPPORTS_LIMIT_OFFSET = true;

	private static final boolean _SUPPORTS_VARIABLE_LIMIT = false;

	private static final boolean _USE_MAX_FOR_LIMIT = false;

}