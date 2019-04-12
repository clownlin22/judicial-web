Ext.define("Rds.judicial.panel.JudicialReagentsGridPanel",{
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
            fields:['reagent_name','enable_flag'],
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                //url: 'judicial/reagents/getCaseInfo.do',
                params:{
                },
                api:{
                    read:'judicial/reagents/queryReagents.do'
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
                        {reagent_name:me.getDockedItems('toolbar')[0].
                            getComponent('reagent_name').getValue(),
                            enable_flag:me.getDockedItems('toolbar')[0].
                                getComponent('enable_flag').getValue()});
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
            {text: '试剂名称',dataIndex: 'reagent_name',width:'30%'},
            {text: '是否有效',dataIndex: 'enable_flag',width:'30%',renderer:function(value){
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
            }}
        ];

        me.dockedItems = [{
            xtype:'toolbar',
            name:'search',
            dock:'top',
            items:[{
                xtype:'textfield',
                name:'reagent_name',
                itemId:'reagent_name',
                labelWidth:120,
                width:220,
                fieldLabel:'试剂名称'
            },{
                xtype:"combobox",
                fieldLabel: '是否有效',
                editable:false,
                store:Ext.create('Ext.data.Store', {
                    fields: ['key', 'value'],
                    data : [
                        {"key":"全部","value":""},
                        {"key":"是","value":"Y"},
                        {"key":"否","value":"N"}
                    ]
                }),
                valueField:'value',
                displayField:'key',
                name:'enable_flag',
                itemId:'enable_flag'
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
                text:'新增',
                iconCls:'Pageadd',
                handler:me.onInsert
            },{
                text:'修改',
                iconCls:'Pageedit',
                handler:me.onUpdate
//            },{
//                text:'删除',
//                iconCls:'Delete',
//                handler:me.onDelete
            }]
        }];

        me.callParent(arguments);
    },

    onDelete:function(){
        var me = this.up("gridpanel");
        var selections =  me.getView().getSelectionModel().getSelection();
        console.log(selections[0].get("reagent_name"));
        if(selections.length<1){
            Ext.Msg.alert("提示", "请选择需要删除的记录!");
            return;
        };
        var values = {
            reagent_name:selections[0].get("reagent_name")
        };
        console.log(values);
        Ext.MessageBox.confirm('提示','确定删除吗',function(id){
            if(id=='yes'){
                Ext.Ajax.request({
                    url:"judicial/reagents/deleteReagents.do",
                    method: "POST",
                    headers: { 'Content-Type': 'application/json' },
                    jsonData: values,
                    success: function (response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response.result == true) {
                            Ext.MessageBox.alert("提示信息", "删除成功！");
                            me.getStore().load();
                        }else {
                            Ext.MessageBox.alert("错误信息", "删除失败！");
                        }
                    },
                    failure: function () {
                        Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                    }
                });
            }
        });
    },
    onInsert:function(){
        var me = this.up("gridpanel");

        var form = Ext.create("Rds.judicial.panel.JudicialReagentsFormPanel",{
            region:"center",
            operType:'add',
            grid:me
        });

        var win = Ext.create("Ext.window.Window",{
            title:'添加试剂',
            width:400,
            iconCls:'Pageadd',
            height:300,
            layout:'border',
            items:[form]
        });
        win.show();
    },
    onUpdate:function(){
        var me = this.up("gridpanel");
        var selections =  me.getView().getSelectionModel().getSelection();
        if(selections.length<1){
            Ext.Msg.alert("提示", "请选择需要修改的记录!");
            return;
        };
        var form = Ext.create("Rds.judicial.panel.JudicialReagentsFormPanel",{
            region:"center",
            operType:"update",
            grid:me
        });

        var win = Ext.create("Ext.window.Window",{
            title:'试剂——修改',
            width:400,
            iconCls:'Pageedit',
            height:300,
            layout:'border',
            items:[form]
        });
        win.show();
        form.loadRecord(selections[0]);
        form.items.get(0).getComponent('reagent_name').setReadOnly(true);
    },
    onSearch:function(){
        var me = this.up("gridpanel");
        console.log(me.getDockedItems('toolbar')[0].
            getComponent('reagent_name').getValue());
        me.store.currentPage = 1;
        me.getStore().load();
    },

    listeners:{
        'afterrender':function(){
            this.store.load();
        }
    }
});