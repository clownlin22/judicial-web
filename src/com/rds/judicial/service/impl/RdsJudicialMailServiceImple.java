package com.rds.judicial.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.code.utils.MailUtils;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.model.MailInfo;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.mapper.RdsCaseFinanceMapper;
import com.rds.judicial.mapper.RdsJudicialMailMapper;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialMailInfoModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleExpressModel;
import com.rds.judicial.service.RdsJudicialMailService;

@Service
@Transactional
public class RdsJudicialMailServiceImple implements RdsJudicialMailService {

	// 配置文件地址
	private static final String XML_PATH_4 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config4.xml";

	@Autowired
	private RdsJudicialMailMapper RdsJudicialMailMapper;

	@Setter
	@Autowired
	private RdsCaseFinanceMapper caseFeeMapper;

	/**
	 * 获取邮件信息
	 */
	@Override
	public List<MailInfo> getMailInfo(Map<String, Object> params) {
		return MailUtils.getMailStr(params.get("mail_code").toString(), params
				.get("mail_type").toString());
	}

	/**
	 * 删除邮件信息
	 */
	@Override
	public Object delMailInfo(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<RdsJudicialMailInfoModel> list = RdsJudicialMailMapper
				.getAllMails(params);
		if (list.size() == 1) {
			map.put("result", false);
			map.put("message", "只剩一条邮寄信息了，不予删除！");
			return map;
		} else {
			int result = RdsJudicialMailMapper.delMailInfo(params);
			if (result > 0) {
				map.put("result", true);
				map.put("message", "删除成功！");
				return map;
			} else {
				map.put("result", false);
				map.put("message", "删除失败，请联系管理员！");
				return map;
			}
		}
	}

	@Override
	public RdsJudicialResponse getMailCaseInfo(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialCaseInfoModel> caseInfoModels = RdsJudicialMailMapper
				.queryMailCaseInfo(params);
		int count = RdsJudicialMailMapper.countMailCaseInfo(params);
		response.setItems(caseInfoModels);
		response.setCount(count);
		return response;
	}

	/**
	 * 保存邮件信息
	 */
	@Override
	public boolean saveMailInfo(Map<String, Object> params) {
		params.put("mail_id", UUIDUtil.getUUID());
		params.put("mail_code", params.get("mail_code").toString().trim());
		int result = RdsJudicialMailMapper.saveMailInfo(params);
		if (result > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 更新邮件信息
	 */
	@Override
	public boolean updateMailInfo(Map<String, Object> params) {
		params.put("mail_code", params.get("mail_code").toString().trim());
		int result = RdsJudicialMailMapper.updateMailInfo(params);
		if (result > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取所有邮件
	 */
	@Override
	public List<RdsJudicialMailInfoModel> getAllMails(Map<String, Object> params) {
		return RdsJudicialMailMapper.getAllMails(params);
	}

	@Override
	public Map<String, Object> insertException(Map<String, Object> params) {
		// 返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		String mail_exception_id = "";
		String mail_exception_desc = "";
		try {
			Map<String, Object> casefee = caseFeeMapper
					.queryCaseFeeById(params);
			//判断报告是否回款，回款是否确认
			String remittance_id = casefee.get("remittance_id") == null ? ""
					: casefee.get("remittance_id").toString();
			if("".equals(remittance_id))
			{
				mail_exception_id = XmlParseUtil.getXmlValue(XML_PATH_4,
						"mail_exception_id");
				mail_exception_desc = "报告已好，未回款";
			}else
			{
				mail_exception_id = XmlParseUtil.getXmlValue(XML_PATH_4,
						"mail_exception_id_confirm");
				mail_exception_desc = "报告已好，回款未确认";
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("message", "插入异常失败！");
			return map;
		}
		params.put("exception_desc", mail_exception_desc);
		params.put("exception_type", mail_exception_id);
		params.put("exception_id", UUIDUtil.getUUID());
		if (RdsJudicialMailMapper.getException(params) == 0) {
			boolean result = RdsJudicialMailMapper.insertException(params);
			if (result) {
				map.put("result", true);
				map.put("message", "插入异常成功！");
			} else {
				map.put("result", false);
				map.put("message", "插入异常失败！");
			}
		} else {
			map.put("result", false);
			map.put("message", "异常已存在！");
		}
		return map;
	}

	@Override
	public List<RdsJudicialSampleExpressModel> querySampleRecive(
			Map<String, Object> params) {
		return RdsJudicialMailMapper.querySampleRecive(params);
	}
}
