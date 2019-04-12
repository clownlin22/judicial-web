/**
 * 
 */
Ext.define("Rds.judicial.form.JudicialFinanceAddForm", {
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
						fieldLabel : '销售经理',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						readOnly : true,
						name : 'username'
					}, {
						xtype : 'textfield',
						fieldLabel : '收款金额',
						labelWidth : 100,
						regex : /^[\d\.]*$/,
						regexText : '请输入数字',
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'sum'

					}, {
						xtype : "combo",
						fieldLabel : '收款账号',
						columnWidth : .45,
						labelWidth : 100,
						mode : 'local',
						name : "bankaccount",
						allowBlank : false,// 不允许为空
						blankText : "不能为空",// 错误提示信息，默认为This
						forceSelection : true,
						emptyText : '请选择账号',
						valueField : "bankaccount",
						displayField : "bankname",
						store : Ext.create('Ext.data.Store', {
									fields : [{
												name : 'bankaccount',
												mapping : 'bankaccount',
												type : 'string'
											}, {
												name : 'bankname',
												mapping : 'bankname',
												type : 'string'
											}],
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'judicial/bank/getBankname.do',
										params : {},
										reader : {
											type : 'json',
											root : 'data'

										}
									},
									autoLoad : true
								})

					}, {
						xtype : 'textarea',
						fieldLabel : '备注',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'remark'

					}, {
						xtype : "textfield",
						hidden : true,
						name : 'id'
					}, {
						xtype : "textfield",
						hidden : true,
						name : 'userid'
					}]
		}];

		me.buttons = [{
					text : '保存',
					iconCls : 'Disk',
					handler : me.onSave,
					listeners : {
						click : {
							fn : function(items, e, eOpts ) {
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
					url : "judicial/finance/saveReturnSum.do",
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