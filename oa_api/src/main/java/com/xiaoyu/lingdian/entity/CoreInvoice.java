package com.xiaoyu.lingdian.entity;

import com.xiaoyu.lingdian.entity.BaseEntity;
import java.util.Date;

/**
* 电子发票表
*/
public class CoreInvoice extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	*标识UNID
	*/
	private Integer creicUnid;

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
	*所属项目
	*/
	private String creicProject;

	/**
	*事项
	*/
	private String creicMatter;

	/**
	*发票类型:(劳务派遣服务费，营销策划服务费，会展服务费，中介服务费)
	*/
	private String creicInvoiceType;

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
	private Date creicCdate;

	/**
	*修改日期
	*/
	private Date creicUdate;

	/**
	*开票人
	*/
	private String creicDrawer;

	/**
	*操作人
	*/
	private String creicOperator;

	public void setCreicUnid(Integer creicUnid) {
		this.creicUnid = creicUnid;
	}

	public Integer getCreicUnid( ) {
		return creicUnid;
	}

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

	public void setCreicCdate(Date creicCdate) {
		this.creicCdate = creicCdate;
	}

	public Date getCreicCdate( ) {
		return creicCdate;
	}

	public void setCreicUdate(Date creicUdate) {
		this.creicUdate = creicUdate;
	}

	public Date getCreicUdate( ) {
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

	public CoreInvoice( ) { 
	}

//<=================定制内容开始==============
//==================定制内容结束==============>

}