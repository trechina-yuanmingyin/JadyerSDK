<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/header.jsp"/>

<div class="c_nav">
	<div class="ti">个人资料</div>
</div>
<!--Content-->
<div class="c_content">
	<!--Table order list-->
	<table class="tab_head tab_in tab_list2" width="100%">
		<tr class="ti"><th colspan="2">详细信息</th></tr>
		<tr><th width="15%">ID：</th><td>${userInfo.id}</td></tr>
		<tr><th>UUID：</th><td>${userInfo.uuid}</td></tr>
		<tr><th>用户名：</th><td>${userInfo.username}</td></tr>
		<tr><th>微信绑定状态：</th><td>${userInfo.bindStatus eq 0 ? '<span class="cf30 fw">未绑定</span>' : '<span class="cgre fw">已绑定</span>'}</td></tr>
		<tr><th>微信号：</th><td>${userInfo.wxNo}</td></tr>
		<tr><th>微信名称：</th><td>${userInfo.wxName}</td></tr>
	</table>
	<!--/Table order list-->
</div>
<!--/Content-->

<jsp:include page="/footer.jsp"/>