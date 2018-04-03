package com.xiaoyu.lingdian.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoyu.lingdian.service.CoreAttachmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoyu.lingdian.core.mybatis.dao.MyBatisDAO;
import com.xiaoyu.lingdian.entity.CoreAttachment;

@Service("coreAttachmentService")
public class CoreAttachmentServiceImpl implements CoreAttachmentService {

	@Autowired
	private MyBatisDAO myBatisDAO;

	@Override
	public boolean insertCoreAttachment(CoreAttachment coreAttachment) {
		myBatisDAO.insert(coreAttachment);
		return true;
	}

	@Override
	public boolean updateCoreAttachment(CoreAttachment coreAttachment) {
		myBatisDAO.update(coreAttachment);
		return true;
	}

	@Override
	public boolean deleteCoreAttachment(CoreAttachment coreAttachment) {
		myBatisDAO.delete(coreAttachment);
		return true;
	}

	@Override
	public CoreAttachment getCoreAttachment(CoreAttachment coreAttachment) {
		return (CoreAttachment) myBatisDAO.findForObject(coreAttachment);
	}

	private static final String FIND_CORE_ATTACHMENT_BY_CND = "findCoreAttachmentByCnd";
	private static final String DELETE_CORE_ATTACHMENT_BY_BUSI = "deleteCoreAttachmentByBusi";
	private static final String UPDATE_CORE_ATTACHMENT_BY_BUS = "updateCoreAttachmentByBus";
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CoreAttachment> findCoreAttachmentByCnd(String cratmBusUuid, Integer cratmType) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("cratmBusUuid", cratmBusUuid);
		hashMap.put("cratmType", cratmType);
		return myBatisDAO.findForList(FIND_CORE_ATTACHMENT_BY_CND, hashMap);
	}

	@Override
	public boolean deleteCoreAttachmentByBusi(CoreAttachment coreAttachment) {
		myBatisDAO.delete(DELETE_CORE_ATTACHMENT_BY_BUSI, coreAttachment);
		return true;
	}

	@Override
	public boolean updateCoreAttachmentByBus(CoreAttachment coreAttachment) {
		myBatisDAO.update(UPDATE_CORE_ATTACHMENT_BY_BUS, coreAttachment);
		return true;
	}

}