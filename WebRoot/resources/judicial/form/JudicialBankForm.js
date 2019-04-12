/**
 * 
 */
Ext.define("Rds.judicial.form.JudicialBankForm", {
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
						fieldLabel : '银行名称',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'bankname',
						maxLength : 20
					}, {
						xtype : "textfield",
						fieldLabel : '银行账户',
						labelWidth : 100,
						regex : /^[\w@\.]*$/,
						regexText : '请输入数字',
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'bankaccount',
						maxLength : 50
					}, {
						xtype : "combo",
						fieldLabel : '所属企业',
						labelWidth : 100,
						mode : 'local',
						name : "companyid",
						allowBlank : false,// 不允许为空
						blankText : "不能为空",// 错误提示信息，默认为This
						forceSelection : true,
						emptyText : '请选择企业单位',
						valueField : "companyid",
						displayField : "companyname",
						store : Ext.create('Ext.data.Store', {
									fields : [{
												name : 'companyid',
												mapping : 'companyid',
												type : 'string'
											}, {
												name : 'companyname',
												mapping : 'companyname',
												type : 'string'
											}],
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'judicial/bank/getCompany.do',
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
			Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
			return;
		}
		var values = form.getValues();
		Ext.MessageBox.wait('正在操作','请稍后...');
		Ext.Ajax.request({
					url : "judicial/bank/save.do",
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