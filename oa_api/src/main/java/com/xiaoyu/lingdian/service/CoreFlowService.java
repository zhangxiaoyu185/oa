package com.xiaoyu.lingdian.service;

import com.xiaoyu.lingdian.entity.CoreFlow;

import java.util.Date;
import java.util.List;
import com.xiaoyu.lingdian.core.mybatis.page.Page;

/**
* 流程表
*/
public interface CoreFlowService {

	/**
	* 添加
	* @param coreFlow
	* @return
	*/
	public boolean insertCoreFlow(CoreFlow coreFlow);

	/**
	* 更新基本信息,为了保持更新事务一致性,需判断上一环节
	* @return
	*/
	public boolean updateCoreFlowBase(String crflwName, String crflwDesc, double crflwLoanMoney, String crflwRemarks, int crflwStatus, 
			int crflwUptype, String crflwUpdateUser, int crflwUpdateJob, Date crflwUdate, String crflwCode, int oldCrflwStep);

	/**
	* 更新流程环节,为了保持更新事务一致性,需判断上一环节
	* @return
	*/
	public boolean updateCoreFlowStep(int crflwStep, String crflwCode, int oldCrflwStep);
	
	/**
	* 查询
	* @param crflwCode
	* @return
	*/
	public CoreFlow getCoreFlow(String crflwCode);

	/**
	* 为了保持更新事务一致性,更新前需要先锁行
	* @param crflwCode
	* @return
	*/
	public CoreFlow getCoreFlowForUpdate(String crflwCode);

	/**
	* 投资客的流程分页列表（根据流程编码集合查询）
	* @return Page
	*/
	public Page<CoreFlow> findCoreFlowPageByCoreInvestment(int pageNum, int pageSize, List<String> list);

	/**
	* 集团管理员的流程分页列表
	* @return Page
	*/
	public Page<CoreFlow> findCoreFlowForPagesByCompany(int pageNum, int pageSize, String crusrCompany);
	
	/**
	* 项目人员的流程分页列表
	* @return Page
	*/
	public Page<CoreFlow> findCoreFlowForPagesByProject(int pageNum, int pageSize, String crusrProject);
	
	/**
	* 财务的流程分页列表
	* @return Page
	*/
	public Page<CoreFlow> findCoreFlowForPagesByFinance(int pageNum, int pageSize, String crusrProject);
	
	/**
	* 超级管理员的流程分页列表 
	* @return Page
	*/
	public Page<CoreFlow> findCoreFlowForPagesByAdmin(int pageNum, int pageSize);
	
	/**
	* 根据项目集合查询拨款流程分页列表(查询时用)
	* @return Page
	*/
	public Page<CoreFlow> findCoreFlowForPagesByProjectList(int pageNum, int pageSize, List<String> list);
	
	/**
	* 超级管理员的待审分页列表
	* @return Page
	*/
	public Page<CoreFlow> findCoreFlowPendingForPagesByAdmin(int pageNum, int pageSize);
	
	/**
	* 渠道的待审分页列表
	* @return Page
	*/
	public Page<CoreFlow> findCoreFlowPendingForPagesByChannel(int pageNum, int pageSize, String crflwApplyUser);
	
	/**
	* 策划的待审分页列表
	* @return Page
	*/
	public Page<CoreFlow> findCoreFlowPendingForPagesByPlan(int pageNum, int pageSize, String crflwApplyUser, String crflwProject);
	
	/**
	* 营销总和项目总的待审分页列表 
	* @return Page
	*/
	public Page<CoreFlow> findCoreFlowPendingForPagesByProject(int pageNum, int pageSize, int crflwStep, String crflwProject);
	
	/**
	* 财务的待审分页列表
	* @return Page
	*/
	public Page<CoreFlow> findCoreFlowPendingForPagesByFinance(int pageNum, int pageSize, String crflwProject, String crflwApplyUser);
	
}