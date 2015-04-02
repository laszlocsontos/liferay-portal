/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.productivity.center.servlet.taglib.ui;

import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Adolfo Pérez
 */
public class PanelContentTag extends IncludeTag {

	@Override
	public void setPortletId(String portletId) {
		_portletId = portletId;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_portletId = null;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"productivity-center-ui:panel-content:portletId", _portletId);
	}

	private static final String _PAGE = "/taglib/ui/panel_content/page.jsp";

	private String _portletId;

}