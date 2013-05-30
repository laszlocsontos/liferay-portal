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

import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portlet.documentlibrary.model.DLFileRank;

/**
 * @author Brian Wing Shun Chan
 */
public class FileRankCreateDateComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC = "DLFileRank.createDate ASC";

	public static final String ORDER_BY_DESC = "DLFileRank.createDate DESC";

	public static final String[] ORDER_BY_FIELDS = {"createDate"};

	public FileRankCreateDateComparator() {
		super(false);
	}

	public FileRankCreateDateComparator(boolean ascending) {
		super(ascending);
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		DLFileRank dlFileRank1 = (DLFileRank)obj1;
		DLFileRank dlFileRank2 = (DLFileRank)obj2;

		int value = DateUtil.compareTo(
			dlFileRank1.getCreateDate(), dlFileRank2.getCreateDate());

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