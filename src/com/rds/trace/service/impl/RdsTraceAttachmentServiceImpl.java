package com.rds.trace.service.impl;

import com.rds.code.image.ImgUtil;
import com.rds.code.utils.DownLoadUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.trace.mapper.RdsTraceAttachmentMapper;
import com.rds.trace.mapper.RdsTraceRegisterMapper;
import com.rds.trace.model.RdsTraceAttachmentModel;
import com.rds.trace.model.RdsTraceAttachmentModelExt;
import com.rds.trace.model.RdsTraceCaseInfoModel;
import com.rds.trace.model.RdsTraceCaseInfoModelExt;
import com.rds.trace.service.RdsTraceAttachmentService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
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
@Service("RdsTraceAttachmentService")
public class RdsTraceAttachmentServiceImpl
        implements RdsTraceAttachmentService {
    private static final String FILE_PATH = ConfigPath.getWebInfPath()
            + "spring" + File.separatorChar + "properties" + File.separatorChar
            + "config.properties";

    private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
            FILE_PATH, "trace_path");

    @Autowired
    @Setter
    private RdsTraceAttachmentMapper rdsTraceAttachmentMapper;

    @Autowired
    @Setter
    private RdsTraceRegisterMapper rdsTraceRegisterMapper;

    @Override
    public void insertAttachement(Map<String, Object> params) {
        params.put("uuid", UUIDUtil.getUUID());
        rdsTraceAttachmentMapper.insertAttachment(params);
    }

    @Override
    public List<RdsTraceAttachmentModelExt> queryAttachment(Map<String, Object> params) {
        return rdsTraceAttachmentMapper.queryAttachment(params);
    }

    @Override
    public int queryCountAttachment(Map<String, Object> params) {
        return rdsTraceAttachmentMapper.queryCountAttachment(params);
    }

    @Override
    public String getDataPath() {
        return PropertiesUtils.readValue(ConfigPath.getWebInfPath()
                + File.separatorChar + "spring" + File.separatorChar +
                "properties" + File.separatorChar + "config.properties", "trace_path");
    }

    @Override
    public void getImg(HttpServletResponse response, String filename) {
        DownLoadUtils.download(response, ATTACHMENTPATH + filename);
    }

    @Override
    public Map<String, Object> turnImg(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<String, Object>();
        File file = new File(ATTACHMENTPATH + params.get("path"));
        if (file.exists()) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(new FileInputStream(file));
                BufferedImage newImage = null;
                if ("left".equals(params.get("direction"))) {
                    newImage = ImgUtil.rotate90SX(image);
                } else {
                    newImage = ImgUtil.rotate90DX(image);
                }
                boolean flag = ImageIO.write(newImage, "jpg", file);
                if (flag) {
                    map.put("success", flag);
                } else
                    throw new IOException();
            } catch (IOException e) {
                e.printStackTrace();
                map.put("success", false);
            }
        }
        return map;
    }

    @Override
    public void download(HttpServletResponse response, String uuid) {
        Map<String,Object> params = new HashMap<>();
        params.put("uuid",uuid);
        String filename = rdsTraceAttachmentMapper.queryAttachment(params).get(0).
                getAttachment_path();
        File file = new File(ATTACHMENTPATH + filename);
        if (file.exists()) {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename="
                    + filename.substring(filename.lastIndexOf(File.separator)));
            response.setContentType("application/octet-stream; charset=utf-8");
            FileInputStream in = null;
            OutputStream toClient = null;
            try {
                in = new FileInputStream(file);
                // 得到文件大小
                int i = in.available();
                byte data[] = new byte[i];
                // 读数据
                in.read(data);
                toClient = response.getOutputStream();
                toClient.write(data);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (toClient != null) {
                    try {
                        toClient.flush();
                        toClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> upload(String case_no,String year,MultipartFile[] files)
            throws Exception {
        String msg = "";
        String attachmentPath = ATTACHMENTPATH + year+case_no +
                File.separatorChar + "jpg" + File.separatorChar;
        Map<String,Object> params = new HashMap<>();
        params.put("year",year);
        params.put("case_no",case_no);
        String case_id  = rdsTraceRegisterMapper.queryCaseId(params);
        if (case_id == null || case_id.equals("")) {
            return setMsg(false, "案例不存在,附件上传失败");
        }
        if (files.length > 0) {
            for (MultipartFile mfile : files) {
                    RdsFileUtil.delFolder(attachmentPath);
                    RdsFileUtil.fileUpload(
                            attachmentPath + mfile.getOriginalFilename(),
                            mfile.getInputStream());
                    rdsTraceAttachmentMapper.deleteJpg(case_id);
                    RdsFileUtil.unrar(attachmentPath + mfile.getOriginalFilename(),attachmentPath);
                    File directory = new File(attachmentPath);
                    File[] jpgs = directory.listFiles();
                    for(File file:jpgs){
                        if(!file.getName().endsWith("jpg"))
                            continue;
                        Map<String,Object> attachParams = new HashMap<>();
                        attachParams.put("case_id",case_id);
                        attachParams.put("uuid",UUIDUtil.getUUID());
                        attachParams.put("attachment_path",year+case_no + File.separatorChar +
                                "jpg" + File.separatorChar+file.getName());
                        //图片附件类型
                        attachParams.put("attachment_type",1);
                        attachParams.put("attachment_date", StringUtils.dateToTen(new Date()));
                        rdsTraceAttachmentMapper.insertAttachment(attachParams);
                    }
                    msg = msg + "文件：" + mfile.getOriginalFilename()
                            + "上传成功。</br>";
            }
            return setMsg(true, msg);
        } else
            return setMsg(false, msg + "未收到文件,上传失败");
    }

    @Override
    public void createTraceDoc(Map<String,Object> params) throws Exception {
        // 要填入模本的数据文件
        Map<String,Object> createparams = new HashMap<>();
        Map dataMap = new HashMap();
        String template_name = (String)params.get("template_name");
        String file_name = (String)params.get("file_name");
        // 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
        // 这里我们的模板是放在com.havenliu.document.template包下面
        RdsTraceCaseInfoModelExt rdsTraceCaseInfoModelExt =
                rdsTraceRegisterMapper.queryByCaseId((String)params.get("case_id"));
        dataMap.put("year", rdsTraceCaseInfoModelExt.getYear());
        String case_no = rdsTraceCaseInfoModelExt.getCase_no()+"";
        if(case_no.length()==1){
            case_no = "0"+case_no;
        }
        dataMap.put("case_no", case_no);
        dataMap.put("company_name", rdsTraceCaseInfoModelExt.getCompany_name());
        dataMap.put("receive_time", StringUtils.dateToChineseTen(rdsTraceCaseInfoModelExt.getReceive_time()));
        dataMap.put("case_type", rdsTraceCaseInfoModelExt.getCase_type());
        dataMap.put("case_attachment_desc",rdsTraceCaseInfoModelExt.getCase_attachment_desc());
        dataMap.put("identification_requirements", rdsTraceCaseInfoModelExt.getIdentification_requirements());
        dataMap.put("identification_time",StringUtils.dateToChineseTen(rdsTraceCaseInfoModelExt.getIdentification_date()));
        dataMap.put("case_local", rdsTraceCaseInfoModelExt.getCase_local());
        dataMap.put("sign_date",StringUtils.dateToChinese(StringUtils.dateToTen(new Date())));
        createparams.put("dataMap",dataMap);
        createparams.put("template_name",template_name);
        createparams.put("file_name",file_name);
        create(createparams);
    }

    public void create(Map<String,Object> params) throws IOException, TemplateException {
        // 要填入模本的数据文件
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        Map<String,Object> dataMap = (Map<String,Object>)params.get("dataMap");
        configuration.setClassForTemplateLoading(this.getClass(),
                "/com/rds/trace/template");
        Template t = null;
            // test.ftl为要装载的模板
            t = configuration.getTemplate((String)params.get("template_name"));
        // 输出文档路径及名称
        File outFile = new File((String)params.get("file_name"));

        Writer out = null;
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outFile),"UTF-8"));
            t.process(dataMap, out);
            out.flush();
            out.close();
    }



    private Map<String, Object> setMsg(boolean result, String message) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", result);
        map.put("message", message);
        return map;
    }
}
