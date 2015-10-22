<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/header.jsp"/>

<!--Content-->
<div class="c_content">
	<!--Title-->
	<div class="title txt_r">
		<span class="sch2" style="margin-right:50px;">
			<input class="inpte fm2" type="text" placeholder="按名称或商家搜索"/>
			<button class="btn fm2">4S点搜索</button>
		</span>
		<a class="bgre va_m" href="#">+新增资源</a>
	</div>
	<!--/Title-->
	<!--Table list-->
	<table class="tab_list" width="100%">
		<tr>
			<th>名称</th>
			<th>类型</th>
			<th>显示</th>
			<th>有效期</th>
			<th>操作</th>
		</tr>
		<tr>
			<td><span>米易芒果</span></td>
			<td><span>单品类</span></td>
			<td><span>是</span></td>
			<td><span>2014-10-01 00:00 至 2014-11-30 23:55</span></td>
			<td><a class="c09f mr_15" href="#">查看</a><a class="c09f" href="#">编辑</a></td>
		</tr>
		<tr>
			<td><span>米易芒果</span></td>
			<td><span>单品类</span></td>
			<td><span>是</span></td>
			<td><span>2014-10-01 00:00 至 2014-11-30 23:55</span></td>
			<td><a class="c09f mr_15" href="#">查看</a><a class="c09f" href="#">编辑</a></td>
		</tr>
		<tr>
			<td><span>米易芒果</span></td>
			<td><span>单品类</span></td>
			<td><span>是</span></td>
			<td><span>2014-10-01 00:00 至 2014-11-30 23:55</span></td>
			<td><a class="c09f mr_15" href="#">查看</a><a class="c09f" href="#">编辑</a></td>
		</tr>
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