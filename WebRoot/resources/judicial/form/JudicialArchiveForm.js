/**
 * @description归档表单
 * @author fushaoming
 * @date 20150427
 */
Ext.define('Rds.judicial.form.JudicialArchiveForm', {
	extend : 'Ext.form.Panel',

	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;

		me.items = [{
			xtype : 'form',
			region : 'center',
			style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
			labelAlign : "right",
			bodyPadding : 10,
			defaults : {
				anchor : '100%',
				labelWidth : 90
			},
			border : false,
			autoHeight : true,
			items : [{
				xtype : "textfield",
				name : 'case_id',
				hidden:true
			},{
						xtype : "textfield",
						fieldLabel : '报告归档位置',
						allowBlank : false,
						name : 'archive_address',
						maxLength : 30
					},{
						xtype : "textfield",
						fieldLabel : '样本归档位置',
						allowBlank : false,
						name : 'sample_archive_address',
						maxLength : 30
					}, {
						xtype : "textfield",
						fieldLabel : '案例条形码',
						readOnly : true,
						regex : /^\w*$/,
						regexText : '请输入英文或数字',
						allowBlank : false,
						name : 'case_code',
						maxLength : 30
					}, {
						xtype : "datefield",
						fieldLabel : '归档日期',
						allowBlank : false,
						name : 'archive_date',
						value : new Date(),
						format : "y-m-d",
						maxValue : new Date()

					}]
		}];

		me.buttons = [{
					text : '归档',
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
		var myGrid = me.up("form");
		var form = me.up('form').getForm();
		if (!form.isValid()) {
			Ext.MessageBox.alert("提示信息", "请正确填写完整表单信息！字数有限制哦");
			return;
		}
		
		Ext.MessageBox.wait('正在操作','请稍后...');
		Ext.Ajax.request({
			url : "judicial/archive/save.do",
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : form.getValues(),
			success : function(response, options) {
				response = Ext.JSON.decode(response.responseText);
				if (response.success == true) {
					myWindow.close();
					Ext.MessageBox.alert("提示信息", response.message);
					myGrid.grid.getStore().load();
				} else {
					Ext.MessageBox.alert("错误信息", "保存失败咯..." + response.message);
				}
			},
			failure : function() {
				Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
			}

		});

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}

});