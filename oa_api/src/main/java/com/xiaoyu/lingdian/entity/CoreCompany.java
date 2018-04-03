package com.xiaoyu.lingdian.entity;

import com.xiaoyu.lingdian.entity.BaseEntity;
import java.util.Date;

/**
* 集团表
*/
public class CoreCompany extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	*标识UNID
	*/
	private Integer crgroUnid;

	/**
	*标识UUID
	*/
	private String crgroUuid;

	/**
	*集团名称
	*/
	private String crgroName;

	/**
	*集团简介
	*/
	private String crgroDesc;

	/**
	*创建日期
	*/
	private Date crgroCdate;

	/**
	*修改日期
	*/
	private Date crgroUdate;

	/**
	*资金额度
	*/
	private Double crgroMoney;

	/**
	*状态:1启用0删除
	*/
	private Integer crgroStatus;

	public void setCrgroUnid(Integer crgroUnid) {
		this.crgroUnid = crgroUnid;
	}

	public Integer getCrgroUnid( ) {
		return crgroUnid;
	}

	public void setCrgroUuid(String crgroUuid) {
		this.crgroUuid = crgroUuid;
	}

	public String getCrgroUuid( ) {
		return crgroUuid;
	}

	public void setCrgroName(String crgroName) {
		this.crgroName = crgroName;
	}

	public String getCrgroName( ) {
		return crgroName;
	}

	public void setCrgroDesc(String crgroDesc) {
		this.crgroDesc = crgroDesc;
	}

	public String getCrgroDesc( ) {
		return crgroDesc;
	}

	public void setCrgroCdate(Date crgroCdate) {
		this.crgroCdate = crgroCdate;
	}

	public Date getCrgroCdate( ) {
		return crgroCdate;
	}

	public void setCrgroUdate(Date crgroUdate) {
		this.crgroUdate = crgroUdate;
	}

	public Date getCrgroUdate( ) {
		return crgroUdate;
	}

	public void setCrgroMoney(Double crgroMoney) {
		this.crgroMoney = crgroMoney;
	}

	public Double getCrgroMoney( ) {
		return crgroMoney;
	}

	public void setCrgroStatus(Integer crgroStatus) {
		this.crgroStatus = crgroStatus;
	}

	public Integer getCrgroStatus( ) {
		return crgroStatus;
	}

	public CoreCompany( ) { 
	}

//<=================定制内容开始==============
//==================定制内容结束==============>

}