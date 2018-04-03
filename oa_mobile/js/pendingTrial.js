/************************待审列表********************************/
Views.pendingTrialListView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'pendingTrialList'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {

    },

    scrollInitRender: function() {
        this.pageNum=1;
        this.pendingTrialList("scrollInit", 1, 5);
    },

    scrollDownRender: function() {
        this.pageNum=1;
        this.pendingTrialList("scrollDown", 1, 5);
    },

    scrollUpRender: function() {
        this.pageNum++;
        if(this.pageNum<=this.lastPageNumber){
            this.pendingTrialList("scrollUp", this.pageNum, 5);
        }
    },
    
    pendingTrialList: function(type,pageNum,pageSize){
        var self = this;
        self.isInvestor = false;       
        if(!pageNum){
            pageNum=1;
        }
        if(!pageSize){
            pageSize=5;
        }

        var processType = dataGet('processType');
        var user = JdataGet("user");
        var data;
        var url = "";
        if(processType == "pending") {
            $('#addInverstorsPross').remove();
            data = {
                pageNum: this.pageNum,
                pageSize: this.pageSize,
                crusrProject: user.crusrProject,
                crflwApplyUser: user.crusrUuid,
                crusrJob: user.crusrJob
            }
            url = WEB_URL + "coreFlow/pending/list";
        }
       
        if(processType == "process") {
            $('#addInverstorsPross').remove();
            $('#title').html("流程列表");
            $('#comm_title').html("流程列表");
            data = {
                pageNum: this.pageNum,
                pageSize: this.pageSize,
                crusrProject: user.crusrProject,
                crusrJob: user.crusrJob,
                crusrCompany: user.crusrCompany,
                crusrUuid: user.crusrUuid
            }
            url = WEB_URL + "coreFlow/flow/list";
        }

        if(processType == "investor") {
            $('#title').html("投资拨款流程列表");
            $('#comm_title').html("投资拨款流程列表");
            self.isInvestor = true;
            data = {
                pageNum: this.pageNum,
                pageSize: this.pageSize,
                crusrJob: 3,
                crusrUuid: dataGet('investorDescUuid')
            }
            url = WEB_URL + "coreFlow/flow/list";
        }

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
                        self.scrollInit('pendingTrialListItem',{list : data.data.result, isInvestor: self.isInvestor});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('pendingTrialListItem',{list : data.data.result, isInvestor: self.isInvestor});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('pendingTrialListItem',{list : data.data.result, isInvestor: self.isInvestor});
                    };
                }
            },true
        );
    },

    toPendingTrialDesc: function(btn) {
        var uuid = $(btn).attr('data-code');
        dataSave('pendingTrialDescUUid', uuid);
        Views.pendingTrialDescView.willShow();
    },

    to_investorProcessList: function() {
        Views.investorProcessListView.willShow();
    },

    deleteInvestor: function(btn) {
        var self = this;
        var data = {
            crsptUser: dataGet('investorDescUuid'),
            crsptFlow: $(btn).attr('data-code'),
 
        }

        var url = WEB_URL + "coreInvestment/delete/coreInvestment";

        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    self.scrollInitRender();
                }
            },true
        );
    }
});

/************************流程详情********************************/
Views.pendingTrialDescView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'pendingTrialDesc'
    },

    willShow: function(param, isBackPage) {
        var self = this;
        var pendingTrialDescUUid = dataGet("pendingTrialDescUUid");
        var data = {
            crflwCode: pendingTrialDescUUid
        }
        var url = WEB_URL + "coreFlow/views";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    var crflwByTypeName = "";
                    self.crflwByType = data.data.crflwByType;
                    if(data.data.crflwByType == 1){crflwByTypeName="渠道";}
                    if(data.data.crflwByType == 2){crflwByTypeName="策划";}
                    if(data.data.crflwByType == 3){crflwByTypeName="财务";}

                    var crusrJobName = "";
                    var crusrJob = JdataGet('user').crusrJob;
                    if(crusrJob == 4){crusrJobName="渠道";}
                    if(crusrJob == 5){crusrJobName="策划";}
                    if(crusrJob == 8){crusrJobName="财务";}

                    var stepName = "经过";
                    if(crflwByTypeName == crusrJobName) {stepName = "发起";}
                    if(crusrJob == 8 && data.data.crflwByType != 3) {stepName = "完成";}
                    if(crusrJob == 1 && data.data.crflwStep == 13) {stepName = "拨款完成";}//超级管理员结束
                    // stepName = "经过";
                    self.stepName = stepName;

                    if(!isBackPage){
                        dataSave('crflwName',data.data.crflwName);
                        dataSave('crflwDesc',data.data.crflwDesc);
                    }

                    self.show({
                        data: data.data, 
                        userCrusrJob:JdataGet('user').crusrJob,
                        stepName: stepName,
                        crflwName: dataGet('crflwName'),
                        crflwDesc: dataGet('crflwDesc'),
                        processType: dataGet('processType')
                    }, isBackPage);
                   self.descData = data.data;
                }
            },true
        );
    },

    didShow: function() {

    },

    changeTitle: function() {
        Views.changeInfo.show({
            title:'修改流程名称',
            type:'text',
            hit: '请输入新的流程名称',
            content: $('#crflwName').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            if(!value){
                alert("流程名称不能为空");
                return;
            }

            if(value.length>14){
                alert("流程名称不能超过14个字符");
                return;
            }
            $('#crflwName').html(value);
            dataSave('crflwName', value);  
            Views.changeInfo.hide();
        });
    },

    changeDesc: function() {
        Views.changeInfo.show({
            title:'修改活动简介',
            type:'textarea',
            hit: '请输入新的活动简介',
            content: $('#crflwDesc').html()
        },function(value){
            Views.changeInfo.hide();
        },function(value){
            if(!value){
                alert("活动简介不能为空");
                return;
            }
            $('#crflwDesc').html(value);
            dataSave('crflwDesc', value);
            Views.changeInfo.hide();
        });
    },

    to_picAttachment: function() {
        dataSave('Attachment_cratmType', 1);//1图片附件
        dataSave('picAttachment_crflwUuid', this.descData.crflwUuid);
        dataSave('picAttachment_crflwCode', this.descData.crflwCode);
        dataSave('picAttachment_stepName',this.stepName)
        Views.picAttachmentView.willShow();
    },

    to_processDirList: function() {
        Views.processDirListView.willShow(this.descData.crflwCode);
    },

    to_electronicInvoice: function() {//电子发票
        dataSave('picAttachment_crflwName', $('#crflwName').html());
        dataSave('picAttachment_crflwCode', this.descData.crflwCode);
        Views.electronicInvoiceView.willShow();
    },

    to_certificate: function() {//拨款凭证
        dataSave('Attachment_cratmType', 2);//2拨款凭证
        dataSave('picAttachment_crflwUuid', this.descData.crflwUuid);
        dataSave('picAttachment_crflwCode', this.descData.crflwCode);

        if(this.stepName=='拨款完成') {
            dataSave('picAttachment_stepName', "拨款完成"); 
        }else{
            dataSave('picAttachment_stepName', "发起"); 
        }
        
        Views.picAttachmentView.willShow();
    },

    sendBack: function(btn) {//不通过
        if(this.crflwByType == 3){
            this.ajaxCoreFlowUpdate(13);
        }else {
            this.ajaxCoreFlowUpdate(3);
        }
    },

    pass: function(btn) {//通过
        if(this.crflwByType == 3){
            this.ajaxCoreFlowUpdate(12);
        }else{
            this.ajaxCoreFlowUpdate(2);
        }   
    },

    sureRefund: function(btn) {//确认已还款
        if(this.crflwByType == 3){
            this.ajaxCoreFlowUpdate(15);
        }else{
            this.ajaxCoreFlowUpdate(5);
        }
    },

    delete: function(btn) {//撤回 删除活动
        if(this.crflwByType == 3){
            this.ajaxCoreFlowUpdate(14);
        }else {
            this.ajaxCoreFlowUpdate(4);
        }
    },

    start: function(btn) {//发起
        if(this.crflwByType == 3){
            this.ajaxCoreFlowUpdate(11);
        }else {
            this.ajaxCoreFlowUpdate(1);
        }
    },

    ajaxCoreFlowUpdate: function(crflwUptype) {//12确认拨款
        var self = this;
        var descData = this.descData;
        var data = {
            crflwCode: descData.crflwCode,
            crflwName: $('#crflwName').html(),
            crflwDesc: $('#crflwDesc').html(),
            crflwLoanMoney: descData.crflwLoanMoney?descData.crflwLoanMoney:0,
            crflwByType: descData.crflwByType,
            crflwStep: descData.crflwStep,
            crflwRemarks: $('#crflwRemarks').val(),
            crflwUptype: crflwUptype,
            crflwUpdateUser: JdataGet('user').crusrUuid,
            crflwUpdateJob: JdataGet('user').crusrJob,
        }
        var url = WEB_URL + "coreFlow/update/coreFlow";
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