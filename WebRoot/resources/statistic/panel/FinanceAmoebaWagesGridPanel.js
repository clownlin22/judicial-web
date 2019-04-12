case_canel = function(me) {
	me.up("window").close();
}
onDownload = function(value) {
	window.location.href = "statistics/wages/downloadAttachment.do?attachment_id=" + value;
}
Ext.define("Rds.statistic.panel.FinanceAmoebaWagesGridPanel",{
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
//						var user_dept_level1 = Ext.create('Ext.form.field.Text', {
//							name : 'user_dept_level1',
//							labelWidth : 60,
//							width : '20%',
//							fieldLabel : '一级事业部'
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
											fields : ['id','attachment_id','wages_name','wages','wagesTemp','wages_socialTemp','wages_accumulationTemp','wages_middleTemp','wages_endTemp','wages_otherTemp',
											          'user_dept_level1','user_dept_level2',"user_dept_level3",'wages_month','create_pername',"create_date",'remark'],
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
													Ext.apply(
															me.store.proxy.extraParams,{								
																wages_month_start:Ext.util.Format.date(wages_month_start.getValue(),"Y-m"),
																wages_month_end:Ext.util.Format.date(wages_month_end.getValue(),"Y-m"),
																user_dept_level1:user_dept_level1.getValue(),
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
				       var pagingToolbar=Ext.getCmp('pagingbarAmoebaWages');
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
							id:'pagingbarAmoebaWages',
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
								
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
							 		xtype:'toolbar',
							 		dock:'top', 		
							 		items:[{
									 			text:'新增导入',
									 			iconCls:'Add',
									 			handler:me.onInsert
									 		},{
									 			text:'删除导入',
									 			iconCls:'Pageedit',
									 			handler:me.onUpdate
									 		}]
								}];
						me.callParent(arguments);
					},
					onUpdate:function(){
						var me = this.up("gridpanel");
//						var selections =  me.getView().getSelectionModel().getSelection();
//						if (selections.length < 1 || selections.length > 1) {
//							Ext.Msg.alert("提示", "请选择一条操作的记录!");
//							return;
//						}
						attachment_onDel = function(me) {
							var grid = me.up("gridpanel");
							var selections = grid.getView().getSelectionModel().getSelection();
							if (selections.length < 1 || selections.length > 1) {
								Ext.Msg.alert("提示", "请选择一条需要删除的记录!");
								return;
							}
							Ext.Msg.show({
										title : '提示',
										msg : '确定删除该记录?',
										width : 300,
										buttons : Ext.Msg.OKCANCEL,
										fn : function(buttonId, text, opt) {
											if (buttonId == 'ok') {
												var values = {
													attachment_id : selections[0].get("attachment_id"),
													attachment_path:selections[0].get("attachment_path")
												}
												Ext.MessageBox.wait('正在操作','请稍后...');
												Ext.Ajax.request({
															url : "statistics/wages/deleteWages.do",
															method : "POST",
															headers : {
																'Content-Type' : 'application/json'
															},
															jsonData : values,
															success : function(response, options) {
																response = Ext.JSON
																		.decode(response.responseText);
																if (response.result == true) {
																	Ext.MessageBox.alert("提示信息",
																			response.msg);
																	var grid = me.up("gridpanel");
																	grid.getStore().load();
																} else {
																	Ext.MessageBox.alert("错误信息",
																			response.msg);
																}
															},
															failure : function() {
																Ext.Msg.alert("提示",
																		"保存失败<br>请联系管理员!");
															}

														});
											} else {
												return;
											}

										},
										animateTarget : 'addAddressBtn',
										icon : Ext.window.MessageBox.INFO
									})
						}
						var win = Ext.create("Ext.window.Window", {
							title : "附件管理",
							width : 700,
							iconCls : 'Find',
							height : 440,
							modal:true,
							resizable:true,
							layout : 'border',
							bodyStyle : "background-color:white;",
							items : [ Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 690,
								height : 400,
								frame : false,
								autoScroll : true,
								viewConfig : {
									forceFit : true,
									stripeRows : true// 在表格中显示斑马线
								},
								dockedItems : [{
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
												xtype : 'button',
												text : '删除',
												iconCls : 'Delete',
												handler : attachment_onDel
											}

									]
								}],
								store : {// 配置数据源
									fields : [ 'attachment_id', 'attachment_path','attachment_date', 'wages_month','create_pername' ],// 定义字段
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'statistics/wages/queryWagesAttachment.do',
										params : {
											//'attachment_id' : selections[0].get("attachment_id")
										},
										reader : {}
									},
									autoLoad : true
								},
								columns : [// 配置表格列
								           {
											text : '月份',
											dataIndex : 'wages_month',
											width : '40%',
											menuDisabled : true,
											flex : 1
										}, {
											text : '附件',
											dataIndex : 'attachment_path',
											width : '40%',
											menuDisabled : true,
											flex : 3
										}, {
											text : '最后上传日期',
											dataIndex : 'attachment_date',
											width : '10%',
											menuDisabled : true,
											flex : 1
										},
										{
											text : '上传人员',
											dataIndex : 'create_pername',
											width : '10%',
											menuDisabled : true,
											flex : 1
										}, {
											header : "操作",
											dataIndex : '',
											flex : 0.5,
											menuDisabled : true,
											renderer : function(value, cellmeta,
													record, rowIndex, columnIndex,
													store) {
												var attachment_id = record.data["attachment_id"];
												return "<a href='#' onclick=\"onDownload('" + attachment_id
												+ "')\"  >下载";
											}
										} ]
							}) ]
						});
						win.show();
					
					},
					onInsert:function(){
						var me = this.up("gridpanel");
						attachment_save=function(mei)
						{
							var form = mei.up("form").getForm();
							if (!form.isValid()) {
								Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
								return;
							}
							var values = {
									wages_month:Ext.util.Format.date(Ext.getCmp("wages_month").getValue(),"Y-m")
							};
							Ext.Ajax.request({  
				    			url:"statistics/wages/queryWagesAttachment.do", 
				    			method: "POST",
				    			headers: { 'Content-Type': 'application/json' },
				    			jsonData: values, 
				    			success: function (response, options) {  
				    				response = Ext.JSON.decode(response.responseText); 
				                    if(response.length>0){
				                    	Ext.MessageBox.confirm("提示", "改月已导入过，确定继续导入？", function (btn) {
									        if("yes"==btn)
									        {
												form.submit({
													url : 'statistics/wages/uploadWages.do',
													method : 'post',
													waitMsg : '正在上传您的文件...',
													success : function(form, action) {
														response = Ext.JSON
																.decode(action.response.responseText);
														if (response.result) {
															Ext.MessageBox.alert("提示信息", response.msg);
															me.getStore().load();
															mei.up("window").close();
														} else {
															Ext.MessageBox.alert("提示信息", response.msg);
														}
													},
													failure : function() {
														Ext.Msg.alert("提示", "上传失败，请联系管理员!");
													}
												});
											}
									    });
				                    }else{
										form.submit({
													url : 'statistics/wages/uploadWages.do',
													method : 'post',
													waitMsg : '正在上传您的文件...',
													success : function(form, action) {
														response = Ext.JSON
																.decode(action.response.responseText);
														if (response.result) {
															Ext.MessageBox.alert("提示信息", response.msg);
															me.getStore().load();
															mei.up("window").close();
														} else {
															Ext.MessageBox.alert("提示信息", response.msg);
														}
													},
													failure : function() {
														Ext.Msg.alert("提示", "上传失败，请联系管理员!");
													}
												});
				                    }
				    			},  
				    			failure: function () {
				    				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
				    			}
				        	       
				          	});
						}
						
						var attachment_insert = Ext.create("Ext.window.Window", {
							title : '附件上传',
							width : 550,
							height : 300,
							modal:true,
							layout : 'border',
							items : [{
								xtype : 'form',
								region : 'center',
								style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
								labelAlign : "right",
								autoScroll : true,
								bodyPadding : 10,
								defaults : {
									anchor : '100%',
									labelWidth : 80,
									labelSeparator : "：",
									labelAlign : 'right'
								},
								border : false,
								autoHeight : true,
								buttons : [{
											text : '保存',
											iconCls : 'Disk',
											handler : attachment_save
										}, {
											text : '取消',
											iconCls : 'Cancel',
											handler : case_canel
										}],
								items : [{
									layout : "column",// 从左往右的布局
									xtype : 'fieldcontainer',
									border : false,
									items : [ 	 {
										xtype : 'filefield',
										name : 'files',
										fieldLabel : '文件<span style="color:red">*</span>',
										msgTarget : 'side',
										allowBlank : false,
										labelWidth : 40,
										anchor : '100%',
										buttonText : '选择文件',
										columnWidth : .60,
				                        validator:function(value){
			                            if(value.endWith('xls')){
			                                return true;
			                            }else{
			                                return '请选择正确的excel文件';
			                            }
			                        }
									},Ext.create('Ext.form.DateField', {
						    			name : 'wages_month',
						    			labelWidth : 70,
										columnWidth : .40,
						    			fieldLabel : '月份',
						    			id:'wages_month',
						    			labelAlign : 'right',
						    			emptyText : '请选择日期',
						    			format: 'Y-m',
						    			value:Ext.util.Format.date(new Date(),"Y-m")
						    		})
									]
								},{
									xtype:"textarea",
					    			fieldLabel: '备注',
					    			labelWidth:40,
					    			fieldStyle:me.fieldStyle, 
					    			name: 'remark',
					    			height:80,
					    			maxLength: 200
								}]
							}]
						})
						attachment_insert.show();
					
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