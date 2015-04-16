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

package com.liferay.portal.users.rest.api.model;

import com.liferay.portal.model.User;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Carlos Sierra Andrés
 */
@XmlRootElement
public class RestUser {

	private User _user;

	public RestUser(User user) {

		_user = user;
	}

	public RestUser() {
	}

	public long getUserId() {
		return _user.getUserId();
	}

	public void setUserId(long userId) {
		_user.setUserId(userId);
	}

	public String getScreenName() {
		return _user.getScreenName();
	}

	public void setScreenName(String screenName) {
		_user.setScreenName(screenName);
	}

	public String getEmailAddress() {
		return _user.getEmailAddress();
	}

	public void setEmailAddress(String emailAddress) {
		_user.setEmailAddress(emailAddress);
	}

	public String getLanguageId() {
		return _user.getLanguageId();
	}

	public void setLanguageId(String languageId) {
		_user.setLanguageId(languageId);
	}

	public String getGreeting() {
		return _user.getGreeting();
	}

	public void setGreeting(String greeting) {
		_user.setGreeting(greeting);
	}

	public String getFirstName() {
		return _user.getFirstName();
	}

	public void setFirstName(String firstName) {
		_user.setFirstName(firstName);
	}

	public String getMiddleName() {
		return _user.getMiddleName();
	}

	public void setMiddleName(String middleName) {
		_user.setMiddleName(middleName);
	}

	public String getLastName() {
		return _user.getLastName();
	}

	public void setLastName(String lastName) {
		_user.setLastName(lastName);
	}

	public String getJobTitle() {
		return _user.getJobTitle();
	}

	public void setJobTitle(String jobTitle) {
		_user.setJobTitle(jobTitle);
	}

	public int getStatus() {
		return _user.getStatus();
	}

	public void setStatus(int status) {
		_user.setStatus(status);
	}

	public long getPortraitId() {
		return _user.getPortraitId();
	}

	public void setPortraitId(long portraitId) {
		_user.setPortraitId(portraitId);
	}

	public long getContactId() {
		return _user.getContactId();
	}

	public void setContactId(long contactId) {
		_user.setContactId(contactId);
	}

	public static Optional<RestUser> fromUser(User user) {
		if (user == null) {
			return Optional.empty();
		}

		return Optional.of(new RestUser(user));
	}

}
