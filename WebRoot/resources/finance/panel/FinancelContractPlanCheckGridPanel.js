onDownload = function(value) {
	window.location.href = "finance/casefinance/downloadAttachment.do?id=" + value;
}
case_canel = function(me) {
	me.up("window").close();
}
/**
 * 汇款单生成
 * 
 * @author yuanxiaobo
 */
Ext.define("Rds.finance.panel.FinancelContractPlanCheckGridPanel",{
	extend:"Ext.grid.Panel",
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var contract_num = Ext.create('Ext.form.field.Text',{
            	name:'contract_num',
            	labelWidth:70,
            	width:'20%',
            	fieldLabel:'合同编号'
            });
		var contract_unit = Ext.create('Ext.form.field.Text',{
        	name:'contract_unit',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'合作单位'
        });
		var customer_name = Ext.create('Ext.form.field.Text',{
        	name:'customer_name',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'客户名称'
        });
		var contract_username = Ext.create('Ext.form.field.Text',{
        	name:'contract_username',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'归属人'
        });
		var contract_areaname =  Ext.create('Ext.form.field.Text',{
        	name:'contract_areaname',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'归属地'
        });
		var create_per_name = Ext.create('Ext.form.field.Text',{
        	name:'create_per_name',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'创建人'
        });
		var oa_num = Ext.create('Ext.form.field.Text',{
        	name:'oa_num',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'OA编号'
        });
		var status = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '合同状态',
			width:'20%',
			labelWidth : 70,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
				fields : [ 'Name', 'Code' ],
				data : [ [ '全部', '' ],[ '待执行', 1 ], [ '执行中', 2 ], [ '未执行', 3 ], [ '执行结束', 4 ] ]
			}),
			value : '',
			mode : 'local',
			name : 'status',
			hidden:true
		});
		var starttime=Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width:'22%',
			labelWidth : 70,
			fieldLabel : '受理时间 从',
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
			width:'18%',
			labelWidth : 20,
			fieldLabel : '到',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY,1),
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
		me.store = Ext.create('Ext.data.Store',{
			fields : ['contract_id','accept_date', 'contract_price', 'contract_unit','customer_name', 'contract_userid','contract_username','contract_num','contract_program',
						'contract_areacode', 'contract_areaname', 'create_per', 'create_per_name','create_date','status','remark',
						'oa_num'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url : 'finance/casefinance/queryPageContractPlan.do',
                params:{
                },
                reader : {
					type : 'json',
					root : 'items',
					totalProperty : 'count'
				}
            },
            listeners:{
            	'beforeload':function(ds, operation, opt){
            		me.getSelectionModel().clearSelections();
        			Ext.apply(me.store.proxy.params, {
        				contract_num:trim(contract_num.getValue()),
        				contract_unit:trim(contract_unit.getValue()),
        				contract_username:trim(contract_username.getValue()),
        				contract_areaname:trim(contract_areaname.getValue()),
        				create_per_name:trim(create_per_name.getValue()),
        				oa_num:trim(oa_num.getValue()),
        				status:status.getValue(),
        				starttime : dateformat(starttime
								.getValue()),
						endtime : dateformat(endtime
								.getValue()),
						customer_name:customer_name.getValue().trim()
        				});
        		}
            }
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
		});
		
		me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录"
        });
		me.columns = [Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'4%', menuDisabled:true}),
	                  { text: '合同编号',	dataIndex: 'contract_num',menuDisabled:true, width : 150,
	                	  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var status= record.data["status"];
								if (status == 4) {
									return "<div style=\"color: red;\">"
											+ value + "</div>"
								} 
								{
									return value;
								}
	
							}
			          },
			          { text: '受理时间',	dataIndex: 'accept_date', menuDisabled:true, width : 100},
			          { text: '合作单位',	dataIndex: 'contract_unit', menuDisabled:true, width : 100},
			          { text: '合同项目',	dataIndex: 'contract_program', menuDisabled:true, width : 100},
			          { text: '客户名称',	dataIndex: 'customer_name', menuDisabled:true, width : 100},
	                  { text: '合同价格',	dataIndex: 'contract_price', menuDisabled:true, width : 100},
	                  { text: '归属人', 	dataIndex: 'contract_username', menuDisabled:true, width : 120},
	                  { text: '归属地', 	dataIndex: 'contract_areaname', menuDisabled:true, width : 150},
	                  { text: '创建人', 	dataIndex: 'create_per_name', menuDisabled:true, width : 100},
	                  { text: '创建时间', 	dataIndex: 'create_date',menuDisabled:true, width : 150},
//	                  { text: '状态', 	dataIndex: 'status', menuDisabled:true,width : 100,
//	                	  renderer : function(value,meta,record) {
//								switch (value) {
//								case '1':
//									return "待执行";
//									break;
//								case '2':
//									return "执行中";
//									break;
//								case '3':
//									return "未执行";
//									break;
//								case '4':
//									return "执行结束";
//									break;
//								default:
//									return "";
//								}
//							}  
//	                  },
	                  { text: 'oa编号', dataIndex: 'oa_num', menuDisabled:true, width : 100},
	                  { text: '备注', dataIndex: 'remark', menuDisabled:true, width : 200}
		              ];
		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[contract_num,contract_unit,contract_username,contract_areaname,create_per_name]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[starttime,endtime,status,oa_num,{
            	text:'查询',
            	iconCls:'Find',
            	handler:me.onSearch}
	 			]
	 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[{
	 			text:'附件管理',
	 			iconCls:'Cog',
	 			handler:me.attachmentManage
	 		},{
	 			text:'回款查看',
	 			iconCls:'Find',
	 			handler:me.onFind
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	attachmentManage:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条操作的记录!");
			return;
		}
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
							url : 'finance/casefinance/upload.do',
							method : 'post',
							waitMsg : '正在上传您的文件...',
							success : function(form, action) {
								response = Ext.JSON
										.decode(action.response.responseText);
								if (response) {
									Ext.MessageBox.alert("提示信息", response.msg);
									var grid = me.up("gridpanel");
									grid.getStore().load();
									mei.up("window").close();
								} else {
									Ext.MessageBox.alert("提示信息", response.msg);
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
							},{
				    			xtype:"textfield",
				    			fieldLabel: '文件类型<span style="color:red">*</span>',
				    			labelWidth:70,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'filetype',
								columnWidth : .4,
								style:'margin-left:10px;'
				    		},
//				    		new Ext.form.field.ComboBox({
//								fieldLabel : '文件类型<span style="color:red">*</span>',
//								labelWidth : 70,
//								labelAlign : 'right',
//								fieldStyle : me.fieldStyle,
//								editable : false,
//								allowBlank : false,
//								triggerAction : 'all',
//								displayField : 'Name',
//								valueField : 'Code',
//								store : new Ext.data.ArrayStore({
//									fields : ['Name', 'Code'],
//									data : [['登记表格', 1], ['身份证', 2],
//											['照片', 3], ['其他', 4]]
//								}),
//								mode : 'local',
//								name : 'filetype',
//								columnWidth : .4,
//								style:'margin-left:10px;'
//							})
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
												},{
									    			xtype:"textfield",
									    			fieldLabel: '文件类型<span style="color:red">*</span>',
									    			labelWidth:70,
									    			fieldStyle:me.fieldStyle, 
									    			name: 'filetype',
													columnWidth : .4,
													style:'margin-left:10px;'
									    		},
//												new Ext.form.field.ComboBox({
//													fieldLabel : '文件类型<span style="color:red">*</span>',
//													labelWidth : 70,
//													labelAlign : 'right',
//													fieldStyle : me.fieldStyle,
//													editable : false,
//													allowBlank : false,
//													triggerAction : 'all',
//													displayField : 'Name',
//													valueField : 'Code',
//													store : new Ext.data.ArrayStore({
//																fields : ['Name', 'Code'],
//																data : [['登记表格', 1], ['身份证', 2],
//																		['照片', 3], ['其他', 4]]
//															}),
//													mode : 'local',
//													name : 'filetype',
//													columnWidth : .4,
//													style:'margin-left:10px;'
//												}),
												{
													xtype : 'button',
													style : 'margin-left:15px;',
													text : '删除',
													handler : function() {
														var me = this.up("form");
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
						name : 'contract_id',
						value : selections[0].get("contract_id")
					}, {
						xtype : 'hiddenfield',
						name : 'contract_num',
						value : selections[0].get("contract_num")
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
			}
			Ext.Msg.show({
						title : '提示',
						msg : '确定删除该记录?',
						width : 300,
						buttons : Ext.Msg.OKCANCEL,
						fn : function(buttonId, text, opt) {
							if (buttonId == 'ok') {
								var values = {
									id : selections[0].get("id"),
								}
								Ext.Ajax.request({
											url : "finance/casefinance/delete.do",
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
					fields : [ 'id', 'contract_id','contract_num', 'attachment_path','attachment_date','attachment_type','create_pername' ],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'finance/casefinance/queryAllContractAttachment.do',
						params : {
							'contract_id' : selections[0].get("contract_id")
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
							flex : 1
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
							dataIndex : 'create_pername',
							width : '10%',
							menuDisabled : true,
							flex : 1
						}, {
							header : "操作",
							dataIndex : '',
							flex : 0.5,
							menuDisabled : true,
							renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var id = record.data["id"];
								return "<a href='#' onclick=\"onDownload('" + id
								+ "')\"  >下载";
							}
						} ]
			}) ]
		});
		win.show();
	},
	onFind:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要查看的记录!");
			return;
		};
		var win = Ext.create("Ext.window.Window", {
			title : "合同编码：" + selections[0].get("contract_num"),
			width : 1100,
			iconCls : 'Find',
			height : 300,
			modal:true,
			layout : 'border',
			bodyStyle : "background-color:white;",
			items : [ Ext.create('Ext.grid.Panel', {
				renderTo : Ext.getBody(),
				width : 1100,
				height : 800,
				frame : false,
				viewConfig : {
					forceFit : true,
					stripeRows : true// 在表格中显示斑马线
				},
				store : {// 配置数据源
					fields : [ 'contract_remittance_planid', 'remittance', 'remittance_date','status','create_pername','insideCost','insideCostUnit','manageCost','manageUnit','externalCost','create_date'],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'finance/casefinance/queryAllContractPlan.do',
						params : {
							'contract_id' : selections[0].get("contract_id")
						},
						reader : {
							type : 'json',
//							root : 'items',
//							totalProperty : 'count'
						}
					},
					autoLoad : true
				// 自动加载
				},
				columns : [// 配置表格列
				{
					header : "回款日期",
					dataIndex : 'remittance_date',
					menuDisabled : true
				},{
					header : "汇款金额",
					dataIndex : 'remittance',
					menuDisabled : true
				},{
					header : "创建人",
					dataIndex : 'create_pername',
					menuDisabled : true
				},{
					header : "创建时间",
					dataIndex : 'create_date',
					menuDisabled : true
				},{
					text : '状态',
					dataIndex : 'status',
					menuDisabled : true,
					renderer : function(value,meta,record) {
						switch (value) {
						case '1':
							return "汇款创建";
							break;
						case '2':
							return "日报生成";
							break;
						case '3':
							return "已汇款";
							break;
						case '4':
							return "已确认汇款";
							break;
						case '5':
							return "已过期";
							break;
						default:
							return "";
						}
					}
				},{
					header : "内部结算部门",
					dataIndex : 'insideCostUnit',
					menuDisabled : true
				},{
					header : "内部结算价",
					dataIndex : 'insideCost',
					menuDisabled : true
				},{
					header : "管理费部门",
					dataIndex : 'manageUnit',
					menuDisabled : true
				},{
					header : "管理费",
					dataIndex : 'manageCost',
					menuDisabled : true
				}, {
					header : "委外成本",
					dataIndex : 'externalCost',
					menuDisabled : true
				},{
					header : "操作",
					dataIndex : '',
					flex : 0.5,
					menuDisabled : true,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
							return "<a href='#'>修改</a>";
					},
					listeners:{
						'click':function(){ 
							var me = this.up("gridpanel");
							var selections = me.getView().getSelectionModel().getSelection();
							if (selections.length < 1 || selections.length > 1) {
								Ext.Msg.alert("提示", "请选择需要修改的一条记录!");
								return;
							}
							if(selections[0].get("status") >= 3){
								Ext.Msg.alert("提示", "该状态不允许修改!");
								return;
							}
							var form = Ext.create("Rds.finance.form.FinanceRemittanceInsertForm", {
										region : "center",
										grid : me
									});
							var win = Ext.create("Ext.window.Window", {
								title : '合同回款—修改',
								width : 400,
								iconCls : 'Add',
								height : 350,
								modal:true,
								layout : 'border',
								items : [form]
							});
							win.show();
							form.loadRecord(selections[0]);
						}
					}
				},{
					header : "操作",
					dataIndex : '',
					flex : 0.5,
					menuDisabled : true,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
							return "<a href='#'>删除</a>";
					},
					listeners:{
						'click':function(){ 
							var me = this.up("gridpanel");
							var selections = me.getView().getSelectionModel().getSelection();
							if (selections.length < 1 || selections.length > 1) {
								Ext.Msg.alert("提示", "请选择需要删除的一条记录!");
								return;
							}
							if(selections[0].get("status") >= 3){
								Ext.Msg.alert("提示", "该状态不允许删除!");
								return;
							}
							Ext.Msg.show({
								title : '提示',
								msg : '确定删除吗？',
								width : 300,
								buttons : Ext.Msg.OKCANCEL,
								fn : function(buttonId, text, opt) {
									if (buttonId == 'ok') {
										Ext.MessageBox.wait('正在操作','请稍后...');
										Ext.Ajax.request({
											url : "finance/casefinance/deleteRemittancePlan.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : {"contract_remittance_planid":selections[0].get("contract_remittance_planid")},
											success : function(response, options) {
												response = Ext.JSON.decode(response.responseText);
												if (response) {
													Ext.MessageBox.alert("提示信息", "操作成功！");
													me.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息", "操作失败，请联系管理员！");
												}
											},
											failure : function() {
												Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
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
					}
				
				} ]
			}) ]
		});
		win.show();
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.getStore().currentPage=1;
		me.getStore().load();
	}
});