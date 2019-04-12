onDownload = function(value) {
	window.location.href = "judicial/attachment/download.do?id=" + value;
}
Ext.define("Rds.alcohol.panel.AlcoholAttachmentGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var search = Ext.create('Ext.form.field.Text', {
					name : 'search',
					labelWidth : 70,
					width : 200,
					regexText : '请输入英文或数字',
					fieldLabel : '案例条形码'
				});
		var starttime = Ext.create('Ext.form.field.Date', {
					name : 'starttime',
					width : 175,
					fieldLabel : '受理日期从',
					labelWidth : 70,
					labelAlign : 'right',
					emptyText : '请选择日期',
					format : 'Y-m-d',
					maxValue : new Date(),
					listeners : {
						'select' : function() {
							var start = starttime.getValue();
							endtime.setMinValue(start);
							var endDate = endtime.getValue();
							if (start > endDate) {
								endtime.setValue(start);
							}
						}
					}
				});
		var endtime = Ext.create('Ext.form.field.Date', {
					// id : 'endtime_attachment_grid',
					name : 'endtime',
					width : 135,
					labelWidth : 20,
					fieldLabel : '到',
					labelAlign : 'right',
					emptyText : '请选择日期',
					format : 'Y-m-d',
					maxValue : new Date(),
					listeners : {
						select : function() {
							var start = starttime.getValue();
							var endDate = endtime.getValue();
							if (start > endDate) {
								starttime.setValue(endDate);
							}
						}
					}
				})
		me.store = Ext.create('Ext.data.Store', {
					fields : ['att_id', 'case_id', 'case_code', 'att_path'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'alcohol/attachment/getAttachment.do',
						params : {},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'total'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							Ext.apply(me.store.proxy.params, {
								case_code : search.getValue()
									// ,
									// starttime : dateformat(starttime
									// .getValue()),
									// endtime : dateformat(endtime.getValue())
								});
						}
					}
				});

		me.selModel = Ext.create('Ext.selection.CheckboxModel', {
					mode : 'SINGLE'
				});

		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : me.pageSize,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
					dataIndex : 'att_id',
					hidden : true
				}, {
					dataIndex : 'case_id',
					hidden : true
				}, {
					text : '案例条形码',
					dataIndex : 'case_code',
					width : '30%',
					menuDisabled : true,
					flex : 2
				}, {
					text : '附件',
					dataIndex : 'att_path',
					width : '40%',
					menuDisabled : true,
					flex : 3
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [search,
							// starttime, endtime,
							{
						text : '查询',
						iconCls : 'Find',
						handler : me.onSearch
					}]
				}, {
					xtype : 'toolbar',
					dock : 'top',
					items : [{
								text : '上传',
								iconCls : 'Add',
								handler : me.onInsert
							}, {
								text : '打印',
								iconCls : 'Printer',
								handler : me.onPrint
							}]
				}];
		me.store.load();
		me.callParent(arguments);
	},
	onDownload : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		window.location.href = "alcohol/attachment/download.do?id="
				+ selections[0].get("id");
	},
	onInsert : function() {
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.alcohol.form.AlcoholAttachmentForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '上传新文件',
					width : 500,
					iconCls : 'Add',
					height : 500,
					layout : 'border',
					modal : true,
					items : [form],
					listeners : {
						close : {
							fn : function() {
								me.getStore().load();
							}
						}
					}
				});
		win.show();
	},
	onDelete : function() {
		var me = this.up("gridpanel");
		Ext.Msg.show({
					title : '提示',
					msg : '确定删除该记录',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							var selections = me.getView().getSelectionModel()
									.getSelection();
							if (selections.length < 1) {
								Ext.Msg.alert("提示", "请选择需要修改的记录!");
								return;
							};
							var values = {
								case_code : selections[0].get("id")

							};
							Ext.Ajax.request({
										url : "judicial/attachment/delete.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : values,
										success : function(response, options) {
											response = Ext.JSON
													.decode(response.responseText);
											if (response.result == true) {
												Ext.MessageBox.alert("提示信息",
														response.message);
												me.getStore().load();
											} else {
												Ext.MessageBox.alert("错误信息",
														response.message);
											}
										},
										failure : function() {
											Ext.Msg.alert("提示",
													"保存失败<br>请联系管理员!");
										}

									});
						} else {
							return;
						}

					},
					animateTarget : 'addAddressBtn',
					icon : Ext.window.MessageBox.INFO
				})
	},
	onPrint : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要打印的记录!");
			return;
		}
		var att_id = selections[0].get("att_id");
		Ext.MessageBox.confirm('提示','是否小白鼠案例？',
				function(id) {
					if (id == 'yes') {
						var print_print = function(me) {
							Ext.MessageBox.confirm('提示', '确定打印吗？', function(id) {
										if (id == 'yes') {
											var iframe = document.getElementById("alcoholatt");
											iframe.contentWindow.focus();
											iframe.contentWindow.print();
											win.close();
										}
									});
						}
						var print_chanel = function() {
							win.close();
						}

						var src = "alcohol/attachment/getPrintAttXBS.do?att_id=" + att_id;
						var win = Ext.create("Ext.window.Window", {
									title : '附件打印',
									maximized : true,
									maximizable : true,
									iconCls : 'Printer',
									modal : true,
									bodyStyle : "background-color:white;",
									html : "<iframe width=100% height=100% id='alcoholatt' src='"
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
					}else
					{
						var print_print = function(me) {
							Ext.MessageBox.confirm('提示', '确定打印吗？', function(id) {
										if (id == 'yes') {
											var iframe = document.getElementById("alcoholatt");
											iframe.contentWindow.focus();
											iframe.contentWindow.print();
											win.close();
										}
									});
						}
						var print_chanel = function() {
							win.close();
						}

						var src = "alcohol/attachment/getPrintAtt.do?att_id=" + att_id;
						var win = Ext.create("Ext.window.Window", {
									title : '附件打印',
									maximized : true,
									maximizable : true,
									iconCls : 'Printer',
									modal : true,
									bodyStyle : "background-color:white;",
									html : "<iframe width=100% height=100% id='alcoholatt' src='"
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
					}
		})
		
		

	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage = 1;
		me.getStore().load();
	}
});