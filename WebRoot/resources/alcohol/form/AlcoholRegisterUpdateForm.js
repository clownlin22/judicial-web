var storeModel = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getReportModels.do',
				params : {
					type : 'alcohol',
					receiver_id : ""
				},
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});


var clientStore = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'alcohol/register/getClient.do',
				params : {

                }				,
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		}
);

var clientStore2 = Ext.create('Ext.data.Store', {
	model : 'model',
	proxy : {
		type : 'jsonajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'alcohol/register/getClient2.do',
		params : {
		},
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	autoLoad : true,
	remoteSort : true
});


Ext.define('Rds.alcohol.form.AlcoholRegisterUpdateForm', {
	extend : 'Ext.form.Panel',
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'form',
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
			items : [{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : "hiddenfield",
									name : "case_id"
								}, {
									xtype : "hiddenfield",
									name : "sample_id"
								}, {
									// 该列在整行中所占的百分比
									columnWidth : .5,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '案例条形码<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
									allowBlank : false,// 不允许为空
									blankText : "不能为空",// 错误提示信息，默认为This field
									// is required!
									maxLength : 50,
									name : 'case_code',
									readOnly : true
								},{
									xtype : 'datefield',
									name : 'close_time',
									columnWidth : .5,
									labelWidth : 80,
									fieldLabel : '打印日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
									labelAlign : 'right',
									format : 'Y-m-d',
									allowBlank : false,// 不允许为空
//									maxValue : new Date(),
									value : Ext.Date.add(new Date(), Ext.Date.DAY)

								}]
					}, {
						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [{
									xtype : 'datefield',
									name : 'client_time',
									columnWidth : .5,
									labelWidth : 80,
									fieldLabel : '委托日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
									labelAlign : 'right',
									format : 'Y-m-d',
									allowBlank : false,// 不允许为空
									maxValue : new Date(),
									value : Ext.Date.add(new Date(),
											Ext.Date.DAY)

								}, {
									xtype : 'datefield',
									name : 'accept_time',
									columnWidth : .5,
									labelWidth : 80,
									fieldLabel : '受理日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
									labelAlign : 'right',
									format : 'Y-m-d',
									allowBlank : false,// 不允许为空
									maxValue : new Date(),
									value : Ext.Date.add(new Date(),
											Ext.Date.DAY)

								}]
					}, {
						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [
								// {
								// // 该列在整行中所占的百分比
								// columnWidth : .5,
								// xtype : "textfield",
								// labelAlign : 'right',
								// fieldLabel : '委托人',
								// allowBlank : false,// 不允许为空
								// labelWidth : 80,
								// maxLength : 50,
								// name : 'client'
								// },
								{
							columnWidth : .5,
							xtype : "combo",
							fieldLabel : '委托人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
							triggerAction : 'all',
							queryMode : 'local',
							labelWidth : 80,
							editable : true,
							labelAlign : 'right',
							allowBlank : false,// 不允许为空
							valueField : "key",
							displayField : "key",
							name : 'client',
							maxLength:20,
							store : clientStore
						},{
							xtype : "hiddenfield",
							name : "area_code"
						},{
							xtype : "hiddenfield",
							name : "case_checkper"
						}, 
						{
							xtype : 'combo',
							columnWidth : .5,
							fieldLabel : '案例所属地',
							labelWidth : 80,
							labelAlign : 'right',
							name : 'area',
							id : 'area_update_alcohol',
							emptyText:'检索方式：如朝阳区(cy)',
							store : Ext.create("Ext.data.Store",
											{
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
													url:"judicial/dicvalues/getAreaInfo.do?area_code="+ownaddressTemp,
													reader : {
														type : "json"
													}
												}
											}),
							displayField : 'value',
							valueField : 'id',
							typeAhead : false,
							forceSelection: true,
							hideTrigger : true,
							minChars : 2,
							matchFieldWidth : true,
							listConfig : {
								loadingText : '正在查找...',
								emptyText : '没有找到匹配的数据'
							},
							listeners : {
								'afterrender':function(){
									if("" != ownaddressTemp)
									{
										this.store.load();
									}
								}
							}
						},
//						{
//							xtype : "combo",
//							fieldLabel : '案例所属地',
//							queryMode : 'remote',
//							forceSelection : false,
//							labelWidth : 80,
//							columnWidth : 0.5,
//							labelAlign : 'right',
//							name : 'area',
//							id : 'area_update_alcohol',
//							valueField : "id",
//							displayField : "value",
//							store : areaStore,
//							listeners : {
//								'beforequery' : function(e) {
//									var combo = e.combo;
//									if (!e.forceAll) {
//										var value = e.query;
//										combo.store.filterBy(function(
//												record, id) {
//											var text = record
//													.get(combo.displayField);
//											return (text.indexOf(value) != -1);
//										});
//										combo.expand();
//										return false;
//									}
//								}
//							}
//						}
						]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .5,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '送检人',
//									maxLength : 50,
									name : 'checkper',
									validator : function(value){
				                        var len = value.replace(/[^\x00-\xff]/g,"xx").length;
				                        if(len>40){
				                            return "长度不能超过40个字符或20个汉字";
				                        }
				                        return true;
				                    } 

								}, {
									// 该列在整行中所占的百分比
									columnWidth : .5,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '送检人电话',
									regex: /(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,11}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/,
									name : 'checkper_phone',
									regexText:"电话格式有误"

								}]
					},{
						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [{
									xtype : 'hiddenfield',
									name : 'receiver_id'
								}, {
									xtype : 'combo',
									fieldLabel : '案例归属<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
									labelWidth : 80,
									columnWidth : 1,
									labelAlign : 'right',
									name : 'receiver_id_update',
									id:'case_receiver_update_alcohol',
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
									minChars : 2,
									allowBlank:false, //不允许为空
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
								
								},
//								{
//									xtype : 'combo',
//									columnWidth : .5,
//									fieldLabel : '归属人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
//									labelWidth : 80,
//									labelAlign : 'right',
//									name : 'receiver_id_update',
//									id:'case_receiver_update_alcohol',
//									emptyText:'检索方式(姓名首字母)：如小红(xh)',
//									store : Ext.create("Ext.data.Store",{
//										 fields:[
//								                  {name:'key',mapping:'key',type:'string'},
//								                  {name:'value',mapping:'value',type:'string'},
//								                  {name:'name',mapping:'name',type:'string'},
//								                  {name:'id',mapping:'id',type:'string'},
//								          ],
//										pageSize : 10,
//										proxy : {
//											type : "ajax",
//											url:"judicial/dicvalues/getUpcUsers.do?area_id="+ownpersonTemp,
//											reader : {
//												type : "json"
//											}
//										}
//									}),
//									displayField : 'name',
//									valueField : 'id',
//									allowBlank : false,// 不允许为空
//									blankText : "不能为空",// 错误提示信息，默认为This field
//									typeAhead : false,
//									forceSelection: true,
//									hideTrigger : true,
//									minChars : 2,
//									matchFieldWidth : true,
//									listConfig : {
//										loadingText : '正在查找...',
//										emptyText : '没有找到匹配的数据'
//									},
//									listeners : {
//										'afterrender':function(){
//											alert(ownpersonTemp);
//											if("" != ownpersonTemp)
//											{
//												this.store.load();
//											}
//										}
//									}
//								}, 
								]
					},{

						border : false,
						xtype : 'fieldcontainer',
						hidden : true,
						layout : "column",
						items : [{
									xtype : 'textarea',
									fieldLabel : '事件描述',
									name : 'event_desc',
									width : 500,
									maxLength : 1000,
									// allowBlank : false,// 不允许为空
									columnWidth : 1,
									labelWidth : 80,
									labelAlign : 'right'
								}]
					
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [ {
							columnWidth : 1,
							xtype : "combo",
							fieldLabel : '模板类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
							mode : 'local',
							labelWidth : 80,
							editable : false,
							labelAlign : 'right',
							blankText : '请选择模板',
							emptyText : '请选择模板',
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field
							// is required!
							valueField : "key",
							displayField : "value",
							id : "report_model_update_alcohol",
							name : 'report_model',
							store : storeModel
						}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .5,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '邮寄地址',
									name : 'mail_address',
									validator : function(value){
				                        var len = value.replace(/[^\x00-\xff]/g,"xx").length;
				                        if(len>50){
				                            return "长度不能超过50个字符或25个汉字";
				                        }
				                        return true;
				                    } 
								}, {
									// 该列在整行中所占的百分比
									columnWidth : .5,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '邮件接收人',
									name : 'mail_per',
									validator : function(value){
				                        var len = value.replace(/[^\x00-\xff]/g,"xx").length;
				                        if(len>40){
				                            return "长度不能超过40个字符或20个汉字";
				                        }
				                        return true;
				                    } 
								}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .5,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '联系电话',
									regex: /(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,11}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/,
									name : 'mail_phone',
									regexText:"电话格式有误"
								},{
					        	    columnWidth : .5,
					                xtype : "combo",
					                fieldLabel : '鉴定人(可多选)<span style="color:red">*</span>',
					                valueField :"key",
					                displayField: "key",
					                id: "case_checkper_update",
					                labelWidth : 100,
//					                forceSelection: true,
					                allowBlank  : false,//不允许为空
					                blankText   : "不能为空",//错误提示信息，默认为This field is required!
					                emptyText:'请选择鉴定人',
					                name:'case_checkper_update',
					                editable:false,
					                multiSelect:true,
					                store : clientStore2
					            }
						]
					}, 
					
	                {
						
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [   new Ext.form.field.ComboBox({
							fieldLabel : '重新鉴定',
							 columnWidth : .5,
							labelWidth : 80,
							editable : false,
							triggerAction : 'all',
							displayField : 'Name',
							labelAlign : 'right',
							valueField : 'Code',
							store : new Ext.data.ArrayStore({
										fields : ['Name', 'Code'],
										data : [['否', 0],['是', 1]]
									}),
							value : 0,
							mode : 'local',
							// typeAhead: true,
							name : 'is_check',
							id:'is_check'
						})]
					},
					
					
					{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'textarea',
									fieldLabel : '备注',
									name : 'remark',
									width : 500,
									maxLength : 400,
									columnWidth : 1,
									labelWidth : 80,
									labelAlign : 'right'
								}]
					}, 
					{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [new Ext.form.field.ComboBox({
									fieldLabel : '血管类型',
									columnWidth : .5,
									labelWidth : 80,
									editable : false,
									triggerAction : 'all',
									displayField : 'Name',
									labelAlign : 'right',
									valueField : 'Code',
									store : new Ext.data.ArrayStore({
												fields : ['Name', 'Code'],
												data : [['真空采血管', 1], ['促凝管', 0]]
											}),
									value : 1,
									mode : 'local',
									// typeAhead: true,
									name : 'isDoubleTube'
								})
						 , new Ext.form.field.ComboBox({
						 fieldLabel : '是否检出',
						 columnWidth : .5,
						 id : 'alcohol_is_detection_update',
						 labelWidth : 80,
						 editable : false,
						 triggerAction : 'all',
						 displayField : 'Name',
						 labelAlign : 'right',
						 valueField : 'Code',
						 store : new Ext.data.ArrayStore({
						 fields : ['Name', 'Code'],
						 data : [['是', 1], ['否', 0]]
						 }),
						 value : 0,
						 mode : 'local',
						 // typeAhead: true,
						 name : 'is_detection',
						 listeners : {
					            select : function(combo,records,options){ 
					            	switch (combo.value) {
									 case 0 :
										 Ext.getCmp("alcohol_sample_remark2").setValue("未出现乙醇的特征色谱峰。");
										 Ext.getCmp("alcohol_sample_remark2").setReadOnly(true);
										 Ext.getCmp("alcohol_sample_result").setValue("血液中未检出乙醇成分。");
										 Ext.getCmp("alcohol_sample_result").setReadOnly(true);
									 break;
									 case 1 :
										 Ext.getCmp("alcohol_sample_remark2").setValue("血液中乙醇含量为mg/mL。");
										 Ext.getCmp("alcohol_sample_remark2").setReadOnly(false);
										 Ext.getCmp("alcohol_sample_result").setValue("血液中乙醇含量为mg/mL。");
										 Ext.getCmp("alcohol_sample_result").setReadOnly(false);
									 break;
								 	}
					            },
			         			change : function(combo,records,options){ 
			         				switch (combo.value) {
			         				case 1 :
			         					Ext.getCmp("alcohol_sample_remark2").setReadOnly(false);
			         					Ext.getCmp("alcohol_sample_result").setReadOnly(false);
			         				break;
			         				case 0 :
			         					Ext.getCmp("alcohol_sample_remark2").setReadOnly(true);
			         					Ext.getCmp("alcohol_sample_result").setReadOnly(true);
			         				}
			         			}
						 }

						 })
						]
					}
			 ,  {
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					items : [{
						// 该列在整行中所占的百分比
						columnWidth : .5,
						id:'bloodnumAidUpdate',
						xtype : "textfield",
						labelAlign : 'right',
						labelWidth : 80,
						fieldLabel : '血液编号',
						maxLength : 20,
						name : 'bloodnumA'
					},{
						// 该列在整行中所占的百分比
						columnWidth : .5,
						xtype : "textfield",
						labelAlign : 'right',
						labelWidth : 80,
						fieldLabel : '血液容量',
						maxLength : 20,
						regex: /^\d+(\.\d{1,4})?$/,
						name : 'bloodnumB',
						validator:function(value){
							if("" != Ext.getCmp("bloodnumAidUpdate").getValue().trim())
							{
								return true;
							}else if("" == Ext.getCmp("bloodnumAidUpdate").getValue().trim() && ""==value.trim() )
							{
								return true;
							}else
							{
								return "血液编号需先填写，谢谢！";
							}
						}
					 
					}]
				}
			 
			 , {
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					items : [{
								xtype : 'textfield',
								fieldLabel : '案例简介',
								name : 'case_intr',
								columnWidth : 1,
								labelWidth : 80,
								labelAlign : 'right',
								validator : function(value){
			                        var len = value.replace(/[^\x00-\xff]/g,"xx").length;
			                        if(len>100){
			                            return "长度不能超过100个字符或50个汉字";
			                        }
			                        return true;
			                    } 
							}]
				}, {
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					items : [{
								xtype : 'textarea',
								fieldLabel : '案例详情',
								name : 'case_det',
								columnWidth : 1,
								maxLength : 500,
								labelWidth : 80,
								labelAlign : 'right',
								validator : function(value){
			                        var len = value.replace(/[^\x00-\xff]/g,"xx").length;
			                        if(len>300){
			                            return "长度不能超过300个字符或150个汉字";
			                        }
			                        return true;
			                    } 
							}]
				}
			 
				, {
					 border : false,
					 xtype : 'fieldcontainer',
					 layout : "column",
					 items : [{
					 // 该列在整行中所占的百分比
					 columnWidth : .5,
					 xtype : "numberfield",
					 value : 0,
					 maxLength : 7,
					 labelAlign : 'right',
					 labelWidth : 80,
					 allowBlank : false,// 不允许为空
					 fieldLabel : '标准金额',
					 maxLength : 50,
					 name : 'stand_sum',
					 value:350.0
					 }, {
					 // 该列在整行中所占的百分比
					 columnWidth : .5,
					 xtype : "numberfield",
					 value : 0,
					 maxLength : 7,
					 labelAlign : 'right',
					 labelWidth : 80,
					 allowBlank : false,// 不允许为空
					 fieldLabel : '实收金额',
					 maxLength : 50,
					 name : 'real_sum',
					 value:350
					 }
				]
	}
			 
			 , {
			 border : false,
			 xtype : 'fieldcontainer',
			 layout : "column",
			 items : [{
			 xtype : 'textarea',
			 fieldLabel : '样本描述<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			 name : 'sample_remark',
			 allowBlank : false,
			 width : 500,
			 maxLength : 400,
			 columnWidth : 1,
			 labelWidth : 80,
			 labelAlign : 'right',
			 value : '送检血液无凝块，包装无渗漏。'
			 }]
			 }, {
			 border : false,
			 xtype : 'fieldcontainer',
			 layout : "column",
			 items : [{
			 xtype : 'textarea',
			 id : 'alcohol_sample_remark2',
			 fieldLabel : '检验描述<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			 name : 'sample_remark2',
			 allowBlank : false,
			 width : 500,
			 maxLength : 400,
			 columnWidth : 1,
			 labelWidth : 80,
			 labelAlign : 'right',
			 value : '未出现乙醇的特征色谱峰。'
			 }]
			 }, {
			 border : false,
			 xtype : 'fieldcontainer',
			 layout : "column",
			 items : [{
			 columnWidth : 1,
			 xtype : 'textarea',
			 id : 'alcohol_sample_result',
			 fieldLabel : '检验结果<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			 name : 'sample_result',
			 allowBlank : false,
			 maxLength : 400,
			 labelWidth : 80,
			 labelAlign : 'right',
			 value : '血液中未检出乙醇成分。',
			 validator : function(value) {
					if(Ext.getCmp("alcohol_is_detection_update").getValue()==1)
					{
						if(Ext.getCmp("alcohol_sample_remark2").getValue()==value)
							return true;
						else 
							return "检验结果和检验描述不一致";
					}else {
						return true;
					}
				}
			 }]
			 }
			]
		}];

		me.buttons = [{
					text : '保存',
					iconCls : 'Disk',
					handler : me.onSave
				}, {
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	listeners : {
		'afterrender' : function() {
			var me = this;
			var values = me.getValues();
			var area_code = values["area_code"];
			var receiver = values["receiver_id"];
			var case_checkper = values["case_checkper"];
//			console.log(case_checkper)
			Ext.getCmp('area_update_alcohol').setValue(area_code);
			Ext.getCmp('case_receiver_update_alcohol').setValue(receiver);
			Ext.getCmp('case_checkper_update').setValue(case_checkper);
			Ext.Ajax.request({
				url : "alcohol/register/getSampleInfo.do",
				method : "POST",
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : {
					sample_id : values["sample_id"]
				},
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					me.down("form").add({
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .5,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									value : response.sample_name,
									allowBlank : false,// 不允许为空
									fieldLabel : '被检验人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
									maxLength : 20,
									name : 'sample_name',
									regex: /^[\u4E00-\u9FA5]+$/,  
					                regexText:'只能输入汉字'  
								}
						
						
						, {
									columnWidth : .5,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '身份证号',
									maxLength : 18,
									value : response.id_number,
									name : 'id_number',
									validator : function(value) {
										var result = false;
										if (value != null && value != "") {
											Ext.Ajax.request({
												url : "judicial/register/verifyId_Number.do",
												method : "POST",
												async : false,
												headers : {
													'Content-Type' : 'application/json'
												},
												jsonData : {
													id_number : value
												},
												success : function(response,
														options) {
													response = Ext.JSON
															.decode(response.responseText);
													if (!response) {
														result = "身份证号码不正确";
													} else {
														result = true;
													}
												},
												failure : function() {
													Ext.Msg.alert("提示",
															"网络故障<br>请联系管理员!");
												}
											});
										} else {
											result = true;
										}
										return result;
									}
								}]
					}
//					, {
//						border : false,
//						xtype : 'fieldcontainer',
//						layout : "column",
//						hidden : true,
//						items : [{
//									// 该列在整行中所占的百分比
//									columnWidth : .5,
//									xtype : 'numberfield',
//									labelAlign : 'right',
//									labelWidth : 80,
//									// allowBlank : false,// 不允许为空
//									fieldLabel : '采样剂量(mL)',
//									maxLength : 5,
//									allowDecimals : false,
//									value : response.sample_ml,
//									name : 'sample_ml',
//									value : 0,
//									listeners : {
//										change : function(field, value) {
//											// if (value < 0) {
//											// field.setValue(0);
//											// }
//										}
//									}
//								}]
//					}
					, {
			border : false,
			xtype : 'fieldcontainer',
			layout : "column",
			hidden : true,
			items : [{
						// 该列在整行中所占的百分比
						columnWidth : .5,
						xtype : 'numberfield',
						labelAlign : 'right',
						labelWidth : 80,
						// allowBlank : false,// 不允许为空
						fieldLabel : '采样剂量(mL)',
						maxLength : 5,
						allowDecimals : false,
						value : response.sample_ml,
						name : 'sample_ml',
						value : 0,
						listeners : {
							change : function(field, value) {
								// if (value < 0) {
								// field.setValue(0);
								// }
							}
						}
					}]
		}
					
					
					);
				}
			});
		}
	},
	onSave : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if (form.isValid()) {
			form.setValues({
						"receiver_id" : values["receiver_id_update"],
						"case_checkper" : values["case_checkper_update"]
					})
			form.submit({
						url : 'alcohol/register/updateCaseInfo.do',
						method : 'post',
						waitMsg : '正在修改数据...',
						success : function(form, action) {
							response = Ext.JSON
									.decode(action.response.responseText);
							if (response) {
								Ext.Msg.alert("提示",  response.message);
								me.grid.getStore().load();
								me.up("window").close();
							} else {
								Ext.Msg.alert("提示",  response.message);
								me.up("window").close();
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "修改失败<br>请联系管理员2!");
							me.up("window").close();
						}
						
//						success: function (form, action) {  
//							response = Ext.JSON
//							.decode(action.response.responseText);
//			                 if (response.result == true) {  
//			                 	me.up("window").close();
//			                 	me.grid.getStore().load();
//			                 	Ext.MessageBox.alert("信息", response.message);
//			                 }else { 
//			                	 Ext.MessageBox.alert("错误信息", response.message);
//			                 } 
//						},  
//						failure: function () {
//							Ext.Msg.alert("提示", "请联系管理员!");
//						}
						
						
						
					});
		}
 

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}
});
