package com.rds.narcotics.mapper;

import com.rds.narcotics.model.RdsNarcoticsIdentifyModel;
import java.util.List;
import java.util.Map;

public interface RdsNarcoticsIdentifyMapper {

    List<RdsNarcoticsIdentifyModel> getIdentifyInfo(Map<String, Object> params);

    Integer getcount(Map<String, Object> params);

    Integer insertinfo( Map<String, Object> params);

    Integer update(Map<String, Object> params);

    Integer delete(Map<String, Object> params);

    Integer exsitper_code(Object per_code);

    Integer exsitper_name(Object per_name);


}
