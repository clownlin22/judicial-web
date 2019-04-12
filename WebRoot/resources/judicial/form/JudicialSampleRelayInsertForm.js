Ext.define(
				'Rds.judicial.form.JudicialSampleRelayInsertForm',
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
						var sampleSelectStore=Ext.create('Ext.data.Store', {
							fields:['sample_id','sample_code'],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/sampleRelay/getSelectSampleInfo.do',
								reader : {
									type : 'json',
									root : 'data'
								}
							},
							autoLoad : true,
							remoteSort : true
						});
						var isForm = new Ext.form.FormPanel({
				            items : [{
				                xtype : 'itemselector',
				                name : 'sampleids',
				                id:'sampleids',
				                toTitle : "已选栏",
				                fromTitle : "可选栏",
				                drawLeftIcon : true,
				                drawRightIcon : true,
				                height:400,
				                style : 'margin-top:10px;margin-left:10px;margin-right:10px;',
				                buttons:['add','remove'],
				                buttonsText:{
				                	add:"选择",
				                	remove:"移除"
				                },
				                imagePath: '../../ux/images/',
				                store:sampleSelectStore,
				                displayField: 'sample_code',
				                valueField: 'sample_id',
				            }]
				        });  
						me.items = [{
							        xtype     : 'textareafield',
							        grow      : true,
							        name      : 'relay_remark',
							        fieldLabel: '交接备注',
							        anchor    : '100%',
							        maxLength : 500,
					                },
					                isForm
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
						if(form.isValid()){
							var sample_ids=values["sampleids"];
							if(sample_ids.length<=0){
								Ext.MessageBox.alert("错误信息", "请选择需要确认样本号");
							}else{
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({  
								url:"judicial/sampleRelay/saveRelaySampleInfo.do", 
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
						}
					},
					onCancel : function() {
						var me = this;
						me.up("window").close();
					}
				});
