package com.xiaoyu.lingdian.service;

import com.xiaoyu.lingdian.entity.CoreCapitalLog;

/**
* 资金变动表
*/
public interface CoreCapitalLogService {

	/**
	* 添加
	* @param coreCapitalLog
	* @return
	*/
	public boolean insertCoreCapitalLog(CoreCapitalLog coreCapitalLog);

}