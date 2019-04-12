package com.rds.children.service.Impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import lombok.Setter;

import org.activiti.engine.TaskService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.rds.activiti.mapper.RdsActivitiJudicialMapper;
import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.children.mapper.RdsChildrenPrintMapper;
import com.rds.children.mapper.RdsChildrenRegisterMapper;
import com.rds.children.mapper.RdsChildrenResultMapper;
import com.rds.children.mapper.RdsChildrenSampleReceiveMapper;
import com.rds.children.model.RdsChildrenCaseInfoModel;
import com.rds.children.model.RdsChildrenCasePhotoModel;
import com.rds.children.model.RdsChildrenPrintCaseModel;
import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.service.RdsChildrenPrintService;
import com.rds.code.utils.DownLoadUtils;
import com.rds.code.utils.FileUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.platform.utils.FontText;
import com.rds.upc.model.RdsUpcUserModel;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@Service
public class RdsChildrenPrintServiceImpl implements RdsChildrenPrintService {
	// 配置文件地址
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";
	// 附件存放地址
	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "children_head_photo");

	// 背景图片
	private static final String BACK_ATTACHMENTPATH = PropertiesUtils
			.readValue(FILE_PATH, "children_background_photo");
	@Setter
	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;
	@Autowired
	private RdsChildrenResultMapper RdsChildrenResultMapper;
	@Autowired
	private RdsChildrenRegisterMapper rdsChildrenRegisterMapper;
	@Autowired
	private RdsChildrenSampleReceiveMapper rdsChildrenSampleReceiveMapper;
	@Autowired
	private TaskService taskService;

	@Autowired
	private RdsActivitiJudicialMapper rdsActivitiJudicialMapper;
	@Autowired
	private RdsChildrenPrintMapper RdsChildrenPrintMapper;

	@Override
	public RdsChildrenResponse getCaseInfo(Map<String, Object> params) {
		List<RdsChildrenPrintCaseModel> caseModels = RdsChildrenPrintMapper
				.getCaseInfo(params);
		int count = RdsChildrenPrintMapper.countCaseInfo(params);
		return new RdsChildrenResponse(count, caseModels);
	}

	@Override
	public RdsChildrenPrintCaseModel printCaseInfo(String case_id) {
		return RdsChildrenPrintMapper.printCaseInfo(case_id);
	}

	@Override
	public List<Map<String, String>> printCaseResult(String case_id) {
		return RdsChildrenPrintMapper.printCaseResult(case_id);
	}

	@Override
	public String getCasePhoto(String case_id) {
		Map<String, Object> temp = new HashMap<>();
		temp.put("case_id", case_id);
		temp.put("photo_type", 1);
		return RdsChildrenPrintMapper.getCasePhoto(temp);
	}

	@Override
	public void getImg(HttpServletResponse response, String filename) {
		if (StringUtils.isNotEmpty(filename)) {
			if (FileUtils.getState(ATTACHMENTPATH + filename)) {
				DownLoadUtils.download(response, ATTACHMENTPATH + filename);
			}
		}
	}

	@Override
	public boolean changePrintState(Map<String, Object> params) {
		String case_id = params.get("case_id").toString();
		int result = RdsChildrenPrintMapper.changePrintState(params);
		RdsChildrenCaseInfoModel caseInfo = RdsChildrenResultMapper
				.getCaseInfoModel(case_id);
		// 判断状态在邮寄前才走流程
		if (caseInfo.getVerify_state() < 7) {
			Map<String, Object> variables = new HashMap<>();
			String taskId = rdsActivitiJudicialMapper.getChildCaseTask(case_id);
			if (taskId == null) {
				return false;
			}
			// 流程加入
			rdsActivitiJudicialService.runByChildCaseCode(case_id, variables,
					(RdsUpcUserModel) params.get("user"));
			Map<String, Object> verify_state = new HashMap<>();
			verify_state.put("case_code", case_id);
			verify_state.put("verify_state", 7);
			// 更新基本信息
			rdsChildrenSampleReceiveMapper.updateCaseState(verify_state);
		}
		return result < 0 ? false : true;
	}

	@Override
	public Map<String, Object> childrenCardCreate(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		String case_id = params.get("case_id").toString();
		String case_code = params.get("case_code").toString();
		Map<String, Object> temp = new HashMap<>();
		temp.put("case_id", case_id);
		temp.put("photo_type", 2);
		String photo = RdsChildrenPrintMapper.getCasePhoto(temp);
		// 案例基本信息
		RdsChildrenPrintCaseModel caseinfo = RdsChildrenPrintMapper
				.printCaseInfo(case_id);
		// 案例位点信息
		List<Map<String, String>> results = RdsChildrenPrintMapper
				.printCaseResult(case_id);
		if (results.size() == 0) {
			result.put("success", false);
			result.put("msg", "该案例没有位点信息，请查看！");
			return result;
		}
		File originalImage = new File(ATTACHMENTPATH + photo);
		// 创建目录
		File file = new File(ATTACHMENTPATH + case_code);
		// 判断目标文件所在的目录是否存在
		if (!file.exists()) {
			// 如果目标文件所在的目录不存在，则创建父目录
			System.out.println("目标文件所在目录不存在，准备创建它！");
			if (!file.mkdirs()) {
				System.out.println("创建目标文件所在目录失败！");
				result.put("success", false);
				result.put("msg", "创建文件夹失败，请联系管理员!");
				return result;
			}
		}
		// 如果已存在清空该文件夹里面的图片
		else {
			String[] tempList = file.list();
			File temp1 = null;
			for (int i = 0; i < tempList.length; i++) {
				temp1 = new File(ATTACHMENTPATH + case_code + File.separator
						+ tempList[i]);
				if (temp1.isFile()) {
					temp1.delete();
				}
			}
		}
		try {
			System.out.println("照片背面生成start");
			String filePathBack = "back.jpg";
			String outPathBack = case_code + File.separatorChar + case_code
					+ "back.jpg";
			// 位点信息图生成
			drawTextInImgBack(BACK_ATTACHMENTPATH + filePathBack,
					ATTACHMENTPATH + outPathBack, new FontText(7, "#32495B",
							49, "微软雅黑"), results, case_code);

			RdsChildrenCasePhotoModel pmodel1 = new RdsChildrenCasePhotoModel();
			pmodel1.setCase_id(case_id);
			pmodel1.setPhoto_type("4");
			// 删除以生成的图片记录
			rdsChildrenRegisterMapper.deleteCasePhoto(pmodel1);
			pmodel1.setPhoto_id(UUIDUtil.getUUID());
			pmodel1.setPhoto_path(outPathBack);
			pmodel1.setUpload_time(new SimpleDateFormat("yyyy-MM-dd HH:MM:SS")
					.format(new Date()));
			// 插入背面生成的图片记录
			rdsChildrenRegisterMapper.insertHeadPhoto(pmodel1);
			System.out.println("照片背面生成end");

			System.out.println("调整图片为正方形start");
			resize(originalImage, new File(ATTACHMENTPATH + case_code
					+ File.separatorChar + "head.jpg"), 343, 1f);
			System.out.println("调整图片为正方形end,输出目录：" + ATTACHMENTPATH + case_code
					+ File.separatorChar + "head.jpg");

			System.out.println("调整图片为圆形start");
			// 改变图片形状
			Image src = ImageIO.read(new File(ATTACHMENTPATH + case_code
					+ File.separatorChar + "head.jpg"));
			BufferedImage url = (BufferedImage) src;
			// 处理图片将其压缩成正方形的小图
			BufferedImage convertImage = scaleByPercentage(url, 100, 100);
			// 裁剪成圆形 （传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的）
			convertImage = convertCircular(url);
			// 生成的图片位置
			String imagePath = ATTACHMENTPATH + case_code + File.separatorChar
					+ "frontHead.jpg";
			ImageIO.write(convertImage,
					imagePath.substring(imagePath.lastIndexOf(".") + 1),
					new File(imagePath));

			System.out.println("调整图片为圆形end；输出目录：" + ATTACHMENTPATH + case_code
					+ File.separatorChar + "frontHead.jpg");

			System.out.println("头像叠加背景图start");

			System.out.println("给图片添加文字start");
			String filePath = case_code + File.separatorChar + "frontHead.jpg";
			String outPath = case_code + File.separatorChar + case_code
					+ "front.jpg";

			// 判断儿童身份证不为空时用横线的背景图
			if (StringUtils.isNotEmpty(caseinfo.getId_number())) {
				// 图片叠加
				overlapImage(BACK_ATTACHMENTPATH + "front1.jpg", ATTACHMENTPATH
						+ case_code + File.separatorChar + "frontHead.jpg",
						ATTACHMENTPATH + filePath);
				System.out.println("头像叠加背景图end;输出目录：" + ATTACHMENTPATH
						+ case_code + File.separatorChar + "frontHead.jpg");
				// 添加文字
				drawTextInImgFront1(ATTACHMENTPATH + filePath, ATTACHMENTPATH
						+ outPath, new FontText(7, "#32495B", 49, "微软雅黑"),
						caseinfo);
			} else {
				// 图片叠加
				overlapImage(BACK_ATTACHMENTPATH + "front2.jpg", ATTACHMENTPATH
						+ case_code + File.separatorChar + "frontHead.jpg",
						ATTACHMENTPATH + filePath);
				System.out.println("头像叠加背景图end;输出目录：" + ATTACHMENTPATH
						+ case_code + File.separatorChar + "frontHead.jpg");

				drawTextInImgFront2(ATTACHMENTPATH + filePath, ATTACHMENTPATH
						+ outPath, new FontText(7, "#32495B", 49, "微软雅黑"),
						caseinfo);
			}

			System.out.println("给图片添加文字end");
			// 数据库添加正面照片记录
			RdsChildrenCasePhotoModel pmodel = new RdsChildrenCasePhotoModel();
			pmodel.setCase_id(case_id);
			pmodel.setPhoto_type("3");
			// 删除以生成的图片
			rdsChildrenRegisterMapper.deleteCasePhoto(pmodel);
			pmodel.setPhoto_id(UUIDUtil.getUUID());
			pmodel.setPhoto_path(outPath);
			pmodel.setUpload_time(new SimpleDateFormat("yyyy-MM-dd HH:MM:SS")
					.format(new Date()));
			if (rdsChildrenRegisterMapper.insertHeadPhoto(pmodel) > 0) {
				result.put("success", true);
				result.put("msg", "生成图片成功！");
			} else {
				result.put("success", false);
				result.put("msg", "生成图片异常，请联系管理员!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "生成图片异常<br>请查看点位录入格式是否正确，或联系管理员!");
			return result;
		}

		RdsChildrenCaseInfoModel caseInfo = RdsChildrenResultMapper
				.getCaseInfoModel(case_id);
		// 判断状态在邮寄前才走流程
		if (caseInfo.getVerify_state() < 7) {
			Map<String, Object> variables = new HashMap<>();
			String taskId = rdsActivitiJudicialMapper
					.getChildCaseTask(case_id);
			if (taskId == null) {
				result.put("success", false);
				result.put("msg", "案例流程有误，请联系管理员!");
				return result;
			}
			// 流程加入
			rdsActivitiJudicialService.runByChildCaseCode(case_id,
					variables, (RdsUpcUserModel) params.get("user"));
			Map<String, Object> verify_state = new HashMap<>();
			verify_state.put("case_code", case_id);
			verify_state.put("verify_state", 7);
			// 更新基本信息
			rdsChildrenSampleReceiveMapper.updateCaseState(verify_state);
		}
	
		//设置打印时间
//		if (RdsChildrenPrintMapper.changePrintState(params) > 0) {}

		return result;
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

	public static final void overlapImage(String bigPath, String smallPath,
			String filePath) {
		try {
			BufferedImage big = ImageIO.read(new File(bigPath));
			BufferedImage small = ImageIO.read(new File(smallPath));
			Graphics2D g = big.createGraphics();
			int x = 668;
			int y = 70;
			System.out.println("x=" + x);
			System.out.println("y=" + y);
			g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
			g.dispose();
			ImageIO.write(big, "PNG", new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 给图片添加文字，该情况是小孩身份证存在的情况，背景图存在横线
	public static void drawTextInImgFront1(String filePath, String outPath,
			FontText text, RdsChildrenPrintCaseModel caseinfo) {
		ImageIcon imgIcon = new ImageIcon(filePath);
		Image img = imgIcon.getImage();
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		BufferedImage bimage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g = bimage.createGraphics();
		g.setColor(getColor(text.getWm_text_color()));
		g.setBackground(Color.white);
		g.drawImage(img, 0, 0, null);
		Font font = null;
		font = new Font(text.getWm_text_font(), Font.PLAIN,
				text.getWm_text_size());
		g.setFont(font);
		if (StringUtils.isNotEmpty(caseinfo.getFather_name())
				&& StringUtils.isNotEmpty(caseinfo.getMother_name())) {
			g.drawString("身份证号：" + caseinfo.getId_number(), 480, 605);
			if (caseinfo.getChild_name().length() == 2) {
				g.drawString("姓名：" + caseinfo.getChild_name().substring(0, 1),
						110, 775);
				g.drawString(caseinfo.getChild_name().substring(1, 2), 340, 775);
			} else
				g.drawString("姓名：" + caseinfo.getChild_name(), 110, 775);
			g.drawString("性别：" + caseinfo.getChildsex(), 480, 775);
			g.drawString(
					"父亲：" + caseinfo.getFather_name() + " "
							+ caseinfo.getFather_number(), 760, 775);
			g.drawString("出生：" + caseinfo.getBirth_date().substring(0, 4), 110,
					878);
			g.drawString("年", 385, 878);
			g.drawString(caseinfo.getBirth_date().substring(5, 7), 447, 878);
			g.drawString("月", 512, 878);
			g.drawString(caseinfo.getBirth_date().substring(8, 10), 563, 878);
			g.drawString("日", 623, 878);
			g.drawString(
					"母亲：" + caseinfo.getMother_name() + " "
							+ caseinfo.getMother_number(), 760, 878);
		} else if (!StringUtils.isNotEmpty(caseinfo.getFather_name())
				&& StringUtils.isNotEmpty(caseinfo.getMother_name())) {
			g.drawString("身份证号：" + caseinfo.getId_number(), 480, 605);
			if (caseinfo.getChild_name().length() == 2) {
				g.drawString("姓名：" + caseinfo.getChild_name().substring(0, 1),
						110, 775);
				g.drawString(caseinfo.getChild_name().substring(1, 2), 340, 775);
			} else
				g.drawString("姓名：" + caseinfo.getChild_name(), 110, 775);
			g.drawString("性别：" + caseinfo.getChildsex(), 480, 775);
			g.drawString(
					"母亲：" + caseinfo.getMother_name() + " "
							+ caseinfo.getMother_number(), 760, 775);
			g.drawString("出生：" + caseinfo.getBirth_date().substring(0, 4), 110,
					878);
			g.drawString("年", 385, 878);
			g.drawString(caseinfo.getBirth_date().substring(5, 7), 447, 878);
			g.drawString("月", 512, 878);
			g.drawString(caseinfo.getBirth_date().substring(8, 10), 563, 878);
			g.drawString("日", 623, 878);
		} else if (StringUtils.isNotEmpty(caseinfo.getFather_name())
				&& !StringUtils.isNotEmpty(caseinfo.getMother_name())) {
			g.drawString("身份证号：" + caseinfo.getId_number(), 480, 605);
			if (caseinfo.getChild_name().length() == 2) {
				g.drawString("姓名：" + caseinfo.getChild_name().substring(0, 1),
						110, 775);
				g.drawString(caseinfo.getChild_name().substring(1, 2), 340, 775);
			} else
				g.drawString("姓名：" + caseinfo.getChild_name(), 110, 775);
			g.drawString("性别：" + caseinfo.getChildsex(), 480, 775);
			g.drawString(
					"父亲：" + caseinfo.getFather_name() + " "
							+ caseinfo.getFather_number(), 760, 775);
			g.drawString("出生：" + caseinfo.getBirth_date().substring(0, 4), 110,
					878);
			g.drawString("年", 385, 878);
			g.drawString(caseinfo.getBirth_date().substring(5, 7), 447, 878);
			g.drawString("月", 512, 878);
			g.drawString(caseinfo.getBirth_date().substring(8, 10), 563, 878);
			g.drawString("日", 623, 878);
		}// 父亲信息都为空
		else {
			g.drawString("身份证号：" + caseinfo.getId_number(), 480, 605);
			// g.drawString("姓名：" + caseinfo.getChild_name(), 200, 750);
			if (caseinfo.getChild_name().length() == 2) {
				g.drawString("姓名：" + caseinfo.getChild_name().substring(0, 1),
						200, 750);
				g.drawString(caseinfo.getChild_name().substring(1, 2), 430, 750);
			} else
				g.drawString("姓名：" + caseinfo.getChild_name(), 200, 750);

			g.drawString("性别：" + caseinfo.getChildsex(), 625, 750);
			g.drawString("出生：" + caseinfo.getBirth_date().substring(0, 4), 940,
					750);
			g.drawString("年", 1210, 750);
			g.drawString(caseinfo.getBirth_date().substring(5, 7), 1270, 750);
			g.drawString("月", 1330, 750);
			g.drawString(caseinfo.getBirth_date().substring(8, 10), 1390, 750);
			g.drawString("日", 1450, 750);
		}
		g.dispose();

		try {
			FileOutputStream out = new FileOutputStream(outPath);
			ImageIO.write(bimage, "PNG", out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 给图片添加文字，改文本添加的是儿童身份证不存在情况，没有横线的背景图
	public static void drawTextInImgFront2(String filePath, String outPath,
			FontText text, RdsChildrenPrintCaseModel caseinfo) {
		ImageIcon imgIcon = new ImageIcon(filePath);
		Image img = imgIcon.getImage();
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		BufferedImage bimage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g = bimage.createGraphics();
		g.setColor(getColor(text.getWm_text_color()));
		g.setBackground(Color.white);
		g.drawImage(img, 0, 0, null);
		Font font = null;
		font = new Font(text.getWm_text_font(), Font.PLAIN,
				text.getWm_text_size());
		g.setFont(font);
		// 父母亲信息都不为空
		if (StringUtils.isNotEmpty(caseinfo.getFather_name())
				&& StringUtils.isNotEmpty(caseinfo.getMother_name())) {
			if (caseinfo.getChild_name().length() == 2) {
				g.drawString("姓名：" + caseinfo.getChild_name().substring(0, 1),
						110, 750);
				g.drawString(caseinfo.getChild_name().substring(1, 2), 340, 750);
			} else
				g.drawString("姓名：" + caseinfo.getChild_name(), 110, 750);
			g.drawString("性别：" + caseinfo.getChildsex(), 480, 750);
			g.drawString(
					"父亲：" + caseinfo.getFather_name() + " "
							+ caseinfo.getFather_number(), 760, 750);
			g.drawString("出生：" + caseinfo.getBirth_date().substring(0, 4), 110,
					853);
			g.drawString("年", 385, 853);
			g.drawString(caseinfo.getBirth_date().substring(5, 7), 447, 853);
			g.drawString("月", 512, 853);
			g.drawString(caseinfo.getBirth_date().substring(8, 10), 563, 853);
			g.drawString("日", 623, 853);
			g.drawString(
					"母亲：" + caseinfo.getMother_name() + " "
							+ caseinfo.getMother_number(), 760, 853);

		}
		// 父亲信息为空
		else if (!StringUtils.isNotEmpty(caseinfo.getFather_name())
				&& StringUtils.isNotEmpty(caseinfo.getMother_name())) {
			if (caseinfo.getChild_name().length() == 2) {
				g.drawString("姓名：" + caseinfo.getChild_name().substring(0, 1),
						110, 750);
				g.drawString(caseinfo.getChild_name().substring(1, 2), 340, 750);
			} else
				g.drawString("姓名：" + caseinfo.getChild_name(), 110, 750);
			g.drawString("性别：" + caseinfo.getChildsex(), 480, 750);
			g.drawString(
					"母亲：" + caseinfo.getMother_name() + " "
							+ caseinfo.getMother_number(), 760, 750);
			g.drawString("出生：" + caseinfo.getBirth_date().substring(0, 4), 110,
					853);
			g.drawString("年", 385, 853);
			g.drawString(caseinfo.getBirth_date().substring(5, 7), 447, 853);
			g.drawString("月", 512, 853);
			g.drawString(caseinfo.getBirth_date().substring(8, 10), 563, 853);
			g.drawString("日", 623, 853);

		}
		// 母亲信息为空
		else if (StringUtils.isNotEmpty(caseinfo.getFather_name())
				&& !StringUtils.isNotEmpty(caseinfo.getMother_name())) {
			if (caseinfo.getChild_name().length() == 2) {
				g.drawString("姓名：" + caseinfo.getChild_name().substring(0, 1),
						110, 750);
				g.drawString(caseinfo.getChild_name().substring(1, 2), 340, 750);
			} else
				g.drawString("姓名：" + caseinfo.getChild_name(), 110, 750);
			g.drawString("性别：" + caseinfo.getChildsex(), 480, 750);
			g.drawString(
					"父亲：" + caseinfo.getFather_name() + " "
							+ caseinfo.getFather_number(), 760, 750);
			g.drawString("出生：" + caseinfo.getBirth_date().substring(0, 4), 110,
					853);
			g.drawString("年", 385, 853);
			g.drawString(caseinfo.getBirth_date().substring(5, 7), 447, 853);
			g.drawString("月", 512, 853);
			g.drawString(caseinfo.getBirth_date().substring(8, 10), 563, 853);
			g.drawString("日", 623, 853);
		}
		// 父亲信息都为空
		else {
			// g.drawString("姓名：" + caseinfo.getChild_name(), 200, 750);
			if (caseinfo.getChild_name().length() == 2) {
				g.drawString("姓名：" + caseinfo.getChild_name().substring(0, 1),
						200, 750);
				g.drawString(caseinfo.getChild_name().substring(1, 2), 430, 750);
			} else
				g.drawString("姓名：" + caseinfo.getChild_name(), 200, 750);

			g.drawString("性别：" + caseinfo.getChildsex(), 625, 750);
			g.drawString("出生：" + caseinfo.getBirth_date().substring(0, 4), 940,
					750);
			g.drawString("年", 1210, 750);
			g.drawString(caseinfo.getBirth_date().substring(5, 7), 1270, 750);
			g.drawString("月", 1330, 750);
			g.drawString(caseinfo.getBirth_date().substring(8, 10), 1390, 750);
			g.drawString("日", 1450, 750);
		}
		g.dispose();

		try {
			FileOutputStream out = new FileOutputStream(outPath);
			ImageIO.write(bimage, "PNG", out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// color #2395439
	public static Color getColor(String color) {
		if (color.charAt(0) == '#') {
			color = color.substring(1);
		}
		if (color.length() != 6) {
			return null;
		}
		try {
			int r = Integer.parseInt(color.substring(0, 2), 16);
			int g = Integer.parseInt(color.substring(2, 4), 16);
			int b = Integer.parseInt(color.substring(4), 16);
			return new Color(r, g, b);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	// 生成背面信息
	public static void drawTextInImgBack(String filePath, String outPath,
			FontText text, List<Map<String, String>> results, String case_code) {
		ImageIcon imgIcon = new ImageIcon(filePath);
		Image img = imgIcon.getImage();
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		BufferedImage bimage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g = bimage.createGraphics();
		g.setColor(getColor(text.getWm_text_color()));
		g.setBackground(Color.white);
		g.drawImage(img, 0, 0, null);
		Font font = null;
		font = new Font(text.getWm_text_font(), Font.PLAIN,
				text.getWm_text_size());

		g.setFont(font);
		g.drawString(case_code, 210, 960);
		if (results.size() == 20) {
			font = new Font(text.getWm_text_font(), Font.PLAIN, 35);
			g.setFont(font);
			g.drawString("Marker", 820, 100);
			g.drawString("Allele 1", 1100, 100);
			g.drawString("Allele 2", 1330, 100);
			for (int i = 0; i < results.size(); i++) {
				g.drawString(results.get(i).get("name"), 820, 145 + 45 * i);
				String allele1 = results.get(i).get("value").split(",")[0];
				String allele2 = results.get(i).get("value").split(",")[1];
				if (allele1.length() == 2) {
					g.drawString(allele1, 1140, 145 + 45 * i);
				} else if (allele1.length() == 4) {
					g.drawString(allele1, 1124, 145 + 45 * i);
				} else
					g.drawString(allele1, 1150, 145 + 45 * i);

				if (allele2.length() == 2) {
					g.drawString(allele2, 1370, 145 + 45 * i);
				} else if (allele2.length() == 4) {
					g.drawString(allele2, 1354, 145 + 45 * i);
				} else
					g.drawString(allele2, 1380, 145 + 45 * i);
			}
		} else if (results.size() == 21)  {
			font = new Font(text.getWm_text_font(), Font.PLAIN, 35);
			g.setFont(font);
			g.drawString("Marker", 820, 75);
			g.drawString("Allele 1", 1100, 75);
			g.drawString("Allele 2", 1330, 75);
			for (int i = 0; i < results.size(); i++) {
				g.drawString(results.get(i).get("name"), 820, 120 + 45 * i);
				String allele1 = results.get(i).get("value").split(",")[0];
				String allele2 = results.get(i).get("value").split(",")[1];
				if (allele1.length() == 2) {
					g.drawString(allele1, 1140, 120 + 45 * i);
				} else if (allele1.length() == 4) {
					g.drawString(allele1, 1124, 120 + 45 * i);
				} else
					g.drawString(allele1, 1150, 120 + 45 * i);

				if (allele2.length() == 2) {
					g.drawString(allele2, 1370, 120 + 45 * i);
				} else if (allele2.length() == 4) {
					g.drawString(allele2, 1354, 120 + 45 * i);
				} else
					g.drawString(allele2, 1380, 120 + 45 * i);

				// g.drawString(allele1, 1150, 120+45*i);
				// g.drawString(allele2, 1380, 120+45*i);
			}
		}else{
			font = new Font(text.getWm_text_font(), Font.PLAIN, 32);
			g.setFont(font);
			g.drawString("Marker", 820, 60);
			g.drawString("Allele 1", 1100, 60);
			g.drawString("Allele 2", 1330, 60);
			for (int i = 0; i < results.size(); i++) {
				g.drawString(results.get(i).get("name"), 820, 105 + 42 * i);
				String allele1 = results.get(i).get("value").split(",")[0];
				String allele2 = results.get(i).get("value").split(",")[1];
				if (allele1.length() == 2) {
					g.drawString(allele1, 1140, 105 + 42 * i);
				} else if (allele1.length() == 4) {
					g.drawString(allele1, 1124, 105 + 42 * i);
				} else
					g.drawString(allele1, 1150, 105 + 42 * i);

				if (allele2.length() == 2) {
					g.drawString(allele2, 1370, 105 + 42 * i);
				} else if (allele2.length() == 4) {
					g.drawString(allele2, 1354, 105 + 42 * i);
				} else
					g.drawString(allele2, 1380, 105 + 42 * i);

				// g.drawString(allele1, 1150, 120+45*i);
				// g.drawString(allele2, 1380, 120+45*i);
			}
		}
		g.dispose();

		try {
			FileOutputStream out = new FileOutputStream(outPath);
			ImageIO.write(bimage, "PNG", out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
