var sampleStore = Ext.create('Ext.data.Store', {
			fields : ['id', 'name'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'crm/register/getSampleCombo.do',
				params : {},
				reader : {
					type : 'json',
					root : 'data'
				}
			}
		});
Ext.define('Rds.crm.form.CrmRegisterInsertForm', {
	extend : 'Ext.form.Panel',
	initComponent : function() {
		var orderTypeStore = Ext.create('Ext.data.Store', {
					fields : ['key', 'value'],
					data : [{
								"key" : "0",
								"value" : "百度竞价"
							}, {
								"key" : "1",
								"value" : "代理"
							}, {
								"key" : "2",
								"value" : "免费推广"
							}, {
								"key" : "3",
								"value" : "电话成交"
							}]
				});
		var isOK = Ext.create('Ext.data.Store', {
					fields : ['key', 'value'],
					data : [{
								"key" : "0",
								"value" : "否"
							}, {
								"key" : "1",
								"value" : "是"
							}]
				});
		var me = this;

		var orderType = Ext.create('Ext.form.ComboBox', {
					name : 'orderType',
					fieldLabel : '下单方式',
					columnWidth : .45,
					labelWidth : 80,
					editable : false,
					mode : 'remote',
					forceSelection : true,
					allowBlank : true,
					triggerAction : 'all',
					displayField : 'value',
					labelAlign : 'right',
					valueField : 'key',
					store : orderTypeStore,
					listeners : {
						change : function(value, newValue, oldValue, eOpts) {
						}
					}
				});
		var isExtendOrder = Ext.create('Ext.form.ComboBox', {
					name : 'isExtendOrder',
					fieldLabel : '延伸案例',
					columnWidth : .45,
					labelWidth : 80,
					editable : false,
					mode : 'remote',
					forceSelection : true,
					allowBlank : true,
					triggerAction : 'all',
					displayField : 'value',
					labelAlign : 'right',
					valueField : 'key',
					store : isOK,
					value : '0',
					listeners : {
						change : function(value, newValue, oldValue, eOpts) {
						}
					}
				});
		var isPostpaid = Ext.create('Ext.form.ComboBox', {
					name : 'isPostpaid',
					fieldLabel : '先出报告',
					columnWidth : .45,
					labelWidth : 80,
					editable : false,
					mode : 'remote',
					forceSelection : true,
					allowBlank : true,
					triggerAction : 'all',
					displayField : 'value',
					labelAlign : 'right',
					valueField : 'key',
					store : isOK,
					value : '0',
					listeners : {
						change : function(value, newValue, oldValue, eOpts) {
						}
					}
				});
		var detection_classStore = Ext.create('Ext.data.Store', {
					fields : ['id', 'name'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'crm/register/getdectionclass.do',
						params : {},
						reader : {
							type : 'json',
							root : 'data'
						}
					}
				});
		var detection_orgStore = Ext.create('Ext.data.Store', {
					fields : ['id', 'name'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'crm/register/getdetectionorg.do',
						params : {},
						reader : {
							type : 'json',
							root : 'data'
						}
					},
					listeners : {
						beforeload : function(store, operation, eOpts) {
							Ext.apply(store.proxy.extraParams, {
										dic_id : detection_class.getValue()
									});
						},
						load :function( store, records, successful, eOpts){
							detection_org.setValue(store.getAt(0));
						}
					}
				});
		var detection_org = Ext.create('Ext.form.ComboBox', {
					name : 'detectionOrg',
					fieldLabel : '检测单位',
					columnWidth : .45,
					labelWidth : 80,
					editable : false,
					mode : 'remote',
					forceSelection : true,
					allowBlank : true,
					triggerAction : 'all',
					displayField : 'name',
					labelAlign : 'right',
					valueField : 'id',
					store : detection_orgStore,
					listeners : {
						change : function(value, newValue, oldValue, eOpts) {

						}
					}
				});

		var detection_class = Ext.create('Ext.form.ComboBox', {
					name : 'detectionClass',
					fieldLabel : '检测项目',
					columnWidth : .45,
					labelWidth : 80,
					editable : false,
					mode : 'remote',
					forceSelection : true,
					allowBlank : true,
					triggerAction : 'all',
					displayField : 'name',
					labelAlign : 'right',
					valueField : 'id',
					store : detection_classStore,
					listeners : {
						select : function(combo, records, eOpts) {
							detection_org.reset();
							Ext.apply(detection_orgStore.proxy.extraParams, {
										dic_id : this.getValue()
									});
							detection_orgStore.load();
							//detection_org.setValue(detection_orgStore.getAt(0));
						}
					}
				});

		var standFee = new Ext.form.field.Number({
					// 该列在整行中所占的百分比
					columnWidth : .45,
					labelAlign : 'right',
					fieldLabel : "标准金额",
					allowBlank : true,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is
					labelWidth : 80,
					hideTrigger : true,
					maxLength : 50,
					name : 'standFee'
				});
		var consultCount = new Ext.form.field.Number({
					// 该列在整行中所占的百分比
					columnWidth : .45,
					labelAlign : 'right',
					fieldLabel : "咨询次数",
					allowBlank : true,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is
					labelWidth : 80,
					hideTrigger : true,
					maxLength : 50,
					allowDecimals : false,
					name : 'consultCount'
				});
		var client = new Ext.form.field.Text({
					columnWidth : .45,
					labelAlign : 'right',
					fieldLabel : "委托人",
					allowBlank : true,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is
					labelWidth : 80,
					maxLength : 50,
					allowDecimals : false,
					name : 'client'
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
					// 该列在整行中所占的百分比
					columnWidth : .45,
					xtype : "textfield",
					labelAlign : 'right',
					labelWidth : 80,
					fieldLabel : "手机号码",
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is
					// required!
					maxLength : 50,
					name : 'phone',
					listeners : {
						blur : function(textfield, The, eOpts) {
							Ext.Ajax.request({
										url : "crm/register/isphonein.do",
										method : "POST",
										async : false,
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : {
											phone : textfield.getValue()
										},
										success : function(response, options) {
											response = Ext.JSON
													.decode(response.responseText);
											if (response) {
												Ext.Msg.alert("消息",
														"此手机号码已经登记过了。")
											}
										},
										failure : function() {
											Ext.Msg.alert("提示",
													"网络异常<br>请联系管理员!");
										}
									});
						}
					}
				}, {
					xtype : 'datefield',
					name : 'orderInDate',
					columnWidth : .45,
					labelWidth : 80,
					fieldLabel : '登记时间',
					labelAlign : 'right',
					format : 'Y-m-d',
					allowBlank : true,// 不允许为空
					maxValue : new Date(),
					value : Ext.Date.add(new Date(), Ext.Date.DAY)
				}]
			}, {
				layout : "column",// 从左往右的布局
				xtype : 'fieldcontainer',
				border : false,
				items : [orderType, isPostpaid]
			}, {
				layout : "column",// 从左往右的布局
				xtype : 'fieldcontainer',
				border : false,
				items : [detection_class, detection_org]
			}, {
				layout : "column",// 从左往右的布局
				xtype : 'fieldcontainer',
				border : false,
				items : [client, consultCount]
			}, {
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
							xtype : 'textarea',
							fieldLabel : "地址",
							name : 'address',
							width : 500,
							allowBlank : true,// 不允许为空
							// blankText : "不能为空",// 错误提示信息，默认为This field
							maxLength : 500,
							columnWidth : 0.9,
							labelWidth : 80,
							labelAlign : 'right'
						}]
			}, {
				layout : "column",// 从左往右的布局
				xtype : 'fieldcontainer',
				border : false,
				items : [{
							xtype : 'textarea',
							fieldLabel : "咨询备注",
							name : 'remark',
							width : 500,
							allowBlank : true,// 不允许为空
							// blankText : "不能为空",// 错误提示信息，默认为This field is
							// required!
							maxLength : 500,
							columnWidth : 0.9,
							labelWidth : 80,
							labelAlign : 'right'
						}]
			}, {
				layout : "column",// 从左往右的布局
				xtype : 'fieldcontainer',
				border : false,
				items : [standFee, isExtendOrder]
			}, {
				layout : 'absolute',// 从左往右的布局
				xtype : 'panel',
				border : false,
				items : [{
					xtype : 'button',
					text : '添加样本',
					width : 100,
					x : 500,
					listeners : {
						click : function() {
							var me = this.up("form");
							me.add({
								xtype : 'form',
								style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
								// bodyPadding : 10,
								defaults : {
									anchor : '100%',
									labelWidth : 80,
									labelSeparator : "：",
									labelAlign : 'right'
								},
								border : false,
								autoHeight : true,
								items : [{

									layout : 'column',
									xtype : 'panel',
									border : false,
									items : [Ext.create('Ext.form.ComboBox', {
												name : 'sampleName',
												fieldLabel : '样本描述',
												columnWidth : .75,
												labelWidth : 80,
												editable : true,
												mode : 'remote',
												allowBlank : true,
												triggerAction : 'all',
												displayField : 'name',
												labelAlign : 'right',
												valueField : 'id',
												store : sampleStore,
												listeners : {
													change : function(value,
															newValue, oldValue,
															eOpts) {

													}
												}
											}), {
										width : 50,
										x : 30,
										xtype : 'button',
										text : '删除',
										listeners : {
											click : function() {
												var mei = this.up("form");
												mei.disable(true);
												mei.up("form").remove(mei);
											}
										}
									}]

								}]

							});
						}
					}
				}]
			}, {
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
							layout : 'auto',
							xtype : 'panel',
							border : false,
							items : []
						}]
			}]
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
	onSave : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if (form.isValid()) {

			Ext.Ajax.request({
						url : "crm/register/saveOrderInfo.do",
						method : "POST",
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : values,
						success : function(response, options) {
							response = Ext.JSON.decode(response.responseText);
							if (response.success == true) {
								Ext.MessageBox.alert("提示信息", "添加订单成功");
								me.grid.getStore().load();
								me.up("window").close();
							} else {
								Ext.MessageBox.alert("错误信息", response.msg);
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
						}
					});
		}
	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}
});
