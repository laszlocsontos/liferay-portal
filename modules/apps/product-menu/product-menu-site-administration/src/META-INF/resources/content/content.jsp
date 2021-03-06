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

<%@ include file="/content/init.jsp" %>

<%
PanelAppRegistry panelAppRegistry = (PanelAppRegistry)request.getAttribute(ApplicationListWebKeys.PANEL_APP_REGISTRY);
PanelCategory panelCategory = (PanelCategory)request.getAttribute(ApplicationListWebKeys.PANEL_CATEGORY);
PanelCategoryRegistry panelCategoryRegistry = (PanelCategoryRegistry)request.getAttribute(ApplicationListWebKeys.PANEL_CATEGORY_REGISTRY);

PanelCategoryHelper panelCategoryHelper = new PanelCategoryHelper(panelAppRegistry, panelCategoryRegistry);

boolean containsActivePortlet = panelCategoryHelper.containsPortlet(themeDisplay.getPpid(), panelCategory);

String panelPageCategoryId = "panel-manage-" + StringUtil.replace(panelCategory.getKey(), StringPool.PERIOD, StringPool.UNDERLINE);
%>

<a aria-expanded="false" class="collapse-icon <%= containsActivePortlet ? StringPool.BLANK : "collapsed" %> list-group-heading" data-toggle="collapse" href="#<%= panelPageCategoryId %>">
	<liferay-ui:message key="content" />
</a>

<div class="collapse <%= containsActivePortlet ? "in" : StringPool.BLANK %>" id="<%= panelPageCategoryId %>">
	<div class="list-group-item">

		<%
		List<Layout> scopeLayouts = new ArrayList<Layout>();

		Group curSite = themeDisplay.getSiteGroup();

		scopeLayouts.addAll(LayoutLocalServiceUtil.getScopeGroupLayouts(curSite.getGroupId(), false));
		scopeLayouts.addAll(LayoutLocalServiceUtil.getScopeGroupLayouts(curSite.getGroupId(), true));
		%>

		<c:if test="<%= !scopeLayouts.isEmpty() %>">
			<ul class="nav nav-equal-height nav-nested">
				<li>
					<div class="nav-equal-height-heading">

						<%
						String scopeLabel = null;

						Group curScopeGroup = themeDisplay.getScopeGroup();

						if (curScopeGroup.isLayout()) {
							scopeLabel = StringUtil.shorten(curScopeGroup.getDescriptiveName(locale), 20);
						}
						else {
							scopeLabel = LanguageUtil.get(request, "default-scope");
						}
						%>

						<span><%= scopeLabel %></span>

						<span class="nav-equal-height-heading-field">
							<liferay-ui:icon-menu direction="down" icon="../aui/cog" message="" showArrow="<%= false %>">

								<%
								Map<String, Object> data = new HashMap<String, Object>();

								data.put("navigation", Boolean.TRUE.toString());

								PortletURL portletURL = PortalUtil.getControlPanelPortletURL(request, curSite, themeDisplay.getPpid(), 0, PortletRequest.RENDER_PHASE);
								%>

								<liferay-ui:icon
									data="<%= data %>"
									iconCssClass="<%= curSite.getIconCssClass() %>"
									message="default-scope"
									url="<%= portletURL.toString() %>"
								/>

								<%
								for (Layout curScopeLayout : scopeLayouts) {
									Group scopeGroup = curScopeLayout.getScopeGroup();

									portletURL = PortalUtil.getControlPanelPortletURL(request, scopeGroup, themeDisplay.getPpid(), 0, PortletRequest.RENDER_PHASE);
								%>

									<liferay-ui:icon
										data="<%= data %>"
										iconCssClass="<%= scopeGroup.getIconCssClass() %>"
										message="<%= HtmlUtil.escape(curScopeLayout.getName(locale)) %>"
										url="<%= portletURL.toString() %>"
									/>

								<%
								}
								%>

							</liferay-ui:icon-menu>
						</span>
					</div>
		</c:if>

		<ul aria-labelledby="<%= panelPageCategoryId %>" class="nav nav-equal-height" role="menu">

			<%
			for (PanelApp panelApp : panelAppRegistry.getPanelApps(panelCategory)) {
			%>

				<c:if test="<%= panelApp.hasAccessPermission(permissionChecker, themeDisplay.getScopeGroup()) %>">
					<liferay-application-list:panel-app
						panelApp="<%= panelApp %>"
						panelCategory="<%= panelCategory %>"
					/>
				</c:if>

			<%
			}
			%>

		</ul>

		<c:if test="<%= !scopeLayouts.isEmpty() %>">
				</ul>
			</li>
		</c:if>
	</div>
</div>