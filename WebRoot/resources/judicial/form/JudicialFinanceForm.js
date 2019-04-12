/**
 * 财务审核
 * 
 * @author fushaoming
 * @date 20150420
 */
Ext.define('Rds.judicial.form.JudicialFinanceForm', {
			extend : 'Ext.form.Panel',
			layout : 'form',
			bodyPadding : '10 10 10 10',
			initComponent : function() {
				var me = this;
				me.items = [{
							xtype : "textfield",
							name : 'fee_id',
							hidden : true
						}, {
							xtype : "displayfield",
							fieldLabel : '汇款人',
							fieldStyle : me.fieldStyle,
							allowBlank : false,
							name : 'username'
						}, {
							xtype : 'displayfield',
							name : 'date',
							readOnly : true,
							hideTrigger : true,
							fieldLabel : '汇款日期',
							allowBlank : false,
							anchor : '100%'
						}, {
							xtype : "displayfield",
							fieldLabel : '应付金额',
							fieldStyle : me.fieldStyle,
							allowBlank : false,
							name : 'sum'
						}, {
							xtype : "displayfield",
							fieldLabel : '实际金额',
							fieldStyle : me.fieldStyle,
							allowBlank : false,
							name : 'realsum'
						}, {
							xtype : "displayfield",
							fieldLabel : '银行',
							fieldStyle : me.fieldStyle,
							allowBlank : false,
							name : 'bank'
						}, {
							xtype : "displayfield",
							fieldLabel : '银行账号',
							fieldStyle : me.fieldStyle,
							allowBlank : false,
							name : 'bankaccount'
						}, {
							xtype : "displayfield",
							fieldLabel : '备注',
							fieldStyle : me.fieldStyle,
							allowBlank : false,
							name : 'remark'
						}, {
							xtype : 'textarea',
							emptyText : '审核不通过时，请填写审核理由',
							fieldLabel : '审核理由',
							name : 'check_remark',
							id : 'check_remark',
							maxLength : 1000,
							width : 450
						}]

				me.buttons = [{
							text : '确认收款',
							iconCls : 'Accept',
							handler : me.onVerify,
							listeners : {
								click : {
									fn : function(items, e, eOpts) {
										items.disable(true);
									}
								}
							}
						}, {
							text : '审核不通过',
							iconCls : 'Cancel',
							handler : me.onNoVerify,
							listeners : {
								click : {
									fn : function(items, e, eOpts) {
										items.disable(true);
									}
								}
							}
						}, {
							text : '取消',
							iconCls : 'Arrowredo',
							handler : me.onCancel
						}]
				me.callParent(arguments);
			},
			onVerify : function() {
				var me = this.up("form");
				var form = me.getForm();
				var values = form.getValues();
				if (form.isValid()) {
					Ext.Ajax.request({
								url : "judicial/finance/yes.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : values,
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response.success == true) {
										Ext.MessageBox.alert("提示信息",
												response.message);
										me.grid.getStore().load();
										me.up("window").close();
									} else {
										Ext.MessageBox.alert("错误信息",
												response.message);
									}
								},
								failure : function() {
									Ext.Msg.alert("提示", "出错<br>请联系管理员!");
								}

							});
				}
			},
			onNoVerify : function() {
				var me = this.up("form");
				var form = me.getForm();
				var values = form.getValues();
				var check_remark = Ext.getCmp("check_remark").getValue();
				if (check_remark == '' || check_remark == null) {
					Ext.Msg.alert("提示", "请填写审核不通过理由。");
					return;
				}
				if (form.isValid()) {
					Ext.Ajax.request({
								url : "judicial/finance/no.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : values,
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response.success == true) {
										Ext.MessageBox.alert("提示信息",
												response.message);
										me.grid.getStore().load();
										me.up("window").close();
									} else {
										Ext.MessageBox.alert("错误信息",
												response.message);
									}
								},
								failure : function() {
									Ext.Msg.alert("提示", "出错<br>请联系管理员!");
								}

							});
				}
			},
			onCancel : function() {
				var me = this;
				me.up("window").close();
			}

		});