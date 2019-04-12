/**
 * @author wind 无创医院归属地配置new
 */
Ext.define("Rds.bacera.panel.BaceraInvasiveHospitalAreaNameGrid", {
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
		var areacode='';
		me.store = Ext.create('Ext.data.Store', {
					fields : ['id', 'areacode', 'areaname', 'hospital', 'remark'],
		            start:0,
					limit:15,
					pageSize:15,
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'bacera/invasivePre/queryallpageHospitalAreaName.do',
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
										hospital:trim(hospital.getValue()),
									    areacode:''
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
			text : '所属医院',
			dataIndex : 'hospital',
			width : 200,
			menuDisabled : true
		}, {
					text : '归属地',
					dataIndex : 'areaname',
					width : 200,
					menuDisabled : true
				},{
					text : '备注',
					dataIndex : 'remark',
					width : 200,
					menuDisabled : true
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [hospital ,search,{
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
		var form = Ext.create("Rds.bacera.form.BaceraHospitalAreaNameForm", {
					region : "center",
					autoScroll : true,
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '归属地医院配置——新增',
					width : 400,
					iconCls : 'Add',
					height : 350,
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
		var form = Ext.create("Rds.bacera.form.BaceraHospitalAreaNameForm", {
					region : "center",
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '归属地医院配置——修改',
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
										url : "bacera/invasivePre/deleteHospitalAreaName.do",
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