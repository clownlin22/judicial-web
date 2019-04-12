Ext.define("Rds.judicial.panel.JudicialBillGridPanel", {
			extend : "Ext.grid.Panel",
			loadMask : true,
			viewConfig : {
				trackOver : false,
				stripeRows : false
			},
			pageSize : 25,
			initComponent : function() {
				var me = this;
				var billper = Ext.create('Ext.form.field.Text', {
							name : 'billper',
							labelWidth : 60,
							width : 200,
							hidden : false,
							fieldLabel : '开票人'
						});
				var casecode = Ext.create('Ext.form.field.Text', {
							name : 'casecode',
							labelWidth : 60,
							width : 200,
							regex : /^\w*$/,
							regexText : '请输入英文或数字',
							fieldLabel : '案例编号'
						});
				var billcode = Ext.create('Ext.form.field.Text', {
							name : 'billcode',
							labelWidth : 80,
							width : 200,
							regex : /^\w*$/,
							regexText : '请输入英文或数字',
							fieldLabel : '发票编号'
						});

				me.store = Ext.create('Ext.data.Store', {
							fields : ['case_id', 'is_bill', 'case_code',
									'case_fee', 'bill_id', 'bill_code',
									'bill_charge', 'username', 'date', 'remark'],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'post'
								},
								url : 'judicial/bill/queryAllBill.do',
								params : {
									limit : 10
								},
								reader : {
									type : 'json',
									root : 'data',
									totalProperty : 'total'
								}
							},
							listeners : {
								'beforeload' : function(ds, operation, opt) {
									Ext.apply(me.store.proxy.params, {
												case_code : casecode.getValue(),
												bill_code : billcode.getValue(),
												bill_per : billper.getValue(),
												starttime : dateformat(Ext
														.getCmp('starttime_bill')
														.getValue()),
												endtime : dateformat(Ext
														.getCmp('endtime_bill')
														.getValue())
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
							dataIndex : 'case_id',
							hidden : true
						}, {
							dataIndex : 'is_bill',
							hidden : true
						}, {
							header : '发票编号',
							dataIndex : 'bill_code',
							flex : 2
						}, {
							header : '案例编号',
							dataIndex : 'case_code',
							flex : 2
						}, {
							header : '登记缴费金额',
							dataIndex : 'case_fee',
							flex : 1
						}, {
							dataIndex : 'bill_id',
							hidden : true
						}, {
							header : '开票金额',
							dataIndex : 'bill_charge',
							flex : 1
						}, {
							header : '开票人',
							dataIndex : 'username',
							flex : 1
						}, {
							header : '开票时间',
							dataIndex : 'date',
							flex : 1
						}, {
							header : '备注',
							dataIndex : 'remark',
							flex : 2
						}];

				me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [billcode, casecode, billper, {
						xtype : 'datefield',
						name : 'starttime',
						id : 'starttime_bill',
						width : 175,
						fieldLabel : '开票时间 从',
						labelWidth : 70,
						labelAlign : 'right',
						emptyText : '请选择日期',
						format : 'Y-m-d',
						maxValue : new Date(),
						listeners : {
							'select' : function() {
								var start = Ext.getCmp('starttime_bill')
										.getValue();
								Ext.getCmp('endtime_bill').setMinValue(start);
								var endDate = Ext.getCmp('endtime_bill')
										.getValue();
								if (start > endDate) {
									Ext.getCmp('endtime_bill').setValue(start);
								}
							}
						}
					}, {
						xtype : 'datefield',
						id : 'endtime_bill',
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
								var start = Ext.getCmp('starttime_bill')
										.getValue();
								var endDate = Ext.getCmp('endtime_bill')
										.getValue();
								if (start > endDate) {
									Ext.getCmp('starttime_bill')
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
								text : '新增发票',
								iconCls : 'Add',
								handler : me.onInsert
							}, {
								text : '删除',
								iconCls : 'Delete',
								handler : me.onDelete
							}]
				}];

				me.callParent(arguments);
			},
			onInsert : function() {
				var me = this.up("gridpanel");
				var form = Ext.create("Rds.judicial.form.JudicialBillForm", {
							region : "center",
							grid : me
						});
				var win = Ext.create("Ext.window.Window", {
							title : '开票',
							width : 400,
							iconCls : 'Add',
							height : 400,
							layout : 'border',
							modal : true,
							items : [form]
						});
				win.show();
			},
			onDelete : function() {
				var me = this.up("gridpanel");
				var selections = me.getView().getSelectionModel()
						.getSelection();
				if (selections.length < 1) {
					Ext.Msg.alert("提示", "请选择需要修改的记录!");
					return;
				};
				var values = {
					bill_id : selections[0].get("bill_id"),
					case_id : selections[0].get("case_id")
				};
				Ext.MessageBox.confirm('提示', '确定删除此发票吗', function(id) {
							if (id == 'yes') {
								Ext.Ajax.request({
											url : "judicial/bill/delete.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : values,
											success : function(response,
													options) {
												response = Ext.JSON
														.decode(response.responseText);
												if (response.result == true) {
													Ext.MessageBox.alert(
															"提示信息",
															response.message);
													me.getStore().load();
												} else {
													Ext.MessageBox.alert(
															"错误信息",
															response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示",
														"保存失败<br>请联系管理员!");
											}

										});
							}
						}, {
							xtype : 'panel',
							region : "center",
							border : false
						});

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