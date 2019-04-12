Ext.define('Rds.trace.form.TraceTypeFormPanel', {
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
                itemId:'type_name',
                xtype:"textfield",
                fieldLabel: '委托事项',
                labelWidth:70,
                fieldStyle:me.fieldStyle,
                name: 'type_name',
                allowBlank:false,
                maxLength:32
            },{
                itemId:'type_id',
                name:'type_id',
                xtype:"textfield",
                hidden:true
            },{
                itemId:'opertype',
                name:'opertype',
                xtype:"textfield",
                value:me.operType,
                hidden:true
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
                url: "trace/type/insertOrUpdateType.do",
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