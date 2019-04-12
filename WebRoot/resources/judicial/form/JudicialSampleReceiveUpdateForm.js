Ext
		.define(
				'Rds.judicial.form.JudicialSampleReceiveUpdateForm',
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
					autoScroll: true, 
					initComponent : function() {
						var me = this;
						me.items = [{
									xtype : 'hiddenfield',
									name : 'receive_id'
								 },{
							        xtype     : 'textareafield',
							        grow      : true,
							        name      : 'receive_remark',
							        fieldLabel: '接收备注',
							        anchor    : '100%',
							        maxLength : 500,
					             },{
									border : false,
									xtype : 'fieldcontainer',
									layout : "column",
									items :[{
									    xtype : 'button',
									    text : '添加样本',
									    width:100,
									    listeners:{
									        click:function(){
									        	var me = this.up("form");
									        	me.add({
													xtype : 'form',
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
													items:[{
														layout:'auto',
														xtype:'panel',
														border:false,
														items:[{
																layout:'column',
																xtype:'panel',
																border:false,
																items:[{columnWidth : .5,
																			xtype : "textfield",
																			fieldLabel : "条形码<span style='color:red'>*</span>",
																		    allowBlank  : false,//不允许为空  
																	        blankText   : "不能为空",//错误提示信息，默认为This field is required!  
																			labelAlign: 'right',
																			maxLength :50,
																			labelWidth : 80,
																			name : 'sample_code',
																			validator : function(value) {
																				var result = false;
																				Ext.Ajax
																						.request({
																							url : "judicial/sampleRelay/exsitReceiveSampleCode.do",
																							method : "POST",
																							async : false,
																							headers : {
																								'Content-Type' : 'application/json'
																							},
																							jsonData : {
																								sample_code : value,
																								type:'receive'
																							},
																							success : function(
																									response,
																									options) {
																								response = Ext.JSON
																										.decode(response.responseText);
																								if (!response) {
																									result = "此条形码已存在";
																								} else {
																									result = true;
																								}
																							},
																							failure : function() {
																								Ext.Msg
																										.alert(
																												"提示",
																												"网络故障<br>请联系管理员!");
																							}
																						});
																				return result;
																			}
																		},{
																				layout:  'absolute',// 从左往右的布局
																				xtype : 'panel',
																				border : false,
																			    columnWidth : .2,
																				items:[{
																					    width:50,
																					    x:30,
																					    xtype : 'button',
																					    text : '删除',
																					    listeners:{
																					        click:function(){
																					        	var mei = this.up("form");
																					        	mei.disable(true);
																					        	mei.up("form").remove(mei);
																					        }
																					       }
																				}]
																			 }]
															     }]
													   }]
													});
									       }
									    }
								}]
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
						var sample_str=values['sample_code'];
						if(form.isValid()){
							var result=true;
							if(sample_str){
									if(!((typeof sample_str=='string')&&sample_str.constructor==String)){
										for(var i=0;i<sample_str.length;i++){
											for(var j=0;j<sample_str.length;j++){
												if(i!=j&&sample_str[i]==sample_str[j]){
													result=false;
												}
											}
										}
									}
							}
							if(!result){
									Ext.MessageBox.alert("错误信息", "存在条形码输入一样的数据");
							}else{
								Ext.Ajax.request({  
									url:"judicial/sampleRelay/updateReceiveSampleInfo.do", 
									method: "POST",
									headers: { 'Content-Type': 'application/json' },
									jsonData: values, 
									success: function (response, options) {  
										response = Ext.JSON.decode(response.responseText); 
						                 if (response==true) {  
						                 	Ext.MessageBox.alert("提示信息", "接收修改成功");
						                 	me.grid.getStore().load();
						                 	me.up("window").close();
						                 }else { 
						                 	Ext.MessageBox.alert("错误信息", "接收修改失败");
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
							var values = me.getValues();
							var receive_id = values["receive_id"];
							Ext.Ajax.request({
								url : 'judicial/sampleRelay/getReceiveSampleInfo.do',
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {
									'receive_id' : receive_id
								},
								success : function(response, options) {
									response = Ext.JSON.decode(response.responseText);
									for (var i = 0; i < response.length; i++) {
										me.add({
											xtype : 'form',
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
											items:[{
												layout:'auto',
												xtype:'panel',
												border:false,
												items:[{
														layout:'column',
														xtype:'panel',
														border:false,
														items:[{columnWidth : .5,
																	xtype : "textfield",
																	fieldLabel : "条形码<span style='color:red'>*</span>",
																    allowBlank  : false,//不允许为空  
															        blankText   : "不能为空",//错误提示信息，默认为This field is required!  
																	labelAlign: 'right',
																	readOnly : true,
																	maxLength :50,
																	labelWidth : 80,
																	value:response[i].sample_code,
																	name : 'sample_code',
																},{
																		layout:  'absolute',// 从左往右的布局
																		xtype : 'panel',
																		border : false,
																	    columnWidth : .25,
																		items:[{
																			    width:50,
																			    x:30,
																			    xtype : 'button',
																			    text : '删除',
																			    listeners:{
																			        click:function(){
																			        	var mei = this.up("form");
																			        	mei.disable(true);
																			        	mei.up("form").remove(mei);
																			        }
																			       }
																		}]
																	 }]
													     }]
											   }]
											});		
									}
								}
							});
				}
		}
});
