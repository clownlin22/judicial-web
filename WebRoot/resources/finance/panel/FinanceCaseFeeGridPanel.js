var exportFinanceInfo=""
Ext.define("Rds.finance.panel.FinanceCaseFeeGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	region : 'center',  
	selType: 'rowmodel',
    plugins: [
              Ext.create('Ext.grid.plugin.CellEditing', {
                  clicksToEdit: 1,
                  listeners:{  
                      'edit':function(e,s){  
                      	Ext.Ajax.request({  
							url:"finance/casefinance/updateCaseFee.do", 
							method: "POST",
							headers: { 'Content-Type': 'application/json' },
							jsonData: {
								fee_id:s.record.data.fee_id,
								stand_sum:s.record.data.stand_sum,
								real_sum:s.record.data.real_sum,
								return_sum:s.record.data.return_sum,
								discountPrice:s.record.data.discountPrice,
								paragraphtime:Ext.util.Format.date(s.record.data.paragraphtime, 'Y-m-d'),
								account:s.record.data.account,
								type:s.record.data.type,
								finance_remark:s.record.data.finance_remark,
								remittanceName:s.record.data.remittanceName,
								remittanceDate:Ext.util.Format.date(s.record.data.remittanceDate, 'Y-m-d')
							}, 
							success: function (response, options) {  
								response = Ext.JSON.decode(response.responseText); 
				                 if (response==false) {  
					                 Ext.MessageBox.alert("错误信息", "修改收费失败，请查看");
				                 }
							},  
							failure: function () {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
				      	});
                      }  
      	          }  
              })
          ],
	initComponent : function() {
		var me = this;
		var remittanceAccountStore=Ext.create('Ext.data.Store',
		        {
		          fields:['accountName'],
		          proxy:{
		    		type: 'jsonajax',
		    		actionMethods:{read:'POST'},
		            url:'judicial/remittance/queryall.do',
		            reader:{
		              type:'json',
		              root:'data'
		            }
		          },
		          autoLoad:true,
		          remoteSort:true						
		        }
		   );
		var storeModel = Ext.create(
		        'Ext.data.Store',
		        {
		          model:'model',
		          proxy:{
		        	type: 'jsonajax',
		            actionMethods:{read:'POST'},
		            url:'judicial/dicvalues/getCaseFeeTypes.do',
		            reader:{
		              type:'json',
		              root:'data'
		            }
		          },
		          autoLoad:true,
		          remoteSort:true,
		          listeners : {
						'load' : function() {
							case_type.select(this.getAt(0));
						}
					}
		        }
		);
		var acounttype=Ext.create(
				'Ext.data.Store',
				{
					fields:['remark'],
					proxy:{
						type: 'jsonajax',
						actionMethods:{read:'POST'},
						url:'judicial/bank/queryallpage.do',
						params:{
							initials:""
						},
						reader:{
							type:'json',
							root:'data'
						}
					},
					autoLoad:true,
					remoteSort:true						
				}
		);
		var casecode = Ext.create('Ext.form.field.Text', {
			name : 'casecode',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '案例编号'
		});
		
		var areaname = Ext.create('Ext.form.field.Text', {
			name : 'areaname',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '地区'
		});
		
		var receiver = Ext.create('Ext.form.field.Text', {
			name : 'receiver',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '归属人'
		});
		
		var client = Ext.create('Ext.form.field.Text', {
			name : 'client',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '姓名'
		});
		
		var starttime = Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '受理时间 从',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY, -7),
			listeners : {
				select : function() {
					var start = starttime.getValue();
					var end = endtime.getValue();
					endtime.setMinValue(start);
				}
			}
		});
		
		var type_state = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '类型',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store :new Ext.data.ArrayStore(
					{fields : ['Name','Code' ],
						data : [
								['全部',-1 ],
								['正常','0' ],
								['为先出报告后付款',1],
								['免单',2],
								['优惠',3],
								['月结',4],
								['二次采样',5],
								['补样',6]
								]
					}),
			value : -1,
			mode : 'local',
			name : 'type_state',
		});
		
		var fee_state = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '确认状态',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
				fields : [ 'Name', 'Code' ],
				data : [ [ '全部', -1 ], [ '已确认', 0 ], [ '未处理', 1 ] ]
			}),
			value : -1,
			mode : 'local',
			name : 'fee_state',
		});
		var case_type = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '案例类型',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			valueField :"key",  
	        displayField: "value", 
			store: storeModel,
			mode : 'local',
			value:'dna',
			name : 'case_type',
			readOnly:('sq_wangyan' == usercode )?true:false
		});
		var endtime = Ext.create('Ext.form.DateField', {
			name : 'endtime',
			width : '18%',
			labelWidth : 20,
			fieldLabel : '到',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners : {
				select : function() {
					var start = starttime.getValue();
					var end = endtime.getValue();
					starttime.setMaxValue(end);
				}
			}
		});
		var paragraphtime_start = new Ext.form.DateField(
				{
					name : 'paragraphtime_start',
					fieldLabel : '到款日期',
					labelWidth : 60,
					labelAlign : 'left',
					width : '20%',
					afterLabelTextTpl : me.required,
					emptyText : '请选择日期',
					format : 'Y-m-d'
				});
		var paragraphtime_end = new Ext.form.DateField(
				{
					name : 'paragraphtime_end',
					fieldLabel : '到',
					labelWidth : 20,
					labelAlign : 'left',
					width : '18%',
					afterLabelTextTpl : me.required,
					emptyText : '请选择日期',
					format : 'Y-m-d'
				});
		var account = Ext.create('Ext.form.field.Text', {
			name : 'account',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '账户类型'
		});
		me.store = Ext.create('Ext.data.Store', {
			fields : [ 'case_id', 'fee_id', 'case_code', 'stand_sum',
					'real_sum', 'return_sum', 'discount', 'status', 'date','receive_name','client','case_remark',
					'finance_remark', 'type',"areaname",'sample_str','confirm_date','paragraphtime','account',
					'discountPrice','fees','siteFee','remittanceName','remittanceDate','is_delete'],
			start:0,
			limit:15,
			pageSize:15,
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'finance/casefinance/getCaseFeeInfo.do',
				params : {
				},
				reader : {
					type : 'json',
					root : 'items',
					totalProperty : 'count'
				}
			},
			listeners : {
				'beforeload' : function(ds, operation, opt) {
					var case_type_temp=case_type.getValue();
					if("subo_zhoujin"== usercode && "dna"!=case_type_temp )
					{
						case_type_temp = "dna";
					}else if("subo_yangmeiming" == usercode && "dna"!=case_type_temp  && "appraisal" != case_type_temp )
					{
						case_type_temp = "dna";
					}
					console.log(case_type_temp);
					exportFinanceInfo = "case_code="+trim(casecode.getValue())+
										"&areaname="+trim(areaname.getValue())+
										"&type_state="+type_state.getValue()+
										"&case_type="+case_type_temp+
										"&fee_state="+fee_state.getValue()+
										"&receiver="+trim(receiver.getValue())+
										"&client="+trim(client.getValue())+
										"&starttime="+dateformat(starttime.getValue())+
										"&endtime="+dateformat(endtime.getValue())+
										"&paragraphtime_start="+dateformat(paragraphtime_start.getValue())+
										"&paragraphtime_end="+dateformat(paragraphtime_end.getValue())+
										"&account="+account.getValue();
					Ext.apply(me.store.proxy.params, {
						case_code : trim(casecode.getValue()),
						areaname:trim(areaname.getValue()),
						type_state:type_state.getValue(),
						case_type:  case_type_temp,
						fee_state : fee_state.getValue(),
						receiver:trim(receiver.getValue()),
						client:trim(client.getValue()),
						starttime : dateformat(starttime.getValue()),
						endtime : dateformat(endtime.getValue()),
						paragraphtime_start : dateformat(paragraphtime_start.getValue()),
						paragraphtime_end : dateformat(paragraphtime_end.getValue()),
						account:account.getValue(),
						usercode:usercode
					});
				}
			}
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
		});
		
		me.bbar = Ext.create('Ext.PagingToolbar', {
			store : me.store,
			pageSize : me.pageSize,
			displayInfo : true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
			emptyMsg : "没有符合条件的记录"
		});
		me.columns = [ {
			header : '案例编号',
			dataIndex : 'case_code',
			width : 120,
			renderer : function(value, cellmeta,
					record, rowIndex, columnIndex,
					store) {
				var isnull= record.data["status"];
				if (isnull == 0) {
					return "<div style=\"color: red;\">"
							+ value + "</div>"
				} else {
					return value;
				}
			}
		}, {
			header : '标准金额',
			dataIndex : 'stand_sum',
			width : 80,
			editor:'textfield'
		},{
			header : '实收金额',
			dataIndex : 'real_sum',
			width : 80,
			menuDisabled : true,
			editor:'textfield'
		}, {
			header : '回款金额',
			dataIndex : 'return_sum',
			width : 80,
			editor:'textfield' 
		}, {
			header : '财务确认时间',
			dataIndex : 'paragraphtime',
			width : 100,
			renderer:Ext.util.Format.dateRenderer('Y-m-d'),
            editor:new Ext.form.DateField({
                 format: 'Y-m-d'
               })
		}, {
			header : '账户类型',
			dataIndex : 'account',
			width : 200,
			editor:new Ext.form.ComboBox({
				autoSelect : true,
				editable:true,
		        name:'account',
		        triggerAction: 'all',
		        queryMode: 'local', 
		        emptyText : "请选择",
		        selectOnTab: true,
		        store: acounttype,
		        maxLength: 50,
		        fieldStyle: me.fieldStyle,
		        displayField:'remark',
		        valueField:'remark',
		        listClass: 'x-combo-list-small'
			})
		}, 
		{ text: '汇款账户',	dataIndex: 'remittanceName', menuDisabled:true, width : 150,
      	  editor:new Ext.form.ComboBox({
  				autoSelect : true,
  				editable:true,
  		        name:'remittanceName',
  		        triggerAction: 'all',
  		        queryMode: 'local', 
  		        emptyText : "请选择",
  		        selectOnTab: true,
  		        store: remittanceAccountStore,
  		        maxLength: 50,
  		        fieldStyle: me.fieldStyle,
  		        displayField:'accountName',
  		        valueField:'accountName',
  		        listClass: 'x-combo-list-small'
  			})
          },
          { text: '汇款时间',	dataIndex: 'remittanceDate', menuDisabled:true, width : 80,
        	  renderer:Ext.util.Format.dateRenderer('Y-m-d'),
              editor:new Ext.form.DateField({
                   format: 'Y-m-d'
                 })
          },
          {
  			header : '优惠价格',
  			dataIndex : 'discountPrice',
  			width : 80,
  			editor:'textfield' 
  		  },
		  {
			header : '类型',
			dataIndex : 'type',
			width : 120,
			editor:Ext.create('Ext.form.ComboBox', {
                labelWidth : 80,
                editable:false,
                triggerAction: 'all',
                displayField: 'Name',
                valueField: 'Code',
                store: new Ext.data.ArrayStore({
                    fields: ['Name', 'Code'],
                    data: [['正常', 0], ['先出报告后付款', 1]]
                })          ,
                mode: 'local',
			}),
			renderer : function(value) {
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
				default:
					return "";
				}
			}
		}, {
			header : '财务备注',
			dataIndex : 'finance_remark',
			width : 200,
			editor:'textfield'
		} , {
			header : '案例备注',
			dataIndex : 'case_remark',
			width : 200
		},{
			header : '样本信息',
			dataIndex : 'sample_str',
			width : 200
		},{
			header : '归属人',
			dataIndex : 'receive_name',
			width : 100
		},{
			header : '地区',
			dataIndex : 'areaname',
			width : 200
		},{
			header : '委托人',
			dataIndex : 'client',
			width : 80
		},{
			header : '折扣',
			dataIndex : 'discount',
			width : 60
		},{
			header : '状态',
			dataIndex : 'status',
			width : 80,
			renderer : function(value) {
				switch (value) {
				case 0:
					return "<span style='color:green'>已确认</span>";
					break;
				case 1:
					return "<span style='color:red'>异常</span>";
					break;
				case 2:
					return "<span style='color:blue'>日报已结算</span>";
					break;
				case 3:
					return "登记状态";
					break;
				case 4:
					return "<span style='color:blue'>月报已结算</span>";
					break;
				default:
					return "";
				}
			}
		}, {
			header : '受理日期',
			dataIndex : 'date',
			width : 100
		},{
			header : '确认日期',
			dataIndex : 'confirm_date',
			width : 160
		}];

		me.dockedItems = [ {
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [casecode,receiver,client, case_type,fee_state]
		},{
			xtype : 'toolbar',
			name : 'search',
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		    },
			items : [areaname,type_state,starttime, endtime,account]
		}, {
			xtype : 'toolbar',
			name : 'search',
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '1px !important'
		    },
			items : [paragraphtime_start,paragraphtime_end, {
				text : '查询',
				iconCls : 'Find',
				handler : me.onSearch
			} ]
		}, {
			xtype : 'toolbar',
			dock : 'top',
			items : [ {
				text : '确认案例',
				iconCls : 'Pageedit',
				handler : me.onConfirm,
				hidden:true
			},{
	 			text:'我是案例编号',
	 			iconCls:'Find',
	 			handler:me.caseCode
	 		},{
	 			text:'回退案例',
	 			iconCls:'Arrowredo',
	 			handler:me.backCase,
	 			hidden:true
	 		},{
	 			text:'财务计算',
	 			iconCls:'Applicationedit',
	 			handler:me.financeCount,
	 			hidden:('masl' == usercode ||　'admin'== usercode )?false:true
	 		},{
	 			text:'导出',
	 			iconCls:'Application',
	 			handler:me.onExport
	 		},'Tips：红色编号为已确认案例']
		} ];

		me.callParent(arguments);
	},
	onExport:function(){
		window.location.href = "finance/casefinance/exportCaseInfoOther.do?"+exportFinanceInfo ;
	},
	financeCount:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要统计案例!");
			return;
		};
		var stand_sum=0;
		var real_sum=0;
		var return_sum=0;
		for(var i = 0 ; i < selections.length ; i ++)
		{
			stand_sum += selections[i].get("stand_sum");
			real_sum += selections[i].get("real_sum");
			return_sum += selections[i].get("return_sum");
		}
		Ext.MessageBox.alert("统计结果", "标准金额："+stand_sum+"<br>实收金额："+real_sum+"<br>回款金额："+return_sum);
	},
	caseCode:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要查看的案例编号!");
			return;
		};
		var case_code="";
		for(var i = 0 ; i < selections.length ; i ++)
		{
			case_code += selections[i].get("case_code")+";";
		}
		Ext.Msg.alert("我是案例编号", case_code);
	},
	backCase : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要回退的记录!");
			return;
		}
		var ids='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if((i+1)==selections.length)
				ids += "'" + selections[i].get("fee_id")+"'";
			else
				ids += "'" + selections[i].get("fee_id")+"',";
		}
			Ext.MessageBox.confirm("提示", "请核对需要回退选中记录，确认选是?", function (btn) {
		        if("yes"==btn)
		        {
					Ext.Ajax.request({  
						url:"judicial/casefee/caseFeeConfirm.do", 
						method: "POST",
						headers: { 'Content-Type': 'application/json' },
						jsonData: {
							ids:ids,
							status:3
						}, 
						success: function (response, options) {  
							response = Ext.JSON.decode(response.responseText); 
							if(response == true){
								Ext.MessageBox.alert("提示信息", "案例回退成功！");
								me.getStore().load();}
							else
								Ext.MessageBox.alert("错误信息", "回退案例失败，请联系管理员！");
						},  
						failure: function () {
							Ext.Msg.alert("提示", "回退案例失败<br>请联系管理员!");
						}
			      	});
		        }
			})
	},
	onConfirm : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要确认的记录!");
			return;
		}
		var ids='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if((i+1)==selections.length)
				ids += "'" + selections[i].get("fee_id")+"'";
			else
				ids += "'" + selections[i].get("fee_id")+"',";
		}
			Ext.MessageBox.confirm("提示", "请核对需要确认选中记录，确认选是?", function (btn) {
		        if("yes"==btn)
		        {
		        	Ext.MessageBox.wait('正在操作','请稍后...');
					Ext.Ajax.request({  
						url:"judicial/casefee/caseFeeConfirm.do", 
						method: "POST",
						headers: { 'Content-Type': 'application/json' },
						jsonData: {
							ids:ids,
							status:0
						}, 
						success: function (response, options) {  
							response = Ext.JSON.decode(response.responseText); 
							if(response == true){
								Ext.MessageBox.alert("提示信息", "案例确认成功!");
								me.getStore().load();}
							else
								Ext.MessageBox.alert("错误信息", "确认案例失败，请联系管理员!");
						},  
						failure: function () {
							Ext.Msg.alert("提示", "确认案例失败<br>请联系管理员!");
						}
			      	});
		        }
			})
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage = 1;
		me.getStore().load();
	},
	listeners : {
		'beforeedit':function(editor, e){
			if(e.record.data.status==0 || !('admin1'==usercode))
				return false;
		},
		'afterrender' : function() {
			this.store.load();
		}
	}
});