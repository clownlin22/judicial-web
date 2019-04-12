/**
 * @author fushaoming
 * @description 发票管理
 * @date 20160421
 */
var bill_charge_value = '';
Ext.define('Rds.judicial.form.JudicialBillForm', {
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
				fieldLabel : '案例编号',
				labelWidth : 100,
				regex:/^\w*$/,
				regexText : '请输入英文或数字',
				fieldStyle : me.fieldStyle,
				allowBlank : false,
				name : 'case_code'
			}, {
				xtype : "textfield",
				fieldLabel : '发票编号',
				labelWidth : 100,
				regex:/^\w*$/,
				regexText : '请输入英文或数字',
				fieldStyle : me.fieldStyle,
				allowBlank : false,
				name : 'bill_codes'
			}, {
				xtype : "textfield",
				id : "bill_charge",
				fieldLabel : '发票金额',
				labelWidth : 100,
				fieldStyle : me.fieldStyle,
				allowBlank : false,
				regex : /^\d+$/,
				regexText : '请输入正确金额',
				name : 'bill_charges'
			}, {
				xtype : 'datefield',
				name : 'date',
				// width : 135,
				labelWidth : 100,
				fieldStyle : me.fieldStyle,
				fieldLabel : '开票时间',
				// labelAlign : 'right',
				emptyText : '请选择日期',
				format : 'Y-m-d',
				maxValue : new Date(),
				value : new Date()
			}, {
				xtype : 'textarea',
				fieldLabel : '备注',
				labelWidth : 100,
				fieldStyle : me.fieldStyle,
				allowBlank : false,
				name : 'remarks'

			}]
		}];

		me.buttons = [{
					text : '开票',
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
			Ext.MessageBox.alert("提示信息", "请填写完整表单信息!");
			return;
		}
		var values = form.getValues();

		Ext.Ajax.request({
					url : "judicial/bill/save.do",
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