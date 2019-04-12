Ext.define("Rds.finance.form.FinanceSpecialActivateForm", {
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
			items : [{xtype : 'hiddenfield',name:"id"},{
			                xtype: 'combo',
			                autoSelect : true,
			                allowBlank:false,
			        		fieldLabel: '案例状态',
			        		labelWidth : 60,
			        		emptyText : "请选择",
			        		triggerAction: 'all',
			     	        queryMode: 'local',
			     	        selectOnTab: true,
			     	        editable:false,  
			     	        forceSelection: true,
			     	        store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['优惠', '3'], ['先发报告后付款', '1'], ['月结', '4'],['免单', '2'],['退款', '5']]
							}),
			                displayField: 'Name',
			                valueField: 'Code',
			                name:'case_state',
			                listeners :{
	                        	"select" : function(combo, record, index){
	                        		  if(combo.getValue()=="3"){
	                        			  Ext.getCmp("preferential").setVisible(true);
	                        			  Ext.getCmp("discount_amount").setFieldLabel("优惠金额");
	                        			  Ext.getCmp("discount_amount").setVisible(true);
	                        			  Ext.getCmp("discount_amount").allowBlank = false;
	                        			  
	                        			  Ext.getCmp("code_amount").setFieldLabel("优惠码数量");
	                        			  Ext.getCmp("code_amount").setVisible(true);
	                        			  Ext.getCmp("code_amount").allowBlank = false;
	                        			  
	                        			  Ext.getCmp("case_type").setVisible(false);
	                        			  Ext.getCmp("case_type").allowBlank = true;
	                        			  
	                        			  Ext.getCmp("estimate_date").setVisible(false);
	                        			  Ext.getCmp("estimate_date").allowBlank = true;
	                        			  
	                        			  Ext.getCmp("monthly_per").setVisible(false);
	                        			  Ext.getCmp("monthly_per").allowBlank = true;
	                        			  
	                        			  Ext.getCmp("monthly_area").setVisible(false);
	                        			  Ext.getCmp("monthly_area").allowBlank = true;
	                        		  }else if(combo.getValue()=="1")
	                        		  {
	                        			  Ext.getCmp("preferential").setVisible(true);
	                        			  
	                        			  Ext.getCmp("case_type").setVisible(true);
	                        			  Ext.getCmp("case_type").allowBlank = false;
	                        			  
	                        			  Ext.getCmp("estimate_date").setVisible(true);
	                        			  Ext.getCmp("estimate_date").allowBlank = false;
	                        			  
	                        			  Ext.getCmp("discount_amount").setVisible(false);
	                        			  Ext.getCmp("discount_amount").allowBlank = true;
	                        			  
	                        			  Ext.getCmp("monthly_per").setVisible(true);
	                        			  Ext.getCmp("monthly_per").allowBlank = false;
	                        			  
	                        			  Ext.getCmp("monthly_area").setVisible(true);
	                        			  Ext.getCmp("monthly_area").allowBlank = false;
	                        			  
	                        			  Ext.getCmp("code_amount").setVisible(false);
	                        		  }else if(combo.getValue()=="4"){
	                        			  Ext.getCmp("preferential").setVisible(true);
	                        			  Ext.getCmp("code_amount").setVisible(false);

	                        			  Ext.getCmp("case_type").setVisible(true);
	                        			  Ext.getCmp("case_type").allowBlank = false;
	                        			  
	                        			  Ext.getCmp("monthly_per").setVisible(true);
	                        			  Ext.getCmp("monthly_per").allowBlank = false;
	                        			  
	                        			  Ext.getCmp("monthly_area").setVisible(true);
	                        			  Ext.getCmp("monthly_area").allowBlank = false;
	                        			  
	                        			  Ext.getCmp("estimate_date").setVisible(true);
	                        			  Ext.getCmp("estimate_date").allowBlank = false;
	                        			  
	                        			  Ext.getCmp("discount_amount").setVisible(false);
	                        			  Ext.getCmp("discount_amount").allowBlank = true;
	                        		  }else if(combo.getValue()=="2"){
	                        			  Ext.getCmp("preferential").setVisible(false);
	                        			  Ext.getCmp("preferential").allowBlank = true;
	                        			  Ext.getCmp("code_amount").setVisible(false);

	                        			  Ext.getCmp("monthly_per").setVisible(false);
	                        			  Ext.getCmp("monthly_per").allowBlank = true;
	                        			  
	                        			  Ext.getCmp("monthly_area").setVisible(false);
	                        			  Ext.getCmp("monthly_area").allowBlank = true;
	                        			  
	                        			  Ext.getCmp("case_type").setVisible(false);
	                        			  Ext.getCmp("case_type").allowBlank = true;
	                        			  
	                        			  Ext.getCmp("estimate_date").setVisible(false);
	                        			  Ext.getCmp("estimate_date").allowBlank = true;
	                        			  
	                        			  Ext.getCmp("discount_amount").setVisible(false);
	                        			  Ext.getCmp("discount_amount").allowBlank = true;
	                        		  }else if(combo.getValue()=="5"){
	                        			  Ext.getCmp("preferential").setVisible(true);
	                        			  Ext.getCmp("code_amount").setVisible(false);

	                        			  Ext.getCmp("discount_amount").setFieldLabel("退款金额");
	                        			  Ext.getCmp("discount_amount").setVisible(true);
	                        			  Ext.getCmp("discount_amount").allowBlank = false;
	                        			  
	                        			  Ext.getCmp("estimate_date").setVisible(false);
	                        			  Ext.getCmp("estimate_date").allowBlank = true;
	                        			  
	                        			  Ext.getCmp("case_type").setVisible(false);
	                        			  Ext.getCmp("case_type").allowBlank = true;
	                        			  
	                        			  Ext.getCmp("monthly_per").setVisible(false);
	                        			  Ext.getCmp("monthly_per").allowBlank = true;
	                        			  
	                        			  Ext.getCmp("monthly_area").setVisible(false);
	                        			  Ext.getCmp("monthly_area").allowBlank = true;
	                        		  }
	                        	}
	                        }
			            },{
		        				xtype : 'combo',
		        				fieldLabel : '申请人',
		        				labelWidth : 60,
		        				columnWidth : 1,
		        				allowBlank:false,
		        				name : 'apply_per',
		        				id:"apply_per",
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
		        				forceSelection: true,
		        				hideTrigger : true,
		        				minChars : 2,
		        				matchFieldWidth : true,
		        				listConfig : {
		        					loadingText : '正在查找...',
		        					emptyText : '没有找到匹配的数据'
		        				}
						},{
							xtype : 'datefield',
							fieldLabel : '申请时间',
							labelWidth : 60,
							columnWidth : .50,
							allowBlank:false,
							format : 'Y-m-d',
							labelAlign : 'left',
							name : 'apply_date',
							maxLength : 1000,
							value : Ext.Date.add(
									new Date(),
									Ext.Date.DAY),
						},{
							xtype : 'textfield',
							fieldLabel : 'oa编码',
							labelWidth : 60,
							columnWidth : .50,
							format : 'Y-m-d',
							labelAlign : 'left',
							name : 'oa_code',
							maxLength : 1000
						},
	        			{
							xtype : 'fieldset',
							title : '信息填写',
							id:'preferential',
							hidden:true,
							layout : 'anchor',
							defaults : {
								anchor : '100%'
							},
							items : [{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ {
									xtype : 'numberfield',
									fieldLabel : '优惠价格',
									labelWidth : 70,
									labelAlign : 'left',
									columnWidth : .70,
									name : 'discount_amount',
									id:'discount_amount',
									maxLength : 1000
								},
								{
									xtype : 'numberfield',
									fieldLabel : '优惠码数量',
									labelWidth : 70,
									labelAlign : 'left',
									columnWidth : .70,
									name : 'code_amount',
									id:'code_amount',
									minValue:1,
									value: 1,
									allowDecimals:false,
									maxLength : 1000
								}]
							},{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [{
					                xtype: 'combo',
					                autoSelect : true,
					                allowBlank:false,
					        		fieldLabel: '案例类型',
					        		labelWidth : 60,
					        		emptyText : "请选择",
									columnWidth : 1,
					        		triggerAction: 'all',
					     	        queryMode: 'local',
					     	        selectOnTab: true,
					     	        editable:false,  
					     	        forceSelection: true,
					     	        store : new Ext.data.ArrayStore({
										fields : ['Name', 'Code'],
										data : [['亲子鉴定', 'dna'], ['儿童基因库', 'children'],['无创产前', 'inversive']]
									}),
					                displayField: 'Name',
					                valueField: 'Code',
					                name:'case_type',
					                id:'case_type'
					            }]
							},{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [{
									xtype : 'datefield',
									fieldLabel : '预计结算日期',
									labelWidth : 90,
									columnWidth : 1,
									format : 'Y-m-d',
									labelAlign : 'left',
									name : 'estimate_date',
									id:'estimate_date',
									maxLength : 1000,
									minValue:new Date()
								}]
							},{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [{
			        				xtype : 'combo',
			        				fieldLabel : '结算人',
			        				labelWidth : 60,
			        				columnWidth : 1,
			        				name : 'monthly_per',
			        				id:"monthly_per",
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
			        				forceSelection: true,
			        				hideTrigger : true,
			        				minChars : 2,
			        				matchFieldWidth : true,
			        				listConfig : {
			        					loadingText : '正在查找...',
			        					emptyText : '没有找到匹配的数据'
			        				}
							}
							]
						},{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [{
			        		    	xtype : "combo",
			        				fieldLabel : '结算地区',
			        				labelWidth : 60,
			        				columnWidth : 1,
			        				id:'monthly_area',
			        				labelAlign : 'left',
			        				name : 'monthly_area',
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
			        				forceSelection: true,
			        				minChars : 2,
			        				matchFieldWidth : true,
			        				listConfig : {
			        					loadingText : '正在查找...',
			        					emptyText : '没有找到匹配的数据'
			        				}
			        			}]
							}
							]
						},{
			    			xtype:"textarea",
			    			fieldLabel: '备注',
			    			labelWidth:60,
			    			fieldStyle:me.fieldStyle, 
			    			name: 'remark',
			    		} ]
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
		console.log(values);
		if (form.isValid()) {
			Ext.MessageBox.confirm('提示', '请确认好填写信息，填写完毕后不可修改！', function(id) {
				if (id == 'yes') {
					Ext.MessageBox.wait('正在操作','请稍后...');
					console.log("" != values["monthly_per"]);
					if("" != values["monthly_per"])
					{
						Ext.Ajax.request({
							url : "finance/chargeStandard/queryFinanceSpecialExist.do",
							method : "POST",
							async: false,
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON.decode(response.responseText);
								if (response == true) {
									Ext.MessageBox.alert("提示信息", "该人和地区已配置过激活码，请查看！");
									return;
								}else
								{
									Ext.Ajax.request({
										url : "finance/chargeStandard/updateFinanceSpecial.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : values,
										async: false,
										success : function(response, options) {
											response = Ext.JSON.decode(response.responseText);
											if (response.result == true) {
												Ext.MessageBox.alert("提示信息", "新增成功，激活码为："+response.confirm_code);
												me.grid.getStore().load();
												me.up("window").close();
											} else {
												Ext.MessageBox.alert("错误信息", "保存失败<br>请联系管理员!");
											}
										},
										failure : function() {
											Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
										}

									});
								}
							}

						});
					}else
					{
						Ext.Ajax.request({
							url : "finance/chargeStandard/updateFinanceSpecial.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							async: false,
							success : function(response, options) {
								response = Ext.JSON.decode(response.responseText);
								if (response.result == true) {
									Ext.MessageBox.alert("提示信息", "新增成功，激活码为："+response.confirm_code);
									me.grid.getStore().load();
									me.up("window").close();
								} else {
									Ext.MessageBox.alert("错误信息", "保存失败<br>请联系管理员!");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}

						});
					}
					
				
					
				
				}
			});
			
			
		}

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}
});