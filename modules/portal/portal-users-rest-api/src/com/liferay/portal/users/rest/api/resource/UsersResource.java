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

package com.liferay.portal.users.rest.api.resource;

import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.users.rest.api.model.Optional;
import com.liferay.portal.users.rest.api.model.RestUser;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	service = UsersResource.class
)
public class UsersResource {

	@GET
	public List<RestUser> listUsers(@Context Company company) {
		List<User> companyUsers = _userLocalService.getCompanyUsers(
			company.getCompanyId(), -1, -1);

		List<RestUser> restUsers = new ArrayList<>(companyUsers.size());

		for (User companyUser : companyUsers) {
			restUsers.add(new RestUser(companyUser));
		}

		return restUsers;
	}

	@GET
	@Path("/{id}")
	public Optional<RestUser> getUser(@PathParam("id") long id) {
		return RestUser.fromUser(_userLocalService.fetchUser(id));
	}

	@Reference
	public void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private UserLocalService _userLocalService;

}
