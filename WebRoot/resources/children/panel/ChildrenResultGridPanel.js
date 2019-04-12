/**
 * 案例登记列表
 * 
 * @author chenwei
 */
Ext.define("Rds.children.panel.ChildrenResultGridPanel", {
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
			labelWidth : 60,
			width : '20%',
			fieldLabel : '儿童姓名'
		});
		var starttime=Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '实验上传 从',
			emptyText : '请选择日期',
			format : 'Y-m-d'
		});
		var endtime=Ext.create('Ext.form.DateField', {
			name : 'endtime',
			width : '20%',
			labelWidth : 20,
			fieldLabel : '到',
			emptyText : '请选择日期',
			format : 'Y-m-d'
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
	   var verify_state = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '审核状态',
			width : '20%',
			labelWidth : 70,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{fields : ['Name','Code' ],
						data : [
								//['全部','' ],
								//['未审核','0' ],
								//['待审核',1],
								//['审核不通过',2],
								//['审核通过',3],
								//['案例样本交接确认中',4],
								['实验中',5],
								['报告打印中',6],
								['邮寄中',7],
								['归档中',8],
								['已归档',9]
								]
					}),
			value : 5,
			mode : 'local',
			name : 'verify_state',
		});
	   var sample_result = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '上传数据',
			width : '20%',
			labelWidth : 70,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部','' ],['未上传',1],['已上传',2],]
					}),
			mode : 'local',
			name : 'sample_result',
		});
		me.store = Ext.create('Ext.data.Store', {
			fields : ['result_id', 'case_code','sample_code', "case_id", "result_in_time","child_name","gather_time",'child_sex'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'children/result/getResultInfo.do',
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
						endtime : dateformat(endtime.getValue()),
						starttime : dateformat(starttime.getValue()),
						gather_time_end : dateformat(gather_time_end.getValue()),
						gather_time_start : dateformat(gather_time_start.getValue()),
						child_name : trim(child_name.getValue()),
						case_code : trim(case_code.getValue()),
						sample_code:trim(sample_code.getValue()),
						verify_state:verify_state.getValue(),
						sample_result:sample_result.getValue()
					});
				}
			}
		});
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
		});
		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : me.pageSize,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
					dataIndex : 'case_id',
					hidden : true
				}, {
					dataIndex : 'result_id',
					hidden : true
				}, {
					text : '案例编号',
					dataIndex : 'case_code',
					menuDisabled : true,
					width : 150
				},{
					text : '样本编号',
					dataIndex : 'sample_code',
					menuDisabled : true,
					width : 150
				}, {
					text : '采集时间',
					dataIndex : 'gather_time',
					width : 200,
					menuDisabled : true
				
				},{
					text : '孩子名称',
					dataIndex : 'child_name',
					width : 200,
					menuDisabled : true
				}, {
					text : '性别',
					dataIndex : 'child_sex',
					menuDisabled : true,
					width : 50,
					renderer : function(value) {
						switch (value) {
							case 0 :
								return "女";
								break;
							case 1 :
								return "男";
								break;
							default :
								return "";
						}
					}
				},{
					text : '上传时间',
					dataIndex : 'result_in_time',
					menuDisabled : true,
					width : 100
				}];

		me.dockedItems = [{
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [case_code, child_name, starttime, endtime,verify_state ]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 	},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [sample_code,gather_time_start, gather_time_end,sample_result, {
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
					}, {
						text : '查看点位数据',
						iconCls : 'Find',
						handler : me.onResult
					}, {
						text : '上传',
						iconCls : 'Pageadd',
						handler : me.onUpload
					}, {
						text : '亲子鉴定导入',
						iconCls : 'Pageadd',
						handler : me.onUploadByIdentify
					}, {
						text : '手动录入',
						iconCls : 'Pageadd',
						handler : me.onUploadByHand
					}]
		}];
		me.callParent(arguments);
	},
	onUpdateByHand:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
			return;
		}
	},
	onUploadByHand:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
			return;
		}
		var result_id = selections[0].get("result_id");
		//判断是否导入过实验数据
		if(null == result_id){
			var form = Ext.create("Rds.children.form.HandCheckInForm", {
				region : "center",
				autoScroll : true,
				grid : me
			});
			var win = Ext.create("Ext.window.Window", {
						title : '点位录入',
						width : 400,
						iconCls : 'Pageedit',
						height : 600,
						layout : 'border',
						items : [form]
					});
			form.loadRecord(selections[0]);
			win.show()
		}else{
			Ext.MessageBox.confirm('提示', '该案例已存在实验数据，确认继续从手动录入?', function(id) {
				if (id == 'yes') {
					var form = Ext.create("Rds.children.form.HandCheckInForm", {
						region : "center",
						autoScroll : true,
						grid : me
					});
					var win = Ext.create("Ext.window.Window", {
								title : '点位录入',
								width : 400,
								iconCls : 'Pageedit',
								height : 600,
								layout : 'border',
								items : [form]
							});
					form.loadRecord(selections[0]);
					win.show();}
			}, {
				xtype : 'panel',
				region : "center",
				border : false
			});
		}
	},
	onUploadByIdentify:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要导入的记录!");
			return;
		};
		
		var sample_codes = "";
		var result_in_time=false;
		for(var i = 0 ; i < selections.length;i++){
			sample_codes += selections[i].get("sample_code")+',';
			if(null !=selections[i].get("result_in_time") && "" != selections[i].get("result_in_time")){
				result_in_time=true;
			}
		}
		sample_codes=sample_codes.substring(0,sample_codes.length-1);
		uploadIdentify_canel = function(me) {
			me.up("window").close();
		};
		uploadIdentify_update = function(m) {
			var mei = this.up("form");
			var form = mei.getForm();
			var values = form.getValues();
			values.sample_codes=sample_codes;
			values.agentia_name=Ext.getCmp("agentia_id_identify").getRawValue();
			if (form.isValid()) {
				//给出提示
				if(result_in_time){
					Ext.MessageBox.confirm('提示', '存在已导入过实验数据，确认继续从亲子鉴定导入?', function(id) {
						if (id == 'yes') {
							Ext.MessageBox.confirm('提示', '确认从亲子鉴定导入样本'+sample_codes+'吗?', function(id) {
								if (id == 'yes') {
									Ext.Ajax.request({
												url : "children/result/uploadSampleByIdentify.do",
												method : "POST",
												headers : {
													'Content-Type' : 'application/json'
												},
												jsonData : values,
												success : function(response, options) {
													response = Ext.JSON
															.decode(response.responseText);
													if (response.result) {
														Ext.MessageBox.alert("提示信息",
																"操作成功！");
														me.getStore().load();
														m.up("window").close();
													} else {
														Ext.MessageBox.alert("错误信息",
																response.message);
													}
												},
												failure : function() {
													Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
												}
											});
								}
							}, {
								xtype : 'panel',
								region : "center",
								border : false
							});
						}
					}, {
						xtype : 'panel',
						region : "center",
						border : false
					});
				}else
				{
					Ext.MessageBox.confirm('提示', '确定从亲子鉴定导入样本'+sample_codes+'吗?', function(id) {
						if (id == 'yes') {
							Ext.Ajax.request({
										url : "children/result/uploadSampleByIdentify.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : values,
										success : function(response, options) {
											response = Ext.JSON
													.decode(response.responseText);
											if (response.result) {
												Ext.MessageBox.alert("提示信息",
														"操作成功！");
												me.getStore().load();
												m.up("window").close();
											} else {
												Ext.MessageBox.alert("错误信息",
														response.message);
											}
										},
										failure : function() {
											Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
										}
									});
						}
					}, {
						xtype : 'panel',
						region : "center",
						border : false
					});
				}
			}
		};
		
		
		var win = Ext.create("Ext.window.Window", {
			title : '试剂选择',
			width : 400,
			iconCls : 'Pageadd',
			height : 200,
			layout : 'border',
			modal:true,
			items : [{
				xtype : 'form',
				region : 'center',
				style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
				labelAlign : "right",
				bodyPadding : 10,
				defaults : {
					anchor : '100%',
					labelWidth : 80,
					labelSeparator : "：",
					labelAlign : 'left'
				},
				border : false,
				autoHeight : true,
				buttons : [{
							text : '导入',
							iconCls : 'Disk',
							handler : uploadIdentify_update
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : uploadIdentify_canel
						}],
				items : [new Ext.form.field.ComboBox({
							name : 'agentia_id',
							id:'agentia_id_identify',
							fieldLabel : "试剂类型<span style='color:red'>*</span>",
							labelWidth : 70,
							editable : false,
							mode : 'remote',
							forceSelection : true,
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field is required!
							triggerAction : 'all',
							displayField : 'agentia_name',
							valueField : 'agentia_id',
							store : Ext.create('Ext.data.Store', {
										fields : ['agentia_id', 'agentia_name'],
										proxy : {
											type : 'jsonajax',
											actionMethods : {
												read : 'POST'
											},
											url : 'children/agentia/getAgentiaCombo.do',
											params : {},
											reader : {
												type : 'json',
												root : 'data'
											}
										}
									})
						})]
			}]
		});
		win.show();
	},
	onResult : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		};
		var result_id = selections[0].get("result_id");
		if(null == result_id){
			Ext.Msg.alert("提示", "该案例未没有实验数据!");
			return;
		}
		var locusStore = Ext.create('Ext.data.Store', {
					fields : ['case_id', 'locus_name', "locus_value"],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'children/result/getLocusInfo.do',
						params : {
							result_id : result_id
						},
						reader : {
							type : 'json',
							root : 'data'
						}
					}
				});
		var win = Ext.create("Ext.window.Window", {
					title : "点位信息（案例条形码：" + selections[0].get("case_code")
							+ "）",
					width : 600,
					iconCls : 'Find',
					height : 580,
					autoHeight : true,
					layout : 'border',
					bodyStyle : "background-color:white;",
					items : [Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 600,
								height : 580,
								frame : false,
								viewConfig : {
									forceFit : true,
									stripeRows : true
									// 在表格中显示斑马线
								},
								store : locusStore,
								columns : [// 配置表格列
								{
											header : "点位",
											dataIndex : 'locus_name',
											flex : 1,
											menuDisabled : true
										}, {
											header : "数据",
											dataIndex : 'locus_value',
											flex : 1,
											menuDisabled : true
										}]
							})]
				});
		locusStore.load();
		win.show();
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
	onUpload : function() {
		var me = this.up("gridpanel");
		upload_canel = function(me) {
			me.up("window").close();
		};
		upload_update = function(m) {
			var mei = this.up("form");
			var form = mei.getForm();
			var values = form.getValues();
			if (form.isValid()) {
				form.submit({
							url : 'children/result/addCaseResult.do',
							method : 'post',
							waitMsg : '正在上传数据...',
							success : function(form, action) {
								response = Ext.JSON
										.decode(action.response.responseText);
								console.log(response);
								if (response.result) {
									if("" == response.message){
										Ext.Msg.alert("提示", "请检查压缩包是否正确！");
									}else
										Ext.Msg.alert("提示", response.message);
									me.getStore().load();
									m.up("window").close();
								} else {
									if("" == response.message){
										Ext.Msg.alert("提示", "请检查压缩包是否正确！");
									}else
										Ext.Msg.alert("提示", response.message);
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "上传失败<br>请联系管理员!");
							}
						});
			}
		};
		var win = Ext.create("Ext.window.Window", {
			title : '文件上传',
			width : 400,
			iconCls : 'Pageadd',
			height : 200,
			layout : 'border',
			modal:true,
			items : [{
				xtype : 'form',
				region : 'center',
				style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
				labelAlign : "right",
				bodyPadding : 10,
				defaults : {
					anchor : '100%',
					labelWidth : 80,
					labelSeparator : "：",
					labelAlign : 'left'
				},
				border : false,
				autoHeight : true,
				buttons : [{
							text : '上传',
							iconCls : 'Disk',
							handler : upload_update
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : upload_canel
						}],
				items : [new Ext.form.field.ComboBox({
							name : 'agentia_id',
							id:'agentia_id',
							fieldLabel : "试剂类型<span style='color:red'>*</span>",
							labelWidth : 70,
							editable : false,
							mode : 'remote',
							forceSelection : true,
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field is required!
							triggerAction : 'all',
							displayField : 'agentia_name',
							valueField : 'agentia_id',
							store : Ext.create('Ext.data.Store', {
										fields : ['agentia_id', 'agentia_name'],
										proxy : {
											type : 'jsonajax',
											actionMethods : {
												read : 'POST'
											},
											url : 'children/agentia/getAgentiaCombo.do',
											params : {},
											reader : {
												type : 'json',
												root : 'data'
											}
										}
									}),
									 listeners :{
					                       	"select" : function(combo, record, index){
					                       		Ext.getCmp("agentia_name").setValue(combo.getRawValue());
					                       	}
					                       }
						}),{
								xtype : "hiddenfield",
								name : 'agentia_name',
								id:'agentia_name'
						},{
							xtype : 'filefield',
							name : 'files',
							labelWidth : 70,
							fieldLabel : "文件<span style='color:red'>*</span>",
							msgTarget : 'side',
							allowBlank : false,
							labelAlign : 'left',
							anchor : '100%',
							buttonText : '选择文件',
							validator : function(v) {
								if (!v.endWith(".rar")) {
									return "请选择.rar类型的文件";
								}
								return true;
							}
						}]
			}]
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
