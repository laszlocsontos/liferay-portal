/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.dao.orm.hibernate;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Laszlo Csontos
 */
@ExecutionTestListeners(listeners = { PersistenceExecutionTestListener.class} )
@RunWith(LiferayPersistenceIntegrationJUnitTestRunner.class)
public class DB2DialectTest {

	@Before
	public void setUp() throws Exception {
		_persistence = new BasePersistenceImpl();

		_persistence.setDataSource(_liferayDataSource);
		_persistence.setSessionFactory(_liferaySessionFactory);
	}

	@Test
	public void testPagingWithOffset() {
		checkDBType();

		doPaging(_MOCK_SQL, 10, 20);
	}

	@Test
	public void testPagingWithoutOffset() {
		checkDBType();

		doPaging(_MOCK_SQL, 0, 20);
	}

	protected void checkDBType() {
		Assert.assertEquals(DB.TYPE_DB2, _db.getType());
	}

	protected void doPaging(String sql, int offset, int limit) {

		Session session = null;

		List<?> result = null;

		try {
			session = _persistence.openSession();

			SQLQuery q = session.createSQLQuery(sql);

			result = QueryUtil.list(
				q, _liferaySessionFactory.getDialect(), offset, offset + limit);
		}
		finally {
			_persistence.closeSession(session);
		}

		Assert.assertNotNull(result);
		Assert.assertEquals(limit, result.size());
	}

	private static final String _MOCK_SQL =
		"SELECT tabname FROM syscat.tables " +
		"WHERE tabschema = 'SYSIBM' ORDER BY tabname";

	private DB _db = DBFactoryUtil.getDB();

	private DataSource _liferayDataSource =
		(DataSource) PortalBeanLocatorUtil.locate("liferayDataSource");

	private SessionFactory _liferaySessionFactory =
		(SessionFactory) PortalBeanLocatorUtil.locate("liferaySessionFactory");

	private BasePersistenceImpl<?> _persistence;

}