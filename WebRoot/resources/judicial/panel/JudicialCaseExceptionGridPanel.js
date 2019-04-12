Ext
		.define(
				"Rds.judicial.panel.JudicialCaseExceptionGridPanel",
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
						var storeModel = Ext.create('Ext.data.Store', {
							model : 'model',
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/caseException/getExceptionTypes.do',
								reader : {
									type : 'json',
									root : 'data'
								}
							},
							autoLoad : true,
							remoteSort : true,
							listeners : {
									'load' : function() {
										var allmodel = new model({'key':'','value':'全部'});
								        this.insert(0,allmodel);
								        other_type.select(this.getAt(0));
									}
							}
						});
						var other_type=Ext.create('Ext.form.ComboBox', 
						    		{
									fieldLabel : '自定义异常类型',
									mode: 'local',   
									labelWidth : 100,
									editable:false,
									valueField :"key",  
									width : 220,
							        displayField: "value",    
									name : 'other_type',
									store: storeModel,
						});
						var receiver = Ext.create('Ext.form.field.Text', {
							name : 'receiver',
							labelWidth : 60,
							width : 200,
							regexText : '请输入归属人',
							fieldLabel : '归属人'
						});
						var client = Ext.create('Ext.form.field.Text', {
							name : 'client',
							labelWidth : 60,
							width : 200,
							regexText : '请输入委托人',
							fieldLabel : '委托人'
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
						var is_mail=Ext.create('Ext.form.ComboBox', 
								{
							fieldLabel : '是否邮寄',
							width : 200,
							labelWidth : 60,
							editable : false,
							triggerAction : 'all',
							displayField : 'Name',
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
														'是',
														0 ],
												[
														'否',
														1 ] ]
									}),
							value : -1,
							mode : 'local',
							// typeAhead: true,
							name : 'is_mail',
				        });
						var is_super_time=Ext.create('Ext.form.ComboBox', 
								{
							fieldLabel : '是否超过48小时',
							width : 200,
							labelWidth : 100,
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
														'是',
														0 ],
												[
														'否',
														1 ] ]
									}),
							value : -1,
							mode : 'local',
							// typeAhead: true,
							name : 'is_super_time',
				        });
						var fee_state=Ext.create('Ext.form.ComboBox', 
								{
							fieldLabel : '回款状态',
							width : 200,
							labelWidth : 70,
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
														'先出报告后付款',
														0 ],
												[
														'已确认',
														1 ],
												[
														'未确认',
														2 ]]
									}),
							value : -1,
							mode : 'local',
							// typeAhead: true,
							name : 'fee_state',
				        });
						var is_report=Ext.create('Ext.form.ComboBox', 
								{
									fieldLabel : '能否出报告',
									width : 200,
									labelWidth : 80,
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
																'能',
																0 ],
														[
																'不能',
																1 ] ]
											}),
									value : -1,
									mode : 'local',
									// typeAhead: true,
									name : 'is_report',
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
											fields : [ 'case_id', 'case_code',"receiver_area","case_receiver","agent",
													"fee_status","fee_type","is_super_time","mail_time","per_photo","sample_in_time",
													"verify_state","close_time","is_report","is_to_time",'client'],
											start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/caseException/getCaseException.do',
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
																client:trim(client.getValue()),
																case_code : trim(case_code.getValue()),
																is_mail:is_mail.getValue(),
																is_report:is_report.getValue(),
																case_receiver:trim(receiver.getValue()),
																other_type:other_type.getValue(),
																fee_state:fee_state.getValue(),
																is_super_time:is_super_time.getValue()
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
									width : 120,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var agent= record.data["agent"];
										if (agent != ''&& agent!=null) {
											return value+"(代理："+agent+")";
										} else {
											return value;
										}
									}
								},
								{
									text : '委托人',
									dataIndex : 'client',
									menuDisabled : true,
									width : 120
								},
								{
									text : '回款状态',
									dataIndex : 'fee_status',
									menuDisabled : true,
									width : 200,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var fee_type= record.data["fee_type"];
										var is_super_time= record.data["is_super_time"];
										var color="red";var state="异常";

										if(is_super_time==1){
											color="green";
											state="正常";
										}
										if (fee_type == 1) {
											return "<div style=\"color:"+color+"\">先出报告后付款("+state+")</div>";
										}if (fee_type == 4) {
											return "<div style=\"color:"+color+"\">月结("+state+")</div>";
										} else {
											if(value==0){
												return "<div style=\"color:"+color+"\">已回款("+state+")</div>";
											}else{
												return "<div style=\"color:"+color+"\">未回款("+state+")</div>";
											}
										}
									}
								},
								{
									text : '是否超过48小时',
									dataIndex : 'is_super_time',
									menuDisabled : true,
									width : 300,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var mail_time= record.data["mail_time"];
										var sample_in_time=record.data["sample_in_time"];
										if (value == 1) {
											return "<div style=\"color:green\">正常（登记时间:"+sample_in_time+",邮寄时间:"+(mail_time==null?"":mail_time)+"）</div>";
										} else {
											if(mail_time!=null||mail_time!=null){
												return "<div style=\"color:red\">异常（登记时间:"+sample_in_time+",邮寄时间:"+mail_time+"）</div>";
											}else{
												return "<div style=\"color:red\">异常（登记时间:"+sample_in_time+",未邮寄）</div>";
											}
										}
									}
								},
								{
									text : '是否出报告',
									dataIndex : 'is_report',
									menuDisabled : true,
									width : 200,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var is_super_time= record.data["is_super_time"];
										var color="red";var state="异常";
										if(is_super_time==1){
											color="green";
											state="正常";
										}
										var close_time= record.data["close_time"];
										if (value == 0) {
											if(close_time!=""&&close_time!=null){
												return "<div style=\"color:green\">已打印，打印时间:"+close_time+"(正常)</div>";
											}
											return "<div style=\"color:"+color+"\">未打印("+state+")</div>";
										} else {
											return "<div style=\"color:"+color+"\">实验未做完("+state+")</div>";
										}
									}
								},
								{
									text : '是否上传照片',
									dataIndex : 'per_photo',
									menuDisabled : true,
									width : 200,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var is_to_time= record.data["is_to_time"];
										var color="red";var state="异常";
										if(is_to_time==1){
											color="green";
											state="正常";
										}
										if (value == 0) {
											return "<div style=\"color:green\">已上传（正常）</div>";
										} else {
											return "<div style=\"color:"+color+"\">未上传（"+state+"）</div>";
										}
									}
								},
								{
									text : '案例审核状态',
									dataIndex : 'verify_state',
									menuDisabled : true,
									width : 200,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var is_super_time= record.data["is_super_time"];
										var color="red";var state="异常";
										if(is_super_time==1){
											color="green";
											state="正常";
										}
										switch (value) {
										case 0:
											return "<span style=\"color:"+color+"\">未审核("+state+")</span>";
											break;
										case 1:
											return "<span style='color:"+color+"'>案例审核未通过("+state+")</span>";
											break;
										case 2:
											return "<span style='color:"+color+"'>案例审核通过,样本未审核("+state+")</span>";
											break;
										case 3:
											return "<span style='color:green'>样本审核通过(正常)</span>";
											break;
										case 4:
											return "<span style='color:"+color+"'>样本审核未通过，已废除("+state+")</span>";
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
									items : [
											case_code,receiver,client,fee_state,is_report]
								},{
									xtype : 'toolbar',
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '1px !important'
								    },
									items : [is_mail,is_super_time,other_type,starttime,endtime
												, {
										text : '查询',
										iconCls : 'Find',
										handler : me.onSearch
									}  ]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '查看自定义异常信息',
										iconCls : 'Find',
										handler : me.onFind
									}, {
										text : '添加自定义异常信息',
										iconCls : 'Pageadd',
										handler : me.onInsert
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
						updateExceptionInfo =function(mei){
							var grid = mei.up("gridpanel");
							var selections = grid.getView().getSelectionModel().getSelection();
							if (selections.length < 1) {
								Ext.Msg.alert("提示", "请选择需要删除的记录!");
								return;
							}
							if(selections[0].get("is_handle")==1){
								Ext.Msg.alert("提示", "请异常已处理!");
								return;
							}
							if(selections[0].get("is_handle")==2){
								Ext.Msg.alert("提示", "请异常已删除!");
								return;
							}
							var form = Ext.create(
									"Rds.judicial.form.JudicialExceptionUpdateForm", {
										region : "center",
										grid : me
									});
							form.loadRecord(selections[0]);
							var win = Ext.create("Ext.window.Window", {
								title : '案例自定义异常修改',
								width : 600,
								iconCls : 'Pageedit',
								height : 300,
								layout : 'border',
								items : [ form ],
							});
							win.show();
							win.on("close",function(){
							      grid.getStore().load();
							});
						}
						deleteExceptionInfo=function(me){
							var grid = me.up("gridpanel");
							var selections = grid.getView().getSelectionModel().getSelection();
							if (selections.length < 1) {
								Ext.Msg.alert("提示", "请选择需要删除的记录!");
								return;
							}
							if(selections[0].get("is_handle")==1){
								Ext.Msg.alert("提示", "请异常已处理!");
								return;
							}
							if(selections[0].get("is_handle")==2){
								Ext.Msg.alert("提示", "请异常已删除!");
								return;
							}
							Ext.MessageBox
							.confirm(
									'提示',
									'确定删除此异常吗',
									function(id) {
										if (id == 'yes') {
											Ext.Ajax.request({
												url : "judicial/caseException/deleteExceptionInfo.do",
												method : "POST",
												headers : {
													'Content-Type' : 'application/json'
												},
												jsonData : {
													exception_id:selections[0].get("exception_id")
												},
												success : function(response, options) {
													response = Ext.JSON
															.decode(response.responseText);
													if (response == true) {
														Ext.MessageBox.alert("提示信息", "删除异常成功");
														grid.getStore().load();
													} else {
														Ext.MessageBox.alert("错误信息", "删除异常失败");
													}
												},
												failure : function() {
													Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
												}
											});
										}
						   });
						}
						handleExceptionInfo=function(me){
										var grid = me.up("gridpanel");
										var selections = grid.getView().getSelectionModel().getSelection();
										if (selections.length < 1) {
											Ext.Msg.alert("提示", "请选择需要处理的记录!");
											return;
										}
										if(selections[0].get("is_handle")==1){
											Ext.Msg.alert("提示", "异常已处理!");
											return;
										}
										if(selections[0].get("is_handle")==2){
											Ext.Msg.alert("提示", "异常已删除!");
											return;
										}
										Ext.MessageBox
										.confirm(
												'提示',
												'确定处理此异常吗',
												function(id) {
													if (id == 'yes') {
														Ext.Ajax.request({
															url : "judicial/caseException/handleExceptionInfo.do",
															method : "POST",
															headers : {
																'Content-Type' : 'application/json'
															},
															jsonData : {
																exception_id:selections[0].get("exception_id")
															},
															success : function(response, options) {
																response = Ext.JSON
																		.decode(response.responseText);
																if (response == true) {
																	Ext.MessageBox.alert("提示信息", "处理异常成功");
																	grid.getStore().load();
																} else {
																	Ext.MessageBox.alert("错误信息", "处理异常失败");
																}
															},
															failure : function() {
																Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
															}
														});
													}
									   });
									}	
						var win = Ext.create("Ext.window.Window", {
							title : "自定义异常信息（案例条形码：" + selections[0].get("case_code") + "）",
							width : 800,
							iconCls : 'Find',
							height : 500,
							layout : 'border',
							bodyStyle : "background-color:white;",
							items : [ Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 800,
								height : 800,
								dockedItems: [{
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '修改自定义异常信息',
										iconCls : 'Pageedit',
										handler : updateExceptionInfo
									}, {
										text : '删除自定义异常信息',
										iconCls : 'Delete',
										handler : deleteExceptionInfo
									},{
										text : '处理自定义异常信息',
										iconCls : 'Bullettick',
										handler : handleExceptionInfo
									}]
								}],
								viewConfig : {
									forceFit : true,
									stripeRows : true// 在表格中显示斑马线
								},
								store : {// 配置数据源
									fields : ['exception_id',"case_id","exception_type","exception_desc","exception_time","exception_type_str",
									          "exception_per","exception_pername","is_handle","handle_time"],// 定义字段
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'judicial/caseException/getOtherException.do',
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
									header : "异常类型",
									dataIndex : 'exception_type_str',
									flex : 1,
									menuDisabled : true
								}, {
									header : "异常具体描述",
									dataIndex : 'exception_desc',
									flex : 3,
									menuDisabled : true
								}, {
									header : "新增异常人",
									dataIndex : 'exception_pername',
									flex : 1,
									menuDisabled : true
								},{
									header : "异常时间",
									dataIndex : 'exception_time',
									flex : 1.5,
									menuDisabled : true
								},{
									header : "是否处理",
									dataIndex : 'is_handle',
									flex : 1,
									menuDisabled : true,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										if (value == 0) {
											return "<div >未处理</div>";
										} else if(value==1){
											return "<div style=\"color:green\">已处理</div>";
										}else{
											return "<div style=\"color:red\">已删除</div>";
										}
									}
								},{
									header : "处理时间",
									dataIndex : 'handle_time',
									flex : 1.5,
									menuDisabled : true
								}]
							}) ]
						});
						win.show();
					},
					onInsert : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要添加异常的案例!");
							return;
						}
						var form = Ext.create(
								"Rds.judicial.form.JudicialExceptionInsertForm", {
									region : "center",
									grid : me
								});
						form.loadRecord(selections[0]);
						var win = Ext.create("Ext.window.Window", {
							title : '案例自定义异常添加',
							width : 600,
							iconCls : 'Pageedit',
							height : 300,
							layout : 'border',
							items : [ form ]
						});
						win.show();
					},
					onSearch : function() {
						var me = this.up("gridpanel");
						me.getStore().currentPage=1;
						me.getStore().load();
					},
					listeners : {
//						'afterrender' : function() {
//							this.store.load();
//						}
					}
				});
