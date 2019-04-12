package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsFinanceKitSetModel {
    private String kit_id;
    private String kit_name;
    private String kit_count;
    private String kit_spec;
    private String kit_bathc_num;
    private String kit_dest;
    private String kit_express_num;
    private String user_dept_level1;
    private String kit_receive_per;
    private String apply_per;
    private String apply_num;
    private String apply_date;
    private String create_per;
    private String create_pername;
    private String create_date;
    private String remark;
    private String confirm_state;
    private String confirm_per;
    private String confirm_pername;
    private String confirm_time;
    private String is_delete;
}
