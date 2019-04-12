Ext.define("Rds.judicial.panel.JudicialMonthlyVerifyPanel", {
			extend : "Ext.grid.Panel",
			loadMask : true,
			viewConfig : {
				trackOver : false,
				stripeRows : false
			},
			pageSize : 25,
			initComponent : function() {
				var me = this;
				var search = Ext.create('Ext.form.field.Text', {
							name : 'search',
							labelWidth : 60,
							width : 200,
							fieldLabel : '销售经理'
						});
				me.store = Ext.create('Ext.data.Store', {
							fields : ['id', 'userid', 'username', 'sum',
									'discountsum', 'daily_time', 'status',
									'remark'],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/finance/getAllMonthly.do',
								params : {},
								reader : {
									type : 'json',
									root : 'data',
									totalProperty : 'total'
								}
							},
							listeners : {
								'beforeload' : function(ds, operation, opt) {
									Ext.apply(me.store.proxy.params, {
												search : search.getValue()
											});
								}
							}
						});

				me.selModel = Ext.create('Ext.selection.CheckboxModel', {
							mode : 'SINGLE'
						});

				me.bbar = Ext.create('Ext.PagingToolbar', {
							store : me.store,
							pageSize : me.pageSize,
							displayInfo : true,
							displayMsg : "第 {0} - {1} 条  共 {2} 条",
							emptyMsg : "没有符合条件的记录"
						});
				me.columns = [{
							dataIndex : 'id',
							hidden : true
						}, {
							dataIndex : 'userid',
							hidden : true
						}, {
							text : '销售经理',
							dataIndex : 'username',
							width : '10%',
							menuDisabled : true
						}, {
							text : '月报金额',
							dataIndex : 'sum',
							width : '10%',
							menuDisabled : true
						}, {
							text : '折扣金额',
							dataIndex : 'discountsum',
							width : '10%',
							menuDisabled : true
						}, {
							text : '月报生成日期',
							dataIndex : 'daily_time',
							width : '10%',
							menuDisabled : true
						}, {
							text : '状态',
							dataIndex : 'status',
							width : '10%',
							menuDisabled : true,
							renderer : function(value) {
								if (value == 2) {
									return "<div style=\"color: red;\">未审核</div>";
								} else if (value == 1) {
									return "已汇款";
								}else{
									return "未汇款";
								}
							}
						}, {
							text : '备注',
							dataIndex : 'remark',
							width : '15%',
							menuDisabled : true
						}];

				me.dockedItems = [{
							xtype : 'toolbar',
							name : 'search',
							dock : 'top',
							items : [search, {
										text : '查询',
										iconCls : 'Find',
										handler : me.onSearch
									}]
						}, {
							xtype : 'toolbar',
							dock : 'top',
							items : [{
										text : '审核',
										iconCls : 'Pageedit',
										handler : me.onUpdate
									}, {
										text : '月报明细',
										iconCls : 'Find',
										handler : me.onRelativeCase
									}]
						}];
				me.store.load();
				me.callParent(arguments);
			},
			onRelativeCase : function() {
				var me = this.up("gridpanel");
				var selections = me.getView().getSelectionModel().getSelection();
				if (selections.length < 1 || selections.length > 1) {
					Ext.Msg.alert("提示", "请一条选择记录查看!");
					return;
				}
				var dailyid = selections[0].get('id');
				var form = Ext.create("Ext.form.Panel", {
							region : "north",
							grid : me,
							height : 60,
							bodyPadding : '5 5 5 5',
							border : 'none',
							layout : 'column',
							items : [{
										xtype : 'textfield',
										name : 'daily_id',
										hidden : true
									}, {
										columnWidth : 0.5,
										labelWidth : 100,
										xtype : 'displayfield',
										fieldLabel : '销售经理',
										name : 'username'
									}, {
										columnWidth : 0.5,
										labelWidth : 100,
										xtype : 'displayfield',
										fieldLabel : '金额',
										name : 'sum'
									}]
						});
				var caseStore = Ext.create('Ext.data.Store', {
							fields : ['case_id', 'case_code', 'receiver_area', 'case_receiver',
									'username', 'stand_sum', 'real_sum', 'return_sum',
									'date'],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'post'
								},
								url : 'judicial/finance/queryDailyDetail.do',
								params : {},
								reader : {
									type : 'json',
									root : 'data',
									totalProperty : 'total'
								}
							},
							listeners : {
								'beforeload' : function(ds, operation, opt) {
									Ext.apply(caseGrid.store.proxy.params, {
										dailyid : dailyid,
										daily_type:1
											});
								}
							}
						});
				var caseGrid = Ext.create("Ext.grid.Panel", {
							region : 'center',
							store : caseStore,
							columns : [{
										dataIndex : 'case_id',
										hidden : true
									}, {
										text : '登记人',
										dataIndex : 'username',
										width : '15%',
										menuDisabled : true
									}, {
										text : '案例编号',
										dataIndex : 'case_code',
										width : '25%',
										menuDisabled : true
									}, {
										text : '案例所属地',
										dataIndex : 'receiver_area',
										menuDisabled : true,
										width : '20%'
									}, {
										text : '案例所属人',
										dataIndex : 'case_receiver',
										menuDisabled : true,
										width : '10%'
									}, {
										text : '标准金额',
										dataIndex : 'stand_sum',
										width : '10%',
										menuDisabled : true
									}, {
										text : '实收金额',
										dataIndex : 'real_sum',
										width : '10%',
										menuDisabled : true
									}, {
										text : '汇款金额',
										dataIndex : 'return_sum',
										width : '10%',
										menuDisabled : true
									}],
							bbar : Ext.create('Ext.PagingToolbar', {
										store : caseStore,
										pageSize : 15,
										displayInfo : true,
										displayMsg : "第 {0} - {1} 条  共 {2} 条",
										emptyMsg : "没有符合条件的记录"
									}),
							listeners : {
								'afterrender' : {
									fn : function() {
										caseGrid.store.load();
									}
								}
							}

						});
				var win = Ext.create("Ext.window.Window", {
							title : '相关案例信息',
							width : 810,
							iconCls : 'Pageedit',
							height : 600,
							layout : 'border',
							modal : true,
							buttons : [{
										text : '确定',
										iconCls : 'Accept',
										handler : function() {
											this.up("window").close();
										}
									}],
							items : [form, caseGrid]

						});
				form.loadRecord(selections[0]);
				win.show();
			},
			onUpdate : function() {
				var me = this.up("gridpanel");
				var selections = me.getView().getSelectionModel()
						.getSelection();
				if (selections.length < 1) {
					Ext.Msg.alert("提示", "请选择需要修改的记录!");
					return;
				};
				if (selections[0].get("status")!=2){
					Ext.Msg.alert("提示", "该月报已审核，无需再审核。");
					return;
				}
				var form = Ext.create(
						"Rds.judicial.form.JudicialMonthlyVerifyForm", {
							region : "center",
							grid : me
						});
				var win = Ext.create("Ext.window.Window", {
							title : '月报审核——修改',
							width : 400,
							iconCls : 'Pageedit',
							height : 350,
							layout : 'border',
							items : [form]
						});
				win.show();
				form.loadRecord(selections[0]);
			},
			onSearch : function() {
				var me = this.up("gridpanel");
				me.getStore().load({
							params : {
								start : 0
							}
						});

			}
		});