/**
 * 
 */
Ext.define("Rds.judicial.form.JudicialFinanceVerifyForm", {
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
						xtype : 'textfield',
						fieldLabel : '销售经理',
						readOnly : true,
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						name : 'username'

					}, {
						xtype : 'textfield',
						fieldLabel : '归属地区',
						readOnly : true,
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						name : 'areaname'

					}, {
						xtype : "textfield",
						fieldLabel : '标准金额',
						readOnly : true,
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						name : 'stand_sum'
					}, {
						xtype : "textfield",
						fieldLabel : '实收金额',
						regex : /^[\w\.]*$/,
						regexText : '请输入数字',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'real_sum'
					}, {
						xtype : "textfield",
						fieldLabel : '回款金额',
						labelWidth : 100,
						regex : /^[\w\.]*$/,
						regexText : '请输入数字',
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'return_sum'
					}, {
						xtype : 'textarea',
						fieldLabel : '备注',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						// allowBlank : false,
						name : 'remark'

					}, {
						xtype : "textfield",
						hidden : true,
						name : 'verify_id'
					}]
		}];

		me.buttons = [{
					text : '保存',
					iconCls : 'Disk',
					handler : me.onSave,
					listeners : {
						click : {
							fn : function(items, e, eOpts) {
								items.disable(true);
							}
						}
					}
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
			Ext.MessageBox.alert("提示信息", "请填写完整表单信息!");
			return;
		}
		var values = form.getValues();

		Ext.Ajax.request({
					url : "judicial/finance/updatestatus.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : values,
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.result == true) {
							Ext.MessageBox.alert("提示信息", response.message);
							me.grid.getStore().load();
							me.up("window").close();
						} else {
							Ext.MessageBox.alert("错误信息", response.message);
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