mail_canel = function(me) {
	me.up("window").close();
}
case_canel = function(me) {
	me.up("window").close();
}
var mailStore = Ext.create('Ext.data.Store', {
	fields:['key','value'],
	proxy : {
		type : 'jsonajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'judicial/dicvalues/getMailModels.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	autoLoad : true,
	remoteSort : true
});

queryMailInfo = function(value, mail_type) {
	var win = Ext.create("Ext.window.Window", {
				title : "快递信息（快递单号：" + value + "）",
				width : 700,
				iconCls : 'Find',
				height : 350,
				layout : 'border',
				modal:true,
				border : false,
				bodyStyle : "background-color:white;",
				items : [Ext.create('Ext.grid.Panel', {
									width : 700,
									height : 350,
									frame : false,
									renderTo : Ext.getBody(),
									viewConfig : {
										forceFit : true,
										stripeRows : true
										,// 在表格中显示斑马线
									},
									store : {// 配置数据源
										fields : ['time', 'content'],// 定义字段
										proxy : {
											type : 'jsonajax',
											actionMethods : {
												read : 'POST'
											},
											url : 'judicial/mail/getMailInfo.do',
											params : {
												'mail_code' : value,
												'mail_type' : mail_type
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
												header : "时间",
												dataIndex : 'time',
												flex : 1.5,
												menuDisabled : true
											}, {
												header : "地点",
												dataIndex : 'content',
												flex : 3,
												menuDisabled : true
											}]
								})]
			});
	win.show();
}

Ext.define("Rds.judicial.panel.JudicialRegisterMedicalGridPanel",{
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
						var case_userid = Ext.create('Ext.form.field.Text', {
							name : 'case_userid',
							labelWidth : 60,
							width : '20%',
							emptyText : '请输入归属人',
							fieldLabel : '归属人'
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
														['实体渠道','0' ],
														['电子渠道','1']]
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
											fields : [ 'case_id', 'case_code','case_userid',
													'case_areacode','typeid', 'receiver_area', 'case_receiver',
													'urgent_state',  'remark',  'print_count','verify_state',
													'accept_time','consignment_time','close_time', 'report_modelname','client',
													'report_model','address', 'case_in_per', 'case_in_pername','phone',
													'sample_in_time','is_delete', 'sample_in_per','unit_type','sample_relation',
													'case_type','agent','copies','parnter_name','case_state','source_type',
													'process_instance_id', 'task_id', 'task_def_key', 'task_name', 'suspension_state','has_comment'
													],
											start:0,
											limit:15,
											pageSize:15,
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
																phone:trim(phone.getValue()),
																//默认为司法亲子鉴定案例 医学
																case_type:'1'
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
//											if(record.get("task_def_key")=="taskAudit"){
//												return "已提交审核";
//											}else{
//												return "未审核";
//											}
											break;
										case 1:
											if(record.get("task_def_key")=="taskAudit"){
												return "已提交审核";
											}else{
												return "<span style='color:red'>案例审核未通过</span><a class='lbtnComment' href='#'>查看原因</a>";
											}
											break;
										case 2:
											 if(record.get("task_def_key")=="taskAudit"){
				                                    return "已提交审核";
				                                }else{
				                                    return "<span style='color:red'>案例审核未通过</span><a class='lbtnComment' href='#'>查看原因</a>";
				                                }
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
											return "<span style='color:red'>48小时</span>";
											break;
										case 2:
											return "<span style='color:red'>24小时</span>";
											break;
										case 3:
											return "<span style='color:red'>8小时</span>";
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
											return "实体渠道";
											break;
										case '1':
											return "电子渠道";
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
										text : '新增',
										iconCls : 'Pageadd',
										handler : me.onInsert
									}, {
										text : '修改',
										iconCls : 'Pageedit',
										handler : me.onUpdate
									}, {
										text : '二次采样',
										iconCls : 'Add',
										handler : me.onSecondCase
									} ,{
										text : '补样',
										iconCls : 'Add',
										handler : me.onCaseFill
									} ,{
										text : '废除',
										iconCls : 'Delete',
										handler : me.onDelete,
//										hidden:usercode=='admin'?false:true
									}, {
										text : '其他新增',
										iconCls : 'Pageadd',
										handler : me.onInsertOther
									},{
										text: '补报告',
										iconCls: 'Add',
										handler: me.onFillReport
									},{
										text: '改报告',
										iconCls: 'Pageedit',
										handler: me.onChangeReport
									},{
										text: '报告邮寄查询',
										iconCls: 'Find',
										handler: me.onReportMail
									},{
										text: '优惠码插入',
										iconCls: 'Add',
										handler: me.confirmCode
									},{
										text: '汇款生成',
										iconCls: 'User',
										handler: me.createFinanceDaily
									}
								]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '采样快递插入',
										iconCls : 'Add',
										handler : me.onExpressAdd
									}, {
										text : '采样快递查看',
										iconCls : 'Find',
										handler : me.onFindExpress
									} ,{
							 			text:'案例照片管理',
							 			iconCls:'Cog',
							 			handler:me.attachmentManage
							 		},
									{
										text: '提交审核',
										iconCls: 'Pageedit',
										handler: me.onSubmit
									}, {
										text : '查看样本信息',
										iconCls : 'Find',
										handler : me.onFind
									}, {
										text : '查看案例关联',
										iconCls : 'Find',
										handler : me.onFindCase
									},
									{
										text: '查看流程状态',
										iconCls: 'Pageedit',
										handler: me.onTaskHistory
									},{
										text : '添加备注',
										iconCls : 'Pageedit',
										handler : me.onRemark
									}
								]
								
								} ];

						me.callParent(arguments);
					},
					onRemark:function(){
						var me = this.up("gridpanel");
						var selections =  me.getView().getSelectionModel().getSelection();
						if(selections.length<1 || selections.length>1 ){
							Ext.Msg.alert("提示", "请选择一条需要备注的记录!");
							return;
						};
						var case_id=selections[0].get("case_id");

						remark_confirm = function(mei) {
							var form = mei.up("form").getForm();
							var values = form.getValues();
							values["case_id"]=case_id;
							if (form.isValid()) {
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
											url : "judicial/verify/updateCaseRemark.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : values,
											success : function(response, options) {						
												response = Ext.JSON.decode(response.responseText);
												if (response) {
													Ext.MessageBox.alert("提示信息", response.message);
													var grid = me.up("gridpanel");
													me.getStore().load();
													remark_add.close();
												} else {
													Ext.MessageBox.alert("错误信息", response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
											}
										});
							}
						}
						
						var remark_add = Ext.create("Ext.window.Window", {
							title : '备注信息',
							width : 350,
							height : 250,
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
											handler : remark_confirm
										}, {
											text : '取消',
											iconCls : 'Cancel',
											handler : case_canel
										}],
								items : [{
									xtype : "textarea",
									fieldLabel : '备注',
									labelAlign : 'right',
									maxLength : 200,
									labelWidth : 80,
									height:130,
									allowBlank : false,
//									regex:/^[^\s]*$/,
//									regexText:'我不喜欢空格',
									name : 'remark',
									value:selections[0].get("confirm_remark")
								},{
									xtype : "hidden",
									name : 'remark_bak',
									value:selections[0].get("remark")
								}]
							}]
						})
						remark_add.show();

					},
					createFinanceDaily:function(){
						var me = this.up("gridpanel");
						var selections =  me.getView().getSelectionModel().getSelection();
						if(selections.length<1 || selections.length>1 ){
							Ext.Msg.alert("提示", "请选择一条需要生成的记录!");
							return;
						}
						var case_id=selections[0].get("case_id");
						var client=selections[0].get("client");
						Ext.Msg.show({
							title : '提示(委托人：'+client+')',
							msg : '确定生成该案例汇款?',
							width : 300,
							buttons : Ext.Msg.OKCANCEL,
							fn : function(buttonId, text, opt) {
								if (buttonId == 'ok') {
									Ext.MessageBox.wait('正在操作','请稍后...');
									Ext.Ajax.request({
												url : "judicial/finance/createFinanceDaily.do",
												method : "POST",
												headers : {
													'Content-Type' : 'application/json'
												},
												jsonData : {"case_id":case_id},
												success : function(response, options) {
													response = Ext.JSON
															.decode(response.responseText);
													console.log(response.result);
													if (response.result) {
														Ext.MessageBox.alert("提示信息", response.message);
													} else {
														Ext.MessageBox.alert("错误信息", response.message);
													}
												},
												failure : function() {
													Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
												}
											});
								} else {
									return;
								}

							},
							animateTarget : 'addAddressBtn',
							icon : Ext.window.MessageBox.INFO
						})
					},
					confirmCode:function(){
						var me = this.up("gridpanel");
						var selections =  me.getView().getSelectionModel().getSelection();
						if(selections.length<1 || selections.length>1 ){
							Ext.Msg.alert("提示", "请选择一条需要优惠的记录!");
							return;
						};
						var case_id=selections[0].get("case_id");
						var case_code=selections[0].get("case_code");
						var accept_time=selections[0].get("accept_time");
						confirmCode_confirm = function(mei) {
							var form = mei.up("form").getForm();
							var values = form.getValues();
							values["case_id"]=case_id;
							values["case_code"]=case_code;
							values["case_type"]='dna';
							values["accept_time"]=accept_time;
							values["finance_type"]="亲子鉴定-医学";
							if (form.isValid()) {
								Ext.Msg.show({
									title : '提示',
									msg : '请核实确定插入?',
									width : 300,
									buttons : Ext.Msg.OKCANCEL,
									fn : function(buttonId, text, opt) {
										if (buttonId == 'ok') {
											Ext.MessageBox.wait('正在操作','请稍后...');
											Ext.Ajax.request({
														url : "finance/casefinance/confirmCodeAdd.do",
														method : "POST",
														headers : {
															'Content-Type' : 'application/json'
														},
														jsonData : values,
														success : function(response, options) {
															response = Ext.JSON
																	.decode(response.responseText);
															console.log(response.result);
															if (response.result) {
																Ext.MessageBox.alert("提示信息", response.message);
																var grid = me.up("gridpanel");
																me.getStore().load();
																confirmCodeadd.close();
															} else {
																Ext.MessageBox.alert("错误信息", response.message);
															}
														},
														failure : function() {
															Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
														}
													});
										} else {
											return;
										}

									},
									animateTarget : 'addAddressBtn',
									icon : Ext.window.MessageBox.INFO
								})
							}
						}
						
						var confirmCodeadd = Ext.create("Ext.window.Window", {
							title : '优惠码信息('+case_code+')',
							width : 350,
							height : 200,
							iconCls : 'Add',
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
											text : '插入',
											iconCls : 'Disk',
											handler : confirmCode_confirm
										}, {
											text : '取消',
											iconCls : 'Cancel',
											handler : case_canel
										}],
								items : [{
									xtype : 'tbtext',
									style:'color:red',
									text : '注意：已经生成汇款单的案例，请删除后再操作！'
								},{
									xtype : "textfield",
									fieldLabel : '优惠码',
									allowBlank:false,
									labelAlign : 'right',
									maxLength : 100,
									regex:/^[^\s]*$/,
									regexText:'请输入正确条形码',
									style:"margin-top:20px;",
									labelWidth : 60,
									name : 'confirm_code'
								}]
							}]
						})
						confirmCodeadd.show();
					},
					attachmentManage:function(){
						var me = this.up("gridpanel");
						var selections =  me.getView().getSelectionModel().getSelection();
						if(selections.length<1){
							Ext.Msg.alert("提示", "请选择需要操作的记录!");
							return;
						};
						if(selections[0].get("delete") == '2')
						{
							Ext.Msg.alert("提示", "该记录已作废!");
							return;
						}
						attachment_onInsert=function(me)
						{

							attachment_save=function(mei)
							{
								var form = mei.up("form").getForm();
								if (!form.isValid()) {
									Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
									return;
								}
								form.submit({
											url : 'judicial/attachment/upload.do',
											method : 'post',
											waitMsg : '正在上传您的文件...',
											success : function(form, action) {
												response = Ext.JSON
														.decode(action.response.responseText);
												console.log(response);
												if (response) {
													Ext.MessageBox.alert("提示信息", "上传成功！");
													var grid = me.up("gridpanel");
													grid.getStore().load();
													mei.up("window").close();
												} else {
													Ext.MessageBox.alert("提示信息", "上传失败，请联系管理员!");
												}
											},
											failure : function() {
												Ext.Msg.alert("提示", "上传失败，请联系管理员!");
											}
										});
							}
							var attachment_insert = Ext.create("Ext.window.Window", {
								title : '附件上传',
								width : 500,
								height : 400,
								layout : 'border',
								items : [{
									xtype : 'form',
									region : 'center',
									style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
									labelAlign : "right",
									autoScroll : true,
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
												handler : attachment_save
											}, {
												text : '取消',
												iconCls : 'Cancel',
												handler : mail_canel
											}],
									items : [{
										xtype : 'fieldset',
										title : '案例附件',
										id:'testFieldset',
										layout : 'anchor',
										defaults : {
											anchor : '100%'
										},
										items : [{
											layout : "column",// 从左往右的布局
											xtype : 'fieldcontainer',
											border : false,
											items : [ 	 {
												xtype : 'filefield',
												name : 'files',
												fieldLabel : '文件<span style="color:red">*</span>',
												msgTarget : 'side',
												allowBlank : false,
												labelWidth : 40,
												anchor : '100%',
												buttonText : '选择文件',
												columnWidth : .60,
											},new Ext.form.field.ComboBox({
												fieldLabel : '文件类型<span style="color:red">*</span>',
												labelWidth : 70,
												labelAlign : 'right',
												fieldStyle : me.fieldStyle,
												editable : false,
												allowBlank : false,
												triggerAction : 'all',
												displayField : 'Name',
												valueField : 'Code',
												store : new Ext.data.ArrayStore({
													fields : ['Name', 'Code'],
													data : [['登记表格', 1], ['身份证', 2],
															['照片', 3], ['其他', 4]]
												}),
												mode : 'local',
												name : 'filetype',
												columnWidth : .4,
												style:'margin-left:10px;'
											})
											]
										},{
											xtype : 'panel',
											layout : 'absolute',
											border : false,
											items : [{
												xtype : 'button',
												text : '增加文件',
												width : 100,
												style:'margin-bottom:10px',
												x : 0,
												handler : function() {
//													var me = this.up('form');
													var me = Ext.getCmp("testFieldset");
													me.add({
														xtype : 'form',
														style : 'margin-top:5px;',
														layout : 'column',
														border : false,
														items : [{
																	xtype : 'filefield',
																	name : 'files',
																	columnWidth : .60,
																	fieldLabel : '文件<span style="color:red">*</span>',
																	labelWidth : 40,
																	msgTarget : 'side',
																	allowBlank : false,
																	anchor : '100%',
																	style:'margin-bottom:10px',
																	buttonText : '选择文件'
																},new Ext.form.field.ComboBox({
																	fieldLabel : '文件类型<span style="color:red">*</span>',
																	labelWidth : 70,
																	labelAlign : 'right',
																	fieldStyle : me.fieldStyle,
																	editable : false,
																	allowBlank : false,
																	triggerAction : 'all',
																	displayField : 'Name',
																	valueField : 'Code',
																	store : new Ext.data.ArrayStore({
																				fields : ['Name', 'Code'],
																				data : [['登记表格', 1], ['身份证', 2],
																						['照片', 3], ['其他', 4]]
																			}),
																	mode : 'local',
																	name : 'filetype',
																	columnWidth : .4,
																	style:'margin-left:10px;'
																}), {
																	xtype : 'button',
																	style : 'margin-left:15px;',
																	text : '删除',
																	handler : function() {
																		var me = this.up("form");
																		console.log(me);
																		me.disable(true);
																		me.up("fieldset").remove(me);
																	}
																}]
													});
												}
											}]

										}
										]
									
							     }, {
										xtype : 'hiddenfield',
										name : 'case_code',
										value : selections[0].get("case_id")
									}]
								}]
							})
							attachment_insert.show();
							
							
						}
						attachment_onDel = function(me) {
							var grid = me.up("gridpanel");
							var selections = grid.getView().getSelectionModel().getSelection();
							if (selections.length < 1) {
								Ext.Msg.alert("提示", "请选择需要修改的记录!");
								return;
							};
							if(!(selections[0].get("verify_state") == '0' || selections[0].get("verify_state") == '2'))
							{
								Ext.MessageBox.alert("提示信息","该案例状态不允许删除！");
								return;
							}
							Ext.Msg.show({
										title : '提示',
										msg : '确定删除该记录?',
										width : 300,
										buttons : Ext.Msg.OKCANCEL,
										fn : function(buttonId, text, opt) {
											if (buttonId == 'ok') {
												var values = {
													id : selections[0].get("id"),
													attachment_path:selections[0].get("attachment_path")
												};
												Ext.Ajax.request({
															url : "judicial/attachment/delete.do",
															method : "POST",
															headers : {
																'Content-Type' : 'application/json'
															},
															jsonData : values,
															success : function(response, options) {
																response = Ext.JSON
																		.decode(response.responseText);
																console.log(response.result)
																if (response.result == true) {
																	Ext.MessageBox.alert("提示信息",
																			response.msg);
																	var grid = me.up("gridpanel");
																	grid.getStore().load();
																} else {
																	Ext.MessageBox.alert("错误信息",
																			response.msg);
																}
															},
															failure : function() {
																Ext.Msg.alert("提示",
																		"保存失败<br>请联系管理员!");
															}

														});
											} else {
												return;
											}

										},
										animateTarget : 'addAddressBtn',
										icon : Ext.window.MessageBox.INFO
									})
						}
						var win = Ext.create("Ext.window.Window", {
							title : "附件管理",
							width : 700,
							iconCls : 'Find',
							height : 400,
							modal:true,
							resizable:false,
							layout : 'border',
							bodyStyle : "background-color:white;",
							items : [ Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 690,
								height : 400,
								frame : false,
								viewConfig : {
									forceFit : true,
									stripeRows : true// 在表格中显示斑马线
								},
								dockedItems : [{
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
												xtype : 'button',
												text : '新增',
												iconCls : 'Pageedit',
												handler : attachment_onInsert
											}, {
												xtype : 'button',
												text : '删除',
												iconCls : 'Delete',
												handler : attachment_onDel
											}

									]
								}],
								store : {// 配置数据源
									fields : [ 'id', 'case_id', 'attachment_path','attachment_date','attachment_type','verify_state','username' ],// 定义字段
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'judicial/attachment/queryAll.do',
										params : {
											'case_id' : selections[0].get("case_id")
										},
										reader : {
//											type : 'json',
//											root : 'items',
//											totalProperty : 'count'
										}
									},
									autoLoad : true
								},
								columns : [// 配置表格列
								           {
											text : '类型',
											dataIndex : 'attachment_type',
											width : '40%',
											menuDisabled : true,
											flex : 1,
											renderer : function(value) {
												switch (value) {
													case 1 :
														return "登记表格";
														break;
													case 2 :
														return "身份证";
														break;
													case 3 :
														return "照片";
														break;
													case 4 :
														return "其他";
														break;
													case 6 :
														return "条纹图";
														break;
													default :
														return "其他";
												}
											}
										}, {
											text : '附件',
											dataIndex : 'attachment_path',
											width : '40%',
											menuDisabled : true,
											flex : 3
										}, {
											text : '最后上传日期',
											dataIndex : 'attachment_date',
											width : '10%',
											menuDisabled : true,
											flex : 1
										},
										{
											text : '上传人员',
											dataIndex : 'username',
											width : '10%',
											menuDisabled : true,
											flex : 1,
											renderer : function(value, cellmeta,
													record, rowIndex, columnIndex,
													store) {
												var isnull= record.data["username"];
												if (isnull == null) {
													return "系统生成"
												} else
												{
													return value;
												}

											}
										}, {
											header : "操作",
											dataIndex : '',
											flex : 0.5,
											menuDisabled : true,
											renderer : function(value, cellmeta,
													record, rowIndex, columnIndex,
													store) {
													return "<a href='#'>查看</a>";
											},
											listeners:{
												'click':function(){ 
													var me = this.up("gridpanel");
													var selections = me.getView().getSelectionModel().getSelection();
													if (selections.length < 1 || selections.length > 1) {
														Ext.Msg.alert("提示", "请选择需要查看的一条记录!");
														return;
													}
													console.log(selections[0].get(""));
													var form = Ext.create(
															"Rds.judicial.form.JudicialImageShow", {
																region : "center",
																grid : me
															});
													var win = Ext.create("Ext.window.Window", {
																title : '图片查看',
																width : 1600,
																iconCls : 'Pageedit',
																height : 1000,
																maximizable : true,
																maximized : true,
																layout : 'border',
																items : [form]
															});
													form.loadRecord(selections[0]);
													win.show();
												}
											}
										} ]
							}) ]
						});
						win.show();
						
					},
					onReportMail:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if(selections[0].get("verify_state") != 9)
						{
							Ext.Msg.alert("提示", "该案例报告没有邮寄!");
							return;
						}
						var showMsg = "<table style='line-height:20px;margin-left:20px;margin-top:5px;'>";
						showMsg += "<tr><td style='color:red'>快递信息</td></tr>";
						
						Ext.Ajax.request({  
							url : 'judicial/mail/getAllMails.do',
							method: "POST",
							async: false, 
							headers: { 'Content-Type': 'application/json' },
							jsonData: {
								case_id:selections[0].get("case_id")
							}, 
							success: function (response, options) { 
								response = Ext.JSON.decode(response.responseText); 
								if(response.length>0)
									{
									  for(var i = 0 ; i < response.length ; i++)
										  {
										  showMsg += "<tr><td>快递编号:<a href='#' onclick=\"queryMailInfo('"+ response[i].mail_code+ "','"+ response[i].mail_type + "')\"   >"
										  + response[i].mail_code+"</a></td></tr>";
										  showMsg += "<tr><td>快递时间:"+response[i].time+"</td></tr>";
										  showMsg += "<tr><td>快递类型:"+response[i].mail_typename+"</td></tr>";
										  showMsg += "<tr><td>收件人:"+response[i].mail_per+"</td></tr>";
										  }
									}
							},
							failure: function () {
								Ext.Msg.alert("提示", "请求失败<br>请联系管理员!");
							}
						});
						showMsg += "</table>";
						var win=Ext.create("Ext.window.Window", {
							width : 300,
							iconCls :'Pageadd',
							height : 400,
							modal:true,
							title:'案例信息',
							layout : 'border',
							html:showMsg,
							bodyStyle : "background-color:white;font-size:15px;font-family:'黑体'"
						});
						win.show();
				},
					onFillReport:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要补报告的记录!");
							return;
						}
						if(selections[0].get("verify_state")<6)
						{
							Ext.Msg.alert("提示", "该案例状态不允许补报告!");
							return;
						}
						
						Ext.MessageBox.confirm('提示','补报告收费吗？',
										function(id) {
											if (id == 'yes') {
												var values = {
														case_id : selections[0].get("case_id"),
														type:selections[0].get("case_state"),
														finance_type:selections[0].get("case_type")=="0"?"亲子鉴定-司法":"亲子鉴定-医学",
														case_type:'dna_add',
														finance_remark:"补报告案例",
														stand_sum:50.0,
														real_sum:50.0,
														return_sum:0,
														discountPrice:0
													};
												Ext.Ajax.request({
															url : "judicial/register/fillReport.do",
															method : "POST",
															headers : {
																'Content-Type' : 'application/json'
															},
															jsonData : values,
															success : function(response,options) {
																response = Ext.JSON.decode(response.responseText);
																if (response.result == true) {
																	Ext.MessageBox.alert("提示信息",response.message);
																	me.getStore().load();
																} else {
																	Ext.MessageBox.alert("错误信息",response.message);
																}
															},
															failure : function() {
																Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
															}
														});
											}else if(id == 'no')
											{
												var values = {
														case_id : selections[0].get("case_id"),
														type:selections[0].get("case_state"),
														finance_type:selections[0].get("case_type")=="0"?"亲子鉴定-司法":"亲子鉴定-医学",
														case_type:'dna_add',
														finance_remark:"补报告案例，免费",
														stand_sum:0.0,
														real_sum:0.0,
														return_sum:0,
														discountPrice:0
													};
												Ext.Ajax.request({
													url : "judicial/register/fillReport.do",
													method : "POST",
													headers : {
														'Content-Type' : 'application/json'
													},
													jsonData : values,
													success : function(response,options) {
														response = Ext.JSON.decode(response.responseText);
														if (response.result == true) {
															Ext.MessageBox.alert("提示信息",response.message);
															me.getStore().load();
														} else {
															Ext.MessageBox.alert("错误信息",response.message);
														}
													},
													failure : function() {
														Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
													}
												});
											}
										});
					},
					onChangeReport:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要改报告的记录!");
							return;
						}
//						if(selections[0].get("verify_state")<6)
//						{
//							Ext.Msg.alert("提示", "该案例状态不允许改报告!");
//							return;
//						}
						Ext.MessageBox.confirm('提示','改报告案例收费吗？',
										function(id) {
											var values = {
													case_id : selections[0].get("case_id"),
													type:selections[0].get("case_state"),
													finance_type:selections[0].get("case_type")=="0"?"亲子鉴定-司法":"亲子鉴定-医学",
													finance_remark:"改报告案例",
													case_type:'dna_change',
													stand_sum:300.0,
													real_sum:300.0,
													return_sum:0,
													discountPrice:0
												};
											if (id == 'yes') {
												Ext.Ajax.request({
															url : "judicial/register/changeReport.do",
															method : "POST",
															headers : {
																'Content-Type' : 'application/json'
															},
															jsonData : values,
															success : function(response,options) {
																response = Ext.JSON.decode(response.responseText);
																if (response.result == true) {
																	Ext.MessageBox.alert("提示信息",response.message);
																	me.getStore().load();
																} else {
																	Ext.MessageBox.alert("错误信息",response.message);
																}
															},
															failure : function() {
																Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
															}
														});
											}else if(id=='no')
											{
												var values = {
														case_id : selections[0].get("case_id"),
														type:selections[0].get("case_state"),
														finance_type:selections[0].get("case_type")=="0"?"亲子鉴定-司法":"亲子鉴定-医学",
														finance_remark:"改报告案例",
														case_type:'dna_change',
														stand_sum:0.0,
														real_sum:0.0,
														return_sum:0,
														discountPrice:0
													};
												Ext.Ajax.request({
															url : "judicial/register/changeReport.do",
															method : "POST",
															headers : {
																'Content-Type' : 'application/json'
															},
															jsonData : values,
															success : function(response,options) {
																response = Ext.JSON.decode(response.responseText);
																if (response.result == true) {
																	Ext.MessageBox.alert("提示信息",response.message);
																	me.getStore().load();
																} else {
																	Ext.MessageBox.alert("错误信息",response.message);
																}
															},
															failure : function() {
																Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
															}
														});
											
											}
										});
					},
					onSecondCase:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要二次采样的记录!");
							return;
						};
						if(selections[0].get("case_state")==5)
						{
							Ext.Msg.alert("提示", "该记录为二次采样记录!");
							return;
						}
						ownpersonTemp=selections[0].get("case_userid") ;
						ownaddressTemp=selections[0].get("case_areacode") ;
						if(null==selections[0].get("report_model") || "" == selections[0].get("report_model"))
						{
							var form = Ext.create(
									"Rds.judicial.form.JudicialRegisterMedicalCaseSecondNoReptForm", {
										region : "center",
										autoScroll : true,
										grid : me
									});
							form.loadRecord(selections[0]);
							var win = Ext.create("Ext.window.Window", {
								title : '二次采样',
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
						}else
						{
							var form = Ext.create(
									"Rds.judicial.form.JudicialRegisterMedicalCaseSecondForm", {
										region : "center",
										autoScroll : true,
										grid : me
									});
							form.loadRecord(selections[0]);
							var win = Ext.create("Ext.window.Window", {
								title : '二次采样',
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
						}
					
					},
					onCaseFill:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要二次采样的记录!");
							return;
						};
						ownpersonTemp=selections[0].get("case_userid") ;
						ownaddressTemp=selections[0].get("case_areacode") ;
						console.log(selections[0].get("report_model"));
						if(null==selections[0].get("report_model") || "" == selections[0].get("report_model"))
						{
							var form = Ext.create(
									"Rds.judicial.form.JudicialRegisterMedicalCaseFillNoReptForm", {
										region : "center",
										autoScroll : true,
										grid : me
									});
							form.loadRecord(selections[0]);
							var win = Ext.create("Ext.window.Window", {
								title : '补样',
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
						}else{
							var form = Ext.create(
									"Rds.judicial.form.JudicialRegisterMedicalCaseFillForm", {
										region : "center",
										autoScroll : true,
										grid : me
									});
							form.loadRecord(selections[0]);
							var win = Ext.create("Ext.window.Window", {
								title : '补样',
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
						}
						
					},
					onFindCase:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要查看的记录!");
							return;
						};
						var win = Ext.create("Ext.window.Window", {
							title : "案例关联信息",
							width : 500,
							iconCls : 'Find',
							height : 300,
							modal:true,
							layout : 'border',
							bodyStyle : "background-color:white;",
							items : [ Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 500,
								height : 300,
								frame : false,
								viewConfig : {
									forceFit : true,
									stripeRows : true// 在表格中显示斑马线
								},
								store : {// 配置数据源
									fields : [ 'case_code', 'case_code_second', 'case_state' ],// 定义字段
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'judicial/register/getCaseSecond.do',
										params : {
											'case_code' : selections[0].get("case_code")
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
									header : "案例条形码",
									dataIndex : 'case_code',
									flex : 1.5,
									menuDisabled : true
								}, {
									header : "关联案例条形码",
									dataIndex : 'case_code_second',
									flex : 1.5,
									menuDisabled : true
								}, {
									header : "类型",
									dataIndex : 'case_state',
									flex : 1,
									menuDisabled : true,
									renderer : function(value,meta,record) {
										switch (value) {
										case '5':
											return "二次采样";
											break;
										case '6':
											return "补样";
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
											return "特殊1";
											break;
										case '2':
											return "特殊2";
											break;
										case '3':
											return "特殊3";
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
					onExpressAdd:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要添加的记录!");
							return;
						};
						var case_id='';
						for(var i = 0 ; i < selections.length ; i ++)
						{
							if((i+1)==selections.length)
								case_id +=  selections[i].get("case_id");
							else
								case_id += selections[i].get("case_id")+",";
						}
						mail_save = function(mei) {
							var form = mei.up("form").getForm();
							var values = form.getValues();
							values["case_id"]=case_id;
							if (form.isValid()) {
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
											url : "judicial/register/insertSampleExpress.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : values,
											success : function(response, options) {
												response = Ext.JSON
														.decode(response.responseText);
												if (response.result == true) {
													Ext.MessageBox.alert("提示信息", response.message);
													var grid = me.up("gridpanel");
													mailform_add.close();
												} else {
													Ext.MessageBox.alert("错误信息", response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
											}
										});
							}
						}
						
						var mailform_add = Ext.create("Ext.window.Window", {
							title : '快递信息',
							width : 320,
							height : 300,
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
											handler : mail_save
										}, {
											text : '取消',
											iconCls : 'Cancel',
											handler : mail_canel
										}],
								items : [{
									xtype : 'fieldset',
									title : '样本邮寄信息',
									layout : 'anchor',
									defaults : {
										anchor : '100%'
									},
									items : [new Ext.form.field.ComboBox({
										fieldLabel : '快递类型',
										labelWidth : 60,
										editable : false,
										triggerAction : 'all',
										labelAlign : 'right',
										// required!
										valueField : "key",
										displayField : "value",
										allowBlank : false,// 不允许为空
										blankText : "不能为空",// 错误提示信息，默认为This field is
															// required!
										store : mailStore,
										mode : 'local',
										// typeAhead: true,
										name : 'express_type'
									}), {
										xtype : "textfield",
										fieldLabel : '快递单号',
										labelAlign : 'right',
										maxLength : 18,
										allowBlank : false,// 不允许为空
										blankText : "不能为空",
										labelWidth : 60,
										name : 'express_num'
									}
									]
						     
						     }]
							}]
						})
						mailform_add.show();
					},
					onFindExpress:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要查看的记录!");
							return;
						};
						mail_onDel = function(me) {
							var grid = me.up("gridpanel");
							var selections = grid.getView().getSelectionModel().getSelection();

							if (selections.length < 1) {
								Ext.Msg.alert("提示", "请选择需要删除的记录!");
								return;
							}
							Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
						        if("yes"==btn)
						        {
						        	Ext.MessageBox.wait('正在操作','请稍后...');
						        	Ext.Ajax.request({
										url : "judicial/register/delSampleExpress.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : {
											"id" : selections[0].get("id")
										},
										success : function(response, options) {
											response = Ext.JSON.decode(response.responseText);
											if (response.result == true) {
												Ext.MessageBox.alert("提示信息", "快递删除成功");
												var grid = me.up("gridpanel");
												grid.getStore().load();
											} else {
												Ext.MessageBox.alert("错误信息", "修改删除失败,请联系管理员!");
											}
										},
										failure : function() {
											Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
										}
									});
						        }
							})
							
							
						}
						mail_onUpdate = function(me) {
							var grid = me.up("gridpanel");
							var selections = grid.getView().getSelectionModel().getSelection();

							if (selections.length < 1) {
								Ext.Msg.alert("提示", "请选择需要修改的记录!");
								return;
							}
							mail_update = function(mei) {
								var form = mei.up("form").getForm();
								var values = form.getValues();
								values["id"] = selections[0].get("id");
								if (form.isValid()) {
									Ext.MessageBox.wait('正在操作','请稍后...');
									Ext.Ajax.request({
												url : "judicial/register/updateSampleExpress.do",
												method : "POST",
												headers : {
													'Content-Type' : 'application/json'
												},
												jsonData : values,
												success : function(response, options) {
													response = Ext.JSON
															.decode(response.responseText);
													if (response.result == true) {
														Ext.MessageBox.alert("提示信息", "修改邮件成功");
														var grid = me.up("gridpanel");
														grid.getStore().load();
														mailform_update.close();
													} else {
														Ext.MessageBox.alert("错误信息", "修改邮件失败");
													}
												},
												failure : function() {
													Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
												}
											});
								}
							}
							var mailform_update = Ext.create("Ext.window.Window", {
								title : '快递信息',
								width : 320,
								height : 300,
								layout : 'border',
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
												handler : mail_update
											}, {
												text : '取消',
												iconCls : 'Cancel',
												handler : mail_canel
											}],
									items : [{
										xtype : 'fieldset',
										title : '样本邮寄信息',
										layout : 'anchor',
										defaults : {
											anchor : '100%'
										},
										items : [ new Ext.form.field.ComboBox({
											fieldLabel : '快递类型',
											labelWidth : 80,
											editable : false,
											triggerAction : 'all',
											labelAlign : 'right',
											value : selections[0].get("express_type"),
											allowBlank : false,// 不允许为空
											blankText : "不能为空",// 错误提示信息，默认为This field is
											valueField : "key",
											displayField : "value",
											allowBlank : false,// 不允许为空
											blankText : "不能为空",// 错误提示信息，默认为This field is
											store : mailStore,
											mode : 'local',
											// typeAhead: true,
											name : 'express_type'
										}), {
											xtype : "textfield",
											fieldLabel : '快递单号',
											labelAlign : 'right',
											maxLength : 18,
											value : selections[0].get("express_num"),
											labelWidth : 80,
											allowBlank : false,// 不允许为空
											blankText : "不能为空",
											name : 'express_num'
										}
										]
							     
							     },{
										xtype : 'fieldset',
										title : '报告回寄信息',
										layout : 'anchor',
										defaults : {
											anchor : '100%'
										},
										items : [ {
											xtype : "textfield",
											fieldLabel : '收件人',
											labelAlign : 'right',
											maxLength : 18,
											labelWidth : 80,
											name : 'express_recive',
											value: selections[0].get("express_recive")
										},{
											xtype : "textfield",
											fieldLabel : '联系方式',
											labelAlign : 'right',
											maxLength : 18,
											labelWidth : 80,
											name : 'express_concat',
											value : selections[0].get("express_concat")
										},{
											xtype : "textfield",
											fieldLabel : '收件地址',
											labelAlign : 'right',
											maxLength : 50,
											labelWidth : 80,
											name : 'express_remark',
											value : selections[0].get("express_remark")
										}
										]
							     
							     }, {
										xtype : 'hiddenfield',
										name : 'id',
										value : selections[0].get("id")
									}]
								}]
							})
							mailform_update.show();
						}
						var win = Ext.create("Ext.window.Window", {
							title : "样本快递信息（案例条形码：" + selections[0].get("case_code") + "）",
							width : 700,
							iconCls : 'Find',
							height : 400,
							resizable:false,
							modal:true,
							layout : 'border',
							bodyStyle : "background-color:white;",
							items : [ Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 690,
								height : 400,
								frame : false,
								viewConfig : {
									forceFit : true,
									stripeRows : true// 在表格中显示斑马线
								},
								dockedItems : [{
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
												xtype : 'button',
												text : '修改',
												iconCls : 'Pageedit',
												handler : mail_onUpdate
											}, {
												xtype : 'button',
												text : '删除',
												iconCls : 'Delete',
												handler : mail_onDel
											}

									]
								}],
								store : {// 配置数据源
									fields : [ 'id', 'case_id', 'express_type','express_type_name','express_num','express_concat',
											'express_time', 'express_recive', 'express_remark' ],// 定义字段
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'judicial/register/querySampleExpress.do',
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
									header : "快递编号",
									dataIndex : 'express_num',
									flex : 1.5,
									menuDisabled : true
								}, {
									header : "快递类型",
									dataIndex : 'express_type_name',
									flex : 1,
									menuDisabled : true
								}, {
									header : "快递时间",
									dataIndex : 'express_time',
									flex : 1.5,
									menuDisabled : true
								}, {
									header : "收件人",
									dataIndex : 'express_recive',
									flex : 1,
									menuDisabled : true
								}, {
									header : "联系人",
									dataIndex : 'express_concat',
									flex : 1,
									menuDisabled : true
								}, {
									header : "快递地址",
									dataIndex : 'express_remark',
									flex : 1.5,
									menuDisabled : true
								}, {
									header : "操作",
									dataIndex : 'express_num',
									flex : 0.5,
									menuDisabled : true,
									renderer : function(value, cellmeta, record,
											rowIndex, columnIndex, store) {
										var express_type = record.data["express_type"];
										if(express_type=='ziqu'){
											return "";
										}else{
											return "<a href='#' onclick=\"queryMailInfo('"
											+ value
											+ "','"
											+ express_type
											+ "')\"  >查询";
										}
									}
								} ]
							}) ]
						});
						win.show();
					},
					onDelete : function() {
						Ext.Msg.alert("提示", "请联系业务支撑作废!");
						return;
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要删除的记录!");
							return;
						};
						if(selections[0].get("verify_state")==3){
							Ext.Msg.alert("提示", "此案例已审核，无法作废!");
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
					onInsertOther:function(){
						var me = this.up("gridpanel");
						ownpersonTemp="";
						ownaddressTemp="";
						var form = Ext.create(
								"Rds.judicial.form.JudicialRegisterMedicalInsertNoReptForm",
								{
									region : "center",
									autoScroll : true,
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '案例添加',
							width : 800,
							modal:true,
							iconCls : 'Pageadd',
							height : 600,
							layout : 'border',
							maximizable :true,
							maximized:true,
							items : [ form ]
						});
						win.show();
					},
					onInsert : function() {
						var me = this.up("gridpanel");
						ownpersonTemp="";
						ownaddressTemp="";
						var form = Ext.create(
								"Rds.judicial.form.JudicialRegisterMedicalInsertForm",
								{
									region : "center",
									autoScroll : true,
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '案例添加',
							width : 800,
							modal:true,
							iconCls : 'Pageadd',
							height : 600,
							layout : 'border',
							maximizable :true,
							maximized:true,
							items : [ form ]
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
						console.log(selections[0].get("report_model"));
						if (selections[0].get("task_def_key") && selections[0].get("task_def_key") != "taskRegister") {
							Ext.Msg.alert("提示", "该状态不允许修改!");
							return;
						}
						if(selections[0].get("is_delete")==1){
							Ext.Msg.alert("提示", "此案例已删除，无法修改!");
							return;
						}
//						if(selections[0].get("verify_state")!=0 && selections[0].get("verify_state")!=2){
//							Ext.Msg.alert("提示", "此案例状态无法修改!");
//							return;
//						}
						//判断财务是否确认
						Ext.Ajax.request({
							url: 'finance/casefinance/queryConfirmCase.do',
							method: "POST",
							async: false,
							headers: {
								'Content-Type': 'application/json'
							},
							jsonData: {
								'case_code': selections[0].get("case_code"),
								'case_type':'dna'
							},
							success: function (response, options) {
								response = Ext.JSON.decode(response.responseText);
								if (response) {
									Ext.Msg.alert("提示", "案例财务已确认！不予修改");
								} else
								{
									if(null!=selections[0].get("report_model") && ""!=selections[0].get("report_model"))
									{
										var form = Ext.create(
												"Rds.judicial.form.JudicialRegisterMedicalUpdateForm", {
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
									}else{
										var form = Ext.create(
												"Rds.judicial.form.JudicialRegisterMedicalUpdateNoReptForm", {
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
									}
								}
							},
							failure: function () {
								Ext.Msg.alert("提示", "发生错误，请联系管理员!");
							}
						});
						
						
					
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
					
					console.log(taskId)
					
					var values = {
						taskId: taskId
					};
					console.log(values.taskId);
					if (values.taskId == null || values.taskId == "") {
						Ext.Msg.alert("提示", "该记录不能进行此项操作!");
						return;
					}
					
					Ext.MessageBox.confirm('提示','确定提交审核此案例吗？',
							function(id) {
								if (id == 'yes') {
									Ext.MessageBox.wait('正在操作','请稍后...');
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
											console.log(response);
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
							});
					
					
				
//					Ext.Ajax.request({
//						url: 'judicial/register/getCaseSubmit.do',
//						method: "POST",
//						headers: {
//							'Content-Type': 'application/json'
//						},
//						jsonData: {
//							'case_id': case_ids
//						},
//						success: function (response, options) {
//							response = Ext.JSON.decode(response.responseText);
//							console.log(response);
//							if (!response.result) {
//								Ext.Msg.alert("提示", response.message);
//							} else {}
//						},
//						failure: function () {
//							Ext.Msg.alert("提示", "发生错误，请联系管理员!");
//						}
//					});
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
							console.log(response);
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
											menuDisabled: true,
											 renderer : function(value, cellmeta, record,
														rowIndex, columnIndex, store) {
//													var str = value;
//													if (value.length > 15) {
//														str = value.substring(0, 15) + "...";
//													}
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
//										sorters: [{
//											property: 'id',
//											direction: 'DESC'
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