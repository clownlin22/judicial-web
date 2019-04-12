package com.rds.judicial.web.controller;

/**
 * 
 * @discription 附件管理
 * @author fushaoming
 * @date 20150407
 */
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.mapper.RdsJudicialPrintMapper;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialPrintSampleModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;
import com.rds.judicial.service.RdsJudicialCaseAttachmentService;
import com.rds.judicial.service.RdsJudicialRegisterService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("judicial/attachment")
public class RdsJudicialCaseAttachmentController {
	
	private static final String FATHER = "father";
	private static final String Mother = "mother";
	@Autowired
	private RdsJudicialRegisterMapper rdsJudicialRegisterMapper;
	@Autowired
	private RdsJudicialPrintMapper rdsJudicialPrintMapper;

	@Setter
	@Autowired
	private RdsJudicialCaseAttachmentService attachmentService;

	@Setter
	@Autowired
	private RdsJudicialRegisterService rdsJudicialRegisterService;

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String USER_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "user_permit");
	
	// 附件存放地址
		private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
				FILE_PATH, "judicial_path");

	/**
	 * 获取图片信息
	 */
	@RequestMapping("getAttachMent")
	@ResponseBody
	public List<RdsJudicialCaseAttachmentModel> getAttachMent(
			@RequestBody Map<String, Object> params) {
		return attachmentService.getAttachMent(params);
	}

	/**
	 * 获取图片文件
	 */
	@RequestMapping("getImg")
	public void getImg(HttpServletResponse response, String filename) {
		attachmentService.getImg(response, filename);
	}
	
	/**
	 * 获取图片文件宽高
	 */
	@RequestMapping("getImgWidth")
	@ResponseBody
	public Map<String, Object> getImgWidth(@RequestBody Map<String, Object> params) {
		Map<String, Object> map = new HashMap<>();
		File file = new File(ATTACHMENTPATH+params.get("filename").toString());
		if (file.exists()) {
			BufferedImage bi = null;
			try {
				bi = javax.imageio.ImageIO.read(file);
				map.put("height", bi.getHeight());
				map.put("width", bi.getWidth());
				System.out.println(bi.getHeight()+"------"+bi.getWidth());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 获取附件列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping("queryallpage")
	@ResponseBody
	public Map<String, Object> queryAllPage(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			if (!USER_PERMIT.contains(user.getUsercode())) {
				params.put("userid", user.getUserid());
			}
		}
		return attachmentService.queryAllPage(params);
	}

	/**
	 * 获取附件列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping("queryAll")
	@ResponseBody
	public List<RdsJudicialCaseAttachmentModel> queryAll(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		return attachmentService.queryAll(params);
	}

	
	@RequestMapping("upload")
	@ResponseBody
	public void upload(@RequestParam String case_code,
			@RequestParam int[] filetype, @RequestParam MultipartFile[] files,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("case_code", case_code);
		params.put("userid", user.getUserid());

		try {
			//上传案例图片
			Map<String, Object> map = attachmentService.upload(case_code,
					files, filetype, user.getUserid());
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"result\":true,\"msg\":\""
							+ (String) map.get("message") + "\"}");
		} catch (IOException e) {
			e.printStackTrace();
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"result\":false,\"msg\":\""
							+ "出错了，请重新上传！" + "\"}");
		}

	}

	@RequestMapping("delete")
	@ResponseBody
	public void delete(@RequestBody Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		boolean result = attachmentService.deleteFile(params);
		try {
			if (result) {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().print(
						"{\"result\":true,\"msg\":\"" + "删除成功！" + "\"}");
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
	 * 下载文件
	 */
	@RequestMapping("download")
	public void download(HttpServletResponse response, @RequestParam String id) {

		attachmentService.download(response, id);
	}

	/**
	 * 获取打印附件信息
	 */
	@RequestMapping("getPrintAttachment")
	@ResponseBody
	public RdsJudicialResponse getPrintAttachment(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
//		params.put("userid", user.getUserid());
		String partner_name="";
		if (user != null) {
			partner_name = user.getParnter_name();
        }
		params.put("partner_name", partner_name);
		return attachmentService.getPrintAttachment(params);
	}

	/**
	 * 改变打印状态
	 */
	@RequestMapping("updateAttachmentPrint")
	@ResponseBody
	public boolean printAttachment(@RequestBody Map<String, Object> params) {
		return attachmentService.printAttachment(params);
	}

	@RequestMapping("printAttachment")
	public String printAttachment(String case_code,String type, String path,
			HttpServletRequest request) {
		RdsJudicialCaseInfoModel rdsJudicialCaseInfoModel = rdsJudicialRegisterMapper
				.queryCaseInfoByCaseCode(case_code);
		String case_id = rdsJudicialCaseInfoModel.getCase_id();
		List<RdsJudicialSampleInfoModel> rdsJudicialSampleInfoModels = rdsJudicialPrintMapper
				.getSampleInfo(case_id);
	String father="";
	String mother="";
	String child="";
			for (RdsJudicialSampleInfoModel sampleInfoModel : rdsJudicialSampleInfoModels) {
					if (FATHER.equals(sampleInfoModel.getSample_call())) {
					    father=sampleInfoModel.getSample_username();
					} else if (Mother.equals(sampleInfoModel.getSample_call())) {
						mother=sampleInfoModel.getSample_username();
					} else {
						child+=sampleInfoModel.getSample_username()+"、";
					}
			}
		child=child.substring(0,child.length()-1);
		File file = new File(ATTACHMENTPATH + path);
		double height = 0.0 ;
		double width=0.0 ;
		if (file.exists()) {
			BufferedImage bi = null;
			try {
				bi = javax.imageio.ImageIO.read(file);
				height =  bi.getHeight();
				width = bi.getWidth();
				System.out.println(bi.getHeight()+"------"+bi.getWidth());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(height>600 && width >500)
		{
			if(height/600.0 > width/500.0)
			{
				width = width / (height/600.0);
				height = 600;
			}else
			{
				height = height /(width/500.0);
				width = 500;

			}
		}else if(height<600 && width >500)
		{
			height = height /(width/500.0);
			width = 500;
		}else if(height>600 && width <500)
		{
			width = width / (height/600.0);
			height = 600;
		}
		int count=0;
		if(case_code.startsWith("Z")&& case_code.length()<11){
			count=1;
			request.setAttribute("case_code", case_code.substring(5));
		}
		else if(case_code.startsWith("YJ")){
			count=4;
			type="33";
			request.setAttribute("case_code", case_code);
			request.setAttribute("code", case_code.substring(2,6));
			request.setAttribute("father", father);
			request.setAttribute("mother", mother);
			child=child.replace('、','和');
			request.setAttribute("child", child);
		}
		else if(case_code.startsWith("WZ-")){
			count=2;
			if(case_code.substring(3,4).equals("0")){
				request.setAttribute("case_code", case_code.substring(4));
			}else{request.setAttribute("case_code", case_code.substring(3));}
		}
		else if(case_code.startsWith("SH")){
			count=3;
			request.setAttribute("case_code", case_code);
		}else{
			request.setAttribute("case_code", case_code.substring(5));
		}
		
		System.out.println("width============="+width+"===========height"+height);
		request.setAttribute("path", path);
		request.setAttribute("type", type);
		request.setAttribute("height", height);
		request.setAttribute("width", width);
		request.setAttribute("count", count);
		return "dna/attachment";
	}

	@RequestMapping("turnImg")
	@ResponseBody
	public Map<String, Object> turnImg(@RequestBody Map<String, Object> params) {
		return attachmentService.turnImg(params);
	}

	@RequestMapping("getCaseInfo")
	@ResponseBody
	public Map<String, Object> getCaseInfo(Map<String, Object> params) {
		params = attachmentService.getCaseInfo(params);
		return params;
	}

	@RequestMapping("updateAllAttachmentPrint")
	@ResponseBody
	public boolean updateAllAttachmentPrint(
			@RequestBody Map<String, Object> params) {
		return attachmentService.updateAllAttachment(params);
	}

	/**
	 * 打印照片和身份证
	 * 
	 * @param case_code
	 * @param count
	 * @param request
	 * @return
	 */
	@RequestMapping("printAllAttachment")
	public String printAllAttachment(@RequestParam String case_code,
			@RequestParam String count, HttpServletRequest request) {
		attachmentService.printAllCaseAttachment(case_code, count, request);
		return "dna/all_attachment";
	}

}
