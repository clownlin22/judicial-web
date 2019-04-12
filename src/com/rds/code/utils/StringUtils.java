package com.rds.code.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/17
 */
public class StringUtils {
	public static String dateToTen(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static String dateToChineseTen(String datestr) throws Exception {
		if (null == datestr || "".equals(datestr.trim())) {
			return "";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(datestr);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
			return sdf1.format(date);
		}
	}
	
	public static String dateToChineseTenTwo(String datestr) throws Exception {
		if (null == datestr || "".equals(datestr.trim())) {
			return "";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(datestr);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年M月d日");
			return sdf1.format(date);
		}
	}

	public static String dateToEight(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}

	public static String dateToSix(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(date);
	}

	public static String dateToSecond(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String getInitials(String str) {
		String reg = "[\\u4e00-\\u9fa5]+";
		String content = "";
		if (!str.matches(reg)) {
			content = str;
		} else {
			for (int i = 0; i < str.length(); i++) {
				char a = str.charAt(i);
				String[] aa = PinyinHelper.toHanyuPinyinStringArray(a);
				content += aa[0].charAt(0);
			}
		}
		return content;
	}

	static int[] weight = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 }; // 十七位数字本体码权重
	static char[] validate = { '1', '0', 'X', '9', '8', '7', '6', '5', '4',
			'3', '2' }; // mod11,对应校验码字符值

	public static char getValidateCode(String id17) {
		int sum = 0;
		int mode = 0;
		for (int i = 0; i < id17.length(); i++) {
			sum = sum + Integer.parseInt(String.valueOf(id17.charAt(i)))
					* weight[i];
		}
		mode = sum % 11;
		return validate[mode];
	}

	public static String complete0(String number, int digit) {
		try {
			String decimal = number.split("\\.")[1];
			for (int i = 0; i < digit - decimal.length(); i++) {
				number = number + "0";
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			number = number + ".0000";
		}
		return number;
	}

	/**
	 * create date:2010-5-22下午03:32:31 描述：将源字符串中的阿拉伯数字格式化为汉字
	 * 
	 * @param sign
	 *            源字符串中的字符
	 * @return
	 */
	public static char formatDigit(char sign) {
		if (sign == '0')
			sign = '〇';
		if (sign == '1')
			sign = '一';
		if (sign == '2')
			sign = '二';
		if (sign == '3')
			sign = '三';
		if (sign == '4')
			sign = '四';
		if (sign == '5')
			sign = '五';
		if (sign == '6')
			sign = '六';
		if (sign == '7')
			sign = '七';
		if (sign == '8')
			sign = '八';
		if (sign == '9')
			sign = '九';
		return sign;
	}

	/**
	 * create date:2010-5-22下午03:31:51 描述： 获得月份字符串的长度
	 * 
	 * @param str
	 *              待转换的源字符串
	 * @param pos1
	 *            第一个'-'的位置
	 * @param pos2
	 *            第二个'-'的位置
	 * @return
	 */
	public static int getMidLen(String str, int pos1, int pos2) {
		return str.substring(pos1 + 1, pos2).length();
	}

	/**
	 * create date:2010-5-22下午03:32:17 描述：获得日期字符串的长度
	 * 
	 * @param str
	 *              待转换的源字符串
	 * @param pos2
	 *            第二个'-'的位置
	 * @return
	 */
	public static int getLastLen(String str, int pos2) {
		return str.substring(pos2 + 1).length();
	}

	/**
	 * create date:2010-5-22下午03:32:46 描述：格式化日期
	 * 
	 * @param str
	 *            源字符串中的字符
	 * @return ex:二〇一六年三月八日
	 */
	public static String dateToChinese(String str) {
		// 把时分秒截取掉
		if (str.contains(" ")) {
			str = str.substring(0, str.indexOf(" "));
		}
		StringBuffer sb = new StringBuffer();
		int pos1 = str.indexOf("-");
		int pos2 = str.lastIndexOf("-");
		for (int i = 0; i < 4; i++) {
			sb.append(formatDigit(str.charAt(i)));
		}
		sb.append('年');
		if (getMidLen(str, pos1, pos2) == 1) {
			sb.append(formatDigit(str.charAt(5)) + "月");
			if (str.charAt(7) != '0') {
				if (getLastLen(str, pos2) == 1) {
					sb.append(formatDigit(str.charAt(7)) + "日");
				}
				if (getLastLen(str, pos2) == 2) {
					if (str.charAt(7) != '1' && str.charAt(8) != '0') {
						sb.append(formatDigit(str.charAt(7)) + "十"
								+ formatDigit(str.charAt(8)) + "日");
					} else if (str.charAt(7) != '1' && str.charAt(8) == '0') {
						sb.append(formatDigit(str.charAt(7)) + "十日");
					} else if (str.charAt(7) == '1' && str.charAt(8) != '0') {
						sb.append("十" + formatDigit(str.charAt(8)) + "日");
					} else {
						sb.append("十日");
					}
				}
			} else {
				sb.append(formatDigit(str.charAt(8)) + "日");
			}
		}
		if (getMidLen(str, pos1, pos2) == 2) {
			if (str.charAt(5) != '0' && str.charAt(6) != '0') {
				sb.append("十" + formatDigit(str.charAt(6)) + "月");
				if (getLastLen(str, pos2) == 1) {
					sb.append(formatDigit(str.charAt(8)) + "日");
				}
				if (getLastLen(str, pos2) == 2) {
					if (str.charAt(8) != '0') {
						if (str.charAt(8) != '1' && str.charAt(9) != '0') {
							sb.append(formatDigit(str.charAt(8)) + "十"
									+ formatDigit(str.charAt(9)) + "日");
						} else if (str.charAt(8) != '1' && str.charAt(9) == '0') {
							sb.append(formatDigit(str.charAt(8)) + "十日");
						} else if (str.charAt(8) == '1' && str.charAt(9) != '0') {
							sb.append("十" + formatDigit(str.charAt(9)) + "日");
						} else {
							sb.append("十日");
						}
					} else {
						sb.append(formatDigit(str.charAt(9)) + "日");
					}
				}
			} else if (str.charAt(5) != '0' && str.charAt(6) == '0') {
				sb.append("十月");
				if (getLastLen(str, pos2) == 1) {
					sb.append(formatDigit(str.charAt(8)) + "日");
				}
				if (getLastLen(str, pos2) == 2) {
					if (str.charAt(8) != '0') {
						if (str.charAt(8) != '1' && str.charAt(9) != '0') {
							sb.append(formatDigit(str.charAt(8)) + "十"
									+ formatDigit(str.charAt(9)) + "日");
						} else if (str.charAt(8) != '1' && str.charAt(9) == '0') {
							sb.append(formatDigit(str.charAt(8)) + "十日");
						} else if (str.charAt(8) == '1' && str.charAt(9) != '0') {
							sb.append("十" + formatDigit(str.charAt(9)) + "日");
						} else {
							sb.append("十日");
						}
					} else {
						sb.append(formatDigit(str.charAt(9)) + "日");
					}
				}
			} else {
				sb.append(formatDigit(str.charAt(6)) + "月");
				if (getLastLen(str, pos2) == 1) {
					sb.append(formatDigit(str.charAt(8)) + "日");
				}
				if (getLastLen(str, pos2) == 2) {
					if (str.charAt(8) != '0') {
						if (str.charAt(8) != '1' && str.charAt(9) != '0') {
							sb.append(formatDigit(str.charAt(8)) + "十"
									+ formatDigit(str.charAt(9)) + "日");
						} else if (str.charAt(8) != '1' && str.charAt(9) == '0') {
							sb.append(formatDigit(str.charAt(8)) + "十日");
						} else if (str.charAt(8) == '1' && str.charAt(9) != '0') {
							sb.append("十" + formatDigit(str.charAt(9)) + "日");
						} else {
							sb.append("十日");
						}
					} else {
						sb.append(formatDigit(str.charAt(9)) + "日");
					}
				}
			}
		}
		return sb.toString();
	}

	public static String dashu(String input) {
		String s1 = "零一二三四五六七八九";
		String s2 = "零个十百千亿";
		String num = input;
		String unit = "";
		String result = "";
		String num1 = "";
		int len = num.length();
		int n = 0;
		int tag = 0;
		for (int i = 0; i < len - 1; i++) {
			n = Integer.parseInt(num.substring(i, i + 1));
			if (n == 0) {
				if (tag == 1) {
					tag = 1;
					continue;
				} else {
					tag = 1;
					result = result.concat("零");
					continue;
				}
			}
			num1 = s1.substring(n, n + 1);
			n = len - i;
			unit = s2.substring(n, n + 1);
			result = result.concat(num1).concat(unit);
			tag = 0;
		}
		n = Integer.parseInt(num.substring(len - 1, len));
		if (n != 0) {
			num = s1.substring(n, n + 1);
			result = result.concat(num) + "万";
		} else {
			if (tag == 1) {
				n = result.length();
				result = result.substring(0, n - 1) + "万零";
			} else {
				result = result + "万零";
			}
		}
		return result;
	}

	public static String xiaoshu(String input) {
		String s1 = "零一二三四五六七八九";
		String s2 = "零个十百千";
		String num = input;
		String unit = "";
		String result = "";
		String num1 = "";
		int len = num.length();
		int n = 0;
		int tag = 0;
		for (int i = 0; i < len - 1; i++) {
			n = Integer.parseInt(num.substring(i, i + 1));
			if (n == 0) {
				if (tag == 1) {
					tag = 1;
					continue;
				} else {
					tag = 1;
					result = result.concat("零");
					continue;
				}
			}
			num1 = s1.substring(n, n + 1);
			n = len - i;
			unit = s2.substring(n, n + 1);
			result = result.concat(num1).concat(unit);
			tag = 0;
		}
		n = Integer.parseInt(num.substring(len - 1, len));
		if (n != 0) {
			num = s1.substring(n, n + 1);
			result = result.concat(num);
		} else {
			if (tag == 1) {
				n = result.length();
				result = result.substring(0, n - 1);
			}
		}
		if (result.indexOf("一十") == 0) {
			result = result.substring(1);
		}
		return result;
	}

	public static String intToChinese(int input) {
		String temp = input + "";
		int len = temp.length();
		String num = "";
		String num2 = "";
		if (len > 4) {
			num = temp.substring(0, len - 4);
			num2 = temp.substring(len - 4, len);
			num = dashu(num);
			num2 = xiaoshu(num2);
			if (num.substring(num.length() - 1, num.length()).equals(
					num2.substring(0, 1))) {
				num = num.substring(0, num.length() - 1);
			}
			return num + num2;
		} else {
			return xiaoshu(temp);
		}
	}

	public static void convert2Html(String fileName, String outPutFile)
			throws TransformerException, IOException,
			ParserConfigurationException {

		HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(
				fileName));// WordToHtmlUtils.loadDoc(new
							// FileInputStream(inputFile));
		// 兼容2007 以上版本
		// XSSFWorkbook xssfwork=new XSSFWorkbook(new
		// FileInputStream(fileName));
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder()
						.newDocument());
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType,
					String suggestedName, float widthInches, float heightInches) {
				return "test/" + suggestedName;
			}
		});
		wordToHtmlConverter.processDocument(wordDocument);
		// save pictures
		List pics = wordDocument.getPicturesTable().getAllPictures();
		if (pics != null) {
			for (int i = 0; i < pics.size(); i++) {
				Picture pic = (Picture) pics.get(i);
				System.out.println();
				try {
					pic.writeImageContent(new FileOutputStream("D:/test/"
							+ pic.suggestFullFileName()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		Document htmlDocument = wordToHtmlConverter.getDocument();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(out);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "HTML");
		serializer.transform(domSource, streamResult);
		out.close();
		writeFile(new String(out.toByteArray()), outPutFile);
	}

	public static void writeFile(String content, String path) {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		org.jsoup.nodes.Document doc = Jsoup.parse(content);
		content = doc.html();
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			bw.write(content);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fos != null)
					fos.close();
			} catch (IOException ie) {
			}
		}
	}
	
	 public boolean convertHtmlToPdf(String inputFile, String outputFile)  
	            throws Exception {  
	  
	        OutputStream os = new FileOutputStream(outputFile);  
	        ITextRenderer renderer = new ITextRenderer();  
	        String url = new File(inputFile).toURI().toURL().toString();  
	        renderer.setDocument(url);  
	        // 解决中文支持问题  
	        ITextFontResolver fontResolver = renderer.getFontResolver();  
	        fontResolver.addFont("C:/Windows/Fonts/simsunb.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);  
	        //解决图片的相对路径问题  
	        renderer.getSharedContext().setBaseURL("file:/D:/test");  
	        renderer.layout();  
	        renderer.createPDF(os);  
	        os.flush();  
	        os.close();  
	        return true;  
	    }  
	 
	 public static void main(String argv[]) {  
	        try {  
	            convert2Html("D:\\Q2017030001.doc","D:\\1.html");  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
}
