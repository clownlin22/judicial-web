Ext.define("Rds.appraisal.panel.AppraisalInfoFormPanel",{
					extend : 'Ext.form.Panel',
					layout : 'border',
					buttonAlign : 'right',
					id:'infoForm',
					initComponent : function() {
						var me = this;
						var menu = Ext
								.create(
										'Ext.menu.Menu',
										{
											id : 'appraisalMainMenu',
											style : {
												overflow : 'visible'
											},
											items : [
													{
														text : '另存为模版',
														iconCls : 'Pagesave',
														handler : function() {
															var tgjc = htmlEditor4.html();
															var fxsm =htmlEditor6.html();
															var jdyj =htmlEditor7.html();
															if(tgjc == '' || fxsm == '' || jdyj == '' )
															{
																Ext.MessageBox.alert("提示信息", "模版信息还没填写哦!");
																return;
															}
															var identify_per_name = me
																	.down(
																			"textfield[name=identify_per_name]")
																	.getValue();
															var win = Ext
																	.create(
																			"Ext.window.Window",
																			{
																				title : '另存为模版',
																				height : 600,
																				width : 800,
																				layout : 'border',
																				buttons : [ {
																					text : '保存',
																					handler : function() {
																						var tgjc = panel
																						.down(
																								"htmleditor[name=process_check]")
																						.getValue();
																						var fxsm = panel
																								.down(
																										"htmleditor[name=analysis_text]")
																								.getValue();
																						var jdyj = panel
																								.down(
																										"htmleditor[name=advice_text]")
																								.getValue();
																						if(tgjc == '<br>' || fxsm == '<br>' || jdyj == '<br>' )
																						{
																							Ext.MessageBox.alert("提示信息", "请正确填写完整模版信息!");
																							return;
																						}
																						Ext.MessageBox
																								.prompt(
																										"模版关键字",
																										"请输入模版关键字",
																										function(id,msg) {
																										    panel
																													.down(
																															"textfield[name=keyword]")
																													.setValue(
																															msg);
																											if(msg=='')
																											{
																												Ext.MessageBox.alert("提示信息", "请填写模版关键字!");
																												return;
																											}
																											console.log(panel.getValues());
																											var values = panel.getValues();
																											Ext.Ajax.request({  
																												url:"appraisal/info/saveTemplate.do", 
																												method: "POST",
																												headers: { 'Content-Type': 'application/json' },
																												jsonData: {
																													process_check:replace_html_tags(values.process_check,"<br />",""),
																													analysis_text:replace_html_tags(values.analysis_text,"<br />",""),
																													advice_text:replace_html_tags(values.advice_text,"<br />",""),
																													keyword:msg
																												}, 
																												success: function (response, options) {  
																													response = Ext.JSON.decode(response.responseText); 
																									                 if (response.result == true) {  
																									                 	Ext.MessageBox.alert("提示信息", response.message);
																														win.close();
																									                 }else { 
																									                 	Ext.MessageBox.alert("错误信息", response.message);
																									                 } 
																												},  
																												failure: function () {
																													Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
																												}
																									    	       
																									      	});
																										});

																					}
																				} ]
																			});
//															var process_check = me
//																	.down("textarea[name=process_check]");
//															var analysis_text = me
//																	.down("textarea[name=analysis_text]");
//															var advice_text = me
//																	.down("textarea[name=advice_text]");
															var panel = Ext
																	.create(
																			"Rds.appraisal.panel.AppraisalTplPanel",
																			{
																				region : 'center'
																			});

															win.add(panel);
															win.show();
															panel
																	.down(
																			"htmleditor[name=process_check]")
																	.setValue(replaceAll(htmlEditor4.html(),identify_per_name,"XXX"));
															panel
																	.down(
																			"htmleditor[name=analysis_text]")
																	.setValue(replaceAll(htmlEditor6.html(),identify_per_name,"XXX")
																			);
															panel
																	.down(
																			"htmleditor[name=advice_text]")
																	.setValue(replaceAll(htmlEditor7.html(),identify_per_name,"XXX")
																			);
														}
													},
													{
														text : '保存全部',
														iconCls : 'Scriptsave',
														handler : function() {
															var me = this.up("form");
															var form = me.getForm();
															var values = form.getValues();
															if(!form.isValid()){
																Ext.MessageBox.alert("提示信息", "请正确填写完整表单信息!");
																return;
															}
															if(htmlEditor1.html()=="")
															{
																Ext.MessageBox.alert("提示信息", "病例摘要为什么不填？");
																return;
															}
															if(htmlEditor2.html()=="")
															{
																Ext.MessageBox.alert("提示信息", "案情摘要为什么不填？");
																return;
															}
															if(htmlEditor3.html()=="")
															{
																Ext.MessageBox.alert("提示信息", "检验方法为什么不填？");
																return;
															}
															if(htmlEditor4.html()=="")
															{
																Ext.MessageBox.alert("提示信息", "体格检查为什么不填？");
																return;
															}
															if(htmlEditor6.html()=="")
															{
																Ext.MessageBox.alert("提示信息", "分析说明为什么不填？");
																return;
															}
															if(htmlEditor7.html()=="")
															{
																Ext.MessageBox.alert("提示信息", "鉴定意见为什么不填？");
																return;
															}
															Ext.Ajax.request({  
																url:"appraisal/info/save.do", 
																method: "POST",
																headers: { 'Content-Type': 'application/json' },
																jsonData: {case_abstract:replace_html_tags(htmlEditor1.html(),"<br />","<br>"),
																	sickness_abstract:replace_html_tags(htmlEditor2.html(),"<br />","<br>"),
																	process_method:replace_html_tags(htmlEditor3.html(),"<br />","<br>"),
																	process_check:replace_html_tags(htmlEditor4.html(),"<br />","<br>"),
																	process_read:replace_html_tags(htmlEditor5.html(),"<br />","<br>"),
																	analysis_text:replace_html_tags(htmlEditor6.html(),"<br />","<br>"),
																	advice_text:replace_html_tags(htmlEditor7.html(),"<br />","<br>"),
																	case_id:values.appraisal_case_id,
																	mainExpert:values.mainExpert,
																	expert:values.expert
																}, 
																success: function (response, options) {  
																	response = Ext.JSON.decode(response.responseText); 
													                 if (response.result == true) {  
													                 	Ext.MessageBox.alert("提示信息", response.message);
																		me.close();
													                 }else { 
													                 	Ext.MessageBox.alert("错误信息", response.message);
													                 } 
																},  
																failure: function () {
																	Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
																}
													    	       
													      	});
															
														}
													} ]

										});
						me.items = [ me.centerPanel() ];
						me.tbar = [
								{
									text : "菜单",
									iconCls : 'Folderpage',
									menu : menu
								},
								{
									text : '模版',
									iconCls : 'Diskmagnify',
									handler : function() {
										me.addRight("tpl");
									}
								},
								 {
								 text : '鉴定标准',
								 iconCls : 'Diskedit',
								 handler : function() {
									 me.addRight("judicial");
								 }
								 },
								'历史报告：',
								{
									xtype : 'combo',
									labelWidth : 60,
									hideLabel : true,
									name : 'history',
									emptyText:'委托函号或被鉴定人姓名',
									store : Ext
											.create(
													"Ext.data.Store",
													{
														fields : [ {
															name : "key",
															type : "key"
														}, {
															name : "value",
															type : "value"
														} ],
														pageSize : 10,
														// autoLoad: true,
														proxy : {
															type : "ajax",
															url : "appraisal/info/queryHistory.do",
															reader : {
																type : "json"
															}
														}
													}),
									fieldLabel : "关键字",
									displayField : 'value',
									valueField : 'key',
									typeAhead : false,
									hideTrigger : true,
									minChars : 1,
									matchFieldWidth : true,
									listConfig : {
										loadingText : '正在查找...',
										emptyText : '没有找到匹配的数据'
									}
								}, {
									xtype : 'button',
									text : '导入',
									iconCls : 'Add',
									handler : me.onImport,
									scope : me
								} ];

						me.callParent(arguments);
					},

					onImport : function() {
						var me = this;
						var history = me.down("combo[name=history]");
						if(null ==history.getValue() || ''==history.getValue() )
						{
							Ext.Msg.alert("提示", "请选择历史报告");
							return;
						}
						var name = history.getRawValue().split(';')[1];
						var identify_per_name = me
						.down(
								"textfield[name=identify_per_name]")
						.getValue()
						var values = {case_id:history.getValue() }
						Ext.Ajax.request({  
							url:"appraisal/info/queryHistoryInfo.do", 
							method: "POST",
							headers: { 'Content-Type': 'application/json' },
							jsonData: values, 
							success: function (response, options) {  
								if(""==response.responseText)
								{
									Ext.Msg.alert("提示", "没有该历史报告哦!");
									return;
								}
								response = Ext.JSON.decode(response.responseText); 
//								var reg=new RegExp("<br>","g"); //创建正则RegExp对象    
								htmlEditor1.html(replaceAll(response.case_abstract,name,identify_per_name));
								htmlEditor2.html(replaceAll(response.sickness_abstract,name,identify_per_name));
								htmlEditor3.html(replaceAll(response.process_method,name,identify_per_name));
								htmlEditor4.html(replaceAll(response.process_check,name,identify_per_name));
								htmlEditor5.html(replaceAll(response.process_read,name,identify_per_name));
								htmlEditor6.html(replaceAll(response.analysis_text,name,identify_per_name));
								htmlEditor7.html(replaceAll(response.advice_text,name,identify_per_name));
							},  
							failure: function () {
								Ext.Msg.alert("提示", "处理失败<br>请联系管理员!");
							}
				    	       
				      	});
					},

					tplPanel : function() {
						var me = this;

						return Ext.create(
								"Rds.appraisal.panel.AppraisalInputTplPanel", {
									input : me
								});
					},

					 judicialStandardPanel : function() {
						 var me = this;
						 return Ext.create("Rds.appraisal.panel.AppraisalInputStandardPanel",
						 {
							 input : me,
							 title : '鉴定标准'
						 });
					 },

					addRight : function(type) {
						var me = this;
						var right = me.down("panel[region=east]");
						if (right) {
							me.remove(right);
						}
						 if (type == "judicial") {
							 me.add(me.judicialStandardPanel());
						 } else if (type = 'tpl') {
							me.add(me.tplPanel());
						}

					},

					centerPanel : function() {
						var me = this;
						return {
							xtype : 'panel',
							autoScroll : true,
							region : 'center',
							items : [ me.baseContentPanel(),
									me.abstractPanel(), me.checkProPanel(),
									me.explainPanel(), me.expertPanel(),me.attachmentPanel() ]
						};
					},

					baseContentPanel : function() {
						var me = this;
						var items = [];
						
						var userStore = Ext.create('Ext.data.Store',{
							fields:['userid','username'],
							autoLoad:true,
							proxy:{
								type:'jsonajax',
								actionMethods:{read:'POST'},
								url:'upc/user/queryall.do',
								params:{
								},
								render:{
									type:'json'
								},
								writer: {
									type: 'json'
								}
							}
						});
						var identify_date_end = new Ext.form.DateField({
							name : 'identify_date_end',
							columnWidth : .18,
							fieldLabel : '到',
							labelWidth : 20,
							labelAlign : 'right',
							afterLabelTextTpl : me.required,
							emptyText : '请选择日期',
							format : 'Y-m-d',
							allowBlank : false,
//							maxValue : new Date()
						});
						var identify_per_both = new Ext.form.DateField({
							name : 'identify_per_both',
							columnWidth : .2,
							fieldLabel : '出生年月',
							labelWidth : 70,
							labelAlign : 'right',
							afterLabelTextTpl : me.required,
							emptyText : '请选择日期',
							format : 'Y-m-d',
							allowBlank : false,
							maxValue : new Date()
						});
						var entrust_per = {
							xtype : "textfield",
							labelAlign : 'left',
							labelWidth : 80,
							fieldLabel : '委 托 人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This
							maxLength : 50,
							name : 'entrust_per',
							readOnly:true
							}
						var entrust_num = {
							xtype : "textfield",
							labelAlign : 'left',
							labelWidth : 80,
							style:'margin-top:10px;',
							fieldLabel : '委托函号<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
//							allowBlank : false,// 不允许为空
//							blankText : "不能为空",// 错误提示信息，默认为This
							maxLength : 200,
							name : 'entrust_num',
							readOnly:true
						}
						var entrust_matter = {
							xtype : "textarea",
							labelAlign : 'left',
							labelWidth : 80,
							style:'margin-top:10px;',
							fieldLabel : '委托事项<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
							name : 'entrust_matter',
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This
							readOnly:true
						}
						var identify_stuff ={
							xtype : "textfield",
							labelAlign : 'left',
							afterLabelTextTpl : me.required,
							labelWidth : 80,
							fieldLabel : '鉴定材料<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This
							name : 'identify_stuff',
							readOnly:true
						};
						var accept_date = {
							xtype : "textfield",
							labelAlign : 'left',
							name : 'accept_date',
							fieldLabel : '受理日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
							labelWidth : 80,
							labelAlign : 'left',
							afterLabelTextTpl : me.required,
							allowBlank : false,
							readOnly:true
							};
						var identify_date = {
							xtype : "textfield",
							labelAlign : 'left',
							name : 'identify_date',
							fieldLabel : '鉴定日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
							labelWidth : 80,
							labelAlign : 'left',
							afterLabelTextTpl : me.required,
							allowBlank : false,
							readOnly:true
						};
						var identify_per = {
								xtype : "textfield",
								labelAlign : 'left',
								name : 'identify_per',
								fieldLabel : '被鉴定人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
								labelWidth : 80,
								labelAlign : 'left',
								afterLabelTextTpl : me.required,
								allowBlank : false,
								readOnly:true
						};
						var case_in_person = {
								xtype : "textfield",
								labelAlign : 'left',
								name : 'case_in_person',
								fieldLabel : '归属人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
								labelWidth : 80,
								labelAlign : 'left',
								afterLabelTextTpl : me.required,
								allowBlank : false,
								readOnly:true
						};
						var judgename = {
								xtype : "textfield",
								labelAlign : 'left',
								name : 'judgename',
								fieldLabel : '鉴定机构<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
								labelWidth : 80,
								labelAlign : 'left',
								afterLabelTextTpl : me.required,
								allowBlank : false,
								readOnly:true
						};
						var values = {case_id:case_id_appraisal.split(',')[0] }
						Ext.Ajax.request({  
							url:"appraisal/info/queryBaseInfo.do", 
							method: "POST",
							headers: { 'Content-Type': 'application/json' },
							jsonData: values, 
							success: function (response, options) {  
								response = Ext.JSON.decode(response.responseText); 
								console.log(response)
								me.down("textfield[name=entrust_per]").setValue(response.entrust_per);
								me.down("textfield[name=identify_per_name]").setValue(response.identify_per_name);
								me.down("textfield[name=entrust_num]").setValue(response.entrust_num);
								me.down("textfield[name=entrust_matter]").setValue(response.entrust_matter);
								me.down("textfield[name=identify_stuff]").setValue(response.identify_stuff);
								me.down("textfield[name=accept_date]").setValue(response.accept_date);
								me.down("textfield[name=identify_date]").setValue(response.identify_date_start+"~"+response.identify_date_end);
								me.down("textfield[name=identify_per]").setValue(response.identify_per_name+","+response.identify_per_sex+ ","
										+response.identify_per_both+"生,住址："+response.identify_per_address+",身份证号:"
										+response.identify_per_idcard);
								me.down("textfield[name=case_in_person]").setValue(response.case_in_person);
								me.down("textfield[name=judgename]").setValue(response.judgename);
							},  
							failure: function () {
								Ext.Msg.alert("提示", "处理失败<br>请联系管理员!");
							}
				    	       
				      	});

						return {
							xtype : 'form',
							title : '一、基础内容',
							defaultType : 'textfield',
							bodyPadding : '5 5 0',
							defaults : {
								anchor : '100%'
							},
							items : [{
								xtype : "textfield",
								labelAlign : 'left',
								labelWidth : 80,
								fieldLabel : 'identify_per_name',
								id:'identify_per_name',
								allowBlank : false,// 不允许为空
								blankText : "不能为空",// 错误提示信息，默认为This
								maxLength : 200,
								name : 'identify_per_name',
								hidden:true
							},{
								xtype : "textfield",
								labelAlign : 'left',
								labelWidth : 80,
								fieldLabel : 'case_id',
								id:'appraisal_case_id',
								allowBlank : false,// 不允许为空
								blankText : "不能为空",// 错误提示信息，默认为This
								maxLength : 200,
								name : 'appraisal_case_id',
								value:case_id_appraisal.split(',')[0],
								hidden:true
							},
							entrust_per,entrust_num,entrust_matter,
							accept_date,identify_stuff,identify_date,
							identify_per,case_in_person,judgename,{
								xtype : 'fieldset',
								title : '鉴定人',
								name : 'identify_per',
								style:'margin-top:10px;',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [ 
{
	xtype : 'form',
	region : 'center',
	name : 'data',
	style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
	labelAlign : "right",
	bodyPadding : 10,
	defaults : {
		anchor : '100%',
		labelWidth : 70
	},
	border : false,
	autoHeight : true,
	items : [
	         {
				xtype: 'combo',
				autoSelect : true,
				labelWidth : 70,
				fieldLabel:'主鉴定人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
		        name:'mainExpert',
		        emptyText : "请选择",
		        allowBlank:false,
		        triggerAction: 'all',
		        queryMode: 'local', 
		        selectOnTab: true,
		        editable:false,
		        store: userStore,
		        fieldStyle: me.fieldStyle,
		        displayField:'username',
		        valueField:'userid',
		        listClass: 'x-combo-list-small'
			},{
				xtype: 'combo',
				autoSelect : true,
				labelWidth : 70,
				fieldLabel:'鉴定人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
		        name:'expert',
		        emptyText : "请选择",
		        allowBlank:false,
		        editable:false,
		        triggerAction: 'all',
		        queryMode: 'local', 
		        selectOnTab: true,
		        store: userStore,
		        fieldStyle: me.fieldStyle,
		        displayField:'username',
		        valueField:'userid',
		        listClass: 'x-combo-list-small'
			},
			{
				xtype : 'panel',
				layout : 'absolute',
				border : false,
				items : [{
					xtype : 'button',
					text : '增加鉴定人',
					width : 100,
					x : 0,
					handler : function() {
						var me = this.up('form');
						me.add({
							xtype : 'form',
							style : 'margin-top:5px;',
							layout : 'column',
							border:false,
							items : [
									{
										xtype: 'combo',
										autoSelect : true,
										allowBlank:false,
										labelWidth : 70,
										fieldLabel:'鉴定人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
								        name:'expert',
								        emptyText : "请选择",
								        triggerAction: 'all',
								        editable:false,
								        queryMode: 'local', 
								        selectOnTab: true,
								        store: userStore,
								        fieldStyle: me.fieldStyle,
								        displayField:'username',
								        valueField:'userid',
								        listClass: 'x-combo-list-small'
									}, {
										xtype : 'button',
										style : 'margin-left:15px;',
										text : '删除',
										handler : function() {
											var me = this
													.up("form");
											me.disable(true);
											me.up("form")
													.remove(me);
										}
									}]
								});
					}
				}]

			}]
}]
								
							}]
						};
					},
					
					abstractPanel : function() {
						var me = this;
						var case_abstract = new Ext.form.TextArea( {
							maxLength:'2000',
							height:150,
							maxLengthText:'文章提示信息长度不能超过2000字',
							name:'case_abstract',
							id:'case_abstract',
							anchor : '100%',
							listeners:{
							  "render": function (f) {
				                   K = KindEditor;
				                   htmlEditor1 = K.create('#case_abstract', {                   	 
//				                        uploadJson: './kindeditor/upload_json.jsp',//路径自己改一下
//				                        fileManagerJson : './kindeditor/file_manager_json.jsp',//路径自己改一下
				                        height: 300,
				                        width: '100%',
				                         // fontSizeTable:['21px','20px','17px','18px','16px','14px','12px'],
				                        resizeType : 1,
										allowPreviewEmoticons : true,
										allowImageUpload : true
				                    });
				                }
							}
						});
						var sickness_abstract = new Ext.form.TextArea( {
							maxLength:'2000',
							height:150,
							maxLengthText:'文章提示信息长度不能超过2000字',
							name:'process_method',
							id:'sickness_abstract',
							anchor : '100%',
							listeners:{
								"render": function (f) {
									K = KindEditor;
									htmlEditor2 = K.create('#sickness_abstract', {                   	 
										uploadJson: './kindeditor/upload_json.jsp',//路径自己改一下
										fileManagerJson : './kindeditor/file_manager_json.jsp',//路径自己改一下
										height: 300,
				                        width: '100%',
										// fontSizeTable:['21px','20px','17px','18px','16px','14px','12px'],
										resizeType : 1,
										allowPreviewEmoticons : true,
										allowImageUpload : true
									});
								}
							}
						});
						return {
							xtype : 'panel',
							title : '二、检案摘要',
							bodyPadding : '5 5 0',
							fieldDefaults : {
								msgTarget : 'side',
								labelWidth : 75
							},
							defaults : {
								anchor : '100%'
							},
							items : [ {
								xtype : 'fieldset',
								title : '（一）案情摘要',
								defaultType : 'textarea',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [ case_abstract,
//								          {
//									fieldLabel : '案情摘要',
//									afterLabelTextTpl : me.required,
//									grow : true,
//									hideLabel : true,
//									height:100,
//									name : 'case_abstract',
//									allowBlank : false,
//									maxLength: 2000
//								} 
								          
								          ]
							}, {
								xtype : 'fieldset',
								title : '（二）病历摘要',
								defaultType : 'textarea',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [sickness_abstract
//								         {
//									fieldLabel : '病历摘要',
//									afterLabelTextTpl : me.required,
//									grow : true,
//									hideLabel : true,
//									name : 'sickness_abstract',
//									allowBlank : false,
//									height:100,
//									maxLength: 2000
//								}
								]
							} ]
						};
					},

					checkProPanel : function() {
						var me = this;
						var process_method = new Ext.form.TextArea( {
							maxLength:'2000',
							height:150,
							maxLengthText:'文章提示信息长度不能超过2000字',
							name:'process_method',
							id:'process_method',
							anchor : '100%',
							listeners:{
								"render": function (f) {
									K = KindEditor;
									htmlEditor3 = K.create('#process_method', {                   	 
										uploadJson: './kindeditor/upload_json.jsp',//路径自己改一下
										fileManagerJson : './kindeditor/file_manager_json.jsp',//路径自己改一下
										height: 300,
				                        width: '100%',
										// fontSizeTable:['21px','20px','17px','18px','16px','14px','12px'],
										resizeType : 1,
										allowPreviewEmoticons : true,
										allowImageUpload : true
									});
								}
							}
						});
						var process_check = new Ext.form.TextArea( {
							maxLength:'2000',
							height:150,
							maxLengthText:'文章提示信息长度不能超过2000字',
							name:'process_check',
							id:'process_check',
							anchor : '100%',
							listeners:{
								"render": function (f) {
									K = KindEditor;
									htmlEditor4 = K.create('#process_check', {                   	 
										uploadJson: './kindeditor/upload_json.jsp',//路径自己改一下
										fileManagerJson : './kindeditor/file_manager_json.jsp',//路径自己改一下
										height: 300,
				                        width: '100%',
										// fontSizeTable:['21px','20px','17px','18px','16px','14px','12px'],
										resizeType : 1,
										allowPreviewEmoticons : true,
										allowImageUpload : true
									});
								}
							}
						});
						var process_read = new Ext.form.TextArea( {
							maxLength:'2000',
							height:150,
							maxLengthText:'文章提示信息长度不能超过2000字',
							name:'process_read',
							id:'process_read',
							anchor : '100%',
							listeners:{
								"render": function (f) {
									K = KindEditor;
									htmlEditor5 = K.create('#process_read', {                   	 
//										uploadJson: './kindeditor/upload_json.jsp',//路径自己改一下
//										fileManagerJson : './kindeditor/file_manager_json.jsp',//路径自己改一下
										height: 300,
				                        width: '100%',
										// fontSizeTable:['21px','20px','17px','18px','16px','14px','12px'],
										resizeType : 1,
										allowPreviewEmoticons : true,
										allowImageUpload : true
									});
								}
							}
						});
						return {
							xtype : 'panel',
							title : '三、检验过程',
							titlePosition : 2,
							bodyPadding : '5 5 0',
							fieldDefaults : {
								msgTarget : 'side',
								labelWidth : 75
							},
							defaults : {
								anchor : '100%'
							},
							items : [ {
								xtype : 'fieldset',
								title : '（一）检验方法',
								defaultType : 'textarea',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [ process_method
//								          {
//									fieldLabel : '检验方法',
//									afterLabelTextTpl : me.required,
//									grow : true,
//									hideLabel : true,
//									name : 'process_method',
//									allowBlank : false,
//									height:100,
//									maxLength: 2000
//								}
								]
							}, {
								xtype : 'fieldset',
								title : '（二）体格检查（2014年8月21日）',
								defaultType : 'textarea',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [process_check
//								         {
//									fieldLabel : '体格检查',
//									afterLabelTextTpl : me.required,
//									grow : true,
//									hideLabel : true,
//									name : 'process_check',
//									allowBlank : false,
//									height:100,
//									maxLength: 2000
//								} 
								]
							}, {
								xtype : 'fieldset',
								title : '（三）阅片所见',
								 checkboxToggle : true,
								 collapsed : true,
								defaultType : 'textarea',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [process_read
//								         {
//									fieldLabel : '阅片所见',
//									afterLabelTextTpl : me.required,
//									grow : true,
//									hideLabel : true,
//									name : 'process_read',
////									allowBlank : false,
//									height:100,
//									maxLength: 2000
//								}
								]
							} ]
						};
					},
					explainPanel : function() {
						var me = this;
						var analysis_text = new Ext.form.TextArea( {
							maxLength:'2000',
							height:150,
							maxLengthText:'文章提示信息长度不能超过2000字',
							name:'analysis_text',
							id:'analysis_text',
							anchor : '100%',
							listeners:{
								"render": function (f) {
									K = KindEditor;
									htmlEditor6 = K.create('#analysis_text', {                   	 
//										uploadJson: './kindeditor/upload_json.jsp',//路径自己改一下
//										fileManagerJson : './kindeditor/file_manager_json.jsp',//路径自己改一下
										height: 300,
				                        width: '100%',
										// fontSizeTable:['21px','20px','17px','18px','16px','14px','12px'],
										resizeType : 1,
										allowPreviewEmoticons : true,
										allowImageUpload : true
									});
								}
							}
						});
						return {
							xtype : 'panel',
							title : '四、分析说明',
							bodyPadding : '5 5 0',
							fieldDefaults : {
								msgTarget : 'side',
								labelWidth : 75
							},
							defaults : {
								anchor : '100%'
							},
							items : [ {
								xtype : 'fieldset',
								title : '（一）分析说明',
								defaultType : 'textarea',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [analysis_text
//								         {
//									fieldLabel : '分析说明',
//									afterLabelTextTpl : me.required,
//									grow : true,
//									hideLabel : true,
//									name : 'analysis_text',
//									allowBlank : false,
//									height:100,
//									maxLength: 2000
//								} 
								]
							} ]
						};
					},
					attachmentPanel : function() {
						var me = this;
						return {
							xtype : 'panel',
							title : '六、附录照片',
							bodyPadding : '5 5 0',
							fieldDefaults : {
								msgTarget : 'side',
								labelWidth : 75
							},
							defaults : {
								anchor : '100%'
							},
							items : [{
								xtype : 'form',
								region : 'center',
								name : 'data',
								labelAlign : "left",
								bodyPadding : 10,
								defaults : {
									anchor : '100%',
									labelWidth : 70
								},
								border : false,
								autoHeight : true,
								items : [{
											xtype : 'panel',
											layout : 'absolute',
											border : false,
											items : [{
												xtype : 'button',
												text : '添加照片',
												width : 100,
												id:'insertPic',
												x : 0,
												handler : function() {
													var me = this.up("gridpanel");
													case_id_appraisal=Ext.getCmp('appraisal_case_id').getValue();
													var form = Ext.create("Rds.appraisal.panel.AppraisalAttachmentFormPanel",
													{
														region : "center",
														grid : me
													});
													var win = Ext.create("Ext.window.Window", {
																title : '上传新文件',
																width : 1000,
																iconCls : 'Add',
																height : 500,
																layout : 'border',
																modal : true,
																items : [form],
																listeners : {
																	close : {
																		fn : function() {
																			if('' != Ext.getCmp('appraisal_case_id').getValue())
																			{
																				Ext.getCmp("insertPic").setText("重新上传");
																				Ext.getCmp("filenames").setValue(case_id_appraisal.split(',')[1]);
																				Ext.getCmp("filenames").show();
																				Ext.getCmp("filenames").setReadOnly(true);
//																				Ext.getCmp("case_id").setValue(case_id_appraisal.split(',')[0]);
																			}
																		}
																	}
																}
															});
													win.show();
													
												}
											}]

										}]
							},{
								xtype : "textfield",
								labelAlign : 'left',
								labelWidth : 80,
								fieldLabel : '文件目录',
								maxLength : 200,
								name : 'filenames',
								id:'filenames',
								value:'',
								hidden:true
							},{
								xtype : "textfield",
								labelAlign : 'left',
								labelWidth : 80,
								fieldLabel : 'case_id',
								maxLength : 200,
								name : 'case_id',
								id:'case_id',
								value:'',
								hidden:true
							}]
						};
					},
					expertPanel : function() {
						var me = this;
						var advice_text = new Ext.form.TextArea( {
							maxLength:'2000',
							height:150,
							maxLengthText:'文章提示信息长度不能超过2000字',
							name:'advice_text',
							id:'advice_text',
							anchor : '100%',
							listeners:{
								"render": function (f) {
									K = KindEditor;
									htmlEditor7 = K.create('#advice_text', {                   	 
//										uploadJson: './kindeditor/upload_json.jsp',//路径自己改一下
//										fileManagerJson : './kindeditor/file_manager_json.jsp',//路径自己改一下
										height: 300,
				                        width: '100%',
										// fontSizeTable:['21px','20px','17px','18px','16px','14px','12px'],
										resizeType : 1,
										allowPreviewEmoticons : true,
										allowImageUpload : true
									});
								}
							}
						});
						return {
							xtype : 'panel',
							title : '五、鉴定意见',
							bodyPadding : '5 5 0',
							fieldDefaults : {
								msgTarget : 'side',
								labelWidth : 75
							},
							defaults : {
								anchor : '100%'
							},
							items : [ {
								xtype : 'fieldset',
								title : '（一）鉴定意见',
								defaultType : 'textarea',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [ advice_text
//								          {
//									fieldLabel : '鉴定意见',
//									afterLabelTextTpl : me.required,
//									grow : true,
//									hideLabel : true,
//									name : 'advice_text',
//									allowBlank : false,
//									height:100,
//									maxLength: 2000
//								}
								]
							} ]
						};
						
						
					}
				});

function replaceAll(obj,str1,str2){         
    var result  = obj.replace(eval("/"+str1+"/gi"),str2);        
    return result;  
}   
function replace_html_tags(str,reallyDo,replaceWith) {
	str = str.replace(/\n|\r\n|\t/g,"");
	var e=new RegExp(reallyDo,"g");
	words = str.replace(e, replaceWith);
	return words;
	} 