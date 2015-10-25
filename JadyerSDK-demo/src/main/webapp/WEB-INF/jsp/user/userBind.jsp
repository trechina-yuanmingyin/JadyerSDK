<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/header.jsp"/>

<script>
function validateForm(){
	if(isEmpty($("#wxId").val())){
		$.promptBox("请填入微信原始ID", "#ffb848");
	}else if(isEmpty($("#wxName").val())){
		$.promptBox("请填入微信名称（测试号请填入\"某某的接口测试号\"）", "#ffb848");
	}else if(isEmpty($("#appId").val())){
		$.promptBox("请填入微信应用ID(appId)", "#ffb848");
	}else if(isEmpty($("#appSecret").val())){
		$.promptBox("请填入微信应用密钥(appSecret)", "#ffb848");
	}else if($("#appAESStatus").val() != 0){
		$("#appAESStatus").attr("value", "0");
		$.promptBox("暂时只能使用明文模式", "#ffb848");
	}else{
		return true;
	}
}
function submit(){
	if(validateForm()){
		if("${userInfo.bindStatus}"=="0" || ("${userInfo.bindStatus}"!="0" && confirm("确定要重新绑定么？\r\n重新绑定过程中微信公众号将无法提供服务！！"))){
			$("#userBindForm").submit();
		}
	}
}
</script>

<div class="c_nav">
	<div class="ti">微信绑定</div>
</div>
<!--Content-->
<div class="c_content">
	<!--Table order list-->
	<form id="userBindForm" action="${ctx}/user/bind" method="post">
		<input type="hidden" name="id" value="${uid}"/>
		<input type="hidden" name="bindStatus" value="0"/>
		<input type="hidden" name="token" value="${token}"/>
		<table class="tab_in2" width="100%">
			<tr class="ti"><th colspan="2">详细信息</th></tr>
			<tr><th width="15%">微信URL：</th><td>${weixinURL}</td></tr>
			<tr><th>微信Token：</th><td>${token}</td></tr>
			<tr><th>微信绑定状态：</th><td>${userInfo.bindStatus eq 0 ? '<span class="cf30 fw">未绑定</span>' : '<span class="cgre fw">已绑定</span>'}</td></tr>
			<tr><th>微信原始ID：</th><td><input class="inpte" type="text" id="wxId" name="wxId" value="${userInfo.wxId}" maxlength="32"/></td></tr>
			<tr><th>微信号：</th><td><input class="inpte" type="text" id="wxNo" name="wxNo" value="${userInfo.wxNo}" maxlength="32"/></td></tr>
			<tr><th>微信名称：</th><td><input class="inpte" type="text" id="wxName" name="wxName" value="${userInfo.wxName}" maxlength="32"/></td></tr>
			<tr><th>微信应用ID：</th><td><input class="inpte" type="text" id="appId" name="appId" value="${userInfo.appId}" maxlength="32"/></td></tr>
			<tr><th>微信应用密钥：</th><td><input class="inpte" type="text" id="appSecret" name="appSecret" value="${userInfo.appSecret}" maxlength="64"/></td></tr>
			<tr><th>微信加解密密钥：</th><td><input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="64"/></td></tr>
			<tr>
				<th>微信加解密方式：</th>
				<td>
					<select id="appAESStatus" name="appAESStatus">
						<option value="0" ${userInfo.appAESStatus eq 0?'selected=selected':''}>明文模式</option>
						<option value="1" ${userInfo.appAESStatus eq 1?'selected=selected':''}>兼容模式</option>
						<option value="2" ${userInfo.appAESStatus eq 2?'selected=selected':''}>安全模式</option>
					</select>
				</td>
			</tr>
		</table>
		<table class="tab_head tab_in tab_list2" width="100%">
			<tr class="ti"><th colspan="3">操作</th></tr>
			<tr><td class="txt_l"><a class="btn_g" href="javascript:submit();">下一步</a></td></tr>
		</table>
	</form>
	<!--/Table order list-->
</div>
<!--/Content-->

<jsp:include page="/footer.jsp"/>