/**
 * 财务登记至确认消耗时间统计
 * 
 * @author wind
 */
casefee_canel = function(me) {
	me.up("window").close();
}
var consumptionTime=""
Ext.define("Rds.statistic.panel.StatisticRemittanceConsumptionTimeGirdPanel",{
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
		
		var time_spent = Ext.create('Ext.form.field.Text',{
        	name:'client',
        	labelWidth:165,
        	emptyText : '大于？小时',
        	width:'30%',
        	fieldLabel:'财务登记至确认耗时(小时)'
        });
		var case_receiver = Ext.create('Ext.form.field.Text',{
        	name:'case_receiver',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'归属人'
        });
//
//		var starttime_accept=Ext.create('Ext.form.DateField', {
//			name : 'starttime_accept',
//			width : '20%',
//			labelWidth : 70,
//			fieldLabel : '受理时间从',
//			emptyText : '请选择日期',
//			format : 'Y-m-d',
//			maxValue : new Date()
//		});
//		var endtime_accept=Ext.create('Ext.form.DateField', {
//			name : 'endtime_accept',
//			width : '20%',
//			labelWidth : 20,
//			fieldLabel : '到',
//			labelAlign : 'right',
//			emptyText : '请选择日期',
//			format : 'Y-m-d',
//			maxValue : new Date(),
//			value : Ext.Date.add(new Date(), Ext.Date.DAY)
//		});
		var starttime_accept = new Ext.form.DateField({
			id:'starttime_accept',
			name : 'starttime_accept',
			width:'20%',
			fieldLabel : '日 期  从',
			labelWidth : 70,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('starttime_accept').getValue();
		            var endDate = Ext.getCmp('endtime_accept').getValue();
		            if (start > endDate) {
		                Ext.getCmp('starttime_accept').setValue(endDate);
		            }
				}
			}
		});
		var endtime_accept = new Ext.form.DateField({
			id:'endtime_accept',
			name : 'endtime_accept',
			width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('starttime_accept').getValue();
		            var endDate = Ext.getCmp('endtime_accept').getValue();
		            if (start > endDate) {
		                Ext.getCmp('starttime_accept').setValue(endDate);
		            }
				}
			}
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
				data : [ [ '全部', '' ], [ '未确认', -1 ],[ '确认通过', 1 ] ]
			}),
			value : '',
			mode : 'local',
			name : 'confirm_state',
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
			readOnly:true,
			store : new Ext.data.ArrayStore({
				fields : [ 'Name', 'Code' ],
				data : [ [ '亲子鉴定', 1 ], [ '合同计划', 2 ] , [ '无创产前', 3 ] ]
			}),
			value : 1,
			mode : 'local',
			name : 'daily_type',
			 listeners :{
             	"select" : function(combo, record, index){
             		  if(combo.getValue()=="2"){
             			 case_state.setDisabled(true);
             		  }else{
             			 case_state.setDisabled(false);
             		  }
             	}
             }
		});
		var itemsPerPage = 20;
		var combo;
		me.store = Ext.create('Ext.data.Store',{
			fields : ['fee_id','case_id', 'case_code',  'case_receiver','remittance_num','case_state',
						'date','confirm_state','remittance_id','create_date','create_per','confirm_date','confirm_per','time_spent'],
				start : 0,
	            limit : itemsPerPage,
	            pageSize : itemsPerPage,
				proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url : 'statistics/Consumption/queryConsumptionTimeAll.do',
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
            		consumptionTime="case_code="+case_code.getValue().trim()+
					'&starttime_accept='+dateformat(starttime_accept.getValue())+
					'&endtime_accept='+dateformat(endtime_accept.getValue())+
					'&confirm_state='+confirm_state.getValue()+
					'&remittance_num='+trim(remittance_num.getValue())+
					'&case_receiver='+trim(case_receiver.getValue())+
					'&case_state='+case_state.getValue()+
					'&time_spent='+trim(time_spent.getValue());
        			Ext.apply(me.store.proxy.params, {
        						case_code:trim(case_code.getValue()),
								starttime_accept:dateformat(starttime_accept.getValue()),
								endtime_accept : dateformat(endtime_accept.getValue()),
								case_state:case_state.getValue(),
								confirm_state:confirm_state.getValue(),
								remittance_num:trim(remittance_num.getValue()),
								case_receiver:trim(case_receiver.getValue()),
								time_spent:trim(time_spent.getValue())
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
	        var pagingToolbar=Ext.getCmp('Consumptionbar');
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
			id:'Consumptionbar',
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
									if (status == 4) {
										return "<div style=\"color: red;\">"
												+ value + "</div>"
									} else
									{
										return value;
									}

							}
			          },
			          { text: '汇款单号',	dataIndex: 'remittance_num', menuDisabled:true, width : 150},
			          { text: '登记人',	dataIndex: 'create_per', menuDisabled:true, width : 100},
			          { text: '登记时间',	dataIndex: 'create_date', menuDisabled:true, width : 150},
			          { text: '确认人',	dataIndex: 'confirm_per', menuDisabled:true, width : 100},
			          { text: '确认时间',	dataIndex: 'confirm_date', menuDisabled:true, width : 150},
			          { text: '登记至确认耗时',	dataIndex: 'time_spent', menuDisabled:true, width : 160,
			        	  renderer : function(value,meta,record) {
			        		  var time_spent = record.data["time_spent"];
			        		  var result='';
									var days = parseInt(time_spent / (1 * 60 * 60 * 24));
									time_spent = time_spent - days * (1 * 60 * 60 * 24);
									var hours = parseInt(time_spent / (1 * 60 * 60));
									time_spent = time_spent - hours * (1 * 60 * 60);
									var min = parseInt(time_spent / (60));
									time_spent = time_spent - min * (1 * 60);
									var second = parseInt(time_spent / (1));

									result += days == 0 ? "" : (days + "天");
									result += hours == 0 ? "" : (hours + "小时");
									result += min == 0 ? "" : (min + "分钟");
									result += second + "秒";
								  if(result=='0秒')
								      {return null;}
								return result;
							}  
			          
			          },
	                  { text: '受理时间',	dataIndex: 'date', menuDisabled:true, width : 100},
	                  { text: '案例归属人', 	dataIndex: 'case_receiver', menuDisabled:true, width : 120},
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
	 		items:[case_code,case_receiver,remittance_num,daily_type,confirm_state]
	 	},{

	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[case_state,starttime_accept,endtime_accept,time_spent,{
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
	 			text:'导出',
	 			iconCls:'Application',
	 			handler:me.consumptionTimeExport
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	consumptionTimeExport:function(){
		window.location.href = "statistics/Consumption/consumptionTimeExport.do?"+consumptionTime ;
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
											 renderer : function(value, cellmeta, record,
														rowIndex, columnIndex, store) {
													var str = value;
													if (value.length > 15) {
														str = value.substring(0, 15) + "...";
													}
													return "<span title='" + value + "'>" + str
															+ "</span>";
												}
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