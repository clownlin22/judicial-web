/**
 * 汇款单生成
 * 
 * @author yuanxiaobo
 */
casefee_canel = function(me) {
	me.up("window").close();
}
var caseFinance="";
Ext.define("Rds.finance.panel.FinancelRemittanceCheckGridPanel",{
	extend:"Ext.grid.Panel",
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var case_code = Ext.create('Ext.form.field.Text',{
            	name:'case_code',
            	labelWidth:60,
            	width:'20%',
            	fieldLabel:'案例编号'
            });
		var remittance_num = Ext.create('Ext.form.field.Text',{
        	name:'remittance_num',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'汇款单号'
        });
		var client = Ext.create('Ext.form.field.Text',{
        	name:'client',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'姓名'
        });
		var case_receiver = Ext.create('Ext.form.field.Text',{
        	name:'case_receiver',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'归属人'
        });
		var starttime=Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '日报时间从',
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
			width : '20%',
			labelWidth : 20,
			fieldLabel : '到',
			labelAlign : 'right',
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
		var starttime_accept=Ext.create('Ext.form.DateField', {
			name : 'starttime_accept',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '受理时间从',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date()
		});
		var endtime_accept=Ext.create('Ext.form.DateField', {
			name : 'endtime_accept',
			width : '20%',
			labelWidth : 20,
			fieldLabel : '到',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var starttime_client=Ext.create('Ext.form.DateField', {
			name : 'starttime_client',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '委托时间从',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date()
		});
		var endtime_client=Ext.create('Ext.form.DateField', {
			name : 'endtime_client',
			width : '20%',
			labelWidth : 20,
			fieldLabel : '到',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var receiver_area = Ext.create('Ext.form.field.Text', {
			name : 'receiver_area',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '归属地'
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
		var confirm_date_start=Ext.create('Ext.form.DateField', {
			name : 'confirm_date_start',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '确认时间从',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date()
		});
		var confirm_date_end=Ext.create('Ext.form.DateField', {
			name : 'confirm_date_end',
			width : '20%',
			labelWidth : 20,
			fieldLabel : '到',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date()
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
			value : -1,
			mode : 'local',
			name : 'status',
		});
		var remittance_type = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '汇款状态',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
				fields : [ 'Name', 'Code' ],
				data : [ [ '全部', -1 ], [ '已汇款', 0 ], [ '未汇款', 1 ] ]
			}),
			value : -1,
			mode : 'local',
			name : 'remittance_type',
		});
		var settlement_type = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '结算类型',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
				fields : [ 'Name', 'Code' ],
				data : [ [ '全部', -1 ], [ '日结', 0 ], [ '月结', 1 ] ]
			}),
			value : -1,
			mode : 'local',
			name : 'settlement_type',
		});
		  var case_state = Ext.create('Ext.form.ComboBox', {
				fieldLabel : '案例状态',
				width : '20%',
				labelWidth : 60,
				editable : false,
				triggerAction : 'all',
				displayField : 'Name',
				valueField : 'Code',
				store : new Ext.data.ArrayStore(
						{fields : ['Name','Code' ],
							data : [
									['全部','' ],
									['正常','0' ],
									['为先出报告后付款',1],
									['免单',2],
									['优惠',3],
									['月结',4],
									['二次采样',5],
									['补样',6]
									]
						}),
				value : '',
				mode : 'local',
				name : 'case_state',
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
				data : [ [ '亲子鉴定', 1 ], [ '合同计划', 2 ] , [ '无创产前', 3 ], [ '儿童基因库', 4 ]  ]
			}),
			value : 1,
			mode : 'local',
			name : 'daily_type',
			 listeners :{
             	"select" : function(combo, record, index){
             		  if(combo.getValue()=="2"){
             			 case_state.setDisabled(true);
             			 settlement_type.setDisabled(true);
             		  }else{
             			 case_state.setDisabled(false);
             			 settlement_type.setDisabled(false);
             		  }
             	}
             }
		});
		var itemsPerPage = 20;
		var combo;
		me.store = Ext.create('Ext.data.Store',{
			fields : ['fee_id','case_id', 'case_code', 'urgent_state','receiver_area', 'case_receiver','client','remittance_num','case_state',
						'username', 'stand_sum', 'real_sum', 'return_sum','discountPrice','sample_str','daily_type','confirm_date',
						'date','type','status','daily_time','confirm_state','remittance_id','finance_remark','remittance_att'],
				start : 0,
	            limit : itemsPerPage,
	            pageSize : itemsPerPage,
				proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url : 'judicial/finance/queryDailyDetail.do',
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
            		caseFinance="case_code="+trim(case_code.getValue())+"&starttime="+dateformat(starttime.getValue())
            					+"&endtime="+dateformat(endtime.getValue())+"&starttime_accept="+dateformat(starttime_accept.getValue())
            					+"&endtime_accept="+dateformat(endtime_accept.getValue())+"&starttime_client="+dateformat(starttime_client.getValue())
            					+"&endtime_client="+dateformat(endtime_client.getValue())+"&confirm_state="+confirm_state.getValue()
            					+"&remittance_type="+remittance_type.getValue()+"&settlement_type="+settlement_type.getValue()
            					+"&remittance_num="+trim(remittance_num.getValue())+"&client="+trim(client.getValue())
            					+"&daily_type="+daily_type.getValue()+"&case_state="+case_state.getValue()
            					+"&case_receiver="+trim(case_receiver.getValue())+"&receiver_area="+trim(receiver_area.getValue())
            					+"&confirm_date_start="+dateformat(confirm_date_start.getValue())+"&confirm_date_end="+dateformat(confirm_date_end.getValue());
        			Ext.apply(me.store.proxy.params, {
        						case_code:trim(case_code.getValue()),
        						starttime : dateformat(starttime.getValue()),
								endtime : dateformat(endtime.getValue()),
								starttime_accept:dateformat(starttime_accept.getValue()),
								endtime_accept : dateformat(endtime_accept.getValue()),
								starttime_client:dateformat(starttime_client.getValue()),
								endtime_client : dateformat(endtime_client.getValue()),
//								status:status.getValue(),
								confirm_state:confirm_state.getValue(),
								remittance_type:remittance_type.getValue(),
								settlement_type:settlement_type.getValue(),
								remittance_num:trim(remittance_num.getValue()),
								client:trim(client.getValue()),
								daily_type:daily_type.getValue(),
								case_state:case_state.getValue(),
								case_receiver:trim(case_receiver.getValue()),
								receiver_area:trim(receiver_area.getValue()),
								confirm_date_start:dateformat(confirm_date_start.getValue()),
								confirm_date_end:dateformat(confirm_date_end.getValue()),
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
	              data: [['20', 20], ['40', 40],['60', 60], ['80', 80], ['100', 100]]
	          }),
	          valueField: 'value',
	          displayField: 'text',
	          emptyText:20,
	          width: 50
	      });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

	   //添加下拉显示条数菜单选中事件
	        combo.on("select", function (comboBox) {
	        var pagingToolbar=Ext.getCmp('pagingbarCheck');
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
			id:'pagingbarCheck',
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录",
	   	 	items: ['-', '每页显示',combo,'条']
        });
		me.columns = [Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'4%', menuDisabled:true}),
	                  { text: '案例编码',	dataIndex: 'case_code',menuDisabled:true, width : 120,
	                	  renderer : function(value, cellmeta,record, rowIndex, columnIndex,store) {
								var status= record.data["status"];
								var confirm_state=record.data["confirm_state"];
								var daily_type = record.data["daily_type"];
								if(daily_type==1 || daily_type==3 || daily_type==4)
								{
									if (status == 0) {
										return "<div style=\"color: green;\">"
												+ value + "</div>"
									} else if(confirm_state == 2)
									{
										return "<div style=\"color: red;\">"
										+ value + "</div>"
									}else
									{
										return value;
									}
								}else
								{
									if (status == 4) {
										return "<div style=\"color: red;\">"
												+ value + "</div>"
									} else
									{
										return value;
									}
								}
								
	
							}
			          },
			          {
							text : '是否紧急',
							dataIndex : 'urgent_state',
							width : 100,
							menuDisabled : true,
							renderer : function(value,meta,record) {
								switch (value) {
								case 1:case 2:case 3:
									return "<div style=\"color: red;\">加急</div>";
									break;
								default:
									return "否";
								}
							}
						},
			          { text: '汇款单号',	dataIndex: 'remittance_num', menuDisabled:true, width : 150},
			          { text: '日报时间',	dataIndex: 'daily_time', menuDisabled:true, width : 100},
	                  { text: '受理时间',	dataIndex: 'date', menuDisabled:true, width : 100},
	                  { text: '案例归属人', 	dataIndex: 'case_receiver', menuDisabled:true, width : 120},
	                  { text: '案例归属地', 	dataIndex: 'receiver_area', menuDisabled:true, width : 200},
	                  { text: '案例委托人', 	dataIndex: 'client', menuDisabled:true, width : 120},
	                  { text: '标准金额', 	dataIndex: 'stand_sum',menuDisabled:true, width : 100,
	                	  renderer : function(value, cellmeta,record, rowIndex, columnIndex,store) {
										return "<div style=\"color: red;\">"
												+ value + "</div>"
							}
	                  },
	                  { text: '优惠金额', 	dataIndex: 'discountPrice', menuDisabled:true,width : 100},
	                  { text: '应收金额', 	dataIndex: 'real_sum', menuDisabled:true, width : 100,
	                	  renderer : function(value, cellmeta,record, rowIndex, columnIndex,store) {
								return "<div style=\"color: red;\">"
										+ value + "</div>"
	                	  }
	                  },
	                  { text: '回款金额', 	dataIndex: 'return_sum', menuDisabled:true,width : 100},
	                  { text: '财务备注', 	dataIndex: 'finance_remark', menuDisabled:true,width : 100},
	                  { text: '样本信息', 	dataIndex: 'sample_str', menuDisabled:true,width : 200},
	                  {
							text : '确认状态',
							dataIndex : 'confirm_state',
							menuDisabled : true,
							width : 100,
							renderer : function(value,meta,record) {
								var daily_type = record.data["daily_type"];
								var status = record.data["status"];
								if(daily_type==1){
									switch (value) {
									case -1:
										return "未确认汇款";
										break;
									case 1:
										return "确认汇款通过";
										break;
									case 2:
										return "确认汇款不通过";
										break;
									default:
										return "未回款";
									}
								}else
								{
									switch (value) {
									case -1:
										return "未确认汇款";
										break;
									case 1:
										return "确认汇款通过";
										break;
									case 2:
										return "确认汇款不通过";
										break;
									default:
										return "";
									}
								}
								
							}
						},
		                { text: '确认时间',	dataIndex: 'confirm_date', menuDisabled:true, width : 150},
						{
							text : '结算类型',
							dataIndex : 'type',
							menuDisabled : true,
							width : 100,
							renderer : function(value,meta,record) {
								switch (value) {
								case 4:
									return "月结";
									break;
								default:
									return "日结";
								}
							}
						},
						{
							text : '日报类型',
							dataIndex : 'daily_type',
							menuDisabled : true,
							width : 100,
							renderer : function(value,meta,record) {
								switch (value) {
								case 1:
									return "亲子鉴定";
									break;
								case 2:
									return "合同计划";
									break;
								case 3:
									return "无创产前";
									break;
								case 4:
									return "儿童基因库";
									break;
								default:
									return "亲子鉴定";
								}
							}
						},
						{
							text : '案例状态',
							dataIndex : 'case_state',
							menuDisabled : true,
							width : 150,
							renderer : function(value,meta,record) {
								switch (value) {
								case 0:
									return "正常";
									break;
								case 1:
									return "为先出报告后付款";
									break;
								case 2:
									return "免单";
									break;
								case 3:
									return "优惠";
									break;
								case 4:
									return "月结";
									break;
								case 5:
									return "二次采样";
									break;
								case 6:
									return "补样";
									break;
								case 7:
									return "加报告";
									break;
								case 8:
									return "改报告";
									break;
								default:
									return "";
								}
							}
						}
		              ];
		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[case_code,case_receiver,remittance_num,remittance_type,settlement_type]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[daily_type,client,starttime,endtime,case_state]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[starttime_accept,endtime_accept,starttime_client,endtime_client,receiver_area]
	 	
	 	},{

	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[confirm_state,confirm_date_start,confirm_date_end,{
            	text:'查询',
            	iconCls:'Find',
            	handler:me.onSearch
            }]
	 	
	 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[
	 		{
	 			text:'详情查看',
	 			iconCls:'Find',
	 			handler:me.onCheck
	 		},{
	 			text:'汇款凭证查看',
	 			iconCls:'Find',
	 			handler:me.onCheckFinanceAttachment
	 		},{
				text : '导出',
				iconCls : 'Pageexcel',
				handler : me.onCaseExport
			}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	onCaseExport:function(){
		window.location.href = "judicial/finance/exportCaseFinance.do?"+caseFinance;
	},
	onCheckFinanceAttachment:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		};
		if(selections[0].get("delete") == '2')
		{
			Ext.Msg.alert("提示", "该记录已作废!");
			return;
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
//													if (value.length > 15) {
//														str = value.substring(0, 15) + "...";
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