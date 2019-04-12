var exportInfo=""
var rowEditing = Ext.create('Ext.grid.plugin.CellEditing',{
            pluginId:'rowEditing',
            saveBtnText: '保存', 
            cancelBtnText: "取消", 
            autoCancel: false, 
            clicksToEdit:1   //双击进行修改  1-单击   2-双击    0-可取消双击/单击事件
});
var remittanceAccountStore=Ext.create('Ext.data.Store',{
    fields:['accountName'],
    proxy:{
			type: 'jsonajax',
			actionMethods:{read:'POST'},
		    url:'judicial/remittance/queryall.do',
		    reader:{type:'json',root:'data'}
     },
     autoLoad:true,
     remoteSort:true						
});
var acounttype=Ext.create('Ext.data.Store',{
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
});
Ext.define("Rds.judicial.panel.JudicialCaseFeeGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	region : 'center',  
	selType: 'rowmodel',
    plugins: [rowEditing],
	initComponent : function() {
		var me = this;
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
//							case_type.select(this.getAt(0));
						}
					}
		        }
		);
		var casecode = Ext.create('Ext.form.field.Text', {
			name : 'casecode',
			labelWidth : 60,
			width : 200,
			fieldLabel : '案例编号'
		});
		
		var oa_num = Ext.create('Ext.form.field.Text', {
			name : 'oa_num',
			labelWidth : 60,
			width : 200,
			fieldLabel : 'OA编号'
		});
		
		var areaname = Ext.create('Ext.form.field.Text', {
			name : 'areaname',
			labelWidth : 60,
			width : 200,
			fieldLabel : '地区'
		});
		
		var receiver = Ext.create('Ext.form.field.Text', {
			name : 'receiver',
			labelWidth : 60,
			width : 200,
			fieldLabel : '归属人'
		});
		
		var client = Ext.create('Ext.form.field.Text', {
			name : 'client',
			labelWidth : 60,
			width : 200,
			fieldLabel : '姓名'
		});
		
		var starttime = Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width : 175,
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
			fieldLabel : '收费类型',
			width : 180,
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
				fields : [ 'Name', 'Code' ],
				data : [ [ '全部', -1 ], [ '正常', 0 ], [ '先出报告后付款', 1 ] ]
			}),
			value : -1,
			mode : 'local',
			// typeAhead: true,
			name : 'type_state',
		});
		
		var fee_state = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '确认状态',
			width : 155,
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
			// typeAhead: true,
			name : 'fee_state',
		});
		var case_type = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '案例类型',
			width : 200,
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			valueField :"key",  
	        displayField: "value", 
			store: storeModel,
			mode : 'local',
			value:('subo_yanjl' == usercode )?'appraisal':'alcohol',
			// typeAhead: true,
			name : 'case_type',
			readOnly:('sq_wangyan' == usercode||'subo_yanjl'== usercode)?true:false
		});
		var endtime = Ext.create('Ext.form.DateField', {
			name : 'endtime',
			width : 135,
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
					width : 175,
					afterLabelTextTpl : me.required,
					emptyText : '请选择日期',
					format : 'Y-m-d',
//					allowBlank:false, //不允许为空
//					blankText:"不能为空", //错误提示信息，默认为This field is required! 
				});
		var paragraphtime_end = new Ext.form.DateField(
				{
					name : 'paragraphtime_end',
					fieldLabel : '到',
					labelWidth : 20,
					labelAlign : 'left',
					width : 135,
					afterLabelTextTpl : me.required,
					emptyText : '请选择日期',
					format : 'Y-m-d',
//					allowBlank:false, //不允许为空
//					blankText:"不能为空", //错误提示信息，默认为This field is required! 
				});
		var account = Ext.create('Ext.form.field.Text', {
			name : 'account',
			labelWidth : 60,
			width : 150,
			fieldLabel : '账户类型'
		});
		me.store = Ext.create('Ext.data.Store', {
			fields : [ 'case_id', 'fee_id', 'case_code', 'stand_sum',
					'real_sum', 'return_sum', 'discount', 'status', 'date','receive_name','client','case_remark',
					'finance_remark', 'type','oa_num',"areaname",'sample_str','confirm_date','paragraphtime','account',
					'discountPrice','remittanceName','remittanceDate','is_delete'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/casefee/getCaseFeeInfo.do',
				params : {
					start : 0,
					limit : 25
				},
				reader : {
					type : 'json',
					root : 'items',
					totalProperty : 'count'
				}
			},
			listeners : {
				'beforeload' : function(ds, operation, opt) {
					exportInfo = "case_code="+trim(casecode.getValue())+
								"&areaname="+trim(areaname.getValue())+
								"&type_state="+type_state.getValue()+
								"&case_type="+case_type.getValue()+
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
						oa_num:trim(oa_num.getValue()),
						areaname:trim(areaname.getValue()),
						type_state:type_state.getValue(),
						case_type:  case_type.getValue(),
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
				if (isnull == 0 || isnull ==2) {
					return "<div style=\"color: red;\">"
							+ value + "</div>"
				} else {
					return value;
				}
			}
		}, {
			header : '标准金额',
			dataIndex : 'stand_sum',
			width : 80
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
			header : '到款时间',
			dataIndex : 'paragraphtime',
			width : 80,
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
//				fieldLabel: "付款类型<span style='color:red'>*</span>",
                labelWidth : 80,
                editable:false,
                triggerAction: 'all',
                displayField: 'Name',
//                value:0,
                valueField: 'Code',
                store: new Ext.data.ArrayStore({
                    fields: ['Name', 'Code'],
                    data: [['正常', 0], ['先出报告后付款', 1]]
                })          ,
                mode: 'local',
                //typeAhead: true,
//                name: 'fee_type'
			}),
			renderer : function(value) {
				switch (value) {
				case 0:
					return "正常";
					break;
				case 1:
					return "先出报告后付款";
					break;
				case 2:
					return "免单";
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
			header : 'OA编号',
			dataIndex : 'oa_num',
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
			items : [oa_num,areaname,type_state,starttime, endtime]
		}, {
			xtype : 'toolbar',
			name : 'search',
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '1px !important'
		    },
			items : [paragraphtime_start,paragraphtime_end,account, {
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
				handler : me.onConfirm
			},{
	 			text:'插入OA编号',
	 			iconCls:'Add',
	 			handler:me.onInsertOA
	 		},{
	 			text:'我是案例编号',
	 			iconCls:'Find',
	 			handler:me.caseCode
	 		},{
	 			text:'回退案例',
	 			iconCls:'Arrowredo',
	 			handler:me.backCase,
	 			hidden:('masl' == usercode || 'admin' == usercode)?false:true
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
		window.location.href = "judicial/casefee/exportCaseInfoOther.do?"+exportInfo ;
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
	onInsertOA:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		var length = selections.length;
		if(length<1){
			Ext.Msg.alert("提示", "请选择需要增加OA编号的记录!");
			return;
		};
		var ids = '';
		for(var i = 0 ; i < length ; i ++)
		{
		   ids += selections[i].get("fee_id")+",";
		}
		ids=ids.substring(0,(ids.length-1));
		Ext.MessageBox.prompt(
				"OA编号",
				"请输入OA编号",
				function(id,msg) {
					if(id == 'ok')
					{
						if(msg=='')
						{
							Ext.Msg.alert("提示","请填写OA编号!");
							return;
						}
						if(msg.length>50)
						{
							alert("OA编号太长了...换个重新来吧...");
							return;
						}
						Ext.Ajax.request({  
							url:"judicial/casefee/insertOAnum.do", 
							method: "POST",
							headers: { 'Content-Type': 'application/json' },
							jsonData: {
								ids:ids,
								oa_num:msg
							}, 
							success: function (response, options) {  
								response = Ext.JSON.decode(response.responseText); 
				                 if (response == true) {  
				                 	Ext.MessageBox.alert("提示信息", "插入成功");
				                 	me.getStore().load();
				                 }else { 
				                 	Ext.MessageBox.alert("错误信息", "插入失败");
				                 } 
							},  
							failure: function () {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
				    	       
				      	});
					}
					
				});
		
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
//		if (selections[0].get("status") == 0
//				|| selections[0].get("status") == 2) {
//			Ext.Msg.alert("提示", "此案例已确认，无法修改!");
//			return;
//		}
//		var form = Ext.create("Rds.judicial.form.JudicialCaseFeeForm", {
//			region : "center",
//			grid : me
//		});
//		form.loadRecord(selections[0]);
//		var win = Ext.create("Ext.window.Window", {
//			title : '更改收费',
//			width : 600,
//			iconCls : 'Pageedit',
//			height : 500,
//			layout : 'border',
//			items : [ form ]
//		});
	},
	onUpdate : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		}
		if (selections[0].get("status") == 0
				|| selections[0].get("status") == 2) {
			Ext.Msg.alert("提示", "此案例已确认，无法修改!");
			return;
		}
		var form = Ext.create("Rds.judicial.form.JudicialCaseFeeForm", {
			region : "center",
			grid : me
		});
		form.loadRecord(selections[0]);
		var win = Ext.create("Ext.window.Window", {
			title : '更改收费',
			width : 600,
			iconCls : 'Pageedit',
			height : 500,
			layout : 'border',
			items : [ form ]
		});
		win.show();
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage = 1;
		me.getStore().load();
	},
	listeners : {
		'beforeedit':function(editor, e){
			if(!('masl' == usercode || 'xieh' == usercode  || 'sq_wangyan'==usercode || 'subo_yanjl'==usercode))
				return false;
			else{
				if(e.record.data.status=='0')
					return false;
			}
			rowEditing.on('edit',afterEdit);
		},
		'afterrender' : function() {
			this.store.load();
		}
	
	}
});
function afterEdit(e,s){
  	Ext.Ajax.request({  
		url:"judicial/casefee/saveCaseFee.do", 
		method: "POST",
		headers: { 'Content-Type': 'application/json' },
		jsonData: {
			fee_id:s.record.data.fee_id,
			real_sum:s.record.data.real_sum,
			return_sum:s.record.data.return_sum,
			discountPrice:s.record.data.discountPrice,
			paragraphtime:Ext.util.Format.date(s.record.data.paragraphtime, 'Y-m-d'),
			account:s.record.data.account,
			type:s.record.data.type,
			remark:s.record.data.finance_remark,
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