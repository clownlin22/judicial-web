Ext.define('Rds.judicial.panel.JudicialExperimentResultsGroupFormPanel', {
    extend : 'Ext.form.Panel',
    config:{
        operType:null
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
                itemId:'results_group',
                xtype:"textfield",
                fieldLabel: 'results_group',
                labelWidth:100,
                fieldStyle:me.fieldStyle,
                name: 'results_group',
                allowBlank:false,
                maxLength:32
            },{
                itemId:'opertype',
                name:'opertype',
                xtype:"textfield",
                value:me.operType,
                hidden:true
            },{
                xtype:"combobox",
                fieldLabel: '是否有效',
                store:Ext.create('Ext.data.Store', {
                    fields: ['key', 'value'],
                    data : [
                        {"key":"是","value":"Y"},
                        {"key":"否","value":"N"}
                    ]
                }),
                valueField:'value',
                displayField:'key',
                editable:false,
                name:'enable_flag',
                itemId:'enable_flag'
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
    },
    onSave:function(){
        var me = this.up("form");
        var form = me.getForm();
        var values = form.getValues();
        if(form.isValid()) {
            Ext.Ajax.request({
                url: "judicial/experiment/insertOrUpdateResultsGroup.do",
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