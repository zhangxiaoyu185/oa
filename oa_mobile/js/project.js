/************************创建项目********************************/
Views.createProjectView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'createProject'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {
    	this.initCompany();
    },

    initCompany: function() {
        var data = {};
        var url = WEB_URL + "coreCompany/find/all";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    var strhtml_select = '<option value =""  style="color:#FF0000">请选择</option>';
                    var arrData = data.data;
                    var ln = arrData.length;
                    for(var i=0;i<ln;i++){
                        strhtml_select += '<option value="'+arrData[i].crgroUuid+'">'+arrData[i].crgroName+'</option>';
                    }
                    $('#crproCompany').html(strhtml_select);
                }
            },true
        );
    },

    saveProject: function(callBack) {
        var self = this;
    	var crproName = $('#crproName').val();
        if(!crproName){
            alert("请输入项目名称");
            return;
        }
        var crproMoney = $('#crproMoney').val();
        if(!crproMoney){
            alert("请输入资金额度");
            return;
        }
        if(isNaN(crproMoney)){
            alert("资金额度格式不正确");
            return;
        }       
        var crproCompany = $('#crproCompany').val();
        if(!crproCompany){
            alert("请选择所属集团简介");
            return;
        }
        var crproDesc = $('#crproDesc').val();
        var crcpaUser = JdataGet("user").crusrUuid;
        var data = {
            crcpaUser: crcpaUser,
        	crproName: crproName,
        	crproMoney: crproMoney,
        	crproCompany: crproCompany,
        	crproDesc: crproDesc
        }

        var url = WEB_URL + "coreProject/add/coreProject";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    Views.projectListView.willShow(JdataGet("user").crusrJob);
                }
            }
        );
    }
});

/************************项目列表********************************/
Views.projectListView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'projectList'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {
        this.user = JdataGet("user");   
    },

    scrollInitRender: function() {       
        this.pageNum=1;
        if (this.user.crusrJob == 1) {
            this.projectList("scrollInit", 1, 10);
        } else {
            this.projectListByCompany("scrollInit", 1, 10);
        }
    },

    scrollDownRender: function() {
        this.pageNum=1;
        if (this.user.crusrJob == 1) {
            this.projectList("scrollDown", 1, 10);
        } else {
            this.projectListByCompany("scrollDown", 1, 10);
        }
    },

    scrollUpRender: function() {
        this.pageNum++;
        if(this.pageNum<=this.lastPageNumber){
            if (this.user.crusrJob == 1) {
                this.projectList("scrollUp", this.pageNum, 10);
            } else {
                this.projectListByCompany("scrollUp", this.pageNum, 10);
            }
        }
    },

    projectList: function(type,pageNum,pageSize){
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
            }
        var url = WEB_URL + "coreProject/find/by/cnd";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    self.lastPageNumber = data.data.lastPageNumber;
                    if (type == "scrollInit") {
                        self.scrollInit('projectListItem',{list : data.data.result});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('projectListItem',{list : data.data.result});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('projectListItem',{list : data.data.result});
                    };
                }
            },true
        );
    },
    
    projectListByCompany: function(type,pageNum,pageSize){
        var self = this;       
        if(!pageNum){
            pageNum=1;
        }
        if(!pageSize){
            pageSize=10;
        }
        var data = {
                crproCompany: this.user.crusrCompany,
                pageNum: pageNum,
                pageSize: pageSize
            }
        var url = WEB_URL + "coreProject/find/by/cnd";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    self.lastPageNumber = data.data.lastPageNumber;
                    if (type == "scrollInit") {
                        self.scrollInit('projectListItem',{list : data.data.result});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('projectListItem',{list : data.data.result});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('projectListItem',{list : data.data.result});
                    };
                }
            },true
        );
    },

    to_projectDetails: function(btn) {
        dataSave('project_details_uuid', $(btn).attr('data-uuid'));
    	Views.projectDetailsView.willShow($(btn).attr('data-uuid'));
    },
    
    to_createProjectView: function() {
    	Views.createProjectView.show();
    }
});

/************************编辑项目详情********************************/
Views.projectDetailsView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'projectDetails'
    },

    willShow: function(param, isBackPage) {
        var self = this;
        var data = {
            crproUuid: dataGet('project_details_uuid')
        }
        var url = WEB_URL + "coreProject/views";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                } else {
                    self.project = data.data;
                    dataSave('company_user_uuid', data.data.crproCompany);
                    self.show({data: data.data},isBackPage);
                }
            },true
        );
    },

    didShow: function() {

    },
    
    deleteProject: function() {
        var self = this;
        var data = {
                crproUuid: this.project.crproUuid
            }
        var url = WEB_URL + "coreProject/disable/coreProject";
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


    ajaxChange: function(param, btnId, value, callBack) {
        var url = WEB_URL + "coreProject/update/coreProject";
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

    changeName: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改项目名称',
            type:'text',
            hit: '请输入新的项目名称',
            content: $('#crproName').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crproUuid: self.project.crproUuid,
                crproName: value
            }
            if(value.length<2){
                alert("项目名称不能少于2个字符");
                return;
            }
            self.ajaxChange(param, 'crproName', value);
        });
    },

    changeDesc: function() {
        var self = this;
        Views.changeInfo.show({
            title:'修改项目简介',
            type:'textArea',
            hit: '请输入新的项目简介',
            content: $('#crproDesc').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crproUuid: self.project.crproUuid,
                crproDesc: value
            }
            self.ajaxChange(param, 'crproDesc', value);
        });
    },
    
    to_workerListView: function(btn) {
        dataSave('project_user_job', $(btn).attr('data-job'));
        Views.workerListView.willShow(this.project.crproUuid);
    }
});

/************************查看项目详情********************************/
Views.projectDetailsOnlyView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'projectDetailsOnly'
    },

    willShow: function(param, isBackPage) {
        var self = this;
        var data = {
            crproUuid: dataGet('project_details_uuid')
        }
        var url = WEB_URL + "coreProject/views";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                } else {
                    self.project = data.data;
                    dataSave('company_user_uuid', data.data.crproCompany);
                    self.show({data: data.data},isBackPage);
                }
            },true
        );
    },

    didShow: function() {

    },
    
    to_workerListView: function(btn) {
        dataSave('project_user_job', $(btn).attr('data-job'));
        Views.workerListView.willShow(this.project.crproUuid);
    }
});

/************************职员列表********************************/
Views.workerListView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'workerList'
    },

    willShow: function(param, isBackPage) {
        this.crproUuid = param;
        this.projectCrusrJob = dataGet('project_user_job');
        this.show(param, isBackPage);
    },

    didShow: function() {
        var user = JdataGet("user");
        if (user.crusrJob != 1 && user.crusrJob != 2){ /*非超级管理员和集团管理员*/
            $('.comm_title_right').hide(); /*隐藏创建按钮*/
        }
    },

    scrollInitRender: function() {
        this.pageNum=1;
        this.coreUserForPagesByProject("scrollInit", 1, 10);
    },

    scrollDownRender: function() {
        this.pageNum=1;
        this.coreUserForPagesByProject("scrollDown", 1, 10);
    },

    scrollUpRender: function() {
        this.pageNum++;
        if(this.pageNum<=this.lastPageNumber){
            this.coreUserForPagesByProject("scrollUp", this.pageNum, 10);
        }
    },

    coreUserForPagesByProject: function(type,pageNum,pageSize){
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
            crusrJob: this.projectCrusrJob,
            crusrProject: this.crproUuid
        };
        var url = WEB_URL + "coreUser/find/coreUserForPagesByProject";
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
                        self.scrollInit('workerListItem',{list : data.data.result});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('workerListItem',{list : data.data.result});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('workerListItem',{list : data.data.result});
                    };
                }
            },true
        );
    },

    to_personInfo: function(btn){
        var user = JdataGet("user");
        var uuid = $(btn).attr('data-uuid');
        if (user.crusrJob == 2 || user.crusrJob == 1){ /*超级管理员和集团管理员*/            
            Views.personInfoUpdateView.willShow(uuid);
        } else {
            Views.personInfoOnlyView.willShow(uuid);
        }          
    },
    
    to_createWorker: function() {
        dataSave('createWorker_crproUuid',this.crproUuid);
        Views.createWorkerView.willShow();
    }   
});

/************************创建职员********************************/
Views.createWorkerView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'createWorker'
    },

    willShow: function(param, isBackPage) {
        this.crproUuid = dataGet('createWorker_crproUuid');
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
        var crusrProject = this.crproUuid;
        var loginUser = JdataGet("user").crusrUuid;
        var crusrCompany = dataGet('company_user_uuid');
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
            crusrCompany: crusrCompany,
            crusrName: crusrName,
            crusrCode: crusrCode,
            crusrPassword: hex_md5(crusrPassword),
            confirmPwd: hex_md5(confirmPwd),
            crusrJob: dataGet('project_user_job'),
            crusrProject: crusrProject,
            crusrMobile: $('#crusrMobile').val(),
            crusrBirthday: this.crusrBirthday,
            crusrAddress: $('#crusrAddress').val(),
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
                   Views.workerListView.willShow(crusrProject);
                }
            }
        );
    }
});