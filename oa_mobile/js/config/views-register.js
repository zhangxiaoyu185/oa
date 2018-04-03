// ------------------------------------------------------------------------
// Views registry
//
var Views = {
	tmpl: {
		login: 'views/tmpl/login/login.html:登录',

		index:'views/tmpl/index.html:首页:#eee',//#... 表示body背景颜色
		
		personInfo: 'views/tmpl/person/personInfo.html:人员信息:#eee',
		personInfoOnly: 'views/tmpl/person/personInfoOnly.html:查看人员信息:#eee',
		personInfoUpdate: 'views/tmpl/person/personInfoUpdate.html:编辑人员信息:#eee',
		changePassword: 'views/tmpl/person/changePassword.html:修改密码:#eee',
		personSearchList: 'views/tmpl/person/personSearchList.html:搜索人员:#eee',
		personSearchListItem: 'views/tmpl/person/personSearchListItem.html',


		createCompany: 'views/tmpl/companyGroup/createCompany.html:创建集团:#eee',
		companyList: 'views/tmpl/companyGroup/companyList.html:集团列表:#eee',
		companyListItem: 'views/tmpl/companyGroup/companyListItem.html',
		companyDetails: 'views/tmpl/companyGroup/companyDetails.html:编辑集团详情:#eee',
		companyDetailsOnly: 'views/tmpl/companyGroup/companyDetailsOnly.html:查看集团详情:#eee',
		administratorList: 'views/tmpl/companyGroup/administratorList.html:集团管理员列表:#eee',
		administratorListItem: 'views/tmpl/companyGroup/administratorListItem.html',
		createAdministrator: 'views/tmpl/companyGroup/createAdministrator.html:创建集团管理员:#eee',

		pendingTrialList: 'views/tmpl/pendingTrial/pendingTrialList.html:待审列表:#eee',
		pendingTrialListItem: 'views/tmpl/pendingTrial/pendingTrialListItem.html',
		pendingTrialDesc: 'views/tmpl/pendingTrial/pendingTrialDesc.html:流程详情:#eee',
		
		processDirList: 'views/tmpl/comm/processDirList.html:流程走向:#eee',
		processDirListItem: 'views/tmpl/comm/processDirListItem.html',
		picAttachment: 'views/tmpl/comm/picAttachment.html:图片附件:#eee',
		startPicAttachment: 'views/tmpl/comm/startPicAttachment.html:图片附件:#eee',
		electronicInvoice: 'views/tmpl/comm/electronicInvoice.html:电子发票:#eee',

		createInvestor: 'views/tmpl/investors/createInvestor.html:创建投资客:#eee',
		investorProcessList: 'views/tmpl/investors/investorProcessList.html:投资拨款流程:#eee',
		investorProcessListItem: 'views/tmpl/investors/investorProcessListItem.html',
		investorList: 'views/tmpl/investors/investorList.html:投资客列表:#eee',
		investorListItem: 'views/tmpl/investors/investorListItem.html',
		investorDesc: 'views/tmpl/investors/investorDesc.html:投资客详情:#eee',

		createProject: 'views/tmpl/project/createProject.html:创建项目:#eee',
		projectList: 'views/tmpl/project/projectList.html:项目列表:#eee',
		projectListItem: 'views/tmpl/project/projectListItem.html',
		projectDetails: 'views/tmpl/project/projectDetails.html:编辑项目详情:#eee',
		projectDetailsOnly: 'views/tmpl/project/projectDetailsOnly.html:查看项目详情:#eee',
		workerList: 'views/tmpl/project/workerList.html:职员列表:#eee',
		workerListItem: 'views/tmpl/project/workerListItem.html',
		createWorker: 'views/tmpl/project/createWorker.html:创建职员:#eee',

		capitalPoolBlue: 'views/tmpl/capitalPool/capitalPoolBlue.html:超级管理员资金池:#fff',
		capitalPoolBlueItem: 'views/tmpl/capitalPool/capitalPoolBlueItem.html',
		capitalPoolYellow: 'views/tmpl/capitalPool/capitalPoolYellow.html:集团资金池:#fff',
		capitalPoolYellowItem: 'views/tmpl/capitalPool/capitalPoolYellowItem.html',
		capitalPoolAppropriation: 'views/tmpl/capitalPool/capitalPoolAppropriation.html:申请拨款:#fff',
		capitalApplyFor: 'views/tmpl/capitalPool/capitalApplyFor.html:申请资金:#fff',
		capitalInvestor: 'views/tmpl/capitalPool/capitalInvestor.html:投资客资金池:#fff',

		test: 'views/tmpl/test.html:页面一',
		test2: 'views/tmpl/test2.html:页面二',
		test3: 'views/tmpl/test3.html:页面3',
		listTest: 'views/tmpl/listTest.html:测试列表分页',
		listTestItem: 'views/tmpl/listTestItem.html:测试列表Item',
		upImgTest: 'views/tmpl/upImgTest.html:上传图片控件'
	},
	initTmpl: 'electronicInvoice',//启动页的配置
	// initData: {list: ['文艺1', '博客2', '摄影3', '电影4', '民谣5', '旅行6', '吉他7']},//启动页的初始化数据，一般不需要测试用
	errMsg: {
		NETWORK_ERROR: '网络繁忙，请稍后重试:-(',
		LOGIN_NO_USERNAME: '请输入手机号',
		LOGIN_NO_PASSWORD: '请输入密码',
		SESSION_TIMEOUT: '您的登录已过期！'
	}
};