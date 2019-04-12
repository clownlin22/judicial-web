var exprotChildrenMail;
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
			width : 400,
			height : 320,
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
				},{
					xtype : "textarea",
					fieldLabel : '收件地址',
					labelAlign : 'right',
					maxLength : 50,
					labelWidth : 80,
					name : 'mail_remark',
					value : selections[0].get("mail_remark")
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
		mail_save = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["case_id"] = sels.get("case_id");
			values["case_code"] = sels.get("case_code");
			values["case_type"]='children';
			console.log(values);
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
			width : 400,
			height : 320,
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
					name : 'mail_per'
				},{
					xtype : "textfield",
					fieldLabel : '联系方式',
					labelAlign : 'right',
					maxLength : 18,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					labelWidth : 80,
					name : 'mail_phone'
				},{
					xtype : "textfield",
					fieldLabel : '收件地址',
					labelAlign : 'right',
					maxLength : 50,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
					labelWidth : 80,
					name : 'mail_address'
				},{
					xtype : "textarea",
					fieldLabel : '备注',
					labelAlign : 'right',
					maxLength : 500,
					labelWidth : 80,
					name : 'mail_remark'
				}]
			}]
		})
		mailform_add.show();
	}
//	var address=sels.get("address");
//	if(address.length>20){
//			address="<span title='"+address+"'>"+address.substring(0,20)+"..</span>";
//	}
	var status = sels.get("status");
	var fee_type = sels.get("fee_type");
	var type_remark="正常";
	if(fee_type==1){
		type_remark="先出报告后付款";
	}
	if(fee_type==2){
		type_remark="免单";
	}
	var ispay="未付款("+type_remark+")";
	if (status ==0||status==2) {
		ispay="已付款("+type_remark+")";
	}
	var case_receiver=sels.get("case_username");
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
									labelWidth : 80,  name:"client",value:sels.get("child_name"),fieldLabel: "委托人",},
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
									labelWidth : 80, value:sels.get("case_areaname"), fieldLabel: "案例归属地", }
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
									labelWidth : 80,fieldLabel: "寄送地址",},  
		                    { xtype: "displayfield",columnWidth : .45,labelAlign: 'right',
										labelWidth : 80, value:sels.get("gather_time"),fieldLabel: "采集时间",}
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
								'time', 'mail_id','mail_per','mail_address','mail_phone','mail_remark'],// 定义字段
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
								header : "快递备注",
								dataIndex : 'mail_remark',
								flex : 1,
								menuDisabled : true,
								renderer : function(value, cellmeta, record, rowIndex, columnIndex,
										store) {
									var str = value==null?"":value;
									return "<span title='" + value + "'>" + str
											+ "</span>";
								}
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
Ext.define("Rds.children.panel.ChildrenMailGridPanel",{
					extend : "Ext.grid.Panel",
					loadMask : true,
					viewConfig : {
						trackOver : false,
						stripeRows : false
					},
					region:'center',
					pageSize : 25,
					initComponent : function() {
						var me = this;
						var case_code = Ext.create('Ext.form.field.Text', {
							name : 'casecode',
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
						var childname = Ext.create('Ext.form.field.Text', {
							name : 'childname',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '孩子姓名'
						});
						var case_areaname = Ext.create('Ext.form.field.Text', {
							name : 'case_areaname',
							labelWidth : 60,
							width : '20%',
							fieldLabel : '归属地'
						});
						var mail_starttime=Ext.create('Ext.form.DateField', {
							name : 'mail_starttime',
							width : '20%',
							labelWidth : 70,
							fieldLabel : '邮寄日期 从',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date()
						});
						var mail_endtime=Ext.create('Ext.form.DateField', {
							name : 'mail_endtime',
							width : '20%',
							labelWidth : 20,
							fieldLabel : '到',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							value : Ext.Date.add(new Date(), Ext.Date.DAY,1)
						});
						var starttime=Ext.create('Ext.form.DateField', {
							name : 'starttime',
							width : '20%',
							labelWidth : 70,
							fieldLabel : '采集日期 从',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							maxValue : new Date()
						});
						var endtime=Ext.create('Ext.form.DateField', {
							name : 'endtime',
							width : '20%',
							labelWidth : 20,
							fieldLabel : '到',
							labelAlign : 'right',
							emptyText : '请选择日期',
							format : 'Y-m-d',
							value : Ext.Date.add(new Date(), Ext.Date.DAY,1)
						});
						var child_sex=Ext.create('Ext.form.ComboBox', {
									fieldLabel : '小孩性别',
									width : '20%',
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
																'女',
																0 ],
														[
																'男',
																1 ] ]
											}),
									value : -1,
									mode : 'local',
									name : 'child_sex',
								});
						var is_paid=Ext.create('Ext.form.ComboBox', {
							fieldLabel : '是否回款',
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
														'0'],
												[
														'否',
														'1' ],
												[
														'是',
														'2' ] ]
									}),
							value : '0',
							mode : 'local',
							name : 'is_paid',
						});
						var case_userid = Ext.create('Ext.form.ComboBox', {
							xtype : 'combo',
							fieldLabel : '归属人',
							labelWidth : 60,
							width : '20%',
							name : 'case_userid',
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
									name : 'is_mail',
								});
						me.store = Ext.create('Ext.data.Store',{
							fields : [ "case_id",'case_code','agentia_id', 'agentia_name','case_username',
									 'case_areaname','address', 'case_userid','receiver_area', 'child_name',
									 'child_sex','birth_date','id_number','birth_hospital', 'house_area',
									 'life_area', 'mail_area','gather_id','case_in_per', 'case_in_time', 'case_in_pername','sample_code',
									 'gather_time', 'tariff_id', 'tariff_name', 'standFee', 'mail_name', 'mail_code','mail_time','print_time','remark',
									 'print_time','fee_type','status','mail_count'
									],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'children/mail/getMailCaseInfo.do',
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
								'beforeload' : function(ds,operation, opt) {
									exprotChildrenMail="endtime="+dateformat(endtime.getValue())+
														"&child_sex="+child_sex.getValue()+
														"&starttime="+dateformat(starttime.getValue())+
														"&mail_starttime="+dateformat(mail_starttime.getValue())+
														"&mail_endtime="+dateformat(mail_endtime.getValue())+
														"&child_name="+trim(childname.getValue())+
														"&case_code="+ trim(case_code.getValue())+
														"&sample_code="+ trim(sample_code.getValue())+
														"&case_userid="+ (case_userid.getValue()==null?"":case_userid.getValue())+
														"&case_areaname="+ trim(case_areaname.getValue())+
														"&is_mail="+ is_mail.getValue()+
														"&is_paid="+ is_paid.getValue();
									Ext.apply(
											me.store.proxy.extraParams,{								
											endtime : dateformat(endtime.getValue()),	
										    child_sex :child_sex.getValue(),
											starttime : dateformat(starttime.getValue()),
											mail_starttime:dateformat(mail_starttime.getValue()),
											mail_endtime:dateformat(mail_endtime.getValue()),
											child_name : trim(childname.getValue()),
											case_code : trim(case_code.getValue()),
											sample_code:trim(sample_code.getValue()),
											case_userid:case_userid.getValue(),
											case_areaname:trim(case_areaname.getValue()),
											is_mail:is_mail.getValue(),
											is_paid:is_paid.getValue()
											});
								}
							}
						});
						me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//							mode: 'SINGLE'
						});
						me.bbar = Ext.create('Ext.PagingToolbar', {
							store : me.store,
							pageSize : me.pageSize,
							displayInfo : true,
							displayMsg : "第 {0} - {1} 条  共 {2} 条",
							emptyMsg : "没有符合条件的记录"
						});
						me.columns = [
								{
									text : '案例条形码',
									dataIndex : 'case_code',
									menuDisabled : true,
									width : 150
								},{
									text : '样本条形码',
									dataIndex : 'sample_code',
									menuDisabled : true,
									width : 150
								},{
									text : '采集时间',
									dataIndex : 'gather_time',
									menuDisabled : true,
									width : 150
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

									text : '付款信息',
									dataIndex : 'status',
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
								
								},{
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
								},{
									text : '快递日期',
									dataIndex : 'mail_time',
									menuDisabled : true,
									width : 200
								},{
									text : '打印状态',
									dataIndex : 'print_time',
									menuDisabled : true,
									width : 100,
									renderer : function(value) {
										if(value==""||value==null){
											return "未打印";
										}
										return "<span style='color:green'>已打印</span>";
									}
								},
								{
									text : '孩子姓名',
									dataIndex : 'child_name',
									width : 200,
									menuDisabled : true
								},
								{
									text : '孩子性别',
									dataIndex : 'child_sex',
									menuDisabled : true,
									width : 100,
									renderer : function(value) {
										switch (value) {
										case 0:
											return "女";
											break;
										case 1:
											return "男";
											break;
										default:
											return "";
										}
									}
								},
								{
									text : '出生日期',
									dataIndex : 'birth_date',
									menuDisabled : true,
									width : 120
								},
								{
									text : '身份证号',
									dataIndex : 'id_number',
									menuDisabled : true,
									width : 200
								},{
									text : '备注',
									dataIndex : 'remark',
									menuDisabled : true,
									width : 120
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [case_code,case_userid,childname,child_sex,case_areaname ]
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [sample_code,starttime,endtime,is_mail,is_paid ]
								
								},{
									style : {
								        borderTopWidth : '0px !important',
								        borderBottomWidth : '0px !important'
								 	},
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [mail_starttime,mail_endtime,
											 {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											} ]
								
								}, {
									xtype : 'toolbar',
									dock : 'top',
									items : [{
										text : '查看监护人信息',
										iconCls : 'Find',
										handler : me.onFind
									}, {
										text : '查看采集人员信息',
										iconCls : 'Find',
										handler : me.onFindGather
									}, {
										text : '邮件管理',
										iconCls : 'Box',
										handler : me.mail_manage
									}, {
										text : '插入异常',
										iconCls : 'Add',
										handler : me.exceptionInsert
									}, {
										text : '查看案例信息',
										iconCls : 'Camera',
										handler : me.onCheckRegister
									}, {
										text : '导出',
										iconCls : 'Pageexcel',
										handler : me.onExport
									}
									]
								} ];

						me.callParent(arguments);
					},
					onExport:function(){
						window.location.href = "children/mail/exportChildrenMail.do?"+exprotChildrenMail;
					},
					onCheckRegister : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if (selections.length < 1 | selections.length > 1) {
							Ext.Msg.alert("提示", "请选择一条需要查看的记录!");
							return;
						}
						var form = Ext.create("Rds.children.form.ChildrenCheckForm", {
				                    region: "center",
				                    grid: me
				                });
						var win = Ext.create("Ext.window.Window", {
									title : '案例查看',
									width : 1600,
									iconCls : 'Pageedit',
									height : 1000,
									maximizable : true,
									maximized : true,
									layout : 'border',
									items : [form]
								});
						form.loadRecord(selections[0]);
						win.show();
						form.loadRecord(selections[0]);
					},
					exceptionInsert:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if(selections.length<1 || selections.length>1 ){
							Ext.Msg.alert("提示", "请选择一条需要插入的记录!");
							return;
						}
						var form = Ext.create(
								"Rds.children.form.ChildrenExceptionInsertForm", {
									region : "center",
									grid : me
								});
						form.loadRecord(selections[0]);
						var win = Ext.create("Ext.window.Window", {
							title : '案例自定义异常添加',
							width : 600,
							iconCls : 'Pageedit',
							height : 300,
							layout : 'border',
							items : [ form ]
						});
						win.show();
					},
					mail_manage:function(){
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						if(selections.length<1 || selections.length>1 ){
							Ext.Msg.alert("提示", "请选择一条需要生成的记录!");
							return;
						}
						if (selections.length >= 1) {
							if(selections[0].get("fee_type")==2  || selections[0].get("status")==0 || selections[0].get("fee_type")==1 || selections[0].get("fee_type")==4)
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
					onSearch : function() {
						var me = this.up("gridpanel");
						me.getStore().currentPage = 1;
						me.getStore().load();
					},
					listeners : {
						'afterrender' : function() {
							this.store.load();
						}
					}
				});
