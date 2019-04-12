package com.rds.platform.logger;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class LoggerProgramAppender extends AppenderSkeleton {

	/**
	 * �ر���Դ
	 */
	@Override
	public void close() {
		if (this.closed) {
			return;
		}
		this.closed = true;
	}

	/**
	 * ������Ҫʹ�ø�ʽ����
	 */
	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	protected void append(LoggingEvent event) {

		// ����̨��ӡ��־
		System.out.println("ceshi"+this.layout.format(event));
//		// ������õĸ�ʽ����û�д����쳣�������ӡ�쳣ջ��Ϣ
//		if (layout.ignoresThrowable()) {
//			String[] throwableStrRep = event.getThrowableStrRep();
//			if (Objects.nonNull(throwableStrRep)) {
//				for (String throwStr : throwableStrRep) {
//					System.out.println(throwStr);
//				}
//			}
//		}
	}

}
