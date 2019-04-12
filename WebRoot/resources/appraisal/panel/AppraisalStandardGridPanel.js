/**
 * 鉴定标准
 * 
 * @author yxb
 */
Ext.define("Rds.appraisal.panel.AppraisalStandardGridPanel", {
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
			fieldLabel : '鉴定标准',
			emptyText : '标准内容或级数',
		});
		var typeStore = Ext.create('Ext.data.Store',{
    	    fields:['type_id','standard_name'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'appraisal/type/queryAll.do',
    	        params:{
    	        },
    	        render:{
    	            type:'json'
    	        },
    	        writer: {
    	            type: 'json'
    	       }
    	    }
    	});
		var typeSerach = new Ext.form.ComboBox({
			autoSelect : true,
			editable:false,
			fieldLabel:'鉴定类型',
	        name:'type_id',
	        id:'type_id',
	        labelWidth:60,
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
	        selectOnTab: true,
	        store: typeStore,
	        fieldStyle: me.fieldStyle,
	        displayField:'standard_name',
	        valueField:'type_id',
	        editable:true
		});
		me.store = Ext.create('Ext.data.Store', {
			fields : [ 'standard_id', 'content', 'series',
					'type_id', 'standard_name'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'appraisal/type/queryStandardAllPage.do',
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
						search : search.getValue(),
						type_id : typeSerach.getValue()
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
			text : '标准内容',
			dataIndex : 'content',
			flex: 2,
			menuDisabled : true
		}, {
			text : '级数',
			dataIndex : 'series',
			flex: .5,
			menuDisabled : true
		}];

		me.dockedItems = [ {
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [ search, typeSerach, {
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
		var form = Ext.create("Rds.appraisal.panel.AppraisalStandardFormPanel", {
			region : "center",
			grid : me
		});
		var win = Ext.create("Ext.window.Window", {
			title : '鉴定标准—新增',
			width : 400,
			iconCls : 'Add',
			height : 300,
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
				standard_id : selections[0].get("standard_id") 
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function(btn) {
			if ("yes" == btn) {
				Ext.Ajax.request({
					url : "appraisal/type/deleteStandard.do",
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
		var form = Ext.create("Rds.appraisal.panel.AppraisalStandardFormPanel", {
			region : "center",
			grid : me
		});
		var win = Ext.create("Ext.window.Window", {
			title : '鉴定标准—修改',
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