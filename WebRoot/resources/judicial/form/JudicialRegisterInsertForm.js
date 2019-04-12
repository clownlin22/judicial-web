Ext
		.define(
				'Rds.judicial.form.JudicialRegisterInsertForm',
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
						var storeModel = Ext.create('Ext.data.Store',{
						          model:'model',
						          proxy:{
						        	type: 'jsonajax',
						            actionMethods:{read:'POST'},
						            url:'judicial/dicvalues/getReportModelByPartner.do',
						            params:{
						            	 type:'dna',
						            	 receiver_id:""
						            },
						            reader:{
						              type:'json',
						              root:'data'
						            }
						          },
						          autoLoad:true,
						          remoteSort:true						
						        }
						      );
						var caseTypeModel = Ext.create('Ext.data.Store',{
					          model:'model',
					          proxy:{
					        	type: 'jsonajax',
					            actionMethods:{read:'POST'},
					            url:'judicial/dicvalues/getCaseTypes.do',
					            reader:{
					              type:'json',
					              root:'data'
					            }
					          },
					          autoLoad:true,
					          remoteSort:true,
					          listeners : {
									'load' : function() {
										case_type.select(this.getAt(0));
									}
								}
					        }
					      );
						var  storeSampleType=Ext.create('Ext.data.Store',{
						          model:'model',
						          proxy:{
						        	type: 'ajax',
						            actionMethods:{read:'POST'},
						            url:'judicial/dicvalues/getSampleType.do',
						            reader:{
						              type:'json',
						              root:'data'
						            }
						          },
						          autoLoad:true,
						          remoteSort:true						
						        }
						      );
						var unitStore=Ext.create('Ext.data.Store',{
						          model:'model',
						          proxy:{
						    		type: 'jsonajax',
						    		actionMethods:{read:'POST'},
						            url:'judicial/dicvalues/getUnitTypes.do',
						            params:{
						          	  initials:""
						            },
						            reader:{
						              type:'json',
						              root:'data'
						            }
						          },
						          autoLoad:true,
						          remoteSort:true,
						          listeners : {
										'load' : function() {
											unit_type.select(this.getAt(0));
										}
									}
						        }
						   );
						var unit_type=Ext.create('Ext.form.ComboBox', {
								columnWidth : .45,
								fieldLabel : "单位类型<span style='color:red'>*</span>",
								mode: 'local',   
								labelWidth : 80,
								editable:false,
								labelAlign: 'right',
								blankText:'请选择单位', 
						        emptyText:'请选择单位',
						        allowBlank  : false,//不允许为空  
					            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
								valueField :"key",   
						        displayField: "value",    
								name : 'unit_type',
								store: unitStore
						});
						var storeSampleCall=Ext.create('Ext.data.Store',{
						          model:'model',
						          proxy:{
						        	type: 'ajax',
						            actionMethods:{read:'POST'},
						            url:'judicial/dicvalues/getSampleCall.do',
						            reader:{
						              type:'json',
						              root:'data'
						            }
						          },
						          autoLoad:true,
						          remoteSort:true						
						        }
						      );
						var typeid = Ext.create('Ext.form.ComboBox', {
							fieldLabel: "单双亲<span style='color:red'>*</span>",
		                    labelWidth : 80,
		                    editable:false,
		                    triggerAction: 'all',
		                    displayField: 'Name',
		                    labelAlign: 'right',
		                    columnWidth : .45,
		                    value:1,
		                    valueField: 'Code',
		                    store: new Ext.data.ArrayStore({
		                        fields: ['Name', 'Code'],
		                        data: [['单亲', 1], ['双亲', 2]]
		                    })          ,
		                    mode: 'local',
		                    //typeAhead: true,
		                    name: 'typeid'
						});
						var per_num=Ext.create('Ext.form.field.Number',{
						    fieldLabel: "样本数<span style='color:red'>*</span>",
						    name: 'per_num',
						    labelWidth : 80,
						    value: 1,
						    maxValue: 9999,
						    minValue: 1    ,
						    columnWidth : .45,
						    labelAlign: 'right',
						    allowBlank:false
						});
						var receiver=Ext.create('Ext.form.ComboBox', {
							columnWidth : .45,
							fieldLabel : '归属人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
							labelWidth : 80,
							labelAlign : 'right',
							name : 'receiver_id',
							emptyText:'检索方式(姓名首字母)：如小红(xh)',
							store : Ext.create("Ext.data.Store",{
								 fields:[
						                  {name:'key',mapping:'key',type:'string'},
						                  {name:'value',mapping:'value',type:'string'},
						                  {name:'name',mapping:'name',type:'string'},
						                  {name:'id',mapping:'id',type:'string'},
						          ],
								pageSize : 10,
								// autoLoad: true,
								proxy : {
									type : "ajax",
									url:"judicial/dicvalues/getUpcUsers.do",
									reader : {
										type : "json"
									}
								}
							}),
							displayField : 'name',
							valueField : 'id',
							allowBlank  : false,//不允许为空  
			                blankText   : "不能为空",//错误提示信息，默认为This field is required!  
							forceSelection: true,
							typeAhead : false,
							hideTrigger : true,
							minChars : 2,
							matchFieldWidth : true,
							listConfig : {
								loadingText : '正在查找...',
								emptyText : '没有找到匹配的数据'
							}
						 });
						var area=Ext.create('Ext.form.ComboBox',{
								columnWidth : .45,
								fieldLabel : '身份证地址',
								labelWidth : 80,
								queryMode: 'remote',
								labelAlign : 'right',
								name : 'area',
								emptyText:'检索方式：如朝阳区(cy)',
								store : Ext.create("Ext.data.Store",{
									 fields:[
							                  {name:'key',mapping:'key',type:'string'},
							                  {name:'value',mapping:'value',type:'string'},
							                  {name:'name',mapping:'name',type:'string'},
							                  {name:'id',mapping:'id',type:'string'},
							          ],
									pageSize : 10,
									// autoLoad: true,
									proxy : {
										type : "ajax",
										url:"judicial/dicvalues/getAreaInfo.do",
										reader : {
											type : "json"
										}
									}
								}),
								displayField : 'value',
								valueField : 'id',
								forceSelection: true,
								typeAhead : false,
								hideTrigger : true,
								minChars : 2,
								matchFieldWidth : true,
								listConfig : {
									loadingText : '正在查找...',
									emptyText : '没有找到匹配的数据'
								}
//								fieldLabel : '身份证地址',
//								queryMode: 'remote',   
//						        forceSelection: true,  
//								labelWidth : 80,
//								columnWidth : 0.45,
//								labelAlign: 'right',
//								blankText:'不能为空',   
//						        emptyText:'检索方式：如朝阳区(cy)',  
//								name : 'area',
//								valueField :"id",   
//						        displayField: "value",   
//								store: areaStore,
//								listeners: {
//									'render' : function(cmp) { 
//						                cmp.getEl().on('keyup', function(e) { 
//						                    if (e.getKey() != e.ENTER) { 
//						            		 Ext.apply(area.store.proxy.params,{initials:area.getRawValue()});
//						            		 area.store.load();
//						                    } 
//						                }); 
//						            },
//						         'focus':function(){
//						            		 Ext.apply(area.store.proxy.params,{initials:""});
//						            		 area.store.load();
//						            		 area.expand();
//					               }  
//							    }
					          });
						var modeltype=Ext.create('Ext.form.ComboBox', 
								{
									columnWidth : .45,
									xtype : "combo",
									fieldLabel : "模板类型<span style='color:red'>*</span>",
									mode: 'local',   
									labelWidth : 80,
									editable:false,
									labelAlign: 'right',
									blankText:'请选择模板',   
							        emptyText:'请选择模板',  
							        allowBlank  : false,//不允许为空  
						            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
									valueField :"key",   
							        displayField: "value", 
									name : 'report_model',
									store: storeModel
						   });
							var case_type=new Ext.form.field.ComboBox({
			                    fieldLabel: "案例类型<span style='color:red'>*</span>",
			                    columnWidth : .45,
			                    labelWidth : 80,
			                    editable:false,
			                    triggerAction: 'all',
			                    displayField: 'Name',
			                    labelAlign: 'right',
			                    blankText:'请选择类型',   
						        emptyText:'请选择类型',  
			                    allowBlank  : false,//不允许为空  
					            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
								valueField :"key",   
						        displayField: "value", 
								store: caseTypeModel,
			                    name: 'case_type'
			                });	
						me.items = [{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [ {
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign: 'right',
									labelWidth : 80,
									fieldLabel : "案例条形码<span style='color:red'>*</span>",
									allowBlank  : false,//不允许为空  
						            blankText   : "不能为空",//错误提示信息，默认为This field is required! 
						            maxLength :50,
									name : 'case_code',
									validator : function(value){
	        			            	var result=false;
	        			            	Ext.Ajax.request({  
	        			    				url:"judicial/register/exsitCaseCode.do", 
	        			    				method: "POST",
	        			    				async : false,
	        			    				headers: { 'Content-Type': 'application/json' },
	        			    				jsonData: {case_code:value}, 
	        			    				success: function (response, options) {  
	        			    					response = Ext.JSON.decode(response.responseText); 	
	        			    					if(!response){
	        			    						result= "此条形码已存在";
	        			    					}else{
	        			    						result= true;
	        			    					}
	        			    				},  
	        			    				failure: function () {
	        			    					Ext.Msg.alert("提示", "网络异常<br>请联系管理员!");
	        			    				}
	        			    	      	});
	        			            	return result;
	        			          }, 
								}, new Ext.form.field.ComboBox({
				                    fieldLabel: '紧急程度',
				                    columnWidth : .45,
				                    labelWidth : 80,
				                    editable:false,
				                    triggerAction: 'all',
				                    displayField: 'Name',
				                    labelAlign: 'right',
				                    value:0,
				                    valueField: 'Code',
				                    store: new Ext.data.ArrayStore({
				                        fields: ['Name', 'Code'],
				                        data: [['普通', 0], ['紧急', 1]]
				                    })          ,
				                    mode: 'local',
				                    //typeAhead: true,
				                    name: 'urgent_state'
				                })]
							},
							{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ receiver,
								area]
							}, 
							{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
   							    items : [
											{
												// 该列在整行中所占的百分比
												columnWidth : .45,
												xtype : "textfield",
												labelAlign: 'right',
												fieldLabel : '采样人员',
												labelWidth : 80,
											    maxLength :50,
												name : 'sample_in_per',
											}, unit_type
   							             ]
							},
							{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
   							    items : [
											{
												// 该列在整行中所占的百分比
												columnWidth : .45,
												xtype : "textfield",
												labelAlign: 'right',
												fieldLabel : '电话号码',
												labelWidth : 80,
											    maxLength :50,
												name : 'phone',
											},
											modeltype
   							             ]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [ {
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign: 'right',
									labelWidth : 80,
									fieldLabel : '委托人',
						            maxLength :50,
									name : 'client',
								},{
									xtype : 'datefield',
									name : 'accept_time',
									columnWidth : .45,
									labelWidth : 80,
									fieldLabel : "受理时间<span style='color:red'>*</span>",
									labelAlign : 'right',
									readOnly:true,
									format : 'Y-m-d',
									allowBlank  : false,//不允许为空 
									maxValue : new Date(),
									value : Ext.Date.add(
											new Date(),
											Ext.Date.DAY),
								}]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [case_type, new Ext.form.field.ComboBox({
				                    fieldLabel: "亲属关系<span style='color:red'>*</span>",
				                    columnWidth : .45,
				                    labelWidth : 80,
				                    editable:false,
				                    triggerAction: 'all',
				                    displayField: 'Name',
				                    labelAlign: 'right',
				                    value:0,
				                    valueField: 'Code',
				                    store: new Ext.data.ArrayStore({
				                        fields: ['Name', 'Code'],
				                        data: [['亲子', 0], ['亲缘', 1],['同胞',2]]
				                    })          ,
				                    mode: 'local',
				                    //typeAhead: true,
				                    name: 'sample_relation'
				                })]
							},{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ typeid,
								          per_num]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
									xtype : 'textarea',
									fieldLabel : '备注',
									name : 'remark',
									width : 500,
									maxLength :500,
									columnWidth : 0.9,
									labelWidth : 80,
									labelAlign : 'right'
								}]
							},
						     {
								layout:  'absolute',// 从左往右的布局
								xtype : 'panel',
								border : false,
								items:[{
									    xtype : 'button',
									    text : '添加样本',
									    width:100,
									    x:630,
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
																items:[{
																			columnWidth : .28,
																			xtype : "textfield",
																			fieldLabel : "条形码<span style='color:red'>*</span>",
																		    allowBlank  : false,//不允许为空  
																	        blankText   : "不能为空",//错误提示信息，默认为This field is required!  
																			labelAlign: 'right',
																			maxLength :50,
																			labelWidth : 80,
																			name : 'sample_code',
																			//regex:/^\w+$/,
																			//regexText:'请输入正确条形码',
																		}, {
																			columnWidth : .28,
																			xtype : "textfield",
																			fieldLabel : "用户名<span style='color:red'>*</span>",
																			labelAlign: 'right',
																			maxLength :50,
																		    allowBlank  : false,//不允许为空  
																	        blankText   : "不能为空",//错误提示信息，默认为This field is required!  
																			labelWidth : 80,
																			name : 'sample_username'
																		}, {
																			columnWidth : .28,
																			xtype : "textfield",
																			fieldLabel : '身份证',
																			labelAlign: 'right',
																			maxLength :18,
																			labelWidth : 80,
																			name : 'id_number',
//																			validator : function(value){
//											        			            	var result=false;
//											        			            	if(value!=null&&value!=""){
//											        			            		Ext.Ajax.request({  
//												        			    				url:"judicial/register/verifyId_Number.do", 
//												        			    				method: "POST",
//												        			    				async : false,
//												        			    				headers: { 'Content-Type': 'application/json' },
//												        			    				jsonData: {id_number:value}, 
//												        			    				success: function (response, options) {  
//												        			    					response = Ext.JSON.decode(response.responseText); 	
//												        			    					if(!response){
//												        			    						result= "身份证号码不正确";
//												        			    					}else{
//												        			    						result= true;
//												        			    					}
//												        			    				},  
//												        			    				failure: function () {
//												        			    					Ext.Msg.alert("提示", "网络异常<br>请联系管理员!");
//												        			    				}
//												        			    	      	});
//											        			            	}else{
//											        			            		result=true;
//											        			            	}
//											        			            	return result;
//											        			          }
														              }]
														     },{
																	layout:'column',
																	xtype : 'panel',
																	border:false,
																	items:[{
																				columnWidth : .28,
																				xtype : "combo",
																				fieldLabel : "取样类型<span style='color:red'>*</span>",
																				mode: 'local',   
																				labelWidth : 60,
																				labelAlign: 'right',
																				blankText:'请选择',   
																				editable:false,
																		        emptyText:'请选择',  
																		        allowBlank  : false,//不允许为空  
																	            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
																				valueField :"key",   
																		        displayField: "value",
																		        maxLength :50,
																				labelWidth : 80,
																				store: storeSampleType,
																				name : 'sample_type',
																				value: 'cy01'
																			}, {
																				columnWidth : .28,
																				xtype : "combo",
																				mode: 'local',   
																				labelWidth : 60,
																				labelAlign: 'right',
																				editable:false,
																				blankText:'请选择',   
																		        emptyText:'请选择',  
																		        allowBlank  : false,//不允许为空  
																	            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
																				valueField :"key",   
																		        displayField: "value",    
																				store: storeSampleCall,
																				fieldLabel : "称谓<span style='color:red'>*</span>",
																				maxLength :50,
																				labelWidth : 80,
																				name : 'sample_call'
																			}, {
																				columnWidth : .28,
																				xtype : "textfield",
																				fieldLabel : '出生日期',
																				emptyText:'yyyy-MM-dd',  
																				labelAlign: 'right',
																				maxLength :20,
																				labelWidth : 80,
																				name : 'birth_date',
																				regex: /^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/,
																				regexText:"格式：yyyy-MM-dd！", 
															              },{
																				layout:  'absolute',// 从左往右的布局
																				xtype : 'panel',
																				border : false,
																			    columnWidth : .15,
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
						var id_numbers = values['id_number'];
						var temp='';
						console.log(values);return;
						if(!(sample_str instanceof Array))
						{
							Ext.MessageBox.alert("提示信息","样本填写错误！");
							return;
						}
						if(values['sample_username'] instanceof Array)
						{
							for(var i = 0 ; i < id_numbers.length ; i ++)
							{
								if(id_numbers[i] != '')
								{
									Ext.Ajax.request({
										url : "judicial/register/verifyId_Number.do",
										method : "POST",
										async : false,
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : {
											id_number : id_numbers[i]
										},
										success : function(response,options) {
											response = Ext.JSON.decode(response.responseText);
											if (!response) {
												temp = Number(i)+1;
											}
										},
										failure : function() {
											Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
										}
									});
									if(temp !='')
									{
										Ext.MessageBox.alert("错误信息","第"+temp+"个身份证号码不正确");
										return;
									}
								}
							}
						}else{
							if(id_numbers !=  undefined && id_numbers != '')
							{
								Ext.Ajax.request({
									url : "judicial/register/verifyId_Number.do",
									method : "POST",
									async : false,
									headers : {
										'Content-Type' : 'application/json'
									},
									jsonData : {
										id_number : id_numbers
									},
									success : function(response,options) {
										response = Ext.JSON.decode(response.responseText);
										if (!response) {
											temp = '1';
										}
									},
									failure : function() {
										Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
									}
								});
								if(temp !='')
								{
									Ext.MessageBox.alert("错误信息","身份证号码不正确");
									return;
								}
							}
						}
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
								var id_result=true;
								var sample_call=values['sample_call'];
								var id_number=values['id_number'];
								if(sample_call){
								   if(!((typeof sample_str=='string')&&sample_str.constructor==String)){
										for(var i=0;i<sample_call.length;i++){
											for(var j=0;j<id_number.length;j++){
												if(i==j){
													if(Ext.util.Format.trim(id_number[j])!=''){
														var num=id_number[j].substring(16, id_number[j].length-1);
														if(sample_call[i]=='mother'||sample_call[i]=='daughter'){
															if(num%2!=0){
																id_result=false;
															}
														}else if(sample_call[i]=='father'||sample_call[i]=='son'){
															if(num%2==0){
																id_result=false;
															}
														}
													}
												}
											}
										}
								   }else{
									   if(Ext.util.Format.trim(id_number)!=''){
											var num=id_number.substring(16, id_number.length-1);
											if(sample_call=='mother'||sample_call=='daughter'){
												if(num%2!=0){
													id_result=false;
												}
											}else if(sample_call=='father'||sample_call=='son'){
												if(num%2==0){
													id_result=false;
												}
											}
										}
								   }
								}
								if(!id_result){
									Ext.MessageBox.alert("错误信息", "存在身份证信息不匹配的数据");
									return;
								}
								var father_num=0,mother_num=0;
								if(sample_call){
									if(!((typeof sample_str=='string')&&sample_str.constructor==String)){
										for(var i=0;i<sample_call.length;i++){
											if(sample_call[i]=='father'){
												father_num++;
											}
											if(sample_call[i]=='mother'){
												mother_num++;
											}
										}
									}
								}
//								if(father_num>1||mother_num>1){
//									Ext.MessageBox.alert("错误信息", "家长称谓出现重复");
//									return;
//								}
								Ext.Ajax.request({  
									url:"judicial/register/saveCaseInfo.do", 
									method: "POST",
									headers: { 'Content-Type': 'application/json' },
									jsonData: values, 
									success: function (response, options) {  
										response = Ext.JSON.decode(response.responseText); 
						                 if (response==true) {  
						                 	Ext.MessageBox.alert("提示信息", "添加案例成功");
						                 	me.grid.getStore().load();
						                 	me.up("window").close();
						                 }else { 
						                 	Ext.MessageBox.alert("错误信息", "添加案例失败");
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
