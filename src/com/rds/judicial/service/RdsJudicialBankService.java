package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

public interface RdsJudicialBankService extends RdsJudicialBaseService{

	List<Map<String, Object>> getBankname();

	List<Map<String, Object>> getCompany();

}
