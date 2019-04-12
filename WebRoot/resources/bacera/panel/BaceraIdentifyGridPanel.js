var identify="";
/**
 * 案例登记列表
 * 
 * @author 
 */
Ext.define("Rds.bacera.panel.BaceraIdentifyGridPanel",{
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
						var agent = Ext.create('Ext.form.field.Text',{
							name:'agent',
							labelWidth:60,
							width:150,
							fieldLabel:'被代理人'
						});
						var case_in_per = Ext.create('Ext.form.field.Text',{
							name:'case_in_per',
							labelWidth:60,
							width:150,
							fieldLabel:'登记人'
						});
						var samplename =  Ext.create('Ext.form.field.Text',{
							name:'samplename',
							labelWidth:60,
							width:150,
							fieldLabel:'样本姓名'
						});
						var type =  new Ext.form.field.ComboBox({
							fieldLabel : '案例类型',
							width : 230,
							labelWidth : 70,
							editable : true,
							triggerAction : 'all',
							displayField : 'Name',
							labelAlign : 'left',
							valueField : 'Code',
							store : new Ext.data.ArrayStore(
								{
									fields : [
											'Name',
											'Code' ],
									data : [
											[
													'亲子鉴定-宿迁子渊',
													'亲子鉴定-宿迁子渊' ],
											[
													'亲子鉴定-东南',
													'亲子鉴定-东南' ],
											[
													'亲子鉴定-苏博医学',
													'亲子鉴定-苏博医学' ],
											[
													'河南唯实',
													'河南唯实' ],
											[
													'广西公众司法',
													'广西公众司法' ],
											[
													'安徽同德',
													'安徽同德' ],
											[
													'亲子鉴定-安徽龙图',
													'亲子鉴定-安徽龙图' ],
											[
													'亲子鉴定-求实',
													'亲子鉴定-求实' ],
											[
													'亲子鉴定-十堰',
													'亲子鉴定-十堰' ],
											[
													'亲子鉴定-福建正泰',
													'亲子鉴定-福建正泰' ],
											[
													'亲子鉴定-杭州千麦',
													'亲子鉴定-杭州千麦' ],
											[
													'亲子鉴定-南天',
													'亲子鉴定-南天' ],
											[
													'亲子鉴定-商丘',
													'亲子鉴定-商丘' ],
											[
													'亲子鉴定-中信',
													'亲子鉴定-中信' ]
											]
									}),
							mode : 'local',
							name : 'type',
							value:(usercode=='sq_wangyan')?'亲子鉴定-商丘':'',
									readOnly:(usercode=='sq_wangyan')?true:false,
						});
						var client = Ext.create('Ext.form.field.Text',{
							name:'client',
							labelWidth:60,
							width:140,
							fieldLabel:'委托人'
						});
						var reportif=new Ext.form.field.ComboBox({
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
							name : 'reportif',
							value: 0
						});
						me.store = Ext.create('Ext.data.Store',
										{
											fields : [ 'case_id', 'case_code',
													'case_areacode',
													'receiver_area',
													'case_receiver','remark',
													'accept_time','close_time','client'
													,'address','case_in_per','receiver_id',
													'case_in_pername','phone',
													'sample_in_time', 'case_id','is_delete',
													'reportif','agentname',
													'expresstype','receivables','type',
													'entrustment_time','entrustment_matter',
													'case_type','typeid'
													],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'bacera/identify/getCaseInfo.do',
												params : {
//													start : 0,
//													limit : 25
												}, 
												reader : {
													 type: 'json',
									                    root:'data',
									                    totalProperty:'total'
												}
											},
											listeners : {
												'beforeload' : function(ds,operation, opt) {
													me.getSelectionModel().clearSelections();
													identify = "case_code=" + trim(Ext.getCmp('case_code_query1').getValue()) + "&receiver="
															+ trim(Ext.getCmp('receiver_query1').getValue()) + "&is_delete="
															+ Ext.getCmp('is_delete_query1').getValue() + "&starttime="
															+ dateformat(Ext.getCmp('starttime_query1').getValue()) + "&endtime="
															+ dateformat(Ext.getCmp('endtime_query1').getValue())+ "&reportif=" 
															+ reportif.getValue() + "&agent=" + agent.getValue() + "&case_in_per="
															+ case_in_per.getValue()+"&type="
															+ type.getValue()+"&samplename="
															+ samplename.getValue() +"&client="
															+ trim(client.getValue())+"&usercode="
															+ usercode;
													Ext.apply(me.store.proxy.extraParams,{								
																endtime : dateformat(Ext.getCmp('endtime_query1').getValue()),	
																starttime : dateformat(Ext.getCmp('starttime_query1').getValue()),
																receiver : trim(Ext.getCmp('receiver_query1').getValue()),
																case_code : trim(Ext.getCmp('case_code_query1').getValue()),
																is_delete : Ext.getCmp('is_delete_query1').getValue(),
																agent:agent.getValue(),
																case_in_per:case_in_per.getValue(),
																reportif:reportif.getValue(),
																type:type.getValue(),
																samplename:samplename.getValue(),
																client:trim(client.getValue())
																});
												}
											}
										});
						me.selModel = Ext.create('Ext.selection.CheckboxModel',{
							mode: 'SINGLE'
						});
						me.bbar = Ext.create('Ext.PagingToolbar', {
				            store: me.store,
				            pageSize:me.pageSize,
				            displayInfo: true,
				            displayMsg : "第 {0} - {1} 条  共 {2} 条",
					   	 	emptyMsg : "没有符合条件的记录"
				        });
						me.columns = [{
									text : '案例条形码',
									dataIndex : 'case_code',
									menuDisabled : true,
									width : 100,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										if (record.data["is_delete"] == 1) {
											return "<div style=\"text-decoration: line-through;color: red;\">"
													+ value + "</div>"
										} else {
											return value;
										}
									}
								},{
									text : '受理日期',
									dataIndex : 'accept_time',
									menuDisabled : true,
									width : 100
								},{
									text : '委托人',
									dataIndex : 'client',
									menuDisabled : true,
									width : 120
								},{
									text : '归属人',
									dataIndex : 'case_receiver',
									menuDisabled : true,
									width : 120
								},{
									text : '被代理人',
									dataIndex : 'agentname',
									menuDisabled : true,
									width : 120
								},{
									text : '样本登记日期',
									dataIndex : 'sample_in_time',
									menuDisabled : true,
									width : 150
								},{
									text : '案例归属地',
									dataIndex : 'receiver_area',
									menuDisabled : true,
									width : 200
								},{ 
									text: '是否发报告',	dataIndex: 'reportif', menuDisabled:true, width : 80,
					            	  renderer : function(value) {
											switch (value) {
											case "1":
												return "是";
												break;
											default:
												return "<span style='color:red'>否</span>";
											}
										}
								},{
									text : '案例类型',
									dataIndex : 'type',
									menuDisabled : true,
									width : 150
								},{
									text : '身份证地址',
									dataIndex : 'case_areacode',
									width : 200,
									menuDisabled : true
								},{
									text : '电话号码',
									dataIndex : 'phone',
									menuDisabled : true,
									width : 120
								},{
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
												id : 'case_code_query1',
												labelWidth : 70,
												width : 155,
												fieldLabel : '案例条形码'
											},
											{
												xtype : 'textfield',
												fieldLabel : '归属人',
												width : 145,
												labelWidth : 50,
												id : 'receiver_query1',
												name : 'receiver'
											},agent,
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
																					-1 ],
																			[
																					'未废除',
																					0 ],
																			[
																					'已废除',
																					1 ] ]
																}),
														value : -1,
														mode : 'local',
														// typeAhead: true,
														name : 'is_delete',
														id : 'is_delete_query1'
													}),
											{
												xtype : 'datefield',
												name : 'starttime',
												id : 'starttime_query1',
												width : 175,
												fieldLabel : '受理时间 从',
												labelWidth : 70,
												labelAlign : 'right',
												emptyText : '请选择日期',
												format : 'Y-m-d',
												value : Ext.Date.add(
														new Date(),
														Ext.Date.DAY,-7),
												maxValue : new Date(),
												listeners : {
													'select' : function() {
														var start = Ext.getCmp(
																'starttime_query1')
																.getValue();
														Ext.getCmp('endtime_query1')
																.setMinValue(
																		start);
														var endDate = Ext
																.getCmp(
																		'endtime_query1')
																.getValue();
														if (start > endDate) {
															Ext
																	.getCmp(
																			'endtime_query1')
																	.setValue(
																			start);
														}
													}
												}
											},
											{
												xtype : 'datefield',
												id : 'endtime_query1',
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
																'starttime_query1')
																.getValue();
														var endDate = Ext
																.getCmp(
																		'endtime_query1')
																.getValue();
														if (start > endDate) {
															Ext
																	.getCmp(
																			'starttime_query1')
																	.setValue(
																			endDate);
														}
													}
												}
											}]
								}, {
							 		style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '1px !important'
								 		},
								 		xtype:'toolbar',
								 		name:'search',
								 		dock:'top',
								 		items:[samplename,client,case_in_per,type,reportif,{
							            	text:'查询',
							            	iconCls:'Find',
							            	handler:me.onSearch
							            }]
								 	},{
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '查看样本信息',
										iconCls : 'Find',
										handler : me.onFind
									}, {
										text : '新增',
										iconCls : 'Add',
										handler : me.onInsert
									}, {
										text : '修改',
										iconCls : 'Pageedit',
										handler : me.onUpdate
									}, {
										text : '作废',
										iconCls : 'Cancel',
										handler : me.onCancel
									},{
										text : '删除',
										iconCls : 'Delete',
										handler : me.onDelete
									},{
							 			text:'导出',
							 			iconCls:'Application',
							 			handler:me.identifyInfoExport
							 		}
									]
								} ];

						me.callParent(arguments);
					},
					identifyInfoExport:function(){
						window.location.href = "bacera/identify/exportIdentifyInfo.do?"+identify ;
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
										url : 'bacera/identify/getSampleInfo.do',
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
					onCancel : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要作废的记录!");
							return;
						};
						if(selections[0].get("is_delete")!=0){
							Ext.Msg.alert("提示", "此案例已废除!");
							return;
						}
						var values = {
							case_id : selections[0].get("case_id"),
							flag:1
						};
						Ext.MessageBox.confirm(
										'提示',
										'确定作废此案例吗',
										function(id) {
											if (id == 'yes') {
												Ext.MessageBox.wait('正在保存','请稍后...');
												Ext.Ajax
														.request({
															url : "bacera/identify/deleteCaseInfo.do",
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
										},{
											xtype:'panel',
											region:"center",
											border : false
										});
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
							case_id : selections[0].get("case_id"),
							flag:2
						};
						Ext.MessageBox
								.confirm(
										'提示',
										'确定删除此案例吗？删除了就回不来了！<br>需要作废请重新选择按钮',
										function(id) {
											if (id == 'yes') {
												Ext.MessageBox.wait('正在保存','请稍后...');
												Ext.Ajax
														.request({
															url : "bacera/identify/deleteCaseInfo.do",
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
										},{
											xtype:'panel',
											region:"center",
											border : false
										});
					},
					onInsert : function() {
						var me = this.up("gridpanel");
						ownpersonTemp = "";
						var form = Ext.create(
								"Rds.bacera.form.BaceraIdentifyForm",
								{
									region : "center",
									autoScroll : true,
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '亲子鉴定-新增',
							width : 650,
							iconCls:'Add',
							height : 550,
							modal:true,
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
							Ext.Msg.alert("提示", "此案例已废除，无法修改!");
							return;
						}
						ownpersonTemp = selections[0].get("receiver_id");
						var form = Ext.create("Rds.bacera.form.BaceraIdentifyUpdateForm", {
									region : "center",
									autoScroll : true,
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '案例修改',
							width : 650,
							iconCls : 'Pageedit',
							height : 550,
							modal:true,
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
