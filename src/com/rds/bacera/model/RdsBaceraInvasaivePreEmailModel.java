package com.rds.bacera.model;


import java.util.Date;
import java.util.Map;

import lombok.Data;

@Data
public class RdsBaceraInvasaivePreEmailModel {



	    
	    /**
	     * 发件人邮箱服务器
	     */
	    private String emailHost;
	    /**
	     * 发件人邮箱
	     */
	    private String emailFrom;

	    /**
	     * 发件人用户名
	     */
	    private String emailUserName;

	    /**
	     * 发件人密码
	     */
	    private String emailPassword;

	    /**
	     * 收件人邮箱，多个邮箱以“;”分隔
	     */
	    private String toEmails;
	    /**
	     * 邮件主题
	     */
	    private String subject;
	    /**
	     * 邮件内容
	     */
	    private String content;
	    /**
	     * 邮件中的图片，为空时无图片。map中的key为图片ID，value为图片地址
	     */
	    private Map<String, String> pictures;
	    /**
	     * 邮件中的附件，为空时无附件。map中的key为附件ID，value为附件地址
	     */
	    private Map<String, String> attachments;
	    
	    
	    
	    private String attachFileNames;//附件 
	    
	    

	    private Date emaildate;
	}

