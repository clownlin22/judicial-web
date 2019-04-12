/**
 * @author yuanxiaobo
 * @description 案例新增
 * @date 20170315
 */

Ext.define('Rds.judicial.form.JudicialRegisterMedicalInsertForm', {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;
		var sample_in_per = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			allowBlank:false,
			labelWidth : 80,
			columnWidth : .50,
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
                data: [['单亲', '1'], ['双亲', '2']]
            })          ,
            mode: 'local',
            name: 'typeid'
		});
		var partner = Ext.create('Ext.form.ComboBox', {
			fieldLabel: "合作方",
            labelWidth : 80,
            editable:true,
            triggerAction: 'all',
            displayField: 'parnter_name',
            labelAlign: 'right',
            columnWidth : .50,
            valueField: 'parnter_name',
            store: partnerModel,
            hidden:true,
            mode: 'local',
            name: 'parnter_name',
            id:'parnter_name'
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
		var modeltype=Ext.create('Ext.form.ComboBox', 
				{
					columnWidth : .50,
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
					store : new Ext.data.ArrayStore(
							{
								fields : ['key','value' ],
								data : [['sbyxmodel','苏博医学模版']]
							}),
		   });
		me.items = [{
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
			items : [{
						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [ 
									{
										xtype : 'datefield',
										name : 'accept_time',
										columnWidth : .50,
										labelWidth : 80,
										fieldLabel : "受理时间<span style='color:red'>*</span>",
										labelAlign : 'right',
										readOnly:true,
										format : 'Y-m-d',
										allowBlank : false,// 不允许为空
										maxValue : new Date(),
										value : Ext.Date.add(
												new Date(),
												Ext.Date.DAY),
									} ]
						},{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [
							         {xtype:"hiddenfield",name:"case_type",value:"1"},new Ext.form.field.ComboBox(
												{
													fieldLabel : '案例来源',
													columnWidth : .50,
													labelWidth : 80,
													editable : false,
													triggerAction : 'all',
													displayField : 'Name',
													labelAlign : 'right',
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
													value:deptcode.indexOf("1005")?"0":"1",
													name : 'source_type'
												}),
									{
										// 该列在整行中所占的百分比
										columnWidth : .50,
										xtype : "textfield",
										labelAlign : 'right',
										labelWidth : 80,
										readOnly: true,
										fieldLabel : "案例条形码<span style='color:red'>*</span>",
										name : 'case_code',
										hidden:true
//										emptyText:'自动生成',
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
															fields : ['Name','Code' ],
															data : [['普通',0 ],['48小时',1 ] ,['24小时',2 ],['8小时',3 ]]
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
							items : [sample_in_per,
//							         {
//								// 该列在整行中所占的百分比
//								columnWidth : .50,
//								xtype : "textfield",
//								labelAlign : 'right',
//								fieldLabel : '采样人员<span style="color:red">*</span>',
//								labelWidth : 80,
//								allowBlank:false, //不允许为空
//								maxLength : 50,
//								name : 'sample_in_per',
//							}, 
							unit_type ]
				     },{
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
							}, modeltype ]
				     },{
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
									}]
				     },{
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
				     },{
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
				     },{
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
							xtype : 'fieldset',
							title : '案例照片(可不传)',
							id:'testFieldset',
							layout : 'anchor',
							defaults : {
								anchor : '100%'
							},
							items : [{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ 	 {
									xtype : 'filefield',
									name : 'files',
									fieldLabel : '文件',
									msgTarget : 'side',
//									allowBlank : false,
									labelWidth : 40,
									anchor : '100%',
									buttonText : '选择文件',
									columnWidth : .25,
								},new Ext.form.field.ComboBox({
									fieldLabel : '文件类型',
									labelWidth : 70,
									labelAlign : 'right',
									fieldStyle : me.fieldStyle,
									editable : false,
//									allowBlank : false,
									triggerAction : 'all',
									displayField : 'Name',
									valueField : 'Code',
									store : new Ext.data.ArrayStore({
												fields : ['Name', 'Code'],
												data : [['登记表格', 1], ['身份证', 2],
														['照片', 3], ['其他', 4]]
											}),
									mode : 'local',
									name : 'filetype',
									value:1,
									readOnly:true,
									columnWidth : .2
								})]
							},{
								xtype : 'panel',
								layout : 'absolute',
								border : false,
								items : [{
									xtype : 'button',
									text : '增加文件',
									width : 100,
									style:'margin-bottom:10px',
									x : 0,
									handler : function() {
//										var me = this.up('form');
										var me = Ext.getCmp("testFieldset");
										me.add({
											xtype : 'form',
											style : 'margin-top:5px;',
											layout : 'column',
											border : false,
											items : [{
														xtype : 'filefield',
														name : 'files',
														columnWidth : .25,
														fieldLabel : '文件<span style="color:red">*</span>',
														labelWidth : 40,
														msgTarget : 'side',
														allowBlank : false,
														anchor : '100%',
														style:'margin-bottom:10px',
														buttonText : '选择文件'
													}, new Ext.form.field.ComboBox({
														fieldLabel : '文件类型<span style="color:red">*</span>',
														labelWidth : 70,
														fieldStyle : me.fieldStyle,
														editable : false,
														allowBlank : false,
														labelAlign : 'right',
														triggerAction : 'all',
														displayField : 'Name',
														valueField : 'Code',
														store : new Ext.data.ArrayStore(
																{
																	fields : ['Name',
																			'Code'],
																	data : [
																			['登记表格', 1],
																			['身份证', 2],
																			['照片', 3],
																			['其他', 4]]
																}),
														mode : 'local',
														name : 'filetype',
														columnWidth : .2
													}), {
														xtype : 'button',
														style : 'margin-left:15px;',
														text : '删除',
														handler : function() {
															var me = this.up("form");
															console.log(me);
															me.disable(true);
															me.up("fieldset").remove(me);
														}
													}]
										});
									}
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
				     },{
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
																				allowBlank : false,// 不允许为空
																				blankText : "不能为空",// 错误提示信息，默认为This
																				labelAlign : 'right',
																				regex:/^\w+$/,
																				regexText:'请输入正确条形码',																				maxLength : 50,
																				labelWidth : 80,
																				name : 'sample_code',
																				listeners:{
																			        blur: function(combo, record, index){
																			        	var sample_code= trim(combo.getValue());
																			        	Ext.Ajax.request({
																							url : "judicial/register/exsitSampleCode.do",
																							method : "POST",
																							async : false,
																							headers : {
																								'Content-Type' : 'application/json'
																							},
																							jsonData : {
																								sample_code : sample_code
																							},
																							success : function(response,options) {
																								response = Ext.JSON.decode(response.responseText);
																								if (!response) {
																									Ext.Msg.alert("提示","此"+sample_code+"条形码系统已存在，请核实无误后录入！");
																								}
																							},
																							failure : function() {
																								Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
																							}
																						});
																			        }
																			    }
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
																                        data: [['否', '0'], ['特殊1', '1'],['特殊2', '2'],['特殊3', '3']]
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
															                        		if( combo.getValue() == 'cy01'|| combo.getValue()== 'cy02' || combo.getValue()== 'cy03')
																                        		mei.getForm().findField('special').setValue('0');
															                        		else
															                        			mei.getForm().findField('special').setValue('2');
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
//																				emptyText : 'yyyy-MM-dd',
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
				     }]
		}];

		me.buttons = [{
					text : '保存',
					iconCls : 'Disk',
					handler : me.onSave
				},{
					text : '保存并提交审核',
					iconCls : 'Disk',
					handler : me.onSaveSubmit
				}, {
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	onSaveSubmit:function(){
		var me = this;
		var myWindow = me.up('window');
		var form = me.up('form').getForm();
		var values = form.getValues();
		
		if(values["express_num"]=="" || values["express_type"]=="")
		{
			Ext.MessageBox.alert("提示信息","直接提交审核需要填写样本邮寄信息！");
			return;
		}
		var sample_str = values['sample_code'];
		if(!(sample_str instanceof Array))
		{
			Ext.MessageBox.alert("提示信息","样本填写错误！");
			return;
		}else
		{
			for(var i=0;i<sample_str.length-1;i++){ 
				for(var j = i+1; j < sample_str.length; j ++)
				{
					console.log(sample_str[i]+"-----"+sample_str[j])
					if (sample_str[i]==sample_str[j]){ 
						Ext.MessageBox.alert("提示信息","重复样本条形码："+sample_str[i]);
						return;
					} 
				}
			} 
		}
		var id_numbers = values['id_number'];
		var temp='';
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
			if (father_num == 0 && mother_num == 0) {
				Ext.MessageBox.alert("错误信息", "非医学亲子案例，请用医学->其他新增录入");
				return;
			}
			form.submit({
				url : 'judicial/register/saveCaseInfo.do',
				method : 'post',
				waitMsg : '正在保存...',
				params:{'receiver_area':Ext.getCmp('case_areacode').getRawValue(),
						'sample_code':values["sample_code"],
						'sample_username':values["sample_username"],
						'id_number':values["id_number"],
						'special':values["special"],
						'sample_type':values["sample_type"],
						'sample_call':values["sample_call"],
						'birth_date':values["birth_date"],
						'sign':'PC',
						'submitFlag':'1'
					},
				success : function(form, action) {
					response = Ext.JSON
							.decode(action.response.responseText);
					console.log(response);
					if (response.result) {
						Ext.MessageBox.alert("提示信息", response.message);
						me.up('form').grid.getStore().load();
						myWindow.close();
					} else {
						Ext.MessageBox.alert("提示信息", response.message);
//						myWindow.close();
					}
				},
				failure : function() {
					Ext.Msg.alert("提示", "保存失败，请联系管理员!");
					myWindow.close();
				}
			});
			
		}else
		{
			Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
			return;
		}
		

	
	},
	onSave : function() {
		var me = this;
		var myWindow = me.up('window');
		var form = me.up('form').getForm();
		var values = form.getValues();
		var sample_str = values['sample_code'];
		if(!(sample_str instanceof Array))
		{
			Ext.MessageBox.alert("提示信息","样本填写错误！");
			return;
		}else
		{
			for(var i=0;i<sample_str.length-1;i++){ 
				for(var j = i+1; j < sample_str.length; j ++)
				{
					console.log(sample_str[i]+"-----"+sample_str[j])
					if (sample_str[i]==sample_str[j]){ 
						Ext.MessageBox.alert("提示信息","重复样本条形码："+sample_str[i]);
						return;
					} 
				}
			} 
		}
		var id_numbers = values['id_number'];
		var temp='';
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
			if (father_num == 0 && mother_num == 0) {
				Ext.MessageBox.alert("错误信息", "非医学亲子案例，请用医学->其他新增录入");
				return;
			}
			form.submit({
				url : 'judicial/register/saveCaseInfo.do',
				method : 'post',
				waitMsg : '正在保存...',
				params:{'receiver_area':Ext.getCmp('case_areacode').getRawValue(),
						'sample_code':values["sample_code"],
						'sample_username':values["sample_username"],
						'id_number':values["id_number"],
						'special':values["special"],
						'sample_type':values["sample_type"],
						'sample_call':values["sample_call"],
						'birth_date':values["birth_date"],
						'sign':'PC'
					},
				success : function(form, action) {
					response = Ext.JSON
							.decode(action.response.responseText);
					console.log(response);
					if (response.result) {
						Ext.MessageBox.alert("提示信息", response.message);
						me.up('form').grid.getStore().load();
						myWindow.close();
					} else {
						Ext.MessageBox.alert("提示信息", response.message);
//						myWindow.close();
					}
				},
				failure : function() {
					Ext.Msg.alert("提示", "保存失败，请联系管理员!");
//					myWindow.close();
				}
			});
			
		}else
		{
			Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
			return;
		}
		

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}

});