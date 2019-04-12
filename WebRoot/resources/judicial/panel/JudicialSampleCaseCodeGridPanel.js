var exportSampleCode=''
Ext.define("Rds.judicial.panel.JudicialSampleCaseCodeGridPanel",{
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
							name : 'case_code',
							labelWidth : 90,
							width : 200,
							regex : /^\w*$/,
							regexText : '请输入案例编号',
							fieldLabel : '案例编号'
						});
						var sample_code = Ext.create('Ext.form.field.Text', {
							name : 'sample_code',
							labelWidth : 60,
							width : 200,
							regex : /^\w*$/,
							regexText : '请输入样本编号',
							fieldLabel : '样本编号'
						});
						var starttime=Ext.create('Ext.form.DateField', {
							name : 'starttime',
							width : 175,
							labelWidth : 70,
							fieldLabel : '受理时间 从',
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
						var sample_starttime=Ext.create('Ext.form.DateField', {
							name : 'sample_starttime',
							width : 200,
							labelWidth : 90,
							fieldLabel : '样本登记时间从',
							labelAlign : 'left',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value : Ext.Date.add(
									new Date(),
									Ext.Date.DAY,-7),
							listeners : {
								select : function() {
									var start = sample_starttime
											.getValue();
									var end = sample_endtime
											.getValue();
									sample_endtime.setMinValue(
											start);
								}
							}
						});
						var sample_endtime=Ext.create('Ext.form.DateField', {
							name : 'sample_endtime',
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
									var start = sample_starttime
											.getValue();
									var end = sample_endtime
											.getValue();
									sample_starttime.setMaxValue(
											end);
								}
							}
						});
						me.store = Ext.create(
										'Ext.data.Store',
										{
											fields : [ 'case_code','sample_code','accept_time','verify_state','sample_in_time','report_modelname','process_instance_id'],
											start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/sampleRelay/getSampleCaseCode.do',
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
													Ext.apply(
															me.store.proxy.extraParams,{								
															endtime : dateformat(endtime
															               .getValue()),	
															starttime : dateformat(starttime
																	.getValue()),
															case_code:trim(case_code.getValue()),
															sample_code:trim(sample_code.getValue()),
															sample_endtime : dateformat(sample_endtime
														               .getValue()),	
														    sample_starttime : dateformat(sample_starttime
																	.getValue()),
															});
													exportSampleCode = trim(case_code.getValue())
													+ "&starttime=" + dateformat(starttime.getValue())
													+ "&endtime=" + dateformat(endtime.getValue())
													+"&sample_code=" + trim(sample_code.getValue())
													+ "&sample_starttime=" + dateformat(sample_starttime.getValue())
													+ "&sample_endtime=" + dateformat(sample_endtime.getValue());
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
						me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//							mode: 'SINGLE'
						});
						me.columns = [
								{
									text : '案例编号',
									dataIndex : 'case_code',
									menuDisabled : true,
									width : 150
								},
								{
									text : '样本编号',
									dataIndex : 'sample_code',
									menuDisabled : true,
									width : 150,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
											return "<a href='#'>"+ value+"</a>";

									},
									listeners:{
										'click':function(){ 
											var me = this.up("gridpanel");
											var selections = me.getView().getSelectionModel().getSelection();
											if (selections.length < 1 || selections.length > 1) {
												Ext.Msg.alert("提示", "请选择需要查看的一条记录!");
												return;
											}
											Ext.Ajax.request({
												url: "judicial/allcaseinfo/queryPlaceBySamplecode.do",
												method: "POST",
												headers: {'Content-Type': 'application/json'},
												jsonData: {"sample_code":selections[0].get("sample_code")},
												success: function (response, options) {
													response = Ext.JSON.decode(response.responseText);
													console.log(response);
													var win = Ext.create("Ext.window.Window", {
														title: '样本位置',
														width: 400,
														iconCls: 'Add',
														layout: 'fit',
														items: {
															xtype: 'grid',
															border: false,
															columns: [
																{
																	text: '实验编号',
																	dataIndex: 'experiment_no',
																	align: 'center',
																	sortable: false,
																	menuDisabled: true
																},
																{
																	text: '实验日期',
																	dataIndex: 'experiment_date',
																	align: 'center',
																	sortable: false,
																	menuDisabled: true
																},
																{
																	text: '位置',
																	dataIndex: 'places',
																	align: 'center',
																	sortable: false,
																	menuDisabled: true,
																}
															],
															store: Ext.create("Ext.data.Store", {
																fields: ['experiment_no', 'places','experiment_date'],
																data: response
															})
														}
													});
													win.show();
												},
												failure: function () {
													Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
												}
											});
											
										}
									}
								},
								{
									text : '受理时间',
									dataIndex : 'accept_time',
									menuDisabled : true,
									width : 200
								},
								{
									text : '样本登记时间',
									dataIndex : 'sample_in_time',
									menuDisabled : true,
									width:200
								},
								{
									text : '模版类型',
									dataIndex : 'report_modelname',
									menuDisabled : true,
									width:150
								},
								{
									text : '案例审核状态',
									dataIndex : 'verify_state',
									menuDisabled : true,
									width : 150,
									renderer : function(value, meta, record) {
										switch (value) {
										case '0':
											return "未审核";
											break;
										case '1':
											return "待审核";
											break;
										case '2':
				                            return "审核不通过";
											break;
										case '3':
											return "<span style='color:green'>审核通过</span>";
											break;
										case '4':
											return "<span style='color:red'>样本交接中</span>";
											break;
										case '5':
											return "<span style='color:red'>实验中</span>";
											break;
										case '6':
											return "<span style='color:red'>报告打印中</span>";
											break;
										case '7':
											return "<span style='color:red'>报告确认中</span>";
											break;
										case '8':
											return "<span style='color:red'>邮寄中</span>";
											break;
										case '9':
											return "<span style='color:red'>归档中</span>";
											break;
										case '10':
											return "<span style='color:red'>已归档</span>";
											break;
										default:
											return "";
										}
									}
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [case_code,sample_code,starttime,endtime]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [sample_starttime,sample_endtime,
											 {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											}]
								
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [{
										text : '案例编号',
										iconCls : 'Find',
										handler : me.onFind
									},{
										text : '案例模版修改',
										iconCls : 'Pageedit',
										handler : me.onUpdateReport
									},{
										text : '样本条码导出',
										iconCls : 'Pageedit',
										handler : me.exportCaseCodeBySampleCode
									},
									{
										text: '查看流程状态',
										iconCls: 'Find',
										handler: me.onTaskHistory
									}
									]
								} ];

						me.callParent(arguments);
					},
					onTaskHistory: function () {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要查看的记录!");
							return;
						}
						var values = {
							processInstanceId: selections[0].get("process_instance_id")
						};
						if (values.processInstanceId == null || values.processInstanceId == "") {
							Ext.Msg.alert("提示", "该记录不能进行此项操作!");
							return;
						}
						Ext.Ajax.request({
							url: "activiti/main/taskDetail.do",
							method: "POST",
							headers: {'Content-Type': 'application/json'},
							jsonData: values,
							success: function (response, options) {
								response = Ext.JSON.decode(response.responseText);
//								console.log(response);
								var win = Ext.create("Ext.window.Window", {
									title: '流程步骤',
									width: 700,
									iconCls: 'Add',
									layout: 'fit',
									modal:true,
									items: {
										xtype: 'grid',
										border: false,
										columns: [
											{
												text: '步骤ID',
												dataIndex: 'id',
												align: 'center',
												sortable: false,
												menuDisabled: true,
												hidden: true
											},
											{
												text: '步骤名称',
												dataIndex: 'name',
												align: 'center',
												sortable: false,
												menuDisabled: true
											},
											{
												text: '活动类型',
												dataIndex: 'taskDefinitionKey',
												align: 'center',
												sortable: false,
												menuDisabled: true,
												hidden: true
											},
											{
												text: '办理人',
												dataIndex: 'assignee',
												align: 'center',
												sortable: false,
												menuDisabled: true
											},
											{
												text: '开始时间',
												dataIndex: 'startTimeString',
												align: 'center',
												width: 120,
												sortable: false,
												menuDisabled: true
											},
											{
												text: '签收时间',
												dataIndex: 'claimTimeString',
												align: 'center',
												width: 120,
												sortable: false,
												hidden: true,
												menuDisabled: true
											},
											{
												text: '结束时间',
												dataIndex: 'endTimeString',
												align: 'center',
												width: 120,
												sortable: false,
												menuDisabled: true
											},
											{
												text: '活动耗时',
												dataIndex: 'durationInMillisString',
												align: 'center',
												sortable: false,
												menuDisabled: true
											},
											{
												text: '原因',
												dataIndex: 'comment',
												align: 'center',
												flex: 1,
												sortable: false,
												menuDisabled: true,
												 renderer : function(value, cellmeta, record,
															rowIndex, columnIndex, store) {
//														var str = value;
//														if (value.length > 15) {
//															str = value.substring(0, 15) + "...";
//														}
													 if(null == value) return "";
													 else 
														return "<span title='" + value + "'>" + value
																+ "</span>";
													}
											}
										],
										store: Ext.create("Ext.data.Store", {
											fields: ['id', 'name', 'taskDefinitionKey', 'assignee', 'claimTime', 'startTime', 'endTime', 'durationInMillis','comment',
												{
													name: 'claimTimeString', type: 'date',
													convert: function (v, rec) {
														return rec.data.claimTime == null ? "" : Ext.Date.format(new Date(rec.data.claimTime), 'Y-m-d H:i');
													}
												},
												{
													name: 'startTimeString', type: 'date',
													convert: function (v, rec) {
														return rec.data.startTime == null ? "" : Ext.Date.format(new Date(rec.data.startTime), 'Y-m-d H:i');
													}
												},
												{
													name: 'endTimeString',
													convert: function (v, rec) {
														return rec.data.endTime == null ? "" : Ext.Date.format(new Date(rec.data.endTime), 'Y-m-d H:i');
													}
												}, {
													name: 'durationInMillisString',
													convert: function (v, rec) {
														var mills = rec.data.durationInMillis;
														var result = "";
														if (mills == null) {
															result = "";
														}
														else if (mills < 1000) {
															result = "小于1秒";
														}
														else {
															var days = parseInt(mills / (1000 * 60 * 60 * 24));
															mills = mills - days * (1000 * 60 * 60 * 24);
															var hours = parseInt(mills / (1000 * 60 * 60));
															mills = mills - hours * (1000 * 60 * 60);
															var min = parseInt(mills / (1000 * 60));
															mills = mills - min * (1000 * 60);
															var second = parseInt(mills / (1000));

															result += days == 0 ? "" : (days + "天");
															result += hours == 0 ? "" : (hours + "小时");
															result += min == 0 ? "" : (min + "分钟");
															result += second + "秒";
														}
														return result;
													}
												}
											],
//											sorters: [{
////												property: 'id',
////												direction: 'DESC'
//											}],
											data: response
										})
									}
								});
								win.show();
								// response = Ext.JSON.decode(response.responseText);
								// if (response.result == true) {
								//     Ext.MessageBox.alert("提示信息", response.message);
								//     me.getStore().load();
								//     // me.up("window").close();
								// } else {
								//     Ext.MessageBox.alert("错误信息", response.message);
								//     me.getStore().load();
								// }
							},
							failure: function () {
								Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
							}
						});
					},
					exportCaseCodeBySampleCode:function(){
						console.log(exportSampleCode);
						var me = this.up("gridpanel");
						window.location.href = "judicial/sampleRelay/exportCaseCodeBySampleCode.do?case_code="+exportSampleCode;
					},
					onUpdateReport:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要修改的案例!");
							return;
						};
						console.log(selections[0].get("case_code"));
						var case_code=selections[0].get("case_code");
						report_save = function(mei) {
							var form = mei.up("form").getForm();
							var values = form.getValues();
							values["case_code"]=case_code;
							if (form.isValid()) {
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
											url : "judicial/sampleRelay/updateCaseReportmodel.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : values,
											success : function(response, options) {
												response = Ext.JSON.decode(response.responseText);
												console.log(response);
												if (response) {
													Ext.MessageBox.alert("提示信息", "修改成功！");
													var grid = me.up("gridpanel");
													me.getStore().load();
													report_update.close();
												} else {
													Ext.MessageBox.alert("错误信息", "修改失败，请联系管理员！");
												}
											},
											failure : function() {
												Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
											}
										});
							}
						}
						var storeModel = Ext.create('Ext.data.Store', {
							model : 'model',
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/dicvalues/getReportModelByPartner.do',
								params : {
									type : 'dna',
								},
								reader : {
									type : 'json',
									root : 'data'
								}
							},
							autoLoad : false,
							remoteSort : true
						});
						var report_update = Ext.create("Ext.window.Window", {
							title : '模版修改',
							width : 320,
							height : 150,
							layout : 'border',
							modal:true,
							items : [{
								xtype : 'form',
								region : 'center',
								style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
								labelAlign : "right",
								bodyPadding : 10,
								defaults : {
									anchor : '100%',
									labelWidth : 80,
									labelSeparator : "：",
									labelAlign : 'right'
								},
								border : false,
								autoHeight : true,
								buttons : [{
											text : '保存',
											iconCls : 'Disk',
											handler : report_save
										}],
								items : [Ext.create('Ext.form.ComboBox', 
										{
									columnWidth : .50,
									xtype : "combo",
									fieldLabel : "模板类型<span style='color:red'>*</span>",
									mode: 'local',   
									labelWidth : 80,
									editable:false,
									labelAlign: 'right',
									blankText:'请选择模板',   
							        emptyText:'请选择模板',  
							        allowBlank  : false,//不允许为空  
						            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
									valueField :"key",   
							        displayField: "value", 
									name : 'report_model',
									store: storeModel
						   })
								]
							}]
						})
						report_update.show();
					
					},
					onFind:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要查看的记录!");
							return;
						};
						var msg = '';
						console.log(selections[0].get("case_code"));
						for(var i = 0 ; i < selections.length ; i ++)
						{
							msg+=selections[i].get("case_code")+',';
						}
						Ext.MessageBox.alert("我是案例编号", msg.substring(0,msg.length-1));
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
