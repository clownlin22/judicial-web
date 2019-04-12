package com.rds.bacera.service;

import java.util.Map;

public interface RdsInvasivePreVerifyService {
    public boolean yesVerify(Map<String, Object> params);
    public boolean noVerify(Map<String, Object> params);


}
