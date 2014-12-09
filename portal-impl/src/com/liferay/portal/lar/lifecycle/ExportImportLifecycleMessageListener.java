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

package com.liferay.portal.lar.lifecycle;

import com.liferay.portal.kernel.lar.lifecycle.ExportImportLifecycleEvent;
import com.liferay.portal.kernel.lar.lifecycle.ExportImportLifecycleEventListenerRegistryUtil;
import com.liferay.portal.kernel.lar.lifecycle.ExportImportLifecycleListener;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;

import java.util.Set;

/**
 * @author Daniel Kocsis
 */
public class ExportImportLifecycleMessageListener extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {
		Set<ExportImportLifecycleListener> exportImportLifecycleListeners =
			getExportImportLifecycleListeners(message);

		ExportImportLifecycleEvent exportImportLifecycleEvent =
			(ExportImportLifecycleEvent)message.get(
				"exportImportLifecycleEvent");

		for (ExportImportLifecycleListener exportImportLifecycleListener :
				exportImportLifecycleListeners) {

			try {
				exportImportLifecycleListener.onExportImportLifecycleEvent(
					exportImportLifecycleEvent);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to call " +
							exportImportLifecycleListener.getClass(),
						e);
				}
			}
		}
	}

	protected Set<ExportImportLifecycleListener>
		getExportImportLifecycleListeners(Message message) {

		String destinationName = message.getDestinationName();

		if (destinationName.equals(
				DestinationNames.EXPORT_IMPORT_LIFECYCLE_EVENT_SYNC)) {

			return ExportImportLifecycleEventListenerRegistryUtil.
				getSyncExportImportLifecycleListeners();
		}

		return ExportImportLifecycleEventListenerRegistryUtil.
			getAsyncExportImportLifecycleListeners();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ExportImportLifecycleMessageListener.class);

}