Ext.define("Rds.judicial.panel.JudicialRegisterChangeGridPanel",{
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
							width : '20%',
							regex : /^\w*$/,
							emptyText : '请输入案例编号',
							fieldLabel : '案例编号'
						});
						var case_userid = Ext.create('Ext.form.ComboBox', {
								xtype : 'combo',
								fieldLabel : '归属人',
								labelWidth : 60,
								width : '20%',
								name : 'case_userid',
								emptyText:'(人员首字母)：如小明(xm)',
								store :Ext.create("Ext.data.Store",{
									   fields:[
											{name:'key',mapping:'key',type:'string'},
											{name:'value',mapping:'value',type:'string'}
							          ],
									pageSize : 20,
									autoLoad: false,
									proxy : {
										type : "ajax",
										url:'judicial/dicvalues/getUsersId.do',
										reader : {
											type : "json"
										}
									}
								}),
								displayField : 'value',
								valueField : 'key',
								typeAhead : false,
								hideTrigger : true,
								minChars : 2,
								matchFieldWidth : true,
								listConfig : {
									loadingText : '正在查找...',
									emptyText : '没有找到匹配的数据'
								}
							});

						var receiver_area = Ext.create('Ext.form.field.Text', {
							name : 'receiver_area',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '归属地'
						});
						var client = Ext.create('Ext.form.field.Text', {
							name : 'client',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '委托人'
						});
						var sample_in_per = Ext.create('Ext.form.field.Text', {
							name : 'sample_in_per',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '采样人'
						});
						var parnter_name = Ext.create('Ext.form.field.Text', {
							name : 'parnter_name',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '合作商'
						});
						var phone = Ext.create('Ext.form.field.Text', {
							name : 'phone',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '电话号码'
						});
						var starttime=Ext.create('Ext.form.DateField', {
							name : 'starttime',
							width : '20%',
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
							width : '20%',
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
					    var is_delete=Ext.create('Ext.form.ComboBox', 
								{
									fieldLabel : '是否废除',
									width : '20%',
									labelWidth : 60,
									editable : false,
									triggerAction : 'all',
									displayField : 'Name',
									valueField : 'Code',
									store : new Ext.data.ArrayStore({
												fields : ['Name','Code' ],
												data : [['全部','' ],
														['未废除','0' ],
														['已废除',1 ]]
											}),
									value : '',
									mode : 'local',
									// typeAhead: true,
									name : 'is_delete',
								});
					    var source_type = Ext.create('Ext.form.ComboBox', 
								{
									fieldLabel : '案例来源',
									width : '20%',
									labelWidth : 60,
									editable : false,
									triggerAction : 'all',
									displayField : 'Name',
									valueField : 'Code',
									store : new Ext.data.ArrayStore(
											{fields : ['Name','Code' ],
												data : [
														['全部','' ],
														['市场','0' ],
														['网络','1']]
											}),
									value : '',
									mode : 'local',
									name : 'source_type',
								});
					    var verify_state = Ext.create('Ext.form.ComboBox', 
								{
									fieldLabel : '审核状态',
									width : '20%',
									labelWidth : 70,
									editable : false,
									triggerAction : 'all',
									displayField : 'Name',
									valueField : 'Code',
									store : new Ext.data.ArrayStore(
											{fields : ['Name','Code' ],
												data : [
														['全部','' ],
														['未审核','0' ],
														['待审核',1],
														['审核不通过',2],
														['审核通过',3],
														['案例样本交接确认中',4],
														['实验中',5],
														['报告打印中',6],
														['报告确认中',7],
														['邮寄中',8],
														['归档中',9],
														['已归档',10]
														]
											}),
									value : '',
									mode : 'local',
									name : 'verify_state',
								});
					    var confirm_code = Ext.create('Ext.form.field.Text', {
							name : 'confirm_code',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '激活码'
						});
					    var case_state = Ext.create('Ext.form.ComboBox', {
							fieldLabel : '案例状态',
							width : '20%',
							labelWidth : 60,
							editable : false,
							triggerAction : 'all',
							displayField : 'Name',
							valueField : 'Code',
							store : new Ext.data.ArrayStore(
									{fields : ['Name','Code' ],
										data : [
												['全部','' ],
												['正常','0' ],
												['为先出报告后付款',1],
												['免单',2],
												['优惠',3],
												['月结',4],
												['二次采样',5],
												['补样',6]
												]
									}),
							value : '',
							mode : 'local',
							name : 'case_state',
						});
						me.store = Ext
								.create(
										'Ext.data.Store',
										{
											fields : [ 'case_id', 'case_code','case_userid','confirm_code',
													'case_areacode','typeid', 'receiver_area', 'case_receiver',
													'urgent_state',  'remark',  'print_count','verify_state',
													'accept_time','consignment_time','close_time', 'report_modelname','client',
													'report_model','address', 'case_in_per', 'case_in_pername','phone',
													'sample_in_time','is_delete', 'sample_in_per','unit_type','sample_relation',
													'case_type','agent','copies','parnter_name','case_state','source_type'
													],
											start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/register/getChangeCaseInfo.do',
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
												'beforeload' : function(ds,operation, opt) {
													Ext.apply(
															me.store.proxy.extraParams,{		
																receiver_area:trim(receiver_area.getValue()),
																endtime : dateformat(endtime
																               .getValue()),	
																starttime : dateformat(starttime
																		.getValue()),
																case_userid : case_userid.getValue(),
																sample_in_per:trim(sample_in_per.getValue()),
																client:trim(client.getValue()),
																case_code : trim(case_code.getValue()),
																source_type:trim(source_type.getValue()),
																parnter_name:trim(parnter_name.getValue()),
																is_delete :is_delete.getValue(),
																confirm_code:trim(confirm_code.getValue()),
																verify_state:verify_state.getValue(),
																case_state:case_state.getValue(),
																phone:trim(phone.getValue())
																});
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
									width : 120,
//									listeners:{
//										'click':function(){ 
//											var me = this.up("gridpanel");
//											var selections = me.getView().getSelectionModel().getSelection();
//											if (selections.length < 1 || selections.length > 1) {
//												Ext.Msg.alert("提示", "请选择需要查看的一条记录!");
//												return;
//											}
//											var form = Ext.create(
//													"Rds.judicial.form.JudicialCaseImageShow", {
//														region : "center",
//														grid : me
//													});
//											var win = Ext.create("Ext.window.Window", {
//														title : '图片查看',
//														width : 1600,
//														iconCls : 'Pageedit',
//														height : 1000,
//														maximizable : true,
//														maximized : true,
//														layout : 'border',
//														items : [form]
//													});
//											form.loadRecord(selections[0]);
//											win.show();
//										}
//									}
								},
								{
									text : '受理日期',
									dataIndex : 'accept_time',
									menuDisabled : true,
									width : 100
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
									width : 80
								},
								{
									text : '被代理人',
									dataIndex : 'agent',
									menuDisabled : true,
									width : 80
								},
								{
									text : '委托人',
									dataIndex : 'client',
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
									text : '案例审核状态',
									dataIndex : 'verify_state',
									menuDisabled : true,
									width : 150,
									renderer : function(value,meta,record) {
										switch (value) {
										case 0:
											return "未审核";
											break;
										case 1:
											return "待审核";
											break;
										case 2:
				                                return "已提交审核";
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
									text : '打印日期',
									dataIndex : 'close_time',
									menuDisabled : true,
									width : 150
								},
								{
									text : '样本登记日期',
									dataIndex : 'sample_in_time',
									menuDisabled : true,
									width : 150
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
									text : '案例状态',
									dataIndex : 'case_state',
									menuDisabled : true,
									width : 150,
									renderer : function(value,meta,record) {
										switch (value) {
										case 0:
											return "正常";
											break;
										case 1:
											return "为先出报告后付款";
											break;
										case 2:
											return "免单";
											break;
										case 3:
											return "优惠";
											break;
										case 4:
											return "月结";
											break;
										case 5:
											return "二次采样";
											break;
										case 6:
											return "补样";
											break;
										default:
											return "";
										}
									}
								},
								{
									text : '紧急程度',
									dataIndex : 'urgent_state',
									menuDisabled : true,
									width : 80,
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
								},{
									text : '合作商',
									dataIndex : 'parnter_name',
									menuDisabled : true,
									width : 100
								},{
									text : '案例来源',
									dataIndex : 'source_type',
									menuDisabled : true,
									width : 100,
									renderer : function(value,meta,record) {
										switch (value) {
										case '0':
											return "市场";
											break;
										case '1':
											return "网络";
											break;
										default:
											return "";
										}
									}
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
									items : [ case_code,case_userid,receiver_area,client,phone]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [ source_type,is_delete,verify_state,case_state,confirm_code]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [parnter_name,sample_in_per,starttime,endtime,{
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											} ]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '修改',
										iconCls : 'Pageedit',
										handler : me.onUpdate
									},{
										text : '查看样本信息',
										iconCls : 'Find',
										handler : me.onFind
									}
								]
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
							modal:true,
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
											'id_number', 'birth_date','special' ],// 定义字段
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
									flex : 1.5,
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
								},{
									text : '特殊样本',
									dataIndex : 'special',
									menuDisabled : true,
									width : 80,
									renderer : function(value,meta,record) {
										switch (value) {
										case '0':
											return "否";
											break;
										case '1':
											return "是";
											break;
										default:
											return "";
										}
									}
								} ]
							}) ]
						});
						win.show();
					},
					onUpdate : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
							return;
						}
						ownpersonTemp=selections[0].get("case_userid") ;
						ownaddressTemp=selections[0].get("case_areacode") ;
						var form = Ext.create(
								"Rds.judicial.form.JudicialRegisterChangeForm", {
									region : "center",
									autoScroll : true,
									grid : me
								});
						form.loadRecord(selections[0]);
						var win = Ext.create("Ext.window.Window", {
							title : '案例修改',
							width : 800,
							modal:true,
							iconCls : 'Pageedit',
							height : 600,
							layout : 'border',
							items : [ form ],
							maximized:true,
							maximizable :true
						});
						win.show();
						//form.get("province");
					},
				onSubmit: function () {
					var me = this.up("gridpanel");
					var selections = me.getView().getSelectionModel().getSelection();
					if (selections.length < 1) {
						Ext.Msg.alert("提示", "请选择需要操作的记录!");
						return;
					}

					var taskId='';
					var case_ids='';
					for(var i = 0 ; i < selections.length ; i ++)
					{
						if(selections[i].get("is_delete")==1){
							Ext.Msg.alert("提示", "存在已删除案例，无法提交!");
							return;
						}
//						if(selections[i].get("verify_state")==2){
//							Ext.Msg.alert("提示", "存在已提交审核案例，无法重复提交!");
//							return;
//						}
						if(selections[i].get("verify_state")==3){
							Ext.Msg.alert("提示", "存在已审核案例，无法重复提交!");
							return;
						}
						if (selections[i].get("task_def_key") != "taskRegister") {
							Ext.Msg.alert("提示", "该状态不允许提交审核!");
							return;
						}
					}
					for(var i = 0 ; i < selections.length ; i ++)
					{
						if((i+1)==selections.length)
							{
								taskId +=  selections[i].get("task_id");
								case_ids += selections[i].get("case_id")
							}
						else
							{

								taskId += selections[i].get("task_id")+",";
								case_ids += selections[i].get("case_id")+",";
							}
					}
					
//					console.log(taskId)
					
					var values = {
						taskId: taskId
					};
//					console.log(values.taskId);
					if (values.taskId == null || values.taskId == "") {
						Ext.Msg.alert("提示", "该记录不能进行此项操作!");
						return;
					}
					Ext.MessageBox.wait('正在操作','请稍后...');
					Ext.Ajax.request({
						url: 'judicial/register/getCaseSubmit.do',
						method: "POST",
						headers: {
							'Content-Type': 'application/json'
						},
						jsonData: {
							'case_id': case_ids
						},
						success: function (response, options) {
							response = Ext.JSON.decode(response.responseText);
//							console.log(response);
							if (!response.result) {
								Ext.Msg.alert("提示", response.message);
							} else {
								Ext.Ajax.request({
									url: "judicial/register/updateCaseVerifyState.do",
									method: "POST",
									async: false,
									headers: {'Content-Type': 'application/json'},
									jsonData: {case_id:case_ids},
									success: function (response, options) {
										response = Ext.JSON.decode(response.responseText);
										if (!response) {
											Ext.MessageBox.alert("错误信息", "提交审核出错，请联系管理员！");
											return;
										} 
									},
									failure: function () {
										Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
									}
								});
								Ext.Ajax.request({
									url: "activiti/main/claimAndComplete.do",
									method: "POST",
									async: false,
									headers: {'Content-Type': 'application/json'},
									jsonData: values,
									success: function (response, options) {
										response = Ext.JSON.decode(response.responseText);
//										console.log(response);
										if (response.result == true) {
											Ext.MessageBox.alert("提示信息", response.message);
											me.getStore().load();
											// me.up("window").close();
										} else {
											Ext.MessageBox.alert("错误信息", response.message);
											me.getStore().load();
										}
									},
									failure: function () {
										Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
									}
								});
							}
						},
						failure: function () {
							Ext.Msg.alert("提示", "发生错误，请联系管理员!");
						}
					});
				},
				onTaskHistory: function () {
					var me = this.up("gridpanel");
					var selections = me.getView().getSelectionModel().getSelection();
					if (selections.length < 1) {
						Ext.Msg.alert("提示", "请选择需要操作的记录!");
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
//							console.log(response);
							var win = Ext.create("Ext.window.Window", {
								title: '流程步骤',
								width: 700,
								iconCls: 'Add',
								layout: 'fit',
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
											menuDisabled: true
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
//										sorters: [{
////											property: 'id',
////											direction: 'DESC'
//										}],
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
					onSearch : function() {
						var me = this.up("gridpanel");
						me.getStore().currentPage=1;
						me.getStore().load();
					},
//					onExport : function() {
//						var me = this.up("gridpanel");
//						var params = me.store.proxy.extraParams;
//						
//						window.location.href = "judicial/register/exportCaseInfo.do?case_code="
//								+ params["case_code"]
//								+ "&receiver="
//								+ params["receiver"]
//								+ "&starttime="
//								+ params["starttime"]
//								+ "&endtime="
//								+ params["endtime"]
//								+ "&urgent_state="
//								+ params["urgent_state"]
//								+ "&is_delete="
//								+ params["is_delete"];
//					},
					listeners : {
						'afterrender' : function() {
							this.store.load();
						},
						'cellclick': function (grid, td, cellIndex, record, tr, rowIndex, e) {
							//查看审核不通过原因
							if (e.getTarget('.lbtnComment')) {
								Ext.create("Ext.window.Window", {
									title: '审核不通过原因',
									width: 500,
									height: 250,
									modal: true,
									layout: 'fit',
									items: [{
										xtype: "gridpanel",
										columns: [
											{
												header: "审核时间",
												dataIndex: "timeString",
												align: 'center',
												width: 150,
												sortable: false,
												menuDisabled: true
											},
											{
												header: "审核人",
												dataIndex: "userId",
												align: 'center',
												width: 100,
												sortable: false,
												menuDisabled: true
											},
											{
												header: "审核意见",
												dataIndex: "fullMessage",
												align: 'center',
												flex: 1,
												sortable: false,
												menuDisabled: true
											}
										],
										store: Ext.create('Ext.data.Store', {
											fields: ['fullMessage', 'userId', 'time', {
												name: 'timeString', type: 'date',
												convert: function (v, rec) {
													return rec.data.time == null ? "" : Ext.Date.format(new Date(rec.data.time), 'Y-m-d H:i');
												}
											}],
											autoLoad: true,
											proxy: {
												type: 'jsonajax',
												actionMethods: {
													read: 'POST'
												},
												url: 'activiti/main/getProcessInstanceComments.do',
												params: {"processInstanceId": record.data.process_instance_id},
												reader: {
													type: 'json'
												}
											}
										})
									}]
								}).show();
							}
						}
					}
				});