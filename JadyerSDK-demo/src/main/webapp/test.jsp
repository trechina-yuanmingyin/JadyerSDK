<%@page import="com.jadyer.sdk.mpp.util.TokenHolder"%>
<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>测试JSSDK</title>
</head>
<jsp:include page="/jssdk.jsp?appid=wx63ae5326e400cca2&appsecret=b6a838ea12d6175c00793503500ede64"/>
<%
out.println(TokenHolder.getWeixinAccessToken("wx63ae5326e400cca2", "b6a838ea12d6175c00793503500ede64"));
out.print(TokenHolder.getWeixinJSApiTicket(TokenHolder.getWeixinAccessToken("wx63ae5326e400cca2", "b6a838ea12d6175c00793503500ede64")));
%>
<body>
<span>拍照或从手机相册中选图接口</span>
<br/>
<button id="chooseImage">我要拍照</button>
<br/>
<br/>
<button id="uploadImage">我要上传</button>
<input type="text" id="testid">
<br/>
<br/>
<img id="testimg" src="" width="100px" height="100px">
</body>
</html>