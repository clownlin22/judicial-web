package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialBillModel;
/**
 * @description 发票管理
 * @author ThinK
 * 2015年4月24日
 *
 */
public interface RdsJudicialBillMapper extends RdsJudicialBaseMapper {
	
	List<RdsJudicialBillModel> queryByCaseId(Map<String, Object> params);

	RdsJudicialBillModel queryByBillId(Map<String, Object> params);
}
