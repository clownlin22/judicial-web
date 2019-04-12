package com.rds.judicial.service.impl;

import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.model.RdsJudicialCompareResultTongBaoModel;
import com.rds.judicial.model.RdsJudicialIdentifyModel;
import com.rds.judicial.model.RdsJudicialSampleResultModel;
import com.rds.judicial.model.RdsJudicialSubCaseInfoModel;
import com.rds.upc.model.RdsUpcUserModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/12/17
 */
@Service("RdsJudicialTongBaoCompareServiceImpl")
public class RdsJudicialTongBaoCompareServiceImpl extends RdsJudicialSampleCompareServiceBase{
	
	@Autowired
	private RdsJudicialRegisterMapper rdsJudicialRegisterMapper;
    @Override
    public String beginCompare(String experiment_no, String laboratory_no, String reagent_name, String identify_id,String exception_per,RdsUpcUserModel user) throws Exception {
        Map<String,Object> paramsExperiment = new HashMap<String, Object>();
        paramsExperiment.put("experiment_no",experiment_no);
        paramsExperiment.put("laboratory_no",laboratory_no);
        paramsExperiment.put("reagent_name",reagent_name);
        //查看该次实验是否为加位点
        String ext_flag = rdsJudicialReagentsService.queryExtFlag(paramsExperiment);
        //更新该次实验所用试剂
        rdsJudicialExperimentMapper.updateReagentName(paramsExperiment);
        //根据实验编号找出该次实验样本集合，并对样本数据进行填充
        List<RdsJudicialSampleResultModel> list =
                rdsJudicialSampleService.queryByExperimentNo(paramsExperiment);

        //该次实验有多少case
        Set<String> set = new HashSet<String>();

        //通过case找到分case
        Set<String> case_ids = new HashSet<>();
        for(RdsJudicialSampleResultModel model:list){
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("case_code",model.getCase_code());
            //查询没有结果的子案例
            List<String> subCaseCode = rdsJudicialSubCaseMapper.querySubCase(params);

            for(String sub_case:subCaseCode){
                //添加鉴定人信息
//                String[] identify_ids = identify_id.split(",");
//                for(int i=0;i<identify_ids.length;i++){
//                    Map<String,Object> identifyPer = new HashMap<>();
//                    identifyPer.put("trans_date",new Date());
//                    identifyPer.put("identify_id",identify_ids[i]);
//                    identifyPer.put("case_id",rdsJudicialSubCaseMapper.queryCaseIdBySubCaseCode(sub_case));
//                    rdsJudicialIdentifyPerService.insertCaseToIdentify(identifyPer);
//                }
                case_ids.add(rdsJudicialSubCaseMapper.queryCaseIdBySubCaseCode(sub_case));
                set.add(sub_case);
            }
        }
//        //添加鉴定人信息
//        for(String case_id : case_ids) {
//            String[] identify_ids = identify_id.split(",");
//            for (int i = 0; i < identify_ids.length; i++) {
//                Map<String, Object> identifyPer = new HashMap<>();
//                identifyPer.put("trans_date", new Date());
//                identifyPer.put("identify_id", identify_ids[i]);
//                identifyPer.put("case_id", case_id);
//                rdsJudicialIdentifyPerService.insertCaseToIdentify(identifyPer);
//            }
//        }
        
      //添加鉴定人信息
        for(String case_id : case_ids) {
        	 String[] identify_ids = identify_id.split(",");
        	//先查询之前该案例是否有鉴定人 有则删除
        	   List<RdsJudicialIdentifyModel> rdsJudicialIdentifyModels =
                       rdsJudicialIdentifyPerService.queryIdentifyByCaseId(case_id);
        	   if (rdsJudicialIdentifyModels != null &&!rdsJudicialIdentifyModels.isEmpty()) {
 		    	  Map<String, Object> params = new HashMap<>();
       		    params.put("case_id", case_id);
       		   rdsJudicialIdentifyPerMapper.deleteIdentifyByCaseid(params);
       		   for (int i = 0; i < identify_ids.length; i++) {
	                Map<String, Object> identifyPer = new HashMap<>();
	                identifyPer.put("trans_date", new Date());
	                identifyPer.put("identify_id", identify_ids[i]);
	                identifyPer.put("case_id", case_id);
	                rdsJudicialIdentifyPerService.insertCaseToIdentify(identifyPer);
	            }
			}else {
			     for (int i = 0; i < identify_ids.length; i++) {
		                Map<String, Object> identifyPer = new HashMap<>();
		                identifyPer.put("trans_date", new Date());
		                identifyPer.put("identify_id", identify_ids[i]);
		                identifyPer.put("case_id", case_id);
		                rdsJudicialIdentifyPerService.insertCaseToIdentify(identifyPer);
		            }
			}
        }
        
        //基本点位比对
        for(String sub_case_code:set) {
            validateSample(sub_case_code,"",laboratory_no,exception_per,user);
        }
        return "success";
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Map<Boolean,String> validateSample(String sub_case_code,String ext_flag,String laboratory_no,String exception_per,RdsUpcUserModel user) throws Exception {
        //返回结果
        Map<Boolean,String> result = new HashMap<Boolean, String>();
        //根据sub_case_code查询样本编号，最多3个
        RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel =
                rdsJudicialSubCaseMapper.querySubCaseRecord(sub_case_code);
        List<String> sample_codes = new LinkedList<String>();
        sample_codes.add(rdsJudicialSubCaseInfoModel.getSample_code1());
//        if(rdsJudicialSubCaseInfoModel.getSample_code2()!=null)
//            sample_codes.add(rdsJudicialSubCaseInfoModel.getSample_code2());
        sample_codes.add(rdsJudicialSubCaseInfoModel.getSample_code3());

        //用于存放需比对的样本对象
        List<RdsJudicialSampleResultModel> toCompare =
                new LinkedList<RdsJudicialSampleResultModel>();
        Map<String, Object> params = new HashMap<String, Object>();
        List<RdsJudicialSampleResultModel> enableResult = null;

        for (String sample_code : sample_codes) {
            params.put("enable_flag", "Y");
            params.put("ext_flag", "N");
            params.put("sample_code", sample_code);
            params.put("case_id",rdsJudicialSubCaseMapper.queryCaseIdBySubCaseCode(sub_case_code));

            //查询有效样本编号是否唯一
            enableResult = rdsJudicialSampleMapper.queryBySampleCode(params);
            Set<Map<String,Object>> atelierData = new HashSet();
            List<String> experiment_nos = rdsJudicialSampleMapper.queryExperiment(params);
            for(String experiment_no : experiment_nos){
                Map<String,Object> atelierDataParam = new HashMap<>();
                atelierDataParam.put("sample_code",sample_code);
                atelierDataParam.put("experiment_no",experiment_no);
                atelierDataParam.put("laboratory_no",laboratory_no);
                atelierData.add(rdsJudicialSampleService.
                        queryOneRecordData(atelierDataParam));
            }
            if (enableResult == null || enableResult.size() == 0||atelierData==null || atelierData.size()==0){
                result.put(false, "该案例缺少样本数据，请核实");
                return result;
            }
            //有效样本编号数量大于1
            else if (atelierData.size()>1) {
                rdsJudicialExceptionService.insert("A004",rdsJudicialSubCaseMapper.queryMainCaseCode(sub_case_code),sample_code,"",laboratory_no);
                result.put(false, "该案例同一样本号下存在不同样本数据，请核实");
                return result;
            }else {
                for(Map map :atelierData){
                    enableResult.get(0).setRecord(map);
                }
            }
            //查询加位点有效样本编号是否唯一
            params.put("ext_flag", "Y");
            params.put("case_id",rdsJudicialSubCaseMapper.queryCaseIdBySubCaseCode(sub_case_code));
            Set<Map<String,Object>> atelierExtData = new HashSet();
            List<String> experiment_nos_ext = rdsJudicialSampleMapper.queryExperiment(params);
            for(String experiment_no : experiment_nos_ext){
                Map<String,Object> atelierDataParam = new HashMap<>();
                atelierDataParam.put("sample_code",sample_code);
                atelierDataParam.put("experiment_no",experiment_no);
                atelierDataParam.put("laboratory_no",laboratory_no);
                atelierExtData.add(rdsJudicialSampleService.
                        queryOneRecordData(atelierDataParam));
            }
            List<RdsJudicialSampleResultModel> enableExtResult =
                    rdsJudicialSampleMapper.queryBySampleCode(params);
            if (enableExtResult == null || enableExtResult.size() == 0||atelierExtData==null || atelierExtData.size()==0) {
                result.put(false, "该案例缺少加位点样本数据，请核实");
                return result;
            }
            //有效样本编号数量大于1
            else if (atelierExtData.size() > 1) {
                rdsJudicialExceptionService.insert("A004",rdsJudicialSubCaseMapper.queryMainCaseCode(sub_case_code),sample_code,"",laboratory_no);
                result.put(false, "该案例同一样本号下存在不同样本数据，请核实");
                return result;
            }else{
                ext_flag = "Y";
                for(Map map :atelierExtData){
                    enableExtResult.get(0).setRecord(map);
                }
                enableResult.add(1, enableExtResult.get(0));
            }
            //需要进行比对的样本,toCompare为size为2的list,
            //list里面包含非加位点和加位点的样本信息
            toCompare.add(enableResult.get(0));
            toCompare.add(enableResult.get(1));
        }
        return compare(toCompare,sub_case_code,laboratory_no,exception_per);
    }

    private Map<Boolean,String> compare(List<RdsJudicialSampleResultModel> toCompare,
                                       String sub_case_code,String laboratory_no,String exception_per) throws Exception{
        Map<Boolean,String> result = new HashMap<Boolean, String>();
        //count->ibscount
        RdsJudicialSampleResultModel person1_part1 = toCompare.get(0);
        RdsJudicialSampleResultModel person1_part2 = toCompare.get(1);
        RdsJudicialSampleResultModel person2_part1 = toCompare.get(2);
        RdsJudicialSampleResultModel person2_part2 = toCompare.get(3);


        Map<String,Object> map1 = twoModelCompare(person1_part1,person2_part1);
        Map<String,Object> map2 = twoModelCompare(person1_part2,person2_part2);
        int ibs_count = (Integer)map1.get("count")+(Integer)map2.get("count");
        RdsJudicialCompareResultTongBaoModel rdsJudicialCompareResultTongBaoModel =
                new RdsJudicialCompareResultTongBaoModel();
        rdsJudicialCompareResultTongBaoModel.setCase_code(sub_case_code);
        if(ibs_count>=42) {
            rdsJudicialCompareResultTongBaoModel.setFinal_result_flag("passed");
            Map<String,Object> sub_case_result = new HashMap<>();
            sub_case_result.put("result","passed");
            sub_case_result.put("laboratory_no",laboratory_no);
            rdsJudicialSubCaseMapper.update(sub_case_result);
        }
        rdsJudicialCompareResultTongBaoModel.setIbs_count(ibs_count);
        rdsJudicialCompareResultTongBaoModel.setReagent_name(person1_part1.getReagent_name());
        rdsJudicialCompareResultTongBaoModel.setUsername_1(person1_part1.getSample_username());
        rdsJudicialCompareResultTongBaoModel.setUsername_2(person2_part1.getSample_username());
        rdsJudicialCompareResultTongBaoModel.setSample_code1(person1_part1.getSample_code());
        rdsJudicialCompareResultTongBaoModel.setSample_code2(person2_part1.getSample_code());
        rdsJudicialCompareResultTongBaoModel.setLaboratory_no(laboratory_no);
        rdsJudicialResultMapper.addTongbaoResult(rdsJudicialCompareResultTongBaoModel);
        
        //更新案例基本表里的比对时间
        Map<String, Object> compareMap = new HashMap<String, Object>();
        compareMap.put("case_code", rdsJudicialCompareResultTongBaoModel.getCase_code().split("_")[0]);
        compareMap.put("compare_date", new Date());
        rdsJudicialRegisterMapper.updateCaseCompareDate(compareMap);
//        System.out.println(map1);
//        System.out.println(map2);
//        String sex1 = SeasonEnum.getSex(person1.getSample_call()).toString();
//        String sex2 = SeasonEnum.getSex(person2.getSample_call()).toString();
//        if(sex1.equals(sex2)){
//            //同男性
//            if(sex1.equals("male")){
//
//            }
//            //同男女性
//            else{
//
//            }
//        //一男一女
//        }else{
//
//        }
        result.put(true,"比对成功");
        return result;
    }

    private boolean yCompareForMan(RdsJudicialSampleResultModel model1,
                                   RdsJudicialSampleResultModel model2){
        return model1.getRecord().equals(model2.getRecord());
    }

    private boolean yCompareForWoman(RdsJudicialSampleResultModel model1,
                                   RdsJudicialSampleResultModel model2) throws Exception{
        if((Integer)twoModelCompare(model1,model2).get("count")<2)
            return true;
        return false;
    }

    private int countMatch(String field1,String field2){
        String[] fields1 = field1.split(",");
        String[] fields2 = field2.split(",");

        if(field1.equals(field2))
            return 2;
        for(String str1 : fields1){
            for(String str2 : fields2){
            if(str1.equals(str2))
                return 1;
            }
        }
        return 0;
    }

    @Override
    public Map<String,Object> twoModelCompare(
            RdsJudicialSampleResultModel model1, RdsJudicialSampleResultModel model2)
            throws Exception {
        Map<String,Object> m1 = model1.getRecord();
        Map<String,Object> m2 = model2.getRecord();
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        int count = 0;
        Set<String> m1key= m1.keySet();
        Iterator it = m1key.iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            count += countMatch((String)m1.get(key),(String)m2.get(key));
        }
        if(!sb.toString().equals(""))
            sb.deleteCharAt(sb.length()-1);
        map.put("count",count);
        map.put("unmatchedNode",sb.toString());
        return map;
    }

    private boolean addResult(String sub_case_code,RdsJudicialSampleResultModel parent1,
                                  RdsJudicialSampleResultModel child,String ext_flag,int count,
                                  boolean final_flag,String unmatched_node,String laboratory_no) throws Exception {
        return true;
    }

}
