Views.testView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'test'
    },

    willShow: function() {
    	// alert("willShow");
    },

    didShow: function() {
        // alert("didShow");
    },

    myFunction: function(me) {
        var a =$(me).attr('data-val');
        alert(a);
    },

    myFunction2: function(btnId) {
        alert("ffffffffffff2222222");
    }
});

function myFunction() {
    alert("ffffffffffff");
}

 function triggerCallback(callbackName, btnId) {
                // if (APPVERSION === 'web') {
                    Callback[callbackName](btnId);  // let exception throw
            //     } else {
            //         try { Callback[callbackName](btnId); } catch (ignore) {}
            //     }
            };

Views.test2View =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'test2'
    }
});

Views.test3View =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'test3'
    },

    willShow: function() {
        // alert("willShow");
    },

    didShow: function() {
        // alert("didShow");
        // cutPic();
         Views.upHead.initImgSrc('uploadphoto', true);
    },

});
/******************************切割图片*************************************/
// function rotate(angle) {
//     $image.cropper('rotate', angle)
// }

// function cutPic() {
//     $image = $('#img_result');

//     var options = {
//         aspectRatio: 750 / 650,
//         resizable: false,
//         dragCrop: false,
//         movable: false,
//         // crop: function (e) {
//         //  console.log("===============");
//         //  console.log(e);
//         //  console.log("===============");
//         // }
//       };

//   $image.on().cropper(options);
// }

// function cancelCutImg() {

// }

// function sureCutImg() {
//     var $this = $(this);
//     var data = $this.data();
//     var $target;
//     var result;

//     if ($this.prop('disabled') || $this.hasClass('disabled')) {
//       return;
//     }

//     if ($('#img_result').data('cropper')) {
//       data = $.extend({}, data);
//       if (typeof data.target !== 'undefined') {
//         $target = $(data.target);

//         if (typeof data.option === 'undefined') {
//           try {
//             data.option = JSON.parse($target.val());
//           } catch (e) {
//             console.log(e.message);
//           }
//         }
//       }

//       result = $image.cropper('getCroppedCanvas',  {width: 750,height: 650}, data.secondOption);

//       if (result) {
//             // var picPath = result.toDataURL('image/jpeg');
//             $('#testImg').attr('src',result.toDataURL('image/jpeg'));
//             // $('#showDivAll').hide();
//         }
//     }
// }

/*******************************************************************/
function firstTest() {
	Views.testView.show({
		list: ['文艺1', '博客2', '摄影3', '电影4', '民谣5', '旅行6', '吉他7']
	});
}

function secondTest() {
	Views.test2View.show({
		list: ['文艺1a', '博客2a', '摄影3a', '电影4a', '民谣5a', '旅行6a', '吉他7a']
	});
}

function thirdTest() {
    // Views.test3View.show();
    // dialogChangeInfo();
    Views.changeInfo.show({
                            title:'标题1',
                            type:'text',
                            hit: '请输入内容1',
                            cancelBtn: '取消1',
                            sureBtn: '确定1'
                        },function(value){
                            alert(value);
                        },function(value){
                            alert(value);
                        });

}

// $(function() {
//     changeHead();
// })

// Views.upHead.initImgSrc('uploadphoto');

function changeHead() {
    // Views.upHead.show({},
    //     function(path) {
    //         $('#testImg').attr('src',path);
    // });

    
    Views.upHead.show({url: WEB_URL + "test"},
        function(path) {
            $('#testImg').attr('src',path);
    });
    Views.upHead.initImgSrc('uploadphoto');

    // var path = Views.upHead.show();

    // $('#testImg').attr('src',path);


   
}
// $(document).ready(function(){
 
// });




Views.upImgTestView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'upImgTest'
    },

    willShow: function() {
        // alert("willShow");
    },

    didShow: function() {
        // alert("didShow");
        Views.upImg.show(
            {url: WEB_URL + "coreAttachment/upload/head/stream",param:{cratmDir:"oa/head",fileName:"aa.png",}},
            function(data) {
                alert(data);
            }
            );
    }
});