/**
 * @class judicialAttachmentPanel
 * @description 附件管理
 * @author fushaoming
 * @date 20150407
 */
onDownload = function(value) {
	window.location.href = "judicial/attachment/download.do?id=" + value;
}
Ext.define("Rds.judicial.panel.JudicialAttachmentGridPanel", {
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
//					regex : /^\w*$/,
					regexText : '请输入英文或数字',
					fieldLabel : '案例条形码'
				});
		var starttime = Ext.create('Ext.form.field.Date', {
					name : 'starttime',
					// id : 'starttime_attachment_grid',
					width : 175,
					fieldLabel : '受理日期从',
					labelWidth : 70,
					labelAlign : 'right',
					emptyText : '请选择日期',
					format : 'Y-m-d',
					value : Ext.Date.add(new Date(), Ext.Date.DAY, -3),
					maxValue : new Date(),
					listeners : {
						'select' : function() {
							var start = starttime.getValue();
							endtime
									.setMinValue(start);
							var endDate = endtime
									.getValue();
							if (start > endDate) {
								endtime
										.setValue(start);
							}
						}
					}
				});
		var endtime = Ext.create('Ext.form.field.Date', {
					//id : 'endtime_attachment_grid',
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
							var start = starttime.getValue();
							var endDate = endtime
									.getValue();
							if (start > endDate) {
								starttime.setValue(endDate);
							}
						}
					}
				})
		me.store = Ext.create('Ext.data.Store', {
					fields : ['id', 'case_id','case_code', 'attachment_path','username',
							'attachment_date', 'attachment_type','verify_state'],
					start:0,
					limit:15,
					pageSize:15,
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'judicial/attachment/queryallpage.do',
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
										search : search.getValue(),
										starttime : dateformat(starttime
												.getValue()),
										endtime : dateformat(endtime.getValue())
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
					dataIndex : 'id',
					hidden : true
				}, {
					text : '案例条形码',
					dataIndex : 'case_code',
					width : '30%',
					menuDisabled : true,
					flex : 2,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
							return "<a href='#'>"+ record.data["case_code"]+"</a>";
					},
					listeners:{
						'click':function(){ 
							var me = this.up("gridpanel");
							var selections = me.getView().getSelectionModel().getSelection();
							if (selections.length < 1 || selections.length > 1) {
								Ext.Msg.alert("提示", "请选择需要查看的一条记录!");
								return;
							}
							console.log(selections[0].get(""));
							var form = Ext.create(
									"Rds.judicial.form.JudicialImageShow", {
										region : "center",
										grid : me
									});
							var win = Ext.create("Ext.window.Window", {
										title : '图片查看',
										width : 1600,
										iconCls : 'Pageedit',
										height : 1000,
										maximizable : true,
										maximized : true,
										layout : 'border',
										items : [form]
									});
							form.loadRecord(selections[0]);
							win.show();
						}
					}
				}, {
					text : '附件',
					dataIndex : 'attachment_path',
					width : '40%',
					menuDisabled : true,
					flex : 3
				}, {
					text : '类型',
					dataIndex : 'attachment_type',
					width : '40%',
					menuDisabled : true,
					flex : 1,
					renderer : function(value) {
						switch (value) {
							case 1 :
								return "登记表格";
								break;
							case 2 :
								return "身份证";
								break;
							case 3 :
								return "照片";
								break;
							case 4 :
								return "其他";
								break;
							case 6 :
								return "条纹图";
								break;
							default :
								return "其他";
						}
					}
				}, {
					text : '最后上传日期',
					dataIndex : 'attachment_date',
					width : '10%',
					menuDisabled : true,
					flex : 1
				},
				{
					text : '上传人员',
					dataIndex : 'username',
					width : '10%',
					menuDisabled : true,
					flex : 1,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						var isnull= record.data["username"];
						if (isnull == null) {
							return "系统生成"
						} else
						{
							return value;
						}

					}
				},
				{
					header : "操作",
					dataIndex : '',
					flex : 0.5,
					menuDisabled : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						var id = record.data["id"];
						return "<a href='#' onclick=\"onDownload('" + id
								+ "')\"  >下载";

					}
				}

		];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [search, starttime, endtime,
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
							},{
								text : '删除',
								iconCls : 'Delete',
								handler : me.onDelete
							},'Tips：点击案例编号查看上传图片']
				}];
		me.store.load();
		me.callParent(arguments);
	},
	onInsert : function() {
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.judicial.panel.JudicialAttachmentFormPanel",
				{
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
//					listeners : {
//						close : {
//							fn : function() {
//								me.getStore().load();
//							}
//						}
//					}
				});
		win.show();
	},
	onDelete : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
				.getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		};
		if(!(selections[0].get("verify_state") == '0' || selections[0].get("verify_state") == '2'))
		{
			Ext.MessageBox.alert("提示信息","该案例状态不允许删除！");
			return;
		}
		Ext.Msg.show({
					title : '提示',
					msg : '确定删除该记录?',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							var values = {
								id : selections[0].get("id"),
								attachment_path:selections[0].get("attachment_path")
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
											console.log(response.result)
											if (response.result == true) {
												Ext.MessageBox.alert("提示信息",
														response.msg);
												me.getStore().load();
											} else {
												Ext.MessageBox.alert("错误信息",
														response.msg);
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
//	onUpdate : function() {
//		var me = this.up("gridpanel");
//		var selections = me.getView().getSelectionModel().getSelection();
//		if (selections.length < 1) {
//			Ext.Msg.alert("提示", "请选择需要修改的记录!");
//			return;
//		};
//		var form = Ext.create("Rds.judicial.panel.JudicialAttachmentFormPanel",
//				{
//					region : "center",
//					grid : me
//				});
//		var win = Ext.create("Ext.window.Window", {
//					title : '增加文件——增加',
//					width : 600,
//					iconCls : 'Pageedit',
//					height : 500,
//					modal : true,
//					layout : 'border',
//					items : [form]
//				});
//		win.show();
//		form.loadRecord(selections[0]);
//		form.down([filefield]).value = null;
//	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage = 1;
		me.getStore().load();
	}
});