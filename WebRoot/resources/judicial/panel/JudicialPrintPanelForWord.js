Ext.define("Rds.judicial.panel.JudicialPrintPanelForWord",
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
						var storeModel = Ext.create('Ext.data.Store', {
							model : 'model',
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/dicvalues/getReportModelByPartner.do',
								params : {
									type : 'dna',
									receiver_id : ""
								},
								reader : {
									type : 'json',
									root : 'data'
								}
							},
							autoLoad : true,
							remoteSort : true,
							listeners : {
								'load' : function() {
									var allmodel = new model({
										'key' : '',
										'value' : '全部'
									});
									this.insert(0, allmodel);
									report_type.select(this.getAt(0));
								}
							}
						});
						var case_code = Ext.create('Ext.form.field.Text', {
							name : 'casecode',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '案例编号'
						});
						var starttime = Ext.create('Ext.form.DateField', {
							name : 'starttime',
							width : '20%',
							labelWidth : 70,
							fieldLabel : '比对时间 从',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value : Ext.Date.add(new Date(), Ext.Date.DAY, -7),
							listeners : {
								select : function() {
									var start = starttime.getValue();
									var end = endtime.getValue();
									endtime.setMinValue(start);
								}
							}
						});
						var endtime = Ext.create('Ext.form.DateField', {
							name : 'endtime',
							width : '18%',
							labelWidth : 20,
							fieldLabel : '到',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value : Ext.Date.add(new Date(), Ext.Date.DAY),
							listeners : {
								select : function() {
									var start = starttime.getValue();
									var end = endtime.getValue();
									starttime.setMaxValue(end);
								}
							}
						});
						var print_state = Ext.create('Ext.form.ComboBox', {
							fieldLabel : '打印状态',
							width : '20%',
							labelWidth : 60,
							editable : false,
							triggerAction : 'all',
							displayField : 'Name',
							labelAlign : 'right',
							valueField : 'Code',
							store : new Ext.data.ArrayStore({
								fields : [ 'Name', 'Code' ],
								data : [ [ '全部', -1 ], [ '已打印', 0 ],
										[ '未打印', 1 ] ]
							}),
							value : -1,
							mode : 'local',
							// typeAhead: true,
							name : 'print_state',
						});
						var report_type = Ext.create('Ext.form.ComboBox', {
							fieldLabel : '模板类型',
							mode : 'local',
							labelWidth : 80,
							editable : false,
							labelAlign : 'right',
							valueField : "key",
							width : '20%',
							displayField : "value",
							name : 'report_type',
							store : storeModel,
						});
						var parnter_name = Ext.create('Ext.form.field.Text', {
							name : 'parnter_name',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '合作商'
						});
						me.selModel = Ext.create('Ext.selection.CheckboxModel',{
						});
						me.store = Ext
								.create(
										'Ext.data.Store',
										{
											fields : [ 'case_id', 'case_code',
													'case_areaname',
													'case_areacode',
													'receiver_area',
													'report_url',
													'case_receiver',
													'print_copies',
													 'copies',
													'urgent_state',
													'print_count',
													'accept_time',
													'close_time',
													'report_modelname',
													'report_model',
													'attach_need',
													"receiver_id",
													'report_chart',
													'sample_in_time',
													'case_id', 'is_delete',
													'compare_date','verify_state','gather_status','remittance_id','fee_type','time1',
											          'time2','iswilltime','confirm_date','isouttime'  ],
											start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/print/getPrintCaseInfoForWord.do',
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
																	me.store.proxy.extraParams,
																	{
																		endtime : dateformat(endtime
																				.getValue()),
																		modeltype : report_type
																				.getValue(),
																		starttime : dateformat(starttime
																				.getValue()),
																		print_state : print_state
																				.getValue(),
																		case_code : trim(case_code
																				.getValue()),
																				parnter_name:trim(parnter_name.getValue())
																				
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
									flex : 120,
								},
								{
									text : '案例归属地',
									dataIndex : 'receiver_area',
									menuDisabled : true,
									flex : 200
								},
								{
									text : '归属人',
									dataIndex : 'case_receiver',
									menuDisabled : true,
									flex : 120
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
									text : '下载状态',
									dataIndex : 'print_count',
									menuDisabled : true,
									flex : 150,
									renderer : function(value, cellmeta, record, rowIndex,
											columnIndex, store) {
//										var print_copies = record.data["print_copies"];
										var print_count = record.data["print_count"];
										return "<span style='color:green'>已下载了"+print_count+"次</span>"
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
								}, {
									text : '回款状态',
									dataIndex : 'gather_status',
									menuDisabled : true,
									width : 150,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var fee_type= record.data["fee_type"];
										var remittance_id=record.data["remittance_id"];
										var color="red";
										var color1="green";
										if (fee_type == 1) {
											if(value==0){
												return "<div style=\"color:"+color1+"\">已回款确认,先出报告后付款</div>";
											}else{
												if(null != remittance_id)
													return "<div style=\"color:"+color1+"\">已回款未确认,先出报告后付款</div>";
												else
													return "<div style=\"color:"+color1+"\">未回款,先出报告后付款</div>";
											}
										}if (fee_type == 4) {
											if(value==0){
												return "<div style=\"color:"+color1+"\">已回款确认,月结</div>";
											}else{
												if(null != remittance_id)
													return "<div style=\"color:"+color1+"\">已回款未确认,月结</div>";
												else
													return "<div style=\"color:"+color1+"\">未回款,月结</div>";
											}
										
										}if (fee_type == 2) {

											if(value==0){
												return "<div style=\"color:"+color1+"\">已回款确认,免单</div>";
											}else{
												if(null != remittance_id)
													return "<div style=\"color:"+color1+"\">已回款未确认,免单</div>";
												else
													return "<div style=\"color:"+color1+"\">未回款,免单</div>";
											}
										
										} else {
											if(value==0){
												return "<div style=\"color:"+color1+"\">已回款确认</div>";
											}else{
												if(null != remittance_id)
													return "<div style=\"color:"+color+"\">已回款未确认</div>";
												else
													return "<div style=\"color:"+color+"\">未回款</div>";
											}
										}
									}
								},{
									text : '案例时效',
									dataIndex : 'time2',
									menuDisabled : true,
									width : 120,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var time2= record.data["time2"];
										var time1= record.data["time1"];
										var confirm_date= record.data["confirm_date"];
										var iswilltime = record.data["iswilltime"];
										
										var color="red";
										var color1="green";
										if (null==value &&time1 !=null&& confirm_date !=null) {
											if(iswilltime>43200 && iswilltime<86400){
												return "<div style=\"color:red\">即将超时,未邮寄</div>";
											}else if(iswilltime<43200){
												return "<div style=\"color:green\">正常,未邮寄</div>";
											}else if(iswilltime>86400){
												return "<div style=\"color:red\">已超时,未邮寄</div>";
											}
										} else if(null!=value && time1 !=null && confirm_date !=null){
											if(iswilltime>43200 && iswilltime<86400){
											return "<div style=\"color:green\">即将超时,已邮寄</div>";
										}else if(iswilltime<43200){
											return "<div style=\"color:green\">正常,已邮寄</div>";
										}else if(iswilltime>86400){
											return "<div style=\"color:red\">已超时,已邮寄</div>";
										}}
									}
								},{
									text : '比对日期',
									dataIndex : 'compare_date',
									menuDisabled : true,
									flex : 150
								}, {
									text : '模板名称',
									dataIndex : 'report_modelname',
									menuDisabled : true,
									flex : 150
								}, {
									text : '样本登记日期',
									dataIndex : 'sample_in_time',
									menuDisabled : true,
									flex : 150
								} ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [ case_code, report_type,
											print_state, starttime, endtime, parnter_name]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [ parnter_name,{
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											} ]
								
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '查看样本信息',
										iconCls : 'Find',
										handler : me.onFind
									}, {
										text : '报告下载',
										iconCls : 'Printer',
										handler : me.getWord
									}, {
										text : '多个报告下载',
										iconCls : 'Printer',
										handler : me.getZip
									},{
										text : '在线打印',
										iconCls : 'Printer',
										handler : me.onPrinter,
									}, {
										text : '普通位点表格下载',
										iconCls : 'Printer',
										handler : me.getConTable
									},{
										text : '加位点位点表格下载',
										iconCls : 'Printer',
										handler : me.getExtTable
									},{
										text : '案例表格下载',
										iconCls : 'Printer',
										handler : me.getTable
									},{
										text : '条纹图打印',
										iconCls : 'Printercolor',
										handler : me.onPhotoPrint
									}, {
										text : '报告下载打印',
										iconCls : 'Printer',
										handler : me.getWordTwo
									}, {
										text : '多份报告下载打印',
										iconCls : 'Printer',
										handler : me.getZipTwo
									}]
								} ];

						me.callParent(arguments);
					},
					onCancel : function() {
			            var me = this;
			            me.up("window").close();
			        },
					onPrinter:function(){
			            var me = this.up("gridpanel");
			            var selections = me.getView().getSelectionModel()
			                .getSelection();
			            if (selections.length < 1) {
			                Ext.Msg.alert("提示", "请选择需要打印的记录!");
			                return;
			            };
			            var a=selections[0].get("gather_status");
						var fee_type= selections[0].get("fee_type");
						if (a ==0||fee_type==4||fee_type==2||fee_type==1) {	}else{
							Ext.Msg.alert("提示", "此案例财务未确认回款不能下载!");
							return;
						}
			            var print=2;
			            var src = "judicial/print/getJudicialWord.do?case_code="
							+ selections[0]
						.get("case_code")
				+ "&case_id="
				+ selections[0]
						.get("case_id")+"&report_model="+selections[0].get("report_model")
            +"&print_count="+selections[0].get("print_count");
			            var win = Ext.create("Ext.window.Window", {
			                config:{
			                    gridpanel:me
			                },
			                title : '报告打印',
			                iconCls : 'Find',
			                layout:"auto",
			                maximized:true,
			                maximizable :true,
			                modal:true,
			                bodyStyle : "background-color:white;",
			                html:"<iframe width=100% height=100% id='verifyWord' src='"+src+"'></iframe>",
			                buttons:[
			                    {
			                        text : '取消',
			                        iconCls : 'Arrowredo',
			                        handler : me.onCancel
			                    }
			                ]
			            });
			            win.show();
			            // Ext.MessageBox
			            //     .confirm(
			            //     '提示',
			            //     '确定打印此案例吗',
			            //     function(id) {
			            //         if (id == 'yes') {
			            //             window.location.href="trace/attachment/getWord.do?year="+
			            //                 selections[0].get("year")+"&case_no="+selections[0].get("case_no")
			            //             +"&case_id="+selections[0].get("case_id")
			            //         }
			            //     });
			        
						
					},
					onSearch : function() {
						var me = this.up("gridpanel");
						me.getStore().currentPage = 1;
						me.getStore().load();
					},
					getWord : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要下载的记录!");
							return;
						}
						;
						var a=selections[0].get("gather_status");
						var fee_type= selections[0].get("fee_type");
						if (a ==0||fee_type==4||fee_type==2||fee_type==1) {	
						Ext.MessageBox
								.confirm(
										'提示',
										'确定下载此案例吗',
										function(id) {
											if (id == 'yes') {
												window.location.href = "judicial/print/getWord.do?case_code="
														+ selections[0]
																.get("case_code")
														+ "&case_id="
														+ selections[0]
																.get("case_id")+ "&verify_state="
														+ selections[0]
																.get("verify_state")+"&report_model="+selections[0].get("report_model")
                                                    +"&print_count="+selections[0].get("print_count")+"&print="+0;
											}
										});}else{
											Ext.Msg.alert("提示", "此案例财务未确认回款不能下载!");
											return;
										}
					},
					getWordTwo : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要下载的记录!");
							return;
						}
						;
						var a=selections[0].get("gather_status");
						var fee_type= selections[0].get("fee_type");
						if (a ==0||fee_type==4||fee_type==2||fee_type==1) {	
						Ext.MessageBox
								.confirm(
										'提示',
										'确定下载此案例吗',
										function(id) {
											if (id == 'yes') {
												window.location.href = "judicial/print/getWord.do?case_code="
														+ selections[0]
																.get("case_code")
														+ "&case_id="
														+ selections[0]
																.get("case_id")+ "&verify_state="
														+ selections[0]
																.get("verify_state")+"&report_model="+selections[0].get("report_model")
                                                    +"&print_count="+selections[0].get("print_count")+"&print="+selections[0].get("copies");
											}
										});}else{
											Ext.Msg.alert("提示", "此案例财务未确认回款不能下载!");
											return;
										}
					},
					onFind : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要查看的记录!");
							return;
						}
						;
						var win = Ext
								.create(
										"Ext.window.Window",
										{
											title : "样本信息（案例条形码："
													+ selections[0]
															.get("case_code")
													+ "）",
											width : 600,
											iconCls : 'Find',
											height : 400,
											layout : 'border',
											bodyStyle : "background-color:white;",
											items : [ Ext
													.create(
															'Ext.grid.Panel',
															{
																renderTo : Ext
																		.getBody(),
																width : 600,
																height : 400,
																frame : false,
																viewConfig : {
																	forceFit : true,
																	stripeRows : true
																// 在表格中显示斑马线
																},
																store : {// 配置数据源
																	fields : [
																			'sample_id',
																			'sample_code',
																			'sample_type',
																			'sample_typename',
																			'sample_call',
																			'sample_callname',
																			'sample_username',
																			'id_number',
																			'birth_date' ],// 定义字段
																	proxy : {
																		type : 'jsonajax',
																		actionMethods : {
																			read : 'POST'
																		},
																		url : 'judicial/register/getSampleInfo.do',
																		params : {
																			'case_id' : selections[0]
																					.get("case_id")
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
																		},
																		{
																			header : "称谓",
																			dataIndex : 'sample_callname',
																			flex : 1,
																			menuDisabled : true
																		},
																		{
																			header : "姓名",
																			dataIndex : 'sample_username',
																			flex : 1,
																			menuDisabled : true
																		},
																		{
																			header : "身份证号",
																			dataIndex : 'id_number',
																			flex : 2,
																			menuDisabled : true
																		},
																		{
																			header : "出生日期",
																			dataIndex : 'birth_date',
																			flex : 1,
																			menuDisabled : true
																		},
																		{
																			header : "样本类型",
																			dataIndex : 'sample_typename',
																			flex : 1,
																			menuDisabled : true
																		} ]
															}) ]
										});
						win.show();
					},
					onPhotoPrint : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要打印的记录!");
							return;
						};
						var a=selections[0].get("gather_status");
						var fee_type= selections[0].get("fee_type");
						if (a ==0||fee_type==4||fee_type==2||fee_type==1) {	
						var case_code = selections[0].get("case_code");
						printSample(case_code)}else{
							Ext.Msg.alert("提示", "此案例财务未确认回款不能下载!");
							return;
						}
					},
					getZip : function(){
						var me = this.up("gridpanel");
						var selections = me.selModel.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要下载的记录!");
							return;
						};
						var data = new Array() ;
						for(var i=0;i<selections.length;i++){
							data[i] = selections[i].data;
							var a=selections[i].get("gather_status");
							var fee_type= selections[0].get("fee_type");
							if (a ==0||fee_type==4||fee_type==2||fee_type==1) {	
								
							}else{
								Ext.Msg.alert("提示", "此案例财务未确认回款不能下载!");
								return;
							}
						}
						Ext.getBody().mask("请稍等，正在下载中...","x-mask-loading");
						Ext.Ajax.request({
							url: "judicial/print/getZip.do",
							method: "POST",
							headers: { 'Content-Type': 'application/json' },
							jsonData: data,
							success: function (response, options) {
								Ext.getBody().unmask();
								window.location.href = "judicial/print/getZipAfter.do"
							},
							failure: function () {
								Ext.getBody().unmask();
								Ext.Msg.alert("提示", "下载失败<br>请联系管理员!");
							}
						});

					},getZipTwo : function(){
						var me = this.up("gridpanel");
						var selections = me.selModel.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要下载的记录!");
							return;
						};
						var data = new Array() ;
						for(var i=0;i<selections.length;i++){
							data[i] = selections[i].data;
							var a=selections[i].get("gather_status");
							var fee_type= selections[0].get("fee_type");
							if (a ==0||fee_type==4||fee_type==2||fee_type==1) {	
								
							}else{
								Ext.Msg.alert("提示", "此案例财务未确认回款不能下载!");
								return;
							}
						}
						Ext.getBody().mask("请稍等，正在下载中...","x-mask-loading");
						Ext.Ajax.request({
							url: "judicial/print/getZipTwo.do",
							method: "POST",
							headers: { 'Content-Type': 'application/json' },
							jsonData: data,
							success: function (response, options) {
								Ext.getBody().unmask();
								window.location.href = "judicial/print/getZipAfter.do"
							},
							failure: function () {
								Ext.getBody().unmask();
								Ext.Msg.alert("提示", "下载失败<br>请联系管理员!");
							}
						});

					},getConTable : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要打印的记录!");
							return;
						}
						;
						var a=selections[0].get("gather_status");
						var fee_type= selections[0].get("fee_type");
						if (a ==0||fee_type==4||fee_type==2||fee_type==1) {	
						Ext.MessageBox
								.confirm(
										'提示',
										'确定只打印普通点位吗',
										function(id) {
											if (id == 'yes') {
												 var d ="zyjdmodeltablereg";
												window.location.href = "judicial/print/getWord.do?case_code="
														+ selections[0]
																.get("case_code")
														+ "&case_id="
														+ selections[0]
																.get("case_id")+ "&verify_state="
														+ selections[0]
																.get("verify_state")+"&report_model="+d
                                                    +"&print_count="+selections[0].get("print_count")+"&print="+0;
											}
										});}else{
											Ext.Msg.alert("提示", "此案例财务未确认回款不能下载!");
											return;
										}
					},getExtTable : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要打印的记录!");
							return;
						}
						;
						var a=selections[0].get("gather_status");
						var fee_type= selections[0].get("fee_type");
						if (a ==0||fee_type==4||fee_type==2||fee_type==1) {	
						Ext.MessageBox
								.confirm(
										'提示',
										'确定只打印加位点位吗',
										function(id) {
											if (id == 'yes') {
												var d ="zyjdmodeltableregext";
												window.location.href = "judicial/print/getWord.do?case_code="
														+ selections[0]
																.get("case_code")
														+ "&case_id="
														+ selections[0]
																.get("case_id")+"&report_model="+d
                                                    +"&print_count="+selections[0].get("print_count")+"&print="+0;
											}
										});}else{
											Ext.Msg.alert("提示", "此案例财务未确认回款不能下载!");
											return;
										}
					},getTable : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要打印的记录!");
							return;
						}
						;
						var a=selections[0].get("gather_status");
						var fee_type= selections[0].get("fee_type");
						if (a ==0||fee_type==4||fee_type==2||fee_type==1) {	
						Ext.MessageBox
								.confirm(
										'提示',
										'确定打印此案例吗',
										function(id) {
											if (id == 'yes') {
												var d ="zyjdmodeltable";
												window.location.href = "judicial/print/getWord.do?case_code="
														+ selections[0]
																.get("case_code")
														+ "&case_id="
														+ selections[0]
																.get("case_id")+ "&verify_state="
														+ selections[0]
																.get("verify_state")+"&report_model="+d
                                                    +"&print_count="+selections[0].get("print_count")+"&print="+0;
											}
										});}else{
											Ext.Msg.alert("提示", "此案例财务未确认回款不能下载!");
											return;
										}
					},
					listeners : {
						'afterrender' : function() {
							this.store.load();
						}
					}
				});

printSample = function(value) {
		Ext.Ajax.request({
			url : "judicial/print/exsitSampleImg.do",
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : {
				case_code : value
			},
			success : function(response, options) {
				response = Ext.JSON.decode(response.responseText);
				if (response == false) {
					Ext.Msg.alert("提示", "此条形码无法打印！");
				} else {
					var print_chanel = function() {
						win.close();
					}
					var print_print = function(me) {
						var iframe = document.getElementById("samplemodel");
						iframe.contentWindow.focus();
						iframe.contentWindow.print();
						win.close();
					}
					var win = Ext.create("Ext.window.Window", {
						title : "打印预览",
						iconCls : 'Find',
						maximized : true,
						maximizable : true,
						layout : "auto",
						modal : true,
						bodyStyle : "background-color:white;",
						html : "<iframe width=100% height=100% id='samplemodel' src='judicial/print/getSampleImg.do?case_code="
								+ value + "'></iframe>",
						buttons : [{
									text : '打印',
									iconCls : 'Disk',
									handler : print_print
								}, {
									text : '取消',
									iconCls : 'Cancel',
									handler : print_chanel
								}]
					});
					win.show();
				}
			},
			failure : function() {
				Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
			}
		});
}