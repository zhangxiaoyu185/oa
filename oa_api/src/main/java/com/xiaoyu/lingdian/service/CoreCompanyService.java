package com.xiaoyu.lingdian.service;

import com.xiaoyu.lingdian.entity.CoreCompany;

import java.util.List;
import java.util.Map;

import com.xiaoyu.lingdian.core.mybatis.page.Page;

/**
* 集团表
*/
public interface CoreCompanyService {

	/**
	* 添加
	* @param coreCompany
	* @return
	*/
	public boolean insertCoreCompany(CoreCompany coreCompany);

	/**
	* 修改
	* @param coreCompany
	* @return
	*/
	public boolean updateCoreCompany(CoreCompany coreCompany);

	/**
	* 禁用
	* @param coreCompany
	* @return
	*/
	public boolean disableCoreCompany(CoreCompany coreCompany);
	
	/**
	* 查询
	* @param coreCompany
	* @return
	*/
	public CoreCompany getCoreCompany(CoreCompany coreCompany);

	/**
	* 根据名称查询
	* @param crgroName
	* @return
	*/
	public CoreCompany getCoreCompanyByName(String crgroName);
	
	/**
	* 查询所有List
	* @return List
	*/
	public List<CoreCompany> findCoreCompanyAll();

	/**
	* 查询所有List,返回Map
	* @return Map
	*/
	public Map<String, CoreCompany> findCoreCompanyMap();
	
	/**
	* 获取分页集团列表
	* @return Page
	*/
	public Page<CoreCompany> findCoreCompanyPage(int pageNum, int pageSize);

//<=================定制内容开始==============
//==================定制内容结束==============>

}