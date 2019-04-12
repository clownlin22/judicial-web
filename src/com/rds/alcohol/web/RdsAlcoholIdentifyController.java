package com.rds.alcohol.web;

import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.service.RdsAlcoholIdentifyService;
import com.rds.upc.model.RdsUpcMessageModel;
import com.rds.upc.model.RdsUpcUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("alcohol/archive")
public class RdsAlcoholIdentifyController {

	@Autowired
	private RdsAlcoholIdentifyService RdsAlcoholIdentifyService;

	@RequestMapping("getidentifyInfo")
	@ResponseBody
	public RdsAlcoholResponse getCaseInfo(@RequestBody Map<String,Object> params,HttpSession session){
		String sql_str="";
		if(session.getAttribute("user")!=null){
			RdsUpcUserModel user=(RdsUpcUserModel)session.getAttribute("user");
			sql_str=user.getSql_str2();
		}
		params.put("sql_str", sql_str);
		return RdsAlcoholIdentifyService.getIdentifyInfo(params);
	}

	public RdsUpcMessageModel setModel(boolean result, String message){

		RdsUpcMessageModel model = new RdsUpcMessageModel();
		model.setResult(result);
		model.setMessage(message);
		return model;
	}

	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String,Object> params,HttpSession session){
		String username="";
		String userid="";
		if(session.getAttribute("user")!=null){
			RdsUpcUserModel user=(RdsUpcUserModel)session.getAttribute("user");
			username=user.getUsername();
			userid = user.getUserid();
		}
		params.put("user_name", username);
		params.put("user_id", userid);

		Object object = params.get("per_id");
		boolean exsitper_name = RdsAlcoholIdentifyService.exsitper_name(params.get("per_name"));
		boolean exsitper_code = RdsAlcoholIdentifyService.exsitper_code(params.get("per_code"));
	
		if (object == null || object.equals("")) {
			if(exsitper_name&&exsitper_code){
				return RdsAlcoholIdentifyService.insert(params)>0?this.setModel(true, "添加成功"):this.setModel(false, "添加失败");
			}else{
				if(!exsitper_name){
					return this.setModel(false, "鉴定人已存在");
				}else{
					return this.setModel(false, "证号已存在");
				}
			}
		} else {
			if(!exsitper_name&&exsitper_code){
				return RdsAlcoholIdentifyService.update(params)>0?this.setModel(true, "修改成功"):this.setModel(false, "修改失败");
			}
			if(exsitper_name&&!exsitper_code){
				return RdsAlcoholIdentifyService.update(params)>0?this.setModel(true, "修改成功"):this.setModel(false, "修改失败");
			}
			if(exsitper_name&&exsitper_code){
				return RdsAlcoholIdentifyService.update(params)>0?this.setModel(true, "修改成功"):this.setModel(false, "修改失败");
			}else{
					return this.setModel(false, "您刚刚输入的已存在!");
			}
		}


	}

	@RequestMapping("delete")
	@ResponseBody
	public 	Boolean delete(@RequestBody Map<String, Object> params) {
		return RdsAlcoholIdentifyService.delete(params)>0?true:false;
	}


	@RequestMapping("exsitper_code")
	@ResponseBody
	public boolean exsitper_code(@RequestBody Map<String, Object> params) {
		String per_code = "";
		if (params.get("per_code") != null) {
			per_code = params.get("per_code").toString();
		}
		return RdsAlcoholIdentifyService.exsitper_code(per_code);
	}
	@RequestMapping("exsitper_name")
	@ResponseBody
	public boolean exsitper_name(@RequestBody Map<String, Object> params) {
		String per_name = "";
		if (params.get("per_name") != null) {
			per_name = params.get("per_name").toString();
		}
		return RdsAlcoholIdentifyService.exsitper_name(per_name);
	}



}




