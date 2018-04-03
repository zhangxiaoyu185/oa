/************************创建投资客********************************/
Views.createInvestorView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'createInvestor'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {
        this.initDateWidget();
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
                    self.birth = valueText;
                    return true;
                }
            });
        }
        fun();
    },

    changeSexLayer: function() {
        var self = this;
        Views.changeSex.show(
            function(result) {//result=='男'?1:2//1男2女3其它
                $('#sex').html(result);
                self.sex = (result=='男'?1:2);
            }
        );
    },

    sure_addInvestor: function() {
        var self = this;
        if(!$('#crusrName').val()){alert("用户账号不能为空"); return;}
        if(!$('#crusrCode').val()){alert("用户姓名不能为空"); return;}
        if($('#crusrPassword').val().length<6){alert("密码不能少于6位"); return;}
        if($('#crusrPassword').val()!=$('#confirmPwd').val()){alert("两次输入密码不一致"); return;}
        // if(!$('#crusrLoan').val()){alert("借出金额不能为空"); return;}
        // if(!$('#crusrMoney').val()){alert("余额不能为空"); return;}
        if(!$('#crusrMobile').val()){alert("联系方式不能为空"); return;}
        if(!Validator.stringIsMobileNo($('#crusrMobile').val())){alert("请输入正确的手机号");return;}
        // if(!$('#crusrAddress').val()){alert("地址不能为空"); return;}

        if(!$('#crusrCopper').val()){alert("铜宝宝不能为空"); return;}
        if(!$('#crusrSilver').val()){alert("银宝宝不能为空"); return;}
        if(!$('#crusrGold').val()){alert("金宝宝不能为空"); return;}
        if(!$('#crusrYestIncome').val()){alert("昨日收益不能为空"); return;}

        var data = {
            crusrName: $('#crusrName').val(),
            crusrCode: $('#crusrCode').val(),
            crusrPassword: hex_md5($('#crusrPassword').val()),
            confirmPwd: hex_md5($('#confirmPwd').val()),
            crusrJob: 3,
            // crusrCompany: $('#').val(),
            // crusrMoney: $('#crusrMoney').val(),
            // crusrLoan: $('#crusrLoan').val(),
            // crusrProject: $('#').val(),
            // crusrEmail: $('#crusrEmail').val(),
            crusrMobile: $('#crusrMobile').val(),
            crusrBirthday: self.birth,
            crusrGender: self.sex,
            // crusrQq: $('#').val(),
            crusrAddress: $('#crusrAddress').val(),
            // crusrRemarks: $('#crusrRemarks').val(),
            loginUser: JdataGet('user').crusrUuid,
            crusrCopper: $('#crusrCopper').val(),
            crusrSilver: $('#crusrSilver').val(),
            crusrGold: $('#crusrGold').val(),
            crusrYestIncome: $('#crusrYestIncome').val(),
        };

        var url = WEB_URL + "coreUser/add/coreUser";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                } else {
                    self.goBack();
                }
            },true
        );
    }
});

    // * @param crusrName 登录名
    // * @param crusrCode 昵称姓名
    // * @param crusrPassword 登录密码(MD5)
    // * @param confirmPwd 确认密码(MD5)
    // * @param crusrJob 职位:1超级管理员2集团管理员3投资客4渠道5策划6营销总7项目总8财务
    // * @param crusrCompany 所属集团(集团管理员)
    // * @param crusrMoney 资金额度(超级管理员和投资客有)
    // * @param crusrLoan 已借款(投资客有)
    // * @param crusrProject 所属项目(项目人员)
    // * @param crusrEmail 电子邮件
    // * @param crusrMobile 手机号码
    // * @param crusrBirthday 生日
    // * @param crusrGender 性别:1男2女3其它
    // * @param crusrQq QQ
    // * @param crusrAddress 地址
    // * @param crusrHead 头像路径
    // * @param crusrRemarks 备注
    // * @param loginUser 登录人uuid(用于添加资金日志记录)

/************************投资客列表********************************/
Views.investorListView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'investorList'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {

    },

    scrollInitRender: function() {
        this.pageNum=1;
        this.investorsList("scrollInit", 1, 10);
    },

    scrollDownRender: function() {
        this.pageNum=1;
        this.investorsList("scrollDown", 1, 10);
    },

    scrollUpRender: function() {
        this.pageNum++;
        if(this.pageNum<=this.lastPageNumber){
            this.investorsList("scrollUp", this.pageNum, 10);
        }
    },

    investorsList: function(type,pageNum,pageSize){
        var self = this;
        if(!pageNum){
            pageNum=1;
        }
        if(!pageSize){
            pageSize=10;
        }
        var data = {
            pageNum: pageNum,
            pageSize: pageSize
        };
        var url = WEB_URL + "coreUser/find/coreUserForPagesByInvestors";
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
                        self.scrollInit('investorListItem',{list : data.data.result});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('investorListItem',{list : data.data.result});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('investorListItem',{list : data.data.result});
                    };
                }
            },true
        );
    },

    createInvestor: function() {
        var user =  JdataGet("user");
        Views.createInvestorView.willShow({job: user.crusrJob});
    },

    to_investorDesc: function(btn) {
        var uuid = $(btn).attr('data-uuid');
        dataSave('investorDescUuid', uuid);
        Views.investorDescView.willShow();
    }
});
// crusrAddress:"杭州西湖边"
// crusrCdate:"2016-10-22"
// crusrCode:"investors"
// crusrEmail:"zy135185@163.com"
// crusrGender:1
// crusrJob:3
// crusrLoan:500002.11
// crusrMobile:"15157135185"
// crusrMoney:5000031.11
// crusrName:"investors"
// crusrPassword:"e10adc3949ba59abbe56e057f20f883e"
// crusrQq:"1556377990"
// crusrRemarks:"投资客"
// crusrStatus:1
// crusrUdate:"2016-10-24"
// crusrUuid:"UCCCCC"

/************************投资客详情********************************/
Views.investorDescView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'investorDesc'
    },

    willShow: function(param, isBackPage) {
        var self = this;
        var uuid = dataGet('investorDescUuid');
        var data = {
            crusrUuid: uuid
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
                    JdataSave('investorsUser', data.data);
                    var user =  JdataGet("user");
                    self.show({data: data.data, job: user.crusrJob},isBackPage);
                }
            },true,true
        );
    },

    didShow: function() {
        this.user = JdataGet("investorsUser");
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

    // changeEmail: function() {
    //     var self = this;
    //     Views.changeInfo.show({
    //         title:'修改邮箱地址',
    //         type:'text',
    //         hit: '请输入新的邮箱地址',
    //         content: $('#email').html()
    //     },function(value){
    //         Views.changeInfo.hide();
    //     },function(value){
    //         var param = {
    //             crusrUuid: self.user.crusrUuid,
    //             crusrEmail: value
    //         }
    //         if(!value){
    //             alert("邮箱地址不能为空");
    //             return;
    //         }
    //         self.ajaxChange(param, 'email', value);
    //     });
    // },

    // changeAddress: function() {
    //     var self = this;
    //     Views.changeInfo.show({
    //         title:'修改地址',
    //         type:'textarea',
    //         hit: '请输入新地址',
    //         content: $('#address').html()
    //     },function(value){
    //         Views.changeInfo.hide();
    //     },function(value){
    //         var param = {
    //             crusrUuid: self.user.crusrUuid,
    //             crusrAddress: value
    //         }
    //         if(!value){
    //             alert("地址不能为空");
    //             return;
    //         }
    //         self.ajaxChange(param, 'address', value);
    //     });
    // },

    changeCrusrLoan: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改已借款',
            type:'text',
            hit: '请输入借贷金额',
            content: $('#crusrLoan').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crcpaUser: JdataGet('user').crusrUuid,
                crusrLoan: value,
                crcpaBusi: self.user.crusrUuid,
            }
            if(!value){
                alert("借贷金额不能为空");
                return;
            }
            self.ajaxChangeUserCapital(param, 'crusrLoan', value);
        });
    },

    changeCrusrMoney: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改余额',
            type:'text',
            hit: '请输入余额',
            content: $('#crusrMoney').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crcpaUser: JdataGet('user').crusrUuid,
                crusrMoney: value,
                crcpaBusi: self.user.crusrUuid,
            }
            if(!value){
                alert("余额不能为空");
                return;
            }
            self.ajaxChangeUserCapital(param, 'crusrMoney', value);
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

    ajaxChangeUserCapital: function(param, btnId, value, callBack) {
        var url = WEB_URL + "coreUser/update/userCapital";
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

    delete_investor: function() {
        var self = this;
        var param = {
                crusrUuid: self.user.crusrUuid
            }
        var url = WEB_URL + "coreUser/disable/coreUser";
        ajaxTool("post",param,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + param);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    self.goBack();   
                }
            },true,true
        );
    },

    to_investorProcessList: function() {
        // Views.investorProcessListView.willShow();
        dataSave('processType', 'investor');
        Views.pendingTrialListView.willShow();
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

/************************投资拨款流程列表********************************/
Views.investorProcessListView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'investorProcessList'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {

    },

    scrollInitRender: function() {
        this.pageNum=1;
        this.coreFlowByProjectList("scrollInit", 1, 5);
    },

    scrollDownRender: function() {
        this.pageNum=1;
        this.coreFlowByProjectList("scrollDown", 1, 5);
    },

    scrollUpRender: function() {
        this.pageNum++;
        if(this.pageNum<=this.lastPageNumber){
            this.coreFlowByProjectList("scrollUp", this.pageNum, 5);
        }
    },
    
    coreFlowByProjectList: function(type,pageNum,pageSize){
        var self = this;      
        if(!pageNum){
            pageNum=1;
        }
        if(!pageSize){
            pageSize=5;
        }

        var data = {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            projectName: $('#searchContent').val(),
            crusrUuid: dataGet('investorDescUuid')
        }
        var url = WEB_URL + "coreFlow/flow/list/by/project";

        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    if (type == "scrollInit") {
                        self.scrollInit('investorProcessListItem',{list : data.data.result});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('investorProcessListItem',{list : data.data.result});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('investorProcessListItem',{list : data.data.result});
                    };
                }
            },true
        );
    },

    goSearch: function() {
        this.scrollInitRender();
    },

    toPendingTrialDesc: function(btn) {
        var uuid = $(btn).attr('data-code');
        dataSave('pendingTrialDescUUid', uuid);
        Views.pendingTrialDescView.willShow();
    },

    addProcess: function(btn) {
        var self = this;
        var data = {
            crsptUser: dataGet('investorDescUuid'),
            crsptFlow: $(btn).attr('data-crsptFlow'),
            crsptCompany: $(btn).attr('data-crsptCompany'),
            crsptProject: $(btn).attr('data-crsptProject'),
        }
        var url = WEB_URL + "coreInvestment/add/coreInvestment";

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
    }
});