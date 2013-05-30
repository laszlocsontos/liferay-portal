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

package com.liferay.portal.util.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.Group;

/**
 * @author Brian Wing Shun Chan
 */
public class GroupFriendlyURLComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC =
		OrderByComparator.TABLE_NAME.concat(".groupFriendlyURL ASC");

	public static final String ORDER_BY_DESC =
		OrderByComparator.TABLE_NAME.concat(".groupFriendlyURL DESC");

	public static final String[] ORDER_BY_FIELDS = {"groupFriendlyURL"};

	public GroupFriendlyURLComparator() {
		super(false);
	}

	public GroupFriendlyURLComparator(boolean ascending) {
		super(ascending);
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		Group group1 = (Group)obj1;
		Group group2 = (Group)obj2;

		String friendlyURL1 = group1.getFriendlyURL();
		String friendlyURL2 = group2.getFriendlyURL();

		int value = friendlyURL1.compareTo(friendlyURL2);

		if (isAscending()) {
			return value;
		}
		else {
			return -value;
		}
	}

	@Override
	public String[] getOrderByFields() {
		return ORDER_BY_FIELDS;
	}

	@Override
	protected String getOrderByAsc() {
		return ORDER_BY_ASC;
	}

	@Override
	protected String getOrderByDesc() {
		return ORDER_BY_DESC;
	}

}