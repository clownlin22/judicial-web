Ext.define("Rds.judicial.panel.JudicialFinanceConfirmPanel", {
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
		var search = Ext.create('Ext.form.field.Text', {
					name : 'username',
					labelWidth : 70,
					width : '20%',
					fieldLabel : '销售经理'
				});
		var type = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '日报类型',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
				fields : [ 'Name', 'Code' ],
				data : [ [ '全部', '' ], [ '亲子鉴定', 1 ], [ '合同计划', 2 ] ]
			}),
			value : '',
			mode : 'local',
			name : 'type',
		});
		me.store = Ext.create('Ext.data.Store', {
					fields : ["daily_id", "username", "userid", "sum", "type",
							"daily_time", "status", "balancetype",
							"confirm_time"],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/finance/getFinanceDaily.do',
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
							Ext.apply(me.store.proxy.params, {
										username : trim(search.getValue()),
										type:type.getValue()
									});
						}
					}
				});
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
		});
		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : me.pageSize,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
					text : 'daily_id',
					dataIndex : 'daily_id',
					hidden : true
					
				}, {
					text : '用户名',
					dataIndex : 'username',
					menuDisabled : true,
					width : 250

				}, {
					text : '日报金额',
					dataIndex : 'sum',
					menuDisabled : true,
					width : 200
				}, {
					text : '日报类型',
					dataIndex : 'type',
					menuDisabled : true,
					width : 220,
					renderer : function(value) {
						switch (value) {
							case 1 :
								return "亲子鉴定";
								break
							case 2 :
								return "合同计划";
								break;
							default :
								return "";
						}
					}
				}, {
					text : '日报生成时间',
					dataIndex : 'daily_time',
					menuDisabled : true,
					width : 250
				}, {
					text : '状态',
					dataIndex : 'status',
					menuDisabled : true,
					width : 220,
					renderer : function(value) {
						switch (value) {
							case 0 :
								return "未汇款";
								break;
							case 1 :
								return "已汇款";
								break;
							case -1 :
								return "已结算";
								break;
							default :
								return "";
						}
					},
					hidden:true
				}, {
					text : '结算类型',
					dataIndex : 'balancetype',
					menuDisabled : true,
					width : 200,
					renderer : function(value) {
						switch (value) {
							case 0 :
								return "日结算";
								break;
							case 1 :
								return "月结算";
								break;
							default :
								return "";
						}
					},
					hidden:true
				}, {
					text : '确认时间',
					dataIndex : 'confirm_time',
					menuDisabled : true,
					width : 250,
					hidden:true
				}, {
					text : '确认状态',
					dataIndex : 'status',
					menuDisabled : true,
					width : 250,
					renderer : function(value) {
						if (value == null || value == "") {
							return "未确认";
						} else {
							return "<span style='color:green'>已确认</span>";
						}
					},
					hidden:true
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [search, type,{
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				}, {
					xtype : 'toolbar',
					dock : 'top',
					items : [{
								text : '案例汇款',
								iconCls : 'Pageadd',
								handler : me.onRemittance,
								hidden:true
							},{
								text : '确认日报',
								iconCls : 'Pageadd',
								handler : me.onConfirm,
								hidden:true
							}, {
								text : '日报明细',
								iconCls : 'Find',
								handler : me.onRelativeCase
							}]
				}];

		me.callParent(arguments);
	},
	onRemittance:function(){
		
	},
	onConfirm : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要确认的记录!");
			return;
		};
		if (selections[0].get("confirm_time") != null
				&& selections[0].get("confirm_time") != "") {
			Ext.Msg.alert("提示", "此日报已确认!");
			return;
		}
		Ext.MessageBox.confirm('提示', '确定确认此日报吗', function(id) {
					if (id == 'yes') {
						Ext.Ajax.request({
									url : "judicial/finance/confirmFinanceDaily.do",
									method : "POST",
									headers : {
										'Content-Type' : 'application/json'
									},
									jsonData : {
										id : selections[0].get("daily_id")
									},
									success : function(response, options) {
										response = Ext.JSON
												.decode(response.responseText);
										if (response == true) {
											Ext.MessageBox.alert("提示信息",
													"确认成功!");
										} else {
											Ext.MessageBox.alert("错误信息",
													"确认失败!");
										}
										me.getStore().load();
									},
									failure : function() {
										Ext.Msg.alert("提示", "连接异常<br>请联系管理员!");
									}
								});
					}
				});

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
	},
	onRelativeCase : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请一条选择记录查看!");
			return;
		}
		var dailyid = selections[0].get('daily_id');
		var type = selections[0].get("type");
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
								daily_type:type
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
	}
});
