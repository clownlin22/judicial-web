package com.rds.upc.service;

import java.util.Map;

public interface RdsUpcCompanyService extends RdsUpcBaseService {
    String queryLaboratoryNo(String companyid);
    boolean verifyId_code(Map<String, Object> params);
}
