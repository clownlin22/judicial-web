/**
 * 案例登记列表
 * 
 * @author chenwei
 */
mail_canel = function(me) {
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

Ext
		.define(
				"Rds.trace.panel.TraceMailGridPanel",
				{
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
                        me.store = Ext
                            .create(
                            'Ext.data.Store',
                            {
                                fields : [ 'case_id', 'case_no',
                                    'company_name',
                                    'year',
                                    'case_type',
                                    'receive_time',
                                    'case_attachment_desc',
                                    'case_local',
                                    'identification_requirements',
                                    'status',
                                    'is_delete',
                                    'receiver_id',
                                    'areaname',
                                    'username',
                                    'verify_baseinfo_state'
                                ],
                                proxy : {
                                    type: 'jsonajax',
                                    actionMethods:{read:'POST'},
                                    //url: 'judicial/experiment/getCaseInfo.do',
                                    params:{

                                    },
                                    api:{
                                        read:'trace/register/queryCaseInfo.do'
                                    },
                                    reader: {
                                        type: 'json',
                                        root:'data',
                                        totalProperty:'total'
                                    },
                                    autoLoad: true//自动加载
                                },
                                listeners : {
                                    'beforeload' : function(ds,operation, opt) {
                                        Ext.apply(me.store.proxy.params,
                                            {
                                                start_time:dateformat(me.getDockedItems('toolbar')[0].
                                                    getComponent('start_time').getValue()),
                                                end_time:dateformat(me.getDockedItems('toolbar')[0].
                                                    getComponent('end_time').getValue()),
                                                is_delete:0,
                                                status:4,
                                                case_no:me.getDockedItems('toolbar')[0].
                                                    getComponent('case_no').getValue()
                                            });
                                    }
                                }
                            });

                        me.selModel = Ext.create('Ext.selection.CheckboxModel',{
                            mode: 'SINGLE'
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
                                text : '案例序号',
                                dataIndex : 'case_no',
                                menuDisabled : true,
                                width : 150,
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
                            },
                            {
                                text : '委托单位',
                                dataIndex : 'company_name',
                                width : 100,
                                menuDisabled : true
                            },
                            {
                                text : '受理日期',
                                dataIndex : 'receive_time',
                                menuDisabled : true,
                                width : 100
                            },
                            {
                                dataIndex : 'case_id',
                                menuDisabled : true,
                                hidden : true
                            },
                            {
                                text : '委托鉴定事项',
                                dataIndex : 'case_type',
                                menuDisabled : true,
                                width : 100
                            },
                            {
                                text : '鉴定材料',
                                dataIndex : 'case_attachment_desc',
                                menuDisabled : true,
                                width : 200
                            },{
                                text : '归属人',
                                dataIndex : 'username',
                                menuDisabled : true,
                                width : 100
                            },{
                                text : '案例归属地',
                                dataIndex : 'areaname',
                                menuDisabled : true,
                                width : 200
                            }
                        ];

						me.dockedItems = [
                            {
                                xtype : 'toolbar',
                                name : 'search',
                                dock : 'top',
                                items : [
                                    {
                                        xtype : 'textfield',
                                        name : 'case_no',
                                        itemId : 'case_no',
                                        labelWidth : 70,
                                        width : 155,
                                        fieldLabel : '案例序号'
                                    },
                                    {
                                        xtype : 'datefield',
                                        name : 'start_time',
                                        itemId : 'start_time',
                                        width : 175,
                                        fieldLabel : '受理时间 从',
                                        labelWidth : 70,
                                        labelAlign : 'right',
                                        emptyText : '请选择日期',
                                        format : 'Y-m-d',
                                        maxValue : new Date(),
                                        value : Ext.Date.add(
                                            new Date(),
                                            Ext.Date.DAY,-7),
                                        listeners : {
                                            'select' : function() {
                                                var start = me.getDockedItems('toolbar')[0].
                                                    getComponent('start_time').getValue();
                                                me.getDockedItems('toolbar')[0].
                                                    getComponent('end_time')
                                                    .setMinValue(
                                                    start);
                                                var endDate = me.getDockedItems('toolbar')[0].
                                                    getComponent('end_time').getValue()
                                                if (start > endDate) {
                                                    me.getDockedItems('toolbar')[0].
                                                        getComponent('start_time').setValue(start);
                                                }
                                            }
                                        }
                                    },
                                    {
                                        xtype : 'datefield',
                                        itemId : 'end_time',
                                        name : 'end_time',
                                        width : 135,
                                        labelWidth : 20,
                                        fieldLabel : '到',
                                        labelAlign : 'right',
                                        emptyText : '请选择日期',
                                        format : 'Y-m-d',
                                        maxValue : new Date(),
                                        value : Ext.Date.add(
                                            new Date(),
                                            Ext.Date.DAY),
                                        listeners : {
                                            select : function() {
                                                var start = me.getDockedItems('toolbar')[0].
                                                    getComponent('start_time').getValue();
                                                var endDate = me.getDockedItems('toolbar')[0].
                                                    getComponent('end_time').getValue();
                                                if (start > endDate) {
                                                    me.getDockedItems('toolbar')[0].
                                                        getComponent('start_time').
                                                        setValue(endDate);
                                                }
                                            }
                                        }
                                    }, {
                                        text : '查询',
                                        iconCls : 'Find',
                                        handler : me.onSearch
                                    } ]
                            }, {
                                    xtype : 'toolbar',
                                    dock : 'top',
                                    items : [  {
                                        text : '查看车辆信息',
                                        iconCls : 'Find',
                                        handler : me.onFind
                                    },{
                                        text : '查看当事人信息',
                                        iconCls : 'Find',
                                        handler : me.onFindPerson
                                    }, {
                                        text : '邮件管理',
                                        iconCls : 'Box',
                                        handler : me.onMail
                                    }]
                                } ];

						me.callParent(arguments);
					},
                    onFind:function(){
                        var me = this.up("gridpanel");
                        var selections = me.getView().getSelectionModel()
                            .getSelection();
                        if (selections.length < 1) {
                            Ext.Msg.alert("提示", "请选择需要查看的记录!");
                            return;
                        };
                        var win = Ext.create("Ext.window.Window", {
                            title : "样本信息（案例序号：" + selections[0].get("case_no")+" 年份："+
                                selections[0].get("year")+"）",
                            width : 600,
                            iconCls : 'Find',
                            height : 400,
                            layout : 'border',
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
                                    fields : [ 'case_id', 'plate_number', 'vehicle_identification_number',
                                        'engine_number', 'vehicle_type'],// 定义字段
                                    proxy : {
                                        type : 'jsonajax',
                                        actionMethods : {
                                            read : 'POST'
                                        },
                                        url : 'trace/vehicle/queryVehicle.do',
                                        params : {
                                            'case_id' : selections[0].get("case_id")
                                        },
                                        reader : {
                                            type : 'json',
                                            root : 'data',
                                            totalProperty : 'total'
                                        }
                                    },
                                    autoLoad : true
                                    // 自动加载
                                },
                                columns : [// 配置表格列
                                    {
                                        header : "车牌号",
                                        dataIndex : 'plate_number',
                                        flex : 1,
                                        menuDisabled : true
                                    }, {
                                        header : "车架号",
                                        dataIndex : 'vehicle_identification_number',
                                        flex : 1,
                                        menuDisabled : true
                                    }, {
                                        header : "发动机号",
                                        dataIndex : 'engine_number',
                                        flex : 1,
                                        menuDisabled : true
                                    }, {
                                        header : "车辆类型",
                                        dataIndex : 'vehicle_type',
                                        flex : 1,
                                        menuDisabled : true
                                    }]
                            }) ]
                        });
                        win.show();
                    },
                    onFindPerson:function(){
                        var me = this.up("gridpanel");
                        var selections = me.getView().getSelectionModel()
                            .getSelection();
                        if (selections.length < 1) {
                            Ext.Msg.alert("提示", "请选择需要查看的记录!");
                            return;
                        };
                        var win = Ext.create("Ext.window.Window", {
                            title : "当事人信息（案例序号：" + selections[0].get("case_no")+" 年份："+
                                selections[0].get("year")+"）",
                            width : 600,
                            iconCls : 'Find',
                            height : 400,
                            layout : 'border',
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
                                    fields : [ 'case_id', 'person_name', 'id_number',
                                        'address'],// 定义字段
                                    proxy : {
                                        type : 'jsonajax',
                                        actionMethods : {
                                            read : 'POST'
                                        },
                                        url : 'trace/person/queryPerson.do',
                                        params : {
                                            'case_id' : selections[0].get("case_id")
                                        },
                                        reader : {
                                            type : 'json',
                                            root : 'data',
                                            totalProperty : 'total'
                                        }
                                    },
                                    autoLoad : true
                                    // 自动加载
                                },
                                columns : [// 配置表格列
                                    {
                                        header : "姓名",
                                        dataIndex : 'person_name',
                                        flex : 1,
                                        menuDisabled : true
                                    }, {
                                        header : "身份证号",
                                        dataIndex : 'id_number',
                                        flex : 1,
                                        menuDisabled : true
                                    }, {
                                        header : "家庭住址",
                                        dataIndex : 'address',
                                        flex : 1,
                                        menuDisabled : true
                                    }]
                            }) ]
                        });
                        win.show();
                    },
					onMail : function() {
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel()
								.getSelection();
						if (selections.length < 1) {
                            Ext.Msg.alert("提示", "请选择需要邮寄的记录!");
                            return;
                        }
						mail_onDel = function(mei) {
							var grid = mei.up("gridpanel");
							var selections_mail = grid.getView().getSelectionModel().getSelection();

							if (selections_mail.length < 1) {
								Ext.Msg.alert("提示", "请选择需要删除的记录!");
								return;
							}
							Ext.Ajax.request({
										url : "trace/mail/delMailInfo.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : {
											"mail_id" : selections_mail[0].get("mail_id"),
											"case_id":selections[0].get("case_id")
										},
										success : function(response, options) {
											grid.store.load();
										},
										failure : function() {
											Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
										}
									});
						}
						mail_onInsert = function(me) {
							mail_save = function(mei) {
								var form = mei.up("form").getForm();
								var values = form.getValues();
								values["case_id"] = selections[0].get("case_id");
								if (form.isValid()) {
									Ext.Ajax.request({
												url : "trace/mail/addMailInfo.do",
												method : "POST",
												headers : {
													'Content-Type' : 'application/json'
												},
												jsonData : values,
												success : function(response, options) {
													response = Ext.JSON
															.decode(response.responseText);
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
													Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
												}
											});
								}
							}
							var mailform_add = Ext.create("Ext.window.Window", {
								title : '快递信息',
								width : 250,
								height : 200,
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
										allowBlank : false,// 不允许为空
										blankText : "不能为空",// 错误提示信息，默认为This field is required!
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
									}]
								}]
							})
							mailform_add.show();
						}
						
						mail_onUpdate = function(me) {
							var grid = me.up("gridpanel");
							var selections_mail = grid.getView().getSelectionModel().getSelection();

							if (selections_mail.length < 1) {
								Ext.Msg.alert("提示", "请选择需要修改的记录!");
								return;
							}
							mail_update = function(mei) {
								var form = mei.up("form").getForm();
								var values = form.getValues();
								values["case_id"] = selections[0].get("case_id");
								if (form.isValid()) {
									Ext.Ajax.request({
												url : "trace/mail/updateMailInfo.do",
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
													Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
												}
											});
								}
							}
							var mailform_update = Ext.create("Ext.window.Window", {
								title : '快递信息',
								width : 250,
								height : 200,
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
												value : selections_mail[0].get("mail_type"),
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This field is
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
										value : selections_mail[0].get("mail_code"),
										allowBlank : false,// 不允许为空
										blankText : "不能为空",// 错误提示信息，默认为This field is required!
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
										value: selections_mail[0].get("mail_per")
									}, {
										xtype : 'hiddenfield',
										name : 'mail_id',
										value : selections_mail[0].get("mail_id")
									}]
								}]
							})
							mailform_update.show();
						},
						queryMailInfo = function(value, mail_type) {
							var win = Ext.create("Ext.window.Window", {
										title : "快递信息（快递单号：" + value + "）",
										width : 700,
										iconCls : 'Find',
										height : 350,
										layout : 'border',
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
																// 在表格中显示斑马线
															},
															store : {// 配置数据源
																fields : ['time', 'content'],// 定义字段
																proxy : {
																	type : 'jsonajax',
																	actionMethods : {
																		read : 'POST'
																	},
																	url : 'trace/mail/getMailById.do',
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
						var win = Ext.create("Ext.window.Window", {
                            title : "快递信息（案例序号：" + selections[0].get("case_no")+" 年份："+
                                selections[0].get("year")+"）",
							width : 600,
							iconCls : 'Find',
							height : 500,
							layout : 'border',
							bodyStyle : "background-color:white;",
							items : [Ext.create("Ext.form.Panel", {
							    width: 580,
							    height: 150,
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
							                        { xtype: "displayfield",columnWidth : .55,labelAlign: 'left',
														labelWidth : 80, value:selections[0].get("company_name"),fieldLabel: "委托单位"}
							                ]
							            },
							            {
							                xtype: "container",
							                layout: "column",
							                height: 35,
							                items: [
							                        { xtype: "displayfield",columnWidth : .55,labelAlign: 'left',
														labelWidth : 80, value:selections[0].get("areaname"),fieldLabel: "案例归属地"},
							                        { xtype: "displayfield",columnWidth : .4,labelAlign: 'left',
														labelWidth : 80, value:selections[0].get("username"),fieldLabel: "归属人"}
							                ]
							            },
							            {
							            	xtype: "container",
							                layout: "column",
							                height: 35,
							                items: [
                                                { xtype: "displayfield",columnWidth : .4,labelAlign: 'left',
                                                    labelWidth : 80, value:selections[0].get("receive_time"),fieldLabel: "受理时间"}
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
											// 在表格中显示斑马线
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
													'mail_time', 'mail_id','mail_per'],// 定义字段
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'trace/mail/getMailInfo.do',
												params : {
													'case_id' : selections[0].get("case_id")
												},
												reader : {
													type : 'json',
													root : 'data',
													totalProperty : 'total'
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
												}, {
													header : "快递时间",
													dataIndex : 'mail_time',
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
														return "<a href='#' onclick=\"queryMailInfo('"
																+ value
																+ "','"
																+ mail_type
																+ "')\"  >查询";

													}
												}]
									})]
						});
						win.show();
					},
					onSearch : function() {
						var me = this.up("gridpanel");
						me.getStore().currentPage=1;
						me.getStore().load();
					},
					listeners : {
						'afterrender' : function() {
							this.store.load();
						}
					}
				});
