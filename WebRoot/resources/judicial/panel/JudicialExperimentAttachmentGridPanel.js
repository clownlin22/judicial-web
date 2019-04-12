onDownload = function(value)
{
	window.location.href = "judicial/experimentAttachment/download.do?uuid=" +value;
}
Ext.define("Rds.judicial.panel.JudicialExperimentAttachmentGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var upload_username = Ext.create('Ext.form.field.Text', {
			name : 'upload_username',
			labelWidth : 60,
			width : 200,
			fieldLabel : '上传人员'
		});
		var download_username = Ext.create('Ext.form.field.Text', {
			name : 'download_username',
			labelWidth : 60,
			width : 200,
			fieldLabel : '下载人员'
		});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['uuid', 'attachment_path','upload_username',
							'attachment_date','download_time','download_username'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'judicial/experimentAttachment/queryAttachMent.do',
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
                                upload_username:trim(upload_username.getValue()),
                                download_username:trim(download_username.getValue())
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
		me.columns = [Ext.create("Ext.grid.RowNumberer",{text: '序号',width : 40, menuDisabled:true}),{
					dataIndex : 'uuid',
					hidden : true
				}, {
					text : '附件',
					dataIndex : 'attachment_path',
					width : 200,
					menuDisabled : true
				}, {
					text : '最后上传日期',
					dataIndex : 'attachment_date',
					width : 150,
					menuDisabled : true
				}, {
					text : '上传人员',
					dataIndex : 'upload_username',
					width : 100,
					menuDisabled : true
				},
				{
					text : '下载人员',
					dataIndex : 'download_username',
					width : 100,
					menuDisabled : true
				},
				{
					text : '最后下载日期',
					dataIndex : 'download_time',
					width : 150,
					menuDisabled : true
				},
				{
					header : "操作",
					dataIndex : '',
					width : 100,
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
                        value : Ext.Date.add(
            					new Date(),
            					Ext.Date.DAY,-7),
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
                    }, upload_username,download_username,{
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
                    handler : me.onDelete
                }]
            }];
		me.callParent(arguments);
	},
	onDownload : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		window.location.href = "judicial/experimentAttachment/download.do?uuid=" +
				selections[0].get("uuid");

	},
	onInsert : function() {
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.judicial.form.JudicialExprimentAttachmentFormPanel",
				{
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '上传新文件',
					width : 400,
					iconCls : 'Add',
					height : 200,
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
					msg : '确定删除该记录？',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							var selections = me.getView().getSelectionModel()
									.getSelection();
							if (selections.length < 1) {
								Ext.Msg.alert("提示", "请选择需要删除的记录!");
								return;
							};
							var values = {
								uuid : selections[0].get("uuid"),
								attachment_path:selections[0].get("attachment_path")
							};
							Ext.Ajax.request({
										url : "judicial/experimentAttachment/delete.do",
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
														"删除失败<br>请联系管理员!");
											}
										},
										failure : function() {
											Ext.Msg.alert("提示",
													"删除失败<br>请联系管理员!");
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