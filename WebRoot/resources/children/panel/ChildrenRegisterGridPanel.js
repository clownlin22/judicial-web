/**
 * 案例登记列表
 * 
 * @author yxb
 */
var exportChildren="";
case_canel = function(me) {
	me.up("window").close();
}
Ext.define("Rds.children.panel.ChildrenRegisterGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	region : 'center',
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var case_code = Ext.create('Ext.form.field.Text', {
			name : 'case_code',
			labelWidth : 60,
			width : '20%',
			regexText : '请输入案例编号',
			fieldLabel : '案例编号'
		});
		var sample_code = Ext.create('Ext.form.field.Text', {
			name : 'sample_code',
			labelWidth : 60,
			width : '20%',
			regexText : '请输入样本条码',
			fieldLabel : '样本条码'
		});
		var child_name = Ext.create('Ext.form.field.Text', {
			name : 'child_name',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '儿童姓名'
		});
		var starttime=Ext.create('Ext.form.DateField', {
			name : 'starttime',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '采集时间 从',
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
	    var verify_state = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '审核状态',
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
								['未审核','0' ],
								['待审核',1],
								['审核不通过',2],
								['审核通过',3],
								['案例样本交接确认中',4],
								['实验中',5],
								['报告打印中',6],
								['邮寄中',7],
								['归档中',8],
								['已归档',9]
								]
					}),
			value : '',
			mode : 'local',
			name : 'verify_state',
		});
	    var is_delete=Ext.create('Ext.form.ComboBox', {
					fieldLabel : '是否废除',
					width : '20%',
					labelWidth : 60,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					valueField : 'Code',
					store : new Ext.data.ArrayStore({
								fields : ['Name','Code' ],
								data : [['全部',-1 ],
										['未废除',0 ],
										['已废除',1 ]]
							}),
					value : -1,
					mode : 'local',
					name : 'is_delete',
				});
		me.store = Ext.create('Ext.data.Store', {
			fields : ['case_id', 'case_code', 'agentia_id', 'agentia_name',
					'case_areacode', 'case_areaname', 'address', 'case_userid',
					'case_username', 'receiver_area', 'child_name', 'birth_date',
					'child_sex', 'id_number', 'birth_hospital', 'house_area',
					'life_area', 'mail_area', 'gather_time', 'gather_id',
					'case_in_per', 'case_in_time', 'case_in_pername','sample_code',
					'is_delete', 'tariff_id', 'tariff_name', 'standFee','verify_state',
					'realFee', 'returnFee','fee_time', 'mail_name', 'mail_code','print_time','remark',
					'process_instance_id', 'task_id', 'task_def_key', 'task_name', 'suspension_state','has_comment'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'children/register/getCaseInfo.do',
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
					exportChildren=
						"case_code="+trim(case_code.getValue())+
						"&starttime="+dateformat(starttime.getValue())+
						"&endtime="+dateformat(endtime.getValue())+
						"&is_delete="+is_delete.getValue()+
						"&verify_state="+verify_state.getValue()+
						"&child_name="+trim(child_name.getValue()) +
						"&sample_code="+trim(sample_code.getValue());
					Ext.apply(me.store.proxy.extraParams, {
						endtime : dateformat(endtime.getValue()),
						starttime : dateformat(starttime.getValue()),
						child_name : trim(child_name.getValue()),
						case_code : trim(case_code.getValue()),
						is_delete : is_delete.getValue(),
						sample_code:trim(sample_code.getValue()),
						verify_state:verify_state.getValue()
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
		me.columns = [{
					dataIndex : 'case_id',
					hidden : true
				}, {
					text : '案例条形码',
					dataIndex : 'case_code',
					menuDisabled : true,
					width : 150,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						var isnull = record.data["is_delete"];
						if (isnull == 1) {
							return "<div style=\"text-decoration: line-through;color: red;\">"
									+ value + "</div>"
						} else {
							return value;
						}

					}
				},{
					text : '样本条码',
					dataIndex : 'sample_code',
					menuDisabled : true,
					width : 150
				}, {
					dataIndex : 'agentia_id',
					hidden : true
				},{
					text : '案例归属地',
					dataIndex : 'case_areaname',
					menuDisabled : true,
					width : 200
				}, {
					text : '归属人',
					dataIndex : 'case_username',
					menuDisabled : true,
					width : 120
				},{
					text : '案例审核状态',
					dataIndex : 'verify_state',
					menuDisabled : true,
					width : 180,
					renderer : function(value,meta,record) {
						switch (value) {
						case 0:
							return "未审核";
							break;
						case 1:
							if(record.get("task_def_key")=="taskAudit"){
								return "已提交审核";
							}else{
								return "<span style='color:red'>案例审核未通过</span><a class='lbtnComment' href='#'>查看原因</a>";
							}
							break;
						case 2:
							 if(record.get("task_def_key")=="taskAudit"){
                                    return "已提交审核";
                                }else{
                                    return "<span style='color:red'>案例审核未通过</span><a class='lbtnComment' href='#'>查看原因</a>";
                                }
								break;
						case 3:
							return "<span style='color:green'>审核通过</span>";
							break;
						case 4:
							return "<span style='color:red'>样本交接中</span>";
							break;
						case 5:
							return "<span style='color:red'>实验中</span>";
							break;
						case 6:
							return "<span style='color:red'>报告打印中</span>";
							break;
						case 7:
							return "<span style='color:red'>邮寄中</span>";
							break;
						case 8:
							return "<span style='color:red'>归档中</span>";
							break;
						case 9:
							return "<span style='color:red'>已归档</span>";
							break;
						default:
							return "";
						}
					}
				
				}, {
					text : '详细地址',
					dataIndex : 'address',
					width : 300,
					menuDisabled : true
				},  {
					text : '快递',
					dataIndex : 'mail_name',
					menuDisabled : true,
					width : 120
				}, {
					text : '快递编号',
					dataIndex : 'mail_code',
					menuDisabled : true,
					width : 120
				}, {
					text : '儿童姓名',
					dataIndex : 'child_name',
					menuDisabled : true,
					width : 120
				}, {
					text : '出生日期',
					dataIndex : 'birth_date',
					menuDisabled : true,
					width : 120
				}, {
					text : '性别',
					dataIndex : 'child_sex',
					menuDisabled : true,
					width : 50,
					renderer : function(value) {
						switch (value) {
							case 0 :
								return "女";
								break;
							case 1 :
								return "男";
								break;
							default :
								return "";
						}
					}
				}, {
					text : '身份证号',
					dataIndex : 'id_number',
					menuDisabled : true,
					width : 150
				}, {
					text : '出生医院名称',
					dataIndex : 'birth_hospital',
					menuDisabled : true,
					width : 200
				}, {
					text : '户籍所在地',
					dataIndex : 'house_area',
					menuDisabled : true,
					width : 200
				}, {
					text : '生活所在地',
					dataIndex : 'life_area',
					menuDisabled : true,
					width : 200
				}, {
					text : '反馈寄送地',
					dataIndex : 'mail_area',
					menuDisabled : true,
					width : 200
				}, {
					text : '采集时间',
					dataIndex : 'gather_time',
					menuDisabled : true,
					width : 100
				}, {
					text : '登记人',
					dataIndex : 'case_in_pername',
					menuDisabled : true,
					width : 100
				}, {
					text : '登记时间',
					dataIndex : 'case_in_time',
					menuDisabled : true,
					width : 100
				},{
					text : '试剂',
					dataIndex : 'agentia_name',
					width : 200,
					menuDisabled : true
				}];

		me.dockedItems = [{
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [case_code, child_name, is_delete, starttime, endtime]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 	},
			xtype : 'toolbar',
			dock : 'top',
			items : [sample_code,verify_state, {
				text : '查询',
				iconCls : 'Find',
				handler : me.onSearch
			}]
		}, {
			xtype : 'toolbar',
			dock : 'top',
			items : [ {
						text : '新增',
						iconCls : 'Pageadd',
						handler : me.onInsert
					}, {
						text : '修改',
						iconCls : 'Pageedit',
						handler : me.onUpdate
					}, {
						text : '废除',
						iconCls : 'Delete',
						handler : me.onDelete
					}, {
						text : '照片管理',
						iconCls : 'Cog',
						handler : me.onPhotoManage
					},{
						text: '提交审核',
						iconCls: 'Pageedit',
						handler: me.onSubmit
					},{
						text: '优惠码插入',
						iconCls: 'Add',
						handler: me.confirmCode
					},{
						text: '汇款生成',
						iconCls: 'User',
						handler: me.createFinanceDaily
					},{
						text : '导出',
						iconCls : 'Pageexcel',
						handler : me.onExport,
						hidden:true
					}]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 	},
			xtype : 'toolbar',
			dock : 'top',
			items : [ {
				text : '查看监护人信息',
				iconCls : 'Find',
				handler : me.onFind
			}, {
				text : '查看采集人员信息',
				iconCls : 'Find',
				handler : me.onFindGather
			},{
				text: '查看流程状态',
				iconCls: 'Pageedit',
				handler: me.onTaskHistory
			}
		]
		}];
		me.callParent(arguments);
	},
	confirmCode:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1 ){
			Ext.Msg.alert("提示", "请选择一条需要优惠的记录!");
			return;
		}
		var case_id=selections[0].get("case_id");
		var case_code=selections[0].get("case_code");
		var accept_time=selections[0].get("gather_time");
		if('' == case_code || null == case_code)
		{
			Ext.Msg.alert("提示", "既然登记忘记填了，不如等案例编号生成了再插入吧");
			return;
		}
		confirmCode_confirm = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["case_id"]=case_id;
			values["case_code"]=case_code;
			values["case_type"]='children';
			values["accept_time"]=accept_time;
			values["finance_type"]="儿童基因库";
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
										url : "finance/casefinance/confirmCodeAdd.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : values,
										success : function(response, options) {
											response = Ext.JSON
													.decode(response.responseText);
											console.log(response.result);
											if (response.result) {
												Ext.MessageBox.alert("提示信息", response.message);
												var grid = me.up("gridpanel");
												me.getStore().load();
												confirmCodeadd.close();
											} else {
												Ext.MessageBox.alert("错误信息", response.message);
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
		
		var confirmCodeadd = Ext.create("Ext.window.Window", {
			title : '优惠码信息('+case_code+')',
			width : 350,
			height : 200,
			iconCls : 'Add',
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
							text : '插入',
							iconCls : 'Disk',
							handler : confirmCode_confirm
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : case_canel
						}],
				items : [{
					xtype : 'tbtext',
					style:'color:red',
					text : '注意：已经生成汇款单的案例，请删除后再操作！'
				},{
					xtype : "textfield",
					fieldLabel : '优惠码',
					allowBlank:false,
					labelAlign : 'right',
					maxLength : 100,
					regex:/^[^\s]*$/,
					regexText:'请输入正确条形码',
					style:"margin-top:20px;",
					labelWidth : 60,
					name : 'confirm_code'
				}]
			}]
		})
		confirmCodeadd.show();
	},
	onSubmit: function () {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		}

		var taskId='';
		var case_ids='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if(selections[i].get("is_delete")==1){
				Ext.Msg.alert("提示", "存在已删除案例，无法提交!");
				return;
			}
			if(selections[i].get("verify_state")==3){
				Ext.Msg.alert("提示", "存在已审核案例，无法重复提交!");
				return;
			}
			if (selections[i].get("task_def_key") != "taskRegister") {
				Ext.Msg.alert("提示", "该状态不允许提交审核!");
				return;
			}
		}
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if((i+1)==selections.length){
					taskId +=  selections[i].get("task_id");
					case_ids += selections[i].get("case_id")
			}
			else{
					taskId += selections[i].get("task_id")+",";
					case_ids += selections[i].get("case_id")+",";
			}
		}
		var values = {
			taskId: taskId
		};
		if (values.taskId == null || values.taskId == "") {
			Ext.Msg.alert("提示", "该记录不能进行此项操作!");
			return;
		}
		
		Ext.MessageBox.confirm('提示','确定提交审核此案例吗？',
				function(id) {
					if (id == 'yes') {
						Ext.MessageBox.wait('正在操作','请稍后...');

						Ext.Ajax.request({
							url: "children/register/updateCaseState.do",
							method: "POST",
							async: false,
							headers: {'Content-Type': 'application/json'},
							jsonData: {
								case_id:case_ids,
								verify_state:1
								},
							success: function (response, options) {
								response = Ext.JSON.decode(response.responseText);
								if (!response) {
									Ext.MessageBox.alert("错误信息", "提交审核出错，请联系管理员！");
									return;
								} 
							},
							failure: function () {
								Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
							}
						});
						Ext.Ajax.request({
							url: "activiti/main/claimAndComplete.do",
							method: "POST",
							async: false,
							headers: {'Content-Type': 'application/json'},
							jsonData: values,
							success: function (response, options) {
								response = Ext.JSON.decode(response.responseText);
								if (response.result == true) {
									Ext.MessageBox.alert("提示信息", response.message);
									me.getStore().load();
								} else {
									Ext.MessageBox.alert("错误信息", response.message);
									me.getStore().load();
								}
							},
							failure: function () {
								Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
							}
						});
					}
				});
		
		
	},
	onTaskHistory: function () {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		}
		var values = {
			processInstanceId: selections[0].get("process_instance_id")
		};
		if (values.processInstanceId == null || values.processInstanceId == "") {
			Ext.Msg.alert("提示", "该记录不能进行此项操作!");
			return;
		}
		Ext.Ajax.request({
			url: "activiti/main/taskDetail.do",
			method: "POST",
			headers: {'Content-Type': 'application/json'},
			jsonData: values,
			success: function (response, options) {
				response = Ext.JSON.decode(response.responseText);
				var win = Ext.create("Ext.window.Window", {
					title: '流程步骤',
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
	onExport:function(){
		window.location.href = "children/register/exportInfo.do?"+exportChildren;
	},
	createFinanceDaily:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1 ){
			Ext.Msg.alert("提示", "请选择一条需要生成的记录!");
			return;
		}
		var case_id=selections[0].get("case_id");
		var child_name=selections[0].get("child_name");
		Ext.Msg.show({
			title : '提示(儿童姓名：'+child_name+')',
			msg : '确定生成该案例汇款?',
			width : 300,
			buttons : Ext.Msg.OKCANCEL,
			fn : function(buttonId, text, opt) {
				if (buttonId == 'ok') {
					Ext.MessageBox.wait('正在操作','请稍后...');
					Ext.Ajax.request({
								url : "judicial/finance/createChildrenFinanceDaily.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {"case_id":case_id},
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									if (response.result) {
										Ext.MessageBox.alert("提示信息", response.message);
									} else {
										Ext.MessageBox.alert("错误信息", response.message);
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
	},
	onFind : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		};
		var win = Ext.create("Ext.window.Window", {
					title : "监护信息（案例条形码：" + selections[0].get("case_code")
							+ "）",
					width : 600,
					iconCls : 'Find',
					height : 400,
					layout : 'border',
					bodyStyle : "background-color:white;",
					items : [Ext.create('Ext.grid.Panel', {
								renderTo : Ext.getBody(),
								width : 600,
								height : 400,
								frame : false,
								viewConfig : {
									forceFit : true,
									stripeRows : true
									// 在表格中显示斑马线
								},
								store : {// 配置数据源
									fields : ['custody_id', 'custody_name',
											'custody_callname', 'id_number',
											'custody_call', 'phone'],// 定义字段
									proxy : {
										type : 'jsonajax',
										actionMethods : {
											read : 'POST'
										},
										url : 'children/register/getCustodyInfo.do',
										params : {
											'case_id' : selections[0]
													.get("case_id")
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
											header : "监护人姓名",
											dataIndex : 'custody_name',
											flex : 1,
											menuDisabled : true
										}, {
											header : "称谓",
											dataIndex : 'custody_callname',
											flex : 1,
											menuDisabled : true
										}, {
											header : "身份证",
											dataIndex : 'id_number',
											flex : 2,
											menuDisabled : true
										}, {
											header : "电话",
											dataIndex : 'phone',
											flex : 1,
											menuDisabled : true
										}]
							})]
				});
		win.show();
	},
	onFindGather : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		};
		if (selections[0].get("gather_id") == null
				|| selections[0].get("gather_id") == '') {
			Ext.Msg.alert("提示", "此案例无采集人员!");
			return;
		}
		Ext.Ajax.request({
			url : "children/register/getGatherInfo.do",
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : {
				gather_id : selections[0].get("gather_id")
			},
			success : function(response, options) {
				response = Ext.JSON.decode(response.responseText);
				var win = Ext.create("Ext.window.Window", {
							title : "采集人员信息（案例条形码："
									+ selections[0].get("case_code") + "）",
							width : 600,
							iconCls : 'Find',
							height : 190,
							bodyStyle : "background-color:white;",
							items : [{
										xtype : "container",
										layout : "column",
										height : 35,
										items : [{
													xtype : "displayfield",
													columnWidth : .45,
													labelAlign : 'right',
													labelWidth : 80,
													fieldLabel : "姓名",
													value : response.gather_name
												}]
									}, {
										xtype : "container",
										layout : "column",
										height : 35,
										items : [{
													xtype : "displayfield",
													columnWidth : .45,
													labelAlign : 'right',
													labelWidth : 80,
													fieldLabel : "身份证号",
													value : response.id_number
												}]
									}, {
										xtype : "container",
										layout : "column",
										height : 35,
										items : [{
													xtype : "displayfield",
													columnWidth : .45,
													labelAlign : 'right',
													labelWidth : 80,
													fieldLabel : "电话",
													value : response.phone
												}]
									}, {
										xtype : "container",
										layout : "column",
										height : 35,
										items : [{
													xtype : "displayfield",
													columnWidth : .45,
													labelAlign : 'right',
													labelWidth : 80,
													fieldLabel : "所属单位",
													value : response.company_name
												}]
									}]
						});
				win.show();
			},
			failure : function() {
				Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
			}
		});
	},
	onDelete : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
			return;
		};
		if (selections[0].get("print_time") != ""&&selections[0].get("print_time") != null) {
			Ext.Msg.alert("提示", "此案例已打印，无法删除!");
			return;
		}
		var values = {
			case_id : selections[0].get("case_id")
		};
		Ext.MessageBox.confirm('提示', '确定删除此案例吗', function(id) {
					if (id == 'yes') {
						Ext.MessageBox.wait('正在操作','请稍后...');
						Ext.Ajax.request({
									url : "children/register/deleteCaseInfo.do",
									method : "POST",
									headers : {
										'Content-Type' : 'application/json'
									},
									jsonData : values,
									success : function(response, options) {
										response = Ext.JSON
												.decode(response.responseText);
										if (response == true) {
											Ext.MessageBox.alert("提示信息",
													"废除成功！");
											me.getStore().load();
										} else {
											Ext.MessageBox.alert("错误信息",
													"废除失败！");
										}
									},
									failure : function() {
										Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
									}
								});
					}
				}, {
					xtype : 'panel',
					region : "center",
					border : false
				});
	},
	onInsert : function() {
		var me = this.up("gridpanel");
		ownpersonTemp="";
		ownaddressTemp="";
		var form = Ext.create("Rds.children.form.ChildrenRegisterInsertForm", {
					region : "center",
					autoScroll : true,
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '案例添加',
					width : 760,
					iconCls : 'Pageadd',
					height : 550,
					layout : 'border',
					items : [form],
					maximizable :true,
					maximized:true,
				});
		win.show();
	},
	onUpdate : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
			return;
		}
		if (selections[0].get("task_def_key") && selections[0].get("task_def_key") != "taskRegister") {
			Ext.Msg.alert("提示", "该状态不允许修改!");
			return;
		}
		if(selections[0].get("is_delete")==1){
			Ext.Msg.alert("提示", "此案例已删除，无法修改!");
			return;
		}		
		ownpersonTemp=selections[0].get("case_userid") ;
		ownaddressTemp=selections[0].get("case_areacode") ;
		var form = Ext.create("Rds.children.form.ChildrenRegisterUpdateForm", {
					region : "center",
					autoScroll : true,
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '案例修改',
					width : 760,
					iconCls : 'Pageedit',
					height : 550,
					layout : 'border',
					items : [form],
					maximizable :true,
					maximized:true
				});
		form.loadRecord(selections[0]);
		win.show();
		form.loadRecord(selections[0]);
		// form.get("province");
	},
	onPhotoManage : function() {
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
			title : "案例照片管理",
			width : 1000,
			iconCls : 'Find',
			height : 300,
			modal:true,
			resizable:false,
			layout : 'border',
			bodyStyle : "background-color:white;",
			items : [ Ext.create('Ext.grid.Panel', {
				renderTo : Ext.getBody(),
				width : 1000,
				height : 300,
				frame : false,
				viewConfig : {
					forceFit : true,
					stripeRows : true// 在表格中显示斑马线
				},
				store : {// 配置数据源
					fields : [ 'photo_id', 'case_id', 'photo_path','upload_time','photo_type','upload_username','child_name' ],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'children/register/queryCasePhoto.do',
						params : {
							'case_id' : selections[0].get("case_id")
						},
						reader : {
//							type : 'json',
//							root : 'items',
//							totalProperty : 'count'
						}
					},
					autoLoad : true
				},
				columns : [{
								text : '儿童姓名',
								dataIndex : 'child_name',
								width : '10%',
								menuDisabled : true,
							},
				           {
							text : '类型',
							dataIndex : 'photo_type',
							width : '10%',
							menuDisabled : true,
							renderer : function(value) {
								switch (value) {
									case "1" :
										return "初始照片";
										break;
									case "2" :
										return "处理后照片";
										break;
									case "5" :
										return "登记表格";
										break;
									default :
										return "";
								}
							}
						}, {
							text : '附件',
							dataIndex : 'photo_path',
							width : '30%',
							menuDisabled : true,
						}, {
							text : '最后上传日期',
							dataIndex : 'upload_time',
							width : '20%',
							menuDisabled : true,
						},
						{
							text : '上传人员',
							dataIndex : 'upload_username',
							width : '10%',
							menuDisabled : true,
						}, {
							header : "查看照片",
							dataIndex : '',
							width : '10%',
							menuDisabled : true,
							renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								if(record.data["photo_type"] != null){
									return "<a href='#'>查看</a>";
								}
							},
							listeners:{
								'click':function(){ 
									var me = this.up("gridpanel");
									var selections = me.getView().getSelectionModel().getSelection();
									if (selections.length < 1 || selections.length > 1) {
										Ext.Msg.alert("提示", "请选择需要查看的一条记录!");
										return;
									}
									var form = Ext.create(
											"Rds.children.form.ChildrenImageShow", {
												region : "center",
												grid : me
											});
									var win = Ext.create("Ext.window.Window", {
												title : '图片查看',
												width : 600,
												iconCls : 'Pageedit',
												height : 600,
												maximizable : true,
												layout : 'border',
												items : [form]
											});
									form.loadRecord(selections[0]);
									win.show();
								}
							}
						} ,{
							header : "操作",
							dataIndex : '',
							width : '10%',
							menuDisabled : true,
							renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								if(record.data["photo_type"]==1 || record.data["photo_type"]==5 || record.data["photo_type"] == null || record.data["photo_type"] == 0){
									return "<a href='#'>重新上传</a>";
								}
							},
							listeners:{
								'click':function(){ 
									var mei = this.up("gridpanel");
									var selections = mei.getView().getSelectionModel().getSelection();
									if (selections.length < 1) {
										Ext.Msg.alert("提示", "请选择案例!");
										return;
									}
									if(selections[0].get("photo_type")==1 || selections[0].get("photo_type")==5 ||selections[0].get("photo_type") == null || selections[0].get("photo_type") == 0)
									{
										var form = Ext.create('Ext.form.Panel', {
											region : "center",
											autoScroll : true,
											layout : 'anchor',
											bodyPadding : 10,
											defaults : {
												anchor : '100%'
											},
											defaultType : 'textfield',
											items : [{
														xtype : "hiddenfield",
														name : 'case_id'
													},{
														xtype : "hiddenfield",
														name : 'photo_id'
													},{
														xtype : "hiddenfield",
														name : 'photo_type'
													},{
														xtype : "hiddenfield",
														name : 'photo_path'
													},{
														xtype : "textfield",
														labelWidth : 80,
														fieldLabel : '儿童姓名',
														readOnly : true,
														name : 'child_name'
													},{
														xtype : 'filefield',
														name : 'headPhoto',
														fieldLabel : '一寸照片',
														msgTarget : 'side',
														labelWidth : 80,
														allowBlank : false,
														anchor : '100%',
														buttonText : '选择照片',
														validator : function(v) {
															if (!v.endWith(".jpg") &&!v.endWith(".JPG")
																	&& !v.endWith(".png") && !v.endWith(".PNG")
																	&& !v.endWith(".gif") && !v.endWith(".GIF")
																	&& !v.endWith(".jpeg") && !v.endWith(".JPEG") ) {
																return "请选择.jpg .png .gif.jpeg类型的图片";
															}
															return true;
														}
													}],
	
											buttons : [{
												text : '上传',
												iconCls : 'Diskupload',
												handler : function() {
													var me = this;
													var myWindow = me.up('window');
													var form = me.up('form').getForm();
													if (!form.isValid()) {
														Ext.MessageBox.alert("提示信息", "请选择照片!");
														return;
													}
													form.submit({
																url : 'children/register/photoUpload.do',
																method : 'post',
																waitMsg : '正在上传您的文件...',
																success : function(form, action) {
																	Ext.Msg.alert("提示", "上传成功!");
																	var grid = mei.up("gridpanel");
																	mei.getStore().load();
																	myWindow.close();
																},
																failure : function() {
																	Ext.Msg.alert("提示", "上传失败，请联系管理员!");
																	myWindow.close();
																}
															});
	
												}
											}, {
												text : '取消',
												iconCls : 'Cancel',
												handler : function() {
													this.up('window').close();
												}
											}]
										});
										var win = Ext.create("Ext.window.Window", {
													title : '案例添加',
													width : 400,
													iconCls : 'Pageadd',
													height : 300,
													modal : true,
													layout : 'border',
													items : [form]
												});
										form.loadRecord(selections[0]);
										win.show();
									}
								}
							}
						
						}]
			}) ]
		});
		win.show();
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage = 1;
		me.getStore().load();
	},
	listeners : {
		'afterrender' : function() {
			this.store.load();
		},
	'cellclick': function (grid, td, cellIndex, record, tr, rowIndex, e) {
		//查看审核不通过原因
		if (e.getTarget('.lbtnComment')) {
			Ext.create("Ext.window.Window", {
				title: '审核不通过原因',
				width: 500,
				height: 250,
				modal: true,
				layout: 'fit',
				items: [{
					xtype: "gridpanel",
					columns: [
						{
							header: "审核时间",
							dataIndex: "timeString",
							align: 'center',
							width: 150,
							sortable: false,
							menuDisabled: true
						},
						{
							header: "审核人",
							dataIndex: "userId",
							align: 'center',
							width: 100,
							sortable: false,
							menuDisabled: true
						},
						{
							header: "审核意见",
							dataIndex: "fullMessage",
							align: 'center',
							flex: 1,
							sortable: false,
							menuDisabled: true,
							 renderer : function(value, cellmeta, record,
										rowIndex, columnIndex, store) {
//									var str = value;
//									if (value.length > 15) {
//										str = value.substring(0, 15) + "...";
//									}
								 if(null == value) return "";
								 else 
									return "<span title='" + value + "'>" + value
											+ "</span>";
								}
						}
					],
					store: Ext.create('Ext.data.Store', {
						fields: ['fullMessage', 'userId', 'time', {
							name: 'timeString', type: 'date',
							convert: function (v, rec) {
								return rec.data.time == null ? "" : Ext.Date.format(new Date(rec.data.time), 'Y-m-d H:i');
							}
						}],
						autoLoad: true,
						proxy: {
							type: 'jsonajax',
							actionMethods: {
								read: 'POST'
							},
							url: 'activiti/main/getProcessInstanceComments.do',
							params: {"processInstanceId": record.data.process_instance_id},
							reader: {
								type: 'json'
							}
						}
					})
				}]
			}).show();
		}
	}
	}
});
