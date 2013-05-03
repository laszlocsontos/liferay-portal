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

package com.liferay.portal.dao.shard.advice;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.spring.aop.SelectionLogic;
import com.liferay.portal.kernel.spring.aop.ShardSelection;
import com.liferay.portal.kernel.spring.aop.ShardSelectorParam;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Shard;
import com.liferay.portal.service.ShardLocalServiceUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Vilmos Papp
 */
public class ShardGeneralAdvice implements MethodInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object thisObject = methodInvocation.getThis();

		Class<?> clazz = thisObject.getClass();

		Method method = methodInvocation.getMethod();

		Object returnValue = null;

		if (clazz.isAnnotationPresent(ShardSelection.class)) {
			ShardSelection shardSelection = (ShardSelection)clazz.getAnnotation(
				ShardSelection.class);

			if (shardSelection.logic() == SelectionLogic.THREADLOCAL) {
				String shardName = _shardAdvice.setShardNameByCompany();

				_shardAdvice.pushCompanyService(shardName);

				try {
					returnValue = methodInvocation.proceed();
				}
				finally {
					_shardAdvice.popCompanyService();
				}
			}
			else if(shardSelection.logic() == SelectionLogic.PARAMETER) {
				returnValue = _invokeOnShardByParameter(methodInvocation, true);
			}
		}
		else {
			returnValue = _invokeOnShardByParameter(methodInvocation, false);
		}

		return returnValue;
	}

	public void setShardAdvice(ShardAdvice shardAdvice) {
		_shardAdvice = shardAdvice;
	}

	private Object _invokeMethod(
			long companyId, MethodInvocation methodInvocation)
		throws Throwable {

		Shard shard = ShardLocalServiceUtil.getShard(
			Company.class.getName(), companyId);

		String shardName = shard.getName();

		if (_log.isInfoEnabled()) {
			_log.info(
				"Setting service to shard " + shardName + " for " +
					methodInvocation.toString());
		}

		Object returnValue = null;

		_shardAdvice.pushCompanyService(shardName);

		try {
			returnValue = methodInvocation.proceed();
		}
		finally {
			_shardAdvice.popCompanyService();
		}

		return returnValue;
	}

	private Object _invokeOnShardByParameter(
			MethodInvocation methodInvocation, boolean defaultParamSelection)
		throws Throwable {

		Object[] arguments = methodInvocation.getArguments();

		if ((arguments == null) || (arguments.length == 0)) {
			return methodInvocation.proceed();
		}

		Method method = methodInvocation.getMethod();

		Annotation[][] parameterAnnotations = method.getParameterAnnotations();

		Object companyIdParam = null;

		for (Annotation[] annotations : parameterAnnotations) {
			for (Annotation annotation : annotations) {
				if (annotation instanceof ShardSelection) {
					ShardSelection shardSelection = (ShardSelection)annotation;
					if (shardSelection.logic() == SelectionLogic.SKIP) {
						return methodInvocation.proceed();
					}
				}
			}
		}

		int i = 0;

		while ((i < parameterAnnotations.length) && (companyIdParam == null)) {
			for (Annotation annotation : parameterAnnotations[i]) {
				if (annotation instanceof ShardSelectorParam) {
					companyIdParam = arguments[i];
					break;
				}
			}

			i++;
		}

		long companyId = -1;

		if (companyIdParam == null) {
			if (defaultParamSelection) {
				companyId = (Long)arguments[0];
			}
			else {
				return methodInvocation.proceed();
			}
		}

		if ((companyId == -1) && (companyIdParam != null)) {
			if (companyIdParam instanceof Long) {
				companyId = (Long)companyIdParam;
			}
			else {
				Class clazz = companyIdParam.getClass();

				Method getCompanyIdMethod = clazz.getMethod("getCompanyId");

				companyId = (Long)getCompanyIdMethod.invoke(clazz);
			}
		}

		return _invokeMethod(companyId, methodInvocation);
	}

	private static Log _log = LogFactoryUtil.getLog(ShardGeneralAdvice.class);

	private ShardAdvice _shardAdvice;

}
