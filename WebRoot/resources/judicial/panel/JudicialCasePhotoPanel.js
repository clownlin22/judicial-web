String.prototype.endWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	if (this.substring(this.length - str.length) == str)
		return true;
	else
		return false;
	return true;
}
function dateformat(value) {
	if (value != null) {
		return Ext.Date.format(value, 'Y-m-d');
	} else {
		return '';
	}
}

function trim(value) {
	return Ext.util.Format.trim(value);
}
Ext.define('model', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'key',
						mapping : 'key',
						type : 'string'
					}, {
						name : 'value',
						mapping : 'value',
						type : 'string'
					}, {
						name : 'name',
						mapping : 'name',
						type : 'string'
					}, {
						name : 'id',
						mapping : 'id',
						type : 'string'
					}]
		});

printCaseImg = function(code, count) {
	var print_chanel = function() {
		win.close();
	}
	var print_print = function(me) {
		Ext.Ajax.request({
					url : "judicial/attachment/updateAllAttachmentPrint.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : {
						case_code : code
					},
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response == true) {
							var iframe = document
									.getElementById("printimgmodel");
							iframe.contentWindow.focus();
							iframe.contentWindow.print();
							win.close();
						} else {
							Ext.Msg.alert("提示", "打印出错");
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
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
		html : "<iframe width=100% height=100% id='printimgmodel' src='judicial/attachment/printAllAttachment.do?case_code="
				+ code
				+ "&count="
				+ count
				+ "&v="
				+ new Date().getTime()
				+ "'></iframe>",
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
};

Ext.define("Rds.judicial.panel.JudicialCasePhotoPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : true,
		stripeRows : true
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
					width : 200,
					regexText : '请输入案例编号',
					fieldLabel : '案例编号'
				});
		var receiver = Ext.create('Ext.form.field.Text', {
					name : 'receiver',
					labelWidth : 60,
					width : 200,
					fieldLabel : '归属人'
				});
		var client = Ext.create('Ext.form.field.Text', {
					name : 'client',
					labelWidth : 60,
					width : 200,
					fieldLabel : '委托人'
				});
		var starttime = Ext.create('Ext.form.DateField', {
					name : 'starttime',
					width : 175,
					labelWidth : 70,
					fieldLabel : '比对时间 从',
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
					width : 135,
					labelWidth : 20,
					fieldLabel : '到',
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
					fieldLabel : '报告状态',
					width : 155,
					labelWidth : 60,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
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
		var photo_state = Ext.create('Ext.form.ComboBox', {
					fieldLabel : '照片状态',
					width : 155,
					labelWidth : 60,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					valueField : 'Code',
					store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['全部', -1], ['已打印', 1], ['未打印', 0]]
							}),
					value : -1,
					mode : 'local',
					// typeAhead: true,
					name : 'photo_state'
				});
		var report_type = Ext.create('Ext.form.ComboBox', {
					fieldLabel : '模板类型',
					mode : 'local',
					labelWidth : 60,
					editable : false,
					valueField : "key",
					width : 200,
					displayField : "value",
					name : 'report_type',
					store : storeModel
				});
		me.selModel = Ext.create('Ext.selection.CheckboxModel', {});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['case_id', 'case_code', 'case_areaname',
							'case_areacode', 'receiver_area', 'report_url',
							'case_receiver', 'print_copies', 'urgent_state',
							'print_count', 'accept_time', 'close_time',
							'report_modelname', 'report_model', 'attach_need',
							"receiver_id", 'report_chart', 'sample_in_time',
							'case_id', 'is_delete', 'compare_date', 'client',
							'photo_state', 'attachment_path', 'attachment_date'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/casephoto/getPrintCaseInfo.do',
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
										starttime : dateformat(starttime
												.getValue()),
										print_state : print_state.getValue(),
										case_code : case_code.getValue(),
										photo_state : photo_state.getValue(),
										client : client.getValue(),
										receiver : receiver.getValue()
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
					width : 200
				}, {
					text : '案例归属地',
					dataIndex : 'receiver_area',
					menuDisabled : true,
					width : 250
				}, {
					text : '所属人',
					dataIndex : 'case_receiver',
					menuDisabled : true,
					width : 250
				}, {
					text : '委托人',
					dataIndex : 'client',
					menuDisabled : true,
					width : 250
				}, {
					text : '照片状态',
					dataIndex : 'photo_state',
					menuDisabled : true,
					width : 200,
					renderer : function(value) {
						switch (value) {
							case '0' :
								return "<span style='color:red'>没有打印</span>";
								break;
							case '1' :
								return "已打印";
								break;
							case '' :
								return "照片未上传";
								break;
							case null :
								return '照片未上传';
								break;
							default :
								return "";
						}
					}
				}, {
					text : '是否需要附本',
					dataIndex : 'attach_need',
					menuDisabled : true,
					width : 200,
					renderer : function(value) {
						switch (value) {
							case 0 :
								return "<span style='color:red'>需要</span>";
								break;
							case 1 :
								return "不需要";
								break;
							default :
								return "";
						}
					}
				}, {
					text : '报告打印状态',
					dataIndex : 'print_count',
					menuDisabled : true,
					width : 350,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						var print_copies = record.data["print_copies"];
						var print_count = record.data["print_count"];
						if (print_copies == 0) {
							return "<span style='color:red'>无法打印（未设置打印次数）</span>";
						} else if (print_copies - print_count > 0) {
							return "<span style='color:green'>可以打印</span>，拥有<span style='color:green'>"
									+ print_copies
									+ "</span>次打印次数,已打印了<span style='color:red'>"
									+ print_count + "</span>次";
						} else {
							return "<span style='color:red'>无法打印</span>，拥有<span style='color:green'>"
									+ print_copies
									+ "</span>次打印次数,已打印了<span style='color:red'>"
									+ print_count + "</span>次";
						}

					}
				}, {
					text : '照片上传日期',
					dataIndex : 'attachment_date',
					menuDisabled : true,
					width : 150
				}, {
					text : '比对日期',
					dataIndex : 'compare_date',
					menuDisabled : true,
					width : 150
				}, {
					text : '样本登记日期',
					dataIndex : 'sample_in_time',
					menuDisabled : true,
					width : 150
				}, {
					text : '案例受理日期',
					dataIndex : 'sample_in_time',
					menuDisabled : true,
					width : 150
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [case_code, report_type, print_state, starttime,
							endtime]
				}, {
					xtype : 'toolbar',
					dock : 'top',
					items : [receiver, client, photo_state, {
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

	onCasePhotoPrint : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要打印的记录!");
			return;
		};
		var case_code = '';
		var attach_need = '';
		for (var i = 0; i < selections.length; i++) {
			case_code = case_code.concat(selections[i].get("case_code"), "|");
			attach_need = attach_need.concat(selections[i].get("attach_need"),
					"|");
		}
		// var case_code = selections[0].get("case_code");
		// var attach_need = selections[0].get("attach_need");
		printCaseImg(case_code, attach_need);
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
