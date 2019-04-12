/**
 * 汇款单生成
 * 
 * @author yuanxiaobo
 */
casefee_canel = function(me) {
	me.up("window").close();
}
var caseFinance="";
Ext.define("Rds.finance.panel.FinancelRemittanceGridPanel",{
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
	        var pagingToolbar=Ext.getCmp('pagingbar');
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
			id:'pagingbar',
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
									if(confirm_state==1){
										return "<div style=\"color: green;\">"
										+ value + "</div>"
									}
									else if (confirm_state == 2) {
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
	 		items:[{
	 			text:'汇款',
	 			iconCls:'Add',
	 			handler:me.onUpdate
	 		},
	 		{
	 			text:'详情查看',
	 			iconCls:'Find',
	 			handler:me.onCheck
	 		},{
	 			text:'汇款凭证查看',
	 			iconCls:'Find',
	 			handler:me.onCheckFinanceAttachment
	 		},{
	 			text:'补款',
	 			iconCls:'Add',
	 			handler:me.onFillCasefee
	 		},{
	 			text:'修改',
	 			iconCls:'Pageedit',
	 			handler:me.onCasefeeEdit
	 		},{
	 			text:'退款',
	 			iconCls:'Arrowleft',
	 			handler:me.onCasefeeBack
	 		},{
				text : '导出',
				iconCls : 'Pageexcel',
				handler : me.onCaseExport
			},{
	 			text:'删除',
	 			iconCls:'Delete',
	 			handler:me.onCasefeeDel,
	 			hidden:true
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
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
	onCaseExport:function(){
		window.location.href = "judicial/finance/exportCaseFinance.do?"+caseFinance;
	},
	onCasefeeEdit:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
			return;
		}
		if(0 != selections[0].get("confirm_state")){
			Ext.Msg.alert("提示", "该案例状态不予修改!");
			return;
		}
		if(2 == selections[0].get("daily_type")){
			Ext.Msg.alert("提示", "合同类型请前往合同计划里修改!");
			return;
		}
		casefeeEdit_save = function(mei) {
			var form = mei.up("form").getForm();
			var grid = mei.up("gridpanel");
			var values1 = form.getValues();
			var values = {
					fee_id : selections[0].get("fee_id"),
					real_sum : values1["real_sum"]
				};
			if (form.isValid()) {
				Ext.Msg.show({
					title : '提示',
					msg : '请核实确定修改?',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							Ext.MessageBox.wait('正在操作','请稍后...');
							Ext.Ajax.request({
								url : "finance/casefinance/updateCaseFeeByRegister.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : values,
								success : function(response,options) {
									response = Ext.JSON.decode(response.responseText);
									if (response.result) {
										Ext.MessageBox.alert("提示信息",response.message);
										me.getStore().load();
										csefeeEdit_add.close();
									} else {
										Ext.MessageBox.alert("错误信息",response.message);
									}
								},
								failure : function() {
									Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
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
		var csefeeEdit_add = Ext.create("Ext.window.Window", {
			title : '修改应收',
			width : 350,
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
							text : '保存',
							iconCls : 'Disk',
							handler : casefeeEdit_save
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : casefee_canel
						}],
				items : [{
					xtype : 'tbtext',
					style:'color:red',
					text : '注意：只能修改应收金额，汇款时请说明修改原因！'
				},{
					xtype : "numberfield",
					fieldLabel : '应收金额<span style="color:red">*</span>',
					labelAlign : 'right',
					maxLength : 50,
					style:'margin-top:20px;',
					labelWidth : 80,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					name : 'real_sum',
					value:selections[0].get("real_sum"),
					minValue:0
				}]
			}]
		})
		csefeeEdit_add.show();
	},
	onCasefeeDel:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要删除的记录!");
			return;
		}
	},
	onCasefeeBack:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要退款的记录!");
			return;
		}
		casefeeBack_save = function(mei) {
			var form = mei.up("form").getForm();
			var grid = mei.up("gridpanel");
			var values1 = form.getValues();
			var finance_type = selections[0].get("daily_type")=="1"?"亲子鉴定":(selections[0].get("daily_type")=="2"?"合同计划":"无创产前");
			var case_type = selections[0].get("daily_type")=="1"?"dna_refund":(selections[0].get("daily_type")=="2"?"contract_refund":"inversive_refund");
			var values = {
					case_id : selections[0].get("case_id"),
					type:0,
					finance_type:finance_type,
					case_type:case_type,
					finance_remark:values1["fill_finance_remark"],
					stand_sum:"-"+values1["fill_stand_sum"],
					real_sum:"-"+values1["fill_stand_sum"],
					return_sum:0,
					discountPrice:0,
					confirm_code:values1["confirm_code"]
				};
			if (form.isValid()) {
				Ext.Msg.show({
					title : '提示',
					msg : '请核实确定插入?',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							Ext.MessageBox.wait('正在操作','请稍后...');
							Ext.Ajax.request({
								url : "finance/casefinance/backCasefee.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : values,
								success : function(response,options) {
									response = Ext.JSON.decode(response.responseText);
									if (response.result) {
										Ext.MessageBox.alert("提示信息",response.message);
										csefeeBack_add.close();
									} else {
										Ext.MessageBox.alert("错误信息",response.message);
									}
								},
								failure : function() {
									Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
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
		
		var csefeeBack_add = Ext.create("Ext.window.Window", {
			title : '退款信息',
			width : 400,
			height : 350,
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
							text : '保存',
							iconCls : 'Disk',
							handler : casefeeBack_save
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : casefee_canel
						}],
				items : [{
					xtype : 'tbtext',
					style:'color:red',
					text : '注意：输入退款码后会自动生成退款金额！'
				},{
					xtype : "textfield",
					fieldLabel : '退款码<span style="color:red">*</span>',
					labelAlign : 'right',
					maxLength : 50,
					labelWidth : 80,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					name : 'confirm_code',
					style:'margin-top:10px;',
					listeners:{
				        blur: function(combo, record, index){
				        	var confirm_code= trim(combo.getValue());
				        	Ext.Ajax.request({
								url : "finance/casefinance/confirmCodeBack.do",
								method : "POST",
								async : false,
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {
									confirm_code : confirm_code
								},
								success : function(response,options) {
									response = Ext.JSON.decode(response.responseText);
									if (!response.result) {
										Ext.Msg.alert("提示",response.message);
									}else
									{
										Ext.getCmp("fill_stand_sum").setValue(response.discount_amount);
									}
								},
								failure : function() {
									Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
								}
							});
				        }
				    }
				},{
					xtype : "numberfield",
					fieldLabel : '退款金额<span style="color:red">*</span>',
					labelAlign : 'right',
					maxLength : 50,
					labelWidth : 80,
					readOnly:true,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					name : 'fill_stand_sum',
					id:'fill_stand_sum',
					minValue:0
				},{
					xtype : "textarea",
					fieldLabel : '退款备注<span style="color:red">*</span>',
					labelAlign : 'right',
					maxLength : 100,
					labelWidth : 80,
					height:150,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					name : 'fill_finance_remark'
				}]
			}]
		})
		csefeeBack_add.show();
	
	
	},
	onFillCasefee:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要补款的记录!");
			return;
		}
		

		fillCasefee_save = function(mei) {
			var form = mei.up("form").getForm();
			var grid = mei.up("gridpanel");
			var values1 = form.getValues();
			var finance_type = selections[0].get("daily_type")=="1"?"亲子鉴定":(selections[0].get("daily_type")=="2"?"合同计划":"无创产前");
			var case_type = selections[0].get("daily_type")=="1"?"dna_repair":(selections[0].get("daily_type")=="2"?"contract_repair":"inversive_repair");
			var values = {
					case_id : selections[0].get("case_id"),
					type:0,
					finance_type:finance_type,
					case_type:case_type,
					finance_remark:values1["fill_finance_remark"],
					stand_sum:values1["fill_stand_sum"],
					real_sum:values1["fill_stand_sum"],
					return_sum:0,
					discountPrice:0
				};
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
					url : "judicial/register/fillReport.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : values,
					success : function(response,options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.result == true) {
							Ext.MessageBox.alert("提示信息","补款成功，请根据案例编号汇款！");
							fillCasefee_add.close();
						} else {
							Ext.MessageBox.alert("错误信息","操作失败，请联系管理员！");
						}
					},
					failure : function() {
						Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
					}
				});
			}
		}
		
		var fillCasefee_add = Ext.create("Ext.window.Window", {
			title : '补款信息',
			width : 400,
			height : 325,
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
							text : '保存',
							iconCls : 'Disk',
							handler : fillCasefee_save
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : casefee_canel
						}],
				items : [{
					xtype : "numberfield",
					fieldLabel : '补款金额<span style="color:red">*</span>',
					labelAlign : 'right',
					maxLength : 18,
					labelWidth : 80,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					name : 'fill_stand_sum',
					minValue:0
				},{
					xtype : "textarea",
					fieldLabel : '补款备注<span style="color:red">*</span>',
					labelAlign : 'right',
					maxLength : 100,
					labelWidth : 80,
					height:150,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					name : 'fill_finance_remark'
				}]
			}]
		})
		fillCasefee_add.show();
	
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
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要汇款的记录!");
			return;
		};
		var fee_id='';
		var real_sum = 0;
		var daily_type='';
		var urgent_state=0;
		for(var i = 0 ; i < selections.length ; i ++)
		{
			daily_type = selections[i].get("daily_type");
			
			if(daily_type == 2)
			{
				if( selections[i].get("return_sum") > 0 || selections[i].get("status") == 3 || selections[i].get("status") == 4 )
				{
					Ext.Msg.alert("提示", "存在已汇款案例!");
					return;
				}
				
			}else
			{
				if( selections[i].get("return_sum") > 0 || selections[i].get("status") == 0 )
				{
					Ext.Msg.alert("提示", "存在已汇款案例!");
					return;
				}
			}
			real_sum += selections[i].get("real_sum");
			if((i+1)==selections.length)
				fee_id +=  selections[i].get("fee_id");
			else
				fee_id += selections[i].get("fee_id")+",";
			urgent_state += selections[i].get("urgent_state")
		}
		remittance_save = function(mei) {
			var form = mei.up("form").getForm();
			var grid = mei.up("gridpanel");
			var values = form.getValues();
			values["fee_id"]=fee_id;
			values["daily_type"]=daily_type;
			if(urgent_state>0)
				urgent_state=1;
			if (form.isValid()) {
				if(values["remittance"]<0){
					Ext.Msg.show({
						title : '提示',
						msg : '该回款金额小于0！如确认不是优惠码问题，请继续操作，否则请联系管理员！后期此类问题一律不处理！',
						width : 300,
						buttons : Ext.Msg.OKCANCEL,
						fn : function(buttonId, text, opt) {
							if (buttonId == 'ok') {
								Ext.MessageBox.wait('正在操作','请稍后...');
								form.submit({
									url : 'finance/casefinance/insertRemittanceRecord.do',
									method : 'post',
									waitMsg : '正在上传您的文件...',
									params:{
										'fee_id':fee_id,
										'daily_type':daily_type,
										'urgent_state':urgent_state
									},
									success : function(form, action) {
										response = Ext.JSON.decode(action.response.responseText);
										if (response) {
											Ext.MessageBox.alert("提示信息", "汇款单生成成功！");
											me.getStore().load();
											remittanceform_add.close();
										} else {
											Ext.MessageBox.alert("错误信息", "生成错误，请联系管理员！");
										}
									},
									failure : function() {
										Ext.Msg.alert("提示", "上传失败，请联系管理员!");
//										myWindow.close();
									}
								});
							} else {
								return;
							}

						},
						animateTarget : 'addAddressBtn',
						icon : Ext.window.MessageBox.INFO
					})
				}else{
					Ext.MessageBox.wait('正在操作','请稍后...');
					form.submit({
						url : 'finance/casefinance/insertRemittanceRecord.do',
						method : 'post',
						waitMsg : '正在上传您的文件...',
						params:{
							'fee_id':fee_id,
							'daily_type':daily_type,
							'urgent_state':urgent_state
						},
						success : function(form, action) {
							response = Ext.JSON.decode(action.response.responseText);
							if (response) {
								Ext.MessageBox.alert("提示信息", "汇款单生成成功！");
								me.getStore().load();
								remittanceform_add.close();
							} else {
								Ext.MessageBox.alert("错误信息", "生成错误，请联系管理员！");
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "上传失败，请联系管理员!");
//							myWindow.close();
						}
					});
					
//					Ext.Ajax.request({
//								url : "finance/casefinance/insertRemittanceRecord.do",
//								method : "POST",
//								headers : {
//									'Content-Type' : 'application/json'
//								},
//								jsonData : values,
//								success : function(response, options) {
//									response = Ext.JSON
//											.decode(response.responseText);
//									if (response) {
//										Ext.MessageBox.alert("提示信息", "汇款单生成成功！");
//										me.getStore().load();
//										remittanceform_add.close();
//									} else {
//										Ext.MessageBox.alert("错误信息", "生成错误，请联系管理员！");
//									}
//								},
//								failure : function() {
//									Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
//								}
//							});
				}
			}
		}
		
		var remittanceform_add = Ext.create("Ext.window.Window", {
			title : '汇款信息',
			width : 600,
			height : 500,
			layout : 'border',
			modal:true,
			items : [{
				xtype : 'form',
				region : 'center',
				style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
				labelAlign : "right",
				bodyPadding : 10,
				autoScroll : true,
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
							handler : remittance_save
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : casefee_canel
						}],
				items : [{
					xtype : "textfield",
					fieldLabel : '汇款金额<span style="color:red">*</span>',
					labelAlign : 'right',
					maxLength : 18,
					labelWidth : 80,
					value:real_sum,
					name : 'remittance',
					readOnly:true
				},{
					xtype : "datefield",
					fieldLabel : '汇款时间<span style="color:red">*</span>',
					labelAlign : 'right',
					format : 'Y-m-d',
					value : Ext.Date.add(
							new Date(),
							Ext.Date.DAY),
					maxValue:Ext.Date.add(
							new Date(),
							Ext.Date.DAY),
					labelWidth : 80,
					name : 'remittance_date',
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
				},{
					xtype : 'combo',
					fieldLabel : '汇款人<span style="color:red">*</span>',
					labelWidth : 80,
					name : 'remittance_per',
					emptyText:'(人员首字母)：如小明(xm)',
					store :Ext.create("Ext.data.Store",{
						   fields:[
								{name:'key',mapping:'key',type:'string'},
								{name:'value',mapping:'value',type:'string'}
				          ],
						pageSize : 20,
						autoLoad: false,
						proxy : {
							type : "ajax",
							url:'judicial/dicvalues/getUsersId.do',
							reader : {
								type : "json"
							}
						}
					}),
					displayField : 'value',
					valueField : 'key',
					typeAhead : false,
					allowBlank:false, //不允许为空
					hideTrigger : true,
					forceSelection: true,
					minChars : 2,
					matchFieldWidth : true,
					listConfig : {
						loadingText : '正在查找...',
						emptyText : '没有找到匹配的数据'
					}
				},{
					xtype : "textfield",
					fieldLabel : '汇款账户<span style="color:red">*</span>',
					labelAlign : 'right',
					maxLength : 50,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					labelWidth : 80,
					name : 'remittance_account'
				},
				Ext.create('Ext.form.ComboBox', {
					fieldLabel : '到款账户<span style="color:red">*</span>',
	                labelWidth : 80,
	                labelAlign : 'right',
	                triggerAction: 'all',
					forceSelection: true,
         	        editable:false,  
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
	                displayField: 'Name',
	                valueField: 'Code',
	                name : 'arrival_account',
	                store: new Ext.data.ArrayStore({
	                    fields: ['Name', 'Code'],
	                    data: [['苏博支付宝', '苏博支付宝'], ['苏博邮储', '苏博邮储'],
	                           ['苏博股份浦发', '苏博股份浦发'], ['苏博股份现金', '苏博股份现金'],
	                           ['南有中行','南有中行'],['南有支付宝','南有支付宝'],
	                           ['医云南京银行','医云南京银行'], ['医云支付宝','医云支付宝'],
	                           ['子渊民丰','子渊民丰'],['子渊农行','子渊农行'],
	                           ['子渊现金','子渊现金'],['陈云农行','陈云农行'],
	                           ['佟朔工行','佟朔工行'],['孙利华建行','孙利华建行'],
	                           ['三和浦发','三和浦发'],['三和建行','三和建行'],
	                           ['正泰浦发','正泰浦发'],['毕节鉴定所','毕节鉴定所'],
	                           ['永建农行','永建农行'],['检测技术南京银行','检测技术南京银行'],['严权超农行','严权超农行'],
	                           ['广西正廉司法鉴定所支付宝','广西正廉司法鉴定所支付宝'],['广西正廉司法鉴定所建行','广西正廉司法鉴定所建行'],
	                           ['南京吉量生物科技有限公司高新开发区支行','南京吉量生物科技有限公司高新开发区支行'],['刘勇工行','刘勇工行']]
	                })          ,
	                mode: 'local',
				}),
//				{
//					xtype : "textfield",
//					fieldLabel : '到款账户<span style="color:red">*</span>',
//					labelAlign : 'right',
//					maxLength : 50,
//					allowBlank : false,// 不允许为空
//					blankText : "不能为空",// 错误提示信息，默认为This field is required!
//					labelWidth : 80,
//					name : 'arrival_account'
//				}
				,{
					xtype : "textarea",
					fieldLabel : '汇款备注',
					labelAlign : 'right',
					maxLength : 200,
					labelWidth : 80,
					name : 'remittance_remark'
				},{
                    xtype: 'combo',
                    autoSelect : true,
                    allowBlank:false,
            		fieldLabel: '是否抵扣<span style="color:red">*</span>',
            		labelWidth : 80,
            		emptyText : "请选择",
            		triggerAction: 'all',
         	        queryMode: 'local',
         	        selectOnTab: true,
         	        editable:false,  
         	        forceSelection: true,
         	        store : new Ext.data.ArrayStore({
						fields : ['Name', 'Code'],
						data : [['是', '1'], ['否', '2']]
					}),
                    displayField: 'Name',
                    valueField: 'Code',
                    value:'2',
                    name:'deductible',
				},{
					xtype : 'filefield',
					name : 'files',
					fieldLabel : '汇款凭证<span style="color:red">*</span>',
					msgTarget : 'side',
					allowBlank : false,
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
				},{

					xtype : 'panel',
					layout : 'absolute',
					border : false,
					items : [{
						xtype : 'button',
						text : '增加文件',
						width : 100,
						x : 0,
						handler : function() {
							var me = this.up('form');
							me.add({
								xtype : 'form',
								style : 'margin-top:5px;',
								layout : 'column',
								border : false,
								items : [{
											xtype : 'filefield',
											name : 'files',
											columnWidth : .9,
											fieldLabel : '汇款凭证<span style="color:red">*</span>',
											labelWidth : 70,
											msgTarget : 'side',
											allowBlank : false,
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
										},  {
											xtype : 'button',
											style : 'margin-left:15px;',
											text : '删除',
											handler : function() {
												var me = this.up("form");
												me.disable(true);
												me.up("form").remove(me);
											}
										}]
							});
						}
					}]

				
				}]
			}]
		})
		remittanceform_add.show();
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.getStore().currentPage=1;
		me.getStore().load();
	}
});