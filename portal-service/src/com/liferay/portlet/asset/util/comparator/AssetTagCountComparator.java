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

package com.liferay.portlet.asset.util.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portlet.asset.model.AssetTag;

/**
 * @author Miguel Pastor
 */
public class AssetTagCountComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC = "AssetTag.assetCount ASC";

	public static final String ORDER_BY_DESC = "AssetTag.assetCount DESC";

	public static final String[] ORDER_BY_FIELDS = {"assetCount"};

	public AssetTagCountComparator() {
		super(false);
	}

	public AssetTagCountComparator(boolean ascending) {
		super(ascending);
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		AssetTag assetTag1 = (AssetTag)obj1;
		AssetTag assetTag2 = (AssetTag)obj2;

		int value = 0;

		if (assetTag1.getAssetCount() < assetTag2.getAssetCount()) {
			value = -1;
		}
		else if (assetTag1.getAssetCount() > assetTag2.getAssetCount()) {
			value = 1;
		}

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