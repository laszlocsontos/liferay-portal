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

package com.liferay.portlet.expando;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.service.ExpandoColumnServiceUtil;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;
import com.liferay.portlet.expando.util.ExpandoPresetUtil;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Raymond Augé
 * @author Drew Brokke
 */
public class ExpandoPortlet extends MVCPortlet {

	public void addExpando(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String modelResource = ParamUtil.getString(
			actionRequest, "modelResource");
		long resourcePrimKey = ParamUtil.getLong(
			actionRequest, "resourcePrimKey");

		String name = ParamUtil.getString(actionRequest, "name");
		String preset = ParamUtil.getString(actionRequest, "type");

		ExpandoBridge expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(
			themeDisplay.getCompanyId(), modelResource, resourcePrimKey);

		if (preset.startsWith("Preset")) {
			ExpandoPresetUtil.addPresetExpando(expandoBridge, preset, name);
		}
		else {
			int type = ParamUtil.getInteger(actionRequest, "type");

			expandoBridge.addAttribute(name, type);

			updateProperties(actionRequest, expandoBridge, name);
		}
	}

	public void deleteExpando(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long columnId = ParamUtil.getLong(actionRequest, "columnId");

		ExpandoColumnServiceUtil.deleteColumn(columnId);
	}

	public void updateExpando(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String modelResource = ParamUtil.getString(
			actionRequest, "modelResource");
		long resourcePrimKey = ParamUtil.getLong(
			actionRequest, "resourcePrimKey");

		String name = ParamUtil.getString(actionRequest, "name");
		int type = ParamUtil.getInteger(actionRequest, "type");

		Serializable defaultValue = getValue(
			actionRequest, "defaultValue", type);

		ExpandoBridge expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(
			themeDisplay.getCompanyId(), modelResource, resourcePrimKey);

		expandoBridge.setAttributeDefault(name, defaultValue);

		updateProperties(actionRequest, expandoBridge, name);
	}

	@Override
	protected void doDispatch(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		if (SessionErrors.contains(
				renderRequest, ColumnNameException.class.getName()) ||
			SessionErrors.contains(
				renderRequest, ColumnTypeException.class.getName()) ||
			SessionErrors.contains(
				renderRequest, DuplicateColumnNameException.class.getName()) ||
			SessionErrors.contains(
				renderRequest, ValueDataException.class.getName())) {

			include(
				"/html/portlet/expando/edit_expando.jsp", renderRequest,
				renderResponse);
		}

		if (SessionErrors.contains(
				renderRequest, NoSuchColumnException.class.getName()) ||
			SessionErrors.contains(
				renderRequest, PrincipalException.getNestedClasses())) {

			include(
				"/html/portlet/expando/error.jsp", renderRequest,
				renderResponse);
		}
		else {
			super.doDispatch(renderRequest, renderResponse);
		}
	}

	protected Serializable getValue(
			PortletRequest portletRequest, String name, int type)
		throws PortalException {

		String delimiter = StringPool.COMMA;

		Serializable value = null;

		if (type == ExpandoColumnConstants.BOOLEAN) {
			value = ParamUtil.getBoolean(portletRequest, name);
		}
		else if (type == ExpandoColumnConstants.BOOLEAN_ARRAY) {
		}
		else if (type == ExpandoColumnConstants.DATE) {
			User user = PortalUtil.getUser(portletRequest);

			int valueDateMonth = ParamUtil.getInteger(
				portletRequest, name + "Month");
			int valueDateDay = ParamUtil.getInteger(
				portletRequest, name + "Day");
			int valueDateYear = ParamUtil.getInteger(
				portletRequest, name + "Year");
			int valueDateHour = ParamUtil.getInteger(
				portletRequest, name + "Hour");
			int valueDateMinute = ParamUtil.getInteger(
				portletRequest, name + "Minute");
			int valueDateAmPm = ParamUtil.getInteger(
				portletRequest, name + "AmPm");

			if (valueDateAmPm == Calendar.PM) {
				valueDateHour += 12;
			}

			value = PortalUtil.getDate(
				valueDateMonth, valueDateDay, valueDateYear, valueDateHour,
				valueDateMinute, user.getTimeZone(), ValueDataException.class);
		}
		else if (type == ExpandoColumnConstants.DATE_ARRAY) {
		}
		else if (type == ExpandoColumnConstants.DOUBLE) {
			value = ParamUtil.getDouble(portletRequest, name);
		}
		else if (type == ExpandoColumnConstants.DOUBLE_ARRAY) {
			String paramValue = ParamUtil.getString(portletRequest, name);

			if (paramValue.contains(StringPool.NEW_LINE)) {
				delimiter = StringPool.NEW_LINE;
			}

			String[] values = StringUtil.split(paramValue, delimiter);

			value = GetterUtil.getDoubleValues(values);
		}
		else if (type == ExpandoColumnConstants.FLOAT) {
			value = ParamUtil.getFloat(portletRequest, name);
		}
		else if (type == ExpandoColumnConstants.FLOAT_ARRAY) {
			String paramValue = ParamUtil.getString(portletRequest, name);

			if (paramValue.contains(StringPool.NEW_LINE)) {
				delimiter = StringPool.NEW_LINE;
			}

			String[] values = StringUtil.split(paramValue, delimiter);

			value = GetterUtil.getFloatValues(values);
		}
		else if (type == ExpandoColumnConstants.INTEGER) {
			value = ParamUtil.getInteger(portletRequest, name);
		}
		else if (type == ExpandoColumnConstants.INTEGER_ARRAY) {
			String paramValue = ParamUtil.getString(portletRequest, name);

			if (paramValue.contains(StringPool.NEW_LINE)) {
				delimiter = StringPool.NEW_LINE;
			}

			String[] values = StringUtil.split(paramValue, delimiter);

			value = GetterUtil.getIntegerValues(values);
		}
		else if (type == ExpandoColumnConstants.LONG) {
			value = ParamUtil.getLong(portletRequest, name);
		}
		else if (type == ExpandoColumnConstants.LONG_ARRAY) {
			String paramValue = ParamUtil.getString(portletRequest, name);

			if (paramValue.contains(StringPool.NEW_LINE)) {
				delimiter = StringPool.NEW_LINE;
			}

			String[] values = StringUtil.split(paramValue, delimiter);

			value = GetterUtil.getLongValues(values);
		}
		else if (type == ExpandoColumnConstants.NUMBER) {
			value = ParamUtil.getNumber(portletRequest, name);
		}
		else if (type == ExpandoColumnConstants.NUMBER_ARRAY) {
			String paramValue = ParamUtil.getString(portletRequest, name);

			if (paramValue.contains(StringPool.NEW_LINE)) {
				delimiter = StringPool.NEW_LINE;
			}

			String[] values = StringUtil.split(paramValue, delimiter);

			value = GetterUtil.getNumberValues(values);
		}
		else if (type == ExpandoColumnConstants.SHORT) {
			value = ParamUtil.getShort(portletRequest, name);
		}
		else if (type == ExpandoColumnConstants.SHORT_ARRAY) {
			String paramValue = ParamUtil.getString(portletRequest, name);

			if (paramValue.contains(StringPool.NEW_LINE)) {
				delimiter = StringPool.NEW_LINE;
			}

			String[] values = StringUtil.split(paramValue, delimiter);

			value = GetterUtil.getShortValues(values);
		}
		else if (type == ExpandoColumnConstants.STRING_ARRAY) {
			String paramValue = ParamUtil.getString(portletRequest, name);

			if (paramValue.contains(StringPool.NEW_LINE)) {
				delimiter = StringPool.NEW_LINE;
			}

			value = StringUtil.split(paramValue, delimiter);
		}
		else if (type == ExpandoColumnConstants.STRING_LOCALIZED) {
			value = (Serializable)LocalizationUtil.getLocalizationMap(
				portletRequest, name);
		}
		else {
			value = ParamUtil.getString(portletRequest, name);
		}

		return value;
	}

	@Override
	protected boolean isSessionErrorException(Throwable cause) {
		if (cause instanceof ColumnNameException ||
			cause instanceof ColumnTypeException ||
			cause instanceof DuplicateColumnNameException ||
			cause instanceof NoSuchColumnException ||
			cause instanceof PrincipalException ||
			cause instanceof ValueDataException) {

			return true;
		}

		return false;
	}

	protected void updateProperties(
			ActionRequest actionRequest, ExpandoBridge expandoBridge,
			String name)
		throws Exception {

		Enumeration<String> enu = actionRequest.getParameterNames();

		UnicodeProperties properties = expandoBridge.getAttributeProperties(
			name);

		List<String> propertyNames = new ArrayList<>();

		while (enu.hasMoreElements()) {
			String param = enu.nextElement();

			if (param.contains("PropertyName--")) {
				String propertyName = ParamUtil.getString(actionRequest, param);

				propertyNames.add(propertyName);
			}
		}

		for (String propertyName : propertyNames) {
			String value = ParamUtil.getString(
				actionRequest, "Property--" + propertyName + "--");

			properties.setProperty(propertyName, value);
		}

		expandoBridge.setAttributeProperties(name, properties);
	}

}