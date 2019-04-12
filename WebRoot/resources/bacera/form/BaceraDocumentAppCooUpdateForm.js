Ext.define('Rds.bacera.form.BaceraDocumentAppCooUpdateForm', {
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
				labelWidth : 70
			},
			border : false,
			autoHeight : true,
			items : [{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [{
										xtype : 'hiddenfield',
										name : 'id'
									},
									{
										// 该列在整行中所占的百分比
										columnWidth : .50,
										xtype : "textfield",
										labelWidth : 90,
										allowBlank:false, //不允许为空
										fieldLabel : "编号<span style='color:red'>*</span>",
										name : 'num',
									},new Ext.form.field.ComboBox({
											fieldLabel : "是否结案<span style='color:red'>*</span>",
							    			labelWidth:80,
							    			editable : false,
							    			triggerAction : 'all',
							    			displayField : 'Name',
							    			labelAlign : 'right',
											allowBlank:false, //不允许为空
							    			valueField : 'Code',
							    			store : new Ext.data.ArrayStore(
							    					{
							    						fields : ['Name','Code' ],
							    						data : [ ['是','是' ],['否','否' ] ]
							    					}),
							    			mode : 'local',
							    			name : 'case_close',
							    			columnWidth : .5,
							    		})
										]
				     },
				     {
							layout : "column",// 从左往右的布局
							xtype : 'fieldcontainer',
							border : false,
							items : [ {
								// 该列在整行中所占的百分比
								columnWidth : .50,
								xtype : "textfield",
								labelWidth : 90,
								allowBlank:false, //不允许为空
								fieldLabel : "委托人<span style='color:red'>*</span>",
								name : 'client',
							},{
								xtype : 'datefield',
								name : 'client_date',
								columnWidth : .50,
								labelWidth : 80,
								fieldLabel : "委托日期<span style='color:red'>*</span>",
				    			labelAlign : 'right',
								format : 'Y-m-d',
								allowBlank : false,// 不允许为空
								maxValue : new Date()
							}
							]
				     },
				     {
							layout : "column",// 从左往右的布局
							xtype : 'fieldcontainer',
							border : false,
							items : [ {
								// 该列在整行中所占的百分比
								columnWidth : .50,
								xtype : "textfield",
								labelWidth : 90,
								fieldLabel : '联系方式<span style="color:red">*</span>',
								allowBlank:false, //不允许为空
								maxLength : 50,
								name : 'phone',
							}, {
								xtype : 'datefield',
								name : 'accept_date',
								columnWidth : .50,
								labelWidth : 80,
				    			labelAlign : 'right',
								fieldLabel : "受理日期<span style='color:red'>*</span>",
								format : 'Y-m-d',
								allowBlank : false,// 不允许为空
								maxValue : new Date()
							} ]
				     },{
							// 该列在整行中所占的百分比
							xtype : "textfield",
							labelWidth : 90,
							fieldLabel : '鉴定项目<span style="color:red">*</span>',
							columnWidth : 1,
							maxLength : 50,
							name : 'appraisal_pro',
						},{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [ {
								xtype : 'textarea',
								fieldLabel : '基本案情',
								name : 'basic_case',
								width : 500,
								maxLength : 500,
								columnWidth : 1,
								labelWidth : 90,
							} ]
						},{
							// 该列在整行中所占的百分比
							xtype : "textfield",
							labelWidth : 90,
							fieldLabel : '鉴定书、材料归还方式',
							columnWidth : 1,
							maxLength : 50,
							name : 'return_type',
						},{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [
									{
										xtype : 'datefield',
										name : 'appraisal_end_date',
										columnWidth : .50,
										labelWidth : 90,
										fieldLabel : "鉴定完成日期",
										format : 'Y-m-d',
										allowBlank : false,// 不允许为空
										maxValue : new Date()
									},{

										xtype : 'combo',
										fieldLabel : '案例归属<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
										labelWidth : 80,
						    			labelAlign : 'right',
										name : 'ownperson',
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
										columnWidth : .50,
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
									
									}
								]
				     },{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [{
				    			xtype:"numberfield",
				    			fieldLabel: '应收款项<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
								labelWidth : 90,
				    			columnWidth : .50,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'receivables',
				    			id: 'receivables',
				    			maxLength: 20,
				    			allowBlank:false, //不允许为空
			        			blankText:"不能为空" //错误提示信息，默认为This field is required!
				    		},{
				    			xtype:"numberfield",
				    			fieldLabel: '开票费用',
				    			labelWidth:80,
				    			columnWidth : .50,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'invoice_exp',
				    			labelAlign : 'right',
				    			maxLength: 20
				    		}]
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
								labelWidth : 90
							} ]
				     }]
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
		var me = this;
		var myWindow = me.up('window');
		var form = me.up('form').getForm();
		var values = form.getValues();
		if (form.isValid()) {
			form.submit({
				url : 'bacera/documentAppCoo/updateCaseInfo.do',
				method : 'post',
				waitMsg : '正在保存...',
				params:{
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
					}
				},
				failure : function() {
					Ext.Msg.alert("提示", "保存失败，请联系管理员!");
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
	},
	listeners : {
		'afterrender' : function() {
			if(confirm_flagTemp == '2')
				Ext.getCmp("receivables").readOnly=true;
		}
	}
});
