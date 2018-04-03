package com.xiaoyu.lingdian.vo;

import com.xiaoyu.lingdian.entity.CoreInvoice;
import com.xiaoyu.lingdian.vo.BaseVO;
import com.xiaoyu.lingdian.tool.DateUtil;

/**
* 电子发票表
*/
public class CoreInvoiceVO implements BaseVO {

	/**
	*标识UUID
	*/
	private String creicUuid;

	/**
	*发票编码(流程编码)
	*/
	private String creicCode;

	/**
	*发票图片(流程编码.png)
	*/
	private String creicPng;

	/**
	*收款单位
	*/
	private String creicPayee;

	/**
	*收款账号
	*/
	private String creicPayeeAccount;

	/**
	*收款开户行
	*/
	private String creicPayeeBank;

	/**
	*付款单位
	*/
	private String creicPayer;

	/**
	*付款账号
	*/
	private String creicPayerAccount;

	/**
	*付款开户行
	*/
	private String creicPayerBank;

	/**
	*所属集团
	*/
	private String creicCompany;

	/**
	*所属集团名称
	*/
	private String creicCompanyName;

	/**
	*所属项目
	*/
	private String creicProject;
	
	/**
	*所属项目名称
	*/
	private String creicProjectName;

	/**
	*事项
	*/
	private String creicMatter;

	/**
	*发票类型
	*/
	private String creicInvoiceType;

	/**
	*发票类型名称
	*/
	private String creicInvoiceTypeName;

	/**
	*小写金额
	*/
	private Double creicLowAmount;

	/**
	*大写金额
	*/
	private String creicCapAmount;

	/**
	*备注
	*/
	private String creicRemarks;

	/**
	*创建日期
	*/
	private String creicCdate;

	/**
	*修改日期
	*/
	private String creicUdate;

	/**
	*开票人
	*/
	private String creicDrawer;

	/**
	*操作人
	*/
	private String creicOperator;

	public void setCreicUuid(String creicUuid) {
		this.creicUuid = creicUuid;
	}

	public String getCreicUuid( ) {
		return creicUuid;
	}

	public void setCreicCode(String creicCode) {
		this.creicCode = creicCode;
	}

	public String getCreicCode( ) {
		return creicCode;
	}

	public String getCreicInvoiceTypeName() {
		return creicInvoiceTypeName;
	}

	public void setCreicInvoiceTypeName(String creicInvoiceTypeName) {
		this.creicInvoiceTypeName = creicInvoiceTypeName;
	}

	public void setCreicPng(String creicPng) {
		this.creicPng = creicPng;
	}

	public String getCreicPng( ) {
		return creicPng;
	}

	public void setCreicPayee(String creicPayee) {
		this.creicPayee = creicPayee;
	}

	public String getCreicPayee( ) {
		return creicPayee;
	}

	public void setCreicPayeeAccount(String creicPayeeAccount) {
		this.creicPayeeAccount = creicPayeeAccount;
	}

	public String getCreicPayeeAccount( ) {
		return creicPayeeAccount;
	}

	public void setCreicPayeeBank(String creicPayeeBank) {
		this.creicPayeeBank = creicPayeeBank;
	}

	public String getCreicPayeeBank( ) {
		return creicPayeeBank;
	}

	public void setCreicPayer(String creicPayer) {
		this.creicPayer = creicPayer;
	}

	public String getCreicPayer( ) {
		return creicPayer;
	}

	public void setCreicPayerAccount(String creicPayerAccount) {
		this.creicPayerAccount = creicPayerAccount;
	}

	public String getCreicPayerAccount( ) {
		return creicPayerAccount;
	}

	public void setCreicPayerBank(String creicPayerBank) {
		this.creicPayerBank = creicPayerBank;
	}

	public String getCreicPayerBank( ) {
		return creicPayerBank;
	}

	public void setCreicCompany(String creicCompany) {
		this.creicCompany = creicCompany;
	}

	public String getCreicCompany( ) {
		return creicCompany;
	}

	public void setCreicProject(String creicProject) {
		this.creicProject = creicProject;
	}

	public String getCreicProject( ) {
		return creicProject;
	}

	public void setCreicMatter(String creicMatter) {
		this.creicMatter = creicMatter;
	}

	public String getCreicMatter( ) {
		return creicMatter;
	}

	public void setCreicInvoiceType(String creicInvoiceType) {
		this.creicInvoiceType = creicInvoiceType;
	}

	public String getCreicInvoiceType( ) {
		return creicInvoiceType;
	}

	public void setCreicLowAmount(Double creicLowAmount) {
		this.creicLowAmount = creicLowAmount;
	}

	public Double getCreicLowAmount( ) {
		return creicLowAmount;
	}

	public void setCreicCapAmount(String creicCapAmount) {
		this.creicCapAmount = creicCapAmount;
	}

	public String getCreicCapAmount( ) {
		return creicCapAmount;
	}

	public void setCreicRemarks(String creicRemarks) {
		this.creicRemarks = creicRemarks;
	}

	public String getCreicRemarks( ) {
		return creicRemarks;
	}

	public void setCreicCdate(String creicCdate) {
		this.creicCdate = creicCdate;
	}

	public String getCreicCdate( ) {
		return creicCdate;
	}

	public void setCreicUdate(String creicUdate) {
		this.creicUdate = creicUdate;
	}

	public String getCreicUdate( ) {
		return creicUdate;
	}

	public void setCreicDrawer(String creicDrawer) {
		this.creicDrawer = creicDrawer;
	}

	public String getCreicDrawer( ) {
		return creicDrawer;
	}

	public void setCreicOperator(String creicOperator) {
		this.creicOperator = creicOperator;
	}

	public String getCreicOperator( ) {
		return creicOperator;
	}

	public String getCreicCompanyName() {
		return creicCompanyName;
	}

	public void setCreicCompanyName(String creicCompanyName) {
		this.creicCompanyName = creicCompanyName;
	}

	public String getCreicProjectName() {
		return creicProjectName;
	}

	public void setCreicProjectName(String creicProjectName) {
		this.creicProjectName = creicProjectName;
	}

	public CoreInvoiceVO( ) { 
	}

	@Override
	public void convertPOToVO(Object poObj) {
		if (null == poObj) {
			return;
		}

		CoreInvoice po = (CoreInvoice) poObj;
		this.creicUuid = po.getCreicUuid();
		this.creicCode = po.getCreicCode();
		this.creicPng = po.getCreicPng();
		this.creicPayee = po.getCreicPayee();
		this.creicPayeeAccount = po.getCreicPayeeAccount();
		this.creicPayeeBank = po.getCreicPayeeBank();
		this.creicPayer = po.getCreicPayer();
		this.creicPayerAccount = po.getCreicPayerAccount();
		this.creicPayerBank = po.getCreicPayerBank();
		this.creicCompany = po.getCreicCompany();
		this.creicProject = po.getCreicProject();
		this.creicMatter = po.getCreicMatter();
		this.creicInvoiceType = po.getCreicInvoiceType();
		this.creicLowAmount = po.getCreicLowAmount();
		this.creicCapAmount = po.getCreicCapAmount();
		this.creicRemarks = po.getCreicRemarks();
		this.creicCdate = po.getCreicCdate()!=null?DateUtil.formatDefaultDate(po.getCreicCdate()):"";
		this.creicUdate = po.getCreicUdate()!=null?DateUtil.formatDefaultDate(po.getCreicUdate()):"";
		this.creicDrawer = po.getCreicDrawer();
		this.creicOperator = po.getCreicOperator();
	}

}