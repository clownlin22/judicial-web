/**
 * @author lys
 * @description 样本记录上传表单
 * @date 20150407
 */
Ext.define('Rds.judicial.panel.JudicialDataUploadCheckFormPanel', {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'form',
			region : 'center',
			name : 'data',
			style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
			labelAlign : "right",
			bodyPadding : 10,
			defaults : {
				anchor : '100%',
				labelWidth : 100
			},
			border : false,
			autoHeight : true,
			items : [{
                xtype : 'filefield',
                name : 'mfiles',
                fieldLabel : '文件',
                labelWidth : 100,
                msgTarget : 'side',
                allowBlank : false,
                anchor : '100%',
                buttonText : '选择文件'
            }]
		}];

		me.buttons = [{
					text : '保存',
					iconCls : 'Disk',
					handler : me.onSave
				}, {
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
        //storeIdentifyPer.load();
		me.callParent(arguments);
	},
	onSave : function() {
        var me = this;
        var myWindow = me.up('window');
        var formObject = me.up('form');
        var form = me.up('form').getForm();
        if (!form.isValid()) {
            Ext.MessageBox.alert("提示信息", "请填写完整表单信息!");
            return;
        }else{
        	 form.submit({
                 url: 'judicial/sample/uploadCheck.do',
                 method: 'post',
                 waitMsg: '正在上传您的文件...',
                 success: function (form, action) {
                     var response = Ext.JSON
                         .decode(action.response.responseText);
                     if (response.result == true) {
                         Ext.MessageBox.alert("提示信息", response.message);
//                         myWindow.close();
                     } else {
                         Ext.MessageBox.alert("错误信息", response.message);
//                         myWindow.close();
                     }
                 },
                 failure: function () {
                     Ext.Msg.alert("提示", "上传失败<br>请联系管理员!");
                 }
             });
        }

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}

});