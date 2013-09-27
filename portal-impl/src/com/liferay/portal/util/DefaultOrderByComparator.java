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

import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 */
public class DefaultOrderByComparator extends OrderByComparator {

	@Override
	public String getOrderBy() {
		return getOrderBy(_tableName);
	}

	public String getOrderBy(String tableName) {
		StringBundler sb = new StringBundler();

		for (int i = 0; i < _columns.length; i += 2) {
			if (i != 0) {
				sb.append(StringPool.COMMA);
			}

			if (Validator.isNotNull(tableName)) {
				sb.append(tableName);
				sb.append(StringPool.PERIOD);
			}

			String columnName = String.valueOf(_columns[i]);
			boolean columnAscending = Boolean.valueOf(
				String.valueOf(_columns[i + 1]));

			sb.append(columnName);

			if (columnAscending) {
				sb.append(_ORDER_BY_ASC);
			}
			else {
				sb.append(_ORDER_BY_DESC);
			}
		}

		return sb.toString();
	}

	@Override
	public String[] getOrderByConditionFields() {
		return getOrderByFields();
	}

	@Override
	public Object[] getOrderByConditionValues(Object obj) {
		String[] fields = getOrderByConditionFields();

		Object[] values = new Object[fields.length];

		for (int i = 0; i < fields.length; i++) {
			values[i] = BeanPropertiesUtil.getObject(obj, fields[i]);
		}

		return values;
	}

	@Override
	public String[] getOrderByFields() {
		String[] fields = new String[_columns.length / 2];

		for (int i = 0; i < _columns.length; i += 2) {
			String column = (String)_columns[i];

			int x = column.indexOf(CharPool.PERIOD);
			int y = column.indexOf(CharPool.SPACE, x);

			if (y == -1) {
				y = column.length();
			}

			fields[i] = column.substring(x + 1, y);
		}

		return fields;
	}

	@Override
	public boolean isAscending(String field) {
		String orderBy = getOrderBy();

		if (orderBy == null) {
			return false;
		}

		int x = orderBy.indexOf(StringPool.PERIOD + field + StringPool.SPACE);

		if (x == -1) {
			return false;
		}

		int y = orderBy.indexOf(_ORDER_BY_ASC, x);

		if (y == -1) {
			return false;
		}

		int z = orderBy.indexOf(_ORDER_BY_DESC, x);

		if ((z >= 0) && (z < y)) {
			return false;
		}
		else {
			return true;
		}
	}

	protected DefaultOrderByComparator(String tableName, Object... columns) {
		_tableName = tableName;
		_columns = columns;
	}

	protected DefaultOrderByComparator(String tableName, String[] columns) {
		this(tableName, columns, false);
	}

	protected DefaultOrderByComparator(
		String tableName, String[] columns, boolean ascending) {

		super(ascending);

		_tableName = tableName;

		_columns = new Object[columns.length * 2];

		for (int i = 0; i < columns.length; i++) {
			_columns[i * 2] = columns[i];
			_columns[i * 2 + 1] = ascending;
		}
	}

	@Override
	protected int doCompare(Object object1, Object object2) {
		for (int i = 0; i < _columns.length; i += 2) {
			String columnName = String.valueOf(_columns[i]);
			boolean columnAscending = Boolean.valueOf(
				String.valueOf(_columns[i + 1]));

			Object columnInstance = null;

			Class<?> columnClass = BeanPropertiesUtil.getObjectTypeSilent(
				object1, columnName);

			if (columnClass.isPrimitive()) {
				columnInstance = _primitiveObjects.get(columnClass);
			}
			else {
				try {
					columnInstance = columnClass.newInstance();
				}
				catch (Exception e) {
				}
			}

			Object columnValue1 = BeanPropertiesUtil.getObjectSilent(
				object1, columnName);
			Object columnValue2 = BeanPropertiesUtil.getObjectSilent(
				object2, columnName);

			if (columnInstance instanceof Date) {
				Date columnValueDate1 = (Date)columnValue1;
				Date columnValueDate2 = (Date)columnValue2;

				int value = DateUtil.compareTo(
					columnValueDate1, columnValueDate2);

				if (value == 0) {
					continue;
				}

				if (columnAscending) {
					return value;
				}
				else {
					return -value;
				}
			}
			else if (columnInstance instanceof Comparable<?>) {
				Comparable<Object> columnValueComparable1 =
					(Comparable<Object>)columnValue1;
				Comparable<Object> columnValueComparable2 =
					(Comparable<Object>)columnValue2;

				int value = columnValueComparable1.compareTo(
					columnValueComparable2);

				if (value == 0) {
					continue;
				}

				if (columnAscending) {
					return value;
				}
				else {
					return -value;
				}
			}
		}

		return 0;
	}

	private static final String _ORDER_BY_ASC = " ASC";

	private static final String _ORDER_BY_DESC = " DESC";

	private static Map<Class<?>, Object> _primitiveObjects =
		new HashMap<Class<?>, Object>();

	static {
		_primitiveObjects.put(boolean.class, new Boolean(true));
		_primitiveObjects.put(byte.class, new Byte("0"));
		_primitiveObjects.put(char.class, new Character('0'));
		_primitiveObjects.put(double.class, new Double(0));
		_primitiveObjects.put(float.class, new Float(0));
		_primitiveObjects.put(int.class, new Integer(0));
		_primitiveObjects.put(long.class, new Long(0));
		_primitiveObjects.put(short.class, new Short("0"));
	}

	private Object[] _columns;
	private String _tableName;

}