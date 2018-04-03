package com.xiaoyu.lingdian.service.impl;

import com.xiaoyu.lingdian.service.CoreInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaoyu.lingdian.core.mybatis.dao.MyBatisDAO;
import com.xiaoyu.lingdian.entity.CoreInvoice;

/**
* 电子发票表
*/
@Service("coreInvoiceService")
public class CoreInvoiceServiceImpl implements CoreInvoiceService {

	@Autowired
	private MyBatisDAO myBatisDAO;

	@Override
	public boolean insertCoreInvoice(CoreInvoice coreInvoice) {
		myBatisDAO.insert(coreInvoice);
		return true;
	}

	@Override
	public boolean updateCoreInvoice(CoreInvoice coreInvoice) {
		myBatisDAO.update(coreInvoice);
		return true;
	}

	@Override
	public CoreInvoice getCoreInvoice(CoreInvoice coreInvoice) {
		return (CoreInvoice) myBatisDAO.findForObject(coreInvoice);
	}

}