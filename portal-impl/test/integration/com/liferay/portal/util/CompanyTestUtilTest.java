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

package com.liferay.portal.util;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.SynchronousDestinationExecutionTestListener;
import com.liferay.portal.test.TransactionalExecutionTestListener;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

/**
 * @author Daniel Kocsis
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		SynchronousDestinationExecutionTestListener.class,
		TransactionalExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Sync
public class CompanyTestUtilTest {

	@Test
	@Transactional
	public void testResetCompanyLocales() throws Exception {
		CompanyTestUtil.resetCompanyLocales(
			TestPropsValues.getCompanyId(), new Locale[] {LocaleUtil.US},
			LocaleUtil.US);

		CompanyTestUtil.resetCompanyLocales(
			TestPropsValues.getCompanyId(), new Locale[] {LocaleUtil.HUNGARY},
			LocaleUtil.HUNGARY);

		CompanyTestUtil.resetCompanyLocales(
			TestPropsValues.getCompanyId(), new Locale[] {LocaleUtil.SPAIN},
			LocaleUtil.SPAIN);

		Company company = CompanyLocalServiceUtil.getCompany(
			TestPropsValues.getCompanyId());

		Assert.assertEquals(LocaleUtil.SPAIN, company.getLocale());
	}

}
