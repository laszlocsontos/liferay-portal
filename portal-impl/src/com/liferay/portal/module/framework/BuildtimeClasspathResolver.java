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

package com.liferay.portal.module.framework;

import com.liferay.portal.kernel.util.StringUtil;

import java.io.File;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Raymond Augé
 */
public class BuildtimeClasspathResolver implements ClasspathResolver {

	@Override
	public URL[] getClasspathURLs() throws Exception {
		Class<?> clazz = getClass();

		URL url = clazz.getResource("/jars.txt");

		File jarsTxtFile = new File(url.toURI());

		File projectRootDir = jarsTxtFile.getParentFile();

		projectRootDir = projectRootDir.getParentFile();

		URI projectRootURI = projectRootDir.toURI();

		URLConnection urlConnection = url.openConnection();

		String[] jarFileNames = StringUtil.split(
			StringUtil.read(urlConnection.getInputStream()));

		URL[] jarURLs = new URL[jarFileNames.length];

		for (int i = 0; i < jarURLs.length; i++) {
			URI jarURI = projectRootURI.resolve(jarFileNames[i]);

			jarURLs[i] = jarURI.toURL();
		}

		return jarURLs;
	}

}