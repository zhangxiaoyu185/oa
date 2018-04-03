package com.xiaoyu.lingdian.tool;

import java.awt.Color;  
import java.awt.Font;  
import java.awt.Graphics2D;  
  
import java.awt.RenderingHints;  
import java.awt.image.BufferedImage;  
import java.io.BufferedOutputStream;  
import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileOutputStream;  
  
import javax.imageio.ImageIO;  
  
import java.util.Iterator;  
import javax.imageio.*;  
import javax.imageio.stream.*; 
  
/** 
 * 加载png格式图片，使用不同方式转为png或者jpg格式的性能对比。 
 * */  
public class PngJpgImagePerTest {  
  
    private static String PNG = "png";  
    private static String JPG = "jpg";  
  
    private static String SimpleSavePNG = "__SimpleSavePNG.png";  
    private static String Convert2ArrayAndSavePNG = "__Convert2ArrayAndSavePNG.png";  
    private static String NewImageSavePNG = "__NewPNG.png";  
    private static String NewImageSaveJPG = "__NewJPG.jpg";  
    private static String SaveJPG = "__JPG_quality.jpg";  
  
    /** 
     * 保存文件
     * */  
    public static void save2File(String filePath, byte[] data) {  
  
        BufferedOutputStream bufferOutput = null;  
  
        try {  
            bufferOutput = new BufferedOutputStream(new FileOutputStream(  
                    new File(filePath)), 1024);  
  
            bufferOutput.write(data);  
            bufferOutput.flush();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } finally {  
            if (bufferOutput != null) {  
                try {  
                    bufferOutput.close();  
                } catch (Exception e) {  
                    throw new RuntimeException(e);  
                }  
            }  
        }  
    }  
  
    /** 
     * load image 
     * */  
    public static BufferedImage loadImage(String filePath) {  
        try {  
            return ImageIO.read(new File(filePath));  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * simple save use ImageIO.write 
     */  
    public static void saveImage(BufferedImage image, String format,  
            String filePath) {  
        try {  
            ImageIO.write(image, format, new File(filePath));  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * convert to byte array. 
     * */  
    public static byte[] convert2bytes(BufferedImage image, String format) {  
        try {  
            ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);  
            ImageIO.write(image, format, bout);  
            byte[] data = bout.toByteArray();  
            return data;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * create new buffer image and save using png format. 
     * */  
    public static void savePngImage(BufferedImage image, String format,  
            String filePath) {  
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image  
                .getHeight(), BufferedImage.TYPE_3BYTE_BGR);  
        bufferedImage.getGraphics().drawImage(image, 0, 0, null);  
        saveImage(bufferedImage, PNG, filePath);  
    }  
  
    /** 
     * create new buffer image and save using jpg format. 
     * */  
    public static void saveJpgImage(BufferedImage image, String format,  
            String filePath) {  
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image  
                .getHeight(), BufferedImage.TYPE_3BYTE_BGR);  
        bufferedImage.getGraphics().drawImage(image, 0, 0, null);  
        saveImage(bufferedImage, JPG, filePath);  
    }  
  
    /** 
     * save jpg with specified quality. 为了图片质量，quality的值不能太低。 
     * */  
    public static void saveImageUsingJPGWithQuality(BufferedImage image,  
            String filePath, float quality) throws Exception {  
  
        BufferedImage newBufferedImage = new BufferedImage(image.getWidth(),  
                image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);  
        newBufferedImage.getGraphics().drawImage(image, 0, 0, null);  
  
        Iterator<ImageWriter> iter = ImageIO  
                .getImageWritersByFormatName("jpeg");  
  
        ImageWriter imageWriter = iter.next();  
        ImageWriteParam iwp = imageWriter.getDefaultWriteParam();  
  
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);  
        iwp.setCompressionQuality(quality);  
  
        File file = new File(filePath);  
        FileImageOutputStream fileImageOutput = new FileImageOutputStream(file);  
        imageWriter.setOutput(fileImageOutput);  
        IIOImage iio_image = new IIOImage(newBufferedImage, null, null);  
        imageWriter.write(null, iio_image, iwp);  
        imageWriter.dispose();  
    }  
  
    public static void drawString(Graphics2D g2, String value, int x, int y) {  
        Font font = new Font("Arial", Font.BOLD, 16);  
        g2.setColor(Color.BLACK);  
        g2.setFont(font);  
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,  
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  
        g2.drawString(value, x, y);  
    }  
  
    private String sourceFilePath;  
    private BufferedImage sourceImage;  
    private int loop;  
    private float quality;  
    
    public void setup() {  
        sourceFilePath = "E:\\allencode\\ImgTest\\stat.png";  
        sourceImage = loadImage(sourceFilePath);  
        loop = 10;  
        quality = 0.8F;  
        Graphics2D g2 = sourceImage.createGraphics();  
        draw(g2);  
    }  
  
    private void draw(Graphics2D g2) {  
        drawString(g2, "Allen Hello world.", 500, 200);  
    }  
  
    /** 
     * ImageIO.write存储图片时主要的性能消耗在计算上。 
     * */    
    public void test_01() {  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < loop; i++) {  
            long start = System.currentTimeMillis();  
            saveImage(sourceImage, PNG, sourceFilePath + SimpleSavePNG);  
            long end = System.currentTimeMillis();  
            sb.append(" saveImage = " + (end - start));  
  
            long convertStart = System.currentTimeMillis();  
            byte[] pngData = convert2bytes(sourceImage, PNG);  
            long convertEnd = System.currentTimeMillis();  
            sb.append(" convert = " + (convertEnd - convertStart));  
  
            long saveStart = System.currentTimeMillis();  
            save2File(sourceFilePath + Convert2ArrayAndSavePNG, pngData);  
            long saveEnd = System.currentTimeMillis();  
            sb.append(" save = " + (saveEnd - saveStart));  
  
            sb.append("\n");  
        }  
  
        System.out.println(sb);  
    }  
  
    /** 
     * ImageIO.write和新建一个BufferedImage，采用jpg或png保存的比较。 
     * */    
    public void test_02() {  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < loop; i++) {  
            long start = System.currentTimeMillis();  
            saveImage(sourceImage, PNG, sourceFilePath + SimpleSavePNG);  
            long end = System.currentTimeMillis();  
            sb.append(" saveImage = " + (end - start));  
  
            start = System.currentTimeMillis();  
            savePngImage(sourceImage, "png", sourceFilePath + NewImageSavePNG);  
            end = System.currentTimeMillis();  
            sb.append(" savePngImage = " + (end - start));  
  
            start = System.currentTimeMillis();  
            saveJpgImage(sourceImage, "jpg", sourceFilePath + NewImageSaveJPG);  
            end = System.currentTimeMillis();  
            sb.append(" saveJpgImage = " + (end - start));  
  
            sb.append("\n");  
        }  
  
        System.out.println(sb);  
    }  
  
    /** 
     * ImageIO.write存储图片和保存jpg的时间对比。 
     * */    
    public void test_03() throws Exception {  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < loop; i++) {  
            long pngStart = System.currentTimeMillis();  
            saveImage(sourceImage, PNG, sourceFilePath + SimpleSavePNG);  
            long pngEnd = System.currentTimeMillis();  
            sb.append(" saveImage = " + (pngEnd - pngStart));  
  
            long jpgStart = System.currentTimeMillis();  
            saveImageUsingJPGWithQuality(sourceImage, sourceFilePath + SaveJPG,  
                    quality);  
            long jpgEnd = System.currentTimeMillis();  
            sb.append(" JPGWithQuality = " + (jpgEnd - jpgStart));  
  
            sb.append(" % = " + (pngEnd - pngStart) * 1D / (jpgEnd - jpgStart));  
            sb.append("\n");  
        }  
  
        System.out.println(sb);  
    }  
}