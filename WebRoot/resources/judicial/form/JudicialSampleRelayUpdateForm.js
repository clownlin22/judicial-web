Ext
		.define(
				'Rds.judicial.form.JudicialSampleRelayUpdateForm',
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
									xtype : 'hiddenfield',
									name : 'relay_id'
					            },{
							        xtype     : 'textareafield',
							        grow      : true,
							        name      : 'relay_remark',
							        fieldLabel: '交接备注',
							        anchor    : '100%',
							        maxLength : 500,
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
						if(form.isValid()){
							var sample_ids=values["sampleids"];
							if(sample_ids.length<=0){
								Ext.MessageBox.alert("错误信息", "请选择需要确认样本号");
							}else{
								Ext.Ajax.request({  
									url:"judicial/sampleRelay/updateRelaySampleInfo.do", 
									method: "POST",
									headers: { 'Content-Type': 'application/json' },
									jsonData: values, 
									success: function (response, options) {  
										response = Ext.JSON.decode(response.responseText); 
						                 if (response==true) {  
						                 	Ext.MessageBox.alert("提示信息", "修改交接成功");
						                 	me.grid.getStore().load();
						                 	me.up("window").close();
						                 }else { 
						                 	Ext.MessageBox.alert("错误信息", "修改交接失败");
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
					},
					listeners : {
						'afterrender' : function() {
							var me = this;
							var form = me.getForm();
							var values = form.getValues();
							var value=values["relay_id"];
							var sampleSelectStore=Ext.create('Ext.data.Store', {
								fields:['sample_id','sample_code'],
								proxy : {
									type : 'jsonajax',
									actionMethods : {
										read : 'POST'
									},
									url : 'judicial/sampleRelay/getSelectSampleInfo.do',
									params : {
										relay_id : value
									},
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
					                toLegend : "已选栏",
					                fromLegend : "可选栏",
					                drawLeftIcon : true,
					                drawRightIcon : true,
					                height:400,
					                style : 'margin-top:10px;margin-left:10px;margin-right:10px;',
					                buttons:['add','remove'],
					                buttonsText:{
					                	add:"选择",
					                	remove:"取消"
					                },
					                imagePath: '../../ux/images/',
					                store:sampleSelectStore,
					                displayField: 'sample_code',
					                valueField: 'sample_id',
					            }]
					        }); 
							me.add(isForm);
							Ext.Ajax.request({  
								url:"judicial/sampleRelay/getRelaySampleInfo.do", 
								method: "POST",
								headers: { 'Content-Type': 'application/json' },
								jsonData: values, 
								success: function (response, options) {  
									response = Ext.JSON.decode(response.responseText); 
									if(response.length>0){
										var str="[";
										for(var i=0;i<response.length;i++){
											str+="'"+response[i].sample_id+"',";
										}
										str=str.substring(0,(str.length-1))+"]";
										isForm.down("itemselector").setValue(eval("(" + str + ")"));
									}
								},  
								failure: function () {
									Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
								}
				         	});
						}
					}
				});
