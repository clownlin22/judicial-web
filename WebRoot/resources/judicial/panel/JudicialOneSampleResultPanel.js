/**
 * Created by lys on 2015/4/8.
 */
Ext.define('Rds.judicial.panel.JudicialOneSampleResultPanel', {
    extend:'Ext.form.Panel',
    bodyPadding: 5,
    width: 800,
    config:{
        sample_code:null,
        experiment_no:null
    },
    // Fields will be arranged vertically, stretched to full width
    layout: {type:'table',columns:1},
    // The fields
    defaults:{xtype:'textfield'},
    autoScroll:true,
    initComponent:function(){
        var me = this;
        me.dockedItems = [{
            xtype:'toolbar',
            name:'search',
            dock:'top',
            items:[{
                xtype:'textfield',
                name:'experiment_no',
                labelWidth:80,
                width:220,
                fieldLabel:'实验编号',
                readOnly:true
            }]
        },{
            xtype:'toolbar',
            name:'search',
            dock:'top',
            items:[{
                xtype:'textfield',
                fieldLabel:'样本编号',
                width: 220,
                labelWidth: 80,
                name:'sample_code',
                readOnly:true
            }]
        }]

        me.buttons = [{
            text:'取消',
            iconCls:'Cancel',
            handler:me.onCancel
        }]
        me.callParent(arguments);
    },
    onCancel:function(){
        var me = this;
        me.up("window").close();
    },
    listeners : {
        'afterrender' : function() {
            var me = this;
            console.log(me);
            Ext.Ajax.request({
                url : 'judicial/sample/queryOneRecord.do',
                method : "POST",
                headers : {
                    'Content-Type' : 'application/json'
                },
                jsonData : {
                    'experiment_no':me.experiment_no,
                    'sample_code':me.sample_code
                },
                success : function(response, options) {
                    console.log(response);
                    response = Ext.JSON.decode(response.responseText);
                    var data = response["record"];
                    console.log(data);
                    for (var key in data) {
                        me.add({
                            xtype : 'textfield',
                            fieldLabel:key,
                            value: data[key],
                            readOnly:true
                        });
                    }
                },
                failure : function() {
                    return;
                }
            });
        }
    }
});