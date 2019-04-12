function dateformat(value) {
	if (value != null) {
		return Ext.Date.format(value, 'Y-m-d');
	} else {
		return '';
	}
}
onDownload = function(value) {
	window.location.href = "judicial/attachment/download.do?id=" + value;
}
Ext.define("Rds.judicial.panel.JudicialCasePhotoUploadPanel", {
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
							// regex : /^\w*$/,
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
							value : Ext.Date.add(new Date(), Ext.Date.DAY),
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
							fields : ['id', 'case_code', 'attachment_path',
									'attachment_date', 'attachment_type'],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'post'
								},
								url : 'judicial/casephoto/getAllCasePhoto.do',
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
											// endtime :
											// dateformat(endtime.getValue())
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
							text : '案例编号',
							dataIndex : 'case_code',
							width : '30%',
							menuDisabled : true,
							flex : 2
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
						}

				];

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
										text : '照片上传',
										iconCls : 'Add',
										handler : me.onCasePhotoUpload
									}]
						}];
				me.store.load();
				me.callParent(arguments);
			},
			onCasePhotoUpload : function() {
				var me = this.up("gridpanel");
				var form = Ext.create(
						"Rds.judicial.form.JudicialCasePhotoForm", {
							region : "center",
							grid : me
						});
				var win = Ext.create("Ext.window.Window", {
							title : '上传新文件',
							width : 700,
							iconCls : 'Add',
							maximizable : true,
							maximized : true,
							height : 700,
							layout : 'border',
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
			onSearch : function() {
				var me = this.up("gridpanel");
				me.getStore().currentPage = 1;
				me.getStore().load();
			}
		});