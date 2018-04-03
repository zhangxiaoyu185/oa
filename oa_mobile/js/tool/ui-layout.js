Views.PanelView = {
	options: {
		tmpl: '',
		id: '',  // 为了统计需要，不输入id的话，默认跟tmpl相同  未做
		name: 'PanelView基类'  // 为了统计需要，没有name不计入统计  未做
	},

	initParam: function(initParam){//下个版本  独立出ajax请求  返回时  直接拿ajax结果去渲染模版  不再经过ajax
		this.initParam = initParam;
	},
	//下个版本 传回initParam
	willShow: function(param, isBackPage) {
		this.show(param, isBackPage);
	},
	
	show: function(param, isBackPage) {
		Util.log('Rendering panel view using tmpl: ' + this.options.tmpl + '.html');
		var self = this;
		// self.willShow();

		if(self.myScroll) {
			self.myScroll.destroy();
			self.myScroll = null;
		}
		self.addEventListener();

		var url = "";
		var title = "";
		var urlTitle = Views.tmpl[self.options.tmpl].split(':');
		if(urlTitle.length >=2){
			url = urlTitle[0];
			title = urlTitle[1];
			if(urlTitle[2]) {
				$("body").css("background", urlTitle[2]);
			}

		}else {
			url = urlTitle[0];
		}

		$.ajax({
			url: url,
			type: 'POST',
			data: {
				rtime: Util.rtime()
			},
			dataType: 'text',
			success: function(sourceData) {
				var render  = template.compile(sourceData);
				var html = render(param);
				var content = document.getElementById('mainStage');
				content.innerHTML = html;
				if(title) {
					$("#title").html(title);
				}
				var baseUrl = window.location.href.split('?');
				baseUrl = baseUrl.length==2 ? baseUrl[0] : baseUrl;
				// changeURL({title:title, content:content.innerHTML, options: options?options:self.options, param: param}, baseUrl + "?rtime=" + Util.rtime());
				if(!isBackPage) {
					changeURL({title:title, content:content.innerHTML, options: self.options, param: param}, baseUrl + "?rtime=" + Util.rtime());
				}
				
				
				self.didShow();
				//检查是否分页页面  要是分页初始化Icroll控件
				if($('#wrapper').length>0){
					self.scrollLoad();
				}

			},
			error: function() {
				Util.log('未找到模板文件' + Views.tmpl[self.options.tmpl]);
				self.didShow();
			}
		});
	},

	clearContent: function() {
		$('#mainStage').html('');
	},

	addEventListener: function() {
		//绑定页面中的点击事件
		var self = this;
        // var btns = $('#mainStage').find('.ui_btn');
        // for(var i=0; i<btns.length; i++) {
       	// 	btns[i].addEventListener('click',function() {
       	// 		var actionName = $(this).attr('data-action');
       	// 		if(self[actionName]){
       	// 			self[actionName](this);
       	// 		}else{
		      //  		console.log(actionName?"事件" + actionName + "未定义": "没有声明事件");
		      //  	}
	       //  },false);	
        // }

   		// $('.ui_btn').live('click',function() {
   		// 	var actionName = $(this).attr('data-action');
   		// 	if(self[actionName]){
   		// 		self[actionName](this);
   		// 	}else{
	    //    		console.log(actionName?"事件" + actionName + "未定义": "没有声明事件");
	    //    	}
    	//  });	
		$('#mainStage').off('click',".ui_btn"); 
        $('#mainStage').on('click', '.ui_btn', function() {
   			var actionName = $(this).attr('data-action');
   			if(self[actionName]){
   				self[actionName](this);
   			}else{
	       		console.log(actionName?"事件" + actionName + "未定义": "没有声明事件");
	       	}
        });	
	},
	
	didShow: function() {

	},

	willHide: function() {

	},

	hide: function() {

	},

	didHide: function() {

	},

	scrollLoad: function() {
		// this.myScroll,
		var self = this,
		upIcon = $("#up-icon"),
		downIcon = $("#down-icon");

		self.scrollInitRender();
		
		
		self.myScroll = new IScroll('#wrapper', { 
			mouseWheel: true,//侦听鼠标滚轮事件
    		scrollbars: false,
   			momentum: true,//在用户快速触摸屏幕时，你可以开/关势能动画。关闭此功能将大幅度提升性能。
			preventDefault:true, //preventDefault 事件冒泡设置
			probeType: 3, //probeType：1  滚动不繁忙的时候触发 2  滚动时每隔一定时间触发 3  每滚动一像素触发一次
			click: self.iScrollClick()
		});
		self.myScroll.on("scroll",function(){
			var y = this.y,
				maxY = this.maxScrollY - y,
				downHasClass = downIcon.hasClass("reverse_icon"),
				upHasClass = upIcon.hasClass("reverse_icon");
			
			if(y >= 40){
				!downHasClass && downIcon.addClass("reverse_icon");
				return "";
			}else if(y < 40 && y > 0){
				downHasClass && downIcon.removeClass("reverse_icon");
				return "";
			}
			
			if(maxY >= 40){
				!upHasClass && upIcon.addClass("reverse_icon");
				return "";
			}else if(maxY < 40 && maxY >=0){
				upHasClass && upIcon.removeClass("reverse_icon");
				return "";
			}
		});
		
		self.myScroll.on("slideDown",function(){
			if(this.y > 40){
				self.scrollDownRender();
				upIcon.removeClass("reverse_icon")
				// self.myScroll.refresh();
			}
		});
		
		self.myScroll.on("slideUp",function(){
			if(this.maxScrollY - this.y > 40){
				self.scrollUpRender();
				upIcon.removeClass("reverse_icon");
				// self.myScroll.refresh();
			}
		});

		// myScroll.on("refresh", function() {
		//     // this.scrollRefresh();

		// });
	},

	iScrollClick: function() {
		if (/iPhone|iPad|iPod|Macintosh/i.test(navigator.userAgent)) return false;
		if (/Chrome/i.test(navigator.userAgent)) return (/Android/i.test(navigator.userAgent));
		if (/Silk/i.test(navigator.userAgent)) return false;
		if (/Android/i.test(navigator.userAgent)) {
		   var s=navigator.userAgent.substr(navigator.userAgent.indexOf('Android')+8,3);
		   return parseFloat(s[0]+s[3]) < 44 ? false : true
	    }
	},

	scrollReadItem: function(tmplStr, param) {
		var url = "";
		var title = "";
		var urlTitle = Views.tmpl[tmplStr].split(':');
		if(urlTitle.length==2){
			url = urlTitle[0];
			title = urlTitle[1];
		}else {
			url = urlTitle[0];
		}
		var htmlStr = "";
		$.ajax({
			url: url,
			type: 'POST',
			async: false,
			// data: {
			// 	rtime: Util.rtime()
			// },
			dataType: 'text',
			success: function(sourceData) {
				var render  = template.compile(sourceData);
				var html = render(param);
				htmlStr = html;
			},
			error: function() {
				Util.log('未找到模板文件' + Views.tmpl[tmplStr]);
				htmlStr = '未找到模板文件' + Views.tmpl[tmplStr];
			}
		});

		return htmlStr;
	},

	scrollInit: function(tmplName, sourceData) {
		var li = $('#scroller').find('#scroller-content').find('ul').find('.notRefresh');
		$('#scroller').find('#scroller-content').find('ul').html('');
		$('#scroller').find('#scroller-content').find('ul').append(li);
		
		var listContent = this.scrollReadItem(tmplName, sourceData);
		$('#scroller').find('#scroller-content').find('ul').append(listContent);
		this.refreshScrollHeight(); 
	},

	scrollDown: function(tmplName, sourceData) {
		var li = $('#scroller').find('#scroller-content').find('ul').find('.notRefresh');
		$('#scroller').find('#scroller-content').find('ul').html('');
		$('#scroller').find('#scroller-content').find('ul').append(li);

		var listContent = this.scrollReadItem(tmplName, sourceData);
		$('#scroller').find('#scroller-content').find('ul').append(listContent);
		this.refreshScrollHeight();
	},

	scrollUp: function(tmplName, sourceData) {
		var listContent = this.scrollReadItem(tmplName, sourceData);
		$('#scroller').find('#scroller-content').find('ul').append(listContent);
		this.refreshScrollHeight();
	},

	refreshScrollHeight: function() {
		this.myScroll.refresh();
		var wrapperHeight = $('#wrapper').height();
		var scrollerHeight = $('#scroller').height();
		if(scrollerHeight<wrapperHeight) {
			$('#scroller-pullUp').remove();
		}
	},

	scrollInitRender: function() {
		// this.scrollInit('listTestItem',{list:[1,2,3,4,5]});//测试
	},

	scrollDownRender: function() {
		// this.scrollDown('listTestItem',{list:[1,2,3]});//测试
	},

	scrollUpRender: function() {
		// this.scrollUp('listTestItem',{list:[1,2,3,4,5]});//测试  加载下一页
	},

	goBack: function() {
		if(self.myScroll) {
			self.myScroll.destroy();
			self.myScroll = null;
		}
		window.history.go(-1);
	}

};

//ajax页面无刷新 记录历史页面
function changeURL(stateJson,url){
	var content = document.getElementById('mainStage');
	window.history.pushState(stateJson,0,url); 
	// console.log(stateJson);  

	window.onpopstate = function(){
	  // content.innerHTML = history.state.content;
	  // if(history.state.title) {
	  // 	$("#title").html(history.state.title);
	  // }

	  // Views.PanelView.show(history.state.param, history.state.options);

	  Views[history.state.options.tmpl + "View"].willShow(history.state.param||{}, true);
	  // history.state.self.show();
	}
}