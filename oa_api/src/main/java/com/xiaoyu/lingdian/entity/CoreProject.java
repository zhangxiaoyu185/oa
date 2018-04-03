package com.xiaoyu.lingdian.entity;

import com.xiaoyu.lingdian.entity.BaseEntity;
import java.util.Date;

/**
* 项目表
*/
public class CoreProject extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	*标识UNID
	*/
	private Integer crproUnid;

	/**
	*标识UUID
	*/
	private String crproUuid;

	/**
	*项目名称
	*/
	private String crproName;

	/**
	*项目简介
	*/
	private String crproDesc;

	/**
	*资金额度
	*/
	private Double crproMoney;

	/**
	*创建日期
	*/
	private Date crproCdate;

	/**
	*修改日期
	*/
	private Date crproUdate;

	/**
	*所属集团
	*/
	private String crproCompany;

	/**
	*状态:1启用0删除
	*/
	private Integer crproStatus;

	public void setCrproUnid(Integer crproUnid) {
		this.crproUnid = crproUnid;
	}

	public Integer getCrproUnid( ) {
		return crproUnid;
	}

	public void setCrproUuid(String crproUuid) {
		this.crproUuid = crproUuid;
	}

	public String getCrproUuid( ) {
		return crproUuid;
	}

	public void setCrproName(String crproName) {
		this.crproName = crproName;
	}

	public String getCrproName( ) {
		return crproName;
	}

	public void setCrproDesc(String crproDesc) {
		this.crproDesc = crproDesc;
	}

	public String getCrproDesc( ) {
		return crproDesc;
	}

	public void setCrproMoney(Double crproMoney) {
		this.crproMoney = crproMoney;
	}

	public Double getCrproMoney( ) {
		return crproMoney;
	}

	public void setCrproCdate(Date crproCdate) {
		this.crproCdate = crproCdate;
	}

	public Date getCrproCdate( ) {
		return crproCdate;
	}

	public void setCrproUdate(Date crproUdate) {
		this.crproUdate = crproUdate;
	}

	public Date getCrproUdate( ) {
		return crproUdate;
	}

	public void setCrproCompany(String crproCompany) {
		this.crproCompany = crproCompany;
	}

	public String getCrproCompany( ) {
		return crproCompany;
	}

	public void setCrproStatus(Integer crproStatus) {
		this.crproStatus = crproStatus;
	}

	public Integer getCrproStatus( ) {
		return crproStatus;
	}

	public CoreProject( ) { 
	}

//<=================定制内容开始==============
//==================定制内容结束==============>

}