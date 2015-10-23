<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/header.jsp"/>

<div class="c_nav">
	<div class="ti">通用的回复</div>
</div>
<!--Content-->
<div class="c_content">
	<!--Table input-->
	<table class="tab_in2" width="100%">
		<tr class="ti">
			<th colspan="2">
				<span class="fl">
					${replyInfo.type eq '4' ? '自动转发多客服' : '未设定'}
				</span>
			</th>
		</tr>
	</table>
	<!--/Table input-->
</div>
<!--/Content-->

<jsp:include page="/footer.jsp"/>