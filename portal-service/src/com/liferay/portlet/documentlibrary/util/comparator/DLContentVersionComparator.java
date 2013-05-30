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

package com.liferay.portlet.documentlibrary.util.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portlet.documentlibrary.model.DLContent;

/**
 * @author Shuyang Zhou
 */
public class DLContentVersionComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC = "DLContent.version ASC";

	public static final String ORDER_BY_DESC = "DLContent.version DESC";

	public static final String[] ORDER_BY_FIELDS = {"version"};

	public DLContentVersionComparator() {
		super(false);
	}

	public DLContentVersionComparator(boolean ascending) {
		super(ascending);
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		DLContent content1 = (DLContent)obj1;
		DLContent content2 = (DLContent)obj2;

		String version1 = content1.getVersion();
		String version2 = content2.getVersion();

		int value = version1.compareTo(version2);

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