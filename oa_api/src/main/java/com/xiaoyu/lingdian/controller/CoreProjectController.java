package com.xiaoyu.lingdian.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoyu.lingdian.core.mybatis.page.Page;
import com.xiaoyu.lingdian.entity.CoreCapitalLog;
import com.xiaoyu.lingdian.entity.CoreCompany;
import com.xiaoyu.lingdian.entity.CoreProject;
import com.xiaoyu.lingdian.entity.CoreUser;
import com.xiaoyu.lingdian.service.CoreCapitalLogService;
import com.xiaoyu.lingdian.service.CoreCompanyService;
import com.xiaoyu.lingdian.service.CoreProjectService;
import com.xiaoyu.lingdian.service.CoreUserService;
import com.xiaoyu.lingdian.tool.RandomUtil;
import com.xiaoyu.lingdian.tool.StringUtil;
import com.xiaoyu.lingdian.tool.out.ResultMessageBuilder;
import com.xiaoyu.lingdian.vo.CoreProjectVO;

@Controller
@RequestMapping(value="/coreProject")
public class CoreProjectController extends BaseController {

	/**
	* 项目表
	*/
	@Autowired
	private CoreProjectService coreProjectService;	
	
	/**
	* 资金变动表
	*/
	@Autowired
	private CoreCapitalLogService coreCapitalLogService;
	
	/**
	* 用户表
	*/
	@Autowired
	private CoreUserService coreUserService;

	/**
	* 集团表
	*/
	@Autowired
	private CoreCompanyService coreCompanyService;
	
	/**
	* 添加(需要记录资金额度日志表)
	*
	* @param crproName 项目名称
	* @param crproDesc 项目简介
	* @param crproMoney 资金额度
	* @param crproCompany 所属集团
	* @param crcpaUser 添加人uuid(用于权限判断和资金日志记录)
	* @return
	*/
	@RequestMapping(value="/add/coreProject", method=RequestMethod.POST)
	public void addCoreProject (String crproName, String crproDesc, Double crproMoney, String crproCompany, String crcpaUser, HttpServletResponse response) {
		logger.info("[CoreProjectController]:begin addCoreProject");
		if (StringUtil.isEmpty(crproName)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[项目名称]不能为空!"), response);
			return;
		}
		if (StringUtil.isEmpty(crproCompany)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[所属集团]不能为空!"), response);
			return;
		}
		if (StringUtil.isEmpty(crcpaUser)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[添加人UUID]不能为空!"), response);
			return;
		}
		if (null==crproMoney) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[资金额度]不能为空!"), response);
			return;
		}
		//判断当前登陆者是否具有添加权限
		CoreUser user = new CoreUser();
		user.setCrusrUuid(crcpaUser);
		user = coreUserService.getCoreUser(user);
		int crusrJob = user.getCrusrJob();
		if(1!=crusrJob && 2!=crusrJob){//非超级管理员、集团管理员没有权限
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "当前登录用户没有添加项目权限！"), response);
			return;
		}
		
		if(null!=(coreProjectService.getCoreProjectByName(crproName))) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "该项目名称已存在，请重新输入！"), response);
			return;
		}
		
		//判断集团下是否还能分配这个资金额度
		CoreCompany coreCompany = new CoreCompany();
		coreCompany.setCrgroUuid(crproCompany);
		coreCompany = coreCompanyService.getCoreCompany(coreCompany);
		if (null == coreCompany) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "所属公司已禁用，请重新选择！"), response);
			return;
		}
		List<CoreProject> projectList = coreProjectService.findCoreProjectList(crproCompany);
		double projectTotal = 0;
		for (CoreProject coreProject : projectList) {
			projectTotal += coreProject.getCrproMoney();
		}
		if (coreCompany.getCrgroMoney() - projectTotal < crproMoney) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "项目资金额度总和已超过所属集团的资金额度，请重新分配！"), response);
			return;
		}
		CoreProject coreProject = new CoreProject();
		String uuid = RandomUtil.generateString(16);
		coreProject.setCrproUuid(uuid);
		coreProject.setCrproName(crproName);
		coreProject.setCrproDesc(crproDesc);
		coreProject.setCrproMoney(crproMoney);
		coreProject.setCrproCdate(new Date());
		coreProject.setCrproUdate(new Date());
		coreProject.setCrproCompany(crproCompany);
		coreProject.setCrproStatus(1);

		coreProjectService.insertCoreProject(coreProject);
		//资金变动日志记录
		if(null!=crproMoney){
			String obj = null;
			if(1==crusrJob){
				obj="超级管理员";
			} else if(2==crusrJob) {
				obj="集团管理员";
			}
			addCoreCapitalLog(crcpaUser, uuid, obj+"：项目资金额度更新前：0,更新后："+crproMoney.doubleValue()+";", 2);
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "新增成功!"),response);
		logger.info("[CoreProjectController]:end addCoreProject");

	}

	/**
	* 修改基本信息
	* 修改时都不判断名称唯一了,单个更新，非空判断交给界面
	* @param crproUuid 标识UUID
	* @param crproName 项目名称
	* @param crproDesc 项目简介
	* @return
	*/
	@RequestMapping(value="/update/coreProject", method=RequestMethod.POST)
	public void updateCoreProject (String crproUuid, String crproName, String crproDesc, HttpServletResponse response) {
		logger.info("[CoreProjectController]:begin updateCoreProject");

		if (StringUtil.isEmpty(crproUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}

		CoreProject coreProject = new CoreProject();
		coreProject.setCrproUuid(crproUuid);
		coreProject.setCrproName(crproName);
		coreProject.setCrproDesc(crproDesc);
		coreProject.setCrproUdate(new Date());

		coreProjectService.updateCoreProject(coreProject);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "修改成功!"),response);
		logger.info("[CoreProjectController]:end updateCoreProject");

	}
	
	/**
	* 修改项目资金(需要判断资金额度是否改变,改变了记录日志表，没改变不记录日志，资金更新说明后台拼接)
	* 修改暂时不判断集团额度和项目总额度的关系了,更新前：100,更新后：500
	* @param crcpaUser 更新人
	* @param crproMoney 资金额度
	* @param crcpaBusi 更新对象(项目)
	* @return
	*/
	@RequestMapping(value="/update/projectCapital", method=RequestMethod.POST)
	public void updateProjectCapital (String crcpaUser, Double crproMoney, String crcpaBusi, HttpServletResponse response) {
		logger.info("[CoreProjectController]:begin updateCoreProject");

		if (StringUtil.isEmpty(crcpaUser)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[更新人UUID]不能为空!"), response);
			return;
		}
		if (StringUtil.isEmpty(crcpaBusi)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[更新对象UUID]不能为空!"), response);
			return;
		}
		if (null==crproMoney) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[资金额度]不能为空!"), response);
			return;
		}
		CoreUser user = new CoreUser();
		user.setCrusrUuid(crcpaUser);
		user = coreUserService.getCoreUser(user);
		int crusrJob = user.getCrusrJob();
		if(1==crusrJob || 2==crusrJob) {//更新人权限判断，只有超级管理员和集团管理员才能修改资金
			CoreProject coreProject = new CoreProject();
			coreProject.setCrproUuid(crcpaBusi);
			CoreProject project = coreProjectService.getCoreProject(coreProject);
			Double crproMoneyOld = project.getCrproMoney();
			coreProject.setCrproMoney(crproMoney);
			coreProject.setCrproUdate(new Date());
			coreProjectService.updateCoreProject(coreProject);
			//资金变动记录到资金池日志表
			if(crproMoney.doubleValue()!=(crproMoneyOld.doubleValue())) {//资金变动
				String obj = null;
				if(1==crusrJob) {//超级管理员
					obj = "超级管理员：";
				} else if(2==crusrJob){//集团管理员
					obj = "集团管理员：";
				}
				addCoreCapitalLog(crcpaUser, crcpaBusi, obj+"：项目资金额度更新前："+crproMoneyOld.doubleValue()+",更新后："+crproMoney.doubleValue()+";", 2);
			} 
		}else {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "当前用户无修改项目资金权限!"), response);
			return;
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "项目资金修改成功!"),response);
		logger.info("[CoreProjectController]:end updateCoreProject");

	}
	
	/**
	* 禁用操作
	*
	* @param crproUuid 标识UUID
	* @return
	*/
	@RequestMapping(value="/disable/coreProject", method=RequestMethod.POST)
	public void disableCoreProject (String crproUuid, HttpServletResponse response) {
		logger.info("[CoreProjectController]:begin disableCoreProject");

		if (StringUtil.isEmpty(crproUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}

		CoreProject coreProject = new CoreProject();
		coreProject.setCrproUuid(crproUuid);
		coreProjectService.disableCoreProject(coreProject);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "禁用成功!"),response);
		logger.info("[CoreProjectController]:end disableCoreProject");

	}
	
	/**
	* 获取单个
	* 
	* @param crproUuid 标识UUID
	* @return
	*/
	@RequestMapping(value="/views", method=RequestMethod.POST)
	public void viewsCoreProject (String crproUuid, HttpServletResponse response) {
		logger.info("[CoreProjectController]:begin viewsCoreProject");

		if (StringUtil.isEmpty(crproUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}

		CoreProject coreProject = new CoreProject();
		coreProject.setCrproUuid(crproUuid);

		coreProject = coreProjectService.getCoreProject(coreProject);
		if (null == coreProject) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "该项目不存在"), response);
			logger.info("[CoreProjectController]:end viewsCoreProject");
		}
	
		CoreCompany coreCompany = new CoreCompany();
		coreCompany.setCrgroUuid(coreProject.getCrproCompany());
		coreCompany = coreCompanyService.getCoreCompany(coreCompany);

		CoreProjectVO coreProjectVO = new CoreProjectVO();
		coreProjectVO.convertPOToVO(coreProject);
		if (null != coreCompany) {
			coreProjectVO.setCrproCompanyName(coreCompany.getCrgroName());
		}

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "获取单个信息成功", coreProjectVO), response);
		logger.info("[CoreProjectController]:end viewsCoreProject");

	}

	/**
	* 获取列表<Page>
	* 
	* @param pageNum 页码
	* @param pageSize 页数
	* @param crproCompany 集团UUID
	* @return
	*/
	@RequestMapping(value="/find/by/cnd", method=RequestMethod.GET)
	public void findCoreProjectPage (Integer pageNum, Integer pageSize,String crproCompany, HttpServletResponse response) {
		logger.info("[CoreProjectController]:begin findCoreProjectPage");

		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		Page<CoreProject> page = coreProjectService.findCoreProjectPage(pageNum, pageSize, crproCompany);
		Page<CoreProjectVO> pageVO = new Page<CoreProjectVO>(page.getPageNumber(), page.getPageSize(), page.getTotalCount());
		List<CoreProjectVO> vos = new ArrayList<CoreProjectVO>();
		CoreProjectVO vo;
		for (CoreProject coreProject : page.getResult()) {
			vo = new CoreProjectVO();

			vo.convertPOToVO(coreProject);

			vos.add(vo);
		}
		pageVO.setResult(vos);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "page列表获取成功!", pageVO),response);
		logger.info("[CoreProjectController]:end findCoreProjectPage");

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

}