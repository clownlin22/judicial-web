package com.rds.narcotics.service.impl;

import com.rds.code.utils.FileUtils;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.narcotics.mapper.RdsNarcoticsMapper;
import com.rds.narcotics.model.*;
import com.rds.narcotics.service.RdsNarcoticsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lxy
 * @Date: 2019/3/20 10:46
 */
@Service
public class RdsNarcoticsServiceImpl implements RdsNarcoticsService {

    @Autowired
    private RdsNarcoticsMapper rdsNarcoticsMapper;

    @Override
    public Map<String, Object> getIdentificationPer() {
        List<RdsNarcoticsDicValueModel> dvList = rdsNarcoticsMapper.getIdentificationPer();
        Map<String, Object> result = new HashMap<>();
        result.put("data", dvList);
        return result;
    }

    @Override
    public boolean addCaseInfo(RdsNarcoticsModel rdsNarcoticsModel) {
        String case_id = UUIDUtil.getUUID();
        rdsNarcoticsModel.setCase_id(case_id);
        String identificationper = rdsNarcoticsModel.getIdentification_per();
        String[] identification_per = identificationper.split(",");
        for (int i = 0; i < identification_per.length; i++) {
            RdsNarcoticsCaseIdentify rdsNarcoticsCaseIdentify = new RdsNarcoticsCaseIdentify();
            rdsNarcoticsCaseIdentify.setId(UUIDUtil.getUUID());
            rdsNarcoticsCaseIdentify.setPid(identification_per[i]);
            rdsNarcoticsCaseIdentify.setCid(case_id);
            rdsNarcoticsCaseIdentify.setCreate_sort(i);
            if (!rdsNarcoticsMapper.addCaseIdentify(rdsNarcoticsCaseIdentify)) {
                return false;
            }
        }
        return rdsNarcoticsMapper.addCaseInfo(rdsNarcoticsModel);
    }

    @Override
    public RdsNarcoticsResponse getCaseInfo(Map<String, Object> params) {
        RdsNarcoticsResponse response = new RdsNarcoticsResponse();
        List<RdsNarcoticsModel> caseInfos = rdsNarcoticsMapper
                .getCaseInfo(params);
        int count = rdsNarcoticsMapper.countCaseInfo(params);
        response.setCount(count);
        response.setItems(caseInfos);
        return response;
    }

    @Override
    public boolean exsitCaseNum(String case_num) {
        return rdsNarcoticsMapper.exsitCaseNum(case_num)>0?false:true;
    }

    @Override
    public boolean updateCaseInfo(RdsNarcoticsModel rdsNarcoticsModel) {
        if(rdsNarcoticsMapper.deleteCaseIdentifyByCaseId(rdsNarcoticsModel.getCase_id())){
            String identificationper = rdsNarcoticsModel.getIdentification_per();
            String[] identification_per = identificationper.split(",");
            for (int i = 0; i < identification_per.length; i++) {
                RdsNarcoticsCaseIdentify rdsNarcoticsCaseIdentify = new RdsNarcoticsCaseIdentify();
                rdsNarcoticsCaseIdentify.setId(UUIDUtil.getUUID());
                rdsNarcoticsCaseIdentify.setPid(identification_per[i]);
                rdsNarcoticsCaseIdentify.setCid(rdsNarcoticsModel.getCase_id());
                rdsNarcoticsCaseIdentify.setCreate_sort(i);
                if (!rdsNarcoticsMapper.addCaseIdentify(rdsNarcoticsCaseIdentify)) {
                    return false;
                }
            }
        }
        return rdsNarcoticsMapper.updateCaseInfo(rdsNarcoticsModel);
    }

    @Override
    public boolean deletecaseInfo(Map<String, Object> params) {
        return rdsNarcoticsMapper.deletecaseInfo(params);
    }

    @Override
    public void createNarcoticsDocByCaseId(Map<String, Object> params) throws Exception{
        Map<String, Object> createparams = new HashMap<>();

        createparams.put("case_id",params.get("case_id"));
        RdsNarcoticsModel caseInfo = rdsNarcoticsMapper.getCaseInfoByCaseId(createparams);
        List<RdsNarcoticsIdentifyModel> RdsNarcoticsIdentifyModel= rdsNarcoticsMapper.getIdentificationPerByCaseId(createparams);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String identification_start = caseInfo.getIdentification_start();
        String identification_end = caseInfo.getIdentification_end();
        if(caseInfo.getIdentification_end().equals(caseInfo.getIdentification_start())){
            caseInfo.setIdentification_start(simpleDateFormat.format(sdf.parse(identification_start)));
        }else{
            caseInfo.setIdentification_start(simpleDateFormat.format(sdf.parse(identification_start))+"-"+simpleDateFormat.format(sdf.parse(identification_end)));
        }

        caseInfo.setIdentification_end(simpleDateFormat.format(sdf.parse(identification_start)));
        caseInfo.setReport_time(dateToCnDate(identification_end));
        caseInfo.setClient_time(simpleDateFormat.format(sdf.parse(caseInfo.getClient_time())));

        createparams.put("caseInfoModel", caseInfo);
        createparams.put("identifyInfoModel", RdsNarcoticsIdentifyModel);
        createparams.put("template_name", "narcotics.ftl");
        createparams.put("file_name", params.get("file_name"));
        create(createparams);
    }

    //日期转中
    public String dateToCnDate(String date) {
        String result = "";
        String[]  cnDate = new String[]{"○","一","二","三","四","五","六","七","八","九"};
        String ten = "十";
        String[] dateStr = date.split("-");
        for (int i=0; i<dateStr.length; i++) {
            for (int j=0; j<dateStr[i].length(); j++) {
                String charStr = dateStr[i];
                String str = String.valueOf(charStr.charAt(j));
                if (charStr.length() == 2) {
                    if (charStr.equals("10")) {
                        result += ten;
                        break;
                    } else {
                        if (j == 0) {
                            if (charStr.charAt(j) == '1')
                                result += ten;
                            else if (charStr.charAt(j) == '0')
                                result += "";
                            else
                                result += cnDate[Integer.parseInt(str)] + ten;
                        }
                        if (j == 1) {
                            if (charStr.charAt(j) == '0')
                                result += "";
                            else
                                result += cnDate[Integer.parseInt(str)];
                        }
                    }
                } else {
                    result += cnDate[Integer.parseInt(str)];
                }
            }
            if (i == 0) {
                result += "年";
                continue;
            }
            if (i == 1) {
                result += "月";
                continue;
            }
            if (i == 2) {
                result += "日";
                continue;
            }
        }
        return result;
    }

    public void create(Map<String, Object> params) throws IOException,TemplateException {
        // 要填入模本的数据文件
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(this.getClass(),
                "/com/rds/narcotics/template");
        Template t = null;
        // test.ftl为要装载的模板
        t = configuration.getTemplate((String) params.get("template_name"));
        // 输出文档路径及名称
        String filepath = (String) params.get("file_name");
        File outFile = new File(filepath);
        File directory = new File(filepath.substring(0,
                filepath.lastIndexOf(File.separator)));
        if (!directory.exists()) {
            FileUtils.createFolder(filepath.substring(0,
                    filepath.lastIndexOf(File.separator)));
        }
        Writer out = null;
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                outFile), "UTF-8"));
        t.process(params, out);
        out.flush();
        out.close();
    }

}
