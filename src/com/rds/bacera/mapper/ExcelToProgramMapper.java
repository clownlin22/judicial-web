package com.rds.bacera.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("ExcelToProgramMapper")
public interface ExcelToProgramMapper {
	public int insertTables(Map<String,Object> map);
	public int insertTables2(Map<String,Object> map);
	public int insertTables3(Map<String,Object> map);
	
	public List<Map<String,Object>> selectYXBcodeTemp(Map<String,Object> map);
	public int insertYXBtemp(Map<String,Object> map);
	public int updateYXBcodeTemp(Map<String,Object> map);
	
	public List<Map<String,Object>> selectCaseId();
}
