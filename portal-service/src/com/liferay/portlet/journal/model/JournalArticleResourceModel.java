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

package com.liferay.portlet.journal.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

/**
 * The base model interface for the JournalArticleResource service. Represents a row in the &quot;JournalArticleResource&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.portlet.journal.model.impl.JournalArticleResourceModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.portlet.journal.model.impl.JournalArticleResourceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see JournalArticleResource
 * @see com.liferay.portlet.journal.model.impl.JournalArticleResourceImpl
 * @see com.liferay.portlet.journal.model.impl.JournalArticleResourceModelImpl
 * @generated
 */
public interface JournalArticleResourceModel extends BaseModel<JournalArticleResource> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a journal article resource model instance should use the {@link JournalArticleResource} interface instead.
	 */

	/**
	 * Returns the primary key of this journal article resource.
	 *
	 * @return the primary key of this journal article resource
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this journal article resource.
	 *
	 * @param primaryKey the primary key of this journal article resource
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this journal article resource.
	 *
	 * @return the uuid of this journal article resource
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this journal article resource.
	 *
	 * @param uuid the uuid of this journal article resource
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the resource prim key of this journal article resource.
	 *
	 * @return the resource prim key of this journal article resource
	 */
	public long getResourcePrimKey();

	/**
	 * Sets the resource prim key of this journal article resource.
	 *
	 * @param resourcePrimKey the resource prim key of this journal article resource
	 */
	public void setResourcePrimKey(long resourcePrimKey);

	/**
	 * Returns the group ID of this journal article resource.
	 *
	 * @return the group ID of this journal article resource
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this journal article resource.
	 *
	 * @param groupId the group ID of this journal article resource
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the article ID of this journal article resource.
	 *
	 * @return the article ID of this journal article resource
	 */
	@AutoEscape
	public String getArticleId();

	/**
	 * Sets the article ID of this journal article resource.
	 *
	 * @param articleId the article ID of this journal article resource
	 */
	public void setArticleId(String articleId);

	/**
	 * Returns the version of this journal article resource.
	 *
	 * @return the version of this journal article resource
	 */
	public double getVersion();

	/**
	 * Sets the version of this journal article resource.
	 *
	 * @param version the version of this journal article resource
	 */
	public void setVersion(double version);

	public boolean isNew();

	public void setNew(boolean n);

	public boolean isCachedModel();

	public void setCachedModel(boolean cachedModel);

	public boolean isEscapedModel();

	public Serializable getPrimaryKeyObj();

	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	public ExpandoBridge getExpandoBridge();

	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	public Object clone();

	public int compareTo(JournalArticleResource journalArticleResource);

	public int hashCode();

	public CacheModel<JournalArticleResource> toCacheModel();

	public JournalArticleResource toEscapedModel();

	public JournalArticleResource toUnescapedModel();

	public String toString();

	public String toXmlString();
}