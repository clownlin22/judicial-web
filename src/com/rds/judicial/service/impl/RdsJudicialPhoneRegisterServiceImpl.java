package com.rds.judicial.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.text.SimpleDateFormat;
import com.rds.code.utils.JScriptInvoke;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialBankMapper;
import com.rds.judicial.mapper.RdsJudicialCaseFeeMapper;
import com.rds.judicial.mapper.RdsJudicialCaseStateMapper;
import com.rds.judicial.mapper.RdsJudicialFeeQuationMapper;
import com.rds.judicial.mapper.RdsJudicialFinanceDailyMapper;
import com.rds.judicial.mapper.RdsJudicialFinanceMapper;
import com.rds.judicial.mapper.RdsJudicialFinanceMonthlyMapper;
import com.rds.judicial.mapper.RdsJudicialPhoneMapper;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.model.RdsJudicialBankModel;
import com.rds.judicial.model.RdsJudicialCaseFeeModel;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialCaseStateModel;
import com.rds.judicial.model.RdsJudicialCaseStatusModel;
import com.rds.judicial.model.RdsJudicialConfirmReturnModel;
import com.rds.judicial.model.RdsJudicialDailyDetailModel;
import com.rds.judicial.model.RdsJudicialFeeQuationModel;
import com.rds.judicial.model.RdsJudicialFinanceDailyModel;
import com.rds.judicial.model.RdsJudicialFinanceMonthlyModel;
import com.rds.judicial.model.RdsJudicialMailInfoModel;
import com.rds.judicial.model.RdsJudicialPhoneCaseListModel;
import com.rds.judicial.model.RdsJudicialPhoneRequestModel;
import com.rds.judicial.model.RdsJudicialRimitModel;
import com.rds.judicial.service.RdsJudicialCaseAttachmentService;
import com.rds.judicial.service.RdsJudicialPhoneRegisterService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.service.RdsUpcUserService;

/**
 * @description 手机端服务
 * @author ThinK 2015年4月15日
 */
@Service("RdsJudicialPhoneRegisterService")
public class RdsJudicialPhoneRegisterServiceImpl implements
		RdsJudicialPhoneRegisterService {

	@Setter
	@Autowired
	private RdsJudicialRegisterMapper rdsJudicialRegisterMapper;

	@Setter
	@Autowired
	private RdsJudicialPhoneMapper pMapper;

	@Setter
	@Autowired
	private RdsJudicialCaseAttachmentService attachmentService;

	@Setter
	@Autowired
	private RdsJudicialCaseFeeMapper caseFeeMapper;

	@Setter
	@Autowired
	private RdsJudicialBankMapper rdsJudicialBankMapper;

	@Setter
	@Autowired
	private RdsUpcUserService rdsUpcUserService;

	@Setter
	@Autowired
	private RdsJudicialFinanceDailyMapper dailyMapper;

	@Setter
	@Autowired
	private RdsJudicialFinanceMonthlyMapper monthlyMapper;

	@Setter
	@Autowired
	private RdsJudicialFeeQuationMapper equationMapper;

	@Setter
	@Autowired
	private RdsJudicialFinanceMapper financeMapper;

	@Setter
	@Autowired
	private RdsJudicialCaseStateMapper rdsJudicialCaseStateMapper;

	private Map<String, Object> setMsg(boolean success, String msg) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("msg", msg);
		return map;
	}
	

	// 上传文件
	private int upload(String case_id, String case_code, MultipartFile[] file,
			String filetype) throws Exception {
		Map<String, Object> map = attachmentService.uploadAttachment(case_id,
				case_code, file, filetype);
		if ((Boolean) map.get("success") == true) {
			return 1;
		} else
			return 0;
	}

	// 获取银行信息
	@Override
	public Map<String, Object> queryBank() {
		List<RdsJudicialBankModel> list = rdsJudicialBankMapper.queryBank();
		if (list.isEmpty()) {
			Map<String, Object> map = setMsg(false, "获取银行列表失败");
			map.put("bank", "");
			map.put("banknum", 0);
			return map;
		} else {
			Map<String, Object> bankInfoMap = new HashMap<String, Object>();
			List<String> bankaccountlist = null;
			Set<String> set = new HashSet<String>();
			Iterator<RdsJudicialBankModel> listit = list.iterator();
			while (listit.hasNext()) {
				set.add(listit.next().getBankname());
			}
			int banknum = set.size();
			Iterator<String> setit = set.iterator();
			while (setit.hasNext()) {
				String bankname = setit.next();
				bankaccountlist = new ArrayList<String>();
				listit = list.iterator();
				while (listit.hasNext()) {
					RdsJudicialBankModel bank = listit.next();

					if (bank.getBankname().equals(bankname)) {

						bankaccountlist.add(bank.getBankaccount());
					}
				}
				bankInfoMap.put(bankname, bankaccountlist);
			}
			Map<String, Object> map = setMsg(true, "OK");
			map.put("bank", bankInfoMap);
			map.put("banknum", banknum);
			return map;
		}
	}

	@Override
	public Map<String, Object> queryHistory(Map<String, Object> params)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) rdsUpcUserService
				.queryForLogin(params);
		if (user == null) {
			map.put("success", false);
			map.put("msg", "未登录");
			return map;
		}
		params.put("usertype", user.getUsertype());
		params.put("deptcode", user.getDeptcode());
		// 获取数据列表
		List<RdsJudicialPhoneCaseListModel> maplist = pMapper
				.queryByCaseinper(params);
		int totalnum = pMapper.queryByCaseinperCount(params);
		if (!maplist.isEmpty()) {
			map.put("success", true);
			map.put("data", maplist);
			map.put("totalnum", totalnum);
		} else {
			map.put("success", false);
			map.put("msg", "没有数据,请稍后再试");
		}
		return map;
	}

	@Override
	@Transactional
	public Map<String, Object> register(String usercode, String userid,
			String case_code, double charge, String remark, int filenum,
			MultipartFile[] file, String filetype) throws Exception {
		// String case_id = pMapper.getCaseID(case_code);
		// // case_id已经存在的话说明这个案例编号已经被使用了
		// if (case_id == null || "".equals(case_id)) {
		// RdsJudicialCaseInfoModel caseInfoModel = new
		// RdsJudicialCaseInfoModel();
		// case_id = UUIDUtil.getUUID();
		// caseInfoModel.setCase_id(case_id);
		// caseInfoModel.setCase_code(case_code);
		// caseInfoModel.setCase_in_per(userid);
		// caseInfoModel.setAccept_time(new SimpleDateFormat("yyyy-MM-dd")
		// .format(new Date()));
		// caseInfoModel.setRemark(remark);
		// // 上传登记照片
		// if (upload(case_id, case_code, file, filetype) > 0) {
		// if (pMapper.firstInsertCase(caseInfoModel) > 0) {
		// RdsJudicialFeePerCaseModel feepercaseModel = new
		// RdsJudicialFeePerCaseModel();
		// feepercaseModel.setCase_id(case_id);
		// feepercaseModel.setCase_fee(charge);
		// // 插入案例费用
		// if (feepercaseMapper.insert(feepercaseModel) > 0) {
		// return setMsg(true, "登记成功");
		// } else
		// return setMsg(false, "登记失败");
		// } else
		// return setMsg(false, "登记失败");
		// } else
		// return setMsg(false, "登记失败");
		// } else
		return setMsg(false, "案例编号已经存在，登记失败");
	}

	@Override
	@Transactional
	public Map<String, Object> reregister(MultipartFile[] file,
			RdsJudicialPhoneRequestModel phoneReq) throws Exception {
		List<RdsJudicialCaseFeeModel> caseFeeList = pMapper
				.getIsNullCase(phoneReq.getIsdeletecase_code());
		if (caseFeeList.size() > 0) {
			String case_id = pMapper.getCaseID(phoneReq.getCase_code());
			if (case_id == null || "".equals(case_id)) {
				RdsJudicialCaseInfoModel caseInfoModel = new RdsJudicialCaseInfoModel();
				case_id = UUIDUtil.getUUID();
				String casefee_id = UUIDUtil.getUUID();
				caseInfoModel.setCase_id(case_id);
				caseInfoModel.setCase_code(phoneReq.getCase_code());
				caseInfoModel.setCase_in_per(phoneReq.getUserid());
				caseInfoModel.setAccept_time(phoneReq.getAccept_time());
				caseInfoModel.setReceiver_id(phoneReq.getAreaid());
				caseFeeList.get(0).setId(casefee_id);
				// 上传登记照片
				if (upload(case_id, phoneReq.getCase_code(), file,
						phoneReq.getFiletype()) > 0) {
					if (pMapper.firstInsertCase(caseInfoModel) > 0) {
						caseFeeList.get(0).setCase_id(case_id);
						if (caseFeeMapper.insertCaseFee(caseFeeList.get(0)) > 0) {
							return setMsg(true, "登记成功");
						} else
							return setMsg(false, "登记失败");
					} else
						return setMsg(false, "登记失败");
				} else
					return setMsg(false, "文件上传失败，登记失败");
			} else
				return setMsg(false, "案例编号已经存在，登记失败");
		} else
			return setMsg(false, "该作废案例不存在，请联系管理员");
	}

	@Override
	public Map<String, Object> isCaseCodeExist(String case_code) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("case_code", case_code);
		if (pMapper.exsitCaseCode(params) > 0) {
			return setMsg(false, "条形码已经存在,请更换条形码重试");
		} else
			return setMsg(true, "OK");
	}

	@Override
	@Transactional
	public Map<String, Object> repassword(String usercode, String oldpass,
			String newpass) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usercode", usercode);
		RdsUpcUserModel user = (RdsUpcUserModel) rdsUpcUserService
				.queryForLogin(params);
		if (user == null) {
			return setMsg(false, "请重新登录");
		}
		if (oldpass.equals(user.getPassword())) {
			params.put("userid", "'" + user.getUserid() + "'");
			params.put("password", newpass);
			if (rdsUpcUserService.updatePass(params) > 0) {
				params.clear();
				params.put("success", true);
				params.put("msg", "OK");
				return params;
			} else {
				params.put("success", false);
				params.put("msg", "修改密码失败");
				return params;
			}
		} else {
			params.put("success", false);
			params.put("msg", "原密码错误，无法修改");
			return params;
		}
	}

	@Override
	public Map<String, Object> getManager(String userid) {
		Map<String, Object> msg = new HashMap<String, Object>();
		List<Map<String, Object>> list = pMapper.getManager(userid);
		if (list.size() < 1) {
			msg.put("success", false);
			msg.put("data", "该采样员未配置销售经理！");
		} else {
			msg.put("success", true);
			msg.put("data", list);
		}
		return msg;
	}

	@Override
	public Map<String, Object> getArea(String managerid) {
		Map<String, Object> msg = new HashMap<String, Object>();
		List<Map<String, Object>> list = pMapper.getArea(managerid);
		if (list.size() < 1) {
			msg.put("success", false);
			msg.put("data", "该销售经理未配置地区！");
		} else {
			msg.put("success", true);
			msg.put("data", list);
		}
		return msg;
	}

	@Override
	public Map<String, Object> getStandFee(Integer typeid, Integer pernum,
			String areaid, Integer case_type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("areaid", areaid);
		map.put("case_type", case_type);
		try {
			List<RdsJudicialFeeQuationModel> equation = equationMapper
					.getEquation(map);
			if (equation.size() > 0) {
				String script = equation.get(0).getEquation()
						.replaceAll("\n", "");
				Double standFee;
				standFee = JScriptInvoke.getStandardFee(script, pernum, typeid);
				map.clear();
				map.put("standFee", standFee == null ? 0 : standFee);
				map.put("discountRate", equation.get(0).getDiscountrate());
				map.put("success", true);
			} else {
				map.put("standFee", "");
				map.put("discountRate", "");
				map.put("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("standFee", "");
			map.put("discountRate", "");
			map.put("success", false);
		}
		return map;
	}

	public Map<String, Object> getEquation(String areaid, String case_type) {
		Map<String, Object> map = new HashMap<>();
		map.put("areaid", areaid);
		map.put("case_type", case_type);
		List<RdsJudicialFeeQuationModel> equation = equationMapper
				.getEquation(map);
		map.clear();
		if (equation.size() > 0) {
			map.put("equation", equation.get(0).getEquation());
			map.put("discountRate", equation.get(0).getDiscountrate());
			map.put("success", true);
		} else {
			map.put("success", false);
			map.put("discountRate", "");
			map.put("equation", "");
		}
		return map;
	}

	@Override
	@Transactional
	public Map<String, Object> register(MultipartFile[] file,
			RdsJudicialPhoneRequestModel phoneReq) throws Exception {
		String case_id = pMapper.getCaseID(phoneReq.getCase_code());
		// case_id已经存在的话说明这个案例编号已经被使用了
		if (case_id == null || "".equals(case_id)) {
			RdsJudicialCaseInfoModel caseInfoModel = new RdsJudicialCaseInfoModel();
			case_id = UUIDUtil.getUUID();
			String casefee_id = UUIDUtil.getUUID();
			caseInfoModel.setCase_id(case_id);
			caseInfoModel.setCase_code(phoneReq.getCase_code());
			caseInfoModel.setCase_in_per(phoneReq.getUserid());
			caseInfoModel.setAccept_time(phoneReq.getAccept_time());
			caseInfoModel.setReceiver_id(phoneReq.getAreaid());
			// 上传登记照片
			if (upload(case_id, phoneReq.getCase_code(), file,
					phoneReq.getFiletype()) > 0) {
				if (pMapper.firstInsertCase(caseInfoModel) > 0) {
					RdsJudicialCaseFeeModel caseFee = new RdsJudicialCaseFeeModel();
					caseFee.setCase_id(case_id);
					caseFee.setReal_sum(phoneReq.getCharge());
					caseFee.setStand_sum(phoneReq.getStandFee());
					caseFee.setDate(phoneReq.getAccept_time());
					caseFee.setId(casefee_id);
					caseFee.setRemark(phoneReq.getRemark());
					caseFee.setType(phoneReq.getIsfree());
					caseFee.setDiscount(phoneReq.getDiscount());
					// 3为登记状态 案例等级完成后销售经理确认回款信息是修改为正常或者异常状态。
					// if (phoneReq.getIsfree() == 0)
					caseFee.setStatus(3);
					// else
					// caseFee.setStatus(0);
					// 插入案例费用
					if (caseFeeMapper.insertCaseFee(caseFee) > 0) {
						return setMsg(true, "登记成功");
					} else
						return setMsg(false, "登记失败");
				} else
					return setMsg(false, "登记失败");
			} else
				return setMsg(false, "登记失败");
		} else
			return setMsg(false, "案例编号已经存在，登记失败");
	}

	@Override
	public Map<String, Object> getDaily(String userid) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<RdsJudicialFinanceDailyModel> list = dailyMapper.getDaily(userid);
		map.put("data", list.size() > 0 ? list : "");
		map.put("success", list.size() > 0 ? true : false);
		return map;
	}

	@Override
	public Map<String, Object> getMonthly(String userid) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<RdsJudicialFinanceDailyModel> list = monthlyMapper
				.getMonthly(userid);
		map.put("data", list.size() > 0 ? list : "");
		map.put("success", list.size() > 0 ? true : false);
		return map;
	}

	@Override
	public Map<String, Object> getAllManager() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = pMapper.getAllManager();
		map.put("data", list.size() > 0 ? list : "");
		map.put("success", list.size() > 0 ? true : false);
		return map;
	}

	@Override
	@Transactional
	public Map<String, Object> confirmReturn(
			RdsJudicialConfirmReturnModel returnModel) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("case_code", returnModel.getCase_code());
		if (pMapper.exsitCaseCode(params) == 0) {
			params.clear();
			params.put("success", false);
			params.put("msg", "案例编号不存在");
			return params;
		}

		double real_sum = returnModel.getReal_sum();
		double standFee = returnModel.getStandFee();
		double return_sum = returnModel.getReturn_sum();
		double discountrate = returnModel.getDiscountrate();

		if (real_sum == standFee && return_sum == real_sum * discountrate) {
			// 0为财务正常
			returnModel.setStatus(0);
		} else {
			// 1为财务异常
			returnModel.setStatus(1);
		}
		if (caseFeeMapper.updateCaseFee(returnModel) > 0) {
			params.put("success", true);
			params.put("msg", "确认成功！");
			return params;
		} else {
			params.put("success", false);
			params.put("msg", "确认失败，请稍后重新确认！");
			return params;
		}
	}

	@Override
	@Transactional
	public Map<String, Object> rimit(RdsJudicialRimitModel rimit) {
		Map<String, Object> params = new HashMap<String, Object>();
		rimit.setConfirm_time(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		params.put("id", rimit.getId());
		params.put("confirm_time",
				new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		if (dailyMapper.updateStatus(params) > 0) {
			params.put("success", true);
			params.put("msg", "汇款成功！");
		} else {
			params.put("success", false);
			params.put("msg", "汇款失败，请稍后重新确认！");
		}
		return params;
	}

	@Override
	@Transactional
	public Map<String, Object> confirmDaily(
			RdsJudicialFinanceDailyModel dailyModel) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", dailyModel.getId());
		map.put("confirm_time",
				new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		if (dailyMapper.updateStatus(map) > 0) {
			map.put("success", true);
			map.put("meg", "确认成功");
		} else {
			map.put("success", false);
			map.put("meg", "确认失败");
		}
		return map;
	}

	@Override
	@Transactional
	public Map<String, Object> confirmMonthly(
			RdsJudicialFinanceMonthlyModel monthlyModel) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (monthlyMapper.confirmStatus(monthlyModel.getId()) > 0) {
			map.put("success", true);
			map.put("meg", "确认成功");
		} else {
			map.put("success", false);
			map.put("meg", "确认失败");
		}
		return map;
	}

	@Override
	public Map<String, Object> getCaseStatus(String case_id) {
		List<RdsJudicialCaseStatusModel> statusModel = pMapper
				.getStatus(case_id);
		if (statusModel.size() > 0) {
			if (!"".equals(((RdsJudicialCaseStatusModel) statusModel.get(0))
					.getMail_code())
					&& ((RdsJudicialCaseStatusModel) statusModel)
							.getMail_code() != null) {
				return setMsg(true, "报告已寄出");
			} else if (!"".equals(((RdsJudicialCaseInfoModel) statusModel)
					.getPrint_count())
					&& ((RdsJudicialCaseInfoModel) statusModel)
							.getPrint_count() != 0) {
				return setMsg(true, "报告打印中");
			} else if (!"".equals(((RdsJudicialCaseStatusModel) statusModel)
					.getVerify_sampleinfo_state())
					&& ((RdsJudicialCaseStatusModel) statusModel)
							.getVerify_sampleinfo_state() != null) {
				return setMsg(true, "案例实验中");
			} else {
				return setMsg(true, "样本寄送中");
			}
		} else {
			return setMsg(false, "获取状态失败");
		}

	}

	@Override
	public Map<String, Object> getDailyDetail(String id) {
		List<RdsJudicialDailyDetailModel> list = pMapper.getDailyDetail(id);
		Map<String, Object> map = new HashMap<String, Object>();
		if (list.size() < 1) {
			map.put("data", "");
			map.put("success", false);
		} else {
			map.put("data", list);
			map.put("success", true);
		}
		return map;
	}

	@Override
	public Map<String, Object> getMonthlyDetail(String id) {
		List<RdsJudicialDailyDetailModel> list = pMapper.getMonthlyDetail(id);
		Map<String, Object> map = new HashMap<String, Object>();
		if (list.size() < 1) {
			map.put("data", "");
			map.put("success", false);
		} else {
			map.put("data", list);
			map.put("success", true);
		}
		return map;
	}

	@Override
	public Map<String, Object> getUnConfirm(String userid) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<RdsJudicialConfirmReturnModel> list = pMapper.getUnConfirm(userid);
		if (list.size() > 0) {
			map.put("data", list);
			map.put("success", true);
		} else {
			map.put("data", "");
			map.put("success", false);
		}
		return map;
	}

	@Override
	public Map<String, Object> getCaseStatesList(Map<String, Object> params) {
		List<Object> list = new ArrayList<Object>();
		try {
			list = rdsJudicialCaseStateMapper.queryAllPage(params);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (list.isEmpty()) {
			Map<String, Object> map = setMsg(false, "获取案例列表失败");
			map.put("list", "");
			map.put("num", 0);
			return map;
		} else {
			int num=0;
			try {
				num = rdsJudicialCaseStateMapper.queryAllCount(params);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			Map<String, Object> caseList = new HashMap<String, Object>();
//			List<String> accountlist = null;
//			Set<String> set = new HashSet<String>();
//			Iterator<RdsJudicialCaseStateInfoModel> listit = list.iterator();
//			while (listit.hasNext()) {
//				set.add(listit.next().getBankname());
//			}
//			int banknum = set.size();
//			Iterator<String> setit = set.iterator();
//			while (setit.hasNext()) {
//				String bankname = setit.next();
//				accountlist = new ArrayList<String>();
//				listit = list.iterator();
//				while (listit.hasNext()) {
//					RdsJudicialCaseStateInfoModel bank = listit.next();
//
//					if (bank.getBankname().equals(bankname)) {
//
//						bankaccountlist.add(bank.getBankaccount());
//					}
//				}
//				caseList.put(bankname, bankaccountlist);
//			}
			Map<String, Object> map = setMsg(true, "OK");
			map.put("num", num);
			map.put("list", list);
			return map;
		}
	}

	@Override
	public Map<String, Object> getCaseStateById(Map<String, String> params) {
		 List<RdsJudicialCaseStateModel> list = this.queryList(params);
		if (list.isEmpty()) {
			Map<String, Object> map = setMsg(false, "获取案例列表失败");
			map.put("list", "");
			return map;
		} else {
			Map<String, Object> map = setMsg(true, "OK");
			map.put("list", list);
			return map;
		}
	}
	
	private List<RdsJudicialCaseStateModel> queryList(Map<String, String> map){
		List<RdsJudicialCaseStatusModel> csmodel;
		RdsJudicialCaseStateModel row;
		List<RdsJudicialCaseStateModel> rows = new ArrayList<>();
		try {
			
			//查询数据库
			csmodel = pMapper.getCaseStatueByIdnumber(map);
			if (csmodel.size()==0) {
				row = new RdsJudicialCaseStateModel();
			} else {
				for (RdsJudicialCaseStatusModel smodel : csmodel) {
					if (StringUtils.isNotBlank(smodel.getAccept_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getAccept_time());
						row.setProcessstatus("案例受理中");
						rows.add(row);
					}
					if (StringUtils.isNotBlank(smodel.getSample_in_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getSample_in_time());
						row.setProcessstatus("案例信息审核中");
						rows.add(row);
					}
					if (StringUtils.isNotBlank(smodel
							.getVerify_baseinfo_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getVerify_baseinfo_time());
						row.setProcessstatus("样本审核中");
						rows.add(row);
					}
					if (StringUtils.isNotBlank(smodel
							.getVerify_sampleinfo_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getVerify_sampleinfo_time());
						row.setProcessstatus("实验中");
						rows.add(row);
					}
					if (StringUtils.isNotBlank(smodel.getTrans_date())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getTrans_date());
						row.setProcessstatus("报告打印中");
						rows.add(row);
					}
					if (StringUtils.isNotBlank(smodel.getClose_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getClose_time());
						row.setProcessstatus("等待邮寄");
						rows.add(row);
					}
					if (StringUtils.isNotBlank(smodel.getMail_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getMail_time());
						row.setProcessstatus("已邮寄报告");
						rows.add(row);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return rows;
	}

	@Override
	public String getProcessInstanceId(String id_number) {
		
		return pMapper.getProcessInstanceId(id_number);
	}


	@Override
	public List<RdsJudicialMailInfoModel> getMail(String id_number) {
		
		return pMapper.getMail(id_number);
	}
}
