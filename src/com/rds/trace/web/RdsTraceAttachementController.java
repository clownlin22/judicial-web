package com.rds.trace.web;

import com.rds.code.utils.StringUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.model.MsgModelUtil;
import com.rds.trace.model.RdsTraceAttachmentModelExt;
import com.rds.trace.service.RdsTraceAttachmentService;
import com.rds.trace.service.RdsTraceRegisterService;
import com.rds.upc.model.RdsUpcMessageModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/21
 */
@Controller
@RequestMapping("trace/attachment")
public class RdsTraceAttachementController {
    //上传路径
    private String dataPath;
    //上传路径+文件名称
    private String wordFilePath;
    //相对路径
    private String wordRelativePath;

    private String pdfFilePath;

    @Autowired
    @Setter
    RdsTraceAttachmentService rdsTraceAttachmentService;

    @Autowired
    @Setter
    RdsTraceRegisterService rdsTraceRegisterService;

    @RequestMapping("wordUpload")
    @ResponseBody
    public RdsUpcMessageModel upload(@RequestParam String year,
                                     @RequestParam String case_no,
                                     @RequestParam String case_id,
                                     @RequestParam String attachment_type,
                                     @RequestParam MultipartFile[] mfiles)
            throws Exception {
        for(MultipartFile mFile:mfiles){
            if (mFile.isEmpty()) {
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.SAVE_FAILED);
            } else {
                dataPath = rdsTraceAttachmentService.getDataPath();
                wordFilePath = dataPath + year + case_no +
                        File.separatorChar + year + case_no+".doc";
                wordRelativePath = year + case_no +
                        File.separatorChar + year + case_no+".doc";
                pdfFilePath = dataPath + year + case_no +
                        File.separatorChar + year + case_no+".pdf";
                File wordFile = new File(wordFilePath);
                File pdfFile = new File(pdfFilePath);
                if(wordFile.exists()||pdfFile.exists()){
                    wordFile.delete();
                    pdfFile.delete();
                }
                RdsFileUtil.fileUpload(wordFilePath, mFile.getInputStream());
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("case_id",case_id);
                params.put("attachment_path",wordRelativePath);
                params.put("attachment_date", StringUtils.dateToTen(new Date()));
                params.put("attachment_type", attachment_type);
                RdsFileUtil.wordToPDF(wordFilePath,pdfFilePath);
                rdsTraceAttachmentService.insertAttachement(params);
                Map<String,Object> statusParams = new HashMap<String,Object>();
                statusParams.put("case_id",case_id);
                statusParams.put("status",1);
                rdsTraceRegisterService.updateStatus(statusParams);
            }
        }
        return MsgModelUtil.getModel(true, RdsUpcConstantUtil.SAVE_SUCCESS);
    }

    @RequestMapping("getPdf")
    public void getPdf(HttpServletResponse response,String year,String case_no) throws Exception{
        FileInputStream hFile = null;
        OutputStream toClient = null;
        try {
            dataPath = rdsTraceAttachmentService.getDataPath();
            hFile = new FileInputStream(dataPath + year + case_no +
                    File.separatorChar + year + case_no + ".pdf");
            //得到文件大小
            int i = hFile.available();
            byte data[] = new byte[i];
            //读数据
            hFile.read(data);

            //得到向客户端输出二进制数据的对象
            toClient = response.getOutputStream();
            //输出数据
            toClient.write(data);

            toClient.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            toClient.close();
            hFile.close();
        }
    }

    @RequestMapping("getWord")
    public Object getWord(HttpServletRequest request, String year,
                          String case_no, String case_id,
                          String template_name,
                          String print_flag) throws Exception{
//        FileInputStream hFile = null;
//        OutputStream toClient = null;
        dataPath = rdsTraceAttachmentService.getDataPath();
        File directory = new File(dataPath+year + case_no);
        if(!directory.exists()){
            directory.mkdirs();
        }
        String file_name = dataPath + year + case_no +
                File.separatorChar + year + case_no + ".doc";
        request.setAttribute("file_name",file_name);
        //try {
            dataPath = rdsTraceAttachmentService.getDataPath();
//            if("Y".equals(firstdownload)){
            Map<String,Object> params = new HashMap<>();
            params.put("year",year);
            params.put("case_no",case_no);
            params.put("case_id",case_id);
            params.put("template_name",template_name);
            params.put("file_name",file_name);
            if(!new File(file_name).exists()){
                rdsTraceAttachmentService.createTraceDoc(params);
            }
//            }else{
//                Map<String,Object> statusMap = new HashMap<>();
//                statusMap.put("case_id",case_id);
//                statusMap.put("status",4);
//                rdsTraceRegisterService.updateStatus(statusMap);
//            }
//            File file = new File(file_name);
//            hFile = new FileInputStream(file);
//            String fileName = file.getName();
//            //得到文件大小
//            int i = hFile.available();
//            byte data[] = new byte[i];
//            //读数据
//            hFile.read(data);
//            // 设置response的Header
//            response.reset();
//            response.setContentType("application/octet-stream; charset=utf-8");
//            response.addHeader("Content-Disposition", "attachment;filename=" +
//                    new String(fileName.replaceAll(" ", "").getBytes("utf-8"),"iso8859-1"));
            //得到向客户端输出二进制数据的对象
//            toClient = response.getOutputStream();
            //输出数据
//            toClient.write(data);
//            toClient.flush();
            if("Y".equals(print_flag)){
                return "pageoffice/print_word";
            }
            return "pageoffice/simple_word";
//        }catch (IOException e){
//            throw e;
//        }finally {
//            toClient.close();
//            hFile.close();
//        }
    }

    /**
     * 审核页面获取图片信息
     */
    @RequestMapping("getAttachMent")
    @ResponseBody
    public List<RdsTraceAttachmentModelExt> getAttachMent(
            @RequestBody Map<String, Object> params) {

        return rdsTraceAttachmentService.queryAttachment(params);
    }

    @RequestMapping("queryAttachMent")
    @ResponseBody
    public Object queryAttachMent(
            @RequestBody Map<String, Object> params) {
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("data",rdsTraceAttachmentService.queryAttachment(params));
        result.put("total",rdsTraceAttachmentService.queryCountAttachment(params));
        return result;
    }



    /**
     * 获取图片文件
     */
    @RequestMapping("getImg")
    public void getImg(HttpServletResponse response, String filename) {
        rdsTraceAttachmentService.getImg(response, filename);
    }

    @RequestMapping("turnImg")
    @ResponseBody
    public Map<String, Object> turnImg(@RequestBody Map<String, Object> params) {
        return rdsTraceAttachmentService.turnImg(params);
    }

    @RequestMapping("download")
    public void download(HttpServletResponse response, @RequestParam String uuid) {
        rdsTraceAttachmentService.download(response, uuid);
    }

    @RequestMapping("upload")
    @ResponseBody
    public void upload(@RequestParam String case_no,@RequestParam String year,
                       @RequestParam MultipartFile[] files, HttpServletResponse response)
            throws Exception {
        Map<String, Object> map = rdsTraceAttachmentService.upload(case_no,year, files);
        try {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().print(
                    "{\"success\":true,\"msg\":\""
                            + (String) map.get("message") + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
