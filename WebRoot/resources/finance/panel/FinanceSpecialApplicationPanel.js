/**
 * @author yxb 财务编码申请和激活
 */
Ext.define("Rds.finance.panel.FinanceSpecialApplicationPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var monthly_pername = Ext.create('Ext.form.field.Text', {
					name : 'monthly_pername',
					labelWidth : 60,
					width : '20%',
					fieldLabel : '结算人'
				});
		var monthly_areaname = Ext.create('Ext.form.field.Text', {
			name : 'monthly_areaname',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '结算地区'
		});
		var confirm_code = Ext.create('Ext.form.field.Text', {
			name : 'confirm_code',
			labelWidth : 50,
			width : '20%',
			fieldLabel : '激活码'
		});
		var oa_code = Ext.create('Ext.form.field.Text', {
			name : 'oa_code',
			labelWidth : 50,
			width : '20%',
			fieldLabel : 'oa编码'
		});
		var apply_pername = Ext.create('Ext.form.field.Text', {
			name : 'apply_pername',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '申请人'
		});
		var case_state = new Ext.form.field.ComboBox({
					fieldLabel : '案例类型',
					labelWidth :60,
					width : '20%',
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					labelAlign : 'left',
					valueField : 'Code',
					store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['全部', ''],['优惠', '3'], ['先发报告后付款', '1'], ['月结', '4'], ['免单', '2'],['退款', '5']]
							}),
					value : '',
					mode : 'local',
					name : 'case_state'
				});
		var case_type = new Ext.form.field.ComboBox({
			fieldLabel : '项目类型',
			labelWidth :60,
			width : '20%',
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name', 'Code'],
						data : [['全部', ''],['亲子鉴定', 'dna'], ['NIPT', 'inversive'], ['儿童基因库', 'children']]
					}),
			value : '',
			mode : 'local',
			name : 'case_type'
		});
		var user_state = new Ext.form.field.ComboBox({
			fieldLabel : '使用状态',
			labelWidth :70,
			width : '20%',
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name', 'Code'],
						data : [['全部', ''],['未使用', '1'], ['已使用', '2'], ['已失效', '3']]
					}),
			value : '',
			mode : 'local',
			name : 'user_state'
		});
		var create_starttime=Ext.create('Ext.form.DateField', {
			name : 'create_starttime',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '创建时间 从',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var create_endtime=Ext.create('Ext.form.DateField', {
			name : 'create_endtime',
			width : '20%',
			labelWidth : 20,
			fieldLabel : '到',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY,1)
		});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['id', 'confirm_code', 'case_state', 'discount_amount','oa_code','estimate_date','case_type',
					          'apply_date','apply_per','apply_pername','monthly_per','monthly_area','confirm_per','confirm_date',
					          'monthly_pername','monthly_areaname','user_state','create_date','remark'],
					start:0,
					limit:15,
					pageSize:15,
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'finance/chargeStandard/queryfianceSpecialPage.do',
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
									confirm_code : trim(confirm_code.getValue()),
									oa_code : trim(oa_code.getValue()),
									case_state:case_state.getValue(),
									create_starttime:dateformat(create_starttime.getValue()),
									create_endtime:dateformat(create_endtime.getValue()),
									apply_pername:trim(apply_pername.getValue()),
									monthly_areaname:trim(monthly_areaname.getValue()),
									monthly_pername:trim(monthly_pername.getValue()),
									user_state:user_state.getValue(),
									case_type:case_type.getValue()
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
		me.columns = [ {
					text : '激活码',
					dataIndex : 'confirm_code',
					width : 150,
					menuDisabled : true
				}, {
					text : ' 项目类型',
					dataIndex : 'case_type',
					width : 120,
					menuDisabled : true,
					renderer : function(value) {
						if (value == 'dna')
							return '亲子鉴定';
						else if(value == 'inversive')
							return 'NIPT';
						else if(value == 'children')
							return '儿童基因库';
						else
							return '';
					}
				},{
					text : '案例类型',
					dataIndex : 'case_state',
					width : 120,
					menuDisabled : true,
					renderer : function(value) {
						if (value == '3')
							return '优惠申请';
						else if(value == '1')
							return '先发报告申请';
						else if(value == '4')
							return '月结申请';
						else if(value == '2')
							return '免单申请';
						else if(value == '5')
							return '退款';
						else
							return '';
					}
				},{
					text : '使用状态',
					dataIndex : 'user_state',
					width : 80,
					menuDisabled : true,
					renderer : function(value) {
						if (value == '1')
							return '未使用';
						else if(value == '2')
							return '<div style=\"color: blue;\">已使用</div>';
						else if(value == '3')
							return '<div style=\"color: red;\">已失效</div>';
						else 
							return '';
					}
				},{
					text : 'oa编码',
					dataIndex : 'oa_code',
					width : 80,
					menuDisabled : true,
				},{
					text : '金额',
					dataIndex : 'discount_amount',
					width : 80,
					menuDisabled : true,
				},{
					text : '申请人',
					dataIndex : 'apply_pername',
					width : 120,
					menuDisabled : true,
				},{
					text : '申请日期',
					dataIndex : 'apply_date',
					width : 120,
					menuDisabled : true,
				},{
					text : '结算地区',
					dataIndex : 'monthly_areaname',
					width : 150,
					menuDisabled : true,
				},{
					text : '结算人',
					dataIndex : 'monthly_pername',
					width : 80,
					menuDisabled : true,
				},{
					text : '预计结算日期',
					dataIndex : 'estimate_date',
					width : 100,
					menuDisabled : true,
				},{
					text : '创建时间',
					dataIndex : 'confirm_date',
					width : 150,
					menuDisabled : true,
				},{
					text : '备注',
					dataIndex : 'remark',
					width : 200,
					menuDisabled : true,
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [confirm_code,case_state,create_starttime,create_endtime,oa_code]
				},{
					style : {
			        borderTopWidth : '0px !important',
			        borderBottomWidth : '0px !important'
			 		},
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [monthly_pername,monthly_areaname,apply_pername,user_state]
				},{
					style : {
			        borderTopWidth : '0px !important',
			        borderBottomWidth : '0px !important'
			 		},
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [case_type, {
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				}, {
					xtype : 'toolbar',
					dock : 'top',
					items : [{
								text : '新增优惠码',
								iconCls : 'Pageedit',
								handler : me.onUpdate
							},{
						        text : '优惠码发送',
								iconCls : 'Send',
								handler : me.onSend,
								hidden:true
							},{
						        text : '我是优惠码',
								iconCls : 'Find',
								handler : me.onShowCode
							},{
						        text : '删除',
								iconCls : 'Delete',
								handler : me.onDelete
							}]
				}];
		me.store.load();
		me.callParent(arguments);
	},
	onDelete:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要删除的记录!");
			return;
		}
		if(selections[0].get("user_state") != '3')
		{
			Ext.Msg.alert("提示", "对不起，该优惠码未失效，无法删除!");
			return;
		}
		
		Ext.Msg.show({
			title : '提示',
			msg : '确定删除吗？',
			width : 300,
			buttons : Ext.Msg.OKCANCEL,
			fn : function(buttonId, text, opt) {
				if (buttonId == 'ok') {
					Ext.Ajax.request({
								url : "finance/chargeStandard/deleteConfirm.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {id:selections[0].get("id")},
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response) {
										Ext.MessageBox.alert("提示信息","删除成功！");
										me.getStore().load();
									} else {
										Ext.MessageBox.alert("错误信息","删除失败，请联系管理员!");
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
	onInsert : function() {
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.finance.form.FinanceSpecialForm", {
					region : "center",
					autoScroll : true,
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '优惠编码—新增',
					width : 260,
					iconCls : 'Add',
					height : 250,
					modal:true,
					layout : 'border',
					items : [form]
				});
		win.show();
	},	
	onShowCode:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择一条需要查看的记录!");
			return;
		}
		var confirm_code="";
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if((i+1)==selections.length)
				confirm_code +=  selections[i].get("confirm_code");
			else
				confirm_code += selections[i].get("confirm_code")+",";
		}
		Ext.Msg.alert("提示", confirm_code);
	},
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		var form = Ext.create("Rds.finance.form.FinanceSpecialActivateForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '优惠编码',
					width : 380,
					iconCls : 'Add',
					height : 450,
					modal:true,
					layout:'border',
					items : [form]
				});
		win.show();
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	}
});