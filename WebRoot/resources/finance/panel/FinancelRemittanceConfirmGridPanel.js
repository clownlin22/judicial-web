/**
 * 汇款单生成
 * 
 * @author yuanxiaobo
 */
var financeRemittance="";
Ext.define("Rds.finance.panel.FinancelRemittanceConfirmGridPanel",{
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
				data : [ [ '全部', '' ], [ '未确认', '-1' ],[ '确认通过', '1' ], [ '确认不通过', '2' ] ]
			}),
			value : '-1',
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
				data : [ [ '全部', '' ], [ '亲子鉴定', 1 ], [ '合同计划', 2 ], [ '无创产前', 3 ], [ '儿童基因库', 4 ] ]
			}),
			value : '',
			mode : 'local',
			name : 'daily_type',
		});
		var urgent_state = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '紧急情况',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
				fields : [ 'Name', 'Code' ],
				data : [ [ '普通', -1 ], [ '普通', 0 ], [ '紧急', 1 ] ]
			}),
			value : -1,
			mode : 'local',
			name : 'urgent_state',
		});
		var starttime=Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width : '18%',
			labelWidth : 70,
			fieldLabel : '汇款时间 从',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
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
		var itemsPerPage = 20;
		var combo;
		me.store = Ext.create('Ext.data.Store',{
			fields : ['remittance_id', 'remittance_num', 'remittance_date', 'remittance','remittance_account','daily_type','urgent_state',
						'arrival_account', 'remittance_per', 'remittance_per_name', 'remittance_remark','create_per','create_date',
						'create_per_name','confirm_per','confirm_per_name','confirm_date','confirm_remark','confirm_state','deductible','remittance_att'],
			start : 0,
            limit : itemsPerPage,
            pageSize : itemsPerPage,
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
            		financeRemittance="remittance_num="+trim(remittance_num.getValue())+"&remittance_per_name="+trim(remittance_per_name.getValue())
            						+"&remittance_account="+trim(remittance_account.getValue())+"&arrival_account="+trim(arrival_account.getValue())
            						+"&create_per_name="+trim(create_per_name.getValue())+"&starttime="+dateformat(starttime.getValue())
            						+"&endtime="+dateformat(endtime.getValue())+"&daily_type="+daily_type.getValue()
            						+"&confirm_state="+confirm_state.getValue()+"&urgent_state="+urgent_state.getValue()
            						+"&confirm_per_name="+trim(confirm_per_name.getValue());
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
						confirm_state:confirm_state.getValue(),
						urgent_state:urgent_state.getValue()
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
	              data: [['20', 20], ['40', 40],['60', 60], ['80', 80], ['100', 100],['200', 200],['400', 400]]
	          }),
	          valueField: 'value',
	          displayField: 'text',
	          emptyText:20,
	          width: 60
	      });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

	   //添加下拉显示条数菜单选中事件
	        combo.on("select", function (comboBox) {
	        var pagingToolbar=Ext.getCmp('pagingbarConfirm');
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
			id:'pagingbarConfirm',
			store: me.store,
			pageSize:me.pageSize,
			displayInfo: true,
           	displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录",
	   	 	items: ['-', '每页显示',combo,'条']
       });
		
//		me.bbar = Ext.create('Ext.PagingToolbar', {
//            store: me.store,
//            pageSize:me.pageSize,
//            displayInfo: true,
//            displayMsg : "第 {0} - {1} 条  共 {2} 条",
//	   	 	emptyMsg : "没有符合条件的记录"
//        });
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
								case '4':
									return "儿童基因库";
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
							text : '是否紧急',
							dataIndex : 'urgent_state',
							menuDisabled : true,
							width : 80,
							renderer : function(value,meta,record) {
								switch (value) {
								case 1:
									return "<div style=\"color: red;\">加急</div>";
									break;
								default:
									return "否";
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
	                  { text: '汇款账户', 	dataIndex: 'remittance_account', menuDisabled:true, width : 200},
	                  { text: '到款账户', 	dataIndex: 'arrival_account', menuDisabled:true, width : 200},
	                  { text: '汇款人', dataIndex: 'remittance_per_name', menuDisabled:true, width : 100},
	                  { text: '汇款备注', 	dataIndex: 'remittance_remark',menuDisabled:true, width : 350,
	                	  renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								var str = value;
								if (value.length > 150) {
									str = value.substring(0, 150) + "...";
								}
								return "<span title='" + value + "'>" + str
										+ "</span>";
							}
	                  },
	                  { text: '汇款创建人', 	dataIndex: 'create_per_name', menuDisabled:true,width : 100},
	                  { text: '汇款确认人', 	dataIndex: 'confirm_per_name', menuDisabled:true, width : 100},
	                  { text: '确认时间', 	dataIndex: 'confirm_date', menuDisabled:true,width : 150},
	                  { text: '确认备注', 	dataIndex: 'confirm_remark', menuDisabled:true,width : 200},
	                  { text: '汇款创建时间', 	dataIndex: 'create_date', menuDisabled:true,width : 150}
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
		 		items:[confirm_state,urgent_state,{
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
//			 			hidden:('subo_houl' == usercode)?false:true
		 			},{
			 			text:'抵扣汇款处理',
			 			iconCls:'Pageedit',
			 			handler:me.onDeductible,
			 			hidden:('subo_houl' == usercode)?false:true
		 			},{
			 			text:'详情查看',
			 			iconCls:'Find',
			 			handler:me.onCheck
			 		},{
			 			text:'汇款金额计算',
			 			iconCls:'User',
			 			handler:me.onCount
			 		},{
						text : '导出',
						iconCls : 'Pageexcel',
						handler : me.onCaseExport
					}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	onDeductible:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要处理的记录!");
			return;
		};
		if(selections[0].get("confirm_state")!='1')
		{
			Ext.Msg.alert("提示", "该汇款单状态不予操作!");
			return;
		}
		Ext.Msg.show({
			title : '提示',
			msg : '确定处理该汇款单下所有案例吗？',
			width : 300,
			buttons : Ext.Msg.OKCANCEL,
			fn : function(buttonId, text, opt) {
				if (buttonId == 'ok') {
					Ext.MessageBox.wait('正在操作','请稍后...');
					Ext.Ajax.request({
								url : "finance/casefinance/deductibleCaseInfo.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {remittance_id:selections[0].get("remittance_id")},
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response) {
										Ext.MessageBox.alert("提示信息","操作成功！");
										me.getStore().load();
									} else {
										Ext.MessageBox.alert("错误信息","操作失败，请稍后重试或联系管理员!");
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
	onCaseExport:function(){
		window.location.href = "finance/casefinance/exportCaseFinance.do?"+financeRemittance;
	},
	onCount:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length < 1){
			Ext.Msg.alert("提示", "请选择需要计算的记录!");
			return;
		}
		var sum=0;
		for(var i = 0 ; i < selections.length ; i ++)
		{
			sum += Number(selections[i].get("remittance"));
		}
		Ext.Msg.alert("提示", "总额："+sum);
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
			height : 350,
			layout : 'border',
			items : [ form ],
		});
		win.show();
		form.loadRecord(selections[0]);
	},
	onConfirm:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1 ){
			Ext.Msg.alert("提示", "请选择一条需要汇款的记录!");
			return;
		};
		var remittance_id='';
		var daily_type='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			daily_type = selections[i].get("daily_type");
			if(selections[i].get("confirm_state") == 1 || selections[i].get("confirm_state") == 2)
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
			values["status"]='1';
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
		canel = function(mei){
			remittanceConfirmform_add.close();
		}
		var remittanceConfirmform_add = Ext.create("Ext.window.Window", {
			title : '汇款信息',
			width : 470,
			height : 600,
			layout : 'border',
			modal:true,
			items : [{
				xtype : 'form',
				region : 'center',
				autoScroll : true,
				id:'financeAttachment',
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
							iconCls : 'Accept',
							handler : remittance_confirm
						}, {
							text : '确认不通过',
							iconCls : 'Cancel',
							handler : remittance_canel
						}, {
							text : '取消',
							handler : canel
						}],
				items : [{
					xtype : "textfield",
					fieldLabel : '汇款金额',
					labelAlign : 'right',
					labelWidth : 80,
					value:selections[0].get("remittance"),
					readOnly:true
				},{
					xtype : "textfield",
					fieldLabel : '汇款时间',
					labelAlign : 'right',
					value : selections[0].get("remittance_date"),
					labelWidth : 80,
					readOnly:true
				},{
					xtype : "textfield",
					fieldLabel : '汇款人员',
					labelAlign : 'right',
					labelWidth : 80,
					value:selections[0].get("remittance_per_name"),
					readOnly:true
				},{
					xtype : "textfield",
					fieldLabel : '汇款账户',
					labelAlign : 'right',
					labelWidth : 80,
					value:selections[0].get("remittance_account"),
					readOnly:true
				},{
					xtype : "textfield",
					fieldLabel : '到款账户',
					labelAlign : 'right',
					labelWidth : 80,
					value:selections[0].get("arrival_account"),
					readOnly:true
				},{
					xtype : "textarea",
					fieldLabel : '汇款备注',
					labelAlign : 'right',
					labelWidth : 80,
					value:selections[0].get("remittance_remark"),
					readOnly:true
				},{
					xtype : "textfield",
					fieldLabel : '是否抵扣',
					labelAlign : 'right',
					labelWidth : 80,
					value:(selections[0].get("deductible")=="1"?"是":"否"),
					readOnly:true
				},{
					xtype : "textarea",
					fieldLabel : '确认备注',
					labelAlign : 'right',
					maxLength : 100,
					labelWidth : 80,
					height:130,
					name : 'confirm_remark',
					value:selections[0].get("confirm_remark")
				}]
			}]
		});
		
		//添加样本信息
		Ext.Ajax.request({
			url : 'finance/casefinance/getFinanceAttachMent.do',
			method : "POST",
			async: false,
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : {
				'remittance_id' : selections[0].get("remittance_id")
			},
			success : function(response, options) {
				response = Ext.JSON.decode(response.responseText);
				for (var i = 0; i < response.length; i++) {
					Ext.getCmp("financeAttachment").add({
						xtype : 'form',
						labelAlign : "right",
//						bodyPadding : 10,
						defaults : {
							anchor : '100%',
							labelWidth : 80,
							labelSeparator : "：",
							labelAlign : 'right'
						},
						border : false,
						autoHeight : true,
						items : [{
			    			border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							style:'margin-top:5px;',
							items : [{
									xtype : "textfield",
									fieldLabel : '<span style="color:red">汇款凭证'+(Number(i)+Number(1))+'</span>',
									labelAlign : 'right',
									labelWidth : 80,
									value:response[i].attachment_path,
				    				columnWidth : .76,
									readOnly:true
								},{
									style:'margin-left:10px;padding-right:15px;',
								    xtype : 'button',
								    iconCls : 'previewIcon',
								    text : '附件查看'+i,
				    				columnWidth : .24,
				    				listeners :{  
				    			        click : function(p,e,eOpts ){  
				    			        console.log(response[p.getText().replace('附件查看','')].attachment_path);  
									if(""==response[p.getText().replace('附件查看','')].attachment_path || null==response[p.getText().replace('附件查看','')].attachment_path){
										Ext.Msg.alert("提示", "该汇款单未上传凭证");
										return;
									}else{
										var form = Ext.create("Rds.finance.form.FinanceImageShow", {
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
										finance_attachment=response[p.getText().replace('附件查看','')].attachment_path;
//										form.loadRecord(selections[0]);
										win.show();
									}

				    			        }  
				    			    }  
//								    handler : function() { 
//									}
								}]
						}]
					});
				}
			},
			failure : function() {
				return;
			}
		});
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
					fields : ['case_id', 'case_code', 'receiver_area', 'case_receiver','urgent_state',
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
								text : '是否紧急',
								dataIndex : 'urgent_state',
								width : 100,
								menuDisabled : true,
								renderer : function(value,meta,record) {
									switch (value) {
									case 1:
										return "<div style=\"color: red;\">加急</div>";
										break;
									default:
										return "否";
									}
								}
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
					width : 850,
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