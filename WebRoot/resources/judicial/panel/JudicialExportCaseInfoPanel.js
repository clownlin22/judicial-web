var exportParams;
Ext.define("Rds.judicial.panel.JudicialExportCaseInfoPanel", {
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
		var case_name = Ext.create('Ext.form.field.Text', {
			name : 'case_name',
			labelWidth : 70,
			width : '20%',
			emptyText : '请输入归属人、委托人或样本姓名',
			fieldLabel : '姓名'
		});
		var serial_number = Ext.create('Ext.form.field.Text', {
			name : 'serial_number',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '物鉴字号'
		});
		var account = Ext.create('Ext.form.field.Text', {
			name : 'account',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '账户类型'
		});
		var sample_in_per = Ext.create('Ext.form.field.Text', {
			name : 'sample_in_per',
			labelWidth : 60,
			width : '20%',
			emptyText : '请输入采样员姓名',
			fieldLabel : '采样人员'
		});
		var receiver_area = Ext.create('Ext.form.field.Text', {
			name : 'receiver_area',
			labelWidth : 60,
			width : '20%',
			emptyText : '请输入案例归属地',
			fieldLabel : '归属地区'
		}); 
		var mail_code = Ext.create('Ext.form.field.Text', {
			name : 'mail_code',
			labelWidth : 60,
			width : '20%',
			emptyText : '请输入案例回寄快递信息',
			fieldLabel : '快递信息'
		}); 
		var fee_type = new Ext.form.field.ComboBox({
			fieldLabel : '案例类型',
			width : '20%',
			labelWidth : 70,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'right',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name', 'Code'],
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
			mode : 'local',
			typeAhead : false,
			value : '',
			name : 'fee_type'
		})
		var fee_status = new Ext.form.field.ComboBox({
			fieldLabel : '财务状态',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name', 'Code'],
						data : [['全部','2'],['未确认', 0], ['已确认', 1]]
					}),
			mode : 'local',
			typeAhead : false,
			value : '2',
			name : 'fee_status'
		})
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
		var confirm_date_start=Ext.create('Ext.form.DateField', {
			name : 'confirm_date_start',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '确认时间 从',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date()
		});
		var confirm_date_end=Ext.create('Ext.form.DateField', {
			name : 'confirm_date_end',
			width : '20%',
			labelWidth : 20,
			fieldLabel : '到',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d'
		});
		var update_date_start=Ext.create('Ext.form.DateField', {
			name : 'update_date_start',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '登记时间 从',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var update_date_end=Ext.create('Ext.form.DateField', {
			name : 'update_date_end',
			width : '20%',
			labelWidth : 20,
			fieldLabel : '到',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var report_model = new Ext.form.field.ComboBox({
			fieldLabel : '模版类型',
			width : '20%',
			labelWidth : 70,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'right',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name', 'Code'],
						data : [['全部', ''],['新安鉴定','xajdmodel'],['鸣证鉴定','mzjdmodel'],['金证鉴定','jzjdmodel'],['青岛万方','qdwfjdmodel'],['云南乾盛','ynqsjdmodel'],['商丘法医学会','fyxhjdmodel'],['临时模板','temporary'],['永建医学','ydjdmodelyx'],['永建鉴定','ydjdmodel'],['鑫证鉴定','xzjdmodel'],['毕节黔','bjqjdmodel'],['安徽至正','ahzzjdmodel'],['曹县—泰安','cxjymodel'],['龙图鉴定', 'ltjdmodel'],['求实鉴定','qsjdmodel'],
								['苏博医学','sbyxmodel'],['商丘鉴定','sqjdmodel'],['十堰鉴定','syjdmodel'],['正泰鉴定','ztjdmodel'],
								['子渊鉴定（直扩）','zyjdmodelzk'],['子渊鉴定（提取）','zyjdmodeltq'],['子渊检验','zyjymodel'],['枣庄鉴定','zzjdmodel']]
					}),
			mode : 'local',
			typeAhead : false,
			value : '',
			name : 'report_model'
		})
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
											['实体渠道','0' ],
											['电子渠道','1']]
								}),
						value : '',
						mode : 'local',
						name : 'source_type',
					});
		var parnter_name = Ext.create('Ext.form.field.Text', {
			name : 'parnter_name',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '合作商',
			value:('subo_zcnt'==usercode)?"广东南天司法鉴定中心":'',
			readOnly:('subo_zcnt'==usercode)?true:false
		});
		me.store = Ext.create('Ext.data.Store', {
			fields : ['case_id', 'case_area', 'case_code', 'case_areacode','fandm','child','serial_number',
					'receiver_area', 'case_receiver', 'accept_time','client','case_in_per',
					'close_time','real_sum','parnter_name','sample_in_per','update_date','account',
					'mail_info', 'sample_in_time','agent','remark','financeRemark','sample_count'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/allcaseinfo/queryExportCaseInfo.do',
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
								case_name : trim(case_name.getValue()),
								update_date_end : dateformat(update_date_end.getValue()),
								update_date_start : dateformat(update_date_start.getValue()),
								endtime : dateformat(endtime.getValue()),
								case_code : trim(case_code.getValue()),
								fee_type : fee_type.getValue(),
								fee_status : fee_status.getValue(),
								mail_code : trim(mail_code.getValue()),
								serial_number:trim(serial_number.getValue()),
								receiver_area : trim(receiver_area.getValue()),
								usercode:usercode,
								report_model:report_model.getValue(),
								source_type:source_type.getValue(),
								parnter_name:trim(parnter_name.getValue()),
								sample_in_per:trim(sample_in_per.getValue()),
								confirm_date_start:dateformat(confirm_date_start.getValue()),
								confirm_date_end:dateformat(confirm_date_end.getValue()),
								account:account.getValue().trim()
							});
					exportParams = trim(case_code.getValue())
						+ "&case_name="
						+ trim(case_name.getValue())
						+ "&starttime=" + dateformat(starttime.getValue())
						+ "&endtime=" + dateformat(endtime.getValue())
						+"&fee_type=" + fee_type.getValue()
						+ "&fee_status=" + fee_status.getValue()
						+ "&mail_code=" + trim(mail_code.getValue())
						+ "&receiver_area=" + trim(receiver_area.getValue())
						+ "&is_delete=0"+"&serial_number="+trim(serial_number.getValue())
						+"&usercode="+usercode
						+"&report_model="+report_model.getValue()
						+"&parnter_name="+trim(parnter_name.getValue())
						+"&source_type="+source_type.getValue()
						+"&sample_in_per="+trim(sample_in_per.getValue())
						+"&update_date_start="+dateformat(update_date_start.getValue())
						+"&update_date_end="+dateformat(update_date_end.getValue())
						+"&confirm_date_start="+dateformat(confirm_date_start.getValue())
						+"&confirm_date_end="+dateformat(confirm_date_end.getValue())
						+"&account="+account.getValue().trim();
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
					text : '物鉴字号',
					dataIndex : 'serial_number',
					menuDisabled : true,
					width : 190
				},{
					text : '案例归属地',
					dataIndex : 'receiver_area',
					menuDisabled : true,
					width : 200
				}, {
					text : '归属人',
					dataIndex : 'case_receiver',
					menuDisabled : true,
					width : 160,
					renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
										var agent= record.data["agent"];
										if (agent != ''&& agent!=null) {
											return value+"(被代理人："+agent+")";
										} else {
											return value;
										}
									}
				}, {
					text : '委托人',
					dataIndex : 'client',
					menuDisabled : true,
					width : 100
				}, {
					text : '采样员',
					dataIndex : 'sample_in_per',
					menuDisabled : true,
					width : 100
				},{
					text : '受理日期',
					dataIndex : 'accept_time',
					menuDisabled : true,
					width : 100
				}, {
					text : '登记日期',
					dataIndex : 'update_date',
					menuDisabled : true,
					width : 100
				}, {
					text : '样本登记日期',
					dataIndex : 'sample_in_time',
					menuDisabled : true,
					width : 150
				}, {
					text : '父母亲',
					dataIndex : 'fandm',
					menuDisabled : true,
					width : 150
				}, {
					text : '孩子',
					dataIndex : 'child',
					menuDisabled : true,
					width : 150
				},{
					text : '快递信息',
					dataIndex : 'mail_info',
					menuDisabled : true,
					width : 400
				}, {
					text : '案例应收',
					dataIndex : 'real_sum',
					menuDisabled : true,
					width : 100
				}, {
					text : '账户类型',
					dataIndex : 'account',
					menuDisabled : true,
					width : 100
				},{
					text : '合作方',
					dataIndex : 'parnter_name',
					menuDisabled : true,
					width : 150
				},{
					text : '案例登记人',
					dataIndex : 'case_in_per',
					menuDisabled : true,
					width : 150
				},{
					text : '案例备注',
					dataIndex : 'remark',
					menuDisabled : true,
					width : 400
				},{
					text : '财务备注',
					dataIndex : 'financeRemark',
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
					items : [case_code, case_name, receiver_area,mail_code,fee_type]
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
					items : [fee_status, starttime, endtime, report_model,parnter_name]
				}, {
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					style : {
						borderTopWidth : '0px !important',
						borderBottomWidth : '0px !important'
					},
					items : [sample_in_per,update_date_start,update_date_end,serial_number,account]
				},{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					style : {
						borderTopWidth : '0px !important',
						borderBottomWidth : '1px !important'
					},
					items : [confirm_date_start,confirm_date_end,{
						text : '查询',
						iconCls : 'Find',
						handler : me.onSearch
					}, {
						text : '案例导出',
						iconCls : 'Pageexcel',
						handler : me.onSampleExport
					}]
				}
			];

		me.callParent(arguments);
	},
	onSampleExport : function() {
		var me = this.up("gridpanel");
		window.location.href = "judicial/allcaseinfo/exportCaseInfo.do?case_code="+exportParams;
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
