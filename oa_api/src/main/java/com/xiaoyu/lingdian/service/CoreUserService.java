package com.xiaoyu.lingdian.service;

import com.xiaoyu.lingdian.entity.CoreUser;
import java.util.List;
import java.util.Map;
import com.xiaoyu.lingdian.core.mybatis.page.Page;

/**
* 用户表
*/
public interface CoreUserService {
	
	/**
	* 根据用户名查找用户信息
	* @param coreUser
	* @return
	*/
	public CoreUser getCoreUserByName(CoreUser coreUser);

	/**
	* 添加
	* @param coreUser
	* @return
	*/
	public boolean insertCoreUser(CoreUser coreUser);

	/**
	* 修改
	* @param coreUser
	* @return
	*/
	public boolean updateCoreUser(CoreUser coreUser);

	/**
	* 禁用
	* @param coreUser
	* @return
	*/
	public boolean disableCoreUser(CoreUser coreUser);

	/**
	* 查询
	* @param coreUser
	* @return
	*/
	public CoreUser getCoreUser(CoreUser coreUser);

	/**
	* 查询所有用户
	* @return List
	*/
	public List<CoreUser> findCoreUserAll();

	/**
	* 查询所有用户
	* @return Map
	*/
	public Map<String, CoreUser> findCoreUserMap();
	
	/**
	* 获取集团下管理员分页列表
	* @return Page
	*/
	public Page<CoreUser> findCoreUserForPagesByCompany(int pageNum, int pageSize, String crusrCompany);
	
	/**
	* 获取项目下各职位人员分页列表
	* @return Page
	*/
	public Page<CoreUser> findCoreUserForPagesByProject(int pageNum, int pageSize, Integer crusrJob, String crusrProject);
	
	/**
	* 获取投资客分页列表
	* @return Page
	*/
	public Page<CoreUser> findCoreUserForPagesByInvestors(int pageNum, int pageSize);
	
	/**
	* 用户列表根据用户名或登录名或手机号模糊查询
	* @return Page
	*/
	public Page<CoreUser> findCoreUserForPagesLikes(int pageNum, int pageSize, String objectStr);

}