Ext.define("Rds.judicial.panel.JudicialMissingDataGridPanel",{
    extend:"Ext.grid.Panel",
    loadMask: true,
    viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    initComponent : function() {
        var me = this;
        me.store = Ext.create('Ext.data.Store',{
            storeId:'Rds.judicial.panel.JudicialMissingDataGridStore',
            fields:['sample_code','case_code','sample_in_time','sample_in_experiment'],
            start:0,
			limit:15,
			pageSize:15,
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                //url: 'judicial/experiment/getCaseInfo.do',
                params:{
                },
                api:{
                    read:'judicial/sample/queryMissingData.do'
                },
                timeout:1000000,
                reader: {
                    type: 'json',
                    root:'data',
                    totalProperty:'total'
                },
                autoLoad: true//自动加载
            },listeners: {
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

        me.viewConfig = {
            getRowClass: function(record, rowIndex, rowParams, store){
                var ztlx = record.get("sample_in_time");
                var today = new Date();
                var sd = new Date(ztlx);
                var ecart = (today-sd)/(1000*3600*24);
                var cls = "white-row";

                if(ecart>3){
                    cls = "red-row";
                }
                return cls;
            }
        };

        me.selModel = Ext.create('Ext.selection.CheckboxModel',{
            mode: 'SINGLE'
        });

        me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
            emptyMsg : "没有符合条件的记录"
        });
        me.columns = [
            {text: '案例编号',dataIndex: 'case_code',width:'25%'},
            {text: '样本编号',dataIndex: 'sample_code',width:'25%'},
            {text: '接收样本日期',dataIndex: 'sample_in_time',width:'30%'},
            {text: '实验次数',dataIndex: 'sample_in_experiment',width:'20%'}
        ];

        me.dockedItems = [{
            xtype:'toolbar',
            name:'search',
            dock:'top',
            items:[{
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
                text:'查询',
                iconCls:'Find',
                handler:me.onSearch
            }]
        }];
        me.callParent(arguments);
    },
    onSearch:function(){
        var me = this.up("gridpanel");
        me.store.currentPage = 1;
        me.getStore().load();
    },
    listeners:{
        'afterrender':function(){
            this.store.load();
        }
    }
});
