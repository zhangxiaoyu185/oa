Views.listTestView =  $.extend({}, Views.PanelView, {
    options: {
        tmpl: 'listTest'
    },

    willShow: function() {

    },

    didShow: function() {
    	// loaded ();
    },

    scrollInitRender: function() {
        this.scrollInit('listTestItem',{list:[199,299,399,499,599]});
    },

    scrollDownRender: function() {
        this.scrollDown('listTestItem',{list:[1,2,3]});
    },

    scrollUpRender: function() {
        // this.scrollUp('listTestItem',{list:[1,2,3,4,5]});
        this.ajaxList('scrollUpRender');
    },

    ajaxList: function(type) {
        var self = this;
        if(type=='scrollUpRender') {
            self.scrollUp('listTestItem',{list:[1,2,3,4,5]});
        }
    },

    listBtnTest: function(btn) {
        // var a = $(btn).attr('data-val');
        // alert(a);
        Views.changePasswordView.show();
    },

});

