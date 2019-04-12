Ext
		.define(
				"Rds.judicial.panel.JudicialRegisterGridPanel",
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
						var receiver = Ext.create('Ext.form.field.Text', {
							name : 'receiver',
							labelWidth : 60,
							width : 145,
							fieldLabel : '归属人'
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
						var urgent_state=Ext.create('Ext.form.ComboBox', 
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
								});
						    var is_delete=Ext.create('Ext.form.ComboBox', 
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
																	-1 ],
															[
																	'未废除',
																	0 ],
															[
																	'已废除',
																	1 ],
																	[
																		'案例变动',
																		2 ]  ]
												}),
										value : -1,
										mode : 'local',
										// typeAhead: true,
										name : 'is_delete',
									});
						me.store = Ext
								.create(
										'Ext.data.Store',
										{
											fields : [ 'case_id', 'case_code',
													'case_areaname',
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
													'sample_in_time', 'case_id','is_delete',"attach_need_case",
													'sample_in_per','unit_type','sample_relation','case_type'
													],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/register/getCaseInfo.do',
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
														    urgent_state :urgent_state.getValue(),
															starttime : dateformat(starttime
																	.getValue()),
															receiver :trim(receiver.getValue()),
															case_code : case_code.getValue(),
															is_delete :is_delete
																	.getValue()});
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
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var isnull= record.data["is_delete"];
										if (isnull == 1) {
											return "<div style=\"text-decoration: line-through;color: red;\">"
													+ value + "</div>"
										} else if(isnull == 2) {
											return "<div style=\"color: yellow;\">"
											+ value + "</div>"
										}else
										{
											return value;
										}

									}
								},
								{
									text : '身份证地址',
									dataIndex : 'case_areaname',
									width : 200,
									menuDisabled : true
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
											break;
										case 1:
											return "<span style='color:red'>案例审核未通过</span>";
											break;
										case 2:
											return "<span style='color:green'>案例审核通过</span>,样本未审核";
											break;
										case 3:
											return "<span style='color:green'>样本审核通过</span>";
											break;
										case 4:
											return "<span style='color:red'>样本审核未通过，已废除</span>";
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
									text : '是否需要副本',
									dataIndex : 'attach_need_case',
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
											 case_code,receiver,urgent_state,is_delete,starttime,endtime,
											 {
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
										text : '新增',
										iconCls : 'Pageadd',
										handler : me.onInsert
									}, {
										text : '修改',
										iconCls : 'Pageedit',
										handler : me.onUpdate
									}, {
										text : '废除',
										iconCls : 'Delete',
										handler : me.onDelete
									}
//									, {
//										text : '导出样本信息',
//										iconCls : 'Pageexcel',
//										handler : me.onSampleExport
//									}
									]
								} ];

						me.callParent(arguments);
					},
					onSampleExport : function(){
						var me = this.up("gridpanel");
						var params = me.store.proxy.extraParams;
						window.location.href = "judicial/register/exportSampleInfo.do?case_code="
								+ params["case_code"]
								+ "&receiver="
								+ params["receiver"]
								+ "&starttime="
								+ params["starttime"]
								+ "&endtime="
								+ params["endtime"]
								+ "&urgent_state="
								+ params["urgent_state"]
								+ "&is_delete="
								+ params["is_delete"];
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
						if(selections[0].get("verify_state")==2||selections[0].get("verify_state")==3){
							Ext.Msg.alert("提示", "此案例已审核，无法删除!");
							return;
						}
						if(selections[0].get("is_delete")!=0){
							Ext.Msg.alert("提示", "此案例已废除!");
							return;
						}
						var values = {
							case_id : selections[0].get("case_id")
						};
						Ext.MessageBox
								.confirm(
										'提示',
										'确定删除此案例吗',
										function(id) {
											if (id == 'yes') {
												Ext.Ajax
														.request({
															url : "judicial/register/deleteCaseInfo.do",
															method : "POST",
															headers : {
																'Content-Type' : 'application/json'
															},
															jsonData : values,
															success : function(
																	response,
																	options) {
																response = Ext.JSON
																		.decode(response.responseText);
																if (response == true) {
																	Ext.MessageBox
																			.alert(
																					"提示信息",
																					"废除成功！");
																	me
																			.getStore()
																			.load();
																} else {
																	Ext.MessageBox
																			.alert(
																					"错误信息",
																					"废除失败！");
																}
															},
															failure : function() {
																Ext.Msg
																		.alert(
																				"提示",
																				"网络故障<br>请联系管理员!");
															}
														});
											}
										});
					},
					onInsert : function() {
						var me = this.up("gridpanel");
						ownpersonTemp="";
						ownaddressTemp="";
						var form = Ext.create(
								"Rds.judicial.form.JudicialRegisterInsertForm",
								{
									region : "center",
									autoScroll : true,
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '案例添加',
							width : 900,
							iconCls : 'Pageadd',
							height : 550,
							layout : 'border',
							items : [ form ]
						});
						win.show();
					},
					onUpdate : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要修改的记录!");
							return;
						}
						if(selections[0].get("is_delete")==1){
							Ext.Msg.alert("提示", "此案例已删除，无法修改!");
							return;
						}
						if(selections[0].get("verify_state")==2||selections[0].get("verify_state")==3){
							Ext.Msg.alert("提示", "此案例已审核，无法修改!");
							return;
						}
						ownpersonTemp=selections[0].get("receiver_id") ;
						ownaddressTemp=selections[0].get("case_areacode") ;
						var form = Ext.create(
								"Rds.judicial.form.JudicialRegisterUpdateForm", {
									region : "center",
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '案例修改',
							width : 760,
							iconCls : 'Pageedit',
							height : 550,
							layout : 'border',
							items : [ form ],
							maximized:true,
							maximizable :false
						});
						form.loadRecord(selections[0]);
						win.show();
						//form.get("province");
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
						}
					}
				});
