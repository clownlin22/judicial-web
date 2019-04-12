package com.rds.mail.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.mail.model.RdsMailKeyValuesModel;
import com.rds.mail.service.RdsMailDicValuesService;

@RequestMapping("mail/dicvalues")
@Controller
public class RdsMailDicValuesController {

	@Autowired
	private RdsMailDicValuesService RdsMailDicValuesService;
	
	@RequestMapping("getMailTypes")
	@ResponseBody
	public List<RdsMailKeyValuesModel> getMailTypes(){
		return RdsMailDicValuesService.getMailTypes();
	}
}
