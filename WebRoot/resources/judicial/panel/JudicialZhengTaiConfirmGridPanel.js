
Ext.define("Rds.judicial.panel.JudicialZhengTaiConfirmGridPanel", {
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
            storeId:'Rds.judicial.panel.JudicialMissingExperimentGridStore',
            fields: ['case_code','sub_case_code',
                'result','username1','username2','username3','unmatched_count',
                'last_compare_date','experiment_count'],
            proxy: {
                type: 'jsonajax',
                actionMethods: {read: 'POST'},
                params: {
                },
                api: {
                    read: 'judicial/subCase/queryAllCountForZhengTaiExt.do'
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
                        case_code: trim(me.getDockedItems('toolbar')[0].
                            getComponent('case_code').getValue()),
                        sub_case_code: trim(me.getDockedItems('toolbar')[0].
                            getComponent('sub_case_code').getValue()),
                        negandext_flag:'Y',
                        starttime: dateformat(me.getDockedItems('toolbar')[0].
                            getComponent('starttime').getValue()),
                        endtime: dateformat(me.getDockedItems('toolbar')[0].
                            getComponent('endtime').getValue())
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
            {text: '案例编号', dataIndex: 'case_code', width: '12%'},
            {text: '子案例编号', dataIndex: 'sub_case_code', width: '12%'},
            {text: '父母名称一', dataIndex: 'username1', width: '12%'},
            {text: '父母名称二', dataIndex: 'username2', width: '12%'},
            {text: '子女名称', dataIndex: 'username3', width: '12%'},
            {text: '比对日期',dataIndex:'last_compare_date',width: '10%'},
            {text: '不匹配基因个数',dataIndex:'unmatched_count',width: '10%'},
            {text: '实验状态',dataIndex:'unmatched_count',width: '10%',renderer:
                function(value){
                    if(value==0)
                        return '肯定';
                    if(value==1||value==2)
                        return '突变';
                    if(value>2)
                        return '否定';
                }
            }
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
                    },{
                        itemId: 'sub_case_code',
                        xtype: 'textfield',
                        name: 'sub_case_code',
                        labelWidth: 80,
                        width: 220,
                        fieldLabel: '子案例编号'
                    },
                    {
                        xtype:'datefield',
                        name:'starttime',
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
                                var start = me.getDockedItems('toolbar')[0].
                                    getComponent('starttime').getValue();
                                me.getDockedItems('toolbar')[0].
                                    getComponent('endtime').setMinValue(start);

                                var endDate = me.getDockedItems('toolbar')[0].
                                    getComponent('endtime').getValue();
                                if (start > endDate) {
                                    me.getDockedItems('toolbar')[0].
                                        getComponent('endtime').setValue(start);
                                }
                            }
                        }
                    }, {
                        xtype: 'datefield',
                        itemId: 'endtime',
                        name:'endtime',
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
                                var start = me.getDockedItems('toolbar')[0].
                                    getComponent('starttime').getValue();
                                var endDate = me.getDockedItems('toolbar')[0].
                                    getComponent('endtime').getValue();
                                if (start > endDate) {
                                    me.getDockedItems('toolbar')[0].
                                        getComponent('starttime').setValue('ExpEndDate');
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
            },{
                xtype: 'toolbar',
                dock: 'top',
                items: [
                    {
                        text: '标记案例为肯定',
                        iconCls: 'Pageedit',
                        handler: me.onUpdate
                    }
                ]
            }
        ];
        me.callParent(arguments);
    },

    onSearch: function () {
        var me = this.up("gridpanel");
        me.store.currentPage = 1;
        me.getStore().load();
    },
    onUpdate: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel()
            .getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要确认的案例!");
            return;
        }
        var values = {
            sub_case_code:selections[0].get('sub_case_code'),
            result:'passed'
        }
        Ext.MessageBox.confirm('案例确认','确认',function(id){
            if(id=='yes') {
                Ext.Ajax.request({
                    url:"judicial/subCase/updateResult.do",
                    method: "POST",
                    headers: { 'Content-Type': 'application/json' },
                    jsonData: values,
                    success: function (response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response.result == true) {
                            me.getStore().load();
                            Ext.MessageBox.alert("提示信息", response.message);
                        }else {
                            me.getStore().load();
                            Ext.MessageBox.alert("错误信息", response.message);
                        }
                    },
                    failure: function () {
                        me.getStore().load();
                        Ext.Msg.alert("提示", "确认案例失败<br>请联系管理员!");
                    }
                });
            }
        })
    },
    listeners: {
        'afterrender': function () {
            this.store.load();
        }
    }
});
