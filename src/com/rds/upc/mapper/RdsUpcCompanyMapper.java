package com.rds.upc.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("rdsUpcCompanyMapper")
public interface RdsUpcCompanyMapper extends RdsUpcBaseMapper {
    String queryLaboratoryNo(String company_id);
    int verifyId_code(Map<String, Object> params);
}
