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

package com.liferay.portlet.shopping.util.comparator;

import com.liferay.portal.util.DefaultOrderByComparator;
import com.liferay.portlet.shopping.model.ShoppingItem;

/**
 * @author Brian Wing Shun Chan
 */
public class ItemSKUComparator extends DefaultOrderByComparator {

	public static final String[] ORDER_BY_FIELDS = {"categoryId", "sku"};

	public static final String TABLE_NAME = "ShoppingItem";

	public ItemSKUComparator() {
		this(false);
	}

	public ItemSKUComparator(boolean ascending) {
		super(TABLE_NAME, ORDER_BY_FIELDS, ascending);
	}

	@Override
	protected int doCompare(Object obj1, Object obj2) {
		ShoppingItem item1 = (ShoppingItem)obj1;
		ShoppingItem item2 = (ShoppingItem)obj2;

		Long categoryId1 = new Long(item1.getCategoryId());
		Long categoryId2 = new Long(item2.getCategoryId());

		int value = categoryId1.compareTo(categoryId2);

		if (value == 0) {
			String sku1 = item1.getSku();
			String sku2 = item2.getSku();

			value = sku1.compareTo(sku2);
		}

		return value;
	}

}