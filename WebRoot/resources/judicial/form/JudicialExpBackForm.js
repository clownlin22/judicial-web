/**
 * @author XiangKang on 2017/1/19.
 */
Ext.define('Rds.judicial.form.JudicialExpBackForm', {
    extend: 'Ext.form.Panel',
    bodyPadding: 10,
    layout: 'fit',
    initComponent: function () {
        var me = this;
        me.items = [
            {
                emptyText: '退回原因',
                name: 'back_remark',
                allowBlank: false,
                xtype: 'textarea'
            }
        ];
        me.buttons = [
            {
                text: '保存',
                iconCls: 'Disk',
                handler: me.onSave
            },
            {
                text: '取消',
                iconCls: 'Cancel',
                handler: me.onCancel
            }
        ];
        me.callParent(arguments);
    },
    onSave: function () {
        var me = this.up("form");
        var form = me.getForm();
        if (!form.isValid()) {
            Ext.MessageBox.alert("提示信息", "请正确填写完整表单信息!");
            return;
        }
        var values = form.getValues();
        values.task_id = me.taskId;
        values.process_instance_id = me.processInstanceId;
        values.case_id = me.caseId;

        Ext.Ajax.request({
            url: "judicial/verify/expBack.do",
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            jsonData: values,
            success: function (response, options) {
                response = Ext.JSON.decode(response.responseText);
                if (response.result == true) {
                    Ext.MessageBox.alert("提示信息", response.message);
                    // me.getStore().load();
                    // me.up("window").close();
                } else {
                    Ext.MessageBox.alert("错误信息", response.message);
                    // me.getStore().load();
                }
            },
            failure: function () {
                Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
            }
        });
    },
    onCancel: function () {
        var me = this;
        me.up("window").close();
    }
});