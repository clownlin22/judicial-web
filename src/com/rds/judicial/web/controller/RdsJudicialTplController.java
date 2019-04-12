package com.rds.judicial.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.model.RdsJudicialStandardModel;
import com.rds.judicial.model.RdsJudicialTplModel;

@Controller
@RequestMapping("judicial/tpl")
public class RdsJudicialTplController {
	
	/**
	 * 查找存储模版
	 * @param query
	 * @return
	 */
	@RequestMapping("queryTplForComplate")
	@ResponseBody
	public Object queryTplForComplate( String query){
		RdsJudicialTplModel model = new RdsJudicialTplModel();
		List<Object> l_result = new ArrayList<Object>();
		if(query.contains("章路峰")){
			String r_1 = "00000";
			model.setTplid(r_1);
			model.setTplname("章路峰，男，1970年1月25日生，住址：江苏省沭阳县沭城镇宣义二巷15号，身份证号：320823197001250533。");
			model.setFxsm("根据送检的鉴定资料记载，被鉴定人章路峰于2013年7月6日因交通事故致右胫腓骨折、多发性肋骨骨折、右外踝骨折，伤后一年余，临床体征稳定。"+
	"根据体格检查及阅片所见，被鉴定人章路峰右侧第4-6及左侧第3-7肋骨骨折（共8根）经治疗后，目前恢复可；右胫腓骨骨折合并右外踝骨折经治疗后目前外固定支架在位，遗留右下肢跛行，右下肢行走、承重功能受限，右下肢丧失功能10%以上，依据《道路交通事故受伤人员伤残评定》（GB18667-2002）标准第4.9.5b、附则5.1及第4.10.10i条规定，被鉴定人章路峰8肋以上骨折构成道路交通事故九级伤残；右胫腓骨骨折合并右外踝骨折构成道路交通事故十级伤残。"+
	"被鉴定人章路峰损伤后，因治疗和康复的需要而无法参加工作，需要休息、存在误工；生活自理能力下降，需要他人帮助；并且需加强营养作为辅助治疗措施，以利于损伤恢复和身体康复。按照《人身损害受伤人员误工损失日评定准则》（GA/T521-2004）及江苏省关于《人身损害受伤人员休息期、营养期、护理期评定标准》等相关规定，结合治疗与恢复情况，被鉴定人章路峰的误工期限以受伤之日起至评残前一日止为宜；护理期限以三个月为宜。");
			model.setJdyj("1被鉴定人章路峰8肋以上骨折构成道路交通事故九级伤残；右胫腓骨骨折合并右外踝骨折构成道路交通事故十级伤残。");
			model.setTgjc("被鉴定人步行入检查室，右下肢跛行，语言清晰，对答切题，查体合作，左额部见长6㎝条状皮肤疤痕。右小腿外固定支架在位，右小腿外侧下段见长12㎝纵形疤痕，右胫前下段见长8.5㎝纵形疤痕，右膝关节下方见长14㎝横形软组织疤痕。右下肢活动受限，双上肢活动好。");
			l_result.add(model);
		}
		return l_result;
	}
	
	/**
	 * 自动完成 查询标准
	 * @param map
	 * @return
	 */
	@RequestMapping("queryTplComplateStandard")
	@ResponseBody
	public Object queryTplComplateStandard(@RequestBody Map<String, Object> map){
		List<Object> l_result = new ArrayList<Object>();
		RdsJudicialStandardModel model = null;
		String index = null;
		
		if(map.get("tplid").equals("00000")){
			model = new RdsJudicialStandardModel();
			index = "00000";
			model.setStdid(index+"stdid");
			model.setStdname("1．5．1(1)器官缺失或功能完全丧失，其他器官不能代偿；");
			model.setStdlevel("一级");
			l_result.add(model);
		}else {
			model = new RdsJudicialStandardModel();
			
			index = "10000";
			model.setStdid(index+"stdid");
			model.setStdname("1．5．2(1)器官严重缺损或畸形，有严重功能障碍或并发症；");
			model.setStdlevel("二级");
			l_result.add(model);
		}
		
		return l_result;
	}

}
