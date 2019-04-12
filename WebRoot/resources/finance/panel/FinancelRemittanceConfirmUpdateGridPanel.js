/**
 * 汇款单生成
 * 
 * @author yuanxiaobo
 */
case_canel = function(me) {
	me.up("window").close();
}
Ext.define("Rds.finance.panel.FinancelRemittanceConfirmUpdateGridPanel",{
	extend:"Ext.grid.Panel",
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var remittance_num = Ext.create('Ext.form.field.Text',{
            	name:'remittance_num',
            	labelWidth:60,
            	width:'20%',
            	fieldLabel:'汇款单号'
            });
		var remittance_per_name = Ext.create('Ext.form.field.Text',{
        	name:'remittance_per_name',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'汇款人'
        });
		var remittance_account = Ext.create('Ext.form.field.Text',{
        	name:'remittance_account',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'汇款账户'
        });
		var arrival_account = Ext.create('Ext.form.field.Text',{
        	name:'arrival_account',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'到款账户'
        });
		var create_per_name = Ext.create('Ext.form.field.Text',{
        	name:'create_per_name',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'创建人'
        });
		var confirm_per_name = Ext.create('Ext.form.field.Text',{
        	name:'confirm_per_name',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'确认人'
        });
		var status = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '确认状态',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
				fields : [ 'Name', 'Code' ],
				data : [ [ '全部', -1 ], [ '已确认', 0 ], [ '未确认', 1 ] ]
			}),
			value : 1,
			mode : 'local',
			name : 'status',
		});
		var confirm_state = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '确认状态',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
				fields : [ 'Name', 'Code' ],
				data : [ [ '全部', '' ], [ '未确认', -1 ],[ '确认通过', 1 ], [ '确认不通过', 2 ] ]
			}),
			value : '',
			mode : 'local',
			name : 'confirm_state',
		});
		var daily_type = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '日报类型',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
				fields : [ 'Name', 'Code' ],
				data : [  [ '亲子鉴定', 1 ], [ '合同计划', 2 ] , [ '无创产前', 3 ], [ '儿童基因库', 4 ]]
			}),
			value : 1,
			mode : 'local',
			name : 'daily_type',
		});
		var starttime=Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width : '18%',
			labelWidth : 70,
			fieldLabel : '汇款时间 从',
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
			width : '16%',
			labelWidth : 40,
			fieldLabel : '到',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
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
			fields : ['remittance_id', 'remittance_num', 'remittance_date', 'remittance','remittance_account','daily_type',
						'arrival_account', 'remittance_per', 'remittance_per_name', 'remittance_remark','create_per','create_per_name',
						'confirm_per','confirm_per_name','confirm_date','confirm_remark','confirm_state','deductible','remittance_att'],
			start:0,
			limit:15,
			pageSize:15,
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url : 'finance/casefinance/queryPageRemittanceInfo.do',
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
        				remittance_num:trim(remittance_num.getValue()),
        				remittance_per_name:trim(remittance_per_name.getValue()),
        				remittance_account:trim(remittance_account.getValue()),
        				arrival_account:trim(arrival_account.getValue()),
        				create_per_name:trim(create_per_name.getValue()),
        				confirm_per_name:trim(confirm_per_name.getValue()),
        				starttime : dateformat(starttime
								.getValue()),
						endtime : dateformat(endtime
								.getValue()),
						daily_type:daily_type.getValue(),
						confirm_state:confirm_state.getValue()
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
		              {
							text : '日报类型',
							dataIndex : 'daily_type',
							menuDisabled : true,
							width : 100,
							renderer : function(value,meta,record) {
								switch (value) {
								case '1':
									return "亲子鉴定";
									break;
								case '2':
									return "合同计划";
									break;
								case '3':
									return "无创产前";
									break;
								default:
									return "亲子鉴定";
								}
							}
						},
	                  { text: '汇款单号',	dataIndex: 'remittance_num',menuDisabled:true, width : 150,
	                	  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var confirm_state= record.data["confirm_state"];
								if (confirm_state != -1) {
									return "<div style=\"color: red;\">"
											+ value + "</div>"
								} 
								{
									return value;
								}
	
							}
			          },{
						text : '确认状态',
						dataIndex : 'confirm_state',
						menuDisabled : true,
						width : 100,
						renderer : function(value,meta,record) {
							switch (value) {
							case '1':
								return "通过";
								break;
							case '2':
								return "<div style=\"color: red;\">不通过</div>"
								break;
							default:
								return "未确认";
							}
						}
			          },{
							text : '是否抵扣',
							dataIndex : 'deductible',
							menuDisabled : true,
							width : 100,
							renderer : function(value,meta,record) {
								switch (value) {
								case '1':
									return "<div style=\"color: red;\">是</div>";
									break;
								case '2':
									return "否"
									break;
								default:
									return "否";
								}
							}
				          },
			          { text: '汇款时间',	dataIndex: 'remittance_date', menuDisabled:true, width : 100},
	                  { text: '汇款金额',	dataIndex: 'remittance', menuDisabled:true, width : 100},
	                  { text: '汇款账户', 	dataIndex: 'remittance_account', menuDisabled:true, width : 120},
	                  { text: '到款账户', 	dataIndex: 'arrival_account', menuDisabled:true, width : 120},
	                  { text: '汇款人', dataIndex: 'remittance_per_name', menuDisabled:true, width : 100},
	                  { text: '汇款备注', 	dataIndex: 'remittance_remark',menuDisabled:true, width : 150},
	                  { text: '汇款创建人', 	dataIndex: 'create_per_name', menuDisabled:true,width : 100},
	                  { text: '汇款确认人', 	dataIndex: 'confirm_per_name', menuDisabled:true, width : 100},
	                  { text: '确认时间', 	dataIndex: 'confirm_date', menuDisabled:true,width : 100},
	                  { text: '确认备注', 	dataIndex: 'confirm_remark', menuDisabled:true,width : 200}
		              ];
		me.dockedItems = [{
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[remittance_num,remittance_per_name,remittance_account,arrival_account,create_per_name]
	 		},{
	 			style : {
			        borderTopWidth : '0px !important',
			        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[starttime,endtime,confirm_per_name,daily_type]
	 		},{

	 			style : {
			        borderTopWidth : '0px !important',
			        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[confirm_state,{
	            	text:'查询',
	            	iconCls:'Find',
	            	handler:me.onSearch
	            }]
	 		
	 		},{
		 		xtype:'toolbar',
		 		dock:'top', 		
		 		items:[{
			 			text:'汇款明细',
			 			iconCls:'Find',
			 			handler:me.onRelativeCase
		 			},{
			 			text:'汇款确认',
			 			iconCls:'Add',
			 			handler:me.onConfirm,
			 			hidden:true
		 			},{
			 			text:'汇款修改',
			 			iconCls:'Pageedit',
			 			handler:me.onUpdate
		 			},{
			 			text:'汇款凭证修改',
			 			iconCls:'Pageedit',
			 			handler:me.onPhotoUpdate
		 			},
			 		{
			 			text:'详情查看',
			 			iconCls:'Find',
			 			handler:me.onCheck
			 		},
			 		{
			 			text:'汇款删除',
			 			iconCls:'Delete',
			 			handler:me.onDelete
			 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	onPhotoUpdate:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
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
							url : 'finance/casefinance/updateRemittancePhoto.do',
							method : 'post',
							waitMsg : '正在上传您的文件...',
							params:{
								'remittance_id':selections[0].get("remittance_id"),
								'remittance_num':selections[0].get("remittance_num")
							},
							success : function(form, action) {
								response = Ext.JSON
										.decode(action.response.responseText);
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
								validator : function(v) {
									if (!v.endWith(".jpg") &&!v.endWith(".JPG")
											&& !v.endWith(".png") && !v.endWith(".PNG")
											&& !v.endWith(".jpeg") && !v.endWith(".JPEG") ) {
										return "请选择.jpg .png .jpeg类型的图片";
									}
									return true;
								}
							}
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
													fieldLabel : '文件<span style="color:red">*</span>',
													labelWidth : 40,
													msgTarget : 'side',
													allowBlank : false,
													columnWidth : .90,
													anchor : '100%',
													style:'margin-bottom:10px',
													buttonText : '选择文件',
													validator : function(v) {
														if (!v.endWith(".jpg") &&!v.endWith(".JPG")
																&& !v.endWith(".png") && !v.endWith(".PNG")
																&& !v.endWith(".jpeg") && !v.endWith(".JPEG") ) {
															return "请选择.jpg .png .jpeg类型的图片";
														}
														return true;
													}
												}, {
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
			if (selections.length < 1) {
				Ext.Msg.alert("提示", "请选择需要修改的记录!");
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
									attachment_path:selections[0].get("attachment_path"),
									remittance_id:selections[0].get("remittance_id")
								};
								Ext.Ajax.request({
											url : "finance/casefinance/deleteFinanceAttachment.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : values,
											success : function(response, options) {
												response = Ext.JSON
														.decode(response.responseText);
												if (response == true) {
													Ext.MessageBox.alert("提示信息","操作成功");
													var grid = me.up("gridpanel");
													grid.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息","操作失败，请联系管理员");
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
					fields : [ 'id', 'remittance_id', 'attachment_path','create_date','create_pername' ],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'finance/casefinance/getFinanceAttachMent.do',
						params : {
							'remittance_id' : selections[0].get("remittance_id")
						},
						reader : {
//							type : 'json',
//							root : 'items',
//							totalProperty : 'count'
						}
					},
					autoLoad : true
				},
				columns : [ {
							text : '附件',
							dataIndex : 'attachment_path',
							width : '40%',
							menuDisabled : true,
							flex : 2
						}, {
							text : '最后上传日期',
							dataIndex : 'create_date',
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
									if("" != selections[0].get("attachment_path") && null !=selections[0].get("attachment_path")){
										var form = Ext.create(
												"Rds.finance.form.FinanceImageShow", {
													region : "center",
													grid : me
												});
										var win = Ext.create("Ext.window.Window", {
													title : '图片查看',
													width : 800,
													iconCls : 'Pageedit',
													height : 700,
													maximizable : true,
													layout : 'border',
													items : [form]
												});
										form.loadRecord(selections[0]);
										win.show();
									}
								}
							}
						} ]
			}) ]
		});
		win.show();
	},
	onDelete:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要删除的记录!");
			return;
		};
		if(selections[0].get("confirm_state")==1)
		{
			Ext.Msg.alert("提示", "该记录已确认，不予删除!");
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
								url : "finance/casefinance/deleteCaseFeeRemittance.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {remittance_id:selections[0].get("remittance_id"),daily_type:selections[0].get("daily_type")},
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
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
			return;
		};
		if(selections[0].get("confirm_state")==1)
		{
			Ext.Msg.alert("提示", "该记录已确认通过!");
			return;
		}
		ownpersonTemp=selections[0].get("remittance_per");
		var form = Ext.create("Rds.finance.form.FinanceRemmitanceUpdateForm", {
					region : "center",
					autoScroll : true,
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
			title : '汇款修改',
			width : 400,
			modal:true,
			iconCls : 'Pageedit',
			height : 370,
			layout : 'border',
			items : [ form ],
		});
		win.show();
		form.loadRecord(selections[0]);
	},
	onConfirm:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要汇款的记录!");
			return;
		};
		var remittance_id='';
		var daily_type='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			daily_type = selections[i].get("daily_type");
			if(selections[i].get("confirm_state") == 1)
			{
				Ext.Msg.alert("提示", "存在已确认汇款单!");
				return;
			}
			if((i+1)==selections.length)
				remittance_id +=  selections[i].get("remittance_id");
			else
				remittance_id += selections[i].get("remittance_id")+",";
		}

		remittance_canel = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["remittance_id"]=remittance_id;
			values["daily_type"]=daily_type;
			values["confirm_state"]=2;
			if(trim(values["confirm_remark"])=="")
			{
				Ext.MessageBox.alert("提示信息", "确认不通过必须填写备注！");
				return;
			}
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "finance/casefinance/updateRemittanceRecord.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response) {
									Ext.MessageBox.alert("提示信息", "汇款单确认不通过成功！");
									var grid = me.up("gridpanel");
									me.getStore().load();
									remittanceConfirmform_add.close();
								} else {
									Ext.MessageBox.alert("错误信息", "确认错误，请联系管理员！");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
			}
		
		}
		remittance_confirm = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["remittance_id"]=remittance_id;
			values["daily_type"]=daily_type;
			values["confirm_state"]=1;
			values["status"]='0';
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "finance/casefinance/updateRemittanceRecord.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response) {
									Ext.MessageBox.alert("提示信息", "汇款单确认成功！");
									var grid = me.up("gridpanel");
									me.getStore().load();
									remittanceConfirmform_add.close();
								} else {
									Ext.MessageBox.alert("错误信息", "确认错误，请联系管理员！");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
			}
		}
		
		var remittanceConfirmform_add = Ext.create("Ext.window.Window", {
			title : '汇款信息',
			width : 320,
			height : 250,
			layout : 'border',
			modal:true,
			items : [{
				xtype : 'form',
				region : 'center',
				style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
				labelAlign : "right",
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
							text : '确认通过',
							iconCls : 'Disk',
							handler : remittance_confirm
						}, {
							text : '确认不通过',
							iconCls : 'Cancel',
							handler : remittance_canel
						}],
				items : [{
					xtype : "textarea",
					fieldLabel : '确认备注',
					labelAlign : 'right',
					maxLength : 100,
					labelWidth : 80,
					name : 'confirm_remark'
				}]
			}]
		})
		remittanceConfirmform_add.show();

	},
	onRelativeCase : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请一条选择记录查看!");
			return;
		}
		var remittance_id = selections[0].get('remittance_id');
		var daily_type =  selections[0].get('daily_type');
		var caseStore = Ext.create('Ext.data.Store', {
					fields : ['case_id', 'case_code', 'receiver_area', 'case_receiver',
							'username', 'stand_sum', 'real_sum', 'return_sum','confirm_code',
							'date'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'judicial/finance/queryDailyDetail.do',
						params : {},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'total'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							Ext.apply(caseGrid.store.proxy.params, {
								remittance_id : remittance_id,
								daily_type:daily_type
									});
						}
					}
				});
		var caseGrid = Ext.create("Ext.grid.Panel", {
			region : 'center',
			store : caseStore,
			columns : [{
						dataIndex : 'case_id',
						hidden : true
					},  {
						text : '案例编号',
						dataIndex : 'case_code',
						width : 150,
						menuDisabled : true
					}, {
						text : '案例所属地',
						dataIndex : 'receiver_area',
						menuDisabled : true,
						width : 150
					}, {
						text : '归属人',
						dataIndex : 'case_receiver',
						menuDisabled : true,
						width : 80
					}, {
						text : '标准金额',
						dataIndex : 'stand_sum',
						width : 80,
						menuDisabled : true
					}, {
						text : '实收金额',
						dataIndex : 'real_sum',
						width : 80,
						menuDisabled : true
					}, {
						text : '汇款金额',
						dataIndex : 'return_sum',
						width : 80,
						menuDisabled : true
					},{
						text : '登记人',
						dataIndex : 'username',
						width : 100,
						menuDisabled : true
					},{
						text : '激活码',
						dataIndex : 'confirm_code',
						width : 150,
						menuDisabled : true
					}],
			bbar : Ext.create('Ext.PagingToolbar', {
						store : caseStore,
						pageSize : 15,
						displayInfo : true,
						displayMsg : "第 {0} - {1} 条  共 {2} 条",
						emptyMsg : "没有符合条件的记录"
					}),
			listeners : {
				'afterrender' : {
					fn : function() {
						caseGrid.store.load();
					}
				}
			}

		});
		var win = Ext.create("Ext.window.Window", {
					title : '相关案例信息',
					width : 810,
					iconCls : 'Pageedit',
					height : 500,
					layout : 'border',
					modal : true,
					buttons : [{
								text : '确定',
								iconCls : 'Accept',
								handler : function() {
									this.up("window").close();
								}
							}],
					items : [caseGrid]

				});
//		form.loadRecord(selections[0]);
		win.show();
	},
	onCheck:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要查看的记录!");
			return;
		};
		remittance_id = selections[0].get("remittance_id");
		if(null == remittance_id)
		{
			Ext.Msg.alert("提示", "请选择已汇款的记录查看!");
			return;
		}
		var win = Ext.create("Ext.window.Window", {
			title : "财务审核详情",
			width : 600,
			iconCls : 'Find',
			height : 350,
			layout : 'border',
			modal:true,
//			border : false,
			bodyStyle : "background-color:white;",
			items : [Ext.create('Ext.grid.Panel', {
								width : 600,
								height : 350,
								frame : false,
								renderTo : Ext.getBody(),
								viewConfig : {
									forceFit : true,
									stripeRows : true// 在表格中显示斑马线
								},
								store : {// 配置数据源
									fields : ['remittance_id', 'confirm_per_name','confirm_state','confirm_date','confirm_remark'],// 定义字段
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'finance/casefinance/queryRemittanceLogs.do',
										params : {
											'remittance_id' : remittance_id
										},
										reader : {
											type : 'json',
											root : 'items',
											totalProperty : 'count'
										}
									},
									autoLoad : true
									// 自动加载
								},
								columns : [// 配置表格列
								{
											header : "操作人",
											dataIndex : 'confirm_per_name',
											flex : 1,
											menuDisabled : true
										}, {
											header : "操作时间",
											dataIndex : 'confirm_date',
											flex : 1,
											menuDisabled : true
										}, {
											header : "操作步骤",
											dataIndex : 'confirm_state',
											flex : 1,
											menuDisabled : true,
											renderer : function(value,meta,record) {
												switch (value) {
												case '-1':
													return "汇款登记";
													break;
												case '1':
													return "汇款确认通过";
													break;
												case '2':
													return "汇款确认不通过";
													break;
												default:
													return "";
												}
											}
										}, {
											header : "操作说明",
											dataIndex : 'confirm_remark',
											flex : 2,
											menuDisabled : true,
											renderer: function(value, meta, record) {
		                                          meta.style = 'overflow:auto;padding: 3px 6px;text-overflow: ellipsis;white-space: nowrap;white-space:normal;line-height:20px;';   
		                                          return value;   
		                                     }
//											 renderer : function(value, cellmeta, record,
//														rowIndex, columnIndex, store) {
//													var str = value;
//													if (value.length > 150) {
//														str = value.substring(0, 150) + "...";
//													}
//													return "<span title='" + value + "'>" + str
//															+ "</span>";
//												}
										}]
							})]
		});
		win.show();
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.getStore().currentPage=1;
		me.getStore().load();
	}
});