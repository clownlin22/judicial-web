Ext
		.define(
				"Rds.judicial.panel.JudicialPrintManageGridPanel",
				{
					extend : "Ext.grid.Panel",
					loadMask : true,
					viewConfig : {
						trackOver : false,
						stripeRows : false
					},
					region:'center',
					pageSize : 25,
					initComponent : function() {
						var me = this;
						me.store = Ext
								.create(
										'Ext.data.Store',
										{
											fields : [ 'print_code','print_name'
													],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/printmanage/getPrintInfo.do',
												params : {
													start : 0,
													limit : 25
												},
												reader : {
													type : 'json',
													root : 'items',
													totalProperty : 'count'
												}
											},
											listeners : {
												'beforeload' : function(ds,
														operation, opt) {
													Ext
													.apply(
															me.store.proxy.extraParams,{								
																print_name : trim(Ext
																				.getCmp(
																						'model_print_manage_query')
																				.getValue())
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
						me.columns = [
								{
									text : '模板编号',
									dataIndex : 'print_code',
									width : 300,
									menuDisabled : true
								},
								{
									text : '模板名称',
									dataIndex : 'print_name',
									menuDisabled : true,
									width : 300
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [{
												xtype : 'textfield',
												fieldLabel : '模板名称',
												width : 200,
												labelWidth : 80,
												id : 'model_print_manage_query',
												name : 'print_name'
											},{
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											} ]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
							 			text:'配置模板',
							 			iconCls:'Cog',
							 			handler:me.onAddPrint
							 		    }
									]
								} ];

						me.callParent(arguments);
					},
					onAddPrint : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要配置的模板!");
							return;
						};
						var form = Ext.create(
								"Rds.judicial.form.JudicialPrintManageForm",
								{
									region : "center"
								});
						form.loadRecord(selections[0]);
						var win = Ext.create("Ext.window.Window", {
							title : '模板配置',
							width : 760,
							iconCls : 'Cog',
							height : 550,
							layout : 'border',
							items : [ form ]
						});
						win.show();
					},
					onSearch : function() {
						var me = this.up("gridpanel");
						me.getStore().currentPage=1;
						me.getStore().load();
					},
					listeners : {
						'afterrender' : function() {
							this.store.load();
						}
					}
				});
