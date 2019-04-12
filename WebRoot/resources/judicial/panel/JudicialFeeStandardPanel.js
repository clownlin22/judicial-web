/**
 * @author fushaoming 收费标准
 */

Ext.define("Rds.judicial.panel.JudicialFeeStandardPanel", {
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
		var feetype = new Ext.form.field.ComboBox({
					fieldLabel : '销售类型',
					// columnWidth : .45,
					labelWidth :80,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					labelAlign : 'left',
					valueField : 'Code',
					store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['司法鉴定', 0], ['医学鉴定', 1]]
							}),
					value : 0,
					mode : 'local',
					// typeAhead: true,
					name : 'feetype'
				});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['type_id', 'userid', 'username', 'area_id',
							'area_code', 'areaname', 'keyid', 'keyvalue',
							'equation', 'discountrate', 'feetype'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/feestandard/querytype.do',
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
										feetype : feetype.getValue()
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
		me.columns = [ {
					text : '销售经理',
					dataIndex : 'username',
					width : '10%',
					menuDisabled : true
				}, {
					text : '销售地区',
					dataIndex : 'areaname',
					width : '15%',
					menuDisabled : true
				}, {
					text : '销售类型',
					dataIndex : 'feetype',
					width : '15%',
					menuDisabled : true,
					renderer : function(value) {
						if (value == 0)
							return '司法鉴定';
						else
							return '医学鉴定';
					}
				}, {
					text : '收费公式',
					dataIndex : 'equation',
					width : '50%',
					menuDisabled : true
				}, {
					text : '优惠比例',
					dataIndex : 'discountrate',
					width : '15%',
					menuDisabled : true
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [search,feetype, {
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
		ownpersonTemp="";
		var form = Ext.create("Rds.judicial.form.JudicialFeeTypeForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '价格标准——新增',
					width : 400,
					iconCls : 'Add',
					height : 400,
					layout : 'border',
					items : [form]
				});
		win.show();
	},
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
		.getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		}
		ownpersonTemp = selections[0].get("area_id") ;
		var form = Ext.create("Rds.judicial.form.JudicialFeeTypeUpdateForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '价格标准——修改',
					width : 400,
					iconCls : 'Add',
					height : 400,
					layout : 'border',
					items : [form]
				});
		form.loadRecord(selections[0]);
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
			id : selections[0].get("type_id")
		};
		Ext.Msg.show({
					title : '提示',
					msg : '确定删除吗？',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							Ext.Ajax.request({
										url : "judicial/feestandard/delete.do",
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
		me.getStore().load({
					params : {
						start : 0
					}
				});
	}
});