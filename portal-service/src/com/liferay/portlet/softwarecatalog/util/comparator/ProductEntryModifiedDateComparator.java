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

package com.liferay.portlet.softwarecatalog.util.comparator;

import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portlet.softwarecatalog.model.SCProductEntry;

/**
 * @author Brian Wing Shun Chan
 */
public class ProductEntryModifiedDateComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC = "SCProductEntry.modifiedDate ASC";

	public static final String ORDER_BY_DESC =
		"SCProductEntry.modifiedDate DESC";

	public static final String[] ORDER_BY_FIELDS = {"modifiedDate"};

	public ProductEntryModifiedDateComparator() {
		super(false);
	}

	public ProductEntryModifiedDateComparator(boolean ascending) {
		super(ascending);
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		SCProductEntry productEntry1 = (SCProductEntry)obj1;
		SCProductEntry productEntry2 = (SCProductEntry)obj2;

		int value = DateUtil.compareTo(
			productEntry1.getModifiedDate(), productEntry2.getModifiedDate());

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