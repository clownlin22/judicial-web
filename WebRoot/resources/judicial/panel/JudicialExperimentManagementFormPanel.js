var storeReagent=Ext.create(
    'Ext.data.Store',
    {
        fields: ['reagent_name'],
        proxy:{
            type: 'jsonajax',
            actionMethods:{read:'POST'},
            url:'judicial/reagents/queryReagents.do',
            reader:{
                type:'json',
                root:'data'
            },
            params:{enable_flag:'Y'}
        },
        remoteSort:true
    }
);

Ext.define('Rds.judicial.panel.JudicialExperimentManagementFormPanel', {
    extend : 'Ext.form.Panel',
    config:{
        operType:null,
        uuid:null,
        grid:null
    },
    initComponent : function() {
        var me = this;

        me.items = [{
            xtype:'form',
            region:'center',
            name:'data',
            style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
            labelAlign:"right",
            bodyPadding: 10,
            border:false,
            autoHeight:true,
            items: [{
                id:'experiment_no',
                xtype:"textfield",
                fieldLabel: '实验编号',
                labelWidth:100,
                fieldStyle:me.fieldStyle,
                name: 'experiment_no',
                allowBlank:false,
                maxLength:32
            },{
//                xtype:"textfield",
//                fieldLabel: '实验执行人',
//                labelWidth:100,
//                fieldStyle:me.fieldStyle,
//                name: 'experimenter',
//                allowBlank:false,
//                maxLength:16
//            },{
                xtype:"datefield",
                fieldLabel: '实验日期',
                format:'Y-m-d',
                labelWidth:100,
                fieldStyle:me.fieldStyle,
                maxValue:new Date(),
                name: 'experiment_date',
                value:Ext.Date.add(new Date(), Ext.Date.DAY),
                allowBlank:false
            },{
                xtype:"textfield",
                name:'operType',
                value:me.operType,
                hidden:true
            },{
                xtype:"textfield",
                name:'uuid',
                hidden:true,
                value:me.uuid
            }]
        }];

        me.buttons = [{
            text:'保存',
            iconCls:'Disk',
            handler:me.onSave
        },{
            text:'取消',
            iconCls:'Cancel',
            handler:me.onCancel
        }]
        me.callParent(arguments);
        storeReagent.load();
    },
    onSave:function(){
        var me = this.up("form");
        var form = me.getForm();
        var values = form.getValues();
        if(form.isValid()) {
            Ext.Ajax.request({
                url: "judicial/experiment/saveExperiment.do",
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                jsonData: values,
                success: function (response, options) {
                    console.log(response.responseText);
                    response = Ext.JSON.decode(response.responseText);
                    if (response.result == true) {
                        Ext.MessageBox.alert("提示信息", response.message);
                        me.grid.getStore().load();
                        me.up("window").close();
                    } else {
                        Ext.MessageBox.alert("错误信息", response.message);
                    }
                },
                failure: function () {
                    Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                }

            });
        }

    },
    onCancel:function(){
        var me = this;
        me.up("window").close();
    }

});