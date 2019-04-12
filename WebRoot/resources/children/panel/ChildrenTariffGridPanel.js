/**
 * 套餐管理
 */
Ext.define("Rds.children.panel.ChildrenTariffGridPanel", {
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
					fields : ['tariff_id', 'tariff_name', "tariff_price",
							"tariff_state", "tariff_remark"],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'children/tariff/gettariffinfo.do',
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
					dataIndex : 'tariff_id',
					hidden : true
				}, {
					text : '套餐名称',
					dataIndex : 'tariff_name',
					menuDisabled : true,
					width : 400
				}, {
					text : '套餐价格',
					dataIndex : 'tariff_price',
					menuDisabled : true,
					width : 400
				}, {
					text : '套餐状态',
					dataIndex : 'tariff_state',
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
					text : '套餐说明',
					dataIndex : 'tariff_remark',
					width : 400,
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
								text : '新建套餐',
								iconCls : 'Add',
								handler : me.onInsert
							}
							// , {
							// text : '修改套餐',
							// iconCls : 'Pageedit',
							// handler : me.onUpdate
							//							}
							, {
								text : '作废套餐',
								iconCls : 'Delete',
								handler : me.onDelete
							}]
				}];
		me.callParent(arguments);
	},
	
	onInsert : function() {
		var me = this.up("gridpanel");
		var form = Ext.create("Ext.form.Panel", {
			region : "center",
			autoScroll : true,
			grid : me,
			labelAlign : "right",
			bodyPadding : 10,
			defaults : {
				anchor : '100%',
				labelWidth : 100
			},
			items : [{
						xtype : "textfield",
						fieldLabel : '套餐名称',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'tariff_name',
						maxLength : 20
					}, {
						xtype : "textfield",
						fieldLabel : '套餐价格',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						regex : /^\d+$/,
						regexText : '请输入正确金额',
						name : 'tariff_price',
						maxLength : 20
					}, {
						xtype : "textarea",
						fieldLabel : '套餐说明',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'tariff_remark'
						// maxLength : 20
				}],
			buttons : [{
						text : '重置',
						handler : function() {
							this.up('form').getForm().reset();
						}
					}, {
						text : '提交',
						formBind : true,
						disabled : true,
						handler : function() {
							var me = this.up("form");
							var form = me.getForm();
							if (!form.isValid()) {
								Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
								return;
							}
							var values = form.getValues();

							Ext.Ajax.request({
										url : "children/tariff/save.do",
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
												me.grid.getStore().load();
												me.up("window").close();
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
						}
					}]

		});
		var win = Ext.create("Ext.window.Window", {
					title : '试剂—新增',
					width : 400,
					iconCls : 'Add',
					height : 300,
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
		if (selections[0].get("tariff_state") == 1) {
			Ext.Msg.alert("提示", "该套餐已经作废！");
			return;
		}
		var values = {
			tariff_id : selections[0].get("tariff_id")
		};
		Ext.Msg.show({
					title : '提示',
					msg : '确定作废该套餐？',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							Ext.Ajax.request({
										url : "children/tariff/delete.do",
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
