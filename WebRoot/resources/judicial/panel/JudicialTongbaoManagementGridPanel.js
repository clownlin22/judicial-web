Ext.define("Rds.judicial.panel.JudicialTongbaoManagementGridPanel", {
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
            fields: ['experiment_no', 'sample_code','ext_flag', 'enable_flag', 'experimenter', 'experiment_date',
                'sample_username','reagent_name'],
            proxy: {
                type: 'jsonajax',
                actionMethods: {read: 'POST'},
                params: {
                },
                api: {
                    read: 'judicial/sample/queryAllRecord.do'
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
                    console.log(123);
                    console.log(me);
                    console.log(me.getDockedItems('toolbar')[0].
                        getComponent('endtime').getValue());
                    Ext.apply(me.store.proxy.params,{
                        starttime: dateformat(me.getDockedItems('toolbar')[0].
                            getComponent('starttime').getValue()),
                        endtime: dateformat(me.getDockedItems('toolbar')[0].
                            getComponent('endtime').getValue()),
                        experiment_no: trim(me.getDockedItems('toolbar')[0].
                            getComponent('experiment_no').getValue()),
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
            {text: '实验编号', dataIndex: 'experiment_no', width: '18%'},
            {text: '样本编号', dataIndex: 'sample_code', width: '17%'},
            {text: '用户名', dataIndex: 'sample_username', width: '17%'},
            {text: '是否为加位点样本',dataIndex:'ext_flag',width:'12%',renderer:function(value){
                switch (value){
                    case 'Y':
                        return '是';
                        break;
                    case 'N':
                        return '否';
                        break;
                    default :
                        return "否";
                }
            }},
            {text: '是否有效数据', dataIndex: 'enable_flag', width: '12%',renderer:function(value){
                switch(value)
                {
                    case 'Y':
                        return "是";
                        break;
                    case 'N':
                        return "否";
                        break;
                    default :
                        return "否";
                }
            }},
            {text: '试剂名称', dataIndex: 'reagent_name', width: '10%'},
            {text: '实验执行人', dataIndex: 'experimenter', width: '12%'},
            {text: '实验日期', dataIndex: 'experiment_date', width: '12%'}
        ];

        me.dockedItems = [
            {
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [
                    {
                        itemId: 'experiment_no',
                        xtype: 'textfield',
                        name: 'experiment_no',
                        labelWidth: 80,
                        width: 220,
                        fieldLabel: '实验编号'
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
            },
            {
                xtype: 'toolbar',
                dock: 'top',
                items: [
                    {
                        text: '导入实验结果',
                        iconCls: 'Add',
                        handler: me.onInsert
                    },
                    {
                        text: '标记样本是否有效',
                        iconCls: 'Pageedit',
                        handler: me.onUpdate
                    },
                    {
                        text: '查询样本结果',
                        iconCls: 'Find',
                        handler: me.onQuery
                    }
                ]
            }
        ];

        me.callParent(arguments);
    },
    onQuery: function () {
        var me = this.up("gridpanel");
        //console.log(me.getDockedItems('toolbar')[0].getComponent('experiment_no').getValue());
        var selections = me.getView().getSelectionModel()
            .getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要查看的记录!");
            return;
        }
        var form = Ext.create(
            "Rds.judicial.panel.JudicialOneSampleResultPanel", {
                region: "center",
                grid: me,
                sample_code:selections[0].get('sample_code'),
                experiment_no:selections[0].get('experiment_no')
            });
        var win = Ext.create("Ext.window.Window", {
            title: '样本DNA详细信息',
            width: 350,
            iconCls: 'Pageedit',
            height: 500,
            modal: true,
            layout: 'fit',
            items: [form]
        });
        win.show();
        form.loadRecord(selections[0]);
    },
    onInsert: function () {
        var me = this.up("gridpanel");
        var form = Ext.create(
            "Rds.judicial.panel.JudicialTongbaoDataUploadFormPanel", {
                region: "center",
                grid: me
            });
        var win = Ext.create("Ext.window.Window", {
            title: '上传新文件',
            width: 450,
            iconCls: 'Add',
            height: 300,
            layout: 'border',
            modal: true,
            items: [form]
        });
        win.show();
    },
    onUpdate: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel()
            .getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要修改的记录!");
            return;
        }

        var form = Ext.create(
            "Rds.judicial.panel.JudicialSampleManagementFormPanel", {
                region: "center",
                grid: me
            });
        var win = Ext.create("Ext.window.Window", {
            title: '样本记录——修改',
            width: 400,
            iconCls: 'Pageedit',
            height: 350,
            modal: true,
            layout: 'border',
            items: [form]
        });
        win.show();
        form.loadRecord(selections[0]);
        form.down([filefield]).value = null;
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
