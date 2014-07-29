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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.cluster.Address;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactoryUtil;
import com.liferay.portal.util.PortalUtil;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.text.Format;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author László Csontos
 */
public class ThreadDumpResult implements Serializable {

	public ThreadDumpResult(int clusterNodeAddressCount) {
		_threadDumps = new ThreadDump[clusterNodeAddressCount];

		if (clusterNodeAddressCount > 1) {
			_countDownLatch = new CountDownLatch(clusterNodeAddressCount);
		}
		else {
			_countDownLatch = null;
		}
	}

	public ThreadDumpResult(String threadDump) {
		this(1);

		String hostName = PortalUtil.getComputerName();

		if (ClusterExecutorUtil.isEnabled()) {
			Address localClusterNodeAddress =
				ClusterExecutorUtil.getLocalClusterNodeAddress();

			hostName = localClusterNodeAddress.getDescription();
		}

		_threadDumps[0] = new ThreadDump(threadDump, new Date(), hostName);
	}

	public void addError(String hostName, Throwable t) {
		addThreadDump(new ThreadDump(t, new Date(), hostName));
	}

	public void addThreadDump(ThreadDump threadDump) {
		if (_currentCount >= _threadDumps.length) {
			throw new ArrayIndexOutOfBoundsException(
				"No more than " + _threadDumps.length +
					" thread dump(s) can be added.");
		}

		_threadDumps[_currentCount++] = threadDump;

		if (_countDownLatch != null) {
			_countDownLatch.countDown();
		}
	}

	public void await() {
		try {
			_countDownLatch.await();
		}
		catch (InterruptedException ie) {
			_log.error(ie);
		}
	}

	public String getContent() {
		return getThreadDump().getContent();
	}

	public byte[] getContentBytes() {
		if (!isClusterWide()) {
			String content = _threadDumps[0].getContent();

			return content.getBytes();
		}

		byte[] contentBytes = null;

		try {
			InputStream contentInputStream = getContentInputStream();

			UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
				new UnsyncByteArrayOutputStream();

			StreamUtil.transfer(
				contentInputStream, unsyncByteArrayOutputStream, true);

			contentBytes = unsyncByteArrayOutputStream.toByteArray();
		}
		catch (IOException e) {
			_log.error(e);
		}

		return contentBytes;
	}

	public String getContentFileName() {
		if (!isClusterWide()) {
			ThreadDump threadDump = _threadDumps[0];

			return _doGetContentFileName(
				threadDump.getCreateDate(), threadDump.getHostName());
		}

		return _doGetContentFileName(null, null);
	}

	public InputStream getContentInputStream() throws IOException {
		if (!isClusterWide()) {
			String content = _threadDumps[0].getContent();

			return new ByteArrayInputStream(content.getBytes());
		}

		ZipWriter zipWriter = ZipWriterFactoryUtil.getZipWriter();

		for (ThreadDump threadDump : _threadDumps) {
			String fileName = _doGetContentFileName(
				threadDump.getCreateDate(), threadDump.getHostName());

			zipWriter.addEntry(
				StringPool.SLASH + fileName, threadDump.getContent());
		}

		return new FileInputStream(zipWriter.getFile());
	}

	public String getContentType() {
		if (isClusterWide()) {
			return ContentTypes.APPLICATION_ZIP;
		}

		return ContentTypes.TEXT_PLAIN;
	}

	public ThreadDump getThreadDump() {
		if (isClusterWide()) {
			throw new UnsupportedOperationException(
				"A single thread dump cannot be requested from cluster wide " +
					"thread dumps.");
		}

		return _threadDumps[0];
	}

	public boolean isClusterWide() {
		if (_threadDumps.length > 1) {
			return true;
		}

		return false;
	}

	private String _doGetContentFileName(Date createDate, String hostName) {
		StringBundler sb = null;

		if (Validator.isNotNull(hostName)) {
			sb = new StringBundler(7);
		}
		else {
			sb = new StringBundler(5);
		}

		if (createDate == null) {
			createDate = new Date();
		}

		sb.append("threadDump");
		sb.append(StringPool.DASH);
		sb.append(_ISO_DATE_FORMAT.format(createDate));

		if (Validator.isNotNull(hostName)) {
			sb.append(StringPool.DASH);
			sb.append(hostName);
		}

		sb.append(StringPool.PERIOD);

		String extension = "txt";

		if (isClusterWide()) {
			extension = "zip";
		}

		sb.append(extension);

		return sb.toString();
	}

	private static final Format _ISO_DATE_FORMAT =
		FastDateFormatFactoryUtil.getSimpleDateFormat("yyyyMMdd'T'HHmmssz");

	private static Log _log = LogFactoryUtil.getLog(ThreadDumpResult.class);

	private final transient CountDownLatch _countDownLatch;
	private int _currentCount = 0;
	private final ThreadDump[] _threadDumps;

}