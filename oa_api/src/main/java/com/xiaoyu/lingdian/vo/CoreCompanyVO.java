package com.xiaoyu.lingdian.vo;

import com.xiaoyu.lingdian.entity.CoreCompany;
import com.xiaoyu.lingdian.vo.BaseVO;
import com.xiaoyu.lingdian.tool.DateUtil;

/**
* 集团表
*/
public class CoreCompanyVO implements BaseVO {

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
	private String crgroCdate;

	/**
	*修改日期
	*/
	private String crgroUdate;

	/**
	*资金额度
	*/
	private Double crgroMoney;

	/**
	*状态:1启用0删除
	*/
	private Integer crgroStatus;

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

	public void setCrgroCdate(String crgroCdate) {
		this.crgroCdate = crgroCdate;
	}

	public String getCrgroCdate( ) {
		return crgroCdate;
	}

	public void setCrgroUdate(String crgroUdate) {
		this.crgroUdate = crgroUdate;
	}

	public String getCrgroUdate( ) {
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

	public CoreCompanyVO( ) { 
	}

	@Override
	public void convertPOToVO(Object poObj) {
		if (null == poObj) {
			return;
		}

		CoreCompany po = (CoreCompany) poObj;
		this.crgroUuid = po.getCrgroUuid();
		this.crgroName = po.getCrgroName();
		this.crgroDesc = po.getCrgroDesc();
		this.crgroCdate = po.getCrgroCdate()!=null?DateUtil.formatDefaultDate(po.getCrgroCdate()):"";
		this.crgroUdate = po.getCrgroUdate()!=null?DateUtil.formatDefaultDate(po.getCrgroUdate()):"";
		this.crgroMoney = po.getCrgroMoney();
		this.crgroStatus = po.getCrgroStatus();
	}
//<=================定制内容开始==============
//==================定制内容结束==============>

}