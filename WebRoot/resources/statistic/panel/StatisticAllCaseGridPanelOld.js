Ext.define("Rds.statistic.panel.StatisticAllCaseGridPanelOld",{
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
            fields:['accept_time',
        'case_code',
        'real_sum',
        'return_sum',
        'ptype',
        'rtype',
        'receiver',
        'user_dept_level1',
        'user_dept_level2',
        'user_dept_level3',
        'agent',
        'county',
        'city',
        'province',
        'partner',
        'client'],
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                params:{
                },
                api:{
                    read:'statistics/allCase/queryOld.do'
                },
                reader: {
                    type: 'json',
                    root:'data',
                    totalProperty:'total'
                },
                autoLoad: true//自动加载
            },listeners:{
                'beforeload':function(ds, operation, opt){
                    Ext.apply(me.store.proxy.params,
                        {
                            ptype:me.getDockedItems('toolbar')[0].
                            	getComponent('ptype').getValue(),
                            receiver:me.getDockedItems('toolbar')[0].
                            	getComponent('receiver').getValue(),
                            deptname:me.getDockedItems('toolbar')[0].
                            	getComponent('deptname').getValue(),
                            agent:me.getDockedItems('toolbar')[0].
                            	getComponent('agent').getValue(),
                            start_time:dateformat(me.getDockedItems('toolbar')[0].
                            		getComponent('start_time').getValue()),
                            end_time:dateformat(me.getDockedItems('toolbar')[0].
                            		getComponent('end_time').getValue()),
                            case_code:me.getDockedItems('toolbar')[0].
                                    getComponent('case_code').getValue(),
                            client:me.getDockedItems('toolbar')[0].
                                    getComponent('client').getValue()
                        });
                }
            }
        });


        me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
            emptyMsg : "没有符合条件的记录"
        });
        me.columns = [
            {text: '案例起始时间',dataIndex: 'accept_time'},
            {text: '编号',dataIndex: 'case_code'},
            {text: '委托人',dataIndex: 'client'},
            {text: '应收款项',dataIndex: 'real_sum'},
            {text: '实收款项',dataIndex: 'return_sum'},
            {text: '项目类别',dataIndex: 'ptype'},
            {text: '报告种类',dataIndex: 'rtype'},
            {text: '员工名字',dataIndex: 'receiver'},
            {text: '一级部门',dataIndex: 'user_dept_level1'},
            {text: '二级部门',dataIndex: 'user_dept_level2'},
            {text: '三级部门',dataIndex: 'user_dept_level3'},
            {text: '代理商名称',dataIndex: 'agent'},
            {text: '合作方',dataIndex: 'partner'},
            {text: '省',dataIndex: 'province'},
            {text: '地市',dataIndex: 'city'},
            {text: '区县',dataIndex: 'county'}
        ];

        me.dockedItems = [{
            xtype:'toolbar',
            name:'search',
            dock:'top',
            items:[{
                xtype : 'textfield',
                name : 'case_code',
                itemId : 'case_code',
                labelWidth : 60,
                width : 125,
                fieldLabel : '案例编号'
            },{
                xtype : 'textfield',
                name : 'client',
                itemId : 'client',
                labelWidth : 40,
                width : 125,
                fieldLabel : '委托人'
            },{
                xtype : 'textfield',
                name : 'ptype',
                itemId : 'ptype',
                labelWidth : 70,
                width : 155,
                fieldLabel : '项目类别'
            },{
                xtype : 'textfield',
                name : 'receiver',
                itemId : 'receiver',
                labelWidth : 60,
                width : 140,
                fieldLabel : '员工名字'
            },{
                xtype : 'textfield',
                name : 'deptname',
                itemId : 'deptname',
                labelWidth : 60,
                width : 140,
                fieldLabel : '所属部门'
            },{
                xtype : 'textfield',
                name : 'agent',
                itemId : 'agent',
                labelWidth : 40,
                width : 125,
                fieldLabel : '代理商'
            },{
                xtype : 'datefield',
                name : 'start_time',
                itemId : 'start_time',
                width : 225,
                fieldLabel : '案例起始时间 从',
                labelWidth : 100,
                labelAlign : 'right',
                emptyText : '请选择日期',
                format : 'Y-m-d',
                maxValue : new Date(),
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")
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
                    Ext.Date.DAY)
            },
            {
                text:'查询',
                iconCls:'Find',
                handler:me.onSearch
            }]
        },{
            xtype:'toolbar',
            dock:'top',
            items:[{
                text : '导出',
                iconCls : 'Pageexcel',
                handler : me.onExport
            }
            ]
        }];

        me.callParent(arguments);
    },

    onExport:function(){
            var me = this.up("gridpanel");
            window.location.href = "statistics/allCase/exportInfoOld.do?start_time="
                + dateformat(me.getDockedItems('toolbar')[0].getComponent('start_time').getValue())
                + "&end_time=" + dateformat(me.getDockedItems('toolbar')[0].getComponent('end_time').getValue())
                + "&ptype="+ me.getDockedItems('toolbar')[0].getComponent('ptype').getValue()
                + "&receiver=" + me.getDockedItems('toolbar')[0].getComponent('receiver').getValue()
                + "&deptname=" + me.getDockedItems('toolbar')[0].getComponent('deptname').getValue()
                + "&agent=" + me.getDockedItems('toolbar')[0].getComponent('agent').getValue()
                + "&case_code="+ me.getDockedItems('toolbar')[0].getComponent('case_code').getValue()
                + "&client="+ me.getDockedItems('toolbar')[0].getComponent('client').getValue();
    },
    onSearch : function() {
        var me = this.up("gridpanel");
        me.getStore().currentPage=1;
        me.getStore().load();
    },

    listeners:{
        'afterrender':function(){
            this.store.load();
        }
    }
});
