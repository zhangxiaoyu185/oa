package com.xiaoyu.lingdian.vo;

import com.xiaoyu.lingdian.entity.CoreInvoiceType;
import com.xiaoyu.lingdian.vo.BaseVO;

/**
* 发票类型表
*/
public class CoreInvoiceTypeVO implements BaseVO {

	/**
	*标识UUID
	*/
	private String crintUuid;

	/**
	*发票类型名称
	*/
	private String crintName;

	/**
	*发票类型描述
	*/
	private String crintDesc;

	public void setCrintUuid(String crintUuid) {
		this.crintUuid = crintUuid;
	}

	public String getCrintUuid( ) {
		return crintUuid;
	}

	public void setCrintName(String crintName) {
		this.crintName = crintName;
	}

	public String getCrintName( ) {
		return crintName;
	}

	public void setCrintDesc(String crintDesc) {
		this.crintDesc = crintDesc;
	}

	public String getCrintDesc( ) {
		return crintDesc;
	}

	public CoreInvoiceTypeVO( ) { 
	}

	@Override
	public void convertPOToVO(Object poObj) {
		if (null == poObj) {
			return;
		}

		CoreInvoiceType po = (CoreInvoiceType) poObj;
		this.crintUuid = po.getCrintUuid();
		this.crintName = po.getCrintName();
		this.crintDesc = po.getCrintDesc();
	}
//<=================定制内容开始==============
//==================定制内容结束==============>

}