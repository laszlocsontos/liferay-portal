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

import com.liferay.portal.model.Group;
import com.liferay.portal.util.DefaultOrderByComparator;

/**
 * @author Brian Wing Shun Chan
 */
public class GroupNameComparator extends DefaultOrderByComparator {

	public static final String[] ORDER_BY_FIELDS = {"groupName"};

	public GroupNameComparator() {
		this(false);
	}

	public GroupNameComparator(boolean ascending) {
		super(null, ORDER_BY_FIELDS, ascending);
	}

	@Override
	protected int doCompare(Object obj1, Object obj2) {
		Group group1 = (Group)obj1;
		Group group2 = (Group)obj2;

		String name1 = group1.getName();
		String name2 = group2.getName();

		int value = name1.compareTo(name2);

		return value;
	}

}