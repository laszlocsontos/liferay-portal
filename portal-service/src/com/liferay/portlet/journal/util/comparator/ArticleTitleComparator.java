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

package com.liferay.portlet.journal.util.comparator;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.DefaultOrderByComparator;
import com.liferay.portlet.journal.model.JournalArticle;

/**
 * @author Brian Wing Shun Chan
 */
public class ArticleTitleComparator extends DefaultOrderByComparator {

	public static final String[] ORDER_BY_FIELDS = {"title"};

	public ArticleTitleComparator() {
		this(false);
	}

	public ArticleTitleComparator(boolean ascending) {
		super(null, ORDER_BY_FIELDS, ascending);
	}

	@Override
	protected int doCompare(Object obj1, Object obj2) {
		JournalArticle article1 = (JournalArticle)obj1;
		JournalArticle article2 = (JournalArticle)obj2;

		String title1 = StringUtil.toLowerCase(article1.getTitle());
		String title2 = StringUtil.toLowerCase(article2.getTitle());

		int value = title1.compareTo(title2);

		return value;
	}

}