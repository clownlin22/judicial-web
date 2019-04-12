package com.rds.upc.web.controller;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rds.upc.model.RdsUpcStResultModel;
import com.rds.upc.model.RdsUpcStatisticslModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.service.RdsUpcStatisticalService;
import com.rds.upc.web.common.RdsUpcConstantUtil;

@Controller
@RequestMapping("upc/statisticsl")
public class RdsUpcStatisticslController extends RdsUpcAbstractController {
	
	@Setter
	@Autowired
	private RdsUpcStatisticalService rdsUpcStatisticalService;
	
	@RequestMapping("queryallprovice")
	public String queryAllProvice(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
		map.put("user_code", user.getUsercode());
		map.put("user_id", user.getUserid());
		List<RdsUpcStatisticslModel> proviceList = rdsUpcStatisticalService.queryAllProvice(map);
		request.setAttribute("proviceList", proviceList);
		request.setAttribute("countyList", rdsUpcStatisticalService.queryAllCounty(map));
		request.setAttribute("cityList", rdsUpcStatisticalService.queryAllCity(map));
		return "lsl";
	}
	
	@RequestMapping("queryallcity")
	@ResponseBody
	public Object queryAllCity(HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String proviceId = request.getParameter("proviceId")==null?"":request.getParameter("proviceId").toString();
		map.put("provice_id", proviceId);
		RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
		map.put("user_code", user.getUsercode());
		map.put("user_id", user.getUserid());
		return rdsUpcStatisticalService.queryAllCity(map);
	}

	@RequestMapping("queryallcounty")
	@ResponseBody
	public Object queryAllCounty(HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String cityId = request.getParameter("cityId").toString();
		map.put("city_id", cityId);
		RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
		map.put("user_code", user.getUsercode());
		map.put("user_id", user.getUserid());
		return rdsUpcStatisticalService.queryAllCounty(map);
	}

	@RequestMapping("search")
	@ResponseBody
	public Object search(HttpServletRequest request)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
//		String city = request.getParameter("city")==null?"": request.getParameter("city").toString();
		return rdsUpcStatisticalService.queryAllCounty(map);
	}
	
	@RequestMapping("queryallStatisticsl")
	@ResponseBody
	public Object queryallStatisticsl(HttpServletRequest request) throws Exception{
		RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
		String provice_id = request.getParameter("provice")==null?"":request.getParameter("provice").toString();
		String city_id = request.getParameter("city")==null?"":request.getParameter("city").toString();
		String county_id = request.getParameter("county")==null?"":request.getParameter("county").toString();
		String year = request.getParameter("year")==null?"":request.getParameter("year").toString();
		Map<String, String> map = new HashMap<String, String>();
		map.put("provice_id", provice_id);
		map.put("city_id", city_id);
		map.put("county_id", county_id);
		map.put("user_code", user.getUsercode());
		map.put("user_id", user.getUserid());
		int inputyear = Integer.parseInt(year==""?"0":year);
		Calendar cal = Calendar.getInstance();
		int nowyear = cal.get(Calendar.YEAR);
		String[] last12Months = new String[12]; 
		if(inputyear == nowyear || "".equals(year))
		{
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)); //要先+1,才能把本月的算进去</span>  此处没+1
			for(int i=0; i<12; i++){  
			    cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1); //逐次往前推1个月 
			    int month = cal.get(Calendar.MONTH)+1;
	            last12Months[11-i] = cal.get(Calendar.YEAR) + (month<10?"0"+month:""+month);  
			}
			for(int i = 1 ; i < 13 ; i++)
			{
				map.put("month"+(i*2-1), last12Months[i-1]+1);
				map.put("month"+(i*2), last12Months[i-1]+2);
			}
		}else
		{
			for(int i = 1 ; i < 13 ; i ++)
			{
				map.put("month"+(i*2-1), year+(i<10?("0"+i):i+1));
				map.put("month"+(i*2), year+(i<10?("0"+i):i+2));
			}
		}
		List<RdsUpcStResultModel> list = rdsUpcStatisticalService.queryAllStatisticsl(map);
		List<String[]> lists = new ArrayList<String[]>();
		for (RdsUpcStResultModel rdsUpcStResultModel : list) {
			String[] temp = { rdsUpcStResultModel.getProvice_name(),
					rdsUpcStResultModel.getCity_name(),
					rdsUpcStResultModel.getCounty_name(),
					rdsUpcStResultModel.getMonth1(),
					rdsUpcStResultModel.getMonth2(),
					rdsUpcStResultModel.getMonth3(),
					rdsUpcStResultModel.getMonth4(),
					rdsUpcStResultModel.getMonth5(),
					rdsUpcStResultModel.getMonth6(),
					rdsUpcStResultModel.getMonth7(),
					rdsUpcStResultModel.getMonth8(),
					rdsUpcStResultModel.getMonth9(),
					rdsUpcStResultModel.getMonth10(),
					rdsUpcStResultModel.getMonth11(),
					rdsUpcStResultModel.getMonth12(),
					rdsUpcStResultModel.getMonth13(),
					rdsUpcStResultModel.getMonth14(),
					rdsUpcStResultModel.getMonth15(),
					rdsUpcStResultModel.getMonth16(),
					rdsUpcStResultModel.getMonth17(),
					rdsUpcStResultModel.getMonth18(),
					rdsUpcStResultModel.getMonth19(),
					rdsUpcStResultModel.getMonth20(),
					rdsUpcStResultModel.getMonth21(),
					rdsUpcStResultModel.getMonth22(),
					rdsUpcStResultModel.getMonth23(),
					rdsUpcStResultModel.getMonth24()};
			lists.add(temp);
		}
		return lists;
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params){
		try {
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
		
	}
	
	@RequestMapping("export")
	@ResponseBody
	public ModelAndView export(HttpServletRequest request) throws Exception{
		RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
		String provice_id = request.getParameter("provice")==null?"":request.getParameter("provice").toString();
		String city_id = request.getParameter("city")==null?"":request.getParameter("city").toString();
		String county_id = request.getParameter("county")==null?"":request.getParameter("county").toString();
		String year = request.getParameter("year")==null?"":request.getParameter("year").toString();
		Map<String, String> map = new HashMap<String, String>();
		map.put("provice_id", provice_id);
		map.put("city_id", city_id);
		map.put("county_id", county_id);
		map.put("user_code", user.getUsercode());
		map.put("user_id", user.getUserid());
		int inputyear = Integer.parseInt(year==""?"0":year);
		Calendar cal = Calendar.getInstance();
		int nowyear = cal.get(Calendar.YEAR);
		String[] last12Months = new String[12]; 
		String[] export12Months = new String[12];
		if(inputyear == nowyear || "".equals(year))
		{
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)); //要先+1,才能把本月的算进去</span>  
			for(int i=0; i<12; i++){  
			    cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1); //逐次往前推1个月 
			    int month = cal.get(Calendar.MONTH)+1;
	            last12Months[11-i] = cal.get(Calendar.YEAR) + (month<10?"0"+month:""+month);  
	            export12Months[11-i] = cal.get(Calendar.YEAR) + (month<10?"0"+month:""+month);  
			}
			for(int i = 1 ; i < 13 ; i++)
			{
				map.put("month"+(i*2-1), last12Months[i-1]+1);
				map.put("month"+(i*2), last12Months[i-1]+2);
			}
		}else
		{
			for(int i = 1 ; i < 13 ; i ++)
			{
				map.put("month"+(i*2-1), year+(i<10?("0"+i):i+1));
				map.put("month"+(i*2), year+(i<10?("0"+i):i+2));
				export12Months[i-1] = year+(i<10?("0"+i):i);
			}
		}
		List<RdsUpcStResultModel> list = rdsUpcStatisticalService.queryAllStatisticsl(map);
		List<String[]> lists = new ArrayList<String[]>();
		for (RdsUpcStResultModel rdsUpcStResultModel : list) {
			String[] temp = { rdsUpcStResultModel.getProvice_name(),
					rdsUpcStResultModel.getCity_name(),
					rdsUpcStResultModel.getCounty_name(),
					rdsUpcStResultModel.getMonth1(),
					rdsUpcStResultModel.getMonth2(),
					rdsUpcStResultModel.getMonth3(),
					rdsUpcStResultModel.getMonth4(),
					rdsUpcStResultModel.getMonth5(),
					rdsUpcStResultModel.getMonth6(),
					rdsUpcStResultModel.getMonth7(),
					rdsUpcStResultModel.getMonth8(),
					rdsUpcStResultModel.getMonth9(),
					rdsUpcStResultModel.getMonth10(),
					rdsUpcStResultModel.getMonth11(),
					rdsUpcStResultModel.getMonth12(),
					rdsUpcStResultModel.getMonth13(),
					rdsUpcStResultModel.getMonth14(),
					rdsUpcStResultModel.getMonth15(),
					rdsUpcStResultModel.getMonth16(),
					rdsUpcStResultModel.getMonth17(),
					rdsUpcStResultModel.getMonth18(),
					rdsUpcStResultModel.getMonth19(),
					rdsUpcStResultModel.getMonth20(),
					rdsUpcStResultModel.getMonth21(),
					rdsUpcStResultModel.getMonth22(),
					rdsUpcStResultModel.getMonth23(),
					rdsUpcStResultModel.getMonth24()};
			lists.add(temp);
		}
		request.setAttribute("export12Months", export12Months);
		request.setAttribute("list", lists);
		return new ModelAndView("exportLsl");
	}

}
