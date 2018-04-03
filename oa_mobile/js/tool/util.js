// 通用方法
var Util = {
	debug: APPVERSION == 'web',
	log: function(msg) {
		if(this.debug) {
			return;
		}
		if(typeof msg === 'object')
			msg = JSON.stringify(msg);
		console.log('=====>>>>> ' + msg + ' <<<<<=====');
	},
	rtime: function() {
		return (Math.floor(Math.random() * 10001)).toString() + (new Date().getTime()).toString();
	}
}

// ------------------------------------------------------------------------
// Validator
//
var Validator = {
	phoneNo: /^\d{8,11}$/,
	mobileNo: /^1[3|4|5|8][0-9]\d{8}$/,
	bankCardNo: /^\d{16,19}$|^\d{6}[- ]\d{10,13}$|^\d{4}[- ]\d{4}[- ]\d{4}[- ]\d{4,7}$/,
	email: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
	realName: /^[\u4e00-\u9fa5]{2,5}$/,
	idCard: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
	medicalCardNo:/^[0-9a-zA-Z]+$/,
	num_en: /^[0-9a-zA-Z]{6,16}$/,
	letter: /^[a-zA-Z]+$/,
	ip: /(\d+\.){3}\d+/,
	stringIsMobileNo: function(s) {
		return this.mobileNo.test(s);
	},
	stringIsIdCard: function(s) {
		return this.idCard.test(s);
	},
	stringIdBankCardNo: function(s) {
		return this.bankCardNo.test(s);
	},
	stringContainsSpace: function(s) {
		return (/\s/).test(s);
	},
	stringIsNumOrEn:function (s) {
		return this.num_en.test(s);
	},
	isValidIp: function(ip) {
		var result = this.ip.test(ip);
		if (result) {
			var ips = ip.split('.');
			for (var i = 0; i < ips.length; ++i) {
				var a = ips[i];
				if (a-0 > 255 && ('' + (a-0) === a)) {
					result = false;
					break;
				}
			}
		}
		return result;
	},
	isValidIpPort: function(ipport) {
		var idx = ipport.indexOf(':');
		if (idx > -1) {
			var ipp = ipport.split(':'),
				ip = ipp[0],
				port = ipp[1],
				portNum = port-0;
			if (/\d+/.test(port) && (portNum > 0 && portNum <= 65535) && ('' + portNum === port))
				return this.isValidIp(ip);
		} else {
			return this.isValidIp(ipport);
		}
		return false;
	}
};