/**
 * @author wind 无创医院归属地配置new
 */
Ext.define("Rds.bacera.panel.BaceraInvasiveOwnpersonToEmailsGrid", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var ownpersonname = Ext.create('Ext.form.field.Text', {
					name : 'ownpersonname',
					labelWidth : 80,
					width : 200,
					fieldLabel : '市场归属人'
				});

		me.store = Ext.create('Ext.data.Store', {
					fields : ['id', 'ownperson', 'ownpersonname', 'toEmails','remark'],
		            start:0,
					limit:15,
					pageSize:15,
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'bacera/invasivePre/queryallpageOwnpersonToEmails.do',
						params : {},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'total'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							me.getSelectionModel().clearSelections();
							Ext.apply(me.store.proxy.params, {
								ownpersonname : trim(ownpersonname.getValue()),
									});
						}
					}
				});

		me.selModel = Ext.create('Ext.selection.CheckboxModel', {
//					mode : 'SINGLE'
				});

		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : me.pageSize,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
			text : '市场归属人',
			dataIndex : 'ownpersonname',
			width : 200,
			menuDisabled : true
		}, {
					text : '邮箱地址',
					dataIndex : 'toEmails',
					width : 200,
					menuDisabled : true
				},{
					text : '备注',
					dataIndex : 'remark',
					width : 200,
					menuDisabled : true
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [ ownpersonname,{
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				}, {
					xtype : 'toolbar',
					dock : 'top',
					items : [{
						        text : '新增',
								iconCls : 'Add',
								handler : me.onInsert
							},{
								text : '修改',
								iconCls : 'Pageedit',
								handler : me.onUpdate
							}, {
								text : '删除',
								iconCls : 'Delete',
								handler : me.onDelete
							}]
				}];
		me.store.load();
		me.callParent(arguments);
	},
	onInsert : function() {
		var me = this.up("gridpanel");
		ownpersonTemp = '';
		var form = Ext.create("Rds.bacera.form.BaceraInvasiveOwnpersonToEmailsForm", {
					region : "center",
					autoScroll : true,
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '归属人邮箱配置——新增',
					width : 400,
					iconCls : 'Add',
					height : 350,
					modal:true,
					layout : 'border',
					items : [form]
				});
		win.show();
	},
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
			return;
		}
		ownpersonTemp = selections[0].get("ownperson");
		var form = Ext.create("Rds.bacera.form.BaceraInvasiveOwnpersonToEmailsForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '归属人邮箱配置——修改',
					width : 400,
					iconCls : 'Add',
					height : 350,
					modal:true,
					layout:'border',
					items : [form]
				});
		win.show();
		form.loadRecord(selections[0]);
	},
	onDelete : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要删除的记录!");
			return;
		}
		var values = {
			id : selections[0].get("id")
		};
		Ext.Msg.show({
					title : '提示',
					msg : '确定删除吗？',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							Ext.Ajax.request({
										url : "bacera/invasivePre/deleteOwnpersonToEmails.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : values,
										success : function(response, options) {
											response = Ext.JSON
													.decode(response.responseText);
											if (response.result == true) {
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
		me.store.currentPage = 1;
		me.getStore().load();
	}
});