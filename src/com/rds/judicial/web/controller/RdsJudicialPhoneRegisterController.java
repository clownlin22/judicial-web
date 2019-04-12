package com.rds.judicial.web.controller;

/**
 * @description 手机登记controller
 * @author fushaoming
 * 2015年4月10日
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Setter;
import net.sf.json.JSONObject;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.activiti.model.RdsActivitiTaskDetailModel;
import com.rds.judicial.model.RdsJudicialConfirmReturnModel;
import com.rds.judicial.model.RdsJudicialMailInfoModel;
import com.rds.judicial.model.RdsJudicialPhoneRequestModel;
import com.rds.judicial.model.RdsJudicialRimitModel;
import com.rds.judicial.service.RdsJudicialPhoneRegisterService;
import com.rds.judicial.service.RdsJudicialRegisterService;

@Controller
@RequestMapping("judicial/phone")
public class RdsJudicialPhoneRegisterController {

	@Setter
	@Autowired
	private RdsJudicialPhoneRegisterService pRService;
	
	@Autowired
	private TaskService taskService;
	  
    @Autowired
    private HistoryService historyService;

	@Setter
	@Autowired
	private RdsJudicialRegisterService RdsJudicialRegisterService;
	
	

	/**
	 * 根据查询条件获取案例列表
	 * 
	 * @param start
	 * @param limit
	 * @param userid
	 * @param type
	 * @param case_code
	 * @param starttime
	 * @param endtime
	 * @param client
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("caselist")
	@ResponseBody
	public Map<String, Object> queryListByCondition(@RequestParam int start,
			@RequestParam int limit, @RequestParam String userid,
			@RequestParam Integer type, @RequestParam String case_code,
			@RequestParam String starttime, @RequestParam String endtime,
			@RequestParam String client, @RequestParam String usercode)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usercode", usercode);
		params.put("userid", userid);
		params.put("start", start);
		params.put("end", limit);
		params.put("type", type);
		params.put("case_code", case_code);
		params.put("starttime", starttime);
		params.put("endtime", endtime);
		params.put("client", client);
		return pRService.queryHistory(params);
	}

	/**
	 * 登记
	 *
	 */

	@RequestMapping("register")
	@ResponseBody
	public Map<String, Object> phoneRegister(
			@RequestParam MultipartFile[] file,
			RdsJudicialPhoneRequestModel phoneReq) throws Exception {
		return pRService.register(file, phoneReq);
	}

	/**
	 * 获取银行信息
	 * 
	 * @return
	 */
	@RequestMapping("bankinfolist")
	@ResponseBody
	public Map<String, Object> queryBank() {
		return pRService.queryBank();
	}
	/**
	 * 查询案例流程
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("taskDetail")
    @ResponseBody
    public Object taskDetail(@RequestParam String params, HttpServletRequest request) {
        String id_number = request.getParameter("params");
        List <RdsJudicialMailInfoModel> mailinfomodel=pRService.getMail(id_number);
        String mail="";
        if(mailinfomodel.size()>0){
        for (RdsJudicialMailInfoModel item :
     	   mailinfomodel) { 
     	   mail=item.getMail_type();
     	   if("ems".equals(mail)){
     		   mail="EMS快递";
     		   String mailcode=item.getMail_code();
     		   mail=mail+":"+mailcode;
     	   }else if("ziqu".equals(mail)){
     		   mail="自取";
     	   }else if("zhongtong".equals(mail)){
     		   mail="中通快递";
     		   String mailcode=item.getMail_code();
     		   mail=mail+":"+mailcode;
     	   }else if("shengtong".equals(mail)){
     		   mail="申通快递";
     		   String mailcode=item.getMail_code();
     		   mail=mail+":"+mailcode;
     	   }else if("youzheng".equals(mail)){
     		   mail="邮政";
     		   String mailcode=item.getMail_code();
     		   mail=mail+":"+mailcode;
     	   }else if("shunfeng".equals(mail)){
     		   mail="顺丰快递";
     		   String mailcode=item.getMail_code();
     		   mail=mail+":"+mailcode;
     	   }
     	   
        }
        }
        String processInstanceId =pRService.getProcessInstanceId(id_number);
        List<RdsActivitiTaskDetailModel> rdsActivitiTaskDetailModelList = new ArrayList<RdsActivitiTaskDetailModel>();
        if(null==processInstanceId ||"".equals(processInstanceId)){
			return null;
        }else{
       
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().desc().list();
        for (HistoricTaskInstance item :
                historicTaskInstanceList) {
            RdsActivitiTaskDetailModel model = new RdsActivitiTaskDetailModel();
            model.setId(item.getId());
            model.setName(item.getName());
            model.setTaskDefinitionKey(item.getTaskDefinitionKey());
            model.setAssignee(item.getAssignee());
            model.setClaimTime(item.getClaimTime());
            model.setStartTime(item.getStartTime());
            model.setEndTime(item.getEndTime());
            model.setDurationInMillis(item.getDurationInMillis());
            model.setMail(mail);
     
            List<Comment> comments = taskService.getTaskComments(item.getId(), "comment");
            if (comments.size() > 0) {
                model.setComment(comments.get(0).getFullMessage());
            }
            rdsActivitiTaskDetailModelList.add(model);     
        }

        return rdsActivitiTaskDetailModelList;
        }
    }

	/**
	 * 修改密码
	 * 
	 * @param usercode
	 * @param originpassword
	 * @param new_password
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("repassword")
	@ResponseBody
	public Map<String, Object> repassword(@RequestParam String usercode,
			@RequestParam String oldpass, @RequestParam String newpass)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", usercode);
		params.put("oldpass", oldpass);
		params.put("newpass", newpass);
		return pRService.repassword(usercode, oldpass, newpass);
	}

	/**
	 * 重新采样
	 * 
	 * @param isnullcase_code
	 * @param usercode
	 * @param case_code
	 * @param token
	 * @param charge
	 * @param remark
	 * @param filenum
	 * @param file
	 * @param filetype
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("reregister")
	@ResponseBody
	public Map<String, Object> reregister(@RequestParam MultipartFile[] file,
			RdsJudicialPhoneRequestModel phoneReq) throws Exception {
		return pRService.reregister(file, phoneReq);
	}

	/**
	 * 案例编号查重
	 * 
	 * @param case_code
	 * @return
	 */
	@RequestMapping("iscasecodeexist")
	@ResponseBody
	public Map<String, Object> isCaseCodeExist(@RequestParam String case_code) {
		return pRService.isCaseCodeExist(case_code);
	}

	/**
	 * 获取销售经理
	 * 
	 * @param userid
	 * @return
	 */
	@RequestMapping("getManager")
	@ResponseBody
	public Map<String, Object> getManager(@RequestParam String userid) {
		return pRService.getManager(userid);
	}

	/**
	 * 获取所有的销售经理
	 * 
	 * @return
	 */
	@RequestMapping("getAllManager")
	@ResponseBody
	public Map<String, Object> getAllManager() {
		return pRService.getAllManager();
	}

	/**
	 * 获取销售地区
	 * 
	 * @param userid
	 * @return
	 */
	@RequestMapping("getArea")
	@ResponseBody
	public Map<String, Object> getArea(@RequestParam String managerid) {
		return pRService.getArea(managerid);
	}

	/**
	 * 获取标准金额
	 * 
	 * @param areaid
	 * @param case_type
	 * @return
	 */
	@RequestMapping("getStandFee")
	@ResponseBody
	public Map<String, Object> getStandFee(@RequestParam String areaid,
			@RequestParam String case_type) {
		return pRService.getEquation(areaid, case_type);
	}

	/**
	 * 获取日报
	 * 
	 * @param userid
	 * @return
	 */
	@RequestMapping("getDaily")
	@ResponseBody
	public Map<String, Object> getDaily(@RequestParam String userid) {
		return pRService.getDaily(userid);
	}

	/**
	 * 获取月报
	 * 
	 * @param userid
	 * @return
	 */
	@RequestMapping("getMonthly")
	@ResponseBody
	public Map<String, Object> getMonthly(@RequestParam String userid) {
		return pRService.getMonthly(userid);
	}

	/**
	 * 手机端确认案例回款金额
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("confirmReturn")
	@ResponseBody
	public Map<String, Object> confirmReturn(
			RdsJudicialConfirmReturnModel returnModel) {
		return pRService.confirmReturn(returnModel);
	}

	/**
	 * 日报确认汇款
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("rimit")
	@ResponseBody
	public Map<String, Object> rimit(RdsJudicialRimitModel rimit) {
		return pRService.rimit(rimit);
	}

	/**
	 * 获取案例状态
	 * 
	 * @param case_id
	 * @return
	 */
	@RequestMapping("getCaseStatus")
	@ResponseBody
	public Map<String, Object> getCaseStatus(@RequestParam String case_id) {
		return pRService.getCaseStatus(case_id);
	}

	/**
	 * 获取日报详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("getDailyDetail")
	@ResponseBody
	public Map<String, Object> getDailyDetail(@RequestParam String id) {
		return pRService.getDailyDetail(id);
	}

	/**
	 * 获取月报详情
	 */
	@RequestMapping("getMonthlyDetail")
	@ResponseBody
	public Map<String, Object> getMonthlyDetail(@RequestParam String id) {
		return pRService.getMonthlyDetail(id);
	}

	/**
	 * 获取未确认的财务信息
	 */
	@RequestMapping("getUnConfirm")
	@ResponseBody
	public Map<String, Object> getUnConfirm(@RequestParam String userid) {
		return pRService.getUnConfirm(userid);
	}

	/**
	 * 获取案例信息
	 * 
	 * @return
	 */
	@RequestMapping("getCaseStateByPhone")
	@ResponseBody
	public Map<String, Object> getCaseStateByPhone(@RequestParam String name,@RequestParam String phone,@RequestParam String id_card,
			@RequestParam String case_code,@RequestParam String mail_code,@RequestParam String ownperson) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("phone", phone);
		params.put("case_code", case_code);
		params.put("mail_code", mail_code);
		params.put("ownperson", ownperson);
		params.put("id_card", id_card);
		params.put("is_delete",-1);
		params.put("urgent_state", -1);
		return pRService.getCaseStatesList(params);
	}

	/**
	 * 获取案例信息
	 * 
	 * @return
	 */
	@RequestMapping("getCaseStateById")
	@ResponseBody
	public Map<String, Object> getCaseStateById(@RequestParam String case_id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("case_id", case_id);
		return pRService.getCaseStateById(params);
	}
}
