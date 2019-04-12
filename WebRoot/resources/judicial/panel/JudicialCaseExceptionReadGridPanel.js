queryMailInfo = function(value, mail_type) {
	var win = Ext.create("Ext.window.Window", {
				title : "快递信息（快递单号：" + value + "）",
				width : 700,
				iconCls : 'Find',
				height : 450,
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
Ext.define("Rds.judicial.panel.JudicialCaseExceptionReadGridPanel",
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
							width : '20%',
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
						var other_type=Ext.create('Ext.form.ComboBox', {
							fieldLabel : '自定义异常类型',
							mode: 'local',   
							labelWidth : 100,
							editable:false,
							valueField :"key",  
							width : '20%',
					        displayField: "value",    
							name : 'other_type',
							store: storeModel
						});
						var receiver = Ext.create('Ext.form.field.Text', {
							name : 'receiver',
							labelWidth : 60,
							width : '20%',
							regexText : '请输入归属人',
							fieldLabel : '归属人'
						});
						var receiver_area = Ext.create('Ext.form.field.Text', {
							name : 'receiver_area',
							labelWidth : 60,
							width : '20%',
							regexText : '请输入归属地',
							fieldLabel : '归属地'
						});
						var client = Ext.create('Ext.form.field.Text', {
							name : 'receiver',
							labelWidth : 60,
							width : '20%',
							regexText : '请输入委托人',
							fieldLabel : '委托人'
						});
						var starttime=Ext.create('Ext.form.DateField', {
							name : 'starttime',
							width : '20%',
							labelWidth : 70,
							fieldLabel : '登记时间 从',
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
						var is_super_time=Ext.create('Ext.form.ComboBox', 
								{
							fieldLabel : '是否超过48小时',
							width : '20%',
							labelWidth : 100,
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
							name : 'is_super_time',
				        });
						var is_mail=Ext.create('Ext.form.ComboBox', 
								{
							fieldLabel : '是否邮寄',
							width : '20%',
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
							name : 'is_mail',
				        });
						var fee_state=Ext.create('Ext.form.ComboBox', {
							fieldLabel : '回款状态',
							width : '20%',
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
														'已确认',
														1 ],
												[
														'未确认',
														2 ]]
									}),
							value : 1,
							mode : 'local',
							name : 'fee_state',
//							readOnly:true
				        });
						var is_report=Ext.create('Ext.form.ComboBox', {
							fieldLabel : '能否出报告',
							width : '20%',
							labelWidth : 80,
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
														'能',
														0 ],
												[
														'不能',
														1 ] ]
									}),
							value : -1,
							mode : 'local',
							name : 'is_report',
						});
						var endtime=Ext.create('Ext.form.DateField', {
							name : 'endtime',
							width : '20%',
							labelWidth : 20,
							fieldLabel : '到',
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
						me.store = Ext.create('Ext.data.Store',{
								fields : [ 'case_id', 'case_code',"receiver_area","case_receiver","agent","remittance_id","fee_id",
										"fee_status","fee_type","is_super_time","mail_time","per_photo","sample_in_time",
										"verify_state","close_time","is_report","is_to_time",'client','accept_time'],
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
										Ext.apply(me.store.proxy.extraParams,{								
													endtime : dateformat(endtime.getValue()),	
													starttime : dateformat(starttime.getValue()),
													client : trim(client.getValue()),
													is_mail : is_mail.getValue(),
													case_code : trim(case_code.getValue()),
													is_report : is_report.getValue(),
													case_receiver : trim(receiver.getValue()),
													other_type : other_type.getValue(),
													fee_state : fee_state.getValue(),
													is_super_time : is_super_time.getValue(),
													receiver_area : trim(receiver_area.getValue())
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
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
											return "<a href='#'>"+ record.data["case_code"]+"</a>";
									},
									listeners:{
										'click':function(){ 
										var me = this.up("gridpanel");
										var selections = me.getView().getSelectionModel().getSelection();
										console.log(selections[0].get("fee_status"));
										if(0 != selections[0].get("fee_status"))
										{
											Ext.Msg.alert("提示", "该案例未汇款确认，不予查看!");
											return;
										}
										Ext.MessageBox.wait('正在操作','请稍后...');
										Ext.Ajax.request({  
											url:"judicial/caseException/getCaseInfo.do", 
											method: "POST",
											headers: { 'Content-Type': 'application/json' },
											jsonData: {
												case_id:selections[0].get("case_id"),
												case_code:selections[0].get("case_code"),
												fee_id:selections[0].get("fee_id")
											}, 
											success: function (response, options) {  
												response = Ext.JSON.decode(response.responseText); 
												var showMsg="<table style='line-height:20px;margin-left:20px;'>";
												showMsg += "<tr><td colspan='6' style='color:red'>基本信息</td></tr>";
												showMsg += "<tr><td>案例条形码:</td><td style='color:blue'>"
														+ response.case_code
														+ "</td>"
														+ "<td>受理时间:</td><td style='color:blue'>"
														+ response.accept_time
														+ "</td>"
														+ "<td>归属地:</td><td style='color:blue'>"
														+ response.areaname
														+ "</td></tr>";
												showMsg += "<tr><td>归属人:</td><td style='color:blue'>"
													+ response.case_receiver
													+ "</td>"
													+ "<td>代理:</td><td style='color:blue'>"
													+ response.agent
													+ "</td></tr>";
												showMsg += "<tr><td>委托人:</td><td style='color:blue'>"
													+ response.client
													+ "</td>"
													+ "<td>登记人:</td><td style='color:blue'>"
													+ response.case_in_pername
													+ "</td>"
													+ "<td>样本登记日期:</td><td style='color:blue'>"
													+ response.sample_in_time
													+ "</td></tr>";
												showMsg += "<tr><td>电话:</td><td style='color:blue'>"
													+ response.phone
													+ "</td>"
													+ "<td>模版:</td><td style='color:blue'>"
													+ (null==response.text?"":response.text)
													+ "</td></tr>";
												showMsg += "<tr><td colspan='1'>备注:</td>"
													+"<td colspan='5' style='color:blue'>"
													+ response.remark + "</td></tr>";
												showMsg += "<tr><td colspan='1'>样本信息:</td>"
													+"<td colspan='5' style='color:blue'>"
													+ response.sampleInfo + "</td></tr>";
												showMsg += "</table>";
												
												var experiment=(response.experimentInfo==null)?"":response.experimentInfo.split(",");
												showMsg += "<table style='line-height:20px;margin-left:20px;margin-top:5px;'>";
												showMsg += "<tr><td style='color:red'>实验结果</td></tr>";
												for(var i = 0 ; i < experiment.length ; i ++)
												{
													showMsg += "<tr><td>"+experiment[i]+"</td></tr>";
												}
												showMsg += "</table>";
												
												showMsg += "<table style='line-height:20px;margin-left:20px;margin-top:5px;'>";
												showMsg += "<tr><td colspan='6' style='color:red'>财务信息</td></tr>";
												showMsg += "<tr><td>标准金额:</td><td style='color:blue'>"
													+ response.stand_sum
													+ "</td>"
													+ "<td>实收金额:</td><td style='color:blue'>"
													+ response.real_sum
													+ "</td>"
													+ "<td>回款金额:</td><td style='color:blue'>"
													+ response.return_sum
													+ "</td></tr>";
												showMsg += "<tr><td>到帐时间:</td><td style='color:blue'>"
													+ (response.paragraphtime==null?"":response.paragraphtime)
													+ "</td>"
													+ "<td>账户类型:</td><td style='color:blue'>"
													+ (response.account==null?"":response.account)
													+ "</td>"
													+ "<td>类型:</td><td style='color:blue'>"
													+ (response.type=='1'?'先出报告后付款':'正常')
													+ "</td></tr>";
												showMsg += "<tr><td colspan='1'>财务备注:</td>"
													+"<td colspan='5' style='color:blue'>"
													+ (response.financeRemark==null?"":response.financeRemark) + "</td></tr>";
												showMsg += "</table>";

												showMsg += "<table style='line-height:20px;margin-left:20px;margin-top:5px;'>";
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
																  + response[i].mail_code+"</a></td><td>快递时间:<td style='color:blue'>" 
																  + response[i].time+"</td></td><td>快递类型:<td style='color:blue'>"
																  + response[i].mail_typename+"</td</td></tr>";
																  showMsg += "<tr><td>收件人:"+response[i].mail_per+"</td></tr>";
																  }
															}
													},
													failure: function () {
														Ext.Msg.alert("提示", "请求失败<br>请联系管理员!");
													}
												});
												showMsg += "</table>";
												
												showMsg += "<table style='line-height:20px;margin-left:20px;margin-top:5px;'>";
												showMsg += "<tr><td style='color:red'>自定义异常信息</td></tr>";
												Ext.Ajax.request({  
													url : 'judicial/caseException/getOtherException.do',
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
																  showMsg += "<tr><td>异常类型:"
																  + response[i].exception_type_str+"</td><td>异常描述:<td style='color:blue'>" 
																  + response[i].exception_desc+"</td></tr>";
																  showMsg += "<tr><td>异常时间:"
																  + response[i].exception_time+"</td><td>是否处理:<td style='color:blue'>" 
																  + (response[i].is_handle=='1'?'已处理':(response[i].is_handle=='0'?'未处理':'已删除'))+"</td></tr>";
																  }
															}
													},
													failure: function () {
														Ext.Msg.alert("提示", "请求失败<br>请联系管理员!");
													}
												});
												showMsg += "</table>";
												var win=Ext.create("Ext.window.Window", {
													width : 700,
													iconCls :'Pageadd',
													height : 600,
													modal:true,
													title:'案例信息',
													layout : 'border',
													html:showMsg,
													bodyStyle : "background-color:white;font-size:15px;font-family:'黑体'"
												});
												win.show();
												Ext.MessageBox.close();
											},  
											failure: function () {
												Ext.Msg.alert("提示", "请求失败<br>请联系管理员!");
											}
								      	});
										
									}  
									} 
								},{
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
								},{
									text : '案例归属地',
									dataIndex : 'receiver_area',
									menuDisabled : true,
									width : 200
								},{
									text : '委托人',
									dataIndex : 'client',
									menuDisabled : true,
									width : 120
								},{
									text : '回款状态',
									dataIndex : 'fee_status',
									menuDisabled : true,
									width : 250,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var fee_type= record.data["fee_type"];
										var is_super_time= record.data["is_super_time"];
										var color="red";
										var state="异常";
										var remittance_id=record.data["remittance_id"];
										
										if(is_super_time==1){
											color="green";
											state="正常";
										}
										if (fee_type == 1) {
											if(value==0){
												return "<div style=\"color:"+color+"\">已回款确认("+state+")先出报告后付款</div>";
											}else{
												if(null != remittance_id)
													return "<div style=\"color:"+color+"\">已回款未确认("+state+")先出报告后付款</div>";
												else
													return "<div style=\"color:"+color+"\">未回款("+state+")先出报告后付款</div>";
											}
//											return "<div style=\"color:"+color+"\">先出报告后付款("+state+")</div>";
										}if (fee_type == 4) {
//											return "<div style=\"color:"+color+"\">月结("+state+")</div>";
											if(value==0){
												return "<div style=\"color:"+color+"\">已回款确认("+state+")月结</div>";
											}else{
												if(null != remittance_id)
													return "<div style=\"color:"+color+"\">已回款未确认("+state+")月结</div>";
												else
													return "<div style=\"color:"+color+"\">未回款("+state+")月结</div>";
											}
										
										} else {
											if(value==0){
												return "<div style=\"color:"+color+"\">已回款确认("+state+")</div>";
											}else{
												if(null != remittance_id)
													return "<div style=\"color:"+color+"\">已回款未确认("+state+")</div>";
												else
													return "<div style=\"color:"+color+"\">未回款("+state+")</div>";
											}
										}
									}
								},{
									text : '是否超过48小时',
									dataIndex : 'is_super_time',
									menuDisabled : true,
									width : 300,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var mail_time= record.data["mail_time"];
										var accept_time=record.data["accept_time"];
										if (value == 1) {
											return "<div style=\"color:green\">正常（登记时间:"+accept_time+",邮寄时间:"+(mail_time==null?"":mail_time)+"）</div>";
										} else {
											if(mail_time!=null||mail_time!=null){
												return "<div style=\"color:red\">异常（登记时间:"+accept_time+",邮寄时间:"+(mail_time==null?"":mail_time)+"）</div>";
											}else{
												return "<div style=\"color:red\">异常（登记时间:"+accept_time+",未邮寄）</div>";
											}
									   }
									}
								},{
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
								},{
									text : '是否上传照片',
									dataIndex : 'per_photo',
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
										if (value == 0) {
											return "<div style=\"color:green\">已上传（正常）</div>";
										} else {
											return "<div style=\"color:"+color+"\">未上传（"+state+"）</div>";
										}
									}
								},{
									text : '案例状态',
									dataIndex : 'verify_state',
									menuDisabled : true,
									width : 200,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										switch (value) {
										case 0:
											return "未审核";
											break;
										case 1:
											return "已提交审核";
											break;
										case 2:
				                            return "<span style='color:red'>案例审核未通过</span>";
											break;
										case 3:
											return "审核通过";
											break;
										case 4:
											return "样本交接中";
											break;
										case 5:
											return "实验中";
											break;
										case 6:
											return "报告打印中";
											break;
										case 7:
											return "报告确认中";
											break;
										case 8:
											return "邮寄中";
											break;
										case 9:
											return "归档中";
											break;
										case 10:
											return "已归档";
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
									items : [case_code,receiver,receiver_area,client,fee_state]
								},{
									xtype : 'toolbar',
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								    },
									items : [is_mail,is_super_time,other_type,is_report]
								},{
									xtype : 'toolbar',
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '1px !important'
								    },
									items : [starttime,endtime, {
										text : '查询',
										iconCls : 'Find',
										handler : me.onSearch
									} ]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '查看自定义异常信息',
										iconCls : 'Find',
										handler : me.onFind
									}]
								}  ];

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
							title : "自定义异常信息（案例条形码：" + selections[0].get("case_code") + "）",
							width : 800,
							iconCls : 'Find',
							height : 600,
							layout : 'border',
							bodyStyle : "background-color:white;",
							items : [ Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 800,
								height : 600,
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
