var orderTypeStore = Ext.create('Ext.data.Store', {
			fields : ['key', 'value'],
			data : [{
						"key" : 0,
						"value" : "百度竞价"
					}, {
						"key" : 1,
						"value" : "代理"
					}, {
						"key" : 2,
						"value" : "免费推广"
					}, {
						"key" : 3,
						"value" : "电话成交"
					}]
		});
var isOK = Ext.create('Ext.data.Store', {
			fields : ['key', 'value'],
			data : [{
						"key" : 0,
						"value" : "否"
					}, {
						"key" : 1,
						"value" : "是"
					}]
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
					// Ext.apply(store.proxy.extraParams, {
					// dic_id : detection_class.getValue()
					// });
				}
			}
		});
var orderFeeStore = Ext.create('Ext.data.Store', {
			fields : ['feeId', 'realFee', 'feeTime'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'crm/register/getOrderFeeList.do',
				params : {},
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			listeners : {
				beforeload : function(store, operation, eOpts) {
					Ext.apply(store.proxy.extraParams, {
								orderId : Ext
										.getCmp('crm_update_query_orderId')
										.getValue()
							});
				}
			}
		});
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
Ext.define('Rds.crm.form.CrmQueryForm', {
	extend : 'Ext.form.Panel',
	initComponent : function() {

		var me = this;

		var sampleForm = Ext.create("Ext.form.Panel", {
			region : "center",
			id : 'crm_query_crmsampleform',
			layout : 'anchor',
			autoScroll : true,
			border : false,
			items : [{
						xtype : 'toolbar',
						name : 'search',
						border : false,
						dock : 'top',
						items : []
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
		});

		var isExtendOrder = Ext.create('Ext.form.ComboBox', {
					name : 'isExtendOrder',
					fieldLabel : '延伸案例',
					readOnly : true,
					columnWidth : .45,
					labelWidth : 80,
					editable : false,
					mode : 'remote',
					forceSelection : true,
					allowBlank : true,
					triggerAction : 'all',
					displayField : 'value',

					valueField : 'key',
					store : isOK,
					value : '0',
					listeners : {
						change : function(value, newValue, oldValue, eOpts) {
						}
					}
				});

		var detection_class = Ext.create('Ext.form.ComboBox', {
					name : 'detectionClass',
					fieldLabel : '检测项目',
					readOnly : true,
					columnWidth : .45,
					labelWidth : 80,
					editable : false,
					allowBlank : true,
					displayField : 'name',

					valueField : 'id',
					store : detection_classStore,
					listeners : {
						select : function(combo, records, eOpts) {
							detection_org.reset();
							Ext.apply(detection_orgStore.proxy.extraParams, {
										dic_id : this.getValue()
									});
							detection_orgStore.load();
						}
					}
				});

		var detection_org = Ext.create('Ext.form.ComboBox', {
					name : 'detectionOrg',
					fieldLabel : '检测单位',
					readOnly : true,
					columnWidth : .45,
					labelWidth : 80,
					editable : false,
					allowBlank : true,
					displayField : 'name',

					valueField : 'id',
					store : detection_orgStore,
					listeners : {
						beforeactivate : function(combo, eOpts) {
							Ext.apply(detection_orgStore.proxy.extraParams, {
										dic_id : detection_class.getValue()
									});
						}
					}
				});
		var standFee = new Ext.form.field.Number({
					// 该列在整行中所占的百分比
					columnWidth : .45,

					readOnly : true,
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

					readOnly : true,
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
					readOnly : true,

					fieldLabel : "委托人",
					allowBlank : true,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is
					labelWidth : 80,
					maxLength : 50,
					allowDecimals : false,
					name : 'client'
				});

		var baseForm = Ext.create("Ext.form.Panel", {
					region : "center",
					// id : 'crm_query_crmbaseform',
					layout : 'form',
					autoScroll : true,
					defaults : {
						readOnly : true,
						defaults : {
							readOnly : true
						}
					},
					border : false,
					items : [{
								xtype : 'hiddenfield',
								id : 'crm_update_query_orderId',
								name : 'orderId'
							}, {
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
											// 该列在整行中所占的百分比
											columnWidth : .45,
											xtype : "textfield",

											labelWidth : 80,
											fieldLabel : "手机号码",
											allowBlank : false,// 不允许为空
											blankText : "不能为空",// 错误提示信息，默认为This
											// field
											// is
											// readOnly : true,
											maxLength : 50,
											name : 'phone',
											listeners : {
												blur : function(textfield, The,
														eOpts) {

												}
											}
										}, {
											xtype : 'datefield',
											name : 'orderInDate',
											columnWidth : .45,
											labelWidth : 80,
											fieldLabel : '登记时间',

											format : 'Y-m-d',
											allowBlank : true,// 不允许为空
											maxValue : new Date(),
											value : Ext.Date.add(new Date(),
													Ext.Date.DAY)
										}]
							}, {
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [Ext.create('Ext.form.ComboBox', {
											name : 'orderType',
											fieldLabel : '下单方式',
											columnWidth : .45,
											labelWidth : 80,
											editable : false,
											allowBlank : true,
											readOnly : true,
											displayField : 'value',

											valueField : 'key',
											store : orderTypeStore,
											listeners : {
												change : function(value,
														newValue, oldValue,
														eOpts) {
												}
											}
										}), Ext.create('Ext.form.ComboBox', {
											name : 'isPostpaid',
											fieldLabel : '先出报告',
											readOnly : true,
											columnWidth : .45,
											labelWidth : 80,
											editable : false,
											allowBlank : true,
											displayField : 'value',

											valueField : 'key',
											store : isOK,
											value : 0,
											listeners : {
												change : function(value,
														newValue, oldValue,
														eOpts) {
												}
											}
										})]
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
											maxLength : 500,
											columnWidth : 0.9,
											labelWidth : 80
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
											maxLength : 500,
											columnWidth : 0.9,
											labelWidth : 80
										}]
							}, {
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [standFee, isExtendOrder]
							}]
				});
		var feeGrid = Ext.create('Ext.grid.Panel', {
					// title : '历史财务',
					loadMask : true,
					height : 300,
					columnWidth : 0.9,
					viewConfig : {
						trackOver : false,
						stripeRows : false,
						enableTextSelection : true
					},
					columns : [{
								text : '实付金额',
								dataIndex : 'realFee',
								menuDisabled : true,
								flex : 1
							}, {
								text : '付款时间',
								dataIndex : 'feeTime',
								menuDisabled : true,
								flex : 1
							}],
					store : orderFeeStore,
					bbar : Ext.create('Ext.PagingToolbar', {
								store : orderFeeStore,
								pageSize : 25,
								displayInfo : true,
								displayMsg : "第 {0} - {1} 条  共 {2} 条",
								emptyMsg : "没有符合条件的记录"
							}),
					listeners : {
						'afterrender' : function() {
						},
						'select' : function(value, record) {
						}
					}

				});
		var feeform = Ext.create("Ext.form.Panel", {
					region : "center",
					// id : 'crmfeeform',
					layout : 'form',
					autoScroll : true,
					border : false,
					items : [{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [new Ext.form.field.Text({
											columnWidth : .45,

											fieldLabel : "已付金额",
											allowBlank : true,// 不允许为空
											readOnly : true,
											blankText : "不能为空",// 错误提示信息，默认为This
											labelWidth : 80,
											maxLength : 50,
											allowDecimals : false,
											name : 'paidFee'
										})]
							}, {
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [feeGrid]
							}]
				});

		me.items = [{
					xtype : 'form',
					bodyPadding : 10,
					layout : "anchor",
					defaults : {
						readOnly : true,
						anchor : '100%',
						defaults : {
							margin : '10 10 10 10',
							height : 370,
							readOnly : true
						}
					},
					border : false,
					autoHeight : true,
					items : [{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [{
											columnWidth : .45,
											xtype : 'fieldset',
											title : '基本信息',
											defaultType : 'textfield',
											layout : 'anchor',
											bodyPadding : '5 5 5 5',
											defaults : {
												anchor : '100%'
											},
											items : [baseForm]
										}, {
											columnWidth : .45,
											xtype : 'fieldset',
											title : '样本信息',
											defaultType : 'textfield',
											layout : 'anchor',
											autoScroll : true,
											defaults : {
												anchor : '100%'
											},
											items : [sampleForm]
										}]
							}, {
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [{
											columnWidth : .45,
											xtype : 'fieldset',
											title : '财务信息',
											layout : 'anchor',
											autoScroll : true,
											defaults : {
												anchor : '100%'
											},
											items : [feeform]
										}]
							}]
				}];

		me.buttons = [{
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}];
		me.callParent(arguments);
	},

	onCancel : function() {
		var me = this;
		me.up("window").close();
	},
	listeners : {
		'afterrender' : function(form, eOpts) {
			var me = this;
			detection_classStore.load();
			sampleStore.load();
			var values = form.getValues();
			var orderId = values["orderId"];
			Ext.apply(detection_orgStore.proxy.extraParams, {
						dic_id : values["detectionClass"]
					});
			detection_orgStore.load();
			orderFeeStore.load();
			Ext.Ajax.request({
				url : "crm/register/getSampleName.do",
				method : "POST",
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : {
					'orderId' : orderId
				},
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					if (response.length > 0) {
						for (var i = 0; i < response.length; i++) {
							Ext.getCmp("crm_query_crmsampleform").add({
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
												readOnly : true,
												editable : true,
												mode : 'remote',
												allowBlank : true,
												triggerAction : 'all',
												displayField : 'name',
												valueField : 'id',
												value : response[i].sampleName,
												store : sampleStore,
												listeners : {
													change : function(value,
															newValue, oldValue,
															eOpts) {

													}
												}
											})]

								}]
							});
						}
					}
				},
				failure : function() {
					return;
				}
			});
		}
	}
});
