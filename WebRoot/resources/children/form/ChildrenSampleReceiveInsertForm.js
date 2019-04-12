Ext.define('Rds.children.form.ChildrenSampleReceiveInsertForm',
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
							        xtype     : 'textareafield',
							        grow      : true,
							        name      : 'remark',
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
									        	test();
									        	function test()
									        	{
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
																	items:[{    columnWidth : .7,
																				xtype : "textfield",
																				fieldLabel : "样本条码<span style='color:red'>*</span>",
																			    allowBlank  : false,//不允许为空  
																		        blankText   : "不能为空",//错误提示信息，默认为This field is required!  
																				labelAlign: 'right',
																				maxLength :50,
																				labelWidth : 80,
																				regex:/^[^\s]*$/,
																				regexText:'请输入正确条形码',
																				name : 'sample_code',
																				listeners: {  
																					'render' : function() { 
																						this.focus(false, 100); 
//																						this.findByType('textfield')[0].focus(true, true); //第一个textfield获得焦点 
																						} ,
																                    specialkey: function(field,e){    
																                        if (e.getKey()==Ext.EventObject.ENTER){    
																                            test();
																                        }  
																                    }  
																                },
																				validator : function(value) {
																					var result = false;
																					Ext.Ajax.request({
																								url : "children/sampleRelay/exsitReceiveSampleCode.do",
																								method : "POST",
																								async : false,
																								headers : {
																									'Content-Type' : 'application/json'
																								},
																								jsonData : {
																									sample_code : value
																								},
																								success : function(response,options) {
																									response = Ext.JSON.decode(response.responseText);
																									if (!response.result) {
																										result = "此条形码不存在!";
																									} else {
																										result = true;
																									}
																								},
																								failure : function() {
																									Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
																								}
																							});
																					return result;
																				}
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
						if(undefined == sample_str)
						{
							Ext.MessageBox.alert("错误信息", "还没输入样本信息！");
							return;
						}
						if(form.isValid()){
							Ext.MessageBox.wait('正在操作','请稍后...');
							var result=true;
							var tempResult= new Array();
							if(sample_str){
									if(!((typeof sample_str=='string')&&sample_str.constructor==String)){
										for(var i=0;i<sample_str.length;i++){
											for(var j=0;j<sample_str.length;j++){
												if(i!=j&&sample_str[i]==sample_str[j]){
													tempResult.push(sample_str[i]);
													result=false;
												}
											}
										}
									}
							}
							if(!result){
									Ext.MessageBox.alert("错误信息", "存在条形码输入一样的数据:"+getArray(tempResult));
									return;
							}else{
								Ext.Msg.confirm("提示", "确认样本接收?保存不可以更改咯！", function (id) {
									if (id == 'yes') {
										Ext.MessageBox.wait('正在操作','请稍后...');
										Ext.Ajax.request({  
											url:"children/sampleRelay/saveReceiveSampleInfo.do", 
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