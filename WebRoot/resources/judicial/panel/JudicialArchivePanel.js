/**
 * @author fushaoming
 * @date 20150424
 * @description 案例归档panel
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
										'sample_typename', 'sample_callname',
										'sample_username', 'id_number',
										'birth_date'],// 定义字段
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
										dataIndex : 'sample_typename',
										flex : 1,
										menuDisabled : true
									}]
						})]
			});
	win.show();
}

Ext.define("Rds.judicial.panel.JudicialArchivePanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		 var verify_state = Ext.create('Ext.form.ComboBox', 
					{
						fieldLabel : '审核状态',
						width : '18%',
						labelWidth : 60,
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
											['报告确认中',6],
											['邮寄中',8],
											['归档中',9],
											['已归档',10]
											]
								}),
						value : '',
						mode : 'local',
						name : 'verify_state',
					});
		me.store = Ext.create('Ext.data.Store', {
			fields : ['case_id', 'case_code', 'case_areacode','client',
					'receiver_area', 'case_receiver', 'urgent_state',
					  'verify_state', 'accept_time', 'consignment_time','close_time',
					'report_modelname', 'report_model', 'address',
					'case_in_per', "receiver_id", 'case_in_pername', 'phone',
					'case_id', 'is_delete'],
			start:0,
			limit:15,
			pageSize:15,
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/archive/getCaseInfo.do',
				params : {
				},
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'total'
				}
			},
			listeners : {
				'beforeload' : function(ds, operation, opt) {
					Ext.apply(me.store.proxy.extraParams, {
								starttime : dateformat(Ext
										.getCmp('starttime_archive').getValue()),
								receiver : trim(Ext.getCmp('receiver_archive')
										.getValue()),
								endtime : dateformat(Ext
										.getCmp('endtime_archive').getValue()),
								case_code : trim(Ext
										.getCmp('case_code_archive').getValue()),
								verify_state:verify_state.getValue(),
								is_delete : 0
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
//			mode: 'SINGLE'
		});
		me.columns = [{
					text : '案例编号',
					dataIndex : 'case_id',
					flex : 1,
					hidden : true,
					menuDisabled : true
				},  {
					text : '案例条形码',
					dataIndex : 'case_code',
					menuDisabled : true,
					flex : 1,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						var isnull = record.data["is_delete"];
						if (isnull == 1) {
							return "<div style=\"text-decoration: line-through;color: red;\">"
									+ value + "</div>"
						} else {
							return value;
						}

					}
				},  {
					text : '案例归属地',
					dataIndex : 'receiver_area',
					menuDisabled : true,
					flex : 1.2
				}, {
					text : '归属人',
					dataIndex : 'case_receiver',
					menuDisabled : true,
					flex : 0.8
				},  {
					text : '委托人',
					dataIndex : 'client',
					menuDisabled : true,
					flex : 0.8
				},	{
					text : '案例审核状态',
					dataIndex : 'verify_state',
					menuDisabled : true,
					width : 200,
					renderer : function(value,meta,record) {
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
							return "待审核";
							break;
						case 2:
							 return "审核不通过";
							break;
						case 3:
							return "审核通过";
							break;
						case 4:
							return "样本交接中";
							break;
						case 5:
							return "实验中";
							break;
						case 6:
							return "报告打印中";
							break;
						case 7:
							return "报告确认中";
							break;
						case 8:
							return "邮寄中";
							break;
						case 9:
							return "归档中";
							break;
						case 10:
							return "已归档";
							break;
						default:
							return "";
						}
					}
				}, {
					text : '受理日期',
					dataIndex : 'accept_time',
					menuDisabled : true,
					flex : 1
				}, {
					text : '委托日期',
					dataIndex : 'consignment_time',
					menuDisabled : true,
					flex : 1
				},{
					text : '登记人',
					dataIndex : 'case_in_pername',
					menuDisabled : true,
					flex : 0.8
				}, {
					text : '样本信息',
					dataIndex : 'case_id',
					menuDisabled : true,
					flex : 1.5,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						var case_code = record.data["case_code"];
						return "<a href='#' onclick=\"getSampleInfo('" + value
								+ "','" + case_code + "')\"  >查看样本</a>"
					}
				}];

		me.dockedItems = [{
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [{
						xtype : 'textfield',
						name : 'case_code',
						id : 'case_code_archive',
						labelWidth : 80,
						width : '18%',
						regex : /^\w*$/,
						regexText : '请输入英文或数字',
						fieldLabel : '案例条形码'
					}, {
						xtype : 'textfield',
						fieldLabel : '归属人',
						width : '18%',
						labelWidth : 50,
						id : 'receiver_archive',
						name : 'receiver'
					}, verify_state, {
						xtype : 'datefield',
						name : 'starttime',
						id : 'starttime_archive',
						width : '18%',
						fieldLabel : '受理日期 从',
						labelWidth : 70,
						labelAlign : 'right',
						emptyText : '请选择日期',
						format : 'Y-m-d',
						value : Ext.Date.add(new Date(), Ext.Date.DAY, -7),
						maxValue : new Date(),
						listeners : {
							'select' : function() {
								var start = Ext.getCmp('starttime_archive')
										.getValue();
								Ext.getCmp('endtime_archive')
										.setMinValue(start);
								var endDate = Ext.getCmp('endtime_archive')
										.getValue();
								if (start > endDate) {
									Ext.getCmp('endtime_archive')
											.setValue(start);
								}
							}
						}
					}, {
						xtype : 'datefield',
						id : 'endtime_archive',
						name : 'endtime',
						width : '16%',
						labelWidth : 20,
						fieldLabel : '到',
						labelAlign : 'right',
						emptyText : '请选择日期',
						format : 'Y-m-d',
						maxValue : new Date(),
						value : Ext.Date.add(new Date(), Ext.Date.DAY),
						listeners : {
							select : function() {
								var start = Ext.getCmp('starttime_archive')
										.getValue();
								var endDate = Ext.getCmp('endtime_archive')
										.getValue();
								if (start > endDate) {
									Ext.getCmp('starttime_archive')
											.setValue(endDate);
								}
							}
						}
					}, {
						text : '查询',
						iconCls : 'Find',
						handler : me.onSearch
					}]
		}, {
			xtype : 'toolbar',
			dock : 'top',
			items : [{
						text : '归档',
						iconCls : 'Bookgo',
						handler : me.onArchive
					}]
		}];

		me.callParent(arguments);
	},
	onArchive : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要归档的记录!");
			return;
		}
		if(selections[0].get("verify_state")!=9)
		{
			Ext.MessageBox.alert("错误信息", "该案例状态不允许归档！");
			return;
		}
		
		var form = Ext.create(
				"Rds.judicial.form.JudicialArchiveForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '案例归档',
					width : 400,
					iconCls : 'Pageedit',
					height : 300,
					layout : 'border',
					modal : true,
					items : [form]
				});
		form.loadRecord(selections[0]);
		win.show();
		
//		Ext.Ajax.request({
//					url : "judicial/archive/queryMailCount.do",
//					method : "POST",
//					headers : {
//						'Content-Type' : 'application/json'
//					},
//					jsonData : {
//						'case_id' : selections[0].get("case_id")
//					},
//					success : function(response, options) {
//						response = Ext.JSON.decode(response.responseText);
//						if (response.success == true) {
//
//							
//
//						} else {
//							Ext.MessageBox.alert("错误信息", response.message);
//						}
//					},
//					failure : function() {
//						Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
//					}
//
//				});

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
