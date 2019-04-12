Ext.define("Rds.statistic.panel.FinanceProgramGridPanel",{
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
						var confirm_date=Ext.create('Ext.form.DateField', {
							name : 'confirm_date',
							id:'confirm_date',
							width : '20%',
							labelWidth : 70,
							fieldLabel : '日期',
							labelAlign : 'right',
							emptyText : '请选择日期',
                            format : 'Y-m-d'
						});
						me.store = Ext.create(
										'Ext.data.Store',
										{
											fields : ['case_area','case_user',"case_agentuser","user_dept_level1","case_type"],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'statistics/queryProgramProvice2.do',
												params : {
													start : 0,
													limit : 25
												},
												reader : {
													type : 'json',
													root : 'items',
													totalProperty : 'count'
												}
											},
											listeners : {
												'beforeload' : function(ds,
														operation, opt) {
													Ext.apply(
															me.store.proxy.extraParams,{								
																confirm_date:Ext.util.Format.date(confirm_date.getValue(),"Y-m-d"),
																});
												}
											}
										});

						me.bbar = {xtype: 'label',id:'totalBbarProgram', text: '',
								style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
						me.columns = [
								{
									text : '所属省区',
									dataIndex : 'case_area',
									menuDisabled : true,
									width : 150,
								},{
									text : '事业部',
									dataIndex : 'user_dept_level1',
									menuDisabled : true,
									width : 150
								},{
									text : '项目类型',
									dataIndex : 'case_type',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '所属业务员',
									dataIndex : 'case_user',
									menuDisabled : true,
									width : 150
								},
								{
									text : '代理',
									dataIndex : 'case_agentuser',
									menuDisabled : true,
									width : 150,
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [confirm_date, {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											}]
								},{
							 		xtype:'toolbar',
							 		dock:'top', 		
							 		items:[{
							 			text:'数据生成',
							 			iconCls:'Add',
							 			handler:me.onCreate
							 		}]
							 	
								}];

						me.callParent(arguments);
						me.store.on("load",function(){
							Ext.getCmp('totalBbarProgram').setText("共 "+me.store.getCount()+" 条");
						});
					},
					onCreate:function(){

						Ext.MessageBox.wait('正在操作','请稍后...');
						var confirm_date = Ext.util.Format.date(Ext.getCmp("confirm_date").getValue(),"Y-m-d");
						Ext.Ajax.request({
							url : "statistics/financeConfig/financeCreate.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : {confirm_date:confirm_date},
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								console.log(response.success);
								if (response.success) {
									Ext.MessageBox.alert("提示信息", "操作成功");
								} else {
									Ext.MessageBox.alert("错误信息", "操作失败");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
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