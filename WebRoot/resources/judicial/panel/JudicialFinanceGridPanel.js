/**
 * 
 * 财务管理
 * 
 * @author fushaoming 2015.4.20
 */
Ext.define('Rds.judicial.panel.JudicialFinanceGridPanel', {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	initComponent : function() {
		var me = this;
		var search = Ext.create('Ext.form.field.Text', {
					name : 'search',
					labelWidth : 60,
					width : 200,
					fieldLabel : '汇款人'
				});
		var startTime = Ext.create('Ext.form.field.Date', {
					name : 'starttime',
					width : 175,
					fieldLabel : '汇款时间 从',
					labelWidth : 70,
					labelAlign : 'right',
					emptyText : '请选择日期',
					format : 'Y-m-d',
					maxValue : new Date(),
					listeners : {
						'select' : function() {
							var start = startTime.getValue();
							endTime.setMinValue(start);
							var endDate = endTime.getValue();
							if (start > endDate) {
								endTime.setValue(start);
							}
						}
					}

				});
		var endTime = Ext.create('Ext.form.field.Date', {
					name : 'endtime',
					width : 135,
					labelWidth : 20,
					fieldLabel : '到',
					labelAlign : 'right',
					emptyText : '请选择日期',
					format : 'Y-m-d',
					maxValue : new Date(),
					value : Ext.Date.add(new Date(), Ext.Date.DAY),
					listeners : {
						select : function() {
							var start = startTime.getValue();
							var endDate = endTime.getValue();
							if (start > endDate) {
								startTime.setValue(endDate);
							}
						}
					}
				});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['fee_id', 'username', 'date', 'sum', 'realsum',
							'status', 'bank', 'bankaccount', 'remark','check_remark'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'judicial/finance/queryallpage.do',
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
										search : search.getValue(),
										startTime : startTime.getValue(),
										endTime : endTime.getValue()
									});
						}
					}
				});

		me.selModel = Ext.create('Ext.selection.CheckboxModel', {
					mode : 'SINGLE'
				});

		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : 25,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
					dataIndex : 'fee_id',
					hidden : true
				}, {
					text : '汇款人',
					dataIndex : 'username',
					width : '5%',
					menuDisabled : true
				}, {
					text : '汇款日期',
					dataIndex : 'date',
					width : '10%',
					menuDisabled : true
				}, {
					text : '实际金额',
					dataIndex : 'realsum',
					width : '10%',
					menuDisabled : true
				}, {
					text : '应付金额',
					dataIndex : 'sum',
					width : '10%',
					menuDisabled : true
				}, {
					text : '审核状态',
					dataIndex : 'status',
					width : '5%',
					menuDisabled : true,
					renderer : function(value) {
						switch (value) {
							case 0 :
								return "<span style='color:red'>未审核</span>";
								break;
							case 1 :
								return "已审核";
								break;
							case 2 :
								return "<span style='color:red'>未通过</span>";
								break;
						}
					}
				}, {
					text : '银行',
					dataIndex : 'bank',
					width : '10%',
					menuDisabled : true
				}, {
					text : '账户',
					dataIndex : 'bankaccount',
					width : '15%',
					menuDisabled : true
				}, {
					text : '备注',
					dataIndex : 'remark',
					width : '15%',
					menuDisabled : true
				},
				{
					text : '审核备注',
					dataIndex : 'check_remark',
					width : '20%',
					menuDisabled : true
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [search, startTime, endTime, {
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				}, {
					xtype : 'toolbar',
					dock : 'top',
					items : [{
								text : '确认收款',
								iconCls : 'Applicationformmagnify',
								handler : me.onVerify
							}, '-', {
								text : '相关案例信息',
								iconCls : 'Borderdraw',
								handler : me.onRelativeCase
							}]
				}];

		me.callParent(arguments);
	},
	onRelativeCase : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择财务记录!");
			return;
		}
		var fee_id = selections[0].get('fee_id');
		var form = Ext.create("Ext.form.Panel", {
					region : "north",
					grid : me,
					height : 60,
					bodyPadding : '5 5 5 5',
					border : 'none',
					layout : 'column',
					items : [{

								xtype : 'textfield',
								name : 'fee_id',
								hidden : true
							}, {
								columnWidth : 0.15,
								labelWidth : 55,
								xtype : 'displayfield',
								fieldLabel : '汇款人',
								name : 'username'
							}, {
								columnWidth : 0.15,
								labelWidth : 60,
								xtype : 'displayfield',
								fieldLabel : '应汇金额',
								name : 'sum'
							}, {
								columnWidth : 0.15,
								labelWidth : 60,
								xtype : 'displayfield',
								fieldLabel : '实汇金额',
								name : 'realsum'
							}, {
								columnWidth : 1,
								labelWidth : 50,
								xtype : 'displayfield',
								fieldLabel : '备注',
								name : 'remark'
							}, caseGrid]
				});
		var caseStore = Ext.create('Ext.data.Store', {
					fields : ['case_id', 'case_code', 'receiver_area',
							'case_receiver', 'username', 'case_fee', 'num',
							'is_delete'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'judicial/finance/queryRealativeCase.do',
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
										fee_id : fee_id
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
						text : '收费',
						dataIndex : 'case_fee',
						width : '10%',
						menuDisabled : true
					}, {
						text : '样本数量',
						dataIndex : 'num',
						width : '10%',
						menuDisabled : true
					}, {
						text : '是否作废',
						dataIndex : 'is_delete',
						width : '10%',
						menuDisabled : true,
						renderer : function(value) {
							switch (value) {
								case 0 :
									return "正常";
									break;
								case 1 :
									return "<span style='color:red'>已作废</span>";
									break;
							}
						}
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
					items : [form, caseGrid]

				});
		form.loadRecord(selections[0]);
		win.show();
	},
	onVerify : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要确认的记录!");
			return;
		}
		if (selections[0].get('status') == 1) {
			Ext.Msg.alert("提示", "该记录已确认!");
			return;
		}
		var form = Ext.create("Rds.judicial.form.JudicialFinanceForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '财务审核',
					width : 500,
					iconCls : 'Pageedit',
					height : 500,
					layout : 'border',
					items : [form]
				});
		form.loadRecord(selections[0]);
		win.show();
	},

	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().load({params:{start:0}});

	},
	listeners : {
		'afterrender' : function() {
			this.store.load();
		}
	}
});