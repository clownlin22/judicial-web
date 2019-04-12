/**
 * @author lys
 * @description 样本记录上传表单
 * @date 20150407
 */
Ext.define('Rds.trace.form.TraceFileUploadFormPanel', {
	extend : 'Ext.form.Panel',
	config : {
		grid : null,
        year :null,
        case_no:null,
        case_id:null
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
                xtype : "textfield",
                fieldLabel : '年份',
                labelWidth : 100,
                name : 'year',
                value : me.year,
                readOnly:true
                },{
                xtype : "textfield",
                fieldLabel : '案例序号',
                labelWidth : 100,
                name : 'case_no',
                value : me.case_no,
                readOnly:true
            }, {
                xtype : "textfield",
                fieldLabel : '案例序号',
                labelWidth : 100,
                name : 'case_id',
                value : me.case_id,
                hidden:true
            }, {
                xtype : "textfield",
                fieldLabel : '附件类型',
                labelWidth : 100,
                name : 'attachment_type',
                value : 6,
                hidden:true
            },{
                xtype : 'filefield',
                name : 'mfiles',
                fieldLabel : '文件',
                labelWidth : 100,
                msgTarget : 'side',
                allowBlank : false,
                anchor : '100%',
                buttonText : '选择文件',
                validator:function(value){
                    if(value.endWith('doc')||value.endWith('docx')){
                        return true;
                    }else{
                        return '请选择正确格式的word文件';
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
            Ext.MessageBox.alert("提示信息", "请填写完整表单信息!");
            return;
        }else{
                form.submit({
                    url: 'trace/attachment/wordUpload.do',
                    method: 'post',
                    waitMsg: '正在上传您的文件...',
                    success: function (form, action) {
                        console.log(action);
                        var response = Ext.JSON
                            .decode(action.response.responseText);
                        if (response.result == true) {
                            Ext.MessageBox.alert("提示信息", response.message);
                            me.up('form').grid.getStore().load();
                            myWindow.close();
                        } else {
                            Ext.MessageBox.alert("错误信息", response.message);
                            myWindow.close();
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