/**
 * 鉴定报告打印
 * 
 * @author yxb
 */
var i = 0;
Ext.define("Rds.appraisal.panel.AppraisalPrinterGridPanel",{
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
							name : 'accept_date_starttime',
							width : 200,
							fieldLabel : '受 理 日 期  从',
							labelWidth : 80,
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
//							value : Ext.Date.add(new Date(), Ext.Date.DAY, -7),
							maxValue : new Date()
						});
						var accept_date_endtime = new Ext.form.DateField({
							name : 'accept_date_endtime',
							width : 135,
							labelWidth : 20,
							fieldLabel : ' 到 ',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value : Ext.Date.add(new Date(), Ext.Date.DAY)
						});
						var identify_date_start = new Ext.form.DateField({
							name : 'identify_date_start',
							width : 200,
							fieldLabel : '鉴定日期开始',
							labelWidth : 80,
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d'
						});
						var identify_date_end = new Ext.form.DateField({
							name : 'identify_date_end',
							width : 200,
							labelWidth : 80,
							fieldLabel : '鉴定日期结束',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d'
						});
						
						me.store = Ext
								.create(
										'Ext.data.Store',
										{
											fields : [ 'case_id',
													'entrust_per',
													'entrust_num',
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
													'recrive_id'],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'appraisal/info/queryAllPage.do',
												params : {flag_status:'6'},
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
																		identify_date_end : dateformat(identify_date_end.getValue())
																	});
												}
											}
										});

						me.selModel = Ext.create('Ext.selection.CheckboxModel',
								{
//									mode : 'SINGLE'
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
											accept_date_endtime
											]
								},
								{
									style : {
										borderTopWidth : '0px !important',
										borderBottomWidth : '1px !important'
									},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [ username, name,
											identify_date_start,
											identify_date_end, {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											} ]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [ 
//									          {
//										text : '修改录入信息',
//										iconCls : 'Pageedit',
//										handler : me.onUpdate
//									}, 
//									{
//										text : '审核录入信息',
//										iconCls : 'Bookopenmark',
//										handler : me.onExamine
//									},
									{
										text : '报告打印',
										iconCls : 'Bookopenmark',
										handler : me.onReportPrint
									}]
								} ];
						me.store.load();
						me.callParent(arguments);
					},
					onReportPrint:function(){
						if (!!window.ActiveXObject || "ActiveXObject" in window)  {

							var print_print=function(me){
								var iframe = document.getElementById("case_printer");
								iframe.contentWindow.focus();
								var a=document.getElementById("PrintAX");
								 a.Mar_left=20;
								 a.Mar_Top=15;
								 a.Mar_Right=10;
								 a.Mar_Bottom=15;
								 a.Orientation="纵向";
								 a.Paper_Size="A4";
								 a.ApplySetup();
								 a.PrintWithOutSetupPrintWithOutByID("case_printer");
								 win.close();
						    }
							var print_chanel=function(){
								win.close();
							}
							var me = this.up("gridpanel");
							var selections = me.getView().getSelectionModel().getSelection();
							if (selections.length < 1 || selections.length > 1) {
								Ext.Msg.alert("提示", "请选择一条需要审核的记录!");
								return;
							};
							if("6" != selections[0].get("flag_status"))
							{
								Ext.Msg.alert("提示", "该记录不能打印");
								return;
							}
							var case_id = selections[0].get("case_id");
							var win=Ext.create("Ext.window.Window",{
								title : "打印预览",
								iconCls : 'Find',
								layout:"auto",
								maximized:true,
								maximizable :true,
								modal:true,
								bodyStyle : "background-color:white;",
								html:"<iframe width=100% height=100% id='case_printer' src='"+"appraisal/printer/getPrinterInfo.do?case_id="+case_id+"'></iframe>",
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
						}else{
							Ext.Msg
							.alert(
									"提示",
									"该浏览器不支持打印,请使用IE!");
						}
					
					},
					onExamine : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要审核的记录!");
							return;
						};
						var case_id = selections[0].get("case_id");
						var win=Ext.create("Ext.window.Window",{
							title : "审核预览",
							iconCls : 'Find',
							layout:"auto",
							maximized:true,
							maximizable :true,
							modal:true,
							bodyStyle : "background-color:white;",
							html:"<iframe width=100% height=100% src='"+"appraisal/printer/getPrinterInfo.do?case_id="+case_id+"'></iframe>",
							buttons:[
										 {
												text : '审核通过',
												iconCls : 'Disk',
												handler : function(){
													checkTrue(case_id,win,me);
												}
										}, {
												text : '审核不通过',
												iconCls : 'Cancel',
												handler : function(){
													 checkFalse(case_id,win,me);
												}
											} 
									]
								});
						   win.show();  
					},
					onUpdate : function() {
						//弹出tab
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要修改的报告!");
							return;
						};
						if("4" != selections[0].get("flag_status"))
						{
							Ext.Msg.alert("提示", "该记录尚未录入");
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
								if (item.getId() == 'appraisalInfoAdd') {
									tab = item;
							}
						}
						if(tab==null)
						{
							tab = tabs.add(Ext.create(
									'Rds.appraisal.panel.AppraisalInfoUpdatePanel', {
										id : 'appraisalInfoAdd',
										title : '报告录入修改',
										closable : true
//										,
//										listeners : {
//											"beforedestroy" : function() {
//												me.getStore().load();
//											}
//										}
									}));
						}
						tab.show();
					},
					onSearch : function() {
						var me = this.up("gridpanel");
						me.store.currentPage = 1;
						me.getStore().load();

					}
				});

function checkTrue(case_id,win,me){
	Ext.Ajax.request({
		url : 'appraisal/info/checkExamine.do',
		params : {case_id:case_id,flag_status:'6'},
		async : false,
		success : function(response, opts) {
			response = Ext.JSON.decode(response.responseText)
			if (response.result == true) {  
             	Ext.MessageBox.alert("提示信息", response.message);
            	win.close();
            	me.getStore().load();
             }else { 
             	Ext.MessageBox.alert("错误信息", response.message);
             } 
		},
		failure : function(response, opts) {
		}
	});
}
function checkFalse(case_id,win,me)
{
	Ext.Ajax.request({
		url : 'appraisal/info/checkExamine.do',
		params : {case_id:case_id,flag_status:'5'},
		async : false,
		success : function(response, opts) {
			response = Ext.JSON.decode(response.responseText)
			if (response.result == true) {  
             	Ext.MessageBox.alert("提示信息", response.message);
            	win.close();
            	me.getStore().load();
             }else { 
             	Ext.MessageBox.alert("错误信息", response.message);
             } 
		},
		failure : function(response, opts) {
		}
	});
}