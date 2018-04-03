
function initConfig() {
	// var data = "&cractName="+cractName;
	// var url = urlfile + "coreAccount/getAccountByName";

	
	// ajaxTool("post",data,url,
	// 	function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
	// 		alert("error:" + data);
	// 	},
	// 	function success(data){
	// 		if(!data.success) {
	// 			alert(data.errMsg);
	// 		}else{
	// 			dataSave("cractUuid",data.data);
	// 			loadFont(data.data);
	// 			loadPic(data.data);
	// 		}
	// 	},
	// 	true
	// );


	// 微信配置
	wx.config({
	    debug: false, 
	    appId: "wxd728dc50b5aa8ee9", 
	    timestamp: '1469973321', 
	    nonceStr: '1937462683', 
	    signature: 'ef209fe61abda43b6591750bdd211ebc1bc7afef',
	    jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage'] // 功能列表，我们要使用JS-SDK的什么功能
	});
}

//分享到朋友圈
function shareTimeline(title, link, imgUrl) {
	wx.onMenuShareTimeline({
	    title: title, // 分享标题
	    link: link, // 分享链接
	    imgUrl: imgUrl, // 分享图标
	    success: function () { 
	        // 用户确认分享后执行的回调函数
	        alert("yes");
	    },
	    cancel: function () { 
	        // 用户取消分享后执行的回调函数
	        alert("no");
	    }
	});
}

//分享给朋友
function shareAppMessage(title) {
	wx.onMenuShareAppMessage({
    title: '', // 分享标题
    desc: '', // 分享描述
    link: '', // 分享链接
    imgUrl: '', // 分享图标
    type: '', // 分享类型,music、video或link，不填默认为link
    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
    success: function () { 
        // 用户确认分享后执行的回调函数
    },
    cancel: function () { 
        // 用户取消分享后执行的回调函数
    }
});
}
