package com.xiaoyu.lingdian.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import com.xiaoyu.lingdian.core.mybatis.page.Page;
import com.xiaoyu.lingdian.core.mybatis.page.PageRequest;
import com.xiaoyu.lingdian.service.CoreFlowLogService;
import com.xiaoyu.lingdian.service.CoreFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaoyu.lingdian.core.mybatis.dao.MyBatisDAO;
import com.xiaoyu.lingdian.entity.CoreFlow;
import com.xiaoyu.lingdian.entity.CoreFlowLog;

/**
* 流程表
*/
@Service("coreFlowService")
public class CoreFlowServiceImpl implements CoreFlowService {

	@Autowired
	private MyBatisDAO myBatisDAO;

	@Autowired
	private CoreFlowLogService coreFlowLogService;
	
	@Override
	public boolean insertCoreFlow(CoreFlow coreFlow) {
		myBatisDAO.insert(coreFlow);
		coreFlow = this.getCoreFlow(coreFlow.getCrflwCode());
		CoreFlowLog coreFlowLog = new CoreFlowLog();
		coreFlowLog.convertFlowToFlowLog(coreFlow);
		this.coreFlowLogService.insertCoreFlowLog(coreFlowLog);
		return true;
	}

	//需要插入日志表
	@Override
	public boolean updateCoreFlowBase(String crflwName, String crflwDesc, double crflwLoanMoney, String crflwRemarks, int crflwStatus,
			int crflwUptype, String crflwUpdateUser, int crflwUpdateJob, Date crflwUdate, String crflwCode, int oldCrflwStep) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crflwName", crflwName);
		hashMap.put("crflwDesc", crflwDesc);
		hashMap.put("crflwLoanMoney", crflwLoanMoney);
		hashMap.put("crflwRemarks", crflwRemarks);
		hashMap.put("crflwStatus", crflwStatus);
		hashMap.put("crflwUptype", crflwUptype);
		hashMap.put("crflwUpdateUser", crflwUpdateUser);
		hashMap.put("crflwUpdateJob", crflwUpdateJob);
		hashMap.put("crflwUdate", crflwUdate);
		hashMap.put("crflwCode", crflwCode);
		hashMap.put("oldCrflwStep", oldCrflwStep);
		int count = myBatisDAO.update("updateCoreFlowBase", hashMap);
		if (count > 0) {
			CoreFlow coreFlow = this.getCoreFlow(crflwCode);
			CoreFlowLog coreFlowLog = new CoreFlowLog();
			coreFlowLog.convertFlowToFlowLog(coreFlow);
			this.coreFlowLogService.insertCoreFlowLog(coreFlowLog);
			return true;
		}
		return false;
	}

	//不需要插入日志表
	@Override
	public boolean updateCoreFlowStep(int crflwStep, String crflwCode, int oldCrflwStep) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crflwStep", crflwStep);
		hashMap.put("crflwCode", crflwCode);
		hashMap.put("oldCrflwStep", oldCrflwStep);
		myBatisDAO.update("updateCoreFlowStep", hashMap);
		return true;
	}

	@Override
	public CoreFlow getCoreFlowForUpdate(String crflwCode) {
		CoreFlow coreFlow = new CoreFlow();
		coreFlow.setCrflwCode(crflwCode);
		return (CoreFlow) myBatisDAO.findForObject(coreFlow);
	}
	
	@Override
	public CoreFlow getCoreFlow(String crflwCode) {
		CoreFlow coreFlow = new CoreFlow();
		coreFlow.setCrflwCode(crflwCode);
		return (CoreFlow) myBatisDAO.findForObject(coreFlow);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreFlow> findCoreFlowPageByCoreInvestment(int pageNum, int pageSize, List<String> list) {
		if (list.size() <= 0) {
			return new Page<CoreFlow>(0, 10, 0);
		}
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("list", list);
		return myBatisDAO.findForPage("findCoreFlowForPagesByInvestment", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreFlow> findCoreFlowForPagesByCompany(int pageNum, int pageSize, String crusrCompany) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crusrCompany", crusrCompany);
		return myBatisDAO.findForPage("findCoreFlowForPagesByCompany", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreFlow> findCoreFlowForPagesByProject(int pageNum, int pageSize, String crusrProject) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crusrProject", crusrProject);
		return myBatisDAO.findForPage("findCoreFlowForPagesByProject", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreFlow> findCoreFlowForPagesByFinance(int pageNum, int pageSize, String crusrProject) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crusrProject", crusrProject);
		return myBatisDAO.findForPage("findCoreFlowForPagesByFinance", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreFlow> findCoreFlowForPagesByAdmin(int pageNum, int pageSize) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		return myBatisDAO.findForPage("findCoreFlowForPagesByAdmin", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreFlow> findCoreFlowForPagesByProjectList(int pageNum, int pageSize, List<String> list) {
		if (list.size() <= 0) {
			return new Page<CoreFlow>(0, 10, 0);
		}
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("list", list);
		return myBatisDAO.findForPage("findCoreFlowForPagesByProjectList", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreFlow> findCoreFlowPendingForPagesByAdmin(int pageNum, int pageSize) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		return myBatisDAO.findForPage("findCoreFlowPendingForPagesByAdmin", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreFlow> findCoreFlowPendingForPagesByChannel(int pageNum, int pageSize, String crflwApplyUser) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crflwApplyUser", crflwApplyUser);
		return myBatisDAO.findForPage("findCoreFlowPendingForPagesByChannel", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreFlow> findCoreFlowPendingForPagesByPlan(int pageNum, int pageSize, String crflwApplyUser, String crflwProject) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crflwApplyUser", crflwApplyUser);
		hashMap.put("crflwProject", crflwProject);
		List<CoreFlow> flowList1 = myBatisDAO.findForList("findCoreFlowPendingByPlanPlan", hashMap);
		List<CoreFlow> flowList2 = myBatisDAO.findForList("findCoreFlowPendingByPlanChannel", hashMap);
		List<String> list = new ArrayList<String>();
		for (CoreFlow coreFlow : flowList1) {
			list.add(coreFlow.getCrflwCode());
		}
		for (CoreFlow coreFlow : flowList2) {
			list.add(coreFlow.getCrflwCode());
		}
		return this.findCoreFlowPageByCoreInvestment(pageNum, pageSize, list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreFlow> findCoreFlowPendingForPagesByProject(int pageNum, int pageSize, int crflwStep, String crflwProject) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crflwStep", crflwStep);
		hashMap.put("crflwProject", crflwProject);
		return myBatisDAO.findForPage("findCoreFlowPendingForPagesByProject", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreFlow> findCoreFlowPendingForPagesByFinance(int pageNum, int pageSize, String crflwProject, String crflwApplyUser) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crflwProject", crflwProject);
		hashMap.put("crflwApplyUser", crflwApplyUser);
		List<CoreFlow> flowList1 = myBatisDAO.findForList("findCoreFlowPendingByFinanceApply", hashMap);
		List<CoreFlow> flowList2 = myBatisDAO.findForList("findCoreFlowPendingByFinanceByFunding", hashMap);
		List<String> list = new ArrayList<String>();
		for (CoreFlow coreFlow : flowList1) {
			list.add(coreFlow.getCrflwCode());
		}
		for (CoreFlow coreFlow : flowList2) {
			list.add(coreFlow.getCrflwCode());
		}
		return this.findCoreFlowPageByCoreInvestment(pageNum, pageSize, list);
	}

}