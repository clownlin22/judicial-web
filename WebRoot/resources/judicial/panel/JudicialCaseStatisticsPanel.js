var storeModel = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/casedaily/getCompany.do',
				params : {},
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
Ext.define("Rds.judicial.panel.JudicialCaseStatisticsPanel", {
	extend : "Ext.panel.Panel",
	layout : 'anchor',
	items : [Ext.create('Ext.grid.Panel', {
		anchor : '100% 30%',
		loadMask : true,
		title : "当日工作信息",
		viewConfig : {

	}	,
		dockedItems : [{
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [{

						columnWidth : .45,
						xtype : "combo",
						id : 'casedaily_companyid',
						fieldLabel : '所属公司',
						mode : 'local',
						labelWidth : 80,
						editable : false,
						labelAlign : 'right',
						valueField : "key",
						displayField : "value",
						name : 'companyid',
						store : storeModel

					}, {
						text : '查询',
						iconCls : 'Arrowrefresh',
						handler : function() {
							if (Ext.getCmp("casedaily_companyid").getValue() == ""
									|| Ext.getCmp("casedaily_companyid")
											.getValue() == null) {
								Ext.Msg.alert("消息", "请选择公司");
								return;
							}
							var gridstore = this.up("grid").getStore();
							gridstore.load();
							Ext.getCmp("statistics_exception").getStore()
									.load();
						}
					}, {
						text : '查看历史异常案例数量统计',
						iconCls : 'Find',
						handler : function() {
							var panel = Ext.create('Ext.grid.Panel', {
								region : 'center',
								columns : [{
											text : '日期',
											dataIndex : 'task_date',
											width : 200,
											menuDisabled : true

										}, {
											text : '本日样本登记案例',
											dataIndex : 'case_sum',
											menuDisabled : true,
											width : 200,
											renderer : function(value) {
												if (value > 200) {
													return "<div style='color:red'>"
															+ value + "<div>";
												} else {
													return value;
												}
											}
										}, {
											text : '本日受理登记案例',
											dataIndex : 'case_accept',
											menuDisabled : true,
											width : 200,
											renderer : function(value) {
												if (value > 200) {
													return "<div style='color:red'>"
															+ value + "<div>";
												} else {
													return value;
												}
											}
										}, {
											text : '本日样本接收数量',
											dataIndex : 'sample_receive',
											width : 200,
											menuDisabled : true

										}, {
											text : '本日样本交接数量',
											dataIndex : 'sample_relay',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日送审案例数量',
											dataIndex : 'case_confirm',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日样本审核案例数',
											dataIndex : 'sample_verify',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日数据上传案例',
											dataIndex : 'case_data_sum',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日上传昨日登记案例数',
											dataIndex : 'data_today_register_yes',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日打印案例',
											dataIndex : 'case_print_sum',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日打印昨日上传案例数',
											dataIndex : 'print_today_data_yes',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日上传打印案例',
											dataIndex : 'print_today_data_tod',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日确认回款案例',
											dataIndex : 'case_comfirm_fee_sum',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日确认昨日登记案例数',
											dataIndex : 'fee_today_register_yes',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日确认登记案例数',
											dataIndex : 'fee_today_register_tod',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日快递案例',
											dataIndex : 'case_mail_sum',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日快递昨日上传案例数',
											dataIndex : 'mail_today_data_yes',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日快递上传案例数',
											dataIndex : 'mail_today_data_tod',
											width : 200,
											menuDisabled : true
										}, {
											text : '本日异常案例数',
											dataIndex : 'case_exception_sum',
											width : 200,
											menuDisabled : true
										}],
								store : Ext.create('Ext.data.Store', {
									fields : ['daily_id', 'case_sum',
											'case_data_sum', 'case_print_sum',
											'case_exception_sum',
											'case_comfirm_fee_sum',
											'case_mail_sum', 'remark',
											'task_date', 'case_accept',
											'sample_receive', 'sample_relay',
											'case_confirm', 'sample_verify',
											'data_today_register_yes',
											'print_today_data_yes',
											'print_today_data_tod',
											'fee_today_register_yes',
											'fee_today_register_tod',
											'mail_today_data_yes',
											'mail_today_data_tod'],
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'judicial/casedaily/getAllinfo.do',
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
										'beforeload' : function(ds, operation,
												opt) {
											Ext.apply(this.proxy.extraParams, {

											});
										}
									}
								}),
								listeners : {
									'afterrender' : function() {
										this.store.load();
									},
									'select' : function(value, record) {
									}
								}
							});
							var win = Ext.create("Ext.window.Window", {
										title : '历史异常案例数统计',
										width : 400,
										iconCls : 'Pageedit',
										height : 400,
										layout : 'border',
										maximizable : true,
										maximized : true,
										items : [panel]
									});
							win.show();
						}
					}, {
						text : '绩效月报表',
						iconCls : 'Find',
						handler : function() {
							var panel = Ext.create('Ext.grid.Panel', {
								dockedItems : [{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [{
												xtype : 'datefield',
												anchor : '100%',
												id : 'performance_date',
												fieldLabel : '绩效月份',
												labelWidth : 70,
												name : 'performance_date',
												format : 'Y-m-d',
												value : new Date()
											}, {
												text : '查询',
												iconCls : 'Arrowrefresh',
												handler : function() {
													var gridstore = this
															.up("grid")
															.getStore();
													gridstore.load();
												}
											}]
								}],
								region : 'center',
								columns : [{
											text : '绩效项目',
											dataIndex : 'desc',
											menuDisabled : true,
											flex : 1
										}, {
											text : '案例数量',
											dataIndex : 'count',
											menuDisabled : true,
											flex : 3
										}],
								store : Ext.create('Ext.data.Store', {
									fields : ['desc', 'count'],
									proxy : {
										type : 'jsonajax',
										timeout : 120000,
										actionMethods : {
											read : 'POST'
										},
										url : 'judicial/casedaily/getPerformance.do',
										params : {},
										reader : {
											type : 'json',
											root : 'date'

										}
									},
									listeners : {
										'beforeload' : function(ds, operation,
												opt) {
											Ext.apply(this.proxy.extraParams, {
												performance_date : Ext
														.getCmp('performance_date')
														.getValue()
											});
										}
									}
								}),
								listeners : {
									'afterrender' : function() {
										this.store.load();
									},
									'select' : function(value, record) {
									}
								}
							});
							var win = Ext.create("Ext.window.Window", {
										title : '绩效月报表',
										width : 400,
										iconCls : 'Pageedit',
										height : 400,
										layout : 'border',
										maximizable : true,
										maximized : true,
										items : [panel]
									});
							win.show();
						}
					}]
		}],
		columns : [{
					text : '日期',
					dataIndex : 'task_date',
					width : 200,
					menuDisabled : true

				}, {
					text : '本日样本登记案例',
					dataIndex : 'case_sum',
					menuDisabled : true,
					width : 200,
					renderer : function(value) {
						if (value > 200) {
							return "<div style='color:red'>" + value + "<div>";
						} else {
							return value;
						}
					}
				}, {
					text : '本日受理登记案例',
					dataIndex : 'case_accept',
					menuDisabled : true,
					width : 200,
					renderer : function(value) {
						if (value > 200) {
							return "<div style='color:red'>" + value + "<div>";
						} else {
							return value;
						}
					}
				}, {
					text : '本日样本接收数量',
					dataIndex : 'sample_receive',
					width : 200,
					menuDisabled : true

				}, {
					text : '本日样本交接数量',
					dataIndex : 'sample_relay',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日送审案例数量',
					dataIndex : 'case_confirm',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日样本审核案例数',
					dataIndex : 'sample_verify',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日数据上传案例',
					dataIndex : 'case_data_sum',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日上传昨日登记案例数',
					dataIndex : 'data_today_register_yes',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日打印案例',
					dataIndex : 'case_print_sum',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日打印昨日上传案例数',
					dataIndex : 'print_today_data_yes',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日上传打印案例',
					dataIndex : 'print_today_data_tod',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日确认回款案例',
					dataIndex : 'case_comfirm_fee_sum',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日确认昨日登记案例数',
					dataIndex : 'fee_today_register_yes',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日确认登记案例数',
					dataIndex : 'fee_today_register_tod',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日快递案例',
					dataIndex : 'case_mail_sum',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日快递昨日上传案例数',
					dataIndex : 'mail_today_data_yes',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日快递上传案例数',
					dataIndex : 'mail_today_data_tod',
					width : 200,
					menuDisabled : true
				}, {
					text : '本日异常案例数',
					dataIndex : 'case_exception_sum',
					width : 200,
					menuDisabled : true
				}],
		store : Ext.create('Ext.data.Store', {
					fields : ['daily_id', 'case_sum', 'case_data_sum',
							'case_print_sum', 'case_exception_sum',
							'case_comfirm_fee_sum', 'case_mail_sum', 'remark',
							'task_date', 'case_accept', 'sample_receive',
							'sample_relay', 'case_confirm', 'sample_verify',
							'data_today_register_yes', 'print_today_data_yes',
							'print_today_data_tod', 'fee_today_register_yes',
							'fee_today_register_tod', 'mail_today_data_yes',
							'mail_today_data_tod'],
					proxy : {
						type : 'jsonajax',
						timeout : 120000,
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/casedaily/getNowinfo.do',
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
							Ext.apply(this.proxy.extraParams, {
										key : Ext
												.getCmp("casedaily_companyid")
												.getValue()
									});
						}
					}
				}),

		listeners : {
			'afterrender' : function() {
				// this.store.load();
			},
			'select' : function(value, record) {
			}
		}

	}), Ext.create('Ext.grid.Panel', {
				id : 'statistics_exception',
				anchor : '100% 70%',
				title : '当前异常情况统计',
				loadMask : true,
				pageSize : 25,
				viewConfig : {
					trackOver : false,
					stripeRows : false,
					enableTextSelection : true
				},
				columns : [{
							text : '距离当前日期',
							dataIndex : 'days',
							menuDisabled : true,
							width : 200,
							renderer : function(value) {
								if (value > 1) {
									return "大于" + value + "天";
								} else {
									return "当前"
								}

							}
						}, {
							text : '未快递数量',
							dataIndex : 'no_mail_all',
							menuDisabled : true,
							width : 200
						}, {
							text : '已超期未快递的案例数',
							dataIndex : 'no_mail_all',
							menuDisabled : true,
							width : 200
						}, {
							text : '已超期异常的案例数',
							dataIndex : 'case_exception',
							width : 200,
							menuDisabled : true

						}, {
							text : '已超期数据未上传的案例数',
							dataIndex : 'no_data',
							width : 200,
							menuDisabled : true
						},
						// {
						// text : '已超期照片未上传的案例数',
						// dataIndex : 'no_photo',
						// width : 200,
						// menuDisabled : true
						// },
						{
							text : '已超期未回款的案例数',
							dataIndex : 'no_fee',
							width : 200,
							menuDisabled : true
						},
						// {
						// text : '已超期数据上传照片未上传未回款的案例数',
						// dataIndex : 'data_nophoto_nofee',
						// width : 200,
						// menuDisabled : true
						// }, {
						// text : '已超期数据上传照片上传未回款的案例数',
						// dataIndex : 'data_photo_nofee',
						// width : 200,
						// menuDisabled : true
						// }, {
						// text : '已超期数据上传照片未上传已回款的案例数',
						// dataIndex : 'data_nophoto_fee',
						// width : 200,
						// menuDisabled : true
						// }, {
						// text : '已超期数据未上传照片已上传未回款的案例数',
						// dataIndex : 'nodata_photo_nofee',
						// width : 200,
						// menuDisabled : true
						// }, {
						// text : '已超期数据未上传照片已上传已回款的案例数',
						// dataIndex : 'nodata_photo_fee',
						// width : 200,
						// menuDisabled : true
						// }, {
						// text : '已超期数据未上传照片未上传已回款的案例数',
						// dataIndex : 'nodata_nophoto_fee',
						// width : 200,
						// menuDisabled : true
						// }, {
						// text : '已超期数据未上传照片未上传未回款的案例数',
						// dataIndex : 'nodata_nophoto_nofee',
						// width : 200,
						// menuDisabled : true
						// },
						{
							text : '其他异常数量',
							dataIndex : 'other_exception',
							width : 200,
							menuDisabled : true
						}],
				store : Ext.create('Ext.data.Store', {
							fields : ['days', 'no_mail ', 'no_mail_all',
									'no_data', 'no_photo', 'other_exception',
									'no_fee', 'data_nophoto_nofee',
									'data_photo_nofee', 'data_nophoto_fee',
									'nodata_photo_nofee', 'nodata_photo_fee',
									'nodata_nophoto_fee',
									'nodata_nophoto_nofee', 'case_exception'],
							proxy : {
								type : 'jsonajax',
								timeout : 120000,
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/casedaily/getdaysstatistics.do',
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
									Ext.apply(this.proxy.extraParams, {
												key : Ext
														.getCmp("casedaily_companyid")
														.getValue()
											});
								}
							}
						}),

				listeners : {
					'afterrender' : function() {
						// this.store.load();
					}
				}

			})]
});