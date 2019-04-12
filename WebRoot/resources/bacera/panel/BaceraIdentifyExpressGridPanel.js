var identifyExpress="";
Ext.define("Rds.bacera.panel.BaceraIdentifyExpressGridPanel",
				{
					extend : "Ext.grid.Panel",
					loadMask : true,
					viewConfig : {
						trackOver : false,
						stripeRows : false
					},
					region:'center',
					pageSize : 25,
					   selType: 'rowmodel',
					    plugins: [ 
					               Ext.create('Ext.grid.plugin.CellEditing', {
					                  clicksToEdit: 1,
					                  listeners:{  
					                      'edit':function(e,s){  					                      	
					                    	  Ext.Ajax.request({  
												url:"bacera/boneAge/saveExpress.do", 
												method: "POST",
												headers: { 'Content-Type': 'application/json' },
												jsonData: {
													id:s.record.data.case_id,
													num:s.record.data.case_code,
													expressnum:s.record.data.expressnum,
													recive:s.record.data.recive,
													expresstime:Ext.util.Format.date(s.record.data.expresstime, 'Y-m-d'),
													case_type:s.record.data.type,
													expresstype:s.record.data.expresstype,
													expressremark:s.record.data.expressremark
												}, 
												success: function (response, options) {  
													response = Ext.JSON.decode(response.responseText); 
									                 if (response==false) {  
										                 Ext.MessageBox.alert("错误信息", "修改收费失败，请查看");
									                 }
												},  
												failure: function () {
													Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
												}
									      	});
					                      }  
					      	          }  
					              })
					          ],
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
							labelWidth:50,
							width:150,
							fieldLabel:'登记人'
						});
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
						var type =  new Ext.form.field.ComboBox(
								{
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
						var expressnum = Ext.create('Ext.form.field.Text',{
							name:'expressnum',
							labelWidth:60,
							width:140,
							fieldLabel:'快递单号'
						});
						var client = Ext.create('Ext.form.field.Text',{
							name:'client',
							labelWidth:60,
							width:140,
							fieldLabel:'委托人'
						});
						var express_starttime = new Ext.form.DateField({
							name : 'express_starttime',
							width : 180,
							fieldLabel : '快递日期从',
							labelWidth : 70,
							labelAlign : 'left',
							emptyText : '请选择日期',
							format : 'Y-m-d'
						});
						var express_endtime = new Ext.form.DateField({
							name : 'express_endtime',
							width : 135,
							fieldLabel : ' 到 ',
							labelWidth : 20,
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d'
						});
						me.store = Ext
								.create(
										'Ext.data.Store',
										{
											fields : [ 'case_id', 'case_code','case_areacode','receiver_area',
														'case_receiver','remark','remarks','accept_time','close_time','client'
														,'address','case_in_per','receiver_id',
														'case_in_pername','phone','sample_in_time','is_delete',
														'reportif','agentname',
														'receivables','payment','paid','paragraphtime','account_type',
														'expressnum','expresstype','recive','type','expresstime',
														'entrustment_time','entrustment_matter','expressremark'
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
												'beforeload' : function(ds,
														operation, opt) {
												identifyExpress = "case_code=" + trim(Ext.getCmp('case_code_query3').getValue()) + "&receiver="
													+ trim(Ext.getCmp('receiver_query3').getValue()) + "&is_delete=0"
													+ "&starttime=" + dateformat(Ext.getCmp('starttime_query3').getValue()) + "&endtime="
													+ dateformat(Ext.getCmp('endtime_query3').getValue())+ "&reportif=" 
													+ reportif.getValue() + "&case_in_per="
													+ case_in_per.getValue()+"&type=" + type.getValue()
													+ "&express_starttime="+dateformat(express_starttime.getValue()) +"&express_endtime="+dateformat(express_endtime.getValue())
													+ "&expressnum="+expressnum.getValue();
													Ext
													.apply(
															me.store.proxy.extraParams,{								
																endtime : dateformat(Ext
																		.getCmp(
																		'endtime_query3')
																               .getValue()),	
																starttime : dateformat(Ext
																		.getCmp(
																				'starttime_query3')
																		.getValue()),
																receiver : trim(Ext
																				.getCmp(
																						'receiver_query3')
																				.getValue()),
																case_code : trim(Ext
																		.getCmp(
																				'case_code_query3')
																		.getValue()),
																is_delete : 0 ,
																case_in_per:case_in_per.getValue(),
																reportif:reportif.getValue(),
																expressnum:trim(expressnum.getValue()),
																client:trim(client.getValue()),
																express_starttime:dateformat(express_starttime.getValue()),
																express_endtime:dateformat(express_endtime.getValue()),
																type:type.getValue()
																});
												}
											}
										});
						me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//							mode: 'SINGLE'
						});
						me.bbar = Ext.create('Ext.PagingToolbar', {
				            store: me.store,
				            pageSize:me.pageSize,
				            displayInfo: true,
				            displayMsg : "第 {0} - {1} 条  共 {2} 条",
					   	 	emptyMsg : "没有符合条件的记录"
				        });
//						me.bbar = {xtype: 'label',id:'totalBbar_identify_express', text: '',style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
						me.columns = [
								{
									text : '案例条形码',
									dataIndex : 'case_code',
									menuDisabled : true,
									width : 150
								}, {
									text : '委托人',
									dataIndex : 'client',
									menuDisabled : true,
									width : 120
								},{ 
									text: '快递单号',	dataIndex: 'expressnum', menuDisabled:true, width : 100,
					            	  editor:'textfield'
					            },{ 
					            	  text: '快递类型',	dataIndex: 'expresstype', menuDisabled:true, width : 100,
					            	  editor:new Ext.form.ComboBox({
					          				autoSelect : true,
					          				editable:true,
					          		        name:'expresstype',
					          		        triggerAction: 'all',
					          		        queryMode: 'local', 
					          		        emptyText : "请选择",
					          		        selectOnTab: true,
					          		        store: mailStore,
					          		        maxLength: 50,
					          		        fieldStyle: me.fieldStyle,
					          		        displayField:'value',
					          		        valueField:'value',
					          		        listClass: 'x-combo-list-small'
					          			})
					              }, 
					              { text: '快递日期',	dataIndex: 'expresstime', menuDisabled:true, width : 110,
				                	  renderer:Ext.util.Format.dateRenderer('Y-m-d'),
				                      editor:new Ext.form.DateField({
				                           format: 'Y-m-d'
				                         })
					              },{
					            	  text: '收件人',	dataIndex: 'recive', menuDisabled:true, width : 120,
					            	  editor:'textfield'
					              }, {
					            	  text: '快递备注',	dataIndex: 'expressremark', menuDisabled:true, width : 150,
				                	  editor:'textfield'
				                  },{
					            	  text: '所付款项',	dataIndex: 'payment', menuDisabled:true, width : 80
				                  },{
				                	  text: '到款时间',	dataIndex: 'paragraphtime', menuDisabled:true, width : 80},
				                  {
				                	  text: '财务备注',	dataIndex: 'remarks', menuDisabled:true, width : 80
				                  },{
									text : '归属人',
									dataIndex : 'case_receiver',
									menuDisabled : true,
									width : 120
								},{
									text : '受理日期',
									dataIndex : 'accept_time',
									menuDisabled : true,
									width : 100
								},{
									text : '样本登记日期',
									dataIndex : 'sample_in_time',
									menuDisabled : true,
									width : 150
								},
								{
									text : '登记人',
									dataIndex : 'case_in_pername',
									menuDisabled : true,
									width : 100
								}, 
								{
									text : '案例归属地',
									dataIndex : 'receiver_area',
									menuDisabled : true,
									width : 200
								},{
									text : '被代理人',
									dataIndex : 'agentname',
									menuDisabled : true,
									width : 120
								},{
									text : '案例类型',
									dataIndex : 'type',
									menuDisabled : true,
									width : 100
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
									text : '电话号码',
									dataIndex : 'phone',
									menuDisabled : true,
									width : 120
								 },								{
									text : '身份证地址',
									dataIndex : 'case_areacode',
									width : 200,
									menuDisabled : true
								},{
									text: '备注要求',	dataIndex: 'remark', menuDisabled:true, width : 300}
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
												id : 'case_code_query3',
												labelWidth : 70,
												width : 155,
												fieldLabel : '案例条形码'
											},
											{
												xtype : 'textfield',
												fieldLabel : '归属人',
												width : 145,
												labelWidth : 50,
												id : 'receiver_query3',
												name : 'receiver'
											},agent,
											{
												xtype : 'datefield',
												name : 'starttime',
												id : 'starttime_query3',
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
																'starttime_query3')
																.getValue();
														Ext.getCmp('endtime_query3')
																.setMinValue(
																		start);
														var endDate = Ext
																.getCmp(
																		'endtime_query3')
																.getValue();
														if (start > endDate) {
															Ext
																	.getCmp(
																			'endtime_query3')
																	.setValue(
																			start);
														}
													}
												}
											},
											{
												xtype : 'datefield',
												id : 'endtime_query3',
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
																'starttime_query3')
																.getValue();
														var endDate = Ext
																.getCmp(
																		'endtime_query3')
																.getValue();
														if (start > endDate) {
															Ext
																	.getCmp(
																			'starttime_query3')
																	.setValue(
																			endDate);
														}
													}
												}
											}]
								},{
							 		style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 		},
								 		xtype:'toolbar',
								 		name:'search',
								 		dock:'top',
								 		items:[reportif,case_in_per,client,expressnum]
								 	},{
								 		style : {
									        borderTopWidth : '0px !important',
									        borderBottomWidth : '1px !important'
									 		},
									 		xtype:'toolbar',
									 		name:'search',
									 		dock:'top',
									 		items:[express_starttime,express_endtime,type,{
								            	text:'查询',
								            	iconCls:'Find',
								            	handler:me.onSearch
								            }]
									 	}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '查看样本信息',
										iconCls : 'Find',
										handler : me.onFind
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
						window.location.href = "bacera/identify/exportIdentifyInfo.do?"+identifyExpress ;
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
