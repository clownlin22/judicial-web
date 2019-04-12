/**
 * 
 */
Ext.define("Rds.children.panel.ChildrenAgentiaGridPanel", {
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
					fields : ['agentia_id', 'agentia_name', "status", "remark"],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'children/agentia/getAgentiaInfo.do',
						params : {
							start : 0,
							limit : 25
						},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'count'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							Ext.apply(me.store.proxy.extraParams, {
									// xxx : xxx,
									});
						}
					}
				});

		me.viewConfig = {
			enableTextSelection : true
		};
		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : me.pageSize,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
					dataIndex : 'agentia_id',
					hidden : true
				}, {
					text : '试剂名称',
					dataIndex : 'agentia_name',
					menuDisabled : true,
					width : 300
				}, {
					text : '状态',
					dataIndex : 'status',
					width : 300,
					menuDisabled : true,
					renderer : function(value) {
						if (value == 0) {
							return '可用';
						} else if (value == 1) {
							return "<span style='color:red'>作废</span>";
						}
					}
				}, {
					text : '备注',
					dataIndex : 'remark',
					width : 300,
					menuDisabled : true
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [{
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				}, {
					xtype : 'toolbar',
					dock : 'top',
					items : [{
								text : '新建试剂',
								iconCls : 'Add',
								handler : me.onInsert
							}, {
								text : '查看点位名称',
								iconCls : 'Find',
								handler : me.onLocus
							}, {
								text : '作废试剂',
								iconCls : 'Delete',
								handler : me.onDelete
							}]
				}];
		me.callParent(arguments);
	},
	onLocus : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		}
		var agentiaStore = Ext.create('Ext.data.Store', {
					fields : ['agentia_id', 'agentia_name', "locus_name"],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'children/agentia/getlocusInfo.do',
						params : {
							agentia_id : selections[0].get("agentia_id")
						},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'count'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							Ext.apply(me.store.proxy.extraParams, {
										agentia_id : selections[0]
												.get("agentia_id")
									});
						}
					}
				});
		var grid = Ext.create("Ext.grid.Panel", {
					region : "center",
					store : agentiaStore,
					columns : [{
								dataIndex : 'agentia_id',
								hidden : true
							}, {
								text : '试剂名称',
								dataIndex : 'agentia_name',
								menuDisabled : true,
								width : 150
							}, {
								text : '试剂名称',
								dataIndex : 'locus_name',
								menuDisabled : true,
								width : 150
							}]

				});
		var win = Ext.create("Ext.window.Window", {
					title : '试剂—名称',
					width : 300,
					iconCls : 'Find',
					height : 600,
					layout : 'border',
					items : [grid]
				});
		grid.store.load();
		win.show();
	},
	onInsert : function() {
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.children.form.ChildrenAgentiaForm", {
					region : "center",
					autoScroll : true,
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '试剂—新增',
					width : 650,
					iconCls : 'Add',
					height : 500,
					layout : 'border',
					items : [form]
				});
		win.show();
	},
	onDelete : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要作废的记录!");
			return;
		}
		if (selections[0].get("status")==1){
			Ext.Msg.alert("提示", "该试剂已经作废！");
			return;
		}
		var values = {
			agentia_id : selections[0].get("agentia_id")
		};
		Ext.Msg.show({
					title : '提示',
					msg : '确定作废该试剂？',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							Ext.Ajax.request({
										url : "children/agentia/delete.do",
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
											Ext.Msg.alert("提示",
													"保存失败<br>请联系管理员!");
										}

									});
						} else {
							return;
						}
					},
					animateTarget : 'addAddressBtn',
					icon : Ext.window.MessageBox.INFO
				})

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
