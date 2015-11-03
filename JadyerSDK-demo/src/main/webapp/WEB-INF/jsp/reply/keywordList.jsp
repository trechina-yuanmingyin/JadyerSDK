<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/header.jsp"/>

<div class="c_nav">
	<div class="ti">关键字回复</div>
</div>
<!--Content-->
<div class="c_content">
	<!--Title-->
	<div class="title txt_r">
		<a class="bgre va_m" href="${ctx}/view?url=reply/keyword&do=add">+新增关键字</a>
	</div>
	<!--/Title-->
	<!--Table list-->
	<table class="tab_list" width="100%">
		<tr>
			<th>关键字</th>
			<th>回复类型</th>
			<th>回复内容</th>
			<th>操作</th>
		</tr>
		<c:forEach items="${replyInfoList}" var="reply">
			<tr>
				<td><span>${reply.keyword}</span></td>
				<td><span>${reply.type eq 0 ? '文本' : reply.type eq 1?'图文' : reply.type eq 2?'图片' : reply.type eq 3?'活动' : reply.type eq 4?'转发到多客服':'未知'}</span></td>
				<td>
					<span>
						<c:if test="${fn:length(reply.content)>16}">
							${fn:substring(reply.content,0,16)}...
						</c:if>
						<c:if test="${fn:length(reply.content)<=16}">
							${reply.content}
						</c:if>
					</span>
				</td>
				<td><a class="c09f mr_15" href="${ctx}/view?url=reply/keyword&id=${reply.id}">查看</a><a class="c09f" href="${ctx}/view?url=reply/keyword&do=update&id=${reply.id}">编辑</a></td>
			</tr>
		</c:forEach>
	</table>
	<!--/Table list-->
</div>
<!--/Content-->

<jsp:include page="/footer.jsp"/>