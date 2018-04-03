/************************首页********************************/
Views.indexView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'index'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {
        var user = JdataGet("user");
        if(user.crusrHead) {
            var headPath = WEB_URL + "coreAttachment/image/get/" + user.crusrHead;
            $('#headImg').css({
                                    'background':'url("'+headPath+'")',
                                    '-webkit-background-size': '114px 114px',
                                    'background-size': '114px 114px',
                                    'border-radius': '57px'
                                });
        }
        
    },

    to_my_information: function() {
        var user = JdataGet("user");
        Views.personInfoView.willShow(user.crusrUuid);
    },

    to_pending_trial: function() {
        dataSave('processType', "pending");
    	Views.pendingTrialListView.willShow();
    },

    to_technological_process: function() {
        dataSave('processType', "process");
    	Views.pendingTrialListView.willShow();
    },

    to_company_group: function() {
        var user = JdataGet("user");
        if (user.crusrJob == 1) { /*超级管理员*/
    	   Views.companyListView.willShow();
        }
        if (user.crusrJob == 2){ /*集团管理员*/
           dataSave('company_details_uuid', user.crusrCompany);
           Views.companyDetailsOnlyView.willShow(user.crusrCompany);
        }
    },

    to_project: function() {
        var user = JdataGet("user");
        if (user.crusrJob == 1 || user.crusrJob == 2) { /*超级管理员或集团管理员*/
    	   Views.projectListView.willShow();
        } else { /*职位人员*/
           dataSave('project_details_uuid', user.crusrProject);
           Views.projectDetailsOnlyView.willShow(user.crusrProject);
        }

    },

    to_capital_pool: function() {
        var user = JdataGet("user");
        if (user.crusrJob == 1) { /*超级管理员*/
    	    Views.capitalPoolBlueView.show();
        } 
        if (user.crusrJob == 2){ /*集团管理员*/
            Views.capitalPoolYellowView.willShow(user.crusrCompany);
        }
    },

    to_capital_investor: function() {
        var user = JdataGet("user");
        if (user.crusrJob != 1){ /*非超级管理员*/
            Views.capitalInvestorView.show();
        }
    },

    to_person_search: function() {
        var user = JdataGet("user");
        if (user.crusrJob == 1){ /*超级管理员*/
            Views.personSearchListView.willShow();
        }
    },

    to_investors: function() {
        Views.investorListView.willShow();
    },

    to_applyFor: function() {
        dataSave('picUuids', "");
        dataSave("applyForName", "填写");
        dataSave("applyForIntroduce", "填写");
        dataSave('applyForNote', "");
    	Views.capitalApplyForView.willShow();
    },

    to_appropriation: function() {
        var user = JdataGet("user");
        dataSave('picUuids', "");
        dataSave('appropriation_crusrProject',user.crusrProject)

        dataSave('changeName', "填写");
        dataSave('changeMeney', "填写");
        dataSave('changeIntroduce', "填写");
        dataSave('leave', "");

    	Views.capitalPoolAppropriationView.willShow();
    },

    to_mchange_password: function() {
        var user = JdataGet("user");
    	Views.changePasswordView.willShow(user.crusrUuid);
    },

    to_exit: function() {
        Views.loginView.show({}, true);
    }
});