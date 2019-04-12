mail_canel = function(me) {
	me.up("window").close();
}
case_canel = function(me) {
	me.up("window").close();
}
var mailStore = Ext.create('Ext.data.Store', {
	model : 'model',
	proxy : {
		type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'judicial/dicvalues/getMailModels.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	autoLoad : true,
	remoteSort : true
});
queryMailInfo = function(value, mail_type) {
	var win = Ext.create("Ext.window.Window", {
				title : "快递信息（快递单号：" + value + "）",
				width : 700,
				iconCls : 'Find',
				height : 350,
				layout : 'border',
				modal:true,
				border : false,
				bodyStyle : "background-color:white;",
				items : [Ext.create('Ext.grid.Panel', {
									width : 700,
									height : 350,
									frame : false,
									renderTo : Ext.getBody(),
									viewConfig : {
										forceFit : true,
										stripeRows : true
										,// 在表格中显示斑马线
									},
									store : {// 配置数据源
										fields : ['time', 'content'],// 定义字段
										proxy : {
											type : 'jsonajax',
											actionMethods : {
												read : 'POST'
											},
											url : 'judicial/mail/getMailInfo.do',
											params : {
												'mail_code' : value,
												'mail_type' : mail_type
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
												header : "时间",
												dataIndex : 'time',
												flex : 1.5,
												menuDisabled : true
											}, {
												header : "地点",
												dataIndex : 'content',
												flex : 3,
												menuDisabled : true
											}]
								})]
			});
	win.show();
}

queryAllMail = function(sels) {
	mail_onDel = function(me) {
		var grid = me.up("gridpanel");
		var selections = grid.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
			return;
		}
		Ext.MessageBox.confirm('提示','确定删除此案例邮寄信息吗?',
				function(id) {
					if (id == 'yes') {
						Ext.MessageBox.wait('正在操作','请稍后...');
						Ext.Ajax.request({
							url : "judicial/mail/delMailInfo.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : {
								"mail_id" : selections[0].get("mail_id"),
								"case_id":sels.get("case_id")
							},
							success : function(response, options) {
								response = Ext.JSON.decode(response.responseText);
								if (response.result == true) {
									Ext.MessageBox.alert("提示信息", response.message);
									var grid = me.up("gridpanel");
									grid.getStore().load();
								} else {
									Ext.MessageBox.alert("错误信息",  response.message);
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
					}
		})
		
	}
	mail_onUpdate = function(me) {
		var grid = me.up("gridpanel");
		var selections = grid.getView().getSelectionModel().getSelection();

		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		}
		mail_update = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["case_id"] = sels.get("case_id");
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "judicial/mail/updateMailInfo.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response == true) {
									Ext.MessageBox.alert("提示信息", "修改邮件成功");
									var grid = me.up("gridpanel");
									grid.getStore().load();
									mailform_update.close();
								} else {
									Ext.MessageBox.alert("错误信息", "修改邮件失败");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
			}
		}
		var mailform_update = Ext.create("Ext.window.Window", {
			title : '快递信息',
			width : 320,
			height : 260,
			layout : 'border',
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
							handler : mail_update
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : mail_canel
						}],
				items : [new Ext.form.field.ComboBox({
							fieldLabel : '快递类型',
							labelWidth : 80,
							editable : false,
							triggerAction : 'all',
							labelAlign : 'right',
							value : selections[0].get("mail_type"),
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field is
							valueField : "key",
							displayField : "value",
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field is
							store : mailStore,
							mode : 'local',
							// typeAhead: true,
							name : 'mail_type'
						}), {
					xtype : "textfield",
					fieldLabel : '快递单号',
					labelAlign : 'right',
					maxLength : 18,
					value : selections[0].get("mail_code"),
					labelWidth : 80,
					name : 'mail_code'
				},{
					xtype : "textfield",
					fieldLabel : '收件人',
					labelAlign : 'right',
					maxLength : 18,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					labelWidth : 80,
					name : 'mail_per',
					value: selections[0].get("mail_per")
				},{
					xtype : "textfield",
					fieldLabel : '联系方式',
					labelAlign : 'right',
					maxLength : 18,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					labelWidth : 80,
					name : 'mail_phone',
					value : selections[0].get("mail_phone")
				},{
					xtype : "textfield",
					fieldLabel : '收件地址',
					labelAlign : 'right',
					maxLength : 50,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					labelWidth : 80,
					name : 'mail_address',
					value : selections[0].get("mail_address")
				}, {
					xtype : 'hiddenfield',
					name : 'mail_id',
					value : selections[0].get("mail_id")
				}]
			}]
		})
		mailform_update.show();
	}
	mail_onInsert = function(me) {
		var express_recive='';
		var express_concat='';
		var express_remark='';
		Ext.Ajax.request({
			url : "judicial/mail/querySampleRecive.do",
			async: false,
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : {case_id:sels.get("case_id")},
			success : function(response, options) {
				response = Ext.JSON.decode(response.responseText);
				if(response.length>0)
				{
					console.log(response[0].express_recive);
					express_recive=response[0].express_recive;
					express_concat=response[0].express_concat;
					express_remark=response[0].express_remark;
				}
					
			},
			failure : function() {
				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
			}
		});
		mail_save = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["case_id"] = sels.get("case_id");
			values["case_code"] = sels.get("case_code");
			
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "judicial/mail/saveMailInfo.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON.decode(response.responseText);
								if (response == true) {
									Ext.MessageBox.alert("提示信息", "添加邮件成功");
									var grid = me.up("gridpanel");
									grid.getStore().load();
									mailform_add.close();
								} else {
									Ext.MessageBox.alert("错误信息", "添加邮件失败");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
			}
		}
		var mailform_add = Ext.create("Ext.window.Window", {
			title : '快递信息',
			width : 320,
			height : 260,
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
							handler : mail_save
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : mail_canel
						}],
				items : [new Ext.form.field.ComboBox({
							fieldLabel : '快递类型',
							labelWidth : 80,
							editable : false,
							triggerAction : 'all',
							labelAlign : 'right',
							// required!
							valueField : "key",
							displayField : "value",
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field is
												// required!
							store : mailStore,
							mode : 'local',
							// typeAhead: true,
							name : 'mail_type'
						}), {
					xtype : "textfield",
					fieldLabel : '快递单号',
					labelAlign : 'right',
					maxLength : 18,
					labelWidth : 80,
					name : 'mail_code'
				},{
					xtype : "textfield",
					fieldLabel : '收件人',
					labelAlign : 'right',
					maxLength : 18,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					labelWidth : 80,
					name : 'mail_per',
					value:express_recive
				},{
					xtype : "textfield",
					fieldLabel : '联系方式',
					labelAlign : 'right',
					maxLength : 18,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					labelWidth : 80,
					name : 'mail_phone',
					value:express_concat
				},{
					xtype : "textfield",
					fieldLabel : '收件地址',
					labelAlign : 'right',
					maxLength : 50,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					labelWidth : 80,
					name : 'mail_address',
					value:express_remark
				}]
			}]
		})
		mailform_add.show();
	}
	var address=sels.get("address");
	if(address.length>20){
			address="<span title='"+address+"'>"+address.substring(0,20)+"..</span>";
	}
	var gather_status = sels.get("gather_status");
	var fee_type = sels.get("fee_type");
	var type_remark="正常";
	if(fee_type==1){
		type_remark="先出报告后付款";
	}
	if(fee_type==2){
		type_remark="免单";
	}
	var ispay="未付款("+type_remark+")";
	if (gather_status ==0||gather_status==2) {
		ispay="已付款("+type_remark+")";
	}
	var case_receiver=sels.get("case_receiver");
	var agent= sels.get("agent");
	if (agent != ''&& agent!=null) {
		case_receiver=case_receiver+"(代理："+agent+")";
	}
	var win = Ext.create("Ext.window.Window", {
		title : "快递信息（案例条形码：" + sels.get("case_code") + "）",
		width : 600,
		iconCls : 'Find',
		height : 500,
		layout : 'border',
		modal:true,
		bodyStyle : "background-color:white;",
		items : [Ext.create("Ext.form.Panel", {
		    width: 580,
		    height: 120,
		    border:"black",
		    region : 'north',
		    defaults : {
				anchor : '100%',
				labelWidth : 80,
				labelAlign: 'right'
			},
			autoHeight : true,
		    items: [
		            {
		                xtype: "container",
		                layout: "column",
		                height: 35,
		                items: [
		                        { xtype: "displayfield",columnWidth : .45,labelAlign: 'right',
									labelWidth : 80,  name:"client",value:sels.get("client"),fieldLabel: "委托人",},
		                        { xtype: "displayfield",columnWidth : .45,labelAlign: 'right',
									labelWidth : 80,value:ispay, fieldLabel: "是否付款",}
		                ]
		            },
		            {
		                xtype: "container",
		                layout: "column",
		                height: 35,
		                items: [
		                    { xtype: "displayfield",columnWidth : .45, labelAlign: 'right',
								labelWidth : 80,  value:case_receiver, fieldLabel: "归属人",},
		                    { xtype: "displayfield",columnWidth : .45, labelAlign: 'right',
									labelWidth : 80, value:sels.get("receiver_area"), fieldLabel: "案例归属地", }
		                ]
		            },
//		            {
//		            	xtype: "hidden",
//		                layout: "column",
//		                height: 35,
//		                items: [
//		                    { xtype: "hidden",columnWidth : .45,labelAlign: 'right',
//								labelWidth : 80,  value:sels.get("achieve"),fieldLabel: "收件人",},
//		                    { xtype: "hidden",columnWidth : .45,labelAlign: 'right',
//									labelWidth : 80, value:sels.get("achieve_phone"), fieldLabel: "电话",},
//		                ]
//		            },
		            {
		            	xtype: "container",
		                layout: "column",
		                height: 35,
		                items: [
		                        { xtype: "hidden", columnWidth :.45,labelAlign: 'right',
									labelWidth : 80, value:address,fieldLabel: "寄送地址",},  
		                    { xtype: "displayfield",columnWidth : .45,labelAlign: 'right',
										labelWidth : 80, value:sels.get("accept_time"),fieldLabel: "受理时间",}
		                ]
		            }
		        ]
		}),Ext.create('Ext.grid.Panel', {
					 region : 'north',
					width : 600,
					height : 350,
					frame : false,
					viewConfig : {
						forceFit : true,
						stripeRows : true
						,// 在表格中显示斑马线
					},
					dockedItems : [{
								xtype : 'toolbar',
								dock : 'top',
								items : [{
											xtype : 'button',
											text : '新增',
											iconCls : 'Pageadd',
											handler : mail_onInsert
										}, {
											xtype : 'button',
											text : '修改',
											iconCls : 'Pageedit',
											handler : mail_onUpdate
										}, {
											xtype : 'button',
											text : '删除',
											iconCls : 'Delete',
											handler : mail_onDel
										}

								]
							}],
					store : {// 配置数据源
						fields : ['mail_type', 'mail_code', 'mail_typename',
								'time', 'mail_id','mail_per','mail_address','mail_phone'],// 定义字段
						proxy : {
							type : 'jsonajax',
							actionMethods : {
								read : 'POST'
							},
							url : 'judicial/mail/getAllMails.do',
							params : {
								'case_id' : sels.get("case_id")
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
								header : "快递类型",
								dataIndex : 'mail_typename',
								flex : 1,
								menuDisabled : true
							}, {
								header : "快递编号",
								dataIndex : 'mail_code',
								flex : 1.5,
								menuDisabled : true
							}, {
								header : "收件人",
								dataIndex : 'mail_per',
								flex : 1,
								menuDisabled : true
							},{
								header : "联系方式",
								dataIndex : 'mail_phone',
								flex : 1,
								menuDisabled : true
							},{
								header : "收件地址",
								dataIndex : 'mail_address',
								flex : 1,
								menuDisabled : true
							},{
								header : "快递时间",
								dataIndex : 'time',
								flex : 1,
								menuDisabled : true
							}, {
								header : "操作",
								dataIndex : 'mail_code',
								flex : 0.5,
								menuDisabled : true,
								renderer : function(value, cellmeta, record,
										rowIndex, columnIndex, store) {
									var mail_type = record.data["mail_type"];
									if(mail_type=='ziqu'){
										return "";
									}else{
										return "<a href='#' onclick=\"queryMailInfo('"
										+ value
										+ "','"
										+ mail_type
										+ "')\"  >查询";
									}
								}
							}]
				})]
	});
	win.show();
};
var case_code = Ext.create('Ext.form.field.Text', {
	name : 'casecode',
	labelWidth : 60,
	width : '20%',
	fieldLabel : '案例编号'
});
//var receiver = Ext.create('Ext.form.field.Text', {
//	name : 'receiver',
//	labelWidth : 60,
//	width : 145,
//	fieldLabel : '归属人'
//});
var receiver = Ext.create('Ext.form.ComboBox', {
	xtype : 'combo',
	fieldLabel : '归属人',
	labelWidth : 60,
	width : '20%',
	name : 'receiver',
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
	hideTrigger : true,
	minChars : 2,
	matchFieldWidth : true,
	listConfig : {
		loadingText : '正在查找...',
		emptyText : '没有找到匹配的数据'
	}
});
var client = Ext.create('Ext.form.field.Text', {
	name : 'client',
	labelWidth : 60,
	width : '20%',
	fieldLabel : '委托人'
});
var starttime=Ext.create('Ext.form.DateField', {
	name : 'starttime',
	width : '20%',
	labelWidth : 70,
	fieldLabel : '受理时间 从',
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
var mail_starttime=Ext.create('Ext.form.DateField', {
	name : 'starttime',
	width : '20%',
	labelWidth : 70,
	fieldLabel : '邮寄时间 从',
	labelAlign : 'right',
	emptyText : '请选择日期',
	format : 'Y-m-d',
	maxValue : new Date(),
	listeners : {
		select : function() {
			var start = mail_starttime
					.getValue();
			var end = mail_endtime
					.getValue();
			mail_endtime.setMinValue(
					start);
		}
	}
});
var mail_endtime=Ext.create('Ext.form.DateField', {
	name : 'endtime',
	width : '20%',
	labelWidth : 20,
	fieldLabel : '到',
	labelAlign : 'right',
	emptyText : '请选择日期',
	format : 'Y-m-d',
	maxValue : new Date(),
	value : Ext.Date.add(new Date(), Ext.Date.DAY,+1),
	listeners : {
		select : function() {
			var start = mail_starttime
					.getValue();
			var end = mail_endtime
					.getValue();
			mail_starttime.setMaxValue(
					end);
		}
	}
});
var is_mail=Ext.create('Ext.form.ComboBox', 
		{
			fieldLabel : '是否邮寄',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
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
										'已邮寄',
										0 ],
								[
										'未邮寄',
										1 ] ]
					}),
			value : -1,
			mode : 'local',
			// typeAhead: true,
			name : 'urgent_state',
		});
 var mail_store=Ext.create('Ext.data.Store', {
	fields : ['case_id', 'case_code', 'case_areaname', 'case_areacode','client',
			'receiver_area', 'gather_status', 'case_receiver','achieve','achieve_phone',
			'mail_count', 'urgent_state', 'accept_time', 'close_time',"agent",
			'address', 'case_in_per', "receiver_id", 'sample_in_time','remark','fee_type',
			'is_delete'],
	start:0,
	limit:15,
	pageSize:15,
	proxy : {
		type : 'jsonajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'judicial/mail/getMailCaseInfo.do',
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
			Ext.apply(mail_store.proxy.extraParams, {
				endtime : dateformat(endtime
						.getValue()),
				is_mail : is_mail
						.getValue(),
				starttime : dateformat(starttime
						.getValue()),
				mail_starttime : dateformat(mail_starttime
								.getValue()),	
				mail_endtime : dateformat(mail_endtime
										.getValue()),				
				client:	trim(client.getValue()),
				receiver : receiver.getValue(),
				case_code :  trim(case_code
						.getValue())
			});
		}
	}
});
 search_mail=function(me){
	 var grid = me.up("gridpanel");
	 grid.getStore().currentPage=1;
	 grid.getStore().load();
	// can_mail_store.currentPage=1;
	 //can_mail_store.load();
 }
 search_sample=function(me){
		var grid = me.up("gridpanel");
		var selections = grid.getView().getSelectionModel()
		.getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		};
		var win = Ext.create("Ext.window.Window", {
			title : "样本信息（案例条形码：" + selections[0].get("case_code") + "）",
			width : 600,
			iconCls : 'Find',
			height : 400,
			layout : 'border',
			modal:true,
			bodyStyle : "background-color:white;",
			items : [ Ext.create('Ext.grid.Panel', {
				renderTo : Ext.getBody(),
				width : 600,
				height : 400,
				frame : false,
				viewConfig : {
					forceFit : true,
					stripeRows : true// 在表格中显示斑马线
				},
				store : {// 配置数据源
					fields : [ 'sample_id', 'sample_code', 'sample_type','sample_typename',
							'sample_call', 'sample_callname', 'sample_username',
							'id_number', 'birth_date' ],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'judicial/register/getSampleInfo.do',
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
				columns : [// 配置表格列
				{
					header : "样本条形码",
					dataIndex : 'sample_code',
					flex : 1,
					menuDisabled : true
				}, {
					header : "称谓",
					dataIndex : 'sample_callname',
					flex : 1,
					menuDisabled : true
				}, {
					header : "姓名",
					dataIndex : 'sample_username',
					flex : 1,
					menuDisabled : true
				}, {
					header : "身份证号",
					dataIndex : 'id_number',
					flex : 2,
					menuDisabled : true
				}, {
					header : "出生日期",
					dataIndex : 'birth_date',
					flex : 1,
					menuDisabled : true
				}, {
					header : "样本类型",
					dataIndex : 'sample_typename',
					flex : 1,
					menuDisabled : true
				} ]
			}) ]
		});
		win.show();
 }
 mail_manage=function(me){
	    var grid = me.up("gridpanel");
		var selections = grid.getView().getSelectionModel().getSelection();
		console.log(selections[0].get("case_id"))
		if (selections.length >= 1) {
			if(selections[0].get("fee_type")==2  || selections[0].get("gather_status")==0 || selections[0].get("fee_type")==1 || selections[0].get("fee_type")==4)
			{
				queryAllMail(selections[0]);
			}else
			{
				Ext.Msg.alert("提示", "该案例没有收费信息!");
				return;
			}
		}else
		{
			Ext.Msg.alert("提示", "请选择需要邮寄的记录!");
			return;
		}
 }
 insertException=function(me){
	    var grid = me.up("gridpanel");
		var selections = grid.getView().getSelectionModel()
		.getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要添加异常的记录!");
			return;
		};
		Ext.MessageBox.confirm('提示','确定此案例插入回款异常吗?',
				function(id) {
					if (id == 'yes') {
						if (selections[0].get("gather_status") !=0) {
							Ext.MessageBox.wait('正在操作','请稍后...');
							Ext.Ajax.request({
								url : "judicial/mail/insertException.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {"case_id":selections[0].get("case_id")},
								success : function(response, options) {
									response = Ext.JSON.decode(response.responseText);
									Ext.MessageBox.alert("提示信息", response.message);
								},
								failure : function() {
									Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
								}
							});
						}else{
							Ext.Msg.alert("提示", "该案例已付款!");
							return;
						}
					}
		})
		
		
	}
 onFind=function(me){
		var grid = me.up("gridpanel");
		var selections =  grid.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1 ){
			Ext.Msg.alert("提示", "请选择一条需要备注的记录!");
			return;
		};
		var remark=selections[0].get("remark");

		Ext.MessageBox.alert("我是备注", remark);
		}
 onRemark=function(me){
		var grid = me.up("gridpanel");
		var selections =  grid.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1 ){
			Ext.Msg.alert("提示", "请选择一条需要备注的记录!");
			return;
		};
		var case_id=selections[0].get("case_id");

		remark_confirm = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["case_id"]=case_id;
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "judicial/verify/updateCaseRemark.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {						
								response = Ext.JSON.decode(response.responseText);
								if (response) {
									Ext.MessageBox.alert("提示信息", response.message);
									var grid = me.up("gridpanel");
									grid.getStore().load();
									remark_add.close();
								} else {
									Ext.MessageBox.alert("错误信息", response.message);
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
			}
		}
		
		var remark_add = Ext.create("Ext.window.Window", {
			title : '备注信息',
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
							handler : remark_confirm
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : case_canel
						}],
				items : [{
					xtype : "textarea",
					fieldLabel : '备注',
					labelAlign : 'right',
					maxLength : 200,
					labelWidth : 80,
					height:130,
					allowBlank : false,
					regex:/^[^\s]*$/,
					regexText:'我不喜欢空格',
					name : 'remark',
					value:selections[0].get("confirm_remark")
				},{
					xtype : "hidden",
					name : 'remark_bak',
					value:selections[0].get("remark")
				}]
			}]
		})
		remark_add.show();

	}
Ext.define("Rds.judicial.panel.JudicialMailPanel", {
	extend : 'Ext.panel.Panel',
	layout : 'border',
	alias:'widget.slideGridView',  
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	items:[Ext.create('Ext.grid.Panel', {
			 	region: 'west',
			 	width:"100%",
			  	title:"<div style='text-align:center;'>邮寄案例信息</div>",
		    	bodyStyle:"border-color:grey;margin-right:5px",
				frame : false,
				viewConfig : {
					forceFit : true,
					stripeRows : true,// 在表格中显示斑马线
				},
				bbar: Ext.create('Ext.PagingToolbar', {
					store : mail_store,
					pageSize : 25,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				}),
				dockedItems: [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [case_code,receiver,starttime,endtime
					         ]
				},{
					xtype : 'toolbar',
					style : {
				        borderTopWidth : '0px !important',
				        borderBottomWidth : '1px !important'
				    },
					items : [client,is_mail,mail_starttime,mail_endtime,{
						text : '查询',
						iconCls : 'Find',
						handler : search_mail
					}  ]
				},{
					xtype : 'toolbar',
					dock : 'top',
					items : [  {
						text : '查看样本信息',
						iconCls : 'Find',
						handler : search_sample
					}, {
						text : '邮件管理',
						iconCls : 'Box',
						handler : mail_manage
					},{
						text : '插入异常',
						iconCls : 'Add',
						handler : insertException
					},{
						text : '添加备注',
						iconCls : 'Pageedit',
						handler : onRemark
					},
					{
						text : '复制备注',
						iconCls : 'Find',
						handler : onFind
					}]
				}],
				store : mail_store,
				columns : [{
					text : '案例条形码',
					dataIndex : 'case_code',
					menuDisabled : true,
					width : 130,
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
						var isnull = record.data["is_delete"];
						if (isnull == 1) {
							return "<div style=\"text-decoration: line-through;color: red;\">"
									+ value + "</div>"
						} else {
							return value;
						}
					}
				},{
					text : '委托人',
					dataIndex : 'client',
					menuDisabled : true,
					width : 130
				},{
					text : '案例归属地',
					dataIndex : 'receiver_area',
					menuDisabled : true,
					width : 130
				},{
					text : '归属人',
					dataIndex : 'case_receiver',
					menuDisabled : true,
					width : 80,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						var agent= record.data["agent"];
						if (agent != ''&& agent!=null) {
							return value+"(代理："+agent+")";
						} else {
							return value;
						}
					}
				},{
					text : '备注',
					dataIndex : 'remark',
					menuDisabled : true,
					width : 300,
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
						var str = value;
//						if (value.length > 15) {
//							str = value.substring(0, 15) + "...";
//						}
						return "<span style='color:red' title='" + value + "'>" + str
								+ "</span>";
					}
				}, {
					hidden:true,
					text : '收件联系人',
					dataIndex : 'achieve',
					menuDisabled : true,
					width : 130
				},{
					text : '付款信息',
					dataIndex : 'gather_status',
					menuDisabled : true,
					width : 130,
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
						var fee_type = record.data["fee_type"];
						var type_remark="正常";
						if(fee_type==1){
							type_remark="先出报告后付款";
						}
						if(fee_type==2){
							type_remark="免单";
						}
						if(fee_type==3){
							type_remark="优惠";
						}
						if(fee_type==4){
							type_remark="月结";
						}
						if(fee_type==5){
							type_remark="二次采样";
						}
						if(fee_type==6){
							type_remark="补样";
						}
						if (value ==0) {
							return "<span style='color:green'>已付款("+type_remark+")</span>";
						} else {
							return "<span style='color:red'>未付款("+type_remark+")</span>";
						}
					}
				}, {
					text : '紧急程度',
					dataIndex : 'urgent_state',
					menuDisabled : true,
					width : 100,
					renderer : function(value) {
						switch (value) {
							case 0 :
								return "普通";
								break;
							case 1 :
								return "<span style='color:red'>紧急</span>";
								break;
							default :
								return "";
						}
					}
				}, {
					text : '快递状态',
					dataIndex : 'mail_count',
					menuDisabled : true,
					width : 100,
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
						if (value > 0) {
							return "<span style='color:green'> 已邮寄" + value
									+ "份</span>";
						} else {
							return "<span style='color:red'> 尚未邮寄</span>";
						}
					}
				}, {
					hidden:true,
					text : '寄送地址',
					dataIndex : 'address',
					menuDisabled : true,
					width : 250
				}, {
					text : '受理日期',
					dataIndex : 'accept_time',
					menuDisabled : true,
					width : 200
				}, {
					text : '截止日期',
					dataIndex : 'close_time',
					menuDisabled : true,
					width : 200
				}],
				listeners : {
					'afterrender' : function() {
						this.store.load();
					}
				}
	   })] 
});
