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

package com.liferay.portlet.blogs.util.comparator;

import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.util.DefaultOrderByComparator;
import com.liferay.portlet.blogs.model.BlogsEntry;

/**
 * @author Alexander Chow
 */
public class EntryDisplayDateComparator extends DefaultOrderByComparator {

	public static final String[] ORDER_BY_FIELDS = {"displayDate", "entryId"};

	public static final String TABLE_NAME = "BlogsEntry";

	public EntryDisplayDateComparator() {
		this(false);
	}

	public EntryDisplayDateComparator(boolean ascending) {
		super(TABLE_NAME, ORDER_BY_FIELDS, ascending);
	}

	@Override
	protected int doCompare(Object obj1, Object obj2) {
		BlogsEntry entry1 = (BlogsEntry)obj1;
		BlogsEntry entry2 = (BlogsEntry)obj2;

		int value = DateUtil.compareTo(
			entry1.getDisplayDate(), entry2.getDisplayDate());

		if (value == 0) {
			if (entry1.getEntryId() < entry2.getEntryId()) {
				value = -1;
			}
			else if (entry1.getEntryId() > entry2.getEntryId()) {
				value = 1;
			}
		}

		return value;
	}

}