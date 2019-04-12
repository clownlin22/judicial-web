package com.rds.statistics.service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.rds.code.utils.ExportUtils;
import com.rds.statistics.mapper.RdsExperimentalDataMapper;
import com.rds.statistics.model.RdsAllCaseInfoModel;
import com.rds.statistics.model.RdsExperimentalDataModel;
import com.rds.statistics.service.RdsExperimentalDataService;
@Service("RdsExperimentalDataService")
public class RdsExperimentalDataServiceImpl implements RdsExperimentalDataService{
     @Setter
     @Autowired
	private RdsExperimentalDataMapper rdsExperimentalDataMapper;
	
	

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String,Object> result =new HashMap<String, Object>();
		result.put("total",rdsExperimentalDataMapper.queryAllCount(params) );
		result.put("data", rdsExperimentalDataMapper.queryAllPage(params));
		return result;
	
	}

	@Override
    public void exportExperimentalData( Map<String, Object> params,  HttpServletResponse httpResponse) {
       
        // excel表格列头
        Object[] titles = { "案例编号",	"采样编号","采样日期","受理日期","实验编号","父母亲",
				"身份证","孩子","出生日期","司法鉴定人","鉴定助理","实验日期","比对日期",
				"报告打印日期","报告发出日期","邮寄地址","电话","资料&照片",	
				"样本数","比对结论"};
        // 表格实体
        List<List<Object>> bodys = new ArrayList<List<Object>>();
        List<Object> firstobject = new ArrayList<Object>();
        for(int i=0;i<19;i++){
            firstobject.add("");
        }
        firstobject.add("是否添加位点");
        firstobject.add("位点结果");
        bodys.add(firstobject);
		List<Object> list=null;
		try {
			list = rdsExperimentalDataMapper.queryAllPage(params);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

        // 循环案例列表拼装表格一行
        for(int i=0;i<list.size();i++){
			List<Object> objects=new ArrayList<>();
			RdsExperimentalDataModel rdsExperimentalDataModel=(RdsExperimentalDataModel) list.get(i);
			//按照顺序对应表头
			objects.add(rdsExperimentalDataModel.getCase_code());
			objects.add(rdsExperimentalDataModel.getSample_codes());
			objects.add(rdsExperimentalDataModel.getConsignment_time());
			objects.add(rdsExperimentalDataModel.getAccept_time());
			objects.add(rdsExperimentalDataModel.getExperiment_no());
			objects.add(rdsExperimentalDataModel.getFandm());
			objects.add(rdsExperimentalDataModel.getId_card());
			objects.add(rdsExperimentalDataModel.getChild());
			objects.add(rdsExperimentalDataModel.getBirth_date());
			objects.add(rdsExperimentalDataModel.getIdentify_per());
			objects.add(rdsExperimentalDataModel.getAssistant());
			objects.add(rdsExperimentalDataModel.getExperiment_date());
			objects.add(rdsExperimentalDataModel.getCompare_date());
			objects.add(rdsExperimentalDataModel.getClose_time());//报告打印时间
			objects.add(rdsExperimentalDataModel.getMail_time());
			objects.add(rdsExperimentalDataModel.getMail_address());
			objects.add(rdsExperimentalDataModel.getMail_phone());
			objects.add(rdsExperimentalDataModel.getData_photo());
			objects.add(rdsExperimentalDataModel.getSample_count());
			objects.add("Y".equals(rdsExperimentalDataModel.getExt_flag())?"是":"否");
			if("passed".equals(rdsExperimentalDataModel.getFinal_result_flag())){
				objects.add("肯定");
			}else if("failed".equals(rdsExperimentalDataModel.getFinal_result_flag())){
				objects.add("否定");
			}else {
				objects.add("");
			}
			bodys.add(objects);
        }

        try {
            OutputStream os = httpResponse.getOutputStream();// 取得输出流
            httpResponse.reset();// 清空输出流
            String fname = new String("实验数据统计".getBytes("gb2312"),"iso8859-1");
            httpResponse.setHeader("Content-disposition",
                    "attachment; filename="+ fname +".xls");// 设定输出文件头
            httpResponse.setContentType("application/vnd.ms-excel");
            WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
            WritableSheet wsheet = wbook.createSheet("sheet1", 0); // sheet名称

            for (int i = 0; i < titles.length; i++) {
                Label label = new Label(i, 0, String.valueOf(titles[i]));
                wsheet.addCell(label);
            }
            for (int i = 0; i < bodys.size(); i++) {
                for (int j = 0; j < bodys.get(i).size(); j++) {
                    if (bodys.get(i).get(j) != null) {
                        if (bodys.get(i).get(j) instanceof String) {
                            Label label = new Label(j, i + 1,
                                    String.valueOf(bodys.get(i).get(j)));
                            wsheet.addCell(label);
                        } else {
                            Number number = new Number(j, i + 1,
                                    Float.valueOf(String.valueOf(bodys.get(i)
                                            .get(j))));
                            wsheet.addCell(number);
                        }
                    } else {
                        Label label = new Label(j, i + 1, "");
                        wsheet.addCell(label);
                    }

                }
            }
            wsheet.mergeCells(19,0,20,0);
            WritableCellFormat writableCellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10,
                    WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                    Colour.BLACK));
            writableCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
            for(int i=0;i<21;i++){
                wsheet.mergeCells(i,0,i,1);
                wsheet.getWritableCell(i,0).setCellFormat(writableCellFormat);
           }
            wsheet.getWritableCell(11,0).setCellFormat(writableCellFormat);
            for(int i=18;i<19;i++){
                wsheet.getWritableCell(i,1).setCellFormat(writableCellFormat);
            }
            // 主体内容生成结束
            wbook.write(); // 写入文件
            wbook.close();
            os.close(); // 关闭流
        } catch (Exception e) {
            //logger.debug(e.getStackTrace());
        }
        //ExportUtils.export(httpResponse, "sheet1", titles, bodys, "经营分析基表"
                //+ DateUtils.Date2String(new Date()));
    }

	
	


}
