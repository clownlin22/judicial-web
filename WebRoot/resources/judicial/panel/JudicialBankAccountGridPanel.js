/**
 * @author fushaoming
 * @description 银行账号管理
 * @date 20150429
 */

Ext.define("Rds.judicial.panel.JudicialBankAccountGridPanel", {
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
							fieldLabel : '银行名称'
						});
				me.store = Ext.create('Ext.data.Store', {
							fields : ['id', 'bankname', 'bankaccount',
									'remark', 'companyid', 'companyname'],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/bank/queryallpage.do',
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
				me.columns = [Ext.create("Ext.grid.RowNumberer", {
									text : '序号',
									width : '4%',
									menuDisabled : true
								}), {
							dataIndex : 'id',
							hidden : true
						}, {
							dataIndex : 'companyid',
							hidden : true
						}, {
							text : '银行名称',
							dataIndex : 'bankname',
							width : '25%',
							menuDisabled : true
						}, {
							text : '银行账户',
							dataIndex : 'bankaccount',
							width : '20%',
							menuDisabled : true
						}, {
							text : '所属单位',
							dataIndex : 'companyname',
							width : '10%',
							menuDisabled : true
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
										text : '添加',
										iconCls : 'Add',
										handler : me.onInsert
									}, {
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
				var form = Ext.create("Rds.judicial.form.JudicialBankForm", {
							region : "center",
							grid : me
						});
				var win = Ext.create("Ext.window.Window", {
							title : '银行账户—新增',
							width : 400,
							iconCls : 'Add',

							height : 300,
							modal:true,
							layout : 'border',
							items : [form]
						});
				win.show();
			},
			onDelete : function() {
				var me = this.up("gridpanel");
				var selections = me.getView().getSelectionModel()
						.getSelection();
				if (selections.length < 1) {
					Ext.Msg.alert("提示", "请选择需要删除的记录!");
					return;
				};
				var values = {
					id : selections[0].get("id")
				};
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "judicial/bank/delete.do",
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
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}

						});
			},
			onUpdate : function() {
				var me = this.up("gridpanel");
				var selections = me.getView().getSelectionModel()
						.getSelection();
				if (selections.length < 1) {
					Ext.Msg.alert("提示", "请选择需要修改的记录!");
					return;
				};
				var form = Ext.create("Rds.judicial.form.JudicialBankForm", {
							region : "center",
							grid : me
						});
				var win = Ext.create("Ext.window.Window", {
							title : '银行账户——修改',
							width : 400,
							iconCls : 'Pageedit',
							modal:true,
							height : 300,

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