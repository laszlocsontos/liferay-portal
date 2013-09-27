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

import com.liferay.portal.util.DefaultOrderByComparator;
import com.liferay.portlet.documentlibrary.model.DLContent;

/**
 * @author Shuyang Zhou
 */
public class DLContentVersionComparator extends DefaultOrderByComparator {

	public static final String[] ORDER_BY_FIELDS = {"version"};

	public static final String TABLE_NAME = "DLContent";

	public DLContentVersionComparator() {
		this(false);
	}

	public DLContentVersionComparator(boolean ascending) {
		super(TABLE_NAME, ORDER_BY_FIELDS, ascending);
	}

	@Override
	protected int doCompare(Object obj1, Object obj2) {
		DLContent content1 = (DLContent)obj1;
		DLContent content2 = (DLContent)obj2;

		String version1 = content1.getVersion();
		String version2 = content2.getVersion();

		int value = version1.compareTo(version2);

		return value;
	}

}