<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/header.jsp"/>

<div class="c_nav">
	<div class="ti">粉丝列表</div>
</div>
<!--Content-->
<div class="c_content">
	<!--Table list-->
	<table class="tab_list" width="100%">
		<tr>
			<th>昵称</th>
			<th>姓名</th>
			<th>手机号</th>
			<th>关注</th>
			<th>省份</th>
			<th>城市</th>
		</tr>
		<c:forEach items="${fansList}" var="fans">
			<tr>
				<td><span>${fans.nickname}</span></td>
				<td><span>${fans.name}</span></td>
				<td><span>${fans.phoneNo}</span></td>
				<td>${fans.subscribe==0 ? '<span class="cf30 fw">未关注</span>' : '<span class="cgre fw">已关注</span>'}</td>
				<td><span>${fans.province}</span></td>
				<td><span>${fans.city}</span></td>
			</tr>
		</c:forEach>
	</table>
	<!--/Table list-->
	<!--Paging-->
	<div class="paging">
		<a href="#" title="第一页">第一页</a>
		<a href="#" title="上一页">上一页</a>
		<a class="curr">1</a>
		<a href="#">2</a>
		<a href="#">3</a>
		<span>…</span>
		<a href="#">4</a>
		<a href="#">5</a>
		<a href="#" title="下一页">下一页</a>
		<a href="#" title="最末页">最末页</a>
		<span class="pl_10">
			<em class="va_m">跳转到</em>
			<input class="inpte" type="text" maxlength="5"/>
			<button class="btn" type="submit">GO</button>
			<b class="va_m pl_10">共 7 页，第 1 页</b>
		</span>
	</div>
	<!--/Paging-->
</div>
<!--/Content-->

<jsp:include page="/footer.jsp"/>