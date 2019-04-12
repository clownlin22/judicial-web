package com.rds.code.utils;

import java.math.BigDecimal;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JScriptInvoke {
	/**
	 * 使用公式获取标准价格
	 * 
	 * @param equation
	 * @param pernum
	 * @return
	 */
	public static Double getStandardFee(String script, Integer pernum,
			Integer feetype) throws Exception {
		Double result = 0.0;
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("js");
		se.eval(script);
		Invocable inv2 = (Invocable) se;
		result = (Double) inv2.invokeFunction("exe", pernum, feetype);
		return result;
	}
	
	/**
	 * 使用公式获取标准价格
	 * 
	 * @param equation
	 * @param pernum
	 * @return
	 */
	public static Double getProgramFinance(String script,Double real_sum, Integer case_num,Integer sample_num,
			Double aptitude_price) throws Exception {
		Double result = 0.0;
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("js");
		se.eval(script);
		Invocable inv2 = (Invocable) se;
		result = (Double) inv2.invokeFunction("exe", real_sum, case_num,sample_num,aptitude_price);
		return Math.ceil(result);
	}
}
