var config = {};

config.api = [
	{
		module : "main",
		payload : [ 
			{
				name : "登录",
				id : "login",
				path : "main/login",
				path2 : "main/login",
				method : "post",
				params : {
					USERCODE : '002',
					PWD : '123'
				}
			},
			{
				name : "完成任务/项目/模块员工",
				id : "finishTaskOrProject",
				path : "main/finishTaskOrProject",
				path2 : "main/finishTaskOrProject",
				method : "post",
				params : {
					FINISH_TYPE : 3,
					ID : 1
				}
			}
		]
	},{
		module : "project",
		payload : [
		    {
		        name : "插入项目",
				id : "insertProject",
				path : "project/insertProject",
				path2 : "project/insertProject",
				method : "post",
				params : {
					USERCODE : '002',
					PROJECT_NAME : '1',
					STARTTIME : '2015-08-09',
					ENDTIME : '2015-09-09',
					ALLTIME : 50,
					STATUS : 1,
					REMARKS : '1'
				} 
		    }
		]
	}
];

var _attachId = function(api) {
	for (var i = 0; i < api.length; i++) {
		var mid = "m" + i;
		api[i].id = mid;
		var pd = api[i].payload;
		for (var j = 0; j < pd.length; j++) {
			var aid = "i" + j;
			pd[j].id = mid + aid;

		}
	}

};
_attachId(config.api);

if ( typeof module === "object" && module && typeof module.exports === "object" ) {	
	module.exports = config.api;
}