package com.rds.judicial.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.mapper.RdsJudicialCaseParamMapper;
import com.rds.judicial.mapper.RdsJudicialExperimentMapper;
import com.rds.judicial.mapper.RdsJudicialIdentifyPerMapper;
import com.rds.judicial.mapper.RdsJudicialResultMapper;
import com.rds.judicial.mapper.RdsJudicialSampleMapper;
import com.rds.judicial.mapper.RdsJudicialSampleRelayMapper;
import com.rds.judicial.mapper.RdsJudicialSubCaseMapper;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialMissingDataModel;
import com.rds.judicial.model.RdsJudicialSampleResultModel;
import com.rds.judicial.service.RdsJudicialCaseAttachmentService;
import com.rds.judicial.service.RdsJudicialExceptionService;
import com.rds.judicial.service.RdsJudicialIdentifyPerService;
import com.rds.judicial.service.RdsJudicialReagentsService;
import com.rds.judicial.service.RdsJudicialSampleCompareService;
import com.rds.judicial.service.RdsJudicialSampleService;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/12/17
 */
public abstract class RdsJudicialSampleCompareServiceBase implements RdsJudicialSampleCompareService{

    public static final Map<String,Integer> map20 = new HashMap<>();

    public static final Map<String,Integer> mapExt = new HashMap<>();
    static {
        map20.put("D19S433",1);
        map20.put("D5S818",2);
        map20.put("D21S11",3);
        map20.put("D18S51",4);
        map20.put("D6S1043",5);
        map20.put("D3S1358",6);
        map20.put("D13S317",7);
        map20.put("D7S820",8);
        map20.put("D16S539",9);
        map20.put("CSF1PO",10);
        map20.put("Penta D",11);
        map20.put("vWA",12);
        map20.put("D8S1179",13);
        map20.put("TPOX",14);
        map20.put("Penta E",15);
        map20.put("TH01",16);
        map20.put("D12S391",17);
        map20.put("D2S1338",18);
        map20.put("FGA",19);
        mapExt.put("D19S433",1);
        mapExt.put("D6S474",2);
        mapExt.put("D12ATA63",3);
        mapExt.put("D22S1045",4);
        mapExt.put("D10S1248",5);
        mapExt.put("D1S1677",6);
        mapExt.put("D11S4463",7);
        mapExt.put("D1S1627",8);
        mapExt.put("D3S4529",9);
        mapExt.put("D2S441",10);
        mapExt.put("D6S1017",11);
        mapExt.put("D4S2408",12);
        mapExt.put("D17S1301",13);
        mapExt.put("D1GATA113",14);
        mapExt.put("D18S853",15);
        mapExt.put("D20S482",16);
        mapExt.put("D14S1434",17);
        mapExt.put("D9S1122",18);
        mapExt.put("D2S1776",19);
        mapExt.put("D10S1435",20);
        mapExt.put("D5S2500",21);
    }

    private static Logger logger = Logger.getLogger(RdsJudicialSampleCompareServiceBase.class);

    public enum SeasonEnum {
        //注：枚举写在最前面，否则编译出错
        male, female;

        private final static List<String> sample_calls = new LinkedList<>();

        static{
            sample_calls.add("father");
            sample_calls.add("son");
            sample_calls.add("grandpa");
            sample_calls.add("uncle");
            sample_calls.add("uncle2");
            sample_calls.add("nephew");
            sample_calls.add("brother");
            sample_calls.add("brother2");
        }

        public static SeasonEnum getSex(String sample_call) {
            if (sample_calls.contains(sample_call))
                return male;
            else
                return female;
        }
    }

    @Autowired
    protected RdsJudicialSampleMapper rdsJudicialSampleMapper;

    @Autowired
    protected RdsJudicialExperimentMapper rdsJudicialExperimentMapper;
    
    @Autowired
    protected RdsJudicialIdentifyPerMapper rdsJudicialIdentifyPerMapper;

    @Autowired
    protected RdsJudicialResultMapper rdsJudicialResultMapper;

    @Autowired
    protected RdsJudicialSubCaseMapper rdsJudicialSubCaseMapper;

    @Autowired
    protected RdsJudicialCaseParamMapper rdsJudicialCaseParamMapper;

    @Autowired
    protected RdsJudicialExceptionService rdsJudicialExceptionService;

    @Autowired
    protected RdsJudicialSampleService rdsJudicialSampleService;

    @Autowired
    protected RdsJudicialReagentsService rdsJudicialReagentsService;

    @Autowired
    protected RdsJudicialIdentifyPerService rdsJudicialIdentifyPerService;

    @Autowired
    protected RdsJudicialCaseAttachmentService rdsJudicialCaseAttachmentService;
    
    @Autowired
    protected RdsJudicialSampleRelayMapper rdsJudicialSampleRelayMapper;

    public static final String MOTHER = "mother";

    public static final String FATHER = "father";

    public static final String SON = "son";

    public static final String DAUGHTER = "daughter";

    @Override
    public void dataFileUpload(String filePath,InputStream inputStream) throws IOException {
    	String del = "";
    	if(isOSLinux())
    	{
    		System.out.println("filePath---------------------"+filePath.lastIndexOf('/'));
    		System.out.println("del==================="+filePath.lastIndexOf('/'));
			//如压缩包文件已上传过，删除对应文件夹；linux环境下修改为filePath.lastIndexOf('/')
	        del = filePath.substring(0,filePath.lastIndexOf('/'));
    	}else
    	{
			System.out.println("filePath---------------------"+filePath.lastIndexOf('\\'));
			System.out.println("del==================="+filePath.lastIndexOf('\\'));
			//如压缩包文件已上传过，删除对应文件夹；linux环境下修改为filePath.lastIndexOf('/')
	        del = filePath.substring(0,filePath.lastIndexOf('\\'));
    	}
        System.out.println("dellllllllllllllllllllllllllllllllllllllllllllllll"+del);
        //删除对应路径文件夹
        RdsFileUtil.delFolder(del);
        //上传新文件压缩包
        RdsFileUtil.fileUpload(filePath, inputStream);
    }

    @Override
    public String getDataPath() {
        return PropertiesUtils.readValue(ConfigPath.getWebInfPath()
                + File.separatorChar + "spring" + File.separatorChar +
                "properties" + File.separatorChar + "config.properties", "dataPath");
    }

	@Override
	public Map<String,Object> preValidateCheck(String directory, String laboratory_no)
			throws Exception {
		Map<String,Object> result = new HashMap<>();
    	//listfile 过滤 txt文件
        String[] ext = new String[]{"txt"};
        //listfile 过滤 pdf文件
        String[] pdf = new String[]{"pdf"};
        //实验数据压缩包内txt文件内容有误异常存放
        List<String> sampleTxtError = new LinkedList<String>();
        //过滤出指定文件目录后缀为pdf的文件集合
        Collection<File> pdfFiles = FileUtils.listFiles(new File(directory), pdf, false);
        //过滤出指定文件目录后最为txt的文件集合
        Collection<File> extFile = FileUtils.listFiles(new File(directory),ext,false);
        //校验压缩包内pdf文件和txt文件是否一一对应
        if(!checkFileNames(pdfFiles,extFile)){
            RdsFileUtil.delFolder(directory);
            throw new Exception("pdf与txt文件不匹配，请检查");
        }
        String returnStr="";
        //这一批文件中的记录的集合
        List<Map<String,Object>> list = new LinkedList<Map<String, Object>>();
        for (File file : extFile){
            Map<String,Object> params = new HashMap<String,Object>();
            String name = file.getName();
            //根据文件名获取样本编号
            String sample_code = name.split(".txt")[0];
            String md5 = null;
            //对上传文件生成md5加密信息，比对数据库是否存在相同加密信息，存在则说明数据导入过，否则保存改样本对应的md5加密信息
            if(!existMD5(md5 =RdsFileUtil.getMd5ByFile(file.getAbsolutePath())))
                params.put("md5",md5);
            else
                throw new Exception(sample_code + "样本该次数据已经导入，请核实");
            //实验室编号
            params.put("laboratory_no",laboratory_no);
            //样本编码
            params.put("sample_code",sample_code);
            //读TXT文件 一共多少行就是多少条数据
            List<String> results = FileUtils.readLines(file);
            //一个样本基本信息的记录：实验室编号，实验编号，样本编号，实验状态，试剂类型
            list.add(params);
            String sb = "";
            String[] strs = new String[21];
            //遍历txt文件每一行，从第二行开始
            for (int i = 1; i < results.size(); i++) {
            	//读取txt每一行
                String str = results.get(i);
                //根据每行制表符拆分行为数组（txt文件有问题会照成拆分有误，导致实验有误）
                String[] str1 = str.split("\\t");
                if (str1[5].equals("OL") || str1[5].isEmpty() || str1[6].equals("OL")) {
                    sampleTxtError.add(sample_code);//str[5]str[6]是每一行的值比如D19S433	B	14	15，14  15就是
                    break;
                }
                //txt第六列为性别位点，第一个值只能为X，男性XY，女性XX
                if (str1[5].equals("Y")){
                    throw new Exception(sample_code+"样本性别位点错误");
                }
                //params.put(str1[3].replaceAll(" ", "_"), str1[5] + "," + str1[6]);
                //位点名称
                params.put("name",str1[3]);
                //位点值
                params.put("value",str1[5] + "," + str1[6]);

                if(map20.get(str1[3])!=null){
                    String atestr = str1[5] + (str1[6].isEmpty() ? str1[5] : str1[6]);
                    int j = map20.get(str1[3]);
                    strs[j-1] = atestr;
                }
            
              
            }
            for(String strtemp:strs){
                if(strtemp!=null) {
                    sb = sb + strtemp;
                }
            }
            params.put("resultstr", sb);
            List<String> otherRecord = rdsJudicialSampleMapper
					.queryOtherRecord(params);
            if(otherRecord.size()>0)
            {
            	for (String string : otherRecord) {
            		returnStr += sample_code+"和"+string+"数据一致;";
				}
            }
        }
        if(!sampleTxtError.isEmpty()){
            StringBuffer sampleCodeMsg = new StringBuffer();
            for(String sampleTxtTempError:sampleTxtError){
                sampleCodeMsg.append(sampleTxtTempError);
                sampleCodeMsg.append(',');
            }
            sampleCodeMsg.deleteCharAt(sampleCodeMsg.length()-1);
            throw new Exception("如下样本编号："+sampleCodeMsg+"  文件格式有误");
        }
        if("".equals(returnStr))
        	result.put("success", true);
        else 
        	result.put("success", false);
        result.put("returnStr", returnStr);
        return result;
    
	}
	
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean preValidate(String directory, String experiment_no,
                               String laboratory_no,String reagent_name) throws Exception {
    	//listfile 过滤 txt文件
        String[] ext = new String[]{"txt"};
        //listfile 过滤 pdf文件
        String[] pdf = new String[]{"pdf"};
        //实验数据压缩包内系统未录入样本编号异常存放
        List<String> sampleNotFound = new LinkedList<String>();
        //实验数据压缩包内txt文件内容有误异常存放
        List<String> sampleTxtError = new LinkedList<String>();
        //实验压缩包txt文件于所选试剂位点不匹配异常存放；txt文件内第四列位点名称于系统设置好的试剂位点名称不一致即为试剂不匹配
        List<String> reagentsError = new LinkedList<String>();
        //案例未审核异常存放，已修改为判断样本未交接确认提示  listCheck
        Set<String> caseNotVerify = new HashSet<>();
        //过滤出指定文件目录后缀为pdf的文件集合
        Collection<File> pdfFiles = FileUtils.listFiles(new File(directory), pdf, false);
        //过滤出指定文件目录后最为txt的文件集合
        Collection<File> extFile = FileUtils.listFiles(new File(directory),ext,false);
        
        /* 判断案例样本编号是否审核通过 start add by yuanxiaobo*/
        List<String> lists = new ArrayList<>();
        for (File file : extFile){
        	String name = file.getName();
            String sample_code = name.split(".txt")[0];
            lists.add(sample_code);
        }
        Map<String,Object> mapCheck = new HashMap<>();
        mapCheck.put("sample_code", lists);
        StringBuilder strCheck = new StringBuilder();
        //校验案例状态是否正确
        List<Map<String,Object>> listVerify = rdsJudicialSampleRelayMapper.queryCaseVerifyBySample(mapCheck);
        if(listVerify.size()>0){
        	for (Map<String, Object> map : listVerify) {
        		strCheck.append(map.get("case_code"));
        		strCheck.append(",");
			}
        	throw new Exception("上传失败，如下案例状态有问题："+strCheck+" 请核实！");
        }
        //校验案例样本是否交接
//        List<Map<String,Object>> listCheck = rdsJudicialSampleRelayMapper.querySampleCodeConfirm(mapCheck);
//        if(listCheck.size()>0)
//        {
//        	for (Map<String, Object> map : listCheck) {
//        		//2表示审核未通过，0标识未审核
//				if("2".equals(map.get("confirm_state").toString()) || "0".equals(map.get("confirm_state").toString()))
//				{
//	             	strCheck.append(map.get("sample_code"));
//	             	strCheck.append(",");
//				}
//			}
//        	if(strCheck.length()>0)
//        		throw new Exception("上传失败，如下样本未交接确认："+strCheck+" 请核实！");
//        }else
//        {
//        	for (String string : lists) {
//        		strCheck.append(string);
//        		strCheck.append(",");
//			}
//        	throw new Exception("上传失败，如下样本未交接确认："+strCheck+" 请核实！");
//        }
        /* 判断案例样本是否审核通过 end add by yuanxiaobo*/
        
        //校验压缩包内pdf文件和txt文件是否一一对应
        if(!checkFileNames(pdfFiles,extFile)){
            RdsFileUtil.delFolder(directory);
            throw new Exception("pdf与txt文件不匹配，请检查");
        }
        //验证该实验下数据是否导入过
        Map<String,Object> checkExperimented = new HashMap<>();
        //实验编号
        checkExperimented.put("experiment_no",experiment_no);
        //实验室编号
        checkExperimented.put("laboratory_no",laboratory_no);
        //查询该实验编号下是否上传过实验数据
        int isexperimented =
                rdsJudicialExperimentMapper.isexperimented(checkExperimented);
        if(isexperimented>0)
            throw new Exception("该实验数据已经导入，请核实");
        //用于判断文件与所选试剂是否一致；根据试剂名查询设置好的位点信息
        TreeSet<String> atelier = rdsJudicialReagentsService.queryReagentAtelier(reagent_name);
        //存储压缩包内txt文件的位点信息
        TreeSet<String> atelierToCompare = new TreeSet<String>();
        //这一批文件中的记录的集合
        List<Map<String,Object>> list = new LinkedList<Map<String, Object>>();
      //存入批量数据，插入数据库一次
        List<RdsJudicialSampleResultModel> listOne = new ArrayList<RdsJudicialSampleResultModel>();
        for (File file : extFile){
        	listOne.clear();
            atelierToCompare.clear();
            Map<String,Object> params = new HashMap<String,Object>();
            String name = file.getName();
            //根据文件名获取样本编号
            String sample_code = name.split(".txt")[0];
            String md5 = null;
            //对上传文件生成md5加密信息，比对数据库是否存在相同加密信息，存在则说明数据导入过，否则保存改样本对应的md5加密信息
            if(!existMD5(md5 =RdsFileUtil.getMd5ByFile(file.getAbsolutePath())))
                params.put("md5",md5);
            else
                throw new Exception(sample_code + "样本该次数据已经导入，请核实");
            //实验室编号
            params.put("laboratory_no",laboratory_no);
            //实验编号
            params.put("experiment_no",experiment_no);
            //样本编码
            params.put("sample_code",sample_code);
            //是否进行实验
            params.put("enable_flag","Y");
            //读TXT文件 一共多少行就是多少条数据
            List<String> results = FileUtils.readLines(file);
            //判断是否加位点的数据
            Map<String,Object> reagentParams = new HashMap<>();
            reagentParams.put("laboratory_no",laboratory_no);
            reagentParams.put("reagent_name",reagent_name);
            String ext_flag = rdsJudicialReagentsService.queryExtFlag(reagentParams);
            //Y：加位点试剂，N：普通试剂
            params.put("ext_flag",ext_flag);

            //一个样本基本信息的记录：实验室编号，实验编号，样本编号，实验状态，试剂类型
            list.add(params);
            //查询样本性别map
            Map<String,Object> querySex = new HashMap<String, Object>();
            querySex.put("sample_code",sample_code);
            //查询登记案例的样本称谓
            String sex = rdsJudicialSampleMapper.querySampleCall(
                    querySex);
            //称谓为空，异常显示样本录入有误
            if(sex == null ||sex.equals("")){
                sampleNotFound.add(sample_code);
            }else {
                String sb = "";
                String[] strs = new String[21];
                
                //遍历txt文件每一行，从第二行开始
                for (int i = 1; i < results.size(); i++) {
                	RdsJudicialSampleResultModel modelSample=new RdsJudicialSampleResultModel();
                	modelSample.setLaboratory_no(laboratory_no);
                	modelSample.setExperiment_no(params.get("experiment_no").toString());
                    //样本编码
                	modelSample.setSample_code(params.get("sample_code").toString());
                    //是否进行实验
                	modelSample.setEnable_flag(params.get("enable_flag").toString());
                	modelSample.setExt_flag(params.get("ext_flag").toString());
                	//读取txt每一行
                    String str = results.get(i);
                    //根据每行制表符拆分行为数组（txt文件有问题会照成拆分有误，导致实验有误）
                    String[] str1 = str.split("\\t");
                    
                    //由于上面判断过案例样本是否确认通过，故此处去除判断案例是否审核通过
//                    for(RdsJudicialCaseInfoModel rdsJudicialCaseInfoModel :RdsJudicialCaseInfoModels){
//                        if(rdsJudicialCaseInfoModel.getVerify_state()!=3){
//                            caseNotVerify.add(rdsJudicialCaseInfoModel.getCase_code());
//                        }
//                    }
                    
                    if (str1[5].equals("OL") || str1[5].isEmpty() || str1[6].equals("OL")) {
                        sampleTxtError.add(sample_code);//str[5]str[6]是每一行的值比如D19S433	B	14	15，14  15就是
                        break;
                    }
                    //txt第六列为性别位点，第一个值只能为X，男性XY，女性XX
                    if (str1[5].equals("Y")){
                        throw new Exception(sample_code+"样本性别位点错误");
                    }
                    //判断称谓为女性，性别位点为男性，插入性别位点异常
                    else if(str1[6].equals("Y") && (sex.equals(MOTHER) || sex.equals(DAUGHTER))) {
                    	//根据样本编号查询没有结果的案例编号和状态
                    	List<RdsJudicialCaseInfoModel> RdsJudicialCaseInfoModels = rdsJudicialSampleMapper.queryCaseCodeBySampleCode(sample_code);
                        for(RdsJudicialCaseInfoModel rdsJudicialCaseInfoModel : RdsJudicialCaseInfoModels){
                            if(rdsJudicialCaseInfoModel.getCase_code() != null && !rdsJudicialCaseInfoModel.getCase_code().equals("")){
                            rdsJudicialExceptionService.insert("A005",
                                    rdsJudicialCaseInfoModel.getCase_code(), sample_code, null,laboratory_no);
                            }
                        }
                    } 
                    //判断称谓为男性，性别位点为女性插入性别位点异常
                    else if (str1[5].equals("X") && str1[6].isEmpty() && (sex.equals(FATHER) || sex.equals(SON))) {
                    	//根据样本编号查询没有结果的案例编号和状态
                        List<RdsJudicialCaseInfoModel> RdsJudicialCaseInfoModels1 = rdsJudicialSampleMapper.queryCaseCodeBySampleCode(sample_code);

                        for(RdsJudicialCaseInfoModel rdsJudicialCaseInfoModel : RdsJudicialCaseInfoModels1){
                            if(rdsJudicialCaseInfoModel.getCase_code() != null && !rdsJudicialCaseInfoModel.getCase_code().equals("")){
                                rdsJudicialExceptionService.insert("A005",rdsJudicialCaseInfoModel.getCase_code(), sample_code, null,laboratory_no);
                            }
                        }
                    }
                    //params.put(str1[3].replaceAll(" ", "_"), str1[5] + "," + str1[6]);
                    //位点名称
                    params.put("name",str1[3]);
                    modelSample.setName(params.get("name").toString());
                    //位点值
                    params.put("value",str1[5] + "," + str1[6]);
                    modelSample.setValue(params.get("value").toString());
                    //插入需比对的位点名称
                    atelierToCompare.add(str1[3]);
                    //存放基因座位点数值，即每个位点对应的值
                  //  rdsJudicialSampleMapper.addRecordData(params);
                    listOne.add(modelSample);
                    //判断是否加位点试剂
                    if("N".equals(params.get("ext_flag"))){
                        if(map20.get(str1[3])!=null){
                            String atestr = str1[5] + (str1[6].isEmpty() ? str1[5] : str1[6]);
                            int j = map20.get(str1[3]);
                            strs[j-1] = atestr;
                        }
                    }else{
//                    性别位点不计入resultstr
//                    拼接resultstr
                        if (str1[5].endsWith("X")) {
                            continue;
                        }
                        sb+=str1[5];
                        sb+=(str1[6].isEmpty() ? str1[5] : str1[6]);
                    }
                }
                rdsJudicialSampleMapper.addRecordDataAll(listOne);
                for(String strtemp:strs){
                    if(strtemp!=null) {
                        sb = sb + strtemp;
                    }
                }
                params.put("resultstr", sb);
                rdsJudicialSampleMapper.addRecordStr(params);// tb_judicial_history_compare插入
            }
            //判断试剂是否选择正确
            if(!atelier.equals(atelierToCompare)){
                reagentsError.add(sample_code);
            }
        }
        if(!sampleTxtError.isEmpty()){
            StringBuffer sampleCodeMsg = new StringBuffer();
            for(String sampleTxtTempError:sampleTxtError){
                sampleCodeMsg.append(sampleTxtTempError);
                sampleCodeMsg.append(',');
            }
            sampleCodeMsg.deleteCharAt(sampleCodeMsg.length()-1);
            throw new Exception("如下样本编号："+sampleCodeMsg+"  文件格式有误");
        }
        if(!sampleNotFound.isEmpty()){
            StringBuffer sampleCodeMsg = new StringBuffer();
            for(String tempSampleCode:sampleNotFound){
                sampleCodeMsg.append(tempSampleCode);
                sampleCodeMsg.append(',');
            }
            sampleCodeMsg.deleteCharAt(sampleCodeMsg.length()-1);
            throw new Exception("如下样本编号："+sampleCodeMsg+"  在系统里没有找到，请核实");
        }
        if(!caseNotVerify.isEmpty()){
            StringBuffer caseCodeMsg = new StringBuffer();
            for(String tempCaseCode:caseNotVerify){
                caseCodeMsg.append(tempCaseCode);
                caseCodeMsg.append(',');
            }
            caseCodeMsg.deleteCharAt(caseCodeMsg.length()-1);//删除某个位置的单个字符
            throw new Exception("如下案例："+caseCodeMsg+"  没有审核，请核实");
        }
        if(!reagentsError.isEmpty()){
            StringBuffer reagentsErrorMsg = new StringBuffer();
            for(String tempSampleCode:reagentsError){
                reagentsErrorMsg.append(tempSampleCode);
                reagentsErrorMsg.append(',');
            }
            reagentsErrorMsg.deleteCharAt(reagentsErrorMsg.length()-1);
            throw new Exception("如下样本编号："+reagentsErrorMsg+"  所选试剂与文件不匹配");
        }
        //保存该样本相关信息
        return saveData(list);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int updateRecord(Map<String, Object> params) {
        return rdsJudicialSampleMapper.update(params);
    }

    @Override
    public List<RdsJudicialMissingDataModel> queryMissingData(Map<String, Object> params) throws Exception {
        return rdsJudicialSampleMapper.queryMissingData(params);
    }

    @Override
    public int queryCountMissingData(Map<String, Object> params) {
        return rdsJudicialSampleMapper.queryCountMissingData(params);
    }

    public Map<String,Object> twoModelCompare(
            RdsJudicialSampleResultModel model1, RdsJudicialSampleResultModel model2)
            throws Exception {
        logger.debug("case_code==========="+model1.getCase_code()+"beginCompare==============================");
        Map<String,Object> m1 = model1.getRecord();
        Map<String,Object> m2 = model2.getRecord();
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        int count = 0;
        Set<String> m1key= m1.keySet();
        Iterator it = m1key.iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            if(!match((String)m1.get(key),(String)m2.get(key))){
                sb.append(key);
                sb.append(",");
                count++;
            }
        }
        if(!sb.toString().equals(""))
            sb.deleteCharAt(sb.length()-1);
        map.put("count",count);
        map.put("unmatchedNode",sb.toString());
        return map;
    }

    /**
     *
     * @param son 孩子样本
     * @param model1
     * @param model2
     * @return
     * @throws Exception
     */
    public Map<String,Object> threeModelCompare(RdsJudicialSampleResultModel son,
                                                 RdsJudicialSampleResultModel model1,
                                                 RdsJudicialSampleResultModel model2)
            throws Exception {
        logger.debug("case_code==========="+son.getCase_code()+"beginCompare==============================");
        Map<String,Object> m1 = model1.getRecord();
        Map<String,Object> m2 = model2.getRecord();
        Map<String,Object> s = son.getRecord();
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        
        //三个人一对一比较不匹配
//        Map<String,Object> unmatchMap = new HashMap<String,Object>();
//        StringBuffer sbf = new StringBuffer();
//        int countf =0;
//        StringBuffer sbm = new StringBuffer();
//        int  countm =0;
        
        int count = 0;
        Set<String> m1key= m1.keySet();
        Iterator it = m1key.iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            if(!match((String)s.get(key),(String)m1.get(key),(String)m2.get(key))){
                sb.append(key);
                sb.append(",");
                count++;
            }
        }
        //孩子和父亲或者母亲两两比对
//        while (it.hasNext()){
//            String key = (String)it.next();
//            if(!match((String)s.get(key),(String)m1.get(key))){
//                sbf.append(key);
//                sbf.append(",");
//                countf++;
//            }
//            if(!match((String)s.get(key),(String)m2.get(key))){
//                sbm.append(key);
//                sbm.append(",");
//                countm++;
//            }  
//        }
//        if(!sbf.toString().equals("")){
//            sbf.deleteCharAt(sb.length()-1);
//        map.put("countf",countf);
//        map.put("unmatchedNodef",sbf.toString());
//        }
//        if(!sbm.toString().equals("")){
//            sbm.deleteCharAt(sb.length()-1);
//            map.put("countm",countm);
//        map.put("unmatchedNodem",sbm.toString());
//        }
        
        
        if(!sb.toString().equals(""))
            sb.deleteCharAt(sb.length()-1);
        map.put("count",count);
        map.put("unmatchedNode",sb.toString());
        return map;
    }

    /**
     * 两两匹配
     * @param field1
     * @param field2
     * @return
     */
    protected boolean match(String field1,String field2){
        if(org.apache.commons.lang.StringUtils.isBlank(field1)||
                org.apache.commons.lang.StringUtils.isBlank(field2)){
            return true;
        }
        String[] strs1 = field1.split(",");
        String[] strs2 = field2.split(",");
        for(String str1:strs1){
            for(String str2:strs2){
                if(str1.equals(str2))
                    return true;
            }
        }
        return false;
    }

    /**
     * 一家人匹配
     * @param son
     * @param field1
     * @param field2
     * @return
     */
    protected boolean match(String son,String field1,String field2){
        String[] sons = son.split(",");
        if(sons.length==1){
            String[] newSons = new String[2];
            System.arraycopy(sons,0,newSons,0,sons.length);
            newSons[1] = newSons[0];
            if((match(newSons[0]+",",field1)&&match(newSons[1]+",",field2))||
                    (match(newSons[0]+",",field2)&&match(newSons[1]+",",field1))){
                return true;
            }
        }else if((match(sons[0]+",",field1)&&match(sons[1]+",",field2))||
                (match(sons[0]+",",field2)&&match(sons[1]+",",field1))){
            return true;
        }
        return false;
    }

    private boolean saveData(List<Map<String,Object>> list) throws Exception {

        for (Map<String,Object> params : list) {
            rdsJudicialSampleMapper.insert(params);
        }
        return true;
    }

    private boolean checkFileNames(Collection<File> pdfFiles,Collection<File> extFiles) throws Exception{
        Set<String> pdfNames = new TreeSet<String>();
        Set<String> extNames = new TreeSet<String>();
        for(File file : pdfFiles){
            pdfNames.add(file.getName().split("\\.")[0]);
        }
        for(File file : extFiles){
            extNames.add(file.getName().split("\\.")[0]);
        }
        logger.debug("pdfnames==========================="+pdfNames);
        logger.debug("txtnames==========================="+extNames);
        if(pdfNames.isEmpty()||extNames.isEmpty()){
            throw new Exception("请确认压缩包中是否包含子文件夹");
        }
        if(pdfNames.equals(extNames))
            return true;
        else return false;
    }

    private boolean existMD5(String md5){
        if(rdsJudicialSampleMapper.queryMD5(md5)>0)
            return true;
        return false;
    }
    
    public static boolean isOSLinux() {
        Properties prop = System.getProperties();

        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().indexOf("linux") > -1) {
            return true;
        } else {
            return false;
        }
    }
}
