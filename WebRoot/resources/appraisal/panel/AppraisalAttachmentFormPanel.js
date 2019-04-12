/**
 * @author yxb
 * @description 鉴定报告附件上传
 * @date 2015-08-05
 */
Ext.define('Rds.appraisal.panel.AppraisalAttachmentFormPanel', {
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
			items : [
			         {
						xtype : "textfield",
						fieldLabel : 'case_id',
						fieldStyle : me.fieldStyle,
						name : 'case_id',
						value:case_id_appraisal.split(',')[0],
						hidden:true
					}, 
//					{
//						xtype : 'filefield',
//						name : 'files',
//						fieldLabel : '文件',
//						msgTarget : 'side',
//						allowBlank : false,
//						anchor : '100%',
//						buttonText : '选择文件'
//					}, 
					{
						xtype : 'panel',
						layout : 'absolute',
						border : false,
						items : [{
							xtype : 'button',
							text : '增加文件',
							width : 100,
							x : 0,
							handler : function() {
								var me = this.up('form');
								me.add({
									xtype : 'form',
									style : 'margin-top:5px;',
									layout : 'column',
									border:false,
									items : [
											{
												xtype : 'filefield',
												name : 'files',
												width:375,
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
													var me = this
															.up("form");
													me.disable(true);
													me.up("form")
															.remove(me);
												}
											},
											{
												xtype : 'textfield',
												name : 'attachment_type',
												width:150,
												fieldLabel : '类型',
												labelAlign : "right",
												labelWidth : 50,
												allowBlank : false,
											},{
												xtype : 'textfield',
												name : 'attachment_name',
												width:150,
												fieldLabel : '名称',
												labelAlign : "right",
												labelWidth : 50,
												allowBlank : false,
											},{
												xtype : 'textfield',
												name : 'attachment_order',
												width:150,
												fieldLabel : '顺序',
												labelAlign : "right",
												labelWidth : 50,
												allowBlank : false,
											}]
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
					url : 'appraisal/info/upload.do',
					method : 'post',
					waitMsg : '正在上传您的文件...',
					success : function(form, action) {
						response = Ext.JSON
								.decode(action.response.responseText);
						if (response.success) {
							case_id_appraisal = response.case_id +","+response.filenames;
							Ext.MessageBox.alert("提示信息", response.msg);
							myWindow.close();
						}else{
							Ext.MessageBox.alert("提示信息", "上传发生错误，请联系管理员");
							myWindow.close();
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