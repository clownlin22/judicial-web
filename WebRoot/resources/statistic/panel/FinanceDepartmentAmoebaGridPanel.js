var exportInfo="";
Ext.define('Rds.statistic.panel.FinanceDepartmentAmoebaGridPanel', {
	extend : 'Ext.tree.Panel',
	collapsible: true,  
    useArrows: true,  
    rootVisible: false,  
    multiSelect: true,  
    columnLines:true,
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
		var deptname = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			labelWidth : 60,
			width : '20%',
			fieldLabel : '事业部',
		    name:'deptname',
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
		var confirm_date_start = new Ext.form.DateField({
			name : 'confirm_date_start',
			width:'20%',
			fieldLabel : '操作日期从',
			labelWidth : 70,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-31)
		});
		var confirm_date_end = new Ext.form.DateField({
			name : 'confirm_date_end',
			width:'20%',
			style:'margin-left:10px;',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		me.store = Ext.create('Ext.data.TreeStore',{
			fields:['deptid','deptname','parentid','parentname','deptcode','leaf','parentdeptcode','deptOutTaxSum','serviceSum','sellSum'
			        ,'insideSum','deptInTaxSum','taxSum','deptCostSum','deptWagesSum','deptMaterialCostSum','deptExternalCostSum','deptCostInsideSum',
			        'deptSaleManageSum','deptAptitudeSum','deptOtherSum','deptInvestmentSum','deptInstrumentSum','deptProfit','deptTax','deptNetProfit'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'statistics/financeConfig/queryDepartmentAll.do',
                autoLoad:true,
                params : {  
                	parentcode : "0",
                },
                reader: {
                    type: 'json'
                }
            },
            listeners:{
				"beforeload":function(ds, operation, opt){
					var parentcode = operation.node.get("deptid");
					if(parentcode==null||parentcode==""){
						parentcode = '0';
					}
					exportInfo="parentcode="+parentcode+"&deptname="+deptname.getValue()+"&confirm_date_start="
					+dateformat(confirm_date_start.getValue())+"&confirm_date_end="+dateformat(confirm_date_end.getValue());
					Ext.apply(me.store.proxy.params, {
						parentcode:parentcode,
						deptname:deptname.getValue(),
						confirm_date_start:dateformat(confirm_date_start.getValue()),
						confirm_date_end:dateformat(confirm_date_end.getValue()),
				  });
				}
            }, 
            folderSort: true  
		});
		
		me.columns = [
	              { xtype: 'treecolumn', text: '部门名称',	dataIndex: 'deptname',width:250, menuDisabled:true
	              },
	              {
	                  text: '收入',//这个就是合并的
	                  columns: [{
	                      text     : '收入合计（不含税）',
	                      width    : 200,
	                      sortable : true,
	                      dataIndex: 'deptOutTaxSum'
	                  }, {
	                      text     : '服务收入',
	                      width    : 150,
	                      sortable : true,
	                      dataIndex: 'serviceSum',
						  renderer : function(value, cellmeta,
								record, rowIndex, columnIndex,
								store) {
							  if("-"==record.data["serviceSum"])
								  return record.data["serviceSum"];
							  else
								  return "<a href='#'>"+ record.data["serviceSum"]+"</a>";
						  },
	                      hidden:true,
						  listeners:{
							'click':function(){
								var me = this.up("treepanel");
								var selections = me.getView().getSelectionModel().getSelection();
								if("-"==selections[0].get("serviceSum"))
									return;
								var user_dept_level1="";
								var user_dept_level2="";
								var user_dept_level3="";
								//合计项
								if(selections[0].get("deptname")=="合计"){
									user_dept_level1 = "";
								}
								//一级部门
								else if(selections[0].get("deptcode").length==4)
								{
									user_dept_level1 = selections[0].get("deptname")=="合计"?"":selections[0].get("deptname");
								}
								//二级部门
								else if(selections[0].get("deptcode").length==7){
									user_dept_level1 = selections[0].get("parentname");
									user_dept_level2 = selections[0].get("deptname");
								}
								//三级部门
								else{
									user_dept_level2 = selections[0].get("parentname");
									user_dept_level3 = selections[0].get("deptname");
								}
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
														type:"1",
														user_dept_level1:user_dept_level1,
														user_dept_level2:user_dept_level2,
														user_dept_level3:user_dept_level3,
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
						           window.location.href = "statistics/financeConfig/exportCaseDetail.do?type=1&user_dept_level1="+user_dept_level1+"&user_dept_level2="+user_dept_level2+"&user_dept_level3="+user_dept_level3
						        		   +"&confirm_date_start="+dateformat(confirm_date_start.getValue())+"&confirm_date_end="+dateformat(confirm_date_end.getValue());
						       }
							   var win = Ext.create("Ext.window.Window", {
									title : "服务收入信息",
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
												            {text: '类型',dataIndex: 'type',width : 150},
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
	                  }, {
	                      text     : '销售收入',
	                      width    : 100,
	                      sortable : true,
	                      dataIndex: 'sellSum',
						  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								  if("-"==record.data["sellSum"])
									  return record.data["sellSum"];
								  else
									  return "<a href='#'>"+ record.data["sellSum"]+"</a>";
							  },
	                      hidden:true,
						  listeners:{
								'click':function(){ 
									var me = this.up("treepanel");
									var selections = me.getView().getSelectionModel().getSelection();
									if("-"==selections[0].get("sellSum"))
										return;
									var user_dept_level1="";
									var user_dept_level2="";
									var user_dept_level3="";
									//合计项
									if(selections[0].get("deptname")=="合计"){
										user_dept_level1 = "";
									}
									//一级部门
									else if(selections[0].get("deptcode").length==4)
									{
										user_dept_level1 = selections[0].get("deptname")=="合计"?"":selections[0].get("deptname");
									}
									//二级部门
									else if(selections[0].get("deptcode").length==7){
										user_dept_level1 = selections[0].get("parentname");
										user_dept_level2 = selections[0].get("deptname");
									}
									//三级级部门
									else{
										user_dept_level2 = selections[0].get("parentname");
										user_dept_level3 = selections[0].get("deptname");
									}
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
															type:"一次性收入",
															user_dept_level1:user_dept_level1,
															user_dept_level2:user_dept_level2,
															user_dept_level3:user_dept_level3,
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
							       onSellExport = function(me) {
							           window.location.href = "statistics/financeConfig/exportCaseDetail.do?type=2&user_dept_level1="+(selections[0].get("deptname")=="合计"?"":selections[0].get("deptname"))
							        		   +"&confirm_date_start="+dateformat(confirm_date_start.getValue())+"&confirm_date_end="+dateformat(confirm_date_end.getValue());
							       }
								   var win = Ext.create("Ext.window.Window", {
										title : "销售收入信息",
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
														items : [{
																	xtype : 'button',
																	text : '导出',
																	iconCls : 'Pageexcel',
																	handler : onSellExport
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
													            {text: '类型',dataIndex: 'type',width : 150},
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
	                  }, {
	                      text     : '内部结算收入',
	                      width    : 100,
	                      sortable : true,
	                      dataIndex: 'insideSum',
	                      renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								  if("-"==record.data["insideSum"])
									  return record.data["insideSum"];
								  else
									  return "<a href='#'>"+ record.data["insideSum"]+"</a>";
							  },
	                      hidden:true,
						  listeners:{
								'click':function(){ 
									var me = this.up("treepanel");
									var selections = me.getView().getSelectionModel().getSelection();
									if("-"==selections[0].get("insideSum"))
										return;
									var user_dept_level1="";
									var user_dept_level2="";
									var user_dept_level3="";
									//合计项
									if(selections[0].get("deptname")=="合计"){
										user_dept_level1 = selections[0].get("parentname")
									}
									//一级部门
									else if(selections[0].get("deptcode").length==4)
									{
										user_dept_level1 = selections[0].get("deptname")=="合计"?"":selections[0].get("deptname");
									}
									//二级部门
									else if(selections[0].get("deptcode").length==7){
										user_dept_level1 = selections[0].get("parentname");
										user_dept_level2 = selections[0].get("deptname");
									}
									//三级级部门
									else{
										user_dept_level2 = selections[0].get("parentname");
										user_dept_level3 = selections[0].get("deptname");
									}
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
															insideCostUnit:user_dept_level1,
															manageCostUnit:user_dept_level1,
								                    		confirm_date_start:dateformat(confirm_date_start.getValue()),
								                    		confirm_date_end:dateformat(confirm_date_end.getValue()),
								                    		queryFlag:1
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
							       onSellExport = function(me) {
							           window.location.href = "statistics/financeConfig/exportCaseDetail.do?insideCostUnit="+user_dept_level1+"&manageCostUnit="+user_dept_level1
							        		   +"&confirm_date_start="+dateformat(confirm_date_start.getValue())+"&confirm_date_end="+dateformat(confirm_date_end.getValue())+"&queryFlag=1";
							       }
								   var win = Ext.create("Ext.window.Window", {
										title : "内部结算收入信息",
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
														items : [{
																	xtype : 'button',
																	text : '导出',
																	iconCls : 'Pageexcel',
																	handler : onSellExport
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
													            {text: '类型',dataIndex: 'type',width : 150},
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
	                  }, {
	                      text     : '收入小计(含税)',
	                      width    : 120,
	                      sortable : true,
	                      dataIndex: 'deptInTaxSum',
	                      hidden:true
	                  }, {
	                      text     : '税',
	                      width    : 100,
	                      sortable : true,
	                      dataIndex: 'taxSum',
	                      hidden:true
	                  }]
	              },{
	                  text: '成本',//这个就是合并的
	                  columns: [{
	                      text     : '成本合计',
	                      width    : 200,
	                      sortable : true,
	                      dataIndex: 'deptCostSum'
	                  }, {
	                      text     : '人工成本',
	                      width    : 150,
	                      sortable : true,
	                      dataIndex: 'deptWagesSum',
						  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								  if("-"==record.data["deptWagesSum"])
									  return record.data["deptWagesSum"];
								  else
									  return "<a href='#'>"+ record.data["deptWagesSum"]+"</a>";
							  },
	                      hidden:true,
						  listeners:{
								'click':function(){ 
									var me = this.up("treepanel");
									var selections = me.getView().getSelectionModel().getSelection();
									if("-"==selections[0].get("deptWagesSum"))
										return;
									var user_dept_level1="";
									var user_dept_level2="";
									var user_dept_level3="";
									//合计项
									if(selections[0].get("deptname")=="合计"){
										user_dept_level1 = "";
									}
									//一级部门
									else if(selections[0].get("deptcode").length==4)
									{
										user_dept_level1 = selections[0].get("deptname")=="合计"?"":selections[0].get("deptname");
									}
									//二级部门
									else if(selections[0].get("deptcode").length==7){
										user_dept_level1 = selections[0].get("parentname");
										user_dept_level2 = selections[0].get("deptname");
									}
									//三级级部门
									else{
										user_dept_level2 = selections[0].get("parentname");
										user_dept_level3 = selections[0].get("deptname");
									}
									var store=Ext.create(
											'Ext.data.Store',
											{
												fields : ['id','attachment_id','wages_name','wages','wagesTemp','wages_socialTemp','wages_accumulationTemp','wages_middleTemp','wages_endTemp','wages_otherTemp',
												          'user_dept_level1','user_dept_level2',"user_dept_level3",'wages_month','create_pername',"create_date",'remark'],// 定义字段
												start:0,
												limit:15,
												pageSize:15,
												proxy : {
													type : 'jsonajax',
													actionMethods : {
														read : 'POST'
													},
													url :'statistics/wages/queryAllPage.do',
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
															wages_month_start:Ext.util.Format.date(confirm_date_start.getValue(),"Y-m"),
															wages_month_end:Ext.util.Format.date(confirm_date_end.getValue(),"Y-m"),
															user_dept_level1:user_dept_level1,
															user_dept_level2:user_dept_level2,
															user_dept_level3:user_dept_level3,
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
							       onWagesExport = function(me) {
							           window.location.href = "statistics/wages/exportWagesInfo.do?user_dept_level1="+user_dept_level1+"&user_dept_level2="+user_dept_level2+"&user_dept_level3="+user_dept_level3
							        		   +"&wages_month_start="+Ext.util.Format.date(confirm_date_start.getValue(),"Y-m")+"&wages_month_end="+Ext.util.Format.date(confirm_date_end.getValue(),"Y-m");
							       }
								   var win = Ext.create("Ext.window.Window", {
										title : "人工成本",
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
																	handler : onWagesExport
																}

														]
													}],
													store : store,
													columns : [
																{
																	text : '人员姓名',
																	dataIndex : 'wages_name',
																	menuDisabled : true,
																	width : 120,
																},{
																	text : '月份',
																	dataIndex : 'wages_month',
																	menuDisabled : true,
																	width : 120,
																},{
																	text : '一级部门',
																	dataIndex : 'user_dept_level1',
																	menuDisabled : true,
																	width : 200
																},{
																	text : '二级部门',
																	dataIndex : 'user_dept_level2',
																	menuDisabled : true,
																	width : 150,
																},{
																	text : '三级部门',
																	dataIndex : 'user_dept_level3',
																	menuDisabled : true,
																	width : 150
																},{
																	text : '人员成本',
																	dataIndex : 'wagesTemp',
																	menuDisabled : true,
																	width : 150,
																},{
																	text : '公司社保',
																	dataIndex : 'wages_socialTemp',
																	menuDisabled : true,
																	width : 150,
																},{
																	text : '公积金',
																	dataIndex : 'wages_accumulationTemp',
																	menuDisabled : true,
																	width : 150,
																},{
																	text : '月中小计',
																	dataIndex : 'wages_middleTemp',
																	menuDisabled : true,
																	width : 150,
																},{
																	text : '月底小计',
																	dataIndex : 'wages_endTemp',
																	menuDisabled : true,
																	width : 150,
																},{
																	text : '其他',
																	dataIndex : 'wages_otherTemp',
																	menuDisabled : true,
																	width : 150,
																},{
																	text : '创建人',
																	dataIndex : 'create_pername',
																	menuDisabled : true,
																	width : 150,
																},{
																	text : '创建时间',
																	dataIndex : 'create_date',
																	menuDisabled : true,
																	width : 150,
																},{
																	text : '备注',
																	dataIndex : 'remark',
																	menuDisabled : true,
																	width : 150,
																}
															 ]
										})
										]
									});
									win.show();
									}  
								}
	                  }, {
	                      text     : '材料成本',
	                      width    : 100,
	                      sortable : true,
	                      dataIndex: 'deptMaterialCostSum',
						  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								  if("-"==record.data["deptMaterialCostSum"])
									  return record.data["deptMaterialCostSum"];
								  else
									  return "<a href='#'>"+ record.data["deptMaterialCostSum"]+"</a>";
							  },
	                      hidden:true,
						  listeners:{
								'click':function(){ 
									var me = this.up("treepanel");
									var selections = me.getView().getSelectionModel().getSelection();
									if("-"==selections[0].get("deptMaterialCostSum"))
										return;
									var user_dept_level1="";
									var user_dept_level2="";
									var user_dept_level3="";
									//合计项
									if(selections[0].get("deptname")=="合计"){
										user_dept_level1 = "";
									}
									//一级部门
									else if(selections[0].get("deptcode").length==4)
									{
										user_dept_level1 = selections[0].get("deptname")=="合计"?"":selections[0].get("deptname");
									}
									//二级部门
									else if(selections[0].get("deptcode").length==7){
										user_dept_level1 = selections[0].get("parentname");
										user_dept_level2 = selections[0].get("deptname");
									}
									//三级级部门
									else{
										user_dept_level2 = selections[0].get("parentname");
										user_dept_level3 = selections[0].get("deptname");
									}
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
															kmmc:"耗材",
															operatedate_start:dateformat(confirm_date_start.getValue()),
															operatedate_end:dateformat(confirm_date_end.getValue()),
															ztsybmc:selections[0].get("deptname")=="合计"?"":selections[0].get("deptname"),
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
							       onWagesExport = function(me) {
							    	   window.location.href = "statistics/financeOA/exportFinanceOA.do?kmmc="+"耗材"+"&ztsybmc="+(selections[0].get("deptname")=="合计"?"":selections[0].get("deptname"))+
							    			   "&operatedate_start="+dateformat(confirm_date_start.getValue())+"&operatedate_end="+dateformat(confirm_date_end.getValue())
							       }
								   var win = Ext.create("Ext.window.Window", {
										title : "材料成本",
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
																	handler : onWagesExport
																}

														]
													}],
													store : store,
													columns : [
													            {text: '请求流程id',dataIndex: 'djbh',width : 100},
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
	                  }, {
	                      text     : '委外检测成本',
	                      width    : 100,
	                      sortable : true,
	                      dataIndex: 'deptExternalCostSum',
						  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								  if("-"==record.data["deptExternalCostSum"])
									  return record.data["deptExternalCostSum"];
								  else
									  return "<a href='#'>"+ record.data["deptExternalCostSum"]+"</a>";
							  },
	                      hidden:true,
						  listeners:{
								'click':function(){ 
									var me = this.up("treepanel");
									var selections = me.getView().getSelectionModel().getSelection();
									var selections = me.getView().getSelectionModel().getSelection();
									if("-"==selections[0].get("deptExternalCostSum"))
										return;
									var user_dept_level1="";
									var user_dept_level2="";
									var user_dept_level3="";
									//合计项
									if(selections[0].get("deptname")=="合计"){
										user_dept_level1 = "";
									}
									//一级部门
									else if(selections[0].get("deptcode").length==4)
									{
										user_dept_level1 = selections[0].get("deptname")=="合计"?"":selections[0].get("deptname");
									}
									//二级部门
									else if(selections[0].get("deptcode").length==7){
										user_dept_level1 = selections[0].get("parentname");
										user_dept_level2 = selections[0].get("deptname");
									}
									//三级级部门
									else{
										user_dept_level2 = selections[0].get("parentname");
										user_dept_level3 = selections[0].get("deptname");
									}
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
															kmmc:"委外检测成本",
															operatedate_start:dateformat(confirm_date_start.getValue()),
															operatedate_end:dateformat(confirm_date_end.getValue()),
															ztsybmc:selections[0].get("deptname")=="合计"?"":selections[0].get("deptname"),
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
							       onWagesExport = function(me) {
							    	   window.location.href = "statistics/financeOA/exportFinanceOA.do?kmmc="+"委外检测成本"+"&ztsybmc="+(selections[0].get("deptname")=="合计"?"":selections[0].get("deptname"))+
							    			   "&operatedate_start="+dateformat(confirm_date_start.getValue())+"&operatedate_end="+dateformat(confirm_date_end.getValue())
							       }
								   var win = Ext.create("Ext.window.Window", {
										title : "委外检测成本",
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
																	handler : onWagesExport
																}

														]
													}],
													store : store,
													columns : [
													            {text: '请求流程id',dataIndex: 'djbh',width : 100},
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
	                  }, {
	                      text     : '内部结算成本',
	                      width    : 100,
	                      sortable : true,
	                      dataIndex: 'deptCostInsideSum',
	                      hidden:true
	                  }, {
	                      text     : '销售管理费用',
	                      width    : 100,
	                      sortable : true,
	                      dataIndex: 'deptSaleManageSum',
						  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								  if("-"==record.data["deptSaleManageSum"])
									  return record.data["deptSaleManageSum"];
								  else
									  return "<a href='#'>"+ record.data["deptSaleManageSum"]+"</a>";
							  },
	                      hidden:true,
						  listeners:{
								'click':function(){ 
									var me = this.up("treepanel");
									var selections = me.getView().getSelectionModel().getSelection();
									var selections = me.getView().getSelectionModel().getSelection();
									if("-"==selections[0].get("deptSaleManageSum"))
										return;
									var user_dept_level1="";
									var user_dept_level2="";
									var user_dept_level3="";
									//合计项
									if(selections[0].get("deptname")=="合计"){
										user_dept_level1 = "";
									}
									//一级部门
									else if(selections[0].get("deptcode").length==4)
									{
										user_dept_level1 = selections[0].get("deptname")=="合计"?"":selections[0].get("deptname");
									}
									//二级部门
									else if(selections[0].get("deptcode").length==7){
										user_dept_level1 = selections[0].get("parentname");
										user_dept_level2 = selections[0].get("deptname");
									}
									//三级级部门
									else{
										user_dept_level2 = selections[0].get("parentname");
										user_dept_level3 = selections[0].get("deptname");
									}
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
															amoebakmmc:'耗材,委外检测成本,仪器设备,采购仪器设备付款',
															operatedate_start:dateformat(confirm_date_start.getValue()),
															operatedate_end:dateformat(confirm_date_end.getValue()),
															ztsybmc:selections[0].get("deptname")=="合计"?"":selections[0].get("deptname"),
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
							       onWagesExport = function(me) {
							    	   window.location.href = "statistics/financeOA/exportFinanceOA.do?amoebakmmc="+"耗材,委外检测成本,仪器设备,采购仪器设备付款"+"&ztsybmc="+(selections[0].get("deptname")=="合计"?"":selections[0].get("deptname"))+
					    			   "&operatedate_start="+dateformat(confirm_date_start.getValue())+"&operatedate_end="+dateformat(confirm_date_end.getValue())
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
																	handler : onWagesExport
																}

														]
													}],
													store : store,
													columns : [
													            {text: '请求流程id',dataIndex: 'djbh',width : 100},
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
	                  }, {
	                      text     : '资质费用',
	                      width    : 100,
	                      sortable : true,
	                      dataIndex: 'deptAptitudeSum',
	                      renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								  if("-"==record.data["deptAptitudeSum"])
									  return record.data["deptAptitudeSum"];
								  else
									  return "<a href='#'>"+ record.data["deptAptitudeSum"]+"</a>";
							  },
	                      hidden:true,
						  listeners:{
								'click':function(){ 
									var me = this.up("treepanel");
									var selections = me.getView().getSelectionModel().getSelection();
									if("-"==selections[0].get("deptAptitudeSum"))
										return;
									var user_dept_level1="";
									var user_dept_level2="";
									var user_dept_level3="";
									//合计项
									if(selections[0].get("deptname")=="合计"){
										user_dept_level1 = "";
									}
									//一级部门
									else if(selections[0].get("deptcode").length==4)
									{
										user_dept_level1 = selections[0].get("deptname")=="合计"?"":selections[0].get("deptname");
									}
									//二级部门
									else if(selections[0].get("deptcode").length==7){
										user_dept_level1 = selections[0].get("parentname");
										user_dept_level2 = selections[0].get("deptname");
									}
									//三级级部门
									else{
										user_dept_level2 = selections[0].get("parentname");
										user_dept_level3 = selections[0].get("deptname");
									}
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
															//去资质费大于0的标志
															aptitude_flag:1,queryFlag:1,
															insideCostUnit:user_dept_level1,
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
							       onSellExport = function(me) {
							           window.location.href = "statistics/financeConfig/exportCaseDetail.do?aptitude_flag=1&insideCostUnit="+user_dept_level1+"&queryFlag=1"
					        		   +"&confirm_date_start="+dateformat(confirm_date_start.getValue())+"&confirm_date_end="+dateformat(confirm_date_end.getValue());
							       }
								   var win = Ext.create("Ext.window.Window", {
										title : "资质费用",
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
														items : [{
																	xtype : 'button',
																	text : '导出',
																	iconCls : 'Pageexcel',
																	handler : onSellExport
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
	                  }, {
	                      text     : '其他费用（含折旧及摊销）',
	                      width    : 180,
	                      sortable : true,
	                      dataIndex: 'deptOtherSum',
	                      hidden:true
	                  }, {
	                      text     : '对外投资',
	                      width    : 100,
	                      sortable : true,
	                      dataIndex: 'deptInvestmentSum',
	                      hidden:true
	                  }, {
	                      text     : '房屋、装修、仪器及设备采购成本',
	                      width    : 220,
	                      sortable : true,
	                      dataIndex: 'deptInstrumentSum',
						  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								  if("-"==record.data["deptInstrumentSum"])
									  return record.data["deptInstrumentSum"];
								  else
									  return "<a href='#'>"+ record.data["deptInstrumentSum"]+"</a>";
							  },
	                      hidden:true,
						  listeners:{
								'click':function(){ 
									var me = this.up("treepanel");
									var selections = me.getView().getSelectionModel().getSelection();
									var selections = me.getView().getSelectionModel().getSelection();
									if("-"==selections[0].get("deptInstrumentSum"))
										return;
									var user_dept_level1="";
									var user_dept_level2="";
									var user_dept_level3="";
									//合计项
									if(selections[0].get("deptname")=="合计"){
										user_dept_level1 = "";
									}
									//一级部门
									else if(selections[0].get("deptcode").length==4)
									{
										user_dept_level1 = selections[0].get("deptname")=="合计"?"":selections[0].get("deptname");
									}
									//二级部门
									else if(selections[0].get("deptcode").length==7){
										user_dept_level1 = selections[0].get("parentname");
										user_dept_level2 = selections[0].get("deptname");
									}
									//三级级部门
									else{
										user_dept_level2 = selections[0].get("parentname");
										user_dept_level3 = selections[0].get("deptname");
									}
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
															amoebakmmc1:'仪器设备,采购仪器设备付款',
															operatedate_start:dateformat(confirm_date_start.getValue()),
															operatedate_end:dateformat(confirm_date_end.getValue()),
															ztsybmc:selections[0].get("deptname")=="合计"?"":selections[0].get("deptname"),
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
							       onWagesExport = function(me) {
							    	   window.location.href = "statistics/financeOA/exportFinanceOA.do?amoebakmmc1="+"仪器设备,采购仪器设备付款"+"&ztsybmc="+(selections[0].get("deptname")=="合计"?"":selections[0].get("deptname"))+
					    			   "&operatedate_start="+dateformat(confirm_date_start.getValue())+"&operatedate_end="+dateformat(confirm_date_end.getValue())
							       }
								   var win = Ext.create("Ext.window.Window", {
										title : "房屋、装修、仪器及设备采购成本",
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
																	handler : onWagesExport
																}

														]
													}],
													store : store,
													columns : [
													            {text: '请求流程id',dataIndex: 'djbh',width : 100},
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
	                  }]
	              },
	              { text: '利润小计', dataIndex: 'deptProfit',width:120},
	              { text: '税', dataIndex: 'deptTax',width:120},
	              { text: '净利润', dataIndex: 'deptNetProfit',width:120}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[deptname,confirm_date_start,confirm_date_end,{
            	text:'查询',
            	iconCls:'Find',
            	handler:me.onSearch
            }]
	 	},,{
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
            },{
    			xtype : 'label',
    			forId : 'myFieldId',
    			margin:'10px',
    			style:"color:red",
    			text : '单击表头“收入”，“成本”展开隐藏详情'
    		}]
		}];
		
		me.callParent(arguments);
	},
	onExport:function(){
        window.location.href = "statistics/financeConfig/exportAmoebaTree.do?"+exportInfo
	},
	onSearch:function(){
		var me = this.up("treepanel");
		me.getStore().load({params:{start:0}});
	},
	listeners : {
		beforeload : function(){
			Ext.MessageBox.wait('正在加载','请稍后...');
		},
		load : function() {  
			Ext.MessageBox.close();
		}, 
		'afterrender' : function() {
//			alert(1);
		},
		headerclick:function(ct,column,e,t,eOpts){ 
			var me = ct.up("treepanel");
			if("收入"==column.text){
				if(me.down("gridcolumn[dataIndex=serviceSum]").isHidden()){
					me.down("gridcolumn[dataIndex=serviceSum]").show();
					me.down("gridcolumn[dataIndex=sellSum]").show();
					me.down("gridcolumn[dataIndex=insideSum]").show();
					me.down("gridcolumn[dataIndex=deptInTaxSum]").show();
					me.down("gridcolumn[dataIndex=taxSum]").show();
				}else
				{
					me.down("gridcolumn[dataIndex=serviceSum]").hide();
					me.down("gridcolumn[dataIndex=sellSum]").hide();
					me.down("gridcolumn[dataIndex=insideSum]").hide();
					me.down("gridcolumn[dataIndex=deptInTaxSum]").hide();
					me.down("gridcolumn[dataIndex=taxSum]").hide();
				}
			}
			if("成本"==column.text){
				if(me.down("gridcolumn[dataIndex=deptWagesSum]").isHidden()){
					me.down("gridcolumn[dataIndex=deptWagesSum]").show();
					me.down("gridcolumn[dataIndex=deptMaterialCostSum]").show();
					me.down("gridcolumn[dataIndex=deptExternalCostSum]").show();
					me.down("gridcolumn[dataIndex=deptCostInsideSum]").show();
					me.down("gridcolumn[dataIndex=deptSaleManageSum]").show();
					me.down("gridcolumn[dataIndex=deptAptitudeSum]").show();
					me.down("gridcolumn[dataIndex=deptOtherSum]").show();
					me.down("gridcolumn[dataIndex=deptInvestmentSum]").show();
					me.down("gridcolumn[dataIndex=deptInstrumentSum]").show();
				}else
				{
					me.down("gridcolumn[dataIndex=deptWagesSum]").hide();
					me.down("gridcolumn[dataIndex=deptMaterialCostSum]").hide();
					me.down("gridcolumn[dataIndex=deptExternalCostSum]").hide();
					me.down("gridcolumn[dataIndex=deptCostInsideSum]").hide();
					me.down("gridcolumn[dataIndex=deptSaleManageSum]").hide();
					me.down("gridcolumn[dataIndex=deptAptitudeSum]").hide();
					me.down("gridcolumn[dataIndex=deptOtherSum]").hide();
					me.down("gridcolumn[dataIndex=deptInvestmentSum]").hide();
					me.down("gridcolumn[dataIndex=deptInstrumentSum]").hide();
				}
			}
		} 
	}
});