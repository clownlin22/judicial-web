/**
 * @author fushaoming
 * @description 新增附件上传表单
 * @date 20150407
 */

Ext.define('Rds.judicial.panel.JudicialAttachmentFormPanel', {
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
				labelWidth : 70
			},
			border : false,
			autoHeight : true,
			items : [{
						xtype : "textfield",
						fieldLabel : '案例条形码',
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						regex : /^\w*$/,
						regexText : '请输入英文或数字',
						name : 'case_code',
						maxLength : 30
					}, {
						xtype : 'filefield',
						name : 'files',
						fieldLabel : '文件',
						msgTarget : 'side',
						allowBlank : false,
						anchor : '100%',
						buttonText : '选择文件'
					}, new Ext.form.field.ComboBox({
								fieldLabel : '文件类型',
								labelWidth : 70,
								fieldStyle : me.fieldStyle,
								editable : false,
								allowBlank : false,
								triggerAction : 'all',
								displayField : 'Name',
								valueField : 'Code',
								store : new Ext.data.ArrayStore({
											fields : ['Name', 'Code'],
											data : [['登记表格', 1], ['身份证', 2],
													['照片', 3], ['其他', 4]]
										}),
								mode : 'local',
								name : 'filetype'

							}), {
						xtype : 'panel',
						layout : 'absolute',
						border : false,
						items : [{
							xtype : 'button',
							text : '增加文件',
							width : 100,
							x : 360,
							handler : function() {
								var me = this.up('form');
								me.add({
									xtype : 'form',
									style : 'margin-top:5px;',
									layout : 'column',
									border : false,
									items : [{
												xtype : 'filefield',
												name : 'files',
												width : 375,
												fieldLabel : '文件',
												labelWidth : 70,
												msgTarget : 'side',
												allowBlank : false,
												anchor : '100%',
												buttonText : '选择文件'
											}, {
												xtype : 'button',
												style : 'margin-left:15px;',
												text : '删除',
												handler : function() {
													var me = this.up("form");
													me.disable(true);
													me.up("form").remove(me);
												}
											}, new Ext.form.field.ComboBox({
												fieldLabel : '文件类型',
												labelWidth : 70,
												fieldStyle : me.fieldStyle,
												editable : false,
												allowBlank : false,
												style : 'margin-top:10px;',
												triggerAction : 'all',
												displayField : 'Name',
												valueField : 'Code',
												store : new Ext.data.ArrayStore(
														{
															fields : ['Name',
																	'Code'],
															data : [
																	['登记表格', 1],
																	['身份证', 2],
																	['照片', 3],
																	['其他', 4]]
														}),
												mode : 'local',
												name : 'filetype'

											})]
								});
							}
						}]

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
			Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
			return;
		}
		form.submit({
					url : 'judicial/attachment/upload.do',
					method : 'post',
					waitMsg : '正在上传您的文件...',
					success : function(form, action) {
						response = Ext.JSON
								.decode(action.response.responseText);
						console.log(response);
						if (response.result) {
							Ext.MessageBox.alert("提示信息", response.msg);
							me.up('form').grid.getStore().load();
							myWindow.close();
						} else {
							Ext.MessageBox.alert("提示信息", response.msg);
//							myWindow.close();
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "上传失败，请联系管理员!");
						myWindow.close();
					}
				});

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}

});