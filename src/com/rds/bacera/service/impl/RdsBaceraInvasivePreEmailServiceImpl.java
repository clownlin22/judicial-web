package com.rds.bacera.service.impl;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.rds.bacera.mapper.RdsBaceraInvasivePreEmailMapper;
import com.rds.bacera.model.RdsBaceraInvasaivePreEmailModel;
import com.rds.bacera.service.RdsBaceraInvasivePreEmailService;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("RdsBaceraInvasivePreEmailService")
public class RdsBaceraInvasivePreEmailServiceImpl implements RdsBaceraInvasivePreEmailService {

	 private static Logger logger = Logger.getLogger(RdsBaceraInvasivePreEmailServiceImpl.class);
	 
	 private static final String FILE_PATH = ConfigPath.getWebInfPath()
				+ "spring" + File.separatorChar + "properties" + File.separatorChar
				+ "config.properties";

		private static final String excelPath = PropertiesUtils.readValue(
				FILE_PATH, "invasivePre_path");
	
		@Autowired
		private RdsBaceraInvasivePreEmailMapper rdsBaceraInvasivePreEmailMapper;
	    
	    @Autowired
	    private JavaMailSender javaMailSender;
	    
	    @Autowired
	    private SimpleMailMessage simpleMailMessage;
	    
	    @Autowired 
	    TaskExecutor taskExecutor;//注入Spring封装的异步执行器
	    
	    @Override
	    public boolean emailManage(Map<String, Object>map){
	    	RdsBaceraInvasaivePreEmailModel mail = new RdsBaceraInvasaivePreEmailModel();
	    	
            System.setProperty("mail.mime.splitlongparameters", "false");

	    	//邮件主题

	    	String subject=map.get("num").toString();
	        mail.setSubject(subject); 
	        
	        //附件
	        String attachment=map.get("attachment_path").toString();
	        Map<String, String> attachments = new HashMap<String, String>();
	        attachments.put(attachment,excelPath+File.separatorChar+attachment);
	        mail.setAttachments(attachments);
	        
	        //内容
	        StringBuilder builder = new StringBuilder();
	        String content=map.get("content").toString();
	        builder.append(content);
	        mail.setContent(content);
	       
	        //收件人邮箱地址
	        String toEmails=map.get("receiveAddress").toString().trim();
	        String[] toEmailArray = toEmails.split(";");
           if (toEmailArray.length>=2){
        	   for(int i=0;i<toEmailArray.length;i++){
        		   if(null==(toEmailArray[i])||"".equals(toEmailArray[i])){
        			   continue;
        		   }
        		   mail.setToEmails(toEmailArray[i]);
   	        try {
   	        	sendMailByAsynchronousMode(mail);//异步发送邮件，多个地址
   			} catch (Exception e) {
   				e.printStackTrace();
   				return false;
   		     	} 
   	           } 
        	   return true;
        	   }else{//单个收件地址，同步发送邮件
        	   mail.setToEmails(toEmailArray[0]);
        	   try {
				sendEmail(mail);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
        	   return true; 
           }
	    }



	    /**
	     * 发送邮件（同步）
	     * @return 
	     * @throws Exception
	     */
	    @Override
	    public boolean sendEmail(RdsBaceraInvasaivePreEmailModel mail) throws Exception  {
            System.setProperty("mail.mime.splitlongparameters", "false");

	        // 建立邮件消息
	        MimeMessage message = javaMailSender.createMimeMessage();
	        
	        MimeMessageHelper messageHelper;
	        try {
	            messageHelper = new MimeMessageHelper(message, true, "UTF-8");
	            
	            // 设置发件人邮箱
	                messageHelper.setFrom(simpleMailMessage.getFrom());
	                
	            // 设置收件人邮箱
	                messageHelper.setTo(mail.getToEmails());
	            
	            
	            // 邮件主题
	            if (mail.getSubject()!=null) {
	                messageHelper.setSubject(mail.getSubject());
	            } 
	            
	            // true 表示启动HTML格式的邮件
	            messageHelper.setText(mail.getContent(), true);
    
	            // 添加附件
	            if (null != mail.getAttachments()) {
	                for (Iterator<Map.Entry<String, String>> it = mail.getAttachments()
	                        .entrySet().iterator(); it.hasNext();) {
	                    Map.Entry<String, String> entry = it.next();
	                    String cid = entry.getKey();
	                    String filePath = entry.getValue();
	                    File file = new File(filePath);
	                    FileSystemResource fileResource = new FileSystemResource(file);
	                    messageHelper.addAttachment(cid, fileResource);
	                }
	            }
	            messageHelper.setSentDate(new Date());
	            // 发送邮件
	            javaMailSender.send(message);
	            logger.info("------------发送邮件完成----------");            
	            return true;
	        } catch (MessagingException e) {  
	            e.printStackTrace();
	            return false;
	        }
			
	    }
	    /**
         * 异步发送邮件
         */
		@Override
		public void sendMailByAsynchronousMode(
				RdsBaceraInvasaivePreEmailModel email) throws Exception {
		final	RdsBaceraInvasaivePreEmailModel email1=email;
			    taskExecutor.execute(new Runnable(){
				   public void run(){
				    try {
				    	sendEmail(email1);
				    } catch (Exception e) {
				    	logger.info(e);
				    }
				   }
				  
				  });
			    
		}

        /**
         * 添加邮件发送记录
         */
        
		@Override
		public int inertEmail(Map<String, Object> map) {
			try {
				map.put("emailFrom", simpleMailMessage.getFrom());//获取发件箱
				return rdsBaceraInvasivePreEmailMapper.insert(map);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}


		/**
		 * 查询邮件发送记录
		 */
		@Override
		public List<Object> queryEmail(Map<String, Object> map) {
			
			try {
				return rdsBaceraInvasivePreEmailMapper.queryEmail(map);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}


       

}
