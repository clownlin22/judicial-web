Ext.define('Rds.alcohol.form.AlcoholRegisterInsertResultForm', {
	extend : 'Ext.form.Panel',
	initComponent : function() {
		var me = this;
		var storeModel = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getReportModels.do',
				params : {
					type : 'alcohol',
					receiver_id : ""
				},
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
        var clientStore = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'alcohol/register/getClient.do',
				params : {},
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
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
									maxLength : 50,
									name : 'case_code',
									readOnly : true
								}]
					},  {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
							xtype : 'filefield',
							name : 'files',
							columnWidth : .45,
							labelWidth : 80,
							fieldLabel : '附件',
							msgTarget : 'side',
							hidden:true,
							labelAlign : 'right',
							anchor : '100%',
							buttonText : '选择文件',
							validator : function(v) {
								if (!v == "") {
									if (!v.endWith(".jpg") &&!v.endWith(".JPG")
											&& !v.endWith(".png") && !v.endWith(".PNG")
											&& !v.endWith(".gif") && !v.endWith(".GIF")
											&& !v.endWith(".jpeg") && !v.endWith(".JPEG") ) {
										return "请选择.jpg .png .gif.jpeg类型的图片";
									}
								}
								return true;
							}
						}, {
							xtype : "textfield",
							fieldLabel : '已有附件',
							labelAlign : 'right',
							columnWidth : .45,
							labelWidth : 80,
							hidden:true,
							readOnly : true,
							columnWidth : .45,
							name : 'attachment'
						}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : 'column',
						items : [{
							xtype : 'button',
							text : '查看附件',
							hidden:true,
							width : 100,
							x : 490,
							listeners : {
								click : function() {
									var me = this.up("form");
									var form = me.getForm();
									var values = form.getValues();
									var win = Ext.create("Ext.window.Window", {
										title : '查看附件',
										width : 700,
										autoScroll : true,
										iconCls : 'Find',
										height : 700,
										layout : 'border',
										items : [{
											xtype : 'box',
											style : 'margin:6px;width:680px;height:680px;',
											autoScroll : true,
											autoEl : {
												tag : 'img',
												src : "alcohol/register/getImg.do?filename="
														+ values["attachment"]
														+ "&v="
														+ new Date().getTime()
											}
										}]
									});
									win.show();
								}
							}
						}]
					}, 
					{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [
						         new Ext.form.field.ComboBox({
									 fieldLabel : '是否检出',
									 columnWidth : .45,
									 id : 'alcohol_is_detection_update',
									 labelWidth : 80,
									 editable : false,
									 triggerAction : 'all',
									 displayField : 'Name',
									 labelAlign : 'right',
									 valueField : 'Code',
									 store : new Ext.data.ArrayStore({
									 fields : ['Name', 'Code'],
									 data : [['是', 1], ['否', 0]]
									 }),
									 value : 0,
									 mode : 'local',
									 // typeAhead: true,
									 name : 'is_detection',
									 listeners : {
								            select : function(combo,records,options){ 
								            	switch (combo.value) {
												 case 0 :
													 Ext.getCmp("alcohol_sample_remark1").setValue("未出现乙醇的特征色谱峰。");
													 Ext.getCmp("alcohol_sample_remark1").setReadOnly(true);
													 Ext.getCmp("alcohol_sample_result1").setValue("血液中未检出乙醇成分。");
													 Ext.getCmp("alcohol_sample_result1").setReadOnly(true);
												 break;
												 case 1 :
													 Ext.getCmp("alcohol_sample_remark1").setValue("血液中乙醇含量为mg/mL。");
													 Ext.getCmp("alcohol_sample_remark1").setReadOnly(false);
													 Ext.getCmp("alcohol_sample_result1").setValue("血液中乙醇含量为mg/mL。");
													 Ext.getCmp("alcohol_sample_result1").setReadOnly(false);
												 break;
											 	}
								            },
						         			change : function(combo,records,options){ 
						         				switch (combo.value) {
						         				case 1 :
						         					Ext.getCmp("alcohol_sample_remark1").setReadOnly(false);
						         					Ext.getCmp("alcohol_sample_result1").setReadOnly(false);
						         				break;
						         				case 0 :
						         					Ext.getCmp("alcohol_sample_remark1").setReadOnly(true);
						         					Ext.getCmp("alcohol_sample_result1").setReadOnly(true);
						         				}
						         			}
									 }
						         
							 })
					]
			}, {
			 border : false,
			 xtype : 'fieldcontainer',
			 layout : "column",
			 items : [{
				 xtype : 'textarea',
				 fieldLabel : '样本描述<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
				 name : 'sample_remark',
				 allowBlank : false,
				 width : 500,
				 maxLength : 400,
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
				 id : 'alcohol_sample_remark1',
				 fieldLabel : '检验描述<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
				 name : 'sample_remark2',
				 readOnly : true,
				 allowBlank : false,
				 width : 500,
				 maxLength : 400,
				 columnWidth : 0.9,
				 labelWidth : 80,
				 labelAlign : 'right',
				 value : '未出现乙醇的特征色谱峰。',
//				 listeners : {
//					 'change' : function(value, newValue, oldValue) {
//								 if (Ext.getCmp("alcohol_is_detection_update").getValue() == 1) 
//								 {
//									 	Ext.getCmp("alcohol_sample_result1").setValue(newValue);
//								 }
//					 		}
//				 	}
			 	}]
			 }, {
			 border : false,
			 xtype : 'fieldcontainer',
			 layout : "column",
			 items : [{
				 columnWidth : 0.9,
				 xtype : 'textarea',
				 id : 'alcohol_sample_result1',
				 fieldLabel : '检验结果<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
				 name : 'sample_result',
				 readOnly : true,
				 allowBlank : false,
				 labelWidth : 80,
				 labelAlign : 'right',
				 maxLength : 400,
				 value : '血液中未检出乙醇成分。',
				 validator : function(value) {
						if(Ext.getCmp("alcohol_is_detection_update").getValue()==1)
						{
							if(Ext.getCmp("alcohol_sample_remark1").getValue()==value)
								return true;
							else 
								return "检验结果和检验描述不一致";
						}else {
							return true;
						}
					}
//				 listeners : {
//					 'change' : function(value, newValue, oldValue) {
//						 if (Ext.getCmp("alcohol_is_detection_update").getValue() == 1) {
//							 	Ext.getCmp("alcohol_sample_remark1").setValue(newValue);
//						 	}
//					 	}
//				 	}
			 	}]
			 }
			]
		}];

		me.buttons = [{
					text : '保存',
					iconCls : 'Disk',
					handler : me.onSave
				}, {
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	listeners : {
		'afterrender' : function() {
			var me = this;
			var values = me.getValues();
			var area_code = values["area_code"];
			var receiver = values["receiver_id"];
//			Ext.getCmp('area_update_alcohol').setValue(area_code);
//			Ext.getCmp('case_receiver_update_alcohol').setValue(receiver);
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
									hidden:true,
									value : response.sample_name,
									//allowBlank : false,// 不允许为空
									fieldLabel : '被检验人',
									maxLength : 50,
									name : 'sample_name'
								}, {
									columnWidth : .45,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '身份证号',
									maxLength : 18,
									hidden:true,
									value : response.id_number,
									name : 'id_number',
									validator : function(value) {
										var result = false;
										if (value != null && value != "") {
											Ext.Ajax.request({
												url : "judicial/register/verifyId_Number.do",
												method : "POST",
												async : false,
												headers : {
													'Content-Type' : 'application/json'
												},
												jsonData : {
													id_number : value
												},
												success : function(response,
														options) {
													response = Ext.JSON
															.decode(response.responseText);
													if (!response) {
														result = "身份证号码不正确";
													} else {
														result = true;
													}
												},
												failure : function() {
													Ext.Msg.alert("提示",
															"网络故障<br>请联系管理员!");
												}
											});
										} else {
											result = true;
										}
										return result;
									}
								}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						hidden : true,
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : 'numberfield',
									labelAlign : 'right',
									labelWidth : 80,
									// allowBlank : false,// 不允许为空
									fieldLabel : '采样剂量(mL)',
									maxLength : 5,
									allowDecimals : false,
									value : response.sample_ml,
									name : 'sample_ml',
									value : 0,
									listeners : {
										change : function(field, value) {
											// if (value < 0) {
											// field.setValue(0);
											// }
										}
									}
								}]
					});
				}
			});
		}
	},
	onSave : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if (form.isValid()) {
			form.setValues({
						"receiver_id" : values["receiver_id_update"]
					})
			form.submit({
						url : 'alcohol/register/updateCaseInfo.do',
						method : 'post',
						waitMsg : '正在录入数据...',
						success : function(form, action) {
							response = Ext.JSON
									.decode(action.response.responseText);
							if (response) {
								Ext.MessageBox.alert("提示信息", "案例结果录入成功");
								me.grid.getStore().load();
								me.up("window").close();
							} else {
								Ext.MessageBox.alert("提示信息", "录入失败<br>请联系管理员");
							}
						},
						failure : function() {
							Ext.MessageBox.alert("提示信息", "录入失败<br>请联系管理员");
							me.up("window").close();
						}
					});
		} else {
			Ext.Msg.alert("提示", "表单未填写完整！");
		}

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}
});
