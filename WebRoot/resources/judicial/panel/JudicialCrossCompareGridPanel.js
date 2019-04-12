Ext.define("Rds.judicial.panel.JudicialCrossCompareGridPanel", {
    extend: "Ext.grid.Panel",
    loadMask: true,
    viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize: 25,
    initComponent: function () {
        var me = this;
        me.store = Ext.create('Ext.data.Store', {
            storeId:'Rds.judicial.panel.JudicialCrossCompareGridStore',
            fields: ['case_code1', 'case_code2','sample_code1','sample_code2',
                'trans_date'],
            start:0,
			limit:15,
			pageSize:15,
            proxy: {
                type: 'jsonajax',
                actionMethods: {read: 'POST'},
                params: {
                },
                api: {
                    read: 'judicial/exception/queryCrossCompare.do'
                },
                reader: {
                    type: 'json',
                    root: 'data',
                    totalProperty: 'total'
                },
                autoLoad: true//自动加载
            },
            listeners: {
                'beforeload': function (ds, operation, opt) {
                    Ext.apply(me.store.proxy.params,{
                        starttime: dateformat(me.getDockedItems('toolbar')[0].
                            getComponent('starttime').getValue()),
                        endtime: dateformat(me.getDockedItems('toolbar')[0].
                            getComponent('endtime').getValue()),
                        case_code: trim(me.getDockedItems('toolbar')[0].
                            getComponent('case_code').getValue()),
                        sample_code: trim(me.getDockedItems('toolbar')[0].
                            getComponent('sample_code').getValue())
                    });
                }
            }
        });

        me.selModel = Ext.create('Ext.selection.CheckboxModel', {
            mode: 'SINGLE'
        });

        me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize: me.pageSize,
            displayInfo: true,
            displayMsg: "第 {0} - {1} 条  共 {2} 条",
            emptyMsg: "没有符合条件的记录"
        });
        me.columns = [
            {text: '涉及案例编号1', dataIndex: 'case_code1', width: '20%'},
            {text: '涉及案例编号2', dataIndex: 'case_code2', width: '20%'},
            {text: '样本编号1', dataIndex: 'sample_code1', width: '15%'},
            {text: '样本编号2', dataIndex: 'sample_code2', width: '15%'},
            {text: '异常发生日期', dataIndex: 'trans_date', width: '15%'}
        ];

        me.dockedItems = [
            {
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [
                    {
                        itemId: 'case_code',
                        xtype: 'textfield',
                        name: 'case_code',
                        labelWidth: 80,
                        width: 220,
                        fieldLabel: '案例编号'
                    },
                    {
                        xtype: 'textfield',
                        itemId: 'sample_code',
                        fieldLabel: '样本编号',
                        width: 220,
                        labelWidth: 80,
                        name: 'sample_code'
                    },
                    {
                        xtype: 'datefield',
                        name: 'starttime',
                        itemId: 'starttime',
                        width: 220,
                        fieldLabel: '开始时间',
                        labelWidth: 100,
                        labelAlign: 'right',
                        emptyText: '请选择日期',
                        format: 'Y-m-d',
                        maxValue: new Date(),
                        value: Ext.Date.add(new Date(), Ext.Date.DAY,-7),
                        listeners: {
                            'select': function () {
                                var start = me.up('gridpanel').getDockedItems('toolbar')[0].
                                    getComponent('starttime');
                                var end = me.up('gridpanel').getDockedItems('toolbar')[0].
                                    getComponent('endtime');
                                end.setMinValue(start.getValue);
                                var endDate = end.getValue();
                                if (start.getValue() > endDate) {
                                    end.setValue(start.value());
                                }
                            }
                        }
                    },
                    {
                        xtype: 'datefield',
                        itemId: 'endtime',
                        name: 'endtime',
                        width: 175,
                        labelWidth: 60,
                        fieldLabel: '结束时间',
                        labelAlign: 'right',
                        emptyText: '请选择日期',
                        format: 'Y-m-d',
                        maxValue: new Date(),
                        value: Ext.Date.add(new Date(), Ext.Date.DAY),
                        listeners: {
                            select: function () {
                                var start = me.up('gridpanel').getDockedItems('toolbar')[0].
                                    getComponent('starttime');
                                var end = me.up('gridpanel').getDockedItems('toolbar')[0].
                                    getComponent('endtime');
                                if (start.getValue() > end.getValue()) {
                                    start.setValue(end.getValue());
                                }
                            }
                        }
                    },
                    {
                        text: '查询',
                        iconCls: 'Find',
                        handler: me.onSearch
                    }
                ]
            }
//            {
//                xtype: 'toolbar',
//                dock: 'top',
//                items: [
//                    {
//                        text: '手动处理异常信息',
//                        iconCls: 'Pageedit',
//                        handler: me.onUpdate
//                    }
//                ]
//            }
        ];

        me.callParent(arguments);
    },
    onUpdate: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel()
            .getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要修改的记录!");
            return;
        }
        var values = {
            uuid:selections[0].get('uuid'),
            choose_flag:0
        }
        Ext.MessageBox.confirm('删除确认','是否标识该异常已被处理',function(id){
            if(id=='yes') {
                Ext.Ajax.request({
                    url:"judicial/exception/deleteException.do",
                    method: "POST",
                    headers: { 'Content-Type': 'application/json' },
                    jsonData: values,
                    success: function (response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response.result == true) {
                            Ext.MessageBox.alert("提示信息", response.message);
                        }else {
                            Ext.MessageBox.alert("错误信息", response.message);
                        }
                    },
                    failure: function () {
                        Ext.Msg.alert("提示", "删除失败<br>请联系管理员!");
                    }
                });
            }
            me.getStore().load();
        })

    },
    onSearch: function () {
        var me = this.up("gridpanel");
        me.store.currentPage = 1;
        me.getStore().load();
    },
    listeners: {
        'afterrender': function () {
            this.store.load();
        }
    }
});
