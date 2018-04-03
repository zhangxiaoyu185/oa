package com.xiaoyu.lingdian.service;

import com.xiaoyu.lingdian.core.mybatis.page.Page;
import com.xiaoyu.lingdian.entity.CoreInvestment;
import java.util.List;

/**
* 投资拨款流程表
*/
public interface CoreInvestmentService {

	/**
	* 根据投资客获取投资的全部拨款流程编码
	* @param crsptUser
	* @return
	*/
	public List<CoreInvestment> findCoreInvestmentByCrsptUser(String crsptUser);
	
	/**
	* 添加
	* @param coreInvestment
	* @return
	*/
	public boolean insertCoreInvestment(CoreInvestment coreInvestment);

	/**
	* 删除
	* @param coreInvestment
	* @return
	*/
	public boolean deleteCoreInvestment(CoreInvestment coreInvestment);

	/**
	* 查询
	* @param coreInvestment
	* @return
	*/
	public CoreInvestment getCoreInvestment(CoreInvestment coreInvestment);
	
	/**
	* 根据项目uuids查询拨款流程分页列表查询所有Page
	* @return Page
	*/
	public Page<CoreInvestment> findCoreInvestmentPageByProjectName(int pageNum, int pageSize, List<String> list);

}