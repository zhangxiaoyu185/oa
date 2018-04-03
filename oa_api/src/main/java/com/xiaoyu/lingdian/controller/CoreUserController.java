package com.xiaoyu.lingdian.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
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
import com.xiaoyu.lingdian.vo.CoreUserVO;

@Controller
@RequestMapping(value="/coreUser")
public class CoreUserController extends BaseController {

	private static final String PHONE_PATTERN = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
	/**
	* 用户表
	*/
	@Autowired
	private CoreUserService coreUserService;
	
	/**
	* 资金变动表
	*/
	@Autowired
	private CoreCapitalLogService coreCapitalLogService;
	
	/**
	* 集团表
	*/
	@Autowired
	private CoreCompanyService coreCompanyService;
	
	/**
	* 项目表
	*/
	@Autowired
	private CoreProjectService coreProjectService;
	
	/**
	 * 重置密码
	 * 
	 * @param crusrUuid	标识UUID
	 * @param password 密码 (MD5处理)
	 * @param response
	 */
	@RequestMapping(value = "/reset/pwd", method = RequestMethod.POST)
	public void resetPwd(String crusrUuid, String password, HttpServletResponse response) {
		logger.info("[CoreUserController.resetPwd]:begin resetPwd.");
		if (StringUtils.isBlank(crusrUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请输入uuid！"), response);
			return;
		}		
		if (StringUtils.isBlank(password)) {
			password = "e10adc3949ba59abbe56e057f20f883e";
		}

		CoreUser coreUser = new CoreUser();
		coreUser.setCrusrUuid(crusrUuid);
		coreUser.setCrusrPassword(password);
		coreUser.setCrusrUdate(new Date());
		coreUserService.updateCoreUser(coreUser);
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "重置密码成功！"), response);
		logger.info("[CoreUserController.resetPwd]:end resetPwd.");
	}

	/**
	 * 登录(需要判断账号所属的项目和集团是否是启用,如果是禁用需要弹提示,不能登录)
	 * 
	 * @param crusrName	登录名
	 * @param crusrPassword (MD5处理)
	 * @param response
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(String crusrName, String crusrPassword, HttpServletResponse response) {
		logger.info("[CoreUserController.login]:begin login.");
		if (StringUtils.isBlank(crusrName)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请输入帐户名称！"), response);
			return;
		}		
		if (StringUtils.isBlank(crusrPassword)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请输入密码！"), response);
			return;
		}
		CoreUser coreUser = new CoreUser();
		coreUser.setCrusrName(crusrName);
		coreUser = coreUserService.getCoreUserByName(coreUser);
		if(coreUser == null || !crusrPassword.equals(coreUser.getCrusrPassword())) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "密码错误！"), response);
			return;
		}
		int crusrJob = coreUser.getCrusrJob();
		if(2==crusrJob) {//集团管理员
			String crusrCompany = coreUser.getCrusrCompany();
			CoreCompany coreCompany = new CoreCompany();
			coreCompany.setCrgroUuid(crusrCompany);
			coreCompany = coreCompanyService.getCoreCompany(coreCompany);
			if(null == coreCompany) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "所属集团已禁用,此账号不能登录！"), response);
				return;
			}
		} else if (4==crusrJob || 5==crusrJob || 6==crusrJob || 7==crusrJob || 8==crusrJob) {//4渠道5策划6营销总7项目总8财务
			String crusrProject = coreUser.getCrusrProject();
			CoreProject coreProject = new CoreProject();
			coreProject.setCrproUuid(crusrProject);
			coreProject = coreProjectService.getCoreProject(coreProject);
			if(null == coreProject) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "所属项目已禁用,此账号不能登录！"), response);
				return;
			}
		}
		
		CoreUserVO vo = new CoreUserVO();
		vo.convertPOToVO(coreUser);
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "登录成功", vo), response);
		logger.info("[CoreUserController.login]:end login.");
	}
	
	/**
	 * 修改密码
	 * 
	 * @param crusrUuid 用户标识
	 * @param oldPwd	旧密码
	 * @param newPwd	新密码
	 * @param confirmPwd	确认密码
	 * @param response
	 */
	@RequestMapping(value = "/update/pwd", method = RequestMethod.POST)
	public void updatePwd(String crusrUuid, String oldPwd, String newPwd, String confirmPwd, HttpServletResponse response) {
		logger.info("[CoreUserController.updatePwd]:begin updatePwd.");
		if (StringUtils.isBlank(crusrUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请输入uuid！"), response);
			return;
		}		
		if (StringUtils.isBlank(oldPwd)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请输入旧密码！"), response);
			return;
		}		
		if (StringUtils.isBlank(newPwd)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请输入新密码！"), response);
			return;
		}		
		if (StringUtils.isBlank(confirmPwd)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请输入确认密码！"), response);
			return;
		}		
		if (!newPwd.equals(confirmPwd)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "密码输入不一致！"), response);
			return;
		}		
		CoreUser coreUser = new CoreUser();
		coreUser.setCrusrUuid(crusrUuid);
		coreUser = coreUserService.getCoreUser(coreUser);
		if(coreUser == null || !oldPwd.equals(coreUser.getCrusrPassword())) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "密码错误！"), response);
			return;
		}
		coreUser.setCrusrPassword(newPwd);
		coreUser.setCrusrUdate(new Date());
		coreUserService.updateCoreUser(coreUser);
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "修改密码成功！"), response);
		logger.info("[CoreUserController.updatePwd]:end updatePwd.");
	}
	
	/**
	* 添加(不能添加超级管理员)
	* 
	* @param crusrName 登录名
	* @param crusrCode 昵称姓名
	* @param crusrPassword 登录密码(MD5)
	* @param confirmPwd	确认密码(MD5)
	* @param crusrJob 职位:2集团管理员3投资客4渠道5策划6营销总7项目总8财务
	* @param crusrCompany 所属集团(集团管理员和项目人员)
	* @param crusrProject 所属项目(项目人员)
	* @param crusrCopper 铜宝宝(除超级管理员外)
	* @param crusrSilver 银宝宝(除超级管理员外)
	* @param crusrGold 金宝宝(除超级管理员外)
	* @param crusrYestIncome 昨日收益
	* @param crusrEmail 电子邮件
	* @param crusrMobile 手机号码
	* @param crusrBirthday 生日
	* @param crusrGender 性别:1男2女3其它
	* @param crusrQq QQ
	* @param crusrAddress 地址
	* @param crusrHead 头像路径
	* @param crusrRemarks 备注
	* @return
	*/
	@RequestMapping(value="/add/coreUser", method=RequestMethod.POST)
	public void addCoreUser (String crusrName, String crusrCode, String crusrPassword, String confirmPwd, Integer crusrJob, String crusrCompany, String crusrProject, Double crusrCopper, Double crusrSilver, Double crusrGold, Double crusrYestIncome, String crusrEmail, String crusrMobile, String crusrBirthday, Integer crusrGender, String crusrQq, String crusrAddress, String crusrHead, String crusrRemarks, String crcpaUser, HttpServletResponse response) {
		logger.info("[CoreUserController]:begin addCoreUser");

		if (StringUtil.isEmpty(crusrName)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[登录名]不能为空!"), response);
			return;
		}
		if (null == crusrJob || crusrJob > 8 || crusrJob < 2) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请输入职位！"), response);
			return;
		}
		if (StringUtil.isEmpty(crusrPassword)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[帐户密码]不能为空!"), response);
			return;
		}
		if (StringUtils.isBlank(confirmPwd)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请输入确认密码！"), response);
			return;
		}		
		if (!crusrPassword.equals(confirmPwd)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "密码输入不一致！"), response);
			return;
		}
		if (!StringUtil.isEmpty(crusrMobile)) {
			Pattern p = Pattern.compile(PHONE_PATTERN);  
	        Matcher m = p.matcher(crusrMobile);  
			if(!m.matches()){
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "手机号格式不正确！"), response);
				return;
			} 
		}
		if (null == crusrCopper) {
			crusrCopper = 0.0;
		}
		if (crusrCopper > 10000) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "铜宝宝金额最多一万,其余请分配入银宝宝！"), response);
			return;
		}

		CoreUser coreUser = new CoreUser();
		coreUser.setCrusrName(crusrName);
		if(coreUserService.getCoreUserByName(coreUser) != null) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "该账户名称已存在，请重新输入！"), response);
			return;
		}		
		if (2 == crusrJob) {
			if (StringUtils.isBlank(crusrCompany)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "集团管理员必传所属集团！"), response);
				return;
			}
		}
		if (2 != crusrJob && 3 != crusrJob) {
			if (StringUtils.isBlank(crusrCompany)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "项目人员必传所属集团！"), response);
				return;
			}
			if (StringUtils.isBlank(crusrProject)) {
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "项目人员必传所属项目！"), response);
				return;
			}
		}
		
		String uuid = RandomUtil.generateString(16);
		coreUser.setCrusrUuid(uuid);
		coreUser.setCrusrCode(crusrCode);
		coreUser.setCrusrPassword(crusrPassword);
		coreUser.setCrusrJob(crusrJob);
		coreUser.setCrusrCompany(crusrCompany);
		coreUser.setCrusrProject(crusrProject);
		coreUser.setCrusrMoney(0.0);
		coreUser.setCrusrCopper(crusrCopper);
		coreUser.setCrusrSilver(crusrSilver);
		coreUser.setCrusrGold(crusrGold);
		coreUser.setCrusrYestIncome(crusrYestIncome);
		coreUser.setCrusrTotalIncome(crusrYestIncome); //总收益暂时不使用,默认就是昨日收益
		coreUser.setCrusrEmail(crusrEmail);
		coreUser.setCrusrMobile(crusrMobile);
		coreUser.setCrusrStatus(1);
		coreUser.setCrusrCdate(new Date());
		coreUser.setCrusrUdate(new Date());
		coreUser.setCrusrBirthday(crusrBirthday);
		coreUser.setCrusrGender(crusrGender);
		coreUser.setCrusrQq(crusrQq);
		coreUser.setCrusrAddress(crusrAddress);
		coreUser.setCrusrHead(crusrHead);
		coreUser.setCrusrRemarks(crusrRemarks);

		coreUserService.insertCoreUser(coreUser);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "新增成功!"),response);
		logger.info("[CoreUserController]:end addCoreUser");

	}

	/**
	* 修改基本信息
	* 修改时是否可为空只能界面判断了,单个更新，非空判断交给界面
	* @param crusrUuid 标识UUID
	* @param crusrCode 昵称姓名
	* @param crusrEmail 电子邮件
	* @param crusrMobile 手机号码
	* @param crusrBirthday 生日
	* @param crusrGender 性别:1男2女3其它
	* @param crusrQq QQ
	* @param crusrAddress 地址
	* @param crusrHead 头像路径
	* @param crusrRemarks 备注
	* @return
	*/
	@RequestMapping(value="/update/coreUser", method=RequestMethod.POST)
	public void updateCoreUser (String crusrUuid, String crusrCode, String crusrEmail, String crusrMobile, String crusrBirthday, Integer crusrGender, String crusrQq, String crusrAddress, String crusrHead, String crusrRemarks, HttpServletResponse response) {
		logger.info("[CoreUserController]:begin updateCoreUser");

		if (StringUtil.isEmpty(crusrUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}
		if (!StringUtil.isEmpty(crusrMobile)) {
			Pattern p = Pattern.compile(PHONE_PATTERN);  
	        Matcher m = p.matcher(crusrMobile);  
			if(!m.matches()){
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "手机号格式不正确！"), response);
				return;
			} 
		}
		
		CoreUser coreUser = new CoreUser();
		coreUser.setCrusrUuid(crusrUuid);
		coreUser.setCrusrCode(crusrCode);
		coreUser.setCrusrEmail(crusrEmail);
		coreUser.setCrusrMobile(crusrMobile);
		coreUser.setCrusrUdate(new Date());
		coreUser.setCrusrBirthday(crusrBirthday);
		coreUser.setCrusrGender(crusrGender);
		coreUser.setCrusrQq(crusrQq);
		coreUser.setCrusrAddress(crusrAddress);
		coreUser.setCrusrHead(crusrHead);
		coreUser.setCrusrRemarks(crusrRemarks);
		coreUserService.updateCoreUser(coreUser);
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "修改成功!"),response);
		logger.info("[CoreUserController]:end updateCoreUser");

	}

	/**
	* 修改资金
	* 
	* @param crusrUuid 标识UUID
	* @param crusrCopper 铜宝宝(除超级管理员外)
	* @param crusrSilver 银宝宝(除超级管理员外)
	* @param crusrGold 金宝宝(除超级管理员外)
	* @param crusrYestIncome 昨日收益
	* @return
	*/
	@RequestMapping(value="/update/coreUser/fee", method=RequestMethod.POST)
	public void updateCoreUserFee (String crusrUuid, Double crusrCopper, Double crusrSilver, Double crusrGold, Double crusrYestIncome, HttpServletResponse response) {
		logger.info("[CoreUserController]:begin updateCoreUserFee");

		if (StringUtil.isEmpty(crusrUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}
		if (null != crusrCopper && crusrCopper > 10000) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "铜宝宝金额最多一万,其余请分配入银宝宝！"), response);
			return;
		}
		CoreUser coreUser = new CoreUser();
		coreUser.setCrusrUuid(crusrUuid);
		coreUser.setCrusrCopper(crusrCopper);
		coreUser.setCrusrSilver(crusrSilver);
		coreUser.setCrusrGold(crusrGold);		
		if (null != crusrYestIncome) {
			coreUser.setCrusrYestIncome(crusrYestIncome);
			coreUser.setCrusrTotalIncome(crusrYestIncome); //总收益暂时不使用,默认就是昨日收益
		}
		
		coreUserService.updateCoreUser(coreUser);
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "修改成功!"),response);
		logger.info("[CoreUserController]:end updateCoreUserFee");

	}
	
	/**
	* 超级管理员资金变动(需要判断是否修改了资金额度,修改了记录日志表,资金更新说明后台拼接)
	*	
	* @param crcpaUser 更新人
	* @param crusrMoney 资金额度
	* @param crcpaDesc 资金更新说明(后台拼接,前台不传"超级管理员：更新前：0,更新后：500")
	* @param crcpaBusi 更新对象
	* @return
	*/
	@RequestMapping(value="/update/userCapital", method=RequestMethod.POST)
	public void updateUserCapital (String crcpaUser, Double crusrMoney, String crcpaBusi, HttpServletResponse response) {
		logger.info("[CoreUserController]:begin updateUserCapital");

		if (StringUtil.isEmpty(crcpaUser)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[更新人UUID]不能为空!"), response);
			return;
		}
		if (StringUtil.isEmpty(crcpaBusi)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[更新对象UUID]不能为空!"), response);
			return;
		}
		if (null == crusrMoney) {
			crusrMoney = 0.0;
		}
		CoreUser user = new CoreUser();
		user.setCrusrUuid(crcpaUser);
		user = coreUserService.getCoreUser(user);
		int crusrJob = user.getCrusrJob();
		if(1 == crusrJob) {//更新人权限判断，只有超级管理员才能修改资金
			CoreUser coreUser = new CoreUser();
			coreUser.setCrusrUuid(crcpaBusi);
			coreUser = coreUserService.getCoreUser(coreUser);
			int busiCrusrJob = coreUser.getCrusrJob();
			if(1 == busiCrusrJob){//判断是否是更新超级管理员
				Double crusrMoneyOld=coreUser.getCrusrMoney();//旧资金
				
				coreUser.setCrusrMoney(crusrMoney);
				coreUser.setCrusrUdate(new Date());
				coreUserService.updateCoreUser(coreUser);
				
				//资金变动记录到资金池日志表
				StringBuffer buffer = new StringBuffer();
				if(crusrMoney.doubleValue() != crusrMoneyOld.doubleValue()) {//资金变动
					buffer.append("超级管理员：更新前："+crusrMoneyOld.doubleValue()+",").append("更新后："+crusrMoney.doubleValue()+";");
					addCoreCapitalLog(crcpaUser, crcpaBusi, buffer.toString(), 3);
				}				
			}
		} else {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "此用户无权修改资金!"), response);
			return;
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "资金修改成功!"),response);
		logger.info("[CoreUserController]:end updateUserCapital");

	}

	/**
	* 禁用操作
	*
	* @param crusrUuid 标识UUID
	* @return
	*/
	@RequestMapping(value="/disable/coreUser", method=RequestMethod.POST)
	public void disableCoreUser (String crusrUuid, HttpServletResponse response) {
		logger.info("[CoreUserController]:begin disableCoreUser");

		if (StringUtil.isEmpty(crusrUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}

		CoreUser coreUser = new CoreUser();
		coreUser.setCrusrUuid(crusrUuid);
		coreUserService.disableCoreUser(coreUser);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "禁用成功!"),response);
		logger.info("[CoreUserController]:end disableCoreUser");

	}
	
	/**
	* 获取单个
	*
	* @param crusrUuid 标识UUID
	* @return
	*/
	@RequestMapping(value="/views", method=RequestMethod.POST)
	public void viewsCoreUser (String crusrUuid, HttpServletResponse response) {
		logger.info("[CoreUserController]:begin viewsCoreUser");

		if (StringUtil.isEmpty(crusrUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[标识UUID]不能为空!"), response);
			return;
		}

		CoreUser coreUser = new CoreUser();
		coreUser.setCrusrUuid(crusrUuid);

		coreUser = coreUserService.getCoreUser(coreUser);

		CoreUserVO coreUserVO = new CoreUserVO();
		coreUserVO.convertPOToVO(coreUser);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "获取单个信息成功", coreUserVO), response);
		logger.info("[CoreUserController]:end viewsCoreUser");

	}

	/**
	* 获取集团下的管理员列表(分页)<Page>
	* 
	* @param pageNum 页数
	* @param pageSize 每页条数
	* @param crusrCompany 集团UUID
	* @return
	*/
	@RequestMapping(value="/find/coreUserForPagesByCompany", method=RequestMethod.POST)
	public void findCoreUserForPagesByCompany (Integer pageNum, Integer pageSize, String crusrCompany, HttpServletResponse response) {
		logger.info("[CoreUserController]:begin findCoreUserForPagesByCompany");
		
		if (StringUtil.isEmpty(crusrCompany)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[集团UUid]不能为空!"), response);
			return;
		}
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		Page<CoreUser> page = coreUserService.findCoreUserForPagesByCompany(pageNum, pageSize, crusrCompany);
		Page<CoreUserVO> pageVO = new Page<CoreUserVO>(page.getPageNumber(), page.getPageSize(), page.getTotalCount());
		List<CoreUserVO> vos = new ArrayList<CoreUserVO>();
		CoreUserVO vo;
		for (CoreUser coreUser : page.getResult()) {
			vo = new CoreUserVO();

			vo.convertPOToVO(coreUser);

			vos.add(vo);
		}
		pageVO.setResult(vos);
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "集团下管理员page列表获取成功!", pageVO),response);
		
		logger.info("[CoreUserController]:end findCoreUserForPagesByCompany");
	}
	
	/**
	* 项目下各职位人员分页列表，根据职位和所在项目查询<Page>
	* 
	* @param pageNum 页数
	* @param pageSize 每页条数
	* @param crusrJob 职位
	* @param crusrProject 项目uuid
	* @return
	*/
	@RequestMapping(value="/find/coreUserForPagesByProject", method=RequestMethod.POST)
	public void findCoreUserForPagesByProject (Integer pageNum, Integer pageSize, Integer crusrJob, String crusrProject, HttpServletResponse response) {
		logger.info("[CoreUserController]:begin findCoreUserForPagesByProject");
		
		if (StringUtil.isEmpty(crusrProject)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[项目UUid]不能为空!"), response);
			return;
		}
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		Page<CoreUser> page = coreUserService.findCoreUserForPagesByProject(pageNum, pageSize, crusrJob, crusrProject);
		Page<CoreUserVO> pageVO = new Page<CoreUserVO>(page.getPageNumber(), page.getPageSize(), page.getTotalCount());
		List<CoreUserVO> vos = new ArrayList<CoreUserVO>();
		CoreUserVO vo;
		for (CoreUser coreUser : page.getResult()) {
			vo = new CoreUserVO();

			vo.convertPOToVO(coreUser);

			vos.add(vo);
		}
		pageVO.setResult(vos);
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "项目下各职位人员page列表获取成功!", pageVO),response);
		
		logger.info("[CoreUserController]:end findCoreUserForPagesByProject");
	}
	
	/**
	* 投资客分页列表<Page>
	* 
	* @param pageNum 页数
	* @param pageSize 每页条数
	* @return
	*/
	@RequestMapping(value="/find/coreUserForPagesByInvestors", method=RequestMethod.POST)
	public void findCoreUserForPagesByInvestors (Integer pageNum, Integer pageSize, HttpServletResponse response) {
		logger.info("[CoreUserController]:begin findCoreUserForPagesByInvestors");
		
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		Page<CoreUser> page = coreUserService.findCoreUserForPagesByInvestors(pageNum, pageSize);
		Page<CoreUserVO> pageVO = new Page<CoreUserVO>(page.getPageNumber(), page.getPageSize(), page.getTotalCount());
		List<CoreUserVO> vos = new ArrayList<CoreUserVO>();
		CoreUserVO vo;
		for (CoreUser coreUser : page.getResult()) {
			vo = new CoreUserVO();

			vo.convertPOToVO(coreUser);

			vos.add(vo);
		}
		pageVO.setResult(vos);
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "投资客page列表获取成功!", pageVO),response);
		
		logger.info("[CoreUserController]:end findCoreUserForPagesByInvestors");
	}
	
	/**
	* 用户列表根据用户名或登录名或手机号模糊查询,超级管理员除外<Page>
	* 
	* @param pageNum 页数
	* @param pageSize 每页条数
	* @param objectStr (用户名或登录名或手机号)
	* @return
	*/
	@RequestMapping(value="/find/coreUserForPagesLikes", method=RequestMethod.POST)
	public void findCoreUserForPagesLikes (Integer pageNum, Integer pageSize, String objectStr, HttpServletResponse response) {
		logger.info("[CoreUserController]:begin findCoreUserForPagesLikes");
		
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		if (StringUtil.isEmpty(objectStr)) {
			objectStr = "";
		}

		Page<CoreUser> page = coreUserService.findCoreUserForPagesLikes(pageNum, pageSize, objectStr);
		Page<CoreUserVO> pageVO = new Page<CoreUserVO>(page.getPageNumber(), page.getPageSize(), page.getTotalCount());
		List<CoreUserVO> vos = new ArrayList<CoreUserVO>();
		CoreUserVO vo;
		for (CoreUser coreUser : page.getResult()) {
			vo = new CoreUserVO();
			vo.convertPOToVO(coreUser);
			vos.add(vo);
		}
		pageVO.setResult(vos);
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "查询用户列表获取成功!", pageVO),response);		
		logger.info("[CoreUserController]:end findCoreUserForPagesLikes");
	}
	
	/**
	* 资金变动添加日志
	*
	* @param crcpaUser 更新人
	* @param crcpaBusi 更新对象
	* @param crcpaDesc 更新说明
	* @param crcpaType 更新类型:1集团2项目3管理员
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