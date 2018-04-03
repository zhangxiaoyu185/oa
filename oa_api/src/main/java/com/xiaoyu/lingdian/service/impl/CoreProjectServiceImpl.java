package com.xiaoyu.lingdian.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import com.xiaoyu.lingdian.core.mybatis.mapper.SimpleMapMapper;
import com.xiaoyu.lingdian.core.mybatis.page.Page;
import com.xiaoyu.lingdian.core.mybatis.page.PageRequest;
import com.xiaoyu.lingdian.service.CoreProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoyu.lingdian.core.mybatis.dao.MyBatisDAO;
import com.xiaoyu.lingdian.entity.CoreProject;

/**
* 项目表
*/
@Service("coreProjectService")
public class CoreProjectServiceImpl implements CoreProjectService {

	private static final SimpleMapMapper<String, CoreProject> PROJECT_UUID_MAPPER = new SimpleMapMapper<String, CoreProject>() {
		@Override
		public String mapKey(CoreProject object, int rowNum) {
			return object.getCrproUuid();
		}
	};
	
	@Autowired
	private MyBatisDAO myBatisDAO;

	@SuppressWarnings("unchecked")
	@Override
	public List<CoreProject> findCoreProjectByName(String crproName) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crproName", crproName);
		return myBatisDAO.findForList("findCoreProjectByName", hashMap);
	}
	
	@Override
	public boolean insertCoreProject(CoreProject coreProject) {
		myBatisDAO.insert(coreProject);
		return true;
	}

	@Override
	public boolean updateCoreProject(CoreProject coreProject) {
		myBatisDAO.update(coreProject);
		return true;
	}
	
	@Override
	public boolean disableCoreProject(CoreProject coreProject) {
		coreProject.setCrproStatus(0);
		coreProject.setCrproUdate(new Date());
		myBatisDAO.update(coreProject);
		return true;
	}
	
	@Override
	public CoreProject getCoreProject(CoreProject coreProject) {
		return (CoreProject) myBatisDAO.findForObject(coreProject);
	}

	@Override
	public CoreProject getCoreProjectByName(String crproName) {
		CoreProject coreProject = new CoreProject();
		coreProject.setCrproName(crproName);
		return (CoreProject) myBatisDAO.findForObject("getCoreProjectByName", coreProject);
	}

	@Override
	public CoreProject getCoreProjectForUpdate(String crproUuid) {
		CoreProject coreProject = new CoreProject();
		coreProject.setCrproUuid(crproUuid);
		return (CoreProject) myBatisDAO.findForObject("getCoreProjectForUpdate", coreProject);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CoreProject> findCoreProjectAll() {
		return myBatisDAO.findForList("findCoreProjectAll", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  Map<String, CoreProject> findCoreProjectMap() {
		return myBatisDAO.findForMap("findCoreProjectAll", PROJECT_UUID_MAPPER);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreProject> findCoreProjectPage(int pageNum, int pageSize, String crproCompany) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crproCompany", crproCompany);
		return myBatisDAO.findForPage("findCoreProjectForPages", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoreProject> findCoreProjectList(String crproCompany) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crproCompany", crproCompany);
		return myBatisDAO.findForList("findCoreProjectForPages", hashMap);
	}

}