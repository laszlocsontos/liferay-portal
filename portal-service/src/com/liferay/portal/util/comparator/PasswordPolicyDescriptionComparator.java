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
import com.liferay.portal.model.PasswordPolicy;

/**
 * @author Brian Wing Shun Chan
 */
public class PasswordPolicyDescriptionComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC = "PasswordPolicy.description ASC";

	public static final String ORDER_BY_DESC =
		"PasswordPolicy.description DESC";

	public static final String[] ORDER_BY_FIELDS = {"description"};

	public PasswordPolicyDescriptionComparator() {
		super(false);
	}

	public PasswordPolicyDescriptionComparator(boolean ascending) {
		super(ascending);
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		PasswordPolicy passwordPolicy1 = (PasswordPolicy)obj1;
		PasswordPolicy passwordPolicy2 = (PasswordPolicy)obj2;

		String description1 = passwordPolicy1.getDescription();
		String description2 = passwordPolicy2.getDescription();

		int value = description1.compareTo(description2);

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