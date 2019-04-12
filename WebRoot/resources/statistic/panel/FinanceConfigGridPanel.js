Ext.define("Rds.statistic.panel.FinanceConfigGridPanel",{
					extend : "Ext.grid.Panel",
					loadMask : true,
					viewConfig : {
						trackOver : false,
						stripeRows : false
					},
					region:'center',
					pageSize : 25,
					initComponent : function() {
						var me = this;
						var program = Ext.create('Ext.form.field.Text', {
							name : 'program',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '总类型'
						});
						var finance_program = Ext.create('Ext.form.field.Text', {
							name : 'finance_program',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '一级项目'
						});
						var finance_program_type = Ext.create('Ext.form.field.Text', {
							name : 'finance_program_type',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '二级项目'
						});
						var finance_manage_dept = Ext.create('Ext.form.field.Text', {
							name : 'finance_manage_dept',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '管理费部门'
						});
						var back_settlement_dept = Ext.create('Ext.form.field.Text', {
							name : 'back_settlement_dept',
							labelWidth : 80,
							width : '20%',
							fieldLabel : '后端结算部门'
						});
						me.store = Ext.create('Ext.data.Store',{
											fields : ['config_id','program','finance_program','finance_program_type',"front_settlement",'finance_manage','price','back_remark',
											          "back_settlement","create_time","finance_manage_dept",'back_settlement_dept','remark','experiment_price',
											          'agency_price','business_support'],
								            start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'statistics/financeConfig/queryPage.do',
												params : {
													start : 0,
													limit : 25
												},
												reader : {
													type: 'json',
								                    root:'data',
								                    totalProperty:'total'
												}
											},
											listeners : {
												'beforeload' : function(ds,
														operation, opt) {
													Ext.apply(
															me.store.proxy.extraParams,{								
																program:program.getValue().trim(),	
																finance_program:finance_program.getValue().trim(),
																finance_program_type:finance_program_type.getValue().trim(),
																finance_manage_dept:finance_manage_dept.getValue().trim(),
																back_settlement_dept:back_settlement_dept.getValue().trim()
															});
												}
											}
										});
						me.bbar = Ext.create('Ext.PagingToolbar', {
							store : me.store,
							pageSize : me.pageSize,
							displayInfo : true,
							displayMsg : "第 {0} - {1} 条  共 {2} 条",
							emptyMsg : "没有符合条件的记录"
						});
//						me.bbar = {xtype: 'label',id:'totalBbarProgram', text: '',
//								style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
						me.selModel = Ext.create('Ext.selection.CheckboxModel',{
							mode: 'SINGLE'
						});
						me.columns = [
								{
									text : '总类型',
									dataIndex : 'program',
									menuDisabled : true,
									width : 150,
								},{
									text : '一级项目',
									dataIndex : 'finance_program',
									menuDisabled : true,
									width : 150,
								},{
									text : '二级项目',
									dataIndex : 'finance_program_type',
									menuDisabled : true,
									width : 150
								},{
									text : '前端结算',
									dataIndex : 'front_settlement',
									menuDisabled : true,
									width : 250,
								},
								{
									text : '管理费',
									dataIndex : 'finance_manage',
									menuDisabled : true,
									width : 250
								},
								{
									text : '后端结算',
									dataIndex : 'back_settlement',
									menuDisabled : true,
									width : 250,
								},
								{
									text : '后端参与结算的部门分配',
									dataIndex : 'back_remark',
									menuDisabled : true,
									width : 200,
								},{
									text : '业务支撑中心',
									dataIndex : 'business_support',
									menuDisabled : true,
									width : 150,
								},{
									text : '代理价',
									dataIndex : 'agency_price',
									menuDisabled : true,
									width : 150,
								},{
									text : '委托实验成本价',
									dataIndex : 'experiment_price',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '定价',
									dataIndex : 'price',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '备注',
									dataIndex : 'remark',
									menuDisabled : true,
									width : 150,
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [program,finance_program,finance_program_type,,]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [finance_manage_dept,back_settlement_dept, {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											}]
								},{
							 		xtype:'toolbar',
							 		dock:'top', 		
							 		items:[{
									 			text:'新增',
									 			iconCls:'Add',
									 			handler:me.onInsert
									 		},
									 		{
									 			text:'修改',
									 			iconCls:'Pageedit',
									 			handler:me.onUpdate
									 		}]
								}];
						me.callParent(arguments);
					},
					onInsert:function(){
						var me = this.up("gridpanel");
						var form = Ext.create("Rds.statistic.form.FinanceConfigForm",{
							region:"center",
							grid:me
						})
						var win = Ext.create("Ext.window.Window",{
							title:'结算规则—新增',
							width:650,
							iconCls:'Add',
							height:600,
							modal:true,
							layout:'border',
							items:[form]
						})
						win.show();
					},
					onUpdate:function(){
						var me = this.up("gridpanel");
						var selections =  me.getView().getSelectionModel().getSelection();
						if(selections.length<1 || selections.length>1){
							Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
							return;
						}
						var form = Ext.create("Rds.statistic.form.FinanceConfigForm",{
							region:"center",
							grid:me
						})
						var win = Ext.create("Ext.window.Window",{
							title:'结算规则—修改',
							width:650,
							iconCls:'Pageedit',
							height:600,
							modal:true,
							layout:'border',	
							items:[form]
						})
						win.show();
						form.loadRecord(selections[0]);
					},
					onSearch : function() {
						var me = this.up("gridpanel");
						me.getStore().currentPage=1;
						me.getStore().load();
					},
					listeners : {
						'afterrender' : function() {
							this.store.load();
						}
					}
				});