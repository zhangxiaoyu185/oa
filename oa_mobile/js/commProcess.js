/************************流程走向********************************/
Views.processDirListView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'processDirList'
    },

    willShow: function(param, isBackPage) {
    	var self = this;
    	self.crflwCode = param;
        self.show(param, isBackPage);
    },

    didShow: function() {

    },

    scrollInitRender: function() {       
        this.pageNum=1;
        this.companyList("scrollInit", 1, 5);
    },

    scrollDownRender: function() {
        this.pageNum=1;
        this.companyList("scrollDown", 1, 5);
    },

    scrollUpRender: function() {
        this.pageNum++;
        if(this.pageNum<=this.lastPageNumber){
            this.companyList("scrollUp", this.pageNum, 5);
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
            crflwCode: self.crflwCode,
        }
        var url = WEB_URL + "coreFlow/log/list";

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
                        self.scrollInit('processDirListItem',{list: data.data, WEB_URL: WEB_URL});
                    };
                    if (type == "scrollDown") {
                        self.scrollDown('processDirListItem',{list: data.data, WEB_URL: WEB_URL});
                    };
                    if (type == "scrollUp") {
                        self.scrollUp('processDirListItem',{list: data.data, WEB_URL: WEB_URL});
                    };
                }
            },true
        );
    },
});

/************************图片附件********************************/
Views.picAttachmentView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'picAttachment'
    },

    willShow: function(param, isBackPage) {//框架还是有些问题 原来传参有问题  后期优化 
        var picAttachment_crflwCode = dataGet('picAttachment_crflwCode');  
        var self = this;

        if(dataGet('picAttachment_stepName')=="发起申请资金" || dataGet('picAttachment_stepName')=="发起申请拨款"){
            var picUuids = dataGet('picUuids');
            var list = new Array();
            if(picUuids&& picUuids!=""){
                var cratmUuids = picUuids.split('|'); 
                for(var i=0; i<cratmUuids.length-1; i++) {
                    list[i] = {cratmUuid: cratmUuids[i]};
                }
            }
            self.show({list: list, WEB_URL: WEB_URL, picAttachment_stepName: "发起"}, isBackPage);
        }else{
            var data = {
                cratmType: dataGet('Attachment_cratmType'),
                cratmBusUuid: picAttachment_crflwCode,
            }
            var url = WEB_URL + "coreAttachment/find/attachement";

            ajaxTool("post",data,url,
                function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                    alert("error:" + data);
                },
                function success(data){
                    if(!data.success) {
                        alert(data.errMsg);
                    }else{
                        
                       self.show({list: data.data, WEB_URL: WEB_URL, picAttachment_stepName: dataGet('picAttachment_stepName'), processType:dataGet('processType')}, isBackPage);
                    }
                },true
            );
        } 
    },

    didShow: function() {

    },

    upImg: function() {
        var self = this;
        var uuids = dataGet('picUuids');
        var url = "";
        var param;
        if(dataGet('picAttachment_stepName')=="发起申请资金"){
            url = WEB_URL + "coreAttachment/upload/start/stream",
            param = {cratmType:1, cratmDir:"oa/pic",fileName:"start.png"};
        }else if(dataGet('picAttachment_stepName')=="发起申请拨款") {
            url = WEB_URL + "coreAttachment/upload/start/stream";
            param = {cratmType:1, cratmDir:"oa/pic",fileName:"fundingstart.png"};
        }else {
            url = WEB_URL + "coreAttachment/upload/process/stream";
            param = {cratmType: dataGet('Attachment_cratmType'), cratmDir:"oa/pic",fileName:"process.png",crflwCode:dataGet('picAttachment_crflwCode')};
        }

        Views.upImg.show(
         {url: url,param: param},
           function(data, imgData) {//data上传图片后返回的结果信息
            console.log(data);
                uuids += data.data + "|"; 
                dataSave('picUuids', uuids);
                console.log(uuids);
                var fielImtem =   '<div class="upImg_item">'+
                                    '<img class="upImg_preview btn" src="'+imgData+'"/>'+
                                    '<div class="upImg_name"></div>'+
                                    '<div class="upImg_top_bar">'+
                                      '<div class="upImg_file_delete" data-uuid="'+data.data+'" onclick="Views.picAttachmentView.deletePic(this)"></div>'+
                                    '</div>'+
                                  '</div>'; 
                 $("#displayImg").prepend(fielImtem);
           }
        );
    },

    deletePic: function(btn) {
        var uuid = $(btn).attr('data-uuid');
        var data = {
            cratmUuid: uuid
        }
        var url = WEB_URL + "coreAttachment/delete";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    $(btn).parent().parent().remove();
                    var items = $('.upImg_item').find('.upImg_file_delete');
                    var ids = "";
                    for(var i=0; i<items.length; i++){
                        ids +=$(items[i]).attr('data-uuid') + "|";
                    }
                    dataSave('picUuids', ids);
                    console.log(ids)
                   
                }
            },true
        );
    }
});

/************************电子发票********************************/
Views.electronicInvoiceView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'electronicInvoice'
    },

    willShow: function(param, isBackPage) {//框架还是有些问题 原来传参有问题  后期优化 
        var picAttachment_crflwCode = dataGet('picAttachment_crflwCode');  
        var self = this;
        
        var data = {
            creicCode: picAttachment_crflwCode,
        }
        var url = WEB_URL + "coreFlow/views/invoice";

        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    var crusrJob = JdataGet('user').crusrJob;
                    var processType = dataGet('processType');
                    self.show({imgSrc: data.data.creicPng, job: crusrJob, processType: processType}, isBackPage);
                }
            },true
        ); 
    },

    didShow: function() {
        if(JdataGet('user').crusrJob!=8) return;

        var data = {}
        var url = WEB_URL + "coreFlow/invoice/type/find/all";

        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                    var htmlStr = "";
                    for(var i=0; i<data.data.length; i++) {
                       htmlStr += '<option value="'+data.data[i].crintUuid+'">'+data.data[i].crintName+'</option>'
                       $('#select').html(htmlStr);
                    }
                }
            },true
        ); 
    },

    to_updateInvoice: function() {
        var self = this;
        var data = {
            creicCode: dataGet('picAttachment_crflwCode'),

            creicPayee: $('#creicPayee').val(),
            creicPayeeAccount: $('#creicPayeeAccount').val(),
            creicPayeeBank: $('#creicPayeeBank').val(),

            creicPayer: $('#creicPayer').val(),
            creicPayerAccount: $('#creicPayerAccount').val(),
            creicPayerBank: $('#creicPayerBank').val(),

            creicMatter: dataGet('picAttachment_crflwName'),
            creicInvoiceType: $('#select').val(),

            creicLowAmount: $('#creicLowAmount').val(),
            creicRemarks: $('#creicRemarks').val(),
            creicDrawer: $('#creicDrawer').val(),
            creicOperator: JdataGet('user').crusrUuid
        }
        var url = WEB_URL + "coreFlow/update/invoice";

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



