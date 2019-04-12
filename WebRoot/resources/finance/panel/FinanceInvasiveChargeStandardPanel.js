/**
 * @author yxb 无创收费标准new
 */
Ext.define("Rds.finance.panel.FinanceInvasiveChargeStandardPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var search = Ext.create('Ext.form.field.Text', {
					name : 'areaname',
					labelWidth : 60,
					width : 200,
					fieldLabel : '所属地区'
				});
		
		var hospital = Ext.create('Ext.form.field.Text', {
			name : 'hospital',
			labelWidth : 60,
			width : 200,
			fieldLabel : '所属医院'
		});
		var program_type = new Ext.form.field.ComboBox({
			fieldLabel : '案例类型',
			labelWidth :80,
			width : 200,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name', 'Code'],
						data : [['全部', ''],['NIPT(博奥)', '1'],['NIPT(成都博奥)', '5'], ['NIPT(贝瑞)', '2'], ['NIPT-plus(博奥)', '3'], ['NIPT-plus(贝瑞)', '4'],['NIPT(精科)','6'],
                   	         ['NIPT-Plus(精科)','7']]
					}),
			value : '',
			mode : 'local',
			name : 'program_type'
		});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['id', 'areacode', 'areaname', 'equation', 'remark','program_type','samplePrice',
					          'urgentPrice','urgentPrice1','urgentPrice2','hospital'],
		            start:0,
					limit:15,
					pageSize:15,
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'finance/chargeStandard/queryInvasivePage.do',
						params : {},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'total'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							me.getSelectionModel().clearSelections();
							Ext.apply(me.store.proxy.params, {
										areaname : trim(search.getValue()),
										program_type:trim(program_type.getValue()),
										hospital:trim(hospital.getValue())
									});
						}
					}
				});

		me.selModel = Ext.create('Ext.selection.CheckboxModel', {
//					mode : 'SINGLE'
				});

		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : me.pageSize,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
					text : '项目类型',
					dataIndex : 'program_type',
					width : 80,
					menuDisabled : true,
					renderer : function(value) {
						if (value == '1')
							return 'NIPT(博奥)';
						else if(value == '2')
							return 'NIPT(贝瑞)';
						else if(value == '3')
							return 'NIPT-plus(博奥)';
						else if(value == '4')
							return 'NIPT-plus(贝瑞)';
						else if(value == '5')
							return 'NIPT(成都博奥)';
						else if(value == '6')
							return 'NIPT(精科)';
						else if(value == '7')
							return 'NIPT-Plus(精科)';
					}
				}, {
					text : '归属地',
					dataIndex : 'areaname',
					width : 200,
					menuDisabled : true
				},
				 {text : '所属医院',
					dataIndex : 'hospital',
					width : 200,
					menuDisabled : true
				},{
					text : '样本价格',
					dataIndex : 'samplePrice',
					width : 100,
					menuDisabled : true
				},{
					text : '48小时加急费用',
					dataIndex : 'urgentPrice',
					width : 120,
					menuDisabled : true
				},{
					text : '24小时加急费用',
					dataIndex : 'urgentPrice1',
					width : 120,
					menuDisabled : true
				},{
					text : '8小时加急费用',
					dataIndex : 'urgentPrice2',
					width : 120,
					menuDisabled : true
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [program_type,search,hospital, {
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				}, {
					xtype : 'toolbar',
					dock : 'top',
					items : [{
						        text : '新增',
								iconCls : 'Add',
								handler : me.onInsert
							},{
								text : '修改',
								iconCls : 'Pageedit',
								handler : me.onUpdate
							}, {
								text : '删除',
								iconCls : 'Delete',
								handler : me.onDelete
							}]
				}];
		me.store.load();
		me.callParent(arguments);
	},
	onInsert : function() {
		var me = this.up("gridpanel");
		codeTemp  = '';
		var form = Ext.create("Rds.finance.form.FinanceInvasiveChargeStandardForm", {
					region : "center",
					autoScroll : true,
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '价格标准——新增',
					width : 400,
					iconCls : 'Add',
					height :400,
					modal:true,
					layout : 'border',
					items : [form]
				});
		win.show();
	},
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
			return;
		}
		codeTemp  = selections[0].get("areacode");
		var form = Ext.create("Rds.finance.form.FinanceInvasiveChargeStandardForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '价格标准——修改',
					width : 400,
					iconCls : 'Add',
					height : 350,
					modal:true,
					layout:'border',
					items : [form]
				});
		win.show();
		form.loadRecord(selections[0]);
	},
	onDelete : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要删除的记录!");
			return;
		}
		var values = {
			id : selections[0].get("id")
		};
		Ext.Msg.show({
					title : '提示',
					msg : '确定删除吗？',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							Ext.Ajax.request({
										url : "finance/chargeStandard/deleteInvasiveStandard.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : values,
										success : function(response, options) {
											response = Ext.JSON
													.decode(response.responseText);
											if (response.result == true) {
												Ext.MessageBox.alert("提示信息",
														response.message);
												me.getStore().load();
											} else {
												Ext.MessageBox.alert("错误信息",
														response.message);
											}
										},
										failure : function() {
											Ext.Msg.alert("提示",
													"保存失败<br>请联系管理员!");
										}

									});
						} else {
							return;
						}

					},
					animateTarget : 'addAddressBtn',
					icon : Ext.window.MessageBox.INFO
				})
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	}
});