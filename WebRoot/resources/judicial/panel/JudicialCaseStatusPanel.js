

Ext.define("Rds.judicial.panel.JudicialCaseStatusPanel", {
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
					fieldLabel : '案例编号'
				});
		var username = Ext.create('Ext.form.field.Text', {
					name : 'username',
					labelWidth : 60,
					width : 200,
					fieldLabel : '案例归属人'
				});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['case_id', 'case_code', 'status',
							'statusmessage', 'date', 'username', 'areaname'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/caseStatus/queryAll.do',
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
										username : username.getValue()
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
					text : '案例编号',
					dataIndex : 'case_code',
					width : '10%',
					menuDisabled : true
				}, {
					text : '案例归属地',
					dataIndex : 'areaname',
					width : '15%',
					menuDisabled : true
				}, {
					text : '案例归属人',
					dataIndex : 'username',
					width : '10%',
					menuDisabled : true
				}, {
					text : '状态',
					dataIndex : 'status',
					width : '15%',
					menuDisabled : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (value == 0) {
							return "正常"
						} else {
							return "<div style=\"color: red;\">异常</div>"
						}
					}
				}, {
					text : '异常原因',
					dataIndex : 'statusmessage',
					width : '40%',
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
								text : '添加',
								iconCls : 'Add',
								handler : me.onInsert
							}, {
								text : '修改',
								iconCls : 'Pageedit',
								handler : me.onUpdate
							}, {
								text : '异常任务完成',
								iconCls : 'Pageedit',
								handler : me.onDelete
							}]
				}];
		me.store.load();
		me.callParent(arguments);
	},
	onInsert : function() {
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.judicial.form.JudicialCaseStatusForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '案例状态—新增',
					width : 400,
					iconCls : 'Add',
					height : 400,
					layout : 'border',
					items : [form]
				});
		win.show();
	},
	onDelete : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
			return;
		};
		Ext.Msg.confirm('提示', '确定要完成异常任务吗？', function(id) {
					if (id == 'no') {
						return;
					} else {
						var values = {
							case_id : selections[0].get("case_id")
						};
						Ext.Ajax.request({
									url : "judicial/caseStatus/setNormal.do",
									method : "POST",
									headers : {
										'Content-Type' : 'application/json'
									},
									jsonData : values,
									success : function(response, options) {
										response = Ext.JSON
												.decode(response.responseText);
										if (response.success == true) {
											Ext.MessageBox.alert("提示信息",
													response.message);
											me.getStore().load();
										} else {
											Ext.MessageBox.alert("错误信息",
													response.message);
										}
									},
									failure : function() {
										Ext.Msg.alert("提示", "保存失败</br>请联系管理员!");
									}
								});
					}
				});
	},
	onUpdate : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		};
		if (selections[0].get("status") == 0) {
			Ext.Msg.alert("提示", "案例正常，不能修改");
			return;
		}
		var form = Ext.create("Rds.judicial.form.JudicialCaseStatusForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '银行账户——修改',
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