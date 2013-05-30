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

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.softwarecatalog.model.SCProductEntry;

/**
 * @author Brian Wing Shun Chan
 */
public class ProductEntryTypeComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC = "SCProductEntry.type ASC";

	public static final String ORDER_BY_DESC = "SCProductEntry.type DESC";

	public static final String[] ORDER_BY_FIELDS = {"type"};

	public ProductEntryTypeComparator() {
		super(false);
	}

	public ProductEntryTypeComparator(boolean ascending) {
		super(ascending);
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		SCProductEntry productEntry1 = (SCProductEntry)obj1;
		SCProductEntry productEntry2 = (SCProductEntry)obj2;

		String type1 = StringUtil.toLowerCase(productEntry1.getType());
		String type2 = StringUtil.toLowerCase(productEntry2.getType());

		int value = type1.compareTo(type2);

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