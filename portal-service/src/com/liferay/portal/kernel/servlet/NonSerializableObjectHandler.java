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

package com.liferay.portal.kernel.servlet;

import com.liferay.portal.kernel.util.ServerDetector;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Igor Spasic
 * @see    com.liferay.portal.kernel.util.TransientValue
 */
public final class NonSerializableObjectHandler implements Serializable {

	public static Object getValue(Object value) {
		if (value instanceof NonSerializableObjectHandler) {
			NonSerializableObjectHandler nonSerializableObjectHandler =
				(NonSerializableObjectHandler)value;

			value = nonSerializableObjectHandler.getValue();
		}

		return value;
	}

	public static HttpServletRequest handleNonSerializableRequest(
		HttpServletRequest request) {

		if (!ServerDetector.isWebLogic() ||
			(NonSerializableObjectRequestWrapper.isWrapped(request) &&
			(request instanceof NonSerializableObjectRequestWrapper))) {

			return request;
		}

		return new NonSerializableObjectRequestWrapper(request);
	}

	public NonSerializableObjectHandler(Object value) {
		while (value instanceof NonSerializableObjectHandler) {
			NonSerializableObjectHandler nonSerializableObjectHandler =
				(NonSerializableObjectHandler)value;

			value = nonSerializableObjectHandler.getValue();
		}

		_value = value;
	}

	public Object getValue() {
		return _value;
	}

	private transient Object _value;

}