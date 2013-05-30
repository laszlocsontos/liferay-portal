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

package com.liferay.portal.kernel.workflow.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowTask;

import java.util.Date;

/**
 * @author Shuyang Zhou
 */
public class BaseWorkflowTaskDueDateComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC = "dueDate ASC, workflowTaskId ASC";

	public static final String ORDER_BY_DESC =
		"dueDate DESC, workflowTaskId DESC";

	public static final String[] ORDER_BY_FIELDS =
		{"dueDate", "workflowTaskId"};

	public BaseWorkflowTaskDueDateComparator() {
		super(false);
	}

	public BaseWorkflowTaskDueDateComparator(boolean ascending) {
		super(ascending);
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		WorkflowTask workflowTask1 = (WorkflowTask)obj1;
		WorkflowTask workflowTask2 = (WorkflowTask)obj2;

		Date dueDate1 = workflowTask1.getDueDate();
		Date dueDate2 = workflowTask2.getDueDate();

		int value = dueDate1.compareTo(dueDate2);

		if (value == 0) {
			Long workflowTaskId1 = workflowTask1.getWorkflowTaskId();
			Long workflowTaskId2 = workflowTask2.getWorkflowTaskId();

			value = workflowTaskId1.compareTo(workflowTaskId2);
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