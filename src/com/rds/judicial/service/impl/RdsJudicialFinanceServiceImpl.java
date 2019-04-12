package com.rds.judicial.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.SimpleDateFormat;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialBillMapper;
import com.rds.judicial.mapper.RdsJudicialCaseFeeMapper;
import com.rds.judicial.mapper.RdsJudicialFinanceDailyMapper;
import com.rds.judicial.mapper.RdsJudicialFinanceMonthlyMapper;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.mapper.RdsJudicialReturnFeeMapper;
import com.rds.judicial.model.RdsJudicialDailyDetailModel;
import com.rds.judicial.model.RdsJudicialFinanceDailyModel;
import com.rds.judicial.model.RdsJudicialFinanceReturnModel;
import com.rds.judicial.model.RdsJudicialFinanceVerifyModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialReturnFeeModel;
import com.rds.judicial.service.RdsJudicialFinanceService;
import com.rds.upc.mapper.RdsUpcUserMapper;

/**
 * @description 财务管理
 * @author ThinK 2015年4月22日
 */
@Service("RdsJudicialFinanceService")
@Transactional
public class RdsJudicialFinanceServiceImpl implements RdsJudicialFinanceService {

	@Setter
	@Autowired
	private RdsJudicialCaseFeeMapper caseFeeMapper;

	@Setter
	@Autowired
	private RdsJudicialBillMapper billMapper;

	@Setter
	@Autowired
	private RdsJudicialFinanceDailyMapper dailyMapper;

	@Setter
	@Autowired
	private RdsJudicialRegisterMapper registerMapper;

	@Setter
	@Autowired
	private RdsJudicialFinanceMonthlyMapper monthlyMapper;

	@Setter
	@Autowired
	private RdsJudicialReturnFeeMapper returnFeeMapper;

	@Setter
	@Autowired
	private RdsUpcUserMapper rdsUpcUserMapper;

	/**
	 * 生成日报
	 */
	@Override
	@Transactional
	public void generateFinanceDaily() {
		// 获取销售经理列表
		List<String> useridlist = dailyMapper.getManagerList();
		List<Map<String, Object>> feelist;
		RdsJudicialFinanceDailyModel dailyModel;
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		double sum = 0;
		for (String userid : useridlist) {
			Map<String, Object> mapTemp = new HashMap<String, Object>();
			mapTemp.put("userid", userid);
			// String telphone = "";
			// String username = "";
			// try {
			// List<Object> list = rdsUpcUserMapper.queryAll(mapTemp);
			// RdsUpcUserModel user = (RdsUpcUserModel) list.get(0);
			// telphone = user.getTelphone();
			// username = user.getUsername();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			feelist = dailyMapper.getCase4Daily(userid);
			if (feelist.size() > 0) {
				dailyModel = new RdsJudicialFinanceDailyModel();
				// 日期
				dailyModel.setDaily_time(date);
				// id
				String id = UUIDUtil.getUUID();
				dailyModel.setId(id);
				// 销售经理id
				dailyModel.setUserid(userid);
				// dailyModel.setBalancetype(balancetype);;
				// 日报类型 1 为亲子鉴定
				dailyModel.setType(1);
				for (Map<String, Object> map : feelist) {
					map.put("id", id);
					map.put("status", 2);
					dailyMapper.updateFeeStatus(map);
					// sum = sum
					// + ((BigDecimal) map.get("return_sum"))
					// .doubleValue();
					sum = sum
							+ Double.valueOf(map.get("real_sum").toString())
									.doubleValue();
				}
				// 日报金额
				dailyModel.setSum(sum);
				dailyMapper.insertDaily(dailyModel);
				// if(!"".equals(telphone))
				// {
				// CRMessageUtils send = new CRMessageUtils();
				// try {
				// send.sendMessage(username+":您"+date+"案例生成日报金额为："+sum,
				// telphone);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				sum = 0;

			}
		}

		// 合同计划日报
		List<String> contractUseridlist = dailyMapper.queryContractUser();
		List<Map<String, Object>> contractFeelist;
		RdsJudicialFinanceDailyModel contractDailyModel;
		String contractDate = new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date());
		double remittance = 0;
		for (String contract_userid : contractUseridlist) {
			Map<String, Object> mapTemp = new HashMap<String, Object>();
			mapTemp.put("userid", contract_userid);
			// String telphone = "";
			// String username = "";
			// try {
			// List<Object> list = rdsUpcUserMapper.queryAll(mapTemp);
			// RdsUpcUserModel user = (RdsUpcUserModel) list.get(0);
			// telphone = user.getTelphone();
			// username = user.getUsername();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			contractFeelist = dailyMapper.queryContractDaily(contract_userid);
			if (contractFeelist.size() > 0) {
				contractDailyModel = new RdsJudicialFinanceDailyModel();
				// 日期
				contractDailyModel.setDaily_time(contractDate);
				// id
				String id = UUIDUtil.getUUID();
				contractDailyModel.setId(id);
				// 销售经理id
				contractDailyModel.setUserid(contract_userid);
				// 日报类型 2合同计划
				contractDailyModel.setType(2);
				for (Map<String, Object> map : contractFeelist) {
					map.put("id", id);
					map.put("status", 2);
					dailyMapper.updateContractStatus(map);
					remittance = remittance
							+ Double.valueOf(map.get("remittance").toString())
									.doubleValue();
				}
				// 日报金额
				contractDailyModel.setSum(remittance);
				dailyMapper.insertDaily(contractDailyModel);
				// if (!"".equals(telphone)) {
				// CRMessageUtils send = new CRMessageUtils();
				// try {
				// send.sendMessage(username+":您"+date+"案例生成日报金额为："+sum,
				// telphone);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				sum = 0;
			}
		}

	}

	/**
	 * 生成月报
	 */
	@Override
	@Transactional
	public void generateFinanceMonthly() {
		// 获取销售人员列表,根据激活码上的结算日期生成月报信息
		List<String> useridlist = monthlyMapper.getManagerListMonthly();
		// 需要结算的财务清单
		List<Map<String, Object>> feelist;
		// 月结算报表
		RdsJudicialFinanceDailyModel dailyModel;
		// 月报生成日期
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		// 月报金额
		double sum = 0;
		// 遍历销售经理
		for (String userid : useridlist) {
			Map<String, Object> mapTemp = new HashMap<String, Object>();
			mapTemp.put("userid", userid);
			// String telphone = "";
			// String username = "";
			// try {
			// List<Object> list = rdsUpcUserMapper.queryAll(mapTemp);
			// RdsUpcUserModel user = (RdsUpcUserModel) list.get(0);
			// telphone = user.getTelphone();
			// username = user.getUsername();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			feelist = monthlyMapper.getCase4Monthly(userid);
			if (feelist.size() > 0) {
				dailyModel = new RdsJudicialFinanceDailyModel();
				String id = UUIDUtil.getUUID();
				dailyModel.setId(id);
				dailyModel.setDaily_time(date);
				// 1 表示亲子鉴定
				dailyModel.setType(1);
				dailyModel.setUserid(userid);
				for (Map<String, Object> map : feelist) {
					map.put("id", id);
					map.put("status", 4);
					dailyMapper.updateFeeStatus(map);
					// sum = sum
					// + ((BigDecimal) map.get("stand_sum")).doubleValue();
					if (map.get("real_sum") == null
							|| map.get("real_sum") == "")
						sum = 0;
					else
						sum = sum
								+ Double.valueOf(map.get("real_sum").toString())
										.doubleValue();
				}
				// 日报金额
				dailyModel.setSum(sum);
				dailyModel.setBalancetype(1);
				monthlyMapper.insertMonthly(dailyModel);
				// if (!"".equals(telphone)) {
				// CRMessageUtils send = new CRMessageUtils();
				// try {
				// send.sendMessage(username+":您"+date+"案例生成月报金额为："+sum,
				// telphone);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				sum = 0;
			}
		}

		// 将过期的激活码置为失效
		try {
			dailyMapper.updateUserState();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Map<String, Object> queryVerify(Map<String, Object> map) {
		List<RdsJudicialFinanceVerifyModel> list = caseFeeMapper
				.queryVerify(map);
		int count = caseFeeMapper.queryVerifyCount();
		map.clear();
		map.put("data", list);
		map.put("total", count);
		return map;
	}

	@Override
	public Map<String, Object> updatestatus(Map<String, Object> map) {
		if (caseFeeMapper.updatestatus(map) > 0) {
			map.clear();
			map.put("result", true);
			map.put("message", "审核通过！");
			return map;
		} else {
			map.clear();
			map.put("result", false);
			map.put("message", "审核失败，请联系管理员！");
			return map;
		}
	}

	@Override
	public Map<String, Object> queryAllSum(Map<String, Object> map) {
		List<RdsJudicialFinanceReturnModel> list = dailyMapper.getAllSum(map);
		map.clear();
		map.put("data", list);
		return map;
	}

	/**
	 * 根据用户查询该用户的所有打款回款信息
	 */
	@Override
	public Map<String, Object> queryDetail(Map<String, Object> map) {
		List<Map<String, Object>> list = dailyMapper.getDetailByUser(map);
		map.clear();
		map.put("data", list);
		return map;
	}

	@Override
	public Map<String, Object> getAllMonthly(Map<String, Object> params) {
		List<Map<String, Object>> list = monthlyMapper.getAllMonthly(params);
		params.clear();
		params.put("data", list);
		params.put("total", monthlyMapper.getAllMonthlyCount(params));
		return params;
	}

	/**
	 * 保存月报审核
	 */
	@Override
	public Map<String, Object> saveMonthlyVerfiy(Map<String, Object> params) {
		double sum = Double.parseDouble((String) params.get("sum"));
		double discountsum = Double.parseDouble((String) params
				.get("discountsum"));
		sum -= discountsum;
		params.put("sum", sum);
		// 从登记状态到 0未汇款状态
		params.put("status", 0);
		if (monthlyMapper.updateStatus(params) > 0) {
			params.clear();
			params.put("result", true);
			params.put("message", "审核成功！");
			return params;
		} else {
			params.clear();
			params.put("result", false);
			params.put("message", "审核失败！");
			return params;
		}
	}

	/**
	 * 根据余额判断是否修改案例财务状态 如果余额大于0 修改所有相关日报月报付款状态 如果小于0 修改款项已经齐全的日报月报付款状态
	 */
	@Override
	@Transactional
	public Map<String, Object> saveReturnSum(Map<String, Object> params) {
		RdsJudicialReturnFeeModel returnFee = new RdsJudicialReturnFeeModel();
		returnFee.setId(UUIDUtil.getUUID());
		returnFee.setReturn_time(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		returnFee.setBankaccount((String) params.get("bankaccount"));
		returnFee.setSum(Double.parseDouble((String) params.get("sum")));
		returnFee.setRemark((String) params.get("remark"));
		returnFee.setUserid((String) params.get("userid"));
		try {
			List<RdsJudicialFinanceDailyModel> dailyList = dailyMapper
					.getUnPay(params);
			Double balance = dailyMapper.getBalance(params);
			balance = (balance == null ? 0 : balance) + returnFee.getSum();
			double tempsum = 0;
			for (int i = 0; dailyList.size() > i; i++) {
				tempsum += dailyList.get(i).getSum();
				if (balance - tempsum >= 0) {
					params.put("id", dailyList.get(i).getId());
					dailyMapper.updateFee(params);
					// dailyMapper.updateCaseGatherid(params);
				}
			}
			returnFeeMapper.insertReturnFee(returnFee);
			params.clear();
			params.put("result", true);
			params.put("message", "收款成功。");
			return params;
		} catch (Exception e) {
			params.clear();
			params.put("result", false);
			params.put("message", "收款失败，请稍后尝试。");
			return params;
		}
	}

	// 日报确认
	@Override
	public RdsJudicialResponse getFinanceDaily(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialFinanceDailyModel> financeDailyModels = dailyMapper
				.getFinanceDaily(params);
		int count = dailyMapper.countFinanceDaily(params);
		response.setCount(count);
		response.setItems(financeDailyModels);
		return response;
	}

	@Override
	public boolean confirmFinanceDaily(Map<String, Object> params) {
		int count = dailyMapper.confirmFinanceDaily(params);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Object> queryDailyDetail(Map<String, Object> params) {
		List<RdsJudicialDailyDetailModel> list = dailyMapper
				.queryDailyDetail(params);
		int count = dailyMapper.queryDailyDetailCount(params);
		params.clear();
		params.put("data", list);
		params.put("total", count);
		return params;
	}

	@Override
	public int createFinanceDaily(String case_id) {
		List<Map<String, Object>> feelist = dailyMapper
				.getCase4DailyById(case_id);
		double sum = 0;
		RdsJudicialFinanceDailyModel dailyModel;
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if (feelist.size() > 0) {
			String userid = feelist.get(0).get("case_userid").toString();
			dailyModel = new RdsJudicialFinanceDailyModel();
			// 日期
			dailyModel.setDaily_time(date);
			// id
			String id = UUIDUtil.getUUID();
			dailyModel.setId(id);
			// 销售经理id
			dailyModel.setUserid(userid);
			// dailyModel.setBalancetype(balancetype);
			// 日报类型 1 为亲子鉴定
			dailyModel.setType(1);
			for (Map<String, Object> map : feelist) {
				map.put("id", id);
				map.put("status", 2);
				dailyMapper.updateFeeStatus(map);
				sum = sum
						+ Double.valueOf(map.get("real_sum").toString())
								.doubleValue();
			}
			// 日报金额
			dailyModel.setSum(sum);
			dailyMapper.insertDaily(dailyModel);
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void inversiveFinanceDaily() {
		// 获取销售经理列表
		List<String> useridlist = dailyMapper.getInversiveManagerList();
		List<Map<String, Object>> feelist;
		RdsJudicialFinanceDailyModel dailyModel;
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		double sum = 0;
		for (String userid : useridlist) {
			Map<String, Object> mapTemp = new HashMap<String, Object>();
			mapTemp.put("userid", userid);
			// String telphone = "";
			// String username = "";
			// try {
			// List<Object> list = rdsUpcUserMapper.queryAll(mapTemp);
			// RdsUpcUserModel user = (RdsUpcUserModel) list.get(0);
			// telphone = user.getTelphone();
			// username = user.getUsername();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			feelist = dailyMapper.getInversiveCase4Daily(userid);
			if (feelist.size() > 0) {
				dailyModel = new RdsJudicialFinanceDailyModel();
				// 日期
				dailyModel.setDaily_time(date);
				// id
				String id = UUIDUtil.getUUID();
				dailyModel.setId(id);
				// 销售经理id
				dailyModel.setUserid(userid);
				// dailyModel.setBalancetype(balancetype);;
				// 日报类型 3 为无创产前
				dailyModel.setType(3);
				for (Map<String, Object> map : feelist) {
					map.put("id", id);
					map.put("status", 2);
					dailyMapper.updateFeeStatus(map);
					// sum = sum
					// + ((BigDecimal) map.get("return_sum"))
					// .doubleValue();
					sum = sum
							+ Double.valueOf(map.get("real_sum").toString())
									.doubleValue();
				}
				// 日报金额
				dailyModel.setSum(sum);
				dailyMapper.insertDaily(dailyModel);
				// if(!"".equals(telphone))
				// {
				// CRMessageUtils send = new CRMessageUtils();
				// try {
				// send.sendMessage(username+":您"+date+"案例生成日报金额为："+sum,
				// telphone);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				sum = 0;

			}
		}
	}

	@Override
	public int createPreFinanceDaily(String case_id) {
		List<Map<String, Object>> feelist = dailyMapper
				.getPreCase4DailyById(case_id);
		double sum = 0;
		RdsJudicialFinanceDailyModel dailyModel;
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if (feelist.size() > 0) {
			String userid = feelist.get(0).get("ownperson").toString();
			dailyModel = new RdsJudicialFinanceDailyModel();
			// 日期
			dailyModel.setDaily_time(date);
			// id
			String id = UUIDUtil.getUUID();
			dailyModel.setId(id);
			// 销售经理id
			dailyModel.setUserid(userid);
			// dailyModel.setBalancetype(balancetype);
			// 日报类型 3 为无创产前
			dailyModel.setType(3);
			for (Map<String, Object> map : feelist) {
				map.put("id", id);
				map.put("status", 2);
				dailyMapper.updateFeeStatus(map);
				sum = sum
						+ Double.valueOf(map.get("real_sum").toString())
								.doubleValue();
			}
			// 日报金额
			dailyModel.setSum(sum);
			dailyMapper.insertDaily(dailyModel);
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int createChildrenFinanceDaily(String case_id) {
		List<Map<String, Object>> feelist = dailyMapper
				.getChildrenCase4DailyById(case_id);
		double sum = 0;
		RdsJudicialFinanceDailyModel dailyModel;
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if (feelist.size() > 0) {
			String userid = feelist.get(0).get("case_userid").toString();
			dailyModel = new RdsJudicialFinanceDailyModel();
			// 日期
			dailyModel.setDaily_time(date);
			// id
			String id = UUIDUtil.getUUID();
			dailyModel.setId(id);
			// 销售经理id
			dailyModel.setUserid(userid);
			// dailyModel.setBalancetype(balancetype);
			// 日报类型 4 为儿童基因库
			dailyModel.setType(4);
			for (Map<String, Object> map : feelist) {
				map.put("id", id);
				map.put("status", 2);
				dailyMapper.updateFeeStatus(map);
				sum = sum
						+ Double.valueOf(map.get("real_sum").toString())
								.doubleValue();
			}
			// 日报金额
			dailyModel.setSum(sum);
			dailyMapper.insertDaily(dailyModel);
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void childrenFinanceDaily() {
		// 获取销售经理列表
		List<String> useridlist = dailyMapper.getChildrenManagerList();
		List<Map<String, Object>> feelist;
		RdsJudicialFinanceDailyModel dailyModel;
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		double sum = 0;
		for (String userid : useridlist) {
			Map<String, Object> mapTemp = new HashMap<String, Object>();
			mapTemp.put("userid", userid);
			feelist = dailyMapper.getChildrenCase4Daily(userid);
			if (feelist.size() > 0) {
				dailyModel = new RdsJudicialFinanceDailyModel();
				// 日期
				dailyModel.setDaily_time(date);
				// id
				String id = UUIDUtil.getUUID();
				dailyModel.setId(id);
				// 销售经理id
				dailyModel.setUserid(userid);
				// dailyModel.setBalancetype(balancetype);;
				// 日报类型 4 为儿童基因库
				dailyModel.setType(4);
				for (Map<String, Object> map : feelist) {
					map.put("id", id);
					map.put("status", 2);
					dailyMapper.updateFeeStatus(map);
					sum = sum
							+ Double.valueOf(map.get("real_sum").toString())
									.doubleValue();
				}
				// 日报金额
				dailyModel.setSum(sum);
				dailyMapper.insertDaily(dailyModel);
				sum = 0;

			}
		}
	}

	@Override
	public int createContractFinanceDaily(String contract_id) {
		try{
			List<Map<String, Object>> feelist = dailyMapper
					.queryContractDailyById(contract_id);
			if(feelist.size()<=0){
				return 0;
			}
			// 日报id
			String id = UUIDUtil.getUUID();
			RdsJudicialFinanceDailyModel contractDailyModel = new RdsJudicialFinanceDailyModel();
			String contractDate = new SimpleDateFormat("yyyy-MM-dd")
					.format(new Date());
			double remittance = 0;
			String userid = "";
			for (Map<String, Object> map : feelist) {
				map.put("id", id);
				map.put("status", 2);
				map.put("contract_remittance_planid",
						map.get("contract_remittance_planid").toString());
				dailyMapper.updateContractStatus(map);
				userid = map.get("contract_userid").toString();
				// 日报金额
				remittance += Double.parseDouble(map.get("remittance").toString());
			}
			// 日期
			contractDailyModel.setDaily_time(contractDate);
			contractDailyModel.setId(id);
			// 销售经理id
			contractDailyModel.setUserid(userid);
			// 日报类型 2合同计划
			contractDailyModel.setType(2);
			contractDailyModel.setSum(remittance);
			dailyMapper.insertDaily(contractDailyModel);
			return 1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void exportCaseFinance(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "案例财务信息表";
		List<RdsJudicialDailyDetailModel> list = dailyMapper
				.queryDailyDetail(params);
		Object[] titles = { "案例编号","案例归属人","案例归属地","受理时间", "日报时间","案例委托人","汇款单号", "标准金额","优惠金额","应收金额", "回款金额",
				"财务备注","财务确认状态", "财务确认时间", "结算类型", "日报类型", "案例状态", "案例样本信息", "是否紧急"};
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsJudicialDailyDetailModel model = list.get(i);
			objects.add(model.getCase_code());
			objects.add(model.getCase_receiver());
			objects.add(model.getReceiver_area());
			objects.add(model.getDate());
			objects.add(model.getDaily_time());
			objects.add(model.getClient());
			objects.add(model.getRemittance_num());
			objects.add(model.getStand_sum());
			objects.add(model.getDiscountPrice());
			objects.add(model.getReal_sum());
			objects.add(model.getReturn_sum());
			objects.add(model.getFinance_remark());
			int confirm_state = model.getConfirm_state();
			switch (confirm_state) {
			case -1:
				objects.add("未确认汇款");
				break;
			case 1:
				objects.add("确认汇款通过");
				break;
			case 2:
				objects.add("确认汇款不通过");
				break;
			default:
				objects.add("未回款");
				break;
			}
			objects.add(model.getConfirm_date());
			objects.add(model.getType()==4?"月结":"日结");
			int daily_type = model.getDaily_type();
			switch (daily_type) {
				case 1:
					objects.add("亲子鉴定");
					break;
				case 2:
					objects.add("合同计划");
					break;
				case 3:
					objects.add("无创产前");
					break;
				case 4:
					objects.add("儿童基因库");
					break;
				default:
					objects.add("亲子鉴定");
			}
			int case_state = model.getCase_state();
			switch (case_state) {
			case 0:
				objects.add("正常");
				break;
			case 1:
				objects.add("先出报告后付款");
				break;
			case 2:
				objects.add("免单");
				break;
			case 3:
				objects.add("优惠");
				break;
			case 4:
				objects.add("月结");
				break;
			case 5:
				objects.add("二次采样");
				break;
			case 6:
				objects.add("补样");
				break;
			case 7:
				objects.add("加报告");
				break;
			case 8:
				objects.add("改报告");
				break;
			default:
				objects.add("");
			}
			objects.add(model.getSample_str());
			int urgent_state = model.getUrgent_state();
			switch (urgent_state) {
			case 1:case 2:case 3:
				objects.add("是");
				break;
			default:
				objects.add("否");
			}
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "案例财务信息表");
	
	}

}
