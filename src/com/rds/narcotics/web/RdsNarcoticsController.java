package com.rds.narcotics.web;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.narcotics.model.RdsNarcoticsModel;
import com.rds.narcotics.model.RdsNarcoticsResponse;
import com.rds.narcotics.service.RdsNarcoticsService;
import com.rds.upc.model.RdsUpcUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @Author: lxy
 * @Date: 2019/3/19 16:37
 */
@RequestMapping("narcotics/register")
@RestController
public class RdsNarcoticsController {

    private static final String FILE_PATH = ConfigPath.getWebInfPath()
            + "spring" + File.separatorChar + "properties" + File.separatorChar
            + "config.properties";
    private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
            FILE_PATH, "narcotics_path");
    private static final String TEMP_ZIP_PATH = PropertiesUtils.readValue(
            FILE_PATH, "temp_zip_path");

    @Autowired
    private RdsNarcoticsService rdsNarcoticsService;

    /**
     *
    添加毒品案例
     */
    @RequestMapping(value ="addCaseInfo")
    public boolean addCaseInfo(RdsNarcoticsModel rdsNarcoticsModel,HttpSession session) {
        if (session.getAttribute("user") != null) {
            RdsUpcUserModel user = (RdsUpcUserModel) session
                    .getAttribute("user");
            rdsNarcoticsModel.setCase_in_per(user.getUserid());
        }
        return rdsNarcoticsService.addCaseInfo(rdsNarcoticsModel);
    }

    /**
     *
    获取鉴定人
     */
    @RequestMapping(value = "getIdentificationPer")
    public Map<String, Object> getIdentificationPer() {
        return rdsNarcoticsService.getIdentificationPer();
    }

    /**
     *
    初始化   获取毒品案例
     */
    @RequestMapping("getCaseInfo")
    public RdsNarcoticsResponse getCaseInfo(@RequestBody Map<String, Object> params, HttpSession session) {

        if (session.getAttribute("user") != null) {
            RdsUpcUserModel user = (RdsUpcUserModel) session
                    .getAttribute("user");
            params.put("case_in_per", user.getUserid());
        }

        return rdsNarcoticsService.getCaseInfo(params);
    }

    /**
     *
    查询是否存在案例号
     */
    @RequestMapping("exsitCaseNum")
    public boolean exsitCaseNum(@RequestBody Map<String, Object> params) {
        return rdsNarcoticsService.exsitCaseNum((String) params.get("case_num"));
    }

    /**
     *
    修改毒品案例
     */
    @RequestMapping(value ="updateCaseInfo")
    public boolean updateCaseInfo(RdsNarcoticsModel rdsNarcoticsModel,HttpSession session) {
        if (session.getAttribute("user") != null) {
            RdsUpcUserModel user = (RdsUpcUserModel) session
                    .getAttribute("user");
            rdsNarcoticsModel.setCase_in_per(user.getUserid());
        }
        return rdsNarcoticsService.updateCaseInfo(rdsNarcoticsModel);
    }

    /**
     *
    删除毒品案例
     */
    @RequestMapping(value ="deletecaseInfo")
    public boolean deletecaseInfo(@RequestBody Map<String, Object> params) {
        String case_ids=(String)params.get("case_ids");
        List<String> list= Arrays.asList(case_ids.split(","));
        params.put("list",list);
        return rdsNarcoticsService.deletecaseInfo(params);
    }

    /**
     *
    下载word
     */
    @RequestMapping("getNarcoticsWord")
    public void getNarcoticsWord(HttpServletResponse response,
                               @RequestParam String case_id,@RequestParam String case_num) throws Exception {
        FileInputStream hFile = null;
        OutputStream toClient = null;
        File directory = new File(ATTACHMENTPATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String file_name = ATTACHMENTPATH + File.separatorChar + case_num
                + File.separatorChar + case_num + ".doc";
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("case_id",case_id);
            params.put("file_name",file_name);
            rdsNarcoticsService.createNarcoticsDocByCaseId(params);
            File file = new File(file_name);
            hFile = new FileInputStream(file);
            String fileName = file.getName();
            // 得到文件大小
            int i = hFile.available();
            byte data[] = new byte[i];
            // 读数据
            hFile.read(data);
            // 设置response的Header
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(
                    fileName.replaceAll(" ", "").getBytes("utf-8"),
                    "iso8859-1"));
            // 得到向客户端输出二进制数据的对象
            toClient = response.getOutputStream();
            // 输出数据
            toClient.write(data);
            toClient.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            toClient.close();
            hFile.close();
        }
    }

    /**
     *
    批量下载word
     */
    @RequestMapping("getZip")
    @ResponseBody
    public String getZip(@RequestBody Map<String, String>[] data,
                         HttpServletRequest request) throws Exception {
        RdsFileUtil.delFile(TEMP_ZIP_PATH + "narcotics.zip");
        File directory = new File(TEMP_ZIP_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        List<File> list = new LinkedList<>();
        for (int i = 0; i < data.length; i++) {
            String case_num = data[i].get("case_num");
            String file_name = ATTACHMENTPATH + File.separatorChar + case_num
                    + File.separatorChar + case_num + ".doc";
            Map<String, Object> params = new HashMap<>();
            params.put("case_id",data[i].get("case_id"));
            params.put("file_name",file_name);
            rdsNarcoticsService.createNarcoticsDocByCaseId(params);
            list.add(new File(file_name));
        }
        RdsFileUtil.zipFiles(list, new File(TEMP_ZIP_PATH + "narcotics.zip"));
        return "success";
    }

    @RequestMapping("getZipAfter")
    public void getZipAfter(HttpServletResponse response) throws Exception {
        FileInputStream hFile = null;
        OutputStream toClient = null;
        try {
            File file = new File(TEMP_ZIP_PATH + "/narcotics.zip");
            hFile = new FileInputStream(file);
            String fileName = file.getName();
            // 得到文件大小
            int i = hFile.available();
            byte data[] = new byte[i];
            // 读数据
            hFile.read(data);
            // 设置response的Header
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(
                    fileName.replaceAll(" ", "").getBytes("utf-8"),
                    "iso8859-1"));
            // 得到向客户端输出二进制数据的对象
            toClient = response.getOutputStream();
            // 输出数据
            toClient.write(data);
            toClient.flush();
        } finally {
            hFile.close();
            toClient.close();
        }
    }


}
