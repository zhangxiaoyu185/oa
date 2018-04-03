package com.xiaoyu.lingdian.vo;

import com.xiaoyu.lingdian.entity.CoreInvestment;
import com.xiaoyu.lingdian.vo.BaseVO;

/**
* 投资拨款流程表
*/
public class CoreInvestmentVO implements BaseVO {

	/**
	*标识UUID
	*/
	private String crsptUuid;

	/**
	*投资客
	*/
	private String crsptUser;

	/**
	*拨款流程
	*/
	private String crsptFlow;

	/**
	*拨款所属集团
	*/
	private String crsptCompany;

	/**
	*拨款所属项目
	*/
	private String crsptProject;

	public void setCrsptUuid(String crsptUuid) {
		this.crsptUuid = crsptUuid;
	}

	public String getCrsptUuid( ) {
		return crsptUuid;
	}

	public void setCrsptUser(String crsptUser) {
		this.crsptUser = crsptUser;
	}

	public String getCrsptUser( ) {
		return crsptUser;
	}

	public void setCrsptFlow(String crsptFlow) {
		this.crsptFlow = crsptFlow;
	}

	public String getCrsptFlow( ) {
		return crsptFlow;
	}

	public void setCrsptCompany(String crsptCompany) {
		this.crsptCompany = crsptCompany;
	}

	public String getCrsptCompany( ) {
		return crsptCompany;
	}

	public void setCrsptProject(String crsptProject) {
		this.crsptProject = crsptProject;
	}

	public String getCrsptProject( ) {
		return crsptProject;
	}

	public CoreInvestmentVO( ) { 
	}

	@Override
	public void convertPOToVO(Object poObj) {
		if (null == poObj) {
			return;
		}

		CoreInvestment po = (CoreInvestment) poObj;
		this.crsptUuid = po.getCrsptUuid();
		this.crsptUser = po.getCrsptUser();
		this.crsptFlow = po.getCrsptFlow();
		this.crsptCompany = po.getCrsptCompany();
		this.crsptProject = po.getCrsptProject();
	}
//<=================定制内容开始==============
//==================定制内容结束==============>

}