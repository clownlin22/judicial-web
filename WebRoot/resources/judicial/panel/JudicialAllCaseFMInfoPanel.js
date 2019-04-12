var exportParams;
Ext.define("Rds.judicial.panel.JudicialAllCaseFMInfoPanel", {
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
			regex : /^\w*$/,
			emptyText : '请输入案例编号',
			fieldLabel : '案例编号'
		});
		var fm = Ext.create('Ext.form.field.Text', {
			name : 'fm',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '父母亲'
		});
		var child = Ext.create('Ext.form.field.Text', {
			name : 'child',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '孩子'
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
		var consignment_time_start=Ext.create('Ext.form.DateField', {
			name : 'consignment_time_start',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '委托时间 从',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var consignment_time_end=Ext.create('Ext.form.DateField', {
			name : 'consignment_time_end',
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
			fields : ['case_id', 'case_code', 'accept_time','consignment_time','fm','fmid',
					'fmbirth', "child", 'childid', 'childbirth'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/allcaseinfo/queryFMChild.do',
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
								consignment_time_start : dateformat(consignment_time_start.getValue()),
								consignment_time_end : dateformat(consignment_time_end.getValue()),
								case_code : trim(case_code.getValue()),
								fm:trim(fm.getValue()),
								child:trim(child.getValue())
							});
					exportParams = trim(case_code.getValue())
						+ "&accept_time_start=" + dateformat(accept_time_start.getValue())
						+ "&accept_time_end=" + dateformat(accept_time_end.getValue())
						+"&consignment_time_start="+dateformat(consignment_time_start.getValue())
						+"&consignment_time_end="+dateformat(consignment_time_end.getValue())
						+"&fm="+trim(fm.getValue())
						+"&child="+trim(child.getValue());
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
					width : 120,
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
				},{
					text : '受理时间',
					dataIndex : 'accept_time',
					menuDisabled : true,
					width : 150
				}, {
					text : '委托时间',
					dataIndex : 'consignment_time',
					menuDisabled : true,
					width : 150
				}, {
					text : '父母亲信息',
					dataIndex : 'fm',
					menuDisabled : true,
					width : 200
				}, {
					text : '父母亲身份证',
					dataIndex : 'fmid',
					menuDisabled : true,
					width : 300
				}, {
					text : '父母亲出生日期',
					dataIndex : 'fmbirth',
					menuDisabled : true,
					width : 400
				}, {
					text : '孩子信息',
					dataIndex : 'child',
					menuDisabled : true,
					width : 100
				},{
					text : '孩子身份证',
					dataIndex : 'childid',
					menuDisabled : true,
					width : 150
				},{
					text : '孩子出生日期',
					dataIndex : 'childbirth',
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
					items : [case_code, accept_time_start, accept_time_end,consignment_time_start,consignment_time_end]
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
					items : [fm, child,{
						text : '查询',
						iconCls : 'Find',
						handler : me.onSearch
					}, {
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
		window.location.href = "judicial/allcaseinfo/exportFMChild.do?case_code="+exportParams;
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
