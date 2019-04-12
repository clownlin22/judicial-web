package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialBankModel;

public interface RdsJudicialBankMapper extends RdsJudicialBaseMapper{
	public List<RdsJudicialBankModel> queryBank();

	public List<Map<String, Object>> getBankname();

	public List<Map<String, Object>> getCompany();
}
