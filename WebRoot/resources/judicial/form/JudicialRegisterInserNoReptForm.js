Ext.define('Rds.judicial.form.JudicialRegisterInserNoReptForm',
				{
					extend : 'Ext.form.Panel',
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
					initComponent : function() {
						var me = this;
						 var receiver_id = new Ext.form.ComboBox({
								autoSelect : true,
								editable:true,
								allowBlank:true,
								labelWidth : 80,
								columnWidth : .25,
								fieldLabel : '鉴定人',
								labelAlign : 'right',
						        name:'receiver_id',
						        triggerAction: 'all',
						        queryMode: 'local', 
						        selectOnTab: true,
						        store: Ext.create('Ext.data.Store',{
						    	    fields:['key','value'],
						    	    autoLoad:true,
						    	    proxy:{
						    	        type:'jsonajax',
						    	        actionMethods:{read:'POST'},
						    	        url:'judicial/agent/queryUserByType.do',
						    	        params:{
						    	        	roletype:'142'
						    	        },
						    	        render:{
						    	            type:'json'
						    	        },
						    	        writer: {
						    	            type: 'json'
						    	       }
						    	    }
						    	}),
						        fieldStyle: me.fieldStyle,
						        displayField:'value',
						        valueField:'value',
						        listClass: 'x-combo-list-small',
							});
					    var sample_in_per = new Ext.form.ComboBox({
							autoSelect : true,
							editable:true,
							allowBlank:false,
							labelWidth : 80,
							columnWidth : .25,
							fieldLabel : '采样人员<span style="color:red">*</span>',
							labelAlign : 'right',
					        name:'sample_in_per',
					        forceSelection:true,
					        triggerAction: 'all',
					        queryMode: 'local', 
					        emptyText : "请选择",
					        selectOnTab: true,
					        store: userModel,
					        fieldStyle: me.fieldStyle,
					        displayField:'value',
					        valueField:'value',
					        listClass: 'x-combo-list-small',
						})
						var mailStore = Ext.create('Ext.data.Store', {
							model : 'model',
							proxy : {
								type : 'ajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/dicvalues/getMailModels.do',
								reader : {
									type : 'json',
									root : 'data'
								}
							},
							autoLoad : true,
							remoteSort : true
						});
						var partnerModel = Ext.create('Ext.data.Store', {
							fields:[
					                  {name:'parnter_name',mapping:'parnter_name',type:'string'},
					          ],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'upc/partnerConfig/queryall.do',
								params : {
									type : 'dna',
								},
								reader : {
									type : 'json',
									root : 'data'
								}
							},
							autoLoad : false,
							remoteSort : true
						});
						var storeModel = Ext.create('Ext.data.Store', {
							model : 'model',
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/dicvalues/getReportModelByPartner.do',
								params : {
									type : 'dna',
								},
								reader : {
									type : 'json',
									root : 'data'
								}
							},
							autoLoad : false,
							remoteSort : true
						});
						var storeSampleType = Ext.create('Ext.data.Store', {
							model : 'model',
							proxy : {
								type : 'ajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/dicvalues/getSampleType.do',
								reader : {
									type : 'json',
									root : 'data'
								}
							},
							autoLoad : true,
							remoteSort : true
						});
						var unitStore=Ext.create(
						        'Ext.data.Store',
						        {
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
							columnWidth : .50,
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
						var storeSampleCall = Ext.create('Ext.data.Store', {
							model : 'model',
							proxy : {
								type : 'ajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/dicvalues/getSampleCall.do',
								reader : {
									type : 'json',
									root : 'data'
								}
							},
							autoLoad : false,
							remoteSort : true
						});
						var typeid = Ext.create('Ext.form.ComboBox', {
							fieldLabel: "单双亲<span style='color:red'>*</span>",
		                    labelWidth : 80,
		                    editable:false,
		                    triggerAction: 'all',
		                    displayField: 'Name',
		                    labelAlign: 'right',
		                    blankText:'请选择类型',   
					        emptyText:'请选择类型',  
		                    allowBlank  : false,//不允许为空  
		                    columnWidth : .50,
		                    valueField: 'Code',
		                    store: new Ext.data.ArrayStore({
		                        fields: ['Name', 'Code'],
		                        data: [['无', '0'], ['单亲', '1'], ['双亲', '2']]
		                    })          ,
		                    mode: 'local',
		                    name: 'typeid'
						});
						var partner = Ext.create('Ext.form.ComboBox', {
							fieldLabel: "合作方",
		                    labelWidth : 80,
		                    editable:false,
		                    triggerAction: 'all',
		                    displayField: 'parnter_name',
		                    labelAlign: 'right',
		                    columnWidth : .50,
		                    valueField: 'parnter_name',
		                    store: partnerModel,
		                    mode: 'local',
//		                    allowBlank:false,
		                    name: 'parnter_name',
		                    id:'parnter_name',
			   				listeners :{
			                       	"select" : function(combo, record, index){
			                       		if(combo.getValue().indexOf("河南商都司法鉴定中心")==0
			                       				||combo.getValue().indexOf("广东永建法医物证司法鉴定所")==0)
			                       			Ext.getCmp("case_code").allowBlank=true;
			                       		else
			                       			Ext.getCmp("case_code").allowBlank=false;
			                       		}
			                       }
						});
						var per_num=Ext.create('Ext.form.field.Number',{
						    fieldLabel: "样本数<span style='color:red'>*</span>",
						    name: 'per_num',
						    labelWidth : 80,
						    value: 0,
						    maxValue: 9999,
						    minValue: 0,
						    columnWidth : .50,
						    labelAlign: 'right',
						    allowBlank:false,
						    readOnly:true
						});
						me.items = [{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [	new Ext.form.field.ComboBox(
									{
										fieldLabel : '案例来源',
										columnWidth : .50,
										labelWidth : 80,
										editable : false,
										triggerAction : 'all',
										displayField : 'Name',
										labelAlign : 'right',
										value : 0,
										valueField : 'Code',
										store : new Ext.data.ArrayStore(
												{
													fields : [
															'Name',
															'Code' ],
													data : [
															[
																	'实体渠道',
																	'0' ],
															[
																	'电子渠道',
																	'1' ] ]
												}),
										mode : 'local',
										value:'0',
										name : 'source_type'
									})
							         ]},
								{
									border : false,
									xtype : 'fieldcontainer',
									layout : "column",
									items : [
									         {xtype:"hiddenfield",name:"case_type",value:"0"},
											{
												// 该列在整行中所占的百分比
												columnWidth : .50,
												xtype : "textfield",
												labelAlign : 'right',
												labelWidth : 80,
												allowBlank:false, //不允许为空
												fieldLabel : "案例条形码<span style='color:red'>*</span>",
												name : 'case_code',
												id:'case_code'
											},
											new Ext.form.field.ComboBox(
													{
														fieldLabel : '紧急程度',
														columnWidth : .50,
														labelWidth : 80,
														editable : false,
														triggerAction : 'all',
														displayField : 'Name',
														labelAlign : 'right',
														value : 0,
														valueField : 'Code',
														store : new Ext.data.ArrayStore(
																{
																	fields : [
																			'Name',
																			'Code' ],
																	data : [
																			[
																					'普通',
																					0 ],
																			[
																					'紧急',
																					1 ] ]
																}),
														mode : 'local',
														// typeAhead: true,
														name : 'urgent_state'
													}) ]
								},
								{
									layout : "column",// 从左往右的布局
									xtype : 'fieldcontainer',
									border : false,
									items : [ {
										xtype : 'combo',
										fieldLabel : '归属人<span style="color:red">*</span>',
										labelWidth : 80,
										columnWidth : .50,
										name : 'case_userid',
										labelAlign : 'right',
										emptyText:'(人员首字母)：如小明(xm)',
										store :Ext.create("Ext.data.Store",{
											   fields:[
													{name:'key',mapping:'key',type:'string'},
													{name:'value',mapping:'value',type:'string'}
									          ],
											pageSize : 20,
											autoLoad: false,
											proxy : {
												type : "ajax",
												url:'judicial/dicvalues/getUsersId.do',
												reader : {
													type : "json"
												}
											}
										}),
										displayField : 'value',
										valueField : 'key',
										typeAhead : false,
										allowBlank:false, //不允许为空
										hideTrigger : true,
										forceSelection: true,
										minChars : 2,
										matchFieldWidth : true,
										listConfig : {
											loadingText : '正在查找...',
											emptyText : '没有找到匹配的数据'
										}
									},{
										xtype : "combo",
				        				fieldLabel : '归属地区<span style="color:red">*</span>',
				        				labelWidth : 80,
				        				columnWidth : .50,
				        				id:'case_areacode',
				        				labelAlign : 'right',
				        				name : 'case_areacode',
				        				emptyText:'检索方式：如朝阳区(cy)',
				        				store : Ext.create("Ext.data.Store",{
				        					 fields:[
				        			                  {name:'key',mapping:'key',type:'string'},
				        			                  {name:'value',mapping:'value',type:'string'},
				        			                  {name:'name',mapping:'name',type:'string'},
				        			                  {name:'id',mapping:'id',type:'string'},
				        			          ],
				        					pageSize : 10,
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
				        				typeAhead : false,
				        				hideTrigger : true,
				        				allowBlank:false, //不允许为空
				        				forceSelection: true,
				        				minChars : 2,
				        				matchFieldWidth : true,
				        				listConfig : {
				        					loadingText : '正在查找...',
				        					emptyText : '没有找到匹配的数据'
				        				}
										}
									]
								}, 
								{
									layout : "column",// 从左往右的布局
									xtype : 'fieldcontainer',
									border : false,
									items : [ sample_in_per,receiver_id,
//									          {
//										// 该列在整行中所占的百分比
//										columnWidth : .50,
//										xtype : "textfield",
//										labelAlign : 'right',
//										fieldLabel : '采样人员',
//										labelWidth : 80,
//										maxLength : 50,
//										name : 'sample_in_per',
//									}, 
									unit_type ]
								},
								{
									layout : "column",// 从左往右的布局
									xtype : 'fieldcontainer',
									border : false,
									items : [ {
										// 该列在整行中所占的百分比
										columnWidth : .50,
										xtype : "textfield",
										labelAlign : 'right',
										fieldLabel : '电话号码',
										labelWidth : 80,
										maxLength : 50,
										name : 'phone',
									},{
										xtype : 'datefield',
										name : 'consignment_time',
										columnWidth : .50,
										labelWidth : 80,
										fieldLabel : "委托时间<span style='color:red'>*</span>",
										labelAlign : 'right',
										format : 'Y-m-d',
										allowBlank : false,// 不允许为空
										maxValue : new Date()
									} ]
								},
								{
									border : false,
									xtype : 'fieldcontainer',
									layout : "column",
									items : [
											{
												// 该列在整行中所占的百分比
												columnWidth : .50,
												xtype : "textfield",
												labelAlign : 'right',
												labelWidth : 80,
												fieldLabel : '委托人',
												maxLength : 50,
												name : 'client',
											},
											{
												xtype : 'datefield',
												name : 'accept_time',
												columnWidth : .50,
												labelWidth : 80,
												fieldLabel : "受理时间<span style='color:red'>*</span>",
												labelAlign : 'right',
												format : 'Y-m-d',
												allowBlank : false,// 不允许为空
												maxValue : new Date(),
												value : Ext.Date.add(
														new Date(),
														Ext.Date.DAY),
											} ]
								},
								{
									border : false,
									xtype : 'fieldcontainer',
									layout : "column",
									items : [Ext.create('Ext.form.field.Number',{
									    fieldLabel: "打印份数<span style='color:red'>*</span>",
									    name: 'copies',
									    labelWidth : 80,
									    value: 2,
									    maxValue: 10,
									    minValue: 0,
									    columnWidth : .50,
									    labelAlign: 'right',
									    allowBlank:false
									}),new Ext.form.field.ComboBox(
													{
														fieldLabel : "亲属关系<span style='color:red'>*</span>",
														columnWidth : .50,
														labelWidth : 80,
														editable : false,
														triggerAction : 'all',
														displayField : 'Name',
														labelAlign : 'right',
														value : 0,
														valueField : 'Code',
														store : new Ext.data.ArrayStore(
																{
																	fields : [
																			'Name',
																			'Code' ],
																	data : [
																			[
																					'亲子',
																					0 ],
																			[
																					'亲缘',
																					1 ],
																			[
																					'同胞',
																					2 ] ]
																}),
														mode : 'local',
														// typeAhead: true,
														name : 'sample_relation'
													}) ]
								},
								{
									layout : "column",// 从左往右的布局
									xtype : 'fieldcontainer',
									border : false,
									items : [ typeid, per_num]
								},{
									layout : "column",// 从左往右的布局
									xtype : 'fieldcontainer',
									border : false,
									items : [partner,{
										// 该列在整行中所占的百分比
										columnWidth : .50,
										xtype : "textfield",
										labelAlign : 'right',
										labelWidth : 80,
										fieldLabel : '优惠码',
										maxLength : 50,
										name : 'confirm_code',
									}]
								},	{
									xtype : 'fieldset',
									title : '样本邮寄信息',
									layout : 'anchor',
									defaults : {
										anchor : '100%'
									},
									items : [{
										layout : "column",// 从左往右的布局
										xtype : 'fieldcontainer',
										border : false,
										items : [ 	{
											columnWidth : .20,
											xtype : "textfield",
											fieldLabel : "快递编号",
											labelAlign : 'right',
											maxLength : 50,
											labelWidth : 60,
											name : 'express_num',
										},new Ext.form.field.ComboBox({
											fieldLabel : '快递类型',
											labelWidth : 60,
											columnWidth : .20,
											editable : false,
											triggerAction : 'all',
											labelAlign : 'right',
											valueField : "key",
											displayField : "value",
											store : mailStore,
											mode : 'local',
											name : 'express_type'
										})]
									}
									]
								},{
									xtype : 'fieldset',
									title : '报告回寄地址',
									layout : 'anchor',
									defaults : {
										anchor : '100%'
									},
									items : [{
										layout : "column",// 从左往右的布局
										xtype : 'fieldcontainer',
										border : false,
										items : [
										{
											columnWidth : .20,
											xtype : "textfield",
											fieldLabel : '收件人',
											labelAlign : 'right',
											maxLength : 18,
											labelWidth : 50,
											name : 'express_recive'
										},{
											columnWidth : .20,
											xtype : "textfield",
											fieldLabel : '联系方式',
											labelAlign : 'right',
											maxLength : 18,
											labelWidth : 60,
											name : 'express_concat'
										},
										{
											columnWidth : .20,
											xtype : "textfield",
											fieldLabel : '收件地址',
											labelAlign : 'right',
											maxLength : 50,
											labelWidth : 60,
											name : 'express_remark'
										}]
									}
									]
						     
								},{
									border : false,
									xtype : 'fieldcontainer',
									layout : "column",
									items : [ {
										xtype : 'textarea',
										fieldLabel : '案例备注',
										name : 'remark',
										width : 500,
										maxLength : 500,
										columnWidth : 1,
										labelWidth : 80,
										labelAlign : 'right'
									} ]
								},
								{
									border : false,
									xtype : 'fieldcontainer',
									layout : "column",
									items : [ {
										xtype : 'button',
										text : '添加样本',
										width : 100,
										x : '90%',
										listeners : {
											click : function() {
												var me = this.up("form");
												var num = per_num.getValue();
												per_num.setValue(++num);
												me.add({
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
															items : [ {
																layout : 'auto',
																xtype : 'panel',
																border : false,
																items : [
																		{
																			layout : 'column',
																			xtype : 'panel',
																			border : false,
																			items : [
																					{
																						columnWidth : .27,
																						xtype : "textfield",
																						fieldLabel : "条形码<span style='color:red'>*</span>",
//																						allowBlank : false,// 不允许为空
//																						blankText : "不能为空",// 错误提示信息，默认为This
																						labelAlign : 'right',
																						maxLength : 50,
																						labelWidth : 80,
																						name : 'sample_code',
//																						regex:/^\w+$/,
//																						regexText:'请输入正确条形码',
																					},
																					{
																						columnWidth : .24,
																						xtype : "textfield",
																						fieldLabel : "用户名<span style='color:red'>*</span>",
																						labelAlign : 'right',
																						maxLength : 50,
																						allowBlank : false,// 不允许为空
																						blankText : "不能为空",// 错误提示信息，默认为This
																						labelWidth : 60,
																						name : 'sample_username'
																					},
																					{
																						columnWidth : .26,
																						xtype : "textfield",
																						fieldLabel : '身份证',
																						labelAlign : 'right',
																						maxLength : 18,
																						labelWidth : 60,
																						name : 'id_number',
																						listeners:
																						{
																							"blur":function(combo, record, index){
																								if(""!=combo.getValue())
																								{
																									var mei = this.up("form");
																									mei.getForm().findField('birth_date').setValue(getBirthdatByIdNo(combo.getValue()));
																									console.log(getBirthdatByIdNo(combo.getValue()));
																								}
																								
																							}
																						}
																					},
																					{
																						layout : 'absolute',// 从左往右的布局
																						xtype : 'panel',
																						border : false,
																						columnWidth : .23,
																						items : [Ext.create('Ext.form.ComboBox', {
																							fieldLabel: "特殊样本",
																		                    labelWidth : 60,
																		                    editable:false,
																		                    width:140,
																		                    triggerAction: 'all',
																		                    displayField: 'Name',
																		                    labelAlign: 'right',
																		                    valueField: 'Code',
																		                    store: new Ext.data.ArrayStore({
																		                        fields: ['Name', 'Code'],
																		                        data: [['否', '0'], ['是', '1']]
																		                    }),
																		                    value:'0',
																		                    mode: 'local',
																		                    name: 'special'
																						}) ]
																					} 
																					
																					]
																		},
																		{
																			layout : 'column',
																			xtype : 'panel',
																			border : false,
																			style : 'margin-top:5px;',
																			items : [
																					{
																						columnWidth : .27,
																						xtype : "combo",
																						fieldLabel : "取样类型<span style='color:red'>*</span>",
																						mode : 'local',
																						labelWidth : 60,
																						labelAlign : 'right',
																						blankText : '请选择',
																						editable : false,
																						emptyText : '请选择',
																						allowBlank : false,// 不允许为空
																						blankText : "不能为空",// 错误提示信息，默认为This
																						valueField : "key",
																						displayField : "value",
																						maxLength : 50,
																						labelWidth : 80,
																						store : storeSampleType,
																						name : 'sample_type',
																						value : 'cy01',
																						 listeners :{
																	                        	"select" : function(combo, record, index){
																	                        		var mei = this.up("form");
																	                        		if( combo.getValue() == 'cy01' || combo.getValue()== 'cy02' || combo.getValue()== 'cy03' )
																		                        		mei.getForm().findField('special').setValue('0');
																	                        		else
																		                        		mei.getForm().findField('special').setValue('1');
																	                        	}
																	                        }
																					},
																					{
																						columnWidth : .24,
																						xtype : "combo",
																						mode : 'local',
																						labelWidth : 60,
																						labelAlign : 'right',
																						editable : false,
																						blankText : '请选择',
																						emptyText : '请选择',
																						allowBlank : false,// 不允许为空
																						blankText : "不能为空",// 错误提示信息，默认为This
																						valueField : "key",
																						displayField : "value",
																						store : storeSampleCall,
																						fieldLabel : "称谓<span style='color:red'>*</span>",
																						maxLength : 50,
																						labelWidth : 60,
																						name : 'sample_call'
																					},
																					{
																						columnWidth : .26,
																						xtype : "textfield",
																						fieldLabel : '出生日期',
																						emptyText : 'yyyy-MM-dd',
																						labelAlign : 'right',
																						maxLength : 20,
																						labelWidth : 60,
																						name : 'birth_date',
																						regex : /^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/,
																						regexText : "格式：yyyy-MM-dd！",
																					},
																					{
																						layout : 'absolute',// 从左往右的布局
																						xtype : 'panel',
																						border : false,
																						columnWidth : .15,
																						items : [ {
																							width : 50,
																							x : 30,
																							xtype : 'button',
																							text : '删除',
																							listeners : {
																								click : function() {
																									var num = per_num.getValue();
																									per_num.setValue(--num);
																									var mei = this.up("form");
																									mei.disable(true);
																									mei.up("form").remove(mei);
																								}
																							}
																						} ]
																					} ]
																		} ]
															} ]
														});
											}
										}
									} ]
								} ];

						me.buttons = [ {
							text : '保存',
							iconCls : 'Disk',
							handler : me.onSave
						},  {
							text : '保存并提交审核',
							iconCls : 'Disk',
							handler : me.onSaveSubmit
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : me.onCancel
						} ]
						me.callParent(arguments);
					},
					onSaveSubmit:function(){

						var me = this.up("form");
						var form = me.getForm();
						var values = form.getValues();
						var sample_str = values['sample_code'];
						var id_numbers = values['id_number'];
						values["case_state"]=0;
						var temp='';
						if(sample_str instanceof Array)
						{
//							var sample_str_temp = sample_str;
//							var nary=sample_str_temp.sort(); 
							console.log(sample_str);
							for(var i=0;i<sample_str.length-1;i++){ 
								for(var j = i+1; j < sample_str.length; j ++)
								{
									console.log(sample_str[i]+"-----"+sample_str[j])
									if (sample_str[i]==sample_str[j]){ 
										if(""!=sample_str[i])
										{
											Ext.MessageBox.alert("提示信息","重复样本条形码："+sample_str[i]);
											return;
										}
									} 
								}
							} 
						}
						//校验身份证准确性
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
						if (form.isValid()) {
							Ext.MessageBox.wait('正在操作','请稍后...');
//							var result = true;
//							if (sample_str) {
//								if (!((typeof sample_str == 'string') && sample_str.constructor == String)) {
//									for (var i = 0; i < sample_str.length; i++) {
//										for (var j = 0; j < sample_str.length; j++) {
//											if (i != j
//													&& sample_str[i] == sample_str[j]) {
//												result = false;
//											}
//										}
//									}
//								}
//							}
//							if (!result) {
//								Ext.MessageBox.alert("错误信息", "存在条形码输入一样的数据");
//							} else {
								var id_result = true;
								var sample_call = values['sample_call'];
								var id_number = values['id_number'];
								if(undefined == sample_call)
								{
									Ext.MessageBox.alert("错误信息", "样本信息还没填呢！");
									return;
								}
								if (sample_call) {
									if (!((typeof sample_str == 'string') && sample_str.constructor == String)) {
										for (var i = 0; i < sample_call.length; i++) {
											for (var j = 0; j < id_number.length; j++) {
												if (i == j) {
													if (Ext.util.Format
															.trim(id_number[j]) != '') {
														var num = id_number[j]
																.substring(
																		16,
																		id_number[j].length - 1);
														if (sample_call[i] == 'mother'
																|| sample_call[i] == 'daughter') {
															if (num % 2 != 0) {
																id_result = false;
															}
														} else if (sample_call[i] == 'father'
																|| sample_call[i] == 'son') {
															if (num % 2 == 0) {
																id_result = false;
															}
														}
													}
												}
											}
										}
									} else {
										if (Ext.util.Format.trim(id_number) != '') {
											var num = id_number.substring(16,
													id_number.length - 1);
											if (sample_call == 'mother'
													|| sample_call == 'daughter') {
												if (num % 2 != 0) {
													id_result = false;
												}
											} else if (sample_call == 'father'
													|| sample_call == 'son') {
												if (num % 2 == 0) {
													id_result = false;
												}
											}
										}
									}
								}
								//判断身份证准确性，性别
								if (!id_result) {
									Ext.MessageBox.alert("错误信息","存在身份证信息不匹配的数据");
									return;
								}
								//判断称谓选错
								var father_num = 0, mother_num = 0;
								if (sample_call) {
									if (!((typeof sample_str == 'string') && sample_str.constructor == String)) {
										for (var i = 0; i < sample_call.length; i++) {
											if (sample_call[i] == 'father') {
												father_num++;
											}
											if (sample_call[i] == 'mother') {
												mother_num++;
											}
										}
									}
								}
								if (father_num > 1 || mother_num > 1) {
									Ext.MessageBox.alert("错误信息", "家长称谓出现重复");
									return;
								}
								if(father_num == 1 && mother_num == 1 && values["typeid"] != "2")
								{
									Ext.MessageBox.alert("错误信息", "单双亲类型和样本类型不符！");
									return;
								}
								//登记标识pc端
								values["sign"] = "pc";
								//保存地区名
								values["receiver_area"]=Ext.getCmp('case_areacode').getRawValue();
								values["submitFlag"]='1';
								//请求
								Ext.Ajax.request({
									url : "judicial/register/saveCaseInfoTemp.do",
									method : "POST",
									headers : {
										'Content-Type' : 'application/json'
									},
									jsonData : values,
									success : function(response, options) {
										response = Ext.JSON
												.decode(response.responseText);
										if (response.result) {
											Ext.MessageBox.alert("提示信息",
													"添加案例成功");
											me.grid.getStore().load();
											me.up("window").close();
										} else {
											Ext.MessageBox.alert("错误信息",response.message);
										}
									},
									failure : function() {
										Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
									}
								});
							}
//						}
					
					},
					onSave : function() {
						var me = this.up("form");
						var form = me.getForm();
						var values = form.getValues();
						var sample_str = values['sample_code'];
						var id_numbers = values['id_number'];
						values["case_state"]=0;
						var temp='';
						if(sample_str instanceof Array)
						{
//							var sample_str_temp = sample_str;
//							var nary=sample_str_temp.sort(); 
							console.log(sample_str);
							for(var i=0;i<sample_str.length-1;i++){ 
								for(var j = i+1; j < sample_str.length; j ++)
								{
									console.log(sample_str[i]+"-----"+sample_str[j])
									if (sample_str[i]==sample_str[j]){ 
										if(""!=sample_str[i])
										{
											Ext.MessageBox.alert("提示信息","重复样本条形码："+sample_str[i]);
											return;
										}
									} 
								}
							} 
						}
						//校验身份证准确性
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
						if (form.isValid()) {
							Ext.MessageBox.wait('正在操作','请稍后...');
//							var result = true;
//							if (sample_str) {
//								if (!((typeof sample_str == 'string') && sample_str.constructor == String)) {
//									for (var i = 0; i < sample_str.length; i++) {
//										for (var j = 0; j < sample_str.length; j++) {
//											if (i != j
//													&& sample_str[i] == sample_str[j]) {
//												result = false;
//											}
//										}
//									}
//								}
//							}
//							if (!result) {
//								Ext.MessageBox.alert("错误信息", "存在条形码输入一样的数据");
//							} else {
								var id_result = true;
								var sample_call = values['sample_call'];
								var id_number = values['id_number'];
								if(undefined == sample_call)
								{
									Ext.MessageBox.alert("错误信息", "样本信息还没填呢！");
									return;
								}
								if (sample_call) {
									if (!((typeof sample_str == 'string') && sample_str.constructor == String)) {
										for (var i = 0; i < sample_call.length; i++) {
											for (var j = 0; j < id_number.length; j++) {
												if (i == j) {
													if (Ext.util.Format
															.trim(id_number[j]) != '') {
														var num = id_number[j]
																.substring(
																		16,
																		id_number[j].length - 1);
														if (sample_call[i] == 'mother'
																|| sample_call[i] == 'daughter') {
															if (num % 2 != 0) {
																id_result = false;
															}
														} else if (sample_call[i] == 'father'
																|| sample_call[i] == 'son') {
															if (num % 2 == 0) {
																id_result = false;
															}
														}
													}
												}
											}
										}
									} else {
										if (Ext.util.Format.trim(id_number) != '') {
											var num = id_number.substring(16,
													id_number.length - 1);
											if (sample_call == 'mother'
													|| sample_call == 'daughter') {
												if (num % 2 != 0) {
													id_result = false;
												}
											} else if (sample_call == 'father'
													|| sample_call == 'son') {
												if (num % 2 == 0) {
													id_result = false;
												}
											}
										}
									}
								}
								//判断身份证准确性，性别
								if (!id_result) {
									Ext.MessageBox.alert("错误信息","存在身份证信息不匹配的数据");
									return;
								}
								//判断称谓选错
								var father_num = 0, mother_num = 0;
								if (sample_call) {
									if (!((typeof sample_str == 'string') && sample_str.constructor == String)) {
										for (var i = 0; i < sample_call.length; i++) {
											if (sample_call[i] == 'father') {
												father_num++;
											}
											if (sample_call[i] == 'mother') {
												mother_num++;
											}
										}
									}
								}
								if (father_num > 1 || mother_num > 1) {
									Ext.MessageBox.alert("错误信息", "家长称谓出现重复");
									return;
								}
								if(father_num == 1 && mother_num == 1 && values["typeid"] != "2")
								{
									Ext.MessageBox.alert("错误信息", "单双亲类型和样本类型不符！");
									return;
								}
								//登记标识pc端
								values["sign"] = "pc";
								//保存地区名
								values["receiver_area"]=Ext.getCmp('case_areacode').getRawValue();
								//请求
								Ext.Ajax.request({
									url : "judicial/register/saveCaseInfoTemp.do",
									method : "POST",
									headers : {
										'Content-Type' : 'application/json'
									},
									jsonData : values,
									success : function(response, options) {
										response = Ext.JSON
												.decode(response.responseText);
										if (response.result) {
											Ext.MessageBox.alert("提示信息",
													"添加案例成功");
											me.grid.getStore().load();
											me.up("window").close();
										} else {
											Ext.MessageBox.alert("错误信息",response.message);
										}
									},
									failure : function() {
										Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
									}
								});
							}
//						}
					},
					onCancel : function() {
						var me = this;
						me.up("window").close();
					}
				});
