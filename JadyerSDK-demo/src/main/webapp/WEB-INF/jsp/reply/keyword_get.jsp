<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/header.jsp"/>

<div class="c_nav">
	<div class="ti">关键字回复</div>
</div>
<!--Content-->
<div class="c_content">
	<!--Table order list-->
	<table class="tab_head tab_in tab_list2" width="100%">
		<tr class="ti"><th colspan="2">关键字信息</th></tr>
		<tr><th width="15%">关键字：</th><td>${reply.keyword}</td></tr>
		<tr><th>回复类型：</th><td>${reply.type eq 0 ? '文本' : reply.type eq 1?'图文' : reply.type eq 2?'图片' : reply.type eq 3?'活动' : reply.type eq 4?'转发到多客服':'未知'}</td></tr>
		<tr><th>回复内容：</th><td>${reply.content}</td></tr>
	</table>
	<table class="tab_head tab_in tab_list2" width="100%">
		<tr class="ti"><th colspan="3">操作</th></tr>
		<tr>
			<td class="txt_l"><a class="btn_r" href="javascript:history.back();">返回</a></td>
		</tr>
	</table>
	<!--/Table order list-->
</div>
<!--/Content-->

<jsp:include page="/footer.jsp"/>