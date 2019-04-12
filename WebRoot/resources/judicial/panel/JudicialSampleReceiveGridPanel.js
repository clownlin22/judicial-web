Ext.define("Rds.judicial.panel.JudicialSampleReceiveGridPanel",{
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
						var starttime=Ext.create('Ext.form.DateField', {
							name : 'starttime',
							width : 175,
							labelWidth : 70,
							fieldLabel : '受理时间 从',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value : Ext.Date.add(
									new Date(),
									Ext.Date.DAY,-7),
							listeners : {
								select : function() {
									var start = starttime
											.getValue();
									var end = endtime
											.getValue();
									endtime.setMinValue(
											start);
								}
							}
						});
						var endtime=Ext.create('Ext.form.DateField', {
							name : 'endtime',
							width : 135,
							labelWidth : 20,
							fieldLabel : '到',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date(),
							value : Ext.Date.add(new Date(), Ext.Date.DAY),
							listeners : {
								select : function() {
									var start = starttime
											.getValue();
									var end = endtime
											.getValue();
									starttime.setMaxValue(
											end);
								}
							}
						});
					    var is_delete=Ext.create('Ext.form.ComboBox', 
								{
									fieldLabel : '是否删除',
									width : 155,
									labelWidth : 60,
									editable : false,
									triggerAction : 'all',
									displayField : 'Name',
									labelAlign : 'right',
									valueField : 'Code',
									store : new Ext.data.ArrayStore(
											{
												fields : [
														'Name',
														'Code' ],
												data : [
														[
																'全部',
																-1 ],
														[
																'未删除',
																0 ],
														[
																'已删除',
																1 ] ]
											}),
									value : 0,
									mode : 'local',
									// typeAhead: true,
									name : 'is_delete',
								});
						me.store = Ext
								.create(
										'Ext.data.Store',
										{
											fields : [ "receive_id","receive_per","use_state","receive_pername","receive_remark","receive_time","is_delete","relay_id","relay_code"],
											start:0,
											limit:15,
											pageSize:15,
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'judicial/sampleRelay/getSampleReceiveInfo.do',
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
													Ext
													.apply(
															me.store.proxy.extraParams,{								
															endtime : dateformat(endtime
															               .getValue()),	
															starttime : dateformat(starttime
																	.getValue()),
															is_delete:is_delete.getValue(),
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
						me.columns = [{
									text : '交接单号',
									dataIndex : 'relay_code',
									menuDisabled : true,
									width : 120
								},{
									text : '接收人',
									dataIndex : 'receive_pername',
									menuDisabled : true,
									width : 200,
									renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var is_delete= record.data["is_delete"];
										if (is_delete == 1) {
											return "<div style=\"text-decoration: line-through;color: red;\">"
													+ value + "</div>";
										} else {
											return value;
										}
									}
								},
								{
									text : '接收时间',
									dataIndex : 'receive_time',
									menuDisabled : true,
									width : 200
								},
								{
									text : '接收备注',
									dataIndex : 'receive_remark',
									menuDisabled : true,
									width:500
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [
											 starttime,endtime,
											 {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											} ]
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [{
										text : '查看样本',
										iconCls : 'Find',
										handler : me.onFind
									}, {
										text : '样本接收',
										iconCls : 'Pageadd',
										handler : me.onInsert
									}, {
										text : '生成单号打印',
										iconCls : 'Printer',
										handler : me.onPrint
									},
									 {
										text : '样本编号下载',
										iconCls : 'Add',
										handler : me.onSampleExport
									},{
										text : '样本接收修改',
										iconCls : 'Pageedit',
										handler : me.onUpdate,
										hidden:true
									}, {
										text : '删除',
										iconCls : 'Delete',
										handler : me.onDelete,
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
					
					onSampleExport:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
						.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要导出单号的记录!");
							return;
						};
						var receive_id=selections[0].get("receive_id");
						var relay_code= selections[0].get("relay_code");
						window.location.href = "judicial/sampleRelay/onSampleExport.do?receive_id="+receive_id+'&relay_code='+relay_code;
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
							width : 300,
							iconCls : 'Find',
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
									fields : [ 'sample_code'],// 定义字段
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'judicial/sampleRelay/getReceiveSampleInfo.do',
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
								}]
							}) ]
						});
						win.show();
					},
				    onDelete : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要删除的记录!");
							return;
						};
						if(selections[0].get("is_delete")==1){
							Ext.Msg.alert("提示", "此次接收已删除!");
							return;
						};
						if(selections[0].get("use_state")==1){
							Ext.Msg.alert("提示", "此次接收中样本已被交接,无法删除!");
							return;
						}
						var values = {
							receive_id : selections[0].get("receive_id")
						};
						Ext.MessageBox
								.confirm(
										'提示',
										'确定删除此次接收吗',
										function(id) {
											if (id == 'yes') {
												Ext.Ajax
														.request({
															url : "judicial/sampleRelay/deleteReceiveSampleInfo.do",
															method : "POST",
															headers : {
																'Content-Type' : 'application/json'
															},
															jsonData : values,
															success : function(
																	response,
																	options) {
																response = Ext.JSON
																		.decode(response.responseText);
																if (response == true) {
																	Ext.MessageBox
																			.alert(
																					"提示信息",
																					"删除成功！");
																	me
																			.getStore()
																			.load();
																} else {
																	Ext.MessageBox
																			.alert(
																					"错误信息",
																					"删除失败！");
																}
															},
															failure : function() {
																Ext.Msg
																		.alert(
																				"提示",
																				"网络故障<br>请联系管理员!");
															}
														});
											}
										});
					},
					onInsert : function() {
						var me = this.up("gridpanel");
						var form = Ext.create(
								"Rds.judicial.form.JudicialSampleReceiveInsertForm",
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
					onUpdate : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要修改的记录!");
							return;
						}
						if(selections[0].get("is_delete")==1){
							Ext.Msg.alert("提示", "此次交接已删除，无法修改!");
							return;
						}
						if(selections[0].get("use_state")==1){
							Ext.Msg.alert("提示", "此次接收中样本已被交接,无法修改!");
							return;
						}
						var form = Ext.create(
								"Rds.judicial.form.JudicialSampleReceiveUpdateForm", {
									region : "center",
									grid : me
								});
						var win = Ext.create("Ext.window.Window", {
							title : '样本接收',
							width : 460,
							iconCls : 'Pageedit',
							height : 550,
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