package com.rds.judicial.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialMissingDataModel;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * @author lys
 * @className 样本对比service
 * @description
 * @date 2015/4/10
 */
public interface RdsJudicialSampleCompareService {
    String getDataPath();

    void dataFileUpload(String filePath,InputStream inputStream) throws Exception;

    //对于上传文件进行校验，包括文件完整性，样本对应的称谓（性别）校验，以及案例状态校验
    boolean preValidate(String directory,String experiment_no,String laboratory_no,String reagent_name) throws Exception;

    //对于某个子案例数据的正确、完整性进行校验，校验通过后进行子案例的比对
    Map<Boolean,String> validateSample(String sub_case_code,String ext_flag,String laboratory_no,String exception_per,RdsUpcUserModel user) throws Exception;

    //比对
    String beginCompare(String experiment_no,String laboratory_no,String reagent_name,String identify_id,String exception_per,RdsUpcUserModel user) throws Exception;

    int updateRecord(Map<String,Object> params);

    List<RdsJudicialMissingDataModel> queryMissingData(Map<String,Object> params) throws Exception;

    int queryCountMissingData(Map<String,Object> params);
    
    //对于二次采样上传文件进行校验
    Map<String,Object> preValidateCheck(String directory,String laboratory_no) throws Exception;

}
