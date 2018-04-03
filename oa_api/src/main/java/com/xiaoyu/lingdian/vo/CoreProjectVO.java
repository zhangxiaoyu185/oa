package com.xiaoyu.lingdian.vo;

import com.xiaoyu.lingdian.entity.CoreProject;
import com.xiaoyu.lingdian.vo.BaseVO;
import com.xiaoyu.lingdian.tool.DateUtil;

/**
* 项目表
*/
public class CoreProjectVO implements BaseVO {

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
	private String crproCdate;

	/**
	*修改日期
	*/
	private String crproUdate;

	/**
	*所属集团
	*/
	private String crproCompany;

	/**
	*所属集团名称
	*/
	private String crproCompanyName;
	
	/**
	*状态:1启用0删除
	*/
	private Integer crproStatus;

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

	public void setCrproCdate(String crproCdate) {
		this.crproCdate = crproCdate;
	}

	public String getCrproCdate( ) {
		return crproCdate;
	}

	public void setCrproUdate(String crproUdate) {
		this.crproUdate = crproUdate;
	}

	public String getCrproUdate( ) {
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

	public String getCrproCompanyName() {
		return crproCompanyName;
	}

	public void setCrproCompanyName(String crproCompanyName) {
		this.crproCompanyName = crproCompanyName;
	}

	public CoreProjectVO( ) { 
	}

	@Override
	public void convertPOToVO(Object poObj) {
		if (null == poObj) {
			return;
		}

		CoreProject po = (CoreProject) poObj;
		this.crproUuid = po.getCrproUuid();
		this.crproName = po.getCrproName();
		this.crproDesc = po.getCrproDesc();
		this.crproMoney = po.getCrproMoney();
		this.crproCdate = po.getCrproCdate()!=null?DateUtil.formatDefaultDate(po.getCrproCdate()):"";
		this.crproUdate = po.getCrproUdate()!=null?DateUtil.formatDefaultDate(po.getCrproUdate()):"";
		this.crproCompany = po.getCrproCompany();
		this.crproStatus = po.getCrproStatus();
	}
//<=================定制内容开始==============
//==================定制内容结束==============>

}