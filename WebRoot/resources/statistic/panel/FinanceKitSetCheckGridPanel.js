Ext.define("Rds.statistic.panel.FinanceKitSetCheckGridPanel",{
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
		var deptname = Ext.create('Ext.data.Store',{
		    fields:['deptname'],
		    autoLoad:true,
		    proxy:{
		    	type: 'jsonajax',
		        actionMethods:{read:'POST'},
		        params:{},
		        url:'statistics/kitSet/queryDeptList.do',
		        reader:{
		          type:'json',
		        },
		        writer: {
		            type: 'json'
		       }
		      },
		      remoteSort:true
		});
		var user_dept_level1 = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			labelWidth : 60,
			width : '30%',
			fieldLabel : '申请部门',
		    name:'user_dept_level1',
		    triggerAction: 'all',
		    queryMode: 'local', 
		    emptyText : "请选择",
		    selectOnTab: true,
		    store: deptname,
		    fieldStyle: me.fieldStyle,
		    displayField:'deptname',
		    valueField:'deptname',
		    listClass: 'x-combo-list-small',
		})
		var apply_per = Ext.create('Ext.form.field.Text', {
			name : 'apply_per',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '申请人'
		});
		var apply_num = Ext.create('Ext.form.field.Text', {
			name : 'apply_num',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '流程编号'
		});
		var kit_receive_per = Ext.create('Ext.form.field.Text', {
			name : 'kit_receive_per',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '收件人'
		});
		var kit_name = Ext.create('Ext.form.field.Text', {
			name : 'kit_name',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '试剂名称'
		});
		var kit_bathc_num = Ext.create('Ext.form.field.Text', {
			name : 'kit_bathc_num',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '批次号'
		});
		var kit_express_num = Ext.create('Ext.form.field.Text', {
			name : 'kit_express_num',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '快递单号'
		});
		var create_pername = Ext.create('Ext.form.field.Text', {
			name : 'create_pername',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '创建人'
		});
		var confirm_pername = Ext.create('Ext.form.field.Text', {
			name : 'confirm_pername',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '确认人'
		});
		var confirm_state=Ext.create('Ext.form.ComboBox', {
			fieldLabel : '是否确认',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name','Code' ],
						data : [['全部','' ],
								['是','2' ],
								['否','1' ]]
					}),
			value : '',
			mode : 'local',
			name : 'confirm_state',
		});
		var is_delete=Ext.create('Ext.form.ComboBox', {
			fieldLabel : '是否删除',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name','Code' ],
						data : [['全部','' ],
								['是','2' ],
								['否','1' ]]
					}),
			value : '',
			mode : 'local',
			name : 'is_delete',
		});
		var create_date_start = new Ext.form.DateField({
			name : 'create_date_start',
			width:'20%',
			fieldLabel : '创建日期从',
			labelWidth : 70,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-31)
		});
		var create_date_end = new Ext.form.DateField({
			name : 'create_date_end',
			width:'20%',
			style:'margin-left:10px;',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		me.store = Ext.create('Ext.data.Store',{
							fields : ['kit_id','kit_count','kit_name','kit_spec',"kit_bathc_num",'user_dept_level1','kit_dest','kit_express_num',
							          'kit_receive_per','apply_per','apply_num','apply_date',"create_per",'create_pername',
							          'create_date','remark','confirm_state','confirm_per','confirm_pername','confirm_time','is_delete'],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'statistics/kitSet/queryAllPage.do',
								params : {
									start : 0,
									limit : 25
								},
								reader : {
									type: 'json',
				                    root:'data',
				                    totalProperty:'total'
								}
							},
							listeners : {
								'beforeload' : function(ds,
										operation, opt) {
									exportKit = "?kit_name=" + trim(kit_name.getValue())
										+ "&create_date_start=" + dateformat(create_date_start.getValue())
										+ "&create_date_end=" + dateformat(create_date_end.getValue())
										+ "&kit_bathc_num=" + kit_bathc_num.getValue().trim()
										+ "&kit_express_num=" + kit_express_num.getValue().trim()
										+ "&kit_receive_per=" + trim(kit_receive_per.getValue())
										+ "&apply_per=" + trim(apply_per.getValue())
										+"&apply_num="+apply_num.getValue().trim()
										+"&user_dept_level1="+(user_dept_level1.getValue()==null?"":user_dept_level1.getValue())
										+"&create_pername="+create_pername.getValue().trim()
										+"&confirm_pername="+confirm_pername.getValue().trim()
										+"&confirm_state="+confirm_state.getValue()
										+"&is_delete="+is_delete.getValue()
									Ext.apply(
											me.store.proxy.extraParams,{								
												kit_name:kit_name.getValue().trim(),
												kit_bathc_num:kit_bathc_num.getValue().trim(),
												kit_express_num:kit_express_num.getValue().trim(),
												kit_receive_per:kit_receive_per.getValue().trim(),
												apply_per:apply_per.getValue().trim(),
												apply_num:apply_num.getValue().trim(),
												user_dept_level1:user_dept_level1.getValue(),
												create_pername:create_pername.getValue().trim(),
												confirm_pername:confirm_pername.getValue().trim(),
												confirm_state:confirm_state.getValue(),
												is_delete:is_delete.getValue(),
												create_date_start:dateformat(create_date_start.getValue()),
												create_date_end:dateformat(create_date_end.getValue()),
											});
								}
							}
						});
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
		});
		//分页的combobox下拉选择显示条数
	    combo = Ext.create('Ext.form.ComboBox',{
	          name: 'pagesize',
	          hiddenName: 'pagesize',
	          store: new Ext.data.ArrayStore({
	              fields: ['text', 'value'],
	              data: [['15', 15], ['30', 30],['60', 60], ['80', 80], ['100', 100]]
	          }),
	          valueField: 'value',
	          displayField: 'text',
	          emptyText:15,
	          width: 60
	     });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

       //添加下拉显示条数菜单选中事件
       combo.on("select", function (comboBox) {
       var pagingToolbar=Ext.getCmp('pagingbarKitCheck');
         	pagingToolbar.pageSize = parseInt(comboBox.getValue());
         	itemsPerPage = parseInt(comboBox.getValue());//更改全局变量itemsPerPage
         	me.store.pageSize = itemsPerPage;//设置store的pageSize，可以将工具栏与查询的数据同步。
         	me.store.load({  
         		params:{  
                 start:0,  
                 limit: itemsPerPage
         		}  
         	});//数据源重新加载
         	me.store.loadPage(1);//显示第一页
        });
       
		me.bbar = Ext.create('Ext.PagingToolbar', {
			id:'pagingbarKitCheck',
			store: me.store,
			pageSize:me.pageSize,
			displayInfo: true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录",
	   	 	items: ['-', '每页显示',combo,'条']
		});
		me.columns = [{
					text : '申请部门',
					dataIndex : 'user_dept_level1',
					menuDisabled : true,
					width : 200,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						var is_delete= record.data["is_delete"];
						var confirm_state= record.data["confirm_state"];
						if (is_delete == 2) {
							return "<div style=\"text-decoration: line-through;color: red;\">"
									+ value+ "</div>"
						} else if(confirm_state == 2) {
							return "<div style=\"color: green;\">"
							+ value + "</div>"
						}else
						{
							return value;
						}

					}
				},{
					text : '申请人',
					dataIndex : 'apply_per',
					menuDisabled : true,
					width : 150,
				},{
					text : '泛微申请流程编号',
					dataIndex : 'apply_num',
					menuDisabled : true,
					width : 150,
				},{
					text : '日期',
					dataIndex : 'apply_date',
					menuDisabled : true,
					width : 150,
				},{
					text : '试剂名称',
					dataIndex : 'kit_name',
					menuDisabled : true,
					width : 150,
				},{
					text : '规格(RXN)',
					dataIndex : 'kit_spec',
					menuDisabled : true,
					width : 200,
				},{
					text : '数量(盒)',
					dataIndex : 'kit_count',
					menuDisabled : true,
					width : 200
				},{
					text : '批次号',
					dataIndex : 'kit_bathc_num',
					menuDisabled : true,
					width : 150,
				},{
					text : '目的地',
					dataIndex : 'kit_dest',
					menuDisabled : true,
					width : 150,
				},{
					text : '顺丰单号',
					dataIndex : 'kit_express_num',
					menuDisabled : true,
					width : 150
				},{
					text : '收件人',
					dataIndex : 'kit_receive_per',
					menuDisabled : true,
					width : 150
				},{
					text : '创建人员',
					dataIndex : 'create_pername',
					menuDisabled : true,
					width : 150
				},{
					text : '创建时间',
					dataIndex : 'create_date',
					menuDisabled : true,
					width : 150,
				},{
					text : '创建人员',
					dataIndex : 'create_pername',
					menuDisabled : true,
					width : 150
				},{
					text : '创建时间',
					dataIndex : 'create_date',
					menuDisabled : true,
					width : 150,
				},{
					text : '确认人员',
					dataIndex : 'confirm_pername',
					menuDisabled : true,
					width : 150
				},{
					text : '确认时间',
					dataIndex : 'confirm_time',
					menuDisabled : true,
					width : 150,
				},{
					text : '备注',
					dataIndex : 'remark',
					menuDisabled : true,
					width : 150,
				}
			 ];

		me.dockedItems = [
				{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [kit_name,kit_bathc_num,kit_express_num,kit_receive_per,apply_per]
				},{
					style : {
				        borderTopWidth : '0px !important',
				        borderBottomWidth : '0px !important'
				 	},
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [apply_num,create_pername,confirm_pername,is_delete,confirm_state,{
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				},{
					style : {
				        borderTopWidth : '0px !important',
				        borderBottomWidth : '0px !important'
				 	},
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [user_dept_level1,create_date_start,create_date_end,{
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				}];
		me.callParent(arguments);
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