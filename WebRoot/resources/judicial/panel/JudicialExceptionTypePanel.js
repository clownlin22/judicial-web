Ext.define("Rds.judicial.panel.JudicialExceptionTypePanel", {
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
					fields : ['type_id', 'type_desc', "is_delete"],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/exceptiontype/getType.do',
						params : {
							start : 0,
							limit : 25
						},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'total'
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
					dataIndex : 'type_id',
					hidden : true
				}, {
					text : '异常描述',
					dataIndex : 'type_desc',
					menuDisabled : true,
					width : 300
				}, {
					text : '是否作废',
					dataIndex : 'is_delete',
					width : 300,
					menuDisabled : true,
					renderer : function(value) {
						if (value == 0) {
							return '可用';
						} else if (value == 1) {
							return "<span style='color:red'>作废</span>";
						}
					}
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
								text : '新增',
								iconCls : 'Add',
								handler : me.onInsert
							}, {
								text : '修改',
								iconCls : 'Pageedit',
								handler : me.onUpdate
							}, {
								text : '作废',
								iconCls : 'Delete',
								handler : me.onDelete
							}]
				}];
		me.callParent(arguments);
	},
	/*
	 * onLocus : function() { var me = this.up("gridpanel"); var selections =
	 * me.getView().getSelectionModel().getSelection(); if (selections.length <
	 * 1) { Ext.Msg.alert("提示", "请选择需要修改的记录!"); return; } var agentiaStore =
	 * Ext.create('Ext.data.Store', { fields : ['agentia_id', 'agentia_name',
	 * "locus_name"], proxy : { type : 'jsonajax', actionMethods : { read :
	 * 'POST' }, url : 'children/agentia/getlocusInfo.do', params : { agentia_id :
	 * selections[0].get("agentia_id") }, reader : { type : 'json', root :
	 * 'data', totalProperty : 'count' } }, listeners : { 'beforeload' :
	 * function(ds, operation, opt) { Ext.apply(me.store.proxy.extraParams, {
	 * agentia_id : selections[0] .get("agentia_id") }); } } }); var grid =
	 * Ext.create("Ext.grid.Panel", { region : "center", store : agentiaStore,
	 * columns : [{ dataIndex : 'agentia_id', hidden : true }, { text : '试剂名称',
	 * dataIndex : 'agentia_name', menuDisabled : true, width : 150 }, { text :
	 * '试剂名称', dataIndex : 'locus_name', menuDisabled : true, width : 150 }]
	 * 
	 * }); var win = Ext.create("Ext.window.Window", { title : '试剂—名称', width :
	 * 300, iconCls : 'Find', height : 600, layout : 'border', items : [grid]
	 * }); grid.store.load(); win.show(); },
	 */
	onInsert : function() {
		var me = this.up("gridpanel");
		var form = Ext.create('Ext.form.Panel', {
			grid : me,
			bodyPadding : 5,
			width : 350,
			height : 200,
			layout : 'anchor',
			defaults : {
				anchor : '100%'
			},

			// The fields
			defaultType : 'textarea',
			items : [{
						fieldLabel : '异常描述',
						size : 500,
						name : 'type_desc',
						allowBlank : false
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
							var me = this;
							var myStore = me.up('form').grid.store;
							// console.info(myStore);
							var myWindow = me.up('window');
							var form = me.up('form').getForm();
							if (!form.isValid()) {
								Ext.MessageBox.alert("提示信息", "请正确填写完整表单信息！");
								return;
							}
							Ext.Ajax.request({
								url : "judicial/exceptiontype/save.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : form.getValues(),
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response == true) {
										myWindow.close();
										Ext.MessageBox.alert("提示信息", "新增成功");
										myStore.load();
									} else {
										Ext.MessageBox.alert("错误信息", "保存失败...");
									}
								},
								failure : function() {
									Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
								}

							});
						}

					}]
		});
		var win = Ext.create("Ext.window.Window", {
					title : '试剂—新增',
					width : 360,
					iconCls : 'Add',
					height : 200,
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
		if (selections[0].get("status") == 1) {
			Ext.Msg.alert("提示", "该异常描述已经作废！");
			return;
		}
		var values = {
			type_id : selections[0].get("type_id")
		};
		Ext.Msg.show({
					title : '提示',
					msg : '确定作废该描述？',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							Ext.Ajax.request({
										url : "judicial/exceptiontype/delete.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : values,
										success : function(response, options) {
											response = Ext.JSON
													.decode(response.responseText);
											if (response == true) {
												Ext.MessageBox.alert("提示信息",
														"作废成功");
												me.getStore().load();
											} else {
												Ext.MessageBox.alert("错误信息",
														"作废失败");
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
	onUpdate : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		};
		if(selections[0].get('is_delete')==1){
			Ext.Msg.alert("提示", "该异常描述已经作废，不能修改！");
			return;
		}
		var form = Ext.create('Ext.form.Panel', {
			grid : me,
			bodyPadding : 5,
			width : 350,
			height : 200,
			layout : 'anchor',
			defaults : {
				anchor : '100%'
			},
			defaultType : 'textarea',
			items : [{
						xtype : 'hiddenfield',
						name : 'type_id'
					}, {
						fieldLabel : '异常描述',
						size : 500,
						name : 'type_desc',
						allowBlank : false
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
							var me = this;
							var myStore = me.up('form').grid.store;
							var myWindow = me.up('window');
							var form = me.up('form').getForm();
							if (!form.isValid()) {
								Ext.MessageBox.alert("提示信息", "请正确填写完整表单信息！");
								return;
							}
							Ext.Ajax.request({
								url : "judicial/exceptiontype/update.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : form.getValues(),
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response == true) {
										myWindow.close();
										Ext.MessageBox.alert("提示信息", "新增成功");
										myStore.load();
									} else {
										Ext.MessageBox.alert("错误信息", "保存失败...");
									}
								},
								failure : function() {
									Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
								}

							});
						}

					}]
		});
		var win = Ext.create("Ext.window.Window", {
					title : '试剂—新增',
					width : 360,
					iconCls : 'Add',
					height : 200,
					layout : 'border',
					items : [form]
				});
		form.loadRecord(selections[0]);
		win.show();
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
