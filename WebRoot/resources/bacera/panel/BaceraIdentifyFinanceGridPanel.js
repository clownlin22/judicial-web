Ext.define("Rds.bacera.panel.BaceraIdentifyFinanceGridPanel",{
					extend : "Ext.grid.Panel",
					loadMask : true,
					viewConfig : {
						trackOver : false,
						stripeRows : false
					},
					region:'center',
					pageSize : 25,
					  selType: 'rowmodel',
					    plugins: [ Ext.create('Ext.grid.plugin.CellEditing', {
					                  clicksToEdit: 1,
					                  listeners:{  
					                      'edit':function(e,s){  
				                    	   var status = s.record.data.status;
				                    	   if(status==0)
					                    	{
				                    		   alert("该案例已确认，修改无效!");
				                    		   return;
					                    	}
					                      	Ext.Ajax.request({  
												url:"finance/chargeStandard/updateFinanceInfo.do", 
												method: "POST",
												headers: { 'Content-Type': 'application/json' },
												jsonData: {
													case_id:s.record.data.case_id,
													case_code:s.record.data.case_code,
													stand_sum:s.record.data.stand_sum,
													real_sum:s.record.data.real_sum,
													return_sum:s.record.data.return_sum,
													paragraph_time:Ext.util.Format.date(s.record.data.paragraph_time, 'Y-m-d'),
													discount_price:s.record.data.discount_price,
													case_type:'亲子鉴定',
													finance_type:s.record.data.type,
													finance_account:s.record.data.finance_account==null?'':s.record.data.finance_account,
													finance_remark:s.record.data.finance_remark==null?'':s.record.data.finance_remark,
													remittance_name:s.record.data.remittance_name==null?'':s.record.data.remittance_name,
													remittance_time:Ext.util.Format.date(s.record.data.remittance_time, 'Y-m-d')
												}, 
												success: function (response, options) {  
													response = Ext.JSON.decode(response.responseText); 
									                 if (response==false) {  
										                 Ext.MessageBox.alert("错误信息", "修改收费失败，请查看");
									                 }
//									                 else{
//									                	 Ext.MessageBox.alert("提示信息", "保存成功！");
//									                 }
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
						var remittanceAccountStore=Ext.create('Ext.data.Store',
						        {
						          fields:['accountName'],
						          proxy:{
						    		type: 'jsonajax',
						    		actionMethods:{read:'POST'},
						            url:'judicial/remittance/queryall.do',
						            reader:{
						              type:'json',
						              root:'data'
						            }
						          },
						          autoLoad:true,
						          remoteSort:true						
						        }
						   );
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
						var samplename = Ext.create('Ext.form.field.Text',{
							name:'samplename',
							labelWidth:60,
							width:160,
							fieldLabel:'样本姓名'
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
						var client = Ext.create('Ext.form.field.Text',{
							name:'client',
							labelWidth:60,
							width:140,
							fieldLabel:'委托人'
						});
						var acounttype=Ext.create(
								'Ext.data.Store',
								{
									fields:['remark'],
									proxy:{
										type: 'jsonajax',
										actionMethods:{read:'POST'},
										url:'judicial/bank/queryallpage.do',
										params:{
											initials:""
										},
										reader:{
											type:'json',
											root:'data'
										}
									},
									autoLoad:true,
									remoteSort:true						
								}
						);
						var paragraphtime_starttime = new Ext.form.DateField({
							id:'identify_starttime_finance_paragraphtime',
							name : 'paragraphtime_starttime',
							width : 180,
							fieldLabel : '到账日期从',
							labelWidth : 70,
							labelAlign : 'left',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							listeners:{
								'select':function(){
									var start = Ext.getCmp('identify_starttime_finance_paragraphtime').getValue();
									var endDate = Ext.getCmp('identify_endtime_finance_paragraphtime').getValue();
									if (start > endDate) {
										Ext.getCmp('identify_starttime_finance_paragraphtime').setValue(endDate);
									}
								}
							}
						});
						var paragraphtime_endtime = new Ext.form.DateField({
							id:'identify_endtime_finance_paragraphtime',
							name : 'paragraphtime_endtime',
							width : 135,
							labelWidth : 20,
							fieldLabel : ' 到 ',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							listeners:{
								'select':function(){
									var start = Ext.getCmp('identify_starttime_finance_paragraphtime').getValue();
									var endDate = Ext.getCmp('identify_endtime_finance_paragraphtime').getValue();
									if (start > endDate) {
										Ext.getCmp('identify_starttime_finance_paragraphtime').setValue(endDate);
									}
								}
							}
						});
						me.store = Ext.create('Ext.data.Store',
						{
							fields : [ 'case_id', 'case_code','case_areacode','receiver_area','case_receiver','remark',
									'accept_time','close_time','client','address','case_in_per','receiver_id',
									'case_in_pername','phone','sample_in_time','is_delete','reportif','agentname',
									'stand_sum','real_sum','return_sum','status','paragraph_time',
									'discount_price','finance_remark','finance_account',
									'expressnum','expresstype','recive','type','fandm',
									'child','id_card','birth_date','sample_count',
									'entrustment_time','entrustment_matter','remittance_name','remittance_time'],
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
									Ext.apply(me.store.proxy.extraParams,{								
												endtime : dateformat(Ext
														.getCmp(
														'endtime_query2')
												               .getValue()),	
												starttime : dateformat(Ext
														.getCmp(
																'starttime_query2')
														.getValue()),
												receiver : trim(Ext
																.getCmp(
																		'receiver_query2')
																.getValue()),
												case_code : trim(Ext
														.getCmp(
																'case_code_query2')
														.getValue()),
												is_delete : 0 ,
												agent:agent.getValue(),
												case_in_per:case_in_per.getValue(),
												reportif:reportif.getValue(),
												paragraphtime_starttime:dateformat(paragraphtime_starttime.getValue()),
						        				paragraphtime_endtime:dateformat(paragraphtime_endtime.getValue()),
						        				type:type.getValue(),
						        				client:trim(client.getValue()),
						        				samplename:trim(samplename.getValue()),
						        				usercode:usercode
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
						me.columns = [ {
										text : '案例条形码',
										dataIndex : 'case_code',
										menuDisabled : true,
										width : 100,
										renderer : function(value, cellmeta,
												record, rowIndex, columnIndex,
												store) {
											var confim_flag= record.data["status"];
											if (confim_flag == 0) {
												return "<div style=\"color: red;\">"
														+ value + "</div>"
											} else {
												return value;
											}
										}
						              }, {
										text : '委托人',
										dataIndex : 'client',
										menuDisabled : true,
										width : 120
									},{
									  text: '标准金额',	dataIndex: 'stand_sum', menuDisabled:true, width : 80
									  },{
										  text: '实收金额',	dataIndex: 'real_sum', menuDisabled:true, width : 80,
					                	  editor:new Ext.form.NumberField()
						              },{ text: '回款金额',	dataIndex: 'return_sum', menuDisabled:true, width : 80,
					                	  editor:new Ext.form.NumberField()
					                  }, { text: '到款时间',	dataIndex: 'paragraph_time', menuDisabled:true, width : 80,
					                	  renderer:Ext.util.Format.dateRenderer('Y-m-d'),
					                      editor:new Ext.form.DateField({
					                           format: 'Y-m-d'
					                         })
					                  },{
					                	  text: '账户类型',	dataIndex: 'finance_account', menuDisabled:true, width : 150,
					                	  editor:new Ext.form.ComboBox({
					          				autoSelect : true,
					          				editable:true,
					          		        name:'account_type',
					          		        triggerAction: 'all',
					          		        queryMode: 'local', 
					          		        emptyText : "请选择",
					          		        selectOnTab: true,
					          		        store: acounttype,
					          		        maxLength: 50,
					          		        fieldStyle: me.fieldStyle,
					          		        displayField:'remark',
					          		        valueField:'remark',
					          		        listClass: 'x-combo-list-small'
					          			})
					                  },{
					                	  text: '汇款账户',	dataIndex: 'remittance_name', menuDisabled:true, width : 150,
					                	  editor:new Ext.form.ComboBox({
					            				autoSelect : true,
					            				editable:true,
					            		        name:'remittance_name',
					            		        triggerAction: 'all',
					            		        queryMode: 'local', 
					            		        emptyText : "请选择",
					            		        selectOnTab: true,
					            		        store: remittanceAccountStore,
					            		        maxLength: 50,
					            		        fieldStyle: me.fieldStyle,
					            		        displayField:'accountName',
					            		        valueField:'accountName',
					            		        listClass: 'x-combo-list-small'
					            			})
					                    },{
					                    	text: '汇款时间',	dataIndex: 'remittance_time', menuDisabled:true, width : 110,
					                    	renderer:Ext.util.Format.dateRenderer('Y-m-d'),
					                        editor:new Ext.form.DateField({
					                             format: 'Y-m-d'
					                           })
					                    },{
					            			header : '优惠价格',
					            			dataIndex : 'discount_price',
					            			width : 80,
					            			editor:new Ext.form.NumberField()
					            		 },{ 
					            			 text: '财务备注',	dataIndex: 'finance_remark', menuDisabled:true, width : 120,
						                	  editor:'textfield'
						                 },{
												text : '样本数',
												dataIndex : 'sample_count',
												menuDisabled : true,
												width : 60
										 },{
											text : '父母亲',
											dataIndex : 'fandm',
											menuDisabled : true,
											width : 150
										},{
											text : '孩子',
											dataIndex : 'child',
											menuDisabled : true,
											width : 150
										},{
											text : '身份证',
											dataIndex : 'id_card',
											menuDisabled : true,
											width : 150
										},{
											text : '出生日期',
											dataIndex : 'birth_date',
											menuDisabled : true,
											width : 150
										},{
											text : '受理日期',
											dataIndex : 'accept_time',
											menuDisabled : true,
											width : 100
										},
										{
											text : '归属人',
											dataIndex : 'case_receiver',
											menuDisabled : true,
											width : 120
										},{
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
											text : '登记人',
											dataIndex : 'case_in_pername',
											menuDisabled : true,
											width : 100
										}, {
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
											text: '备注要求',	dataIndex: 'remark', menuDisabled:true, width : 300
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
												id : 'case_code_query2',
												labelWidth : 70,
												width : 155,
												fieldLabel : '案例条形码'
											},
											{
												xtype : 'textfield',
												fieldLabel : '归属人',
												width : 145,
												labelWidth : 50,
												id : 'receiver_query2',
												name : 'receiver'
											},agent,
											{
												xtype : 'datefield',
												name : 'starttime',
												id : 'starttime_query2',
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
																'starttime_query2')
																.getValue();
														Ext.getCmp('endtime_query2')
																.setMinValue(
																		start);
														var endDate = Ext
																.getCmp(
																		'endtime_query2')
																.getValue();
														if (start > endDate) {
															Ext
																	.getCmp(
																			'endtime_query2')
																	.setValue(
																			start);
														}
													}
												}
											},
											{
												xtype : 'datefield',
												id : 'endtime_query2',
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
																'starttime_query2')
																.getValue();
														var endDate = Ext
																.getCmp(
																		'endtime_query2')
																.getValue();
														if (start > endDate) {
															Ext
																	.getCmp(
																			'starttime_query2')
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
								 		items:[reportif,case_in_per,paragraphtime_starttime,paragraphtime_endtime]
								 	},{
								 		style : {
									        borderTopWidth : '0px !important',
									        borderBottomWidth : '1px !important'
									 		},
									 		xtype:'toolbar',
									 		name:'search',
									 		dock:'top',
									 		items:[type,samplename,client,{
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
							 			text:'我是案例编号',
							 			iconCls:'Find',
							 			handler:me.caseCode
							 		},{
							 			text:'确认案例',
							 			iconCls:'Pageedit',
							 			handler:me.onConfirm
							 		}
									]
								} ];

						me.callParent(arguments);
//						me.store.on("load",function(){
//							Ext.getCmp('totalBbar_identify_finance').setText("共 "+me.store.getCount()+" 条");
//						});
					},
					onConfirm:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要确认的记录!");
							return;
						}
						var ids='';
						for(var i = 0 ; i < selections.length ; i ++)
						{

							if(selections[i].get("real_sum")!=selections[i].get("return_sum"))
							{
								Ext.Msg.alert("提示", "确认记录中存在实收和回款金额不一致的案例,请查看！");
								return;
							}
							if((i+1)==selections.length)
								ids += "'" + selections[i].get("case_id")+"'";
							else
								ids += "'" + selections[i].get("case_id")+"',";
						}
						Ext.MessageBox.confirm("提示", "请核对需要确认选中记录，确认选是", function (btn) {
					        if("yes"==btn)
					        {
								Ext.Ajax.request({  
									url:"bacera/boneAge/caseFeeConfirm.do", 
									method: "POST",
									headers: { 'Content-Type': 'application/json' },
									jsonData: {
										ids:ids,
										status:0
									}, 
									success: function (response, options) {  
										response = Ext.JSON.decode(response.responseText); 
										if(response == true){
											Ext.MessageBox.alert("提示信息", "案例确认成功");
											me.getStore().load();}
										else
											Ext.MessageBox.alert("错误信息", "确认案例失败，请联系管理员");
									},  
									failure: function () {
										Ext.Msg.alert("提示", "确认案例失败<br>请联系管理员!");
									}
						      	});
					        }
						})
//						}
						
					},
					caseCode:function(){
						var me = this.up("gridpanel");
						var selections =  me.getView().getSelectionModel().getSelection();
						if(selections.length<1){
							Ext.Msg.alert("提示", "请选择需要查看的案例编号!");
							return;
						};
						var num="";
						for(var i = 0 ; i < selections.length ; i ++)
						{
							num += selections[i].get("case_code")+";";
//							if(i>0&&i%8==0) num +="<br>";
						}
						Ext.Msg.alert("我是案例编号", num);
					},
					onFind:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要查看的记录!");
							return;
						};
						if (selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要查看的记录!");
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
