Ext.define("Rds.judicial.panel.JudicialSampleHistoryGridPanel",{
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
            fields:['sample_code','name','value','value2'],
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                api:{
                    read:'judicial/sample/querySampleHistory.do'
                },
                params:{
                },
                reader: {
                    type: 'json',
                    root:'data',
                    totalProperty:'total'
                },
                autoLoad: true//自动加载
            },listeners:{
                'beforeload':function(ds, operation, opt){
                    Ext.apply(me.store.proxy.params,{
                            name:me.getDockedItems('toolbar')[0].getComponent('name').getValue(),
                            value:me.getDockedItems('toolbar')[0].getComponent('value').getValue()
                        });
                }
            }
        });

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
            {text: '样本编号',dataIndex: 'sample_code',width:'25%'},
            {text: '基因座',dataIndex: 'name',width:'25%'},
            {text: '基因座值1',dataIndex: 'value',width:'25%'},
            {text: '基因座值2',dataIndex: 'value2',width:'25%'}
        ];

        me.dockedItems = [{
            xtype:'toolbar',
            name:'search',
            dock:'top',
            items:[{
                xtype:'textfield',
                name:'name',
                itemId:'name',
                labelWidth:120,
                width:220,
                allowBlank:false,
                fieldLabel:'基因座'
            },{
                xtype:'textfield',
                name:'value',
                itemId:'value',
                labelWidth:120,
                width:220,
                allowBlank:false,
                fieldLabel:'位点值'
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
        console.log(me.getDockedItems('toolbar')[0].getComponent('name').getValue());
        me.store.currentPage = 1;
        if(me.getDockedItems('toolbar')[0].getComponent('name').getValue()==''
            ||me.getDockedItems('toolbar')[0].getComponent('value').getValue()==''){
            Ext.MessageBox.alert("提示信息",
                "请填写基因座名称和位点值");
        }else{
            me.getStore().load();
        }
    },

    listeners:{
        'afterrender':function(){
            this.store.load();
        }
    }
});
