onDownload = function(value) {
	window.location.href = "children/register/downloadPhoto.do?photo_id=" + value;
}
Ext.define("Rds.children.panel.ChildrenPhotoProcessGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	region : 'center',
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
		var child_name = Ext.create('Ext.form.field.Text', {
			name : 'child_name',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '儿童姓名'
		});
		var id_number = Ext.create('Ext.form.field.Text', {
			name : 'id_number',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '儿童身份证'
		});
		var gather_time_start=Ext.create('Ext.form.DateField', {
			name : 'gather_time_start',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '采集时间从',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var gather_time_end=Ext.create('Ext.form.DateField', {
			name : 'gather_time_end',
			width : '20%',
			labelWidth : 20,
			fieldLabel : '到',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY,1)
		});
	   var  photo_type = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '处理状态',
			width : '20%',
			labelWidth : 70,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{   
						fields : ['Name','Code' ],
						data : [
								['未处理',1],
								['已处理',2]
								]
					}),
			value : 1,
			mode : 'local',
			name : 'photo_type',
		});
		me.store = Ext.create('Ext.data.Store', {
			fields : [ 'photo_id', 'case_code','sample_code','case_id', 'photo_path','id_number',
			           'upload_time','photo_type','upload_username','child_name'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'children/register/queryPageCasePhoto.do',
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
				'beforeload' : function(ds, operation, opt) {
					Ext.apply(me.store.proxy.extraParams, {
						gather_time_end : dateformat(gather_time_end.getValue()),
						gather_time_start : dateformat(gather_time_start.getValue()),
						child_name : trim(child_name.getValue()),
						case_code : trim(case_code.getValue()),
						sample_code:trim(sample_code.getValue()),
						photo_type:photo_type.getValue(),
						id_number:trim(id_number.getValue())
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
						text : '案例编号',
						dataIndex : 'case_code',
						width : '10%',
						menuDisabled : true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var photo_type = record.data["photo_type"];
							if (photo_type == 2) {
								return "<div style=\"color: green;\">"
										+ value + "</div>"
							} else {
								return "<div style=\"color: red;\">"
								+ value + "</div>"
							}
						}
					},{
						text : '样本编号',
						dataIndex : 'sample_code',
						width : '10%',
						menuDisabled : true,
					},{
						text : '儿童姓名',
						dataIndex : 'child_name',
						width : '7%',
						menuDisabled : true,
					}, {
						text : '儿童身份证',
						dataIndex : 'id_number',
						width : '10%',
						menuDisabled : true,
					},{
						header : "查看照片",
						dataIndex : '',
						width : '7%',
						menuDisabled : true,
						renderer : function(value, cellmeta,
								record, rowIndex, columnIndex,
								store) {
							if(record.data["photo_type"] != null){
								return "<a href='#'>查看</a>";
							}
						},
						listeners:{
							'click':function(){ 
								var me = this.up("gridpanel");
								var selections = me.getView().getSelectionModel().getSelection();
								if (selections.length < 1 || selections.length > 1) {
									Ext.Msg.alert("提示", "请选择需要查看的一条记录!");
									return;
								}

								if(null != selections[0].get("photo_type")){
									var form = Ext.create(
											"Rds.children.form.ChildrenImageShow", {
												region : "center",
												grid : me
											});
									var win = Ext.create("Ext.window.Window", {
												title : '图片查看',
												width : 600,
												iconCls : 'Pageedit',
												height : 600,
												maximizable : true,
												layout : 'border',
												items : [form]
											});
									form.loadRecord(selections[0]);
									win.show();
								}
							}
						}
					} ,{
						header : "下载",
						dataIndex : 'photo_id',
						width : '7%',
						menuDisabled : true,
						renderer : function(value, cellmeta,
								record, rowIndex, columnIndex,
								store) {
							var photo_id = record.data["photo_id"];
							var photo_type = record.data["photo_type"];
							if (value == null||value=='') {
								return "<div style=\"color: red;\">尚未上传</div>"
							} else {
								if(record.data["photo_type"]==1 || record.data["photo_type"] == 2)
									return "<a href='#' onclick=\"onDownload('" + photo_id
									+ "')\"  >下载";
								else 
									return "";
							}
						}
					},{
						header : "操作",
						dataIndex : '',
						width : '10%',
						menuDisabled : true,
						renderer : function(value, cellmeta,
								record, rowIndex, columnIndex,
								store) {
							if(record.data["photo_type"]==1 || record.data["photo_type"] == 2){
								return "<a href='#'>重新上传</a>";
							}
						},
						listeners:{
							'click':function(){ 
								var mei = this.up("gridpanel");
								var selections = mei.getView().getSelectionModel().getSelection();
								if (selections.length < 1) {
									Ext.Msg.alert("提示", "请选择案例!");
									return;
								}
								if( selections[0].get("photo_type") == 1 ||  selections[0].get("photo_type")==2){
									var form = Ext.create('Ext.form.Panel', {
										region : "center",
										autoScroll : true,
										layout : 'anchor',
										bodyPadding : 10,
										defaults : {
											anchor : '100%'
										},
										defaultType : 'textfield',
										items : [{
													xtype : "hiddenfield",
													name : 'case_id'
												},{
													xtype : "hiddenfield",
													name : 'photo_id'
												},{
													xtype : "hiddenfield",
													name : 'photo_type'
												},{
													xtype : "hiddenfield",
													name : 'photo_type_old',
													value:'2'
												},{
													xtype : "hiddenfield",
													name : 'photo_path'
												},{
													xtype : "textfield",
													labelWidth : 80,
													fieldLabel : '儿童姓名',
													readOnly : true,
													name : 'child_name'
												},{
													xtype : 'filefield',
													name : 'headPhoto',
													fieldLabel : '一寸照片',
													msgTarget : 'side',
													labelWidth : 80,
													allowBlank : false,
													anchor : '100%',
													buttonText : '选择照片',
													validator : function(v) {
														if (!v.endWith(".jpg") &&!v.endWith(".JPG")
																&& !v.endWith(".png") && !v.endWith(".PNG")
																&& !v.endWith(".gif") && !v.endWith(".GIF")
																&& !v.endWith(".jpeg") && !v.endWith(".JPEG") ) {
															return "请选择.jpg .png .gif.jpeg类型的图片";
														}
														return true;
													}
												}],
					
										buttons : [{
											text : '上传',
											iconCls : 'Diskupload',
											handler : function() {
												var me = this;
												var myWindow = me.up('window');
												var form = me.up('form').getForm();
												if (!form.isValid()) {
													Ext.MessageBox.alert("提示信息", "请选择照片!");
													return;
												}
												form.submit({
															url : 'children/register/photoUpload.do',
															method : 'post',
															waitMsg : '正在上传您的文件...',
															success : function(form, action) {
																Ext.Msg.alert("提示", "上传成功!");
																var grid = mei.up("gridpanel");
																mei.getStore().load();
																myWindow.close();
															},
															failure : function() {
																Ext.Msg.alert("提示", "上传失败，请联系管理员!");
															}
														});
					
											}
										}, {
											text : '取消',
											iconCls : 'Cancel',
											handler : function() {
												this.up('window').close();
											}
										}]
									});
									var win = Ext.create("Ext.window.Window", {
												title : '案例添加',
												width : 400,
												iconCls : 'Pageadd',
												height : 300,
												modal : true,
												layout : 'border',
												items : [form]
											});
									form.loadRecord(selections[0]);
									win.show();
								}
							}
						}
					
					},{
						text : '类型',
						dataIndex : 'photo_type',
						width : '10%',
						menuDisabled : true,
						renderer : function(value) {
							switch (value) {
								case "1" :
									return "<div style=\"color: green;\">初始照片</div>";
									break;
								case "2" :
									return "<div style=\"color: red;\">已处理照片</div>";
									break;
								case "3" :
									return "<div style=\"color: red;\">生成照片正面</div>";
									break;
								case "4" :
									return "<div style=\"color: red;\">生成照片反面</div>";
									break;
								case "5" :
									return "<div>登记表格</div>";
									break;
								default :
									return "";
							}
						}
					}, {
					text : '最后上传日期',
					dataIndex : 'upload_time',
					width : '20%',
					menuDisabled : true,
				},{
					text : '上传人员',
					dataIndex : 'upload_username',
					width : '10%',
					menuDisabled : true,
				}];

		me.dockedItems = [{
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [case_code, child_name, photo_type,gather_time_start, gather_time_end ]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 	},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [sample_code,id_number, {
						text : '查询',
						iconCls : 'Find',
						handler : me.onSearch
					}]
		
		}, {
			xtype : 'toolbar',
			dock : 'top',
			items : [{
						text : '查看监护人信息',
						iconCls : 'Find',
						handler : me.onFind
					}]
		}];
		me.callParent(arguments);
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
