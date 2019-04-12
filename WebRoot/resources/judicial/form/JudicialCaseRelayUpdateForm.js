Ext
		.define(
				'Rds.judicial.form.JudicialCaseRelayUpdateForm',
				{
					extend : 'Ext.form.Panel',
					style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					bodyPadding : 10,
					defaults : {
						anchor : '100%',
						labelWidth : 80,
						labelSeparator: "：",
						labelAlign: 'right'
					},
					border : false,
					autoHeight : true,
					initComponent : function() {
						var me = this;
						me.items = [{
							xtype : 'hiddenfield',
							name : 'relay_id'
					            },{
							        xtype     : 'textareafield',
							        grow      : true,
							        name      : 'relay_remark',
							        fieldLabel: '提交报告备注',
							        anchor    : '100%',
							        height:100,
							        maxLength : 500,
					             }];
						me.buttons = [ {
							text : '保存',
							iconCls : 'Disk',
							handler : me.onSave
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : me.onCancel
						} ]
						me.callParent(arguments);
					},
					onSave : function() {
						var me = this.up("form");
						var form = me.getForm();
						var values = form.getValues();
						if(form.isValid()){
							var grid = me.down("gridpanel[name=grid_add]");
							var records=grid.getStore().getRange(0,grid.getStore().getCount());
							if(records.length<1){
								Ext.Msg.alert("提示", "请选择需要交接的报告!");
								return;
							};
							var ids = '';
							for(var i = 0 ; i < records.length ; i ++)
							{
							   ids += records[i].get("case_id")+",";
							}
							ids = ids.substring(0,(ids.length-1));
							values["case_ids"] = ids;
							Ext.Ajax.request({  
								url:"judicial/caseRelay/updateCaseRelayInfo.do", 
								method: "POST",
								headers: { 'Content-Type': 'application/json' },
								jsonData: values, 
								success: function (response, options) {  
									response = Ext.JSON.decode(response.responseText); 
					                 if (response==true) {  
					                 	Ext.MessageBox.alert("提示信息", "添加交接成功");
					                 	me.grid.getStore().load();
					                 	me.up("window").close();
					                 }else { 
					                 	Ext.MessageBox.alert("错误信息", "添加交接失败");
					                 } 
								},  
								failure: function () {
									Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
								}
					      	});
						}
					},
					onCancel : function() {
						var me = this;
						me.up("window").close();
					},
					listeners : {
						'afterrender' : function() {
							var me = this;
							var form = me.getForm();
							var values = form.getValues();
							var store=Ext.create(
									'Ext.data.Store',
									{
										fields : [ "case_id","case_code",'selected'],// 定义字段
										proxy : {
											type : 'jsonajax',
											actionMethods : {
												read : 'POST'
											},
											url :'judicial/caseRelay/getPrintCaseCode.do',
											params : {
												start : 0,
												relay_id:values["relay_id"]
											},
											reader : {
												type : 'json',
												root : 'items',
												totalProperty : 'count'
											}
										},
										autoLoad : true
									});
							var store_add=Ext.create(
									'Ext.data.Store',
									{
										fields : [ "case_id","case_code",'selected'],// 定义字段
										proxy : {
											type : 'jsonajax',
											actionMethods : {
												read : 'POST'
											},
											url :'judicial/caseRelay/getRelayCaseCode.do',
											params : {
												relay_id:values["relay_id"]
											},
											reader : {
												type : 'json',
												root : 'items',
												totalProperty : 'count'
											}
										},
										autoLoad : true
									});
							var grid=Ext.create('Ext.grid.Panel', {
								height : 300,
								frame : false,
								loadMask : true,
								width:480,
								name:"grid",
								region : 'center',
								viewConfig : {
									forceFit : true,
									trackOver : false,
									stripeRows : false
								},
								selModel:Ext.create('Ext.selection.CheckboxModel',{
//									mode: 'SINGLE'
								}),
								bbar:Ext.create('Ext.PagingToolbar', {
									store : store,
									pageSize : 25,
									displayInfo : true,
									displayMsg : "第 {0} - {1} 条  共 {2} 条",
									emptyMsg : "没有符合条件的记录"
								}),
								store : store,
								columns : [// 配置表格列
								{
									header : "可选案例条形码",
									dataIndex : 'case_code',
									flex : 1,
									menuDisabled : true
								}]
							});
							var grid_add=Ext.create('Ext.grid.Panel', {
								height : 300,
								width:280,
								frame : false,
								loadMask : true,
								name:"grid_add",
								viewConfig : {
									forceFit : true,
									trackOver : false,
									stripeRows : false
								},
								region : 'east',
								store : store_add,
								columns : [// 配置表格列
								{
									header : "已选案例条形码",
									dataIndex : 'case_code',
									flex : 1,
									menuDisabled : true
								}]
							});
							grid_add.on('itemdblclick', function(g, record, index, eOpts) {
								grid_add.store.remove(record);
								var records=grid.getStore().getRange(0,grid.getStore().getCount()); 
								for(var i=0;i<records.length;i++){
									if(records[i].get("case_code")==record.get("case_code")){
										grid.getSelectionModel().deselect(records[i]);
						    		 }
								}
						    });
							grid.store.on('load', function(store, records, options) {
							    var re=grid_add.getStore().getRange(0,grid_add.getStore().getCount()); 
							    var r=[];
							    for(var i=0;i<records.length;i++){
							        for(var j=0;j<re.length;j++){
							    		 if(re[j].get("case_code")==records[i].get("case_code")){
							    			 r.push(records[i]);
							    		 }
								    }
							    }
							    if(r.length>0){
							    	  grid.getSelectionModel().select(r); 
							    }
						    });
							grid.on('select', function(grid, record, index, eOpts) {
								var records=grid_add.getStore().getRange(0,grid_add.getStore().getCount()); 
								var result=true;
								for(var i=0;i<records.length;i++){
									if(record.get("case_code")==records[i].get("case_code")){
										result=false;
									}
								}
								if(result){
									grid_add.store.insert(0,record);
								}
						    });
							grid.on('deselect', function(grid, record, index, eOpts) {
								var records=grid_add.getStore().getRange(0,grid_add.getStore().getCount()); 
								for(var i=0;i<records.length;i++){
									if(record.get("case_code")==records[i].get("case_code")){
										grid_add.store.remove(records[i]);
									}
								}
						    });
							me.add({
				    			xtype : 'fieldset',
								title : '勾选左侧编号进入右侧选中框，如取消，双击右侧选项&nbsp;',
								style:'margin-top:10px;',
								width:850,
				            	height:370,
				            	layout :"border",// 从左往右的布局
								defaults : {
									anchor : '100%'
								},
								items : [grid,							            
					            	grid_add]	
			                });
						}
					}
				});
