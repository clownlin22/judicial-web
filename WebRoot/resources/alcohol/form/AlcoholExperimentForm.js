/**
 * 新增实验表单
 * 
 * @date 20150611
 */

Ext.define('Rds.alcohol.form.AlcoholExperimentForm', {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;

		me.items = [{
			xtype : 'form',
			region : 'north',
			labelAlign : "right",
			bodyPadding : 10,
			layout : 'column',
			defaults : {
				anchor : '100%',
				labelWidth : 100,
				border : false,
				style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;'
			},
			border : false,
			autoHeight : true,
			items : [{

						xtype : "textfield",
						name : 'case_id',
						hidden : true
					}, {
						xtype : "displayfield",
						fieldLabel : '案例编号',
						labelWidth : 60,
						columnWidth : 0.5,
						name : 'case_code'
					}, {
						xtype : "displayfield",
						fieldLabel : '当事人',
						labelWidth : 50,
						columnWidth : 0.5,
						name : 'sample_name'
					}, {
						xtype : "displayfield",
						fieldLabel : '身份证',
						labelWidth : 50,
						columnWidth : 1,
						name : 'id_number'

					}]
		}, {
			xtype : 'form',
			region : 'center',
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

						border : false,
						items : [{
									xtype : "displayfield",
									fieldLabel : '乙醇',
									labelWidth : 100,
									maxLength : 999999999999,
									columnWidth : 0.5
								}, {
									xtype : "displayfield",
									fieldLabel : '叔丁醇',
									maxLength : 999999999999,
									labelWidth : 100,
									columnWidth : 0.5
								}]

					}, {
						border : false,
						items : [{
									xtype : "textfield",
									labelWidth : 100,
									maxLength : 999999999999,
									columnWidth : 0.5,
									name : 'alcohol'
								}, {
									xtype : "textfield",
									labelWidth : 100,
									maxLength : 999999999999,
									columnWidth : 0.5,
									name : 'butanol'
								}]
					}, {
						border : false,
						items : [{
									xtype : "textfield",
									labelWidth : 100,
									maxLength : 999999999999,
									columnWidth : 0.5,
									name : 'alcohol'
								}, {
									xtype : "textfield",
									labelWidth : 100,
									maxLength : 999999999999,
									columnWidth : 0.5,
									name : 'butanol'
								}]

					}]

		}];

		me.buttons = [{
					text : '计算结果',
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
					url : "alcohol/experiment/addExperiment.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : values,
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.success == true) {
							Ext.MessageBox.alert("提示信息", response.result);
							me.grid.getStore().load();
							me.up("window").close();
						} else {
							me.grid.getStore().load();
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