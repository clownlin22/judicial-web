/**
 * 案例登记列表
 *
 * @author chenwei
 */
Ext.define(
    "Rds.trace.panel.TracePrintGridPanel",
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
                            print_param:1
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
                    items : [ {
                        text : '查看车辆信息',
                        iconCls : 'Find',
                        handler : me.onFind
                    },{
                        text : '查看当事人信息',
                        iconCls : 'Find',
                        handler : me.onFindPerson
                    },{
                        text : '报告打印',
                        iconCls : 'Printer',
                        handler : me.onCasePrint
                    }]
                }];

            me.callParent(arguments);
        },onFindPerson:function(){
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
        onCasePrint : function() {
            var me = this.up("gridpanel");
            var selections = me.getView().getSelectionModel()
                .getSelection();
            if (selections.length < 1) {
                Ext.Msg.alert("提示", "请选择需要打印的记录!");
                return;
            };
            if(selections[0].get("is_delete")!=0){
                Ext.Msg.alert("提示", "此案例已废除!");
                return;
            }
            var src = "trace/attachment/getWord.do?year="+
                selections[0].get("year")+"&case_no="+selections[0].get("case_no")
                +"&case_id="+selections[0].get("case_id")+"&print_flag=Y";
            var case_id = selections[0].get("case_id");
            var win = Ext.create("Ext.window.Window", {
                config:{
                    case_id:case_id,
                    gridpanel:me
                },
                title : '报告打印',
                iconCls : 'Find',
                layout:"auto",
                maximized:true,
                maximizable :true,
                modal:true,
                bodyStyle : "background-color:white;",
                html:"<iframe width=100% height=100% id='verifyWord' src='"+src+"'></iframe>",
                buttons:[
                   {
                        text : '取消',
                        iconCls : 'Arrowredo',
                        handler : me.onCancel
                    }
                ]
            });
            win.show();
            // Ext.MessageBox
            //     .confirm(
            //     '提示',
            //     '确定打印此案例吗',
            //     function(id) {
            //         if (id == 'yes') {
            //             window.location.href="trace/attachment/getWord.do?year="+
            //                 selections[0].get("year")+"&case_no="+selections[0].get("case_no")
            //             +"&case_id="+selections[0].get("case_id")
            //         }
            //     });
        },
        onSearch : function() {
            var me = this.up("gridpanel");
            me.getStore().currentPage=1;
            me.getStore().load();
        },
        onCancel : function() {
            var me = this;
            me.up("window").close();
        },
        listeners : {
            'afterrender' : function() {
                this.store.load();
            }
        }
    });
