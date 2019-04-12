Ext
		.define(
				"Rds.judicial.panel.JudicialCaseRelayGridPanel",
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
						var relay_code = Ext.create('Ext.form.field.Text', {
							name : 'relay_code',
							labelWidth : 60,
							width : 200,
							regex : /^\w*$/,
							regexText : '请输入交接单号',
							fieldLabel : '交接单号'
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
						var is_delete=Ext.create('Ext.form.ComboBox', 
								{
									fieldLabel : '是否删除',
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
																'未删除',
																0 ],
														[
																'已删除',
																1 ] ]
											}),
									value : -1,
									mode : 'local',
									// typeAhead: true,
									name : 'is_delete',
								});
						var confirm_state=Ext.create('Ext.form.ComboBox', 
								{
									fieldLabel : '确认状态',
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
																'未确认',
																0 ],
														[
																'已确认',
																1 ] ]
											}),
									value : -1,
									mode : 'local',
									// typeAhead: true,
									name : 'confirm_state',
								});
						me.store = Ext
								.create(
										'Ext.data.Store',
										{
											fields : [ "relay_id","confirm_time","confirm_remark","confirm_pername","relay_per",'relay_code',"confirm_state","relay_pername","relay_remark","relay_time","is_delete"
											           ,"relay_check" ,"relay_checktwo","relay_Gluing","relay_Seal","relay_split"],
											start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/caseRelay/getCaseRelayInfo.do',
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
																	.getValue()),
															is_delete:is_delete.getValue(),
															confirm_state:confirm_state.getValue(),
															relay_code:relay_code.getValue()
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
									text : '交接单号',
									dataIndex : 'relay_code',
									menuDisabled : true,
									width : 125,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var is_delete= record.data["is_delete"];
										if (is_delete == 1) {
											return "<div style=\"text-decoration: line-through;color: red;\">"
													+ value + "</div>";
										} else {
											return value;
										}
									}
								},
								{
									text : '交接人',
									dataIndex : 'relay_pername',
									menuDisabled : true,
									width : 80
								},
								{
									text : '交接时间',
									dataIndex : 'relay_time',
									menuDisabled : true,
									width : 140
								},
								{
									text : '交接备注',
									dataIndex : 'relay_remark',
									menuDisabled : true,
									width:100
								},
								{
									text : '核对报告人员1',
									dataIndex : 'relay_check',
									menuDisabled : true,
									width:100
								},{
									text : '核对报告人员2',
									dataIndex : 'relay_checktwo',
									menuDisabled : true,
									width:100
								},
								{
									text : '胶装人员',
									dataIndex : 'relay_Gluing',
									menuDisabled : true,
									width:70
								},
								{
									text : '盖章人员',
									dataIndex : 'relay_Seal',
									menuDisabled : true,
									width:70
								},
								{
									text : '拆分人员',
									dataIndex : 'relay_split',
									menuDisabled : true,
									width:70
								},
								{
									text : '确认状态',
									dataIndex : 'confirm_state',
									menuDisabled : true,
									width:70,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										if (value == 1) {
											return "<div style=\"color: green;\">已确认</div>";
										} else {
											return "未确认";
										}
									}
								},
								{
									text : '确认人',
									dataIndex : 'confirm_pername',
									menuDisabled : true,
									width:80
								},
								{
									text : '确认时间',
									dataIndex : 'confirm_time',
									menuDisabled : true,
									width:140
								},
								{
									text : '确认备注',
									dataIndex : 'confirm_remark',
									menuDisabled : true,
									width:100
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [
											 relay_code,starttime,endtime,confirm_state,
											 {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											}]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [{
										text : '查看案例',
										iconCls : 'Find',
										handler : me.onFind
									},{
										text : '添加报告单号',
										iconCls : 'Pageadd',
										handler : me.onInsert
									},{
										text : '生成单号打印',
										iconCls : 'Printer',
										handler : me.onPrint
									},{
										text : '导出单号',
										iconCls : 'Pageexcel',
										handler : me.onCaseExport
									},{
										text : '修改报告单号',
										iconCls : 'Pageedit',
										handler : me.onUpdate,
										hidden:true
									},{
										text : '删除',
										iconCls : 'Delete',
										handler : me.onDelete,
										hidden:true
									}
									]
								} ];

						me.callParent(arguments);
					},
					onPrint:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要打印的记录!");
							return;
						};
						var relay_id=selections[0].get("relay_id");
						printRelay(relay_id);
					},
					onCaseExport:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要导出单号的记录!");
							return;
						};
						var relay_id=selections[0].get("relay_id");
						window.location.href = "judicial/caseRelay/exportCaseCode.do?relay_id="+relay_id;
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
							title : "案例信息",
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
									fields : [ 'case_code','confirm_state','case_id'],// 定义字段
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'judicial/caseRelay/getRelayCaseInfo.do',
										params : {
											'relay_id' : selections[0].get("relay_id")
										},
										reader : {
											type : 'json',
											reader:'array'
										}
									},
									autoLoad : true
								// 自动加载
								},
								columns : [// 配置表格列
								{
									header : "案例条形码",
									dataIndex : 'case_code',
									flex : 1,
									menuDisabled : true
								},{
									header : "报告审核状态",
									dataIndex : 'confirm_state',
									flex : 1,
									menuDisabled : true,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										if (value == 1) {
											return "<div style=\"color: green;\">案例已确认</div>";
										}else if (value == 2) {
											return "<div style=\"color: red;\">案例确认未通过</div>";
										} else {
											return "案例未确认";
										}
									}
								}]
							}) ]
						});
						win.show();
					},
					onInsert :function() {
						var me = this.up("gridpanel");
						var form = Ext.create(
								"Rds.judicial.form.JudicialCaseRelayInsertForm", {
									region : "center",
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '报告单号添加',
							width : 900,
							iconCls : 'Pageedit',
							height : 600,
							modal:true,
							layout : 'border',
							items : [ form ]
						});
						win.show();
					},
					 onDelete : function() {
							var me = this.up("gridpanel");
							me.getStore().load();
							var selections = me.getView().getSelectionModel()
									.getSelection();
							if (selections.length < 1) {
								Ext.Msg.alert("提示", "请选择需要删除的记录!");
								return;
							};
							if (selections[0].get("confirm_state")==1) {
								Ext.Msg.alert("提示", "此次报告已确认!");
								return;
							};
							if(selections[0].get("is_delete")==1){
								Ext.Msg.alert("提示", "此次报告已删除，无法修改!");
								return;
							}
							var values = {
								relay_id : selections[0].get("relay_id")
							};
							Ext.MessageBox
									.confirm(
											'提示',
											'确定删除此次交接吗',
											function(id) {
												if (id == 'yes') {
													Ext.Ajax
															.request({
																url : "judicial/caseRelay/deleteRelayCaseInfo.do",
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
																						"删除成功！");
																		me
																				.getStore()
																				.load();
																	} else {
																		Ext.MessageBox
																				.alert(
																						"错误信息",
																						"删除失败！");
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
					onUpdate : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要修改的记录!");
							return;
						};
						if (selections[0].get("confirm_state")==1) {
							Ext.Msg.alert("提示", "此次报告已确认!");
							return;
						};
						if(selections[0].get("is_delete")==1){
							Ext.Msg.alert("提示", "此次报告已删除，无法修改!");
							return;
						}
						var form = Ext.create(
								"Rds.judicial.form.JudicialCaseRelayUpdateForm", {
									region : "center",
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '报告单号修改',
							width : 900,
							iconCls : 'Pageedit',
							height : 600,
							layout : 'border',
							items : [ form ]
						});
						form.loadRecord(selections[0]);
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
printRelay=function(relay_id){
	var print_chanel=function(){
		win.close();
	}
	var print_print=function(me){
		 var iframe = document.getElementById("relay_paper");
	     iframe.contentWindow.focus();
		 iframe.contentWindow.print();
		 win.close();
	}
	var win=Ext.create("Ext.window.Window",{
		title : "打印预览",
		iconCls : 'Find',
		layout:"auto",
		maximized:true,
		maximizable :true,
		modal:true,
		bodyStyle : "background-color:white;",
		html:"<iframe width=100% height=100% id='relay_paper' src='judicial/caseRelay/printCaseRelay.do?relay_id="+relay_id+"'></iframe>",
		buttons:[
					 {
							text : '打印',
							iconCls : 'Disk',
							handler:  print_print
					}, {
							text : '取消',
							iconCls : 'Cancel',
							handler: print_chanel
						} 
				]
			});
	win.show();
}