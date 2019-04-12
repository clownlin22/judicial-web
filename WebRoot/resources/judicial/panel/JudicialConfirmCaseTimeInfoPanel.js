var exportConfirmParams;
Ext.define("Rds.judicial.panel.JudicialConfirmCaseTimeInfoPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {},
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
					report_model.select(this.getAt(0));
				}
			}
		});
		var case_code = Ext.create('Ext.form.field.Text', {
			name : 'casecode',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '案例编号'
		});
		var username = Ext.create('Ext.form.field.Text', {
			name : 'username',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '归属人'
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
		 var report_model = Ext.create('Ext.form.ComboBox', {
				fieldLabel : '模板类型',
				mode : 'local',
				labelWidth : 60,
				editable : false,
				valueField : "key",
				width : '20%',
				displayField : "value",
				name : 'report_model',
				store : storeModel,
			});
		var starttime=Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '受理时间 从',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-30),
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
		me.store = Ext.create('Ext.data.Store', {
			fields : ['case_code', 'username', 'registerUsername', 'registerPer','registerTime','verifyPer',
					'receiver_area', 'verifyTime', 'sampleRecivePer','sampleTime','sampleConfirmPer',
					'sampleConfirmTime', "reportCreateTime", 'reportMakeTime', 'reportCheckTime','emailPer',
					'emailTime','reportCircleTime','financeTime', 'remark','exception_desc'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/allcaseinfo/queryConfirmTimePage.do',
				params : {
					start : 0,
					limit : 25
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
								starttime : dateformat(starttime.getValue()),
								endtime : dateformat(endtime.getValue()),
								case_code : trim(case_code.getValue()),
								username : trim(username.getValue()),
								verify_state:verify_state.getValue(),
								report_model:report_model.getValue()
								
							});
					exportConfirmParams = trim(case_code.getValue())
						+ "&username="
						+ trim(username.getValue())
						+ "&starttime=" + dateformat(starttime.getValue())
						+ "&endtime=" + dateformat(endtime.getValue())
						+ "&case_code=" + trim(case_code.getValue())
						+ "&verify_state="+verify_state.getValue()
						+ "&report_model="+report_model.getValue();
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
					text : '案例条形码',
					dataIndex : 'case_code',
					menuDisabled : true,
					width : 120
				}, {
					text : '归属人',
					dataIndex : 'username',
					menuDisabled : true,
					width : 120
				}, {
					text : '历史登记人员',
					dataIndex : 'registerPer',
					menuDisabled : true,
					width : 100
				},{
					text : '当前登记人员',
					dataIndex : 'registerUsername',
					menuDisabled : true,
					width : 100
				}, {
					text : '登记时长',
					dataIndex : 'registerTime',
					menuDisabled : true,
					width : 150
				},{
					text : '审核人员',
					dataIndex : 'verifyPer',
					menuDisabled : true,
					width : 100
				}, {
					text : '审核时长',
					dataIndex : 'verifyTime',
					menuDisabled : true,
					width : 150
				}, {
					text : '样本交接人员',
					dataIndex : 'sampleRecivePer',
					menuDisabled : true,
					width : 100
				}, {
					text : '样本交接时长',
					dataIndex : 'sampleTime',
					menuDisabled : true,
					width : 150
				}, {
					text : '样本确认人员',
					dataIndex : 'sampleConfirmPer',
					menuDisabled : true,
					width : 100
				},{
					text : '样本确认时长',
					dataIndex : 'sampleConfirmTime',
					menuDisabled : true,
					width : 150
				},{
					text : '电子报告生成时长',
					dataIndex : 'reportCreateTime',
					menuDisabled : true,
					width : 150
				},{
					text : '报告书制作时长',
					dataIndex : 'reportMakeTime',
					menuDisabled : true,
					width : 150
				},{
					text : '报告书交接时长',
					dataIndex : 'reportCheckTime',
					menuDisabled : true,
					width : 150
				},{
					text : '邮寄人员',
					dataIndex : 'emailPer',
					menuDisabled : true,
					width : 100
				},{
					text : '邮寄时长',
					dataIndex : 'emailTime',
					menuDisabled : true,
					width : 150
				},{
					text : '报告周期',
					dataIndex : 'reportCircleTime',
					menuDisabled : true,
					width : 150
				},{
					text : '财务确认耗时',
					dataIndex : 'financeTime',
					menuDisabled : true,
					width : 150
				},{
					text : '异常信息',
					dataIndex : 'exception_desc',
					menuDisabled : true,
					width : 150
				},{
					text : '案例备注',
					dataIndex : 'remark',
					menuDisabled : true,
					width : 400
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					style : {
						borderTopWidth : '0px !important',
						borderBottomWidth : '0px !important'
					},
					defaults : {
						labelAlign : 'right'
					},
					items : [case_code, username, starttime,endtime,verify_state]
				},{

					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					style : {
						borderTopWidth : '0px !important',
						borderBottomWidth : '0px !important'
					},
					defaults : {
						labelAlign : 'right'
					},
					items : [report_model,{
						text : '查询',
						iconCls : 'Find',
						handler : me.onSearch
					}]
				},{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					style : {
						borderTopWidth : '0px !important',
						borderBottomWidth : '1px !important'
					},
					items : [{
						text : '导出报表信息',
						iconCls : 'Pageexcel',
						handler : me.onExport
					}]
				}
			];

		me.callParent(arguments);
	},
	onExport : function() {
		var me = this.up("gridpanel");
		window.location.href = "judicial/allcaseinfo/exportConfirmTimeInfo.do?case_code="+exportConfirmParams;
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
