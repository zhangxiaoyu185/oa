package com.xiaoyu.lingdian.vo;

import com.xiaoyu.lingdian.entity.CoreUser;
import com.xiaoyu.lingdian.vo.BaseVO;
import com.xiaoyu.lingdian.tool.DateUtil;

/**
* 用户表
*/
public class CoreUserVO implements BaseVO {

	/**
	*标识UUID
	*/
	private String crusrUuid;

	/**
	*登录名
	*/
	private String crusrName;

	/**
	*昵称姓名
	*/
	private String crusrCode;

	/**
	*登录密码(MD5)
	*/
	private String crusrPassword;

	/**
	*职位:1超级管理员2集团管理员3投资客4渠道5策划6营销总7项目总8财务
	*/
	private Integer crusrJob;

	/**
	*所属集团(集团管理员)
	*/
	private String crusrCompany;

	/**
	*所属项目(项目人员)
	*/
	private String crusrProject;

	/**
	*资金额度(超级管理员)
	*/
	private Double crusrMoney;

	/**
	*铜宝宝(除超级管理员外)
	*/
	private Double crusrCopper;

	/**
	*银宝宝(除超级管理员外)
	*/
	private Double crusrSilver;

	/**
	*金宝宝(除超级管理员外)
	*/
	private Double crusrGold;

	/**
	*昨日收益
	*/
	private Double crusrYestIncome;

	/**
	*总收益
	*/
	private Double crusrTotalIncome;

	/**
	*电子邮件
	*/
	private String crusrEmail;

	/**
	*手机号码
	*/
	private String crusrMobile;

	/**
	*状态:1启用0删除
	*/
	private Integer crusrStatus;

	/**
	*创建日期
	*/
	private String crusrCdate;

	/**
	*修改日期
	*/
	private String crusrUdate;

	/**
	*生日
	*/
	private String crusrBirthday;

	/**
	*性别:1男2女3其它
	*/
	private Integer crusrGender;

	/**
	*QQ
	*/
	private String crusrQq;

	/**
	*地址
	*/
	private String crusrAddress;

	/**
	*头像路径
	*/
	private String crusrHead;

	/**
	*备注
	*/
	private String crusrRemarks;

	public void setCrusrUuid(String crusrUuid) {
		this.crusrUuid = crusrUuid;
	}

	public String getCrusrUuid( ) {
		return crusrUuid;
	}

	public void setCrusrName(String crusrName) {
		this.crusrName = crusrName;
	}

	public String getCrusrName( ) {
		return crusrName;
	}

	public void setCrusrCode(String crusrCode) {
		this.crusrCode = crusrCode;
	}

	public String getCrusrCode( ) {
		return crusrCode;
	}

	public void setCrusrPassword(String crusrPassword) {
		this.crusrPassword = crusrPassword;
	}

	public String getCrusrPassword( ) {
		return crusrPassword;
	}

	public void setCrusrJob(Integer crusrJob) {
		this.crusrJob = crusrJob;
	}

	public Integer getCrusrJob( ) {
		return crusrJob;
	}

	public void setCrusrCompany(String crusrCompany) {
		this.crusrCompany = crusrCompany;
	}

	public String getCrusrCompany( ) {
		return crusrCompany;
	}

	public void setCrusrMoney(Double crusrMoney) {
		this.crusrMoney = crusrMoney;
	}

	public Double getCrusrMoney( ) {
		return crusrMoney;
	}

	public Double getCrusrCopper() {
		return crusrCopper;
	}

	public void setCrusrCopper(Double crusrCopper) {
		this.crusrCopper = crusrCopper;
	}

	public Double getCrusrSilver() {
		return crusrSilver;
	}

	public void setCrusrSilver(Double crusrSilver) {
		this.crusrSilver = crusrSilver;
	}

	public Double getCrusrGold() {
		return crusrGold;
	}

	public void setCrusrGold(Double crusrGold) {
		this.crusrGold = crusrGold;
	}

	public Double getCrusrYestIncome() {
		return crusrYestIncome;
	}

	public void setCrusrYestIncome(Double crusrYestIncome) {
		this.crusrYestIncome = crusrYestIncome;
	}

	public Double getCrusrTotalIncome() {
		return crusrTotalIncome;
	}

	public void setCrusrTotalIncome(Double crusrTotalIncome) {
		this.crusrTotalIncome = crusrTotalIncome;
	}

	public void setCrusrProject(String crusrProject) {
		this.crusrProject = crusrProject;
	}

	public String getCrusrProject( ) {
		return crusrProject;
	}

	public void setCrusrEmail(String crusrEmail) {
		this.crusrEmail = crusrEmail;
	}

	public String getCrusrEmail( ) {
		return crusrEmail;
	}

	public void setCrusrMobile(String crusrMobile) {
		this.crusrMobile = crusrMobile;
	}

	public String getCrusrMobile( ) {
		return crusrMobile;
	}

	public void setCrusrStatus(Integer crusrStatus) {
		this.crusrStatus = crusrStatus;
	}

	public Integer getCrusrStatus( ) {
		return crusrStatus;
	}

	public void setCrusrCdate(String crusrCdate) {
		this.crusrCdate = crusrCdate;
	}

	public String getCrusrCdate( ) {
		return crusrCdate;
	}

	public void setCrusrUdate(String crusrUdate) {
		this.crusrUdate = crusrUdate;
	}

	public String getCrusrUdate( ) {
		return crusrUdate;
	}

	public void setCrusrBirthday(String crusrBirthday) {
		this.crusrBirthday = crusrBirthday;
	}

	public String getCrusrBirthday( ) {
		return crusrBirthday;
	}

	public void setCrusrGender(Integer crusrGender) {
		this.crusrGender = crusrGender;
	}

	public Integer getCrusrGender( ) {
		return crusrGender;
	}

	public void setCrusrQq(String crusrQq) {
		this.crusrQq = crusrQq;
	}

	public String getCrusrQq( ) {
		return crusrQq;
	}

	public void setCrusrAddress(String crusrAddress) {
		this.crusrAddress = crusrAddress;
	}

	public String getCrusrAddress( ) {
		return crusrAddress;
	}

	public void setCrusrHead(String crusrHead) {
		this.crusrHead = crusrHead;
	}

	public String getCrusrHead( ) {
		return crusrHead;
	}

	public void setCrusrRemarks(String crusrRemarks) {
		this.crusrRemarks = crusrRemarks;
	}

	public String getCrusrRemarks( ) {
		return crusrRemarks;
	}

	public CoreUserVO( ) { 
	}

	@Override
	public void convertPOToVO(Object poObj) {
		if (null == poObj) {
			return;
		}

		CoreUser po = (CoreUser) poObj;
		this.crusrUuid = po.getCrusrUuid();
		this.crusrName = po.getCrusrName();
		this.crusrCode = po.getCrusrCode();
		this.crusrPassword = po.getCrusrPassword();
		this.crusrJob = po.getCrusrJob();
		this.crusrCompany = po.getCrusrCompany();
		this.crusrMoney = po.getCrusrMoney();
		this.crusrCopper = po.getCrusrCopper();
		this.crusrGold = po.getCrusrGold();
		this.crusrSilver = po.getCrusrSilver();
		this.crusrTotalIncome = po.getCrusrTotalIncome();
		this.crusrYestIncome = po.getCrusrYestIncome();
		this.crusrProject = po.getCrusrProject();
		this.crusrEmail = po.getCrusrEmail();
		this.crusrMobile = po.getCrusrMobile();
		this.crusrStatus = po.getCrusrStatus();
		this.crusrCdate = po.getCrusrCdate()!=null?DateUtil.formatDefaultDate(po.getCrusrCdate()):"";
		this.crusrUdate = po.getCrusrUdate()!=null?DateUtil.formatDefaultDate(po.getCrusrUdate()):"";
		this.crusrBirthday = po.getCrusrBirthday();
		this.crusrGender = po.getCrusrGender();
		this.crusrQq = po.getCrusrQq();
		this.crusrAddress = po.getCrusrAddress();
		this.crusrHead = po.getCrusrHead();
		this.crusrRemarks = po.getCrusrRemarks();
	}

}