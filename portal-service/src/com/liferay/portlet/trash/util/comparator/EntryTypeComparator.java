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

import com.liferay.portal.util.DefaultOrderByComparator;
import com.liferay.portlet.trash.model.TrashEntry;

/**
 * @author Sergio González
 */
public class EntryTypeComparator extends DefaultOrderByComparator {

	public static final String[] ORDER_BY_FIELDS = {"classNameId"};

	public EntryTypeComparator() {
		this(false);
	}

	public EntryTypeComparator(boolean ascending) {
		super(null, ORDER_BY_FIELDS, ascending);
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		TrashEntry entry1 = (TrashEntry)obj1;
		TrashEntry entry2 = (TrashEntry)obj2;

		int value = 0;

		if (entry1.getClassNameId() > entry2.getClassNameId()) {
			value = 1;
		}
		else if (entry1.getClassNameId() < entry2.getClassNameId()) {
			value = -1;
		}

		return value;
	}

}