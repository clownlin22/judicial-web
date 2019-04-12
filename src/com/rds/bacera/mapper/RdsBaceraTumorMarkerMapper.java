package com.rds.bacera.mapper;


import java.util.Map;

import org.springframework.stereotype.Component;
@Component("RdsBaceraTumorMarkerMapper")
public interface RdsBaceraTumorMarkerMapper extends RdsBaceraBaseMapper{
	int exsitCaseCode(Map<String, Object> params);
	
}
