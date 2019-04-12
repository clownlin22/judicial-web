package com.rds.platform.thread;

import java.io.IOException;

import com.rds.platform.msg.CRMessageUtils;

public class SendMessageThread implements Runnable {
	private String msg;
	
	public SendMessageThread(){
		
	}
	
	public SendMessageThread(String msg){
		this.msg = msg;
	}

	@Override
	public void run() {
		System.out.println(this.msg);
		if(msg.startsWith("8")){
			String sub_msg = msg.substring(3, msg.length()-1);
//			System.out.println("8 888888"+sub_msg);
			String []msgs = sub_msg.split("");
			String mobile = msgs[0];
//			String dispath = msgs[1];
//			String date = msgs[2];
//			String time = msgs[3];
			String receive_msg = msgs[4];
			String content = receive_msg;
			try {
				CRMessageUtils.sendMessage("Notice:"+content, mobile);
			} catch (IOException e) {
				e.printStackTrace();
			}
//			System.out.print(mobile+"/"+dispath+"/"+date+"/"+time+"/"+receive_msg);
		}else if(msg.startsWith("2")){
			String sub_msg = msg.substring(3, msg.length()-1);
			String []msgs = sub_msg.split("");
			String mobile = msgs[0];
			String content = msgs[4]+" "+msgs[5]+" "+msgs[6];
			try {
				CRMessageUtils.sendMessage("Job Notice:"+content, mobile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
