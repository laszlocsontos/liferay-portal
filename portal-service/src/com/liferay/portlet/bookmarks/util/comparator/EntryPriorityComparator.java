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

package com.liferay.portlet.bookmarks.util.comparator;

import com.liferay.portal.util.DefaultOrderByComparator;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;

/**
 * @author Brian Wing Shun Chan
 */
public class EntryPriorityComparator extends DefaultOrderByComparator {

	public static final String[] ORDER_BY_FIELDS = {"priority"};

	public static final String TABLE_NAME = "BookmarksEntry";

	public EntryPriorityComparator() {
		this(false);
	}

	public EntryPriorityComparator(boolean ascending) {
		super(TABLE_NAME, ORDER_BY_FIELDS, ascending);
	}

	@Override
	protected int doCompare(Object obj1, Object obj2) {
		BookmarksEntry entry1 = (BookmarksEntry)obj1;
		BookmarksEntry entry2 = (BookmarksEntry)obj2;

		int value = 0;

		if (entry1.getPriority() < entry2.getPriority()) {
			value = -1;
		}
		else if (entry1.getPriority() > entry2.getPriority()) {
			value = 1;
		}

		return value;
	}

}