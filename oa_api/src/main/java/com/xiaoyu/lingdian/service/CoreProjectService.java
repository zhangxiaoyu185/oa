package com.xiaoyu.lingdian.service;

import com.xiaoyu.lingdian.entity.CoreProject;
import java.util.List;
import java.util.Map;
import com.xiaoyu.lingdian.core.mybatis.page.Page;

/**
* 项目表
*/
public interface CoreProjectService {

	/**
	* 项目名称模糊查询所有List
	* @return List
	*/
	public List<CoreProject> findCoreProjectByName(String crproName);
	
	/**
	* 添加
	* @param coreProject
	* @return
	*/
	public boolean insertCoreProject(CoreProject coreProject);

	/**
	* 修改
	* @param coreProject
	* @return
	*/
	public boolean updateCoreProject(CoreProject coreProject);

	/**
	* 禁用
	* @param coreProject
	* @return
	*/
	public boolean disableCoreProject(CoreProject coreProject);
	
	/**
	* 查询
	* @param coreProject
	* @return
	*/
	public CoreProject getCoreProject(CoreProject coreProject);

	/**
	* 根据名称查询
	* @param crproName
	* @return
	*/
	public CoreProject getCoreProjectByName(String crproName);
	
	/**
	* 查询锁行,为了拨款和还款时的项目资金额度操作
	* @param crproUuid
	* @return
	*/
	public CoreProject getCoreProjectForUpdate(String crproUuid);
	
	/**
	* 查询所有List
	* @return List
	*/
	public List<CoreProject> findCoreProjectAll();

	/**
	* 查询所有List,返回Map
	* @return Map
	*/
	public Map<String, CoreProject> findCoreProjectMap();
	
	/**
	* 查询所有Page
	* @return Page
	*/
	public Page<CoreProject> findCoreProjectPage(int pageNum, int pageSize, String crproCompany);

	/**
	* 公司下的项目集合
	* @return Page
	*/
	public List<CoreProject> findCoreProjectList(String crproCompany);

}