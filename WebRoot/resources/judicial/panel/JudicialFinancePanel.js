
var username = Ext.create('Ext.form.field.Text', {
			name : 'username',
			labelWidth : 60,
			width : 200,
			fieldLabel : '销售经理'
		});
var financeStore = Ext.create('Ext.data.Store', {
			fields : ['userid', 'username', 'return_all_sum', 'all_sum',
					'balance'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'post'
				},
				url : 'judicial/finance/queryallsum.do',
				params : {},
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'total'
				}
			},
			listeners : {
				'beforeload' : function(ds, operation, opt) {
					Ext.apply(financeStore.proxy.params, {
								username : username.getValue()
							});
				}
			}
		});
var detailStore = Ext.create('Ext.data.Store', {
			fields : ['date', 'return_sum', 'sum'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'post'
				},
				url : 'judicial/finance/querydetail.do',
				params : {},
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'total'
				}
			}
		});
Ext.define("Rds.judicial.panel.JudicialFinancePanel", {
			extend : "Ext.panel.Panel",
			layout : 'border',
			items : [{
				xtype : 'gridpanel',
				region : 'center',
				title : '回款总览',
				columns : [{
							dataIndex : 'userid',
							hidden : true
						}, {
							text : '销售经理',
							dataIndex : 'username',
							width : '20%',
							menuDisabled : true
						}, {
							text : '应回款总额',
							width : '20%',
							dataIndex : 'return_all_sum',
							menuDisabled : true
						}, {
							text : '已回款总额',
							width : '20%',
							dataIndex : 'all_sum',
							menuDisabled : true
						}, {
							text : '余额',
							width : '20%',
							dataIndex : 'balance',
							menuDisabled : true,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (value < 0) {
									return "<div style=\"color: red;\">"
											+ value + "</div>";
								} else {
									return value;
								}
							}
						}],
				store : financeStore,
				dockedItems : [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [{
						xtype : 'toolbar',
						dock : 'top',
						items : [{
							text : '添加',
							iconCls : 'Add',
							handler : function() {
								var me = this.up("gridpanel");
								var selections = me.getView()
										.getSelectionModel().getSelection();
								if (selections.length < 1) {
									Ext.Msg.alert("提示", "请选择需要新增的销售经理");
									return;
								};
								var form = Ext
										.create(
												"Rds.judicial.form.JudicialFinanceAddForm",
												{
													region : "center",
													grid : me
												});
								var win = Ext.create("Ext.window.Window", {
											title : '回款——新增',
											width : 400,
											iconCls : 'Add',
											height : 400,
											layout : 'border',
											items : [form]
										});
								form.loadRecord(selections[0]);
								win.show();
							}
						}, '-', username, {
							text : '查询',
							iconCls : 'Find',
							handler : function() {
								var me = this.up("gridpanel");
								me.getStore().load({
											params : {
												start : 0
											}
										});
							}
						}]
					}]
				}],
				listeners : {
					'afterrender' : function() {
						financeStore.load();
					},
					'select' : function(value, record) {
						console.log(record.isModel);
						Ext.getCmp('financeform').loadRecord(record);
						Ext.apply(detailStore.proxy.params, {
									userid : record.get('userid')
								});
						detailStore.load();
					}
				}

			}, {
				xtype : 'panel',
				region : 'east',
				border : false,
				title : '回款明细',
				width : '30%',
				layout : 'border',
				items : [{
							xtype : 'form',
							border : false,
							id : 'financeform',
							bodyPadding : '10 10 10 10',
							layout : 'form',
							region : 'center',
							defaults : {},
							items : [{
										xtype : "textfield",
										hidden : true,
										name : 'userid'
									}, {
										xtype : 'textfield',
										fieldLabel : '销售经理',
										readOnly : true,
										labelWidth : 100,
										name : 'username'
									}, {
										xtype : 'textfield',
										fieldLabel : '应回款总额',
										readOnly : true,
										labelWidth : 100,
										name : 'return_all_sum'

									}, {
										xtype : 'textfield',
										fieldLabel : '已汇款总额',
										readOnly : true,
										labelWidth : 100,
										name : 'all_sum'

									}, {
										xtype : 'textfield',
										fieldLabel : '余额',
										readOnly : true,
										labelWidth : 100,
										name : 'balance'
									}]
						}, {
							xtype : 'grid',
							region : 'south',
							height : '70%',
							store : detailStore,
							columns : [{
										text : '日期',
										dataIndex : 'date',
										width : '33%'
									}, {
										text : '应回款额',
										dataIndex : 'return_sum',
										width : '33%'
									}, {
										text : '已回款额',
										width : '33%',
										dataIndex : 'sum'
									}]
						}]
			}]

		});
