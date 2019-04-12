var amboeaPerson="";
Ext.define("Rds.statistic.panel.FinanceAmoebaPersonGridPanel",{
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
						var wages_month_start = Ext.create('Ext.form.DateField', {
							fieldLabel: '人工成本从', 
	                        width: '20%', 
//							editable:true,
	                        labelWidth: 70, 
	                        name : 'wages_month_start',
							format : 'Y-m-d',
						});
						var wages_month_end = Ext.create('Ext.form.DateField', {
							fieldLabel: '到', 
	                        width: '20%', 
//							editable:true,
	                        labelWidth: 20, 
	                        name : 'wages_month_end',
							format : 'Y-m-d',
						});
						var confirm_date_start=Ext.create('Ext.form.DateField', {
							name : 'confirm_date_start',
							width : '20%',
							labelWidth : 70,
							fieldLabel : '确认时间 从',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")
						});
						var confirm_date_end=Ext.create('Ext.form.DateField', {
							name : 'confirm_date_end',
							width : '20%',
							labelWidth : 20,
							fieldLabel : '到',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							value:Ext.Date.add(
				                    new Date(),
				                    Ext.Date.DAY)
						});
						var sqrq_start=Ext.create('Ext.form.DateField', {
							name : 'sqrq_start',
							width : '20%',
							labelWidth : 70,
							fieldLabel : '申请时间 从',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")
						});
						var sqrq_end=Ext.create('Ext.form.DateField', {
							name : 'sqrq_end',
							width : '20%',
							labelWidth : 20,
							fieldLabel : '到',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							value:Ext.Date.add(
				                    new Date(),
				                    Ext.Date.DAY)
						});
						var operatedate_start=Ext.create('Ext.form.DateField', {
							name : 'operatedate_start',
							width : '20%',
							labelWidth : 70,
							fieldLabel : '操作时间 从',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")
						});
						var operatedate_end=Ext.create('Ext.form.DateField', {
							name : 'operatedate_end',
							width : '20%',
							labelWidth : 20,
							fieldLabel : '到',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							value:Ext.Date.add(
				                    new Date(),
				                    Ext.Date.DAY)
						});
						var user_dept_level1 = new Ext.form.ComboBox({
							autoSelect : true,
							editable:true,
							labelWidth : 70,
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
						var user_dept_level2 = Ext.create('Ext.form.field.Text', {
							name : 'user_dept_level2',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '二级部门'
						});
						var user_dept_level3 = Ext.create('Ext.form.field.Text', {
							name : 'user_dept_level3',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '三级部门'
						});
						var user_dept_level4 = Ext.create('Ext.form.field.Text', {
							name : 'user_dept_level4',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '四级部门'
						});
						var user_dept_level5 = Ext.create('Ext.form.field.Text', {
							name : 'user_dept_level5',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '五级部门'
						});
						var username = Ext.create('Ext.form.field.Text', {
							name : 'username',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '人员姓名'
						});
						var webchart = Ext.create('Ext.form.field.Text', {
							name : 'webchart',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '员工编号'
						});
						var usertype = Ext.create('Ext.form.field.Text', {
							name : 'usertype',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '职位类别'
						});
						var telphone = Ext.create('Ext.form.field.Text', {
							name : 'telphone',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '电话'
						});
						me.store = Ext.create('Ext.data.Store',{
											fields : ['username','usertype','webchart','telphone','return_sum','mx1bxje','wages','dept1','dept2','dept3','dept4','dept5'],
											start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'statistics/personAmboea/queryAllPage.do',
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
												amboeaPerson = "username="+username.getValue().trim()
													+"&wages_month_start="+Ext.util.Format.date(wages_month_start.getValue(),"Y-m")
													+"&wages_month_end="+Ext.util.Format.date(wages_month_end.getValue(),"Y-m")
													+"&confirm_date_start="+Ext.util.Format.date(confirm_date_start.getValue(),"Y-m-d")
													+"&confirm_date_end="+Ext.util.Format.date(confirm_date_end.getValue(),"Y-m-d")
													+"&sqrq_start="+Ext.util.Format.date(sqrq_start.getValue(),"Y-m-d")
													+"&sqrq_end="+Ext.util.Format.date(sqrq_end.getValue(),"Y-m-d")
													+"&operatedate_start="+Ext.util.Format.date(operatedate_start.getValue(),"Y-m-d")
													+"&operatedate_end="+Ext.util.Format.date(operatedate_end.getValue(),"Y-m-d")
													+"&user_dept_level1="+(user_dept_level1.getValue()==null?"":user_dept_level1.getValue())
													+"&user_dept_level2="+user_dept_level2.getValue().trim()
													+"&user_dept_level3="+user_dept_level3.getValue().trim()
													+"&user_dept_level4="+user_dept_level4.getValue().trim()
													+"&user_dept_level5="+user_dept_level5.getValue().trim()
													+"&webchart="+webchart.getValue().trim()
													+"&usertype="+usertype.getValue().trim()
													+"&telphone="+telphone.getValue().trim();
													Ext.apply(
															me.store.proxy.extraParams,{								
																wages_month_start:Ext.util.Format.date(wages_month_start.getValue(),"Y-m"),
																wages_month_end:Ext.util.Format.date(wages_month_end.getValue(),"Y-m"),
																user_dept_level1:(user_dept_level1.getValue()==null?"":user_dept_level1.getValue()),
																user_dept_level2:user_dept_level2.getValue().trim(),
																user_dept_level3:user_dept_level3.getValue().trim(),
																user_dept_level4:user_dept_level4.getValue().trim(),
																user_dept_level5:user_dept_level5.getValue().trim(),
																username:username.getValue().trim(),
																confirm_date_start:Ext.util.Format.date(confirm_date_start.getValue(),"Y-m-d"),
																confirm_date_end:Ext.util.Format.date(confirm_date_end.getValue(),"Y-m-d"),
																sqrq_start:Ext.util.Format.date(sqrq_start.getValue(),"Y-m-d"),
																sqrq_end:Ext.util.Format.date(sqrq_end.getValue(),"Y-m-d"),
																operatedate_start:Ext.util.Format.date(operatedate_start.getValue(),"Y-m-d"),
																operatedate_end:Ext.util.Format.date(operatedate_end.getValue(),"Y-m-d"),
																webchart:webchart.getValue().trim(),
																usertype:usertype.getValue().trim(),
																telphone:telphone.getValue().trim()
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
				       var pagingToolbar=Ext.getCmp('pagingbarAmoebaPerson');
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
							id:'pagingbarAmoebaPerson',
							store: me.store,
							pageSize:me.pageSize,
							displayInfo: true,
							displayMsg : "第 {0} - {1} 条  共 {2} 条",
					   	 	emptyMsg : "没有符合条件的记录",
					   	 	items: ['-', '每页显示',combo,'条']
						});
						me.columns = [
								{
									text : '人员姓名',
									dataIndex : 'username',
									menuDisabled : true,
									width : 120,
								},{
									text : '员工编号',
									dataIndex : 'webchart',
									menuDisabled : true,
									width : 120,
								},{
									text : '职位',
									dataIndex : 'usertype',
									menuDisabled : true,
									width : 120,
								},{
									text : '电话',
									dataIndex : 'telphone',
									menuDisabled : true,
									width : 120,
								},{
									text : '收入',
									dataIndex : 'return_sum',
									menuDisabled : true,
									width : 120,
									renderer : function(value, cellmeta,
												record, rowIndex, columnIndex,
												store) {
											  if(""==record.data["return_sum"] || null == record.data["return_sum"])
												  return "";
											  else
												  return "<a href='#'>"+ record.data["return_sum"]+"</a>";
										  },
									listeners:{
											'click':function(){
												var me = this.up("gridpanel");
												var selections =  me.getView().getSelectionModel().getSelection();
												if(""==selections[0].get("return_sum") || null == selections[0].get("return_sum"))
													return;
												var store=Ext.create(
														'Ext.data.Store',
														{
															fields : ['id','case_id','case_code','case_user', 'case_area','confirm_date','return_sum','user_dept_level1',
													                    'user_dept_level2','user_dept_level3','insideCost','insideCostUnit','manageCost','manageCostUnit',
													                    'externalCost','aptitudeCost','experimentCost','case_type','case_subtype','type','partner'],// 定义字段
															start:0,
															limit:15,
															pageSize:15,
															proxy : {
																type : 'jsonajax',
																actionMethods : {
																	read : 'POST'
																},
																url :'statistics/financeConfig/queryCaseDetailAllPage.do',
																params : {
																	start : 0,
																},
																reader : {
												                    type: 'json',
												                    root:'data',
												                    totalProperty:'total'
												                }
															},
															autoLoad : true,
															listeners : {
																'beforeload' : function(ds,
																		operation, opt) {
																	Ext.apply(store.proxy.extraParams,{
																		case_user:selections[0].get("username"),
											                    		confirm_date_start:dateformat(confirm_date_start.getValue()),
											                    		confirm_date_end:dateformat(confirm_date_end.getValue())
																	});
																	
																}
															}
														});
												//分页的combobox下拉选择显示条数
											    combo = Ext.create('Ext.form.ComboBox',{
											          name: 'pagesize',
											          hiddenName: 'pagesize',
											          store: new Ext.data.ArrayStore({
											              fields: ['text', 'value'],
											              data: [['15', 15], ['30', 30],['60', 60], ['100', 100], ['500', 500], ['1000', 1000]]
											          }),
											          valueField: 'value',
											          displayField: 'text',
											          emptyText:15,
											          width: 60
											     });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

										       //添加下拉显示条数菜单选中事件
										       combo.on("select", function (comboBox) {
										       var pagingToolbar=Ext.getCmp('pagingbarAmoebaDepartment');
										         	pagingToolbar.pageSize = parseInt(comboBox.getValue());
										         	itemsPerPage = parseInt(comboBox.getValue());//更改全局变量itemsPerPage
										         	store.pageSize = itemsPerPage;//设置store的pageSize，可以将工具栏与查询的数据同步。
										         	store.load({  
										         		params:{  
										                 start:0,  
										                 limit: itemsPerPage
										         		}  
										         	});//数据源重新加载
										         	store.loadPage(1);//显示第一页
										        });
										       onServiceExport = function(me) {
										           window.location.href = "statistics/financeConfig/exportCaseDetail.do?confirm_date_start="+dateformat(confirm_date_start.getValue())+
										           "&confirm_date_end="+dateformat(confirm_date_end.getValue())+"&case_user="+selections[0].get("username");
										       }
											   var win = Ext.create("Ext.window.Window", {
													title : "收入信息",
													width : 800,
													iconCls : 'Find',
													height : 600,
													layout : 'border',
													maximizable :true,
													maximized:true,
													modal:true,
													bodyStyle : "background-color:white;",
													items : [Ext.create('Ext.grid.Panel', {
																height:300,
																frame:false,
																loadMask:true,
																width:480,
																name:"grid",
																region : 'center',
																viewConfig : {
																	forceFit : true,
																	trackOver : false,
																	stripeRows : false
																},
																selModel:Ext.create('Ext.selection.CheckboxModel',{
																	mode: 'SINGLE'
																}),
																bbar:Ext.create('Ext.PagingToolbar', {
																	id:'pagingbarAmoebaDepartment',
																	store: store,
																	pageSize:15,
																	displayInfo: true,
																	displayMsg : "第 {0} - {1} 条  共 {2} 条",
															   	 	emptyMsg : "没有符合条件的记录",
															   	 	items: ['-', '每页显示',combo,'条']
																}),
																dockedItems : [{
																	xtype : 'toolbar',
																	dock : 'top',
																	items : [{
																				xtype : 'button',
																				text : '导出',
																				iconCls : 'Pageexcel',
																				handler : onServiceExport
																			}

																	]
																}],
																store : store,
																columns : [
																            {text: '案例编号',dataIndex: 'case_code',width : 100},
																            {text: '归属人',dataIndex: 'case_user',width : 100},
																            {text: '归属地',dataIndex: 'case_area',width : 200},
																            {text: '确认时间',dataIndex: 'confirm_date',width : 150, renderer: Ext.util.Format.dateRenderer('Y-m-d')},
																    		{text: '款项',dataIndex: 'return_sum',width : 150},
																            {text: '项目',dataIndex: 'case_type',width : 150},
																            {text: '子项目',dataIndex: 'case_subtype',width : 150},
																            {text: '类型',dataIndex: 'type',width : 150,
																            	 renderer : function(value) {
																						switch (value) {
																						case "1":
																							return "服务收入";
																							break;
																						case "2":
																							return "销售收入";
																							break;
																						case "3":
																							return "试剂盒";
																							break;
																						default:
																							return "";
																						}
																					}},
																            {text: '一级部门',dataIndex: 'user_dept_level1',width : 150},
																            {text: '二级部门',dataIndex: 'user_dept_level2',width : 150},
																            {text: '三级部门',dataIndex: 'user_dept_level3',width : 150},
																            {text: '内部结算部门',dataIndex: 'insideCostUnit',width : 150},
																            {text: '结算费用',dataIndex: 'insideCost',width : 150},
																            {text: '管理费部门',dataIndex: 'manageCostUnit',width : 150},
																            {text: '管理费',dataIndex: 'manageCost',width : 150},
																            {text: '委外成本',dataIndex: 'externalCost',width : 150},
																            {text: '资质费',dataIndex: 'aptitudeCost',width : 150},
																            {text: '实验费',dataIndex: 'experimentCost',width : 150},
																            {text: '合作方',dataIndex: 'partner',width : 150},
																        ]
													})
													]
												});
												win.show();
												}  
											}
								},{
									text : '差旅费',
									dataIndex : 'mx1bxje',
									menuDisabled : true,
									width : 150,
									 renderer : function(value, cellmeta,
												record, rowIndex, columnIndex,
												store) {
											  if(""==record.data["mx1bxje"] || null == record.data["mx1bxje"])
												  return "";
											  else
												  return "<a href='#'>"+ record.data["mx1bxje"]+"</a>";
										  },
									  listeners:{
											'click':function(){ 
												var me = this.up("gridpanel");
												var selections =  me.getView().getSelectionModel().getSelection();
												if(""==selections[0].get("mx1bxje") || null ==selections[0].get("mx1bxje"))
													return;
												var store=Ext.create(
														'Ext.data.Store',
														{
															fields : ['case_id','requestid','ztsybmc','operatedate','operatetime', 'bxsm','mx1bxje','sqrq','sqrxm',
													                    'tdrxm','kmmc','djbh'],// 定义字段
															start:0,
															limit:15,
															pageSize:15,
															proxy : {
																type : 'jsonajax',
																actionMethods : {
																	read : 'POST'
																},
																url :'statistics/financeOA/queryAllPage.do',
																params : {
																	start : 0,
																},
																reader : {
												                    type: 'json',
												                    root:'data',
												                    totalProperty:'total'
												                }
															},
															autoLoad : true,
															listeners : {
																'beforeload' : function(ds,
																		operation, opt) {
																	Ext.apply(store.proxy.extraParams,{
																		amoebakmmc1:'差旅费',
																		operatedate_start:dateformat(operatedate_start.getValue()),
																		operatedate_end:dateformat(operatedate_end.getValue()),
																		sqrq_start:dateformat(sqrq_start.getValue()),
																		sqrq_end:dateformat(sqrq_end.getValue()),
																		tdrxm:selections[0].get("username"),
																	});
																	
																}
															}
														});
												//分页的combobox下拉选择显示条数
											    combo = Ext.create('Ext.form.ComboBox',{
											          name: 'pagesize',
											          hiddenName: 'pagesize',
											          store: new Ext.data.ArrayStore({
											              fields: ['text', 'value'],
											              data: [['15', 15], ['30', 30],['60', 60], ['100', 100], ['500', 500], ['1000', 1000]]
											          }),
											          valueField: 'value',
											          displayField: 'text',
											          emptyText:15,
											          width: 60
											     });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

										       //添加下拉显示条数菜单选中事件
										       combo.on("select", function (comboBox) {
										       var pagingToolbar=Ext.getCmp('pagingbarAmoebaDepartment');
										         	pagingToolbar.pageSize = parseInt(comboBox.getValue());
										         	itemsPerPage = parseInt(comboBox.getValue());//更改全局变量itemsPerPage
										         	store.pageSize = itemsPerPage;//设置store的pageSize，可以将工具栏与查询的数据同步。
										         	store.load({  
										         		params:{  
										                 start:0,  
										                 limit: itemsPerPage
										         		}  
										         	});//数据源重新加载
										         	store.loadPage(1);//显示第一页
										        });
										       onPersonAmoebaExport = function(me) {
										    	   window.location.href = "statistics/financeOA/exportFinanceOA.do?amoebakmmc1="+"差旅费"+"&tdrxm="+selections[0].get("username")
										    	   +"&operatedate_start="+dateformat(operatedate_start.getValue())+"&operatedate_end="+dateformat(operatedate_end.getValue())
										    	   +"&sqrq_start="+dateformat(sqrq_start.getValue())+"&sqrq_end="+dateformat(sqrq_end.getValue())
								       }
											   var win = Ext.create("Ext.window.Window", {
													title : "销售管理费用",
													width : 800,
													iconCls : 'Find',
													height : 600,
													layout : 'border',
													maximizable :true,
													modal:true,
													maximized:true,
													bodyStyle : "background-color:white;",
													items : [Ext.create('Ext.grid.Panel', {
																height:300,
																frame:false,
																loadMask:true,
																width:480,
																name:"grid",
																region : 'center',
																viewConfig : {
																	forceFit : true,
																	trackOver : false,
																	stripeRows : false
																},
																selModel:Ext.create('Ext.selection.CheckboxModel',{
																	mode: 'SINGLE'
																}),
																bbar:Ext.create('Ext.PagingToolbar', {
																	id:'pagingbarAmoebaDepartment',
																	store: store,
																	pageSize:15,
																	displayInfo: true,
																	displayMsg : "第 {0} - {1} 条  共 {2} 条",
															   	 	emptyMsg : "没有符合条件的记录",
															   	 	items: ['-', '每页显示',combo,'条']
																}),
																dockedItems : [{
																	xtype : 'toolbar',
																	dock : 'top',
																	items : [
																	         {
																				xtype : 'button',
																				text : '导出',
																				iconCls : 'Pageexcel',
																				handler : onPersonAmoebaExport
																			}

																	]
																}],
																store : store,
																columns : [
																            {text: '请求流程编号',dataIndex: 'djbh',width : 150},
																            {text: '承担部门名称',dataIndex: 'ztsybmc',width : 200},
																    		{text: '费用类型',dataIndex: 'kmmc',width : 150},
																            {text: '申请人姓名',dataIndex: 'sqrxm',width : 150},
																            {text: '提单人姓名',dataIndex: 'tdrxm',width : 150},
																            {text: '金额',dataIndex: 'mx1bxje',width : 150},
																            {text: '操作日期',dataIndex: 'operatedate',width : 150, renderer: Ext.util.Format.dateRenderer('Y-m-d')},
																            {text: '申请日期',dataIndex: 'sqrq',width : 150, renderer: Ext.util.Format.dateRenderer('Y-m-d')},
																            {text: '说明',dataIndex: 'bxsm',width : 500,
																            	renderer : function(value, cellmeta, record,
																						rowIndex, columnIndex, store) {
																					var str = value;
																					if (value.length > 150) {
																						str = value.substring(0, 150) + "...";
																					}
																					return "<span title='" + value + "'>" + str
																							+ "</span>";
																				}
																            },
																        ]
													})
													]
												});
												win.show();
											}  
										}
								},{
									text : '人工成本',
									dataIndex : 'wages',
									menuDisabled : true,
									width : 150,
								},{
									text : '一级部门',
									dataIndex : 'dept1',
									menuDisabled : true,
									width : 150,
								},{
									text : '二级部门',
									dataIndex : 'dept2',
									menuDisabled : true,
									width : 150,
								},{
									text : '三级部门',
									dataIndex : 'dept3',
									menuDisabled : true,
									width : 150,
								},{
									text : '四级部门',
									dataIndex : 'dept4',
									menuDisabled : true,
									width : 150,
								},{
									text : '五级部门',
									dataIndex : 'dept5',
									menuDisabled : true,
									width : 150,
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [username,wages_month_start,wages_month_end,confirm_date_start,confirm_date_end,]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [user_dept_level1,user_dept_level2,user_dept_level3,user_dept_level4,user_dept_level5]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [webchart,operatedate_start,operatedate_end,sqrq_start,sqrq_end]
								},{

									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [usertype,telphone,{
										text : '查询',
										iconCls : 'Find',
										handler : me.onSearch
									}]
								
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
							 		xtype:'toolbar',
							 		dock:'top', 		
							 		items:[{
						                text : '导出',
						                iconCls : 'Pageexcel',
						                handler : me.onExport
						            }]
								}];
						me.callParent(arguments);
					},
				    onExport:function(){
				        window.location.href = "statistics/personAmboea/exportPersonAmboeaInfo.do?"+amboeaPerson
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