Ext.define("Rds.children.panel.ChildrenCaseExceptionGridPanel",{
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	region:'center',
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var case_code = Ext.create('Ext.form.field.Text', {
			name : 'case_code',
			labelWidth : 60,
			width : '20%',
			regexText : '请输入案例编号',
			fieldLabel : '案例编号'
		});
		var sample_code = Ext.create('Ext.form.field.Text', {
			name : 'sample_code',
			labelWidth : 60,
			width : '20%',
			regexText : '请输入样本条码',
			fieldLabel : '样本条码'
		});
		var child_name = Ext.create('Ext.form.field.Text', {
			name : 'child_name',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '儿童姓名'
		});
		var starttime=Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '采集时间 从',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date()
		});
		var endtime=Ext.create('Ext.form.DateField', {
			name : 'endtime',
			width : '20%',
			labelWidth : 20,
			fieldLabel : '到',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var case_areaname = Ext.create('Ext.form.field.Text', {
			name : 'case_areaname',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '归属地'
		});
		var storeModel = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/caseException/getExceptionTypes.do',
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true,
			listeners : {
					'load' : function() {
						var allmodel = new model({'key':'','value':'全部'});
				        this.insert(0,allmodel);
				        exception_type.select(this.getAt(0));
					}
			}
		});
		var exception_type=Ext.create('Ext.form.ComboBox', {
			fieldLabel : '自定义异常类型',
			mode: 'local',   
			labelWidth : 100,
			editable:false,
			valueField :"value",  
			width : '20%',
	        displayField: "value",    
			name : 'exception_type',
			store: storeModel,
		});
		var has_exception=Ext.create('Ext.form.ComboBox',{
			fieldLabel : '是否存在异常',
			width : '20%',
			labelWidth : 80,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'right',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name','Code' ],
						data : [['全部',-1 ],
								['是',1 ],
								['否',2 ] ]
					}),
			value : 1,
			mode : 'local',
			name : 'has_exception',
			readOnly:true
        });
		var is_handle=Ext.create('Ext.form.ComboBox',{
			fieldLabel : '是否处理',
			width : '20%',
			labelWidth : 80,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'right',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name','Code' ],
						data : [['全部','' ],
								['未处理','0' ],
								['已处理','1' ],
								['已删除','2' ] ]
					}),
			value : '0',
			mode : 'local',
			name : 'is_handle',
        });
		me.store = Ext.create('Ext.data.Store',{
				fields : [ 'exception_id','case_id', 'case_code',"sample_code","case_areaname","case_username",
						"child_name","id_number","gather_time","exception_type","exception_pername",'exception_time','handle_time',"handle_pername",
						"is_handle","child_sex","birth_date",'exception_desc'],
				start:0,
				limit:15,
				pageSize:15,
				proxy : {
					type : 'jsonajax',
					actionMethods : {
						read : 'POST'
					},
					url : 'children/exception/getCaseException.do',
					params : {
					},
					reader : {
						type : 'json',
						root : 'items',
						totalProperty : 'count'
					}
				},
				listeners : {
					'beforeload' : function(ds,operation, opt) {
						Ext.apply(
								me.store.proxy.extraParams,{								
									endtime : dateformat(endtime.getValue()),	
									starttime : dateformat(starttime.getValue()),
									child_name : trim(child_name.getValue()),
									case_code : trim(case_code.getValue()),
									sample_code:trim(sample_code.getValue()),
									case_areaname:trim(case_areaname.getValue()),
									exception_type:exception_type.getValue()=='全部'?'':exception_type.getValue(),
									has_exception:has_exception.getValue(),
									is_handle:is_handle.getValue()
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
		me.columns = [
				{
					text : '案例条形码',
					dataIndex : 'case_code',
					menuDisabled : true,
					width : 120,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						var is_handle = record.data["is_handle"];
						if (is_handle == 2) {
							return "<div style=\"text-decoration: line-through;color: red;\">"
									+ value + "</div>"
						} else {
							return value;
						}

					}
				},{
					header : "是否处理",
					dataIndex : 'is_handle',
					width : 120,
					menuDisabled : true,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						if (value == 0) {
							return "<div style=\"color:red\">未处理</div>";
						} else if(value==1){
							return "<div style=\"color:green\">已处理</div>";
						}else{
							return "<div style=\"color:red\">已删除</div>";
						}
					}
				},{
					text : '异常类型',
					dataIndex : 'exception_type',
					menuDisabled : true,
					width : 200,
				},{
					text : '异常描述',
					dataIndex : 'exception_desc',
					menuDisabled : true,
					width : 200,
					renderer : function(value, cellmeta, record,
							rowIndex, columnIndex, store) {
						var str = value;
						return "<span title='" + value + "'>" + str
								+ "</span>";
					}
				},{
					text : '异常添加人',
					dataIndex : 'exception_pername',
					menuDisabled : true,
					width : 150,
				},{
					text : '异常添加时间',
					dataIndex : 'exception_time',
					menuDisabled : true,
					width : 150,
				},{
					text : '异常处理人',
					dataIndex : 'handle_pername',
					menuDisabled : true,
					width : 150,
				},{
					text : '异常处理时间',
					dataIndex : 'handle_time',
					menuDisabled : true,
					width : 150,
				},{
					text : '样本条形码',
					dataIndex : 'sample_code',
					menuDisabled : true,
					width : 150,
				},
				{
					text : '案例归属地',
					dataIndex : 'case_areaname',
					menuDisabled : true,
					width : 200
				},
				{
					text : '归属人',
					dataIndex : 'case_username',
					menuDisabled : true,
					width : 120
				},{
					text : '采集时间',
					dataIndex : 'gather_time',
					menuDisabled : true,
					width : 100
				},{
					text : '儿童姓名',
					dataIndex : 'child_name',
					menuDisabled : true,
					width : 120
				}, {
					text : '出生日期',
					dataIndex : 'birth_date',
					menuDisabled : true,
					width : 120
				}, {
					text : '性别',
					dataIndex : 'child_sex',
					menuDisabled : true,
					width : 50,
					renderer : function(value) {
						switch (value) {
							case 0 :
								return "女";
								break;
							case 1 :
								return "男";
								break;
							default :
								return "";
						}
					}
				}, {
					text : '身份证号',
					dataIndex : 'id_number',
					menuDisabled : true,
					width : 150
				}
			 ];

		me.dockedItems = [
				{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [case_code,sample_code,starttime,endtime,child_name]
				},{
					xtype : 'toolbar',
					style : {
				        borderTopWidth : '0px !important',
				        borderBottomWidth : '1px !important'
				    },
					items : [case_areaname,exception_type,has_exception,is_handle, {
						text : '查询',
						iconCls : 'Find',
						handler : me.onSearch
					}  ]
				}, {
					xtype : 'toolbar',
					dock : 'top',
					items : [ {
						text : '异常处理',
						iconCls : 'Pageedit',
						handler : me.onHandle
					}, {
						text : '异常删除',
						iconCls : 'Delete',
						handler : me.onDelete
					}]
				} ];

		me.callParent(arguments);
	},
	onHandle:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要处理的记录!");
			return;
		}
		if (selections[0].get("is_handle") != 0) {
			Ext.Msg.alert("提示", "该异常无法处理!");
			return;
		}
		Ext.MessageBox.confirm('提示', '确定处理该异常吗？', function(id) {
			if (id == 'yes') {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "children/exception/updateException.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : {
								is_handle:1,
								exception_id:selections[0].get("exception_id")
								},
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response.success) {
									Ext.MessageBox.alert("提示信息",response.msg);
									me.getStore().load();
								} else {
									Ext.MessageBox.alert("错误信息",response.msg);
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
							}
						});
			}
		}, {
			xtype : 'panel',
			region : "center",
			border : false
		});
	},
	onDelete : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要删除的记录!");
			return;
		}
		if (selections[0].get("is_handle") == 2) {
			Ext.Msg.alert("提示", "该异常无法处理!");
			return;
		}
		Ext.MessageBox.confirm('提示', '确定删除该异常吗？', function(id) {
			if (id == 'yes') {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "children/exception/deleteException.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : {exception_id:selections[0].get("exception_id")},
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response.success) {
									Ext.MessageBox.alert("提示信息",response.msg);
									me.getStore().load();
								} else {
									Ext.MessageBox.alert("错误信息",response.msg);
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
							}
						});
			}
		}, {
			xtype : 'panel',
			region : "center",
			border : false
		});
	
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage=1;
		me.getStore().load();
	},
	listeners : {
		'afterrender' : function() {
			this.store.load();
		}
	}
});
