Ext.define('Rds.judicial.form.JudicialExprimentAttachmentFormPanel', {
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
				labelWidth : 70
			},
			border : false,
			autoHeight : true,
			items : [ {
						xtype : 'filefield',
						name : 'files',
						fieldLabel : '文件',
						msgTarget : 'side',
						allowBlank : false,
						anchor : '100%',
						buttonText : '选择文件',
                        validator:function(value){
                            if(value.endWith('rar')){
                                return true;
                            }else{
                                return '请选择正确格式的压缩文件';
                            }
                        }
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
		me.callParent(arguments);
	},
	onSave : function() {
		var me = this;
		var myWindow = me.up('window');
		var form = me.up('form').getForm();
		if (!form.isValid()) {
			Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
			return;
		}
		form.submit({
					url : 'judicial/experimentAttachment/upload.do',
					method : 'post',
					waitMsg : '正在上传您的文件...',
					success : function(form, action) {
						response = Ext.JSON
								.decode(action.response.responseText);
						if (response.success) {
							Ext.MessageBox.alert("提示信息", response.msg);
							myWindow.close();
						}else{
							Ext.MessageBox.alert("提示信息", "上传发生错误，请联系管理员");
							myWindow.close();
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "上传失败，请联系管理员!");
						myWindow.close();
					}
				});

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}

});