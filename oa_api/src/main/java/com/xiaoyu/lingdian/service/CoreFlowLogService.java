package com.xiaoyu.lingdian.service;

import com.xiaoyu.lingdian.entity.CoreFlowLog;
import java.util.List;

/**
* 流程日志表
*/
public interface CoreFlowLogService {

	/**
	* 根据流程编号获取流程日志list
	* @param crflwCode
	* @return
	*/
	public List<CoreFlowLog> findCoreFlowLogListsByCrflwCode(String crflwCode);
	
	/**
	* 添加
	* @param coreFlowLog
	* @return
	*/
	public boolean insertCoreFlowLog(CoreFlowLog coreFlowLog);

}