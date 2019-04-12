Ext.define("Rds.children.form.ChildrenAgentiaForm", {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var order = 0;
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
						fieldLabel : '试剂名称',
						labelWidth : 60,
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'agentia_name',
						maxLength : 20
					}, {
						xtype : 'textarea',
						fieldLabel : '备注',
						labelWidth : 60,
						fieldStyle : me.fieldStyle,
						allowBlank : true,
						name : 'remark',
						height : 22,
						maxLength : 100
					}, {
						xtype : "textfield",
						hidden : true,
						name : 'agentia_id'
					}, {
						xtype : 'panel',
						layout : 'absolute',
						border : false,
						items : [{
							xtype : 'button',
							text : '增加点位名称',
							width : 100,
							x : 480,
							handler : function() {
								var me = this.up('form');
								if (order >= 1) {
									Ext.getCmp('deleteButton' + order).hide();
								}
								order += 1;
								me.add({
									xtype : 'form',
									layout : 'column',
									padding : '5 5 5 5',
									anchor : '100%',
									border : false,
									items : [{
												xtype : "textfield",
												fieldLabel : '点位名称',
												labelWidth : 60,
												fieldStyle : me.fieldStyle,
												allowBlank : false,
												name : 'locus_name',
												maxLength : 100
											}, {
												xtype : "numberfield",
												fieldLabel : '排序',
												labelWidth : 60,
												fieldStyle : me.fieldStyle,
												allowBlank : false,
												name : 'order',
												maxLength : 2,
												readOnly : true,
												value : order
											}, {
												id : 'deleteButton' + order,
												xtype : 'button',
												style : 'margin-left:15px;',
												text : '删除',
												handler : function() {
													order -= 1;
													if (order >= 1) {
														Ext.getCmp('deleteButton'
																		+ order)
																.show();
													}
													var me = this.up("form");
													me.disable(true);
													me.up("form").remove(me);
												}
											}]
								});
							}
						}]
					}]
		}];

		me.buttons = [{
					text : '保存',
					iconCls : 'Disk',
					handler : me.onSave,
					listeners : {

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

		Ext.Ajax.request({
					url : "children/agentia/save.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : values,
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.success == true) {
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