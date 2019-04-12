/**
 * @author yxb 收费标准new
 */
Ext.define("Rds.finance.panel.FinanceChargeStandardPanel", {
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
		var username = Ext.create('Ext.form.field.Text', {
			name : 'username',
			labelWidth : 60,
			width : 200,
			fieldLabel : '人员'
		});
		var type = new Ext.form.field.ComboBox({
					fieldLabel : '项目类型',
					labelWidth :80,
					width : 200,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					labelAlign : 'left',
					valueField : 'Code',
					store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['全部', ''],['司法鉴定', '0'], ['医学鉴定', '1']]
							}),
					value : '',
					mode : 'local',
					name : 'type'
				});
		var source_type = new Ext.form.field.ComboBox({
			fieldLabel : '案例来源',
			labelWidth :80,
			width : 200,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name', 'Code'],
						data : [['全部', ''],['电子渠道', '1'], ['实体渠道', '0']]
					}),
			value : '',
			mode : 'local',
			name : 'source_type'
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
						data : [['全部', ''],['亲子鉴定', '0'], ['亲缘鉴定', '1'], ['同胞鉴定', '2']]
					}),
			value : '',
			mode : 'local',
			name : 'program_type'
		});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['id', 'areacode', 'areaname', 'equation', 'remark', 'type','source_type','program_type',
					          'userid','username','agentid','agentname','singlePrice','doublePrice','samplePrice',
					          'gapPrice','specialPirce','specialPirce1','specialPirce2','urgentPrice','urgentPrice1','urgentPrice2'],
		            start:0,
					limit:15,
					pageSize:15,
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'finance/chargeStandard/queryPage.do',
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
										type : type.getValue(),
										username:trim(username.getValue()),
										source_type:trim(source_type.getValue()),
										program_type:trim(program_type.getValue())
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
					text : '案例来源',
					dataIndex : 'source_type',
					width : 80,
					menuDisabled : true,
					renderer : function(value) {
						if (value == '1')
							return '电子渠道';
						else if(value == '0')
							return '实体渠道';
					}
				}, {
					text : '项目类型',
					dataIndex : 'program_type',
					width : 80,
					menuDisabled : true,
					renderer : function(value) {
						if (value == '0')
							return '亲子鉴定';
						else if(value == '1')
							return '亲缘鉴定';
						else if(value == '2')
							return '同胞鉴定';
					}
				},{
					text : '案例类型',
					dataIndex : 'type',
					width : 80,
					menuDisabled : true,
					renderer : function(value) {
						if (value == '0')
							return '司法鉴定';
						else if(value == '1')
							return '医学鉴定';
						else if(value == '2')
							return '法医临床';
						else if(value == '3')
							return '酒精检测';
						else if(value == '4')
							return '儿童基因库>';
						else if(value == '5')
							return '痕迹鉴定';
					}
				},{
					text : '归属人',
					dataIndex : 'username',
					width : 100,
					menuDisabled : true
				}, {
					text : '归属地',
					dataIndex : 'areaname',
					width : 200,
					menuDisabled : true
				},{
					text : '被代理人',
					dataIndex : 'agentname',
					width : 100,
					menuDisabled : true
				}, {
					text : '单亲价格',
					dataIndex : 'singlePrice',
					width : 100,
					menuDisabled : true
				},{
					text : '双亲价格',
					dataIndex : 'doublePrice',
					width : 100,
					menuDisabled : true
				},{
					text : '加样价格',
					dataIndex : 'samplePrice',
					width : 100,
					menuDisabled : true
				},{
					text : '差价价格',
					dataIndex : 'gapPrice',
					width : 100,
					menuDisabled : true
				},{
					text : '特殊1样本价格',
					dataIndex : 'specialPirce',
					width : 120,
					menuDisabled : true
				},{
					text : '特殊2样本价格',
					dataIndex : 'specialPirce1',
					width : 120,
					menuDisabled : true
				},{
					text : '特殊3样本价格',
					dataIndex : 'specialPirce2',
					width : 120,
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
					items : [source_type,program_type,,type,username,search, {
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
							},{
								text : '批量修改',
								iconCls : 'Pageedit',
								handler : me.onUpdates
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
		personTemp  = '';
		codeTemp  = '';
		var form = Ext.create("Rds.finance.form.FinanceChargeStandardForm", {
					region : "center",
					autoScroll : true,
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '价格标准——新增',
					width : 480,
					iconCls : 'Add',
					height : 500,
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
		personTemp  = selections[0].get("userid");
		codeTemp  = selections[0].get("areacode");
		var form = Ext.create("Rds.finance.form.FinanceChargeStandardForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '价格标准——修改',
					width : 480,
					iconCls : 'Add',
					height : 500,
					modal:true,
					layout:'border',
					items : [form]
				});
		win.show();
		form.loadRecord(selections[0]);
	},
	onUpdates:function(){
		var mi = this.up("gridpanel");
		var selections = mi.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		}
		var ids = '';
		for(var i = 0 ; i < selections.length ; i ++ )
		{
			if(i == selections.length-1)
				ids += "'"+selections[i].get("id")+"'";
			else 
				ids += "'"+selections[i].get("id")+"',";
		}
		var win=Ext.create("Ext.window.Window", {
    		width : 480,
    		iconCls :'Pageadd',
    		height : 200,
    		modal:true,
    		title:'修改公式',
    		layout : 'border',
    		bodyStyle : "background-color:white;",
    		items : [{
    			xtype:"form",
    			region : 'center',
    			bodyPadding : 10,
    			defaults : {
    				anchor : '100%',
    				labelWidth : 80,
    				labelSeparator: "：",
    				labelAlign: 'right'
    			},
    			buttons:[
    					{
    							text : '保存',
    							iconCls : 'Disk',
    							handler:function(me){
    								var form  = me.up("form").getForm();
    								var values = form.getValues();
    								values["id"]=ids;
    								if(form.isValid()){
    									Ext.MessageBox.wait('正在操作','请稍后...');
    									Ext.Ajax.request({  
    										url:"finance/chargeStandard/updateStandards.do", 
    										method: "POST",
    										headers: { 'Content-Type': 'application/json' },
    										jsonData: values, 
    										success: function (response, options) {  
    											response = Ext.JSON.decode(response.responseText); 
    							                 if (response.result==true) {  
    							                 	Ext.MessageBox.alert("提示信息", response.message);
    							                 	mi.getStore().load();
    							                 	win.close();
    							                 }else { 
    							                 	Ext.MessageBox.alert("错误信息", response.message);
    							                 } 
    										},  
    										failure: function () {
    											Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
    										}
    							      	});
    								}
    							}
    					}, {
    							text : '取消',
    							iconCls : 'Cancel',
    							handler:function(){
    								win.close();
    							}
    					} 
    			],
    			border : false,
    			autoHeight : true,
    			items:[{
					xtype : 'fieldset',
					title : '价格标准',
					layout : 'anchor',
					defaults : {
						anchor : '100%'
					},
					items : [{
						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [ {
							xtype : 'numberfield',
							fieldLabel : '单亲价格',
							labelWidth : 60,
							labelAlign : 'left',
							columnWidth : .33,
							allowBlank : false,
							name : 'singlePrice',
							maxLength : 1000
						},{
							xtype : 'numberfield',
							fieldLabel : '双亲价格',
							labelWidth : 60,
							labelAlign : 'right',
							columnWidth : .33,
							allowBlank : false,
							name : 'doublePrice',
							maxLength : 1000
						},{
							xtype:'numberfield',
							fieldLabel : '加样价格',
							labelWidth : 60,
							labelAlign : 'right',
							columnWidth : .33,
							allowBlank : false,
							name:'samplePrice'
						}]
					},{
						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [ {
							xtype : 'numberfield',
							fieldLabel : '差价',
							labelWidth : 60,
							columnWidth : .33,
							value:'0',
							allowBlank : false,
							labelAlign : 'left',
							name : 'gapPrice',
							maxLength : 1000
						},{
							xtype : 'numberfield',
							fieldLabel : '特殊1样本价格',
							labelWidth : 88,
							value:'300',
							labelAlign : 'right',
							columnWidth : .50,
							allowBlank : false,
							name : 'specialPirce',
							maxLength : 1000
						}]
					},{

						layout : "column",// 从左往右的布局
						xtype : 'fieldcontainer',
						border : false,
						items : [ {
							xtype : 'numberfield',
							fieldLabel : '特殊2样本价格',
							labelWidth : 88,
							value:'300',
							labelAlign : 'right',
							columnWidth : .50,
							allowBlank : false,
							name : 'specialPirce1',
							maxLength : 1000
						},{
							xtype : 'numberfield',
							fieldLabel : '特殊3样本价格',
							labelWidth : 88,
							value:'300',
							labelAlign : 'right',
							columnWidth : .50,
							allowBlank : false,
							name : 'specialPirce2',
							maxLength : 1000
						}]
					
					},{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ {
									xtype : 'numberfield',
									fieldLabel : '48小时加急',
									labelWidth : 60,
									columnWidth : .33,
									value:'300',
									allowBlank : false,
									labelAlign : 'left',
									name : 'urgentPrice',
									maxLength : 1000
								},{
									xtype : 'numberfield',
									fieldLabel : '24小时加急',
									labelWidth : 60,
									columnWidth : .33,
									value:'300',
									allowBlank : false,
									labelAlign : 'left',
									name : 'urgentPrice',
									maxLength : 1000
								},{
									xtype : 'numberfield',
									fieldLabel : '8小时加急',
									labelWidth : 60,
									columnWidth : .33,
									value:'300',
									allowBlank : false,
									labelAlign : 'left',
									name : 'urgentPrice',
									maxLength : 1000
								}]
							}
					]
				}
    			]
    		}]
    	});
    	win.show();
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
										url : "finance/chargeStandard/delete.do",
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