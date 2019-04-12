package com.rds.trace.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/21
 */
@Data
public class RdsTraceVehicleInfoModel {
    private String uuid;
    private String case_id;
    //车牌号
    private String plate_number;
    //车架号
    private String vehicle_identification_number;
    //发动机号
    private String engine_number;
    //车辆类型
    private String vehicle_type;
}
