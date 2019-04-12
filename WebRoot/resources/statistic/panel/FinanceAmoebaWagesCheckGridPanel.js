var wagesInfo=""
Ext.define("Rds.statistic.panel.FinanceAmoebaWagesCheckGridPanel",{
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
						var user_dept_level1 = new Ext.form.ComboBox({
							autoSelect : true,
							editable:true,
							labelWidth : 50,
							width : '20%',
							fieldLabel : '事业部',
						    name:'user_dept_level1',
						    triggerAction: 'all',
						    queryMode: 'local', 
						    emptyText : "请选择",
						    selectOnTab: true,
						    store: deptname,
						    fieldStyle: me.fieldStyle,
						    displayField:'deptname',
						    valueField:'deptname',
						    listClass: 'x-combo-list-small',
						})
						var wages_month_start = Ext.create('Ext.ux.form.MonthField', {
							fieldLabel: '月份起', 
	                        width: '20%', 
							editable:false,
	                        labelWidth: 50, 
	                        name : 'wages_month_start',
	                        format: 'Y-m'
						});
						var wages_month_end = Ext.create('Ext.ux.form.MonthField', {
							fieldLabel: '月份止', 
	                        width: '20%', 
							editable:false,
	                        labelWidth: 50, 
	                        name : 'wages_month_end',
	                        format: 'Y-m'
						});
//						var user_dept_level1 = Ext.create('Ext.form.field.Text', {
//							name : 'user_dept_level1',
//							labelWidth : 60,
//							width : '20%',
//							fieldLabel : '一级部门'
//						});
						var user_dept_level2 = Ext.create('Ext.form.field.Text', {
							name : 'user_dept_level2',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '二级部门'
						});
						var user_dept_level3 = Ext.create('Ext.form.field.Text', {
							name : 'user_dept_level3',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '三级部门'
						});
						var wages_name = Ext.create('Ext.form.field.Text', {
							name : 'wages_name',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '人员姓名'
						});
						me.store = Ext.create('Ext.data.Store',{
											fields : ['id','workcode','attachment_id','wages_name','wages','wagesTemp','wages_socialTemp','wages_accumulationTemp','wages_middleTemp','wages_endTemp','wages_otherTemp',
											          'user_dept_level1','user_dept_level2',"user_dept_level3",'user_dept_level4','user_dept_level5','wages_month','create_pername',"create_date",'remark'],
											start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'statistics/wages/queryAllPage.do',
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
													wagesInfo= "wages_month_start="+Ext.util.Format.date(wages_month_start.getValue(),"Y-m")+
																"&wages_month_end="+Ext.util.Format.date(wages_month_end.getValue(),"Y-m")+
																"&user_dept_level1="+(user_dept_level1.getValue()==null?"":user_dept_level1.getValue())+
																"&user_dept_level2="+user_dept_level2.getValue().trim()+
																"&user_dept_level3="+user_dept_level3.getValue().trim()+
																"&wages_name="+wages_name.getValue().trim()
													Ext.apply(
															me.store.proxy.extraParams,{								
																wages_month_start:Ext.util.Format.date(wages_month_start.getValue(),"Y-m"),
																wages_month_end:Ext.util.Format.date(wages_month_end.getValue(),"Y-m"),
																user_dept_level1:(user_dept_level1.getValue()==null?"":user_dept_level1.getValue()),
																user_dept_level2:user_dept_level2.getValue().trim(),
																user_dept_level3:user_dept_level3.getValue().trim(),
																wages_name:wages_name.getValue().trim()
															});
												}
											}
										});
						me.selModel = Ext.create('Ext.selection.CheckboxModel',{
							mode: 'SINGLE'
						});
						//分页的combobox下拉选择显示条数
					    combo = Ext.create('Ext.form.ComboBox',{
					          name: 'pagesize',
					          hiddenName: 'pagesize',
					          store: new Ext.data.ArrayStore({
					              fields: ['text', 'value'],
					              data: [['15', 15], ['30', 30],['60', 60], ['80', 80], ['100', 100]]
					          }),
					          valueField: 'value',
					          displayField: 'text',
					          emptyText:15,
					          width: 60
					     });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

				       //添加下拉显示条数菜单选中事件
				       combo.on("select", function (comboBox) {
				       var pagingToolbar=Ext.getCmp('pagingbarAmoebaWagesCheck');
				         	pagingToolbar.pageSize = parseInt(comboBox.getValue());
				         	itemsPerPage = parseInt(comboBox.getValue());//更改全局变量itemsPerPage
				         	me.store.pageSize = itemsPerPage;//设置store的pageSize，可以将工具栏与查询的数据同步。
				         	me.store.load({  
				         		params:{  
				                 start:0,  
				                 limit: itemsPerPage
				         		}  
				         	});//数据源重新加载
				         	me.store.loadPage(1);//显示第一页
				        });
				       
						me.bbar = Ext.create('Ext.PagingToolbar', {
							id:'pagingbarAmoebaWagesCheck',
							store: me.store,
							pageSize:me.pageSize,
							displayInfo: true,
							displayMsg : "第 {0} - {1} 条  共 {2} 条",
					   	 	emptyMsg : "没有符合条件的记录",
					   	 	items: ['-', '每页显示',combo,'条']
						});
						me.columns = [
						           {
			                           text: '人工成本归属划分核心部分',//这个就是合并的
			                           columns: [{text: '成本归属事业部',dataIndex: 'user_dept_level1',width : 150},
			                        			{text: '成本归属二级部门',dataIndex: 'user_dept_level2',width : 150},
			                     			    {text: '成本归属三级部门',dataIndex: 'user_dept_level3',width : 150},
			                     			    {text: '成本归属四级部门',dataIndex: 'user_dept_level4',width : 150},
			                     			    {text: '成本归属五级部门',dataIndex: 'user_dept_level5',width : 150},
			                     			    {text: '成本归属人',dataIndex: 'wages_name',width : 120},
			                     			    {text: '归属人工号',dataIndex: 'workcode',width : 120}
			                                 ]
			                      
			                        },
									{
										text : '发生月份',
										dataIndex : 'wages_month',
										menuDisabled : true,
										width : 120,
									},{
				                           text: '人工成本',//这个就是合并的
				                           columns: [{text: '合计',dataIndex: 'wagesTemp',width : 150},
				                        			{text: '社保',dataIndex: 'wages_socialTemp',width : 150},
				                     			    {text: '公积金',dataIndex: 'wages_accumulationTemp',width : 150},
				                     			    {text: '月中小计',dataIndex: 'wages_middleTemp',width : 150},
				                     			    {text: '月底小计',dataIndex: 'wages_endTemp',width : 150},
				                     			    {text: '其他',dataIndex: 'wages_otherTemp',width : 100}
				                                 ]
				                      
				                    },
									{
										text : '创建人',
										dataIndex : 'create_pername',
										menuDisabled : true,
										width : 150,
									},
									{
										text : '创建时间',
										dataIndex : 'create_date',
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
									items : [user_dept_level1,user_dept_level2,user_dept_level3]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [wages_month_start,wages_month_end,wages_name,{
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											}]
								
								},
								{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 		},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [{
						                text : '导出',
						                iconCls : 'Pageexcel',
						                handler : me.onExport
						            }]
								}];
						me.callParent(arguments);
					},
				    onExport:function(){
				        window.location.href = "statistics/wages/exportWagesInfo.do?"+wagesInfo
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