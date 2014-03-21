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

package com.liferay.portlet.documentlibrary.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.util.DLAppTestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Manuel de la Peña
 * @author László Csontos
 */
@ExecutionTestListeners(
	listeners = {
		EnvironmentExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class DLAppLocalServiceTest {

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddFileEntryConcurrent() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(
			_THREAD_POOL_SIZE);

		try {
			String sourceFileName = ServiceTestUtil.randomString();
			String title = ServiceTestUtil.randomString();

			CountDownLatch countDownLatch = new CountDownLatch(
				_NUM_FILE_ENTRIES);

			Future<Object[]>[] futures = new Future[_NUM_FILE_ENTRIES];

			for (int i = 0; i < _NUM_FILE_ENTRIES; i++) {
				futures[i] = executorService.submit(
					new AddFileEntryCallable(
						sourceFileName, title, countDownLatch));
			}

			countDownLatch.await(60, TimeUnit.SECONDS);

			Assert.assertEquals(0, countDownLatch.getCount());

			int addedFileEntryCount = 0;
			int portalExceptionCount = 0;

			List<Exception> otherExceptions = new ArrayList<Exception>();

			for (int i = 0; i < _NUM_FILE_ENTRIES; i++) {
				Object[] result = futures[i].get();

				if (result[0] != null) {
					addedFileEntryCount++;

					continue;
				}

				Exception e = (Exception)result[1];

				if (e instanceof PortalException) {
					portalExceptionCount++;
				}
				else {
					otherExceptions.add(e);
				}
			}

			int otherExceptionCount = otherExceptions.size();

			Assert.assertEquals(
				_NUM_FILE_ENTRIES,
				addedFileEntryCount + portalExceptionCount +
					otherExceptionCount);

			// Only one thread should succeed

			Assert.assertEquals(1, addedFileEntryCount);

			// All the threads should throw DuplicateFileException and they
			// shouldn't propagate technical ORMExceptions to the caller

			Assert.assertEquals(
				"Unexpected exceptions: " + otherExceptions.toString(), 0,
				otherExceptionCount);

			Assert.assertEquals(_NUM_FILE_ENTRIES - 1, portalExceptionCount);
		}
		finally {
			executorService.shutdownNow();
		}
	}

	@Test
	public void testAddFileEntrySerial() throws Exception {
		String sourceFileName = ServiceTestUtil.randomString();
		String title = ServiceTestUtil.randomString();

		Callable<Object[]> addFileEntryCallable = new AddFileEntryCallable(
			sourceFileName, title, null);

		Object[] result = addFileEntryCallable.call();

		Assert.assertNotNull(result[0]);
		Assert.assertNull(result[1]);
	}

	@Test
	public void testAddFolder() throws Exception {
		Folder folder = addFolder(true);

		Assert.assertNotNull(folder);
	}

	@Test
	public void testAddRootFolder() throws Exception {
		Folder folder = addFolder(false);

		Assert.assertNotNull(folder);
	}

	protected Folder addFolder(boolean rootFolder) throws Exception {
		return addFolder(rootFolder, ServiceTestUtil.randomString());
	}

	protected Folder addFolder(boolean rootFolder, String name)
		throws Exception {

		long parentFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

		if (!rootFolder) {
			Folder parentFolder = addFolder(
				parentFolderId, "Test Folder", true);

			parentFolderId = parentFolder.getFolderId();
		}

		return addFolder(parentFolderId, name);
	}

	protected Folder addFolder(long parentFolderId, String name)
		throws Exception {

		return addFolder(parentFolderId, name, false);
	}

	protected Folder addFolder(
			long parentFolderId, String name, boolean deleteExisting)
		throws Exception {

		return DLAppTestUtil.addFolder(
			_group.getGroupId(), parentFolderId, name, deleteExisting);
	}

	private static final int _NUM_FILE_ENTRIES = 50;

	private static final int _THREAD_POOL_SIZE = 5;

	private Group _group;

	private class AddFileEntryCallable implements Callable<Object[]> {

		public AddFileEntryCallable(
			String sourceFileName, String title,
			CountDownLatch countDownLatch) {

			_countDownLatch = countDownLatch;
			_sourceFileName = sourceFileName;
			_title = title;
		}

		@Override
		public Object[] call() throws Exception {
			FileEntry addedFileEntry = null;

			Exception addFileEntryException = null;

			try {
				Group group = GroupLocalServiceUtil.getGroup(
					TestPropsValues.getCompanyId(), GroupConstants.GUEST);

				addedFileEntry = DLAppTestUtil.addFileEntry(
					group.getGroupId(),
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, _sourceFileName,
					_title);
			}
			catch (Exception e) {
				addFileEntryException = e;
			}

			if (_countDownLatch != null) {
				_countDownLatch.countDown();
			}

			return new Object[] {addedFileEntry, addFileEntryException};
		}

		private CountDownLatch _countDownLatch;
		private String _sourceFileName;
		private String _title;
	}

}