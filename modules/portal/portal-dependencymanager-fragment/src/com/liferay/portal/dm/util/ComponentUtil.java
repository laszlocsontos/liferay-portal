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

package com.liferay.portal.dm.util;

import com.liferay.portal.dm.tccl.TCCLDependencyManager;

import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.dm.ServiceDependency;

/**
 * @author Carlos Sierra Andrés
 */
public class ComponentUtil {

	public static TCCLComponentHelper createHelper(final Component component) {
		DependencyManager dependencyManager = component.getDependencyManager();

		if (!(dependencyManager instanceof TCCLDependencyManager)) {
			throw new IllegalArgumentException(
				"Component must be created using a TCCLDependencyManager in " +
					"order to use this helper");
		}

		final TCCLDependencyManager tcclDependencyManager =
			(TCCLDependencyManager)dependencyManager;

		return new TCCLComponentHelper() {
			@Override
			public ServiceDependency addTCCLDependency(
				boolean required, Class<?> clazz, String filter, String addName,
				String removeName) {

				ServiceDependency serviceDependency =
					tcclDependencyManager.createTCCLServiceDependency();

				serviceDependency.setRequired(required);

				if (clazz == null) {
					serviceDependency.setService(filter);
				}
				else {
					serviceDependency.setService(clazz, filter);
				}

				serviceDependency.setCallbacks(addName, removeName);

				component.add(serviceDependency);

				return serviceDependency;
			}
		};
	}

	public interface TCCLComponentHelper {
		public ServiceDependency addTCCLDependency(
			boolean required, Class<?> clazz, String filter, String addName,
			String removeName);

	}

}