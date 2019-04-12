Ext
		.define(
				"Rds.judicial.panel.JudicialCaseTimeReadGridPanel",
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
						var case_code = Ext.create('Ext.form.field.Text', {
							name : 'casecode',
							labelWidth : 60,
							width : 200,
							regex : /^\w*$/,
							regexText : '请输入案例编号',
							fieldLabel : '案例编号'
						});
						var expressif=new Ext.form.field.ComboBox({
							fieldLabel : '是否发报告',
							width : 150,
							labelWidth : 70,
							editable : false,
							triggerAction : 'all',
							displayField : 'Name',
							labelAlign : 'left',
							valueField : 'Code',
							store : new Ext.data.ArrayStore(
									{
										fields : ['Name','Code' ],
										data : [['全部',0],['是',1 ],['否',2 ] ]
									}),
							value : '',
							mode : 'local',
							name : 'expressif',
							value: 0
						});
						var printif=new Ext.form.field.ComboBox({
							fieldLabel : '是否打印报告',
							width : 150,
							labelWidth : 80,
							editable : false,
							triggerAction : 'all',
							displayField : 'Name',
							labelAlign : 'left',
							valueField : 'Code',
							store : new Ext.data.ArrayStore(
									{
										fields : ['Name','Code' ],
										data : [['全部',0],['是',1 ],['否',2 ] ]
									}),
							value : '',
							mode : 'local',
							name : 'printif',
							value: 0
						});
						var starttime=Ext.create('Ext.form.DateField', {
							name : 'starttime',
							width : 175,
							labelWidth : 70,
							fieldLabel : '登记时间 从',
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
											fields : ['case_code','accept_time',"close_time","sample_in_time","verify_baseinfo_time",
											          "verify_sampleinfo_time","mail_time","compare_date","confirm_date"],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'statistics/statCaseTime.do',
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
																endtime : dateformat(endtime
																               .getValue()),	
																starttime : dateformat(starttime
																		.getValue()),
																case_code : case_code.getValue(),
																expressif:expressif.getValue(),
																printif:printif.getValue()
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
									width : 150,
								},
								{
									text : '受理日期',
									dataIndex : 'accept_time',
									menuDisabled : true,
									width : 120
								},
								{
									text : '报告打印日期',
									dataIndex : 'close_time',
									menuDisabled : true,
									width : 150
								},
								{
									text : '登记日期',
									dataIndex : 'sample_in_time',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '案例审核日期',
									dataIndex : 'verify_baseinfo_time',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '样本审核日期',
									dataIndex : 'verify_sampleinfo_time',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '邮寄日期',
									dataIndex : 'mail_time',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '实验数据上传日期',
									dataIndex : 'compare_date',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '回款日期',
									dataIndex : 'confirm_date',
									menuDisabled : true,
									width : 150,
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [
											case_code,starttime,endtime,expressif,printif, {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											}]
								}];

						me.callParent(arguments);
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
