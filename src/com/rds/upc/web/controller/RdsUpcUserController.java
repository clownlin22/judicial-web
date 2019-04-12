package com.rds.upc.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.upc.model.RdsUpcDepartmentModel;
import com.rds.upc.model.RdsUpcTokenModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.service.RdsUpcDepartmentService;
import com.rds.upc.service.RdsUpcUserService;
import com.rds.upc.web.common.MD5Util;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;
import com.rds.upc.web.common.SessionOperation;

/**
 * 
 * @ClassName: RdsUpcUserController
 * @Description: 人员管理Controller
 * @author yxb
 * @date 2015年4月20日
 *
 */

@Controller
public class RdsUpcUserController extends RdsUpcAbstractController {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String USER_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "user_permit");

	private static final String USER_PASSWORD = PropertiesUtils.readValue(
			FILE_PATH, "user_password");

	@Setter
	@Autowired
	private RdsUpcUserService rdsUpcUserService;

	@Setter
	@Autowired
	private RdsUpcDepartmentService rdsUpcDepartmentService;

	/**
	 * 分页查询人员信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("upc/user/queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params = this.pageSet(params);
		if (!USER_PERMIT.contains(user.getUsercode()))
			params.put("companyid", user.getCompanyid());
		return rdsUpcUserService.queryAllPage(params);
	}

	/**
	 * 查询人员信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("upc/user/queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {

		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (!USER_PERMIT.contains(user.getUsercode()))
			params.put("companyid", user.getCompanyid());
		return rdsUpcUserService.queryAll(params);
	}

	/**
	 * 根据人员id删除人员
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("upc/user/delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsUpcUserService.delete(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}

	}

	/**
	 * 根据用户id新增和更新人员信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("upc/user/save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params) {
		Object userid = params.get("userid");
		if (userid == null || userid.equals("")) {
			return insert(params);
		} else {
			return update(params);
		}
	}

	/**
	 * 保存用户部门信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("upc/user/saveDept")
	@ResponseBody
	public Object saveDept(@RequestBody Map<String, Object> params) {
		// 获取部门编号
		try {
			RdsUpcDepartmentModel depart = (RdsUpcDepartmentModel) rdsUpcDepartmentService
					.queryModel(params);
			params.put("deptcode", depart.getDeptcode());
			rdsUpcUserService.updateDept(params);
			return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, RdsUpcConstantUtil.EXCEPTION);
		}
	}

	/**
	 * 保存配置人员角色
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("upc/user/saveRole")
	@ResponseBody
	public Object saveRole(@RequestBody Map<String, Object> params) {
		int result = rdsUpcUserService.updateRole(params);
		if (result > 0) {
			return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
		} else {
			return this.setModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
		}
	}

	/**
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("upc/user/saveUserReport")
	@ResponseBody
	public Object saveUserReport(@RequestBody Map<String, Object> params) {
		String typeids = params.get("typeids") == null ? "" : params.get(
				"typeids").toString();
		String[] typeid = typeids.split(",");
		int result = 0;
		for (int i = 0; i < typeid.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", RdsUpcUUIDUtil.getUUID());
			map.put("usercode", params.get("usercode"));
			map.put("typeid", typeid[i]);
			result = rdsUpcUserService.saveUserReport(map);
		}
		if (result > 0) {
			return this.setModel(true, RdsUpcConstantUtil.SAVE_SUCCESS);
		} else {
			return this.setModel(false, RdsUpcConstantUtil.SAVE_FAILED);
		}
	}

	/**
	 * 查询角色信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("upc/user/queryRoleType")
	@ResponseBody
	public Object queryRoleType(@RequestBody Map<String, Object> params) {
		return rdsUpcUserService.queryRoleType();
	}

	/**
	 * 新增用户
	 * 
	 * @param params
	 * @return
	 */
	private Object insert(Map<String, Object> params) {
		try {
			String usercode = params.get("usercode") == null ? "" : params.get(
					"usercode").toString();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("usercode", usercode.trim());
			int flag = rdsUpcUserService.queryAllCount(map);
			// 判断是否有用户名存在
			if (flag > 0) {
				return this.setModel(false, RdsUpcConstantUtil.REPART_MSG);
			}
			params.put("userid", RdsUpcUUIDUtil.getUUID());
			params.put("relation_id", RdsUpcUUIDUtil.getUUID());
			params.put("password", MD5Util.string2MD5(USER_PASSWORD));
			// 名字中文简称
			String initials = StringUtils.getInitials(params.get("username")
					.toString());
			params.put("initials", initials);
			// 插入用户信息
			int result = rdsUpcUserService.insert(params);
			// 判断插入状态
			if (result > 0) {
				return this.setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.INSERT_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
					+ "\n" + e.getMessage());
		}
	}

	/**
	 * 更新用户
	 * 
	 * @param params
	 * @return
	 */
	private Object update(Map<String, Object> params) {
		try {
			// 获取用户code
			String usercode = params.get("usercode") == null ? "" : params.get(
					"usercode").toString();
			// 获取用户id
			String userid = params.get("userid") == null ? "" : params.get(
					"userid").toString();
			// 判断code是否改变
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("usercode", usercode.trim());
			map.put("userid", userid.trim());
			int flag = rdsUpcUserService.queryAllCount(map);
			if (flag <= 0) {
				// 改变情况下判断该code是否已存在
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("usercode", usercode.trim());
				// 已存在，返回，否则继续
				if (rdsUpcUserService.queryAllCount(map1) > 0)
					return this.setModel(false, RdsUpcConstantUtil.REPART_MSG);
			}
			// 该人员姓名缩写
			String initials = StringUtils.getInitials(params.get("username")
					.toString());
			params.put("initials", initials);

			int result = 0;
			// 更新用户信息
			result = rdsUpcUserService.update(params);

			if (result > 0) {
				return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	/**
	 * 查询 用户类型
	 * 
	 * @return
	 */
	@RequestMapping("upc/user/queryusertype")
	@ResponseBody
	public Object queryUserType() {
		return rdsUpcUserService.queryUserType();
	}

	/**
	 * 修改密码
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("upc/user/updatePass")
	@ResponseBody
	public Object updatePass(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		// Map<String, Object> result = new HashMap<String, Object>();
		String oldPasss = params.get("oldPasss") == null ? "" : params.get(
				"oldPasss").toString();
		String newPass = params.get("newPass") == null ? "" : params.get(
				"newPass").toString();
		if (!user.getPassword().equals(MD5Util.string2MD5(oldPasss))) {
			return this.setModel(false, RdsUpcConstantUtil.PASSWORD_WRONG);
		}
		// Map<String, Object> param = new HashMap<String, Object>();
		params.put("userid", "'" + user.getUserid() + "'");
		params.put("password", MD5Util.string2MD5(newPass));
		try {
			int falg = rdsUpcUserService.updatePass(params);
			if (falg > 0) {
				return this.setModel(true, RdsUpcConstantUtil.PASS_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.PASS_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(false, RdsUpcConstantUtil.EXCEPTION);
		}
	}

	/**
	 * 重置密码
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("upc/user/resetUserPass")
	@ResponseBody
	public Object resetUserPass(@RequestBody Map<String, Object> params) {
		params.put("password", MD5Util.string2MD5("888888"));
		try {
			int falg = rdsUpcUserService.updatePass(params);
			if (falg > 0) {
				return this.setModel(true,
						RdsUpcConstantUtil.UPDATEPASS_SUCCESS);
			} else {
				return this.setModel(false,
						RdsUpcConstantUtil.UPDATEPASS_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(false, RdsUpcConstantUtil.EXCEPTION);
		}
	}

	/**
	 * 用户登录
	 * 
	 * @param usercode
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping("upc/user/login")
	@ResponseBody
	public Object login(@RequestParam String usercode,
			@RequestParam String password, @RequestParam String token,
			@RequestParam String device, HttpServletRequest request) {
		if ("".equals(token)) {
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> result = new HashMap<String, Object>();
			params.put("usercode", usercode);
			// params.put("password", MD5Util.string2MD5(password));
			// RdsUpcUserModel usertemp =
			// (RdsUpcUserModel)request.getSession().getAttribute("user");
			try {
				RdsUpcUserModel user = (RdsUpcUserModel) rdsUpcUserService
						.queryForLogin(params);
				if (null == user) {
					result.put("success", false);
					result.put("msg", "登录失败！用户名或密码错误");
					return result;
				}
				if (!user.getPassword().equals(MD5Util.string2MD5(password))) {
					result.put("success", false);
					result.put("msg", "登录失败！用户名或密码错误");
				} else {

					// 存储session
					SessionOperation session = new SessionOperation();
					session.setSesion(request, user);
					result.put("success", true);
					// 如果非浏览器登录
					if (!"web".equals(device)) {
						// 存放token
						Map<String, Object> param = new HashMap<String, Object>();
						String id = RdsUpcUUIDUtil.getUUID();
						param.put("usercode", usercode);
						param.put("token", id);
						param.put("id", id);
						rdsUpcUserService.insertToken(param);
						result.put("token", id);
					}
					result.put("userid", user.getUserid());
					result.put("roletype", user.getRoletype());
					result.put("username", user.getUsername());
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				result.put("success", false);
				result.put("result", "登录失败" + e.getMessage());
				return result;
			}
		} else {
			return tokenCheck(token, request);
		}
	}

	/**
	 * 手机端登录token处理
	 * 
	 * @param usercode
	 * @param token
	 * @return
	 */
	public Object tokenCheck(String token, HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Object> map = rdsUpcUserService.queryToken(params);
			if (!map.isEmpty()) {
				RdsUpcTokenModel tokeModel = (RdsUpcTokenModel) map.get(0);
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("usercode", tokeModel.getUsercode());
				if (tokeModel.getFlag().equals("true")) {
					RdsUpcUserModel user = (RdsUpcUserModel) rdsUpcUserService
							.queryForLogin(param);
					// 存储session
					SessionOperation session = new SessionOperation();
					session.setSesion(request, user);
					result.put("userid", user.getUserid());
					result.put("roletype", user.getRoletype());
					result.put("username", user.getUsername());
					result.put("success", true);
					result.put("msg", "token尚未失效");
				}
				result.put("success", tokeModel.getFlag().equals("true") ? true
						: false);
				result.put("msg",
						tokeModel.getFlag().equals("true") ? "token尚未失效"
								: "token已失效");
				return result;
			} else {
				result.put("success", false);
				result.put("msg", "token已失效");
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "查询失败");
			return result;
		}
	}

	/**
	 * 退出登录
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("quit")
	@ResponseBody
	public void quit(HttpServletRequest request, HttpServletResponse response) {
		try {
			SessionOperation session = new SessionOperation();
			session.clearSession(request);
			response.sendRedirect("loginIndex.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 跳转登录页
	 * 
	 * @return
	 */
	@RequestMapping("success")
	public String adminMain() {
		// 登录直接调用接口
		return "admin/main";
	}

	/**
	 * 根据用户id新增和更新人员信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("upc/user/userCompanyAdd")
	@ResponseBody
	public Object userCompanyAdd(@RequestBody Map<String, Object> params) {
		try {
			// 记录有问题人员usercode
			String usercodeExist = "";
			// 获取部门编号
			RdsUpcDepartmentModel depart = (RdsUpcDepartmentModel) rdsUpcDepartmentService
					.queryModel(params);
			// usercode前台传递不可能为空
			String[] usercodes = params.get("usercodes").toString().split(",");
			// 遍历前台选中用户
			for (String usercode : usercodes) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("usercode", params.get("usercode_plus") + usercode);
				RdsUpcUserModel user = null;
				// 1、判断该用户号是否存在
				if (rdsUpcUserService.queryAllCount(map) > 0)
					usercodeExist += usercode + ";";
				else {
					// 用户号不存在，查处该用户实体
					map.put("usercode", usercode);
					user = (RdsUpcUserModel) rdsUpcUserService.queryModel(map);
					// 关联同一个人不同公司帐号的id
					map.put("relation_id", user.getRelation_id());
					// 2、判断新增时不会增加同一个部门
					String deptListId = rdsUpcUserService.queryDeptList(map)
							.get("deptListId") == null ? "" : rdsUpcUserService
							.queryDeptList(map).get("deptListId").toString();
					// 判断该用户是否在同一部门下添加
					if (deptListId.contains(params.get("deptid").toString())) {
						// 如果是，则不添加该条记录
						user = null;
						// 返回前台展示提示信息
						usercodeExist += usercode + ";";
					}
				}
				if (null != user) {
					// 3、插入新的用户信息记录
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("userid", RdsUpcUUIDUtil.getUUID());
					param.put("relation_id", user.getRelation_id());
					param.put("usercode", params.get("usercode_plus")
							+ usercode);
					param.put("username",
							params.get("name_plus") + user.getUsername());
					param.put("password", MD5Util.string2MD5(USER_PASSWORD));
					param.put("deptcode", depart.getDeptcode());
					param.put("usertype", user.getUsertype());
					param.put("roletype", user.getRoletype());
					param.put("telphone", user.getTelphone());
					param.put("qq", user.getQq());
					param.put("webchart", user.getWebchart());
					param.put("email", user.getEmail());
					param.put("address", user.getAddress());
					param.put("sex", user.getSex());
					param.put("deptid", params.get("deptid"));
					param.put("certificateno", user.getCertificateno());
					param.put(
							"initials",
							StringUtils.getInitials(params.get("name_plus")
									+ user.getUsername()));
					rdsUpcUserService.insert(param);
				}
			}
			// 4、弹出提示信息
			if ("".equals(usercodeExist))
				return this.setModel(true, RdsUpcConstantUtil.SAVE_SUCCESS);
			else
				return this.setModel(true, usercodeExist
						+ "<br>这些案例无法保存，请查看！<br>可能存在相同案例编号，或者该帐号已存在该部门！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(false, RdsUpcConstantUtil.EXCEPTION);
		}
	}
}
