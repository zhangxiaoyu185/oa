package com.xiaoyu.lingdian.service;

import com.xiaoyu.lingdian.entity.CoreInvoice;

/**
* 电子发票表
*/
public interface CoreInvoiceService {

	/**
	* 添加
	* @param coreInvoice
	* @return
	*/
	public boolean insertCoreInvoice(CoreInvoice coreInvoice);

	/**
	* 修改
	* @param coreInvoice
	* @return
	*/
	public boolean updateCoreInvoice(CoreInvoice coreInvoice);

	/**
	* 查询
	* @param coreInvoice
	* @return
	*/
	public CoreInvoice getCoreInvoice(CoreInvoice coreInvoice);

}