Ext.define("Rds.trace.panel.TraceTypeGridPanel",{
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
            fields:['type_id','type_name','create_time'],
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                //url: 'trace/type/getCaseInfo.do',
                params:{
                },
                api:{
                    read:'trace/type/queryType.do'
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
                        { type_name:me.getDockedItems('toolbar')[0].
                                getComponent('type_name').getValue()});
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
            Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'10%', menuDisabled:true}),
            {text: '材料名称',dataIndex: 'type_name',width:'45%'},
            {text: 'id',dataIndex: 'type_id',hidden:true},
            {text: '创建时间',dataIndex: 'create_time',width:'45%'}
        ];

        me.dockedItems = [{
            xtype:'toolbar',
            name:'search',
            dock:'top',
            items:[{
                xtype:'textfield',
                name:'type_name',
                itemId:'type_name',
                labelWidth:70,
                width:220,
                fieldLabel:'委托事项'
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
            },{
                text:'删除',
                iconCls:'Delete',
                handler:me.onDelete
            }]
        }];

        me.callParent(arguments);
    },

    onDelete:function(){
        var me = this.up("gridpanel");
        var selections =  me.getView().getSelectionModel().getSelection();
        if(selections.length<1){
            Ext.Msg.alert("提示", "请选择需要删除的记录!");
            return;
        };
        var values = {
            type_id:selections[0].get("type_id")
        };
        console.log(values);
        Ext.MessageBox.confirm('提示','确定删除吗',function(id){
            if(id=='yes'){
                Ext.Ajax.request({
                    url:"trace/type/deleteType.do",
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

        var form = Ext.create("Rds.trace.form.TraceTypeFormPanel",{
            region:"center",
            operType:"add",
            grid:me
        });

        var win = Ext.create("Ext.window.Window",{
            title:'添加项目',
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
        var form = Ext.create("Rds.trace.form.TraceTypeFormPanel",{
            region:"center",
            operType:"update",
            grid:me
        });

        var win = Ext.create("Ext.window.Window",{
            title:'项目——修改',
            width:400,
            iconCls:'Pageedit',
            height:300,
            layout:'border',
            items:[form]
        });
        win.show();
        form.loadRecord(selections[0]);
    },
    onSearch:function(){
        var me = this.up("gridpanel");
        console.log(me.getDockedItems('toolbar')[0].
            getComponent('type_name').getValue());
        me.store.currentPage = 1;
        me.getStore().load();
    },

    listeners:{
        'afterrender':function(){
            this.store.load();
        }
    }
});
