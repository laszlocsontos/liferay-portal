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

package com.liferay.portlet.documentlibrary.util;

import com.liferay.portal.kernel.search.BaseSearcher;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalFolder;

/**
 * @author Julio Camarero
 * @author Eudaldo Alonso
 */
public class JournalSearcher extends BaseSearcher {

	public static final String[] CLASS_NAMES = {
		JournalArticle.class.getName(), JournalFolder.class.getName()
	};

	public static Indexer getInstance() {
		return new JournalSearcher();
	}

	protected JournalSearcher() {
		super(CLASS_NAMES);

		setFilterSearch(true);
		setPermissionAware(true);
	}

}