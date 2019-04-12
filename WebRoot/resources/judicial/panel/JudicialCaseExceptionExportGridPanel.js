Ext.define("Rds.judicial.panel.JudicialCaseExceptionExportGridPanel",
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
						
						var starttime=Ext.create('Ext.form.DateField', {
							name : 'starttime',
							id:'exceptionStarttime',
							width : 175,
							labelWidth : 70,
							fieldLabel : '异常时间 从',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value : Ext.Date.add(
									new Date(),
									Ext.Date.DAY,-7),
							listeners : {
								select : function() {
									var start = starttime
											.getValue();
									var end = endtime
											.getValue();
									endtime.setMinValue(
											start);
								}
							}
						});
						var endtime=Ext.create('Ext.form.DateField', {
							name : 'endtime',
							id:'exceptionEndtime',
							width : 135,
							labelWidth : 20,
							fieldLabel : '到',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value : Ext.Date.add(new Date(), Ext.Date.DAY),
							listeners : {
								select : function() {
									var start = starttime
											.getValue();
									var end = endtime
											.getValue();
									starttime.setMaxValue(
											end);
								}
							}
						});
						me.store = Ext
								.create(
										'Ext.data.Store',
										{
											fields : ['case_code','username','client','accept_time',
													'exception_desc','exception_time','agent'],
											start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/caseException/getExportException.do',
												params : {
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
																endtime : dateformat(endtime
																               .getValue()),	
																starttime : dateformat(starttime
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
									text : '案例条形码',
									dataIndex : 'case_code',
									menuDisabled : true,
									width : 120
								},
								{
									text : '归属人',
									dataIndex : 'username',
									menuDisabled : true,
									width : 150,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var agent= record.data["agent"];
										if (agent != ''&& agent!=null) {
											return value+"(代理："+agent+")";
										} else {
											return value;
										}
									}
								},
								{
									text : '委托人',
									dataIndex : 'client',
									menuDisabled : true,
									width : 120
								},
								{
									text : '受理时间',
									dataIndex : 'accept_time',
									menuDisabled : true,
									width : 120
								},
								{
									text : '异常登记时间',
									dataIndex : 'exception_time',
									menuDisabled : true,
									width : 150
								},
								{
									text : '异常描述',
									dataIndex : 'exception_desc',
									menuDisabled : true,
									width : 250
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '1px !important'
								    },
									items : [starttime,endtime
												, {
										text : '查询',
										iconCls : 'Find',
										handler : me.onSearch
									}  ]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '导出',
										iconCls : 'Application',
										handler : me.excetpionExport
									}]
								}  ];

						me.callParent(arguments);
					},
					excetpionExport:function(){
						window.location.href = "judicial/caseException/exportException.do?starttime="+dateformat(Ext.getCmp('exceptionStarttime').getValue())
						+"&endtime="+dateformat(Ext.getCmp('exceptionEndtime').getValue()) ;
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
