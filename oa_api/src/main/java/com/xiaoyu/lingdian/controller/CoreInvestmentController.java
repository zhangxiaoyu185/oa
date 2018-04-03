package com.xiaoyu.lingdian.controller;

import com.xiaoyu.lingdian.tool.RandomUtil;
import com.xiaoyu.lingdian.tool.StringUtil;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import com.xiaoyu.lingdian.tool.out.ResultMessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import com.xiaoyu.lingdian.controller.BaseController;
import com.xiaoyu.lingdian.entity.CoreInvestment;
import com.xiaoyu.lingdian.service.CoreInvestmentService;

@Controller
@RequestMapping(value="/coreInvestment")
public class CoreInvestmentController extends BaseController {

	/**
	* 投资拨款流程表
	*/
	@Autowired
	private CoreInvestmentService coreInvestmentService;
	
	/**
	* 添加
	*
	* @param crsptUser 投资客
	* @param crsptFlow 拨款流程流程编码
	* @param crsptCompany 拨款所属集团
	* @param crsptProject 拨款所属项目
	* @return
	*/
	@RequestMapping(value="/add/coreInvestment", method=RequestMethod.POST)
	public void addCoreInvestment (String crsptUser, String crsptFlow, String crsptCompany, String crsptProject, HttpServletResponse response) {
		logger.info("[CoreInvestmentController]:begin addCoreInvestment");

		if (StringUtil.isEmpty(crsptUser)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[投资客UUID]不能为空!"), response);
			return;
		}
		if (StringUtil.isEmpty(crsptFlow)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[拨款流程UUID]不能为空!"), response);
			return;
		}
		if (StringUtil.isEmpty(crsptCompany)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[拨款所属集团UUID]不能为空!"), response);
			return;
		}
		if (StringUtil.isEmpty(crsptProject)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[拨款所属项目UUID]不能为空!"), response);
			return;
		}
		CoreInvestment coreInvestment = new CoreInvestment();		coreInvestment.setCrsptUser(crsptUser);
		coreInvestment.setCrsptFlow(crsptFlow);
		if(null!=(coreInvestmentService.getCoreInvestment(coreInvestment))){
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "该流程已存在，请重新输入！"), response);
			return;
		}
		String uuid = RandomUtil.generateString(16);
		coreInvestment.setCrsptUuid(uuid);
		coreInvestment.setCrsptCompany(crsptCompany);
		coreInvestment.setCrsptProject(crsptProject);

		coreInvestmentService.insertCoreInvestment(coreInvestment);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "新增成功!"),response);
		logger.info("[CoreInvestmentController]:end addCoreInvestment");

	}

	/**
	* 删除
	*
	* @param crsptUser 投资客UUID
	* @param crsptFlow 拨款流程编码
	* @return
	*/
	@RequestMapping(value="/delete/coreInvestment", method=RequestMethod.POST)
	public void deleteCoreInvestment (String crsptUser, String crsptFlow, HttpServletResponse response) {
		logger.info("[CoreInvestmentController]:begin deleteCoreInvestment");
		if (StringUtil.isEmpty(crsptUser)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[投资客UUID]不能为空!"), response);
			return;
		}
		if (StringUtil.isEmpty(crsptFlow)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "[拨款流程编码]不能为空!"), response);
			return;
		}
		CoreInvestment coreInvestment = new CoreInvestment();
		coreInvestment.setCrsptUser(crsptUser);
		coreInvestment.setCrsptFlow(crsptFlow);
		coreInvestmentService.deleteCoreInvestment(coreInvestment);

		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "投资客拨款解除成功!"),response);
		logger.info("[CoreInvestmentController]:end deleteCoreInvestment");

	}

}