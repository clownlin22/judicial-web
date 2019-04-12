/**
 * 
 */
function dateformat(value) {
	if (value != null) {
		return Ext.Date.format(value, 'Y-m-d');
	} else {
		return '';
	}
}
var phone = new Ext.form.field.Text({
			fieldLabel : "手机号码",
			allowBlank : true,
			// id : 'crm_phone',
			labelWidth : 80,
			maxLength : 50,
			allowDecimals : false,
			name : 'phone'
		});
var orderGridStore = Ext.create('Ext.data.Store', {
			fields : ['orderId', 'phone', "client", "orderType",
					"consultCount", 'address', 'status', 'isArchive', 'remark',
					'orderInDate', 'isExtendOrder', 'isPostpaid',
					'detectionClass', 'detectionClassName', 'detectionOrg',
					'detectionOrgName', 'standFee', 'paidFee', 'isRefund',
					'sampleInfo'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'crm/register/getOrderList.do',
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
								phone : phone.getValue(),
								client : Ext.getCmp("crm_client").getValue(),
								starttime : dateformat(Ext
										.getCmp("starttime_crm_grid")
										.getValue()),
								endtime : dateformat(Ext
										.getCmp("endtime_crm_grid").getValue())
							});
				}
			}
		});
var callbackStore = Ext.create('Ext.data.Store', {
			fields : ['cbId', 'cbInPer', "cbTime", "cbContent"],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'crm/register/getCallBack.do',
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

					});
				}
			}
		});

Ext.define("Rds.crm.panel.CrmRegisterPanel", {
	extend : "Ext.panel.Panel",
	layout : 'border',
	items : [Ext.create('Ext.grid.Panel', {
				region : 'center',
				// title : '订单信息',
				width : '70%',
				loadMask : true,
				pageSize : 25,
				viewConfig : {
					trackOver : false,
					stripeRows : false,
					enableTextSelection : true
				},
				columns : [{
							dataIndex : 'orderId',
							hidden : true
						}, {
							dataIndex : 'detectionClass',
							hidden : true
						}, {
							dataIndex : 'detectionOrg',
							hidden : true
						}, {
							text : '手机号码',
							dataIndex : 'phone',
							menuDisabled : true,
							width : 150
						}, {
							text : '委托人',
							dataIndex : 'client',
							menuDisabled : true,
							width : 150
						}, {
							text : '下单方式',
							dataIndex : 'orderType',
							width : 150,
							menuDisabled : true,
							renderer : function(value) {
								switch (value) {
									case 0 :
										return "百度竞价";
										break;
									case 1 :
										return "代理";
										break;
									case 2 :
										return "免费推广";
										break;
									case 3 :
										return "电话成交";
										break;
								}

							}
						}, {
							text : '咨询次数',
							dataIndex : 'consultCount',
							width : 100,
							menuDisabled : true
						}, {
							text : '地址',
							dataIndex : 'address',
							width : 400,
							menuDisabled : true
						}, {
							text : '状态',
							dataIndex : 'status',
							width : 100,
							menuDisabled : true,
							renderer : function(value) {
								switch (value) {
									case 0 :
										return "未成交";
										break;
									case 1 :
										return "成交";
										break;
									case 2 :
										return "<span style='color:red'>退款</span>";
										break;
								}
							}
						}, {
							text : '完结状态',
							dataIndex : 'isArchive',
							width : 100,
							menuDisabled : true,
							renderer : function(value) {
								switch (value) {
									case 0 :
										return "未完结";
										break;
									case 1 :
										return "完结";
										break;
								}
							}
						}, {
							text : '登记日期',
							dataIndex : 'orderInDate',
							width : 100,
							menuDisabled : true
						}, {
							text : '备注',
							dataIndex : 'remark',
							width : 200,
							menuDisabled : true
						}, {
							text : '样本信息',
							dataIndex : 'sampleInfo',
							width : 400,
							menuDisabled : true
						}, {
							text : '标准金额',
							dataIndex : 'standFee',
							width : 100,
							menuDisabled : true
						}, {
							text : '已付金额',
							dataIndex : 'paidFee',
							width : 100,
							menuDisabled : true
						}, {
							hidden : true,
							dataIndex : 'isRefund'
						}, {
							text : '是否延伸案例',
							dataIndex : 'isExtendOrder',
							width : 100,
							menuDisabled : true,
							renderer : function(value) {
								switch (value) {
									case 0 :
										return "否";
										break;
									case 1 :
										return "<span style='color:red'>是</span>";
										break;
								}
							}
						}, {
							text : '是否先出报告',
							dataIndex : 'isPostpaid',
							width : 100,
							menuDisabled : true,
							renderer : function(value) {
								switch (value) {
									case 0 :
										return "否";
										break;
									case 1 :
										return "<span style='color:red'>是</span>";
										break;
								}
							}
						}, {
							text : '检测类型',
							dataIndex : 'detectionClassName',
							width : 100,
							menuDisabled : true
						}, {
							text : '检测单位',
							dataIndex : 'detectionOrgName',
							width : 100,
							menuDisabled : true
						}],
				store : orderGridStore,
				bbar : Ext.create('Ext.PagingToolbar', {
							store : orderGridStore,
							pageSize : 25,
							displayInfo : true,
							displayMsg : "第 {0} - {1} 条  共 {2} 条",
							emptyMsg : "没有符合条件的记录"
						}),
				dockedItems : [{
							xtype : 'buttongroup',
							columns : 3,
							floatable : true,
							defaults : {
								scale : 'small'
							},
							items : [phone, new Ext.form.field.Text({
												fieldLabel : "委托人",
												allowBlank : true,
												id : 'crm_client',
												labelWidth : 80,
												maxLength : 50,
												name : 'client'
											})]
						}
//						, {
//							xtype : 'toolbar',
//							name : 'search',
//							dock : 'top',
//							items : []
//						}
						, {
							xtype : 'toolbar',
							dock : 'top',
							items : [Ext.create('Ext.form.field.Date', {
										name : 'starttime',
										id : 'starttime_crm_grid',
										width : 175,
										fieldLabel : '受理日期从',
										labelWidth : 70,
										labelAlign : 'right',
										emptyText : '请选择日期',
										format : 'Y-m-d',
										value : Ext.Date.add(new Date(),
												Ext.Date.DAY, -3),
										maxValue : new Date(),
										listeners : {
											'select' : function() {
												var start = this.getValue();
												Ext.getCmp("endtime_crm_grid")
														.setMinValue(start);
												var endDate = Ext
														.getCmp("endtime_crm_grid")
														.getValue();
												if (start > endDate) {
													Ext
															.getCmp("endtime_crm_grid")
															.setValue(start);
												}
											}
										}
									}), Ext.create('Ext.form.field.Date', {
										id : 'endtime_crm_grid',
										name : 'endtime',
										width : 135,
										labelWidth : 20,
										fieldLabel : '到',
										labelAlign : 'right',
										emptyText : '请选择日期',
										format : 'Y-m-d',
										maxValue : new Date(),
										value : Ext.Date.add(new Date(),
												Ext.Date.DAY),
										listeners : {
											select : function() {
												var start = Ext
														.getCmp("starttime_crm_grid")
														.getValue();
												var endDate = this.getValue();
												if (start > endDate) {
													Ext
															.getCmp("starttime_crm_grid")
															.setValue(endDate);
												}
											}
										}
									}), '-', {
								text : '查询',
								iconCls : 'Find',
								handler : function() {
									var me = this.up("gridpanel");
									me.getStore().currentPage = 1;
									me.getStore().load();
								}
							}]
						}, {
							xtype : 'toolbar',
							dock : 'top',
							items : [{
								text : '新建订单',
								iconCls : 'Add',
								handler : function() {
									var me = this.up("gridpanel");
									var form = Ext
											.create(
													"Rds.crm.form.CrmRegisterInsertForm",
													{
														region : "center",
														autoScroll : true,
														grid : me
													});
									var win = Ext.create("Ext.window.Window", {
												title : '订单新增',
												width : 700,
												iconCls : 'Pageadd',
												height : 600,
												layout : 'border',
												items : [form]
											});
									win.show();
								}
							}, '-', {
								text : '更新订单',
								iconCls : 'Pageedit',
								handler : function() {
									var me = this.up("gridpanel");
									var selections = me.getView()
											.getSelectionModel().getSelection();
									if (selections.length < 1) {
										Ext.Msg.alert("提示", "请选择需要更新的订单!");
										return;
									}
									var form = Ext
											.create(
													"Rds.crm.form.CrmRegisterUpdateForm",
													{
														region : "center",
														autoScroll : true,
														grid : me
													});
									console.info(selections[0]);

									var win = Ext.create("Ext.window.Window", {
												title : '订单新增',
												width : 1366,
												iconCls : 'Pageadd',
												height : 768,
												layout : 'border',
												items : [form],
												maximizable : true,
												maximized : true
											});
									form.loadRecord(selections[0]);
									win.show();
									form.loadRecord(selections[0]);

								}
							}]
						}],
				listeners : {
					'afterrender' : function() {
						orderGridStore.load();
					},
					'select' : function(value, record) {
						// console.log(record.isModel);
						Ext.getCmp('crm_callback_form').loadRecord(record);
						Ext.apply(callbackStore.proxy.params, {
									orderId : record.get('orderId')
								});
						callbackStore.load();
					}
				}

			}), Ext.create('Ext.panel.Panel', {
		layout : 'border',
		width : '30%',
		region : 'east',
		items : [Ext.create("Ext.form.Panel", {
			region : "center",
			name : 'addCallBack',
			id : 'crm_callback_form',
			// title : '新增回访信息',
			autoScroll : true,
			height : 155,
			defaults : {
				padding : '10 10 10 10'
			},
			layout : 'column',
			items : [{
						xtype : "textfield",
						maxLength : 50,
						hidden : true,
						name : 'orderId'

					}, {
						xtype : 'textarea',
						fieldLabel : "回访信息",
						name : 'cbContent',
						id : 'crm_callback_cbContent',
						height : 150,
						allowBlank : false,// 不允许为空
						maxLength : 1000,
						columnWidth : 1,
						labelWidth : 80,
						labelAlign : 'right'
					}],
			buttons : [{
				text : '保存',
				iconCls : 'Disk',
				handler : function() {
					var me = this.up("form");
					var form = me.getForm();
					var values = form.getValues();
					if (values['orderId'].trim() == '') {
						Ext.MessageBox.alert("提示信息", "请选中回访案例。");
						return;
					}
					if (form.isValid()) {
						Ext.Ajax.request({
									url : "crm/register/saveCallBack.do",
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
													"新增回访成功");
											Ext.apply(
													callbackStore.proxy.params,
													{
														orderId : values['orderId']
													});
											callbackStore.load();
											Ext
													.getCmp('crm_callback_cbContent')
													.setValue('');
										} else {
											Ext.MessageBox.alert("错误信息",
													response.msg);
										}
									},
									failure : function() {
										Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
									}
								});
					}
				}
			}]
		}), Ext.create('Ext.grid.Panel', {
			region : 'south',
			height : '50%',
			loadMask : true,
			viewConfig : {
				trackOver : false,
				stripeRows : false,
				enableTextSelection : true
			},
			columns : [{
						dataIndex : 'cbId',
						hidden : true
					}, {
						text : '历史回访信息',
						xtype : 'templatecolumn',
						tpl : '<p>回访人：{cbInPer}&nbsp&nbsp|&nbsp&nbsp时间：{cbTime}</p><p> 回访内容：<p style="white-space:normal;font-size:18px">{cbContent}</p></p>',
						flex : 1
					}],
			store : callbackStore,
			// bbar : Ext.create('Ext.PagingToolbar', {
			// store : callbackStore,
			// pageSize : 25,
			// displayInfo : true,
			// displayMsg : "第 {0} - {1} 条 共 {2} 条",
			// emptyMsg : "没有符合条件的记录"
			// }),
			/*
			 * dockedItems : [{ xtype : 'toolbar', dock : 'top', items : [{ text :
			 * '新增回访', iconCls : 'Add', handler : function() { var me =
			 * this.up("gridpanel"); var form = Ext.create("Ext.form.Panel", {
			 * region : "center", autoScroll : true, grid : me, defaults : {
			 * padding : '10 10 10 10' }, layout : 'column', items : [ // { // //
			 * 该列在整行中所占的百分比 // columnWidth : 1, // xtype : "textfield", //
			 * labelAlign : 'right', // labelWidth : 80, // fieldLabel : "手机号码", //
			 * allowBlank : false,// // 不允许为空 // blankText : "不能为空",// //
			 * 错误提示信息，默认为This // maxLength : 50, // name : 'phone' // // }, {
			 * xtype : 'textarea', fieldLabel : "回访信息", name : 'cbContent', //
			 * width : 500, allowBlank : true,// 不允许为空 maxLength : 500,
			 * columnWidth : 1, labelWidth : 80, labelAlign : 'right' }],
			 * buttons : [{ text : '保存', iconCls : 'Disk', handler : function() {
			 * var me = this.up("form"); var form = me.getForm(); var values =
			 * form.getValues(); if (form.isValid()) { Ext.Ajax.request({ url :
			 * "crm/register/saveCallBack.do", method : "POST", headers : {
			 * 'Content-Type' : 'application/json' }, jsonData : values, success :
			 * function(response, options) { response = Ext.JSON
			 * .decode(response.responseText); if (response.success == true) {
			 * Ext.MessageBox.alert( "提示信息", "新增回访成功");
			 * me.grid.getStore().load(); me.up("window").close(); } else {
			 * Ext.MessageBox.alert( "错误信息", response.msg); } }, failure :
			 * function() { Ext.Msg.alert("提示", "保存失败<br>请联系管理员!"); } }); } } }, {
			 * text : '取消', iconCls : 'Cancel', handler : function() { var me =
			 * this; me.up("window").close(); } }] }); var win =
			 * Ext.create("Ext.window.Window", { title : '订单新增', width : 400,
			 * iconCls : 'Pageadd', height : 400, layout : 'border', items :
			 * [form] }); win.show(); } } ] }],
			 */
			listeners : {
				'afterrender' : function() {
				},
				'select' : function(value, record) {
				}
			}

		})]
	})]
});