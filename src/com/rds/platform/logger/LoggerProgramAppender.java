package com.rds.platform.logger;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class LoggerProgramAppender extends AppenderSkeleton {

	/**
	 * 关闭资源
	 */
	@Override
	public void close() {
		if (this.closed) {
			return;
		}
		this.closed = true;
	}

	/**
	 * 这里需要使用格式化器
	 */
	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	protected void append(LoggingEvent event) {

		// 控制台打印日志
		System.out.println("ceshi"+this.layout.format(event));
//		// 如果配置的格式化器没有处理异常，这里打印异常栈信息
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
