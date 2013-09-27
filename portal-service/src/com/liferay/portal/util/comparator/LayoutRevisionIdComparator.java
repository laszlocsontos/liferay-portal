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

import com.liferay.portal.model.LayoutRevision;
import com.liferay.portal.util.DefaultOrderByComparator;

/**
 * @author Raymond Augé
 */
public class LayoutRevisionIdComparator extends DefaultOrderByComparator {

	public static final String[] ORDER_BY_FIELDS = {"layoutRevisionId"};

	public static final String TABLE_NAME = "LayoutRevision";

	public LayoutRevisionIdComparator() {
		this(false);
	}

	public LayoutRevisionIdComparator(boolean ascending) {
		super(TABLE_NAME, ORDER_BY_FIELDS, ascending);
	}

	@Override
	protected int doCompare(Object obj1, Object obj2) {
		LayoutRevision layoutRevision1 = (LayoutRevision)obj1;
		LayoutRevision layoutRevision2 = (LayoutRevision)obj2;

		int value = 0;

		if (layoutRevision1.getLayoutRevisionId() >
				layoutRevision2.getLayoutRevisionId()) {

			value = 1;
		}
		else if (layoutRevision1.getLayoutRevisionId() <
					layoutRevision2.getLayoutRevisionId()) {

			value = -1;
		}

		return value;
	}

}