/************************登录********************************/
Views.loginView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'login'
    },

    willShow: function(param, isBackPage) {
        this.show(param, isBackPage);
    },

    didShow: function() {
        var account_name = dataGet('account_name');
        var account_password = dataGet('account_password')
        if(account_name){
            $('#account_name').val(account_name);
        }
        if(account_password){
            $('#account_password').val(account_password);

            $('#remember_flag').removeClass('remember_nocheck').addClass('remember_checked')
            
        }
    },

    login: function() {
        var account_name = $('#account_name').val();
        if(!account_name){
            alert("请输入您的帐号");
            return;
        }
        var account_password = $('#account_password').val();
        if(!account_password){
            alert("请输入您的密码");
            return;
        }
        var data = {
            crusrName: account_name,
            crusrPassword: hex_md5(account_password)
        }

        var url = WEB_URL + "coreUser/login";
        ajaxTool("post",data,url,
            function error(XMLHttpRequest, textStatus, errorThrown,fnErr){
                alert("error:" + data);
            },
            function success(data){
                if(!data.success) {
                    alert(data.errMsg);
                }else{
                   if($('#remember_flag').hasClass('remember_checked')) {
                        dataSave('account_name', account_name);
                        dataSave('account_password', account_password);
                   }else {
                        dataSave('account_name', '');
                        dataSave('account_password', '');
                   }
                   JdataSave("user", data.data); 
                   Views.indexView.willShow({data: data.data}); 
                }
            },true
        );
	
	}


});
