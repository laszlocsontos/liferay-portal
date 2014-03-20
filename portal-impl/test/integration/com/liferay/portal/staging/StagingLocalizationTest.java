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

package com.liferay.portal.staging;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.SynchronousDestinationExecutionTestListener;
import com.liferay.portal.util.CompanyTestUtil;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceUtil;
import com.liferay.portlet.journal.util.JournalTestUtil;

import java.util.Locale;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Daniel Kocsis
 */
@ExecutionTestListeners(
	listeners = {
		MainServletExecutionTestListener.class,
		SynchronousDestinationExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Sync
public class StagingLocalizationTest {

	@Before
	public void setUp() throws Exception {
		_companyLocales = LanguageUtil.getAvailableLocales(
			TestPropsValues.getCompanyId());
		_companyDefaultLocale = LocaleThreadLocal.getDefaultLocale();

		CompanyTestUtil.resetCompanyLocales(
			TestPropsValues.getCompanyId(), _locales, Locale.US);

		_group = GroupTestUtil.addGroup();
	}

	@After
	public void tearDown() throws Exception {
		GroupLocalServiceUtil.deleteGroup(_group);

		CompanyTestUtil.resetCompanyLocales(
			TestPropsValues.getCompanyId(), _companyLocales,
			_companyDefaultLocale);
	}

	@Test
	public void testChangeDefaultLocale() throws Exception {
		enableLocalStagingChangingLocales(
			"es_ES", "de_DE,en_US,es_ES", "en_US");
	}

	@Test
	public void testChangeDefaultLocaleAndDefaultContentLocale()
		throws Exception {

		enableLocalStagingChangingLocales(
			"es_ES", "de_DE,en_US,es_ES", "de_DE");
	}

	@Test
	public void testRemoveDefaultLocale() throws Exception {
		enableLocalStagingChangingLocales("es_ES", "de_DE,es_ES", "en_US");
	}

	@Test
	public void testRemoveDefaultLocaleAndChangeContentDefaultLocale()
		throws Exception {

		enableLocalStagingChangingLocales("es_ES", "de_DE,es_ES", "de_DE");
	}

	@Test
	public void testSameLocales() throws Exception {
		enableLocalStagingChangingLocales(
			"en_US", "de_DE,en_US,es_ES", "en_US");
	}

	protected void enableLocalStagingChangingLocales(
			String defaultLanguageId, String languageIds,
			String contentDefaultLanguageId)
		throws Exception {

		// Create Content

		JournalArticle article = JournalTestUtil.addArticle(
			_group.getGroupId(), "Title", "content",
			LocaleUtil.fromLanguageId(contentDefaultLanguageId));

		CompanyTestUtil.resetCompanyLocales(
			TestPropsValues.getCompanyId(), languageIds, defaultLanguageId);

		// Enable Staging

		GroupTestUtil.enableLocalStaging(_group);

		Group stagingGroup = _group.getStagingGroup();

		// Update Content in Staging

		JournalArticleResource articleResource =
			JournalArticleResourceLocalServiceUtil.
				fetchJournalArticleResourceByUuidAndGroupId(
					article.getArticleResourceUuid(),
					stagingGroup.getGroupId());

		Assert.assertNotNull(
			"Article resource must not be null", articleResource);

		JournalArticle stagingArticle =
			JournalArticleLocalServiceUtil.getLatestArticle(
				articleResource.getResourcePrimKey(),
				WorkflowConstants.STATUS_ANY, false);

		if (languageIds.contains(contentDefaultLanguageId)) {
			Assert.assertEquals(
				article.getDefaultLanguageId(),
				stagingArticle.getDefaultLanguageId());
		}
		else {
			Assert.assertEquals(
				defaultLanguageId, stagingArticle.getDefaultLanguageId());
		}

		for (Locale locale : _locales) {
			if (languageIds.contains(LocaleUtil.toLanguageId(locale)) ||
				languageIds.contains(contentDefaultLanguageId)) {

				Assert.assertEquals(
					article.getTitle(locale), stagingArticle.getTitle(locale));
			}
			else {
				Assert.assertEquals(
					article.getTitle(defaultLanguageId),
					stagingArticle.getTitle(locale));
			}
		}
	}

	private Locale _companyDefaultLocale;
	private Locale[] _companyLocales;
	private Group _group;
	private Locale[] _locales = {
		LocaleUtil.US, LocaleUtil.GERMANY, LocaleUtil.SPAIN
	};

}