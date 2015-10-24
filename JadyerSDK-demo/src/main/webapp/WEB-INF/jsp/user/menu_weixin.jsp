<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/header.jsp"/>

<div class="c_nav">
	<div class="ti">微信菜单</div>
</div>
<!--Content-->
<div class="c_content">
	<table class="tab_head tab_in tab_list2" width="100%">
		<tr class="ti"><th colspan="4">请注意</th></tr>
		<tr><td><span>1、自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。</span></td></tr>
		<tr><td><span>2、一级菜单最多4个汉字，二级菜单最多7个汉字，多出来的部分将会以“...”代替。</span></td></tr>
		<tr><td><span>3、创建自定义菜单后，由于微信客户端缓存，需要24小时微信客户端才会展现出来。测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果。</span></td></tr>
		<tr><td><span>注：以上限制来自<a href="http://mp.weixin.qq.com/wiki/13/43de8269be54a0a6f64413e4dfa94f39.html" target="_blank" style="color:blue;">微信公众平台</a>。</span></td></tr>
	</table>
	<!--Table order list-->
	<table class="tab_in2" width="100%">
		<tr class="ti"><th colspan="2">详细信息</th></tr>
		<tr><th width="15%" style="color:blue; font-weight:bold;">一级菜单AA：</th><td><input class="inpte" style="width:80px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="4"/></td></tr>
		<tr>
			<th style="color:green;">二级菜单AA01：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_aa01" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_aa01"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单AA02：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_aa02" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_aa02"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单AA03：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_aa03" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_aa03"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单AA04：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_aa04" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_aa04"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单AA05：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_aa05" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_aa05"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr><th>&nbsp;</th><td>&nbsp;</td></tr>
		<tr><th style="color:blue; font-weight:bold;">一级菜单BB：</th><td><input class="inpte" style="width:80px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="4"/></td></tr>
		<tr>
			<th style="color:green;">二级菜单BB01：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_bb01" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_bb01"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单BB02：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_bb02" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_bb02"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单BB03：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_bb03" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_bb03"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单BB04：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_bb04" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_bb04"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单BB05：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_bb05" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_bb05"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr><th style="color:green;">&nbsp;</th><td>&nbsp;</td></tr>
		<tr><th style="color:blue; font-weight:bold;">一级菜单CC：</th><td><input class="inpte" style="width:80px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="4"/></td></tr>
		<tr>
			<th style="color:green;">二级菜单CC01：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_cc01" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_cc01"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单CC02：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_cc02" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_cc02"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单CC03：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_cc03" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_cc03"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单CC04：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_cc04" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_cc04"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
		<tr>
			<th style="color:green;">二级菜单CC05：</th>
			<td>
				<input class="inpte" style="width:120px;" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="7"/>
				&nbsp;
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_cc05" checked="checked"/><span class="va_m">文本</span></label>
				<label class="mr_20"><input class="va_m" type="radio" name="inpr1_cc05"/><span class="va_m">网址</span></label>
				&nbsp;
				<input class="inpte" type="text" id="appAESKey" name="appAESKey" value="${userInfo.appAESKey}" maxlength="600"/>
			</td>
		</tr>
	</table>
	<!--/Table order list-->
	<table class="tab_head tab_in tab_list2" width="100%">
		<tr class="ti"><th colspan="3">操作</th></tr>
		<tr><td class="txt_l"><a class="btn_g" href="javascript:submit();">保存并发布</a></td></tr>
	</table>
</div>
<!--/Content-->

<jsp:include page="/footer.jsp"/>