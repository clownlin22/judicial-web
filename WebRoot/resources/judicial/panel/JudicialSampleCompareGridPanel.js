Ext.define("Rds.judicial.panel.JudicialSampleCompareGridPanel", {
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
            storeId:'Rds.judicial.panel.JudicialSampleCompareGridStore',
            fields: ['case_code','sub_case_code',
                'result','username1','username2','username3','unmatched_count',
                'last_compare_date','experiment_count','need_ext'],
            start:0,
			limit:15,
			pageSize:15,
            proxy: {
                type: 'jsonajax',
                actionMethods: {read: 'POST'},
                params: {
                },
                api: {
                    read: 'judicial/subCase/queryAll.do'
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
                            getComponent('sub_case_code').getValue())
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
            {text: '案例编号', dataIndex: 'case_code', width: '10%'},
            {text: '子案例编号', dataIndex: 'sub_case_code', width: '10%'},
            {text: '父母名称一', dataIndex: 'username1', width: '10%'},
            {text: '父母名称二', dataIndex: 'username2', width: '10%'},
            {text: '子女名称', dataIndex: 'username3', width: '10%'},
            {text: '比对最终结果', dataIndex: 'result', width: '10%',renderer:
                function(value){
                    if(value=='failed')
                        return '匹配不成功';
                    if(value=='passed')
                        return '匹配成功';
                }
            },
            {text: '比对日期',dataIndex:'last_compare_date',width: '12%'},
            {text: '不匹配基因个数',dataIndex:'unmatched_count',width: '10%'},
            {text: '实验状态',dataIndex:'unmatched_count',width: '12%',renderer:
                function(value){
                    if(value==0)
                        return '肯定';
                    if(value==1||value==2)
                        return '突变';
                    if(value>2)
                        return '否定';
                }
            }
            //{text: '完成实验次数',dataIndex:'experiment_count',width: '10%'}
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
                        text: '普通样本手动比对',
                        iconCls: 'Find',
                        handler: me.basecompare
                    },
                    {
                        text: '加位点样本手动比对',
                        iconCls: 'Find',
                        handler: me.extcompare
                    }
                ]
            }
        ];

        me.callParent(arguments);
    },
    basecompare: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel()
            .getSelection();


        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要比对的记录!");
            return;
        }
        if (selections[0].get("result")) {
            Ext.Msg.alert("提示", "该案例已比对完毕");
            return;
        }
        if(selections[0].get("unmatched_count")==1 || selections[0].get("unmatched_count")==2){
            Ext.Msg.alert("提示", "请选择加位点比对");
            return;
        }
        var values = {
            ext_flag:"N",
            sub_case_code:selections[0].get("sub_case_code")
        };
        Ext.MessageBox.confirm('比对确认','是否确定进行不带加位点比对',function(id){
            if(id=='yes') {
                Ext.Ajax.request({
                    url:"judicial/sample/compare.do",
                    method: "POST",
                    headers: { 'Content-Type': 'application/json' },
                    jsonData: values,
                    success: function (response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response.result == true) {
                            Ext.MessageBox.alert("提示信息", response.message);
                            me.getStore().load();
                        }else {
                            Ext.MessageBox.alert("错误信息", response.message);
                            me.getStore().load();
                        }
                    },
                    failure: function () {
                        Ext.Msg.alert("提示", "比对失败<br>请联系管理员!");
                        me.getStore().load();
                    }
                });
            }
        })

    },
    extcompare: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel()
            .getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要比对的记录!");
            return;
        }
        if(selections[0].get("unmatched_count")==null){
            Ext.Msg.alert("提示", "缺少样本数据");
            return;
        }
        if(selections[0].get("unmatched_count")!=1
            && selections[0].get("unmatched_count")!=2
            && selections[0].get("need_ext")!='Y'){
            Ext.Msg.alert("提示", "请选择普通样本比对");
            return;
        }
        if (selections[0].get("result")) {
            Ext.Msg.alert("提示", "该案例已比对完毕");
            return;
        }
        var values = {
            ext_flag:"Y",
            sub_case_code:selections[0].get("sub_case_code")
        };
        Ext.MessageBox.confirm('比对确认','是否确定进行加位点比对',function(id){
            if(id=='yes') {
                Ext.Ajax.request({
                    url:"judicial/sample/compare.do",
                    method: "POST",
                    headers: { 'Content-Type': 'application/json' },
                    jsonData: values,
                    success: function (response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response.result == true) {
                            Ext.MessageBox.alert("提示信息", response.message);
                            me.getStore().load();
                        }else {
                            Ext.MessageBox.alert("错误信息", response.message);
                            me.getStore().load();
                        }
                    },
                    failure: function () {
                        Ext.Msg.alert("提示", "比对失败<br>请联系管理员!");
                        me.getStore().load();
                    }
                });
            }
        })

    },
    onSearch: function () {
        var me = this.up("gridpanel");
        me.store.currentPage = 1;
        me.getStore().load();
    },
    listeners: {
        'afterrender': function () {
//            this.store.load();
        }
    }
});