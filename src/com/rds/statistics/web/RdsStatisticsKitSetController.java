package com.rds.statistics.web;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.statistics.mapper.RdsFinanceConfigMapper;
import com.rds.statistics.service.RdsStatisticsKitSetService;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * @author yxb
 * @className
 * @description
 * @date 2018/03/22
 */
@Controller
@RequestMapping("statistics/kitSet")
public class RdsStatisticsKitSetController {

	@Autowired
	private RdsStatisticsKitSetService rdsStatisticsKitSetService;

	@Autowired
	private RdsFinanceConfigMapper rdsFinanceConfigMapper;

	@RequestMapping("queryAllPage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
		String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
				.get(0).get("userlevel").toString()):"";
		if(!"1".equals(level))
			params.put("userid", user.getUserid());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsStatisticsKitSetService.queryAllPage(params));
		map.put("total", rdsStatisticsKitSetService.queryAllCount(params));
		return map;
	}

	@RequestMapping("insertKitSet")
	@ResponseBody
	public Object insertKitSet(@RequestBody Map<String, Object> params,
			HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if (null == user)
			return false;
		try {
			params.put("create_per", user.getUserid());
			if (null == params.get("kit_id") || "".equals(params.get("kit_id"))) {
				params.put("kit_id", UUIDUtil.getUUID());
				if (rdsStatisticsKitSetService.insertKitSet(params)) {
					result.put("success", true);
					result.put("msg", "操作成功");
				} else {
					result.put("success", false);
					result.put("msg", "操作失败，请稍后重试或联系管理员！");
				}
			} else {
				if (rdsStatisticsKitSetService.updateKitSet(params)) {
					result.put("success", true);
					result.put("msg", "操作成功");
				} else {
					result.put("success", false);
					result.put("msg", "操作失败，请稍后重试或联系管理员！");
				}
			}
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "操作异常，请稍后重试或联系管理员！");
			e.printStackTrace();
		}

		return result;
	}

	@RequestMapping("comfirmKitSet")
	@ResponseBody
	public Object comfirmKitSet(@RequestBody Map<String, Object> params,
			HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if (null == user)
			return false;
		try {
			List<String> list = new ArrayList<>();
			String []ids = params.get("ids").toString().split(",");
			for (String string : ids) {
				list.add(string);
			}
			params.put("list", list);
			params.put("confirm_per", user.getUserid());
			if (rdsStatisticsKitSetService.comfirmKitSet(params)) {
				result.put("success", true);
				result.put("msg", "操作成功");
			} else {
				result.put("success", false);
				result.put("msg", "操作失败，请稍后重试或联系管理员！");
			}
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "操作异常，请稍后重试或联系管理员！");
			e.printStackTrace();
		}

		return result;
	}

	@RequestMapping("deleteKitSet")
	@ResponseBody
	public Object deleteKitSet(@RequestBody Map<String, Object> params,
			HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if (null == user)
			return false;
		try {
			List<String> list = new ArrayList<>();
			String []ids = params.get("ids").toString().split(",");
			for (String string : ids) {
				list.add(string);
			}
			params.put("list", list);
			params.put("create_per", user.getUserid());
			if (rdsStatisticsKitSetService.deleteKitSet(params)) {
				result.put("success", true);
				result.put("msg", "操作成功");
			} else {
				result.put("success", false);
				result.put("msg", "操作失败，请稍后重试或联系管理员！");
			}
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "操作异常，请稍后重试或联系管理员！");
			e.printStackTrace();
		}

		return result;
	}
	
	@RequestMapping("queryDeptList")
	@ResponseBody
	public Object queryDeptList(@RequestBody Map<String, Object> params) {
		List<Map<String, Object>> list = new ArrayList<>();
		List<String> lists = rdsStatisticsKitSetService.queryDeptList();
		for (String string : lists) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptname", string);
			list.add(map);
		}
		return list;
	}

	@RequestMapping("exportKit")
	public void exportKit(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if (null != user) {
			Map<String, Object> params = new HashMap<String, Object>();
			String kit_name = request.getParameter("kit_name");
			params.put("kit_name", kit_name);
			String kit_bathc_num = request.getParameter("kit_bathc_num");
			params.put("kit_bathc_num", kit_bathc_num);
			String kit_express_num = request.getParameter("kit_express_num");
			params.put("kit_express_num", kit_express_num);
			String kit_receive_per = request.getParameter("kit_receive_per");
			params.put("kit_receive_per", kit_receive_per);
			String user_dept_level1 = request.getParameter("user_dept_level1");
			params.put("user_dept_level1", user_dept_level1);
			String apply_per = request.getParameter("apply_per");
			params.put("apply_per", apply_per);
			String apply_num = request.getParameter("apply_num");
			params.put("apply_num", apply_num);
			String confirm_pername = request.getParameter("confirm_pername");
			params.put("confirm_pername", confirm_pername);
			String confirm_state = request.getParameter("confirm_state");
			params.put("confirm_state", confirm_state);
			String is_delete = request.getParameter("is_delete");
			params.put("is_delete", is_delete);
			String create_date_start = request.getParameter("create_date_start");
			params.put("create_date_start", create_date_start);
			String create_date_end = request.getParameter("create_date_end");
			params.put("create_date_end", create_date_end);
			String create_pername = request.getParameter("create_pername");
			params.put("create_pername", create_pername);
			
			String userCode = "";
			if (session.getAttribute("user") != null) {
				userCode = user.getUsercode();
			}
			
			if (!"".equals(userCode)) {
				List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
				String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
						.get(0).get("userlevel").toString()):"";
				if(!"1".equals(level))
					params.put("userid", user.getUserid());
				rdsStatisticsKitSetService.exportKit(params, response);
			}
		}
	}
	
}
