<%--
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
--%>

<%@ include file="/blogs/init.jsp" %>

<%
SearchContainer searchContainer = (SearchContainer)request.getAttribute("view_entry_content.jsp-searchContainer");

BlogsEntry entry = (BlogsEntry)request.getAttribute("view_entry_content.jsp-entry");

AssetEntry assetEntry = (AssetEntry)request.getAttribute("view_entry_content.jsp-assetEntry");
%>

<c:choose>
	<c:when test="<%= BlogsEntryPermission.contains(permissionChecker, entry, ActionKeys.VIEW) && (entry.isVisible() || (entry.getUserId() == user.getUserId()) || BlogsEntryPermission.contains(permissionChecker, entry, ActionKeys.UPDATE)) %>">
		<div class="entry <%= WorkflowConstants.getStatusLabel(entry.getStatus()) %>" id="<portlet:namespace /><%= entry.getEntryId() %>">
			<div class="entry-body">

				<%
				String mvcRenderCommandName = ParamUtil.getString(request, "mvcRenderCommandName");

				long assetCategoryId = ParamUtil.getLong(request, "categoryId");
				String assetTagName = ParamUtil.getString(request, "tag");

				boolean viewSingleEntry = mvcRenderCommandName.equals("/blogs/view_entry") && (assetCategoryId == 0) && Validator.isNull(assetTagName);
				%>

				<c:if test="<%= BlogsEntryPermission.contains(permissionChecker, entry, ActionKeys.DELETE) || BlogsEntryPermission.contains(permissionChecker, entry, ActionKeys.UPDATE) %>">
					<liferay-ui:icon-menu cssClass="entry-options" direction="right" icon="<%= StringPool.BLANK %>" message="<%= StringPool.BLANK %>" scroll="<%= false %>" showWhenSingleIcon="<%= true %>" triggerCssClass="text-muted" view="lexicon">
						<c:if test="<%= BlogsEntryPermission.contains(permissionChecker, entry, ActionKeys.UPDATE) %>">
							<portlet:renderURL var="editEntryURL">
								<portlet:param name="mvcRenderCommandName" value="/blogs/edit_entry" />
								<portlet:param name="redirect" value="<%= currentURL %>" />
								<portlet:param name="backURL" value="<%= currentURL %>" />
								<portlet:param name="entryId" value="<%= String.valueOf(entry.getEntryId()) %>" />
							</portlet:renderURL>

							<liferay-ui:icon
								iconCssClass="icon-edit"
								label="<%= true %>"
								message="edit"
								url="<%= editEntryURL %>"
							/>
						</c:if>

						<c:if test="<%= BlogsEntryPermission.contains(permissionChecker, entry, ActionKeys.DELETE) %>">
							<portlet:renderURL var="viewURL">
								<portlet:param name="mvcRenderCommandName" value="/blogs/view" />
							</portlet:renderURL>

							<portlet:actionURL name="/blogs/edit_entry" var="deleteEntryURL">
								<portlet:param name="<%= Constants.CMD %>" value="<%= TrashUtil.isTrashEnabled(scopeGroupId) ? Constants.MOVE_TO_TRASH : Constants.DELETE %>" />
								<portlet:param name="redirect" value="<%= viewURL %>" />
								<portlet:param name="entryId" value="<%= String.valueOf(entry.getEntryId()) %>" />
							</portlet:actionURL>

							<liferay-ui:icon-delete
								label="<%= true %>"
								trash="<%= TrashUtil.isTrashEnabled(scopeGroupId) %>"
								url="<%= deleteEntryURL %>"
							/>
						</c:if>
					</liferay-ui:icon-menu>
				</c:if>

				<%
				String coverImageURL = entry.getCoverImageURL(themeDisplay);
				%>

				<c:if test="<%= Validator.isNotNull(coverImageURL) %>">
					<div class="cover-image-container" style="background-image: url(<%= coverImageURL %>)"></div>

					<c:if test="<%= viewSingleEntry %>">
						<div class="cover-image-caption">
							<small><%= entry.getCoverImageCaption() %></small>
						</div>
					</c:if>
				</c:if>

				<c:if test="<%= !viewSingleEntry %>">
					<div class="entry-info text-muted">
						<small>
							<strong><%= entry.getUserName() %></strong>
							<span> - </span>
							<span class="hide-accessible"><liferay-ui:message key="published-date" /></span>
							<%= dateFormatDate.format(entry.getDisplayDate()) %>
						</small>
					</div>
				</c:if>

				<portlet:renderURL var="viewEntryURL">
					<portlet:param name="mvcRenderCommandName" value="/blogs/view_entry" />
					<portlet:param name="redirect" value="<%= currentURL %>" />
					<portlet:param name="urlTitle" value="<%= entry.getUrlTitle() %>" />
				</portlet:renderURL>

				<div class="entry-title">
					<c:choose>
						<c:when test="<%= !viewSingleEntry %>">
							<h2>
								<aui:a href="<%= viewEntryURL %>"><%= HtmlUtil.escape(entry.getTitle()) %></aui:a>
							</h2>
						</c:when>
						<c:otherwise>
							<h1><%= HtmlUtil.escape(entry.getTitle()) %></h1>
						</c:otherwise>
					</c:choose>
				</div>

				<%
				String subtitle = entry.getSubtitle();
				%>

				<c:if test="<%= viewSingleEntry && Validator.isNotNull(subtitle) %>">
					<div class="entry-subtitle">
						<h4><%= HtmlUtil.escape(subtitle) %></h4>
					</div>
				</c:if>
			</div>

			<div class="entry-body">
				<c:choose>
					<c:when test="<%= blogsPortletInstanceConfiguration.displayStyle().equals(BlogsUtil.DISPLAY_STYLE_ABSTRACT) && !viewSingleEntry %>">

						<%
						String summary = entry.getDescription();

						if (Validator.isNull(summary)) {
							summary = entry.getContent();
						}
						%>

						<p>
							<%= StringUtil.shorten(HtmlUtil.stripHtml(summary), pageAbstractLength) %>
						</p>
					</c:when>
					<c:when test="<%= blogsPortletInstanceConfiguration.displayStyle().equals(BlogsUtil.DISPLAY_STYLE_FULL_CONTENT) || viewSingleEntry %>">
						<div class="entry-content">
							<%= entry.getContent() %>
						</div>

						<liferay-ui:custom-attributes-available className="<%= BlogsEntry.class.getName() %>">
							<liferay-ui:custom-attribute-list
								className="<%= BlogsEntry.class.getName() %>"
								classPK="<%= entry.getEntryId() %>"
								editable="<%= false %>"
								label="<%= true %>"
							/>
						</liferay-ui:custom-attributes-available>

					</c:when>
				</c:choose>
			</div>

			<c:if test="<%= viewSingleEntry %>">
				<aui:container cssClass="entry-metadata">
					<aui:col width="<%= 40 %>">
						<c:if test="<%= blogsPortletInstanceConfiguration.enableRelatedAssets() %>">
							<div class="entry-links">
								<liferay-ui:asset-links
									assetEntryId="<%= (assetEntry != null) ? assetEntry.getEntryId() : 0 %>"
									className="<%= BlogsEntry.class.getName() %>"
									classPK="<%= entry.getEntryId() %>"
								/>
							</div>
						</c:if>

						<liferay-ui:asset-categories-available
							className="<%= BlogsEntry.class.getName() %>"
							classPK="<%= entry.getEntryId() %>"
						>
							<p><liferay-ui:message key="categories" />:</p>

							<div class="entry-categories">
								<liferay-ui:asset-categories-summary
									className="<%= BlogsEntry.class.getName() %>"
									classPK="<%= entry.getEntryId() %>"
									portletURL="<%= renderResponse.createRenderURL() %>"
								/>
							</div>
						</liferay-ui:asset-categories-available>
					</aui:col>

					<aui:col width="<%= 60 %>">
						<liferay-ui:asset-tags-available
							className="<%= BlogsEntry.class.getName() %>"
							classPK="<%= entry.getEntryId() %>"
						>
							<div class="entry-tags">
								<p><liferay-ui:message key="tags" />:</p>

								<liferay-ui:asset-tags-summary
									className="<%= BlogsEntry.class.getName() %>"
									classPK="<%= entry.getEntryId() %>"
									portletURL="<%= renderResponse.createRenderURL() %>"
								/>
							</div>
						</liferay-ui:asset-tags-available>
					</aui:col>
				</aui:container>
			</c:if>

			<div class="<%= viewSingleEntry ? "border-top" : StringPool.BLANK %> entry-footer">
				<c:if test="<%= viewSingleEntry %>">
					<div class="entry-author">
						<liferay-ui:user-display
							userId="<%= entry.getUserId() %>"
							userName="<%= entry.getUserName() %>"
							view="lexicon"
						>
							<%= dateFormatDateTime.format(entry.getDisplayDate()) %>
						</liferay-ui:user-display>
					</div>
				</c:if>

				<div class="entry-social">
					<c:if test="<%= !viewSingleEntry && blogsPortletInstanceConfiguration.enableComments() %>">

						<%
						int messagesCount = CommentManagerUtil.getCommentsCount(BlogsEntry.class.getName(), entry.getEntryId());
						%>

						<portlet:renderURL var="viewEntryCommentsURL">
							<portlet:param name="mvcRenderCommandName" value="/blogs/view_entry" />
							<portlet:param name="redirect" value="<%= currentURL %>" />
							<portlet:param name="scroll" value='<%= renderResponse.getNamespace() + "discussionContainer" %>' />
							<portlet:param name="urlTitle" value="<%= entry.getUrlTitle() %>" />
						</portlet:renderURL>

						<div class="comments">
							<a href="<%= viewEntryCommentsURL %>">
								<i class="icon-comment"></i>
								<span><%= String.valueOf(messagesCount) %></span>
							</a>
						</div>
					</c:if>

					<c:if test="<%= blogsPortletInstanceConfiguration.enableRatings() %>">
						<div class="ratings">
							<liferay-ui:ratings
								className="<%= BlogsEntry.class.getName() %>"
								classPK="<%= entry.getEntryId() %>"
							/>
						</div>
					</c:if>

					<c:if test="<%= blogsPortletInstanceConfiguration.enableSocialBookmarks() %>">
						<portlet:renderURL var="bookmarkURL" windowState="<%= WindowState.NORMAL.toString() %>">
							<portlet:param name="mvcRenderCommandName" value="/blogs/view_entry" />
							<portlet:param name="urlTitle" value="<%= entry.getUrlTitle() %>" />
						</portlet:renderURL>

						<div class="social-bookmarks">
							<liferay-ui:social-bookmarks
								contentId="<%= String.valueOf(entry.getEntryId()) %>"
								displayStyle="<%= blogsPortletInstanceConfiguration.socialBookmarksDisplayStyle() %>"
								target="_blank"
								title="<%= entry.getTitle() %>"
								types="<%= blogsPortletInstanceConfiguration.socialBookmarksTypes() %>"
								url="<%= PortalUtil.getCanonicalURL(bookmarkURL.toString(), themeDisplay, layout) %>"
							/>
						</div>
					</c:if>

					<c:if test="<%= viewSingleEntry && blogsPortletInstanceConfiguration.enableFlags() %>">
						<div class="flags">
							<liferay-ui:flags
								className="<%= BlogsEntry.class.getName() %>"
								classPK="<%= entry.getEntryId() %>"
								contentTitle="<%= entry.getTitle() %>"
								reportedUserId="<%= entry.getUserId() %>"
							/>
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</c:when>
	<c:otherwise>

		<%
		if (searchContainer != null) {
			searchContainer.setTotal(searchContainer.getTotal() - 1);
		}
		%>

	</c:otherwise>
</c:choose>