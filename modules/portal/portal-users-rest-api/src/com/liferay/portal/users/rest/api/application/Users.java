/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.users.rest.api.application;

import com.liferay.portal.users.rest.api.resource.UsersResource;
import com.liferay.portal.users.rest.provider.CompanyContextProvider;
import com.liferay.portal.users.rest.provider.TokenRequestFilter;
import com.liferay.portal.users.rest.provider.OptionalBodyWriter;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = "jaxrs.application=true",
	service = Application.class
)
@ApplicationPath("/users")
public class Users extends Application {

	private CompanyContextProvider _companyContextProvider;

	@Override
	public Set<Class<?>> getClasses() {
		return new HashSet<>(Arrays.asList(
			CompanyContextProvider.class, OptionalBodyWriter.class));
	}

	@Override
	public Set<Object> getSingletons() {
		return new HashSet<>(
			Arrays.asList(
				_companyRequestFilter, _companyContextProvider,
				_usersResource));
	}

	@Reference
	public void setCompanyContextProvider(
		CompanyContextProvider companyContextProvider) {

		_companyContextProvider = companyContextProvider;
	}

	@Reference
	public void setCompanyRequestFilter(
		TokenRequestFilter companyRequestFilter) {

		_companyRequestFilter = companyRequestFilter;
	}

	@Reference
	public void setUsersResource(UsersResource usersResource) {
		_usersResource = usersResource;
	}

	private TokenRequestFilter _companyRequestFilter;

	private UsersResource _usersResource;
}
