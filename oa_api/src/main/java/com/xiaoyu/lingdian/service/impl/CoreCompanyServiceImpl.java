package com.xiaoyu.lingdian.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoyu.lingdian.core.mybatis.dao.MyBatisDAO;
import com.xiaoyu.lingdian.core.mybatis.mapper.SimpleMapMapper;
import com.xiaoyu.lingdian.core.mybatis.page.Page;
import com.xiaoyu.lingdian.core.mybatis.page.PageRequest;
import com.xiaoyu.lingdian.entity.CoreCompany;
import com.xiaoyu.lingdian.service.CoreCompanyService;

/**
* 集团表
*/
@Service("coreCompanyService")
public class CoreCompanyServiceImpl implements CoreCompanyService {

	private static final SimpleMapMapper<String, CoreCompany> COMPANY_UUID_MAPPER = new SimpleMapMapper<String, CoreCompany>() {
		@Override
		public String mapKey(CoreCompany object, int rowNum) {
			return object.getCrgroUuid();
		}
	};
	
	@Autowired
	private MyBatisDAO myBatisDAO;

	@Override
	public boolean insertCoreCompany(CoreCompany coreCompany) {
		myBatisDAO.insert(coreCompany);
		return true;
	}

	@Override
	public boolean updateCoreCompany(CoreCompany coreCompany) {
		myBatisDAO.update(coreCompany);
		return true;
	}

	@Override
	public boolean disableCoreCompany(CoreCompany coreCompany) {
		coreCompany.setCrgroStatus(0);
		coreCompany.setCrgroUdate(new Date());
		myBatisDAO.update(coreCompany);
		return true;
	}

	@Override
	public CoreCompany getCoreCompany(CoreCompany coreCompany) {
		return (CoreCompany) myBatisDAO.findForObject(coreCompany);
	}

	@Override
	public CoreCompany getCoreCompanyByName(String crgroName) {
		CoreCompany coreCompany = new CoreCompany();
		coreCompany.setCrgroName(crgroName);
		return (CoreCompany) myBatisDAO.findForObject(coreCompany);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoreCompany> findCoreCompanyAll() {
		return myBatisDAO.findForList("findCoreCompanyAll", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  Map<String, CoreCompany> findCoreCompanyMap() {
		return myBatisDAO.findForMap("findCoreCompanyAll", COMPANY_UUID_MAPPER);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<CoreCompany> findCoreCompanyPage(int pageNum, int pageSize) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		return myBatisDAO.findForPage("findCoreCompanyForPages", new PageRequest(pageNum, pageSize, hashMap));
	}

}