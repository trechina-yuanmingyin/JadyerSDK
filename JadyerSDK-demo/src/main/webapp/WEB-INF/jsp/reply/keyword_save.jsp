<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/header.jsp"/>

<script>
function validateForm(){
	if(isEmpty($("#keyword").val())){
		$.promptBox("请输入关键字", "#ffb848");
	}else if($("#type").val() != 0){
		$("#type").attr("value", "0");
		$.promptBox("暂时只能回复文本", "#ffb848");
	}else if(isEmpty($("#content").val())){
		$.promptBox("请输入回复的文本内容", "#ffb848");
	}else if($("#content").val().length > 1024){
		$.promptBox("回复的文本内容不能超过1024", "#ffb848");
	}else{
		return true;
	}
}
function submit(){
	if(validateForm()){
		$("#keywordForm").submit();
	}
}
</script>

<div class="c_nav">
	<div class="ti">关键字回复</div>
</div>
<!--Content-->
<div class="c_content">
	<!--Table order list-->
	<form id="keywordForm" action="${ctx}/reply/keyword/save" method="post">
		<input type="hidden" name="category" value="2"/>
		<input type="hidden" name="uid" value="${uid}"/>
		<input type="hidden" name="id" value="${empty replyInfo.id ? 0 : replyInfo.id}"/>
		<table class="tab_in2" width="100%">
			<tr class="ti">
				<th colspan="2">关键字信息</th>
			</tr>
			<tr>
				<th width="15%">关键字：</th>
				<td><input class="inpte" type="text" id="keyword" name="keyword" value="${replyInfo.keyword}" maxlength="16"/></td>
			</tr>
			<tr>
				<th>回复类型：</th>
				<td>
					<select id="type" name="type">
						<option value="0" ${replyInfo.type eq 0?'selected=selected':''}>文本</option>
						<option value="1" ${replyInfo.type eq 1?'selected=selected':''}>图文</option>
						<option value="2" ${replyInfo.type eq 2?'selected=selected':''}>图片</option>
						<option value="3" ${replyInfo.type eq 3?'selected=selected':''}>活动</option>
						<option value="4" ${replyInfo.type eq 4?'selected=selected':''}>转发多客服</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>回复内容：</th>
				<td><textarea id="content" name="content" style="height:300px;">${replyInfo.content}</textarea></td>
			</tr>
		</table>
		<table class="tab_head tab_in tab_list2" width="100%">
			<tr class="ti"><th colspan="3">操作</th></tr>
			<tr>
				<td class="txt_l"><a class="btn_g" href="javascript:submit();">保存</a></td>
			</tr>
		</table>
	</form>
	<!--/Table order list-->
</div>
<!--/Content-->

<jsp:include page="/footer.jsp"/>