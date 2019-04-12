/**
 * @author fushaoming
 * @description 归档查看
 * @date 20150427
 */
Ext.define("Rds.alcohol.panel.AlcoholArchiveReadPanel", {
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
					labelWidth : 70,
					width : 200,
					regex:/^\w*$/,
					regexText:'请输入英文或数字',
					fieldLabel : '案例条形码'
				});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['archive_id', 'archive_code', 'case_code',
							'archive_address', 'archive_date', 'archive_per','archive_pername'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'alcohol/archive/getArchiveInfo.do',
						params : {},
						reader : {
							type : 'json',
							root : 'items',
							totalProperty : 'total'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							Ext.apply(me.store.proxy.params, {
								case_code : search.getValue()
									});
						}
					}
				});

		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : me.pageSize,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
					dataIndex : 'archive_id',
					hidden : true
				}, {
					text : '归档编号',
					dataIndex : 'archive_code',
					width : '25%',
					menuDisabled : true,
					flex : 2
				}, {
					text : '案例条形码',
					dataIndex : 'case_code',
					width : '25%',
					menuDisabled : true,
					flex : 2
				}, {
					text : '归档地址',
					dataIndex : 'archive_address',
					width : '20%',
					menuDisabled : true,
					flex : 2
				}, {
					text : '归档日期',
					dataIndex : 'archive_date',
					width : '10%',
					menuDisabled : true,
					flex : 1
				}, {
					text : '归档人',
					dataIndex : 'archive_pername',
					width : '10%',
					menuDisabled : true,
					flex : 1
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
								text : '添加阅读',
								iconCls : 'Add',
								handler : me.onInsert
							}, '-', {
								text : '查看阅读',
								iconCls : 'Bookopen',
								handler : me.onRead
							}]
				}];
		
		me.store.load();
		me.callParent(arguments);
	},
	onInsert : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要添加的记录!");
			return;
		}
		var form = Ext.create("Ext.form.Panel", {
					region : "center",
					bodyPadding : 10,
					defaults : {
						anchor : '100%',
						labelWidth : 70
					},
					items : [{
								xtype : 'textfield',
								name : 'archive_id',
								hidden : true
							}, {
								xtype : "textfield",
								fieldLabel : '阅读人',
								allowBlank : false,
								maxLength:50,
								name : 'read_per'
							}, {
								xtype : "datefield",
								fieldLabel : '阅读时间',
								allowBlank : false,
								name : 'read_date',
								format : 'Y-m-d',
								editable:false,
								maxValue : new Date()

							}],
					buttons : [{
								text : '保存',
								iconCls : 'Disk',
								handler : me.onSave
							}, {
								text : '取消',
								iconCls : 'Cancel',
								handler : me.onCancel
							}]
				});
		var win = Ext.create("Ext.window.Window", {
					title : '添加阅读记录',
					width : 500,
					iconCls : 'Add',
					height : 500,
					layout : 'border',
					modal : true,
					items : [form]
				});
		form.loadRecord(selections[0]);
		win.show();
	},
	onRead : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		}
		var myGrid = Ext.create('Ext.grid.Panel', {
					region:'center',
					store : Ext.create('Ext.data.Store', {
								fields : ['id', 'archive_id', 'read_per',
										'read_date'],
								proxy : {
									type : 'jsonajax',
									actionMethods : {
										read : 'post'
									},
									url : 'alcohol/archive/getReadInfo.do',
									params : {},
									reader : {
										type : 'json',
										root : 'data',
										totalProperty : 'total'
									}
								},
								listeners : {
									'beforeload' : function(ds, operation, opt) {
										Ext.apply(myGrid.store.proxy.params, {
													archive_id :selections[0].get('archive_id')
												});
									}
								}
							}),
					columns : [{
								dataIndex : 'id',
								hidden : true
							}, {
								dataIndex : 'archive_id',
								hidden : true
							}, {
								text : '查看人',
								dataIndex : 'read_per',
								width : '50%',
								menuDisabled : true,
								flex : 2
							}, {
								text : '查看时间',
								dataIndex : 'read_date',
								width : '50%',
								editable:false,
								menuDisabled : true,
								flex : 2
							}],
					buttons : [{
								text : '确定',
								iconCls : 'Accept',
								handler : function() {
									this.up("window").close();
								}
							}],
					listeners : {
						'afterrender' : {
							fn : function() {
								myGrid.store.load();
							}
						}
					}
				});
		var win = Ext.create("Ext.window.Window", {
					title : '阅读记录',
					width : 500,
					iconCls : 'Add',
					height : 500,
					layout : 'border',
					modal : true,
					items : [myGrid]
				});
		win.show();
	},
	onSave : function() {
		var me = this;
		var myWindow = me.up('window');
		var form = me.up('form').getForm();
		if (!form.isValid()) {
			Ext.MessageBox.alert("提示信息", "请填写完整表单信息!");
			return;
		}
		Ext.Ajax.request({
					url : "alcohol/archive/addReadInfo.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : form.getValues(),
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response == true) {
							myWindow.close();
							Ext.MessageBox.alert("提示信息", "添加成功");
						} else {
							Ext.MessageBox.alert("错误信息", "添加失败");
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
					}

				});

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage=1;
		me.getStore().load();
	}
});