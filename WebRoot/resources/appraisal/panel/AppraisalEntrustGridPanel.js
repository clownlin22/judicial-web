/**
 * 委托人
 * 
 * @author yxb
 */
Ext.define("Rds.appraisal.panel.AppraisalEntrustGridPanel", {
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
			fieldLabel : '委托人',
			emptyText : '委托人名称',
		});
		me.store = Ext.create('Ext.data.Store', {
			fields : [ 'id', 'name' ],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'appraisal/entrust/queryAllPage.do',
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
		me.columns = [ Ext.create("Ext.grid.RowNumberer", {
			text : '序号',
			width : '3%',
			menuDisabled : true
		}), {
			text : '委托人名称',
			dataIndex : 'name',
			flex: 1,
			menuDisabled : true
		} ];

		me.dockedItems = [ {
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [ search, {
				text : '查询',
				iconCls : 'Find',
				handler : me.onSearch
			} ]
		}, {
			xtype : 'toolbar',
			dock : 'top',
			items : [ {
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
			} ]
		} ];
		me.store.load();
		me.callParent(arguments);
	},
	onInsert : function() {
		var me = this.up("gridpanel");
//		var tabs = this.up("tabpanel[region=center]");
//		var tab = null;
//		tab = tabs.add(Ext.create('Rds.appraisal.panel.AppraisalTypeGridPanel',
//				{
//					id : '123',
//					title : '12344',
//					closable : true,
//					listeners : {
//						"beforedestroy" : function() {
//							me.getStore().load();
//						}
//					}
//				}));
//		tab.show();
//		return;

		var form = Ext.create("Rds.appraisal.panel.AppraisalEntrustFormPanel",
				{
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
			title : '委托人—新增',
			width : 300,
			iconCls : 'Add',
			height : 150,
			layout : 'border',
			items : [ form ]
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
		var values = {
			id : selections[0].get("id")
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function(btn) {
			if ("yes" == btn) {
				Ext.Ajax.request({
					url : "appraisal/entrust/delete.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : values,
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.result == true) {
							Ext.MessageBox.alert("提示信息", response.message);
							me.getStore().load();
						} else {
							Ext.MessageBox.alert("错误信息", response.message);
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
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
		}
		;
		var form = Ext.create("Rds.appraisal.panel.AppraisalEntrustFormPanel",
				{
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
			title : '委托人—修改',
			width : 300,
			iconCls : 'Pageedit',
			height : 150,
			layout : 'border',
			items : [ form ]
		});
		win.show();
		console.log(selections[0]);
		form.loadRecord(selections[0]);
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();

	}
});