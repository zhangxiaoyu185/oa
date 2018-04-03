package com.xiaoyu.lingdian.tool.qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

public class QRCodeUtil {

	public static void main(String[] args) {
		ByteArrayOutputStream out = QRCode.from("Hello World")
				.to(ImageType.PNG).stream();

		try {
			FileOutputStream fout = new FileOutputStream(new File(
					"E:\\logs\\QR_Code.JPG"));

			fout.write(out.toByteArray());

			fout.flush();
			fout.close();

		} catch (FileNotFoundException e) {
			// Do Logging
		} catch (IOException e) {
			// Do Logging
		}

		// override the image type to be JPG
		QRCode.from("Hello World").to(ImageType.JPG).file();
		QRCode.from("Hello World").to(ImageType.JPG).stream();

		// override image size to be 250x250
		QRCode.from("Hello World").withSize(250, 250).file();
		QRCode.from("Hello World").withSize(250, 250).stream();

		// override size and image type
		QRCode.from("Hello World").to(ImageType.GIF).withSize(250, 250).file();
		QRCode.from("Hello World").to(ImageType.GIF).withSize(250, 250)
				.stream();

		// Website Link (URLs) QR Code in Java
		QRCode.from("http://viralpatel.net").to(ImageType.PNG).stream();
	}

	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 *            二维码图片的内容
	 * @param imgPath
	 *            生成二维码图片完整的路径
	 * @param ccbpath
	 *            二维码图片中间的logo路径
	 */
	public static int createQRCode(String content, String imgPath,
			String ccbPath) {
		try {
			Qrcode qrcodeHandler = new Qrcode();
			qrcodeHandler.setQrcodeErrorCorrect('M');
			qrcodeHandler.setQrcodeEncodeMode('B');
			qrcodeHandler.setQrcodeVersion(7);

			// System.out.println(content);
			byte[] contentBytes = content.getBytes("gb2312");
			BufferedImage bufImg = new BufferedImage(140, 140,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();

			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, 140, 140);

			// 设定图像颜色 > BLACK
			gs.setColor(Color.BLACK);
			// 设置偏移量 不设置可能导致解析出错
			int pixoff = 2;
			// 输出内容 > 二维码
			if (contentBytes.length > 0 && contentBytes.length < 120) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				System.err.println("QRCode content bytes length = "
						+ contentBytes.length + " not in [ 0,120 ]. ");
				return -1;
			}
			Image img = ImageIO.read(new File(ccbPath));
			// 实例化一个Image对象。
			gs.drawImage(img, 55, 55, null);
			gs.dispose();
			bufImg.flush();

			// 实例化一个Image对象。
			gs.drawImage(img, 55, 55, null);
			gs.dispose();
			bufImg.flush();
			// 生成二维码QRCode图片
			File imgFile = new File(imgPath);
			ImageIO.write(bufImg, "png", imgFile);
		} catch (Exception e) {
			e.printStackTrace();
			return -100;
		}
		return 0;
	}

}