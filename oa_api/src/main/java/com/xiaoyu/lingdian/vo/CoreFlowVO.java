package com.xiaoyu.lingdian.vo;

import com.xiaoyu.lingdian.entity.CoreFlow;
import com.xiaoyu.lingdian.vo.BaseVO;
import com.xiaoyu.lingdian.enums.CrflwType;
import com.xiaoyu.lingdian.tool.DateUtil;

/**
* 流程表
*/
public class CoreFlowVO implements BaseVO {

	/**
	*标识UUID
	*/
	private String crflwUuid;

	/**
	*流程类型(APPLY申请资金FUNDING拨款)
	*/
	private CrflwType crflwType;

	/**
	*流程编码(自动生成唯一)
	*/
	private String crflwCode;

	/**
	*流程标题
	*/
	private String crflwName;

	/**
	*流程详情
	*/
	private String crflwDesc;

	/**
	*拨款金额(计算剩余和判断额度)
	*/
	private Double crflwLoanMoney;

	/**
	*所属集团
	*/
	private String crflwCompany;

	/**
	*所属集团名称
	*/
	private String crflwCompanyName;
	
	/**
	*所属项目
	*/
	private String crflwProject;
	
	/**
	*所属项目名称
	*/
	private String crflwProjectName;

	/**
	*申请人
	*/
	private String crflwApplyUser;

	/**
	*发起类型:1渠道2策划3财务
	*/
	private Integer crflwByType;

	/**
	*申请资金环节:1渠道发起2策划发起3策划审核4营销总审核5项目总审核6财务审核7已完成;拨款环节:11拨款发起12拨款审核13已拨款14已还款
	*/
	private Integer crflwStep;

	/**
	*版本号每次加一
	*/
	private Integer crflwVersion;

	/**
	*当前意见
	*/
	private String crflwRemarks;

	/**
	*创建日期
	*/
	private String crflwCdate;

	/**
	*修改日期
	*/
	private String crflwUdate;

	/**
	*状态:1启用0取消
	*/
	private Integer crflwStatus;

	/**
	*申请资金更新类型:1已发起2已通过3已退回4已取消5已完成;拨款更新类型:11已发起12已拨款13已退回14已取消15已还款
	*/
	private Integer crflwUptype;

	/**
	*更新人
	*/
	private String crflwUpdateUser;

	/**
	*更新人职位:1超级管理员4渠道5策划6营销总7项目总8财务
	*/
	private Integer crflwUpdateJob;
	
	/**
	*是否已关联
	*/
	private boolean isRelate;
	
	public void setCrflwUuid(String crflwUuid) {
		this.crflwUuid = crflwUuid;
	}

	public String getCrflwUuid( ) {
		return crflwUuid;
	}

	public void setCrflwType(CrflwType crflwType) {
		this.crflwType = crflwType;
	}

	public CrflwType getCrflwType( ) {
		return crflwType;
	}

	public void setCrflwCode(String crflwCode) {
		this.crflwCode = crflwCode;
	}

	public String getCrflwCode( ) {
		return crflwCode;
	}

	public void setCrflwName(String crflwName) {
		this.crflwName = crflwName;
	}

	public String getCrflwName( ) {
		return crflwName;
	}

	public void setCrflwDesc(String crflwDesc) {
		this.crflwDesc = crflwDesc;
	}

	public String getCrflwDesc( ) {
		return crflwDesc;
	}

	public void setCrflwLoanMoney(Double crflwLoanMoney) {
		this.crflwLoanMoney = crflwLoanMoney;
	}

	public Double getCrflwLoanMoney( ) {
		return crflwLoanMoney;
	}

	public void setCrflwCompany(String crflwCompany) {
		this.crflwCompany = crflwCompany;
	}

	public String getCrflwCompany( ) {
		return crflwCompany;
	}

	public void setCrflwProject(String crflwProject) {
		this.crflwProject = crflwProject;
	}

	public String getCrflwProject( ) {
		return crflwProject;
	}

	public void setCrflwApplyUser(String crflwApplyUser) {
		this.crflwApplyUser = crflwApplyUser;
	}

	public String getCrflwApplyUser( ) {
		return crflwApplyUser;
	}

	public void setCrflwByType(Integer crflwByType) {
		this.crflwByType = crflwByType;
	}

	public Integer getCrflwByType( ) {
		return crflwByType;
	}

	public void setCrflwStep(Integer crflwStep) {
		this.crflwStep = crflwStep;
	}

	public Integer getCrflwStep( ) {
		return crflwStep;
	}

	public void setCrflwVersion(Integer crflwVersion) {
		this.crflwVersion = crflwVersion;
	}

	public Integer getCrflwVersion( ) {
		return crflwVersion;
	}

	public void setCrflwRemarks(String crflwRemarks) {
		this.crflwRemarks = crflwRemarks;
	}

	public String getCrflwRemarks( ) {
		return crflwRemarks;
	}

	public void setCrflwCdate(String crflwCdate) {
		this.crflwCdate = crflwCdate;
	}

	public String getCrflwCdate( ) {
		return crflwCdate;
	}

	public void setCrflwUdate(String crflwUdate) {
		this.crflwUdate = crflwUdate;
	}

	public String getCrflwUdate( ) {
		return crflwUdate;
	}

	public void setCrflwStatus(Integer crflwStatus) {
		this.crflwStatus = crflwStatus;
	}

	public Integer getCrflwStatus( ) {
		return crflwStatus;
	}

	public void setCrflwUptype(Integer crflwUptype) {
		this.crflwUptype = crflwUptype;
	}

	public Integer getCrflwUptype( ) {
		return crflwUptype;
	}

	public void setCrflwUpdateUser(String crflwUpdateUser) {
		this.crflwUpdateUser = crflwUpdateUser;
	}

	public String getCrflwUpdateUser( ) {
		return crflwUpdateUser;
	}

	public void setCrflwUpdateJob(Integer crflwUpdateJob) {
		this.crflwUpdateJob = crflwUpdateJob;
	}

	public Integer getCrflwUpdateJob( ) {
		return crflwUpdateJob;
	}

	public String getCrflwCompanyName() {
		return crflwCompanyName;
	}

	public void setCrflwCompanyName(String crflwCompanyName) {
		this.crflwCompanyName = crflwCompanyName;
	}

	public String getCrflwProjectName() {
		return crflwProjectName;
	}

	public void setCrflwProjectName(String crflwProjectName) {
		this.crflwProjectName = crflwProjectName;
	}

	public boolean isRelate() {
		return isRelate;
	}

	public void setRelate(boolean isRelate) {
		this.isRelate = isRelate;
	}

	public CoreFlowVO( ) { 
	}

	@Override
	public void convertPOToVO(Object poObj) {
		if (null == poObj) {
			return;
		}

		CoreFlow po = (CoreFlow) poObj;
		this.crflwUuid = po.getCrflwUuid();
		this.crflwType = po.getCrflwType();
		this.crflwCode = po.getCrflwCode();
		this.crflwName = po.getCrflwName();
		this.crflwDesc = po.getCrflwDesc();
		this.crflwLoanMoney = po.getCrflwLoanMoney();
		this.crflwCompany = po.getCrflwCompany();
		this.crflwProject = po.getCrflwProject();
		this.crflwApplyUser = po.getCrflwApplyUser();
		this.crflwByType = po.getCrflwByType();
		this.crflwStep = po.getCrflwStep();
		this.crflwVersion = po.getCrflwVersion();
		this.crflwRemarks = po.getCrflwRemarks();
		this.crflwCdate = po.getCrflwCdate()!=null?DateUtil.formatDefaultDate(po.getCrflwCdate()):"";
		this.crflwUdate = po.getCrflwUdate()!=null?DateUtil.formatDefaultDate(po.getCrflwUdate()):"";
		this.crflwStatus = po.getCrflwStatus();
		this.crflwUptype = po.getCrflwUptype();
		this.crflwUpdateUser = po.getCrflwUpdateUser();
		this.crflwUpdateJob = po.getCrflwUpdateJob();
	}
//<=================定制内容开始==============
//==================定制内容结束==============>

}