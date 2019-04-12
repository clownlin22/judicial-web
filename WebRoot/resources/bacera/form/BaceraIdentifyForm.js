Ext.define('Rds.bacera.form.BaceraIdentifyForm',{
					extend : 'Ext.form.Panel',
					initComponent : function() {
						var me = this;
						var typeid = Ext.create('Ext.form.ComboBox', {
							fieldLabel: '单双亲<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
						    labelWidth : 70,
						    editable:false,
						    triggerAction: 'all',
						    displayField: 'Name',
						    labelAlign: 'left',
						    columnWidth : .5,
						    value:1,
						    valueField: 'Code',
						    store: new Ext.data.ArrayStore({
						        fields: ['Name', 'Code'],
						        data: [['无','0'],['单亲', 1], ['双亲', 2]]
						    })          ,
						    mode: 'local',
						    name: 'typeid'
						});
						var case_type=new Ext.form.field.ComboBox({
		                    fieldLabel: '鉴定类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
		                    columnWidth : .50,
		                    labelWidth : 70,
		                    editable:false,
		                    style:'margin-left:5px;',
		                    triggerAction: 'all',
		                    displayField: 'Name',
		                    labelAlign: 'left',
		                    blankText:'请选择类型',   
					        emptyText:'请选择类型',  
		                    allowBlank  : false,//不允许为空  
				            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
							valueField :"key",   
					        displayField: "value", 
							store: caseTypeModel,
		                    name: 'case_type'
		                });	
						me.items = [ {
							xtype : 'form',
							style : 'margin-left:5px;margin-top:5px;margin-bottom:5px;',
							bodyPadding : 10,
							defaults : {
								anchor : '100%',
								labelWidth : 80,
								labelSeparator: "：",
								labelAlign: 'right'
							},
							border : false,
							autoHeight : true,
							items : [ {
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [ {
									// 该列在整行中所占的百分比
									columnWidth : .50,
									xtype : "textfield",
									labelAlign: 'left',
									labelWidth : 70,
									fieldLabel : '案例条码<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
									allowBlank  : false,//不允许为空  
						            blankText   : "不能为空",//错误提示信息，默认为This field is required! 
						            maxLength :50,
									name : 'case_code'
								},{
   										xtype : "textfield",
   										fieldLabel : '身份证地址',
   										queryMode: 'remote',   
   								        forceSelection: true,  
   								        labelWidth : 70,
										style:'margin-left:5px;',
   										columnWidth : 0.50,
   										labelAlign: 'left',
   										blankText:'不能为空',  
   										name : 'case_areacode',
   										maxLength :50,
   							          }]
							},
							{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ {
									xtype : 'combo',
									columnWidth : .50,
									fieldLabel : '案例归属<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
									labelWidth : 70,
									name : 'receiver_id',
									emptyText:'(姓名/地区首字母)：如小红(xh)',
									store :Ext.create("Ext.data.Store",
											{
										 fields:[
								                  {name:'id',mapping:'id',type:'string'},
								                  {name:'ascription',mapping:'ascription',type:'string'}
								          ],
										pageSize : 10,
										autoLoad: false,
										proxy : {
											type : "ajax",
											url:"finance/chargeStandard/getAscriptionPer.do?id="+ownpersonTemp,
											reader : {
												type : "json"
											}
										}
									}),
									displayField : 'ascription',
									valueField : 'id',
									typeAhead : false,
									hideTrigger : true,
									forceSelection: true,
									allowBlank:false, //不允许为空
									minChars : 2,
									matchFieldWidth : true,
									listConfig : {
										loadingText : '正在查找...',
										emptyText : '没有找到匹配的数据'
									},
								listeners: {
									'afterrender':function(){
										if("" != ownpersonTemp)
										{
											this.store.load();
										}
									}
									}
								}]
							}, {
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
									// 该列在整行中所占的百分比
									columnWidth : .50,
									xtype : "textfield",
									labelAlign: 'left',
									labelWidth : 70,
									fieldLabel : '委托人',
						            maxLength :50,
									name : 'client',
								},{
										xtype : "textfield",
   										fieldLabel : '电话号码',
   										labelWidth : 70,
   										style:'margin-left:5px;',
   										columnWidth : 0.50,
   										labelAlign: 'left',
   										blankText:'不能为空',  
   										name : 'phone',
   										maxLength :20,
   							          }]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
									xtype : 'datefield',
									name : 'accept_time',
									columnWidth : .50,
									labelWidth : 70,
									fieldLabel : '受理时间<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
									labelAlign : 'left',
									format : 'Y-m-d',
									allowBlank  : false,//不允许为空 
									value:new Date()
								},
								new Ext.form.field.ComboBox(
										{
											fieldLabel : '案例类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
											width : 155,
											labelWidth : 70,
											editable : false,
											triggerAction : 'all',
											displayField : 'Name',
											labelAlign : 'left',
											valueField : 'Code',
											style:'margin-left:5px;',
											columnWidth : 0.50,
											forceSelection:true,
											allowBlank  : false,//不允许为空  
								            blankText   : "不能为空",//错误提示信息，默认为This field is required! 
								            maxLength :50,
											store : new Ext.data.ArrayStore(
												{
													fields : [
															'Name',
															'Code' ],
													data : [
															[
																	'亲子鉴定-宿迁子渊 ',
																	'亲子鉴定-宿迁子渊 ' ],
															[
																	'亲子鉴定-东南',
																	'亲子鉴定-东南' ],
															[
																	'亲子鉴定-苏博医学',
																	'亲子鉴定-苏博医学' ],
															[
																	'河南唯实',
																	'河南唯实' ],
															[
																	'广西公众司法',
																	'广西公众司法' ],
															[
																	'安徽同德',
																	'安徽同德' ],
															[
																	'亲子鉴定-安徽龙图',
																	'亲子鉴定-安徽龙图' ],
															[
																	'亲子鉴定-求实',
																	'亲子鉴定-求实' ],
															[
																	'亲子鉴定-十堰',
																	'亲子鉴定-十堰' ],
															[
																	'亲子鉴定-福建正泰',
																	'亲子鉴定-福建正泰' ],
															[
																	'亲子鉴定-杭州千麦',
																	'亲子鉴定-杭州千麦' ],
															[
																	'亲子鉴定-南天',
																	'亲子鉴定-南天' ],
															[
																	'亲子鉴定-商丘',
																	'亲子鉴定-商丘' ],
															[
																	'亲子鉴定-中信',
																	'亲子鉴定-中信' ]
															]
													}),
											mode : 'local',
											name : 'type'
										})
								]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
									xtype : 'datefield',
									name : 'entrustment_time',
									columnWidth : .50,
									labelWidth : 70,
									fieldLabel : '委托日期',
									labelAlign : 'left',
									format : 'Y-m-d',
								},
								{
									    xtype : "textfield",
										fieldLabel : '委托事项',
										labelWidth : 70,
										style:'margin-left:5px;',
										columnWidth : 0.50,
										labelAlign: 'left',
										name : 'entrustment_matter',
										maxLength :50,
							          }
								]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [typeid,case_type]
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
									columnWidth : 1,
									labelWidth : 70,
									labelAlign : 'left'
								}]
							}, {
								layout:  'absolute',// 从左往右的布局
								xtype : 'panel',
								style : 'margin-top:15px;margin-bottom:5px;',
								border : false,
								items:[{
									    xtype : 'button',
									    text : '添加样本',
									    width:100,
									    x:0,
									    listeners:{
									        click:function(){
									        	var me = this.up("form");
									        	me.add({
													xtype : 'form',
													style : 'margin-left:5px;margin-top:5px;margin-bottom:5px;',
													bodyPadding : 10,
													defaults : {
														anchor : '100%',
														labelWidth : 70,
														labelSeparator: "：",
														labelAlign: 'left'
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
																			columnWidth : .26,
																			xtype : "textfield",
																			fieldLabel : '条形码<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
																		    allowBlank  : false,//不允许为空  
																	        blankText   : "不能为空",//错误提示信息，默认为This field is required!  
																			labelAlign: 'left',
																			maxLength :50,
																			labelWidth : 65,
																			name : 'sample_code',
																		}, {
																			columnWidth : .26,
																			xtype : "textfield",
																			fieldLabel : '用户名<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
																			style:'margin-left:5px;',
																			labelAlign: 'left',
																			maxLength :50,
																		    allowBlank  : false,//不允许为空  
																	        blankText   : "不能为空",//错误提示信息，默认为This field is required!  
																			labelWidth : 60,
																			name : 'sample_username'
																		}, {
																			columnWidth : .33,
																			xtype : "textfield",
																			fieldLabel : '身份证',
																			style:'margin-left:5px;',
																			labelAlign: 'left',
																			maxLength :18,
																			labelWidth : 60,
																			name : 'id_number',
																			regex : /^(\d{18,18}|\d{15,15}|\d{17,17}X)$/,
																			regexText : '输入正确的身份号码'
														              }]
														     },{
																	layout:'column',
																	xtype:'panel',
																	border:false,
																	style : 'margin-top:5px;',
																	items:[{
																				columnWidth : .26,
																				xtype : "combo",
																				fieldLabel : '取样类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
																				mode: 'local',   
																				labelWidth : 65,
																				labelAlign: 'left',
																				blankText:'请选择',   
																				editable:false,
																		        emptyText:'请选择',  
																		        allowBlank  : false,//不允许为空  
																	            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
																				valueField :"key",   
																		        displayField: "value",
																		        maxLength :50,
																				store: storeSampleType,
																				name : 'sample_type',
																				value:'cy01'
																			}, {
																				columnWidth : .26,
																				xtype : "combo",
																				mode: 'local',   
																				labelWidth : 60,
																				labelAlign: 'left',
																				editable:false,
																				style:'margin-left:5px;',
																				blankText:'请选择',   
																		        emptyText:'请选择',  
																		        allowBlank  : false,//不允许为空  
																	            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
																				valueField :"key",   
																		        displayField: "value",    
																				store: storeSampleCall,
																				fieldLabel : '称谓<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
																				maxLength :50,
																				name : 'sample_call'
																			}, {
																				columnWidth : .33,
																				xtype : "textfield",
																				style:'margin-left:5px;',
																				fieldLabel : '出生日期',
																				emptyText:'yyyy-MM-dd',  
																				labelAlign: 'left',
																				maxLength :20,
																				labelWidth : 60,
																				name : 'birth_date',
																				regex: /^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/,
																				regexText:"格式：yyyy-MM-dd！"
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
							Ext.MessageBox.wait('正在操作','请稍后...');
//							var result=true;
//							if(sample_str){
//									if(!((typeof sample_str=='string')&&sample_str.constructor==String)){
//										for(var i=0;i<sample_str.length;i++){
//											for(var j=0;j<sample_str.length;j++){
//												if(i!=j&&sample_str[i]==sample_str[j]){
//													result=false;
//												}
//											}
//										}
//									}
//							}
//							if(!result){
//									Ext.MessageBox.alert("错误信息", "存在条形码输入一样的数据");
//							}else{
//								var id_result=true;
//								var sample_call=values['sample_call'];
//								var id_number=values['id_number'];
//								if(sample_call){
//								   if(!((typeof sample_str=='string')&&sample_str.constructor==String)){
//										for(var i=0;i<sample_call.length;i++){
//											for(var j=0;j<id_number.length;j++){
//												if(i==j){
//													if(Ext.util.Format.trim(id_number[j])!=''){
//														var num=id_number[j].substring(16, id_number[j].length-1);
//														if(sample_call[i]=='mother'||sample_call[i]=='daughter'){
//															if(num%2!=0){
//																id_result=false;
//															}
//														}else if(sample_call[i]=='father'||sample_call[i]=='son'){
//															if(num%2==0){
//																id_result=false;
//															}
//														}
//													}
//												}
//											}
//										}
//								   }else{
//									   if(Ext.util.Format.trim(id_number)!=''){
//											var num=id_number.substring(16, id_number.length-1);
//											if(sample_call=='mother'||sample_call=='daughter'){
//												if(num%2!=0){
//													id_result=false;
//												}
//											}else if(sample_call=='father'||sample_call=='son'){
//												if(num%2==0){
//													id_result=false;
//												}
//											}
//										}
//								   }
//								}
//								if(!id_result){
//									Ext.MessageBox.alert("错误信息", "存在身份证信息不匹配的数据");
//									return;
//								}
								Ext.Ajax.request({  
									url:"bacera/identify/saveCaseInfo.do", 
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
						                 	Ext.MessageBox.alert("错误信息", "操作失败,请联系管理员；<br>看看是不是案例编号存在了？");
						                 } 
									},  
									failure: function () {
										Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
									}
						      	});
//							}
						}
					},
					onCancel : function() {
						var me = this;
						me.up("window").close();
					}
				});
