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

	@Override
	public abstract int compare(Object obj1, Object obj2);

	public abstract String getOrderBy();

	public abstract String[] getOrderByConditionFields();

	public abstract Object[] getOrderByConditionValues(Object obj);

	public abstract String[] getOrderByFields();

	public boolean isAscending() {
		String orderBy = getOrderBy();

		if ((orderBy == null) ||
			StringUtil.toUpperCase(orderBy).endsWith(_ORDER_BY_DESC)) {

			return false;
		}
		else {
			return true;
		}
	}

	public boolean isAscending(String field) {
		return isAscending();
	}

	@Override
	public String toString() {
		return getOrderBy();
	}

	private static final String _ORDER_BY_DESC = " DESC";

}