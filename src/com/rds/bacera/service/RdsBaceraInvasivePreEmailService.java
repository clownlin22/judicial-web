package com.rds.bacera.service;

import java.util.Map;

import com.rds.bacera.model.RdsBaceraInvasaivePreEmailModel;

public interface  RdsBaceraInvasivePreEmailService  {

    /**
     * email配置
     * @return
     */
    public boolean emailManage(Map<String , Object>map);
    
    /**
     * 发送邮件
     * @param mail
     */
    public boolean sendEmail(RdsBaceraInvasaivePreEmailModel mail)throws Exception;
    /**
     * 添加发送邮件记录
     * @param map
     * @return
     */
    public int inertEmail(Map<String, Object> map);
    
    /**
     * 查询发送邮件记录
     * @param map
     * @return
     */
    public Object queryEmail(Map<String, Object>map);
    
    /**
     * 异步发送邮件
     * @param email
     * @return
     * @throws Exception
     */
    public void sendMailByAsynchronousMode(RdsBaceraInvasaivePreEmailModel email)throws Exception;
    

}
