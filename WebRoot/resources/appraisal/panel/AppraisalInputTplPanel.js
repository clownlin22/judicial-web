/**
 * 报告录入界面，右侧面板
 */
var htmlEditor8;
var htmlEditor9;
var htmlEditor10;
Ext.define(
				'Rds.appraisal.panel.AppraisalInputTplPanel',
				{
					extend : 'Ext.panel.Panel',
					config : {
						input : null
					},
					autoScroll : true,
					region : 'east',
					split : true,
					width : 350,
					title : '模版',
					initComponent : function() {
						var me = this;
						var process_check1 = new Ext.form.TextArea( {
							maxLength:'2000',
							height:150,
							maxLengthText:'文章提示信息长度不能超过2000字',
							name:'process_check1',
							id:'process_check1',
							anchor : '100%',
							listeners:{
								"render": function (f) {
									K = KindEditor;
									htmlEditor8 = K.create('#process_check1', {                   	 
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
						var process_check2= {
							xtype : 'fieldset',
							title : '体格检查',
							defaultType : 'textarea',
							layout : 'anchor',
							defaults : {
								anchor : '100%'
							},
							items : [process_check1]
						};
						var analysis_text1 = new Ext.form.TextArea( {
							maxLength:'2000',
							height:150,
							maxLengthText:'文章提示信息长度不能超过2000字',
							name:'analysis_text1',
							id:'analysis_text1',
							anchor : '100%',
							listeners:{
								"render": function (f) {
									K = KindEditor;
									htmlEditor9 = K.create('#analysis_text1', {                   	 
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
						var analysis_text2= {
							xtype : 'fieldset',
							title : '分析说明',
							defaultType : 'textarea',
							layout : 'anchor',
							defaults : {
								anchor : '100%'
							},
							items : [analysis_text1]
						};
						var advice_text1 = new Ext.form.TextArea( {
							maxLength:'2000',
							height:150,
							maxLengthText:'文章提示信息长度不能超过2000字',
							name:'advice_text1',
							id:'advice_text1',
							anchor : '100%',
							listeners:{
								"render": function (f) {
									K = KindEditor;
									htmlEditor10 = K.create('#advice_text1', {                   	 
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
						var advice_text2= {
								xtype : 'fieldset',
								title : '鉴定意见',
								defaultType : 'textarea',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [advice_text1]
						};
						me.tools = [
								{
									type : 'clear',
									tooltip : '清除',
									handler : function() {
										var form = me.down("form");
										form.getForm().reset();
//										me.down("grid").getStore().removeAll();
									}
								},
								{
									type : 'save',
									tooltip : '保存',
									handler : function() {
										var form = me.down("form");
										var values = form.getForm().getValues();
										var reg=new RegExp("<br>","g"); //创建正则RegExp对象    
										var identify_per_name = me.input.down("textfield[name=identify_per_name]").getValue();
										htmlEditor4.html(htmlEditor4.html()+replaceAll(htmlEditor8.html(),"XXX",identify_per_name));
										htmlEditor6.html(htmlEditor6.html()+replaceAll(htmlEditor9.html(),"XXX",identify_per_name));
										htmlEditor7.html(htmlEditor7.html()+replaceAll(htmlEditor10.html(),"XXX",identify_per_name));
//										htmlEditor6.html(htmlEditor6.html()+replaceAll(values.analysis_text1,"XXX",identify_per_name));
//										htmlEditor7.html(htmlEditor7.html()+replaceAll(values.advice_text1,"XXX",identify_per_name));
										
//										var tgjc = me.input.down("textarea[name=process_check]");
//										tgjc.setValue(tgjc.getValue() + replaceAll(values.process_check1,"XXX",identify_per_name).replace(reg,"\n"));
//										
//										var fxsm = me.input.down("textarea[name=analysis_text]");
//										fxsm.setValue(fxsm.getValue() + replaceAll(values.analysis_text1,"XXX",identify_per_name).replace(reg,"\n"));
//										
//										var jdyj = me.input.down("textarea[name=advice_text]");
//										jdyj.setValue(jdyj.getValue() + replaceAll(values.advice_text1,"XXX",identify_per_name).replace(reg,"\n"));
										this.up("panel").close();
//										me.hide();
										// this.setDisabled(true);
									}
								} ];
						me.items = [{
									xtype : 'form',
									bodyPadding : '5 5 0',
									defaults : {
										anchor : '100%'
									},
									items : [
											{
												xtype : 'combo',
												labelWidth : 50,
												name : 'tpl',
												store : Ext.create(
																"Ext.data.Store",
																{
																	fields : [
																			{
																				name : "template_id",
																				type : "string"
																			},{
																				name : "keyword",
																				type : "string"
																			},
																			{
																				name : "process_check",
																				type : "string"
																			},
																			{
																				name : "analysis_text",
																				type : "string"
																			},
																			{
																				name : "advice_text",
																				type : "string"
																			}],
																	pageSize : 10,
// autoLoad: true,
                             proxy: {
																		// type:
																		// "ajax",
																		// actionMethods
																		// : {
																		// read
																		// :
																		// 'POST'
																		// },
																		type : 'jsonajax',
																		actionMethods : {
																			read : 'POST'
																		},
																		url : "appraisal/info/template.do",
																		reader : {
																			type : "json"
																		}
																	}
																}),
												fieldLabel : "关键字",
												displayField : 'keyword',
												typeAhead : false,
												hideLabel : false,
												hideTrigger : true,
												minChars : 1,
												matchFieldWidth : true,
												listConfig : {
													loadingText : '正在查找...',
													emptyText : '没有找到匹配的数据'
												},
												listeners : {
													'select' : function(combo,
															records, eOpts) {
														var form = me.down("form");
														// 选择
														var record = records[0];
														htmlEditor8.html(record.getData().process_check);
														htmlEditor9.html(record.getData().analysis_text);
														htmlEditor10.html(record.getData().advice_text);
//														me.down("htmleditor[name=analysis_text1]").setValue(record.getData().analysis_text);
//														me.down("htmleditor[name=advice_text1]").setValue(record.getData().advice_text);
//														var src = Ext.create('Rds.appraisal.panel.AppraisalTplModel',
//																		form.getForm().getValues());
//														var join = Ext.create('Rds.appraisal.panel.AppraisalTplModel',
//																		record.getData());
//														var f = new ObjectUtil().joinObject(src,join);
//														form.loadRecord(f);
													}
												}

											}, process_check2,analysis_text2,advice_text2 ]
								}
								];
						me.callParent(arguments);
					}
				});

function replaceAll(obj,str1,str2){         
    var result  = obj.replace(eval("/"+str1+"/gi"),str2);        
    return result;  
}   