package com.rds.judicial.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RdsJudicialLoginController {
	
	
	@RequestMapping(value="login")
	public String initLogin(){
		return "init-login";
	}
	
	@RequestMapping("initadminlogin")
	public String initAdminLogin(){
		return "init-admin-login";
	}
	
	@RequestMapping("adminlogin")
	public String adminMain(@RequestParam String usercode,@RequestParam String password,Model model){
		if(usercode.equals("chenw")){
			model.addAttribute("userid", "2ba6dc9bee434d3db1ed55b8b8595b2a");
			model.addAttribute("usercode", "chenw");
			model.addAttribute("companyid", "54783d747af74ef8bb8e9cf951b7abec");
			model.addAttribute("_super", "super_admin");
		}else{
			model.addAttribute("userid", "8fe97d74e24b4c568b40b8ceb4cbe5b3");
			model.addAttribute("usercode", "super_admin");
			model.addAttribute("companyid", "144206778a5c4fbdac1eeb7f8d0d44fc");
			model.addAttribute("_super", "super_admin");
		}
		
		//登录直接调用接口
		return "admin/main";
	}
	
}
