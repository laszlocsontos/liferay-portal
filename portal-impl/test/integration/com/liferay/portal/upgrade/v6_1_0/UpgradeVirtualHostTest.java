package com.liferay.portal.upgrade.v6_1_0;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.util.InitUtil;
import com.liferay.util.PwdGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author László Csontos
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataAccess.class})
public class UpgradeVirtualHostTest extends UpgradeVirtualHost {

	@BeforeClass
	public static void init() throws Exception {
		checkOracle();
		checkPrivileges();

		InitUtil.initWithSpring();

		_datasource = InfrastructureUtil.getDataSource();
	}

	@Before
	public void setUp() throws Exception {
		createSchema();
	}

	@After
	public void tearDown() throws Exception {
		dropSchema(_schemaName);
	}

	@Test
	public void testUpdateCompany() throws Exception {
		openConnectionWithCurrentSchema();
	}

	@Test
	public void testUpdateLayoutSet() throws Exception {
		openConnectionWithCurrentSchema();
	}

	protected static void checkOracle() {
		DB db = DBFactoryUtil.getDB();

		String dbType = db.getType();

		Assume.assumeTrue(dbType.equals(DB.TYPE_ORACLE));
	}

	protected static void checkPrivileges() throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = openConnection();

			StringBundler sb = new StringBundler();

			sb.append("SELECT COUNT(*) AS CNT ");
			sb.append("FROM dba_role_privs r, dba_sys_privs s ");
			sb.append("WHERE r.granted_role = s.grantee AND ");
			sb.append("r.grantee = USER AND ");
			sb.append("s.privilege IN (");
			sb.append("\'ALTER SESSION\', \'CREATE ANY TABLE\',");
			sb.append("\'CREATE USER\')");

			String sql = sb.toString();

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			if (rs.next()) {
				Assume.assumeThat(rs.getInt(1), is(3));
			}
		}
		catch (SQLException sqle) {
			Assume.assumeNoException(sqle);
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	protected static Connection openConnection() throws Exception {
		return _datasource.getConnection();
	}

	protected void createSchema() throws Exception {
		Connection connection = openConnection();
		Statement statement = null;

		_schemaName =
			"UPGRADEVIRTUALHOSTTEST_" +
				PwdGenerator.getPassword(PwdGenerator.KEY2, 7);

		try {
			statement = connection.createStatement();

			statement.executeUpdate(
				"CREATE USER " + _schemaName + " IDENTIFIED BY pwd");
		}
		finally {
			DataAccess.cleanUp(connection, statement);
		}
	}

	protected void dropSchema(String schemaName) throws Exception {
		runSQL("DROP USER " + schemaName + " CASCADE");
	}

	protected Connection openConnectionWithCurrentSchema()
			throws Exception {

		Connection connection = openConnection();
		Statement statement = null;

		try {
			statement = connection.createStatement();

			statement.executeUpdate(
				"ALTER SESSION SET CURRENT_SCHEMA = " + _schemaName);
		}
		finally {
			DataAccess.cleanUp(statement);
		}

		mockStatic(DataAccess.class);

		when(
			DataAccess.getUpgradeOptimizedConnection()
		).thenReturn(
			connection
		);

		return connection;
	}

	private static DataSource _datasource;

	private String _schemaName;
}