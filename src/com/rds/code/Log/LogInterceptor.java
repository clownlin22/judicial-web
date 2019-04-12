package com.rds.code.Log;

import org.aopalliance.intercept.MethodInterceptor;  
import org.aopalliance.intercept.MethodInvocation;  
import org.apache.log4j.Logger;  

public class LogInterceptor implements MethodInterceptor  
{  
  
    public Object invoke(MethodInvocation invocation) throws Throwable  
    {  
        Logger loger = Logger.getLogger(invocation.getClass());  
  
        loger.info("-------------------------------------------------------------------------------");  
        loger.info(invocation.getMethod() + ":BEGIN!");
        Object obj = invocation.proceed();
        loger.info(invocation.getMethod() + ":END!");
        loger.info("-------------------------------------------------------------------------------------------------");  
  
        return obj;  
    }  
  
} 
