Ext
		.define(
				"Rds.judicial.panel.JudicialDeleteDataGridPanel",
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
											fields : [ 'case_id', 'case_code',
													'case_areacode',
													'receiver_area',
													'case_receiver',
													'urgent_state',
												    'remark',
													 'print_count','verify_state',
													'accept_time','close_time',
													'report_modelname','client',
													'report_model','address',
													'case_in_per',"receiver_id",
													'case_in_pername','phone','is_new',
													'sample_in_time', 'case_id','is_delete'
													],
											start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/register/getExperimentCaseInfo.do',
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
																endtime : dateformat(Ext
																		.getCmp(
																		'delete_data_endtime_query')
																               .getValue()),
															    urgent_state : Ext
																		.getCmp(
																		'delete_data_urgent_state_query')
															            	.getValue(),
																starttime : dateformat(Ext
																		.getCmp(
																				'delete_data_starttime_query')
																		.getValue()),
																receiver : trim(Ext
																				.getCmp(
																						'delete_data_receiver_query')
																				.getValue()),
																case_code : trim(Ext
																		.getCmp(
																				'delete_data_case_code_query')
																		.getValue()),
																is_delete : Ext
																		.getCmp(
																				'delete_data_is_delete_query')
																		.getValue()});
												}
											}
										});
						me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//							mode: 'SINGLE'
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
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var isnull= record.data["is_delete"];
										if (isnull == 1) {
											return "<div style=\"text-decoration: line-through;color: red;\">"
													+ value + "</div>"
										} else {
											return value;
										}

									}
								},
								{
									text : '案例归属地',
									dataIndex : 'receiver_area',
									menuDisabled : true,
									width : 200
								},
								{
									text : '归属人',
									dataIndex : 'case_receiver',
									menuDisabled : true,
									width : 120
								},
								{
									text : '电话号码',
									dataIndex : 'phone',
									menuDisabled : true,
									width : 120
								},
								{
									text : '委托人',
									dataIndex : 'client',
									menuDisabled : true,
									width : 120
								},
								{
									text : '紧急程度',
									dataIndex : 'urgent_state',
									menuDisabled : true,
									width : 100,
									renderer : function(value) {
										switch (value) {
										case 0:
											return "普通";
											break;
										case 1:
											return "<span style='color:red'>紧急</span>";
											break;
										default:
											return "";
										}
									}
								},
								{
									text : '案例审核状态',
									dataIndex : 'verify_state',
									menuDisabled : true,
									width : 200,
									renderer : function(value) {
										switch (value) {
										case 0:
											return "未审核";
//											if(record.get("task_def_key")=="taskAudit"){
//												return "已提交审核";
//											}else{
//												return "未审核";
//											}
											break;
										case 1:
											return "待审核审核";
											break;
										case 2:
											return "<span style='color:red'>案例审核未通过</span>";
											break;
										case 3:
											return "<span style='color:green'>审核通过</span>";
											break;
										case 4:
											return "<span style='color:red'>样本交接中</span>";
											break;
										case 5:
											return "<span style='color:red'>实验中</span>";
											break;
										case 6:
											return "<span style='color:red'>报告打印中</span>";
											break;
										case 7:
											return "<span style='color:red'>报告确认中</span>";
											break;
										case 8:
											return "<span style='color:red'>邮寄中</span>";
											break;
										case 9:
											return "<span style='color:red'>归档中</span>";
											break;
										case 10:
											return "<span style='color:red'>已归档</span>";
											break;
										default:
											return "";
										}
									}
								},
								{
									text : '受理日期',
									dataIndex : 'accept_time',
									menuDisabled : true,
									width : 100
								},
								{
									text : '截止日期',
									dataIndex : 'close_time',
									menuDisabled : true,
									width : 100
								},
								{
									text : '样本登记日期',
									dataIndex : 'sample_in_time',
									menuDisabled : true,
									width : 100
								},
								{
									text : '模板名称',
									dataIndex : 'report_modelname',
									menuDisabled : true,
									width : 100
								},
								{
									text : '登记人',
									dataIndex : 'case_in_pername',
									menuDisabled : true,
									width : 100
								},{
									text : '备注',
									dataIndex : 'remark',
									menuDisabled : true,
									width : 300
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [
											{
												xtype : 'textfield',
												name : 'case_code',
												id : 'delete_data_case_code_query',
												labelWidth : 70,
												width : 155,
												fieldLabel : '案例条形码'
											},
											{
												xtype : 'textfield',
												fieldLabel : '归属人',
												width : 145,
												labelWidth : 50,
												id : 'delete_data_receiver_query',
												name : 'receiver'
											},
											new Ext.form.field.ComboBox(
													{
														fieldLabel : '紧急程度',
														width : 155,
														labelWidth : 60,
														editable : false,
														triggerAction : 'all',
														displayField : 'Name',
														labelAlign : 'right',
														valueField : 'Code',
														store : new Ext.data.ArrayStore(
																{
																	fields : [
																			'Name',
																			'Code' ],
																	data : [
																			[
																					'全部',
																					-1 ],
																			[
																					'普通',
																					0 ],
																			[
																					'紧急',
																					1 ] ]
																}),
														value : -1,
														mode : 'local',
														// typeAhead: true,
														name : 'urgent_state',
														id : 'delete_data_urgent_state_query'
													}),
											new Ext.form.field.ComboBox(
													{
														fieldLabel : '是否废除',
														width : 155,
														labelWidth : 60,
														editable : false,
														triggerAction : 'all',
														displayField : 'Name',
														labelAlign : 'right',
														valueField : 'Code',
														store : new Ext.data.ArrayStore(
																{
																	fields : [
																			'Name',
																			'Code' ],
																	data : [
																			[
																					'全部',
																					'' ],
																			[
																					'未废除',
																					0 ],
																			[
																					'已废除',
																					1 ] ]
																}),
														value : '',
														mode : 'local',
														// typeAhead: true,
														name : 'is_delete',
														id : 'delete_data_is_delete_query'
													}),
											{
												xtype : 'datefield',
												name : 'starttime',
												id : 'delete_data_starttime_query',
												width : 175,
												fieldLabel : '受理时间 从',
												labelWidth : 70,
												labelAlign : 'right',
												emptyText : '请选择日期',
												format : 'Y-m-d',
												maxValue : new Date(),
												value : Ext.Date.add(
														new Date(),
														Ext.Date.DAY,-7),
												listeners : {
													'select' : function() {
														var start = Ext.getCmp(
																'delete_data_starttime_query')
																.getValue();
														Ext.getCmp('delete_data_endtime_query')
																.setMinValue(
																		start);
														var endDate = Ext
																.getCmp(
																		'delete_data_endtime_query')
																.getValue();
														if (start > endDate) {
															Ext
																	.getCmp(
																			'delete_data_endtime_query')
																	.setValue(
																			start);
														}
													}
												}
											},
											{
												xtype : 'datefield',
												id : 'delete_data_endtime_query',
												name : 'endtime',
												width : 135,
												labelWidth : 20,
												fieldLabel : '到',
												labelAlign : 'right',
												emptyText : '请选择日期',
												format : 'Y-m-d',
												maxValue : new Date(),
												value : Ext.Date.add(
														new Date(),
														Ext.Date.DAY),
												listeners : {
													select : function() {
														var start = Ext.getCmp(
																'delete_data_starttime_query')
																.getValue();
														var endDate = Ext
																.getCmp(
																		'delete_data_endtime_query')
																.getValue();
														if (start > endDate) {
															Ext
																	.getCmp(
																			'delete_data_starttime_query')
																	.setValue(
																			endDate);
														}
													}
												}
											}, {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											} ]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '查看样本信息',
										iconCls : 'Find',
										handler : me.onFind
									}, {
										text : '删除实验数据',
										iconCls : 'Delete',
										handler : me.onDelete
									},{
										text : '批量删除实验数据',
										iconCls : 'Delete',
										handler : me.allDelete
									}]
								} ];

						me.callParent(arguments);
					},
					onFind:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要查看的记录!");
							return;
						};
						var win = Ext.create("Ext.window.Window", {
							title : "样本信息（案例条形码：" + selections[0].get("case_code") + "）",
							width : 600,
							iconCls : 'Find',
							height : 400,
							layout : 'border',
							bodyStyle : "background-color:white;",
							items : [ Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 600,
								height : 400,
								frame : false,
								viewConfig : {
									forceFit : true,
									stripeRows : true// 在表格中显示斑马线
								},
								store : {// 配置数据源
									fields : [ 'sample_id', 'sample_code', 'sample_type','sample_typename',
											'sample_call', 'sample_callname', 'sample_username',
											'id_number', 'birth_date' ],// 定义字段
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'judicial/register/getSampleInfo.do',
										params : {
											'case_id' : selections[0].get("case_id")
										},
										reader : {
											type : 'json',
											root : 'items',
											totalProperty : 'count'
										}
									},
									autoLoad : true
								// 自动加载
								},
								columns : [// 配置表格列
								{
									header : "样本条形码",
									dataIndex : 'sample_code',
									flex : 1,
									menuDisabled : true
								}, {
									header : "称谓",
									dataIndex : 'sample_callname',
									flex : 1,
									menuDisabled : true
								}, {
									header : "姓名",
									dataIndex : 'sample_username',
									flex : 1,
									menuDisabled : true
								}, {
									header : "身份证号",
									dataIndex : 'id_number',
									flex : 2,
									menuDisabled : true
								}, {
									header : "出生日期",
									dataIndex : 'birth_date',
									flex : 1,
									menuDisabled : true
								}, {
									header : "样本类型",
									dataIndex : 'sample_typename',
									flex : 1,
									menuDisabled : true
								} ]
							}) ]
						});
						win.show();
					},
					onDelete : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要删除的记录!");
							return;
						};
						var values = {
							case_code : selections[0].get("case_code")
						};
						Ext.MessageBox
								.confirm(
										'提示',
										'确定删除此案例数据吗',
										function(id) {
											if (id == 'yes') {
												Ext.getBody().mask("请稍等，正在删除...","x-mask-loading");
												Ext.Ajax
														.request({
															url : "judicial/subCase/deleteData.do",
															method : "POST",
															headers : {
																'Content-Type' : 'application/json'
															},
                                                            timeout:1000000,
															jsonData : values,
                                                            //async:false,
															success : function(
																	response,
																	options) {
																Ext.getBody().unmask();
																response = Ext.JSON
																		.decode(response.responseText);
																if (response == true) {
																	Ext.MessageBox
																			.alert(
																					"提示信息",
                                                                        response.message);
																	me
																			.getStore()
																			.load();
																} else {
																	Ext.MessageBox
																			.alert(
																					"错误信息",
                                                                        response.message);
																}
															},
															failure : function() {
																Ext.getBody().unmask();
																Ext.Msg
																		.alert(
																				"提示",
																				"网络故障<br>请联系管理员!");
															}
														});
											}
										},{
											xtype:'panel',
											region:"center",
											border : false
										});
					},
					allDelete : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要操作的记录!");
							return;
						}
						var data = new Array() ;
						for(var i=0;i<selections.length;i++){
							data[i] = selections[i].data;
					
						}
						Ext.MessageBox
								.confirm(
										'提示',
										'确定删除此案例数据吗',
										function(id) {
											if (id == 'yes') {
												Ext.getBody().mask("请稍等，正在删除...","x-mask-loading");
												Ext.Ajax
														.request({
															url : "judicial/subCase/allDelete.do",
															method : "POST",
															headers : {
																'Content-Type' : 'application/json'
															},
                                                            timeout:1000000,
															jsonData : data,
                                                            //async:false,
															success : function(
																	response,
																	options) {
																Ext.getBody().unmask();
																response = Ext.JSON
																		.decode(response.responseText);
																if (response == true) {
																	Ext.MessageBox
																			.alert(
																					"提示信息",
                                                                        response.message);
																	me
																			.getStore()
																			.load();
																} else {
																	Ext.MessageBox
																			.alert(
																					"提示信息",
                                                                        response.message);
																}
															},
															failure : function() {
																Ext.getBody().unmask();
																Ext.Msg
																		.alert(
																				"提示",
																				"网络故障<br>请联系管理员!");
															}
														});
											}
										},{
											xtype:'panel',
											region:"center",
											border : false
										});
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
