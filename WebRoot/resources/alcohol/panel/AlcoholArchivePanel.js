/**
 * @author fushaoming
 * @date 20150424
 * @description 案例归档panel
 */
Ext.define("Rds.alcohol.panel.AlcoholArchivePanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		me.store = Ext.create('Ext.data.Store', {
			fields : ['case_id', 'case_code', "client", "checkper","checkper_phone",
					"area_code", "event_desc", "area_name", "remark",
					 "case_in_pername", "report_model",
					"report_modelname", "accept_time", "mail_address",
					"sample_id", "mail_per", 'receiver', 'receiver_area',
					"mail_phone", "attachment", "stateStr", "case_in_per",
					"state"],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'alcohol/archive/getCaseInfo.do',
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
								.getCmp('endtime_archive_alcohol').getValue()),
						starttime : dateformat(Ext
								.getCmp('starttime_archive_alcohol').getValue()),
						case_code : trim(Ext
								.getCmp('case_code_archive_alcohol').getValue())
						
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
					text : '地址',
					dataIndex : 'address',
					menuDisabled : true,
					width : 200
				}, {
					hidden : true,
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
						id : 'case_code_archive_alcohol',
						labelWidth : 70,
						width : '20%',
						fieldLabel : '案例条形码'
					}, {
						xtype : 'datefield',
						name : 'starttime',
						id : 'starttime_archive_alcohol',
						width : '20%',
						fieldLabel : '受理时间 从',
						labelWidth : 70,
						labelAlign : 'right',
						emptyText : '请选择日期',
						format : 'Y-m-d',
						maxValue : new Date(),
						listeners : {
							'select' : function() {
								var start = Ext
										.getCmp('starttime_archive_alcohol')
										.getValue();
								Ext.getCmp('endtime_archive_alcohol')
										.setMinValue(start);
								var endDate = Ext
										.getCmp('endtime_archive_alcohol')
										.getValue();
								if (start > endDate) {
									Ext.getCmp('endtime_archive_alcohol')
											.setValue(start);
								}
							}
						}
					}, {
						xtype : 'datefield',
						id : 'endtime_archive_alcohol',
						name : 'endtime',
						width : '20%',
						labelWidth : 20,
						fieldLabel : '到',
						labelAlign : 'right',
						emptyText : '请选择日期',
						format : 'Y-m-d',
						maxValue : new Date(),
						listeners : {
							select : function() {
								var start = Ext
										.getCmp('starttime_archive_alcohol')
										.getValue();
								var endDate = Ext
										.getCmp('endtime_archive_alcohol')
										.getValue();
								if (start > endDate) {
									Ext.getCmp('starttime_archive_alcohol')
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
						text : '归档',
						iconCls : 'Bookgo',
						handler : me.onArchive
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
	onArchive : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要归档的记录!");
			return;
		}
		mail_canel = function(mei) {
			mei.up("window").close();
		}
		mail_save = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			if (form.isValid()) {
				Ext.Ajax.request({
							url : "alcohol/archive/addArchiveInfo.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response == true) {
									Ext.MessageBox.alert("提示信息", "归档成功");
									me.getStore().load();
									win.close();
								} else {
									Ext.MessageBox.alert("错误信息", "归档失败");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
							}
						});
			}
		}
		var win = Ext.create("Ext.window.Window", {
			title : '案例归档',
			width : 400,
			iconCls : 'Pageedit',
			height : 300,
			layout : 'border',
			modal : true,
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
				items : [{
							xtype : "hiddenfield",
							value : selections[0].get("case_id"),
							name : 'case_id'
						}, {
							xtype : "textfield",
							fieldLabel : '归档位置',
							allowBlank : false,
							maxLength : 50,
							name : 'archive_address'
						}, {
							xtype : "textfield",
							fieldLabel : '案例条形码',
							readOnly : true,
							allowBlank : false,
							value : selections[0].get("case_code"),
							name : 'case_code'
						}, {
							xtype : "datefield",
							fieldLabel : '归档日期',
							allowBlank : false,
							name : 'archive_date',
							value : new Date(),
							format : 'Y-m-d',
							maxValue : new Date()
							
						}]
			}]
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
