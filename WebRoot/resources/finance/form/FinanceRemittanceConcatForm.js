Ext.define("Rds.finance.form.FinanceRemittanceConcatForm", {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'form',
			region : 'center',
			name : 'data',
			style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
			labelAlign : "right",
			bodyPadding : 10,
			defaults : {
				anchor : '100%',
				labelWidth : 100
			},
			border : false,
			autoHeight : true,
			items : [{
							xtype : "datefield",
							fieldLabel : "受理时间<span style='color:red'>*</span>",
							mode : 'local',
							labelAlign : 'right',
							blankText : '请选择',
							emptyText : '请选择',
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This
							maxLength : 50,
							labelWidth : 70,
							format : 'Y-m-d',
							name : 'accept_date'
						},{
			    			xtype:"textfield",
			    			fieldLabel: '合作单位<span style="color:red">*</span>',
			    			labelWidth:70,
			    			labelAlign : 'right',
			    			fieldStyle:me.fieldStyle, 
			    			name: 'contract_unit',
			    			maxValue:1000,
			    			allowBlank:false
			    		} ,{
			    			xtype:"textfield",
			    			fieldLabel: '合同项目<span style="color:red">*</span>',
			    			labelWidth:70,
			    			labelAlign : 'right',
			    			fieldStyle:me.fieldStyle, 
			    			name: 'contract_program',
			    			maxValue:1000,
			    			allowBlank:false
			    		} ,{
			    			xtype:"textfield",
			    			fieldLabel: '客户名称',
			    			labelWidth:70,
			    			labelAlign : 'right',
			    			fieldStyle:me.fieldStyle, 
			    			name: 'customer_name',
			    			maxValue:500,
			    		} ,{
			    			xtype:"numberfield",
			    			fieldLabel: '合同金额<span style="color:red">*</span>',
			    			labelWidth:70,
			    			labelAlign : 'right',
			    			fieldStyle:me.fieldStyle, 
			    			name: 'contract_price',
			    			maxValue:10000000,
			    			allowBlank:false
			    		} ,Ext.create('Ext.form.ComboBox', {
							xtype : 'combo',
							fieldLabel : '归属人<span style="color:red">*</span>',
							labelWidth : 70,
							name : 'contract_userid',
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
							allowBlank:false,
							forceSelection: true,
							hideTrigger : true,
							minChars : 2,
							matchFieldWidth : true,
							listConfig : {
								loadingText : '正在查找...',
								emptyText : '没有找到匹配的数据'
							}
						}),{
							xtype : "combo",
	        				fieldLabel : '归属地区<span style="color:red">*</span>',
	        				labelWidth : 70,
	        				labelAlign : 'right',
	        				name : 'contract_areacode',
	        				id:'contract_areacode',
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
						},{
			    			xtype:"textfield",
			    			fieldLabel: 'OA编号',
			    			labelWidth:70,
			    			labelAlign : 'right',
			    			fieldStyle:me.fieldStyle, 
			    			name: 'oa_num',
			    		}, {
			    			xtype:"textarea",
			    			fieldLabel: '备注',
			    			labelWidth:70,
			    			labelAlign : 'right',
			    			fieldStyle:me.fieldStyle, 
			    			name: 'remark',
			    		} ,{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [ {
								xtype : 'button',
								text : '添加回款',
								hidden:true,
								width : 100,
								x : 230,
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
													items : [ {
														layout : 'auto',
														xtype : 'panel',
														border : false,
														items : [	{
															layout : 'column',
															xtype : 'panel',
															border : false,
															items : [{xtype:'hiddenfield',name:'status',value:1},
																		{
																			columnWidth : .70,
																			xtype : "datefield",
																			fieldLabel : "汇款时间<span style='color:red'>*</span>",
																			mode : 'local',
																			labelAlign : 'right',
																			blankText : '请选择',
																			emptyText : '请选择',
																			allowBlank : false,// 不允许为空
																			blankText : "不能为空",// 错误提示信息，默认为This
																			maxLength : 50,
																			labelWidth : 80,
																			format : 'Y-m-d',
																			name : 'remittance_date',
																			minValue:new Date()
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
																				columnWidth : .70,
																				xtype : "numberfield",
																				fieldLabel : "回款金额<span style='color:red'>*</span>",
																				mode : 'local',
																				labelAlign : 'right',
																				blankText : '请选择',
																				emptyText : '请选择',
																				allowBlank : false,// 不允许为空
																				maxLength : 50,
																				labelWidth : 80,
																				name : 'remittance',
																			},
																			{
																				layout : 'absolute',// 从左往右的布局
																				xtype : 'panel',
																				border : false,
																				columnWidth : .30,
																				items : [ {
																					width : 50,
																					x : 30,
																					xtype : 'button',
																					text : '删除',
																					listeners : {
																						click : function() {
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
	onSave : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		values["contract_areaname"] = Ext.getCmp("contract_areacode").getRawValue();
		/*if(null == values["remittance"])
		{
			Ext.MessageBox.alert("提示信息", "回款计划都没有哦！");
			return;
		}
		
		if(values["remittance_date"] instanceof Array)
		{
			var sum = 0;
			for(var i = 0 ; i < values["remittance_date"].length ;i ++  )
			{
				sum += parseFloat(values["remittance"][i]);
			}
		}
		else
			sum = values["remittance"];
		
		if(sum != values["contract_price"])
		{
			Ext.MessageBox.alert("提示信息", "回款计划合同金额不匹配！");
			return;
		}*/
		
		if (form.isValid()) {
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({
				url : "finance/casefinance/insertContractPlan.do",
				method : "POST",
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : values,
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					if (response) {
						Ext.MessageBox.alert("提示信息", "操作成功！");
						me.grid.getStore().load();
						me.up("window").close();
					} else {
						Ext.MessageBox.alert("错误信息", "操作失败，请联系管理员！");
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
	}
});