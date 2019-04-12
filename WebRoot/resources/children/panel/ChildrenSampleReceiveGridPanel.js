Ext.define("Rds.children.panel.ChildrenSampleReceiveGridPanel",{
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
						var sample_code = Ext.create('Ext.form.field.Text', {
							name : 'sample_code',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '样本编号'
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
						var confirm_state=Ext.create('Ext.form.ComboBox',{
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
								data : [['全部','' ],
										['未确认',0 ],
										['确认通过',1 ],
										['确认不通过',2 ]]
							}),
							value : '',
							mode : 'local',
							name : 'confirm_state',
						});
						me.store = Ext.create('Ext.data.Store',{
								fields : [ "receive_id","receive_pername",'transfer_num',"transfer_time","transfer_pername",
								           "sample_code","receive_remark","receive_time","confirm_state",'remark'],
								start:0,
								limit:15,
								pageSize:15,
								proxy : {
									type : 'jsonajax',
									actionMethods : {
										read : 'POST'
									},
									url : 'children/sampleRelay/getSampleReceiveInfo.do',
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
										Ext.apply(
												me.store.proxy.extraParams,{								
												transfer_time_start : dateformat(transfer_time_start.getValue()),	
												transfer_time_end : dateformat(transfer_time_end.getValue()),
												transfer_num:trim(transfer_num.getValue()),	
												receive_time_start : dateformat(receive_time_start.getValue()),	
												receive_time_end : dateformat(receive_time_end.getValue()),
												receive_per:receive_per.getValue(),
												sample_code:trim(sample_code.getValue()),
												transfer_per:transfer_per.getValue(),
												confirm_state:confirm_state.getValue()
												});
									}
								}
							});
						me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//							mode: 'SINGLE'
						});
						me.bbar = Ext.create('Ext.PagingToolbar', {
							store : me.store,
							pageSize : me.pageSize,
							displayInfo : true,
							displayMsg : "第 {0} - {1} 条  共 {2} 条",
							emptyMsg : "没有符合条件的记录"
						});
						me.columns = [{
									text : '样本编号',
									dataIndex : 'sample_code',
									menuDisabled : true,
									width : 120
								},{
									text : '交接状态',
									dataIndex : 'confirm_state',
									menuDisabled : true,
									width : 80,
									renderer : function(value) {
										switch (value) {
											case 0 :
												return "未确认";
												break;
											case 1 :
												return "确认通过";
												break;
											case 2 :
												return "<span style='color:red'>确认不通过</span>";
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
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [transfer_num,sample_code,transfer_time_start,transfer_time_end,transfer_per]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [receive_time_start,receive_time_end,receive_per,confirm_state,
											 {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											 }]
								
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [{
										text : '样本接收',
										iconCls : 'Pageadd',
										handler : me.onInsert
									}, {
										text : '生成单号打印',
										iconCls : 'Printer',
										handler : me.onPrint,
										hidden:true
									}
									]
								} ];

						me.callParent(arguments);
					},
					onPrint:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要打印的记录!");
							return;
						};
						var relay_id=selections[0].get("relay_id");
						printRelay(relay_id);
					},
					onInsert : function() {
						var me = this.up("gridpanel");
						var form = Ext.create(
								"Rds.children.form.ChildrenSampleReceiveInsertForm",
								{
									region : "center",
									autoScroll : true,
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '样本接收',
							width : 460,
							iconCls : 'Pageadd',
							height : 550,
							modal:true,
							layout : 'border',
							items : [ form ]
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
printRelay=function(relay_id){
	var print_chanel=function(){
		win.close();
	}
	var print_print=function(me){
		 var iframe = document.getElementById("relay_paper");
	     iframe.contentWindow.focus();
		 iframe.contentWindow.print();
		 win.close();
	}
	var win=Ext.create("Ext.window.Window",{
		title : "打印预览",
		iconCls : 'Find',
		layout:"auto",
		maximized:true,
		maximizable :true,
		modal:true,
		bodyStyle : "background-color:white;",
		html:"<iframe width=100% height=100% id='relay_paper' src='judicial/sampleRelay/printRelay.do?relay_id="+relay_id+"'></iframe>",
		buttons:[
					 {
							text : '打印',
							iconCls : 'Disk',
							handler:  print_print
					}, {
							text : '取消',
							iconCls : 'Cancel',
							handler: print_chanel
						} 
				]
			});
	win.show();
}