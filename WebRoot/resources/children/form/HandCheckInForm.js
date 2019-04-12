Ext.define('Rds.children.form.HandCheckInForm',
				{
					extend : 'Ext.form.Panel',
					style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					bodyPadding : 10,
					defaults : {
						anchor : '100%',
						labelWidth : 80,
						labelSeparator: "：",
						labelAlign: 'right'
					},
					border : false,
					autoHeight : true,
					initComponent : function() {
						var me = this;
						me.items = [{
							xtype : 'tbtext',
							style:'color:red;margin-bottom:5px;',
							text : '位点信息手动录入样例：12,12',
						},{
							columnWidth : .24,
							xtype : "textfield",
							fieldLabel : "案例编号",
							maxLength : 50,
							labelWidth : 70,
							name : "case_code",
							readOnly:true
						},new Ext.form.field.ComboBox({
							name : 'agentia_id',
							id:'agentia_id_hand',
							fieldLabel : "试剂类型<span style='color:red'>*</span>",
							labelWidth : 70,
							editable : false,
							mode : 'remote',
							forceSelection : true,
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field is required!
							triggerAction : 'all',
							displayField : 'agentia_name',
							valueField : 'agentia_id',
							store : Ext.create('Ext.data.Store', {
										fields : ['agentia_id', 'agentia_name'],
										proxy : {
											type : 'jsonajax',
											actionMethods : {
												read : 'POST'
											},
											url : 'children/agentia/getAgentiaCombo.do',
											params : {},
											reader : {
												type : 'json',
												root : 'data'
											}
										}
									}),
							listeners :{
			                       	"select" : function(combo, record, index){
										var me = Ext.getCmp("agentiaFieldset");
										var length = me.items.length;
										for(var i = 0 ; i < length ; i ++){
											me.remove(me.items.get(0));
										}
			                       		Ext.Ajax.request({
			                   				url : "children/agentia/getlocusInfo.do",
			                   				method : "POST",
			                   				headers : {
			                   					'Content-Type' : 'application/json'
			                   				},
			                   				jsonData : {agentia_id:combo.getValue()},
			                   				success : function(response, options) {
			                   					response = Ext.JSON.decode(response.responseText);
			                   					agentia = response.data;
			                   					for(var i = 0 ; i < agentia.length; i ++){
			                   						me.add({
														columnWidth : .24,
														xtype : "textfield",
														fieldLabel : agentia[i].locus_name,
														regex:/^[^\s]*$/,
														regexText:'请输入正确点位值',
														labelAlign : 'right',
														maxLength : 50,
														allowBlank : false,// 不允许为空
														blankText : "不能为空",// 错误提示信息，默认为This
														labelWidth : 80,
														name : agentia[i].locus_name
													});
			                   					}
			                   				},
			                   				failure : function() {
			                   					Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
			                   				}
			
			                   			});
			                       		
			                       	}
			                   }
						}),{
							xtype : 'fieldset',
							title : '位点信息',
							id:'agentiaFieldset',
							layout : 'anchor',
							defaults : {
								anchor : '100%'
							},
							items : []
						}];
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
						values.agentia_name=Ext.getCmp("agentia_id_hand").getRawValue();
						if(form.isValid()){
							Ext.MessageBox.wait('正在操作','请稍后...');
							Ext.Msg.confirm("提示", "确认保存手动录入该案例？", function (id) {
								if (id == 'yes') {
									Ext.MessageBox.wait('正在操作','请稍后...');
									Ext.Ajax.request({  
										url:"children/result/addCaseResultByHand.do", 
										method: "POST",
										headers: { 'Content-Type': 'application/json' },
										jsonData: values, 
										success: function (response, options) {  
											response = Ext.JSON.decode(response.responseText); 
							                 if (response.result==true) {  
							                 	Ext.MessageBox.alert("提示信息", response.message);
							                 	me.grid.getStore().load();
							                 	me.up("window").close();
							                 }else { 
							                 	Ext.MessageBox.alert("错误信息", response.message);
							                 } 
										},  
										failure: function () {
											Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
										}
							      	});
								}
							})
						
						}
					},
					onCancel : function() {
						var me = this;
						me.up("window").close();
					}
				});
function getArray(a) {
	 var hash = {},
	     len = a.length,
	     result = [];

	 for (var i = 0; i < len; i++){
	     if (!hash[a[i]]){
	         hash[a[i]] = true;
	         result.push(a[i]);
	     } 
	 }
	 return result;
	}