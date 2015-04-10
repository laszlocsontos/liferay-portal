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

package com.liferay.portal.rest.extender;

import aQute.bnd.annotation.metatype.Configurable;

import com.liferay.portal.dm.tccl.TCCLDependencyManager;
import com.liferay.portal.rest.extender.configuration.RestExtenderConfiguration;

import java.util.Map;

import javax.ws.rs.core.Application;

import org.apache.cxf.Bus;
import org.apache.felix.dm.ServiceDependency;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	configurationPid = "com.liferay.portal.rest.extender.RestExtenderConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE
)
public class RestExtender {

	@Activate
	public void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_restExtenderConfiguration = Configurable.createConfigurable(
			RestExtenderConfiguration.class, properties);

		_dependencyManager = new TCCLDependencyManager(bundleContext);

		_component = _dependencyManager.createComponent();

		_component.setImplementation(
			new CXFJaxRSServiceRegistrator(properties));

		addApplicationDependencies();
		addBusDependencies();
		addProviderDependencies();
		addServiceDependencies();

		_component.start();
	}

	@Deactivate
	public void deactivate() {
		_dependencyManager.clear();
	}

	public RestExtenderConfiguration getRestExtenderConfiguration() {
		return _restExtenderConfiguration;
	}

	@Modified
	public void modified(
		BundleContext bundleContext, Map<String, Object> properties) {

		deactivate();

		activate(bundleContext, properties);
	}

	protected void addApplicationDependencies() {
		RestExtenderConfiguration restExtenderConfiguration =
			getRestExtenderConfiguration();

		String[] applicationFilters =
			restExtenderConfiguration.applicationFilters();

		if (applicationFilters != null) {
			for (String applicationFilter : applicationFilters) {
				addTCCLDependency(
					false, Application.class, applicationFilter,
					"addApplication", "removeApplication");
			}
		}
	}

	protected void addBusDependencies() {
		String[] contextPathStrings =
			getRestExtenderConfiguration().contextPaths();

		if (contextPathStrings != null) {
			for (String contextPath : contextPathStrings) {
				addTCCLDependency(
					true, Bus.class,
					"(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH +
						"=" + contextPath + ")", "addBus", "removeBus");
			}
		}
	}

	protected void addProviderDependencies() {
		RestExtenderConfiguration soapExtenderConfiguration =
			getRestExtenderConfiguration();

		String[] providerFilters = soapExtenderConfiguration.providerFilters();

		if (providerFilters != null) {
			for (String providerFilter : providerFilters) {
				addTCCLDependency(
					false, null, providerFilter, "addProvider",
					"removeProvider");
			}
		}
	}

	protected void addServiceDependencies() {
		RestExtenderConfiguration soapExtenderConfiguration =
			getRestExtenderConfiguration();

		String[] serviceFilters = soapExtenderConfiguration.serviceFilters();

		if (serviceFilters != null) {
			for (String serviceFilter : serviceFilters) {
				addTCCLDependency(
					false, null, serviceFilter, "addService", "removeService");
			}
		}
	}

	protected ServiceDependency addTCCLDependency(
		boolean required, Class<?> clazz, String filter, String addName,
		String removeName) {

		ServiceDependency serviceDependency =
			_dependencyManager.createTCCLServiceDependency();

		serviceDependency.setRequired(required);

		if (clazz == null) {
			serviceDependency.setService(filter);
		}
		else {
			serviceDependency.setService(clazz, filter);
		}

		serviceDependency.setCallbacks(addName, removeName);

		_component.add(serviceDependency);

		return serviceDependency;
	}

	private org.apache.felix.dm.Component _component;
	private TCCLDependencyManager _dependencyManager;
	private RestExtenderConfiguration _restExtenderConfiguration;

}