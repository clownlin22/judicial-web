var i;
Ext.define("Rds.judicial.panel.JudicialAgentGridPanel",{
	extend:"Ext.grid.Panel",
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		i = 0;
		var me = this;
		var agentstore = Ext.create('Ext.data.Store',{
    	    fields:['key','value'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'judicial/agent/queryUserByType.do',
    	        params:{
    	        	roletype:'104'
    	        },
    	        render:{
    	            type:'json'
    	        },
    	        writer: {
    	            type: 'json'
    	       }
    	    }
    	});
		var marketstore = Ext.create('Ext.data.Store',{
    	    fields:['key','value'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'judicial/agent/queryUserByType.do',
    	        params:{
    	        	roletype:'103'
    	        },
    	        render:{
    	            type:'json'
    	        },
    	        writer: {
    	            type: 'json'
    	       }
    	    }
    	});
		var agentCombo = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			fieldLabel:'代理商',
	        name:'userid',
	        triggerAction: 'all',
	        labelWidth:60,
	        width:155,
	        queryMode: 'local', 
	        emptyText : "请选择",
	        selectOnTab: true,
	        store: agentstore,
	        fieldStyle: me.fieldStyle,
	        displayField:'value',
	        valueField:'key',
	        listClass: 'x-combo-list-small',
		});
		var marketCombo = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			labelWidth:60,
			width:155,
			fieldLabel:'市场人员',
	        name:'peruserid',
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
	        selectOnTab: true,
	        store: marketstore,
	        fieldStyle: me.fieldStyle,
	        displayField:'value',
	        valueField:'key',
	        listClass: 'x-combo-list-small',
		});
		var case_code = Ext.create('Ext.form.field.Text', {
				name : 'case_code',
				labelWidth : 70,
				width : 155,
				fieldLabel : '案例条形码'
		});
		var is_delete = new Ext.form.field.ComboBox({
			autoSelect : true,
			editable:true,
			labelWidth:60,
			width:155,
			fieldLabel:'是否废除',
	        name:'is_delete',
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
	        selectOnTab: true,
	        store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部','' ],['未废除','0' ],['已废除','1' ] ]
					}),	       
			fieldStyle: me.fieldStyle,
	        displayField:'Name',
	        valueField:'Code',
	        listClass: 'x-combo-list-small',
			value : ''
//					fieldLabel : '是否废除',
//					width : 155,
//					labelWidth : 60,
//					editable : false,
//					triggerAction : 'all',
//					displayField : 'Name',
//					labelAlign : 'right',
//					valueField : 'Code',
//					store : new Ext.data.ArrayStore(
//							{
//								fields : [
//										'Name',
//										'Code' ],
//								data : [
//										[
//												'全部',
//												-1 ],
//										[
//												'未废除',
//												0 ],
//										[
//												'已废除',
//												1 ] ]
//							}),
//					value : -1,
//					mode : 'local',
//					// typeAhead: true,
//					name : 'is_delete'
////						,
////					id : 'is_delete_query'
				});
		var urgent_state = new Ext.form.field.ComboBox({
					fieldLabel : '紧急程度',
					width : 155,
					labelWidth : 70,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					labelAlign : 'right',
					valueField : 'Code',
					store : new Ext.data.ArrayStore(
							{
								fields : ['Name','Code' ],
								data : [['全部','' ],['普通',0 ],['紧急',1 ] ]
							}),
					value : '',
					mode : 'local',
					// typeAhead: true,
					name : 'urgent_state'
//						,
//					id : 'urgent_state_query'
				});
		var verify_state = new Ext.form.field.ComboBox({
			fieldLabel : '审核状态',
			width : 250,
			labelWidth : 70,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'right',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部','-1'],['未审核',0 ],['审核不通过',1 ],['审核通过，样本未审核',2 ],['样本审核通过',3 ],['样本审核未通过',4 ] ]
					}),
			value : '',
			mode : 'local',
			// typeAhead: true,
			name : 'verify_state',
			value: 0
//						,
//					id : 'urgent_state_query'
		});
		var starttime= new Ext.form.DateField({
			name : 'starttime',
//			id : 'starttime_query',
			width : 175,
			fieldLabel : '受理日期 从',
			labelWidth : 70,
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			maxValue : new Date()
//		,
//			listeners : {
//				'select' : function() {
//					var start = Ext.getCmp(
//							'starttime_query')
//							.getValue();
//					Ext.getCmp('endtime_query')
//							.setMinValue(
//									start);
//					var endDate = Ext
//							.getCmp(
//									'endtime_query')
//							.getValue();
//					if (start > endDate) {
//						Ext
//								.getCmp(
//										'endtime_query')
//								.setValue(
//										start);
//					}
//				}
//			}
		});
		
		var endtime= new Ext.form.DateField({
//			id : 'endtime_query',
			name : 'endtime',
			width : 135,
			labelWidth : 20,
			fieldLabel : '到',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY)
//					,
//			listeners : {
//				select : function() {
//					var start = Ext.getCmp(
//							'starttime_query')
//							.getValue();
//					var endDate = Ext
//							.getCmp(
//									'endtime_query')
//							.getValue();
//					if (start > endDate) {
//						Ext
//								.getCmp(
//										'starttime_query')
//								.setValue(
//										endDate);
//					}
//				}
//			}
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','userid','peruserid','remark','username','case_code','perusername','case_areaname','case_reciver','idAdress',
			        'client','phone','urgent_state','verify_state','accept_time','close_time','text','is_delete'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'judicial/agent/querypage.do',
                params:{
                },
                reader: {
                    type: 'json',
                    root:'data',
                    totalProperty:'total'
                }
            },
            listeners:{
            	'beforeload':function(ds, operation, opt){
        			Ext.apply(me.store.proxy.params, {
        					userid:agentCombo.getValue(),
        					peruserid:marketCombo.getValue(),
						    urgent_state : urgent_state.getValue(),
							case_code : Ext.util.Format.trim(case_code.getValue()),
							is_delete : is_delete.getValue(),
							starttime:dateformat(starttime.getValue()),
							endtime:dateformat(endtime.getValue()),
							flag:i,
							verify_state:verify_state.getValue()
						})
        		}
            }
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode: 'SINGLE'
		});
		
		me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录"
        });
		me.columns = [
		              Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'4%', menuDisabled:true}),
	                  { text: '市场人员',	dataIndex: 'perusername', menuDisabled:true, flex: .6},
	                  { text: '代理商',	dataIndex: 'username', menuDisabled:true, flex: .6},
	                  { text: '案例编号',	dataIndex: 'case_code', menuDisabled:true, flex: .8,
	                	  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var isnull= record.data["is_delete"];
								if (isnull == 1) {
									return "<div style=\"text-decoration: line-through;color: red;\">"
											+ value + "</div>"
								} else {
									return value;
								}

							}
	                  },
		              { text: '案例归属地',	dataIndex: 'case_areaname', menuDisabled:true, flex: 1.5},
		              { text: '归属人',	dataIndex: 'case_reciver', menuDisabled:true, flex: .6},
		              { text: '身份证地址',	dataIndex: 'idAdress', menuDisabled:true, flex: 1},
		              { text: '电话号码',	dataIndex: 'phone', menuDisabled:true, flex: 1},
		              { text: '委托人',	dataIndex: 'client', menuDisabled:true, flex: .6},
		              { text: '紧急程度',	dataIndex: 'urgent_state', menuDisabled:true, flex: 1,
		            	  renderer : function(value) {
							switch (value) {
							case 0:
								return "普通";
								break;
							case 1:
								return "<span style='color:red'>紧急</span>";
								break;
							default:
								return "";
							}
						}},
		              { text: '案例审核状态',	dataIndex: 'verify_state', menuDisabled:true, flex: 1,
		            	  renderer : function(value) {
								switch (value) {
								case 0:
									return "未审核";
									break;
								case 1:
									return "<span style='color:red'>审核不通过</span>";
									break;
								case 2:
									return "<span style='color:green'>审核通过，样本未审核</span>";
									break;
								case 3:
									return "<span style='color:green'>样本审核通过</span>";
									break;
								case 4:
									return "<span style='color:red'>样本审核未通过</span>";
									break;
								default:
									return "";
								}
							}
						},
		              { text: '受理日期',	dataIndex: 'accept_time', menuDisabled:true, flex: 1},
		              { text: '截至日期',	dataIndex: 'close_time', menuDisabled:true, flex: 1},
		              { text: '模版名称',	dataIndex: 'text', menuDisabled:true, flex: 1}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[case_code,is_delete,urgent_state,verify_state]
	 	}
		,{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '1px !important'
		    },
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top', 		
	 		items:[marketCombo,agentCombo,starttime,endtime,{
            	text:'查询',
            	iconCls:'Find',
            	handler:me.onSearch
}
//	 		      {
//	 			text:'添加',
//	 			iconCls:'Add',
//	 			handler:me.onInsert
//	 		}
//	 		,{
//	 			text:'修改',
//	 			iconCls:'Pageedit',
//	 			handler:me.onUpdate
//	 		}
//	 		,{
//	 			text:'删除',
//	 			iconCls:'Delete',
//	 			handler:me.onDelete
//	 		}
	 		]
	 	}
		];
		me.store.load();
		me.callParent(arguments);
	},
	onInsert:function(){
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.judicial.panel.JudicialAgentFormPanel",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'代理商管理—新增',
			width:400,
			iconCls:'Add',
			height:180,
			layout:'border',
			items:[form]
		});
		win.show();
	},
	onDelete:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		};
		var values = {
				id:"'"+selections[0].get("id")+"'"
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.Ajax.request({  
	    			url:"judicial/dickeys/delete.do", 
	    			method: "POST",
	    			headers: { 'Content-Type': 'application/json' },
	    			jsonData: values, 
	    			success: function (response, options) {  
	    				response = Ext.JSON.decode(response.responseText); 
	                     if (response.result == true) {  
	                     	Ext.MessageBox.alert("提示信息", response.message);
	                     	me.getStore().load();
	                     }else { 
	                     	Ext.MessageBox.alert("错误信息", response.message);
	                     } 
	    			},  
	    			failure: function () {
	    				Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
	    			}
	        	       
	          	});
	        }
		});
		
	
	},
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		};
		var form = Ext.create("Rds.judicial.panel.JudicialAgentFormPanel",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'代理商管理—修改',
			width:400,
			iconCls:'Pageedit',
			height:180,
			layout:'border',	
			items:[form]
		});
		win.show();
		form.loadRecord(selections[0]);
	},
	onSearch:function(){
		i++;
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
		
	}
});
function dateformat(value) {
	if (value != null) {
		return Ext.Date.format(value, 'Y-m-d');
	} else {
		return '';
	}
}