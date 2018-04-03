package com.xiaoyu.lingdian.service;

import com.xiaoyu.lingdian.entity.CoreInvoiceType;
import java.util.List;

/**
* 发票类型表
*/
public interface CoreInvoiceTypeService {

	/**
	* 查询
	* @param coreInvoiceType
	* @return
	*/
	public CoreInvoiceType getCoreInvoiceType(CoreInvoiceType coreInvoiceType);

	/**
	* 查询所有List
	* @return List
	*/
	public List<CoreInvoiceType> findCoreInvoiceTypeList();

}