package com.rds.code.image;

import java.awt.image.BufferedImage;

/**
 * @desciption 图片旋转工具
 * @author fushaoming 2015年5月19日
 *
 */
public class ImgUtil {

	/**
	 * 逆时针旋转90度
	 * 
	 * @param bi
	 * @return
	 */
	public static BufferedImage rotate90DX(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();

		BufferedImage bufferedImage = new BufferedImage(height, width,
				bi.getType());

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				bufferedImage.setRGB(height - 1 - j, i, bi.getRGB(i, j));

		return bufferedImage;
	}

	/**
	 * 顺时针旋转90度
	 * 
	 * @param bi
	 * @return
	 */
	public static BufferedImage rotate90SX(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();

		BufferedImage bufferedImage = new BufferedImage(height, width,
				bi.getType());

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				bufferedImage.setRGB(j, width - i - 1, bi.getRGB(i, j));
		return bufferedImage;
	}
}