package com.rds.appraisal.service.impl;

/**
 * @author yxb
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rds.appraisal.mapper.RdsAppraisalEntrustMapper;
import com.rds.appraisal.mapper.RdsAppraisalInfoMapper;
import com.rds.appraisal.model.RdsAppraisalAttachmentModel;
import com.rds.appraisal.model.RdsAppraisalStandardModel;
import com.rds.appraisal.service.RdsAppraisalInfoService;
import com.rds.code.date.DateUtils;
import com.rds.code.utils.DownLoadUtils;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialCaseFeeModel;

@Service("RdsAppraisalInfoService")
public class RdsAppraisalInfoServiceImpl implements RdsAppraisalInfoService {

	@Setter
	@Autowired
	private RdsAppraisalInfoMapper rdsAppraisalInfoMapper;
	@Setter
	@Autowired
	private RdsAppraisalEntrustMapper rdsAppraisalEntrustMapper;
	
	

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";
	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "judicial_path");

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsAppraisalInfoMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return rdsAppraisalInfoMapper.queryModel(params);
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsAppraisalInfoMapper.queryAllCount(params));
		result.put("data", rdsAppraisalInfoMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsAppraisalInfoMapper.queryAllCount(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int update(Object params) throws Exception {
		int result = 0 ;
		//删除鉴定人信息
		result = rdsAppraisalEntrustMapper.deleteJudge(params);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("case_id", ((Map<String, Object>)params).get("case_id"));
		map.put("userid",  ((Map<String, Object>)params).get("mainExpert"));
		map.put("flag",  1);
		map.put("id", UUIDUtil.getUUID());
		result = rdsAppraisalEntrustMapper.insertJudge(map);
		//鉴定人
		String expert = ((Map<String, Object>)params).get("expert").toString();
		String[] experts = expert.split(",");
		if(experts.length>1)
		{
			for(int i = 0 ; i < experts.length ; i ++)
			{
				if (i == 0) {
					map.put("id", UUIDUtil.getUUID());
					map.put("userid",
							experts[i].substring(1, experts[i].length()).trim());
					map.put("flag", 0);
					result = rdsAppraisalEntrustMapper.insertJudge(map);
				}
				if (i == (experts.length - 1)) {
					map.put("id", UUIDUtil.getUUID());
					map.put("userid",
							experts[i].substring(0, experts[i].length() - 1).trim());
					map.put("flag", 0);
					result = rdsAppraisalEntrustMapper.insertJudge(map);
				}
				if (i > 0 && i < (experts.length - 1)) {
					map.put("id", UUIDUtil.getUUID());
					map.put("userid", experts[i].trim());
					map.put("flag", 0);
					result = rdsAppraisalEntrustMapper.insertJudge(map);
				}
			}
		}else{
			map.put("id", UUIDUtil.getUUID());
			map.put("userid", expert.trim());
			map.put("flag", 0);
			result = rdsAppraisalEntrustMapper.insertJudge(map);
		}
		//更新摘要
		result = rdsAppraisalInfoMapper.updateAbstract(params);
		//更新检验过程
		result = rdsAppraisalInfoMapper.updateAdvice(params);
		//更新分析说明
		result = rdsAppraisalInfoMapper.updateAnalysis(params);
		//更新鉴定意见
		result = rdsAppraisalInfoMapper.updateProcess(params);
		
		map.put("flag_status", "4");
		result = rdsAppraisalInfoMapper.updateExamineBaseInfo(map);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insert(Object params) throws Exception {
		int result = 0;
		// 检案摘要
		result = rdsAppraisalInfoMapper.insertAbstract(params);
		//检验过程
		result = rdsAppraisalInfoMapper.insertProcess(params);
		//分析说明
		result = rdsAppraisalInfoMapper.insertAnalysis(params);
		//鉴定意见
		result = rdsAppraisalInfoMapper.insertAdvice(params);
		
		//更新案例状态 、主鉴定人保存信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("case_id", ((Map<String, Object>)params).get("case_id"));
		map.put("flag_status", 4);
		map.put("userid",  ((Map<String, Object>)params).get("mainExpert"));
		map.put("flag",  1);
		map.put("id", UUIDUtil.getUUID());
		//更新状态
		result = rdsAppraisalInfoMapper.updateBaseInfo(map);
		//主鉴定人
		result = rdsAppraisalEntrustMapper.insertJudge(map);
		//鉴定人
		String expert = ((Map<String, Object>)params).get("expert").toString();
		String[] experts = expert.split(",");
		if(experts.length>1)
		{
			for(int i = 0 ; i < experts.length ; i ++)
			{
				if (i == 0) {
					map.put("id", UUIDUtil.getUUID());
					map.put("userid",
							experts[i].substring(1, experts[i].length()).trim());
					map.put("flag", 0);
					result = rdsAppraisalEntrustMapper.insertJudge(map);
				}
				if (i == (experts.length - 1)) {
					map.put("id", UUIDUtil.getUUID());
					map.put("userid",
							experts[i].substring(0, experts[i].length() - 1).trim());
					map.put("flag", 0);
					result = rdsAppraisalEntrustMapper.insertJudge(map);
				}
				if (i > 0 && i < (experts.length - 1)) {
					map.put("id", UUIDUtil.getUUID());
					map.put("userid", experts[i].trim());
					map.put("flag", 0);
					result = rdsAppraisalEntrustMapper.insertJudge(map);
				}
			}
		}else{
			map.put("id", UUIDUtil.getUUID());
			map.put("userid", expert.trim());
			map.put("flag", 0);
			result = rdsAppraisalEntrustMapper.insertJudge(map);
		}
		return result;
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsAppraisalInfoMapper.delete(params);
	}

	@Override
	public Object queryTemplate(Object params) throws Exception {
		return rdsAppraisalInfoMapper.queryTemplate(params);
	}

	@Override
	public int insertTemplate(Object params) throws Exception {
		return rdsAppraisalInfoMapper.insertTemplate(params);
	}

	@Override
	public int insertInfo(Object params) throws Exception {
		return rdsAppraisalInfoMapper.insertInfo(params);
	}

	@Override
	public int insertAbstract(Object params) throws Exception {
		return rdsAppraisalInfoMapper.insertAbstract(params);
	}

	@Override
	public int insertProcess(Object params) throws Exception {
		return rdsAppraisalInfoMapper.insertProcess(params);
	}

	@Override
	public int insertAnalysis(Object params) throws Exception {
		return rdsAppraisalInfoMapper.insertAnalysis(params);
	}

	@Override
	public int insertAdvice(Object params) throws Exception {
		return rdsAppraisalInfoMapper.insertAdvice(params);
	}

	/**
	 * 文件上传
	 */
	@Override
	@Transactional
	public Map<String, Object> upload(String case_id, String attachment_type,
			String attachment_name, String attachment_order,
			MultipartFile[] files) throws Exception {
		String msg = "";
		// 附件目录
		String attachmentPath = ATTACHMENTPATH + case_id
				+ File.separatorChar;
		// 附件名称集合
		String[] attachment_names = attachment_name.split(",");
		// 附件类型集合
		String[] attachment_types = attachment_type.split(",");
		// 附件顺序集合
		String[] attachment_orders = attachment_order.split(",");
		// 保存附件实体
		RdsAppraisalAttachmentModel attachmentModel = new RdsAppraisalAttachmentModel();
		attachmentModel.setCase_id(case_id);
		
		String result = this.queryAttachment(attachmentModel);
		// 对应文件和描述
		int i = 0;
		String filenames = "";
		if (files.length > 0) {
			if(!"".equals(result))
			{
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("case_id", case_id);
				rdsAppraisalInfoMapper.deleteAttachment(params);
			}
			// 遍历文件
			for (MultipartFile mfile : files) {
				attachmentModel.setAttachment_id(UUIDUtil.getUUID());
				attachmentModel.setAttachment_filename(case_id
						+ File.separatorChar + mfile.getOriginalFilename());
				attachmentModel.setAttachment_name(attachment_names[i]);
				attachmentModel.setAttachment_order(attachment_orders[i]);
				attachmentModel.setAttachment_type(attachment_types[i]);
				if (!RdsFileUtil.getState(attachmentPath
						+ mfile.getOriginalFilename())) {
					RdsFileUtil.fileUpload(
							attachmentPath + mfile.getOriginalFilename(),
							mfile.getInputStream());

					if (rdsAppraisalInfoMapper
							.insertAttachment(attachmentModel) > 0)
						msg = msg + "文件：" + mfile.getOriginalFilename()
								+ "上传成功。</br>";
					else
						msg = msg + "文件：" + mfile.getOriginalFilename()
								+ "上传失败。</br>";
				} else {
					rdsAppraisalInfoMapper.insertAttachment(attachmentModel);
					msg = msg + "文件：" + mfile.getOriginalFilename()
							+ "已存在。</br>";
				}
				filenames += mfile.getOriginalFilename() + ";";
				i++;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", true);
			map.put("message", msg);
			map.put("case_id", case_id);
			map.put("filenames", filenames);
			return map;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", false);
			map.put("message", msg + "未收到文件,上传失败");
			return map;
		}
	}

	@Override
	public String queryAttachment(Object params) throws Exception {
		String attachment = "";
		List<RdsAppraisalAttachmentModel> lists = rdsAppraisalInfoMapper
				.queryAttachment(params);
		for (RdsAppraisalAttachmentModel rdsAppraisalAttachmentModel : lists) {
			attachment += rdsAppraisalAttachmentModel.getAttachment_filename()
					+ ";";
		}
		return attachment;
	}
	
	@Override
	public List<RdsAppraisalAttachmentModel> queryAttachmentList(Object params) {
		List<RdsAppraisalAttachmentModel> lists = null;
		try {
			lists = rdsAppraisalInfoMapper
					.queryAttachment(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lists;
	}

	@Override
	public int insertRelation(Object params) throws Exception {
		return rdsAppraisalInfoMapper.insertRelation(params);
	}

	@Override
	public List<Object> queryCaseType(Object params) throws Exception {
		return rdsAppraisalInfoMapper.queryCaseType(params);
	}

	@Override
	public List<RdsAppraisalStandardModel> queryStandard(Object params) throws Exception {
		return (List<RdsAppraisalStandardModel> )rdsAppraisalInfoMapper.queryStandard(params);
	}

	@Override
	public int insertBaseInfo(Object params) throws Exception {
		int result = 0;
		// 被鉴定人信息插入
		result = rdsAppraisalInfoMapper.insertInfo(params);
		result = rdsAppraisalInfoMapper.insert(params);
		return result;
	}

	@Override
	public int deleteType(Object params) throws Exception {
		return rdsAppraisalInfoMapper.deleteType(params);
	}

	@Override
	public int updateBaseInfo(Object params) throws Exception {
		int result = 0;
		result = rdsAppraisalInfoMapper.updateBaseInfo(params);
		result = rdsAppraisalInfoMapper.updateIdentifyInfo(params);
		return result;
	}

	@Override
	public int updateIdentifyInfo(Object params) throws Exception {
		return rdsAppraisalInfoMapper.updateIdentifyInfo(params);
	}

	@Override
	public int updateExamineBaseInfo(Object params) throws Exception {
		return rdsAppraisalInfoMapper.updateExamineBaseInfo(params);
	}

	@Override
	public Object queryHistoryInfo(Object params) throws Exception {
		return rdsAppraisalInfoMapper.queryHistoryInfo(params);
	}

	@Override
	public int updateAbstract(Object params) throws Exception {
		return rdsAppraisalInfoMapper.updateAbstract(params);
	}

	@Override
	public int updateAdvice(Object params) throws Exception {
		return rdsAppraisalInfoMapper.updateAdvice(params);
	}

	@Override
	public int updateAnalysis(Object params) throws Exception {
		return rdsAppraisalInfoMapper.updateAnalysis(params);
	}

	@Override
	public int updateProcess(Object params) throws Exception {
		return rdsAppraisalInfoMapper.updateProcess(params);
	}

	@Override
	public int deleteAttachment(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void getImg(HttpServletResponse response, String filename) {
		DownLoadUtils.download(response, ATTACHMENTPATH + filename);
	}

	@Override
	public int queryBaseCount() throws Exception {
		return rdsAppraisalInfoMapper.queryBaseCount();
	}

	@Override
	public int insertMechanism(Object params) throws Exception {
		return rdsAppraisalInfoMapper.insertMechanism(params);
	}

	@Override
	public int insertCaseFee(Map<String, Object> params) {
		return rdsAppraisalInfoMapper.insertCaseFee(params);
	}

	@Override
	public void exportInfo(String start_time, String end_time, HttpServletResponse response) throws Exception {
		 Map<String, Object> params = new HashMap<String, Object>();
	     params.put("accept_date_starttime", start_time);
	     params.put("accept_date_endtime", end_time);		
	     List<Map<String,Object>> list = rdsAppraisalInfoMapper.queryExportInfo(params);
	     // excel表格列头
	        Object[] titles = { "编号", "受理时间","委托人", "委托函号", "被鉴定人", "被鉴定人身份证号", "归属人",
	                "归属地", "应收款项", "所付款项", "汇款时间","财务确认时间",  "财务备注" };
	        // 表格实体
	        List<List<Object>> bodys = new ArrayList<List<Object>>();
	        // 循环案例列表拼装表格一行
	        for (int i = 0; i < list.size(); i++) {
	            List<Object> objects = new ArrayList<Object>();
	            objects.add(list.get(i).get("case_number"));
				objects.add(StringUtils.dateToChineseTen(list.get(i).get(
						"accept_date") == null ? "" : list.get(i)
						.get("accept_date").toString()));
	            objects.add(list.get(i).get("entrust_per"));
	            objects.add(list.get(i).get("entrust_num"));
	            objects.add(list.get(i).get("identify_per_name"));
	            objects.add(list.get(i).get("identify_per_idcard"));
	            objects.add(list.get(i).get("case_in_person"));
	            objects.add(list.get(i).get("case_in_area"));
	            objects.add(list.get(i).get("real_sum"));
	            objects.add(list.get(i).get("return_sum"));
				objects.add(StringUtils.dateToChineseTen(list.get(i).get(
						"paragraphtime") == null ? "" : list.get(i)
						.get("paragraphtime").toString()));
				objects.add(StringUtils.dateToChineseTen(list.get(i).get(
						"confirm_date") == null ? "" : list.get(i)
						.get("confirm_date").toString()));
	            objects.add(list.get(i).get("finance_remark"));
	            bodys.add(objects);
	        }
	        ExportUtils.export(response, "临床", titles, bodys, "临床管理"
	                + DateUtils.Date2String(new Date()));
	}

}
