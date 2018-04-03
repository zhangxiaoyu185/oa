package com.xiaoyu.lingdian.controller;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import com.xiaoyu.lingdian.tool.RandomUtil;
import com.xiaoyu.lingdian.tool.StringUtil;
import javax.servlet.http.HttpServletResponse;
import com.xiaoyu.lingdian.core.mybatis.page.Page;
import org.springframework.stereotype.Controller;
import com.xiaoyu.lingdian.tool.out.ResultMessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import com.xiaoyu.lingdian.controller.BaseController;
import com.xiaoyu.lingdian.entity.CoreCapitalLog;
import com.xiaoyu.lingdian.entity.CoreCompany;
import com.xiaoyu.lingdian.entity.CoreUser;
import com.xiaoyu.lingdian.service.CoreCapitalLogService;
import com.xiaoyu.lingdian.service.CoreCompanyService;
import com.xiaoyu.lingdian.service.CoreUserService;
import com.xiaoyu.lingdian.vo.CoreCompanyVO;

@Controller
@RequestMapping(value="/coreCompany")
public class CoreCompanyController extends BaseController {

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
	* 用户表
	*/
	@Autowired
	private CoreUserService coreUserService;
	
	/**
	* 添加(需要记录资金额度日志表)
	*
	* @param crgroName 集团名称
	* @param crgroDesc 集团简介
	* @param crgroMoney 资金额度(可为0)
	* @param crcpaUser 添加人uuid(用于权限判断和资金日志记录)
	* @return
	*/
	@RequestMapping(value="/add/coreCompany", method=RequestMethod.POST)
	public void addCoreCompany (String crgroName, String crgroDesc, Double crgroMoney, String crcpaUser, HttpServletResponse response) {
		logger.info("[CoreCompanyController]:begin addCoreCompany");
		
		if (StringUtil.isEmpty(crgroName)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[集团名称]不能为空!"), response);
			return;
		}
		if (null==crgroMoney) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[资金额度]不能为空!"), response);
			return;
		}
		if (StringUtil.isEmpty(crcpaUser)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[添加人UUID]不能为空!"), response);
			return;
		}
		//判断当前登陆者是否具有添加权限
		CoreUser user = new CoreUser();
		user.setCrusrUuid(crcpaUser);
		user = coreUserService.getCoreUser(user);
		int crusrJob = user.getCrusrJob();
		if(1!=crusrJob){//非超级管理员人员没有权限
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "当前登录用户没有添加集团权限！"), response);
			return;
		}
		if(null!=(coreCompanyService.getCoreCompanyByName(crgroName))){
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "该集团名称已存在，请重新输入！"), response);
			return;
		}
		
		CoreCompany coreCompany = new CoreCompany();
		String uuid = RandomUtil.generateString(16);
		coreCompany.setCrgroUuid(uuid);
		coreCompany.setCrgroName(crgroName);
		coreCompany.setCrgroDesc(crgroDesc);
		coreCompany.setCrgroCdate(new Date());
		coreCompany.setCrgroUdate(new Date());
		coreCompany.setCrgroMoney(crgroMoney);
		coreCompany.setCrgroStatus(1);

		coreCompanyService.insertCoreCompany(coreCompany);

		//资金变动日志记录
		if(null!=crgroMoney){
			addCoreCapitalLog(crcpaUser, uuid, "超级管理员：集团资金额度更新前：0,更新后："+crgroMoney.doubleValue()+";", 1);
		}
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "新增成功!"),response);
		logger.info("[CoreCompanyController]:end addCoreCompany");

	}

	/**
	* 修改基本信息
	* 修改时都不判断名称唯一了,单个更新，非空判断交给界面
	* @param crgroUuid 标识UUID
	* @param crgroName 集团名称
	* @param crgroDesc 集团简介
	* @return
	*/
	@RequestMapping(value="/update/coreCompany", method=RequestMethod.POST)
	public void updateCoreCompany (String crgroUuid, String crgroName, String crgroDesc, HttpServletResponse response) {
		logger.info("[CoreCompanyController]:begin updateCoreCompany");
		if (StringUtil.isEmpty(crgroUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}
		CoreCompany coreCompany = new CoreCompany();
		coreCompany.setCrgroUuid(crgroUuid);
		coreCompany.setCrgroName(crgroName);
		coreCompany.setCrgroDesc(crgroDesc);
		coreCompany.setCrgroUdate(new Date());

		coreCompanyService.updateCoreCompany(coreCompany);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "修改成功!"),response);
		logger.info("[CoreCompanyController]:end updateCoreCompany");

	}
	
	/**
	* 修改集团资金(需要判断资金额度是否改变,改变了记录日志表，没改变不记录日志，资金更新说明后台拼接)
	* 更新前：100,更新后：500
	* @param crcpaUser 更新人
	* @param crgroMoney 资金额度
	* @param crcpaBusi 更新对象(集团)
	* @return
	*/
	@RequestMapping(value="/update/companyCapital", method=RequestMethod.POST)
	public void updateCompanyCapital (String crcpaUser, Double crgroMoney, String crcpaBusi, HttpServletResponse response) {
		logger.info("[CoreCompanyController]:begin updateCompanyCapital");

		if (StringUtil.isEmpty(crcpaUser)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[更新人UUID]不能为空!"), response);
			return;
		}
		if (StringUtil.isEmpty(crcpaBusi)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[更新对象UUID]不能为空!"), response);
			return;
		}
		if (null==crgroMoney) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[资金额度]不能为空!"), response);
			return;
		}
		CoreUser user = new CoreUser();
		user.setCrusrUuid(crcpaUser);
		user = coreUserService.getCoreUser(user);
		int crusrJob = user.getCrusrJob();
		if(1==crusrJob || 2==crusrJob) {//更新人权限判断，只有超级管理员和集团管理员才能修改资金
			CoreCompany coreCompany = new CoreCompany();
			coreCompany.setCrgroUuid(crcpaBusi);
			CoreCompany company = coreCompanyService.getCoreCompany(coreCompany);
			Double crgroMoneyOld = company.getCrgroMoney();
			coreCompany.setCrgroMoney(crgroMoney);
			coreCompany.setCrgroUdate(new Date());
			coreCompanyService.updateCoreCompany(coreCompany);
			//资金变动记录到资金池日志表
			if(crgroMoney.doubleValue()!=(crgroMoneyOld.doubleValue())) {//资金变动
				String obj = null;
				if(1==crusrJob) {//超级管理员
					obj = "超级管理员：";
				} else if(2==crusrJob){//集团管理员
					obj = "集团管理员：";
				}
				addCoreCapitalLog(crcpaUser, crcpaBusi, obj+"：集团资金额度更新前："+crgroMoneyOld.doubleValue()+",更新后："+crgroMoney.doubleValue()+";", 1);
			}
		} else {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "当前用户无修改集团资金权限!"), response);
			return;
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "集团资金修改成功!"),response);
		logger.info("[CoreCompanyController]:end updateCompanyCapital");

	}
	
	/**
	* 禁用操作
	*
	* @param crgroUuid 标识UUID
	* @return
	*/
	@RequestMapping(value="/disable/coreCompany", method=RequestMethod.POST)
	public void disableCoreCompany (String crgroUuid, HttpServletResponse response) {
		logger.info("[CoreCompanyController]:begin disableCoreCompany");

		if (StringUtil.isEmpty(crgroUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}

		CoreCompany coreCompany = new CoreCompany();
		coreCompany.setCrgroUuid(crgroUuid);
		coreCompanyService.disableCoreCompany(coreCompany);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "禁用成功!"),response);
		logger.info("[CoreCompanyController]:end disableCoreCompany");

	}
	
	/**
	* 获取单个
	*
	* @param crgroUuid 标识UUID
	* @return
	*/
	@RequestMapping(value="/views", method=RequestMethod.POST)
	public void viewsCoreCompany (String crgroUuid, HttpServletResponse response) {
		logger.info("[CoreCompanyController]:begin viewsCoreCompany");
		if (StringUtil.isEmpty(crgroUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}
		CoreCompany coreCompany = new CoreCompany();
		coreCompany.setCrgroUuid(crgroUuid);

		coreCompany = coreCompanyService.getCoreCompany(coreCompany);

		CoreCompanyVO coreCompanyVO = new CoreCompanyVO();
		coreCompanyVO.convertPOToVO(coreCompany);
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "获取单个信息成功", coreCompanyVO), response);
		logger.info("[CoreCompanyController]:end viewsCoreCompany");

	}

	/**
	* 获取集团列表
	* 
	* @return
	*/
	@RequestMapping(value="/find/all", method=RequestMethod.POST)
	public void findAll (HttpServletResponse response) {
		logger.info("[CoreCompanyController]:begin findAll");
		List<CoreCompany> list = coreCompanyService.findCoreCompanyAll();

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "集团列表获取成功!", list), response);
		logger.info("[CoreCompanyController]:end findAll");
	}

	/**
	* 获取分页集团列表
	* 
	* @param pageNum 页码
	* @param pageSize 页数
	* @return
	*/
	@RequestMapping(value="/find/coreCompanyPage", method=RequestMethod.POST)
	public void findCoreCompanyPage (Integer pageNum, Integer pageSize, HttpServletResponse response) {
		logger.info("[CoreCompanyController]:begin findCoreCompanyPage");
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		Page<CoreCompany> page = coreCompanyService.findCoreCompanyPage(pageNum, pageSize);
		Page<CoreCompanyVO> pageVO = new Page<CoreCompanyVO>(page.getPageNumber(), page.getPageSize(), page.getTotalCount());
		List<CoreCompanyVO> vos = new ArrayList<CoreCompanyVO>();
		CoreCompanyVO vo;
		for (CoreCompany coreCompany : page.getResult()) {
			vo = new CoreCompanyVO();
			vo.convertPOToVO(coreCompany);
			vos.add(vo);
		}
		pageVO.setResult(vos);
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "page列表获取成功!", pageVO),response);
		logger.info("[CoreCompanyController]:end findCoreCompanyPage");

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