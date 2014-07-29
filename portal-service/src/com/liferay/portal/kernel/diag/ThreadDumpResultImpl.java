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

import com.liferay.portal.kernel.cluster.Address;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.util.PortalUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.text.Format;

import java.util.Date;

/**
 * @author László Csontos
 */
public class ThreadDumpResultImpl implements ThreadDumpResult {

	public ThreadDumpResultImpl(String threadDump) {
		String hostName = PortalUtil.getComputerName();

		if (ClusterExecutorUtil.isEnabled()) {
			Address localClusterNodeAddress =
				ClusterExecutorUtil.getLocalClusterNodeAddress();

			hostName = localClusterNodeAddress.getDescription();
		}

		_threadDump = new ThreadDump(threadDump, new Date(), hostName);
	}

	@Override
	public String getContentType() {
		return ContentTypes.TEXT_PLAIN;
	}

	@Override
	public String getFileName() {
		return (
			"threadDump-" + _ISO_DATE_FORMAT.format(
				_threadDump.getCreateDate()) + ".txt");
	}

	@Override
	public InputStream getInputStream() {
		String content = _threadDump.getContent();

		return new ByteArrayInputStream(content.getBytes());
	}

	@Override
	public ThreadDump getThreadDump() {
		return _threadDump;
	}

	private static final Format _ISO_DATE_FORMAT =
		FastDateFormatFactoryUtil.getSimpleDateFormat("yyyyMMdd'T'HHmmssz");

	private final ThreadDump _threadDump;

}