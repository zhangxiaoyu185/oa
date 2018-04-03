package com.xiaoyu.lingdian.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import com.xiaoyu.lingdian.core.mybatis.mapper.SimpleMapMapper;
import com.xiaoyu.lingdian.core.mybatis.page.Page;
import com.xiaoyu.lingdian.core.mybatis.page.PageRequest;
import com.xiaoyu.lingdian.service.CoreUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaoyu.lingdian.core.mybatis.dao.MyBatisDAO;
import com.xiaoyu.lingdian.entity.CoreUser;

/**
* 用户表
*/
@Service("coreUserService")
public class CoreUserServiceImpl implements CoreUserService {

	private static final SimpleMapMapper<String, CoreUser> USER_UUID_MAPPER = new SimpleMapMapper<String, CoreUser>() {
		@Override
		public String mapKey(CoreUser object, int rowNum) {
			return object.getCrusrUuid();
		}
	};

	@Autowired
	private MyBatisDAO myBatisDAO;
	
	@Override
	public CoreUser getCoreUserByName(CoreUser coreUser) {
		return (CoreUser) myBatisDAO.findForObject("getCoreUserByName", coreUser);
	}
	
	@Override
	public boolean insertCoreUser(CoreUser coreUser) {
		myBatisDAO.insert(coreUser);
		return true;
	}

	@Override
	public boolean updateCoreUser(CoreUser coreUser) {
		myBatisDAO.update(coreUser);
		return true;
	}

	@Override
	public boolean disableCoreUser(CoreUser coreUser) {
		coreUser.setCrusrStatus(0);
		coreUser.setCrusrUdate(new Date());
		myBatisDAO.update(coreUser);
		return true;
	}
	
	@Override
	public CoreUser getCoreUser(CoreUser coreUser) {
		return (CoreUser) myBatisDAO.findForObject(coreUser);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoreUser> findCoreUserAll() {
		return myBatisDAO.findForList("findCoreUserAll", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  Map<String, CoreUser> findCoreUserMap() {
		return myBatisDAO.findForMap("findCoreUserAll", USER_UUID_MAPPER);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreUser> findCoreUserForPagesByCompany(int pageNum, int pageSize, String crusrCompany) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crusrCompany", crusrCompany);
		return myBatisDAO.findForPage("findCoreUserForPagesByCompany", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreUser> findCoreUserForPagesByProject(int pageNum, int pageSize, Integer crusrJob, String crusrProject) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("crusrJob", crusrJob);
		hashMap.put("crusrProject", crusrProject);
		return myBatisDAO.findForPage("findCoreUserForPagesByProject", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreUser> findCoreUserForPagesByInvestors(int pageNum, int pageSize) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		return myBatisDAO.findForPage("findCoreUserForPagesByInvestors", new PageRequest(pageNum, pageSize, hashMap));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreUser> findCoreUserForPagesLikes(int pageNum, int pageSize, String objectStr) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("objectStr", objectStr);
		return myBatisDAO.findForPage("findCoreUserForPagesLikes", new PageRequest(pageNum, pageSize, hashMap));
	}

}