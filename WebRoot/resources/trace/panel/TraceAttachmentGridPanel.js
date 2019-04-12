/**
 * @class judicialAttachmentPanel
 * @description 附件管理
 * @author fushaoming
 * @date 20150407
 */
onDownload = function(value)
{
	window.location.href = "trace/attachment/download.do?uuid=" +value;
}
Ext.define("Rds.trace.panel.TraceAttachmentGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;

		me.store = Ext.create('Ext.data.Store', {
					fields : ['uuid','case_id', 'case_no','year','receive_time', 'attachment_path',
							'attachment_date'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'trace/attachment/queryAttachMent.do',
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
                                start_time:dateformat(me.getDockedItems('toolbar')[0].
                                    getComponent('start_time').getValue()),
                                end_time:dateformat(me.getDockedItems('toolbar')[0].
                                    getComponent('end_time').getValue()),
                                case_no:me.getDockedItems('toolbar')[0].
                                    getComponent('case_no').getValue()
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
					dataIndex : 'uuid',
					hidden : true
				}, {
					text : '案例序号',
					dataIndex : 'case_no',
					width : '30%',
					menuDisabled : true,
					flex : 2
				}, {
                    text : '年份',
                    dataIndex : 'year',
                    width : '30%',
                    menuDisabled : true,
                    flex : 2
                },  {
					text : '附件',
					dataIndex : 'attachment_path',
					width : '40%',
					menuDisabled : true,
					flex : 3
				}, {
					text : '最后上传日期',
					dataIndex : 'attachment_date',
					width : '10%',
					menuDisabled : true,
					flex : 1
				}, 
//				{
//					xtype : 'actioncolumn',
//					width : '10%',
//					items : [{
//								iconCls : 'Picturesave',
//								tooltip : '保存',
//								handler : function(value){
//									console.log(value);
//									onDownload(1);
//								}
//							}]
//				},
				{
					header : "操作",
					dataIndex : '',
					flex : 0.5,
					menuDisabled : true,
					renderer : function(value, cellmeta, record,
							rowIndex, columnIndex, store) {
						var uuid = record.data["uuid"];
						return "<a href='#' onclick=\"onDownload('"
								+ uuid
								+ "')\"  >下载";

					}
				}
				
				];
        me.dockedItems = [
            {
                xtype : 'toolbar',
                name : 'search',
                dock : 'top',
                items : [
                    {
                        xtype : 'textfield',
                        name : 'case_no',
                        itemId : 'case_no',
                        labelWidth : 70,
                        width : 155,
                        fieldLabel : '案例序号'
                    },
                    {
                        xtype : 'datefield',
                        name : 'start_time',
                        itemId : 'start_time',
                        width : 175,
                        fieldLabel : '上传时间 从',
                        labelWidth : 70,
                        labelAlign : 'right',
                        emptyText : '请选择日期',
                        format : 'Y-m-d',
                        maxValue : new Date(),
                        listeners : {
                            'select' : function() {
                                var start = me.getDockedItems('toolbar')[0].
                                    getComponent('start_time').getValue();
                                me.getDockedItems('toolbar')[0].
                                    getComponent('end_time')
                                    .setMinValue(
                                    start);
                                var endDate = me.getDockedItems('toolbar')[0].
                                    getComponent('end_time').getValue()
                                if (start > endDate) {
                                    me.getDockedItems('toolbar')[0].
                                        getComponent('start_time').setValue(start);
                                }
                            }
                        }
                    },
                    {
                        xtype : 'datefield',
                        itemId : 'end_time',
                        name : 'end_time',
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
                                var start = me.getDockedItems('toolbar')[0].
                                    getComponent('start_time').getValue();
                                var endDate = me.getDockedItems('toolbar')[0].
                                    getComponent('end_time').getValue();
                                if (start > endDate) {
                                    me.getDockedItems('toolbar')[0].
                                        getComponent('start_time').
                                        setValue(endDate);
                                }
                            }
                        }
                    }, {
                        text : '查询',
                        iconCls : 'Find',
                        handler : me.onSearch
                    } ]
            }, {
                xtype : 'toolbar',
                dock : 'top',
                items : [{
                    text : '上传',
                    iconCls : 'Add',
                    handler : me.onInsert
                }, {
                    text : '删除',
                    iconCls : 'Delete',
                    hidden : true,
                    handler : me.onDelete
                }]
            }];
		me.callParent(arguments);
	},
	onDownload : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		window.location.href = "judicial/attachment/download.do?id=" +
				selections[0].get("id");

	},
	onInsert : function() {
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.trace.form.TraceAttachmentFormPanel",
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
	onUpdate : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		};
		var form = Ext.create("Rds.judicial.panel.JudicialAttachmentFormPanel",
				{
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '增加文件——增加',
					width : 600,
					iconCls : 'Pageedit',
					height : 500,
					modal : true,
					layout : 'border',
					items : [form]
				});
		win.show();
		form.loadRecord(selections[0]);
		form.down([filefield]).value = null;
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