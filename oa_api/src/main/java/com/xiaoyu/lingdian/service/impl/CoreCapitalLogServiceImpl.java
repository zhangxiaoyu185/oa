package com.xiaoyu.lingdian.service.impl;

import com.xiaoyu.lingdian.service.CoreCapitalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaoyu.lingdian.core.mybatis.dao.MyBatisDAO;
import com.xiaoyu.lingdian.entity.CoreCapitalLog;

/**
* 资金变动表
*/
@Service("coreCapitalLogService")
public class CoreCapitalLogServiceImpl implements CoreCapitalLogService {

	@Autowired
	private MyBatisDAO myBatisDAO;

	@Override
	public boolean insertCoreCapitalLog(CoreCapitalLog coreCapitalLog) {
		myBatisDAO.insert(coreCapitalLog);
		return true;
	}

}