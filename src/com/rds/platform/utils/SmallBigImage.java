/**
 * Copyright (c) YMCN Team
 * All rights reserved.
 *
 * The YMCN Project
 * http://www.ymcn.org
 *
 * email: obullxl@163.com  MSN: obullxl@hotmail.com  QQ: 303630027
 *
 * WebSite: http://www.ymcn.org  http://hi.baidu.com/obullxl
 */
package com.rds.platform.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author oldbulla
 *
 *         email: obullxl@163.com MSN: obullxl@hotmail.com QQ: 303630027
 *
 *         WebSite: http://www.ymcn.org http://hi.baidu.com/obullxl
 */
public final class SmallBigImage {

	public static final void overlapImage(String bigPath, String smallPath) {
		try {
			BufferedImage big = ImageIO.read(new File(bigPath));
			BufferedImage small = ImageIO.read(new File(smallPath));
			Graphics2D g = big.createGraphics();
			int x = 668;
			int y = 70;
			System.out.println("x="+x);
			System.out.println("y="+y);
			g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
			g.dispose();
			ImageIO.write(big, "PNG", new File("d:\\Big1Small.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** the main method */
	public static final void main(String[] args) {
		try {
			// 改变图片 长宽
			File originalImage = new File("d:\\222.jpg");
			resize(originalImage, new File("D:\\1207-1.jpg"), 343, 1f);
			System.out.println("调整图片为正方形");
			// 改变图片形状
			Image src = ImageIO.read(new File("d:\\1207-1.jpg"));
			BufferedImage url = (BufferedImage) src;
			// 处理图片将其压缩成正方形的小图
			BufferedImage convertImage = scaleByPercentage(url, 100, 100);
			// 裁剪成圆形 （传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的）
			convertImage = convertCircular(url);
			// 生成的图片位置
			String imagePath = "D:/Imag.png";
			ImageIO.write(convertImage,
					imagePath.substring(imagePath.lastIndexOf(".") + 1),
					new File(imagePath));

			System.out.println("调整图片为圆形形");
		} catch (Exception e) {
			e.printStackTrace();
		}
		overlapImage("d:\\front2.jpg", "d:\\Imag.png");
		System.out.println("图片叠加");
	}

	/**
	 * public static void main(String[] args) throws IOException { try { //
	 * http://avatar.csdn.net/3/1/7/1_qq_27292113.jpg?1488183229974 // 是头像地址 //
	 * 获取图片的流 // BufferedImage url = // getUrlByBufferedImage(
	 * "http://avatar.csdn.net/3/1/7/1_qq_27292113.jpg?1488183229974");
	 * 
	 * Image src = ImageIO.read(new File( "d:\\1207-1.jpg")); //改变图片 长宽 // File
	 * originalImage = new File("d:\\222.png"); // resize(originalImage, new
	 * File("D:\\1207-1.jpg"),150, 1f);
	 * 
	 * 
	 * BufferedImage url = (BufferedImage) src; // 处理图片将其压缩成正方形的小图 BufferedImage
	 * convertImage = scaleByPercentage(url, 100, 100); // 裁剪成圆形 （传入的图像必须是正方形的
	 * 才会 圆形 如果是长方形的比例则会变成椭圆的） convertImage = convertCircular(url); // 生成的图片位置
	 * String imagePath = "D:/Imag.png"; ImageIO.write(convertImage,
	 * imagePath.substring(imagePath.lastIndexOf(".") + 1), new
	 * File(imagePath)); System.out.println("ok"); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 **/
	/**
	 * 缩小Image，此方法返回源图像按给定宽度、高度限制下缩放后的图像
	 * 
	 * @param inputImage
	 * @param maxWidth
	 *            ：压缩后宽度
	 * @param maxHeight
	 *            ：压缩后高度
	 * @throws java.io.IOException
	 *             return
	 */
	public static BufferedImage scaleByPercentage(BufferedImage inputImage,
			int newWidth, int newHeight) throws Exception {
		// 获取原始图像透明度类型
		int type = inputImage.getColorModel().getTransparency();
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		// 开启抗锯齿
		RenderingHints renderingHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// 使用高质量压缩
		renderingHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		BufferedImage img = new BufferedImage(newWidth, newHeight, type);
		Graphics2D graphics2d = img.createGraphics();
		graphics2d.setRenderingHints(renderingHints);
		graphics2d.drawImage(inputImage, 0, 0, newWidth, newHeight, 0, 0,
				width, height, null);
		graphics2d.dispose();
		return img;
	}

	/**
	 * 通过网络获取图片
	 * 
	 * @param url
	 * @return
	 */
	public static BufferedImage getUrlByBufferedImage(String url) {
		try {
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj
					.openConnection();
			// 连接超时
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(25000);
			// 读取超时 --服务器响应比较慢,增大时间
			conn.setReadTimeout(25000);
			conn.setRequestMethod("GET");
			conn.addRequestProperty("Accept-Language", "zh-cn");
			conn.addRequestProperty("Content-type", "image/jpeg");
			conn.addRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");
			conn.connect();
			BufferedImage bufImg = ImageIO.read(conn.getInputStream());
			conn.disconnect();
			return bufImg;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的
	 * 
	 * @param url
	 *            用户头像地址
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage convertCircular(BufferedImage bi1)
			throws IOException {
		// BufferedImage bi1 = ImageIO.read(new File(url));
		// 这种是黑色底的
		// BufferedImage bi2 = new
		// BufferedImage(bi1.getWidth(),bi1.getHeight(),BufferedImage.TYPE_INT_RGB);

		// 透明底的图片
		BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(),
				bi1.getHeight());
		Graphics2D g2 = bi2.createGraphics();
		g2.setClip(shape);
		// 使用 setRenderingHint 设置抗锯齿
		g2.drawImage(bi1, 0, 0, null);
		// 设置颜色
		g2.setBackground(Color.green);
		g2.dispose();
		return bi2;
	}

	public static void resize(File originalFile, File resizedFile,
			int newWidth, float quality) throws IOException {

		if (quality > 1) {
			throw new IllegalArgumentException(
					"Quality has to be between 0 and 1");
		}

		ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
		Image i = ii.getImage();
		Image resizedImage = null;

		int iWidth = i.getWidth(null);
		int iHeight = i.getHeight(null);

		if (iWidth > iHeight) {
			resizedImage = i.getScaledInstance(newWidth, newWidth,
					Image.SCALE_SMOOTH);
		} else {
			resizedImage = i.getScaledInstance(newWidth, newWidth,
					Image.SCALE_SMOOTH);
		}

		// This code ensures that all the pixels in the image are loaded.
		Image temp = new ImageIcon(resizedImage).getImage();

		// Create the buffered image.
		BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
				temp.getHeight(null), BufferedImage.TYPE_INT_RGB);

		// Copy image to buffered image.
		Graphics g = bufferedImage.createGraphics();

		// Clear background and paint the image.
		g.setColor(Color.white);
		g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
		g.drawImage(temp, 0, 0, null);
		g.dispose();

		// Soften.
		float softenFactor = 0.05f;
		float[] softenArray = { 0, softenFactor, 0, softenFactor,
				1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };
		Kernel kernel = new Kernel(3, 3, softenArray);
		ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		bufferedImage = cOp.filter(bufferedImage, null);

		// Write the jpeg to a file.
		FileOutputStream out = new FileOutputStream(resizedFile);

		// Encodes image as a JPEG data stream
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

		JPEGEncodeParam param = encoder
				.getDefaultJPEGEncodeParam(bufferedImage);

		param.setQuality(quality, true);

		encoder.setJPEGEncodeParam(param);
		encoder.encode(bufferedImage);
	}

}
