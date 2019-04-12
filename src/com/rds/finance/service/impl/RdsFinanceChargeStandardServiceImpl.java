package com.rds.finance.service.impl;

/**
 * yuanxiaobo 2016-10-08
 */
import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.code.utils.JScriptInvoke;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.mapper.RdsFinanceChargeStandardMapper;
import com.rds.finance.model.RdsFinanceAscriptionInfo;
import com.rds.finance.model.RdsFinanceChargeStandardModel;
import com.rds.finance.model.RdsFinanceSpecialModel;
import com.rds.finance.service.RdsFinanceChargeStandardService;

@Service("RdsFinanceChargeStandardService")
public class RdsFinanceChargeStandardServiceImpl implements
		RdsFinanceChargeStandardService {

	// 配置文件地址
	private static final String XML_PATH = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config.xml";

	@Setter
	@Autowired
	private RdsFinanceChargeStandardMapper rdsFinanceChargeStandardMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsFinanceChargeStandardMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return rdsFinanceChargeStandardMapper.queryModel(params);
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total",
				rdsFinanceChargeStandardMapper.queryAllCount(params));
		result.put("data", rdsFinanceChargeStandardMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return 0;
	}

	@Override
	public int update(Object params) {
		try {
			return rdsFinanceChargeStandardMapper.update(params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int updates(Object params) {
		try {
			return rdsFinanceChargeStandardMapper.updates(params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int insert(Object params) {
		try {
			return rdsFinanceChargeStandardMapper.insert(params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int insertOld(Object params) {
		try {
			return rdsFinanceChargeStandardMapper.insertOld(params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsFinanceChargeStandardMapper.delete(params);
	}

	@Override
	public Object queryAreaInitials(Object params) throws Exception {
		return rdsFinanceChargeStandardMapper.queryAreaInitials(params);
	}

	@Override
	public Object queryMarketByAgent(Object params) throws Exception {
		return rdsFinanceChargeStandardMapper.queryMarketByAgent(params);
	}

	@Override
	public List<RdsFinanceAscriptionInfo> queryAscription(Object params)
			throws Exception {
		return rdsFinanceChargeStandardMapper.queryAscription(params);
	}

	@Override
	public Map<String, Object> getStandFee(Integer typeid, Integer pernum,
			String areaid, Integer case_type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("areaid", areaid);
		map.put("case_type", case_type);
		try {
			List<RdsFinanceChargeStandardModel> equation = rdsFinanceChargeStandardMapper
					.getEquation(map);
			if (equation.size() > 0) {
				String script = equation.get(0).getEquation()
						.replaceAll("\n", "");
				Double standFee;
				standFee = JScriptInvoke.getStandardFee(script, pernum, typeid);
				map.clear();
				map.put("standFee", standFee == null ? 0 : standFee);
				// map.put("discountRate", equation.get(0).getDiscountrate());
				map.put("success", true);
			} else {
				map.put("standFee", "");
				// map.put("discountRate", "");
				map.put("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("standFee", "");
			// map.put("discountRate", "");
			map.put("success", false);
		}
		return map;
	}

	@Override
	public int queryExistCount(Map<String, Object> map) throws Exception {
		return rdsFinanceChargeStandardMapper.queryExistCount(map);
	}

	@Override
	public int insertApplicationCode(Map<String, Object> params) {
		try {
			String now_time = com.rds.code.utils.StringUtils
					.dateToEight(new Date());
			DecimalFormat df = new DecimalFormat("0000");
			String confirm_code = "";
			// 激活码生成数量
			int num = Integer.parseInt(params.get("num").toString());
			for (int i = 0; i < num; i++) {
				// 根据当前日期生成激活码号
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH,
						"finance_date");
				if (xml_time.equals(now_time)) {
					confirm_code = params.get("usercode")
							+ "-"
							+ now_time
							+ "-"
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH, "finance_special")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH, "finance_special", String
									.valueOf(Integer.parseInt(XmlParseUtil
											.getXmlValue(XML_PATH,
													"finance_special")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH, "finance_date",
							now_time);
					XmlParseUtil.updateXmlValue(XML_PATH, "finance_special",
							"1");
					confirm_code = params.get("usercode")
							+ "-"
							+ now_time
							+ "-"
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH, "finance_special")));
				}
				// if(!"1".equals(XmlParseUtil.getXmlValue(XML_PATH,
				// "finance_special")))
				// {
				// XmlParseUtil.updateXmlValue(XML_PATH, "finance_special",
				// String
				// .valueOf(Integer.parseInt(XmlParseUtil.getXmlValue(
				// XML_PATH, "finance_special")) + 1));
				// }
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", UUIDUtil.getUUID());
				map.put("confirm_code", confirm_code);
				map.put("create_per", params.get("userid"));
				map.put("remark", params.get("remark"));
				rdsFinanceChargeStandardMapper.insertApplicationCode(map);
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	@Transactional
	public Object updateApplicationCode(Map<String, Object> params)
			throws Exception {
		Map<String, Object> map = new HashMap<>();
		if ("3".equals(params.get("case_state").toString())){
		int code_amount=Integer.parseInt( params.get("code_amount").toString());
		String confirm_code0 = null;
		String confirm_code00=null;
		if(1==code_amount){
		try {
			String now_time = com.rds.code.utils.StringUtils
					.dateToEight(new Date());
			DecimalFormat df = new DecimalFormat("0000");
			String confirm_code = "";
			// 根据当前日期生成激活码号
			String xml_time = XmlParseUtil
					.getXmlValue(XML_PATH, "finance_date");
			if (xml_time.equals(now_time)) {
				confirm_code = params.get("usercode")
						+ "-"
						+ now_time
						+ "-"
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "finance_special")) + 1);
				XmlParseUtil.updateXmlValue(XML_PATH, "finance_special", String
						.valueOf(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "finance_special")) + 1));
			} else {
				XmlParseUtil.updateXmlValue(XML_PATH, "finance_date", now_time);
				XmlParseUtil.updateXmlValue(XML_PATH, "finance_special", "1");
				confirm_code = params.get("usercode")
						+ "-"
						+ now_time
						+ "-"
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "finance_special")));
			}
			params.put("id", UUIDUtil.getUUID());
			params.put("confirm_code", confirm_code);
			if( rdsFinanceChargeStandardMapper.insertApplicationCode(params)>0)
			{
				map.put("result", true);
				map.put("confirm_code", confirm_code);
				return map;
			}else
			{
				map.put("result", false);
				return map;
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			return map;
		}}
		else{
			for(int i=1;i<=code_amount;i++){
				String now_time = com.rds.code.utils.StringUtils
						.dateToEight(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				String confirm_code = "";
				// 根据当前日期生成激活码号
				String xml_time = XmlParseUtil
						.getXmlValue(XML_PATH, "finance_date");
				if (xml_time.equals(now_time)) {
					confirm_code = params.get("usercode")
							+ "-"
							+ now_time
							+ "-"
							+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
									XML_PATH, "finance_special")) + 1);
					XmlParseUtil.updateXmlValue(XML_PATH, "finance_special", String
							.valueOf(Integer.parseInt(XmlParseUtil.getXmlValue(
									XML_PATH, "finance_special")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH, "finance_date", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH, "finance_special", "1");
					confirm_code = params.get("usercode")
							+ "-"
							+ now_time
							+ "-"
							+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
									XML_PATH, "finance_special")));
				}
				params.put("id", UUIDUtil.getUUID());
				params.put("confirm_code", confirm_code);
				int result=rdsFinanceChargeStandardMapper.insertApplicationCode(params);
				if(result<=0){
					map.put("result", false);
					return map;
				}
				if(1==i){
					 confirm_code0=params.get("confirm_code").toString();
				}
				if(i==code_amount){
					confirm_code00=params.get("confirm_code").toString();
					
				}
			} 
			String cofirm_code000= confirm_code0+"---"+confirm_code00;
			map.put("result", true);
			map.put("confirm_code",cofirm_code000 );
			return map;
		}
		}else{
			try {
				String now_time = com.rds.code.utils.StringUtils
						.dateToEight(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				String confirm_code = "";
				// 根据当前日期生成激活码号
				String xml_time = XmlParseUtil
						.getXmlValue(XML_PATH, "finance_date");
				if (xml_time.equals(now_time)) {
					confirm_code = params.get("usercode")
							+ "-"
							+ now_time
							+ "-"
							+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
									XML_PATH, "finance_special")) + 1);
					XmlParseUtil.updateXmlValue(XML_PATH, "finance_special", String
							.valueOf(Integer.parseInt(XmlParseUtil.getXmlValue(
									XML_PATH, "finance_special")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH, "finance_date", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH, "finance_special", "1");
					confirm_code = params.get("usercode")
							+ "-"
							+ now_time
							+ "-"
							+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
									XML_PATH, "finance_special")));
				}
				params.put("id", UUIDUtil.getUUID());
				params.put("confirm_code", confirm_code);
				if( rdsFinanceChargeStandardMapper.insertApplicationCode(params)>0)
				{
					map.put("result", true);
					map.put("confirm_code", confirm_code);
					return map;
				}else
				{
					map.put("result", false);
					return map;
				}
					
			} catch (Exception e) {
				e.printStackTrace();
				map.put("result", false);
				return map;
			}
		}
	}

	@Override
	public List<RdsFinanceSpecialModel> queryAllSpecialFinance(Object params)
			throws Exception {
		return rdsFinanceChargeStandardMapper.queryAllSpecialFinance(params);
	}

	@Override
	public int queryCountSpecialFinance(Object params) throws Exception {
		return rdsFinanceChargeStandardMapper.queryCountSpecialFinance(params);
	}

	@Override
	public int updateCodeUsed(Map<String, Object> map) throws Exception {
		return rdsFinanceChargeStandardMapper.updateCodeUsed(map);
	}

	@Override
	public boolean queryFinanceSpecialExist(Map<String, Object> map)
			throws Exception {
		return rdsFinanceChargeStandardMapper.queryFinanceSpecialExist(map) > 0 ? true
				: false;
	}

	@Override
	public boolean deleteConfirm(Map<String, Object> map) throws Exception {
		return rdsFinanceChargeStandardMapper.deleteConfirm(map);
	}

	@Override
	public Object queryInvasiveAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total",
				rdsFinanceChargeStandardMapper.queryInvasiveAllCount(params));
		result.put("data", rdsFinanceChargeStandardMapper.queryInvasiveAllPage(params));
		return result;
	}

	@Override
	public int insertInvasiveStandard(Object params) throws Exception {
		return rdsFinanceChargeStandardMapper.insertInvasiveStandard(params);
	}

	@Override
	public int updateInvasiveStandard(Object params) throws Exception {
		return rdsFinanceChargeStandardMapper.updateInvasiveStandard(params);
	}

	@Override
	public int deleteInvasiveStandard(Object params) throws Exception {
		return rdsFinanceChargeStandardMapper.deleteInvasiveStandard(params);
	}

	@Override
	public int queryExistInversiveCount(Map<String, Object> map)
			throws Exception {
		return rdsFinanceChargeStandardMapper.queryExistInversiveCount(map);
	}

}
