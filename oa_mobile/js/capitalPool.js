/***********************资金池 蓝 超级管理员********************************/
Views.capitalPoolBlueView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'capitalPoolBlue'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {
        this.crusrUuid = JdataGet('user').crusrUuid;
        this.initMoney();
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
            pageSize=5;
        }
        var data = {
                pageNum: pageNum,
                pageSize: pageSize
            }
        var url = WEB_URL + "coreCompany/find/coreCompanyPage";
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
                        self.scrollInit('capitalPoolBlueItem',{list : data.data.result});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('capitalPoolBlueItem',{list : data.data.result});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('capitalPoolBlueItem',{list : data.data.result});
                    };
                }
            },true
        );
    },

    to_projectPoolYellow: function(btn) {
        Views.capitalPoolYellowView.willShow($(btn).attr('data-uuid'));
    },

    initMoney: function() {
        var data = {crusrUuid : this.crusrUuid};
        var url = WEB_URL + "coreUser/views";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    $('#crusrMoney').html(data.data.crusrMoney);
                }
            },true
        );
    },

    ajaxChange: function(param, btnId, value, callBack) {
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

    updateMoney: function() {
        var self = this;
        Views.changeInfo.show({
            title:'编辑额度',
            type:'text',
            hit: '请输入资金额度',
            content: $('#crusrMoney').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crcpaUser: self.crusrUuid,
                crcpaBusi: self.crusrUuid,
                crusrMoney: value
            }
            if(!value){
                alert("请输入资金额度");
                return;
            }
            if(isNaN(value)){
                alert("资金额度格式不正确");
                return;
            }
            self.ajaxChange(param, 'crusrMoney', value);
        });
    }
});

/************************资金池 黄 集团********************************/
Views.capitalPoolYellowView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'capitalPoolYellow'
    },

    willShow: function(param, isBackPage) {
        this.crusrCompany = param;
        this.show(param, isBackPage);
    },

    didShow: function() {
        this.initCompany();
    },

    initCompany: function() {
        var data = {crgroUuid : this.crusrCompany};
        var url = WEB_URL + "coreCompany/views";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    $('#crgroMoney').html(data.data.crgroMoney);
                }
            },true
        );
    },

    scrollInitRender: function() {
        this.pageNum=1;
        this.projectList("scrollInit", 1, 10);
    },

    scrollDownRender: function() {
        this.pageNum=1;
        this.projectList("scrollDown", 1, 10);
    },

    scrollUpRender: function() {
        this.pageNum++;
        if(this.pageNum<=this.lastPageNumber){
            this.projectList("scrollUp", this.pageNum, 10);
        }
    },

    projectList: function(type,pageNum,pageSize){
        var self = this;       
        if(!pageNum){
            pageNum=1;
        }
        if(!pageSize){
            pageSize=5;
        }
        var data = {
                crproCompany: this.crusrCompany,
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
                        self.scrollInit('capitalPoolYellowItem',{list : data.data.result});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('capitalPoolYellowItem',{list : data.data.result});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('capitalPoolYellowItem',{list : data.data.result});
                    };
                }
            },true
        );
    },

    ajaxChange: function(param, btnId, value, url, callBack) {
        var url = WEB_URL + url;
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
                    // $('#'+btnId).html(value);
                   
                    if($(btnId).find('.money_value').length != 0){
                        $(btnId).find('.money_value').html(value);
                    }else{
                        $('#crgroMoney').html(value);
                    }

                    if(callBack){
                        callBack(true);
                    }else{
                        Views.changeInfo.hide(); 
                    }
                }
            },true,true
        );
    },

    changeMoney: function(btn) {
        var self = this;
        Views.changeInfo.show({
            title:'编辑额度',
            type:'text',
            hit: '请输入资金额度',
            content: $(btn).find('.money_value').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {    
                crcpaUser: JdataGet('user').crusrUuid,
                crcpaBusi: $(btn).attr('data-uuid'),
                crproMoney: value
            }
            if(!value){
                alert("请输入资金额度");
                return;
            }
            if(isNaN(value)){
                alert("资金额度格式不正确");
                return;
            }
            self.ajaxChange(param, btn, value, 'coreProject/update/projectCapital');
        });
    },

    updateMoney: function() {
        var self = this;
        Views.changeInfo.show({
            title:'编辑额度',
            type:'text',
            hit: '请输入资金额度',
            content: $('#crgroMoney').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            var param = {
                crcpaUser: JdataGet('user').crusrUuid,
                crcpaBusi: self.crusrCompany,
                crgroMoney: value
            }
            if(!value){
                alert("请输入资金额度");
                return;
            }
            if(isNaN(value)){
                alert("资金额度格式不正确");
                return;
            }
            self.ajaxChange(param, 'crgroMoney', value, 'coreCompany/update/companyCapital');
        });
    }
});

/************************财富********************************/
Views.capitalInvestorView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'capitalInvestor'
    },

    willShow: function(param, isBackPage) {       
        this.show(param, isBackPage);
    },

    didShow: function() {
        this.crusrUuid = JdataGet('user').crusrUuid;
        this.initMoney();
    },

    initMoney: function() {
        var data = {crusrUuid : this.crusrUuid};
        var url = WEB_URL + "coreUser/views";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    var crusrMoneyTotal = (data.data.crusrCopper + data.data.crusrSilver + data.data.crusrGold + data.data.crusrTotalIncome).toFixed(2);
                    $('#crusrYestIncome').html(data.data.crusrYestIncome);

                    $('#crusrMoneyTotal').html(crusrMoneyTotal);
                    $('#crusrCopper').html('￥' + data.data.crusrCopper);
                    $('#crusrSilver').html('￥' + data.data.crusrSilver);
                    $('#crusrGold').html('￥' + data.data.crusrGold);
                }
            },true
        );
    },

    go_investorProcessList: function() {
        dataSave('processType', "process")
        Views.pendingTrialListView.willShow();
    }
});

/************************申请拨款********************************/
Views.capitalPoolAppropriationView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'capitalPoolAppropriation'
    },

    willShow: function(param, isBackPage) {
        var self = this;
        self.uuids = "";

        var crusrProject = dataGet('appropriation_crusrProject');
        var url = WEB_URL + "coreProject/views";
        var data = {crproUuid: crusrProject};
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    self.show({data: data.data}, isBackPage);
                }
            },true
        );

        // this.show(param, isBackPage);
    },

// crproCdate:"2016-10-23"
// crproCompany:"CAAAAA"
// crproCompanyName:"万科房产"
// crproDesc:"北辰之光位于城北1"
// crproMoney:500000
// crproName:"北宸之光"
// crproStatus:1
// crproUdate:"2016-10-26"
// crproUuid:"PAAAAA"

    didShow: function(btn) {
        $('#changeName').html(dataGet('changeName'));
        var changeMeneyStr = dataGet('changeMeney')=="填写"?"":'￥';
        $('#changeMeney').html(changeMeneyStr+dataGet('changeMeney'));
        $('#changeIntroduce').html(dataGet('changeIntroduce'));
        $('#leave').val(dataGet('leave'));
    },

    changeName: function(btn) {
        var self = this;
        Views.changeInfo.show({
            title:'流程名称',
            type:'text',
            hit: '请您填入活动名称',
            content:  $('#changeName').html()=="填写"? "" : $('#changeName').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            if(!value) {
                alert("流程名称不能为空");
                return;
            }

            if(value.length>14){
                alert("流程名称不能超过14个字符");
                return;
            }

            $('#changeName').html(value);
            dataSave('changeName', value);
            self.Name = value;
            Views.changeInfo.hide();
        });
    },

    changeMeney: function(btn) {
        var self = this;
        Views.changeInfo.show({
            title:'资金额度',
            type:'number',
            hit: '请您填入申请金额',
            content:  $('#changeMeney').html()=="填写"? "" : $('#changeMeney').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            if(!value) {
                alert("申请金额不能为空");
                return;
            }
            $('#changeMeney').html('￥'+ value);

            dataSave('changeMeney', value);
            self.Meney = value;
            Views.changeInfo.hide();
        });
    },

    upImg: function(btn) {
        // var self = this;
        // Views.upImg.show(
        //  {url: WEB_URL + "coreAttachment/upload/start/stream",param:{cratmType:1, cratmDir:"oa/pic",fileName:"fundingstart.png",}},
        //    function(data) {//data上传图片后返回的结果信息
        //        self.uuids += data.data + "|"; 
        //    }
        // );
        
        dataSave('leave', $('#leave').val());
        dataSave('picAttachment_stepName', "发起申请拨款");
        Views.picAttachmentView.willShow();
    },

    changeIntroduce: function(btn) {
        var self = this;
        Views.changeInfo.show({
            title:'活动简介',
            type:'textarea',
            hit: '请您填入活动简介',
            content:  $('#changeIntroduce').html()=="填写"? "" : $('#changeIntroduce').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            if(!value) {
                alert("活动简介不能为空");
                return;
            }
            $('#changeIntroduce').html(value);
            self.Introduce = value;
            dataSave('changeIntroduce', value);
            Views.changeInfo.hide();
        });
    },

    sure_apply: function () {
        var self = this;

        var changeName = this.Name;
        var changeMeney = this.Meney;
        var changeIntroduce = this.Introduce;
        var leave = $('#leave').val();

        if(!changeName) {alert("流程名称不能为空"); return;}
        if(!changeMeney) {alert("申请金额不能为空"); return;}
        if(!changeIntroduce) {alert("活动简介不能为空"); return;}

        var crusrProject = dataGet('appropriation_crusrProject');
        var user = JdataGet("user");
        var url = WEB_URL + "coreFlow/add/coreFlow/funding";
        var data = {
            crflwName: changeName,
            crflwDesc: changeIntroduce,
            crflwLoanMoney: changeMeney,
            crflwCompany: user.crusrCompany,
            crflwProject: user.crusrProject,
            crflwApplyUser: user.crusrUuid,
            crflwRemarks: leave,
            crflwUpdateJob: user.crusrJob,
            // uuids:  self.uuids
            uuids:  dataGet('picUuids')
        };
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


/************************申请资金********************************/
Views.capitalApplyForView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'capitalApplyFor'
    },

    willShow: function(param, isBackPage) {
        this.uuids = "";
        this.show(param, isBackPage);       
    },

    didShow: function() {
        $('#applyForName').html(dataGet('applyForName'));
        $('#applyForIntroduce').html(dataGet('applyForIntroduce'));
        $('#applyForNote').val(dataGet('applyForNote'));
    },

    applyForName: function(btn) {
        var self = this;
        Views.changeInfo.show({
            title:'流程名称',
            type:'text',
            hit: '请您填入活动名称',
            content:  $('#applyForName').html()=="填写"? "" : $('#applyForName').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            if(!value) {
                alert("流程名称不能为空");
                return;
            }

            if(value.length>14){
                alert("流程名称不能超过14个字符");
                return;
            }
            $('#applyForName').html(value);
            dataSave("applyForName", value);
            Views.changeInfo.hide();
        });
    },

    applyForIntroduce :function(btn) {
        var self = this;
        Views.changeInfo.show({
            title:'活动简介',
            type:'textarea',
            hit: '请您填入活动简介',
            content: $('#applyForIntroduce').html()=="填写"? "" : $('#applyForIntroduce').html()
        },function(value) {
            Views.changeInfo.hide();
        },function(value) {
            if(!value) {
                alert("活动简介不能为空");
                return;
            }
            $('#applyForIntroduce').html(value);
            dataSave("applyForIntroduce", value);
            Views.changeInfo.hide();
        });
    },

    upImg: function() {
        // var self = this;
        // Views.upImg.show(
        //  {url: WEB_URL + "coreAttachment/upload/start/stream",param:{cratmType:1, cratmDir:"oa/pic",fileName:"start.png",}},
        //    function(data) {//data上传图片后返回的结果信息
        //        self.uuids += data.data + "|"; 
        //    }
        // );
        dataSave('applyForNote', $('#applyForNote').val());
        dataSave('picAttachment_stepName', "发起申请资金");
        Views.picAttachmentView.willShow();
    },

    applyForSure: function(btn) {
        var self = this;

        var crflwName = $('#applyForName').html()=="填写"? "" : $('#applyForName').html();
        if(!crflwName) {
            alert("流程名称不能为空");
            return;
        }
        var crflwDesc = $('#applyForIntroduce').html()=="填写"? "" : $('#applyForIntroduce').html();
        if(!crflwDesc) {
            alert("活动简介不能为空");
            return;
        }


        var user = JdataGet("user");
        var param = {
            crflwName: crflwName,
            crflwDesc: crflwDesc,
            crflwCompany: user.crusrCompany,//
            crflwProject: user.crusrProject,
            crflwApplyUser: user.crusrUuid,
            crflwRemarks: $('#applyForNote').val(),
            crflwUpdateJob: user.crusrJob,
            // uuids: self.uuids//图片附件UUIDS,用"|"隔开
            uuids: dataGet('picUuids')
        }
        var url = WEB_URL + "coreFlow/add/coreFlow/apply";
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
            },true
        );
    }
});

     