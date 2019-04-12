var deptname = Ext.create('Ext.data.Store',{
    fields:['deptname'],
    autoLoad:true,
    proxy:{
    	type: 'jsonajax',
        actionMethods:{read:'POST'},
        params:{},
        url:'statistics/financeConfig/queryUserDept.do',
        reader:{
          type:'json',
        },
        writer: {
            type: 'json'
       }
      },
      remoteSort:true
});
Ext.define("Rds.statistic.form.FinanceAmoebaForm", {
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
			                xtype: 'combo',
			                autoSelect : true,
			                allowBlank:false,
			        		fieldLabel: '阿米巴项目',
			        		labelWidth : 70,
			        		emptyText : "请选择",
			        		triggerAction: 'all',
			     	        queryMode: 'local',
			     	        selectOnTab: true,
			     	        editable:false,  
			     	        forceSelection: true,
			     	        store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['其他费用（含折旧及摊销）', '其他费用（含折旧及摊销）'],['对外投资', '对外投资'],['材料成本', '材料成本']]
							}),
			                displayField: 'Name',
			                valueField: 'Code',
			                name:'amoeba_program'
			            },{
							layout : 'auto',
							xtype : 'panel',
							border : false,
							items : [	
									{
										layout : 'column',
										xtype : 'panel',
										border : false,
										style : 'margin-top:5px;margin-bottom:10px;',
										items : [Ext.create('Ext.form.DateField', {
									    			name : 'amoeba_month',
									    			width : '20%',
									    			labelWidth : 70,
													columnWidth : .70,
									    			fieldLabel : '统计月份',
									    			labelAlign : 'right',
									    			emptyText : '请选择日期',
									    			format: 'Y-m',
									    			value:Ext.util.Format.date(new Date(),"Y-m")
									    		}),{
													columnWidth : .30,
													xtype : "numberfield",
													fieldLabel : "排序<span style='color:red'>*</span>",
													mode : 'local',
													labelAlign : 'right',
													allowBlank : false,// 不允许为空
													maxLength : 50,
													labelWidth : 50,
													name : 'sort',
												}]
									} ]
			            },{
							xtype : "textarea",
							fieldLabel : "备注",
							mode : 'local',
							labelAlign : 'right',
							maxLength : 500,
							labelWidth : 70,
							name : 'remark'
						},{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [ {
								xtype : 'button',
								text : '添加部门款项',
								width : 100,
								x : 520,
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
														labelAlign : 'left'
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
																	style : 'margin-top:5px;',
																	items : [new Ext.form.ComboBox({
																				autoSelect : true,
																				editable:true,
																				labelWidth : 50,
																				columnWidth : .35,
																				fieldLabel : "事业部<span style='color:red'>*</span>",
																			    name:'amoeba_deptment',
																			    forceSelection:true,
																				allowBlank : false,// 不允许为空
																			    triggerAction: 'all',
																			    queryMode: 'local', 
																			    emptyText : "请选择",
																			    selectOnTab: true,
																				labelAlign : 'right',
																			    store: deptname,
																			    fieldStyle: me.fieldStyle,
																			    displayField:'deptname',
																			    valueField:'deptname',
																			    listClass: 'x-combo-list-small',
																			}),{
																				columnWidth : .25,
																				xtype : "textfield",
																				fieldLabel : "二级部门",
																				mode : 'local',
																				labelAlign : 'right',
																				allowBlank : false,// 不允许为空
																				maxLength : 50,
																				labelWidth : 60,
																				name : 'amoeba_deptment1',
																			},
																			{
																				columnWidth : .20,
																				xtype : "numberfield",
																				fieldLabel : "款项<span style='color:red'>*</span>",
																				mode : 'local',
																				labelAlign : 'right',
																				allowBlank : false,// 不允许为空
																				maxLength : 50,
																				labelWidth : 50,
																				name : 'amoeba_sum',
																			},
																			{
																				layout : 'absolute',// 从左往右的布局
																				xtype : 'panel',
																				border : false,
																				columnWidth : .20,
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
			var me = this.up("form");
			var form = me.getForm();
			var values = form.getValues();
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
					url : "statistics/financeConfig/insertAmoebaInfo.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : values,
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.success) {
							Ext.MessageBox.alert("提示信息", "操作成功！");
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
		}
});