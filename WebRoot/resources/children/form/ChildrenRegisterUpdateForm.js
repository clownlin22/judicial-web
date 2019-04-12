var storeCall = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'ajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getCustodyCall.do',
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
var agentiaStore = Ext.create('Ext.data.Store', {
			fields : ['agentia_id', 'agentia_name'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'children/agentia/getAgentiaCombo.do',
				params : {},
				reader : {
					type : 'json',
					root : 'data'
				}
			}
		});
var tariffStore = Ext.create('Ext.data.Store', {
			fields : ['tariff_id', 'tariff_name', 'tariff_price'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'children/register/getTariff.do',
				params : {},
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true
		});
Ext.define('Rds.children.form.ChildrenRegisterUpdateForm', {
	extend : 'Ext.form.Panel',
	initComponent : function() {
		var me = this;
		var case_userid = Ext.create('Ext.form.ComboBox', {
			xtype : 'combo',
			columnWidth : .50,
			fieldLabel : '归属人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			labelWidth : 80,
			labelAlign : 'right',
			name : 'case_userid_update',
			id:'case_userid_update',
			emptyText:'检索方式(姓名首字母)：如小红(xh)',
			store : Ext.create("Ext.data.Store",{
				 fields:[
							{name:'key',mapping:'key',type:'string'},
							{name:'value',mapping:'value',type:'string'}
			          ],
				pageSize : 10,
				proxy : {
					type : "ajax",
					url:'judicial/dicvalues/getUsersId.do?userid='+ownpersonTemp,
					reader : {
						type : "json"
					}
				}
			}),
			displayField : 'value',
			valueField : 'key',
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
			},
			listeners : {
				'afterrender':function(){
					console.log(ownpersonTemp);
					if("" != ownpersonTemp)
					{
						this.store.load();
					}
				}
			}
		});
		var case_areacode=Ext.create('Ext.form.ComboBox',{
			xtype : 'combo',
			columnWidth : .50,
			fieldLabel : '归属地区<span style="color:red">*</span>',
			labelWidth : 80,
			labelAlign : 'right',
			name : 'case_areacode_update',
			id:'case_areacode_update',
			allowBlank  : false,//不允许为空  
            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
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
					url:"judicial/dicvalues/getAreaInfo.do?area_code="+ownaddressTemp,
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
			},
			listeners : {
				'afterrender':function(){
					if("" != ownaddressTemp)
					{
						this.store.load();
					}
				}
			}
          });
		var tariff=Ext.create('Ext.form.ComboBox',{
				name : 'tariff_id',
				fieldLabel : '套餐类型',
				columnWidth : .5,
				labelWidth : 80,
				editable : false,
				mode : 'remote',
				forceSelection : true,
				allowBlank : true,
				triggerAction : 'all',
				displayField : 'tariff_name',
				labelAlign : 'right',
				valueField : 'tariff_id',
				store : tariffStore,
				listeners : {
					change : function(value, newValue, oldValue,
							eOpts) {
						stand_sum.setValue(tariffStore.findRecord(
										"tariff_id", newValue)
										.get("tariff_price"));
					}
				}
		  });
		var stand_sum=new Ext.form.TextField({
			// 该列在整行中所占的百分比
			columnWidth : .5,
			xtype : "textfield",
			labelAlign : 'right',
			fieldLabel : "标准金额<span style='color:red'>*</span>",
			allowBlank : false,// 不允许为空
			blankText : "不能为空",// 错误提示信息，默认为This field is
			readOnly:true,
			labelWidth : 80,
			maxLength : 50,
			name : 'stand_sum'
			});
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
			items : [ {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'hiddenfield',
									name : 'case_id'
								}, {
									xtype : 'hiddenfield',
									name : 'case_userid'
								}, {
									xtype : 'hiddenfield',
									name : 'case_areacode'
								}, {
									xtype : 'hiddenfield',
									name : 'gather_id'
								},{
									// 该列在整行中所占的百分比
									columnWidth : .5,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '案例条形码',
									readOnly : true,
									maxLength : 50,
									name : 'case_code'
								}]
					},{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [case_userid,case_areacode]
					}, {
						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .5,
									xtype : "textfield",
									fieldLabel : "样本编号",
									labelWidth : 80,
									labelAlign : 'right',
									maxLength : 50,
									name : 'sample_code',
									regex:/^[^\s]*$/,
									regexText:'请输入正确样本编号'
								},{
									xtype : 'datefield',
									name : 'gather_time',
									columnWidth : .45,
									labelWidth : 80,
									fieldLabel : '采集时间',
									labelAlign : 'right',
									format : 'Y-m-d',
									allowBlank : false,// 不允许为空
									maxValue : new Date(),
									value : Ext.Date.add(new Date(),
											Ext.Date.DAY)

								}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'textarea',
									fieldLabel : "详细地址<span style='color:red'>*</span>",
									name : 'address',
									width : 500,
									allowBlank : false,// 不允许为空
									blankText : "不能为空",// 错误提示信息，默认为This field
									maxLength : 500,
									columnWidth : 1,
									labelWidth : 80,
									labelAlign : 'right'
								}]
					},{
						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [{
									// 该列在整行中所占的百分比
									columnWidth : .5,
									xtype : "textfield",
									labelAlign : 'right',
									fieldLabel : "儿童姓名<span style='color:red'>*</span>",
									allowBlank : false,// 不允许为空
									blankText : "不能为空",// 错误提示信息，默认为This field
									labelWidth : 80,
									maxLength : 50,
									name : 'child_name'

								}, {
									columnWidth : .5,
									xtype : "textfield",
									fieldLabel : '身份证',
									labelAlign : 'right',
									maxLength : 18,
									labelWidth : 80,
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
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'datefield',
									name : 'birth_date',
									columnWidth : .5,
									labelWidth : 80,
									fieldLabel : '出生时间',
									labelAlign : 'right',
									format : 'Y-m-d',
									allowBlank : false,// 不允许为空
									maxValue : new Date(),
									value : Ext.Date.add(new Date(),
											Ext.Date.DAY)

								}, new Ext.form.field.ComboBox({
											fieldLabel : '性别',
											columnWidth : .5,
											labelWidth : 80,
											editable : false,
											triggerAction : 'all',
											displayField : 'Name',
											labelAlign : 'right',
											valueField : 'Code',
											store : new Ext.data.ArrayStore({
														fields : ['Name',
																'Code'],
														data : [['女', 0],
																['男', 1]]
													}),
											value : 0,
											mode : 'local',
											// typeAhead: true,
											name : 'child_sex'

										})]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'textarea',
									fieldLabel : '出生医院',
									name : 'birth_hospital',
									width : 500,
									maxLength : 500,
									columnWidth : 0.5,
									labelWidth : 80,
									labelAlign : 'right'
								},{
									xtype : 'textarea',
									fieldLabel : '户籍所在地',
									name : 'house_area',
									width : 500,
									maxLength : 500,
									columnWidth : 0.5,
									labelWidth : 80,
									labelAlign : 'right'
								}]
					}, {
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [{
									xtype : 'textarea',
									fieldLabel : '生活所在地',
									name : 'life_area',
									width : 500,
									maxLength : 500,
									columnWidth : 0.5,
									labelWidth : 80,
									labelAlign : 'right'
								},{
									xtype : 'textarea',
									fieldLabel : '反馈寄送地',
									name : 'mail_area',
									width : 500,
									maxLength : 500,
									columnWidth : 0.5,
									labelWidth : 80,
									labelAlign : 'right'
								}]
					},{
						xtype : 'fieldset',
						title : '案例财务信息',
						layout : 'anchor',
						defaults : {
							anchor : '100%'
						},
						items : [{
							layout : "column",// 从左往右的布局
							xtype : 'fieldcontainer',
							border : false,
							items : [tariff,stand_sum]
						}]
					}, {
						xtype : 'fieldset',
						title : '案例邮寄信息',
						layout : 'anchor',
						defaults : {
							anchor : '100%'
						},
						items : [{
							layout : "column",// 从左往右的布局
							xtype : 'fieldcontainer',
							border : false,
							items : [{
								// 该列在整行中所占的百分比
								columnWidth : .5,
								xtype : "textfield",
								labelAlign : 'right',
								padding : "0 0 5 0",
								fieldLabel : '快递名称',
								allowBlank : true,// 不允许为空
								labelWidth : 80,
								maxLength : 50,
								name : 'mail_name'
							},{
								// 该列在整行中所占的百分比
								columnWidth : .5,
								xtype : "textfield",
								padding : "0 0 5 0",
								labelAlign : 'right',
								fieldLabel : '快递编号',
								allowBlank : true,// 不允许为空
								labelWidth : 80,
								maxLength : 50,
								name : 'mail_code'
							}]
						}]
					},{
						xtype : 'fieldset',
						title : '案例采集人信息',
						layout : 'anchor',
						defaults : {
							anchor : '100%'
						},
						items : [{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [{
										columnWidth : .45,
										xtype : "textfield",
										fieldLabel : '采集人姓名',
										labelAlign : 'right',
										maxLength : 50,
										labelWidth : 80,
										name : 'gather_name'
									}, {
										columnWidth : .45,
										xtype : "textfield",
										fieldLabel : '采集人身份证',
										labelAlign : 'right',
										maxLength : 18,
										labelWidth : 120,
										name : 'gather_id_number',
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
													success : function(response, options) {
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
																"网络异常<br>请联系管理员!");
													}
												});
											} else {
												result = true;
											}
											return result;
										}
									}]
						}, {
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [{
										columnWidth : .45,
										xtype : "textfield",
										fieldLabel : '手机号码',
										labelAlign : 'right',
										maxLength : 20,
										labelWidth : 80,
										name : 'gather_phone'
									}, {
										columnWidth : .45,
										xtype : "textfield",
										fieldLabel : '所属单位',
										labelAlign : 'right',
										maxLength : 500,
										labelWidth : 120,
										name : 'gather_company_name'
									}]
						}]
					}, {
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
//					{
//						border : false,
//						xtype : 'fieldcontainer',
//						layout : "column",
//						items : [new Ext.form.field.ComboBox({
//									name : 'agentia_id',
//									fieldLabel : "试剂类型<span style='color:red'>*</span>",
//									columnWidth : .45,
//									labelWidth : 80,
//									editable : false,
//									mode : 'remote',
//									forceSelection : true,
//									allowBlank : false,// 不允许为空
//									blankText : "不能为空",// 错误提示信息，默认为This
//									triggerAction : 'all',
//									displayField : 'agentia_name',
//									labelAlign : 'right',
//									valueField : 'agentia_id',
//									hiddenname : 'agentia_id',
//									store : agentiaStore
//								})]
//					}, 
					{
						layout : 'absolute',// 从左往右的布局
						xtype : 'panel',
						border : false,
						items : [{
							xtype : 'button',
							text : '添加监护人',
							width : 100,
							x : '90%',
							listeners : {
								click : function() {
									var me = this.up("form");
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
										items : [{
											layout : 'auto',
											xtype : 'panel',
											border : false,
											items : [{
												layout : 'column',
												xtype : 'panel',
												border : false,
												items : [{
															columnWidth : .2,
															xtype : "textfield",
															fieldLabel : "监护人姓名<span style='color:red'>*</span>",
															allowBlank : false,// 不允许为空
															blankText : "不能为空",// 错误提示信息，默认为This
															labelAlign : 'right',
															maxLength : 50,
															labelWidth : 80,
															name : 'custody_name'

														}, {
															columnWidth : .2,
															xtype : "textfield",
															fieldLabel : '身份证',
															labelAlign : 'right',
															maxLength : 18,
															labelWidth : 80,
															name : 'custody_id_number',
															validator : function(
																	value) {
																var result = false;
																if (value != null
																		&& value != "") {
																	Ext.Ajax
																			.request(
																					{
																						url : "judicial/register/verifyId_Number.do",
																						method : "POST",
																						async : false,
																						headers : {
																							'Content-Type' : 'application/json'
																						},
																						jsonData : {
																							id_number : value
																						},
																						success : function(
																								response,
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
																							Ext.Msg
																									.alert(
																											"提示",
																											"网络异常<br>请联系管理员!");
																						}
																					});
																} else {
																	result = true;
																}
																return result;
															}
														},{
															columnWidth : .2,
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
															store : storeCall,
															fieldLabel : "称谓<span style='color:red'>*</span>",
															maxLength : 50,
															labelWidth : 80,
															name : 'custody_call'
														}, {
															columnWidth : .2,
															xtype : "textfield",
															fieldLabel : '手机号码',
															labelAlign : 'right',
															maxLength : 20,
															labelWidth : 80,
															name : 'custody_phone'
														}, {
															layout : 'absolute',// 从左往右的布局
															xtype : 'panel',
															border : false,
															columnWidth : .15,
															items : [{
																width : 50,
																x : 30,
																xtype : 'button',
																text : '删除',
																listeners : {
																	click : function() {
																		var mei = this
																				.up("form");
																		mei
																				.disable(true);
																		mei
																				.up("form")
																				.remove(mei);
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

		me.buttons = [{
					text : '修改',
					iconCls : 'Disk',
					handler : me.onUpdate
				},{
					text : '修改并提交审核',
					iconCls : 'Disk',
					handler : me.onUpdateCommit
				}, {
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	onUpdate : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if (form.isValid()) {
			Ext.MessageBox.wait('正在操作','请稍后...');
			values["case_areacode"] = Ext.getCmp("case_areacode_update").getValue();
			values["case_areaname"]= Ext.getCmp("case_areacode_update").getRawValue();
			values["case_userid"] = Ext.getCmp("case_userid_update").getValue();
			values["case_username"] =Ext.getCmp("case_userid_update").getRawValue();
			var id_result = true;
			var gather_result = true;
			var child_sex = values['child_sex'];
			var id_number = values['id_number'];
			var custody_id_number = values['custody_id_number'];
			var custody_call = values['custody_call'];
			var custody_name = values['custody_name'];
			var gather_name = values['gather_name'];
			var gather_id_number = values['gather_id_number'];
			var gather_phone = values['gather_phone'];
			var gather_company_name = values['gather_company_name'];
			if (Ext.util.Format.trim(id_number) != '') {
				var num = id_number.substring(16, id_number.length - 1);
				if (num % 2 != child_sex) {
					id_result = false;
				}
			}
			if (gather_id_number != "" || gather_phone != ""
					|| gather_company_name != "") {
				if (Ext.util.Format.trim(gather_name) == "") {
					gather_result = false;
				}
			}
			if (custody_call) {
				if (!((typeof custody_name == 'string') && custody_name.constructor == String)) {
					for (var i = 0; i < custody_call.length; i++) {
						for (var j = 0; j < custody_id_number.length; j++) {
							if (i == j) {
								if (Ext.util.Format.trim(custody_id_number[j]) != '') {
									var num = custody_id_number[j]
											.substring(16,
													custody_id_number[j].length
															- 1);
									if (custody_call[i] == 'mother') {
										if (num % 2 != 0) {
											id_result = false;
										}
									} else if (custody_call[i] == 'father') {
										if (num % 2 == 0) {
											id_result = false;
										}
									}
								}
							}
						}
					}
				} else {
					if (Ext.util.Format.trim(custody_id_number) != '') {
						var num = custody_id_number.substring(16,
								custody_id_number.length - 1);
						if (custody_call == 'mother') {
							if (num % 2 != 0) {
								id_result = false;
							}
						} else if (custody_call == 'father') {
							if (num % 2 == 0) {
								id_result = false;
							}
						}
					}
				}
			}
			var father_num = 0, mother_num = 0;
			if (custody_call instanceof Array) {
				for (var i = 0; i < custody_call.length; i++) {
					if(custody_call[i]=='father')
						father_num++;
					if(custody_call[i]=='mother')
						mother_num++;
					
				}
			} 
			if (father_num > 1 || mother_num > 1) {
				Ext.MessageBox.alert("错误信息", "家长称谓出现重复");
				return;
			}
			if (!id_result) {
				Ext.MessageBox.alert("错误信息", "存在身份证信息不匹配的数据!");
				return;
			}
			if (!gather_result) {
				Ext.MessageBox.alert("错误信息", "添加采集备注内容时必须填写采集人姓名!");
				return;
			}
			values.case_type='children';
			Ext.Ajax.request({
						url : "children/register/updateCaseInfo.do",
						method : "POST",
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : values,
						success : function(response, options) {
							response = Ext.JSON.decode(response.responseText);
							if (response.result == true) {
								Ext.MessageBox.alert("提示信息", "修改案例成功");
								me.grid.getStore().load();
								me.up("window").close();
							} else {
								Ext.MessageBox.alert("错误信息", response.msg);
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
						}
					});
		}
	},
	onUpdateCommit:function(){
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if (form.isValid()) {
			Ext.MessageBox.wait('正在操作','请稍后...');
			values["case_areacode"] = Ext.getCmp("case_areacode_update").getValue();
			values["case_areaname"]= Ext.getCmp("case_areacode_update").getRawValue();
			values["case_userid"] = Ext.getCmp("case_userid_update").getValue();
			values["case_username"] =Ext.getCmp("case_userid_update").getRawValue();
			values.submitFlag=1;
			var id_result = true;
			var gather_result = true;
			var child_sex = values['child_sex'];
			var id_number = values['id_number'];
			var custody_id_number = values['custody_id_number'];
			var custody_call = values['custody_call'];
			var custody_name = values['custody_name'];
			var gather_name = values['gather_name'];
			var gather_id_number = values['gather_id_number'];
			var gather_phone = values['gather_phone'];
			var gather_company_name = values['gather_company_name'];
			if (Ext.util.Format.trim(id_number) != '') {
				var num = id_number.substring(16, id_number.length - 1);
				if (num % 2 != child_sex) {
					id_result = false;
				}
			}
			if (gather_id_number != "" || gather_phone != ""
					|| gather_company_name != "") {
				if (Ext.util.Format.trim(gather_name) == "") {
					gather_result = false;
				}
			}
			if (custody_call) {
				if (!((typeof custody_name == 'string') && custody_name.constructor == String)) {
					for (var i = 0; i < custody_call.length; i++) {
						for (var j = 0; j < custody_id_number.length; j++) {
							if (i == j) {
								if (Ext.util.Format.trim(custody_id_number[j]) != '') {
									var num = custody_id_number[j]
											.substring(16,
													custody_id_number[j].length
															- 1);
									if (custody_call[i] == 'mother') {
										if (num % 2 != 0) {
											id_result = false;
										}
									} else if (custody_call[i] == 'father') {
										if (num % 2 == 0) {
											id_result = false;
										}
									}
								}
							}
						}
					}
				} else {
					if (Ext.util.Format.trim(custody_id_number) != '') {
						var num = custody_id_number.substring(16,
								custody_id_number.length - 1);
						if (custody_call == 'mother') {
							if (num % 2 != 0) {
								id_result = false;
							}
						} else if (custody_call == 'father') {
							if (num % 2 == 0) {
								id_result = false;
							}
						}
					}
				}
			}
			var father_num = 0, mother_num = 0;
			if (custody_call instanceof Array) {
				for (var i = 0; i < custody_call.length; i++) {
					if(custody_call[i]=='father')
						father_num++;
					if(custody_call[i]=='mother')
						mother_num++;
					
				}
			} 
			if (father_num > 1 || mother_num > 1) {
				Ext.MessageBox.alert("错误信息", "家长称谓出现重复");
				return;
			}
			if (!id_result) {
				Ext.MessageBox.alert("错误信息", "存在身份证信息不匹配的数据!");
				return;
			}
			if (!gather_result) {
				Ext.MessageBox.alert("错误信息", "添加采集备注内容时必须填写采集人姓名!");
				return;
			}
			values.case_type='children';
			Ext.Ajax.request({
						url : "children/register/updateCaseInfo.do",
						method : "POST",
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : values,
						success : function(response, options) {
							response = Ext.JSON.decode(response.responseText);
							if (response.result == true) {
								Ext.MessageBox.alert("提示信息", "修改案例成功");
								me.grid.getStore().load();
								me.up("window").close();
							} else {
								Ext.MessageBox.alert("错误信息", response.msg);
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
						}
					});
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
			var case_id = values["case_id"];
			var gather_id = values["gather_id"];
			agentiaStore.load();
			tariffStore.load();
			//归属地设置
			var area = me.down("combo[name=case_areacode_update]");
			area.setValue(values["case_areacode"]);
			//归属人
			var case_userid = me.down("combo[name=case_userid_update]");
			case_userid.setValue(values["case_userid"]);
			if (gather_id) {
				Ext.Ajax.request({
							url : "children/register/getGatherInfo.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : {
								'gather_id' : gather_id
							},
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								var gather_name = me.down("textfield[name=gather_name]");
								gather_name.setValue(response.gather_name);
								var gather_id_number = me.down("textfield[name=gather_id_number]");
								gather_id_number.setValue(response.id_number);
								var gather_phone = me.down("textfield[name=gather_phone]");
								gather_phone.setValue(response.phone);
								var gather_company_name = me.down("textfield[name=gather_company_name]");
								gather_company_name.setValue(response.company_name);
							},
							failure : function() {
								return;
							}
						});
			}
			Ext.Ajax.request({
				url : "children/register/getCustodyInfo.do",
				method : "POST",
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : {
					'case_id' : case_id
				},
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					if (response.length > 0) {
							for (var i = 0; i < response.length; i++) {
								me.down("form").add({
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
										layout : 'auto',
										xtype : 'panel',
										border : false,
										items : [{
											layout : 'column',
											xtype : 'panel',
											border : false,
											items : [{
												columnWidth : .2,
												xtype : "textfield",
												fieldLabel : "监护人姓名<span style='color:red'>*</span>",
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												labelAlign : 'right',
												maxLength : 50,
												value : response[i].custody_name,
												labelWidth : 80,
												name : 'custody_name'

											}, {
												columnWidth : .2,
												xtype : "textfield",
												fieldLabel : '身份证',
												labelAlign : 'right',
												maxLength : 18,
												labelWidth : 80,
												value : response[i].id_number,
												name : 'custody_id_number',
												validator : function(value) {
													var result = false;
													if (value != null
															&& value != "") {
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
															success : function(
																	response,
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
																Ext.Msg
																		.alert(
																				"提示",
																				"网络异常<br>请联系管理员!");
															}
														});
													} else {
														result = true;
													}
													return result;
												}
											},{
												columnWidth : .2,
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
												store : storeCall,
												value : response[i].custody_call,
												fieldLabel : "称谓<span style='color:red'>*</span>",
												maxLength : 50,
												labelWidth : 80,
												name : 'custody_call'
											}, {
												columnWidth : .2,
												xtype : "textfield",
												fieldLabel : '手机号码',
												labelAlign : 'right',
												maxLength : 20,
												value : response[i].phone,
												labelWidth : 80,
												name : 'custody_phone'

											}, {
												layout : 'absolute',// 从左往右的布局
												xtype : 'panel',
												border : false,
												columnWidth : .15,
												items : [{
													width : 50,
													x : 30,
													xtype : 'button',
													text : '删除',
													listeners : {
														click : function() {
															var mei = this
																	.up("form");
															mei.disable(true);
															mei
																	.up("form")
																	.remove(mei);
														}
													}
												}]
											}]
										}]
									}]
								});
							}
					}
				},
				failure : function() {
					return;
				}
			});
		}
	}
});
