package com.rds.statistics.service.impl;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.statistics.mapper.RdsStatisticsMapper;
import com.rds.statistics.model.RdsStatisticsBaseModel;
import com.rds.statistics.model.RdsStatisticsProgramModel;
import com.rds.statistics.model.RdsStatisticsResponse;
import com.rds.statistics.model.RdsStatisticsTypeModel;
import com.rds.statistics.model.RdsStatsticsJudicialTimeModel;
import com.rds.statistics.service.RdsStatisticsService;

@Service
public class RdsStatisticsServiceImpl implements RdsStatisticsService {

	@Autowired
	private RdsStatisticsMapper RdsStatisticsMapper;

	// 配置文件地址
	private static final String XML_PATH = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "type_statistics.xml";

	private List<RdsStatisticsTypeModel> getTypeModel() {
		List<RdsStatisticsTypeModel> typeModels = new ArrayList<RdsStatisticsTypeModel>();
		SAXReader saxReader = new SAXReader();
		try {
			Document dom = saxReader.read(XML_PATH);
			Element element = dom.getRootElement();
			List<Element> els = element.elements("type");
			for (Element e : els) {
				RdsStatisticsTypeModel typeModel = new RdsStatisticsTypeModel();
				typeModel.setKey(e.element("key").getText().trim());
				typeModel.setType(e.element("name").getText().trim());
				typeModel.setCase_type(e.element("value").getText().trim());
				typeModels.add(typeModel);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return typeModels;
	}

	@Override
	public RdsStatisticsResponse getTotalStatistics(String date) {
		RdsStatisticsResponse response = new RdsStatisticsResponse();
		List<RdsStatisticsTypeModel> typeModels = getTypeModel();
		List<RdsStatisticsBaseModel> baseModels = RdsStatisticsMapper
				.getBaseModel(date);
		response.setTypes(typeModels);
		response.setTotalModels(baseModels);
		return response;
	}

	@Override
	public RdsStatisticsResponse getPerStatistics(String date) {
		RdsStatisticsResponse response=new RdsStatisticsResponse();
		List<RdsStatisticsTypeModel> typeModels = getTypeModel();
		response.setTypes(typeModels);
		List<String> persons=RdsStatisticsMapper.getOwnPerson(date);
		response.setOwnPersons(persons);
		List<RdsStatisticsBaseModel> baseModels = RdsStatisticsMapper
				.getPerBaseModel(date);
		response.setTotalModels(baseModels);
		return response;
	}

	@Override
	public RdsJudicialResponse statTime(Map<String, Object> params) {
		RdsJudicialResponse response=new RdsJudicialResponse();
		List<RdsStatsticsJudicialTimeModel> timeModels=RdsStatisticsMapper.getTimeModel(params);
		int count=RdsStatisticsMapper.countTimeModel(params);
		response.setCount(count);
		response.setItems(timeModels);
		return response;
	}

	@Override
	public void exportTotalStatistics(String date,HttpServletResponse response) {
		List<RdsStatisticsTypeModel> typeModels = getTypeModel();
		List<RdsStatisticsBaseModel> baseModels = RdsStatisticsMapper
				.getBaseModel(date);
		String[] dates={"01","02","03","04","05","06","07","08","09","10","11","12"};
		Object[] titles = { "类别", "名称", "一月", "二月", "三月", "四月", "五月",
				"六月", "七月","八月", "九月", "十月", "十一月", "十二月", "合计"};
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for(int i=0;i<typeModels.size();i++){
			List<Object> objects = new ArrayList<Object>();
			objects.add(typeModels.get(i).getType());
			objects.add(typeModels.get(i).getCase_type());
			int rec_total=0;
			for(String da:dates){
				int total=0;
				for(RdsStatisticsBaseModel baseModel:baseModels){
					if(da.equals(baseModel.getDate())&&baseModel.getCase_type().equals(typeModels.get(i).getKey())){
						total=baseModel.getRec_total();
						rec_total+=baseModel.getRec_total();
					}
				}
				objects.add(total);
			}
			objects.add(rec_total);
			bodys.add(objects);
		}
		List<Object> objects = new ArrayList<Object>();
		objects.add("合计");
		objects.add("");
		int all_total=0;
		for(String da:dates){
			int total=0;
			for(RdsStatisticsBaseModel baseModel:baseModels){
				if(da.equals(baseModel.getDate())){
					total+=baseModel.getRec_total();
					all_total+=baseModel.getRec_total();
				}
			}
			objects.add(total);
		}
		objects.add(all_total);
		bodys.add(objects);
		ExportUtils.export(response, "项目汇总统计", titles, bodys, date+"项目汇总表");
	}

	@Override
	public void exportPerStatistics(String date, HttpServletResponse response) {
		List<RdsStatisticsTypeModel> typeModels = getTypeModel();
		List<String> persons=RdsStatisticsMapper.getOwnPerson(date);
		List<RdsStatisticsBaseModel> baseModels = RdsStatisticsMapper
				.getPerBaseModel(date);
		try {
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			String fname = date+new String("应收实收汇总表".getBytes("gb2312"),"iso8859-1");
			response.setHeader("Content-disposition",
					"attachment; filename="+ fname +".xls");// 设定输出文件头
			response.setContentType("application/vnd.ms-excel");
			WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
			WritableSheet wsheet = wbook.createSheet("应收实收汇总", 0); // sheet名称
			// 设置excel标题
			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
					Colour.BLACK);
			WritableCellFormat wcfFC = new WritableCellFormat(wfont);
			wcfFC.setBackground(Colour.AQUA);
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			Label label = new Label(0, 0, "姓名");
			wsheet.addCell(label);
			wsheet.mergeCells(0, 0, 0, 1);   
			for(int i=0;i<typeModels.size();i++){
				Label la = new Label((i+1)*2-1, 0, typeModels.get(i).getCase_type());
				wsheet.addCell(la);
				wsheet.mergeCells((i+1)*2-1,0,(i+1)*2, 0);   
			}
			Label label2 = new Label( (typeModels.size()+1)*2-1,0, "本期应收汇总");
			wsheet.addCell(label2);
			wsheet.mergeCells((typeModels.size()+1)*2-1, 0, (typeModels.size()+1)*2-1, 1); 
			Label label3 = new Label( (typeModels.size()+1)*2,0, "本期已收汇总");
			wsheet.addCell(label3);
			wsheet.mergeCells((typeModels.size()+1)*2, 0, (typeModels.size()+1)*2, 1);   
			wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 14,
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
					Colour.BLACK);
			wcfFC = new WritableCellFormat(wfont);
			
			for(int i=0;i<typeModels.size();i++){
				Label la1 = new Label((i+1)*2-1, 1, "本期应收");
				wsheet.addCell(la1);
				Label la2 = new Label((i+1)*2, 1, "本期已收");
				wsheet.addCell(la2);
			}
			
			for(int i=0;i<persons.size();i++){
				int rec_sum=0;
				int pre_sum=0;
				List<Object> objects = new ArrayList<Object>();
				objects.add(persons.get(i));
				for(RdsStatisticsTypeModel type:typeModels){
					int pre_total=0;int rec_total=0;
					for(RdsStatisticsBaseModel baseModel:baseModels){
						if(baseModel.getCase_type().equals(type.getKey())&&baseModel.getOwnperson().equals(persons.get(i))){
							pre_total+=baseModel.getPre_total();
							rec_total+=baseModel.getRec_total();
						}
					}
					objects.add(pre_total);
					objects.add(rec_total);
					pre_sum+=pre_total;
					rec_sum+=rec_total;
				}
				objects.add(pre_sum);
				objects.add(rec_sum);
				bodys.add(objects);
			}
			List<Object> objects = new ArrayList<Object>();
			objects.add("合计");
			int pre_all_sum=0;int rec_all_sum=0;
			for(RdsStatisticsTypeModel type:typeModels){
				int pre_total=0;int rec_total=0;
				for(RdsStatisticsBaseModel baseModel:baseModels){
					if(baseModel.getCase_type().equals(type.getKey())){
						pre_total+=baseModel.getPre_total();
						rec_total+=baseModel.getRec_total();
					}
				}
				pre_all_sum+=pre_total;
				rec_all_sum+=rec_total;
				objects.add(pre_total);
				objects.add(rec_total);
			}
			objects.add(pre_all_sum);
			objects.add(rec_all_sum);
			bodys.add(objects);
			for (int i = 0; i < bodys.size(); i++) {
				for (int j = 0; j < bodys.get(i).size(); j++) {
					if (bodys.get(i).get(j) != null) {
						if (bodys.get(i).get(j) instanceof String) {
							Label l = new Label(j, i + 2,
									String.valueOf(bodys.get(i).get(j)));
							wsheet.addCell(l);
						} else {
							Number number = new Number(j, i + 2,
									Float.valueOf(String.valueOf(bodys.get(i)
											.get(j))));
							wsheet.addCell(number);
						}
					} else {
						Label l= new Label(j, i + 2, "");
						wsheet.addCell(l);
					}

				}
			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exportPerBanlance(String date, HttpServletResponse response) {
		List<RdsStatisticsTypeModel> typeModels = getTypeModel();
		List<String> persons=RdsStatisticsMapper.getOwnPerson(date);
		List<RdsStatisticsBaseModel> baseModels = RdsStatisticsMapper
				.getPerBaseModel(date);
		Object[] titles=new String[typeModels.size()+2];
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		titles[0]="姓名";
		for(int i=0;i<typeModels.size();i++){
			titles[i+1]=typeModels.get(i).getCase_type();
		}
		titles[titles.length-1]="合计";
		for(int i=0;i<persons.size();i++){
			List<Object> objects = new ArrayList<Object>();
			int sum=0;
			objects.add(persons.get(i));
			for(RdsStatisticsTypeModel type:typeModels){
				int total=0;
				for(RdsStatisticsBaseModel baseModel:baseModels){
					if(baseModel.getCase_type().equals(type.getKey())&&baseModel.getOwnperson().equals(persons.get(i))){
						 total=baseModel.getPre_total()-baseModel.getRec_total();
					}
				}
				objects.add(total);
				sum+=total;
			}
			objects.add(sum);
			bodys.add(objects);
		}
		List<Object> objects = new ArrayList<Object>();
		objects.add("合计");
		int all_sum=0;
		for(RdsStatisticsTypeModel type:typeModels){
			int total=0;
			for(RdsStatisticsBaseModel baseModel:baseModels){
				if(baseModel.getCase_type().equals(type.getKey())){
					 total+=(baseModel.getPre_total()-baseModel.getRec_total());
				}
			}
			objects.add(total);
			all_sum+=total;
		}
		objects.add(all_sum);
		bodys.add(objects);
		ExportUtils.export(response, "余额汇总统计", titles, bodys, date+"余额汇总表");
	}

	@Override
	public RdsJudicialResponse queryProgramProvice(
			Map<String, Object> params) {
		RdsJudicialResponse response=new RdsJudicialResponse();
		List<RdsStatisticsProgramModel> timeModels=RdsStatisticsMapper.queryProgramProvice(params);
		int count=RdsStatisticsMapper.queryProgramProviceCount(params);
		response.setCount(count);
		response.setItems(timeModels);
		return response;
	}
}