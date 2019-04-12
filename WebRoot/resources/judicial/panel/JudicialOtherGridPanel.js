var exportTemp="";
case_canel = function(me) {
	me.up("window").close();
}
/**
 * @description 案例审核
 * @author fushaoming
 * @date 20150414
 */
getSampleInfo = function(value, code) {
	var win = Ext.create("Ext.window.Window", {
				title : "样本信息（案例条形码：" + code + "）",
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
								fields : ['sample_id', 'sample_code',
										'sample_type', 'sample_call',
										'sample_callname', 'sample_username',
										'id_number', 'birth_date'],// 定义字段
								proxy : {
									type : 'jsonajax',
									actionMethods : {
										read : 'POST'
									},
									url : 'judicial/register/getSampleInfo.do',
									params : {
										'case_id' : value
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
										header : "样本条形码",
										dataIndex : 'sample_code',
										flex : 1,
										menuDisabled : true
									}, {
										header : "称谓",
										dataIndex : 'sample_callname',
										flex : 1,
										menuDisabled : true
									}, {
										header : "姓名",
										dataIndex : 'sample_username',
										flex : 1,
										menuDisabled : true
									}, {
										header : "身份证号",
										dataIndex : 'id_number',
										flex : 2,
										menuDisabled : true
									}, {
										header : "出生日期",
										dataIndex : 'birth_date',
										flex : 1,
										menuDisabled : true
									}, {
										header : "样本类型",
										dataIndex : 'sample_type',
										flex : 1,
										menuDisabled : true
									}]
						})]
			});
	win.show();
}

Ext.define("Rds.judicial.panel.JudicialOtherGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
    selModel: Ext.create('Ext.selection.CheckboxModel'),
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var storeModel = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getReportModelByPartner.do',
				params : {
					type : 'dna',
					receiver_id : ""
				},
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true,
			listeners : {
				'load' : function() {
					var allmodel = new model({
						'key' : '',
						'value' : '全部'
					});
					this.insert(0, allmodel);
					report_type.select(this.getAt(0));
				}
			}
		});
		var report_type = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '模板类型',
			mode : 'local',
			labelWidth : 60,
			editable : false,
			valueField : "key",
			width : '20%',
			displayField : "value",
			name : 'report_type',
			store : storeModel,
		});
		var case_code = Ext.create('Ext.form.field.Text', {
			name : 'case_code',
			labelWidth : 60,
			width : '20%',
			regex : /^\w*$/,
			emptyText : '请输入案例编号',
			fieldLabel : '案例编号'
		});
		var case_userid = Ext.create('Ext.form.field.Text', {
			name : 'case_userid',
			labelWidth : 70,
			width : '20%',
			emptyText : '请输入归属人',
			fieldLabel : '归属人'
		});

		var receiver_area = Ext.create('Ext.form.field.Text', {
			name : 'receiver_area',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '归属地'
		});
		var serial_number = Ext.create('Ext.form.field.Text', {
			name : 'serial_number',
			labelWidth : 60,
			width : '20%',
			emptyText : '请输入物鉴字号',
			fieldLabel : '物鉴字号'
		});
		var client = Ext.create('Ext.form.field.Text', {
			name : 'client',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '委托人'
		});
		var sample_in_per = Ext.create('Ext.form.field.Text', {
			name : 'sample_in_per',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '采样人'
		});
		var parnter_name = Ext.create('Ext.form.field.Text', {
			name : 'parnter_name',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '合作商'
		});
		var phone = Ext.create('Ext.form.field.Text', {
			name : 'phone',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '电话号码'
		});
		var starttime=Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '鉴定日期 从',
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY,-7),
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
			width : '20%',
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
	    var source_type = Ext.create('Ext.form.ComboBox', 
				{
					fieldLabel : '案例来源',
					width : '20%',
					labelWidth : 60,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					valueField : 'Code',
					store : new Ext.data.ArrayStore(
							{fields : ['Name','Code' ],
								data : [
										['全部','' ],
										['市场','0' ],
										['网络','1']]
							}),
					value : '',
					mode : 'local',
					name : 'source_type',
				});
	    var verify_state = Ext.create('Ext.form.ComboBox', 
				{
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
										['全部','' ],
										['未审核','0' ],
										['待审核',1],
										['审核不通过',2],
										['审核通过',3],
										['案例样本交接确认中',4],
										['实验中',5],
										['报告打印中',6],
										['报告确认中',7],
										['邮寄中',8],
										['归档中',9],
										['已归档',10]
										]
							}),
					value : '',
					mode : 'local',
					name : 'verify_state',
				});
	    var confirm_code = Ext.create('Ext.form.field.Text', {
			name : 'confirm_code',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '激活码'
		});
	    var case_state = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '案例状态',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{fields : ['Name','Code' ],
						data : [
								['全部','' ],
								['正常','0' ],
								['为先出报告后付款',1],
								['免单',2],
								['优惠',3],
								['月结',4],
								['二次采样',5],
								['补样',6]
								]
					}),
			value : '',
			mode : 'local',
			name : 'case_state',
		});
		me.store = Ext.create('Ext.data.Store', {
					fields : [
								'case_id', 'case_code','case_userid','confirm_code','serial_number',
								'case_areacode','typeid', 'receiver_area', 'case_receiver',
								'urgent_state',  'remark',  'print_count','verify_state',
								'accept_time','consignment_time','close_time', 'report_modelname','client',
								'report_model','address', 'purpose','case_in_per', 'case_in_pername','phone',
								'sample_in_time','is_delete', 'sample_in_per','unit_type','sample_relation',
								'case_type','agent','copies','parnter_name','case_state','source_type','case_state',
								'process_instance_id', 'task_id', 'task_def_key', 'task_name', 'suspension_state','has_comment'
                    ],
                    start:0,
					limit:15,
					pageSize:15,
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/verify/getCaseInfo.do',
						params : {},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'total'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							exportTemp = "starttime="+dateformat(starttime.getValue())+
										 "&endtime="+dateformat(endtime.getValue())+
										 "&case_code="+trim(case_code.getValue())+
										 "&verify_state="+verify_state.getValue()+
										 "&client="+trim(client.getValue())+
										 "&report_model="+(report_type.getValue()==null?"":report_type.getValue())+
										 "&phone="+trim(phone.getValue());
							Ext.apply(me.store.proxy.extraParams, {
								starttime : dateformat(starttime
										.getValue()),
								case_userid : case_userid.getValue(),
								endtime : dateformat(endtime.getValue()),
								case_code : trim(case_code.getValue()),
								receiver_area:trim(receiver_area.getValue()),
								verify_state :verify_state.getValue(),
								phone:trim(phone.getValue()),
								client:trim(client.getValue()),
								source_type:source_type.getValue(),
								case_state:case_state.getValue(),
								confirm_code:trim(confirm_code.getValue()),
								parnter_name:trim(parnter_name.getValue()),
								sample_in_per:trim(sample_in_per.getValue()),
								serial_number:trim(serial_number.getValue()),
//								case_type:'0',
								report_model:report_type.getValue(),
								
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
					text : '案例case_id',
					dataIndex : 'case_id',
					width : '10%',
					hidden : true,
					menuDisabled : true
				},{
					text : '案例条形码',
					dataIndex : 'case_code',
					menuDisabled : true,
					width : 150
				},
				{
					text : '物鉴字号',
					dataIndex : 'serial_number',
					menuDisabled : true,
					width : 190
				},
				{
					text : '鉴定日期',
					dataIndex : 'accept_time',
					menuDisabled : true,
					width : 100
				},
				{
					text : '案例归属地',
					dataIndex : 'receiver_area',
					menuDisabled : true,
					width : 200
				},
				{
					text : '归属人',
					dataIndex : 'case_receiver',
					menuDisabled : true,
					width : 120,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						var agent= record.data["agent"];
						if (agent != ''&& agent!=null) {
							return value+"(代理："+agent+")";
						} else {
							return value;
						}
					}
				},
				{
					text : '委托人',
					dataIndex : 'client',
					menuDisabled : true,
					width : 120
				},
				{
					text : '电话号码',
					dataIndex : 'phone',
					menuDisabled : true,
					width : 120
				},
				{
					text : '紧急程度',
					dataIndex : 'urgent_state',
					menuDisabled : true,
					width : 100,
					renderer : function(value) {
						switch (value) {
						case 0:
							return "普通";
							break;
						case 1:
							return "<span style='color:red'>紧急</span>";
							break;
						default:
							return "";
						}
					}
				}, {
					text : '案例审核状态',
					dataIndex : 'verify_state',
					menuDisabled : true,
					width : 150,
					renderer : function(value, meta, record) {
						switch (value) {
						case 0:
							return "未审核";
//							if(record.get("task_def_key")=="taskAudit"){
//								return "已提交审核";
//							}else{
//								return "未审核";
//							}
							break;
						case 1:
							if(record.get("task_def_key")=="taskAudit"){
								return "已提交审核";
							}else{
								return "<span style='color:red'>案例审核未通过</span><a class='lbtnComment' href='#'>查看原因</a>";
							}
							break;
						case 2:
							 if(record.get("task_def_key")=="taskAudit"){
                                    return "已提交审核";
                                }else{
                                    return "<span style='color:red'>案例审核未通过</span><a class='lbtnComment' href='#'>查看原因</a>";
                                }
								break;
						case 3:
							if(record.get("case_code")=='')
								return "<span style='color:red'>审核通过有误，重新审核</span>";
							else
								return "<span style='color:green'>审核通过</span>";
							break;
						case 4:
							return "<span style='color:red'>样本交接中</span>";
							break;
						case 5:
							return "<span style='color:red'>实验中</span>";
							break;
						case 6:
							return "<span style='color:red'>报告打印中</span>";
							break;
						case 7:
							return "<span style='color:red'>报告确认中</span>";
							break;
						case 8:
							return "<span style='color:red'>邮寄中</span>";
							break;
						case 9:
							return "<span style='color:red'>归档中</span>";
							break;
						case 10:
							return "<span style='color:red'>已归档</span>";
							break;
						default:
							return "";
						}
					}
				},{
					text : '截止日期',
					dataIndex : 'close_time',
					menuDisabled : true,
					width : 100
				},
				{
					text : '样本登记日期',
					dataIndex : 'sample_in_time',
					menuDisabled : true,
					width : 150
				},
				{
					text : '模板名称',
					dataIndex : 'report_modelname',
					menuDisabled : true,
					width : 100
				},
				{
					text : '登记人',
					dataIndex : 'case_in_pername',
					menuDisabled : true,
					width : 100
				},{
					text : '采样人',
					dataIndex : 'sample_in_per',
					menuDisabled : true,
					width : 100
				},{
					text : '案例状态',
					dataIndex : 'case_state',
					menuDisabled : true,
					width : 200,
					renderer : function(value,meta,record) {
						switch (value) {
						case 0:
							return "正常";
							break;
						case 1:
							return "为先出报告后付款";
							break;
						case 2:
							return "免单";
							break;
						case 3:
							return "优惠";
							break;
						case 4:
							return "月结";
							break;
						case 5:
							return "二次采样";
							break;
						case 6:
							return "补样";
							break;
						default:
							return "";
						}
					}
				},{
					text : '合作商',
					dataIndex : 'parnter_name',
					menuDisabled : true,
					width : 100
				},{
					text : '案例来源',
					dataIndex : 'source_type',
					menuDisabled : true,
					width : 200,
					renderer : function(value,meta,record) {
						switch (value) {
						case '0':
							return "市场";
							break;
						case '1':
							return "网络";
							break;
						default:
							return "";
						}
					}
				},{
					text : '备注',
					dataIndex : 'remark',
					menuDisabled : true,
					width : 300
				}];

		me.dockedItems = [{
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [case_code,case_userid,receiver_area,receiver_area,client,phone ]
		}, {
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 	},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [ source_type,verify_state,case_state,sample_in_per,confirm_code]
		},{

			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 	},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [parnter_name,starttime, endtime, report_type,serial_number]
		
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 	},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [{
						text : '查询',
						iconCls : 'Find',
						handler : me.onSearch
					} ]
		},{
				xtype : 'toolbar',
				dock : 'top',
				items : [ {
					text : '查看案例信息',
					iconCls : 'Camera',
					handler : me.onVerifyCheck
				},{
					text : '查看样本信息',
					iconCls : 'Find',
					handler : me.onFind
				},
				{
					text: '查看流程状态',
					iconCls: 'Pageedit',
					handler: me.onTaskHistory
				},{
					text : '查看案例关联',
					iconCls : 'Find',
					handler : me.onFindCase
				},{
					text : '导出案例流程信息',
					iconCls : 'Pageexcel',
					handler : me.onCaseExport
				}
			]
			},{
				style : {
			        borderTopWidth : '0px !important',
			        borderBottomWidth : '0px !important'
			 	},
				xtype : 'toolbar',
				dock : 'top',
				items : [{
							text : '跳过样本接收',
							iconCls : 'Pageedit',
							handler : me.onSkip1,
						},{
							text : '跳过样本交接',
							iconCls : 'Pageedit',
							handler : me.onSkip2,
						},{
							text : '跳过样本确认',
							iconCls : 'Pageedit',
							handler : me.onSkip3,
						},{
							text : '跳过实验',
							iconCls : 'Pageedit',
							handler : me.onSkip4,
						},{
							text : '跳过报告打印',
							iconCls : 'Pageedit',
							handler : me.onSkip5,
						},{
							text : '跳过报告交接',
							iconCls : 'Pageedit',
							handler : me.onSkip6,
						},{
							text : '跳过报告确认',
							iconCls : 'Pageedit',
							handler : me.onSkip7,
						}
		                ]
				}];

		me.callParent(arguments);
	},
	onSkip1 : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
				.getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
			return;
		};
		if(selections[0].get("case_code")=='' || selections[0].get("case_code")==null){
			Ext.Msg.alert("提示", "此案例未生成案例编号，无法操作!");
			return;
		}
		if(selections[0].get("is_delete")!=0){
			Ext.Msg.alert("提示", "此案例已废除!");
			return;
		}
		var values = {
			case_id : selections[0].get("case_id"),
			case_code : selections[0].get("case_code"),
			state:'1'
		};
		Ext.MessageBox.confirm(
						'提示',
						'确定此案例跳过样本接受？'+selections[0].get("case_code"),
						function(id) {
							if (id == 'yes') {
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
											url : "judicial/other/caseCodeRun.do",
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
												console.log(response);
												if (response.result) {
													Ext.MessageBox.alert("提示信息","流程操作成功！");
													me.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息",response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
											}
										});
							}
						});
	},
	onSkip2 : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
				.getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
			return;
		};
		if(selections[0].get("case_code")=='' || selections[0].get("case_code")==null){
			Ext.Msg.alert("提示", "此案例未生成案例编号，无法操作!");
			return;
		}
		if(selections[0].get("is_delete")!=0){
			Ext.Msg.alert("提示", "此案例已废除!");
			return;
		}
		var values = {
			case_id : selections[0].get("case_id"),
			case_code : selections[0].get("case_code"),
			state:'2'
		};
		Ext.MessageBox.confirm(
						'提示',
						'确定此案例跳过样本交接?'+selections[0].get("case_code"),
						function(id) {
							if (id == 'yes') {
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
											url : "judicial/other/caseCodeRun.do",
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
												if (response.result) {
													Ext.MessageBox.alert("提示信息","流程操作成功！");
													me.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息",response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
											}
										});
							}
						});
	},
	onSkip3 : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
				.getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
			return;
		};
		if(selections[0].get("case_code")=='' || selections[0].get("case_code")==null){
			Ext.Msg.alert("提示", "此案例未生成案例编号，无法操作!");
			return;
		}
		if(selections[0].get("is_delete")!=0){
			Ext.Msg.alert("提示", "此案例已废除!");
			return;
		}
		var values = {
			case_id : selections[0].get("case_id"),
			case_code : selections[0].get("case_code"),
			state:'3'
		};
		Ext.MessageBox.confirm(
						'提示',
						'确定此案例跳过样本确认?'+selections[0].get("case_code"),
						function(id) {
							if (id == 'yes') {
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
											url : "judicial/other/caseCodeRun.do",
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
												if (response.result) {
													Ext.MessageBox.alert("提示信息","流程操作成功！");
													me.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息",response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
											}
										});
							}
						});
	},
	onSkip4 : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
				.getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
			return;
		};
		if(selections[0].get("case_code")=='' || selections[0].get("case_code")==null){
			Ext.Msg.alert("提示", "此案例未生成案例编号，无法操作!");
			return;
		}
		if(selections[0].get("is_delete")!=0){
			Ext.Msg.alert("提示", "此案例已废除!");
			return;
		}
		var values = {
			case_id : selections[0].get("case_id"),
			case_code : selections[0].get("case_code"),
			state:'4'
		};
		Ext.MessageBox.confirm(
						'提示',
						'确定此案例跳过实验?'+selections[0].get("case_code"),
						function(id) {
							if (id == 'yes') {
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
											url : "judicial/other/caseCodeRun.do",
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
												if (response.result) {
													Ext.MessageBox.alert("提示信息","流程操作成功！");
													me.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息",response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
											}
										});
							}
						});
	},
	onSkip5 : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
				.getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
			return;
		};
		if(selections[0].get("case_code")=='' || selections[0].get("case_code")==null){
			Ext.Msg.alert("提示", "此案例未生成案例编号，无法操作!");
			return;
		}
		if(selections[0].get("is_delete")!=0){
			Ext.Msg.alert("提示", "此案例已废除!");
			return;
		}
		var values = {
			case_id : selections[0].get("case_id"),
			case_code : selections[0].get("case_code"),
			state:'5'
		};
		Ext.MessageBox.confirm(
						'提示',
						'确定此案例跳过报告打印？'+selections[0].get("case_code"),
						function(id) {
							if (id == 'yes') {
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
											url : "judicial/other/caseCodeRun.do",
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
												if (response.result) {
													Ext.MessageBox.alert("提示信息","流程操作成功！");
													me.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息",response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
											}
										});
							}
						});
	},
	onSkip6 : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
				.getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
			return;
		};
		if(selections[0].get("case_code")=='' || selections[0].get("case_code")==null){
			Ext.Msg.alert("提示", "此案例未生成案例编号，无法操作!");
			return;
		}
		if(selections[0].get("is_delete")!=0){
			Ext.Msg.alert("提示", "此案例已废除!");
			return;
		}
		var values = {
			case_id : selections[0].get("case_id"),
			case_code : selections[0].get("case_code"),
			state:'6'
		};
		Ext.MessageBox.confirm(
						'提示',
						'确定此案例跳过报告交接？'+selections[0].get("case_code"),
						function(id) {
							if (id == 'yes') {
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
											url : "judicial/other/caseCodeRun.do",
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
												if (response.result) {
													Ext.MessageBox.alert("提示信息","流程操作成功！");
													me.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息",response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
											}
										});
							}
						});
	},
	onSkip7 : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
				.getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
			return;
		};
		if(selections[0].get("case_code")=='' || selections[0].get("case_code")==null){
			Ext.Msg.alert("提示", "此案例未生成案例编号，无法操作!");
			return;
		}
		if(selections[0].get("is_delete")!=0){
			Ext.Msg.alert("提示", "此案例已废除!");
			return;
		}
		var values = {
			case_id : selections[0].get("case_id"),
			case_code : selections[0].get("case_code"),
			state:'7'
		};
		Ext.MessageBox.confirm(
						'提示',
						'确定此案例跳过报告确认？'+selections[0].get("case_code"),
						function(id) {
							if (id == 'yes') {
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
											url : "judicial/other/caseCodeRun.do",
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
												if (response.result) {
													Ext.MessageBox.alert("提示信息","流程操作成功！");
													me.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息",response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
											}
										});
							}
						});
	},
	onCaseExport:function(){
		window.location.href = "judicial/other/exportCaseInfo.do?"+exportTemp;
	},
	onFindCase:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
		.getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		};
		var win = Ext.create("Ext.window.Window", {
			title : "案例关联信息",
			width : 500,
			iconCls : 'Find',
			height : 300,
			modal:true,
			layout : 'border',
			bodyStyle : "background-color:white;",
			items : [ Ext.create('Ext.grid.Panel', {
				renderTo : Ext.getBody(),
				width : 500,
				height : 300,
				frame : false,
				viewConfig : {
					forceFit : true,
					stripeRows : true// 在表格中显示斑马线
				},
				store : {// 配置数据源
					fields : [ 'case_code', 'case_code_second', 'case_state' ],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/register/getCaseSecond.do',
						params : {
							'case_code' : selections[0].get("case_code")
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
					header : "案例条形码",
					dataIndex : 'case_code',
					flex : 1.5,
					menuDisabled : true
				}, {
					header : "关联案例条形码",
					dataIndex : 'case_code_second',
					flex : 1.5,
					menuDisabled : true
				}, {
					header : "类型",
					dataIndex : 'case_state',
					flex : 1,
					menuDisabled : true,
					renderer : function(value,meta,record) {
						switch (value) {
						case '5':
							return "二次采样";
							break;
						case '6':
							return "补样";
							break;
						default:
							return "";
						}
					}
				} ]
			}) ]
		});
		win.show();
	},
	onVerifySample : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要审核的记录!");
			return;
		}
		if (selections[0].get("is_delete") == 1) {
			Ext.Msg.alert("提示", "此案例已被废除，无法审核!");
			return;
		}
		if (selections[0].get("verify_state") == 0) {
			Ext.Msg.alert("提示", "此案例登记未审核，无法审核样本!");
			return;
		}
		if (selections[0].get("verify_state") == 1) {
			Ext.Msg.alert("提示", "此案例未审核通过，无法审核样本!");
			return;
		}
		if (selections[0].get("verify_state") == 3) {
			Ext.Msg.alert("提示", "此案例样本审核通过，无法再次审核!");
			return;
		}
		var form = Ext.create("Rds.judicial.form.JudicialSampleVerifyForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '样本审核',
					width : 1000,
					iconCls : 'Pageedit',
					height : 600,
					layout : 'border',
					items : [form]
				});
		form.loadRecord(selections[0]);
		win.show();
	},
	onVerifyCheck : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 | selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要查看的记录!");
			return;
		}
		var form = Ext.create(
                "Rds.judicial.form.JudicialVerifyFormCheck", {
                    region: "center",
                    taskId: selections[0].get("task_id"),
                    processInstanceId: selections[0].get("process_instance_id"),
                    grid: me
                });
		form.loadRecord(selections[0]);
		var win = Ext.create("Ext.window.Window", {
					title : '案例审核',
					width : 1600,
					iconCls : 'Pageedit',
					height : 1000,
					maximizable : true,
					maximized : true,
					layout : 'border',
					items : [form]
				});
		win.show();
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
			title : "样本信息（案例条形码：" + selections[0].get("case_code") + "）",
			width : 600,
			iconCls : 'Find',
			height : 400,
			modal:true,
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
					fields : [ 'sample_id', 'sample_code', 'sample_type','sample_typename',
							'sample_call', 'sample_callname', 'sample_username',
							'id_number', 'birth_date','special' ],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/register/getSampleInfo.do',
						params : {
							'case_id' : selections[0].get("case_id")
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
					header : "样本条形码",
					dataIndex : 'sample_code',
					flex : 1.5,
					menuDisabled : true
				}, {
					header : "称谓",
					dataIndex : 'sample_callname',
					flex : 1,
					menuDisabled : true
				}, {
					header : "姓名",
					dataIndex : 'sample_username',
					flex : 1,
					menuDisabled : true
				}, {
					header : "身份证号",
					dataIndex : 'id_number',
					flex : 2,
					menuDisabled : true
				}, {
					header : "出生日期",
					dataIndex : 'birth_date',
					flex : 1,
					menuDisabled : true
				}, {
					header : "样本类型",
					dataIndex : 'sample_typename',
					flex : 1,
					menuDisabled : true
				},{
					text : '特殊样本',
					dataIndex : 'special',
					menuDisabled : true,
					width : 80,
					renderer : function(value,meta,record) {
						switch (value) {
						case '0':
							return "否";
							break;
						case '1':
							return "是";
							break;
						default:
							return "";
						}
					}
				} ]
			}) ]
		});
		win.show();
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage = 1;
		me.getStore().load();
	},
    onTaskHistory: function () {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		}
		var values = {
			processInstanceId: selections[0].get("process_instance_id")
		};
		if (values.processInstanceId == null || values.processInstanceId == "") {
			Ext.Msg.alert("提示", "该记录不能进行此项操作!");
			return;
		}
		Ext.Ajax.request({
			url: "activiti/main/taskDetail.do",
			method: "POST",
			headers: {'Content-Type': 'application/json'},
			jsonData: values,
			success: function (response, options) {
				response = Ext.JSON.decode(response.responseText);
//				console.log(response);
				var win = Ext.create("Ext.window.Window", {
					title: '流程步骤',
					width: 700,
					iconCls: 'Add',
					layout: 'fit',
					items: {
						xtype: 'grid',
						border: false,
						columns: [
							{
								text: '步骤ID',
								dataIndex: 'id',
								align: 'center',
								sortable: false,
								menuDisabled: true,
								hidden: true
							},
							{
								text: '步骤名称',
								dataIndex: 'name',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '活动类型',
								dataIndex: 'taskDefinitionKey',
								align: 'center',
								sortable: false,
								menuDisabled: true,
								hidden: true
							},
							{
								text: '办理人',
								dataIndex: 'assignee',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '开始时间',
								dataIndex: 'startTimeString',
								align: 'center',
								width: 120,
								sortable: false,
								menuDisabled: true
							},
							{
								text: '签收时间',
								dataIndex: 'claimTimeString',
								align: 'center',
								width: 120,
								sortable: false,
								hidden: true,
								menuDisabled: true
							},
							{
								text: '结束时间',
								dataIndex: 'endTimeString',
								align: 'center',
								width: 120,
								sortable: false,
								menuDisabled: true
							},
							{
								text: '活动耗时',
								dataIndex: 'durationInMillisString',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '原因',
								dataIndex: 'comment',
								align: 'center',
								flex: 1,
								sortable: false,
								menuDisabled: true,
								 renderer : function(value, cellmeta, record,
											rowIndex, columnIndex, store) {
//										var str = value;
//										if (value.length > 15) {
//											str = value.substring(0, 15) + "...";
//										}
									 if(null == value) return "";
									 else 
										return "<span title='" + value + "'>" + value
												+ "</span>";
									}
							}
						],
						store: Ext.create("Ext.data.Store", {
							fields: ['id', 'name', 'taskDefinitionKey', 'assignee', 'claimTime', 'startTime', 'endTime', 'durationInMillis','comment',
								{
									name: 'claimTimeString', type: 'date',
									convert: function (v, rec) {
										return rec.data.claimTime == null ? "" : Ext.Date.format(new Date(rec.data.claimTime), 'Y-m-d H:i');
									}
								},
								{
									name: 'startTimeString', type: 'date',
									convert: function (v, rec) {
										return rec.data.startTime == null ? "" : Ext.Date.format(new Date(rec.data.startTime), 'Y-m-d H:i');
									}
								},
								{
									name: 'endTimeString',
									convert: function (v, rec) {
										return rec.data.endTime == null ? "" : Ext.Date.format(new Date(rec.data.endTime), 'Y-m-d H:i');
									}
								}, {
									name: 'durationInMillisString',
									convert: function (v, rec) {
										var mills = rec.data.durationInMillis;
										var result = "";
										if (mills == null) {
											result = "";
										}
										else if (mills < 1000) {
											result = "小于1秒";
										}
										else {
											var days = parseInt(mills / (1000 * 60 * 60 * 24));
											mills = mills - days * (1000 * 60 * 60 * 24);
											var hours = parseInt(mills / (1000 * 60 * 60));
											mills = mills - hours * (1000 * 60 * 60);
											var min = parseInt(mills / (1000 * 60));
											mills = mills - min * (1000 * 60);
											var second = parseInt(mills / (1000));

											result += days == 0 ? "" : (days + "天");
											result += hours == 0 ? "" : (hours + "小时");
											result += min == 0 ? "" : (min + "分钟");
											result += second + "秒";
										}
										return result;
									}
								}
							],
//							sorters: [{
////								property: 'id',
////								direction: 'DESC'
//							}],
							data: response
						})
					}
				});
				win.show();
				// response = Ext.JSON.decode(response.responseText);
				// if (response.result == true) {
				//     Ext.MessageBox.alert("提示信息", response.message);
				//     me.getStore().load();
				//     // me.up("window").close();
				// } else {
				//     Ext.MessageBox.alert("错误信息", response.message);
				//     me.getStore().load();
				// }
			},
			failure: function () {
				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
			}
		});
	},
	listeners : {
		'afterrender' : function() {
			this.store.load();
		},
        'cellclick': function (grid, td, cellIndex, record, tr, rowIndex, e) {
            //查看审核不通过原因
            if (e.getTarget('.lbtnComment')) {
                Ext.create("Ext.window.Window", {
                    title: '审核不通过原因',
                    width: 500,
                    height: 250,
                    modal: true,
                    layout: 'fit',
                    items: [{
                        xtype: "gridpanel",
                        columns: [
                            {
                                header: "审核时间",
                                dataIndex: "timeString",
                                align: 'center',
                                width: 150,
                                sortable: false,
                                menuDisabled: true
                            },
                            {
                                header: "审核人",
                                dataIndex: "userId",
                                align: 'center',
                                width: 100,
                                sortable: false,
                                menuDisabled: true
                            },
                            {
                                header: "审核意见",
                                dataIndex: "fullMessage",
                                align: 'center',
                                flex: 1,
                                sortable: false,
                                menuDisabled: true
                            }
                        ],
                        store: Ext.create('Ext.data.Store', {
                            fields: ['fullMessage', 'userId', 'time', {
                                name: 'timeString', type: 'date',
                                convert: function (v, rec) {
                                    return rec.data.time == null ? "" : Ext.Date.format(new Date(rec.data.time), 'Y-m-d H:i');
                                }
                            }],
                            autoLoad: true,
                            proxy: {
                                type: 'jsonajax',
                                actionMethods: {
                                    read: 'POST'
                                },
                                url: 'activiti/main/getProcessInstanceComments.do',
                                params: {"processInstanceId": record.data.process_instance_id},
                                reader: {
                                    type: 'json'
                                }
                            }
                        })
                    }]
                }).show();
            }
        }
	}
});
