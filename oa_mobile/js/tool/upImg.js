/******************************上传头像附件************************************/
//使用var upHead = Views.upHead.init('uploadphoto');
//upHead.show({
//url: WEB_URL + "coreAttachment/upload/head/stream",param:{cratmDir:"oa/head",fileName:"head.png",}},
//function(returnImagData, ajaxResult) {doSomething})
Views.upHead = {
	param: {
		note: '图片质量太高可能会加载缓慢，请耐心等待',
		width: 750,
		height: 650,
		angle: 90,
		quality: 0.8,//quality压缩图片的质量 默认值
		url: '',//没有写url就代表不用上传图片
		param: {}
	},

	show: function(opt, sureCallback){
		this.TemPparam =  $.extend(false, this.param, opt); 
		var htmlStr = 	'<div id="showDivAll">'+
							'<div id="showDiv" class="cutImg_div_shade" ></div>'+
							'<div class="cutImg_content">'+
								'<div class="cutImg_top">'+
								    '<div class="img-container">'+
								    	'<img id="img_result"  src="" alt="Picture">'+
								    '</div>'+
								    '<div style="text-align: center;" class="span_red">'+this.TemPparam.note+'</div>'+
							    '</div>'+
							    '<div class="cutImg_bottom">'+
							    	'<div class="cutImg_rotate_btn">'+
								    	'<div id="rotate_right" class="rotate_right" data-angle="'+this.TemPparam.angle+'"></div>'+
								    	'<div id="rotate_left" class="rotate_left" data-angle="-'+this.TemPparam.angle+'"></div>'+
							    	'</div>'+
								    '<div class="cutImg_bottom_btn">'+
								    	'<div class="item">'+
									    	'<div id="leftBtn" class="cutImg_btn">取消</div>'+
									    '</div>'+
									    '<div class="item">'+
									    	'<div id="rightBtn" class="cutImg_btn">确定</div>'+
									    '</div>'+
								    '</div>'+
							    '</div>'+
							'</div>'+
						'</div>';

		$('#mainStage').append(htmlStr);

		this.initCropper();
		var self = this;
		$('#leftBtn').on('click',function(){
		 	self.hide();
		});
		$('#rightBtn').on('click',function(allowEmpty){
			var returnImgData = self.sureCutImg();
			if(!self.TemPparam.url) {//没有写url就代表不用上传图片
				if(sureCallback){
			 		sureCallback(returnImgData);
			 	}	
			 	self.hide();
			}else {
				self.upPic(returnImgData, function(ajaxresult){
					if(sureCallback){
						sureCallback(returnImgData, ajaxresult);
					}
					// self.hide();
				})
			}
		 	
		});

		
	},

	init: function(btnId) {
		var self = this;
		$('#'+btnId).localResizeIMG({
	    	quality: 0.8,
		    success: function (result) {
				self.$image.cropper('reset').cropper('replace', result.base64);
		    },
		    error:function(XMLHttpRequest, textStatus, errorThrown){ //上传失败 
               alert(XMLHttpRequest.status);
               alert(XMLHttpRequest.readyState);
               alert(textStatus);
            }
	    });

	    return this;
	},

	sureCutImg: function() {
	    var $this = $(this);
	    var data = $this.data();
	    var $target;
	    var result;

	    if ($this.prop('disabled') || $this.hasClass('disabled')) {
	      return;
	    }

	    if ($('#img_result').data('cropper')) {
	      data = $.extend({}, data);
	      if (typeof data.target !== 'undefined') {
	        $target = $(data.target);

	        if (typeof data.option === 'undefined') {
	          try {
	            data.option = JSON.parse($target.val());
	          } catch (e) {
	            console.log(e.message);
	          }
	        }
	      }

	      result = this.$image.cropper('getCroppedCanvas',  {width: this.TemPparam.width, height: this.TemPparam.height}, data.secondOption);

	      if (result) {
	            var picPath = result.toDataURL('image/jpeg');
	            return picPath;
	        }
	    }
	},

	upPic: function(picPath, callBack) {
		var self = this;
		self.TemPparam.param.imgData = picPath;

		layer.open({shadeClose: false,type: 2});
		$.ajax({
		   	type: "POST",
		   	url: self.TemPparam.url,
		   	data: self.TemPparam.param,
		   	dataType:"json",
		   	success: function(data){
		   		layer.closeAll();
			   	if(!data.success) {
					alert(data.errMsg);
				}else{
					if(typeof(callBack) == 'function'){
					   callBack(data);
					}
				}
		   	}, 
			complete :function(XMLHttpRequest, textStatus){
				layer.closeAll();
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){ //上传失败 
			   alert(textStatus);
			   layer.closeAll();
			}
		});
	},

	hide: function() {
		$('#showDivAll').remove();
	},

	initCropper: function() {
		var self = this;

		this.$image = $('#img_result');
	    var options = {
	        aspectRatio: self.TemPparam.width / self.TemPparam.height,
	        resizable: false,
	        dragCrop: false,
	        movable: false,
	        // crop: function (e) {
	        //  console.log("===============");
	        //  console.log(e);
	        //  console.log("===============");
	        // }
	      };

	  	this.$image.on().cropper(options);

	  	$('#rotate_right').on('click',function(){
			var angle = $(this).attr('data-angle');
			var angle = $(this).attr('data-angle');
			self.rotate(angle);
		});

		$('#rotate_left').on('click',function(){
			var angle = $(this).attr('data-angle');
			self.rotate(angle);
		});
	},

	rotate: function(angle) {
		this.$image.cropper('rotate', angle);
	}
}

/******************************上传图片附件************************************/
//使用
//Views.upImg.show(
//	{url: WEB_URL + "coreAttachment/upload/head/stream",param:{cratmDir:"oa/head",fileName:"aa.png",}},
//    function(data,imgData) {//data上传图片后返回的结果信息,上传成功的图片
//        doSomething
//    },function() {
//			doComplete//全部上传成功后
//	  }
//);
Views.upImg = {
	param: {
		url: '',//没有写url就代表不用上传图片
		param: {},
		quality: 0.8,//quality压缩图片的质量 默认值
	},

	show: function(opt, sureCallback){
		document.documentElement.style.overflow = "hidden";
		this.TemPparam =  $.extend(false, this.param, opt); 
		var self = this;
		var htmlStr = 	'<div id="showDivAll" class="upImg_bg">'+
							'<div class="comm_title">上传图片</div>'+
							'<div id="goBack" class="comm_goBack ui_btn"></div>'+
							'<div id="img_border" class="upImg_border">'+
								'<div id="imgDispalyDiv"></div>'+
							'</div>'+
							'<div class="upImg_btn_border">'+
								'<label class="upImg_btn_item" for="xFile">新增</label>'+
								'<input id="xFile" type="file" style="position:absolute;clip:rect(0 0 0 0);" enctype="multipart/form-data">'+
								'<div id="sureUpImg" class="upImg_btn_item sureUp">上传</div>'+
							'</div>'+
						'</div>';
		$('#mainStage').append(htmlStr);
		this.initImgSrc('xFile');

		$('#sureUpImg').click(function() {
			self.upPic(sureCallback);
		});

		$('#goBack').click(function() {
			self.hide();
		});

		$('#showDivAll').off('click',".upImg_preview"); 
		$('#showDivAll').on('click', '.upImg_preview', function() {
			self.imgPreview(this);
		})
	},

	imgPreview: function(me) {
	  var path = $(me).attr('src');
	  var content = '<img class="img_preview" src="'+path+'"  style="width: 100%;"/>'+
			        '<div class="upImg_file_delete" onclick="layer.closeAll()" style="position: absolute;top: 0px;right: 0px;"></div>';
	  layer.open({
	    type: 1,
	    title: false,
	    closeBtn: 0,
	    area: '516px',
	    skin: 'layui-layer-nobg', //没有背景色
	    shadeClose: true,
	    content: content
	  });
	},

	fileDelete: function(me) {
	  $(me).parent().parent().remove();
	},

	selectImg: function(imgSrc) {
		var fielImtem = 	'<div class="upImg_item">'+
			                    '<img class="upImg_preview btn" src="'+imgSrc+'"/>'+
			                    '<div class="upImg_name"></div>'+
			                    '<div class="upImg_top_bar">'+
			                      '<div class="upImg_file_delete" onclick="Views.upImg.fileDelete(this)"></div>'+
			                    '</div>'+
			                  '</div>'; 
		  $("#imgDispalyDiv").append(fielImtem);
		  $("#sureUpImg").show();
	},

	initImgSrc: function(btnId, isInit) {
		var self = this;
		$('#'+btnId).localResizeIMG({
	    	quality: self.TemPparam.quality,
		    success: function (result) {
  				self.selectImg(result.base64);
		    },
		    error:function(XMLHttpRequest, textStatus, errorThrown){ //上传失败 
               alert(XMLHttpRequest.status);
               alert(XMLHttpRequest.readyState);
               alert(textStatus);
            }
	    });
	},

	upPic: function(callBack, complete) {
		var self = this;
		var imgs = $('#imgDispalyDiv img');
		for (var i=0; i<imgs.length; i++ ) {
			var imgData = $(imgs[i]).attr('src');
			self.TemPparam.param.imgData = imgData;
			layer.open({shadeClose: false,type: 2});
			$.ajax({
			   	type: "POST",
			   	url: self.TemPparam.url,
			   	data: self.TemPparam.param,
			   	async: false, 
			   	dataType:"json",
			   	success: function(data){
			   		layer.closeAll();
				   	if(!data.success) {
						$(imgs[i]).parent().find('.upImg_name').html("上传失败,请重新上传");	
					}else{
						$(imgs[i]).parent().remove();
						if(typeof(callBack) == 'function'){
							callBack(data, imgData);
						} 
						if($('#imgDispalyDiv img').length == 0){
							if(typeof(complete) == 'function'){
								complete();
							}else{
								self.hide();
							}	
						}
					}
			   	}, 
				complete :function(XMLHttpRequest, textStatus){
					layer.closeAll();
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){ //上传失败 
					$(imgs[i]).parent().find('.upImg_name').html("上传失败,请重新上传");
				   	layer.closeAll();
				}
			});
		}	
	},

	hide: function() {
		document.documentElement.style.overflow = "scroll";
		$('#showDivAll').remove();
	}
}