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

import com.liferay.portal.NoShardSelectedException;
import com.liferay.portal.kernel.annotation.AnnotationLocator;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.spring.aop.ShardSelection;
import com.liferay.portal.kernel.spring.aop.ShardSelectionMethod;
import com.liferay.portal.kernel.spring.aop.ShardSelectorParam;
import com.liferay.portal.kernel.util.AutoResetThreadLocal;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Shard;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.ShardLocalServiceUtil;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;
import com.liferay.portal.spring.aop.ServiceBeanAopCacheManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.util.HashSet;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Vilmos Papp
 */
public class ShardAnnotationAdvice
	extends AnnotationChainableMethodAdvice<ShardSelection> {

	@Override
	public Object before(MethodInvocation methodInvocation) throws Throwable {
		ShardSelection annotation = findAnnotation(methodInvocation);

		if (annotation == _nullShardSelection) {
			return null;
		}

		ShardSelectionMethod selectionMethod = annotation.selectionMethod();

		if (annotation.isClassAnnotation()) {
			if (selectionMethod.equals(ShardSelectionMethod.THREADLOCAL)) {
				_setShardFromThreadLocal(methodInvocation);
			}
			else if (selectionMethod.equals(ShardSelectionMethod.PARAMETER)) {
				_setShardByParameter(methodInvocation);
			}
		}
		else {
			if (selectionMethod.equals(ShardSelectionMethod.PARAMETER)) {
				_setShardByParameter(methodInvocation);
			}
		}

		return null;
	}

	@Override
	public void duringFinally(MethodInvocation methodInvocation) {
		_popMethodInvocation(methodInvocation);
	}

	@Override
	public ShardSelection getNullAnnotation() {
		return _nullShardSelection;
	}

	public void setShardAdvice(ShardAdvice shardAdvice) {
		_shardAdvice = shardAdvice;
	}

	@Override
	protected void setServiceBeanAopCacheManager(
		ServiceBeanAopCacheManager serviceBeanAopCacheManager) {

		if (this.serviceBeanAopCacheManager != null) {
			return;
		}

		this.serviceBeanAopCacheManager = serviceBeanAopCacheManager;

		serviceBeanAopCacheManager.registerAnnotationChainableMethodAdvice(
			ShardSelection.class, null);
	}

	private Object _getCompanyIdParam(
			Object[] arguments, Annotation[][] parameterAnnotations)
		throws NoShardSelectedException {

		Object companyIdParamObject = null;
		int i = 0;

		while ((i < parameterAnnotations.length) &&
			   (companyIdParamObject == null)) {

			for (Annotation parameterAnnotation : parameterAnnotations[i]) {
				if (parameterAnnotation instanceof ShardSelectorParam) {
					companyIdParamObject = arguments[i];

					if (companyIdParamObject == null) {
						throw new NoShardSelectedException();
					}

					break;
				}
			}

			i++;
		}

		if (companyIdParamObject == null) {
			companyIdParamObject = arguments[0];
		}

		return companyIdParamObject;
	}

	private Set<MethodInvocation> _getShardStateStack() {
		Set<MethodInvocation> shardStackState = _shardStackState.get();

		return shardStackState;
	}

	private void _popMethodInvocation(MethodInvocation methodInvocation) {
		if (_getShardStateStack().contains(methodInvocation)) {
			_getShardStateStack().remove(methodInvocation);

			_shardAdvice.popCompanyService();
		}
	}

	private void _pushMethodInvocation(MethodInvocation methodInvocation) {
		_getShardStateStack().add(methodInvocation);
	}

	private void _setShard(Long companyId, MethodInvocation methodInvocation)
		throws Throwable {

		Shard shard = ShardLocalServiceUtil.getShard(
			Company.class.getName(), companyId.longValue());

		String shardName = shard.getName();

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Setting service to shard " + shardName + " for " +
					methodInvocation.toString());
		}

		_shardAdvice.pushCompanyService(shardName);

		_pushMethodInvocation(methodInvocation);
	}

	private void _setShardByParameter(MethodInvocation methodInvocation)
		throws Throwable {

		Object[] arguments = methodInvocation.getArguments();
		Method method = methodInvocation.getMethod();
		Object thisObject = methodInvocation.getThis();
		Class<?> targetClass = thisObject.getClass();

		Annotation[][] parameterAnnotations = AnnotationLocator.locate(
			method, targetClass, arguments);

		Object companyIdParam = _getCompanyIdParam(
			arguments, parameterAnnotations);

		Long companyId = null;

		if (companyIdParam == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"No parameter annotation found for method: " +
						methodInvocation.getMethod().getName() +
							" try to get companyId from first" +
								"method parameter for: " +
									methodInvocation.toString());
			}

			if (arguments[0] instanceof Long) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Set first method parameter: " + arguments[0] +
							" as companyId for shard selection for method: " +
								methodInvocation.toString());
				}

				companyId = (Long)arguments[0];
			}
		}
		else {
			if (companyIdParam instanceof Long) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Set companyId for shard selection from long" +
								" value: " + (Long)companyIdParam +
								" for method: " + methodInvocation.toString());
				}

				companyId = (Long)companyIdParam;
			}
			else {
				Class clazz = companyIdParam.getClass();

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Try to retrive companyId from class: " +
							clazz.getName() +
								" by invoke getCompanyId() on it.");
				}

				try {
					Method getCompanyIdMethod = clazz.getMethod("getCompanyId");

					if (_log.isDebugEnabled()) {
						_log.debug(
							"Class: " + clazz.getName() +
								" implements getCompanyId() method.");
					}

					try {
						if (_log.isDebugEnabled()) {
							_log.debug(
								"Invoke getCompanyId() on class: " +
									clazz.getName());
						}

						companyId = (Long)getCompanyIdMethod.invoke(
							companyIdParam);
					}
					catch (IllegalArgumentException iae) {
						throw new NoShardSelectedException();
					}
				}
				catch (NoSuchMethodException nsme) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							"Class: " + clazz.getName() +
								" doesn't implement getCompanyId() method.");
					}
				}
			}
		}

		if (companyId == null) {
			throw new NoShardSelectedException();
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				"CompanyId retrived for shard selection: " +
					companyId.longValue() + " for method invocation: " +
						methodInvocation.toString());
		}

		_setShard(companyId, methodInvocation);
	}

	private void _setShardFromThreadLocal(MethodInvocation methodInvocation)
		throws Throwable {

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Invoke method on shard selected by companyId retrived" +
					"from CompanyThreadLocal: " + methodInvocation.toString());
		}

		_setShard(CompanyThreadLocal.getCompanyId(), methodInvocation);
	}

	private static Log _log = LogFactoryUtil.getLog(
		ShardAnnotationAdvice.class);

	private static ShardSelection _nullShardSelection = new ShardSelection() {
		@Override
		public Class<? extends Annotation> annotationType() {
			return ShardSelection.class;
		}

		@Override
		public boolean isClassAnnotation() {
			return false;
		}

		@Override
		public ShardSelectionMethod selectionMethod() {
			return ShardSelectionMethod.NONE;
		}
	};

	private static ThreadLocal<Set<MethodInvocation>> _shardStackState =
		new AutoResetThreadLocal<Set<MethodInvocation>>(
			ShardAnnotationAdvice.class + "._shardStackState",
			new HashSet<MethodInvocation>());

	private ShardAdvice _shardAdvice;

}