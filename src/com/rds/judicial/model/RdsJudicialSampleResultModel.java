package com.rds.judicial.model;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author lys
 * @className  RdsJudicialSampleResultModel
 * @description  记录样本实验结果
 * @date 2015/4/13
 */
@Data
public class RdsJudicialSampleResultModel {
//    static{
//        String str = PropertiesUtils.readValue(ConfigPath.getWebInfPath()
//                + "spring" + File.separatorChar + "properties" + File.separatorChar
//                + "record.properties","record");
//        Set<String> recordModel = new HashSet<String>();
//        for(String field:str.split(",")){
//            recordModel.add(field);
//        }
//        RdsJudicialSampleResultModel.recordModel=recordModel;
//    }
    //从第12个属性开始为基因点位
    //public static final int BEGIN_FIELD=14;

    private Map<String,Object> record;
    //存放需比较的配置
    //public static Set<String> recordModel;
    //实验编号
    private String experiment_no;
    //实验日期
    private String experiment_date;
    //实验人
    private String experimenter;
    //样本编号
    private String sample_code;
    //案例编号
    private String case_code;
    //该样本实验结果字符串
    private String resultstr;
    //是否加位点
    private String ext_flag;
    //是否生效
    private String enable_flag;
    //样本归属人姓名
    private String sample_username;
    //样本归属人称谓
    private String sample_call;
    //MD5
    private String md5;
    //试剂名称
    private String reagent_name;
    //实验室编号
    private String laboratory_no;
    
    private String name;
    
    private String value;
    
    @Override
    public boolean equals(Object model){
        if(model instanceof RdsJudicialSampleResultModel)
            return this.getRecord().equals(((RdsJudicialSampleResultModel) model).getRecord());
        else return false;
    }

    public RdsJudicialSampleResultModel merge(RdsJudicialSampleResultModel m2) throws Exception {
        Map<String,Object> map = m2.getRecord();
        Set<String> set = map.keySet();
        for(String key:set){
            if(this.getRecord().get(key)!=null && m2.getRecord().get(key) !=null
                    && !this.getRecord().get(key).equals(m2.getRecord().get(key))){
                throw new Exception(this.getSample_code()+"样本加位点与基本位点相同基因的数据不匹配");
            }
            this.getRecord().put(key,m2.getRecord().get(key));
        }
        this.reagent_name = m2.getReagent_name();
        return this;
    }

}
