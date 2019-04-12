Ext
		.define(
				"Rds.judicial.panel.JudicialReportGridPanel",
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
													 'print_count','is_new',
													'accept_time','close_time',
													'client',
													'report_model','address',
													'case_in_per',"receiver_id",
													'phone',
													'sample_in_time', 'case_id',
													],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/report/getCaseInfo.do',
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
															receiver :trim(receiver.getValue()),
															case_code : case_code.getValue()
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
									text : '备注',
									dataIndex : 'remark',
									menuDisabled : true,
									width : 300
								} ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [
											case_code,receiver,starttime,endtime, {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											} ]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [{
										text : '查看样本信息',
										iconCls : 'Find',
										handler : me.onFind
									}, {
										text : '报告修改',
										iconCls : 'Pageadd',
										handler : me.onUpdate
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
					onUpdate : function(me) {
						var grid = me.up("gridpanel");
						var selections = grid.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要修改的记录!");
							return;
						};
                         var report_download=function(meii){
                 			var grid = meii.up("gridpanel");
							var selections = grid.getView().getSelectionModel().getSelection();
							if (selections.length < 1) {
								Ext.Msg.alert("提示", "请选择需要下载的记录!");
								return;
							}
							var path=selections[0].get("report_path")
							if(path==""){
								Ext.Msg.alert("提示", "报告不存在");
							}else{
								window.location.href="judicial/report/downloadReport.do?filepath="+selections[0].get("report_path")
							}
                         }
						 var report_update=function(meii){
								var grid = meii.up("gridpanel");
								report_from_canel=function(mei){
									mei.up("window").close();
								}
								report_from_update=function(mei){
									var form = mei.up("form").getForm();
									if(form.isValid()){
										form.submit({
											url : 'judicial/report/updateReport.do',
											method : 'post',
											waitMsg : '正在修改数据...',
											success : function(form, action) {
												response = Ext.JSON
														.decode(action.response.responseText);
												if(response){
													Ext.Msg.alert("提示", "保存成功");
													grid.getStore().load();
													mei.up("window").close();
												}else{
													Ext.Msg.alert("提示", "保存失败");
												}
											},
											failure : function() {
												Ext.Msg.alert("提示", "上传失败<br>请联系管理员!");
											}
										});
									}
								}
								var selections = grid.getView().getSelectionModel().getSelection();
								if (selections.length < 1) {
									Ext.Msg.alert("提示", "请选择需要修改的记录!");
									return;
								}
								var report_update_win=Ext.create("Ext.window.Window", {
									title : '报告信息',
									width : 250,
									height : 180,
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
											labelSeparator : ":",
											labelAlign : 'right'
										},
										border : false,
										autoHeight : true,
										buttons : [{
													text : '修改',
													iconCls : 'Disk',
													handler:report_from_update
												}, {
													text : '取消',
													iconCls : 'Cancel',
													handler:report_from_canel
												}],
										items : [	new Ext.form.field.ComboBox(
												{
													fieldLabel : '手动结论',
													labelWidth : 70,
													editable : false,
													triggerAction : 'all',
													displayField : 'Name',
													labelAlign : 'right',
													allowBlank : false,
													valueField : 'Code',
													store : new Ext.data.ArrayStore(
															{
																fields : [
																		'Name',
																		'Code' ],
																data : [
																		[
																				'否定(手动)',
																				'hand_failed' ],
																		[
																				'肯定(手动)',
																				'hand_passed'  ],
																		]
															}),
													mode : 'local',
													// typeAhead: true,
													name : 'result',
												}),{
											xtype : 'filefield',
											name : 'files',
											labelWidth : 70,
											fieldLabel : '修改报告',
											msgTarget : 'side',
											allowBlank : false,
											labelAlign : 'right',
											anchor : '100%',
											buttonText : '上传',
											 validator: function(v){
												    if(!v.endWith(".doc")&&!v.endWith(".docx")){
												      return "请选择.doc .docx类型的图片";
												    }
												    return true;
											}
										},{
											xtype : 'hiddenfield',
											name : 'sub_case_code',
											value : selections[0].get("sub_case_code")
										}]
									}]
								})
								report_update_win.show();
						 }
						 var win = Ext.create("Ext.window.Window", {
									title : '报告修改',
									width : 800,
									iconCls : 'Pageadd',
									height : 400,
									layout : 'border',
									items : [Ext.create('Ext.grid.Panel', {
										width : 800,
										height : 350,
										frame : false,
										renderTo : Ext.getBody(),
										viewConfig : {
											forceFit : true,
											stripeRows : true
											,// 在表格中显示斑马线
										},
										store : {// 配置数据源
											fields : ['case_code', 'sub_case_code','sample_name1','sample_name2','sample_name3'
											          ,'sample_code1','sample_code2','sample_code3','result','report_path'],// 定义字段
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : "judicial/report/getCaseReport.do",
												params : {
													"is_new":selections[0].get("is_new"),
													"case_code":selections[0].get("case_code")
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
										dockedItems : [{
											xtype : 'toolbar',
											dock : 'top',
											items : [{
														xtype : 'button',
														text : '修改子案例',
														iconCls : 'Pageedit',
														handler : report_update
													},{
														xtype : 'button',
														text : '下载报告',
														iconCls : 'Pageedit',
														handler : report_download
													} 
											]
										}],
										columns : [// 配置表格列
										{
													header : "子案例编号",
													dataIndex : 'sub_case_code',
													flex : 2,
													menuDisabled : true,
												}, {
													header : "样本1",
													dataIndex : 'sample_name1',
													flex : 3,
													menuDisabled : true,
													renderer : function(value, cellmeta,
															record, rowIndex, columnIndex,
															store) {
														var sample_code= record.data["sample_code1"];
													    return value+"("+sample_code+")";
													}
												},
												{
													header : "样本2",
													dataIndex : 'sample_name2',
													flex : 3,
													menuDisabled : true,
													renderer : function(value, cellmeta,
															record, rowIndex, columnIndex,
															store) {
														if(value==null||value==''){
															return "";
														}else{
															var sample_code= record.data["sample_code2"];
														    return value+"("+sample_code+")";
														}
													}
												},
												{
													header : "样本3",
													dataIndex : 'sample_name3',
													flex : 3,
													menuDisabled : true,
													renderer : function(value, cellmeta,
															record, rowIndex, columnIndex,
															store) {
														var sample_code= record.data["sample_code3"];
													    return value+"("+sample_code+")";
													}
												},{
													header : "结论",
													dataIndex : 'result',
													flex : 3,
													menuDisabled : true,
													renderer : function(value) {
														if(value=="hand_passed"){
															return "存在亲子关系(手动修改)";
														}else if(value=="passed"){
															return "存在亲子关系";
														}else if(value=="hand_failed"){
															return "不存在亲子关系(手动修改)";
														}else if(value=="failed"){
															return "不存在亲子关系";
														}else {
															return "未知";
														}
													}
												}]
									})],
								});
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
