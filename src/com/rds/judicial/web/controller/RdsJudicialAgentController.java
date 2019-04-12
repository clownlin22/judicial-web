package com.rds.judicial.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.service.RdsJudicialAgentService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("judicial/agent")
public class RdsJudicialAgentController extends RdsJudicialAbstractController {

	@Getter
	@Setter
	private String KEY = "id";
	
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String USER_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "user_permit");

	@Setter
	@Autowired
	private RdsJudicialAgentService rdsJudicialAgentService;

	@Override
	public Integer insert(Map<String, Object> params) throws Exception {
		params.put(KEY, UUIDUtil.getUUID());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", params.get("userid"));
		if(!params.get("flag").toString().equals("1"))
		{
			map.put("peruserid", params.get("peruserid"));
		}
		int check = rdsJudicialAgentService.queryAgentCount(map);
		int result = 0;
		if (check > 0)
			result = -1;
		else
			result = rdsJudicialAgentService.insert(params);
		return result;
	}

	@Override
	public Integer update(Map<String, Object> params) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		int result = 0;
		//判断是否当前市场人员和代理
		int check = rdsJudicialAgentService.queryAgentCount(params);
		//不是
		if(check==0){
			//判断该代理是否配置过
			map.put("userid", params.get("userid"));
			map.put("flag", params.get("flag"));
			check = rdsJudicialAgentService.queryAgentCount(map);
			if (check > 0)
				result = -1;
			else
				result = rdsJudicialAgentService.update(params);
		}else
		{
			result = rdsJudicialAgentService.update(params);
		}
		return result;
	}

	@Override
	public Object delete(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return rdsJudicialAgentService.delete(params);
	}

	@Override
	public Object queryModel(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAll(@RequestBody Map<String, Object> params)
			throws Exception {
		return null;
		// return rdsJudicialAgentService.queryAll(params);
	}

	@RequestMapping("querypage")
	@ResponseBody
	public Object queryPage(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
		int flag = params.get("flag")==null?0:Integer.parseInt(params.get("flag").toString());
		if(flag > 0)
		{
			if (user.getRoletype().equals("103")) {
				if(params.get("peruserid")==null && params.get("usercode")!=null)
//				if(params.get("perusercode")==null){}
					params.put("peruserid", "");
				else
					params.put("peruserid", user.getUserid());
			}
			else if((user.getRoletype().equals("100") || user.getRoletype().equals("101")) && (params.get("peruserid")==null && params.get("userid")==null  ))
				params.put("roletype", "103");
			else if(params.get("userid")!=null && params.get("peruserid")==null)
				params.put("peruserid", "");
			else if((user.getRoletype().equals("100") || user.getRoletype().equals("101"))&&params.get("peruserid")!=null&& params.get("userid")==null)
			{
				params.put("userid", "");
			}else if((user.getRoletype().equals("100") || user.getRoletype().equals("101"))&&params.get("peruserid")!=null&& params.get("userid")!=null)
			{
				params.put("peruserid", "");
			}
			else 
			{
				params.put("userid", user.getUserid());
			}
		}else
		{
			if (user.getRoletype().equals("103")) 
				params.put("peruserid", user.getUserid());
			else if(user.getRoletype().equals("100") || user.getRoletype().equals("101") )
			{
				params.put("roletype", "103");
			}
			else
				params.put("userid", user.getUserid());
				
		}
		return rdsJudicialAgentService.queryAllPage(this.pageSet(params));
	}

	/**
	 * 根据usertype获取用户信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryUserByType")
	@ResponseBody
	public Object queryUserByType(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
//		if (!USER_PERMIT.contains(user.getUsercode()))
//		{
//			params.put("userid", user.getUserid());
//		}
//		if (user.getRoletype().equals("100") || user.getRoletype().equals("101") ) {
//			params.put("userid", "");
//		} else{
//			
//		}
		return rdsJudicialAgentService.queryUserByType(params);
	}

	/**
	 * 分页获取市场人员和代理商关系
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAgent")
	@ResponseBody
	public Object queryAgent(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsJudicialAgentService.queryAgent(this.pageSet(params));
	}

	@Override
	public Object queryAllPage(Map<String, Object> params) throws Exception {
		return null;
	}
	
	@RequestMapping("queryAttachment")
	@ResponseBody
	public Object queryAttachment(@RequestBody Map<String, Object> params)
			throws Exception {
		return null;
	}
}
