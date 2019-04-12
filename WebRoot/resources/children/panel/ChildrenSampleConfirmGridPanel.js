Ext.define("Rds.children.panel.ChildrenSampleConfirmGridPanel",{
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
						var transfer_time_start=Ext.create('Ext.form.DateField', {
							name : 'transfer_time_start',
							width : '20%',
							labelWidth : 70,
							fieldLabel : '接收时间 从',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value : Ext.Date.add(
									new Date(),
									Ext.Date.DAY,-7)
						});
						var transfer_time_end=Ext.create('Ext.form.DateField', {
							name : 'transfer_time_end',
							width : '20%',
							labelWidth : 20,
							fieldLabel : '到',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							value : Ext.Date.add(new Date(), Ext.Date.DAY,1)
						});
						var transfer_num = Ext.create('Ext.form.field.Text', {
							name : 'transfer_num',
							labelWidth : 70,
							width : '20%',
							fieldLabel : '交接单号'
						});
						var receive_time_start=Ext.create('Ext.form.DateField', {
							name : 'receive_time_start',
							width : '20%',
							labelWidth : 70,
							fieldLabel : '确认时间 从',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date()
						});
						var receive_time_end=Ext.create('Ext.form.DateField', {
							name : 'receive_time_end',
							width : '20%',
							labelWidth : 20,
							fieldLabel : '到',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d'
						});
						var receive_per = Ext.create('Ext.form.ComboBox', {
							xtype : 'combo',
							fieldLabel : '确认人',
							labelWidth : 60,
							width : '20%',
							name : 'receive_per',
							emptyText:'(人员首字母)：如小明(xm)',
							store :Ext.create("Ext.data.Store",{
								   fields:[
										{name:'key',mapping:'key',type:'string'},
										{name:'value',mapping:'value',type:'string'}
						          ],
								pageSize : 20,
								autoLoad: false,
								proxy : {
									type : "ajax",
									url:'judicial/dicvalues/getUsersId.do',
									reader : {
										type : "json"
									}
								}
							}),
							displayField : 'value',
							valueField : 'key',
							typeAhead : false,
							hideTrigger : true,
							minChars : 2,
							matchFieldWidth : true,
							listConfig : {
								loadingText : '正在查找...',
								emptyText : '没有找到匹配的数据'
							}
						});
						var transfer_per = Ext.create('Ext.form.ComboBox', {
							xtype : 'combo',
							fieldLabel : '交接人',
							labelWidth : 60,
							width : '20%',
							name : 'transfer_per',
							emptyText:'(人员首字母)：如小明(xm)',
							store :Ext.create("Ext.data.Store",{
								   fields:[
										{name:'key',mapping:'key',type:'string'},
										{name:'value',mapping:'value',type:'string'}
						          ],
								pageSize : 20,
								autoLoad: false,
								proxy : {
									type : "ajax",
									url:'judicial/dicvalues/getUsersId.do',
									reader : {
										type : "json"
									}
								}
							}),
							displayField : 'value',
							valueField : 'key',
							typeAhead : false,
							hideTrigger : true,
							minChars : 2,
							matchFieldWidth : true,
							listConfig : {
								loadingText : '正在查找...',
								emptyText : '没有找到匹配的数据'
							}
						});
						var state=Ext.create('Ext.form.ComboBox',{
							fieldLabel : '确认状态',
							width : '20%',
							labelWidth : 60,
							editable : false,
							triggerAction : 'all',
							displayField : 'Name',
							labelAlign : 'right',
							valueField : 'Code',
							store : new Ext.data.ArrayStore({
								fields : ['Name','Code' ],
								data : [['全部','' ],['未确认',1 ],['已确认',2 ] ]
							}),
							value : '',
							mode : 'local',
							name : 'state',
						});
						me.store = Ext.create('Ext.data.Store',{
							fields : [ "receive_id","receive_pername",'transfer_num',"transfer_time","transfer_pername",
							           "receive_remark","receive_time","state",'remark'],
							start:0,
							limit:15,
							pageSize:15,
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'children/sampleRelay/getTransferInfo.do',
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
								'beforeload' : function(ds,operation, opt) {
									Ext.apply(me.store.proxy.extraParams,{								
											transfer_time_start : dateformat(transfer_time_start.getValue()),	
											transfer_time_end : dateformat(transfer_time_end.getValue()),
											transfer_num:trim(transfer_num.getValue()),	
											receive_time_start : dateformat(receive_time_start.getValue()),	
											receive_time_end : dateformat(receive_time_end.getValue()),
											receive_per:receive_per.getValue(),
											state:state.getValue(),
											transfer_per:transfer_per.getValue()
											});
								}
							}
						});
						me.selModel = Ext.create('Ext.selection.CheckboxModel',{
							mode: 'SINGLE'
						});
						me.bbar = Ext.create('Ext.PagingToolbar', {
							store : me.store,
							pageSize : me.pageSize,
							displayInfo : true,
							displayMsg : "第 {0} - {1} 条  共 {2} 条",
							emptyMsg : "没有符合条件的记录"
						});
						me.columns = [{
							text : '交接单号',
							dataIndex : 'transfer_num',
							menuDisabled : true,
							width : 150
						},{
							text : '交接状态',
							dataIndex : 'state',
							menuDisabled : true,
							width : 80,
							renderer : function(value) {
								switch (value) {
									case 1 :
										return "<span style='color:red'>未确认</span>";
										break;
									case 2 :
										return "已确认";
										break;
									default :
										return "";
								}
							}
						
						},{
							text : '交接单号',
							dataIndex : 'transfer_num',
							menuDisabled : true,
							width : 150
						},{
							text : '接收人',
							dataIndex : 'transfer_pername',
							menuDisabled : true,
							width : 120
						},
						{
							text : '接收时间',
							dataIndex : 'transfer_time',
							menuDisabled : true,
							width : 200
						},{
							text : '确认人',
							dataIndex : 'receive_pername',
							menuDisabled : true,
							width : 120
						},{
							text : '确认时间',
							dataIndex : 'receive_time',
							menuDisabled : true,
							width : 200
						},{
							text : '确认备注',
							dataIndex : 'receive_remark',
							menuDisabled : true,
							width:500,
						    renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								var str = value;
								if (value.length > 50) {
									str = value.substring(0, 50) + "...";
								}
								return "<span title='" + value + "'>" + str
										+ "</span>";
							}
						},{
							text : '接收备注',
							dataIndex : 'remark',
							menuDisabled : true,
							width:200,
						    renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								var str = value;
								if (value.length > 50) {
									str = value.substring(0, 50) + "...";
								}
								return "<span title='" + value + "'>" + str
										+ "</span>";
							}
						
						}];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [transfer_num,state,transfer_time_start,transfer_time_end,transfer_per]
								},{

									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [receive_time_start,receive_time_end,receive_per,
											 {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											}]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [{
										text : '查看样本',
										iconCls : 'Find',
										handler : me.onFind
									},{
										text : '确认单号',
										iconCls : 'Pageedit',
										handler : me.onConfirm
									}
									]
								} ];

						me.callParent(arguments);
					},
					onFind:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要查看的记录!");
							return;
						};
						var win = Ext.create("Ext.window.Window", {
							title : "样本信息",
							width : 600,
							iconCls : 'Find',
							modal:true,
							height : 400,
							layout : 'border',
							bodyStyle : "background-color:white;",
							items : [ Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 600,
								height : 400,
								frame : false,
								viewConfig : {
									forceFit : true,
									stripeRows : true// 在表格中显示斑马线
								},
								store : {// 配置数据源
									fields : [ 'sample_code','confirm_state'],// 定义字段
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'children/sampleRelay/getRelaySampleInfo.do',
										params : {
											'receive_id' : selections[0].get("receive_id")
										},
										reader : {
											type : 'json',
											reader:'array'
										}
									},
									autoLoad : true
								// 自动加载
								},
								columns : [// 配置表格列
								{
									header : "样本条形码",
									dataIndex : 'sample_code',
									flex : 1,
									menuDisabled : true
								},{
									header : "样本审核状态",
									dataIndex : 'confirm_state',
									flex : 1,
									menuDisabled : true,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										if (value == 1) {
											return "<div style=\"color: green;\">确认通过</div>";
										}else if (value == 2) {
											return "<div style=\"color: red;\">确认未通过</div>";
										} else {
											return "样本未确认";
										}
									}
								}]
							}) ]
						});
						win.show();
					},
					onConfirm : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要确认的记录!");
							return;
						};
						if (selections[0].get("state")==2) {
							Ext.Msg.alert("提示", "此次交接已确认!");
							return;
						};
						var form = Ext.create(
								"Rds.children.form.ChildrenSampleRelayConfirmForm", {
									region : "center",
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '样本交接确认',
							width : 600,
							iconCls : 'Pageedit',
							height : 600,
							modal:true,
							layout : 'border',
							items : [ form ]
						});
						form.loadRecord(selections[0]);
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
