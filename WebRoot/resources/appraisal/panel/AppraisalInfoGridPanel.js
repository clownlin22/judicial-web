/**
 * 鉴定报告
 * 
 * @author yxb
 */
var i = 0;
Ext.define(
				"Rds.appraisal.panel.AppraisalInfoGridPanel",
				{
					extend : "Ext.grid.Panel",
					loadMask : true,
					viewConfig : {
						trackOver : false,
						stripeRows : false
					},
					pageSize : 25,
					initComponent : function() {
						var me = this;
						var entrust_per = Ext.create('Ext.form.field.Text', {
							name : 'entrust_per',
							labelWidth : 60,
							width : 200,
							fieldLabel : '委托人',
							emptyText : '委托人名称',
						});
						var entrust_num = Ext.create('Ext.form.field.Text', {
							name : 'entrust_num',
							labelWidth : 70,
							width : 200,
							fieldLabel : '委托人函号',
							emptyText : '委托人函号',
						});
						var case_number = Ext.create('Ext.form.field.Text', {
							name : 'case_number',
							labelWidth : 70,
							width : 200,
							fieldLabel : '案例编号',
							emptyText : '案例编号',
						});
						var name = Ext.create('Ext.form.field.Text', {
							name : 'name',
							labelWidth : 70,
							width : 200,
							fieldLabel : '被鉴定人',
							emptyText : '名字或身份号',
						});
						var id_card = Ext.create('Ext.form.field.Text', {
							name : 'id_card',
							labelWidth : 60,
							width : 200,
							fieldLabel : '被鉴定人身份证号'
						});
						var username = Ext.create('Ext.form.field.Text', {
							name : 'username',
							labelWidth : 60,
							width : 200,
							fieldLabel : '归属人',
							emptyText:'归属人名称'
						});
						var accept_date_starttime = new Ext.form.DateField({
							id:'accept_date_starttime',
							name : 'accept_date_starttime',
							width : 200,
							fieldLabel : '受 理 日 期  从',
							labelWidth : 80,
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							value : Ext.Date.add(new Date(), Ext.Date.DAY, -7),
							maxValue : new Date(),
							listeners:{
								'select':function(){
									var start = Ext.getCmp('accept_date_starttime').getValue();
				                    var endDate = Ext.getCmp('accept_date_endtime').getValue();
				                    if (start > endDate) {
				                        Ext.getCmp('accept_date_starttime').setValue(endDate);
				                    }
								}
							}
						});
						var accept_date_endtime = new Ext.form.DateField({
							id:'accept_date_endtime',
							name : 'accept_date_endtime',
							width : 135,
							labelWidth : 20,
							fieldLabel : ' 到 ',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value : Ext.Date.add(new Date(), Ext.Date.DAY),
							listeners:{
								'select':function(){
									var start = Ext.getCmp('accept_date_starttime').getValue();
				                    var endDate = Ext.getCmp('accept_date_endtime').getValue();
				                    if (start > endDate) {
				                        Ext.getCmp('accept_date_starttime').setValue(endDate);
				                    }
								}
							}
						});
						var identify_date_start = new Ext.form.DateField({
							id:'identify_date_start1',
							name : 'identify_date_start',
							width : 200,
							fieldLabel : '鉴定日期开始',
							labelWidth : 80,
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							listeners:{
								'select':function(){
									var start = Ext.getCmp('identify_date_start1').getValue();
				                    var endDate = Ext.getCmp('identify_date_end1').getValue();
				                    if (start > endDate) {
				                        Ext.getCmp('identify_date_start1').setValue(endDate);
				                    }
								}
							}
						});
						var identify_date_end = new Ext.form.DateField({
							id:'identify_date_end1',
							name : 'identify_date_end',
							width : 200,
							labelWidth : 80,
							fieldLabel : '鉴定日期结束',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							//value : Ext.Date.add(new Date(), Ext.Date.DAY,7),
							listeners:{
								'select':function(){
									var start = Ext.getCmp('identify_date_start1').getValue();
				                    var endDate = Ext.getCmp('identify_date_end1').getValue();
				                    if (start > endDate) {
				                        Ext.getCmp('identify_date_start1').setValue(endDate);
				                    }
								}
							}
						});
						var flag_status = new Ext.form.field.ComboBox({
							fieldLabel : '审核状态',
							width : 200,
							labelWidth : 60,
							editable : false,
							triggerAction : 'all',
							displayField : 'Name',
							labelAlign : 'left',
							valueField : 'Code',
							store : new Ext.data.ArrayStore(
									{
										fields : ['Name','Code' ],
										data : [['全部',0],['未审核',2 ],['登记已审核',3 ],['已回退',1 ],['录入待审核',4],['录入审核未通过',5],['录入审核通过',6] ]
									}),
							value : '',
							mode : 'local',
							name : 'flag_status',
							value: 0
						});
						var advice = new Ext.form.field.ComboBox({
//								xtype : 'combo',
								labelWidth : 60,
								hideLabel : true,
								name : 'advice',
								emptyText:'鉴定意见',
//								store : Ext
//										.create(
//												"Ext.data.Store",
//												{
//													fields : [ {
//														name : "key",
//														type : "key"
//													}, {
//														name : "value",
//														type : "value"
//													} ],
//													pageSize : 10,
//													// autoLoad: true,
//													proxy : {
//														type : "ajax",
//														url : "appraisal/info/queryAdvice.do",
//														reader : {
//															type : "json"
//														}
//													}
//												}),
								fieldLabel : "关键字",
								displayField : 'value',
								valueField : 'key',
								typeAhead : false,
								hideTrigger : true,
								minChars : 1,
								matchFieldWidth : true,
								listConfig : {
									loadingText : '正在查找...',
									emptyText : '没有找到匹配的数据'
								}
							});
						me.store = Ext
								.create(
										'Ext.data.Store',
										{
											fields : [ 'case_id',
													'entrust_per',
													'entrust_num',
													'case_number',
													'entrust_matter',
													'identify_stuff',
													'accept_date',
													'identify_place',
													'identify_per_sex',
													'identify_per_both',
													'identify_per_address',
													'identify_date_start',
													'identify_date_end',
													'flag_status',
													'identify_per_name',
													'identify_per_idcard',
													'case_in_person',
													'recrive_id',
													'judgename'],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'appraisal/info/queryAllPage.do',
												params : {},
												reader : {
													type : 'json',
													root : 'data',
													totalProperty : 'total'
												}
											},
											listeners : {
												'beforeload' : function(ds,
														operation, opt) {
													me.getSelectionModel()
															.clearSelections();
													Ext
															.apply(
																	me.store.proxy.params,
																	{
																		entrust_per : entrust_per
																				.getValue(),
																		entrust_num : entrust_num
																				.getValue(),
																		name : name
																				.getValue(),
																		accept_date_starttime : dateformat(accept_date_starttime
																				.getValue()),
																		accept_date_endtime : dateformat(accept_date_endtime
																				.getValue()),
																		username : username
																				.getValue(),
																		identify_date_start : dateformat(identify_date_start.getValue()),
																		identify_date_end : dateformat(identify_date_end.getValue()),
																		flag_status : flag_status.getValue(),
																		advice:advice.getValue(),
																		case_number:case_number.getValue()
																	});
												}
											}
										});

						me.selModel = Ext.create('Ext.selection.CheckboxModel',
								{
								});
						me.bbar = Ext.create('Ext.PagingToolbar', {
							store : me.store,
							pageSize : me.pageSize,
							displayInfo : true,
							displayMsg : "第 {0} - {1} 条  共 {2} 条",
							emptyMsg : "没有符合条件的记录"
						});
						me.columns = [ Ext.create("Ext.grid.RowNumberer", {
							text : '序号',
							width : '3%',
							menuDisabled : true
						}), {
							text : '委托人',
							dataIndex : 'entrust_per',
							flex: 1,
							menuDisabled : true
						},{
							text : '案例编号',
							dataIndex : 'case_number',
							flex: 1,
							menuDisabled : true
						}, {
							text : '委托函号',
							dataIndex : 'entrust_num',
							flex: 1,
							menuDisabled : true
						}, {
							text : '被鉴定人',
							dataIndex : 'identify_per_name',
							flex: 1,
							menuDisabled : true
						}, {
							text : '被鉴定人身份证号',
							dataIndex : 'identify_per_idcard',
							flex: 1,
							menuDisabled : true
						}, {
							text : '案例归属人',
							dataIndex : 'case_in_person',
							flex: 1,
							menuDisabled : true
						}, {
							text : '受理日期',
							dataIndex : 'accept_date',
							flex: 1,
							menuDisabled : true
						}, {
							text : '鉴定开始日期',
							dataIndex : 'identify_date_start',
							flex: 1,
							menuDisabled : true
						}, {
							text : '状态',
							dataIndex : 'flag_status',
							flex: .5,
							menuDisabled : true,
							renderer : function(value) {
								switch (value) {
								case '1':
									return "<span style='color:red'>已回退</span>";;
									break;
								case '2':
									return "未审核";
									break;
								case '3':
									return "<span style='color:green'>登记已审核</span>";
									break;
								case '4':
									return "<span style='color:blue'>录入待审核</span>";
									break;
								case '5':
									return "<span style='color:red'>录入审核未通过</span>";
									break;
								case '6':
									return "<span style='color:green'>录入审核通过</span>";
									break;
								default:
									return "未审核";
								}
							}
						} ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [ entrust_per, entrust_num,
											accept_date_starttime,
											accept_date_endtime]
								},
								{
									style : {
										borderTopWidth : '0px !important',
										borderBottomWidth : '0px !important'
									},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [ username, name,
											identify_date_start,
											identify_date_end]
								},{
									style : {
										borderTopWidth : '0px !important',
										borderBottomWidth : '1px !important'
									},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [case_number,flag_status,'<span style="color:black;">鉴定意见:</span>', advice, {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											}]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '鉴定登记',
										iconCls : 'Add',
										handler : me.onInsert
									}, {
										text : '修改登记信息',
										iconCls : 'Pageedit',
										handler : me.onUpdate
									}, {
										text : '审核登记信息',
										iconCls : 'Bookopenmark',
										handler : me.onDelete
									}, {
										text : '报告录入',
										iconCls : 'Pageadd',
										handler : me.onReportPrint,
										hidden : true
									} , {
										text : '修改录入信息',
										iconCls : 'Pageedit',
										handler : me.onUpdateIn,
										hidden : true
									},{
				                        text : '导出',
				                        iconCls : 'Pageexcel',
				                        handler : me.onExport
				                    } ]
								} ];
						me.store.load();
						me.callParent(arguments);
					},
					onExport:function(){
			            var me = this.up("gridpanel");
			            window.location.href = "appraisal/info/exportInfo.do?accept_date_starttime="
			            + dateformat(me.getDockedItems('toolbar')[0].getComponent('accept_date_starttime').getValue())
			            + "&accept_date_endtime=" + dateformat(me.getDockedItems('toolbar')[0].getComponent('accept_date_endtime').getValue());
			        },
					onReportPrint:function(){
						//弹出tab
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要录入的报告!");
							return;
						};
						if("4" == selections[0].get("flag_status"))
						{
							Ext.Msg.alert("提示", "该记录已录入");
							return;
						}
						if("6" == selections[0].get("flag_status"))
						{
							Ext.Msg.alert("提示", "该记录已录入审核通过，可打印");
							return;
						}
						if("3" != selections[0].get("flag_status"))
						{
							Ext.Msg.alert("提示", "该记录未审核通过，不能录入");
							return;
						}
						case_id_appraisal = selections[0].get("case_id");
						var tabs = this.up("tabpanel[region=center]");
						var tab = null;
						for ( var i = 0; i < tabs.items.getCount(); i++) {
							var item = tabs.items.get(i);
								if (item.getId() == 'appraisalInfoAdd') {
									tab = item;
							}
						}
						if(tab==null)
						{
							tab = tabs.add(Ext.create(
									'Rds.appraisal.panel.AppraisalInfoFormPanel', {
										id : 'appraisalInfoAdd',
										title : '报告录入',
										closable : true,
										listeners : {
											"beforedestroy" : function() {
												me.getStore().load();
											}
										}
									}));
						}
						tab.show();
					},
					onInsert : function() {
						var me = this.up("gridpanel");
						case_id_appraisal = '';
						ownpersonTemp="";
						var form = Ext.create("Rds.appraisal.panel.AppraisalBaseInfoFormPanel", {
							region : "center",
							grid : me
						});
						var win = Ext.create("Ext.window.Window", {
							title : '鉴定—登记',
							width : 600,
							iconCls : 'Add',
							height : 650,
							modal:true,
							layout : 'border',
							items : [ form ]
						});
						win.show();
					},
					onDelete : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要审核的记录!");
							return;
						};
						case_id_appraisal = selections[0].get("case_id");
						ownpersonTemp=selections[0].get("recrive_id");
						if(selections[0].get("flag_status")!=2 && selections[0].get("flag_status")!=1)
						{
							Ext.Msg.alert("提示", "该记录已审核过!");
							return;
						}
						var form = Ext.create("Rds.appraisal.panel.AppraisalExamineFormPanel",
								{
									region : "center",
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '鉴定—审核',
							width : 600,
							iconCls : 'Pageedit',
							height : 600,
							layout : 'border',
							items : [ form ]
						});
						win.show();
						form.loadRecord(selections[0]);
					},
					onUpdateIn:function(){
						//弹出tab
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要修改的报告!");
							return;
						};
						if("5" != selections[0].get("flag_status"))
						{
							Ext.Msg.alert("提示", "该记录状态不能修改");
							return;
						}
//						if("3" != selections[0].get("flag_status"))
//						{
//							Ext.Msg.alert("提示", "该记录未审核通过，不能录入");
//							return;
//						}
						case_id_appraisal = selections[0].get("case_id");
						var tabs = this.up("tabpanel[region=center]");
						var tab = null;
						for ( var i = 0; i < tabs.items.getCount(); i++) {
							var item = tabs.items.get(i);
								if (item.getId() == 'appraisalInfoUpdate') {
									tab = item;
							}
						}
						if(tab==null)
						{
							tab = tabs.add(Ext.create(
									'Rds.appraisal.panel.AppraisalInfoUpdatePanel', {
										id : 'appraisalInfoUpdate',
										title : '报告录入修改',
										closable : true,
										listeners : {
											"beforedestroy" : function() {
												me.getStore().load();
											}
										}
									}));
						}
						tab.show();
					},
					onUpdate : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
							return;
						};
						if("1" != selections[0].get("flag_status")  && "2" != selections[0].get("flag_status"))
						{
							Ext.Msg.alert("提示", "该记录状态不能修改");
							return;
						}
						ownpersonTemp=selections[0].get("recrive_id");
						case_id_appraisal = selections[0].get("case_id");
						var form = Ext.create("Rds.appraisal.panel.AppraisalBaseInfoFormPanel",
								{
									region : "center",
									grid : me
								});
						form.loadRecord(selections[0]);
						var win = Ext.create("Ext.window.Window", {
							title : '鉴定—修改',
							width : 600,
							modal:true,
							iconCls : 'Pageedit',
							height : 650,
							layout : 'border',
							items : [ form ]
						});
						win.show();
					},
					onSearch : function() {
						var me = this.up("gridpanel");
						me.store.currentPage = 1;
						me.getStore().load();

					}
				});