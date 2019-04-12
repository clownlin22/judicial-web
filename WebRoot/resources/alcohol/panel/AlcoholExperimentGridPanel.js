/**
 * 
 */
function isregpastdue() {
	Ext.Ajax.request({
				url : "alcohol/experiment/isregpastdue.do",
				method : "POST",
				headers : {
					'Content-Type' : 'application/json'
				},
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					if (response.success == 0) {
						return true;
					} else if (response.success == 1) {
						Ext.MessageBox.confirm('提示', response.result, function(
										id) {
									if (id == 'yes')
										return false;
									else
										return true;
								});

					} else if (response.success == 2) {
						Ext.Msg.alert("提示", response.result);
						return false;
					}
				},
				failure : function() {
					return false;
				}

			});
};
Ext.define("Rds.alcohol.panel.AlcoholExperimentGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var case_code = Ext.create('Ext.form.field.Text', {
					name : 'case_code',
					labelWidth : 70,
					width : 200,
					fieldLabel : '案例条形码'
				});
		var sample_name = Ext.create('Ext.form.field.Text', {
					name : 'sample_name',
					labelWidth : 60,
					width : 200,
					fieldLabel : '当事人'
				});
		var state = Ext.create('Ext.form.field.ComboBox', {

					fieldLabel : '案例状态',
					width : 155,
					labelWidth : 60,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					labelAlign : 'right',
					valueField : 'Code',
					value : 2,
					store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['未实验', 2], ['已实验', 3]]
							})
				});

		me.store = Ext.create('Ext.data.Store', {
					fields : ['case_id', 'case_code', 'sample_name',
							'sample_sex', 'id_number'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'alcohol/experiment/querycaseinfo.do',
						params : {
							limit : 25
						},
						reader : {
							type : 'json',
							root : 'items',
							totalProperty : 'count'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							Ext.apply(me.store.proxy.params, {
										case_code : case_code.getValue(),
										state : state.getValue(),
										sample_name : sample_name.getValue()
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
		me.columns = [{
					dataIndex : 'case_id',
					hidden : true
				}, {
					header : '案例编号',
					dataIndex : 'case_code',
					flex : 2
				}, {
					header : '当事人',
					dataIndex : 'sample_name',
					flex : 1
				}, {
					header : '身份证号',
					dataIndex : 'id_number',
					flex : 2
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [case_code, sample_name, state, "-", {
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				}, {
					xtype : 'toolbar',
					dock : 'top',
					items : [{
								text : '校准曲线计算',
								iconCls : 'Textletteromega',
								handler : me.onRegression
							}, {
								text : '新增实验',
								iconCls : 'Add',
								handler : me.isregpastdue
							}, {
								text : '查看实验详情',
								iconCls : 'Find',
								handler : me.onQueryExper
							}, {
								text : '作废实验信息',
								iconCls : 'Delete',
								handler : me.onDelete
							}]
				}];

		me.callParent(arguments);
	},
	onDelete : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择实验案例!");
			return;
		}
		var values = Ext.JSON.encode(selections[0].getData());
		Ext.Msg.show({
					title : '提示',
					msg : '确定作废案例实验结果？',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							Ext.Ajax.request({
										url : "alcohol/experiment/deleteExper.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : values,
										success : function(response, options) {
											response = Ext.JSON
													.decode(response.responseText);
											if (response.success == true) {
												Ext.Msg.alert("提示",
														response.result);
												me.getStore().load({
															params : {
																start : 0
															}
														});
												return;
											} else {
												Ext.Msg.alert("提示",
														response.result);
												return;
											}
										},
										failure : function() {
											return false;
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
	onQueryExper : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择实验案例!");
			return;
		}
		var case_id = selections[0].get('case_id');
		var experDetailStore = Ext.create('Ext.data.Store', {
					fields : ['case_id', 'exper_id', 'exper_code', 'reg',
							'exper_time', 'result', 'alcohol', 'butanol',
							'exper_isdelete'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'alcohol/experiment/queryExperDetail.do',
						params : {},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'total'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							Ext.apply(experDetail.store.proxy.params, {
										case_id : case_id
									});
						}
					}
				});
		var experDetail = Ext.create('Ext.grid.Panel', {
					region : 'center',
					store : experDetailStore,
					columns : [{
								dataIndex : 'exper_id',
								hidden : true
							}, {
								dataIndex : 'case_id',
								hidden : true
							}, {
								text : '实验编号',
								dataIndex : 'exper_code',
								width : '15%',
								menuDisabled : true
							}, {
								text : '校验曲线',
								dataIndex : 'reg',
								width : '25%',
								menuDisabled : true
							}, {
								text : '实验时间',
								dataIndex : 'exper_time',
								menuDisabled : true,
								width : '20%'
							}, {
								text : '实验结果',
								dataIndex : 'result',
								menuDisabled : true,
								width : '10%'
							}, {
								text : '酒精',
								dataIndex : 'alcohol',
								width : '10%',
								menuDisabled : true
							}, {
								text : '叔丁醇',
								dataIndex : 'butanol',
								width : '10%',
								menuDisabled : true
							}, {
								text : '实验有效性',
								dataIndex : 'exper_isdelete',
								width : '10%',
								menuDisabled : true,
								renderer : function(value) {
									switch (value) {
										case 1 :
											return "<span style='color:red'>无效</span>";
											break;
										case 0 :
											return "有效";
											break;
										default :
											break;
									}
								}
							}],
					listeners : {
						'afterrender' : {
							fn : function() {
								experDetail.store.load();
							}
						}
					}
				});
		var form = Ext.create("Rds.alcohol.form.AlcoholExperDetailForm", {
					region : "north",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '实验详情',
					width : 800,
					iconCls : 'Add',
					height : 600,
					layout : 'border',
					modal : true,
					items : [form, experDetail]
				});
		form.loadRecord(selections[0]);
		win.show();

	},
	isregpastdue : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择实验案例!");
			return;
		}
		var values = Ext.JSON.encode(selections[0].getData());
		Ext.Ajax.request({
			url : "alcohol/experiment/isregpastdue.do",
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : values,
			success : function(response, options) {
				response = Ext.JSON.decode(response.responseText);
				if (response.success == 0) {
					var form = Ext.create(
							"Rds.alcohol.form.AlcoholExperimentForm", {
								region : "center",
								grid : me
							});
					var win = Ext.create("Ext.window.Window", {
								title : '新增实验',
								width : 500,
								iconCls : 'Add',
								height : 500,
								layout : 'border',
								modal : true,
								items : [form]
							});
					form.loadRecord(selections[0]);
					win.show();
				} else if (response.success == 1) {
					Ext.MessageBox.confirm('提示', response.result, function(id) {
								if (id == 'yes')
									return;
								else
									var selections = me.getView()
											.getSelectionModel().getSelection();
								if (selections.length < 1) {
									Ext.Msg.alert("提示", "请选择实验案例!");
									return;
								}
								var form = Ext
										.create(
												"Rds.alcohol.form.AlcoholExperimentForm",
												{
													region : "center",
													grid : me
												});
								var win = Ext.create("Ext.window.Window", {
											title : '新增实验',
											width : 500,
											iconCls : 'Add',
											height : 500,
											layout : 'border',
											modal : true,
											items : [form]
										});
								form.loadRecord(selections[0]);
								win.show();
							});

				} else if (response.success == 2) {
					Ext.Msg.alert("提示", response.result);
					return;
				}
			},
			failure : function() {
				return false;
			}

		});
	},
	onRegression : function() {
		var me = this.up("gridpanel");

		var form = Ext.create("Rds.alcohol.form.AlcoholRegressionForm", {
					region : "center",
					autoScroll : true,
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '生成校验曲线',
					width : 700,
					iconCls : 'Pageedit',
					height : 450,
					layout : 'border',
					modal : true,
					items : [form]
				});
		win.show();
	},

	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().load({
					params : {
						start : 0
					}
				});

	},
	listeners : {
		'afterrender' : function() {
			this.store.load();
		}

	}
});