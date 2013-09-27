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

package com.liferay.portlet.backgroundtask.util.comparator;

import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.util.DefaultOrderByComparator;

/**
 * @author Eduardo Garcia
 */
public class BackgroundTaskCompletionDateComparator
	extends DefaultOrderByComparator {

	public static final String[] ORDER_BY_FIELDS = {"completionDate"};

	public static final String TABLE_NAME = "BackgroundTask";

	public BackgroundTaskCompletionDateComparator() {
		this(false);
	}

	public BackgroundTaskCompletionDateComparator(boolean ascending) {
		super(TABLE_NAME, ORDER_BY_FIELDS, ascending);
	}

	@Override
	protected int doCompare(Object obj1, Object obj2) {
		BackgroundTask backgroundTask1 = (BackgroundTask)obj1;
		BackgroundTask backgroundTask2 = (BackgroundTask)obj2;

		int value = DateUtil.compareTo(
			backgroundTask1.getCompletionDate(),
			backgroundTask2.getCompletionDate());

		return value;
	}

}