package com.rds.statistics.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.statistics.mapper.RdsFinanceConfigMapper;
import com.rds.statistics.model.RdsFinanceWagesAttachmentModel;
import com.rds.statistics.service.RdsFinanceConfigService;
import com.rds.statistics.service.RdsStatisticsWagesService;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * @author yxb
 * @className
 * @description
 * @date 2017/8/16
 */
@Controller
@RequestMapping("statistics/wages")
public class RdsStatisticsWagesController {
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "wage_path");

	@Autowired
	private RdsStatisticsWagesService rdsStatisticsWagesService;

	@Autowired
	private RdsFinanceConfigService rdsFinanceConfigService;
	
	@Autowired
	private RdsFinanceConfigMapper rdsFinanceConfigMapper;

	@RequestMapping("queryAllPage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("deptcode", user.getDeptcode());
		List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
		String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
				.get(0).get("userlevel").toString()):"";
		if("1".equals(level)){
//			params.put("user_dept_level1","");
		}else if ("2".equals(level)) {
			params.put("user_dept_level1",
					rdsFinanceConfigService.queryUserDepartment(params)
							.get(0).get("user_dept_level1"));
		}else if("3".equals(level)){
			params.put("user_dept_level1",
					rdsFinanceConfigService.queryUserDepartment(params)
							.get(0).get("user_dept_level1"));
			params.put("user_dept_level2",
					rdsFinanceConfigService.queryUserDepartment(params)
							.get(0).get("user_dept_level2"));
		}else if("4".equals(level)){
			params.put("user_dept_level1",
					rdsFinanceConfigService.queryUserDepartment(params)
							.get(0).get("user_dept_level1"));
			params.put("user_dept_level2",
					rdsFinanceConfigService.queryUserDepartment(params)
							.get(0).get("user_dept_level2"));
			params.put("user_dept_level3",
					rdsFinanceConfigService.queryUserDepartment(params)
							.get(0).get("user_dept_level3"));
		}else{
			params.put("username",user.getUsername());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsStatisticsWagesService.queryAllPage(params));
		map.put("total", rdsStatisticsWagesService.queryAllCount(params));
		return map;
	}
	
	@RequestMapping("uploadWages")
	@ResponseBody
	public void uploadWages(@RequestParam String wages_month,@RequestParam String remark,
			@RequestParam MultipartFile files, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("remark", remark);
		params.put("wages_month", wages_month);
		params.put("create_per", user.getUserid());
		String attachment_id = UUIDUtil.getUUID();
		try {
			String attachmentPath = ATTACHMENTPATH + wages_month
					+ File.separatorChar+files
					.getOriginalFilename();
			//上传表格
			if (!RdsFileUtil.getState(attachmentPath)) {
				RdsFileUtil.fileUpload(
								attachmentPath,
								files.getInputStream());
				params.put("attachment_path", wages_month
						+ File.separatorChar+files
						.getOriginalFilename());
				params.put("attachment_id", attachment_id);
				//插入附件列表信息
				rdsStatisticsWagesService.insertWagesAttachment(params);
			} else {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().print(
						"{\"success\":true,\"result\":false,\"msg\":\""
								+ files
								.getOriginalFilename()+"：文件已存在！" + "\"}");
				return;
			}
			// 上传案例文件
			Map<String, Object> map = rdsStatisticsWagesService.uploadWages(wages_month, attachmentPath,  user.getUserid(),attachment_id);
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"result\":true,\"msg\":\""
							+ map.get("message") + "\"}");
		} catch (IOException e) {
			e.printStackTrace();
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"result\":false,\"msg\":\""
							+ "出错了，请重新上传！" + "\"}");
		}

	}
	
	@RequestMapping("queryWagesAttachment")
	@ResponseBody
	public Object queryWagesAttachment(
			@RequestBody Map<String, Object> params, HttpSession session){
		return rdsStatisticsWagesService.queryWagesAttachment(params);
	}
	
	@RequestMapping("deleteWages")
	@ResponseBody
	public void deleteWages(@RequestBody Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		try {
			//先删除库里的数据
			boolean result = rdsStatisticsWagesService.deleteWages(params);
			//删除附件
			RdsFileUtil.delFolder(ATTACHMENTPATH+params.get("attachment_path").toString());
			if (result) {
				//删除上传的附件信息
				if(rdsStatisticsWagesService.updateWagesAttachment(params)){
					response.setContentType("text/html; charset=utf-8");
					response.getWriter().print(
							"{\"result\":true,\"msg\":\"" + "删除成功！" + "\"}");
				}else
				{
					response.setContentType("text/html; charset=utf-8");
					response.getWriter()
							.print("{\"result\":false,\"msg\":\"" + "删除失败，请联系管理员！"
									+ "\"}");
				}
			} else {

				response.setContentType("text/html; charset=utf-8");
				response.getWriter()
						.print("{\"result\":false,\"msg\":\"" + "删除失败，请联系管理员！"
								+ "\"}");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 下载图片
	 */
	@RequestMapping("downloadAttachment")
	public void downloadAttachment(HttpServletResponse response,
			@RequestParam String attachment_id) {
		Map<String,Object> map = new HashMap<>();
		map.put("attachment_id", attachment_id);
		List<Object> list = rdsStatisticsWagesService.queryWagesAttachment(map);
		RdsFinanceWagesAttachmentModel model = (RdsFinanceWagesAttachmentModel)list.get(0);
		String filename = model.getAttachment_path();
		File file = new File(ATTACHMENTPATH + filename);
		if (file.exists()) {
			response.reset();
			response.setHeader(
					"Content-Disposition",
					"attachment; filename="
							+ filename.substring(filename
									.lastIndexOf(File.separator) + 1));
			response.setContentType("application/octet-stream; charset=utf-8");
			FileInputStream in = null;
			OutputStream toClient = null;
			try {
				in = new FileInputStream(file);
				// 得到文件大小
				int i = in.available();
				byte data[] = new byte[i];
				// 读数据
				in.read(data);
				toClient = response.getOutputStream();
				toClient.write(data);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (toClient != null) {
					try {
						toClient.flush();
						toClient.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}
	
	@RequestMapping("exportWagesInfo")
	@ResponseBody
	public void exportWagesInfo(HttpServletRequest request,
			HttpServletResponse response) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String wages_month_start = request.getParameter("wages_month_start");
			params.put("wages_month_start", wages_month_start);
			String wages_month_end = request.getParameter("wages_month_end");
			params.put("wages_month_end", wages_month_end);
			String user_dept_level1 = request.getParameter("user_dept_level1");
			params.put("user_dept_level1", user_dept_level1);
			String user_dept_level2 = request.getParameter("user_dept_level2");
			params.put("user_dept_level2", user_dept_level2);
			String user_dept_level3 = request.getParameter("user_dept_level3");
			params.put("user_dept_level3", user_dept_level3);
			String wages_name = request.getParameter("wages_name");
			params.put("wages_name", wages_name);

			
			params.put("deptcode", user.getDeptcode());
			List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
			String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
					.get(0).get("userlevel").toString()):"";
			if("1".equals(level)){
//				params.put("user_dept_level1","");
			}else if ("2".equals(level)) {
				params.put("user_dept_level1",
						rdsFinanceConfigService.queryUserDepartment(params)
								.get(0).get("user_dept_level1"));
			}else if("3".equals(level)){
				params.put("user_dept_level1",
						rdsFinanceConfigService.queryUserDepartment(params)
								.get(0).get("user_dept_level1"));
				params.put("user_dept_level2",
						rdsFinanceConfigService.queryUserDepartment(params)
								.get(0).get("user_dept_level2"));
			}else if("4".equals(level)){
				params.put("user_dept_level1",
						rdsFinanceConfigService.queryUserDepartment(params)
								.get(0).get("user_dept_level1"));
				params.put("user_dept_level2",
						rdsFinanceConfigService.queryUserDepartment(params)
								.get(0).get("user_dept_level2"));
				params.put("user_dept_level3",
						rdsFinanceConfigService.queryUserDepartment(params)
								.get(0).get("user_dept_level3"));
			}else{
				params.put("username",user.getUsername());
			}
			
//			params.put("deptcode", user.getDeptcode());
//			if (!AMOEBA_PERMIT.contains(user.getUsercode())) {
//				params.put("ztsybmc",
//						rdsFinanceConfigService.queryUserDepartment(params)
//								.get(0).get("user_dept_level1"));
//			}
			rdsStatisticsWagesService.exportWagesInfo(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
