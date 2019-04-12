/**
 * 汇款单生成
 * 
 * @author yuanxiaobo
 */
remittance_canel = function(me) {
	me.up("window").close();
}
Ext.define("Rds.judicial.panel.JudicialRemittanceGridPanel",{
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
            	width:200,
            	fieldLabel:'案例编号'
            });
		var starttime=Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width : 175,
			labelWidth : 70,
			fieldLabel : '日报时间 从',
			labelAlign : 'right',
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
			width : 135,
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
		me.store = Ext.create('Ext.data.Store',{
			fields : ['case_id', 'case_code', 'receiver_area', 'case_receiver','client',
						'username', 'stand_sum', 'real_sum', 'return_sum','discountPrice','sample_str',
						'date','type','status','daily_time'],
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
        			Ext.apply(me.store.proxy.params, {
        				case_code:trim(case_code.getValue()),
        				starttime : dateformat(starttime
								.getValue()),
						endtime : dateformat(endtime
								.getValue()),
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
	                  { text: '案例编码',	dataIndex: 'case_code',menuDisabled:true, width : 100,
	                	  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var status= record.data["status"];
								if (status == 0) {
									return "<div style=\"color: red;\">"
											+ value + "</div>"
								} 
								{
									return value;
								}
	
							}
			          },
			          { text: '日报时间',	dataIndex: 'daily_time', menuDisabled:true, width : 100},
	                  { text: '受理时间',	dataIndex: 'date', menuDisabled:true, width : 100},
	                  { text: '案例归属人', 	dataIndex: 'case_receiver', menuDisabled:true, width : 120},
	                  { text: '案例归属地', 	dataIndex: 'receiver_area', menuDisabled:true, width : 200},
	                  { text: '案例委托人', 	dataIndex: 'client', menuDisabled:true, width : 120},
	                  { text: '标准金额', 	dataIndex: 'stand_sum',menuDisabled:true, width : 100},
	                  { text: '优惠金额', 	dataIndex: 'discountPrice', menuDisabled:true,width : 100},
	                  { text: '应收金额', 	dataIndex: 'real_sum', menuDisabled:true, width : 100},
	                  { text: '回款金额', 	dataIndex: 'return_sum', menuDisabled:true,width : 100},
	                  { text: '样本信息', 	dataIndex: 'sample_str', menuDisabled:true,width : 200},
	                  {
							text : '案例状态',
							dataIndex : 'status',
							menuDisabled : true,
							width : 100,
							renderer : function(value,meta,record) {
								switch (value) {
								case 0:
									return "已确认汇款";
									break;
								case 1:
									return "异常";
									break;
								case 2:
									return "日报已结算";
									break;
								case 3:
									return "登记状态";
									break;
								case 4:
									return "月报已结算";
									break;
								default:
									return "";
								}
							}
						},
						{
							text : '案例类型',
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
						}
		              ];
		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[case_code,starttime,endtime,{
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
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要汇款的记录!");
			return;
		};
		var case_id='';
		var real_sum = 0;
		for(var i = 0 ; i < selections.length ; i ++)
		{
//			if( selections[i].get("return_sum") > 0 || selections[i].get("status") == 0 )
//			{
//				Ext.Msg.alert("提示", "存在已汇款案例!");
//				return;
//			}
			real_sum += selections[i].get("real_sum");
			if((i+1)==selections.length)
				case_id +=  selections[i].get("case_id");
			else
				case_id += selections[i].get("case_id")+",";
		}
		remittance_save = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["case_id"]=case_id;
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "finance/casefinance/insertRemittanceRecord.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response) {
									Ext.MessageBox.alert("提示信息", "汇款单生成成功！");
									var grid = me.up("gridpanel");
									mailform_add.close();
								} else {
									Ext.MessageBox.alert("错误信息", "生成错误，请联系管理员！");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
			}
		}
		
		var remittanceform_add = Ext.create("Ext.window.Window", {
			title : '汇款信息',
			width : 320,
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
							handler : remittance_save
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : remittance_canel
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
					maxLength : 18,
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
					maxLength : 20,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					labelWidth : 80,
					name : 'remittance_account'
				},{
					xtype : "textfield",
					fieldLabel : '到款账户<span style="color:red">*</span>',
					labelAlign : 'right',
					maxLength : 20,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					labelWidth : 80,
					name : 'arrival_account'
				},{
					xtype : "textarea",
					fieldLabel : '汇款备注',
					labelAlign : 'right',
					maxLength : 100,
					labelWidth : 80,
					name : 'remittance_remark'
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