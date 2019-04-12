Ext.define("Rds.judicial.panel.JudicialAllCaseMarketInfoPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		me.store = Ext.create('Ext.data.Store', {
			fields : ['case_id', 'case_area', 'case_code', 'case_areacode',
					'receiver_area', 'case_receiver', 'accept_time','client',
					'close_time', 'receiver_id','sample_in_time','agent'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/allcaseinfo/getallcaseinfo.do',
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
								starttime : dateformat(Ext
										.getCmp('starttime_allcase').getValue()),
								case_name : trim(Ext
										.getCmp('case_name_allcase').getValue()),
								endtime : dateformat(Ext
										.getCmp('endtime_allcase').getValue()),
								case_code : trim(Ext
										.getCmp('case_code_allcase').getValue()),
								report_model:'sqjdmodel',
//								fee_type : Ext.getCmp('fee_type_allcase')
//										.getValue(),
//								fee_status : Ext.getCmp('fee_status_allcase')
//										.getValue(),
								mail_code : trim(Ext
										.getCmp('mail_code_allcase').getValue()),
								area : trim(Ext.getCmp('area_allcase')
										.getValue()),
								usercode:usercode
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
					text : '案例编号',
					dataIndex : 'case_id',
					hidden : true,
					menuDisabled : true
				}, {
					text : '案例条形码',
					dataIndex : 'case_code',
					menuDisabled : true,
					width : 200,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						var isnull = record.data["is_delete"];
						if (isnull == 1) {
							return "<div style=\"text-decoration: line-through;color: red;\">"
									+ value + "</div>"
						} else {
							return value;
						}

					}
				}, {
					text : '身份证地址',
					dataIndex : 'case_area',
					menuDisabled : true,
					width : 150,
					hidden:true
				}, {
					text : '案例归属地',
					dataIndex : 'receiver_area',
					menuDisabled : true,
					width : 150,
					hidden:true
				}, {
					text : '归属人',
					dataIndex : 'case_receiver',
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
									},
									hidden:true
				}, {
					text : '委托人',
					dataIndex : 'client',
					menuDisabled : true,
					width : 200
				}, {
					text : '受理日期',
					dataIndex : 'accept_time',
					menuDisabled : true,
					width : 200
				}, {
					text : '样本登记日期',
					dataIndex : 'sample_in_time',
					menuDisabled : true,
					width : 150,
					hidden:true
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					style : {
						borderTopWidth : '0px !important',
						borderBottomWidth : '0px !important'
					},
					defaults : {
						labelAlign : 'right'
					},
					items : [{
								xtype : 'textfield',
								name : 'case_code',
								id : 'case_code_allcase',
								labelWidth : 70,
								width : 175,
//								regex : /^\w*$/,
//								regexText : '请输入英文或数字',
								fieldLabel : '案例条形码'
							}, {
								xtype : 'textfield',
								fieldLabel : '姓名',
								width : 175,
								labelWidth : 70,
								id : 'case_name_allcase',
								name : 'case_name'
							}, {
								xtype : 'textfield',
								fieldLabel : '地区',
								width : 175,
								labelWidth : 70,
								id : 'area_allcase',
								name : 'area',
								hidden:true
							}, {
								xtype : 'textfield',
								fieldLabel : '快递单号',
								width : 175,
								labelWidth : 70,
								id : 'mail_code_allcase',
								name : 'mail_code',
								hidden:true
							},{
								xtype : 'datefield',
								name : 'starttime',
								id : 'starttime_allcase',
								width : 175,
								fieldLabel : '受理日期 从',
								labelWidth : 70,
								labelAlign : 'right',
								emptyText : '请选择日期',
								format : 'Y-m-d',
								value : Ext.Date.add(new Date(), Ext.Date.DAY, -7),
								maxValue : new Date(),
								listeners : {
									'select' : function() {
										var start = Ext.getCmp('starttime_allcase')
												.getValue();
										Ext.getCmp('endtime_allcase')
												.setMinValue(start);
										var endDate = Ext.getCmp('endtime_allcase')
												.getValue();
										if (start > endDate) {
											Ext.getCmp('endtime_allcase')
													.setValue(start);
										}
									}
								}
							}, {
								xtype : 'datefield',
								id : 'endtime_allcase',
								name : 'endtime',
								width : 130,
								labelWidth : 20,
								fieldLabel : '到&nbsp&nbsp',
								labelAlign : 'right',
								emptyText : '请选择日期',
								format : 'Y-m-d',
								maxValue : new Date(),
								value : Ext.Date.add(new Date(), Ext.Date.DAY),
								listeners : {
									select : function() {
										var start = Ext.getCmp('starttime_allcase')
												.getValue();
										var endDate = Ext.getCmp('endtime_allcase')
												.getValue();
										if (start > endDate) {
											Ext.getCmp('starttime_allcase')
													.setValue(endDate);
										}
									}
								}
							}, {
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				}];

		me.callParent(arguments);
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
