Ext.define("Rds.judicial.form.JudicialRemittanceAccountForm", {
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
						xtype : "textfield",
						fieldLabel : '账户名称',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'accountName',
						maxLength : 20
					}, {
						xtype : "textfield",
						fieldLabel : '汇款账户',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'remittanceAccount',
						maxLength : 50
					},  {
						xtype : 'textarea',
						fieldLabel : '备注',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						name : 'remark',
						maxLength : 100

					}, {
						xtype : "textfield",
						hidden : true,
						name : 'id'
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
		var me = this.up("form");
		var form = me.getForm();
		if (!form.isValid()) {
			Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
			return;
		}
		var values = form.getValues();
		Ext.Ajax.request({
			url : "judicial/remittance/queryAccount.do",
			method : "POST",
			async:false,
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : values,
			success : function(response, options) {
				response = Ext.JSON.decode(response.responseText);
				if(response){
					Ext.Msg.alert("提示", "已存在汇款账户或账户名称，请查看!");
				}else{
					me.up("window").close();
					Ext.Ajax.request({
						url : "judicial/remittance/save.do",
						method : "POST",
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : values,
						async:false,
						success : function(response, options) {
							response = Ext.JSON.decode(response.responseText);
							console.log(response);
							if (response.result == true) {
								Ext.MessageBox.alert("提示信息", "新增保存成功");
								me.grid.getStore().load();
							} else {
								Ext.MessageBox.alert("错误信息", "保存失败<br>请联系管理员!");
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
						}

					});
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