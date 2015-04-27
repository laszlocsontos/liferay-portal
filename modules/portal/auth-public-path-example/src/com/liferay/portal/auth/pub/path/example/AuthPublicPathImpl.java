package com.liferay.portal.auth.pub.path.example;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.struts.path.AuthPublicPath;

import org.osgi.service.component.annotations.Component;

@Component(immediate = true)
public class AuthPublicPathImpl implements AuthPublicPath {

	@Override
	public String path() {
		_log.info("path()");

		return "/portal/update_reminder_query";
	}

	private Log _log = LogFactoryUtil.getLog(AuthPublicPathImpl.class);
}