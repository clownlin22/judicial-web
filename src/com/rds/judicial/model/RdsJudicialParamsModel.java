package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialParamsModel {
            private String starttime;
            private String endtime;
            private String case_code;
            private String receiver;
            private Integer is_delete;
            private Integer urgent_state;
            private String sql_str;
            private String case_name;
            private String fee_type;
            private String fee_status;
            private String mail_code;
            private String area;
            private String usercode;
            private String report_model;
            private String parnter_name;
            private String source_type;
            private String update_date_start;
            private String update_date_end;
            private String confirm_date_start;
            private String confirm_date_end;
}
