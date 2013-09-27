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

import java.io.Serializable;

import java.util.Comparator;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
@SuppressWarnings("rawtypes")
public abstract class OrderByComparator implements Comparator, Serializable {

	public OrderByComparator() {
		this(true);
	}

	public OrderByComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		int value = doCompare(obj1, obj2);

		if (isAscending()) {
			return value;
		}
		else {
			return -value;
		}
	}

	public abstract String getOrderBy();

	public abstract String getOrderBy(String tableName);

	public abstract String[] getOrderByConditionFields();

	public abstract Object[] getOrderByConditionValues(Object obj);

	public abstract String[] getOrderByFields();

	public boolean isAscending() {
		return _ascending;
	}

	public abstract boolean isAscending(String field);

	@Override
	public String toString() {
		return getOrderBy();
	}

	protected abstract int doCompare(Object obj1, Object obj2);

	private boolean _ascending;

}