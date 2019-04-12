var storeModel = Ext.create('Ext.data.Store', {
	model : 'model',
	proxy : {
		type : 'jsonajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'judicial/caseException/getExceptionTypes.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	autoLoad : true,
	remoteSort : true
});
Ext
		.define(
				'Rds.judicial.form.JudicialExceptionUpdateForm',
				{
					extend : 'Ext.form.Panel',
					style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					bodyPadding : 10,
					defaults : {
						anchor : '100%',
						labelWidth : 80,
						labelSeparator : "：",
						labelAlign : 'right'
					},
					border : false,
					autoHeight : true,
					initComponent : function() {
						var me = this;
						var exceptiontype=Ext.create('Ext.form.ComboBox', 
								{
									xtype : "combo",
									fieldLabel : "异常类型<span style='color:red'>*</span>",
									mode: 'local',   
									labelWidth : 80,
									editable:false,
									labelSeparator : "：",
									labelAlign: 'right',
									blankText:'请选择异常类型',   
							        emptyText:'请选择异常类型',  
							        allowBlank  : false,//不允许为空  
						            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
									valueField :"key",   
							        displayField: "value", 
									name : 'exception_type_update',
									store: storeModel
						   });
						me.items = [
								{
									xtype : 'hiddenfield',
									name : 'exception_id'
								},
								{
									xtype : 'hiddenfield',
									name : 'exception_type'
								},
								exceptiontype,
								{
									xtype : 'textarea',
									fieldLabel : "异常描述<span style='color:red'>*</span>",
									name : 'exception_desc',
									labelSeparator : "：",
									width : 500,
									allowBlank  : false,//不允许为空  
						            blankText   : "不能为空",//错误提示信息，默认为This field is required!
									maxLength :500,
									labelWidth : 80,
									labelAlign : 'right'
								 }
								];

						me.buttons = [ {
							text : '保存',
							iconCls : 'Disk',
							handler : me.onSave
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : me.onCancel
						} ]
						me.callParent(arguments);
					},
					onSave : function() {
						var mi = this.up("form");
						var form = mi.getForm();
						var values = form.getValues();
						var sample_str = values['sample_code'];
						if (form.isValid()) {
							Ext.Ajax.request({
								url : "judicial/caseException/updateExceptionInfo.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : values,
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response == true) {
										Ext.MessageBox.alert("提示信息",
												"修改自定义异常成功");
										mi.up("window").close();
									} else {
										Ext.MessageBox.alert("错误信息",
												"修改自定义异常失败");
									}
								},
								failure : function() {
									Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
								}
							});
						}
					},
					onCancel : function() {
						var me = this;
						me.up("window").close();
					},
					listeners : {
						'afterrender' : function() {
							var me = this;
							var values = me.getValues();
							storeModel.load();
							var type=me.down("combo[name=exception_type_update]")
							type.setValue(values["exception_type"]);
						}
					}
				});
