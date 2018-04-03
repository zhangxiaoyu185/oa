package com.xiaoyu.lingdian.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.xiaoyu.lingdian.controller.BaseController;
import com.xiaoyu.lingdian.entity.CoreAttachment;
import com.xiaoyu.lingdian.service.CoreAttachmentService;
import com.xiaoyu.lingdian.tool.FileUtil;
import com.xiaoyu.lingdian.tool.RandomUtil;
import com.xiaoyu.lingdian.tool.init.ConfigIni;
import com.xiaoyu.lingdian.tool.out.ResultMessageBuilder;
import com.xiaoyu.lingdian.vo.CoreAttachmentVO;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(value="/coreAttachment")
public class CoreAttachmentController extends BaseController {

	@Autowired
	private CoreAttachmentService coreAttachmentService;
	
	/**
	 * 缩略图文件夹
	 */
	public static final String THUMB = "thumb/";
	
	/**
	 * 验证图片
	 */
	public static Pattern p = Pattern.compile("(?i).+?\\.(jpg|gif|bmp|png)$");

	/**
	 * base64流上传图片,只支持单文件(流程发起时)
	 * 
	 * @param cratmType 1图片附件2拨款凭证
	 * @param fileName 文件名,带后缀
	 * @param cratmDir 目录,例a/b
	 * @param imgData base64流
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/upload/start/stream", method = RequestMethod.POST)
	public void uploadStartStream(Integer cratmType, String cratmDir, String fileName, String imgData, HttpServletResponse response) {
		logger.info("[CoreAttachmentController.uploadStartStream]:begin uploadStartStream.");		
		String error = "";
		if (null == cratmType || 0 == cratmType || cratmType > 2) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少上传的文件类型！"), response);
			return;
		}
		if (StringUtils.isBlank(fileName)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少带后缀的文件名！"), response);
			return;
		}
		if (StringUtils.isBlank(imgData)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少图像Base64流！"), response);
			return;
		}
		if (StringUtils.isBlank(cratmDir)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少上级目录！"), response);
			return;
		}
		String uuid = RandomUtil.generateString(16);
		BASE64Decoder decoder = new BASE64Decoder();
		OutputStream out = null;
		try {
			int pre = (int) System.currentTimeMillis();
			String[] strList = imgData.split(";");
			if (strList.length == 2) {
				imgData = strList[1].substring(7, strList[1].length());
			}
			byte[] b = decoder.decodeBuffer(imgData); // Base64解码
			for (int i = 0; i < b.length; i++) {
				if (b[i] < 0) { // 调整异常数据
					b[i] += 256;
				}
			}
			String name = delAfterLastPoint(fileName) + RandomUtil.generateString(5) + getAfterLastPoint(fileName); //带随机数的新文件名
			String path = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path")  + cratmDir + "/" + name;			
			File file = new File(path);
			FileUtil.createFile(file);
			out = new FileOutputStream(path); // 对字节数组字符串进行Base64解码
			out.write(b);
			out.flush();
			out.close();
			file = new File(path);
			Matcher m = p.matcher(name);
			if (!m.matches()) {
				error += fileName;
				error += "上传失败：只支持JPG或JPEG、BMP、PNG格式的图片文件";
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, error), response);                   	
		    	logger.info("[CoreAttachmentController.uploadStartStream]:end uploadStartStream.");
		    	return;
			}
			if(file.length() <= 9737184){ //大小超过9M的不上传
				//获得上传图片的宽度
                int width = getPicWidth(file);
                int height = getPicHeight(file);
                if(width == -1 || width == 0){
					error += fileName;
					error += "上传失败：图片损坏，不能上传";
					writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, error), response);                   	
			    	logger.info("[CoreAttachmentController.uploadStartStream]:end uploadStartStream.");
			    	return;
				}
				//缩略图
				byte[] bytes = null;
				ByteOutputStream thumbOut = new ByteOutputStream();
                double times = width / 300.0;
                int thumbHeight = (int)(height / times);
                changeImage(file, 300, thumbHeight, thumbOut);
                bytes = thumbOut.getBytes();
                if(null != bytes && bytes.length >0){
                	String thumbPath = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + cratmDir + "/" + THUMB + name;
                    FileUtil.writeBytes(bytes, thumbPath);
                }
                thumbOut.close();
                // 记录上传该文件后的时间
				int finaltime = (int) System.currentTimeMillis();
				int min = finaltime - pre;
				logger.info(fileName + "耗时 : "+min);
				//插入数据库中
				CoreAttachment coreAttachment = new CoreAttachment();
				coreAttachment.setCratmUuid(uuid);
				coreAttachment.setCratmCdate(new Date());
				coreAttachment.setCratmFileName(name);
				coreAttachment.setCratmStatus(1);
				coreAttachment.setCratmDir(cratmDir);
				coreAttachment.setCratmExtension(getAfterLastPoint(name));
				coreAttachment.setCratmHeight(height);
				coreAttachment.setCratmType(cratmType);
				coreAttachment.setCratmWidth(width);
				coreAttachmentService.insertCoreAttachment(coreAttachment);
			} else {
				error += fileName;
				error += "上传失败：图片大小请保持在9M以内";
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, error), response);                   	
		    	logger.info("[CoreAttachmentController.uploadStartStream]:end uploadStartStream.");
		    	return;
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "上传附件失败！"), response);
			return;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.info(e.getMessage());
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "上传附件失败！"), response);
				return;
			}
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "上传附件成功！", uuid), response);
		logger.info("[CoreAttachmentController.uploadStartStream]:end uploadStartStream.");
	}
    
	/**
	 * base64流上传图片,只支持单文件(流程进行时)
	 * 
	 * @param cratmType 1图片附件2拨款凭证
	 * @param fileName 文件名,带后缀
	 * @param cratmDir 目录,例a/b
	 * @param imgData base64流
	 * @param crflwCode 流程编码
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/upload/process/stream", method = RequestMethod.POST)
	public void uploadProcessStream(Integer cratmType, String cratmDir, String fileName, String imgData, String crflwCode, HttpServletResponse response) {
		logger.info("[CoreAttachmentController.uploadProcessStream]:begin uploadProcessStream.");		
		String error = "";
		if (null == cratmType || 0 == cratmType || cratmType > 2) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少上传的文件类型！"), response);
			return;
		}
		if (StringUtils.isBlank(fileName)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少带后缀的文件名！"), response);
			return;
		}
		if (StringUtils.isBlank(imgData)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少图像Base64流！"), response);
			return;
		}
		if (StringUtils.isBlank(cratmDir)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少上级目录！"), response);
			return;
		}
		if (StringUtils.isBlank(crflwCode)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少流程编码！"), response);
			return;
		}
		String uuid = RandomUtil.generateString(16);
		BASE64Decoder decoder = new BASE64Decoder();
		OutputStream out = null;
		try {
			int pre = (int) System.currentTimeMillis();
			String[] strList = imgData.split(";");
			if (strList.length == 2) {
				imgData = strList[1].substring(7, strList[1].length());
			}
			byte[] b = decoder.decodeBuffer(imgData); // Base64解码
			for (int i = 0; i < b.length; i++) {
				if (b[i] < 0) { // 调整异常数据
					b[i] += 256;
				}
			}
			String name = delAfterLastPoint(fileName) + RandomUtil.generateString(5) + getAfterLastPoint(fileName); //带随机数的新文件名
			String path = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path")  + cratmDir + "/" + name;			
			File file = new File(path);
			FileUtil.createFile(file);
			out = new FileOutputStream(path); // 对字节数组字符串进行Base64解码
			out.write(b);
			out.flush();
			out.close();
			file = new File(path);
			Matcher m = p.matcher(name);
			if (!m.matches()) {
				error += fileName;
				error += "上传失败：只支持JPG或JPEG、BMP、PNG格式的图片文件";
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, error), response);                   	
		    	logger.info("[CoreAttachmentController.uploadProcessStream]:end uploadProcessStream.");
		    	return;
			}
			if(file.length() <= 9737184){ //大小超过9M的不上传
				//获得上传图片的宽度
                int width = getPicWidth(file);
                int height = getPicHeight(file);
                if(width == -1 || width == 0){
					error += fileName;
					error += "上传失败：图片损坏，不能上传";
					writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, error), response);                   	
			    	logger.info("[CoreAttachmentController.uploadProcessStream]:end uploadProcessStream.");
			    	return;
				}
				//缩略图
				byte[] bytes = null;
				ByteOutputStream thumbOut = new ByteOutputStream();
                double times = width / 300.0;
                int thumbHeight = (int)(height / times);
                changeImage(file, 300, thumbHeight, thumbOut);
                bytes = thumbOut.getBytes();
                if(null != bytes && bytes.length >0){
                	String thumbPath = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + cratmDir + "/" + THUMB + name;
                    FileUtil.writeBytes(bytes, thumbPath);
                }
                thumbOut.close();
                // 记录上传该文件后的时间
				int finaltime = (int) System.currentTimeMillis();
				int min = finaltime - pre;
				logger.info(fileName + "耗时 : "+min);
				//插入数据库中
				CoreAttachment coreAttachment = new CoreAttachment();
				coreAttachment.setCratmUuid(uuid);
				coreAttachment.setCratmCdate(new Date());
				coreAttachment.setCratmFileName(name);
				coreAttachment.setCratmStatus(1);
				coreAttachment.setCratmDir(cratmDir);
				coreAttachment.setCratmExtension(getAfterLastPoint(name));
				coreAttachment.setCratmHeight(height);
				coreAttachment.setCratmType(cratmType);
				coreAttachment.setCratmWidth(width);
				coreAttachment.setCratmBusUuid(crflwCode);
				coreAttachmentService.insertCoreAttachment(coreAttachment);
			} else {
				error += fileName;
				error += "上传失败：图片大小请保持在9M以内";
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, error), response);                   	
		    	logger.info("[CoreAttachmentController.uploadProcessStream]:end uploadProcessStream.");
		    	return;
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "上传附件失败！"), response);
			return;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.info(e.getMessage());
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "上传附件失败！"), response);
				return;
			}
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "上传附件成功！", uuid), response);
		logger.info("[CoreAttachmentController.uploadProcessStream]:end uploadProcessStream.");
	}
	
	/**
	 * base64流上传头像,返回头像UUID
	 * 
	 * @param fileName 文件名,带后缀
	 * @param cratmDir 目录,例a/b
	 * @param imgData base64流
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/upload/head/stream", method = RequestMethod.POST)
	public void uploadHeadStream(String cratmDir, String fileName, String imgData, HttpServletResponse response) {
		logger.info("[CoreAttachmentController.uploadHeadStream]:begin uploadHeadStream.");
		String error = "";
		if (StringUtils.isBlank(fileName)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少带后缀的文件名！"), response);
			return;
		}
		if (StringUtils.isBlank(imgData)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少图像Base64流！"), response);
			return;
		}
		if (StringUtils.isBlank(cratmDir)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少上级目录！"), response);
			return;
		}
		String uuid = RandomUtil.generateString(16);
		BASE64Decoder decoder = new BASE64Decoder();
		OutputStream out = null;
		try {
			int pre = (int) System.currentTimeMillis();
			String[] strList = imgData.split(";");
			if (strList.length == 2) {
				imgData = strList[1].substring(7, strList[1].length());
			}
			byte[] b = decoder.decodeBuffer(imgData); // Base64解码
			for (int i = 0; i < b.length; i++) {
				if (b[i] < 0) { // 调整异常数据
					b[i] += 256;
				}
			}
			String name = delAfterLastPoint(fileName) + RandomUtil.generateString(5) + getAfterLastPoint(fileName); // 带随机数的新文件名
			String path = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + cratmDir + "/" + name;
			File file = new File(path);
			FileUtil.createFile(file);
			out = new FileOutputStream(path); // 对字节数组字符串进行Base64解码
			out.write(b);
			out.flush();
			out.close();
			file = new File(path);
			Matcher m = p.matcher(name);
			if (!m.matches()) {
				error += fileName;
				error += "上传失败：只支持JPG或JPEG、BMP、PNG格式的图片文件";
				writeAjaxJSONResponse(
						ResultMessageBuilder.build(false, -1, error), response);
				logger.info("[CoreAttachmentController.uploadHeadStream]:end uploadHeadStream.");
				return;
			}
			if (file.length() <= 9737184) { // 大小超过9M的不上传
				// 获得上传图片的宽度
				int width = getPicWidth(file);
				int height = getPicHeight(file);
				if (width == -1 || width == 0) {
					error += fileName;
					error += "上传失败：图片损坏，不能上传";
					writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, error), response);
					logger.info("[CoreAttachmentController.uploadHeadStream]:end uploadHeadStream.");
					return;
				}
				// 缩略图
				byte[] bytes = null;
				ByteOutputStream thumbOut = new ByteOutputStream();
				double times = width / 300.0;
				int thumbHeight = (int) (height / times);
				changeImage(file, 300, thumbHeight, thumbOut);
				bytes = thumbOut.getBytes();
				if(null != bytes && bytes.length >0){
                	String thumbPath = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + cratmDir + "/" + THUMB + name;
                    FileUtil.writeBytes(bytes, thumbPath);
                }
				thumbOut.close();
				// 记录上传该文件后的时间
				int finaltime = (int) System.currentTimeMillis();
				int min = finaltime - pre;
				logger.info(fileName + "耗时 : " + min);
				CoreAttachment coreAttachment = new CoreAttachment();				
				coreAttachment.setCratmUuid(uuid);
				coreAttachment.setCratmFileName(name);
				coreAttachment.setCratmStatus(1);
				coreAttachment.setCratmDir(cratmDir);
				coreAttachment.setCratmExtension(getAfterLastPoint(name));
				coreAttachment.setCratmHeight(height);
				coreAttachment.setCratmType(4); //头像
				coreAttachment.setCratmWidth(width);
				coreAttachment.setCratmCdate(new Date());
				coreAttachmentService.insertCoreAttachment(coreAttachment);
			} else {
				error += fileName;
				error += "上传失败：图片大小请保持在9M以内";
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, error), response);
				logger.info("[CoreAttachmentController.uploadHeadStream]:end uploadHeadStream.");
				return;
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "上传附件失败！"), response);
			return;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.info(e.getMessage());
				writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "上传附件失败！"), response);
				return;
			}
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "上传附件成功！", uuid), response);
		logger.info("[CoreAttachmentController.uploadHeadStream]:end uploadHeadStream.");
	}

	/**
	 * 根据附件类型和流程编码(业务UUID)查询附件列表
	 * 
	 * @param cratmType 1图片附件2拨款凭证
	 * @param cratmBusUuid
	 * @param response
	 */
	@RequestMapping(value = "/find/attachement", method = RequestMethod.POST)
	public void findAttachement(Integer cratmType, String cratmBusUuid, HttpServletResponse response) {
		logger.info("[CorePostController.findAttachement]:begin findAttachement.");
		if (StringUtils.isBlank(cratmBusUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "请输入业务UUID！"), response);
			return;
		}
		if (null == cratmType || 0 == cratmType || cratmType > 2) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少上传的文件类型！"), response);
			return;
		}
		List<CoreAttachment> list = coreAttachmentService.findCoreAttachmentByCnd(cratmBusUuid, cratmType);
		List<CoreAttachmentVO> voList = new ArrayList<>();
		CoreAttachmentVO vo = new CoreAttachmentVO();
		for (CoreAttachment coreAttachment : list) {
			vo = new CoreAttachmentVO();
			vo.convertPOToVO(coreAttachment);
			voList.add(vo);
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "查询附件列表成功", voList), response);
		logger.info("[CorePostController.findAttachement]:end findAttachement.");
	}
		
	/**
	 * 根据业务ID删除附件
	 * 
	 * @param cratmBusUuid
	 * @param response
	 */
	@RequestMapping(value="/delete/by/busi", method=RequestMethod.POST)
	public void deleteByBusi(String cratmBusUuid, HttpServletResponse response) {
		logger.info("[CoreAttachmentController.deleteByBusi]:begin deleteByBusi");
		if (StringUtils.isBlank(cratmBusUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少业务UUID参数！"), response);
			return;
		}
		List<CoreAttachment> list = coreAttachmentService.findCoreAttachmentByCnd(cratmBusUuid, null);
		//删附件数据
		CoreAttachment coreAttachment = new CoreAttachment();
		coreAttachment.setCratmBusUuid(cratmBusUuid);
		coreAttachmentService.deleteCoreAttachmentByBusi(coreAttachment);
		//删附件文件
		for (CoreAttachment model : list) {
			if (model.getCratmFileName() != null && !("").equals(model.getCratmFileName())) {				
				FileUtil.deleteFile(ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + model.getCratmDir() + "/" + model.getCratmFileName()); //删除服务器上文件
				if (1 == model.getCratmType()) { //图片
					FileUtil.deleteFile(ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + model.getCratmDir() + "/" + THUMB + model.getCratmFileName()); //删除缩略图
				}
			}
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "删除附件成功！"), response);
		logger.info("[CoreAttachmentController.deleteByBusi]:end deleteByBusi");
	}
	
	/**
	 * 根据附件UUID删除附件
	 * 
	 * @param cratmUuid
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public void delete(String cratmUuid, HttpServletResponse response) {
		logger.info("[CoreAttachmentController.delete]:begin delete");
		if (StringUtils.isBlank(cratmUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少附件UUID参数！"), response);
			return;
		}
		CoreAttachment coreAttachment = new CoreAttachment();
		coreAttachment.setCratmUuid(cratmUuid);
		coreAttachment = coreAttachmentService.getCoreAttachment(coreAttachment);
		if (coreAttachment == null) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "该附件不存在！"), response);
			return;
		}
		coreAttachmentService.deleteCoreAttachment(coreAttachment);
		if (coreAttachment.getCratmFileName() != null && !("").equals(coreAttachment.getCratmFileName())) {			
			FileUtil.deleteFile(ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + coreAttachment.getCratmDir() + "/" + coreAttachment.getCratmFileName()); //删除服务器上文件
			if (1 == coreAttachment.getCratmType()) { //图片
				FileUtil.deleteFile(ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + coreAttachment.getCratmDir() + "/" + THUMB + coreAttachment.getCratmFileName()); //删除缩略图
			}
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "删除附件成功！"), response);
		logger.info("[CoreAttachmentController.delete]:end delete");
	}
	
	/**
	 * 根据附件UUID获取原图
	 * 
	 * @param cratmUuid
	 * @return
	 */
	@RequestMapping(value = "/image/get/{cratmUuid}", method = RequestMethod.GET)
	public void getImage(@PathVariable String cratmUuid, HttpServletResponse response) {
		logger.info("[CoreAttachmentController.getImage]:begin getImage");
		byte[] bytes = null;
		CoreAttachment coreAttachment = new CoreAttachment();
		coreAttachment.setCratmUuid(cratmUuid);
		coreAttachment = coreAttachmentService.getCoreAttachment(coreAttachment); // 获取
		if (coreAttachment == null) {
			writePicStream(response, null);
			return;
		}
		String path = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + coreAttachment.getCratmDir() + "/" + coreAttachment.getCratmFileName();
		InputStream in = null;
		try {
			in = new FileInputStream(new File(path));
			bytes = IOUtils.toByteArray(in); // 文件二进制码
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.info(e.getMessage());
				}
			}
		}
		
		writePicStream(response, bytes);
		logger.info("[CoreAttachmentController.getImage]:end getImage");	
	}
	
	/**
	 * 根据附件UUID获取缩略图
	 * 
	 * @param cratmUuid
	 * @return
	 */
	@RequestMapping(value = "/image/get/thumb/{cratmUuid}", method = RequestMethod.GET)
	public void getThumbImage(@PathVariable String cratmUuid, HttpServletResponse response) {
		logger.info("[CoreAttachmentController.getThumbImage]:begin getThumbImage");
		byte[] bytes = null;
		CoreAttachment coreAttachment = new CoreAttachment();
		coreAttachment.setCratmUuid(cratmUuid);
		coreAttachment = coreAttachmentService.getCoreAttachment(coreAttachment); // 获取
		if (coreAttachment == null) {
			writePicStream(response, null);
			return;
		}
		String path = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + coreAttachment.getCratmDir() + "/" + THUMB + coreAttachment.getCratmFileName();
		InputStream in = null;
		try {
			in = new FileInputStream(new File(path));
			bytes = IOUtils.toByteArray(in); // 文件二进制码
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.info(e.getMessage());
				}
			}
		}
		
		writePicStream(response, bytes);
		logger.info("[CoreAttachmentController.getThumbImage]:end getThumbImage");	
	}
	
	/**
	 * 根据附件UUID获取原图
	 * 
	 * @param cratmUuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/image/get/other/{cratmUuid}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getOtherImage(@PathVariable String cratmUuid) {
		logger.info("[CoreAttachmentController.getOtherImage]:begin getOtherImage");
		byte[] bytes = null;
		CoreAttachment coreAttachment = new CoreAttachment();
		coreAttachment.setCratmUuid(cratmUuid);
		coreAttachment = coreAttachmentService.getCoreAttachment(coreAttachment); // 获取
		if (coreAttachment == null) {
			return null;
		}
		String path = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + coreAttachment.getCratmDir() + "/" + coreAttachment.getCratmFileName();
		InputStream in = null;
		try {
			in = new FileInputStream(new File(path));
			bytes = IOUtils.toByteArray(in); // 文件二进制码
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.info(e.getMessage());
				}
			}
		}
		logger.info("[CoreAttachmentController.getOtherImage]:end getOtherImage");
		return bytes;
	}
	
	/**
	 * 根据附件UUID获取缩略图
	 * 
	 * @param cratmUuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/image/get/other/thumb/{cratmUuid}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getOtherThumbImage(@PathVariable String cratmUuid) {
		logger.info("[CoreAttachmentController.getOtherThumbImage]:begin getOtherThumbImage");
		byte[] bytes = null;
		CoreAttachment coreAttachment = new CoreAttachment();
		coreAttachment.setCratmUuid(cratmUuid);
		coreAttachment = coreAttachmentService.getCoreAttachment(coreAttachment); // 获取
		if (coreAttachment == null) {
			return null;
		}
		String path = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + coreAttachment.getCratmDir() + "/" + THUMB + coreAttachment.getCratmFileName();
		InputStream in = null;
		try {
			in = new FileInputStream(new File(path));
			bytes = IOUtils.toByteArray(in); // 文件二进制码
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.info(e.getMessage());
				}
			}
		}
		logger.info("[CoreAttachmentController.getOtherThumbImage]:end getOtherThumbImage");
		return bytes;
	}
	
	/**
	 * 根据附件UUID获取附件base64流
	 * 
	 * @param cratmUuid
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/get/stream", method = RequestMethod.POST)
	public void getStream(String cratmUuid, HttpServletResponse response) {
		logger.info("[CoreAttachmentController.getStream]:begin getStream");
		if (StringUtils.isBlank(cratmUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少附件UUID参数！"), response);
			return;
		}
		byte[] bytes = null;
		CoreAttachment coreAttachment = new CoreAttachment();
		coreAttachment.setCratmUuid(cratmUuid);
		coreAttachment = coreAttachmentService.getCoreAttachment(coreAttachment); // 获取
		if (coreAttachment == null) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "获取附件base64流失败！", ""), response);
			logger.info("[CoreAttachmentController.getStream]:end getStream");
			return;
		}
		String path = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + coreAttachment.getCratmDir() + "/" + coreAttachment.getCratmFileName();
		String strBase64 = "";
		InputStream in = null;
		try {
			in = new FileInputStream(new File(path));
			bytes = IOUtils.toByteArray(in); // 文件二进制码
		    strBase64 = new BASE64Encoder().encode(bytes);      //将字节流数组转换为字符串
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.info(e.getMessage());
				}
			}
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "获取附件base64流成功！", strBase64), response);
		logger.info("[CoreAttachmentController.getStream]:end getStream");
	}

	/**
	 * 根据附件UUID获取图片缩略图base64流
	 * 
	 * @param cratmUuid
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/get/stream/thumb", method = RequestMethod.POST)
	public void getStreamThumb(String cratmUuid, HttpServletResponse response) {
		logger.info("[CoreAttachmentController.getStreamThumb]:begin getStreamThumb");
		if (StringUtils.isBlank(cratmUuid)) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "缺少附件UUID参数！"), response);
			return;
		}
		byte[] bytes = null;
		CoreAttachment coreAttachment = new CoreAttachment();
		coreAttachment.setCratmUuid(cratmUuid);
		coreAttachment = coreAttachmentService.getCoreAttachment(coreAttachment); // 获取
		if (coreAttachment == null) {
			writeAjaxJSONResponse(ResultMessageBuilder.build(false, -1, "获取图片缩略图base64流失败！", ""), response);
			logger.info("[CoreAttachmentController.getStreamThumb]:end getStreamThumb");
			return;
		}
		String path = ConfigIni.getIniStrValue("ATTACHMENT_DIR", "path") + coreAttachment.getCratmDir() + "/" + THUMB + coreAttachment.getCratmFileName();
		String strBase64 = "";
		InputStream in = null;
		try {
			in = new FileInputStream(new File(path));
			bytes = IOUtils.toByteArray(in); // 文件二进制码
		    strBase64 = new BASE64Encoder().encode(bytes);      //将字节流数组转换为字符串
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.info(e.getMessage());
				}
			}
		}
		
		writeAjaxJSONResponse(ResultMessageBuilder.build(true, 1, "获取图片缩略图base64流成功！", strBase64), response);
		logger.info("[CoreAttachmentController.getStreamThumb]:end getStreamThumb");
	}
	
	/**
	 * 获取一个文件名的后缀名（最后一个点后面的字符串）,带上点
	 * 
	 * @param fileName
	 * @return
	 */
	public String getAfterLastPoint(String fileName) {
		int last = fileName.lastIndexOf(".");
		if (last <= 0) {
			return "";
		}
		return fileName.substring(last, fileName.length());
	}
	
	/**
	 * 去除一个文件的后缀名（去除最后一个点后面的字符串）,获取文件名
	 * 
	 * @param fileName
	 * @return
	 */
	public String delAfterLastPoint(String fileName) {
		int last = fileName.lastIndexOf(".");
		if (last <= 0) {
			return "";
		}
		return fileName.substring(0, last);
	}

    /**
     * 重新生成图像
     * 
     * @param file
     * @param width
     * @param height
     * @param out
     */
    private void changeImage(File file, int width, int height, OutputStream out) {
        try {
            Image img = ImageIO.read(file);
            int newWidth; int newHeight;
            if(img.getHeight(null)<height && img.getWidth(null)<width){
                newWidth = img.getWidth(null);
                newHeight = img.getHeight(null);
            } else {
                newWidth = width;
                newHeight =img.getHeight(null)<height? img.getHeight(null):height;
            }
            BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            out.close();
        } catch (Exception e) {
            logger.error("unknown exception",e);
        }
    }
    
	/**
	 * 获得上传图片文件的高度
	 * 
	 * @param file
	 * @return
	 */
    private int getPicHeight(File file) {
        int picHeight = 0;
        try {
            Image img = ImageIO.read(file);
            picHeight=img.getHeight(null);
        } catch (IOException e) {
        	logger.error(e.getMessage(),e);
        }
        return picHeight;
    }

    /**
     * 获得上传图片文件的宽度
     * 
     * @param file
     * @return
     */
    private int getPicWidth(File file) {
        int picWidth=0;
        try {
            Image img = ImageIO.read(file);
            picWidth=img.getWidth(null);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        return picWidth;
    }
    
}