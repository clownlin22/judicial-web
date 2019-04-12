Ext.define("Rds.judicial.panel.JudicialAgentManageGridPanel",{
	extend:"Ext.grid.Panel",
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
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
//    	        	roletype:'103'
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
	        labelWidth:50,
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
			fieldLabel:'员工',
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
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','userid','peruserid','remark','username','perusername','createtime','createper'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'judicial/agent/queryAgent.do',
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
            		me.getSelectionModel().clearSelections();
        			Ext.apply(me.store.proxy.params, {
        					userid:agentCombo.getValue(),
        					peruserid:marketCombo.getValue(),
        					flag:1
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
	                  { text: '市场人员',	dataIndex: 'perusername', menuDisabled:true, width:200},
	                  { text: '代理商',	dataIndex: 'username', menuDisabled:true, width:200},
	                  { text: '更新时间',	dataIndex: 'createtime', menuDisabled:true, width:200},
	                  { text: '创建人员',	dataIndex: 'createper', menuDisabled:true, width:200},
	                  { text: '备注',	dataIndex: 'remark', menuDisabled:true, width:200}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[marketCombo,agentCombo,{
					            	text:'查询',
					            	iconCls:'Find',
					            	handler:me.onSearch
            }]
	 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[{
	 			text:'添加',
	 			iconCls:'Add',
	 			handler:me.onInsert
	 		},{
	 			text:'修改',
	 			iconCls:'Pageedit',
	 			handler:me.onUpdate
	 		},{
	 			text:'删除',
	 			iconCls:'Delete',
	 			handler:me.onDelete
	 		},{
	 			text:'协议管理',
	 			iconCls:'Cog',
	 			handler:me.onConfig
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	onConfig:function(){

		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length < 1){
			Ext.Msg.alert("提示", "请选择一条需要操作的记录!");
			return;
		};
		attachment_onInsert=function(me)
		{

			attachment_save=function(mei)
			{
				var form = mei.up("form").getForm();
				if (!form.isValid()) {
					Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
					return;
				}
				form.submit({
							url : 'judicial/agent/uploadAttachment.do',
							method : 'post',
							waitMsg : '正在上传您的文件...',
							success : function(form, action) {
								response = Ext.JSON
										.decode(action.response.responseText);
								console.log(response);
								if (response) {
									Ext.MessageBox.alert("提示信息", "上传成功！");
									var grid = me.up("gridpanel");
									grid.getStore().load();
									mei.up("window").close();
								} else {
									Ext.MessageBox.alert("提示信息", "上传失败，请联系管理员!");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "上传失败，请联系管理员!");
							}
						});
			}
			var attachment_insert = Ext.create("Ext.window.Window", {
				title : '附件上传',
				width : 500,
				height : 400,
				layout : 'border',
				items : [{
					xtype : 'form',
					region : 'center',
					style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					labelAlign : "right",
					autoScroll : true,
					bodyPadding : 10,
					defaults : {
						anchor : '100%',
						labelWidth : 80,
						labelSeparator : "：",
						labelAlign : 'right'
					},
					border : false,
					autoHeight : true,
					buttons : [{
								text : '保存',
								iconCls : 'Disk',
								handler : attachment_save
							}, {
								text : '取消',
								iconCls : 'Cancel',
								handler : case_canel
							}],
					items : [{
						xtype : 'fieldset',
						title : '案例附件',
						id:'testFieldset',
						layout : 'anchor',
						defaults : {
							anchor : '100%'
						},
						items : [{
							layout : "column",// 从左往右的布局
							xtype : 'fieldcontainer',
							border : false,
							items : [ 	 {
								xtype : 'filefield',
								name : 'files',
								fieldLabel : '文件<span style="color:red">*</span>',
								msgTarget : 'side',
								allowBlank : false,
								labelWidth : 40,
								anchor : '100%',
								buttonText : '选择文件',
								columnWidth : .60,
							},new Ext.form.field.ComboBox({
								fieldLabel : '文件类型<span style="color:red">*</span>',
								labelWidth : 70,
								labelAlign : 'right',
								fieldStyle : me.fieldStyle,
								editable : false,
								allowBlank : false,
								triggerAction : 'all',
								displayField : 'Name',
								valueField : 'Code',
								store : new Ext.data.ArrayStore({
									fields : ['Name', 'Code'],
									data : [['协议', 1], ['其他', 2]]
								}),
								mode : 'local',
								name : 'filetype',
								columnWidth : .4,
								style:'margin-left:10px;'
							})
							]
						},{
							xtype : 'panel',
							layout : 'absolute',
							border : false,
							items : [{
								xtype : 'button',
								text : '增加文件',
								width : 100,
								style:'margin-bottom:10px',
								x : 0,
								handler : function() {
//									var me = this.up('form');
									var me = Ext.getCmp("testFieldset");
									me.add({
										xtype : 'form',
										style : 'margin-top:5px;',
										layout : 'column',
										border : false,
										items : [{
													xtype : 'filefield',
													name : 'files',
													columnWidth : .60,
													fieldLabel : '文件<span style="color:red">*</span>',
													labelWidth : 40,
													msgTarget : 'side',
													allowBlank : false,
													anchor : '100%',
													style:'margin-bottom:10px',
													buttonText : '选择文件'
												},new Ext.form.field.ComboBox({
													fieldLabel : '文件类型<span style="color:red">*</span>',
													labelWidth : 70,
													labelAlign : 'right',
													fieldStyle : me.fieldStyle,
													editable : false,
													allowBlank : false,
													triggerAction : 'all',
													displayField : 'Name',
													valueField : 'Code',
													store : new Ext.data.ArrayStore({
																fields : ['Name', 'Code'],
																data : [['协议', 1], ['其他', 2]]
															}),
													mode : 'local',
													name : 'filetype',
													columnWidth : .4,
													style:'margin-left:10px;'
												}), {
													xtype : 'button',
													style : 'margin-left:15px;',
													text : '删除',
													handler : function() {
														var me = this.up("form");
														console.log(me);
														me.disable(true);
														me.up("fieldset").remove(me);
													}
												}]
									});
								}
							}]

						}
						]
					
			     }, {
						xtype : 'hiddenfield',
						name : 'case_code',
						value : selections[0].get("case_id")
					}]
				}]
			})
			attachment_insert.show();
			
			
		}
		attachment_onDel = function(me) {
			var grid = me.up("gridpanel");
			var selections = grid.getView().getSelectionModel().getSelection();
			if (selections.length < 1 || selections.length > 1) {
				Ext.Msg.alert("提示", "请选择一条需要删除的记录!");
				return;
			};
			Ext.Msg.show({
						title : '提示',
						msg : '确定删除该记录?',
						width : 300,
						buttons : Ext.Msg.OKCANCEL,
						fn : function(buttonId, text, opt) {
							if (buttonId == 'ok') {
								var values = {
									id : selections[0].get("id"),
									attachment_path:selections[0].get("attachment_path")
								};
								Ext.Ajax.request({
											url : "judicial/agent/deleteAttachment.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : values,
											success : function(response, options) {
												response = Ext.JSON
														.decode(response.responseText);
												console.log(response.result)
												if (response.result == true) {
													Ext.MessageBox.alert("提示信息",
															response.msg);
													var grid = me.up("gridpanel");
													grid.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息",
															response.msg);
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
		}
		var win = Ext.create("Ext.window.Window", {
			title : "附件管理",
			width : 700,
			iconCls : 'Find',
			height : 400,
			modal:true,
			resizable:false,
			layout : 'border',
			bodyStyle : "background-color:white;",
			items : [ Ext.create('Ext.grid.Panel', {
				renderTo : Ext.getBody(),
				width : 690,
				height : 400,
				frame : false,
				viewConfig : {
					forceFit : true,
					stripeRows : true// 在表格中显示斑马线
				},
				dockedItems : [{
					xtype : 'toolbar',
					dock : 'top',
					items : [ {
								xtype : 'button',
								text : '新增',
								iconCls : 'Pageedit',
								handler : attachment_onInsert
							}, {
								xtype : 'button',
								text : '删除',
								iconCls : 'Delete',
								handler : attachment_onDel
							}

					]
				}],
				store : {// 配置数据源
					fields : [ 'id', 'case_id', 'attachment_path','attachment_date','attachment_type','username' ],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/agent/queryAttachment.do',
						params : {
							'case_id' : selections[0].get("case_id")
						},
						reader : {
//							type : 'json',
//							root : 'items',
//							totalProperty : 'count'
						}
					},
					autoLoad : true
				},
				columns : [// 配置表格列
				           {
							text : '类型',
							dataIndex : 'attachment_type',
							width : '40%',
							menuDisabled : true,
							flex : 1,
							renderer : function(value) {
								switch (value) {
									case 1 :
										return "协议";
										break;
									case 2 :
										return "其他";
										break;
									default :
										return "其他";
								}
							}
						}, {
							text : '附件',
							dataIndex : 'attachment_path',
							width : '40%',
							menuDisabled : true,
							flex : 3
						}, {
							text : '最后上传日期',
							dataIndex : 'attachment_date',
							width : '10%',
							menuDisabled : true,
							flex : 1
						},
						{
							text : '上传人员',
							dataIndex : 'username',
							width : '10%',
							menuDisabled : true,
							flex : 1,
							renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var isnull= record.data["username"];
								if (isnull == null) {
									return "系统生成"
								} else
								{
									return value;
								}

							}
						}, {
							header : "操作",
							dataIndex : '',
							flex : 0.5,
							menuDisabled : true,
							renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
									return "<a href='#'>查看</a>";
							},
							listeners:{
								'click':function(){ 
									var me = this.up("gridpanel");
									var selections = me.getView().getSelectionModel().getSelection();
									if (selections.length < 1 || selections.length > 1) {
										Ext.Msg.alert("提示", "请选择需要查看的一条记录!");
										return;
									}
									console.log(selections[0].get(""));
									var form = Ext.create(
											"Rds.judicial.form.JudicialImageShow", {
												region : "center",
												grid : me
											});
									var win = Ext.create("Ext.window.Window", {
												title : '图片查看',
												width : 1600,
												iconCls : 'Pageedit',
												height : 1000,
												maximizable : true,
												maximized : true,
												layout : 'border',
												items : [form]
											});
									form.loadRecord(selections[0]);
									win.show();
								}
							}
						} ]
			}) ]
		});
		win.show();
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
			height:250,
			modal:true,
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
				id:selections[0].get("id")
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在操作','请稍后...');
	        	Ext.Ajax.request({  
	    			url:"judicial/agent/delete.do", 
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
			height:250,
			modal:true,
			layout:'border',	
			items:[form]
		});
		win.show();
		form.loadRecord(selections[0]);
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.getStore().load();
		
	}
});