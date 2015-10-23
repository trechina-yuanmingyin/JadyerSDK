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
		<tr><th width="15%">用户名：</th><td>${userInfo.username}</td></tr>
		<tr><th>UUID：</th><td>${userInfo.uuid}</td></tr>
		<tr><th>微信绑定状态：</th><td>${userInfo.bindStatus==0 ? '未绑定' : '已绑定'}</td></tr>
		<tr><th>微信Token：</th><td>${userInfo.token}</td></tr>
		<tr><th>微信原始ID：</th><td>${userInfo.wxId}</td></tr>
		<tr><th>微信号：</th><td>${userInfo.wxNo}</td></tr>
		<tr><th>微信名称：</th><td>${userInfo.wxName}</td></tr>
		<tr><th>微信应用ID：</th><td>${userInfo.appId}</td></tr>
		<tr><th>微信应用密钥：</th><td>${userInfo.appSecret}</td></tr>
		<tr><th>微信加解密密钥：</th><td>${userInfo.appAESKey}</td></tr>
		<tr><th>微信加解密方式：</th><td>${userInfo.appAESStatus==0 ? '明文模式' : userInfo.appAESStatus==1?'兼容模式': userInfo.appAESStatus==2?'安全模式':'未知'}</td></tr>
	</table>
	<!--/Table order list-->
</div>
<!--/Content-->

<jsp:include page="/footer.jsp"/>