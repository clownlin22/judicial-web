package com.rds.narcotics.web;

import com.rds.narcotics.model.RdsNarcoticsResponse;
import com.rds.narcotics.service.RdsNarcoticsIdentifyService;
import com.rds.upc.model.RdsUpcMessageModel;
import com.rds.upc.model.RdsUpcUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/narcotics/archive")
public class RdsNarcoticsIdentifyController {

    @Autowired
    private RdsNarcoticsIdentifyService rdsNarcoticsIdentifyService;

    public RdsUpcMessageModel setModel(boolean result, String message) {
        RdsUpcMessageModel model = new RdsUpcMessageModel();
        model.setResult(result);
        model.setMessage(message);
        return model;
    }

    /**
     *
      初始化   获取鉴定人
     */
    @RequestMapping(value ="getidentifyInfo")
    public RdsNarcoticsResponse getCaseInfo(@RequestBody Map<String, Object> params, HttpSession session) {
        String sql_str = "";
        if (session.getAttribute("user") != null) {
            RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
            sql_str = user.getSql_str2();
        }
        params.put("sql_str", sql_str);
        return rdsNarcoticsIdentifyService.getIdentifyInfo(params);
    }

    /**
     *
     保存鉴定人
     */
    @RequestMapping(value = "save")
    public Object save(@RequestBody Map<String, Object> params, HttpSession session) {
        String username = "";
        String userid = "";
        if (session.getAttribute("user") != null) {
            RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
            username = user.getUsername();
            userid = user.getUserid();
        }
        params.put("user_name", username);
        params.put("user_id", userid);

        Object object = params.get("per_id");
        boolean exsitper_name = rdsNarcoticsIdentifyService.exsitper_name(params.get("per_name"));
        boolean exsitper_code = rdsNarcoticsIdentifyService.exsitper_code(params.get("per_code"));

        if (object == null || object.equals("")) {
            if (exsitper_name && exsitper_code) {
                return rdsNarcoticsIdentifyService.insert(params) > 0 ? this.setModel(true, "添加成功") : this.setModel(false, "添加失败");
            } else {
                if (!exsitper_name) {
                    return this.setModel(false, "鉴定人已存在");
                } else {
                    return this.setModel(false, "证号已存在");
                }
            }
        } else {
            if (!exsitper_name && exsitper_code) {
                return rdsNarcoticsIdentifyService.update(params) > 0 ? this.setModel(true, "修改成功") : this.setModel(false, "修改失败");
            }
            if (exsitper_name && !exsitper_code) {
                return rdsNarcoticsIdentifyService.update(params) > 0 ? this.setModel(true, "修改成功") : this.setModel(false, "修改失败");
            }
            if (exsitper_name && exsitper_code) {
                return rdsNarcoticsIdentifyService.update(params) > 0 ? this.setModel(true, "修改成功") : this.setModel(false, "修改失败");
            } else {
                return this.setModel(false, "您刚刚输入的已存在!");
            }
        }


    }

    /**
     *
     删除鉴定人
     */
    @RequestMapping(value ="delete")
    public Boolean delete(@RequestBody Map<String, Object> params) {
        return rdsNarcoticsIdentifyService.delete(params) > 0 ? true : false;
    }


}




