package com.rds.appraisal.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.appraisal.model.RdsAppraisalInfoModel;
import com.rds.appraisal.model.RdsAppraisalLogModel;
import com.rds.appraisal.model.RdsAppraisalStandardModel;
import com.rds.appraisal.model.RdsAppraisalTemplateModel;
import com.rds.appraisal.model.RdsAppraisalTypeModel;
import com.rds.appraisal.service.RdsAppraisalEntrustService;
import com.rds.appraisal.service.RdsAppraisalInfoService;
import com.rds.appraisal.service.RdsAppraisalLogService;
import com.rds.appraisal.service.RdsAppraisalTypeService;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;

/**
 * 鉴定报告controller
 * 
 * @author yxb 2015-07-29
 */
@Controller
@RequestMapping("appraisal/info")
public class RdsAppraisalInfoController extends RdsAppraisalAbstractController {

	@Setter
	@Autowired
	private RdsAppraisalInfoService rdsAppraisalInfoService;
	@Setter
	@Autowired
	private RdsAppraisalTypeService rdsAppraisalTypeService;
	@Setter
	@Autowired
	private RdsAppraisalLogService rdsAppraisalLogService;
	@Setter
	@Autowired
	private RdsAppraisalEntrustService rdsAppraisalEntrustService;
	
	/**
	 * 保存
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@ResponseBody
	public Object insert(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		try {
			
			//日志记录
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			RdsAppraisalLogModel logModel = (RdsAppraisalLogModel)rdsAppraisalLogService.queryModel(params);
			Map<String,Object> mapLog = new HashMap<String, Object>();
			mapLog.put("case_id", params.get("case_id"));
			mapLog.put("nowstatus", 4);
			mapLog.put("prestatus", logModel.getNowstatus());
			mapLog.put("fquser", user.getUsername());
			rdsAppraisalLogService.update(mapLog);
			
			int reslut = rdsAppraisalInfoService.insert(params);
			if(reslut>0){
				return this.setModel(true, RdsUpcConstantUtil.SAVE_SUCCESS);
			}
			else
				return this.setModel(false, RdsUpcConstantUtil.SAVE_FAILED);
		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}
	}
	
	/**
	 * 保存更新
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("saveUpdate")
	@ResponseBody
	public Object saveUpdate(@RequestBody Map<String, Object> params) {
		try {
			int reslut = rdsAppraisalInfoService.update(params);
			if(reslut>0){
				return this.setModel(true, RdsUpcConstantUtil.SAVE_SUCCESS);
			}
			else
				return this.setModel(false, RdsUpcConstantUtil.SAVE_FAILED);
		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}
		
	}
	/**
	 * 保存登记信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("saveBaseInfo")
	@ResponseBody
	public Object saveBaseInfo(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			//案例id
			String case_id = "";
			if(null == params.get("case_id") || "".equals(params.get("case_id")))
			{
				case_id = UUIDUtil.getUUID();
				//委托事项id处理
				String entrust_type = (params.get("entrust_type")==null?"":params.get("entrust_type")).toString();
				String[] entrust_types = entrust_type.split(",");
				//鉴定类型集合
				String type_ids = "";
				for (String str : entrust_types) {
					type_ids +="'"+str.replace("[", "").replace("]", "").trim()+"',";
				}
				//查询
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("type_id", type_ids.substring(0, type_ids.length()-1));
				//委托事项
//				String entrust_des = params.get("identify_per_name").toString()+"的";
				//查询出所有鉴定类型对应的实体
				List<RdsAppraisalTypeModel> list = (List<RdsAppraisalTypeModel>) rdsAppraisalTypeService
						.queryAll(map);
				//遍历鉴定类型对应的实体，封装委托事项，并且保存到对应的鉴定类型表
				for (RdsAppraisalTypeModel rdsAppraisalTypeModel : list) {
//					entrust_des += (rdsAppraisalTypeModel.getStandard_name() + ":" + rdsAppraisalTypeModel
//							.getStandard_desc()) + ";";
					//鉴定类型
					Map<String,Object> mapType = new HashMap<String, Object>();
					mapType.put("type_id", rdsAppraisalTypeModel.getType_id());
					mapType.put("case_id", case_id);
					mapType.put("id", UUIDUtil.getUUID());
					//插入案例对应的鉴定类型
					rdsAppraisalInfoService.insertRelation(mapType);
				}
//				params.put("entrust_matter", entrust_des);
				params.put("case_id", case_id);
				params.put("create_per", user.getUserid());
//				params.put("case_number", rdsAppraisalInfoService.queryBaseCount()+1);
				//基本信息插入
				rdsAppraisalInfoService.insertBaseInfo(params);
				params.put("fee_id", UUIDUtil.getUUID());
				rdsAppraisalInfoService.insertCaseFee(params);
				Map<String,Object> mapLog = new HashMap<String, Object>();
				mapLog.put("id", UUIDUtil.getUUID());
				mapLog.put("case_id", case_id);
				mapLog.put("nowstatus", "2");
				mapLog.put("fquser", user.getUsername());
				rdsAppraisalLogService.insert(mapLog);
				//正常，返回结果
				return this.setModel(true, RdsUpcConstantUtil.SAVE_SUCCESS);
			}else
			{
				//案例id
				case_id = params.get("case_id").toString();
				//先删除案例对应的类型，再插入更新
				rdsAppraisalInfoService.deleteType(params);
				//委托事项id处理
				String entrust_type = (params.get("entrust_type")==null?"":params.get("entrust_type")).toString();
				String[] entrust_types = entrust_type.split(",");
				//鉴定类型集合
				String type_ids = "";
				for (String str : entrust_types) {
					type_ids +="'"+str.replace("[", "").replace("]", "").trim()+"',";
				}
				//查询
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("type_id", type_ids.substring(0, type_ids.length()-1));
//				//委托事项id处理
//				String entrust_matter = params.get("entrust_matter").toString();
//				String[] entrust_matters = entrust_matter.split(",");
//				//鉴定类型集合
//				String type_ids = "";
//				for (String str : entrust_matters) {
//					type_ids +="'"+str.replace("[", "").replace("]", "").trim()+"',";
//				}
//				//查询
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("type_id", type_ids.substring(0, type_ids.length()-1));
				//委托事项
//				String entrust_des = params.get("identify_per_name").toString()+"的";
				//查询出所有鉴定类型对应的实体
				List<RdsAppraisalTypeModel> list = (List<RdsAppraisalTypeModel>) rdsAppraisalTypeService
						.queryAll(map);
				//遍历鉴定类型对应的实体，封装委托事项，并且保存到对应的鉴定类型表
				for (RdsAppraisalTypeModel rdsAppraisalTypeModel : list) {
//					entrust_des += (rdsAppraisalTypeModel.getStandard_name() + ":" + rdsAppraisalTypeModel
//							.getStandard_desc()) + ";";
					//鉴定类型
					Map<String,Object> mapType = new HashMap<String, Object>();
					mapType.put("type_id", rdsAppraisalTypeModel.getType_id());
					mapType.put("case_id", case_id);
					mapType.put("id", UUIDUtil.getUUID());
					//插入案例对应的鉴定类型
					rdsAppraisalInfoService.insertRelation(mapType);
				}
//				params.put("entrust_matter", entrust_des);
				params.put("case_id", case_id);
				params.put("create_per", user.getUserid());
				//基本信息插入
				rdsAppraisalInfoService.updateBaseInfo(params);
				//正常，返回结果
				return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
			}
			
			
		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}
		
	}

	/**
	 * 上传文件
	 * @param attachment_type
	 * @param attachment_name
	 * @param attachment_order
	 * @param files
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("upload")
	@ResponseBody
	public void upload(@RequestParam String case_id,
			@RequestParam String attachment_type,
			@RequestParam String attachment_name,
			@RequestParam String attachment_order,
			@RequestParam MultipartFile[] files, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = rdsAppraisalInfoService.upload(case_id,
				attachment_type, attachment_name, attachment_order, files);
		try {
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"msg\":\""
							+ (String) map.get("message") + "\""
							+ ",\"case_id\":\"" + (String) map.get("case_id")+"\""
							+ ",\"filenames\":\""+(String)map.get("filenames")
							+ "\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 获取上传文件目录
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAttachment")
	@ResponseBody
	public Object queryAttachment(@RequestBody Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		try
		{
			//根据case_id获取文件目录集合，前台显示
			String msg = rdsAppraisalInfoService.queryAttachment(params);
			if("".equals(msg))
			{
				return this.setModel(false,"query failed!");
			}else
			{
				return this.setModel(true,msg);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}
	/**
	 * 
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		return null;
	}

	/**
	 * 
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryModel")
	@ResponseBody
	public Object queryModel(@RequestBody Map<String, Object> params)
			throws Exception {
		return null;
	}
	
	/**
	 * 
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("matter")
	@ResponseBody
	public Object queryMatter(@RequestBody Map<String, Object> params)
			throws Exception {
		return null;
	}
	
	/**
	 * 模版查询
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("template")
	@ResponseBody
	public Object queryTemplate(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsAppraisalInfoService.queryTemplate(params);
	}
	
	/**
	 * 保存模版
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("saveTemplate")
	@ResponseBody
	public Object saveTemplate(@RequestBody Map<String, Object> params)
			throws Exception {
		int result = 0;
		try
		{
			params.put("template_id", UUIDUtil.getUUID());
			result = rdsAppraisalInfoService.insertTemplate(params);
			if(result > 0)
			{
				return this.setModel(true,
						RdsUpcConstantUtil.INSERT_SUCCESS);
			}else
			{
				return this.setModel(false,
						RdsUpcConstantUtil.INSERT_FAILED);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	/**
	 * 
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAll")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params)
			throws Exception {
		return null;
	}

	/**
	 * 分页查询鉴定
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAllPage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("usercode", user.getUsercode());
			params.put("recrive_name", "徐元敏");
		}
		return rdsAppraisalInfoService.queryAllPage(this.pageSet(params));
	}
	
	/**
	 * 查询登记信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryBaseInfo")
	@ResponseBody
	public Object queryBaseInfo(@RequestBody Map<String, Object> params)
			throws Exception {
		RdsAppraisalInfoModel infoModel = (RdsAppraisalInfoModel)rdsAppraisalInfoService.queryModel(params);
		return infoModel;
	}
	/**
	 * 查询登记信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryJudgeInfo")
	@ResponseBody
	public Object queryJudgeInfo(@RequestBody Map<String, Object> params)
			throws Exception {
		List<String> list = rdsAppraisalEntrustService.queryJudge(params);
		return list;
	}
	
	/**
	 * 查询历史记录
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryHistoryInfo")
	@ResponseBody
	public Object queryHistoryInfo(@RequestBody Map<String, Object> params)
			throws Exception {
		RdsAppraisalTemplateModel infoModel = (RdsAppraisalTemplateModel)rdsAppraisalInfoService.queryHistoryInfo(params);
		return infoModel;
	}
	
	/**
	 * 条件查询鉴定类型
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryCaseType")
	@ResponseBody
	public Object queryCaseType(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsAppraisalInfoService.queryCaseType(params);
	}
	
	/**
	 * 条件查询鉴定标准
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryStandard")
	@ResponseBody
	public Object queryStandard(String query,String appraisaltype)
			throws Exception {
		List<RdsAppraisalStandardModel> lists = new ArrayList<RdsAppraisalStandardModel>();
		if("" == appraisaltype)
		{
			return lists;
		}else
		{
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("type_id", appraisaltype);
			map.put("keyword", query);
			lists = (List<RdsAppraisalStandardModel>) rdsAppraisalInfoService.queryStandard(map);
			return lists;
		}
	}
	/**
	 * 条件历史报告
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryHistory")
	@ResponseBody
	public Object queryHistory(String query)
			throws Exception {
//		query=new String(query.getBytes("iso-8859-1"),"utf-8");
		System.out.println("query-------------------"+query);
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("search", query);
		params.put("flag_status", 6);
		return rdsAppraisalInfoService.queryAll(params);
	}
	
	/**
	 * 建议查询条件历史报告
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAdvice")
	@ResponseBody
	public Object queryAdvice(String query)
			throws Exception {
//		query=new String(query.getBytes("iso-8859-1"),"utf-8");
		System.out.println("query-------------------"+query);
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("advice", query);
		params.put("flag_status", 6);
		return rdsAppraisalInfoService.queryAll(params);
	}
	
	
	/**
	 * 审核登记信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("examineBaseInfo")
	@ResponseBody
	public Object examineBaseInfo(@RequestBody Map<String, Object> params,HttpServletRequest request)
			throws Exception {
		int result = 0;
		result = rdsAppraisalInfoService.updateExamineBaseInfo(params);
		//日志记录
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
		RdsAppraisalLogModel logModel = (RdsAppraisalLogModel)rdsAppraisalLogService.queryModel(params);
		Map<String,Object> mapLog = new HashMap<String, Object>();
		mapLog.put("case_id", params.get("case_id"));
		mapLog.put("nowstatus", params.get("flag_status"));
		mapLog.put("prestatus", logModel.getNowstatus());
		mapLog.put("fquser", user.getUsername());
		rdsAppraisalLogService.update(mapLog);
		
		if(result > 0)
		{
			return this.setModel(true,
					RdsUpcConstantUtil.UPDATE_SUCCESS);
		}else
		{
			return this.setModel(false,
					RdsUpcConstantUtil.UPDATE_FAILED);
		}
	}
	
	/**
	 * 获取图片文件
	 */
	@RequestMapping("getImg")
	public void getImg(HttpServletResponse response, String filename) {
//		try {
//			filename=new String(filename.getBytes("iso-8859-1"),"utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}  
		rdsAppraisalInfoService.getImg(response, filename);
	}
	
	@RequestMapping("checkExamine")
	@ResponseBody
	public Object checkExamine(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HashMap<Object, Object> params = new HashMap<Object, Object>();
		params.put("case_id", request.getParameter("case_id"));
		params.put("flag_status", request.getParameter("flag_status"));
		int result = rdsAppraisalInfoService.updateExamineBaseInfo(params);
		
		//日志记录
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
		RdsAppraisalLogModel logModel = (RdsAppraisalLogModel)rdsAppraisalLogService.queryModel(params);
		Map<String,Object> mapLog = new HashMap<String, Object>();
		mapLog.put("case_id", params.get("case_id"));
		mapLog.put("nowstatus", request.getParameter("flag_status"));
		mapLog.put("prestatus", logModel.getNowstatus());
		mapLog.put("fquser", user.getUsername());
		rdsAppraisalLogService.update(mapLog);
		
		if(result>0)
			return this.setModel(true,"审核成功");
		else
			return this.setModel(false,"审核失败") ;
	}
	
    @RequestMapping("exportInfo")
    public void exportInfo(@RequestParam String accept_date_starttime, @RequestParam String accept_date_endtime,
                           HttpServletResponse response) {
    	try {
			rdsAppraisalInfoService.exportInfo(accept_date_starttime,accept_date_endtime,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
