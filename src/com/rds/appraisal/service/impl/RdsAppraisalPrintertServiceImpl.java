package com.rds.appraisal.service.impl;

/**
 * @author yxb
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.appraisal.mapper.RdsAppraisalInfoMapper;
import com.rds.appraisal.mapper.RdsAppraisalPrinterMapper;
import com.rds.appraisal.model.RdsAppraisalAttachmentModel;
import com.rds.appraisal.model.RdsAppraisalInfoModel;
import com.rds.appraisal.model.RdsAppraisalKeyValueModel;
import com.rds.appraisal.model.RdsAppraisalTemplateModel;
import com.rds.appraisal.service.RdsAppraisalPrinterService;

@Service("RdsAppraisalPrinterService")
public class RdsAppraisalPrintertServiceImpl implements
		RdsAppraisalPrinterService {

	@Setter
	@Autowired
	private RdsAppraisalPrinterMapper rdsAppraisalPrinterMapper;

	@Setter
	@Autowired
	private RdsAppraisalInfoMapper rdsAppraisalInfoMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsAppraisalPrinterMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		return null;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return 0;
	}

	@Override
	public int update(Object params) throws Exception {
		return 0;
	}

	@Override
	public int insert(Object params) throws Exception {
		return 0;
	}

	@Override
	public int delete(Object params) throws Exception {
		return 0;
	}

	@Override
	public List<String> getPrinterInfo(Object params) throws Exception {
		RdsAppraisalInfoModel infoModel = (RdsAppraisalInfoModel) rdsAppraisalInfoMapper
				.queryModel(params);
		RdsAppraisalTemplateModel tempModel = (RdsAppraisalTemplateModel) rdsAppraisalInfoMapper
				.queryHistoryInfo(params);
		List<RdsAppraisalAttachmentModel> attModel = rdsAppraisalInfoMapper
				.queryAttachment(params);
		List<String> lists = new ArrayList<String>();
		Calendar now = Calendar.getInstance();
		lists.add("<DIV class='headerNav font_bold;'><B>"
				+ infoModel.getJudgename() + "司法鉴定意见书</B></DIV>");
		lists.add("<DIV class='header_mark'>" + infoModel.getJudgename() + "["
				+ infoModel.getAccept_date().substring(0,4) + "]法临鉴字第"
				+ infoModel.getCase_number() + "号</DIV>");
//		lists.add("<DIV class='header_mark'>" + infoModel.getJudgename() + "["
//				+ now.get(Calendar.YEAR) + "]法临鉴字第"
//				+ infoModel.getCase_number() + "号</DIV>");
		lists.add("<DIV class='panel_title font_bold pageem'>一、基本情况</DIV>");
		lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>委 托 人：</SPAN><SPAN class='font_nomal'>"
				+ infoModel.getEntrust_per() + "</SPAN></DIV>");
		int size = 0;
		size = infoModel.getEntrust_num().length();
		if(size>0)
		{
			if (size > 25) {
				lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>委托函号：</SPAN><SPAN class='font_nomal'>"
						+ infoModel.getEntrust_num().substring(0, 25) + "</SPAN></DIV>");
				lists.add("<DIV class='panel'><SPAN  class='font_nomal'>"
						+ infoModel.getEntrust_num().substring(25, size) + "</SPAN></DIV>");
			} else {
				lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>委托函号：</SPAN><SPAN class='font_nomal'>"
						+ infoModel.getEntrust_num().substring(0, size) + "</SPAN></DIV>");
			}
			
		}
		float lineSize = 35.0f;
		size = infoModel.getEntrust_matter().length();
		if (size < 25) {
			lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>委托事项：</SPAN><SPAN  class='font_nomal'>"
					+ infoModel.getEntrust_matter() + "</SPAN></DIV>");
		} else {
			lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>委托事项：</SPAN><SPAN  class='font_nomal'>"
					+ infoModel.getEntrust_matter().substring(0, 25)
					+ "</SPAN></DIV>");
			if ((size - 25) > 35) {
				int times = (int) Math.ceil((size - 25) / lineSize);
				for (int i = 0; i < times - 1; i++) {
					lists.add("<DIV class='panel'><SPAN class='font_nomal'>"
							+ infoModel.getEntrust_matter().substring(
									i * 35 + 25, i * 35 + 60) + "</SPAN></DIV>");
				}
				lists.add("<DIV class='panel'><SPAN  class='font_nomal'>"
						+ infoModel.getEntrust_matter().substring(
								(times - 2) * 35 + 60, size) + "</SPAN></DIV>");
			} else {
				lists.add("<DIV class='panel'><SPAN  class='font_nomal'>"
						+ infoModel.getEntrust_matter().substring(25, size)
						+ "</SPAN></DIV>");
			}
		}

		lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>受理日期：</SPAN><SPAN  class='font_nomal'>"
				+ formatDate(infoModel.getAccept_date()) + "</SPAN></DIV>");
		size = infoModel.getIdentify_stuff().length();
		if (size > 25) {
			lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>鉴定材料：</SPAN><SPAN class='font_nomal'>"
					+ infoModel.getIdentify_stuff().substring(0, 25) + "</SPAN></DIV>");
			lists.add("<DIV class='panel'><SPAN  class='font_nomal'>"
					+ infoModel.getIdentify_stuff().substring(25, size) + "</SPAN></DIV>");
		} else {
			lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>鉴定材料：</SPAN><SPAN class='font_nomal'>"
					+ infoModel.getIdentify_stuff().substring(0, size) + "</SPAN></DIV>");
		}
//		lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>鉴定材料：</SPAN><SPAN class='font_nomal'>"
//				+ infoModel.getIdentify_stuff() + "</SPAN></DIV>");
		lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>鉴定日期：</SPAN><SPAN class='font_nomal'>"
				+ formatDate(infoModel.getIdentify_date_start())
				+ "-"
				+ formatDate(infoModel.getIdentify_date_end())
				+ "</SPAN></DIV>");
		lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>鉴定地点：</SPAN><SPAN class='font_nomal'>"
				+ infoModel.getIdentify_place() + "</SPAN></DIV>");
		String entrust_pre = infoModel.getIdentify_per_name() + ","
				+ infoModel.getIdentify_per_sex() + ","
				+ formatDate(infoModel.getIdentify_per_both()) + "生 ,住址："
				+ infoModel.getIdentify_per_address() + ",身份证号："
				+ infoModel.getIdentify_per_idcard();
		size = entrust_pre.length();
		if (size > 30) {
			lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>被鉴定人：</SPAN><SPAN class='font_nomal'>"
					+ entrust_pre.substring(0, 30) + "</SPAN></DIV>");
			lists.add("<DIV class='panel'><SPAN  class='font_nomal'>"
					+ entrust_pre.substring(30, size) + "</SPAN></DIV>");
			// String temp = entrust_pre.substring(30,size);
			// List<String> list = listPrint(temp);
			// for (String string2 : list) {
			// lists.add(string2);
			// }
		} else {
			lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>被鉴定人：</SPAN><SPAN class='font_nomal'>"
					+ entrust_pre.substring(0, size) + "</SPAN></DIV>");
		}

		lists.add("<DIV class='panel_title font_bold pageem'>二、检案摘要</DIV>");
		
		lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>(一)案情摘要</SPAN></DIV>");
//		String caseAbstract = tempModel.getCase_abstract().replaceAll("</p><p>", "<br>").replaceAll("</p>", "").replaceAll("<p>", "");
		String caseAbstract = tempModel.getCase_abstract();
		lists = packageList(lists, caseAbstract);
		
		lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>(二)病历摘要</SPAN></DIV>");
		String sicknessAabstract = tempModel.getSickness_abstract();
		lists = packageList(lists, sicknessAabstract);
		
		lists.add("<DIV class='panel_title font_bold pageem'>三、检验过程</DIV>");
		// 检验方法
		lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>(一)检验方法</SPAN></DIV>");
		String method = tempModel.getProcess_method();
		lists = packageList(lists, method);

		// 体格检查
		lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>(二)体格检查</SPAN></DIV>");
		String check = tempModel.getProcess_check();
		lists = packageList(lists, check);
		
		// 阅片所见
		if (null != tempModel.getProcess_read()
				&& !"".equals(tempModel.getProcess_read())) {
			lists.add("<DIV class='panel pageem'><SPAN class='font_bold'>(三)阅片所见</SPAN></DIV>");
			String read = tempModel.getProcess_read();
			lists = packageList(lists, read);
		}

		// 分析说明
		lists.add("<DIV class='panel_title font_bold pageem'>四、分析说明</DIV>");
		String analysis = tempModel.getAnalysis_text();
		lists = packageList(lists, analysis);
		
		// 鉴定意见
		lists.add("<DIV class='panel_title font_bold pageem'>五、鉴定意见</DIV>");
		String advice = tempModel.getAdvice_text();
		lists = packageList(lists, advice);
//		String[] adviceList = advice.split("<br>");
//		for (String string : adviceList) {
//			if(!string.contains("table"))
//			{
//				if (!("".equals(string))) {
//					List<String> list = listPrint(string);
//					for (String string2 : list) {
//						lists.add(string2);
//					}
//				}
//			}else
//			{
//				String[] listTemp = string.split("</tr>");
//				for(int i = 0 ; i < listTemp.length ; i ++)
//				{
//					if(listTemp.length-1 == i)
//						lists.add(listTemp[i]);
//					else
//					    lists.add(listTemp[i]+"</tr>");
//				}
//			}
//		}
		// 查询该案例鉴证人
		List<Object> listJudge = rdsAppraisalPrinterMapper.queryAll(params);
		for (int i = 0; i < listJudge.size(); i++) {
			RdsAppraisalKeyValueModel model = (RdsAppraisalKeyValueModel) listJudge
					.get(i);
			lists.add("<DIV style='padding-left: 500px; font-size: 30px;'>鉴定人："
					+ model.getKey() + "</DIV>");
			lists.add("<DIV style='padding-left:280px; font-size: 30px;'>《司法鉴定人执业证》证号："
					+ model.getValue() + "</DIV>");
		}
		lists.add("<DIV style='padding-left: 480px; font-size: 30px;'>"
				+ method2China(String.valueOf(now.get(Calendar.YEAR))) + "年"
				+ method2China(String.valueOf(now.get(Calendar.MONTH) + 1))
				+ "月"
				+ method2China(String.valueOf(now.get(Calendar.DAY_OF_MONTH)))
				+ "日</DIV>");
		if (attModel.size() != 0) {
			lists.add("<DIV style='font-size: 30px;'>附件：被鉴定人"
					+ infoModel.getIdentify_per_name() + " 照片"
					+ attModel.size() + "张（共1页）</DIV>");
		}
		lists.add(infoModel.getCase_number());
		return lists;
	}

	// 处理每行样式
	public List<String> listPrint(String string) throws Exception {
		int size;
		List<String> lists = new ArrayList<String>();
		size = string.length();
		if (size <= 30) {
			lists.add("<DIV class='panel pageem'><SPAN class='font_nomal'>"
					+ string + "</SPAN></DIV>");
		} else {
			int len = string.getBytes("GBK").length;
			String first = getSubString(string, 60);
			String temp = "";
			lists.add("<DIV class='panel pageem'><SPAN  class='font_nomal'>"
					+ first + "</SPAN></DIV>");
			for (int i = 0; i < len / 64 - 1; i++) {
				temp += first;
				first = string.substring(temp.length(), string.length());
				first = getSubString(first, 64);
				lists.add("<DIV class='panel'><SPAN  class='font_nomal'>"
						+ first + "</SPAN></DIV>");
				System.out.println(first);
			}
			temp += first;
			first = string.substring(temp.length(), string.length());
			if (!"".equals(first)) {
				lists.add("<DIV class='panel'><SPAN  class='font_nomal'>"
						+ getSubString(first, len - temp.getBytes("GBK").length)
						+ "</SPAN></DIV>");
			}
		}
		return lists;
	}

	//按字节截取字符
	public static String getSubString(String targetString, int byteIndex)
			throws Exception {
		if (targetString.getBytes("GBK").length < byteIndex) {
			return targetString;
		}
		String temp = targetString;
		for (int i = 0; i < targetString.length(); i++) {
			if (temp.getBytes("GBK").length <= byteIndex) {
				break;
			}
			temp = temp.substring(0, temp.length() - 1);
		}
		return temp;
	}

	//日期格式化
	public static String formatDate(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		return format.format(date);
	}

	//数据转大写汉字
	public static String method2China(String num) {
		String[] arrNum = { "O", "一", "二", "三", "四", "五", "六", "七", "八", "九" }; // 大写数字
		String[] strNum = num.split("|");
		String newStr = "";
		for (int i = 0; i < strNum.length; i++) {
			if (strNum[i] != null && !"".equals(strNum[i])) {
				int n = Integer.parseInt(strNum[i]);
				switch (n) {
				case 0:
					newStr += arrNum[n];
					break;
				case 1:
					newStr += arrNum[n];
					break;
				case 2:
					newStr += arrNum[n];
					break;
				case 3:
					newStr += arrNum[n];
					break;
				case 4:
					newStr += arrNum[n];
					break;
				case 5:
					newStr += arrNum[n];
					break;
				case 6:
					newStr += arrNum[n];
					break;
				case 7:
					newStr += arrNum[n];
					break;
				case 8:
					newStr += arrNum[n];
					break;
				case 9:
					newStr += arrNum[n];
					break;
				}
			}
		}
		return newStr;
	}
	
	//表格处理
	public List<String> packageList(List<String> lists, String check)
			throws Exception {
		if (check.contains("table")) {
			String head = check.split("<table")[0];
			String boot = "";
			if (check.split("</table>").length > 1) {
				boot = check.split("</table>")[1];
			}
			String body = check.substring(head.length(),
					check.length() - boot.length());
			if (head == "" && boot == "") {
				String[] listTemp = body.split("</tr>");
				for (int i = 0; i < listTemp.length-1; i++) {
					if (listTemp.length - 2 == i)
						lists.add(listTemp[i] + "</tr>"+listTemp[listTemp.length - 1]);
					else{
						lists.add(listTemp[i] + "</tr>");
					}
				}
			} else {
				if (head != null || head != "") {
					String[] checkList = head.split("<br>");
					for (String string : checkList) {
						if (!("".equals(string))) {
							List<String> list = listPrint(string);
							for (String string2 : list) {
								lists.add(string2);
							}
						}
					}
				}
				if (body != null || body != "") {
					String[] listTemp = body.split("</tr>");
					for (int i = 0; i < listTemp.length-1; i++) {
						if (listTemp.length - 2 == i)
							lists.add(listTemp[i] + "</tr>"+listTemp[listTemp.length - 1]);
						else{
							lists.add(listTemp[i] + "</tr>");
						}
//						if (listTemp.length - 1 == i)
//							lists.add(listTemp[i]);
//						else{
//							if (listTemp[i].contains("<table"))
//								lists.add(listTemp[i] + "</tr>");
//							else
//								lists.add(listTemp[i] + "</tr>");
//						}
							
							
					}
				}
				if (boot != null && boot != "") {
					String[] checkList = boot.split("<br>");
					for (String string : checkList) {
						if (!("".equals(string))) {
							List<String> list = listPrint(string);
							for (String string2 : list) {
								lists.add(string2);
							}
						}
					}
				}
			}

		} else {
			String[] checkList = check.split("<br>");
			for (String string : checkList) {
				List<String> list = listPrint(string);
				for (String string2 : list) {
					lists.add(string2);
				}
			}
		}
		return lists;
	}
}
