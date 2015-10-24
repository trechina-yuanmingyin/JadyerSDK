<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/header.jsp"/>

<script>
function pageSubmit(pageNo){
	if(-1 == pageNo){
		pageNo = $("#go").val()-1;
		if(isEmpty(pageNo) || isNotNumber(pageNo)){
			$.promptBox("请填写页数", "#ffb848");
			return;
		}
		if(pageNo > ${page.totalPages}){
			$("#go").val(${page.totalPages});
			$.promptBox("最大页数${page.totalPages}页", "#ffb848");
			return;
		}
	}
	$("#pageForm").attr("action", "${ctx}/fans/list?pageNo=" + pageNo);
	$("#pageForm").submit();
}
</script>

<div class="c_nav">
	<div class="ti">粉丝列表</div>
</div>
<!--Content-->
<div class="c_content">
	<!--Table list-->
	<table class="tab_list" width="100%">
		<tr>
			<th>头像</th>
			<th>昵称</th>
			<th>姓名</th>
			<th>手机号</th>
			<th>关注</th>
			<th>省份</th>
			<th>城市</th>
		</tr>
		<c:forEach items="${page.content}" var="fans">
			<tr>
				<td><span><img alt="粉丝头像" src="${fans.headimgurl}" height="30px" width="30px"></span></td>
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
	<form id="pageForm" method="post">
		<div class="paging">
			<a href="javascript:pageSubmit(0)" title="首页" class="curr">首页</a>
			<a href="#" title="上页">上页</a>
			<a href="#" title="下页">下页</a>
			<a href="javascript:pageSubmit('${page.totalPages-1}')" title="尾页">尾页</a>
			<span class="pl_10">
				<em class="va_m">跳转到</em>
				&nbsp;<input class="inpte" type="text" maxlength="3" id="go" onchange="this.value=this.value.replace(/\D/g,'')"/>
				&nbsp;<button class="btn" type="submit" onclick="pageSubmit(-1)">GO</button>
				<b class="va_m pl_10">第&nbsp;${page.number+1}&nbsp;页，共&nbsp;${page.totalPages}&nbsp;页，合计：${page.totalElements}条</b>
			</span>
		</div>
	</form>
	<!--/Paging-->
</div>
<!--/Content-->

<jsp:include page="/footer.jsp"/>