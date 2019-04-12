printCaseImg=function(code,count){
	var print_chanel=function(){
		win.close();
	}
	var print_print=function(me){
		Ext.Ajax.request({
			url : "judicial/attachment/updateAllAttachmentPrint.do",
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : {
				case_code:code
			},
			success : function(
					response,
					options) {
				response = Ext.JSON
						.decode(response.responseText);
				if (response == true) {
					var iframe = document.getElementById("printimgmodel");
					iframe.contentWindow.focus();
					iframe.contentWindow.print();
					win.close();
				}else{
					Ext.Msg
					.alert(
							"提示",
							"打印出错");
				}
			},
			failure : function() {
				Ext.Msg
						.alert(
								"提示",
								"保存失败<br>请联系管理员!");
			}
		});
	}
	
	var win=Ext.create("Ext.window.Window",{
		title : "打印预览",
		iconCls : 'Find',
		layout:"auto",
		maximized:true,
		maximizable :true,
		modal:true,
		bodyStyle : "background-color:white;",
		html:"<iframe width=100% height=100% id='printimgmodel' src='judicial/attachment/printAllAttachment.do?case_code="+code+"&count="+count+"&v="+new Date().getTime()+"'></iframe>",
		buttons:[
//					{
//						text : '左转',
//						iconCls : 'Arrowturnleft',
//						handler : onTurnLeft
//					}, {
//						text : '右转',
//						iconCls : 'Arrowturnright',
//						handler : onTurnRight
//					},
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
};
printCaseOneKey = function(value, url, copies, print_count, case_id,
		print_chart) {
	if (!!window.ActiveXObject || "ActiveXObject" in window) {
//		if (attach_need == 0) {
//			Ext.Msg.alert("提示", "此案例需要打印附本!", function() {
//				var src = url + "&case_code=" + value + "&case_id=" + case_id;
//				var chart = print_chart + "&case_code=" + value + "&case_id="
//						+ case_id;
//				Ext.Ajax.request({
//					url : "judicial/print/exsitPrintTable.do",
//					method : "POST",
//					headers : {
//						'Content-Type' : 'application/json'
//					},
//					jsonData : {
//						case_code : value
//					},
//					success : function(response, options) {
//						response = Ext.JSON.decode(response.responseText);
//						if (response == false) {
//							var print_chanel = function() {
//								win.close();
//							}
//							var print_print = function(me) {
//								Ext.Ajax.request({
//									url : "judicial/print/usePrintCount.do",
//									method : "POST",
//									headers : {
//										'Content-Type' : 'application/json'
//									},
//									jsonData : {
//										case_code : value,
//										print_count : print_count
//									},
//									success : function(response, options) {
//										response = Ext.JSON
//												.decode(response.responseText);
//										if (response == true) {
//											var iframe = document
//													.getElementById("dnamodel");
//											var a = document
//													.getElementById("PrintAX");
//											for (var i = 0; i < 3; i++) {
//												iframe.contentWindow.focus();
//												a.Mar_left = 20;
//												a.Mar_Top = 14;
//												a.Mar_Right = 20;
//												a.Mar_Bottom = 14;
//												a.Orientation = "纵向";
//												a.Paper_Size = "A4";
//												a.ApplySetup();
//												a
//														.PrintWithOutSetupPrintWithOutByID("dnamodel");
//											}
//											var iframe = document
//													.getElementById("samplemodel");
//											iframe.contentWindow.focus();
//											a.Mar_left = 5;
//											a.Mar_Top = 5;
//											a.Mar_Right = 5;
//											a.Mar_Bottom = 5;
//											a.Orientation = "纵向";
//											a.Paper_Size = "A4";
//											a.ApplySetup();
//											a
//													.PrintWithOutSetupPrintWithOutByID("samplemodel");
//											win.close();
//										}
//									},
//									failure : function() {
//										Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
//									}
//								});
//							}
//							var win = Ext.create("Ext.window.Window", {
//								title : "打印预览",
//								iconCls : 'Find',
//								layout : "auto",
//								maximized : true,
//								maximizable : true,
//								modal : true,
//								bodyStyle : "background-color:white;",
//								html : "<iframe width=100% height=100% id='dnamodel' src='"
//										+ src
//										+ "'></iframe><iframe width=100% height=100% id='samplemodel' src='judicial/print/getSampleImg.do?case_code="
//										+ value + "'></iframe>",
//								buttons : [{
//											text : '打印',
//											iconCls : 'Disk',
//											handler : print_print
//										}, {
//											text : '取消',
//											iconCls : 'Cancel',
//											handler : print_chanel
//										}]
//							});
//							win.show();
//						} else {
//							var print_chanel = function() {
//								win.close();
//							}
//							var print_print = function(me) {
//								Ext.Ajax.request({
//									url : "judicial/print/usePrintCount.do",
//									method : "POST",
//									headers : {
//										'Content-Type' : 'application/json'
//									},
//									jsonData : {
//										case_code : value,
//										print_count : print_count
//									},
//									success : function(response, options) {
//										response = Ext.JSON
//												.decode(response.responseText);
//										if (response == true) {
//											var iframe = document
//													.getElementById("dnamodel");
//											var a = document
//													.getElementById("PrintAX");
//											for (var i = 0; i < 3; i++) {
//												iframe.contentWindow.focus();
//												a.Mar_left = 20;
//												a.Mar_Top = 14;
//												a.Mar_Right = 20;
//												a.Mar_Bottom = 14;
//												a.Orientation = "纵向";
//												a.Paper_Size = "A4";
//												a.ApplySetup();
//												a
//														.PrintWithOutSetupPrintWithOutByID("dnamodel");
//											}
//											var iframe = document
//													.getElementById("samplemodel");
//											iframe.contentWindow.focus();
//											a.Mar_left = 5;
//											a.Mar_Top = 5;
//											a.Mar_Right = 5;
//											a.Mar_Bottom = 5;
//											a.Orientation = "纵向";
//											a.Paper_Size = "A4";
//											a.ApplySetup();
//											a
//													.PrintWithOutSetupPrintWithOutByID("samplemodel");
//											var iframe = document
//													.getElementById("tablemodel");
//											iframe.contentWindow.focus();
//											a.Mar_left = 10;
//											a.Mar_Top = 10;
//											a.Mar_Right = 10;
//											a.Mar_Bottom = 10;
//											a.Orientation = "横向";
//											a.Paper_Size = "A4";
//											a.ApplySetup();
//											a
//													.PrintWithOutSetupPrintWithOutByID("tablemodel");
//											win.close();
//										}
//									},
//									failure : function() {
//										Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
//									}
//								});
//							}
//							var win = Ext.create("Ext.window.Window", {
//								title : "打印预览",
//								iconCls : 'Find',
//								layout : "auto",
//								maximized : true,
//								maximizable : true,
//								modal : true,
//								bodyStyle : "background-color:white;",
//								html : "<iframe width=100% height=100% id='dnamodel' src='"
//										+ src
//										+ "'></iframe><iframe width=100% height=100% id='samplemodel' src='judicial/print/getSampleImg.do?case_code="
//										+ value
//										+ "'></iframe><iframe width=100% height=100% id='tablemodel' src='"
//										+ chart + "'></iframe>",
//								buttons : [{
//											text : '打印',
//											iconCls : 'Disk',
//											handler : print_print
//										}, {
//											text : '取消',
//											iconCls : 'Cancel',
//											handler : print_chanel
//										}]
//							});
//							win.show();
//						}
//					}
//				});
//			});
//		} else {
		
		Ext.Msg.alert("提示", "此案例需要打印"+copies+"份!", function() {
			var src = url + "&case_code=" + value + "&case_id=" + case_id;
			var chart = print_chart + "&case_code=" + value + "&case_id="
					+ case_id;
			Ext.Ajax.request({
				url : "judicial/print/exsitPrintTable.do",
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
						var print_chanel = function() {
							win.close();
						}
						var print_print = function(me) {
							Ext.Ajax.request({
								url : "judicial/print/usePrintCount.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {
									case_code : value,
									print_count : print_count
								},
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response == true) {
										var iframe = document
												.getElementById("dnamodel");
										var a = document
												.getElementById("PrintAX");
										for (var i = 0; i < 2; i++) {
											iframe.contentWindow.focus();
											a.Mar_left = 20;
											a.Mar_Top = 14;
											a.Mar_Right = 20;
											a.Mar_Bottom = 14;
											a.Orientation = "纵向";
											a.Paper_Size = "A4";
											a.ApplySetup();
											a.PrintWithOutSetupPrintWithOutByID("dnamodel");
										}
										var iframe = document
												.getElementById("samplemodel");
										iframe.contentWindow.focus();
										a.Mar_left = 5;
										a.Mar_Top = 5;
										a.Mar_Right = 5;
										a.Mar_Bottom = 5;
										a.Orientation = "纵向";
										a.Paper_Size = "A4";
										a.ApplySetup();
										a
												.PrintWithOutSetupPrintWithOutByID("samplemodel");
										win.close();
									}
								},
								failure : function() {
									Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
								}
							});
						}
						var win = Ext.create("Ext.window.Window", {
							title : "打印预览",
							iconCls : 'Find',
							layout : "auto",
							maximized : true,
							maximizable : true,
							modal : true,
							bodyStyle : "background-color:white;",
							html : "<iframe width=100% height=100% id='dnamodel' src='"
									+ src
									+ "'></iframe><iframe width=100% height=100% id='samplemodel' src='judicial/print/getSampleImg.do?case_code="
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
					} else {
						var print_chanel = function() {
							win.close();
						}
						var print_print = function(me) {
							Ext.Ajax.request({
								url : "judicial/print/usePrintCount.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {
									case_code : value,
									print_count : print_count
								},
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response == true) {
										var iframe = document.getElementById("dnamodel");
										var a = document.getElementById("PrintAX");
										for (var i = 0; i < 2; i++) {
											iframe.contentWindow.focus();
											a.Mar_left = 20;
											a.Mar_Top = 14;
											a.Mar_Right = 20;
											a.Mar_Bottom = 14;
											a.Orientation = "纵向";
											a.Paper_Size = "A4";
											a.ApplySetup();
											a.PrintWithOutSetupPrintWithOutByID("dnamodel");
										}
										var iframe = document.getElementById("samplemodel");
										iframe.contentWindow.focus();
										a.Mar_left = 5;
										a.Mar_Top = 5;
										a.Mar_Right = 5;
										a.Mar_Bottom = 5;
										a.Orientation = "纵向";
										a.Paper_Size = "A4";
										a.ApplySetup();
										a.PrintWithOutSetupPrintWithOutByID("samplemodel");
										var iframe = document.getElementById("tablemodel");
										iframe.contentWindow.focus();
										a.Mar_left = 10;
										a.Mar_Top = 10;
										a.Mar_Right = 10;
										a.Mar_Bottom = 10;
										a.Orientation = "横向";
										a.Paper_Size = "A4";
										a.ApplySetup();
										a.PrintWithOutSetupPrintWithOutByID("tablemodel");
										win.close();
									}
								},
								failure : function() {
									Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
								}
							});
						}
						var win = Ext.create("Ext.window.Window", {
							title : "打印预览",
							iconCls : 'Find',
							layout : "auto",
							maximized : true,
							maximizable : true,
							modal : true,
							bodyStyle : "background-color:white;",
							html : "<iframe width=100% height=100% id='dnamodel' src='"
									+ src
									+ "'></iframe><iframe width=100% height=100% id='samplemodel' src='judicial/print/getSampleImg.do?case_code="
									+ value
									+ "'></iframe><iframe width=100% height=100% id='tablemodel' src='"
									+ chart + "'></iframe>",
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
				}
			});
		})
			

//		}
	} else {
		Ext.Msg.alert("提示", "该浏览器不支持打印,请使用IE!");
	}
}

printCase = function(value, url, copies, print_count, case_id) {
	if (!!window.ActiveXObject || "ActiveXObject" in window) {
//		if (attach_need == 0) {
//			Ext.Msg.alert("提示", "此案例需要打印附本!", function() {
//				var src = url + "&case_code=" + value + "&case_id=" + case_id;
//				var print_chanel = function() {
//					win.close();
//				}
//				var print_print = function(me) {
//					Ext.Ajax.request({
//								url : "judicial/print/usePrintCount.do",
//								method : "POST",
//								headers : {
//									'Content-Type' : 'application/json'
//								},
//								jsonData : {
//									case_code : value,
//									print_count : print_count
//								},
//								success : function(response, options) {
//									response = Ext.JSON
//											.decode(response.responseText);
//									if (response == true) {
//										var iframe = document
//												.getElementById("dnamodel");
//										iframe.contentWindow.focus();
//										iframe.contentWindow.print();
//										win.close();
//									}
//								},
//								failure : function() {
//									Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
//								}
//							});
//				}
//				var win = Ext.create("Ext.window.Window", {
//							title : "打印预览",
//							iconCls : 'Find',
//							layout : "auto",
//							maximized : true,
//							maximizable : true,
//							modal : true,
//							bodyStyle : "background-color:white;",
//							html : "<iframe width=100% height=100% id='dnamodel' src='"
//									+ src + "'></iframe>",
//							buttons : [{
//										text : '打印',
//										iconCls : 'Disk',
//										handler : print_print
//									}, {
//										text : '取消',
//										iconCls : 'Cancel',
//										handler : print_chanel
//									}]
//						});
//				win.show();
//			});
//		} else {
		Ext.Msg.alert("提示", "此案例需要打印"+copies+"份!", function() {
			var src = url + "&case_code=" + value + "&case_id=" + case_id;
			var print_chanel = function() {
				win.close();
			}
			var print_print = function(me) {
				Ext.Ajax.request({
							url : "judicial/print/usePrintCount.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : {
								case_code : value,
								print_count : print_count
							},
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response == true) {
									var iframe = document
											.getElementById("dnamodel");
									iframe.contentWindow.focus();
									iframe.contentWindow.print();
									win.close();
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
							}
						});
			}
			var win = Ext.create("Ext.window.Window", {
						title : "打印预览",
						iconCls : 'Find',
						layout : "auto",
						maximized : true,
						maximizable : true,
						modal : true,
						bodyStyle : "background-color:white;",
						html : "<iframe width=100% height=100% id='dnamodel' src='"
								+ src + "'></iframe>",
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
		})
			
//		}
	} else {
		Ext.Msg.alert("提示", "该浏览器不支持打印,请使用IE!");
	}
}

printSample = function(value) {
	if (!!window.ActiveXObject || "ActiveXObject" in window) {
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
	} else {
		Ext.Msg.alert("提示", "该浏览器不支持打印,请使用IE!");
	}
}

printTable = function(value, case_id, print_chart) {
	var chart = print_chart + "&case_code=" + value + "&case_id=" + case_id;
	if (!!window.ActiveXObject || "ActiveXObject" in window) {
		Ext.Ajax.request({
			url : "judicial/print/exsitPrintTable.do",
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
					Ext.Msg.alert("提示", "此图表无法打印！");
				} else {
					var print_chanel = function() {
						win.close();
					}
					var print_print = function(me) {
						var iframe = document.getElementById("tablemodel");
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
						html : "<iframe width=100% height=100% id='tablemodel' src='"
								+ chart + "'></iframe>",
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
	} else {
		Ext.Msg.alert("提示", "该浏览器不支持打印,请使用IE!");
	}

}

Ext.define("Rds.judicial.panel.JudicialChangePrintPanel", {
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
					regex : /^\w*$/,
					regexText : '请输入案例编号',
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
								fields : ['Name', 'Code'],
								data : [['全部', -1], ['已打印', 0], ['未打印', 1]]
							}),
					value : -1,
					mode : 'local',
					// typeAhead: true,
					name : 'print_state'
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
					store : storeModel
				});
		var parnter_name = Ext.create('Ext.form.field.Text', {
			name : 'parnter_name',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '合作商'
		});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['case_id', 'case_code', 'case_areaname',
							'case_areacode', 'receiver_area', 'report_url',
							'case_receiver', 'print_copies', 'urgent_state','copies',
							'print_count', 'accept_time', 'close_time',
							'report_modelname', 'report_model', 'attach_need',
							"receiver_id", 'report_chart', 'sample_in_time',
							'case_id', 'is_delete', 'compare_date'],
					start:0,
					limit:15,
					pageSize:15,
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/print/getChangePrintCaseInfo.do',
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
						'beforeload' : function(ds, operation, opt) {
							Ext.apply(me.store.proxy.extraParams, {
										endtime : dateformat(endtime.getValue()),
										modeltype : report_type.getValue(),
										starttime : dateformat(starttime.getValue()),
										print_state : print_state.getValue(),
										case_code : trim(case_code.getValue()),
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
		me.columns = [{
					text : '案例条形码',
					dataIndex : 'case_code',
					menuDisabled : true,
					flex : 150
				}, {
					text : '案例归属地',
					dataIndex : 'receiver_area',
					menuDisabled : true,
					flex : 250
				}, {
					text : '归属人',
					dataIndex : 'case_receiver',
					menuDisabled : true,
					flex : 100
				}, {
					text : '紧急程度',
					dataIndex : 'urgent_state',
					menuDisabled : true,
					flex : 100,
					renderer : function(value) {
						switch (value) {
							case 0 :
								return "普通";
								break;
							case 1 :
								return "<span style='color:red'>紧急</span>";
								break;
							default :
								return "";
						}
					}
				},  {
					text : '打印份数',
					dataIndex : 'copies',
					menuDisabled : true,
					flex : 100
				}, {
					text : '打印状态',
					dataIndex : 'print_count',
					menuDisabled : true,
					flex : 150,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
//						var print_copies = record.data["print_copies"];
						var print_count = record.data["print_count"];
						return "<span style='color:green'>已打印了"+print_count+"次</span>"
//						if (print_copies == 0) {
//							return "<span style='color:red'>无法打印（未设置打印次数）</span>";
//						} else if (print_copies - print_count > 0) {
//							return "<span style='color:green'>可以打印</span>，拥有<span style='color:green'>"
//									+ print_copies
//									+ "</span>次打印次数,已打印了<span style='color:red'>"
//									+ print_count + "</span>次";
//						} else {
//							return "<span style='color:red'>无法打印</span>，拥有<span style='color:green'>"
//									+ print_copies
//									+ "</span>次打印次数,已打印了<span style='color:red'>"
//									+ print_count + "</span>次";
//						}

					}
				}, {
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
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [case_code, report_type, print_state, starttime,endtime]
				},{
					style : {
				        borderTopWidth : '0px !important',
				        borderBottomWidth : '0px !important'
				 	},
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [parnter_name,{
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				
				}, {
					xtype : 'toolbar',
					dock : 'top',
					items : [{
								text : '查看样本信息',
								iconCls : 'Find',
								handler : me.onFind
							}, {
								text : '一键报告打印',
								iconCls : 'Printer',
								handler : me.onCasePrintOneKey
							}, {
								text : '报告打印',
								iconCls : 'Printer',
								handler : me.onCasePrint
							}, {
								text : '图表打印',
								iconCls : 'Printermono',
								handler : me.onTablePrint
							}, {
								text : '条纹图打印',
								iconCls : 'Printercolor',
								handler : me.onPhotoPrint
							}, {
								text : '案例照片打印',
								iconCls : 'Printercolor',
								handler : me.onCasePhotoPrint
							}]
				}];

		me.callParent(arguments);
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage = 1;
		me.getStore().load();
	},
	onCasePrintOneKey : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要打印的记录!");
			return;
		};
		var print_copies = selections[0].get("print_copies");
		var case_code = selections[0].get("case_code");
		var case_id = selections[0].get("case_id");
		var print_url = selections[0].get("report_url");
		var copies = selections[0].get("copies");
		var print_count = selections[0].get("print_count");
		var print_chart = selections[0].get("report_chart");
//		if (print_copies - print_count > 0) {
			printCaseOneKey(case_code, print_url, copies, print_count,
					case_id, print_chart);
//		} else if (print_copies == 0) {
//			Ext.Msg.alert("提示", "无打印权限!");
//		} else {
//			Ext.Msg.alert("提示", "打印次数已用完!");
//		}
	},
	onCasePrint : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要打印的记录!");
			return;
		};
		var print_copies = selections[0].get("print_copies");
		var case_code = selections[0].get("case_code");
		var case_id = selections[0].get("case_id");
		var print_url = selections[0].get("report_url");
		var copies = selections[0].get("copies");
		var print_count = selections[0].get("print_count");
//		if (print_copies - print_count > 0) {
			printCase(case_code, print_url, copies, print_count, case_id);
//		} else if (print_copies == 0) {
//			Ext.Msg.alert("提示", "无打印权限!");
//		} else {
//			Ext.Msg.alert("提示", "打印次数已用完!");
//		}
	},
	onTablePrint : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要打印的记录!");
			return;
		};
		var print_copies = selections[0].get("print_copies");
		var print_count = selections[0].get("print_count");
		var case_code = selections[0].get("case_code");
		var case_id = selections[0].get("case_id");
		var report_chart = selections[0].get("report_chart");
//		if (print_copies - print_count > 0) {
			printTable(case_code, case_id, report_chart)
//		} else if (print_copies == 0) {
//			Ext.Msg.alert("提示", "无打印权限!");
//		} else {
//			Ext.Msg.alert("提示", "打印次数已用完!");
//		}
	},
	onPhotoPrint : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要打印的记录!");
			return;
		};
		var print_copies = selections[0].get("print_copies");
		var print_count = selections[0].get("print_count");
		var case_code = selections[0].get("case_code");
//		if (print_copies - print_count > 0) {
			printSample(case_code)
//		} else if (print_copies == 0) {
//			Ext.Msg.alert("提示", "无打印权限!");
//		} else {
//			Ext.Msg.alert("提示", "打印次数已用完!");
//		}
	},
	onCasePhotoPrint : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要打印的记录!");
			return;
		};
		var case_code = selections[0].get("case_code");
		var copies = selections[0].get("copies");
		printCaseImg(case_code,copies);
	},
	onFind : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		};
		var win = Ext.create("Ext.window.Window", {
					title : "样本信息（案例条形码：" + selections[0].get("case_code")
							+ "）",
					width : 600,
					iconCls : 'Find',
					height : 400,
					layout : 'border',
					bodyStyle : "background-color:white;",
					items : [Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 600,
								height : 400,
								frame : false,
								viewConfig : {
									forceFit : true,
									stripeRows : true
									// 在表格中显示斑马线
								},
								store : {// 配置数据源
									fields : ['sample_id', 'sample_code',
											'sample_type', 'sample_typename',
											'sample_call', 'sample_callname',
											'sample_username', 'id_number',
											'birth_date'],// 定义字段
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
										}]
							})]
				});
		win.show();
	},
	listeners : {
		'afterrender' : function() {
			this.store.load();
		}
	}
});
