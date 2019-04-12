package com.rds.trace.service;

import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.trace.model.RdsTraceAttachmentModel;
import com.rds.trace.model.RdsTraceAttachmentModelExt;
import freemarker.template.TemplateException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/21
 */
public interface RdsTraceAttachmentService {
    void insertAttachement(Map<String,Object> params);

    List<RdsTraceAttachmentModelExt> queryAttachment(Map<String,Object> params);

    String getDataPath();

    Map<String, Object> turnImg(Map<String, Object> params);

    void getImg(HttpServletResponse response, String filename);

    int queryCountAttachment(Map<String,Object> params);

    void download(HttpServletResponse response, String uuid);

    Map<String, Object> upload(String case_no,String year,MultipartFile[] files)
            throws Exception;

    void createTraceDoc(Map<String,Object> params) throws Exception;
}
