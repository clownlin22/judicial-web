var storeModel = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'ajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getReportModels.do?type=alcohol',
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
Ext.define('Rds.alcohol.form.AlcoholVerifyForm', {
	extend : 'Ext.form.Panel',
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'form',
			style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
			bodyPadding : 10,
			defaults : {
				anchor : '100%',
				labelWidth : 80,
				labelSeparator : "：",
				labelAlign : 'right'
			},
			border : false,
			autoHeight : true,
			items : [{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : "hiddenfield",
									name : "case_id"
								}, {
									xtype : "hiddenfield",
									name : "sample_id"
								}, {
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '案例条形码',
									allowBlank : false,// 不允许为空
									blankText : "不能为空",// 错误提示信息，默认为This field
									// is required!
//									maxLength : 50,
									name : 'case_code',
									readOnly : true

								},{
									xtype : 'datefield',
									name : 'close_time',
									columnWidth : .45,
									labelWidth : 80,
									fieldLabel : '打印日期',
									labelAlign : 'right',
									format : 'Y-m-d',
									readOnly : true,
									allowBlank : false,// 不允许为空
//									maxValue : new Date(),
//									value : Ext.Date.add(new Date(),
//											Ext.Date.DAY)

								},]
					}, {
						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [{
									xtype : 'datefield',
									name : 'client_time',
									columnWidth : .45,
									labelWidth : 80,
									fieldLabel : '委托日期',
									labelAlign : 'right',
									format : 'Y-m-d',
									readOnly : true,
									allowBlank : false,// 不允许为空
									maxValue : new Date(),
									value : Ext.Date.add(new Date(),
											Ext.Date.DAY)

								}, {
									xtype : 'datefield',
									name : 'accept_time',
									columnWidth : .45,
									labelWidth : 80,
									fieldLabel : '受理日期',
									labelAlign : 'right',
									format : 'Y-m-d',
									readOnly : true,
									allowBlank : false,// 不允许为空
									maxValue : new Date(),
									value : Ext.Date.add(new Date(),
											Ext.Date.DAY)

								}]
					}, {
						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign : 'right',
									fieldLabel : '委托人',
									allowBlank : false,// 不允许为空
									labelWidth : 80,
//									maxLength : 50,
									readOnly : true,
									name : 'client'

								}, {
									xtype : "textfield",
									columnWidth : .45,
									labelAlign : 'right',
									readOnly : true,
									labelWidth : 80,
									fieldLabel : '案例所属地',
									name : "area_name"
								}]
					},{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '送检人',
//									maxLength : 50,
									readOnly : true,
									name : 'checkper'

								}, {
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '送检人电话',
//									maxLength : 50,
									readOnly : true,
									name : 'checkper_phone'

								}]
					}, {
						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign : 'right',
									readOnly : true,
									fieldLabel : '案例归属地',
									labelWidth : 80,
//									maxLength : 100,
									name : 'receiver_area'

								}, {
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign : 'right',
									readOnly : true,
									fieldLabel : '归属人',
									labelWidth : 80,
//									maxLength : 50,
									name : 'receiver'

								}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : "textfield",
									columnWidth : .45,
									labelAlign : 'right',
									readOnly : true,
									labelWidth : 80,
									fieldLabel : '模板名称',
									name : "report_modelname"
								}]
					}, {
						border : false,
						hidden : true,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'textarea',
									fieldLabel : '事件描述',
									name : 'event_desc',
									width : 500,
									columnWidth : 0.9,
									readOnly : true,
									labelWidth : 80,
									labelAlign : 'right'
								}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									readOnly : true,
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '邮寄地址',
//									maxLength : 50,
									name : 'mail_address'

								}, {
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign : 'right',
									readOnly : true,
									labelWidth : 80,
									fieldLabel : '接收人',
//									maxLength : 50,
									name : 'mail_per'

								}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									readOnly : true,
									fieldLabel : '联系电话',
//									maxLength : 50,
									name : 'mail_phone'

								}  ,{
									columnWidth : .45,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									readOnly : true,
									fieldLabel : '鉴定人',
//									maxLength : 50,
									name : 'case_checkper'
					            }
								
						
//						, {
//							// 该列在整行中所占的百分比
//							columnWidth : .45,
//							xtype : "textfield",
//							labelAlign : 'right',
//							labelWidth : 80,
//							fieldLabel : '采血管编号',
//							maxLength : 50,
//							name : 'bloodnum'
//						}
						]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'textarea',
									fieldLabel : '备注',
									name : 'remark',
									width : 500,
									readOnly : true,
									columnWidth : 0.9,
									labelWidth : 80,
									labelAlign : 'right'
								}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [new Ext.form.field.ComboBox({
											fieldLabel : '血管类型',
											columnWidth : .45,
											labelWidth : 80,
											editable : false,
											readOnly : true,
											triggerAction : 'all',
											displayField : 'Name',
											labelAlign : 'right',
											valueField : 'Code',
											store : new Ext.data.ArrayStore({
														fields : ['Name',
																'Code'],
														data : [['真空采血管', 1],
																['促凝管', 0]]
													}),
											value : 1,
											mode : 'local',
											// typeAhead: true,
											name : 'isDoubleTube'
										}), new Ext.form.field.ComboBox({
											fieldLabel : '是否检出',
											columnWidth : .45,
											// id:'alcohol_is_detection',
											labelWidth : 80,
											readOnly : true,
											editable : false,
											triggerAction : 'all',
											displayField : 'Name',
											labelAlign : 'right',
											valueField : 'Code',
											store : new Ext.data.ArrayStore({
														fields : ['Name',
																'Code'],
														data : [['是', 1],
																['否', 0]]
													}),
											value : 0,
											mode : 'local',
											// typeAhead: true,
											name : 'is_detection'

										})]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [ {
								// 该列在整行中所占的百分比
								columnWidth : .45,
								xtype : "textfield",
								labelAlign : 'right',
								labelWidth : 80,
								fieldLabel : '血液编号',
//								maxLength : 50,
								readOnly : true,
								name : 'bloodnumA'
							}, {
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '血液容量',
//									maxLength : 50,
									readOnly : true,
									name : 'bloodnumB'
								}]
					},
					
					{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'textfield',
									fieldLabel : '案例简介',
									name : 'case_intr',
									columnWidth : 0.9,
									labelWidth : 80,
									readOnly : true,
									labelAlign : 'right' 
								}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'textarea',
									fieldLabel : '案例详情',
									name : 'case_det',
									readOnly : true,
									columnWidth : 0.9,
//									maxLength : 500,
									labelWidth : 80,
									labelAlign : 'right' 
								}]
					},		 
					{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [   new Ext.form.field.ComboBox({
							 columnWidth : .45,
							fieldLabel : '重新鉴定',
							labelWidth : 80,
							editable : false,
							triggerAction : 'all',
							displayField : 'Name',
							labelAlign : 'right',
							valueField : 'Code',
							readOnly : true,
							store : new Ext.data.ArrayStore({
										fields : ['Name', 'Code'],
										data : [['否', 0],['是', 1]]
									}),
							value : 0,
							mode : 'local',
							// typeAhead: true,
							name : 'is_check'
						})]
					},
					
					, {
						 border : false,
						 xtype : 'fieldcontainer',
						 layout : "column",
						 items : [{
						 // 该列在整行中所占的百分比
						 columnWidth : .45,
						 xtype : "numberfield",
						 value : 0,
//						 maxLength : 7,
						 labelAlign : 'right',
						 labelWidth : 80,
						 allowBlank : false,// 不允许为空
						 fieldLabel : '标准金额',
//						 maxLength : 50,
						 readOnly : true,
						 name : 'stand_sum',
						 value:350.0
						 }, {
						 // 该列在整行中所占的百分比
						 columnWidth : .45,
						 xtype : "numberfield",
						 value : 0,
//						 maxLength : 7,
						 labelAlign : 'right',
						 labelWidth : 80,
						 allowBlank : false,// 不允许为空
						 fieldLabel : '实收金额',
//						 maxLength : 50,
						 readOnly : true,
						 name : 'real_sum',
						 value:350
						 }]
						 },
					
					{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'textarea',
									fieldLabel : '样本描述',
									name : 'sample_remark',
									allowBlank : false,
									width : 500,
									readOnly : true,
//									maxLength : 500,
									columnWidth : 0.9,
									labelWidth : 80,
									labelAlign : 'right',
									value : '送检血液无凝块，包装无渗漏。'
								}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'textarea',
									fieldLabel : '检验描述',
									name : 'sample_remark2',
									readOnly : true,
									allowBlank : false,
									width : 500,
//									maxLength : 500,
									columnWidth : 0.9,
									labelWidth : 80,
									labelAlign : 'right'
								}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									columnWidth : 0.9,
									xtype : 'textarea',
									fieldLabel : '检验结果',
									name : 'sample_result',
									readOnly : true,
									allowBlank : false,
									labelWidth : 80,
									labelAlign : 'right'
								}]
					}]
		}];

		me.buttons = [{
					text : '审核通过',
					iconCls : 'Accept',
					handler : me.onVerify
				}, {
					text : '审核不通过',
					iconCls : 'Cancel',
					handler : me.onNoVerify
				}, {
					text : '取消',
					iconCls : 'Arrowredo',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	listeners : {
		'afterrender' : function() {
			var me = this;
			var values = me.getValues();
			Ext.Ajax.request({
						url : "alcohol/register/getSampleInfo.do",
						method : "POST",
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : {
							sample_id : values["sample_id"]
						},
						success : function(response, options) {
							response = Ext.JSON.decode(response.responseText);
							me.down("form").add({
										border : false,
										xtype : 'fieldcontainer',
										layout : "column",
										items : [{
													// 该列在整行中所占的百分比
													columnWidth : .45,
													xtype : "textfield",
													labelAlign : 'right',
													labelWidth : 80,
													readOnly : true,
													value : response.sample_name,
													allowBlank : false,// 不允许为空
													fieldLabel : '被检验人',
//													maxLength : 50,
													name : 'sample_name'

												}, {
													columnWidth : .45,
													xtype : "textfield",
													labelAlign : 'right',
													labelWidth : 80,
													readOnly : true,
													fieldLabel : '身份证号',
//													maxLength : 50,
													value : response.id_number,
													name : 'id_number'

												}]
									}, {
										border : false,
										hidden : true,
										xtype : 'fieldcontainer',
										layout : "column",
										items : [{
													// 该列在整行中所占的百分比
													columnWidth : .45,
													xtype : 'textfield',
													labelAlign : 'right',
													labelWidth : 80,
													readOnly : true,
													allowBlank : false,// 不允许为空
													fieldLabel : '采样剂量(mL)',
													value : response.sample_ml,
													name : 'sample_ml'

												}]
									}, {
										xtype : 'textarea',
										emptyText : '审核不通过时，请填写审核理由',
										fieldLabel : '审核理由',
										name : 'verify_remark',
//										maxLength : 1000,
										width : 500
									});
						}
					});
		}
	},
	onNoVerify : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		var case_id = values["case_id"];
		var verify_remark = values["verify_remark"];
		if (verify_remark == "") {
			Ext.MessageBox.alert("错误信息", "审核不通过时，请填写审核理由");
			return;
		}
		if (form.isValid()) {
			Ext.Ajax.request({
						url : "alcohol/verify/verifyCaseInfo.do",
						method : "POST",
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : {
							'case_id' : case_id,
							'state' : 1,
							'verify_mark' : verify_remark
						},
						success : function(response, options) {
							response = Ext.JSON.decode(response.responseText);
							if (response == true) {
								Ext.MessageBox.alert("提示信息", "审核案例成功");
								me.grid.getStore().load();
								me.up("window").close();
							} else {
								Ext.MessageBox.alert("错误信息", "审核案例失败");
								me.grid.getStore().load();
								me.up("window").close();
							}
						},
						failure : function() {
							return;
						}
					});
		}
	},
	onVerify : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		var case_id = values["case_id"];
		var verify_remark = values["verify_remark"];
		if (form.isValid()) {
			Ext.Ajax.request({
						url : "alcohol/verify/verifyCaseInfo.do",
						method : "POST",
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : {
							'case_id' : case_id,
							'state' : 3,// 审核过可以直接打印
							'verify_mark' : verify_remark
						},
						success : function(response, options) {
							response = Ext.JSON.decode(response.responseText);
							if (response == true) {
								Ext.MessageBox.alert("提示信息", "审核案例成功");
								me.grid.getStore().load();
								me.up("window").close();
							} else {
								Ext.MessageBox.alert("错误信息", "审核案例失败");
								me.grid.getStore().load();
								me.up("window").close();
							}
						},
						failure : function() {
							return;
						}
					});
		}
	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}
});
