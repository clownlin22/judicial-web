mail_canel = function(me) {
	me.up("window").close();
}
var mailStore = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'ajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getMailModels.do',
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});

Ext.define("Rds.alcohol.panel.AlcoholMailGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	region : 'center',
	pageSize : 25,
	initComponent : function() {
		var me = this;
		me.store = Ext.create('Ext.data.Store', {
					fields : ['case_id', 'case_code', "client", "checkper","checkper_phone",
							"area_code", "event_desc", "area_name", "remark",
							 "case_in_pername", "accept_time",
							"mail_address", "sample_id", "mail_per",
							'receiver', 'receiver_area', "mail_phone",
							"mail_count", "attachment", "stateStr",
							"case_in_per", "state"],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'alcohol/mail/getCaseInfo.do',
						params : {
							start : 0,
							limit : 25
						},
						reader : {
							type : 'json',
							root : 'items',
							totalProperty : 'count'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							Ext.apply(me.store.proxy.extraParams, {
										endtime : dateformat(Ext
												.getCmp('endtime_mail_alcohol')
												.getValue()),
										starttime : dateformat(Ext
												.getCmp('starttime_mail_alcohol')
												.getValue()),
										case_code : trim(Ext
												.getCmp('case_code_mail_alcohol')
												.getValue())
									});
						}
					}
				});

		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : me.pageSize,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
					text : '案例条形码',
					dataIndex : 'case_code',
					menuDisabled : true,
					width : 150
					
				}, {
					text : '委托人',
					dataIndex : 'client',
					width : 200,
					menuDisabled : true
				}, {
					text : '委托人号码',
					dataIndex : 'client_phone',
					menuDisabled : true,
					width : 200
				}, {
					text : '邮寄状态',
					dataIndex : 'state',
					menuDisabled : true,
					width : 200,
					renderer : function(value) {
						if (value == 4) {
							return "未邮寄"
						} else {
							return "<span style='color:green'>已邮寄</span>";
						}
					}
				}, {
					text : '案例归属地',
					dataIndex : 'receiver_area',
					menuDisabled : true,
					width : 200
				}, {
					text : '归属人',
					dataIndex : 'receiver',
					menuDisabled : true,
					width : 200
				}, {
					text : '所在地区',
					dataIndex : 'area_name',
					menuDisabled : true,
					width : 200
				}, {
					text : '邮寄地址',
					dataIndex : 'mail_address',
					menuDisabled : true,
					width : 100
					
				}, {
					text : '接收人',
					dataIndex : 'mail_per',
					menuDisabled : true,
					width : 100
					
				}, {
					text : '联系电话',
					dataIndex : 'mail_phone',
					menuDisabled : true,
					width : 100
					
				}, {
					text : '事件描述',
					dataIndex : 'event_desc',
					menuDisabled : true,
					width : 500
				}, {
					text : '受理时间',
					dataIndex : 'accept_time',
					menuDisabled : true,
					width : 200
				}, {
					text : '登记人',
					dataIndex : 'case_in_pername',
					menuDisabled : true,
					width : 120
				}, {
					text : '备注',
					dataIndex : 'remark',
					menuDisabled : true,
					width : 300
				}];

		me.dockedItems = [{
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [{
						xtype : 'textfield',
						name : 'case_code',
						id : 'case_code_mail_alcohol',
						labelWidth : 70,
						width : 155,
						fieldLabel : '案例条形码'
					}, {
						xtype : 'datefield',
						name : 'starttime',
						id : 'starttime_mail_alcohol',
						width : 175,
						fieldLabel : '受理时间 从',
						labelWidth : 70,
						labelAlign : 'right',
						emptyText : '请选择日期',
						format : 'Y-m-d',
						maxValue : new Date(),
						listeners : {
							'select' : function() {
								var start = Ext
										.getCmp('starttime_mail_alcohol')
										.getValue();
								Ext.getCmp('endtime_mail_alcohol')
										.setMinValue(start);
								var endDate = Ext
										.getCmp('endtime_mail_alcohol')
										.getValue();
								if (start > endDate) {
									Ext.getCmp('endtime_mail_alcohol')
											.setValue(start);
								}
							}
						}
					}, {
						xtype : 'datefield',
						id : 'endtime_mail_alcohol',
						name : 'endtime',
						width : 135,
						labelWidth : 20,
						fieldLabel : '到',
						labelAlign : 'right',
						emptyText : '请选择日期',
						format : 'Y-m-d',
						maxValue : new Date(),
						listeners : {
							select : function() {
								var start = Ext
										.getCmp('starttime_mail_alcohol')
										.getValue();
								var endDate = Ext
										.getCmp('endtime_mail_alcohol')
										.getValue();
								if (start > endDate) {
									Ext.getCmp('starttime_mail_alcohol')
											.setValue(endDate);
								}
							}
						}
					}, {
						text : '查询',
						iconCls : 'Find',
						handler : me.onSearch
					}]
		}, {
			xtype : 'toolbar',
			dock : 'top',
			items : [{
						text : '查看样品信息',
						iconCls : 'Find',
						handler : me.onFind
					}, {
						text : '邮件管理',
						iconCls : 'Box',
						handler : me.onMail
					}]
		}];

		me.callParent(arguments);
	},
	onFind : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		};
		Ext.Ajax.request({
			url : "alcohol/register/getSampleInfo.do",
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : {
				sample_id : selections[0].get("sample_id")
			},
			success : function(response, options) {
				response = Ext.JSON.decode(response.responseText);
				var sex = "男";
				if (response.sample_sex == 0) {
					sex = "女"
				}
				var win = Ext.create("Ext.window.Window", {
							title : "样本信息（案例条形码："
									+ selections[0].get("case_code") + "）",
							width : 600,
							iconCls : 'Find',
							height : 190,
							bodyStyle : "background-color:white;",
							items : [{
										xtype : "container",
										layout : "column",
										height : 35,
										items : [{
													xtype : "displayfield",
													columnWidth : .45,
													labelAlign : 'right',
													labelWidth : 80,
													fieldLabel : "姓名",
													value : response.sample_name
												}]
									}, {
										xtype : "container",
										layout : "column",
										height : 35,
										items : [{
													xtype : "displayfield",
													columnWidth : .45,
													labelAlign : 'right',
													labelWidth : 80,
													fieldLabel : "身份证号",
													value : response.id_number
												}]
									}, {
										xtype : "container",
										layout : "column",
										height : 35,
										items : [
//											{
//													xtype : "displayfield",
//													columnWidth : .45,
//													labelAlign : 'right',
//													labelWidth : 80,
//													fieldLabel : "采样剂量",
//													value : response.sample_ml
//															+ "mL"
//												}
												]
									}]
						});
				win.show();
			},
			failure : function() {
				Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
			}
		});
	},
	onMail : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要邮寄的记录!");
			return;
		}
		var address = selections[0].get("mail_address");
		if (selections[0].get("mail_address").length > 15) {
			address = "<span title='" + selections[0].get("mail_address")
					+ "'>" + selections[0].get("mail_address").substring(0, 15)
					+ "..</span>";
		}
		mail_onDel = function(mei) {
			var grid = mei.up("gridpanel");
			var selections_mail = grid.getView().getSelectionModel()
					.getSelection();

			if (selections_mail.length < 1) {
				Ext.Msg.alert("提示", "请选择需要删除的记录!");
				return;
			}
			Ext.Ajax.request({
						url : "alcohol/mail/delMailInfo.do",
						method : "POST",
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : {
							"mail_id" : selections_mail[0].get("mail_id"),
							"case_id" : selections[0].get("case_id")
						},
						success : function(response, options) {
							grid.store.load();
						},
						failure : function() {
							Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
						}
					});
		}
		mail_onInsert = function(me) {
			mail_save = function(mei) {
				var form = mei.up("form").getForm();
				var values = form.getValues();
				values["case_id"] = selections[0].get("case_id");
				if (form.isValid()) {
					Ext.Ajax.request({
								url : "alcohol/mail/addMailInfo.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : values,
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response == true) {
										Ext.MessageBox.alert("提示信息", "添加邮件成功");
										var grid = me.up("gridpanel");
										grid.getStore().load();
										mailform_add.close();
									} else {
										Ext.MessageBox.alert("错误信息", "添加邮件失败");
									}
								},
								failure : function() {
									Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
								}
							});
				}
			}
			var mailform_add = Ext.create("Ext.window.Window", {
				title : '快递信息',
				width : 250,
				height : 200,
				layout : 'border',
				items : [{
					xtype : 'form',
					region : 'center',
					style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					labelAlign : "right",
					bodyPadding : 10,
					defaults : {
						anchor : '100%',
						labelWidth : 80,
						labelSeparator : "：",
						labelAlign : 'right'
					},
					border : false,
					autoHeight : true,
					buttons : [{
								text : '保存',
								iconCls : 'Disk',
								handler : mail_save
							}, {
								text : '取消',
								iconCls : 'Cancel',
								handler : mail_canel
							}],
					items : [new Ext.form.field.ComboBox({
										fieldLabel : '快递类型',
										labelWidth : 80,
										editable : false,
										triggerAction : 'all',
										labelAlign : 'right',
										// required!
										valueField : "key",
										displayField : "value",
										allowBlank : false,// 不允许为空
										blankText : "不能为空",// 错误提示信息，默认为This
															// field is
										// required!
										store : mailStore,
										mode : 'local',
										// typeAhead: true,
										name : 'mail_type'
									}), {
								xtype : "textfield",
								fieldLabel : '快递单号',
								labelAlign : 'right',
								maxLength : 18,
								labelWidth : 80,
								name : 'mail_code'
							}, {
								xtype : "textfield",
								fieldLabel : '收件人',
								labelAlign : 'right',
								maxLength : 18,
								allowBlank : false,// 不允许为空
								blankText : "不能为空",// 错误提示信息，默认为This field is
													// required!
								labelWidth : 80,
								name : 'mail_per'
							}]
				}]
			})
			mailform_add.show();
		}

		mail_onUpdate = function(me) {
			var grid = me.up("gridpanel");
			var selections_mail = grid.getView().getSelectionModel()
					.getSelection();

			if (selections_mail.length < 1) {
				Ext.Msg.alert("提示", "请选择需要修改的记录!");
				return;
			}
			mail_update = function(mei) {
				var form = mei.up("form").getForm();
				var values = form.getValues();
				values["case_id"] = selections[0].get("case_id");
				if (form.isValid()) {
					Ext.Ajax.request({
								url : "alcohol/mail/updateMailInfo.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : values,
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response == true) {
										Ext.MessageBox.alert("提示信息", "修改邮件成功");
										var grid = me.up("gridpanel");
										grid.getStore().load();
										mailform_update.close();
									} else {
										Ext.MessageBox.alert("错误信息", "修改邮件失败");
									}
								},
								failure : function() {
									Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
								}
							});
				}
			}
			var mailform_update = Ext.create("Ext.window.Window", {
				title : '快递信息',
				width : 250,
				height : 200,
				layout : 'border',
				items : [{
					xtype : 'form',
					region : 'center',
					style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					labelAlign : "right",
					bodyPadding : 10,
					defaults : {
						anchor : '100%',
						labelWidth : 80,
						labelSeparator : "：",
						labelAlign : 'right'
					},
					border : false,
					autoHeight : true,
					buttons : [{
								text : '保存',
								iconCls : 'Disk',
								handler : mail_update
							}, {
								text : '取消',
								iconCls : 'Cancel',
								handler : mail_canel
							}],
					items : [new Ext.form.field.ComboBox({
										fieldLabel : '快递类型',
										labelWidth : 80,
										editable : false,
										triggerAction : 'all',
										labelAlign : 'right',
										value : selections_mail[0]
												.get("mail_type"),
										allowBlank : false,// 不允许为空
										blankText : "不能为空",// 错误提示信息，默认为This
															// field is
										valueField : "key",
										displayField : "value",
										allowBlank : false,// 不允许为空
										blankText : "不能为空",// 错误提示信息，默认为This
															// field is
										// required!
										store : mailStore,
										mode : 'local',
										// typeAhead: true,
										name : 'mail_type'
									}), {
								xtype : "textfield",
								fieldLabel : '快递单号',
								labelAlign : 'right',
								maxLength : 18,
								value : selections_mail[0].get("mail_code"),
								labelWidth : 80,
								name : 'mail_code'
							}, {
								xtype : "textfield",
								fieldLabel : '收件人',
								labelAlign : 'right',
								maxLength : 18,
								allowBlank : false,// 不允许为空
								blankText : "不能为空",// 错误提示信息，默认为This field is
													// required!
								labelWidth : 80,
								name : 'mail_per',
								value : selections_mail[0].get("mail_per")
							}, {
								xtype : 'hiddenfield',
								name : 'mail_id',
								value : selections_mail[0].get("mail_id")
							}]
				}]
			})
			mailform_update.show();
		}
		queryMailInfo = function(value, mail_type) {
			var win = Ext.create("Ext.window.Window", {
						title : "快递信息（快递单号：" + value + "）",
						width : 700,
						iconCls : 'Find',
						height : 350,
						layout : 'border',
						border : false,
						bodyStyle : "background-color:white;",
						items : [Ext.create('Ext.grid.Panel', {
									width : 700,
									height : 350,
									frame : false,
									renderTo : Ext.getBody(),
									viewConfig : {
										forceFit : true,
										stripeRows : true
										// 在表格中显示斑马线
									},
									store : {// 配置数据源
										fields : ['time', 'content'],// 定义字段
										proxy : {
											type : 'jsonajax',
											actionMethods : {
												read : 'POST'
											},
											url : 'alcohol/mail/getMailById.do',
											params : {
												'mail_code' : value,
												'mail_type' : mail_type
											},
											reader : {
												type : 'json',
												root : 'items',
												totalProperty : 'count'
											}
										},
										autoLoad : true
										// 自动加载
									},
									columns : [// 配置表格列
									{
												header : "时间",
												dataIndex : 'time',
												flex : 1.5,
												menuDisabled : true
											}, {
												header : "地点",
												dataIndex : 'content',
												flex : 3,
												menuDisabled : true
											}]
								})]
					});
			win.show();
		}
		var win = Ext.create("Ext.window.Window", {
			title : "快递信息（案例条形码：" + selections[0].get("case_code") + "）",
			width : 600,
			iconCls : 'Find',
			height : 500,
			layout : 'border',
			bodyStyle : "background-color:white;",
			items : [Ext.create("Ext.form.Panel", {
				width : 580,
				height : 150,
				border : "black",
				region : 'north',
				defaults : {
					anchor : '100%',
					labelWidth : 80,
					labelAlign : 'right'
				},
				autoHeight : true,
				items : [{
							xtype : "container",
							layout : "column",
							height : 35,
							items : [{
										xtype : "displayfield",
										columnWidth : .55,
										labelAlign : 'right',
										labelWidth : 80,
										value : selections[0].get("client"),
										fieldLabel : "委托人"
										
									}, {
										xtype : "displayfield",
										columnWidth : .4,
										labelAlign : 'right',
										labelWidth : 80,
										value : selections[0].get("area_name"),
										fieldLabel : "所在地区"
										
									}]
						}, {
							xtype : "container",
							layout : "column",
							height : 35,
							items : [{
										xtype : "displayfield",
										columnWidth : .55,
										labelAlign : 'right',
										labelWidth : 80,
										value : selections[0]
												.get("receiver_area"),
										fieldLabel : "案例归属地"
										
									}, {
										xtype : "displayfield",
										columnWidth : .4,
										labelAlign : 'right',
										labelWidth : 80,
										value : selections[0].get("receiver"),
										fieldLabel : "归属人"
										
									}]
						}, {
							xtype : "container",
							layout : "column",
							height : 35,
							items : [{
										xtype : "displayfield",
										columnWidth : .55,
										labelAlign : 'right',
										labelWidth : 80,
										value : selections[0].get("mail_per"),
										fieldLabel : "收件人"
										
									}, {
										xtype : "displayfield",
										columnWidth : .45,
										labelAlign : 'right',
										labelWidth : 80,
										value : selections[0].get("mail_phone"),
										fieldLabel : "电话"
										
									}]
						}, {
							xtype : "container",
							layout : "column",
							height : 35,
							items : [{
										xtype : "displayfield",
										columnWidth : .55,
										labelAlign : 'right',
										labelWidth : 80,
										value : address,
										fieldLabel : "寄送地址"
										
									}, {
										xtype : "displayfield",
										columnWidth : .4,
										labelAlign : 'right',
										labelWidth : 80,
										value : selections[0]
												.get("accept_time"),
										fieldLabel : "受理时间"
										
									}]
						}]
				
			}), Ext.create('Ext.grid.Panel', {
				region : 'north',
				width : 600,
				height : 350,
				frame : false,
				viewConfig : {
					forceFit : true,
					stripeRows : true
					// 在表格中显示斑马线
				},
				dockedItems : [{
							xtype : 'toolbar',
							dock : 'top',
							items : [{
										xtype : 'button',
										text : '新增',
										iconCls : 'Pageadd',
										handler : mail_onInsert
									}, {
										xtype : 'button',
										text : '修改',
										iconCls : 'Pageedit',
										handler : mail_onUpdate
									}, {
										xtype : 'button',
										text : '删除',
										iconCls : 'Delete',
										handler : mail_onDel
									}

							]
						}],
				store : {// 配置数据源
					fields : ['mail_type', 'mail_code', 'mail_typename',
							'mail_time', 'mail_id', 'mail_per'],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'alcohol/mail/getMailInfo.do',
						params : {
							'case_id' : selections[0].get("case_id")
						},
						reader : {
							type : 'json',
							root : 'items',
							totalProperty : 'count'
						}
					},
					autoLoad : true
					// 自动加载
				},
				columns : [// 配置表格列
				{
							header : "快递类型",
							dataIndex : 'mail_typename',
							flex : 1,
							menuDisabled : true
						}, {
							header : "快递编号",
							dataIndex : 'mail_code',
							flex : 1.5,
							menuDisabled : true
						}, {
							header : "收件人",
							dataIndex : 'mail_per',
							flex : 1,
							menuDisabled : true
						}, {
							header : "快递时间",
							dataIndex : 'mail_time',
							flex : 1,
							menuDisabled : true
						}, {
							header : "操作",
							dataIndex : 'mail_code',
							flex : 0.5,
							menuDisabled : true,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								var mail_type = record.data["mail_type"];
								if (mail_type == "ziqu") {
									return "";
								} else {
									return "<a href='#' onclick=\"queryMailInfo('"
											+ value
											+ "','"
											+ mail_type
											+ "')\"  >查询";
								}
							}
						}]
			})]
		});
		win.show();
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage = 1;
		me.getStore().load();
	},
	listeners : {
		'afterrender' : function() {
			this.store.load();
		}
	}
});
