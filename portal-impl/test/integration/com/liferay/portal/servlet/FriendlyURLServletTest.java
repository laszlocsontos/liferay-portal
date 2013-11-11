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

package com.liferay.portal.servlet;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import com.liferay.portal.util.Portal;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.testng.Assert;

/**
 * @author László Csontos
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class FriendlyURLServletTest {

	@Before
	public void setUp() throws Exception {
		_friendlyURLServlet = new FriendlyURLServlet();
		_mockHttpServletRequest = new MockHttpServletRequest();
		_mockHttpServletResponse = new MockHttpServletResponse();
	}


	@Test
	public void testGetRedirectWithGuest() throws Exception {
		doTestGetRedirect(
			"/web/guest/home", Portal.PATH_MAIN, null, new Object[] {
				Portal.PATH_MAIN, false
			});
	}

	@Test
	public void testGetRedirectWithInvalidPath() throws Exception {
		doTestGetRedirect(
			null, Portal.PATH_MAIN, null, new Object[] {
				Portal.PATH_MAIN, false
			});

		doTestGetRedirect(
			"test", Portal.PATH_MAIN, null, new Object[] {
				Portal.PATH_MAIN, false
			});
	}

	protected void doTestGetRedirect(
			String path, String mainPath, Map<String, String[]> params,
			Object[] expectedRedirectArray)
		throws Exception {

		Object[] actualRedirectArray = _friendlyURLServlet.getRedirect(
			_mockHttpServletRequest, path, mainPath, params);

		Assert.assertEquals(actualRedirectArray, expectedRedirectArray);
	}

	private FriendlyURLServlet _friendlyURLServlet;
	private MockHttpServletRequest _mockHttpServletRequest;
	private MockHttpServletResponse _mockHttpServletResponse;

}