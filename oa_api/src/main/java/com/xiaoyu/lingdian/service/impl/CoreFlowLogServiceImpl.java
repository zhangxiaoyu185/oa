package com.xiaoyu.lingdian.service.impl;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import com.xiaoyu.lingdian.service.CoreFlowLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaoyu.lingdian.core.mybatis.dao.MyBatisDAO;
import com.xiaoyu.lingdian.entity.CoreFlowLog;

/**
* 流程日志表
*/
@Service("coreFlowLogService")
public class CoreFlowLogServiceImpl implements CoreFlowLogService {

	@Autowired
	private MyBatisDAO myBatisDAO;

	@Override
	public boolean insertCoreFlowLog(CoreFlowLog coreFlowLog) {
		myBatisDAO.insert(coreFlowLog);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoreFlowLog> findCoreFlowLogListsByCrflwCode(String crflwCode) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crflwCode", crflwCode);
		return myBatisDAO.findForList("findCoreFlowLogListsByCrflwCode", hashMap);
	}

}