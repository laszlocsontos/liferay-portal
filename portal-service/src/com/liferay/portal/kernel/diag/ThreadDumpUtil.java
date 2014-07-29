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
import com.liferay.portal.kernel.cluster.BaseClusterResponseCallback;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterNodeResponse;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.cluster.ClusterResponseCallback;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Tina Tian
 * @author Shuyang Zhou
 * @author László Csontos
 */
public class ThreadDumpUtil {

	public static ThreadDumpResult takeThreadDump() {
		return takeThreadDump(false);
	}

	public static ThreadDumpResult takeThreadDump(boolean clusterWide) {
		if (clusterWide && ClusterExecutorUtil.isEnabled()) {
			return _doClusterWideThreadDump();
		}
		else {
			return _doLocalThreadDump();
		}
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	public static String threadDump() {
		ThreadDumpResult threadDumpResult = takeThreadDump();

		ThreadDump threadDump = threadDumpResult.getThreadDump();

		return "\n\n".concat(threadDump.getContent());
	}

	public static void writeThreadDump(boolean clusterWide) {
		ThreadDumpResult threadDumpResult = takeThreadDump(clusterWide);

		File threadDumpFile = new File(
			_getThreadDumpDestDir(), threadDumpResult.getFileName());

		try {
			FileUtil.write(threadDumpFile, threadDumpResult.getInputStream());

			if (_log.isInfoEnabled()) {
				_log.info("Thread dump has been written to " + threadDumpFile);
			}
		}
		catch (IOException ioe) {
			_log.error(ioe);
		}
	}

	private static ThreadDumpResult _doClusterWideThreadDump() {
		ClusterRequest clusterRequest = ClusterRequest.createMulticastRequest(
			new MethodHandler(_TAKE_THREAD_DUMP_METHOD_KEY), false);

		List<Address> clusterNodeAddresses =
			ClusterExecutorUtil.getClusterNodeAddresses();

		ClusterWideThreadDumpResultImpl threadDumpResult =
			new ClusterWideThreadDumpResultImpl(clusterNodeAddresses.size());

		ClusterResponseCallback threadDumpClusterResponseCallback =
			new ThreadDumpClusterResponseCallback(
				clusterNodeAddresses, threadDumpResult);

		ClusterExecutorUtil.execute(
			clusterRequest, threadDumpClusterResponseCallback);

		if (_log.isInfoEnabled()) {
			_log.info("Cluster wide thread dump request has been submitted.");
		}

		threadDumpResult.await();

		return threadDumpResult;
	}

	private static ThreadDumpResult _doLocalThreadDump() {
		String threadDump = _getThreadDumpFromJstack();

		if (Validator.isNull(threadDump)) {
			threadDump = _getThreadDumpFromStackTrace();
		}

		return new ThreadDumpResultImpl(threadDump);
	}

	private static File _getThreadDumpDestDir() {
		String destDir = PropsUtil.get(PropsKeys.THREAD_DUMP_DEST_DIR);

		if (Validator.isBlank(destDir)) {
			destDir = SystemProperties.get(SystemProperties.TMP_DIR);
		}

		if (!FileUtil.exists(destDir)) {
			FileUtil.mkdirs(destDir);
		}

		return new File(destDir);
	}

	private static String _getThreadDumpFromJstack() {
		UnsyncByteArrayOutputStream outputStream =
			new UnsyncByteArrayOutputStream();

		try {
			String vendorURL = System.getProperty("java.vendor.url");

			if (!vendorURL.equals("http://java.oracle.com/") &&
				!vendorURL.equals("http://java.sun.com/")) {

				return StringPool.BLANK;
			}

			RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

			String name = runtimeMXBean.getName();

			if (Validator.isNull(name)) {
				return StringPool.BLANK;
			}

			int pos = name.indexOf(CharPool.AT);

			if (pos == -1) {
				return StringPool.BLANK;
			}

			String pidString = name.substring(0, pos);

			if (!Validator.isNumber(pidString)) {
				return StringPool.BLANK;
			}

			Runtime runtime = Runtime.getRuntime();

			int pid = GetterUtil.getInteger(pidString);

			String[] cmd = new String[] {"jstack", String.valueOf(pid)};

			Process process = runtime.exec(cmd);

			InputStream inputStream = process.getInputStream();

			StreamUtil.transfer(inputStream, outputStream);
		}
		catch (Exception e) {
		}

		return outputStream.toString();
	}

	private static String _getThreadDumpFromStackTrace() {
		String jvm =
			System.getProperty("java.vm.name") + " " +
				System.getProperty("java.vm.version");

		StringBundler sb = new StringBundler(
			"Full thread dump of " + jvm + " on " + String.valueOf(new Date()) +
				"\n\n");

		Map<Thread, StackTraceElement[]> stackTraces =
			Thread.getAllStackTraces();

		for (Map.Entry<Thread, StackTraceElement[]> entry :
				stackTraces.entrySet()) {

			Thread thread = entry.getKey();
			StackTraceElement[] elements = entry.getValue();

			sb.append(StringPool.QUOTE);
			sb.append(thread.getName());
			sb.append(StringPool.QUOTE);

			if (thread.getThreadGroup() != null) {
				sb.append(StringPool.SPACE);
				sb.append(StringPool.OPEN_PARENTHESIS);
				sb.append(thread.getThreadGroup().getName());
				sb.append(StringPool.CLOSE_PARENTHESIS);
			}

			sb.append(", priority=");
			sb.append(thread.getPriority());
			sb.append(", id=");
			sb.append(thread.getId());
			sb.append(", state=");
			sb.append(thread.getState());
			sb.append("\n");

			for (int i = 0; i < elements.length; i++) {
				sb.append("\t");
				sb.append(elements[i]);
				sb.append("\n");
			}

			sb.append("\n");
		}

		return sb.toString();
	}

	private static final MethodKey _TAKE_THREAD_DUMP_METHOD_KEY = new MethodKey(
		ThreadDumpUtil.class, "takeThreadDump");

	private static final int _THREAD_DUMP_CLUSTER_WIDE_TIMEOUT =
		GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.THREAD_DUMP_CLUSTER_WIDE_TIMEOUT));

	private static Log _log = LogFactoryUtil.getLog(ThreadDumpUtil.class);

	private static class ThreadDumpClusterResponseCallback
		extends BaseClusterResponseCallback {

		@Override
		public void callback(BlockingQueue<ClusterNodeResponse> blockingQueue) {
			do {
				_clusterNodeAddressCount--;

				ClusterNodeResponse clusterNodeResponse = null;

				try {
					clusterNodeResponse = blockingQueue.poll(
						_THREAD_DUMP_CLUSTER_WIDE_TIMEOUT, TimeUnit.SECONDS);
				}
				catch (InterruptedException ie) {
					_log.error(
						"Unable to get cluster node response in " +
							_THREAD_DUMP_CLUSTER_WIDE_TIMEOUT +
								TimeUnit.SECONDS);
				}

				if (clusterNodeResponse == null) {
					continue;
				}

				Address clusterNodeAddress = clusterNodeResponse.getAddress();

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Processing response of node " + clusterNodeAddress);
				}

				try {
					ThreadDumpResult threadDumpResult =
						(ThreadDumpResult)clusterNodeResponse.getResult();

					_silentClusterNodeAddresses.remove(clusterNodeAddress);

					if (threadDumpResult == null) {
						continue;
					}

					ThreadDump threadDump = threadDumpResult.getThreadDump();

					_threadDumpResult.addThreadDump(threadDump);
				}
				catch (Exception e) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							"Exception occured on node " + clusterNodeAddress,
							e);
					}

					_threadDumpResult.addError(
						clusterNodeAddress.getDescription(), e);
				}
			}
			while (_clusterNodeAddressCount > 0);

			// Silent nodes

			if (_log.isWarnEnabled() &&
				!_silentClusterNodeAddresses.isEmpty()) {

				_log.warn(
					"The following nodes gave no response: " +
						StringUtil.merge(_silentClusterNodeAddresses));
			}
		}

		@Override
		public void processInterruptedException(
			InterruptedException interruptedException) {

			if (_log.isWarnEnabled()) {
				_log.warn(
					"Cluster wide thread dump generation has been " +
						"interrupted; sealing bundle.");
			}
		}

		@Override
		public void processTimeoutException(TimeoutException timeoutException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Waiting for cluster nodes has timed out; sealing bundle.");
			}
		}

		private ThreadDumpClusterResponseCallback(
			List<Address> clusterNodeAddresses,
			ClusterWideThreadDumpResultImpl threadDumpResult) {

			_clusterNodeAddressCount = clusterNodeAddresses.size();

			_silentClusterNodeAddresses = SetUtil.fromList(
				clusterNodeAddresses);

			_threadDumpResult = threadDumpResult;
		}

		private int _clusterNodeAddressCount;
		private Set<Address> _silentClusterNodeAddresses;
		private ClusterWideThreadDumpResultImpl _threadDumpResult;

	}

}