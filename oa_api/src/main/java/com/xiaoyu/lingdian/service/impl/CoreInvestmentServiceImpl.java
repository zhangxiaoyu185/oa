package com.xiaoyu.lingdian.service.impl;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import com.xiaoyu.lingdian.service.CoreInvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaoyu.lingdian.core.mybatis.dao.MyBatisDAO;
import com.xiaoyu.lingdian.core.mybatis.page.Page;
import com.xiaoyu.lingdian.core.mybatis.page.PageRequest;
import com.xiaoyu.lingdian.entity.CoreInvestment;

/**
* 投资拨款流程表
*/
@Service("coreInvestmentService")
public class CoreInvestmentServiceImpl implements CoreInvestmentService {

	@Autowired
	private MyBatisDAO myBatisDAO;

	@Override
	public boolean insertCoreInvestment(CoreInvestment coreInvestment) {
		myBatisDAO.insert(coreInvestment);
		return true;
	}

	@Override
	public boolean deleteCoreInvestment(CoreInvestment coreInvestment) {
		myBatisDAO.delete(coreInvestment);
		return true;
	}

	@Override
	public CoreInvestment getCoreInvestment(CoreInvestment coreInvestment) {
		return (CoreInvestment) myBatisDAO.findForObject(coreInvestment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoreInvestment> findCoreInvestmentByCrsptUser(String crsptUser) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crsptUser", crsptUser);
		return myBatisDAO.findForList("findCoreInvestmentByCrsptUser", hashMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreInvestment> findCoreInvestmentPageByProjectName(int pageNum, int pageSize, List<String> list) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("list", list);
		return myBatisDAO.findForPage("findCoreInvestmentByProjects", new PageRequest(pageNum, pageSize, hashMap));
	}

}