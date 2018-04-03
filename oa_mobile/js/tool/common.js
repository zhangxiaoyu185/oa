//var urlfile = "http://cdz.hzlingdian.com/shuoshuo/";
// var urlfile = "http://127.0.0.1:8080/shuoshuo/";

var packageName = "oa_";

function dataSave (key, value) {
	localStorage.setItem(packageName + key, value);
}

function JdataSave (key, value) {
	localStorage.setItem(packageName + key, JSON.stringify(value));
};

function dataGet (key) {
	return localStorage.getItem(packageName + key);
}

function JdataGet (key) {
	return JSON.parse(localStorage.getItem(packageName + key));
}

function dataRemove (key) {
	localStorage.removeItem(key);
}

function dataClear (argument) {
	localStorage.clear();
}

function refreshPage() {
	window.location.reload();
};

function backPage() {
	history.back();
}

function backPageAndRef() {
	backGo(-1);
	// location.href=document.referrer;
}

function backGo(index) {
	window.history.go(index);
}

function ajaxTool(type,data,url,error,success,isShadeLoad, isNotAsync) {
	if(isShadeLoad){
		layer.open({shadeClose: false,type: 2});
	}
    if(APPVERSION == 'debug') {
        console.log("ajax请求url：" + url);
        console.log("ajax请求参数");
        console.log(data);
    }

    var isAsync = !isNotAsync;
	$.ajax({
		data: data,
		type: type,
        async: isAsync, 
	    url: url,
		error:function(XMLHttpRequest, textStatus, errorThrown,fnErr){
			if(isShadeLoad){
				layer.closeAll();
			}
		  	var errorno = XMLHttpRequest.readyState;
            var oMessage = {
                "timeout": errorno+"请求超时",
                "error": errorno+"请求超时",
                "notmodified": errorno+"请求超时",
                "parsererror": errorno+"数据格式出错"
            };
            if(fnErr){
                fnErr();
                return;
            }
            if(!textStatus && errorThrown){
                alert(errorThrown);
            }
            if(textStatus){
                switch (textStatus) {
                    case "timeout":
                        alert(oMessage.timeout);
                        break;
                    case "parsererror":
                        alert(oMessage.parsererror);
                        break;
                    default:
                        break;
                }
            }
			error(XMLHttpRequest, textStatus, errorThrown,fnErr);
		},
		success:function(data){
			if(isShadeLoad){
				layer.closeAll();
			}
			success(data);
            if(APPVERSION == 'debug') {
                console.log("ajax返回结果");
                console.log(data);
            }
		}
	});
}

//字符串日期格式yyyy-MM-dd
var getDateFormat = function (str){
	var date = str.substr(0,4)+"-"+str.substr(4,2)+"-"+str.substr(6,2);
	return date;
};
//字符串时间格式 hh:mm:ss
var getTimeFormat = function (str){
	var diff = 6-str.length;
	var time = str;
	for(var j=0;j<diff;j++){
		time = "0"+time;
	}
	time =  time.substr(0,2)+":"+time.substr(2,2)+":"+time.substr(4,2);
	return time;
};
//字符串日期时间格式 yyyy-MM-dd hh:mm:ss
var getDateTimeFormat = function (str){
	var datetime =  str.substr(0,4)+"-"+str.substr(4,2)+"-"+str.substr(6,2)+' '+str.substr(8,2)+":"+str.substr(10,2)+":"+str.substr(12,2);
	return datetime;
};
//验证手机号码
var IsMobile = function (text){
    var _emp = /^\s*|\s*$/g;
    text = text.replace(_emp, "");
    var _d = /^1[3578][01379]\d{8}$/g;
    var _l = /^1[34578][01256]\d{8}$/g;
    var _y = /^(134[012345678]\d{7}|1[34578][012356789]\d{8})$/g;
    if (_d.test(text) || _l.test(text) || _y.test(text)) {
        return 1;
    }
    return 0;
};
//限制输入11位数字
var chekNum = function (obj){
    obj.val(obj.val().replace(/[^\d.]/g,""));
    obj.val(obj.val().replace(/^\./g,""));
    obj.val(obj.val().replace(/\.{2,}/g,"."));
    obj.val(obj.val().replace(".","$#$").replace(/\./g,"").replace("$#$","."));
    if(obj.val().length > 11){
    	obj.val(obj.val().substring(0,11));      
    }
};
/*限制输入字数
 * @minwidth :最小字数;
 * @maxwidth :最大字数;
 * @thistext :所选的输入框;
*/
function limitInput(minwidth,maxwidth,thistext){
	if(thistext.val().length>maxwidth){
		thistext.val(thistext.val().substring(0,maxwidth));
		alert("不能超过"+maxwidth+"个！");
	}
	if(thistext.val().length<minwidth){
		alert("不能少于"+minwidth+"个！");
		thistext.focus();
		return 0;
	}
}
/*限制输入字数
 * @maxwidth :最大字数;
 * @thistext :所选的输入框;
*/
function limitMaxInput(maxwidth,thistext){
	if(thistext.val().length>maxwidth){
		thistext.val(thistext.val().substring(0,maxwidth));
		alert("不能超过"+maxwidth+"个！");
	}
}

//获得url参数
function GetParpam(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

//页面跳转
function nextView(url) {
	location.href=url;
}


// 对Date的扩展，将 Date 转化为指定格式的String   
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
// 例子：   
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
} 


function checked(thisElement,nocheckClass, checkedClass) {
	var element = $(thisElement);
	if(element.hasClass(nocheckClass)) {
		element.removeClass(nocheckClass).addClass(checkedClass);
	}else {
		element.removeClass(checkedClass).addClass(nocheckClass);
	}
}

function radio(thisElement, nocheckClass, checkedClass) {
	var  elementCheck = $('body').find("."+checkedClass);
	elementCheck.removeClass(checkedClass).addClass(nocheckClass);
	 $(thisElement).removeClass(nocheckClass).addClass(checkedClass);
}

function layerCloseAll() {
	layer.closeAll();
}

function timestamp() {
	return "?timestamp=" + (new Date()).valueOf();
}

//用正则,计算时间差, 且精确到秒 type:d,h,m,s
function timeCompare(nowDay1, oldDay2, type){
    var y1, y2, y3, m1, m2, m3, d1, d2, d3, h1, h2, h3, _m1, _m2, _m3, s1, s2, s3;
    var reg = /年|月|日 |\/|:|-| /;
    //dayinfo -  用正则分割
    var DI1 = nowDay1.split(reg);
    var DI2 = oldDay2.split(reg);
    var date1 = new Date(DI1[0], DI1[1], DI1[2], DI1[3], DI1[4], DI1[5]);
    var date2 = new Date(DI2[0], DI2[1], DI2[2], DI2[3], DI2[4], DI2[5]);
    //用距标准时间差来获取相距时间
    var minsec = Date.parse(date1) - Date.parse(date2);
    var timeshort;
    if(type == "s"){
    	timeshort = minsec / 1000;
    }
    if(type == "m"){
    	timeshort = minsec / 1000 / 60;
    }
    if(type == "h"){
    	timeshort = minsec / 1000 / 60 / 60;
    }
    if(type == "d"){
    	timeshort = minsec/ 1000 / 60 / 60 / 24;
    }

    // var days = minsec / 1000 / 60 / 60 / 24; //factor: second / minute / hour / day
   
    return timeshort;
}

//下拉框的值
function selectValue(id) {
	var  myselect=document.getElementById(id);
  	var index=myselect.selectedIndex ;             // selectedIndex代表的是你所选中项的index
  	return myselect.options[index].value;
}

//下拉框的值
function selectText(id) {
	var  myselect=document.getElementById(id);
  	var index=myselect.selectedIndex ;             // selectedIndex代表的是你所选中项的index
  	return myselect.options[index].text;
}