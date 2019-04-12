
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

Ext.define("Rds.judicial.panel.JudicialVerifyGridPanel", {
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
					value : 1,
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
								case_type:'0',
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
			items : [{
						text : '审核案例信息',
						iconCls : 'Applicationformmagnify',
						handler : me.onVerifyRegister
					},{
	                    text: '打印条形码',
	                    iconCls: 'Printer',
	                    handler: me.onPrintBarcode,
	                    hidden:true
	                },{
						text: '实验室退回',
						iconCls: 'Arrowleft',
						handler: me.onExpBack
					},{
						text: '补报告',
						iconCls: 'Add',
						handler: me.onFillReport
					},{
						text: '改报告',
						iconCls: 'Pageedit',
						handler: me.onChangeReport
					},{
						text : '修改样本条形码',
						iconCls : 'Pageedit',
						handler : me.onChangeSample
					},{
						text : '添加备注',
						iconCls : 'Pageedit',
						handler : me.onRemark
					},{
						text : '修改委托时间/受理时间',
						iconCls : 'Pageedit',
						handler : me.onConsignmentTime,
						hidden : (usercode == 'admin' ) ? false : true
					},{
						text : '废除',
						iconCls : 'Delete',
						handler : me.onDelete,
					}
	                ]
			},{
				style : {
			        borderTopWidth : '0px !important',
			        borderBottomWidth : '0px !important'
			 	},
				xtype : 'toolbar',
				dock : 'top',
				items : [ {
					text : '查看案例信息',
					iconCls : 'Camera',
					handler : me.onVerifyCheck
				},  {
					text : '查看审核历史',
					iconCls : 'Camera',
					handler : me.onVerifyHistory,
					hidden:true
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
					text : '导出案例信息',
					iconCls : 'Pageexcel',
					handler : me.onCaseExport
				},{
					text : '案例条码设置',
					iconCls : 'Cog',
					handler : me.onCaseCodeConfig
				},{
					text: '样本交接',
					iconCls: 'Add',
					handler: me.onSubmitSample
				},{
					text: '跳跃至实验',
					iconCls: 'Add',
					handler: me.onSubmitVerifyExperiment
				}
			]
			}];

		me.callParent(arguments);
	},
	onDelete : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
				.getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
			return;
		};
//		if(selections[0].get("verify_state")==3){
//			Ext.Msg.alert("提示", "此案例已审核，无法作废!");
//			return;
//		}
		if(selections[0].get("is_delete")!=0){
			Ext.Msg.alert("提示", "此案例已废除!");
			return;
		}
		var values = {
			case_id : selections[0].get("case_id")
		};
		Ext.MessageBox
				.confirm(
						'提示',
						'确定删除此案例吗',
						function(id) {
							if (id == 'yes') {
								Ext.MessageBox.wait('正在操作','请稍后...');
								Ext.Ajax.request({
											url : "judicial/register/deleteCaseInfo.do",
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
													Ext.MessageBox.alert("提示信息","废除成功！");
													me.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息","只限池瑞操作！");
												}
											},
											failure : function() {
												Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
											}
										});
							}
						});
	},
	onSubmitVerifyExperiment: function () {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		}

		var case_codes='';
		var taskId='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if(selections[i].get("is_delete")==1){
				Ext.Msg.alert("提示", "存在已删除案例，无法提交!");
				return;
			}
			if(selections[i].get("parnter_name")!='河南商都司法鉴定中心'||selections[i].get("report_model")!='gxjdmodel'){
				Ext.Msg.alert("提示", "非河南合作方案例或广西公众案例不可操作!");
				return;
			}
			if(selections[i].get("verify_state")!=3){
				Ext.Msg.alert("提示", "案例非就绪状态!");
				return;
			}
		}
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if((i+1)==selections.length)
				{
					taskId +=  selections[i].get("task_id");
					case_codes += selections[i].get("case_code")
				}
			else
				{

					taskId += selections[i].get("task_id")+",";
					case_codes += selections[i].get("case_code")+",";
				}
		}
		var values = {
			taskId: taskId,
			case_codes:case_codes
		};
		if (values.taskId == null || values.taskId == "") {
			Ext.Msg.alert("提示", "该记录不能进行此项操作!");
			return;
		}
		
		Ext.MessageBox.confirm('提示','确定跳过样本交接样本确认直接实验吗？',
				function(id) {
					if (id == 'yes') {
						Ext.MessageBox.wait('正在操作','请稍后...');

						Ext.Ajax.request({
							url: "judicial/verify/onSubmitVerifyExperiment.do",
							method: "POST",
							async: false,
							headers: {'Content-Type': 'application/json'},
							jsonData: values,
								success: function (response, options) {
									response = Ext.JSON.decode(response.responseText);
									if (!response) {
										Ext.MessageBox.alert("错误信息", "操作出错，请联系管理员！");
										return;
									} else {
										Ext.MessageBox.alert("提示","操作成功！");
										me.getStore().load();
										}
								},
							failure: function () {
								Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
							}
						});
						Ext.Ajax.request({});
					}
				});
		
		
	},
	
	onSubmitSample: function () {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		}

		var case_ids='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if(selections[i].get("is_delete")==1){
				Ext.Msg.alert("提示", "存在已删除案例，无法提交!");
				return;
			}
			if(selections[i].get("verify_state")!=3){
				Ext.Msg.alert("提示", "案例非样本交接状态!");
				return;
			}
		}
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if((i+1)==selections.length)
				{
					
					case_ids += selections[i].get("case_id")
				}
			else
				{
					case_ids += selections[i].get("case_id")+",";
				}
		}
		var values = {

		};
		
		
		Ext.MessageBox.confirm('提示','确定交接此案例样本吗？',
				function(id) {
					if (id == 'yes') {
						Ext.MessageBox.wait('正在操作','请稍后...');

						Ext.Ajax.request({
							url: "judicial/sampleRelay/onSubmitSample.do",
							method: "POST",
							async: false,
							headers: {'Content-Type': 'application/json'},
							jsonData: {case_ids:case_ids},
							success: function (response, options) {  
								response = Ext.JSON.decode(response.responseText); 
				                 if (response.result==true) {  
				                 	Ext.MessageBox.alert("提示信息", response.message);
				                 	me.getStore().load();
				                 }else { 
				                 	Ext.MessageBox.alert("错误信息", response.message);
				                 } 
							},
							failure: function () {
								Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
							}
						});
						Ext.Ajax.request({});
					}
				});
		
		
	},
	onCaseCodeConfig:function(){
		uploadIdentify_canel = function(me) {
			me.up("window").close();
		};
		uploadIdentify_update = function(m) {
			var mei = this.up("form");
			var form = mei.getForm();
			var values = form.getValues();
			if (form.isValid()) {

				Ext.MessageBox.confirm('提示', '确认修改该案例编号设置吗?', function(id) {
					if (id == 'yes') {
						if(values.key=="case_code_num_sdf"){
							Ext.Ajax.request({
										url : "judicial/register/setXMLnum7.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : values,
										success : function(response, options) {
											response = Ext.JSON
													.decode(response.responseText);
											if (response) {
												Ext.MessageBox.alert("提示信息",
														"操作成功！");
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
						}else
						{
							Ext.Ajax.request({
								url : "judicial/register/setXMLnum2.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : values,
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response) {
										Ext.MessageBox.alert("提示信息",
												"操作成功！");
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
						
					}
				}, {
					xtype : 'panel',
					region : "center",
					border : false
				});
			
			
			}
		};
		
		var win = Ext.create("Ext.window.Window", {
			title : '编号类型选择',
			width : 450,
			iconCls : 'Pageadd',
			height : 250,
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
							name : 'key',
							fieldLabel : "编号类型",
							labelWidth : 60,
							editable : false,
							mode : 'remote',
							forceSelection : true,
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field is required!
							triggerAction : 'all',
							displayField : 'value',
							valueField : 'key',
							store : new Ext.data.ArrayStore(
									{fields : ['key','value' ],
										data : [
												['case_code_num_d','商都业务员D' ],
												['case_code_num_sd','商都案例SD' ],
												['case_code_num_sdf','商都法院SDF'],
												]
									})
						}),{
							xtype : 'tbtext',
							style:'color:red',
							text : '注意：编号数为月份后数量，例：SD201711032，编号数填32即可修改<br>当前编号为32，案例会从SD201711033开始<br>'
						}, Ext.create('Ext.form.field.Number', {
							name : 'value',
							style:'margin-top:10px;',
							labelWidth : 60,
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field is required!
							width : '20%',
							fieldLabel : '编号数'
						})]
			}]
		});
		win.show();
	},
	onCaseExport:function(){
		window.location.href = "judicial/allcaseinfo/exportPartnerAllCaseInfo.do";
	},
	onConsignmentTime:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1 ){
			Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
			return;
		};
		if(selections[0].get("receiver_area").indexOf("江苏省"))
		{
			Ext.Msg.alert("提示", "请选择一条省内需要修改的记录!");
			return;
		}
		console.log(selections[0].get("receiver_area"));
		var case_id=selections[0].get("case_id");

		consignmentTime_update = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["case_id"]=case_id;
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "judicial/verify/updateCaseConsignment.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {						
								response = Ext.JSON.decode(response.responseText);
								if (response) {
									Ext.MessageBox.alert("提示信息", response.message);
									var grid = me.up("gridpanel");
									me.getStore().load();
									consignmentTime_add.close();
								} else {
									Ext.MessageBox.alert("错误信息", response.message);
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
			}
		}
		
		var consignmentTime_add = Ext.create("Ext.window.Window", {
			title : '修改信息',
			width : 350,
			height : 250,
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
					labelAlign : 'right'
				},
				border : false,
				autoHeight : true,
				buttons : [{
							text : '保存',
							iconCls : 'Disk',
							handler : consignmentTime_update
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : case_canel
						}],
				items : [{

					xtype : 'datefield',
					name : 'accept_time',
					columnWidth : .50,
					labelWidth : 80,
					fieldLabel : "受理时间<span style='color:red'>*</span>",
					labelAlign : 'right',
					format : 'Y-m-d',
					allowBlank : false,// 不允许为空
					value:selections[0].get("accept_time")
				},{
							xtype : 'datefield',
							name : 'consignment_time',
							columnWidth : .50,
							labelWidth : 80,
							fieldLabel : "委托时间<span style='color:red'>*</span>",
							labelAlign : 'right',
							format : 'Y-m-d',
							allowBlank : false,// 不允许为空
							value:selections[0].get("consignment_time")
				}
				]
			}]
		})
		consignmentTime_add.show();

	},
	onRemark:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1 ){
			Ext.Msg.alert("提示", "请选择一条需要备注的记录!");
			return;
		};
		var case_id=selections[0].get("case_id");

		remark_confirm = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["case_id"]=case_id;
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "judicial/verify/updateCaseRemark.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {						
								response = Ext.JSON.decode(response.responseText);
								if (response) {
									Ext.MessageBox.alert("提示信息", response.message);
									var grid = me.up("gridpanel");
									me.getStore().load();
									remark_add.close();
								} else {
									Ext.MessageBox.alert("错误信息", response.message);
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
			}
		}
		
		var remark_add = Ext.create("Ext.window.Window", {
			title : '备注信息',
			width : 350,
			height : 250,
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
					labelAlign : 'right'
				},
				border : false,
				autoHeight : true,
				buttons : [{
							text : '保存',
							iconCls : 'Disk',
							handler : remark_confirm
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : case_canel
						}],
				items : [{
					xtype : "textarea",
					fieldLabel : '备注',
					labelAlign : 'right',
					maxLength : 200,
					labelWidth : 80,
					height:130,
					allowBlank : false,
//					regex:/^[^\s]*$/,
//					regexText:'我不喜欢空格',
					name : 'remark',
					value:selections[0].get("confirm_remark")
				},{
					xtype : "hidden",
					name : 'remark_bak',
					value:selections[0].get("remark")
				}]
			}]
		})
		remark_add.show();

	},
	onChangeSample:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel()
				.getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		};
		if(5< selections[0].get("verify_state"))
		{
			Ext.Msg.alert("提示", "该状态不允许修改!");
			return;
		}
//		if("5" != selections[0].get("case_state") && "6" != selections[0].get("case_state"))
//		{
//			Ext.Msg.alert("提示", "该状态不允许修改!");
//			return;
//		}
		var form = Ext.create(
                "Rds.judicial.form.JudicialSampleUpdateForm", {
                    region: "center",
                    autoScroll : true,
                    grid: me
                });
		form.loadRecord(selections[0]);
			var win = Ext.create("Ext.window.Window", {
						title : selections[0].get("case_code")+'案例样本修改',
						width : 1000,
						iconCls : 'Pageedit',
						height : 500,
						modal:true,
						layout : 'border',
						items : [form]
					});
			win.show();
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
	onFillReport:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要补报告的记录!");
			return;
		}
		if(selections[0].get("verify_state")<6)
		{
			Ext.Msg.alert("提示", "该案例状态不允许补报告!");
			return;
		}
		Ext.MessageBox.confirm('提示','补报告案例，确认收费吗？',
						function(id) {
							if (id == 'yes') {

								var values = {
										case_id : selections[0].get("case_id"),
										type:selections[0].get("case_state"),
										finance_type:selections[0].get("case_type")=="0"?"亲子鉴定-司法":"亲子鉴定-医学",
										case_type:'dna_add',
										finance_remark:"补报告案例",
										stand_sum:50.0,
										real_sum:50.0,
										return_sum:0,
										discountPrice:0
									};
								Ext.Ajax.request({
											url : "judicial/register/fillReport.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : values,
											success : function(response,options) {
												response = Ext.JSON.decode(response.responseText);
												if (response.result == true) {
													Ext.MessageBox.alert("提示信息",response.message);
													me.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息",response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
											}
										});
							}else if (id == 'no') 
							{
								var values = {
										case_id : selections[0].get("case_id"),
										type:selections[0].get("case_state"),
										finance_type:selections[0].get("case_type")=="0"?"亲子鉴定-司法":"亲子鉴定-医学",
										case_type:'dna_add',
										finance_remark:"补报告案例,免费",
										stand_sum:0.0,
										real_sum:0.0,
										return_sum:0,
										discountPrice:0
									};

								Ext.Ajax.request({
											url : "judicial/register/fillReport.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : values,
											success : function(response,options) {
												response = Ext.JSON.decode(response.responseText);
												if (response.result == true) {
													Ext.MessageBox.alert("提示信息",response.message);
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
	onChangeReport:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要改报告的记录!");
			return;
		}
		if(selections[0].get("verify_state")<6)
		{
			Ext.Msg.alert("提示", "该案例状态不允许改报告!");
			return;
		}
		Ext.MessageBox.confirm('提示','改报告案例收费吗？',
						function(id) {
							if (id == 'yes') {
								var values = {
										case_id : selections[0].get("case_id"),
										type:selections[0].get("case_state"),
										finance_type:selections[0].get("case_type")=="0"?"亲子鉴定-司法":"亲子鉴定-医学",
										finance_remark:"改报告案例",
										case_type:'dna_change',
										stand_sum:300.0,
										real_sum:300.0,
										return_sum:0,
										discountPrice:0
									};
								Ext.Ajax.request({
											url : "judicial/register/changeReport.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : values,
											success : function(response,options) {
												response = Ext.JSON.decode(response.responseText);
												if (response.result == true) {
													Ext.MessageBox.alert("提示信息",response.message);
													me.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息",response.message);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
											}
										});
							}else if(id=='no')
							{var values = {
									case_id : selections[0].get("case_id"),
									type:selections[0].get("case_state"),
									finance_type:selections[0].get("case_type")=="0"?"亲子鉴定-司法":"亲子鉴定-医学",
									finance_remark:"改报告案例，免费",
									case_type:'dna_change',
									stand_sum:0.0,
									real_sum:0.0,
									return_sum:0,
									discountPrice:0
								};
							Ext.Ajax.request({
										url : "judicial/register/changeReport.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : values,
										success : function(response,options) {
											response = Ext.JSON.decode(response.responseText);
											if (response.result == true) {
												Ext.MessageBox.alert("提示信息",response.message);
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
	onVerifyHistory : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		}
		var myGrid = Ext.create('Ext.grid.Panel', {
			region : 'center',
			store : Ext.create('Ext.data.Store', {
						fields : ['uuid', 'verify_baseinfo_time',
								'verify_baseinfo_state', 'username',
								'verify_baseinfo_remark'],
						proxy : {
							type : 'jsonajax',
							actionMethods : {
								read : 'post'
							},
							url : 'judicial/verify/queryVerifyHistory.do',
							params : {},
							reader : {
								type : 'json',
								root : 'data',
								totalProperty : 'total'
							}
						},
						listeners : {
							'beforeload' : function(ds, operation, opt) {
								Ext.apply(myGrid.store.proxy.params, {
											case_id : selections[0]
													.get('case_id')
										});
							}
						}
					}),
			columns : [{
						dataIndex : 'uuid',
						hidden : true
					}, {
						text : '审核时间',
						dataIndex : 'verify_baseinfo_time',
						width : '30%',
						menuDisabled : true

					}, {
						text : '审核状态',
						dataIndex : 'verify_baseinfo_state',
						width : '15%',
						menuDisabled : true,

						renderer : function(value) {
							switch (value) {
								case 0 :
									return "未审核";
									break;
								case 1 :
									return "<span style='color:green'>案例审核通过</span>";
									break;
								case 2 :
									return "<span style='color:red'>审核未通过</span>";
									break;
								case 3 :
									return "<span style='color:blue'>样本审核通过</span>";
								default :
									return "";
							}
						}
					}, {
						text : '审核人',
						dataIndex : 'username',
						width : '20%',
						menuDisabled : true
					}, {
						text : '审核理由',
						dataIndex : 'verify_baseinfo_remark',
						width : '40%',
						menuDisabled : true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var str = value;
							if (value.length > 10) {
								str = value.substring(0, 10) + "...（鼠标悬浮显示更多）";
							}
							return "<div title='" + value + "'>" + str
									+ "</div>";
						}
					}],
			buttons : [{
						text : '确定',
						iconCls : 'Accept',
						handler : function() {
							this.up("window").close();
						}
					}],
			listeners : {
				'afterrender' : {
					fn : function() {
						myGrid.store.load();
					}
				}
			}
		});
		var win = Ext.create("Ext.window.Window", {
					title : '审核历史',
					width : 700,
					iconCls : 'Add',
					height : 500,
					layout : 'border',
					modal : true,
					items : [myGrid]
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
	onVerifyRegister : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 | selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要审核的记录!");
			return;
		}
		if (selections[0].get("task_def_key") != "taskAudit") {
			Ext.Msg.alert("提示", "该状态不允许审核!");
			return;
		}
		if (selections[0].get("is_delete") == 1) {
			Ext.Msg.alert("提示", "此案例已被废除，无法审核!");
			return;
		}
		if (selections[0].get("verify_state") == 2) {
			Ext.Msg.alert("提示", "此案例已审核，无法再次审核!");
			return;
		}
		if (selections[0].get("verify_state") == 3 && selections[0].get("case_code") !="") {
			Ext.Msg.alert("提示", "此案例样本审核通过，无法再次审核!");
			return;
		}
		var form = Ext.create(
                "Rds.judicial.form.JudicialVerifyForm", {
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
    onPrintBarcode: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要打印条形码的记录!");
            return;
        }
//        if (!window.ActiveXObject && !("ActiveXObject" in window)) {
//            Ext.Msg.alert("提示", "该浏览器不支持打印,请使用IE!");
//            return;
//        }

        var caseCodes = "";
        for (var i = 0; i < selections.length; i++) {
            caseCodes += (caseCodes == "" ? selections[i].get("case_code") : ("," + selections[i].get("case_code")));
        }
        var src = "judicial/verify/getPrintBarcode.do?caseCodes=" + caseCodes;

        Ext.create("Ext.window.Window", {
            title: "打印预览",
            iconCls: 'Find',
            layout: "auto",
            maximized: true,
            maximizable: true,
            modal: true,
            bodyStyle: "background-color:white;",
            html: "<iframe width=100% height=100% id='dnamodel' src='"
            + src + "'></iframe>",
            buttons: [{
                text: '打印',
                iconCls: 'Printer',
                handler: function (me) {
                    var iframe = document
                        .getElementById("dnamodel");
                    iframe.contentWindow.focus();
                    iframe.contentWindow.print();
                    me.close();
                }
            }, {
                text: '取消',
                iconCls: 'Cancel',
                handler: function (me) {
                    me.close();
                }
            }]
        }).show();
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
	onExpBack: function () {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		}
		if (selections[0].get("is_delete") == 1) {
			Ext.Msg.alert("提示", "此案例已删除，无法退回!");
			return;
		}
		console.log(selections[0].get("task_def_key")!= "taskExperiment");
		if (selections[0].get("task_def_key") != "taskExperiment") {
			Ext.Msg.alert("提示", "该状态不允许退回!");
			return;
		}
		if (selections[0].get("verify_state") <= 3) {
			Ext.Msg.alert("提示", "此案例不允许退回!");
			return;
		}

		if (selections[0].get("task_id") == null || selections[0].get("task_id") == "") {
			Ext.Msg.alert("提示", "该记录不能进行此项操作!");
			return;
		}

		var form = Ext.create("Rds.judicial.form.JudicialExpBackForm", {
			taskId: selections[0].get("task_id"),
			processInstanceId: selections[0].get("process_instance_id"),
			caseId: selections[0].get("case_id")
		});
		Ext.create("Ext.window.Window", {
			title: '实验室退回',
			width: 450,
			height: 250,
			modal: true,
			layout: 'fit',
			items: [form]
		}).show();
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
