package com.xiaoyu.lingdian.service.impl;

import java.util.List;
import com.xiaoyu.lingdian.service.CoreInvoiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaoyu.lingdian.core.mybatis.dao.MyBatisDAO;
import com.xiaoyu.lingdian.entity.CoreInvoiceType;

/**
* 发票类型表
*/
@Service("coreInvoiceTypeService")
public class CoreInvoiceTypeServiceImpl implements CoreInvoiceTypeService {

	@Autowired
	private MyBatisDAO myBatisDAO;

	@Override
	public CoreInvoiceType getCoreInvoiceType(CoreInvoiceType coreInvoiceType) {
		return (CoreInvoiceType) myBatisDAO.findForObject(coreInvoiceType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CoreInvoiceType> findCoreInvoiceTypeList() {
		return myBatisDAO.findForList("findCoreInvoiceTypeForLists", null);
	}

}