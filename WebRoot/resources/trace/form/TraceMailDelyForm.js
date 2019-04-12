Ext.define('Rds.trace.form.TraceMailDelyForm', {
    extend : 'Ext.form.Panel',
    config:{
        operType:null,
        test:null
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
            defaults: {
                anchor: '100%',
                labelWidth: 80
            },
            autoHeight:true,
            items: [{
                xtype:"textarea",
    			fieldLabel: '延期原因',
    			labelWidth:70,
    			fieldStyle:me.fieldStyle, 
    			allowBlank:false,
    			emptyText:'请填写详细延期原因',
    			name: 'dely_reson',
    			height:80,
    			maxLength: 200,
            },{
                name:'case_id',
                xtype:"textfield",
                hidden:true
            }]
        }];

        me.buttons = [{
            text:'保存',
            iconCls:'Disk',
            handler:me.onSave,
            hidden:(roletype=='1000')?true:false
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
                url: "trace/register/traceMailDely.do",
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                jsonData: values,
                success: function (response, options) {
                    console.log(response.responseText);
                    response = Ext.JSON.decode(response.responseText);
                    if (response == true) {
                        Ext.MessageBox.alert("提示信息", "插入延期原因成功！");
                        me.grid.getStore().load();
                        me.up("window").close();
                    } else {
                        Ext.MessageBox.alert("错误信息", "插入延期原因失败，请联系管理员！");
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