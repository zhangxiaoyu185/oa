package com.xiaoyu.lingdian.vo;

import com.xiaoyu.lingdian.entity.CoreFlowLog;
import com.xiaoyu.lingdian.vo.BaseVO;
import com.xiaoyu.lingdian.tool.DateUtil;

/**
* 流程日志表
*/
public class CoreFlowLogVO implements BaseVO {

	/**
	*流程编码(自动生成唯一)
	*/
	private String crflwCode;

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
	*修改日期
	*/
	private String crflwUdate;

	/**
	*申请资金更新类型:1已发起2已通过3已退回4已取消5已完成;拨款更新类型:11已发起12已拨款13已退回14已取消15已还款
	*/
	private Integer crflwUptype;

	/**
	*更新人
	*/
	private String crflwUpdateUser;

	/**
	*更新人姓名
	*/
	private String crflwUpdateUserName;
	
	/**
	*更新人头像
	*/
	private String crflwUpdateUserHead;
	
	/**
	*更新人职位:1超级管理员4渠道5策划6营销总7项目总8财务
	*/
	private Integer crflwUpdateJob;

	public void setCrflwCode(String crflwCode) {
		this.crflwCode = crflwCode;
	}

	public String getCrflwCode( ) {
		return crflwCode;
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

	public void setCrflwUdate(String crflwUdate) {
		this.crflwUdate = crflwUdate;
	}

	public String getCrflwUdate( ) {
		return crflwUdate;
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

	public String getCrflwUpdateUserName() {
		return crflwUpdateUserName;
	}

	public void setCrflwUpdateUserName(String crflwUpdateUserName) {
		this.crflwUpdateUserName = crflwUpdateUserName;
	}

	public String getCrflwUpdateUserHead() {
		return crflwUpdateUserHead;
	}

	public void setCrflwUpdateUserHead(String crflwUpdateUserHead) {
		this.crflwUpdateUserHead = crflwUpdateUserHead;
	}

	public CoreFlowLogVO( ) { 
	}

	@Override
	public void convertPOToVO(Object poObj) {
		if (null == poObj) {
			return;
		}

		CoreFlowLog po = (CoreFlowLog) poObj;
		this.crflwCode = po.getCrflwCode();
		this.crflwStep = po.getCrflwStep();
		this.crflwVersion = po.getCrflwVersion();
		this.crflwRemarks = po.getCrflwRemarks();
		this.crflwUdate = po.getCrflwUdate()!=null?DateUtil.formatDefaultDate(po.getCrflwUdate()):"";
		this.crflwUptype = po.getCrflwUptype();
		this.crflwUpdateUser = po.getCrflwUpdateUser();
		this.crflwUpdateJob = po.getCrflwUpdateJob();
	}

}