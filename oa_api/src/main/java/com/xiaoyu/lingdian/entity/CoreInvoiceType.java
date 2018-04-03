package com.xiaoyu.lingdian.entity;

import com.xiaoyu.lingdian.entity.BaseEntity;

/**
* 发票类型表
*/
public class CoreInvoiceType extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	*标识UNID
	*/
	private Integer crintUnid;

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

	public void setCrintUnid(Integer crintUnid) {
		this.crintUnid = crintUnid;
	}

	public Integer getCrintUnid( ) {
		return crintUnid;
	}

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

	public CoreInvoiceType( ) { 
	}

//<=================定制内容开始==============
//==================定制内容结束==============>

}