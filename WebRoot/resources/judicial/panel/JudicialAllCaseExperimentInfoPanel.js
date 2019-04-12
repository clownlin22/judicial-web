var exportParams;
Ext.define("Rds.judicial.panel.JudicialAllCaseExperimentInfoPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var case_code = Ext.create('Ext.form.field.Text', {
			name : 'casecode',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '案例编号'
		});
		var parnter_name = Ext.create('Ext.form.field.Text', {
			name : 'parnter_name',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '合作方'
		});
		var accept_time_start=Ext.create('Ext.form.DateField', {
			name : 'accept_time_start',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '受理时间 从',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var accept_time_end=Ext.create('Ext.form.DateField', {
			name : 'accept_time_end',
			width : '20%',
			labelWidth : 20,
			fieldLabel : '到',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		me.store = Ext.create('Ext.data.Store', {
			fields : ['case_code', 'accept_time','sampleConfirm','experiment','reportConfirm',
					'result', "reagent_name_ext", 'parnter_name'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/allcaseinfo/queryExperimentInfo.do',
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
								accept_time_start : dateformat(accept_time_start.getValue()),
								accept_time_end : dateformat(accept_time_end.getValue()),
								case_code : trim(case_code.getValue()),
								parnter_name:trim(parnter_name.getValue())
							});
					exportParams = trim(case_code.getValue())
						+ "&accept_time_start=" + dateformat(accept_time_start.getValue())
						+ "&accept_time_end=" + dateformat(accept_time_end.getValue())
						+"&parnter_name="+trim(parnter_name.getValue());
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
				},{
					text : '受理时间',
					dataIndex : 'accept_time',
					menuDisabled : true,
					width : 150
				}, {
					text : '样本确认时间',
					dataIndex : 'sampleConfirm',
					menuDisabled : true,
					width : 150
				}, {
					text : '实验时间',
					dataIndex : 'experiment',
					menuDisabled : true,
					width : 200
				}, {
					text : '报告确认时间',
					dataIndex : 'reportConfirm',
					menuDisabled : true,
					width : 300
				}, {
					text : '是否存在加位点',
					dataIndex : 'reagent_name_ext',
					menuDisabled : true,
					width : 150
				}, {
					text : '是否存在否定案例',
					dataIndex : 'result',
					menuDisabled : true,
					width : 150
				},{
					text : '合作方',
					dataIndex : 'parnter_name',
					menuDisabled : true,
					width : 150
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
					items : [case_code, accept_time_start, accept_time_end,parnter_name,{
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
						borderBottomWidth : '0px !important'
					},
					defaults : {
						labelAlign : 'right'
					},
					items : [ {
						text : '导出样本信息',
						iconCls : 'Pageexcel',
						handler : me.onSampleExport
					}]
				}
			];

		me.callParent(arguments);
	},
	onSampleExport : function() {
		var me = this.up("gridpanel");
		window.location.href = "judicial/allcaseinfo/exportExperimentInfo.do?case_code="+exportParams;
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
