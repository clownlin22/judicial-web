Ext.define("Rds.children.panel.ChildrenPrintGridPanel",{
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
						var case_code = Ext.create('Ext.form.field.Text', {
							name : 'casecode',
							labelWidth : 60,
							width : '20%',
							regexText : '请输入案例编号',
							fieldLabel : '案例编号'
						});
						var sample_code = Ext.create('Ext.form.field.Text', {
							name : 'sample_code',
							labelWidth : 60,
							width : '20%',
							regexText : '请输入样本条码',
							fieldLabel : '样本条码'
						});
						var childname = Ext.create('Ext.form.field.Text', {
							name : 'childname',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '孩子姓名'
						});
						var case_areaname = Ext.create('Ext.form.field.Text', {
							name : 'case_areaname',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '归属地'
						});
						var starttime=Ext.create('Ext.form.DateField', {
							name : 'starttime',
							width : '20%',
							labelWidth : 70,
							fieldLabel : '采集日期 从',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date()
						});
						var endtime=Ext.create('Ext.form.DateField', {
							name : 'endtime',
							width : '20%',
							labelWidth : 20,
							fieldLabel : '到',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							value : Ext.Date.add(new Date(), Ext.Date.DAY,1)
						});
						var child_sex=Ext.create('Ext.form.ComboBox', {
									fieldLabel : '小孩性别',
									width : '20%',
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
																'女',
																0 ],
														[
																'男',
																1 ] ]
											}),
									value : -1,
									mode : 'local',
									name : 'child_sex',
								});
						var if_download=Ext.create('Ext.form.ComboBox', {
							fieldLabel : '是否下载',
							width : '20%',
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
														'是',
														1 ],
												[
														'否',
														2 ] ]
									}),
							value : -1,
							mode : 'local',
							name : 'if_download',
						});
						var case_userid = Ext.create('Ext.form.ComboBox', {
							xtype : 'combo',
							fieldLabel : '归属人',
							labelWidth : 60,
							width : '20%',
							name : 'case_userid',
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
						me.store = Ext.create('Ext.data.Store',{
							fields : [ "case_id",
									 'case_code',
									 'sample_code',
									 'child_name',
									 'child_sex',
									 'birth_date',
									 'id_number',
									 'house_area',
									 'life_area',
									 'case_username',
									 'case_areaname',
									 'print_time',
									 'gather_id',
									 'verify_state'],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'children/print/getCaseInfo.do',
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
											endtime : dateformat(endtime.getValue()),	
										    child_sex :child_sex.getValue(),
											starttime : dateformat(starttime.getValue()),
											child_name : trim(childname.getValue()),
											case_code : trim(case_code.getValue()),
											sample_code:trim(sample_code.getValue()),
											case_userid:case_userid.getValue(),
											case_areaname:trim(case_areaname.getValue()),
											if_download:if_download.getValue()
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
						me.columns = [
								{
									text : '案例条形码',
									dataIndex : 'case_code',
									menuDisabled : true,
									width : 150
								},{
									text : '样本条形码',
									dataIndex : 'sample_code',
									menuDisabled : true,
									width : 150
								},{
									text : '案例归属地',
									dataIndex : 'case_areaname',
									menuDisabled : true,
									width : 200
								}, {
									text : '归属人',
									dataIndex : 'case_username',
									menuDisabled : true,
									width : 120
								},{
									text : '打印状态',
									dataIndex : 'print_time',
									menuDisabled : true,
									width : 120,
									renderer : function(value, cellmeta, record, rowIndex,
											columnIndex, store) {
										var verify_state = record.data["verify_state"];
										var print_time = record.data["print_time"];
										if (verify_state > 6 && (""==print_time || null == print_time)) {
											return "<div style=\"color: red;\">已生成图片未下载</div>"
										} else if(verify_state < 7 ) {
											return "<div style=\"color: red;\">未生成图片</div>"
										} else{
											return "<div style=\"color: green;\">已生成图片已下载</div>"
										}

									}
								},
								{
									text : '孩子姓名',
									dataIndex : 'child_name',
									width : 200,
									menuDisabled : true
								},
								{
									text : '孩子性别',
									dataIndex : 'child_sex',
									menuDisabled : true,
									width : 100,
									renderer : function(value) {
										switch (value) {
										case 0:
											return "女";
											break;
										case 1:
											return "男";
											break;
										default:
											return "";
										}
									}
								},
								{
									text : '出生日期',
									dataIndex : 'birth_date',
									menuDisabled : true,
									width : 120
								},
								{
									text : '身份证号',
									dataIndex : 'id_number',
									menuDisabled : true,
									width : 200
								},
								{
									text : '户籍所在地',
									dataIndex : 'house_area',
									menuDisabled : true,
									width : 300
								},
								{
									text : '生活所在地',
									dataIndex : 'life_area',
									menuDisabled : true,
									width : 300,
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [case_code,case_userid,childname,child_sex,case_areaname ]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [sample_code,starttime,endtime,if_download,
											 {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											} ]
								
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [ {
										text : '查看监护人信息',
										iconCls : 'Find',
										handler : me.onFind
									}, {
										text : '查看采集人员信息',
										iconCls : 'Find',
										handler : me.onFindGather
									},{
										text : '生成打印图片',
										iconCls : 'User',
										handler : me.childrenCardCreate
									},{
										text : '下载图片',
										iconCls : 'Printer',
										handler : me.childrenCardDown
									},{
										text : '下载报告',
										iconCls : 'Printer',
										handler : me.childrenwordDown
									}
									]
								} ];

						me.callParent(arguments);
					},
					childrenwordDown:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();//得到被选中的标识
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
							return;
						}
						if(selections[0].get("verify_state") < 7 )
						{
							Ext.Msg.alert("提示", "该案例未生成打印图片，请先生成打印照片!");
							return;
						}
						Ext.MessageBox.confirm('提示', '确定下载报告吗？', function(id) {
							if (id == 'yes') {
								window.location.href = "children/register/downWord.do?case_id=" 
								+ selections[0].get("case_id")+"&case_code="
								+ selections[0].get("case_code");
							    me.getStore().load();
							}
						})
					},
					childrenCardDown:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
							return;
						}
						if(selections[0].get("verify_state") < 7 )
						{
							Ext.Msg.alert("提示", "该案例未生成打印图片!");
							return;
						}
						Ext.MessageBox.confirm('提示', '确定生成该案例照片吗？', function(id) {
							if (id == 'yes') {
								window.location.href = "children/register/download.do?case_id=" + selections[0].get("case_id")+"&case_code="+ selections[0].get("case_code");
							    me.getStore().load();
							}
						})
					},
					childrenCardCreate:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 || selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
							return;
						};
						Ext.MessageBox.confirm('提示', '确定生成该案例照片吗？', function(id) {
							if (id == 'yes') {
					         	Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
									url : "children/print/childrenCardCreate.do",
									method : "POST",
									headers : {
										'Content-Type' : 'application/json'
									},
									jsonData : {
										case_id : selections[0].get("case_id"),
										case_code:selections[0].get("case_code")
									},
									success : function(response, options) {
										response = Ext.JSON.decode(response.responseText);
										if(response.success){
											Ext.Msg.alert("提示", "卡片图片生成成功!");
										    me.getStore().load();
										}
										else
											Ext.Msg.alert("提示", response.msg);
									},
									failure : function() {
										Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
									}
								});
							}
						})
						
					},
					onFind : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要查看的记录!");
							return;
						};
						var win = Ext.create("Ext.window.Window", {
									title : "监护信息（案例条形码：" + selections[0].get("case_code")
											+ "）",
									width : 600,
									iconCls : 'Find',
									height : 400,
									layout : 'border',
									bodyStyle : "background-color:white;",
									items : [Ext.create('Ext.grid.Panel', {
												renderTo : Ext.getBody(),
												width : 600,
												height : 400,
												frame : false,
												viewConfig : {
													forceFit : true,
													stripeRows : true
													// 在表格中显示斑马线
												},
												store : {// 配置数据源
													fields : ['custody_id', 'custody_name',
															'custody_callname', 'id_number',
															'custody_call', 'phone'],// 定义字段
													proxy : {
														type : 'jsonajax',
														actionMethods : {
															read : 'POST'
														},
														url : 'children/register/getCustodyInfo.do',
														params : {
															'case_id' : selections[0]
																	.get("case_id")
														},
														reader : {
															type : 'json',
															root : 'items',
															totalProperty : 'count'
														}
													},
													autoLoad : true
													// 自动加载
												},
												columns : [// 配置表格列
												{
															header : "监护人姓名",
															dataIndex : 'custody_name',
															flex : 1,
															menuDisabled : true
														}, {
															header : "称谓",
															dataIndex : 'custody_callname',
															flex : 1,
															menuDisabled : true
														}, {
															header : "身份证",
															dataIndex : 'id_number',
															flex : 2,
															menuDisabled : true
														}, {
															header : "电话",
															dataIndex : 'phone',
															flex : 1,
															menuDisabled : true
														}]
											})]
								});
						win.show();
					},
					onFindGather : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要查看的记录!");
							return;
						};
						if (selections[0].get("gather_id") == null
								|| selections[0].get("gather_id") == '') {
							Ext.Msg.alert("提示", "此案例无采集人员!");
							return;
						}
						Ext.Ajax.request({
							url : "children/register/getGatherInfo.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : {
								gather_id : selections[0].get("gather_id")
							},
							success : function(response, options) {
								response = Ext.JSON.decode(response.responseText);
								var win = Ext.create("Ext.window.Window", {
											title : "采集人员信息（案例条形码："
													+ selections[0].get("case_code") + "）",
											width : 600,
											iconCls : 'Find',
											height : 190,
											bodyStyle : "background-color:white;",
											items : [{
														xtype : "container",
														layout : "column",
														height : 35,
														items : [{
																	xtype : "displayfield",
																	columnWidth : .45,
																	labelAlign : 'right',
																	labelWidth : 80,
																	fieldLabel : "姓名",
																	value : response.gather_name
																}]
													}, {
														xtype : "container",
														layout : "column",
														height : 35,
														items : [{
																	xtype : "displayfield",
																	columnWidth : .45,
																	labelAlign : 'right',
																	labelWidth : 80,
																	fieldLabel : "身份证号",
																	value : response.id_number
																}]
													}, {
														xtype : "container",
														layout : "column",
														height : 35,
														items : [{
																	xtype : "displayfield",
																	columnWidth : .45,
																	labelAlign : 'right',
																	labelWidth : 80,
																	fieldLabel : "电话",
																	value : response.phone
																}]
													}, {
														xtype : "container",
														layout : "column",
														height : 35,
														items : [{
																	xtype : "displayfield",
																	columnWidth : .45,
																	labelAlign : 'right',
																	labelWidth : 80,
																	fieldLabel : "所属单位",
																	value : response.company_name
																}]
													}]
										});
								win.show();
							},
							failure : function() {
								Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
							}
						});
					},
					onSearch : function() {
						var me = this.up("gridpanel");
						me.getStore().currentPage = 1;
						me.getStore().load();
					},
					listeners : {
						'afterrender' : function() {
							this.store.load();
						}
					}
				});
