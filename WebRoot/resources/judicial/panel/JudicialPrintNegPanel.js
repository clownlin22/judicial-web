printNegCase=function(value,url,attach_need,print_count,case_id,isword){
                var src = "";
                if(isword=='word'){
                    src = url;
                }else{
                    src=url+"&case_code="+value+"&case_id="+case_id;
                }
									var print_chanel=function(){
										win.close();
									}
									var print_print=function(me){
										if(confirm("确认该案例可打印?")){

											Ext.Ajax.request({
												url : "judicial/subCase/checkNegReport.do",
												method : "POST",
												headers : {
													'Content-Type' : 'application/json'
												},
												jsonData : {
													case_code:value
												},
												success : function(
														response,
														options) {
													response = Ext.JSON
															.decode(response.responseText);
                                                    if (response.result == true) {
                                                        Ext.MessageBox.alert(
                                                            "提示信息",
                                                            response.message);
                                                        print_chanel();
                                                    } else {
                                                        Ext.MessageBox.alert(
                                                            "错误信息",
                                                            response.message);
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
//                                        Ext.Msg.confirm(
//                                            "提示",
//                                            "确认该案例可打印!",function(id){
//                                                if(id=='yes'){}
//                                            })
                                    }
												var win=Ext.create("Ext.window.Window",{
													title : "打印预览",
													iconCls : 'Find',
													layout:"auto",
													maximized:true,
													maximizable :true,
													modal:true,
													bodyStyle : "background-color:white;",
													html:"<iframe width=100% height=100% id='dnamodel' src='"+src+"'></iframe>",
													buttons:[
																 {
																		text : '确认该报告可打印',
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
}

var storeModel = Ext.create(
        'Ext.data.Store',
        {
          model:'model',
          proxy:{
        	type: 'jsonajax',
            actionMethods:{read:'POST'},
            url : 'judicial/dicvalues/getReportModelByPartner.do',
			params : {
				type : 'dna',
				receiver_id : ""
			},
            reader:{
              type:'json',
              root:'data'
            }
          },
          autoLoad:true,
          remoteSort:true,
          listeners : {
				'load' : function() {
					var allmodel = new model({'key':'','value':'全部'});
			        this.insert(0,allmodel);
			        Ext
			    	.getCmp(
			    	'modeltype_neg_print').select(this.getAt(0));
				}
			}
        }
      );

Ext
		.define(
				"Rds.judicial.panel.JudicialPrintNegPanel",
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
						me.store = Ext
								.create(
										'Ext.data.Store',
										{
											fields : [ 'case_id', 'case_code',
													'case_areaname',
													'case_areacode',
													'receiver_area','report_url',
													'case_receiver','print_copies',
													'urgent_state','print_count',
													'accept_time','close_time',
													'report_modelname',
													'report_model','attach_need',
													"receiver_id",
													'sample_in_time', 'case_id',
													'is_delete','compare_date' ],
											start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/print/getPrintCaseInfo.do',
												params : {
													start : 0,
													limit : 25,
                                                    checkNeg : 'Y'
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
																endtime : dateformat(Ext
																		.getCmp(
																		'endtime_neg_print')
																               .getValue()),	
															    modeltype : Ext
																		.getCmp(
																		'modeltype_neg_print')
															            	.getValue(),
																starttime : dateformat(Ext
																		.getCmp(
																				'starttime_neg_print')
																		.getValue()),
																print_state:Ext
																		.getCmp(
																		'print_state_neg_print')
															            	.getValue(),		
																case_code : trim(Ext
																		.getCmp(
																				'case_code_neg_print')
																		.getValue()),
                                                                checkNeg : 1
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
									flex : 200,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
											return "<a href='#'>"+ record.data["case_code"]+"</a>";
									},
									listeners:{
										'click':function(){ 
											var me = this.up("gridpanel");
											Ext.MessageBox.wait('正在操作','请稍后...');
											var selections = me.getView().getSelectionModel().getSelection();
											Ext.Ajax.request({  
												url:"judicial/caseException/getCaseInfo.do", 
												method: "POST",
												headers: { 'Content-Type': 'application/json' },
												jsonData: {
													case_id:selections[0].get("case_id"),
													case_code:selections[0].get("case_code")
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
								},
								{
									text : '案例归属地',
									dataIndex : 'receiver_area',
									menuDisabled : true,
									flex : 250
								},
								{
									text : '归属人',
									dataIndex : 'case_receiver',
									menuDisabled : true,
									flex : 250
								},
								{
									text : '紧急程度',
									dataIndex : 'urgent_state',
									menuDisabled : true,
									flex : 100,
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
									text : '打印状态',
									dataIndex : 'print_count',
									menuDisabled : true,
									flex : 150,
									renderer : function(value, cellmeta, record, rowIndex,
											columnIndex, store) {
//										var print_copies = record.data["print_copies"];
										var print_count = record.data["print_count"];
										return "<span style='color:green'>已打印了"+print_count+"次</span>"
//										if (print_copies == 0) {
//											return "<span style='color:red'>无法打印（未设置打印次数）</span>";
//										} else if (print_copies - print_count > 0) {
//											return "<span style='color:green'>可以打印</span>，拥有<span style='color:green'>"
//													+ print_copies
//													+ "</span>次打印次数,已打印了<span style='color:red'>"
//													+ print_count + "</span>次";
//										} else {
//											return "<span style='color:red'>无法打印</span>，拥有<span style='color:green'>"
//													+ print_copies
//													+ "</span>次打印次数,已打印了<span style='color:red'>"
//													+ print_count + "</span>次";
//										}

									}
								},
                                {
                                    text : '比对日期',
                                    dataIndex : 'compare_date',
                                    menuDisabled : true,
                                    flex : 150
                                },
								{
									text : '模板名称',
									dataIndex : 'report_modelname',
									menuDisabled : true,
									flex : 150
								},
								{
									text : '样本登记日期',
									dataIndex : 'sample_in_time',
									menuDisabled : true,
									flex : 150
								}];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [
											{
												xtype : 'textfield',
												name : 'case_code',
												id : 'case_code_neg_print',
												labelWidth : 80,
												width : 175,
												fieldLabel : '案例条形码'
											},{
												xtype : "combo",
												fieldLabel : '模板类型',
												mode: 'local',   
												labelWidth : 80,
												editable:false,
												labelAlign: 'right',
												valueField :"key",  
												width : 210,
										        displayField: "value",    
												name : 'report_type',
												store: storeModel,
												id : 'modeltype_neg_print'
											},
											new Ext.form.field.ComboBox(
															{
																fieldLabel : '打印状态',
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
																							'已打印',
																							0 ],
																					[
																							'未打印',
																							1 ] ]
																		}),
																value : -1,
																mode : 'local',
																// typeAhead: true,
																name : 'print_state',
																id : 'print_state_neg_print'
											}),
											{
												xtype : 'datefield',
												name : 'starttime',
												id : 'starttime_neg_print',
												width : 175,
												fieldLabel : '比对时间 从',
												labelWidth : 70,
												labelAlign : 'right',
												emptyText : '请选择日期',
												format : 'Y-m-d',
//												value : Ext.Date.add(
//														new Date(),
//														Ext.Date.DAY,-7),
												maxValue : new Date(),
												listeners : {
													'select' : function() {
														var start = Ext.getCmp(
																'starttime_neg_print')
																.getValue();
														Ext.getCmp('endtime_neg_print')
																.setMinValue(
																		start);
														var endDate = Ext
																.getCmp(
																		'endtime_neg_print')
																.getValue();
														if (start > endDate) {
															Ext
																	.getCmp(
																			'endtime_neg_print')
																	.setValue(
																			start);
														}
													}
												}
											},
											{
												xtype : 'datefield',
												id : 'endtime_neg_print',
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
																'starttime_neg_print')
																.getValue();
														var endDate = Ext
																.getCmp(
																		'endtime_neg_print')
																.getValue();
														if (start > endDate) {
															Ext
																	.getCmp(
																			'starttime_neg_print')
																	.setValue(
																			endDate);
														}
													}
												}
											}, {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											} ]
								},{
									xtype : 'toolbar',
									dock : 'top',
									items : [  {
										text : '查看样本信息',
										iconCls : 'Find',
										handler : me.onFind
									},{
										text : '否定报告审核',
										iconCls : 'Printer',
										handler : me.onCasePrint
									}]
								}];

						me.callParent(arguments);
					},
					onSearch : function() {
						var me = this.up("gridpanel");
						me.getStore().currentPage=1;
						me.getStore().load();
					},
					onCasePrint:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要审核的记录!");
							return;
						};
						 var print_copies= selections[0].get("print_copies");
						 var case_code=selections[0].get("case_code");
						 var case_id=selections[0].get("case_id");
						 var print_url=selections[0].get("report_url");
						 var attach_need=selections[0].get("attach_need");
						 var print_count=selections[0].get("print_count");
                         var report_model=selections[0].get("report_model");
                        if(report_model!='cxjymodel'
                            && report_model!='dnmodel'
                            && report_model!='jjjdmodel'
                            && report_model!='jjjymodel'
                            && report_model!='zyjdmodel'
                            && report_model!='zyjymodel'
                            && report_model!='zzjdmodel'
                            && report_model!='zyjdmodelzk'
                            && report_model!='zyjdmodeltq'
                            	){
                            var src='judicial/verify/verifyWord.do?case_code='+case_code+'&report_model='+report_model;
                            printNegCase(case_code,src,attach_need,print_count,case_id,"word");
//                        }
//						else if(print_copies-print_count>0){
//							printNegCase(case_code,print_url,attach_need,print_count,case_id,"noword");
//						}else if(print_copies==0){
//							Ext.Msg.alert("提示", "无打印权限!");
						}else {
							printNegCase(case_code,print_url,attach_need,print_count,case_id,"noword");
//							Ext.Msg.alert("提示", "打印次数已用完!");
						}
					},
					onTablePrint:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要打印的记录!");
							return;
						};
						var print_copies= selections[0].get("print_copies");
						var print_count=selections[0].get("print_count");
						var case_code=selections[0].get("case_code");
						var case_id=selections[0].get("case_id");
						if(print_copies-print_count>0){
							printTable(case_code,case_id)
						}else if(print_copies==0){
								Ext.Msg.alert("提示", "无打印权限!");
						}else {
								Ext.Msg.alert("提示", "打印次数已用完!");
						}
					},
					onPhotoPrint:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要打印的记录!");
							return;
						};	
						var print_copies= selections[0].get("print_copies");
						var print_count=selections[0].get("print_count");
						var case_code=selections[0].get("case_code");
						if(print_copies-print_count>0){
							printSample(case_code)
						}else if(print_copies==0){
								Ext.Msg.alert("提示", "无打印权限!");
						}else {
								Ext.Msg.alert("提示", "打印次数已用完!");
						}
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
					listeners : {
						'afterrender' : function() {
							this.store.load();
						}
					}
				});
