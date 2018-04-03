package com.xiaoyu.lingdian.tool;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoyu.lingdian.tool.init.ConfigIni;

/**
 * 电子发票生成
 *
 */
public class InvoicePicUtil {

	protected final Logger logger = LoggerFactory.getLogger("BASE_LOGGER");
	
	public static void main(String[] args) throws IOException {
		int pre = (int) System.currentTimeMillis();
		String bgImgPath = "F:\\code\\src\\main\\resources\\basic\\dzfp.png";
		new InvoicePicUtil().outPic(bgImgPath, "2016110311121326", "XXXXXXXXXXXXXXXX", "6222228546975216529", "XXXXXXXXXXXX", 
				"XXXXXXXXXXXXXXXX", "6222228546975216528", "XXXXXXXXXXXX", "XXXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXXXXXXXXXXXXX", 
				"XXXXXXXXXX", 9999999.99, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", DateUtil.formatDateByFormat(new Date(), DateUtil.CHINESE_PATTEN), "XXXXXX");
		// 记录上传该文件后的时间
		int finaltime = (int) System.currentTimeMillis();
		int min = finaltime - pre;
		System.out.println("耗时 : " + min);
	}

	Font f = null;
	Graphics2D g = null;

	public boolean outPic(String bgImgPath, String creicCode, String creicPayee, String creicPayeeAccount, String creicPayeeBank, 
			String creicPayer, String creicPayerAccount, String creicPayerBank, String creicProjectName, String creicMatter, 
			String creicInvoiceTypeName, double creicLowAmount, String creicRemarks, String creicUdate, String creicDrawer) {		
		String afterFormat = getAfterLastPoint(bgImgPath).replace(".", "").toUpperCase();
		String fileName = creicCode + ".png";
		
		try {
			Image bgImgSrc = ImageIO.read(new File(bgImgPath));
			BufferedImage buffImg = new BufferedImage(bgImgSrc.getWidth(null), bgImgSrc.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
			g = buffImg.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(bgImgSrc, 0, 0, null);

			//数字
			f = new Font("宋体", Font.PLAIN, 13);// 字体，样式,和字号
			g.setFont(f);
			g.setColor(new Color(102,102,102));
			String creicCapAmount = ConvertNumUtil.NumToChinese(creicLowAmount);
			g.drawString(creicCapAmount, 150, 305);
			g.drawString(String.valueOf(creicLowAmount), 440, 305);
			g.drawString(creicPayeeAccount, 440, 145);
			g.drawString(creicPayerAccount, 440, 209);
			g.drawString(creicCode, 490, 100);
			
			//文字
			f = new Font("微软雅黑", Font.PLAIN, 15);// 字体，样式,和字号
			g.setFont(f);
			g.drawString(creicPayee, 150, 145);			
			g.drawString(creicPayeeBank, 165, 177);
			g.drawString(creicPayer, 150, 209);
			g.drawString(creicPayerBank, 165, 241);
			g.drawString(creicMatter, 130, 273);
			g.drawString(creicProjectName, 150, 338);
			g.drawString(creicRemarks, 130, 372);
			g.drawString(creicDrawer, 530, 440);
			g.drawString(creicInvoiceTypeName, 305, 100);
			g.drawString(creicUdate, 130, 100);

			g.dispose();
			
			String dirPath = ConfigIni.getIniStrValue("INVOICE_DIR", "path", "F:\\code\\src\\main\\webapp\\views\\invoice\\");
			ImageIO.write(buffImg, afterFormat, new File(dirPath + fileName));
			String mediaDirPath = ConfigIni.getIniStrValue("INVOICE_DIR", "mediaPath", "C:\\attachment\\oa\\invoice\\");
			ImageIO.write(buffImg, afterFormat, new File(mediaDirPath + fileName));
            logger.info("生成" + creicCode + "图片成功");
		} catch (Exception e) {
			logger.info("生成" + creicCode + "图片失败");
			return false;
		}

		return true;
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

}
