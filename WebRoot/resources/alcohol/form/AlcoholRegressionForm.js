/**
 * 
 */
var bill_charge_value = '';
Ext.define('Rds.alcohol.form.AlcoholRegressionForm', {
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
				labelWidth : 100,
				style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
				xtype : 'panel',
				layout : 'column',
				defaults : {
					style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					allowBlank : false,
					regex : /^[\d.]+$/,
					regexText : '请输入数字',
					layout : 'column',
					border : true
				}
			},
			border : false,
			autoHeight : true,
			items : [{
						xtype : 'panel',
						layout : 'column',
						border : false,
						items : [{
									xtype : "textfield",
									fieldLabel : '标准 R',
									regex : /^[0-9\.]*$/,
									regexText : '请输入数字',
									labelWidth : 50,
									value : 0.9992,
									columnWidth : 0.3,
									name : 'reg_R2'
								}]
					}, {

						border : false,
						items : [{
									xtype : "displayfield",
									fieldLabel : '样品',
									labelWidth : 100,
									columnWidth : 0.3
								}, {
									xtype : "displayfield",
									fieldLabel : '乙醇',
									labelWidth : 100,
									columnWidth : 0.3
								}, {
									xtype : "displayfield",
									fieldLabel : '叔丁醇',
									labelWidth : 100,
									columnWidth : 0.3
								}]

					}, {
						border : false,
						items : [{
									xtype : "numberfield",
									labelWidth : 100,
									maxValue : 999999999999999,
									minValue:1,
									columnWidth : 0.3,
									value : 20,
									name : 'concentration'
								}, {
									xtype : "numberfield",
									labelWidth : 100,
									minValue:1,
									maxValue : 999999999999999,
									columnWidth : 0.3,
									name : 'alcohol'
								}, {
									xtype : "numberfield",
									labelWidth : 100,
									minValue:1,
									maxValue : 999999999999999,
									columnWidth : 0.3,
									name : 'butanol'
								}]
					}, {
						border : false,
						items : [{
									xtype : "numberfield",
									labelWidth : 100,
									maxValue : 999999999999999,
									minValue:1,
									columnWidth : 0.3,
									value : 40,
									name : 'concentration'
								}, {
									xtype : "numberfield",
									labelWidth : 100,
									maxValue : 999999999999999,
									minValue:1,
									columnWidth : 0.3,
									name : 'alcohol'
								}, {
									xtype : "numberfield",
									labelWidth : 100,
									maxValue : 999999999999999,
									minValue:1,
									columnWidth : 0.3,
									name : 'butanol'
								}]

					}, {
						border : false,
						items : [{
									xtype : "numberfield",
									labelWidth : 100,
									maxValue : 999999999999999,
									minValue:1,
									columnWidth : 0.3,
									value : 80,
									name : 'concentration'
								}, {
									xtype : "numberfield",
									labelWidth : 100,
									maxValue : 999999999999999,
									minValue:1,
									columnWidth : 0.3,
									name : 'alcohol'
								}, {
									xtype : "numberfield",
									labelWidth : 100,
									maxValue : 99999999999999,
									minValue:1,
									columnWidth : 0.3,
									name : 'butanol'
								}]

					}, {
						border : false,
						items : [{
							layout : 'absolute',
							border : false,
							items : [{
								xtype : 'button',
								text : '增加',
								width : 100,
								x : 500,
								handler : function() {
									var me = this.up('form');
									me.add({
										xtype : 'form',
										layout : 'column',
										border : false,
										items : [{
													xtype : "numberfield",
													labelWidth : 100,
													maxValue : 999999999999999,
													minValue:1,
													columnWidth : 0.33,
													name : 'concentration'
												}, {
													xtype : "numberfield",
													labelWidth : 100,
													maxValue : 999999999999999,
													minValue:1,
													columnWidth : 0.33,
													name : 'alcohol'
												}, {
													xtype : "numberfield",
													labelWidth : 100,
													maxValue : 9999999999999999,
													minValue:1,
													columnWidth : 0.33,
													name : 'butanol'
												}, {
													xtype : 'button',
													style : 'margin-left:15px;',
													text : '删除',
													handler : function() {
														var me = this
																.up("form");
														me.disable(true);
														me.up("form")
																.remove(me);
													}
												}]
									});
								}
							}]

						}]
					}]
		}];

		me.buttons = [{
					text : '生成',
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
					url : "alcohol/experiment/addRegression.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : values,
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.success == true) {
							Ext.MessageBox.alert("提示信息", response.result);
							me.up("window").close();
						} else {
							Ext.MessageBox.alert("错误信息", response.result);
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