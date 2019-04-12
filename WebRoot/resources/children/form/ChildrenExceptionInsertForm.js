Ext.define('Rds.children.form.ChildrenExceptionInsertForm',{
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
						valueField :"value",   
				        displayField: "value", 
						name : 'exception_type',
						store: storeModel
			   });
			me.items = [
					{
						xtype : 'hiddenfield',
						name : 'case_id'
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
			var me = this.up("form");
			var form = me.getForm();
			var values = form.getValues();
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
					url : "children/exception/saveException.do",
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
									"新增自定义异常成功!");
							me.grid.getStore().load();
							me.up("window").close();
						} else {
							Ext.MessageBox.alert("错误信息",
									"新增自定义异常失败!");
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
		}
	});
