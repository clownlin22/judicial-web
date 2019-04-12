package com.rds.bacera.web.controller;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.bacera.service.RdsBaceraInvasivePreAttachmentService;
import com.rds.bacera.service.RdsBaceraInvasivePreEmailService;
import com.rds.bacera.service.RdsBaceraInvasivePreService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("bacera/sendEmail")
public class RdsBaceraInvasivePreSendMailController extends RdsBaceraAbstractController {
	  private static final Logger logger = LoggerFactory.getLogger(RdsBaceraInvasivePreSendMailController.class);

	    @Autowired
	    RdsBaceraInvasivePreEmailService emailService;
	    @Autowired
	    RdsBaceraInvasivePreService rdsBaceraInvasivePreService;
	    
	    @RequestMapping("sendEmail")
	    @ResponseBody
	    public Object sendEmail(@RequestBody Map<String, Object> params,HttpServletRequest request) {
	    	try {
	    		 logger.info("-------------执行发送邮件START---------------");
	 	         boolean result=   emailService.emailManage(params);
	 	        if(result){
	 	     	 logger.info("-------------执行发送邮件END---------------");
	 	     	 //发送成功，插入记录
	 	     	RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
	 					.getAttribute("user");
	 	     	String emailUserName=user.getUsername();
	 	     	params.put("emailUserName", emailUserName);
	 	     	 emailService.inertEmail(params);
	 	     	 //修改发送邮件标识
	 	     	 String emailflag=params.get("emailflag").toString();
	 	     	 if("0".equals(emailflag)){
	 	     		rdsBaceraInvasivePreService.updateEmailFlag(params);
	 	     	 }
	 	        	return this.setModel(true, "发送邮件成功");
	 	        }else{
	 	        	return this.setModel(false, "发送邮件失败");
	 	        }
			} catch (Exception e) {
				e.printStackTrace();
				return this.setModel(true, false, e.getMessage());
			} 
	    }
	    @RequestMapping("queryEmail")
	    @ResponseBody
	    public Object queryEmail(@RequestBody Map<String, Object> params) {

			return emailService.queryEmail(params);
	    }
}
