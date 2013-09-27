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

package com.liferay.portlet.trash.util.comparator;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.DefaultOrderByComparator;
import com.liferay.portlet.trash.model.TrashEntry;

/**
 * @author Sergio González
 */
public class EntryUserNameComparator extends DefaultOrderByComparator {

	public static final String[] ORDER_BY_FIELDS = {"userName"};

	public static final String TABLE_NAME = "TrashEntry";

	public EntryUserNameComparator() {
		this(false);
	}

	public EntryUserNameComparator(boolean ascending) {
		super(TABLE_NAME, ORDER_BY_FIELDS, ascending);
	}

	@Override
	protected int doCompare(Object obj1, Object obj2) {
		TrashEntry entry1 = (TrashEntry)obj1;
		TrashEntry entry2 = (TrashEntry)obj2;

		String name1 = StringUtil.toLowerCase(entry1.getUserName());
		String name2 = StringUtil.toLowerCase(entry2.getUserName());

		int value = name1.compareTo(name2);

		return value;
	}

}