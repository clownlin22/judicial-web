package com.rds.bacera.model;
import lombok.Data;

/**
 * testItems userd by Tumors of individual
 * @author Administrator
 *
 */
@Data
public class RdsBaceraTumorPerItemsModel {
	//id
	private String id;
	//检测项目
	private String items_name;
	//创建时间
	private String create_time;
	//金额
	private String fees;
	//删除标识
	private String delflag;
	//备注
	private String remark;
}
