Ext.define("Rds.statistic.panel.FinanceAmoebaGridPanel",{
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
						var amoeba_program = Ext.create('Ext.form.field.Text', {
							name : 'amoeba_program',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '项目'
						});
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
						var amoeba_deptment = new Ext.form.ComboBox({
							autoSelect : true,
							editable:true,
							labelWidth : 50,
							width : '20%',
							fieldLabel : '事业部',
						    name:'amoeba_deptment',
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
						
//						var amoeba_deptment = Ext.create('Ext.form.field.Text', {
//							name : 'amoeba_deptment',
//							labelWidth : 60,
//							width : '20%',
//							fieldLabel : '部门'
//						});
						me.store = Ext.create('Ext.data.Store',{
											fields : ['amoeba_id','amoeba_program','amoeba_deptment','amoeba_deptment1','amoeba_sum',"amoeba_month",'create_pername',"create_date",'remark'],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'statistics/financeConfig/queryPageAmoeba.do',
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
																amoeba_program:amoeba_program.getValue().trim(),
																amoeba_deptment:amoeba_deptment.getValue()
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
				       var pagingToolbar=Ext.getCmp('pagingbarAmoeba');
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
							id:'pagingbarAmoeba',
							store: me.store,
							pageSize:me.pageSize,
							displayInfo: true,
							displayMsg : "第 {0} - {1} 条  共 {2} 条",
					   	 	emptyMsg : "没有符合条件的记录",
					   	 	items: ['-', '每页显示',combo,'条']
						});
						me.columns = [
								{
									text : '阿米巴项目',
									dataIndex : 'amoeba_program',
									menuDisabled : true,
									width : 200,
								},{
									text : '一级部门',
									dataIndex : 'amoeba_deptment',
									menuDisabled : true,
									width : 200
								},{
									text : '二级部门',
									dataIndex : 'amoeba_deptment1',
									menuDisabled : true,
									width : 200
								},{
									text : '月份',
									dataIndex : 'amoeba_month',
									menuDisabled : true,
									width : 150,
								},{
									text : '金额',
									dataIndex : 'amoeba_sum',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '创建人员',
									dataIndex : 'create_pername',
									menuDisabled : true,
									width : 150
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
									items : [amoeba_program,amoeba_deptment,{
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
									 			text:'新增',
									 			iconCls:'Add',
									 			handler:me.onInsert
									 		},{
									 			text:'删除',
									 			iconCls:'Delete',
									 			handler:me.onDelete
									 		}]
								}];
						me.callParent(arguments);
					},
					onDelete:function(){
						Ext.Msg.alert("提示", "暂未开通!");
					},
					onInsert:function(){
						var me = this.up("gridpanel");
						var form = Ext.create("Rds.statistic.form.FinanceAmoebaForm",{
							region:"center",
							autoScroll : true,
							grid:me
						});
						var win = Ext.create("Ext.window.Window",{
							title:'阿米巴项目—新增',
							width:750,
							iconCls:'Add',
							height:600,
							modal:true,
							layout:'border',
							items:[form]
						});
						win.show();
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