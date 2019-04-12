/**
 * @description 案例审核
 * @author fushaoming
 * @date 20150414
 */
Ext.define("Rds.trace.panel.TraceVerifyGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		me.store = Ext.create('Ext.data.Store', {
					fields : [ 'case_id', 'case_no',
                        'company_name',
                        'year',
                        'case_type',
                        'receive_time',
                        'case_attachment_desc',
                        'case_local',
                        'identification_requirements',
                        'status','areaname',
                        'username',
                        'is_delete','verify_baseinfo_state'
                         ],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'trace/verify/queryVerify.do',
						params : {},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'total'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							Ext.apply(me.store.proxy.extraParams, {
                                start_time:dateformat(me.getDockedItems('toolbar')[0].
                                    getComponent('start_time').getValue()),
                                end_time:dateformat(me.getDockedItems('toolbar')[0].
                                    getComponent('end_time').getValue()),
                                is_delete:0,
                                case_no:me.getDockedItems('toolbar')[0].
                                    getComponent('case_no').getValue(),
                                verify_baseinfo_state:me.getDockedItems('toolbar')[0].
                                    getComponent('verify_baseinfo_state').getValue()
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
            },{text: '案例状态',dataIndex: 'status',flex : 1,renderer:function(value){
                switch(value)
                {
                    case 0:
                        return "基本信息未审核";
                        break;
                    case 1:
                        return "基本信息已审核";
                        break;
                    case 2:
                        return "已完成案例编写";
                        break;
                    case 3:
                        return "审核报告不通过";
                        break;
                    case 4:
                        return "审核报告通过";
                        break;
                    case 5:
                        return "已寄送";
                        break;
                    case 6:
                        return "已归档";
                        break;
                }
            }},
            {
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
                    new Ext.form.field.ComboBox(
                        {
                            fieldLabel : '是否已审核',
                            hidden:true,
                            width : 170,
                            labelWidth : 75,
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
                                            '未审核',
                                            0 ],
                                        [
                                            '已审核',
                                            1 ] ]
                                }),
                            value : -1,
                            mode : 'local',
                            // typeAhead: true,
                            name : 'verify_baseinfo_state',
                            itemId : 'verify_baseinfo_state'
                        }),
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
			items : [{
						text : '审核登记信息',
						iconCls : 'Applicationformmagnify',
						handler : me.onVerifyRegister
					}, '-', {
                        text : '审核word文档',
                        iconCls : 'Bookopenmark',
                        handler : me.onVerifyWord
                    }, '-', {
                    text : '查看审核历史',
                    iconCls : 'Camera',
                    handler : me.onVerifyHistory
                }]
		}];

		me.callParent(arguments);
	},
	onVerifyRegister : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要审核的记录!");
			return;
		}
		if (selections[0].get("is_delete") == 1) {
			Ext.Msg.alert("提示", "此案例已被废除，无法审核!");
			return;
		}
		if (selections[0].get("status") != 0) {
			Ext.Msg.alert("提示", "此案例已审核通过，无法再次审核!");
			return;
		}
		Ext.Ajax.request({
					url : 'trace/vehicle/queryVehicle.do',
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : {
						'case_id' : selections[0].get("case_id")
					},
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						var items = response["data"];
						// if (items.length <= 0) {
						// 	Ext.Msg.alert("提示", "该案例没有车辆信息，请完善后再审核！");
						// } else {
							var form = Ext.create(
									"Rds.trace.form.TraceVerifyForm", {
										region : "center",
										grid : me,
                                        autoScroll:true,
                                        case_id:selections[0].get("case_id")
									});
							var win = Ext.create("Ext.window.Window", {
                                        title : '案例审核',
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
						// }
					},
					failure : function() {
						Ext.Msg.alert("提示", "发生错误，请联系管理员!");
					}
				});

	},
    onVerifyHistory : function() {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要查看的记录!");
            return;
        }
        var myGrid = Ext.create('Ext.grid.Panel', {
            region : 'center',
            store : Ext.create('Ext.data.Store', {
                fields : ['uuid', 'verify_baseinfo_time',
                    'verify_baseinfo_state', 'verify_baseinfo_person',
                    'verify_baseinfo_remark'],
                proxy : {
                    type : 'jsonajax',
                    actionMethods : {
                        read : 'post'
                    },
                    url : 'trace/verify/queryVerifyHistory.do',
                    params : {},
                    reader : {
                        type : 'json',
                        root : 'data',
                        totalProperty : 'total'
                    }
                },
                listeners : {
                    'beforeload' : function(ds, operation, opt) {
                        Ext.apply(myGrid.store.proxy.params, {
                            case_id : selections[0]
                                .get('case_id')
                        });
                    }
                }
            }),
            columns : [{
                dataIndex : 'uuid',
                hidden : true
            }, {
                text : '审核时间',
                dataIndex : 'verify_baseinfo_time',
                width : '30%',
                menuDisabled : true

            }, {
                text : '审核状态',
                dataIndex : 'verify_baseinfo_state',
                width : '15%',
                menuDisabled : true,

                renderer : function(value) {
                    switch (value) {
                        case '0' :
                            return "未审核";
                            break;
                        case '1' :
                            return "<span style='color:green'>案例审核通过</span>";
                            break;
                        case '2' :
                            return "<span style='color:red'>审核未通过</span>";
                            break;
                        default :
                            return "";
                    }
                }
            }, {
                text : '审核人',
                dataIndex : 'verify_baseinfo_person',
                width : '20%',
                menuDisabled : true
            }, {
                text : '审核理由',
                dataIndex : 'verify_baseinfo_remark',
                width : '40%',
                menuDisabled : true,
                renderer : function(value, cellmeta, record, rowIndex,
                                    columnIndex, store) {
                    var str = value;
                    if (value.length > 10) {
                        str = value.substring(0, 10) + "...（鼠标悬浮显示更多）";
                    }
                    return "<div title='" + value + "'>" + str
                        + "</div>";
                }
            }],
            buttons : [{
                text : '确定',
                iconCls : 'Accept',
                handler : function() {
                    this.up("window").close();
                }
            }],
            listeners : {
                'afterrender' : {
                    fn : function() {
                        myGrid.store.load();
                    }
                }
            }
        });
        var win = Ext.create("Ext.window.Window", {
            title : '审核历史',
            width : 700,
            iconCls : 'Add',
            height : 500,
            layout : 'border',
            modal : true,
            items : [myGrid]
        });
        win.show();
    },
    onVerifyWord : function() {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要审核的记录!");
            return;
        }
        if (selections[0].get("is_delete") == 1) {
            Ext.Msg.alert("提示", "此案例已被废除，无法审核!");
            return;
        }
        if (selections[0].get("status") != 2) {
            Ext.Msg.alert("提示", "请先在线编辑word文档!");
            return;
        }
        Ext.Ajax.request({
            url : 'trace/vehicle/queryVehicle.do',
            method : "POST",
            headers : {
                'Content-Type' : 'application/json'
            },
            jsonData : {
                'case_id' : selections[0].get("case_id")
            },
            success : function(response, options) {
                response = Ext.JSON.decode(response.responseText);
                var items = response["data"];
                // if (items.length <= 0) {
                //     Ext.Msg.alert("提示", "该案例没有车辆信息，请完善后再审核！");
                // } else {
                    var year = selections[0].get("year");
                    var case_no = selections[0].get("case_no");
                    var case_id = selections[0].get("case_id");
                    var src="trace/verify/verifyWord.do?year="+year+"&case_no="+case_no;
                    var win = Ext.create("Ext.window.Window", {
                        config:{
                            case_id:case_id,
                            gridpanel:me
                        },
                        title : '报告审核',
                        iconCls : 'Find',
                        layout:"auto",
                        maximized:true,
                        maximizable :true,
                        modal:true,
                        bodyStyle : "background-color:white;",
                        html:"<iframe width=100% height=100% id='verifyWord' src='"+src+"'></iframe>",
                        buttons:[
                            {
                                text : '审核通过',
                                iconCls : 'Disk',
                                handler : me.accept
                            }, {
                                text : '审核不通过',
                                iconCls : 'Cancel',
                                handler : me.refuse
                            },{
                                text : '取消',
                                iconCls : 'Arrowredo',
                                handler : me.onCancel
                            }
                        ]
                    });
                    win.show();
                // }
            },
            failure : function() {
                Ext.Msg.alert("提示", "发生错误，请联系管理员!");
            }
        });

    },
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage = 1;
		me.getStore().load();
	},

    onCancel : function() {
        var me = this;
        me.up("window").close();
    },
    accept : function() {
        var gridpanel = this.up("window").getInitialConfig().gridpanel;
        var selections = gridpanel.getView().getSelectionModel().getSelection();
        var me = this.up("window");
        var values = {
            case_id:selections[0].get("case_id"),
            status:4
        };
        Ext.Ajax.request({
            url:"trace/verify/updateStatus.do",
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            jsonData: values,
            success: function (response, options) {
                response = Ext.JSON.decode(response.responseText);
                if (response.result==true) {
                    Ext.MessageBox.alert("提示信息", "审核成功");
                    gridpanel.getStore().load();
                    me.close();
                }else {
                    Ext.MessageBox.alert("错误信息", "审核失败");
                }
            },
            failure: function () {
                Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
            }
        });
    },

    refuse : function() {
        var gridpanel = this.up("window").getInitialConfig().gridpanel;
        var selections = gridpanel.getView().getSelectionModel().getSelection();
        var me = this.up("window");
        var values = {
            case_id:selections[0].get("case_id"),
            status:3
        };
        Ext.Ajax.request({
            url:"trace/verify/updateStatus.do",
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            jsonData: values,
            success: function (response, options) {
                response = Ext.JSON.decode(response.responseText);
                if (response.result==true) {
                    Ext.MessageBox.alert("提示信息", "审核成功");
                    gridpanel.getStore().load();
                    me.close();
                }else {
                    Ext.MessageBox.alert("错误信息", "审核失败");
                }
            },
            failure: function () {
                Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
            }
        });
    },
	listeners : {
		'afterrender' : function() {
			this.store.load();
		}
	}
});
