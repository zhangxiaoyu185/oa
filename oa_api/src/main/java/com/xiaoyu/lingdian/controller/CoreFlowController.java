package com.xiaoyu.lingdian.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoyu.lingdian.core.mybatis.page.Page;
import com.xiaoyu.lingdian.entity.CoreAttachment;
import com.xiaoyu.lingdian.entity.CoreCapitalLog;
import com.xiaoyu.lingdian.entity.CoreCompany;
import com.xiaoyu.lingdian.entity.CoreFlow;
import com.xiaoyu.lingdian.entity.CoreFlowLog;
import com.xiaoyu.lingdian.entity.CoreInvestment;
import com.xiaoyu.lingdian.entity.CoreInvoice;
import com.xiaoyu.lingdian.entity.CoreInvoiceType;
import com.xiaoyu.lingdian.entity.CoreProject;
import com.xiaoyu.lingdian.entity.CoreUser;
import com.xiaoyu.lingdian.enums.CrflwType;
import com.xiaoyu.lingdian.service.CoreAttachmentService;
import com.xiaoyu.lingdian.service.CoreCapitalLogService;
import com.xiaoyu.lingdian.service.CoreCompanyService;
import com.xiaoyu.lingdian.service.CoreFlowLogService;
import com.xiaoyu.lingdian.service.CoreFlowService;
import com.xiaoyu.lingdian.service.CoreInvestmentService;
import com.xiaoyu.lingdian.service.CoreInvoiceService;
import com.xiaoyu.lingdian.service.CoreInvoiceTypeService;
import com.xiaoyu.lingdian.service.CoreProjectService;
import com.xiaoyu.lingdian.service.CoreUserService;
import com.xiaoyu.lingdian.tool.ConvertNumUtil;
import com.xiaoyu.lingdian.tool.DateUtil;
import com.xiaoyu.lingdian.tool.FileUtil;
import com.xiaoyu.lingdian.tool.IOUtil;
import com.xiaoyu.lingdian.tool.InvoicePicUtil;
import com.xiaoyu.lingdian.tool.RandomUtil;
import com.xiaoyu.lingdian.tool.StringUtil;
import com.xiaoyu.lingdian.tool.init.ConfigIni;
import com.xiaoyu.lingdian.tool.out.ResultMessageBuilder;
import com.xiaoyu.lingdian.vo.CoreFlowLogVO;
import com.xiaoyu.lingdian.vo.CoreFlowVO;
import com.xiaoyu.lingdian.vo.CoreInvoiceTypeVO;
import com.xiaoyu.lingdian.vo.CoreInvoiceVO;

@Controller
@RequestMapping(value="/coreFlow")
public class CoreFlowController extends BaseController {

	/**
	* 流程表
	*/
	@Autowired
	private CoreFlowService coreFlowService;
	
	/**
	* 投资拨款流程表
	*/
	@Autowired
	private CoreInvestmentService coreInvestmentService;
	
	/**
	* 流程日志表
	*/
	@Autowired
	private CoreFlowLogService coreFlowLogService;
	
	/**
	* 附件表
	*/
	@Autowired
	private CoreAttachmentService coreAttachmentService;

	/**
	* 用户表
	*/
	@Autowired
	private CoreUserService coreUserService;
	
	/**
	* 项目表
	*/
	@Autowired
	private CoreProjectService coreProjectService;
	
	/**
	* 集团表
	*/
	@Autowired
	private CoreCompanyService coreCompanyService;
	
	/**
	* 资金变动表
	*/
	@Autowired
	private CoreCapitalLogService coreCapitalLogService;

	/**
	* 电子发票表
	*/
	@Autowired
	private CoreInvoiceService coreInvoiceService;	

	/**
	* 发票类型表
	*/
	@Autowired
	private CoreInvoiceTypeService coreInvoiceTypeService;

	/**
	 * 渠道、策划申请资金发起
	 * 
	 * @param crflwName 流程标题
	 * @param crflwDesc 流程详情
	 * @param crflwCompany 所属集团
	 * @param crflwProject 所属项目
	 * @param crflwApplyUser 申请人
	 * @param crflwRemarks 当前意见
	 * @param crflwUpdateJob 更新人职位:4渠道5策划
	 * @param uuids 图片附件UUIDS,用"|"隔开
	 * @param response
	 */
	@RequestMapping(value="/add/coreFlow/apply", method=RequestMethod.POST)
	public void addCoreFlowApply (String crflwName, String crflwDesc, String crflwCompany, String crflwProject, String crflwApplyUser, 
			String crflwRemarks, Integer crflwUpdateJob, String uuids, HttpServletResponse response) {
		logger.info("[CoreFlowController]:begin addCoreFlowApply");
		if (StringUtils.isBlank(crflwName)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "流程标题必传！"), response);
			return;
		}
		if (StringUtils.isBlank(crflwProject)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "所属项目必传！"), response);
			return;
		}
		if (StringUtils.isBlank(crflwApplyUser)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "申请人必传！"), response);
			return;
		}
		if (crflwUpdateJob != 4 && crflwUpdateJob != 5) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "申请发起的职位不匹配！"), response);
			return;
		}
		
		CoreProject coreProject = new CoreProject();
		coreProject.setCrproUuid(crflwProject);
		coreProject = coreProjectService.getCoreProject(coreProject);
		if (coreProject == null) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "所属项目不存在！"), response);
			return;
		}
		crflwCompany = coreProject.getCrproCompany();
		
		//默认渠道
		int crflwByType = 1;
		int crflwStep = 1;
		if (crflwUpdateJob == 5) { //策划
			crflwByType = 2;
			crflwStep = 2;
		}
		addCoreFlowAndUpdate(CrflwType.APPLY, crflwName, crflwDesc, null, crflwCompany, crflwProject, 
				crflwApplyUser, crflwByType, crflwStep, crflwRemarks, crflwApplyUser, crflwUpdateJob, uuids);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "申请成功!"),response);
		logger.info("[CoreFlowController]:end addCoreFlowApply");
	}
	
	/**
	 * 财务发起拨款
	 * 
	 * @param crflwName 流程标题
	 * @param crflwDesc 流程详情
	 * @param crflwLoanMoney 拨款金额(计算剩余和判断额度)
	 * @param crflwCompany 所属集团
	 * @param crflwProject 所属项目
	 * @param crflwApplyUser 申请人
	 * @param crflwRemarks 当前意见
	 * @param crflwUpdateJob 更新人职位:8财务
	 * @param uuids 图片附件UUIDS,用"|"隔开
	 * @param response
	 */
	@RequestMapping(value="/add/coreFlow/funding", method=RequestMethod.POST)
	public void addCoreFlowFunding (String crflwName, String crflwDesc, Double crflwLoanMoney, String crflwCompany, String crflwProject, String crflwApplyUser, 
			String crflwRemarks, Integer crflwUpdateJob, String uuids, HttpServletResponse response) {
		logger.info("[CoreFlowController]:begin addCoreFlowFunding");
		if (StringUtils.isBlank(crflwName)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "流程标题必传！"), response);
			return;
		}
		if (StringUtils.isBlank(crflwProject)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "所属项目必传！"), response);
			return;
		}
		if (StringUtils.isBlank(crflwApplyUser)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "申请人必传！"), response);
			return;
		}
		if (null == crflwLoanMoney || 0 == crflwLoanMoney) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "拨款金额必传！"), response);
			return;
		}
		if (crflwUpdateJob != 8) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "申请发起的职位不匹配！"), response);
			return;
		}
		//判断资金额度范围
		CoreProject coreProject = new CoreProject();
		coreProject.setCrproUuid(crflwProject);
		coreProject = coreProjectService.getCoreProject(coreProject);
		if (null == coreProject) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "所属项目不存在！"), response);
			return;
		}
		if (crflwLoanMoney > coreProject.getCrproMoney()) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "申请的资金额度大于项目的资金额度！"), response);
			return;
		}
		crflwCompany = coreProject.getCrproCompany();
		
		addCoreFlowAndUpdate (CrflwType.FUNDING, crflwName, crflwDesc, crflwLoanMoney, crflwCompany, crflwProject, 
				crflwApplyUser, 3, 11, crflwRemarks, crflwApplyUser, crflwUpdateJob, uuids);
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "申请成功!"),response);
		logger.info("[CoreFlowController]:end addCoreFlowFunding");
	}
	
	/**
	* 添加流程发起,并更新流程环节和版本
	*
	* @param crflwType 流程类型(APPLY申请资金FUNDING拨款)
	* @param crflwName 流程标题
	* @param crflwDesc 流程详情
	* @param crflwLoanMoney 拨款金额(计算剩余和判断额度)
	* @param crflwCompany 所属集团
	* @param crflwProject 所属项目
	* @param crflwApplyUser 申请人
	* @param crflwByType 发起类型:1渠道2策划3财务
	* @param crflwStep 申请资金环节:1渠道发起2策划发起3策划审核4营销总审核5项目总审核6财务审核7已完成;拨款环节:11拨款发起12拨款审核13已拨款14已还款
	* @param crflwRemarks 当前意见
	* @param crflwUpdateUser 更新人
	* @param crflwUpdateJob 更新人职位:1超级管理员4渠道5策划6营销总7项目总8财务
	* @param uuids 图片附件UUIDS,用"|"隔开
	*/
	public void addCoreFlowAndUpdate (CrflwType crflwType, String crflwName, String crflwDesc, Double crflwLoanMoney, String crflwCompany, String crflwProject, 
			String crflwApplyUser, Integer crflwByType, Integer crflwStep, String crflwRemarks, String crflwUpdateUser, Integer crflwUpdateJob, String uuids) {
		CoreFlow coreFlow = new CoreFlow();
		String uuid = RandomUtil.generateString(16);
		coreFlow.setCrflwUuid(uuid);
		coreFlow.setCrflwType(crflwType);
		String crflwCode = RandomUtil.generateString(16);
		coreFlow.setCrflwCode(crflwCode);
		coreFlow.setCrflwName(crflwName);
		coreFlow.setCrflwDesc(crflwDesc);
		coreFlow.setCrflwLoanMoney(crflwLoanMoney);
		coreFlow.setCrflwCompany(crflwCompany);
		coreFlow.setCrflwProject(crflwProject);
		coreFlow.setCrflwApplyUser(crflwApplyUser);
		coreFlow.setCrflwByType(crflwByType);
		coreFlow.setCrflwStep(crflwStep);
		coreFlow.setCrflwVersion(1);
		coreFlow.setCrflwRemarks(crflwRemarks);
		coreFlow.setCrflwCdate(new Date());
		coreFlow.setCrflwUdate(new Date());
		coreFlow.setCrflwStatus(1);
		coreFlow.setCrflwUptype(1);
		coreFlow.setCrflwUpdateUser(crflwUpdateUser);
		coreFlow.setCrflwUpdateJob(crflwUpdateJob);
		coreFlowService.insertCoreFlow(coreFlow);
		
		// 添加图片附件
		if (!StringUtils.isBlank(uuids)) {
			CoreAttachment coreAttachment = new CoreAttachment();
			String[] uuidModel = uuids.split("\\|");
			for (int i = 0; i < uuidModel.length; i++) {
				coreAttachment = new CoreAttachment();
				coreAttachment.setCratmUuid(uuidModel[i]);
				coreAttachment.setCratmBusUuid(crflwCode);
				coreAttachmentService.updateCoreAttachment(coreAttachment);
			}
		}
		
		//默认渠道发起->策划审核
		int nextCrflwStep = 3;
		if (coreFlow.getCrflwStep() == 2) {
			nextCrflwStep = 4; //策划发起->营销总审核
		}
		if (coreFlow.getCrflwStep() == 11) {
			nextCrflwStep = 12; //拨款发起->拨款审核
		}
		coreFlowService.updateCoreFlowStep(nextCrflwStep, crflwCode, coreFlow.getCrflwStep());
		
		if (crflwType == CrflwType.APPLY) {
			this.addCoreInvoice(crflwCode, crflwCode + ".png", crflwCompany, crflwProject, crflwName, crflwApplyUser);
		}
	}
	
	/**
	 * 待审更新
	   	渠道发起：显示取消和发起按钮，可上传删除图片附件
			渠道取消先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程状态改为禁用同时版本号+1
			渠道发起先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为策划审核同时版本号+1
		策划发起：显示取消和发起按钮，可上传删除图片附件
			策划取消先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程状态改为禁用同时版本号+1
			策划发起先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为营销总审核同时版本号+1
		策划审核：显示退回和通过按钮
			策划退回先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为渠道发起同时版本号+1
			策划通过先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为营销总审核同时版本号+1
		营销总审核：显示退回和通过按钮
			营销总退回先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再根据发起类型修改流程环节，渠道发起把流程环节改为策划审核同时版本号+1，策划发起把流程环节改为策划发起同时版本号+1，
			营销总通过先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为项目总审核同时版本号+1
		项目总审核：显示退回和通过按钮
			项目总退回先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为营销中审核同时版本号+1
			项目总通过先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为财务审核同时版本号+1
		财务审核：显示退回和完成按钮，编辑电子发票
			编辑电子发票在点击完成时保存成一张图片，如若未编辑则不能点击完成操作
			财务退回先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为项目总审核同时版本号+1
			财务完成先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为已完成同时版本号+1		
		拨款发起：显示取消和发起按钮，可上传删除图片附件
			财务取消先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程状态改为禁用同时版本号+1
			财务发起先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为拨款审核同时版本号+1
		拨款审核：显示退回和拨款按钮，上传删除拨款凭证
			拨款凭证在点击完成时判断是否已存在，如不存在则不能点击拨款操作
			超级管理员退回先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为拨款发起同时版本号+1
			超级管理员拨款先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为已拨款同时版本号+1		
		已拨款：显示退回和还款按钮
			超级管理员退回先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为拨款审核同时版本号+1
			超级管理员还款先更新流程表的更新人、更新职位和更新类型、备注，日志表中插入更新后的数据，再把流程环节改为已还款同时版本号+1
	 * @param crflwCode 流程编码(自动生成唯一)
	 * @param crflwName 流程标题
	 * @param crflwDesc 流程详情
	 * @param crflwLoanMoney 拨款金额(计算剩余和判断额度)
	 * @param crflwByType 发起类型:1渠道2策划3财务
	 * @param crflwStep 当前所在步骤 申请资金环节:1渠道发起2策划发起3策划审核4营销总审核5项目总审核6财务审核7已完成;拨款环节:11拨款发起12拨款审核13已拨款14已还款
	 * @param crflwRemarks 当前意见
	 * @param crflwUptype 申请资金更新类型:1已发起2已通过3已退回4已取消5已完成;拨款更新类型:11已发起12已拨款13已退回14已取消15已还款
	 * @param crflwUpdateUser 更新人
	 * @param crflwUpdateJob 更新人职位:1超级管理员4渠道5策划6营销总7项目总8财务
	 * @param response
	 */
	@RequestMapping(value="/update/coreFlow", method=RequestMethod.POST)
	public void updateCoreFlow (String crflwCode, String crflwName, String crflwDesc, Double crflwLoanMoney, Integer crflwByType, 
			Integer crflwStep, String crflwRemarks, Integer crflwUptype, String crflwUpdateUser, Integer crflwUpdateJob, HttpServletResponse response) {
		logger.info("[CoreFlowController]:begin updateCoreFlow");
		if (StringUtils.isBlank(crflwName)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "流程标题必传！"), response);
			return;
		}
		if (StringUtils.isBlank(crflwUpdateUser)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "更新人必传！"), response);
			return;
		}
		if (null == crflwByType || crflwByType == 0 || crflwByType > 3) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "发起类型不匹配！"), response);
			return;
		}
		if (null == crflwStep || crflwByType == 0) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "当前所在步骤不匹配！"), response);
			return;
		}
		if (null == crflwUptype || crflwUptype == 0) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "更新类型不匹配！"), response);
			return;
		}
		if (null == crflwUpdateJob || crflwUpdateJob == 0 || crflwUpdateJob > 8) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "更新人职位不匹配！"), response);
			return;
		}
		
		CoreFlow coreFlow = coreFlowService.getCoreFlowForUpdate(crflwCode);
		if (null == coreFlow) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "流程不存在!"),response);
			logger.info("[CoreFlowController]:end updateCoreFlow");
			return;
		}
		if (!coreFlow.getCrflwStep().equals(crflwStep)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "该流程别人已操作,环节已改变!"),response);
			logger.info("[CoreFlowController]:end updateCoreFlow");
			return;
		}

		CoreProject coreProject = coreProjectService.getCoreProjectForUpdate(coreFlow.getCrflwProject());
		if (null == coreProject) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "流程所在项目不存在!"),response);
			logger.info("[CoreFlowController]:end updateCoreFlow");
			return;
		}
		
		//默认启用
		int crflwStatus = 1;
		if (crflwUptype == 4 || crflwUptype == 14) { //取消
			crflwStatus = 0;
		}

		int newCrflwStep = 1;
		if (crflwUptype == 1) { //发起
			if(crflwStep == 1) { //渠道发起
				newCrflwStep = 3; //策划审核
			} else if (crflwStep == 2) { //策划发起
				newCrflwStep = 4; //营销总审核
			} else {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "步骤不符合要求！"), response);
				return;
			}
		}
		if (crflwUptype == 2) { //通过
			if(crflwStep == 3) { //策划审核
				newCrflwStep = 4; //营销总审核
			} else if (crflwStep == 4) { //营销总审核
				newCrflwStep = 5; //项目总审核
			} else if (crflwStep == 5) { //项目总审核
				newCrflwStep = 6; //财务审核
			} else {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "步骤不符合要求！"), response);
				return;
			}
		}
		if (crflwUptype == 3) { //退回
			if(crflwStep == 3) { //策划审核
				newCrflwStep = 1; //渠道发起
			} else if (crflwStep == 4) { //营销总审核
				if (crflwByType == 1) { //渠道发起类型
					newCrflwStep = 3; //策划审核
				} else if (crflwByType == 2) { //策划发起类型
					newCrflwStep = 2; //策划发起
				} else {
					writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "步骤不符合要求！"), response);
					return;
				}
			} else if (crflwStep == 5) { //项目总审核
				newCrflwStep = 4; //营销总审核
			} else if (crflwStep == 6) { //财务审核
				newCrflwStep = 5; //项目总审核
			} else {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "步骤不符合要求！"), response);
				return;
			}
		}
		if (crflwUptype == 4) { //取消
			if (crflwStep == 1 || crflwStep == 2) { //渠道发起或策划发起
				newCrflwStep = crflwStep; //取消不改变下一状态
			} else {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "步骤不符合要求！"), response);
				return;
			}			
		}
		if (crflwUptype == 5) { //完成
			if (crflwStep == 6) { //财务审核
				newCrflwStep = 7; //已完成
				//判断是否填写电子发票
				CoreInvoice coreInvoice = new CoreInvoice();
				coreInvoice.setCreicCode(crflwCode);
				coreInvoice = coreInvoiceService.getCoreInvoice(coreInvoice);
				if (null == coreInvoice || null == coreInvoice.getCreicPayee()) {
					writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请先填写电子发票！"), response);
					return;
				}
			} else {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "步骤不符合要求！"), response);
				return;
			}
		}

		if (crflwUptype == 11) { //发起
			//拨款资金额度必填
			if (null == crflwLoanMoney || crflwLoanMoney == 0) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "拨款资金额度必填！"), response);
				return;
			}
			if (crflwStep == 11) { //拨款发起
				newCrflwStep = 12; //拨款审核
				//判断拨款资金额度是否大于项目的资金额度
				if (crflwLoanMoney > coreProject.getCrproMoney()) {
					writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "拨款资金额大于项目的资金额度,不合法！"), response);
					return;
				}
			} else {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "步骤不符合要求！"), response);
				return;
			}
		}
		if (crflwUptype == 12) { //拨款
			//拨款资金额度必填
			if (null == crflwLoanMoney || crflwLoanMoney == 0) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "拨款资金额度必填！"), response);
				return;
			}
			if (crflwStep == 12) { //拨款审核
				newCrflwStep = 13; //已拨款
				//判断是否已上传凭证
				List<CoreAttachment> list = coreAttachmentService.findCoreAttachmentByCnd(crflwCode, 2);
				if (null == list || list.size() <= 0 || null == list.get(0)) {
					writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请先上传拨款凭证！"), response);
					return;
				}
				//判断资金额度是否大于项目的资金额度
				if (crflwLoanMoney > coreProject.getCrproMoney()) {
					writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "拨款资金额大于项目的资金额度,不合法！"), response);
					return;
				}
			} else {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "步骤不符合要求！"), response);
				return;
			}
		}
		//已拨款后不能退回,只能返回到上一界面
		if (crflwUptype == 13) { //退回
			if (crflwStep == 12) { //拨款审核
				newCrflwStep = 11; //拨款发起
			} else {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "步骤不符合要求！"), response);
				return;
			}
		}
		if (crflwUptype == 14) { //取消
			if (crflwStep == 11) { //拨款发起
				newCrflwStep = crflwStep; //取消不改变下一状态
			} else {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "步骤不符合要求！"), response);
				return;
			}
		}
		if (crflwUptype == 15) { //还款
			if (crflwStep == 13) { //已拨款
				newCrflwStep = 14; //已还款
			} else {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "步骤不符合要求！"), response);
				return;
			}
		}

		//当前所在步骤即为原来步骤
		boolean flag = coreFlowService.updateCoreFlowBase(crflwName, crflwDesc, crflwLoanMoney, crflwRemarks, crflwStatus, crflwUptype, crflwUpdateUser, crflwUpdateJob, new Date(), crflwCode, crflwStep);
		if(!flag) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "该流程别人已操作!"),response);
			logger.info("[CoreFlowController]:end updateCoreFlow");
			return;
		}
		//判断如果是拨款审核到已拨款，需要扣除项目相应资金额度，记录日志表
		if (crflwUptype == 12) { //拨款
			if (crflwStep == 12) { //拨款审核
				double oldMoney = coreProject.getCrproMoney();
				double newMoney = coreProject.getCrproMoney() - crflwLoanMoney;
				coreProject.setCrproMoney(newMoney);
				coreProjectService.updateCoreProject(coreProject);
				String crcpaDesc = "更新前：" + oldMoney + ",更新后：" + newMoney;
				this.addCoreCapitalLog(crflwUpdateUser, coreProject.getCrproUuid(), crcpaDesc, 2);
			}
		}
		//判断如果是已拨款到已还款，需要添加项目相应资金额度，记录日志表
		if (crflwUptype == 15) { //还款
			if (crflwStep == 13) { //已拨款
				double oldMoney = coreProject.getCrproMoney();
				double newMoney = coreProject.getCrproMoney() + crflwLoanMoney;
				coreProject.setCrproMoney(newMoney);
				coreProjectService.updateCoreProject(coreProject);
				String crcpaDesc = "更新前：" + oldMoney + ",更新后：" + newMoney;
				this.addCoreCapitalLog(crflwUpdateUser, coreProject.getCrproUuid(), crcpaDesc, 2);
			}
		}
		
		coreFlowService.updateCoreFlowStep(newCrflwStep, crflwCode, crflwStep);
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "操作成功!"),response);
		logger.info("[CoreFlowController]:end updateCoreFlow");
	}

	/**
	* 流程走向<List>
	* 
	* @param crflwCode 流程编码
	* @param response
	*/
	@RequestMapping(value="/log/list", method=RequestMethod.POST)
	public void logList (String crflwCode, HttpServletResponse response) {
		logger.info("[CoreFlowController]:begin logList");
		if (StringUtils.isBlank(crflwCode)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "流程编码必传！"), response);
			return;
		}
		
		Map<String, CoreUser> userMap = coreUserService.findCoreUserMap();
		List<CoreFlowLog> lists = coreFlowLogService.findCoreFlowLogListsByCrflwCode(crflwCode);
		List<CoreFlowLogVO> vos = new ArrayList<CoreFlowLogVO>();
		CoreFlowLogVO vo;
		for (CoreFlowLog coreFlowLog : lists) {
			vo = new CoreFlowLogVO();
			vo.convertPOToVO(coreFlowLog);
			CoreUser coreUser = userMap.get(coreFlowLog.getCrflwUpdateUser());
			if (null != coreUser) {
				vo.setCrflwUpdateUserHead(coreUser.getCrusrHead());
				vo.setCrflwUpdateUserName(coreUser.getCrusrName());
			}
			vos.add(vo);
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "流程走向获取成功!", vos),response);
		logger.info("[CoreFlowController]:end logList");
	}

	/**
	* 获取流程详情
	*
	* @param crflwCode 流程编码
	* @return
	*/
	@RequestMapping(value="/views", method=RequestMethod.POST)
	public void viewsCoreFlow (String crflwCode, HttpServletResponse response) {
		logger.info("[CoreFlowController]:begin viewsCoreFlow");

		if (StringUtil.isEmpty(crflwCode)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "流程编码不能为空!"), response);
			return;
		}

		CoreFlow coreFlow = coreFlowService.getCoreFlow(crflwCode);
		if (null == coreFlow) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "该流程不存在"), response);
			logger.info("[CoreFlowController]:end viewsCoreFlow");
			return;
		}
		CoreFlowVO coreFlowVO = new CoreFlowVO();
		coreFlowVO.convertPOToVO(coreFlow);
		CoreProject coreProject = new CoreProject();
		coreProject.setCrproUuid(coreFlow.getCrflwProject());
		coreProject = coreProjectService.getCoreProject(coreProject);
		if (null != coreProject) {
			coreFlowVO.setCrflwProjectName(coreProject.getCrproName());
		}
		CoreCompany coreCompany = new CoreCompany();
		coreCompany.setCrgroUuid(coreFlow.getCrflwCompany());
		coreCompany = coreCompanyService.getCoreCompany(coreCompany);
		if (null != coreCompany) {
			coreFlowVO.setCrflwCompanyName(coreCompany.getCrgroName());
		}

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "获取流程详情成功", coreFlowVO), response);
		logger.info("[CoreFlowController]:end viewsCoreFlow");
	}

	/**
	* 流程分页列表<Page>
	* 
	* @param pageNum 页码
	* @param pageSize 页数
	* @param crusrJob 职位 必传
	* @param crusrCompany 所属集团(集团管理员必传)
	* @param crusrProject 所属项目(渠道/策划/营销总/项目总/财务必传)
	* @param crusrUuid 投资客UUID(投资客必传)
	* @return
	*/
	@RequestMapping(value="/flow/list", method=RequestMethod.POST)
	public void flowList (Integer pageNum, Integer pageSize, Integer crusrJob, String crusrCompany, String crusrProject, String crusrUuid, HttpServletResponse response) {
		logger.info("[CoreFlowController]:begin flowList");
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		if (crusrJob == null || crusrJob == 0 || crusrJob > 8) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少职位"), response);
			logger.info("[CoreFlowController]:end flowList");
			return;
		}
		
		Page<CoreFlow> page = null;
		if (3 == crusrJob) { // 投资客
			if (StringUtil.isEmpty(crusrUuid)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "投资客流程缺少投资客UUID"), response);
				logger.info("[CoreFlowController]:end flowList");
				return;
			}
			List<CoreInvestment> coreInvestments = coreInvestmentService.findCoreInvestmentByCrsptUser(crusrUuid);
			if(null != coreInvestments && coreInvestments.size() > 0){
				List<String> codeList = new ArrayList<String>();
				for (CoreInvestment coreInvestment : coreInvestments) {
					codeList.add(coreInvestment.getCrsptFlow());
				}
				page = coreFlowService.findCoreFlowPageByCoreInvestment(pageNum, pageSize, codeList);
			}			
		}
		if (2 == crusrJob) { // 集团管理员
			if (StringUtil.isEmpty(crusrCompany)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "集团管理员流程缺少集团"), response);
				logger.info("[CoreFlowController]:end flowList");
				return;
			}
			page = coreFlowService.findCoreFlowForPagesByCompany(pageNum, pageSize, crusrCompany);
		}
		if (1 == crusrJob) { // 超级管理员
			page = coreFlowService.findCoreFlowForPagesByAdmin(pageNum, pageSize);
		}
		if (4 == crusrJob || 5 == crusrJob || 6 == crusrJob || 7 == crusrJob) { //项目人员
			if (StringUtil.isEmpty(crusrProject)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "项目人员流程缺少项目"), response);
				logger.info("[CoreFlowController]:end flowList");
				return;
			}
			page = coreFlowService.findCoreFlowForPagesByProject(pageNum, pageSize, crusrProject);
		}
		if (8 == crusrJob) { //财务
			if (StringUtil.isEmpty(crusrProject)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "财务流程缺少项目"), response);
				logger.info("[CoreFlowController]:end flowList");
				return;
			}
			page = coreFlowService.findCoreFlowForPagesByFinance(pageNum, pageSize, crusrProject);
		}
		Page<CoreFlowVO> pageVO = new Page<CoreFlowVO>(0, 10, 0);
		if(null != page) {
			Map<String, CoreProject> projectMap = coreProjectService.findCoreProjectMap();
			pageVO = new Page<CoreFlowVO>(page.getPageNumber(), page.getPageSize(), page.getTotalCount());
			List<CoreFlowVO> vos = new ArrayList<CoreFlowVO>();
			CoreFlowVO vo;
			for (CoreFlow coreFlow : page.getResult()) {
				vo = new CoreFlowVO();
				vo.convertPOToVO(coreFlow);
				CoreProject coreProject = projectMap.get(coreFlow.getCrflwProject());
				if (coreProject != null) {
					vo.setCrflwProjectName(coreProject.getCrproName());
				}			
				vos.add(vo);
			}
			pageVO.setResult(vos);
		}

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "流程分页列表获取成功!", pageVO),response);
		logger.info("[CoreFlowController]:end flowList");
	}

	/**
	* 待审分页列表<Page>
	* 
	* @param pageNum 页码
	* @param pageSize 页数
	* @param crusrJob 职位 必传
	* @param crusrProject 所属项目(营销总/项目总/财务必传)
	* @param crflwApplyUser 申请人(渠道/策划/财务必传)
	* @return
	*/
	@RequestMapping(value="/pending/list", method=RequestMethod.POST)
	public void pengingList (Integer pageNum, Integer pageSize, Integer crusrJob, String crusrProject, String crflwApplyUser, HttpServletResponse response) {
		logger.info("[CoreFlowController]:begin pengingList");
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		if (crusrJob != 1 && crusrJob != 4 && crusrJob != 5 && crusrJob != 6 && crusrJob != 7 && crusrJob != 8) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少职位"), response);
			logger.info("[CoreFlowController]:end pengingList");
			return;
		}
		
		Page<CoreFlow> page = null;
		if (1 == crusrJob) { // 超级管理员
			page = coreFlowService.findCoreFlowPendingForPagesByAdmin(pageNum, pageSize);		
		}
		if (4 == crusrJob) { // 渠道
			if (StringUtil.isEmpty(crflwApplyUser)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "渠道待审列表缺少申请人"), response);
				logger.info("[CoreFlowController]:end pengingList");
				return;
			}
			page = coreFlowService.findCoreFlowPendingForPagesByChannel(pageNum, pageSize, crflwApplyUser);
		}
		if (5 == crusrJob) { // 策划
			if (StringUtil.isEmpty(crflwApplyUser)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "策划待审列表缺少申请人"), response);
				logger.info("[CoreFlowController]:end pengingList");
				return;
			}
			if (StringUtil.isEmpty(crusrProject)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "策划待审列表缺少项目"), response);
				logger.info("[CoreFlowController]:end pengingList");
				return;
			}
			page = coreFlowService.findCoreFlowPendingForPagesByPlan(pageNum, pageSize, crflwApplyUser, crusrProject);
		}
		if (6 == crusrJob) { //营销总
			if (StringUtil.isEmpty(crusrProject)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "营销总待审列表缺少项目"), response);
				logger.info("[CoreFlowController]:end pengingList");
				return;
			}
			page = coreFlowService.findCoreFlowPendingForPagesByProject(pageNum, pageSize, 4, crusrProject);
		}
		if (7 == crusrJob) { //项目总
			if (StringUtil.isEmpty(crusrProject)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "项目总待审列表缺少项目"), response);
				logger.info("[CoreFlowController]:end pengingList");
				return;
			}
			page = coreFlowService.findCoreFlowPendingForPagesByProject(pageNum, pageSize, 5, crusrProject);
		}
		if (8 == crusrJob) { //财务
			if (StringUtil.isEmpty(crflwApplyUser)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "财务待审列表缺少申请人"), response);
				logger.info("[CoreFlowController]:end pengingList");
				return;
			}
			if (StringUtil.isEmpty(crusrProject)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "财务待审列表缺少项目"), response);
				logger.info("[CoreFlowController]:end pengingList");
				return;
			}
			page = coreFlowService.findCoreFlowPendingForPagesByFinance(pageNum, pageSize, crusrProject, crflwApplyUser);
		}
		Page<CoreFlowVO> pageVO = new Page<CoreFlowVO>(0, 10, 0);
		if(null != page) {
			Map<String, CoreProject> projectMap = coreProjectService.findCoreProjectMap();
			pageVO = new Page<CoreFlowVO>(page.getPageNumber(), page.getPageSize(), page.getTotalCount());
			List<CoreFlowVO> vos = new ArrayList<CoreFlowVO>();
			CoreFlowVO vo;
			for (CoreFlow coreFlow : page.getResult()) {
				vo = new CoreFlowVO();
				vo.convertPOToVO(coreFlow);
				CoreProject coreProject = projectMap.get(coreFlow.getCrflwProject());
				if (coreProject != null) {
					vo.setCrflwProjectName(coreProject.getCrproName());
				}			
				vos.add(vo);
			}
			pageVO.setResult(vos);
		}

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "待审分页列表获取成功!", pageVO),response);
		logger.info("[CoreFlowController]:end pengingList");
	}
	
	/**
	* 根据项目名称查询流程分页列表<Page>
	* 
	* @param pageNum 页码
	* @param pageSize 页数
	* @param projectName 项目名称
	* @param crusrUuid 投资客UUID
	* @return
	*/
	@RequestMapping(value="/flow/list/by/project", method=RequestMethod.POST)
	public void flowListByProject (Integer pageNum, Integer pageSize, String projectName, String crusrUuid, HttpServletResponse response) {
		logger.info("[CoreFlowController]:begin flowListByProject");
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		Page<CoreFlow> page = null;
		List<CoreProject> projectList = coreProjectService.findCoreProjectByName(projectName);
		if(null != projectList && projectList.size() > 0){
			List<String> projectUuidList = new ArrayList<String>();
			for (CoreProject coreProject : projectList) {
				projectUuidList.add(coreProject.getCrproUuid());
			}
			page = coreFlowService.findCoreFlowForPagesByProjectList(pageNum, pageSize, projectUuidList);
		}
		Page<CoreFlowVO> pageVO = new Page<CoreFlowVO>(0, 10, 0);
		if(null != page) {
			Map<String, CoreProject> projectMap = coreProjectService.findCoreProjectMap();
			pageVO = new Page<CoreFlowVO>(page.getPageNumber(), page.getPageSize(), page.getTotalCount());
			List<CoreFlowVO> vos = new ArrayList<CoreFlowVO>();
			CoreFlowVO vo;
			for (CoreFlow coreFlow : page.getResult()) {
				vo = new CoreFlowVO();
				vo.convertPOToVO(coreFlow);
				CoreProject coreProject = projectMap.get(coreFlow.getCrflwProject());
				if (coreProject != null) {
					vo.setCrflwProjectName(coreProject.getCrproName());
				}
				
				//判断是否已关联
				CoreInvestment coreInvestment = new CoreInvestment();
				coreInvestment.setCrsptFlow(coreFlow.getCrflwCode());
				coreInvestment.setCrsptUser(crusrUuid);
				coreInvestment = coreInvestmentService.getCoreInvestment(coreInvestment);
				if (null == coreInvestment) {
					vo.setRelate(false);
				} else {
					vo.setRelate(true);
				}
				
				vos.add(vo);
			}
			pageVO.setResult(vos);
		}

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "拨款流程分页列表获取成功!", pageVO),response);
		logger.info("[CoreFlowController]:end pengingList");
	}
	
	/**
	* 资金变动添加日志
	*
	* @param crcpaUser 更新人
	* @param crcpaBusi 更新对象
	* @param crcpaDesc 更新说明
	* @param crcpaType 更新类型:1集团2项目3超级管理员
	* @return
	*/
	public void addCoreCapitalLog (String crcpaUser, String crcpaBusi, String crcpaDesc, Integer crcpaType) {
		CoreCapitalLog coreCapitalLog = new CoreCapitalLog();
		String uuid = RandomUtil.generateString(16);
		coreCapitalLog.setCrcpaUuid(uuid);
		coreCapitalLog.setCrcpaCdate(new Date());
		coreCapitalLog.setCrcpaUser(crcpaUser);
		coreCapitalLog.setCrcpaBusi(crcpaBusi);
		coreCapitalLog.setCrcpaDesc(crcpaDesc);
		coreCapitalLog.setCrcpaType(crcpaType);
		coreCapitalLogService.insertCoreCapitalLog(coreCapitalLog);
	}

	/**
	* 申请资金时添加电子发票
	*
	* @param creicCode 发票编码(流程编码)
	* @param creicPng 发票图片(流程编码.png)
	* @param creicCompany 所属集团
	* @param creicProject 所属项目
	* @param creicMatter 事项
	* @param creicOperator 操作人
	*/
	public void addCoreInvoice (String creicCode, String creicPng, String creicCompany, String creicProject, String creicMatter, String creicOperator) {
		CoreInvoice coreInvoice = new CoreInvoice();
		String uuid = RandomUtil.generateString(16);
		coreInvoice.setCreicUuid(uuid);
		coreInvoice.setCreicCode(creicCode);
		coreInvoice.setCreicPng(creicPng);
		coreInvoice.setCreicCompany(creicCompany);
		coreInvoice.setCreicProject(creicProject);
		coreInvoice.setCreicMatter(creicMatter);
		coreInvoice.setCreicCdate(new Date());
		coreInvoice.setCreicUdate(new Date());
		coreInvoice.setCreicOperator(creicOperator);
		coreInvoiceService.insertCoreInvoice(coreInvoice);
		
		String srcPath = Thread.currentThread().getContextClassLoader().getResource("\\basic\\dzfp.png").getPath();
		String dirPath = ConfigIni.getIniStrValue("INVOICE_DIR", "path", "F:\\code\\src\\main\\webapp\\views\\invoice\\");
		String mediaDirPath = ConfigIni.getIniStrValue("INVOICE_DIR", "mediaPath", "C:\\attachment\\oa\\invoice\\");
		//拷贝图片
		try {
			IOUtil.copy(srcPath, dirPath + creicPng);
			IOUtil.copy(srcPath, mediaDirPath + creicPng);
			logger.info(creicCode + "申请资金时拷贝电子发票成功!");
		} catch (IOException e) {
			logger.info(creicCode + "申请资金时拷贝电子发票失败!");
		}	
	}

	/**
	* 修改保存电子发票
	*
	* @param creicCode 发票编码(流程编码)
	* @param creicPayee 收款单位
	* @param creicPayeeAccount 收款账号
	* @param creicPayeeBank 收款开户行
	* @param creicPayer 付款单位
	* @param creicPayerAccount 付款账号
	* @param creicPayerBank 付款开户行
	* @param creicMatter 事项
	* @param creicInvoiceType 发票类型
	* @param creicLowAmount 小写金额
	* @param creicRemarks 备注
	* @param creicDrawer 开票人
	* @param creicOperator 操作人
	* @return
	*/
	@RequestMapping(value="/update/invoice", method=RequestMethod.POST)
	public void updateInvoice (String creicCode, String creicPayee, String creicPayeeAccount, String creicPayeeBank, 
			String creicPayer, String creicPayerAccount, String creicPayerBank, String creicMatter, String creicInvoiceType, 
			Double creicLowAmount, String creicRemarks, String creicDrawer, String creicOperator, HttpServletResponse response) {
		logger.info("[CoreInvoiceController]:begin updateInvoice");

		if (StringUtil.isEmpty(creicCode)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "发票编码(流程编码)不能为空!"), response);
			return;
		}
		if (StringUtil.isEmpty(creicPayee)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "收款单位必填!"), response);
			return;
		}
		if (StringUtil.isEmpty(creicPayeeAccount)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "收款账号必填!"), response);
			return;
		}
		if (StringUtil.isEmpty(creicPayeeBank)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "收款开户行必填!"), response);
			return;
		}
		if (StringUtil.isEmpty(creicPayer)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "付款单位必填!"), response);
			return;
		}
		if (StringUtil.isEmpty(creicPayerAccount)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "付款账号必填!"), response);
			return;
		}
		if (StringUtil.isEmpty(creicPayerBank)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "付款开户行必填!"), response);
			return;
		}
		if (StringUtil.isEmpty(creicInvoiceType)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "发票类型必填!"), response);
			return;
		}
		if (StringUtil.isEmpty(creicDrawer)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "开票人必填!"), response);
			return;
		}
		if (StringUtil.isEmpty(creicOperator)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[操作人]不能为空!"), response);
			return;
		}
		if (null == creicLowAmount) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "金额必填!"), response);
			return;
		}
		
		CoreInvoice coreInvoice = new CoreInvoice();
		coreInvoice.setCreicCode(creicCode);
		coreInvoice = coreInvoiceService.getCoreInvoice(coreInvoice);
		if (null == coreInvoice) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "电子发票不存在"), response);
			logger.info("[CoreFlowController]:end updateInvoice");
		}

		coreInvoice.setCreicPayee(creicPayee);
		coreInvoice.setCreicPayeeAccount(creicPayeeAccount);
		coreInvoice.setCreicPayeeBank(creicPayeeBank);
		coreInvoice.setCreicPayer(creicPayer);
		coreInvoice.setCreicPayerAccount(creicPayerAccount);
		coreInvoice.setCreicPayerBank(creicPayerBank);
		coreInvoice.setCreicMatter(creicMatter);
		coreInvoice.setCreicInvoiceType(creicInvoiceType);
		coreInvoice.setCreicLowAmount(creicLowAmount);
		coreInvoice.setCreicCapAmount(ConvertNumUtil.NumToChinese(creicLowAmount));
		coreInvoice.setCreicRemarks(creicRemarks);
		Date date = new Date();
		coreInvoice.setCreicUdate(date);
		coreInvoice.setCreicDrawer(creicDrawer);
		coreInvoice.setCreicOperator(creicOperator);

		//获取项目数据
		CoreProject coreProject = new CoreProject();
		coreProject.setCrproUuid(coreInvoice.getCreicProject());
		coreProject = coreProjectService.getCoreProject(coreProject);
		if(null == coreProject){
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "所属项目不存在!"), response);
			return;
		}

		//获取发票类型名称
		CoreInvoiceType coreInvoiceType = new CoreInvoiceType();
		coreInvoiceType.setCrintUuid(coreInvoice.getCreicInvoiceType());
		coreInvoiceType = coreInvoiceTypeService.getCoreInvoiceType(coreInvoiceType);
		if (null == coreInvoiceType) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "发票类型不存在!"), response);
			return;
		}
				
		coreInvoiceService.updateCoreInvoice(coreInvoice);
		
		String bgImgPath = Thread.currentThread().getContextClassLoader().getResource("\\basic\\dzfp.png").getPath();
		new InvoicePicUtil().outPic(bgImgPath, creicCode, creicPayee, creicPayeeAccount, creicPayeeBank, creicPayerBank, creicPayerAccount, 
				creicPayerBank, coreProject.getCrproName(), creicMatter, coreInvoiceType.getCrintName(), creicLowAmount, creicRemarks, 
				DateUtil.formatDateByFormat(date, DateUtil.CHINESE_PATTEN), creicDrawer);
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "修改保存电子发票!"),response);
		logger.info("[CoreInvoiceController]:end updateInvoice");
	}

	/**
	* 根据发票编码(流程编码)获取电子发票详情
	*
	* @param creicCode 发票编码(流程编码)
	* @return
	*/
	@RequestMapping(value="views/invoice", method=RequestMethod.POST)
	public void viewsInvoice (String creicCode, HttpServletResponse response) {
		logger.info("[CoreFlowController]:begin viewsInvoice");

		if (StringUtil.isEmpty(creicCode)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "发票编码(流程编码)不能为空!"), response);
			return;
		}

		CoreInvoice coreInvoice = new CoreInvoice();
		coreInvoice.setCreicCode(creicCode);
		coreInvoice = coreInvoiceService.getCoreInvoice(coreInvoice);
		if (null == coreInvoice) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "电子发票不存在"), response);
			logger.info("[CoreFlowController]:end viewsInvoice");
		}

		CoreInvoiceVO coreInvoiceVO = new CoreInvoiceVO();
		coreInvoiceVO.convertPOToVO(coreInvoice);
		
		//获取集团数据
		CoreCompany coreCompany = new CoreCompany();
		coreCompany.setCrgroUuid(coreInvoice.getCreicCompany());
		coreCompany = coreCompanyService.getCoreCompany(coreCompany);
		if(null != coreCompany){
			coreInvoiceVO.setCreicCompanyName(coreCompany.getCrgroName());
		}
		
		//获取项目数据
		CoreProject coreProject = new CoreProject();
		coreProject.setCrproUuid(coreInvoice.getCreicProject());
		coreProject = coreProjectService.getCoreProject(coreProject);
		if(null != coreProject){
			coreInvoiceVO.setCreicProjectName(coreProject.getCrproName());
		}

		//获取发票类型名称
		if (!StringUtil.isEmpty(coreInvoice.getCreicInvoiceType())) {
			CoreInvoiceType coreInvoiceType = new CoreInvoiceType();
			coreInvoiceType.setCrintUuid(coreInvoice.getCreicInvoiceType());
			coreInvoiceType = coreInvoiceTypeService.getCoreInvoiceType(coreInvoiceType);
			if (null != coreInvoiceType) {
				coreInvoiceVO.setCreicInvoiceTypeName(coreInvoiceType.getCrintName());
			}
		}
		
		//获取电子发票URL
		String urlPath = ConfigIni.getIniStrValue("INVOICE_DIR", "urlPath", "http://localhost:8080/oa/views/invoice/");
		String urlMediaPath = ConfigIni.getIniStrValue("INVOICE_DIR", "urlMediaPath", "http://media.hzlingdian.com/oa/invoice/");
		if (FileUtil.isExist(urlPath + coreInvoice.getCreicPng())) {
			coreInvoiceVO.setCreicPng(urlPath + coreInvoice.getCreicPng() + "?random = " + new Date().getTime());
		} else {
			coreInvoiceVO.setCreicPng(urlMediaPath + coreInvoice.getCreicPng() + "?random = " + new Date().getTime());
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "获取电子发票成功", coreInvoiceVO), response);
		logger.info("[CoreFlowController]:end viewsInvoice");
	}
	
	/**
	* 获取电子发票类型列表<List>
	* 
	* @return
	*/
	@RequestMapping(value="/invoice/type/find/all", method=RequestMethod.POST)
	public void findCoreInvoiceTypeList (HttpServletResponse response) {
		logger.info("[CoreFlowController]:begin findCoreInvoiceTypeList");

		List<CoreInvoiceType> lists = coreInvoiceTypeService.findCoreInvoiceTypeList();
		List<CoreInvoiceTypeVO> vos = new ArrayList<CoreInvoiceTypeVO>();
		CoreInvoiceTypeVO vo;
		for (CoreInvoiceType coreInvoiceType : lists) {
			vo = new CoreInvoiceTypeVO();
			vo.convertPOToVO(coreInvoiceType);
			vos.add(vo);
		}

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "list列表获取成功!", vos),response);
		logger.info("[CoreFlowController]:end findCoreInvoiceTypeList");
	}

}