<%@ page pageEncoding="UTF-8"%>

<script src="<%=request.getContextPath()%>/js/jquery-1.11.3.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>

<script>
$(function(){
	$.post("${pageContext.request.contextPath}/weixin/helper/sign/jssdk",
		{appid:"${param.appid}", appsecret:"${param.appsecret}", url:window.location.href.split("#")[0]},
		function(data){
			wx.config({
				debug: false,
				appId: "${param.appid}",
				timestamp: data.timestamp,
				nonceStr: data.noncestr,
				signature: data.signature,
				jsApiList: ["chooseImage", "previewImage", "uploadImage", "downloadImage"]
			});
		}
	);
});
wx.error(function(res){
	//wx.config失败时会在这里打印错误信息
	alert(res.errMsg);
});
var images = {
	localId: [],
	serverId: []
};
wx.ready(function(){
	wx.checkJsApi({
		jsApiList: ["chooseImage", "previewImage", "uploadImage", "downloadImage"],
		success: function(res){
			alert(JSON.stringify(res));
		}
	});
	document.querySelector("#chooseImage").onclick = function(){
		wx.chooseImage({
			sizeType: ["compressed"],
			sourceType: ["camera"],
			success: function(res){
				images.localId = res.localIds;
				alert("已选择" + res.localIds.length + "张图片");
				$("#testimg").attr("src", images.localId);
			}
		});
	};
	document.querySelector("#uploadImage").onclick = function(){
		if(images.localId.length == 0){
			alert("请先拍照");
			return;
		}
		var i = 0;
		var length = images.localId.length;
		images.serverId = [];
		function upload(){
			wx.uploadImage({
				localId: images.localId[i],
				success: function(res){
					i++;
					//alert("已上传：" + i + "/" + length);
					images.serverId.push(res.serverId);
					$("#testid").val(images.serverId);
					if(i < length){
						upload();
					}
				},
				fail: function(res){
					alert(JSON.stringify(res));
				}
			});
		}
		upload();
	};
});
/*
document.querySelector('#downloadImage').onclick = function () {
  if (images.serverId.length === 0) {
    alert('请先使用 uploadImage 上传图片');
    return;
  }
  var i = 0, length = images.serverId.length;
  images.localId = [];
  function download() {
    wx.downloadImage({
      serverId: images.serverId[i],
      success: function (res) {
        i++;
        alert('已下载：' + i + '/' + length);
        images.localId.push(res.localId);
        if (i < length) {
          download();
        }
      }
    });
  }
  download();
};
*/
</script>