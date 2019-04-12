package com.rds.narcotics.service.impl;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.narcotics.mapper.RdsNarcoticsIdentifyMapper;
import com.rds.narcotics.model.RdsNarcoticsIdentifyModel;
import com.rds.narcotics.model.RdsNarcoticsResponse;
import com.rds.narcotics.service.RdsNarcoticsIdentifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RdsNarcoticsIdentifyServiceImpl implements RdsNarcoticsIdentifyService {

    @Autowired
    private RdsNarcoticsIdentifyMapper rdsNarcoticsIdentifyMapper;

    @Override
    public RdsNarcoticsResponse getIdentifyInfo(Map<String, Object> params) {
        RdsNarcoticsResponse response=new RdsNarcoticsResponse();
        List<RdsNarcoticsIdentifyModel> list=rdsNarcoticsIdentifyMapper.getIdentifyInfo(params);
        int count=rdsNarcoticsIdentifyMapper.getcount(params);
        response.setCount(count);
        response.setItems(list);
        return response;
    }

    @Override
    public Integer insert(Map<String, Object> params) {
        String uuid = UUIDUtil.getUUID();
        params.put("per_id", uuid);
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("per_sys",  dFormat.format(System.currentTimeMillis()));
        return rdsNarcoticsIdentifyMapper.insertinfo(params);
    }

    @Override
    public Integer delete(Map<String, Object> params) {
        return rdsNarcoticsIdentifyMapper.delete(params);
    }

    @Override
    public Integer update(Map<String, Object> params) {
        return rdsNarcoticsIdentifyMapper.update(params);
    }

    @Override
    public boolean exsitper_code(Object per_code) {
        int count = rdsNarcoticsIdentifyMapper.exsitper_code(per_code);
        if (count > 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean exsitper_name(Object per_name) {
        int count = rdsNarcoticsIdentifyMapper.exsitper_name(per_name);
        if (count > 0) {
            return false;
        }
        return true;
    }

}
