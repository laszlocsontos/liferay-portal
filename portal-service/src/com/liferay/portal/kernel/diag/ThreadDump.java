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

package com.liferay.portal.kernel.diag;

import com.liferay.portal.kernel.util.StackTraceUtil;

import java.io.Serializable;

import java.util.Date;

/**
 * @author László Csontos
 */
public class ThreadDump implements Serializable {

	public ThreadDump(String content, Date createDate, String hostName) {
		_content = content;
		_createDate = createDate;
		_hostName = hostName;
	}

	public ThreadDump(Throwable t, Date createDate, String hostName) {
		this(StackTraceUtil.getStackTrace(t), createDate, hostName);
	}

	public String getContent() {
		return _content;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public String getHostName() {
		return _hostName;
	}

	private String _content;
	private Date _createDate;
	private String _hostName;

}