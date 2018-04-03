Views.changeInfo = {
	param: {
		title:'标题',
		type:'text',
		hit: '请输入内容',
		leftBtn: '取消',
		rightBtn: '确定',
		content: ''
	},

	show: function(opt, leftCallback, rightCallback){
		var temParam =  $.extend(false, this.param, opt); 

		var typeStr = ""
		var	self = this;
		if(temParam.type == 'text'|| temParam.type == 'number') {
			typeStr = '<input id="result" type="'+temParam.type+'" class="input"  placeholder="'+ temParam.hit +'" value="'+temParam.content+'"/>'
		}else {
			typeStr = '<textarea id="result" class="textarea" placeholder="'+ temParam.hit +'">'+temParam.content+'</textarea>'
		}

		var htmlStr = 	'<div id="dialog_shadow_bg" class="dialog_shadow_bg">'+	
						'</div>'+
						'<div id="dialog_border" class="dialog_border">'+
							'<div class="title">'+ temParam.title +'</div>'+
							typeStr +
							'<div class="bottom">'+
								'<div id="leftBtn" class="item left">'+ temParam.leftBtn +'</div>'+
								'<div id="rightBtn" class="item right">'+ temParam.rightBtn +'</div>'+
							'</div>'+
						'</div>';

		$('#mainStage').append(htmlStr);
		
		$('#leftBtn').on('click',function(){
			var returnValue = $('#result').val();
		 	// if(leftCallback){
		 		leftCallback(returnValue);
		 	// }else{
		 	// 	self.leftCallback(returnValue);
		 	// }
		 	// self.hide();
		});
		$('#rightBtn').on('click',function(allowEmpty){
			var returnValue = $('#result').val();
			// if(!allowEmpty && !returnValue) {
			// 	alert("输入内容不能为空");
			// 	return;
			// }

		 	// if(rightCallback){
		 		rightCallback(returnValue);
		 	// }else{
		 	// 	self.rightCallback(returnValue);
		 	// }	
		 	// self.hide();
		});
	},

	// leftCallback: function() {
	// },

	// rightCallback: function(returnValue) {
	// 	console.log(returnValue);
	// 	return returnValue;
	// },

	hide: function() {
		$('#dialog_shadow_bg').remove();
		$('#dialog_border').remove();
	}
}

Views.changeSex = {
	show: function(callback) {
		var test = '<div id="sexChange">'+
                '<div class="div-sex-border-top">'+
                    '<div class="div-item-sex" data-lity-close="" data-sex="1">男</div>'+
                    '<div class="div-item-sex div-top-border" data-lity-close="" data-sex="0">女</div>'+
                '</div>'+
                '<div class="sex-opacity"></div>'+
                '<button type="button"  id="cancelSex" class="div-sex-border-down div-item-sex">取消</button>'+
            '</div>';

        layer.open({
            content: test,
            style: 'width: 100%;background: transparent;'
        });

        $('.div-item-sex').on('click',function(){
        	var sex = $(this).attr('data-sex');
            var sexStr = sex==1?"男":"女";
            callback(sexStr);
            layer.closeAll();
        }); 

        $('#cancelSex').on('click',function(){
            layer.closeAll();
        });
	}
}
