Ext.define("Rds.statistic.panel.FinanceAptitudeGridPanel",{
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
						var provice = Ext.create('Ext.form.field.Text', {
							name : 'provice',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '省区'
						});
						var partnername = Ext.create('Ext.form.field.Text', {
							name : 'partnername',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '合作单位'
						});
						var partner_flag=Ext.create('Ext.form.ComboBox', {
							fieldLabel : '是否合作',
							width : '20%',
							labelWidth : 60,
							editable : false,
							triggerAction : 'all',
							displayField : 'Name',
							valueField : 'Code',
							store : new Ext.data.ArrayStore({
										fields : ['Name','Code' ],
										data : [['全部','' ],
												['是','1' ],
												['否','2' ]]
									}),
							value : '1',
							mode : 'local',
							name : 'partner_flag',
						});
						me.store = Ext.create('Ext.data.Store',{
											fields : ['config_id','provice','partnername',"aptitude_sample",'aptitude_case',"experiment_sample","experiment_case","partner_start",'partner_end','partner_flag',
											          'month_num','month_num_reduce','sample_special1','sample_special2','remark'],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'statistics/financeConfig/queryPageAptitude.do',
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
																provice:provice.getValue().trim(),
																partnername:partnername.getValue().trim(),
																partner_flag:partner_flag.getValue()
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
						me.selModel = Ext.create('Ext.selection.CheckboxModel',{
							mode: 'SINGLE'
						});
						me.columns = [
								{
									text : '所属省区',
									dataIndex : 'provice',
									menuDisabled : true,
									width : 150,
								},{
									text : '合作单位',
									dataIndex : 'partnername',
									menuDisabled : true,
									width : 200
								},{
									text : '资质费(样本)',
									dataIndex : 'aptitude_sample',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '资质费(案例)',
									dataIndex : 'aptitude_case',
									menuDisabled : true,
									width : 150
								},
								{
									text : '样本费(实验)',
									dataIndex : 'experiment_sample',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '案例费(实验)',
									dataIndex : 'experiment_case',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '合作起时间',
									dataIndex : 'partner_start',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '合作截至时间',
									dataIndex : 'partner_end',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '是否合作',
									dataIndex : 'partner_flag',
									menuDisabled : true,
									width : 150,
									renderer : function(value,meta,record) {
										switch (value) {
										case '1':
											return "是";
											break;
										default:
											return "否";
										}
									}
								},
								{
									text : '月度案例数',
									dataIndex : 'month_num',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '月度案例差值',
									dataIndex : 'month_num_reduce',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '一类样本加价',
									dataIndex : 'sample_special1',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '二类样本加价',
									dataIndex : 'sample_special2',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '备注',
									dataIndex : 'remark',
									menuDisabled : true,
									width : 150
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [provice,partnername, partner_flag,{
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
									 		},
									 		{
									 			text:'修改',
									 			iconCls:'Pageedit',
									 			handler:me.onUpdate
									 		},
									 		{
									 			text:'终止',
									 			iconCls:'Delete',
									 			handler:me.onDelete
									 		}]
								}];
						me.callParent(arguments);
					},
					onInsert:function(){
						var me = this.up("gridpanel");
						var form = Ext.create("Rds.statistic.form.FinanceAptitudeForm",{
							region:"center",
							grid:me
						});
						var win = Ext.create("Ext.window.Window",{
							title:'合作方资质—新增',
							width:550,
							iconCls:'Add',
							height:400,
							modal:true,
							layout:'border',
							items:[form]
						});
						win.show();
					},
					onUpdate:function(){
						var me = this.up("gridpanel");
						var selections =  me.getView().getSelectionModel().getSelection();
						if(selections.length<1 || selections.length>1){
							Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
							return;
						};
						if(selections[0].get("partner_flag") != '1')
						{
							Ext.Msg.alert("提示", "该合作方已终止合作，不能修改!");
							return;
						}
						var form = Ext.create("Rds.statistic.form.FinanceAptitudeForm",{
							region:"center",
							grid:me
						});
						var win = Ext.create("Ext.window.Window",{
							title:'合作方资质—修改',
							width:550,
							iconCls:'Pageedit',
							height:400,
							modal:true,
							layout:'border',	
							items:[form]
						});
						win.show();
						form.loadRecord(selections[0]);
					},
					onDelete:function(){
						var me = this.up("gridpanel");
						var selections =  me.getView().getSelectionModel().getSelection();
						if(selections.length<1 ||selections.length>1){
							Ext.Msg.alert("提示", "请选择一条需要删除的记录!");
							return;
						}
						if(selections[0].get("partner_flag") != '1')
						{
							Ext.Msg.alert("提示", "该合作方已终止合作!");
							return;
						}
						var values = {
								config_id:selections[0].get("config_id"),
								partner_flag:2
						};
						Ext.MessageBox.confirm("提示", "确认终止选中合作方？", function (btn) {
					        if("yes"==btn)
					        {
					        	Ext.MessageBox.wait('正在操作','请稍后...');
					    		Ext.Ajax.request({  
					    			url:"statistics/financeConfig/deleteFinanceConfig.do", 
					    			method: "POST",
					    			headers: { 'Content-Type': 'application/json' },
					    			jsonData: values, 
					    			success: function (response, options) {  
					    				response = Ext.JSON.decode(response.responseText); 
					                     if (response.success == true) {  
					                     	Ext.MessageBox.alert("提示信息", response.msg);
					                     	me.getStore().load();
					                     }else { 
					                     	Ext.MessageBox.alert("错误信息", response.msg);
					                     } 
					    			},  
					    			failure: function () {
					    				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
					    			}
					        	       
					          	});
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