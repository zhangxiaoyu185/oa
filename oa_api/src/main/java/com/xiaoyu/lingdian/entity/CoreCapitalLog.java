package com.xiaoyu.lingdian.entity;

import com.xiaoyu.lingdian.entity.BaseEntity;
import java.util.Date;

/**
* 资金变动表
*/
public class CoreCapitalLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	*标识UNID
	*/
	private Integer crcpaUnid;

	/**
	*标识UUID
	*/
	private String crcpaUuid;

	/**
	*创建日期
	*/
	private Date crcpaCdate;

	/**
	*更新人
	*/
	private String crcpaUser;

	/**
	*更新对象
	*/
	private String crcpaBusi;

	/**
	*更新说明
	*/
	private String crcpaDesc;

	/**
	*更新类型:1集团2项目3管理员4投资客
	*/
	private Integer crcpaType;

	public void setCrcpaUnid(Integer crcpaUnid) {
		this.crcpaUnid = crcpaUnid;
	}

	public Integer getCrcpaUnid( ) {
		return crcpaUnid;
	}

	public void setCrcpaUuid(String crcpaUuid) {
		this.crcpaUuid = crcpaUuid;
	}

	public String getCrcpaUuid( ) {
		return crcpaUuid;
	}

	public void setCrcpaCdate(Date crcpaCdate) {
		this.crcpaCdate = crcpaCdate;
	}

	public Date getCrcpaCdate( ) {
		return crcpaCdate;
	}

	public void setCrcpaUser(String crcpaUser) {
		this.crcpaUser = crcpaUser;
	}

	public String getCrcpaUser( ) {
		return crcpaUser;
	}

	public void setCrcpaBusi(String crcpaBusi) {
		this.crcpaBusi = crcpaBusi;
	}

	public String getCrcpaBusi( ) {
		return crcpaBusi;
	}

	public void setCrcpaDesc(String crcpaDesc) {
		this.crcpaDesc = crcpaDesc;
	}

	public String getCrcpaDesc( ) {
		return crcpaDesc;
	}

	public void setCrcpaType(Integer crcpaType) {
		this.crcpaType = crcpaType;
	}

	public Integer getCrcpaType( ) {
		return crcpaType;
	}

	public CoreCapitalLog( ) { 
	}

//<=================定制内容开始==============
//==================定制内容结束==============>

}