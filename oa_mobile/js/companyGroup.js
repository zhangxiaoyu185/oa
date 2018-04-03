/************************创建集团********************************/
Views.createCompanyView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'createCompany'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {

    },

    saveCompany: function() {
    	var crgroName = $('#crgroName').val();
        if(!crgroName){
            alert("请输入集团名字");
            return;
        }
        var crgroMoney = $('#crgroMoney').val();
        if(!crgroMoney){
            alert("请输入资金额度");
            return;
        }
        if(isNaN(crgroMoney)){
            alert("资金额度格式不正确");
            return;
        }
        
        var crgroDesc = $('#crgroDesc').val();
        var crcpaUser = JdataGet("user").crusrUuid;
        var data = {
            crcpaUser: crcpaUser,
        	crgroName: crgroName,
        	crgroMoney: crgroMoney,
        	crgroDesc: crgroDesc
        };

        var url = WEB_URL + "coreCompany/add/coreCompany";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                   alert(data.errMsg);
                } else {
                   Views.companyListView.show(); 
                }
            }
        );
    }
});

/************************集团列表********************************/
Views.companyListView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'companyList'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {
    	
    },

    scrollInitRender: function() {
        this.pageNum=1;
        this.companyList("scrollInit", 1, 10);
    },

    scrollDownRender: function() {
        this.pageNum=1;
        this.companyList("scrollDown", 1, 10);
    },

    scrollUpRender: function() {
        this.pageNum++;
        if(this.pageNum<=this.lastPageNumber){
            this.companyList("scrollUp", this.pageNum, 10);
        }
    },

    companyList: function(type,pageNum,pageSize){
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
        var url = WEB_URL + "coreCompany/find/coreCompanyPage";
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
                        self.scrollInit('companyListItem',{list : data.data.result});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('companyListItem',{list : data.data.result});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('companyListItem',{list : data.data.result});
                    };
                }
            },true
        );
    },

    to_companyDetails: function(btn) {
        dataSave('company_details_uuid', $(btn).attr('data-uuid'));
        Views.companyDetailsView.willShow($(btn).attr('data-uuid'));
    },

    to_createCompanyView: function(){
        Views.createCompanyView.show();
    }
});

/************************编辑集团详情********************************/
Views.companyDetailsView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'companyDetails'
    },

    willShow: function(param, isBackPage) {
        var self = this;
        var data = {
            crgroUuid: dataGet('company_details_uuid')
        }
        var url = WEB_URL + "coreCompany/views";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                } else {
                    self.company = data.data;
                    self.show({data: data.data},isBackPage);
                }
            },true
        );
    },

    didShow: function() {

    },
    
    deleteCompany: function() {
        var self = this;
        var data = {
                crgroUuid: this.company.crgroUuid
            }
        var url = WEB_URL + "coreCompany/disable/coreCompany";
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
    },

    ajaxChange: function(param, btnId, value, callBack) {
        var url = WEB_URL + "coreCompany/update/coreCompany";
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
                } else {
                    $('#'+btnId).html(value);
                    if(callBack){
                        callBack(true);
                    } else {
                        Views.changeInfo.hide();
                    }
                }
            },true,true
        );
    },

    changeName: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改集团名称',
            type:'text',
            hit: '请输入新的集团名称',
            content: $('#crgroName').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crgroUuid: self.company.crgroUuid,
                crgroName: value
            }
            if(value.length<2){
                alert("集团名称不能少于2个字符");
                return;
            }
            self.ajaxChange(param, 'crgroName', value);
        });
    },

    changeDesc: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改集团简介',
            type:'textArea',
            hit: '请输入新的集团简介',
            content: $('#crgroDesc').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crgroUuid: self.company.crgroUuid,
                crgroDesc: value
            }
            self.ajaxChange(param, 'crgroDesc', value);
        });
    },

    to_administratorList: function() {
        Views.administratorListView.willShow(this.company.crgroUuid);        
    }
});

/************************查看集团详情********************************/
Views.companyDetailsOnlyView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'companyDetailsOnly'
    },

    willShow: function(param, isBackPage) {
        var self = this;
        var data = {
            crgroUuid: dataGet('company_details_uuid')
        }
        var url = WEB_URL + "coreCompany/views";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                } else {
                    self.company = data.data;
                    self.show({data: data.data},isBackPage);
                }
            },true
        );
    },

    didShow: function() {

    },
    
    to_administratorList: function() {
        Views.administratorListView.willShow(this.company.crgroUuid);       
    }
});

/************************集团管理员列表********************************/
Views.administratorListView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'administratorList'
    },

    willShow: function(param, isBackPage) {
        this.crgroUuid = param;
        this.show(param, isBackPage);
    },

    didShow: function() {
        var user = JdataGet("user");
        if (user.crusrJob != 1){ /*非超级管理员*/
            $('.comm_title_right').hide(); /*隐藏创建按钮*/
        }
    },

    scrollInitRender: function() {
        this.pageNum=1;
        this.coreUserForPagesByCompany("scrollInit", 1, 10);
    },

    scrollDownRender: function() {
        this.pageNum=1;
        this.coreUserForPagesByCompany("scrollDown", 1, 10);
    },

    scrollUpRender: function() {
        this.pageNum++;
        if(this.pageNum<=this.lastPageNumber){
            this.coreUserForPagesByCompany("scrollUp", this.pageNum, 10);
        }
    },

    coreUserForPagesByCompany: function(type,pageNum,pageSize){
        var self = this;
        if(!pageNum){
            pageNum=1;
        }
        if(!pageSize){
            pageSize=10;
        }
        var data = {
            pageNum: pageNum,
            pageSize: pageSize,
            crusrCompany: this.crgroUuid
        };
        var url = WEB_URL + "coreUser/find/coreUserForPagesByCompany";
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
                        self.scrollInit('administratorListItem',{list : data.data.result});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('administratorListItem',{list : data.data.result});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('administratorListItem',{list : data.data.result});
                    };
                }
            },true
        );
    },

    to_personInfo: function(btn){
        var user = JdataGet("user");
        var uuid = $(btn).attr('data-uuid');
        if (user.crusrJob == 2){ /*集团管理员*/
            Views.personInfoOnlyView.willShow(uuid);
        }
        if (user.crusrJob == 1){ /*超级管理员*/
            Views.personInfoUpdateView.willShow(uuid);
        }
    },
    
    to_createAdministrator: function() {
        dataSave('createAdministrator_crgroUuid', this.crgroUuid);
        Views.createAdministratorView.willShow();
    }
});

/************************创建集团管理员********************************/
Views.createAdministratorView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'createAdministrator'
    },

    willShow: function(param, isBackPage) {
        this.crgroUuid = dataGet('createAdministrator_crgroUuid');
        var user =  JdataGet("user");
        this.show({job: user.crusrJob}, isBackPage);
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
                    self.crusrBirthday = valueText;
                    return true;
                }
            });
        }
        fun();
    },

    changeSexLayer: function() {
        var self = this;
        Views.changeSex.show(
            function(result) {
                $('#crusrGender').html(result);
                self.crusrGender = result=='男'?1:2;//1男2女3其它
            }
        );
    },

    saveUsers: function(){
        var crusrCompany = this.crgroUuid;
        var loginUser = JdataGet("user").crusrUuid;
        var crusrName = $('#crusrName').val();
        if(!crusrName){
            alert("请输入登录名");
            return;
        }
        var crusrCode = $('#crusrCode').val();
        var crusrPassword = $('#crusrPassword').val();
        if(!crusrPassword){
            alert("请输入登录密码");
            return;
        }
        var confirmPwd = $('#confirmPwd').val();
        if(!confirmPwd){
            alert("请输入确认密码");
            return;
        }        
        if(confirmPwd!=crusrPassword){
            alert("请输入确认密码不一致");
            return;
        }

        if(!Validator.stringIsMobileNo($('#crusrMobile').val())){alert("请输入正确的手机号");return;}
        if(!$('#crusrCopper').val()){alert("铜宝宝不能为空"); return;}
        if(!$('#crusrSilver').val()){alert("银宝宝不能为空"); return;}
        if(!$('#crusrGold').val()){alert("金宝宝不能为空"); return;}
        if(!$('#crusrYestIncome').val()){alert("昨日收益不能为空"); return;}

        var data = {
            crusrName: crusrName,
            crusrCode: crusrCode,
            crusrPassword: hex_md5(crusrPassword),
            confirmPwd: hex_md5(confirmPwd),
            crusrJob: "2",
            crusrCompany: crusrCompany,
            crusrMobile: $('#crusrMobile').val(),
            // crusrBirthday: this.crusrBirthday,
            // crusrAddress: $('#crusrAddress').val(),
            crusrGender: this.crusrGender,
            loginUser: loginUser,
            crusrCopper: $('#crusrCopper').val(),
            crusrSilver: $('#crusrSilver').val(),
            crusrGold: $('#crusrGold').val(),
            crusrYestIncome: $('#crusrYestIncome').val(),
        }

        var url = WEB_URL + "coreUser/add/coreUser";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                   alert(data.errMsg);
                }else{
                   Views.administratorListView.willShow(crusrCompany);       
                }
            }
        );
    }
});