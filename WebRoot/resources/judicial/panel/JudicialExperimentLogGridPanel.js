var experimentLog="";
Ext.define("Rds.judicial.panel.JudicialExperimentLogGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var start_time = new Ext.form.DateField({
			name : 'start_time',
			width : 175,
			fieldLabel : '上传日期 从',
			labelWidth : 70,
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var end_time = new Ext.form.DateField({
			name : 'end_time',
			width : 135,
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var down_start_time = new Ext.form.DateField({
			id:'down_start_time',
			name : 'down_start_time',
			width : 175,
			fieldLabel : '下载日期从',
			labelWidth : 70,
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('down_start_time').getValue();
	                var endDate = Ext.getCmp('down_end_time').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('down_start_time').setValue(endDate);
	                }
				}
			}
		});
		var down_end_time = new Ext.form.DateField({
			id:'down_end_time',
			name : 'down_end_time',
			width : 135,
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('down_start_time').getValue();
	                var endDate = Ext.getCmp('down_end_time').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('down_start_time').setValue(endDate);
	                }
				}
			}
		});
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
		var sample_code = Ext.create('Ext.form.field.Text', {
			name : 'sample_code',
			labelWidth : 60,
			width : 200,
			fieldLabel : '案例编号'
		});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['uuid', 'upload_username','sample_code',
							'attachment_date','download_time','download_username'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'judicial/experimentAttachment/queryExperimentLog.do',
						params : {},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'total'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							experimentLog= "start_time=" + dateformat(start_time.getValue())
										+ "&end_time=" + dateformat(end_time.getValue())
										+ "&down_start_time=" + dateformat(down_start_time.getValue())
										+ "&down_end_time=" + dateformat(down_end_time.getValue())
										+ "&upload_username=" + trim(upload_username.getValue())
										+ "&download_username=" + trim(download_username.getValue())
										+"&sample_code = "+ trim(sample_code.getValue());
							Ext.apply(me.store.proxy.params, {
                                start_time:dateformat(start_time.getValue()),
                                end_time:dateformat(end_time.getValue()),
                                down_start_time:dateformat(down_start_time.getValue()),
                                down_end_time:dateformat(down_end_time.getValue()),
                                upload_username:trim(upload_username.getValue()),
                                download_username:trim(download_username.getValue()),
                                sample_code:trim(sample_code.getValue())
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
					text : '案例样本编号',
					dataIndex : 'sample_code',
					width : 120,
					menuDisabled : true
				},{
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
					text : '最后下载日期',
					dataIndex : 'download_time',
					width : 150,
					menuDisabled : true
				},
				{
					text : '下载人员',
					dataIndex : 'download_username',
					width : 100,
					menuDisabled : true
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
                    }, upload_username,download_username]
            }, {
    	 		style : {
    		        borderTopWidth : '0px !important',
    		        borderBottomWidth : '1px !important'
    		 		},
    		 		xtype:'toolbar',
    		 		name:'search',
    		 		dock:'top',
    		 		items:[down_start_time,down_end_time,sample_code,{
    	            	text:'查询',
    	            	iconCls:'Find',
    	            	handler:me.onSearch
    	            }]
    		 	},{
                xtype : 'toolbar',
                dock : 'top',
                items : [{
                    text : '导出',
                    iconCls : 'Application',
                    handler : me.onExport
                }]
            }];
		me.callParent(arguments);
	},
	onExport : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		window.location.href = "judicial/experimentAttachment/exortExperimentLog.do?experimentLog=" +experimentLog;

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