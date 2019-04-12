Ext.define("Rds.judicial.panel.JudicialFinanceVerifyPanel", {
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
							fields : ['verify_id', 'case_code', 'receiver_id',
									'username', 'areacode', 'areaname', 'date',
									'stand_sum', 'real_sum', 'return_sum',
									'status','type', 'remark'],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/finance/queryVerify.do',
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
							dataIndex : 'verify_id',
							hidden : true
						}, {
							dataIndex : 'userid',
							hidden : true
						}, {
							dataIndex : 'areacode',
							hidden : true
						}, {
							text : '案例编号',
							dataIndex : 'case_code',
							width : '10%',
							menuDisabled : true
						}, {
							text : '销售经理',
							dataIndex : 'username',
							width : '10%',
							menuDisabled : true
						}, {
							text : '所属地区',
							dataIndex : 'areaname',
							width : '15%',
							menuDisabled : true
						}, {
							text : '日期',
							dataIndex : 'date',
							width : '15%',
							menuDisabled : true
						}, {
							text : '标准金额',
							dataIndex : 'stand_sum',
							width : '5%',
							menuDisabled : true
						}, {
							text : '实收金额',
							dataIndex : 'real_sum',
							width : '5%',
							menuDisabled : true
						}, {
							text : '回款金额',
							dataIndex : 'return_sum',
							width : '5%',
							menuDisabled : true
						}, {
							text : '状态',
							dataIndex : 'status',
							width : '5%',
							menuDisabled : true,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (value == 0) {
									return "正常"
								} else {
									return "<div style=\"color: red;\">异常</div>"
								}
							}
						},{
							text : '类型',
							dataIndex : 'type',
							width : '10%',
							menuDisabled : true,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (value == 0) {
									return "正常"
								} else if (value==1){
									return "<div style=\"color: red;\">先出报告后付款</div>"
								}else{
									return "<div style=\"color: red;\">免单</div>"
								}
							}
						}, {
							text : '备注',
							dataIndex : 'remark',
							width : '30%',
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
									}]
						}];
				me.store.load();
				me.callParent(arguments);
			},

			onUpdate : function() {
				var me = this.up("gridpanel");
				var selections = me.getView().getSelectionModel()
						.getSelection();
				if (selections.length < 1) {
					Ext.Msg.alert("提示", "请选择需要修改的记录!");
					return;
				};
				if(selections[0].get("status")==0){
					Ext.Msg.alert("提示", "正常状态无法审核");
					return;
				}
				var form = Ext.create(
						"Rds.judicial.form.JudicialFinanceVerifyForm", {
							region : "center",
							grid : me
						});
				var win = Ext.create("Ext.window.Window", {
							title : '财务——修改',
							width : 400,
							iconCls : 'Pageedit',
							height : 400,
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