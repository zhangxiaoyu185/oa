/************************人员信息********************************/
Views.personInfoView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'personInfo'
    },

    willShow: function(param, isBackPage) {
        var self = this;
        var data = {
            crusrUuid: param
        }
        var url = WEB_URL + "coreUser/views";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                   JdataSave("user", data.data); 
                   self.show({data: data.data},isBackPage);
                }
            },true,true
        );
    },

    didShow: function() {
        this.user = JdataGet("user");
        var self = this;
    	this.initDateWidget();

        if(this.user.crusrHead) {
            var headPath = WEB_URL + "coreAttachment/image/get/" + this.user.crusrHead;
            $('#img_head_pic').css({
                                    'background':'url("'+headPath+'")',
                                    '-webkit-background-size': '46px 46px',
                                    'background-size': '46px 46px'
                                });
        }

        var upHead = Views.upHead.init('uploadphoto');
        $('#img_head_pic').on('change',function() {
            upHead.show(
                {url: WEB_URL + "coreAttachment/upload/head/stream",param:{cratmDir:"oa/head",fileName:"head.png",}},
                function(returnImagData, ajaxResult) {//图片数据， 上传图片后返回的ajax数据
                    $('#img_head_pic').css({
                        'background':'url("'+returnImagData+'")',
                        '-webkit-background-size': '46px 46px',
                        'background-size': '46px 46px'
                    });
                    var param = {
                        crusrUuid: self.user.crusrUuid,
                        crusrHead: ajaxResult.data
                    }
                    self.ajaxChange(param,null,null,function(flag){
                        if(flag) {
                            self.user.crusrHead = ajaxResult.data;
                            JdataSave('user', self.user);
                            Views.upHead.hide();
                        }
                    });   
                }
            );
        })
    },

    initDateWidget: function() {
        var self = this;
        var curr = new Date().getFullYear();
        var fun = function () {
            var test = $('#birth').scroller('destroy').scroller({
                preset: 'date',
                dateFormat : 'yy-mm-dd', // 日期格式 
                dateOrder : 'yymmdd', //面板中日期排列格式 
                lang: 'zh',
                onSelect:function(valueText,inst){
                    var param = {
                        crusrUuid: self.user.crusrUuid,
                        crusrBirthday: valueText
                    }
                    var success;
                    self.ajaxChange(param,'birth', valueText, function(isSuccess){
                        success = isSuccess;
                    });
                    return success;
                }
            });
        }
        // $('.settings select').bind('change', function () {
        //     fun();
        // });
        fun();
    },

    changeSexLayer: function() {
        var self = this;
        Views.changeSex.show(
            function(result) {
                var param = {
                    crusrUuid: self.user.crusrUuid,
                    crusrGender: result=='男'?1:2//1男2女3其它
                }
                self.ajaxChange(param, 'sexValue', result);
            }
        );
    },

    changeName: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改昵称',
            type:'text',
            hit: '请输入新的昵称',
            content: $('#name').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrCode: value
            }
            if(value.length<2){
                alert("昵称不能短于2个字符");
                return;
            }
            self.ajaxChange(param, 'name', value);
        });
    },

    changeNumber: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改手机号',
            type:'text',
            hit: '请输入新的手机号',
            content: $('#mobileNo').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrMobile: value
            }
            if(!value){
                alert("手机号不能为空");
                return;
            }

            if(!Validator.stringIsMobileNo(value)){
                alert("请输入正确的手机号");
                return;
            }
            self.ajaxChange(param, 'mobileNo', value);
        });
    },

    changeEmail: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改邮箱地址',
            type:'text',
            hit: '请输入新的邮箱地址',
            content: $('#email').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrEmail: value
            }
            if(!value){
                alert("邮箱地址不能为空");
                return;
            }
            self.ajaxChange(param, 'email', value);
        });
    },

    changeAddress: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改地址',
            type:'textarea',
            hit: '请输入新地址',
            content: $('#address').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrAddress: value
            }
            if(!value){
                alert("地址不能为空");
                return;
            }
            self.ajaxChange(param, 'address', value);
        });
    },

    // * @param crusrUuid 标识UUID
    // * @param crusrCode 昵称姓名
    // * @param crusrEmail 电子邮件
    // * @param crusrMobile 手机号码
    // * @param crusrBirthday 生日
    // * @param crusrGender 性别:1男2女3其它
    // * @param crusrQq QQ
    // * @param crusrAddress 地址
    // * @param crusrHead 头像路径
    // * @param crusrRemarks 备注

    ajaxChange: function(param, btnId, value, callBack) {
        var url = WEB_URL + "coreUser/update/coreUser";
        ajaxTool("post",param,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + param);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                    if(callBack){
                        callBack(false);
                    } 
                }else{
                   $('#'+btnId).html(value);
                   
                    if(callBack){
                        callBack(true);
                    }else{
                        Views.changeInfo.hide(); 
                    }
                }
            },true,true
        );
    }

});

/************************查看人员信息********************************/
Views.personInfoOnlyView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'personInfoOnly'
    },

    willShow: function(param, isBackPage) {
        var self = this;
        var data = {
            crusrUuid: param
        }
        var url = WEB_URL + "coreUser/views";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                   self.user = data.data;
                   self.show({data: data.data},isBackPage);
                }
            },true,true
        );
    },

    didShow: function() {
        if(this.user.crusrHead) {
            var headPath = WEB_URL + "coreAttachment/image/get/" + this.user.crusrHead;
            $('#img_head_pic').css({
                                    'background':'url("'+headPath+'")',
                                    '-webkit-background-size': '46px 46px',
                                    'background-size': '46px 46px'
                                });
        }       
    }

});

/************************编辑人员信息********************************/
Views.personInfoUpdateView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'personInfoUpdate'
    },

    willShow: function(param, isBackPage) {
        var self = this;
        var data = {
            crusrUuid: param
        }
        var url = WEB_URL + "coreUser/views";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                   self.user = data.data;
                   self.show({data: data.data},isBackPage);
                }
            },true,true
        );
    },

    didShow: function() {
        var self = this;
        this.initDateWidget();

        if(this.user.crusrHead) {
            var headPath = WEB_URL + "coreAttachment/image/get/" + this.user.crusrHead;
            $('#img_head_pic').css({
                                    'background':'url("'+headPath+'")',
                                    '-webkit-background-size': '46px 46px',
                                    'background-size': '46px 46px'
                                });
        }

        var upHead = Views.upHead.init('uploadphoto');
        $('#img_head_pic').on('change',function() {
            upHead.show(
                {url: WEB_URL + "coreAttachment/upload/head/stream",param:{cratmDir:"oa/head",fileName:"head.png",}},
                function(returnImagData, ajaxResult) {//图片数据， 上传图片后返回的ajax数据
                    $('#img_head_pic').css({
                        'background':'url("'+returnImagData+'")',
                        '-webkit-background-size': '46px 46px',
                        'background-size': '46px 46px'
                    });
                    var param = {
                        crusrUuid: self.user.crusrUuid,
                        crusrHead: ajaxResult.data
                    }
                    self.ajaxChange(param,null,null,function(flag){
                        if(flag) {
                            self.user.crusrHead = ajaxResult.data;
                            Views.upHead.hide();
                        }
                    });   
                }
            );
        })
    },

    initDateWidget: function() {
        var self = this;
        var curr = new Date().getFullYear();
        var fun = function () {
            var test = $('#birth').scroller('destroy').scroller({
                preset: 'date',
                dateFormat : 'yy-mm-dd', // 日期格式 
                dateOrder : 'yymmdd', //面板中日期排列格式 
                lang: 'zh',
                onSelect:function(valueText,inst){
                    var param = {
                        crusrUuid: self.user.crusrUuid,
                        crusrBirthday: valueText
                    }
                    var success;
                    self.ajaxChange(param,'birth', valueText, function(isSuccess){
                        success = isSuccess;
                    });
                    return success;
                }
            });
        }
        fun();
    },

    changeSexLayer: function() {
        var self = this;
        Views.changeSex.show(
            function(result) {
                var param = {
                    crusrUuid: self.user.crusrUuid,
                    crusrGender: result=='男'?1:2//1男2女3其它
                }
                self.ajaxChange(param, 'sexValue', result);
            }
        );
    },

    changeName: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改昵称',
            type:'text',
            hit: '请输入新的昵称',
            content: $('#name').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrCode: value
            }
            if(value.length<2){
                alert("昵称不能短于2个字符");
                return;
            }
            self.ajaxChange(param, 'name', value);
        });
    },

    changeNumber: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改手机号',
            type:'text',
            hit: '请输入新的手机号',
            content: $('#mobileNo').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrMobile: value
            }
            if(!value){
                alert("手机号不能为空");
                return;
            }
            self.ajaxChange(param, 'mobileNo', value);
        });
    },

    changeEmail: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改邮箱地址',
            type:'text',
            hit: '请输入新的邮箱地址',
            content: $('#email').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrEmail: value
            }
            if(!value){
                alert("邮箱地址不能为空");
                return;
            }
            self.ajaxChange(param, 'email', value);
        });
    },

    changeAddress: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改地址',
            type:'textarea',
            hit: '请输入新地址',
            content: $('#address').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrAddress: value
            }
            if(!value){
                alert("地址不能为空");
                return;
            }
            self.ajaxChange(param, 'address', value);
        });
    },

    // * @param crusrUuid 标识UUID
    // * @param crusrCode 昵称姓名
    // * @param crusrEmail 电子邮件
    // * @param crusrMobile 手机号码
    // * @param crusrBirthday 生日
    // * @param crusrGender 性别:1男2女3其它
    // * @param crusrQq QQ
    // * @param crusrAddress 地址
    // * @param crusrHead 头像路径
    // * @param crusrRemarks 备注

    ajaxChange: function(param, btnId, value, callBack) {
        var url = WEB_URL + "coreUser/update/coreUser";
        ajaxTool("post",param,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + param);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                    if(callBack){
                        callBack(false);
                    } 
                }else{
                   $('#'+btnId).html(value);
                   
                    if(callBack){
                        callBack(true);
                    }else{
                        Views.changeInfo.hide(); 
                    }
                }
            },true,true
        );
    },

    deletePerson: function(btn) {
        var self = this;
        var data = {
                crusrUuid: self.user.crusrUuid,
            }
        var url = WEB_URL + "coreUserdisable/coreUser";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    self.goBack(); 
                }
            },true
        );
    },

    changeCrusrCopper: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改铜宝宝',
            type:'text',
            hit: '请输入铜宝宝金额',
            content: $('#crusrCopper').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrCopper: value
            }
            if(!value){
                alert("铜宝宝金额不能为空");
                return;
            }
            self.ajaxChangeFee(param, 'crusrCopper', value);
        });
    },

    changeCrusrSilver: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改银宝宝',
            type:'text',
            hit: '请输入银宝宝金额',
            content: $('#crusrSilver').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrSilver: value
            }
            if(!value){
                alert("银宝宝金额不能为空");
                return;
            }
            self.ajaxChangeFee(param, 'crusrSilver', value);
        });
    },

    changeCrusrGold: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改金宝宝',
            type:'text',
            hit: '请输入金宝宝金额',
            content: $('#crusrGold').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrGold: value
            }
            if(!value){
                alert("金宝宝金额不能为空");
                return;
            }
            self.ajaxChangeFee(param, 'crusrGold', value);
        });
    },

    changeCrusrYestIncome: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改昨日收益',
            type:'text',
            hit: '请输入昨日收益金额',
            content: $('#crusrYestIncome').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crusrUuid: self.user.crusrUuid,
                crusrYestIncome: value
            }
            if(!value){
                alert("昨日收益金额不能为空");
                return;
            }
            self.ajaxChangeFee(param, 'crusrYestIncome', value);
        });
    },

    ajaxChangeFee: function(param, btnId, value, callBack) {
        var url = WEB_URL + "coreUser/update/coreUser/fee";
        ajaxTool("post",param,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + param);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                    if(callBack){
                        callBack(false);
                    } 
                }else{
                   $('#'+btnId).html(value);
                   
                    if(callBack){
                        callBack(true);
                    }else{
                        Views.changeInfo.hide(); 
                    }
                }
            },true,true
        );
    },

});

/************************修改密码********************************/
Views.changePasswordView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'changePassword'
    },

    willShow: function(param, isBackPage) {
        this.crusrUuid = param;
        this.show(param, isBackPage);
    },

    didShow: function() {

    },

    suerChangePwd: function() {
        this.updatePwd();
    },

    updatePwd: function() {
        var oldPassword = $('#oldPassword').val();
        var newPassword = $('#newPassword').val();
        var surePassword = $('#surePassword').val();
        if(!oldPassword) {
            alert("请输入原来的密码");
            return;
        }
        if(newPassword.length<6) {
            alert("密码必须大于6个字符");
            return;
        }
        if(newPassword!=surePassword) {
            alert("两次输入的新密码不一致");
            return;
        }


        var data = {
            crusrUuid: this.crusrUuid,
            oldPwd: hex_md5(oldPassword),
            newPwd: hex_md5(newPassword),
            confirmPwd: hex_md5(surePassword),
        }

        var self = this;
        var url = WEB_URL + "coreUser/update/pwd";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    dataSave('account_password', ''); 
                    self.goBack();
                }
            },true
        );
    }
});

/************************人员搜索********************************/           
Views.personSearchListView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'personSearchList'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {

    },

    goSearch: function() {
        this.scrollInitRender();
    },

    scrollInitRender: function() {
        this.pageNum=1;
        this.coreUserForPagesLikes("scrollInit", 1, 20);
    },

    scrollDownRender: function() {
        this.pageNum=1;
        this.coreUserForPagesLikes("scrollDown", 1, 20);
    },

    scrollUpRender: function() {
        this.pageNum++;
        if(this.pageNum<=this.lastPageNumber){
            this.coreUserForPagesLikes("scrollUp", this.pageNum, 20);
        }
    },

    coreUserForPagesLikes: function(type,pageNum,pageSize){
        var self = this;
        if(!pageNum){
            pageNum=1;
        }
        if(!pageSize){
            pageSize=20;
        }
        var data = {
            pageNum: pageNum,
            pageSize: pageSize,
            objectStr: $('#searchContent').val()
        };
        var url = WEB_URL + "coreUser/find/coreUserForPagesLikes";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                } else {
                    self.lastPageNumber = data.data.lastPageNumber;
                    if (type == "scrollInit") {
                        self.scrollInit('personSearchListItem',{list : data.data.result});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('personSearchListItem',{list : data.data.result});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('personSearchListItem',{list : data.data.result});
                    };
                }
            },true
        );
    },

    to_personUpdate: function(btn) {
        var uuid = $(btn).attr('data-uuid');
        Views.personInfoUpdateView.willShow(uuid);
    }
});