checkMailRate=function(mail_code,mail_type){
	var win = Ext.create("Ext.window.Window", {
		title : "快递信息（快递单号：" + mail_code + "）",
		width : 700,
		iconCls : 'Find',
		height : 350,
		layout : 'border',
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
										'mail_code' : mail_code,
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

Ext
		.define(
				"Rds.mail.panel.RdsMailCaseGridPanel",
				{
					extend : "Ext.grid.Panel",
					loadMask : true,
					viewConfig : {
						trackOver : false,
						stripeRows : false
					},
					region:'center',
					pageSize : 25,
					plugins: [{
				            ptype: 'rowexpander',
				            rowBodyTpl : [
				                '<p><b>样本信息:</b> {sample_str}</p>',
				                '<p><b>录入时间:</b> {case_time} &nbsp;<b>付款时间:</b> {fee_date}</p>',
				                '<p><b>快递单号:</b> {mail_codes}</p>'
				            ]
				    }],
					initComponent : function() {
						var me = this;
						var case_code = Ext.create('Ext.form.field.Text', {
							name : 'casecode',
							labelWidth : 60,
							width : 200,
							regexText : '请输入案例编号',
							fieldLabel : '案例编号'
						});
						var receiver = Ext.create('Ext.form.field.Text', {
							name : 'receiver',
							labelWidth : 60,
							width : 145,
							fieldLabel : '归属人'
						});
						var client = Ext.create('Ext.form.field.Text', {
							name : 'client',
							labelWidth : 60,
							width : 145,
							fieldLabel : '委托人'
						});
						var mail_code = Ext.create('Ext.form.field.Text', {
							name : 'mail_code',
							labelWidth : 60,
							width : 145,
							fieldLabel : '快递单号'
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
						var storeModel = Ext.create(
						        'Ext.data.Store',
						        {
						          model:'model',
						          proxy:{
						        	type: 'jsonajax',
						            actionMethods:{read:'POST'},
						            url:'mail/dicvalues/getMailTypes.do',
						            reader:{
						              type:'json',
						              root:'data'
						            }
						          },
						          autoLoad:true,
						          remoteSort:true,
						          listeners : {
										'load' : function() {
											case_type.select(this.getAt(0));
										}
									}
						        }
						);
						var case_type = Ext.create('Ext.form.ComboBox', {
							fieldLabel : '案例类型',
							width : 200,
							labelWidth : 60,
							editable : false,
							triggerAction : 'all',
							valueField :"key",  
					        displayField: "value", 
							store: storeModel,
							mode : 'local',
							value:'alcohol',
							// typeAhead: true,
							name : 'case_type',
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
											fields : [ 'case_id', 'case_code',"mail_codes",
													"client","fee_remark","fee_status","fee_date","fee_type","sample_str","receiver_name","areaname","case_time"
													,"mail_state","mail_count","case_type"],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'mail/getCaseInfo.do',
												params : {
													start : 0,
													limit : 25
												},
												reader : {
													type : 'json',
													root : 'caseinfos',
													totalProperty : 'count'
												}
											},
											listeners : {
												'beforeload' : function(ds,
														operation, opt) {
													Ext
													.apply(
															me.store.proxy.extraParams,{
																case_code:trim(case_code.getValue()),
																case_type:case_type.getValue(),
																receiver:trim(receiver.getValue()),
																client:trim(client.getValue()),
																mail_code:trim(mail_code.getValue()),
																starttime : dateformat(starttime.getValue()),
																endtime : dateformat(endtime.getValue()),
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
									width : 250
								},
								{
									text : '委托人',
									dataIndex : 'client',
									menuDisabled : true,
									width : 120
								},
								{
									text : '归属人',
									dataIndex : 'receiver_name',
									menuDisabled : true,
									width : 120
								},
								{
									text : '案例归属地',
									dataIndex : 'areaname',
									menuDisabled : true,
									width : 200
								},
								{
									text : '付款信息',
									dataIndex : 'fee_status',
									menuDisabled : true,
									width : 130,
									renderer : function(value, cellmeta, record, rowIndex, columnIndex,
											store) {
										var fee_type = record.data["fee_type"];
										var type_remark="正常";
										if(fee_type==1){
											type_remark="先出报告后付款";
										}
										if(fee_type==2){
											type_remark="免单";
										}
										if (value ==0||value==2) {
											return "<span style='color:green'>已付款("+type_remark+")</span>";
										} else {
											return "<span style='color:red'>未付款("+type_remark+")</span>";
										}
									}
								},
								{
									text : '是否邮寄',
									dataIndex : 'mail_state',
									menuDisabled : true,
									width : 120,
									renderer : function(value, cellmeta, record, rowIndex, columnIndex,
											store) {
										var mail_count = record.data["mail_count"];
										if (value ==1) {
											return "<span style='color:green'>已邮寄("+mail_count+"份)</span>";
										} else {
											return "未邮寄";
										}
									}
								},
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [
											 case_code,receiver,client,mail_code
											 ]
								}, {
									xtype : 'toolbar',
									name : 'search',
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '1px !important'
								    },
									items : [case_type,starttime,endtime, {
										text : '查询',
										iconCls : 'Find',
										handler : me.onSearch
									} ]
								},{
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '新增快递单号',
										iconCls : 'Pageadd',
										handler : me.onInsert
									}, {
										text : '修改快递单号',
										iconCls : 'Pageedit',
										handler : me.onUpdate
									}, {
										text : '删除错误单号',
										iconCls : 'Delete',
										handler : me.onDelete
									}, {
										text : '邮寄信息查询',
										iconCls : 'Find',
										handler : me.onFind
									}
									]
								} ];

						me.callParent(arguments);
					},
					onInsert : function() {
						var me = this.up("gridpanel");
						var form = Ext.create(
								"Rds.mail.form.MailCaseInsertForm",
								{
									region : "center",
									autoScroll : true,
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '邮寄添加',
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
						var form = Ext.create(
								"Rds.mail.form.MailCaseUpdateForm",
								{
									region : "center",
									autoScroll : true,
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '邮寄修改',
							width : 900,
							iconCls : 'Pageedit',
							height : 550,
							layout : 'border',
							items : [ form ]
						});
						win.show();
					},
					onDelete:function(){
						var me = this.up("gridpanel");
						Ext.MessageBox.prompt(
								"快递单号",
								"请输入快递单号",
								function(id,msg) {
									if(id == 'ok')
									{
										if(msg=='')
										{
											Ext.Msg.alert("提示","请填写快递单号!");
											return;
										}
										if(msg.length>30)
										{
											alert("快递单太长了...换个重新来吧...");
											return;
										}
										Ext.Ajax.request({  
											url:"mail/deleteMailInfo.do", 
											method: "POST",
											headers: { 'Content-Type': 'application/json' },
											jsonData: {
												mail_code:msg
											}, 
											success: function (response, options) {  
												response = Ext.JSON.decode(response.responseText); 
								                 if (response == true) {  
								                 	Ext.MessageBox.alert("提示信息", "删除成功");
								                 	me.getStore().load();
								                 }else { 
								                 	Ext.MessageBox.alert("错误信息", "删除失败");
								                 } 
											},  
											failure: function () {
												Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
											}
								    	       
								      	});
									}
									
						});
					},
					onFind:function(){
						var me = this;
						var grid = me.up("gridpanel");
						var selections = grid.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要查看的记录!");
							return;
						};
						var win = Ext.create("Ext.window.Window", {
							title : '快递信息',
							width : 600,
							iconCls : 'Pageedit',
							height : 550,
							layout : 'border',
							items : [Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 600,
								height : 550,
								frame : false,
								viewConfig : {
									forceFit : true,
									stripeRows : true// 在表格中显示斑马线
								},
								store : Ext.create(
										'Ext.data.Store',
										{
											fields : ["mail_id","mail_code","mail_type","mail_typeStr","mail_per","mail_pername","addressee","mail_time"],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												params : {
													'case_id' : selections[0].get("case_id"),
													"case_type":selections[0].get("case_type")
												},
												url : 'mail/checkMails.do',
												reader : {
													type : 'json',
												}
											}
										}),
								columns : [{
									text : '快递单号',
									dataIndex : 'mail_code',
									width : 100
								},{
									text : '快递类型',
									dataIndex : 'mail_typeStr',
									menuDisabled : true,
									width : 100,
								}, {
									text : '收件人',
									dataIndex : 'addressee',
									menuDisabled : true,
									width : 100,
								},{
									text : '快递时间',
									dataIndex : 'mail_time',
									menuDisabled : true,
									width : 100
								},{
									text : '邮寄人',
									dataIndex : 'mail_pername',
									menuDisabled : true,
									width : 100
								},{
									header : "操作",
									dataIndex : 'mail_code',
									width : 100,
									menuDisabled : true,
									renderer : function(value, cellmeta, record,
											rowIndex, columnIndex, store) {
										var mail_type = record.data["mail_type"];
										if(mail_type=='ziqu'){
											return "";
										}else{
											return "<a href='#' onclick=\"checkMailRate('"
											+ value
											+ "','"
											+ mail_type
											+ "')\"  >查询</a>";
										}
									}
								}],
								listeners : {
									'afterrender' : function() {
										this.store.load();
									}
								}
					      })]
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
