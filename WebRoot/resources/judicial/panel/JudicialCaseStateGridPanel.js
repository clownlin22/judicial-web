/**
 * 字典名称列表
 * 
 * @author chenwei
 */
Ext.define("Rds.judicial.panel.JudicialCaseStateGridPanel",{
	extend:"Ext.grid.Panel",
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var name = Ext.create('Ext.form.field.Text',{
        	name:'name',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'姓名',
        	emptyText : '委托人或样本姓名',
        });
		var phone = Ext.create('Ext.form.field.Text',{
        	name:'phone',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'登记电话'
        });
		var ownperson = Ext.create('Ext.form.field.Text',{
	    	name:'ownperson',
	    	labelWidth:50,
	    	width:'20%',
	    	fieldLabel:'归属人'
	    });
		var case_code = Ext.create('Ext.form.field.Text',{
	    	name:'case_code',
	    	labelWidth:60,
	    	width:'20%',
	    	fieldLabel:'案例编号'
	    });
		var mail_code = Ext.create('Ext.form.field.Text',{
	    	name:'mail_code',
	    	labelWidth:60,
	    	width:'20%',
	    	fieldLabel:'邮件编号'
	    });
		var starttime= new Ext.form.DateField({
			name : 'starttime',
			width:'20%',
			fieldLabel : '受理日期 从',
			labelWidth : 70,
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			maxValue : new Date()
		});
		
		var endtime= new Ext.form.DateField({
			name : 'endtime',
			width:'20%',
			labelWidth : 20,
			fieldLabel : '到',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY)
		});
		var is_delete=Ext.create('Ext.form.ComboBox', 
				{
					fieldLabel : '是否废除',
					width:'20%',
					labelWidth : 60,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					labelAlign : 'left',
					valueField : 'Code',
					store : new Ext.data.ArrayStore(
							{
								fields : [
										'Name',
										'Code' ],
								data : [
										[
												'全部',
												-1 ],
										[
												'未废除',
												0 ],
										[
												'已废除',
												1 ] ]
							}),
					value : -1,
					mode : 'local',
					// typeAhead: true,
					name : 'is_delete',
				});
		var is_report=Ext.create('Ext.form.ComboBox', 
				{
					fieldLabel : '是否邮寄',
					width:'20%',
					labelWidth : 60,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					labelAlign : 'left',
					valueField : 'Code',
					store : new Ext.data.ArrayStore(
							{
								fields : [
										'Name',
										'Code' ],
								data : [
										[
												'全部',
												'-1' ],
										[
												'是',
												'0' ],
										[
												'否',
												'1' ] ]
							}),
					value : '-1',
					mode : 'local',
					// typeAhead: true,
					name : 'is_report',
				});
		var urgent_state=Ext.create('Ext.form.ComboBox', 
				{
					fieldLabel : '紧急程度',
					width:'20%',
					labelWidth : 60,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					labelAlign : 'right',
					valueField : 'Code',
					store : new Ext.data.ArrayStore(
							{
								fields : [
										'Name',
										'Code' ],
								data : [
										[
												'全部',
												-1 ],
										[
												'普通',
												0 ],
										[
												'紧急',
												1 ] ]
							}),
					value : -1,
					mode : 'local',
					// typeAhead: true,
					name : 'urgent_state',
				});
		me.store = Ext.create('Ext.data.Store',{
			fields:['case_id', 'case_code','fandm','child','process_instance_id',
					'accept_time','close_time','client'
					,'address','phone','username',
					'sample_in_time','is_delete','mail_code'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'judicial/caseState/querypage.do',
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
        				name:name.getValue(),
        				starttime:dateformat(starttime.getValue()),
						endtime:dateformat(endtime.getValue()),
						ownperson:ownperson.getValue(),
						case_code:case_code.getValue(),
						is_delete:is_delete.getValue(),
						mail_code:mail_code.getValue(),
						urgent_state:urgent_state.getValue(),
						phone:trim(phone.getValue()),
						is_report:is_report.getValue()
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
		me.columns = [
		              Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'4%', menuDisabled:true}),
		              {
							text : '案例条形码',
							dataIndex : 'case_code',
							menuDisabled : true,
							width : 120,
							renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var isnull= record.data["is_delete"];
								if (isnull == 1) {
									return "<div style=\"text-decoration: line-through;color: red;\">"
											+ value + "</div>"
								} else {
									return value;
								}
							}
						},{
							text : '归属人',
							dataIndex : 'username',
							menuDisabled : true,
							width : 120
						},
						{
							text : '委托人',
							dataIndex : 'client',
							menuDisabled : true,
							width : 120
						},
						{
							text : '父母亲',
							dataIndex : 'fandm',
							menuDisabled : true,
							width : 120
						},
						{
							text : '孩子',
							dataIndex : 'child',
							menuDisabled : true,
							width : 120
						},
						{
							text : '受理日期',
							dataIndex : 'accept_time',
							menuDisabled : true,
							width : 100
						},
						{
							text : '截止日期',
							dataIndex : 'close_time',
							menuDisabled : true,
							width : 150
						},
						{
							text : '样本登记日期',
							dataIndex : 'sample_in_time',
							menuDisabled : true,
							width : 150
						},
						{
							text : '邮件编号',
							dataIndex : 'mail_code',
							menuDisabled : true,
							width : 100
						},
						{
							text : '电话号码',
							dataIndex : 'phone',
							menuDisabled : true,
							width : 120
						},
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[name,ownperson,starttime,endtime,phone]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		    },
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top', 		
	 		items:[case_code,is_delete,mail_code,urgent_state]
	 	},{

			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '1px !important'
		    },
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top', 		
	 		items:[is_report,{
            	text:'查询',
            	iconCls:'Find',
            	handler:me.onSearch
	 		}]
	 	
	 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[{
	 			text:'状态查看',
	 			iconCls:'Find',
	 			handler:me.onTaskHistory
	 		},{
	 			text:'我是案例编号',
	 			iconCls:'Application',
	 			handler:me.caseCode
	 		},{
	 			text:'我是邮件编号',
	 			iconCls:'Application',
	 			handler:me.mailCode
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	onTaskHistory: function () {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		}
		var case_code = selections[0].get("case_code");
		var values = {
			case_code:case_code,
			processInstanceId: selections[0].get("process_instance_id")
		};
		if (values.processInstanceId == null || values.processInstanceId == "") {
			Ext.Msg.alert("提示", "该记录不能进行此项操作!");
			return;
		}
		var flag ='';
		if('' != case_code && null != case_code){
			Ext.Ajax.request({
				url: "judicial/caseState/queryCompareResultCount.do",
				method: "POST",
				async: false,
				headers: {'Content-Type': 'application/json'},
				jsonData: values,
				success: function (response, options) {
					response = Ext.JSON.decode(response.responseText);
					if(response){
						flag='<span style="color:black">（该案例存在否定或突变的子案例哦!）</span>'
					}
				},
				failure: function () {
					Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
				}
			});
		}
		Ext.Ajax.request({
			url: "activiti/main/taskDetail.do",
			method: "POST",
			async: false,
			headers: {'Content-Type': 'application/json'},
			jsonData: values,
			success: function (response, options) {
				response = Ext.JSON.decode(response.responseText);
				var win = Ext.create("Ext.window.Window", {
					title: '流程步骤'+flag,
					width: 700,
					iconCls: 'Add',
					layout: 'fit',
					items: {
						xtype: 'grid',
						border: false,
						columns: [
							{
								text: '步骤ID',
								dataIndex: 'id',
								align: 'center',
								sortable: false,
								menuDisabled: true,
								hidden: true
							},
							{
								text: '步骤名称',
								dataIndex: 'name',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '活动类型',
								dataIndex: 'taskDefinitionKey',
								align: 'center',
								sortable: false,
								menuDisabled: true,
								hidden: true
							},
							{
								text: '办理人',
								dataIndex: 'assignee',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '开始时间',
								dataIndex: 'startTimeString',
								align: 'center',
								width: 120,
								sortable: false,
								menuDisabled: true
							},
							{
								text: '签收时间',
								dataIndex: 'claimTimeString',
								align: 'center',
								width: 120,
								sortable: false,
								hidden: true,
								menuDisabled: true
							},
							{
								text: '结束时间',
								dataIndex: 'endTimeString',
								align: 'center',
								width: 120,
								sortable: false,
								menuDisabled: true
							},
							{
								text: '活动耗时',
								dataIndex: 'durationInMillisString',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '原因',
								dataIndex: 'comment',
								align: 'center',
								flex: 1,
								sortable: false,
								menuDisabled: true,
								 renderer : function(value, cellmeta, record,
											rowIndex, columnIndex, store) {
//										var str = value;
//										if (value.length > 15) {
//											str = value.substring(0, 15) + "...";
//										}
									 if(null == value) return "";
									 else 
										return "<span title='" + value + "'>" + value
												+ "</span>";
									}
							}
						],
						store: Ext.create("Ext.data.Store", {
							fields: ['id', 'name', 'taskDefinitionKey', 'assignee', 'claimTime', 'startTime', 'endTime', 'durationInMillis','comment',
								{
									name: 'claimTimeString', type: 'date',
									convert: function (v, rec) {
										return rec.data.claimTime == null ? "" : Ext.Date.format(new Date(rec.data.claimTime), 'Y-m-d H:i');
									}
								},
								{
									name: 'startTimeString', type: 'date',
									convert: function (v, rec) {
										return rec.data.startTime == null ? "" : Ext.Date.format(new Date(rec.data.startTime), 'Y-m-d H:i');
									}
								},
								{
									name: 'endTimeString',
									convert: function (v, rec) {
										return rec.data.endTime == null ? "" : Ext.Date.format(new Date(rec.data.endTime), 'Y-m-d H:i');
									}
								}, {
									name: 'durationInMillisString',
									convert: function (v, rec) {
										var mills = rec.data.durationInMillis;
										var result = "";
										if (mills == null) {
											result = "";
										}
										else if (mills < 1000) {
											result = "小于1秒";
										}
										else {
											var days = parseInt(mills / (1000 * 60 * 60 * 24));
											mills = mills - days * (1000 * 60 * 60 * 24);
											var hours = parseInt(mills / (1000 * 60 * 60));
											mills = mills - hours * (1000 * 60 * 60);
											var min = parseInt(mills / (1000 * 60));
											mills = mills - min * (1000 * 60);
											var second = parseInt(mills / (1000));

											result += days == 0 ? "" : (days + "天");
											result += hours == 0 ? "" : (hours + "小时");
											result += min == 0 ? "" : (min + "分钟");
											result += second + "秒";
										}
										return result;
									}
								}
							],
//							sorters: [{
////								property: 'id',
////								direction: 'DESC'
//							}],
							data: response
						})
					}
				});
				win.show();
				// response = Ext.JSON.decode(response.responseText);
				// if (response.result == true) {
				//     Ext.MessageBox.alert("提示信息", response.message);
				//     me.getStore().load();
				//     // me.up("window").close();
				// } else {
				//     Ext.MessageBox.alert("错误信息", response.message);
				//     me.getStore().load();
				// }
			},
			failure: function () {
				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
			}
		});
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
			//if((i+1)%20==0) num +="<br>";
		}
		Ext.Msg.alert("我是案例编号", case_code);
	},
	mailCode:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要查看的邮件编号!");
			return;
		};
		var mail_code="";
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if(null != selections[i].get("mail_code")){
				mail_code += selections[i].get("mail_code")+";";
			}
		}
		Ext.Msg.alert("我是邮件编号", mail_code);
	},
	onCheck:function(){
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		};
		if (selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要查看的记录!");
			return;
		};
		var grid = Ext.create("Ext.grid.Panel",{
			store : {// 配置数据源
				fields : [ 'processtime', 'processstatus'],// 定义字段
				proxy : {
					type : 'jsonajax',
					actionMethods : {
						read : 'POST'
					},
					url : 'judicial/caseState/querymodel.do',
					params : {
						'case_id' : selections[0].get("case_id")
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
//			selModel:selModel,
			columns: [// 配置表格列
						{
							header : "案例状态",
							dataIndex : 'processstatus',
							flex : 1,
							menuDisabled : true
						}, {
							header : "时间",
							dataIndex : 'processtime',
							flex : 1,
							menuDisabled : true
						} ],
			region:'center',
			multiSelect: true
		});
		var win = Ext.create("Ext.window.Window", {
			title : "案例状态（案例条形码：" + selections[0].get("case_code") + "）",
			width : 400,
			iconCls : 'Find',
			height : 300,
			modal:true,
			layout : 'border',
			items :grid
		});
		win.show();
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	}
});