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

import com.liferay.portal.kernel.bean.BeanPropertiesUtil;

import java.io.Serializable;

import java.util.Comparator;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
@SuppressWarnings("rawtypes")
public abstract class OrderByComparator implements Comparator, Serializable {

	public static final String TABLE_NAME = "[$TABLE$]";

	public OrderByComparator() {
		this(false);
	}

	public OrderByComparator(boolean ascending) {
		this.ascending = ascending;
	}

	@Override
	public abstract int compare(Object obj1, Object obj2);

	public String getOrderBy() {
		return getOrderBy(null);
	}

	public String getOrderBy(String tableName) {
		String orderBy = null;

		if (isAscending()) {
			orderBy = getOrderByAsc();
		}
		else {
			orderBy = getOrderByDesc();
		}

		if (Validator.isNull(orderBy)) {
			return null;
		}

		if (orderBy.indexOf(TABLE_NAME) < 0) {
			return orderBy;
		}

		if (Validator.isNull(tableName)) {
			return StringUtil.replace(
				orderBy, TABLE_NAME.concat(StringPool.PERIOD),
				StringPool.BLANK);
		}

		return StringUtil.replace(orderBy, TABLE_NAME, tableName);
	}

	public String[] getOrderByConditionFields() {
		return getOrderByFields();
	}

	public Object[] getOrderByConditionValues(Object obj) {
		String[] fields = getOrderByConditionFields();

		Object[] values = new Object[fields.length];

		for (int i = 0; i < fields.length; i++) {
			values[i] = BeanPropertiesUtil.getObject(obj, fields[i]);
		}

		return values;
	}

	public String[] getOrderByFields() {
		String orderBy = getOrderBy();

		if (orderBy == null) {
			return null;
		}

		String[] parts = StringUtil.split(orderBy);

		String[] fields = new String[parts.length];

		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];

			int x = part.indexOf(CharPool.PERIOD);
			int y = part.indexOf(CharPool.SPACE, x);

			if (y == -1) {
				y = part.length();
			}

			fields[i] = part.substring(x + 1, y);
		}

		return fields;
	}

	public boolean isAscending() {
		return ascending;
	}

	public boolean isAscending(String field) {
		return isAscending();
	}

	@Override
	public String toString() {
		return getOrderBy();
	}

	protected abstract String getOrderByAsc();

	protected abstract String getOrderByDesc();

	protected boolean ascending;

	private static final String _ORDER_BY_DESC = " DESC";

}