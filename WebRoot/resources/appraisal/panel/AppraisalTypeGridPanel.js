/**
 * 鉴定类型
 * 
 * @author yxb
 */
Ext.define("Rds.appraisal.panel.AppraisalTypeGridPanel", {
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
			fieldLabel : '鉴定类型',
			emptyText : '鉴定类型名称或说明',
		});
		me.store = Ext.create('Ext.data.Store', {
			fields : [ 'type_id', 'appendix_desc', 'standard_name',
					'appendix_status', 'standard_desc', 'create_time' ],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'appraisal/type/queryAllPage.do',
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
			text : '鉴定类型名称',
			dataIndex : 'standard_name',
			flex: .5,
			menuDisabled : true
		}, {
			text : '鉴定类型说明',
			dataIndex : 'standard_desc',
			flex: 2,
			menuDisabled : true
		}, {
			text : '有无附录',
			dataIndex : 'appendix_status',
			flex: .3,
			menuDisabled : true,
			renderer : function(value) {
				switch (value) {
				case '0':
					return "无";
					break;
				case '1':
					return "有";
					break;
				default:
					return "";
				}
			}
		}, {
			text : '创建时间',
			dataIndex : 'create_time',
			flex: .5,
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
		var form = Ext.create("Rds.appraisal.panel.AppraisalTypeFormPanel", {
			region : "center",
			grid : me
		});
		var win = Ext.create("Ext.window.Window", {
			title : '鉴定类型—新增',
			width : 600,
			iconCls : 'Add',
			height : 450,
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
		}
		;
		var values = {
			type_id : selections[0].get("type_id") 
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function(btn) {
			if ("yes" == btn) {
				Ext.Ajax.request({
					url : "appraisal/type/delete.do",
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
		var form = Ext.create("Rds.appraisal.panel.AppraisalTypeFormPanel", {
			region : "center",
			grid : me
		});
		var win = Ext.create("Ext.window.Window", {
			title : '鉴定类型—修改',
			width : 600,
			iconCls : 'Pageedit',
			height : 450,
			layout : 'border',
			items : [ form ]
		});
		win.show();
		form.loadRecord(selections[0]);
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();

	}
});