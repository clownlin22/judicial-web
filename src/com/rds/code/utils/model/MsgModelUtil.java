package com.rds.code.utils.model;

import com.rds.upc.model.RdsUpcMessageModel;

/**
 * @author linys
 * @ClassName: MsgModelUtil
 * @Description: 获取返回页面内容模板工具类
 * @date 2015年4月8日
 */
public class MsgModelUtil {
    public static RdsUpcMessageModel getModel(boolean result,String msg){
        RdsUpcMessageModel model = new RdsUpcMessageModel();
        model.setResult(result);
        model.setMessage(msg);
        return model;
    }
}
