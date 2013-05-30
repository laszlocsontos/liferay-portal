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

package com.liferay.portal.util.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.Organization;

/**
 * @author Brian Wing Shun Chan
 */
public class OrganizationTypeComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC =
		"[$TABLE$].orgType ASC, [$TABLE$].orgName ASC";

	public static final String ORDER_BY_DESC =
		"[$TABLE$].orgType DESC, [$TABLE$].orgName DESC";

	public static final String[] ORDER_BY_FIELDS = {"type", "name"};

	public OrganizationTypeComparator() {
		super(false);
	}

	public OrganizationTypeComparator(boolean ascending) {
		super(ascending);
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		Organization organization1 = (Organization)obj1;
		Organization organization2 = (Organization)obj2;

		int typeOrder1 = organization1.getTypeOrder();
		int typeOrder2 = organization2.getTypeOrder();

		int value = typeOrder1 - typeOrder2;

		if (value == 0) {
			String name1 = organization1.getName();
			String name2 = organization2.getName();

			value = name1.compareTo(name2);
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