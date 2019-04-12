Ext.apply(Ext.form.field.VTypes, {
    daterange: function (val, field) {
        var date = field.parseDate(val);

        if (!date) {
            return false;
        }
        if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
            var start = field.up('grid').down('#' + field.startDateField);
            start.setMaxValue(date);
            start.validate();
            this.dateRangeMax = date;
        }
        else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
            var end = field.up('grid').down('#' + field.endDateField);
            end.setMinValue(date);
            end.validate();
            this.dateRangeMin = date;
        }
        return true;
    },
    daterangeText: '开始日期必须小于结束日期'
});

Ext.define("Rds.alcohol.panel.AlcoholVerifyGridPanel", {
    extend: "Ext.grid.Panel",
    loadMask: true,
    viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    region: 'center',
    pageSize: 25,
    initComponent: function () {
        var me = this;
        var case_code = Ext.create('Ext.form.field.Text', {
            name: 'case_code',
            labelWidth: 70,
            width: '20%',
            fieldLabel: '案例条形码'
        });
        var state = new Ext.form.field.ComboBox({
            fieldLabel: '状态信息',
            width: '20%',
            labelWidth: 60,
            editable: false,
            triggerAction: 'all',
            labelAlign: 'right',
            displayField: 'Name',
            valueField: 'Code',
            store: new Ext.data.ArrayStore({
                fields: ['Name', 'Code'],
                data: [['全部', -1], ['未审核', 0],
                    ['审核未通过', 1], ['审核已通过', 2]]
            }),
            value: 0,
            mode: 'local',
            name: 'state',
            id: 'state_check_alcohol'
        })
        var starttime = new Ext.form.DateField({
            id: 'alcohol_sample_starttime22',
            name: 'starttime',
            width: '20%',
            fieldLabel: '受理时间从',
            labelWidth: 70,
            labelAlign: 'left',
            emptyText: '请选择日期',
            vtype: 'daterange',
            endDateField: 'alcohol_sample_endtime22',
            maxValue: new Date(),
            format: 'Y-m-d',
            listeners: {
                'change': function (dom, value) {
                    if (value == null) {
                        this.up('toolbar').down('#alcohol_sample_endtime22').setMinValue('')
                    }
                }
            }
        });
        var endtime = new Ext.form.DateField({
            id: 'alcohol_sample_endtime22',
            name: 'endtime',
            width: '20%',
            labelWidth: 20,
            fieldLabel: ' 到 ',
            labelAlign: 'right',
            emptyText: '请选择日期',
            format: 'Y-m-d',
            maxValue: new Date()
        });
        var client = Ext.create('Ext.form.field.Text', {
            name: 'client',
            labelWidth: 40,
            width: '20%',
            fieldLabel: '委托人'
        });
        var checkper = Ext.create('Ext.form.field.Text', {
            name: 'checkper',
            labelWidth: 60,
            width: '20%',
            fieldLabel: '送检人'
        });
        var checkper_phone = Ext.create('Ext.form.field.Text', {
            name: 'checkper_phone',
            labelWidth: 65,
            width: '20%',
            fieldLabel: '送检人电话'
        });
        var receiver = Ext.create('Ext.form.field.Text', {
            name: 'receiver',
            labelWidth: 70,
            width: '20%',
            fieldLabel: '归属人'
        });
        var areacode = new Ext.form.field.ComboBox({
            fieldLabel: '所属地区',
            labelWidth: 60,
            width: '20%',
            name: 'area_code',
            emptyText: '检索方式：如朝阳区(cy)',
            store: Ext.create("Ext.data.Store",
                {
                    fields: [
                        {name: 'key', mapping: 'key', type: 'string'},
                        {name: 'value', mapping: 'value', type: 'string'},
                        {name: 'name', mapping: 'name', type: 'string'},
                        {name: 'id', mapping: 'id', type: 'string'},
                    ],
                    pageSize: 10,
                    // autoLoad: true,
                    proxy: {
                        type: "ajax",
                        url: "judicial/dicvalues/getAreaInfo.do",
                        reader: {
                            type: "json"
                        }
                    }
                }),
            displayField: 'value',
            valueField: 'id',
//			forceSelection: true, 
            typeAhead: false,
            hideTrigger: true,
            minChars: 2,
            matchFieldWidth: true,
            listConfig: {
                loadingText: '正在查找...',
                emptyText: '没有找到匹配的数据'
            }
        });
        var mail_address = Ext.create('Ext.form.field.Text', {
            name: 'mail_address',
            labelWidth: 60,
            width: '20%',
            fieldLabel: '邮寄地址'
        });
        var mail_per = Ext.create('Ext.form.field.Text', {
            name: 'mail_per',
            labelWidth: 65,
            width: '20%',
            fieldLabel: '邮件接收人'
        });
        var mail_phone = Ext.create('Ext.form.field.Text', {
            name: 'mail_phone',
            labelWidth: 60,
            width: '20%',
            labelAlign: 'right',
            fieldLabel: '联系电话'
        });
        var isDoubleTube = new Ext.form.field.ComboBox({
            fieldLabel: '血管类型',
            width: '20%',
            labelWidth: 65,
            editable: false,
            triggerAction: 'all',
            displayField: 'Name',
            labelAlign: 'right',
            valueField: 'Code',
            store: new Ext.data.ArrayStore({
                fields: ['Name', 'Code'],
                data: [['全部', ''], ['真空采血管', '1'],
                    ['促凝管', '0']]
            }),
            value: '',
            mode: 'local',
            name: 'isDoubleTube'
        });
        var is_detection = new Ext.form.field.ComboBox({
            fieldLabel: '是否检出',
            width: '20%',
            labelWidth: 60,
            editable: false,
            triggerAction: 'all',
            displayField: 'Name',
            labelAlign: 'right',
            valueField: 'Code',
            store: new Ext.data.ArrayStore({
                fields: ['Name', 'Code'],
                data: [['全部', ''], ['是', '1'],
                    ['否', '0']]
            }),
            value: '',
            mode: 'local',
            name: 'is_detection'
        });
        var case_in_pername = Ext.create('Ext.form.field.Text', {
            name: 'case_in_pername',
            labelWidth: 60,
            width: '20%',
            fieldLabel: '登记人'
        });
        var sample_time_start = new Ext.form.DateField({
            id: 'alcohol_sample_starttime2',
            name: 'sample_time_start',
            width: '20%',
            fieldLabel: '登记日期从',
            labelWidth: 70,
            labelAlign: 'left',
            emptyText: '请选择日期',
            vtype: 'daterange',
            endDateField: 'alcohol_sample_endtime2',
            maxValue: new Date(),
            format: 'Y-m-d',
            listeners: {
                'change': function (dom, value) {
                    if (value == null) {
                        this.up('toolbar').down('#alcohol_sample_endtime2').setMinValue('')
                    }
                }
            }
        });
        var sample_time_end = new Ext.form.DateField({
            id: 'alcohol_sample_endtime2',
            name: 'sample_time_end',
            width: '20%',
            labelWidth: 20,
            fieldLabel: ' 到 ',
            labelAlign: 'right',
            emptyText: '请选择日期',
            format: 'Y-m-d',
            maxValue: new Date()
        });
        me.store = Ext.create('Ext.data.Store', {
            fields: ['case_id', 'case_code', "client", "checkper", "checkper_phone",
                "area_code", "event_desc", "area_name", "remark",
                "case_in_pername", "report_model",
                "report_modelname", "accept_time", "mail_address",
                "sample_id", "close_time", "mail_per", 'receiver',
                'receiver_area', "mail_phone", "receiver_id",
                "attachment", "stateStr", "case_in_per", "state",
                "client_time", "sample_remark", "sample_result", "case_checkper",
                "isDoubleTube", "sample_remark2", "is_detection", "bloodnumA", "bloodnumB",
                "sample_time", "bloodnumA", "bloodnumB", "case_intr", "case_det", "stand_sum", "real_sum", "is_check"
            ],
            proxy: {
                type: 'jsonajax',
                actionMethods: {
                    read: 'POST'
                },
                url: 'alcohol/verify/getVerifyCaseInfo.do',
                params: {
                    start: 0,
                    limit: 25
                },
                reader: {
                    type: 'json',
                    root: 'items',
                    totalProperty: 'count'
                }
            },
            listeners: {
                'beforeload': function (ds, operation, opt) {
                    Ext.apply(me.store.proxy.extraParams, {
                        endtime: dateformat(endtime.getValue()),
                        starttime: dateformat(starttime.getValue()),
                        case_code: trim(case_code.getValue()),
                        state: state.getValue(),
                        client: trim(client.getValue()),
                        sample_time_start: dateformat(sample_time_start.getValue()),
                        sample_time_end: dateformat(sample_time_end.getValue()),
                        checkper: trim(checkper.getValue()),
                        checkper_phone: trim(checkper_phone.getValue()),
                        receiver: trim(receiver.getValue()),
                        areacode: areacode.getValue(),
                        mail_address: trim(mail_address.getValue()),
                        mail_per: trim(mail_per.getValue()),
                        mail_phone: trim(mail_phone.getValue()),
                        isDoubleTube: isDoubleTube.getValue(),
                        is_detection: is_detection.getValue(),
                        case_in_pername: trim(case_in_pername.getValue())
                    });
                }
            }
        });
        me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize: me.pageSize,
            displayInfo: true,
            displayMsg: "第 {0} - {1} 条  共 {2} 条",
            emptyMsg: "没有符合条件的记录"
        });
        me.columns = [
            {
                text: '案例条形码',
                dataIndex: 'case_code',
                menuDisabled: true,
                width: 120

            },
            {
                text: '委托人',
                dataIndex: 'client',
                width: 200,
                menuDisabled: true
            },
            {
                text: '送检人',
                dataIndex: 'checkper',
                menuDisabled: true,
                width: 100
            },
            {
                text: '送检人电话',
                dataIndex: 'checkper_phone',
                menuDisabled: true,
                width: 120
            },
            {
                text: '案例归属地',
                dataIndex: 'receiver_area',
                menuDisabled: true,
                width: 200
            },
            {
                text: '归属人',
                dataIndex: 'receiver',
                menuDisabled: true,
                width: 100
            },
            {
                text: '所属地区',
                dataIndex: 'area_name',
                menuDisabled: true,
                width: 200
            },
            {
                text: "状态",
                dataIndex: 'stateStr',
                menuDisabled: true,
                width: 100,
                renderer: function (value, cellmeta, record, rowIndex,
                                    columnIndex, store) {
                    var state = record.data["state"];
                    if (state == 1) {
                        return "<span style='color:red'>" + value
                            + "</span>";
                    }
                    if (state == 0) {
                        return value;
                    }
                    return "<span style='color:green'>审核已通过</span>";
                }
            },
            {
                text: '邮寄地址',
                dataIndex: 'mail_address',
                menuDisabled: true,
                width: 100
            },
            {
                text: '邮件接收人',
                dataIndex: 'mail_per',
                menuDisabled: true,
                width: 100
            },
            {
                text: '联系电话',
                dataIndex: 'mail_phone',
                menuDisabled: true,
                width: 100
            },
            {
                hidden: true,
                text: '事件描述',
                dataIndex: 'event_desc',
                menuDisabled: true
            },
            {
                text: '受理时间',
                dataIndex: 'accept_time',
                menuDisabled: true,
                width: 100
            },
            {
                text: '登记日期',
                dataIndex: 'sample_time',
                menuDisabled: true,
                width: 100
            },
            {
                text: '登记人',
                dataIndex: 'case_in_pername',
                menuDisabled: true,
                width: 120
            },
            {
                text: '样本描述',
                dataIndex: 'sample_remark',
                menuDisabled: true,
                width: 150
            },
            {
                text: '检验结果',
                dataIndex: 'sample_result',
                menuDisabled: true,
                width: 120
            },
            {
                text: '检验描述',
                dataIndex: 'sample_remark2',
                menuDisabled: true,
                width: 120
            },
            {
                text: '血管类型',
                dataIndex: 'isDoubleTube',
                menuDisabled: true,
                width: 80,
                renderer: function (value) {
                    if (value == 0) {
                        return "<span style='color:red'>促凝管</span>";
                    }
                    return "<span style='color:blue'>真空采血管</span>";
                }
            },
            {
                text: '血液编号',
                dataIndex: 'bloodnumA',
                menuDisabled: true,
                width: 120
            },
            {
                text: '血液容量',
                dataIndex: 'bloodnumB',
                menuDisabled: true,
                width: 120
            },
            {
                text: '案例简介',
                dataIndex: 'case_intr',
                menuDisabled: true,
                width: 120
            },
            {
                text: '案例详情',
                dataIndex: 'case_det',
                menuDisabled: true,
                width: 120
            },
            {
                text: '标准金额',
                dataIndex: 'stand_sum',
                menuDisabled: true,
                width: 120
            },
            {
                text: '实收金额',
                dataIndex: 'real_sum',
                menuDisabled: true,
                width: 120
            },
            {
                text: '是否重新鉴定',
                dataIndex: 'is_check',
                menuDisabled: true,
                width: 120,
                renderer: function (value) {
                    if (value == 0) {
                        return "<span style='color:red'>否</span>";
                    }
                    return "<span style='color:blue'>是</span>";
                }
            },
            {
                text: '是否检出',
                dataIndex: 'is_detection',
                menuDisabled: true,
                width: 80,
                renderer: function (value) {
                    if (value == 0) {
                        return "<span style='color:red'>否</span>";
                    }
                    return "<span style='color:blue'>是</span>";
                }
            },
            {
                text: '备注',
                dataIndex: 'remark',
                menuDisabled: true,
                width: 300
            }];
        me.dockedItems = [
            {
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [case_code, client, checkper, checkper_phone, state]
            },
            {
                style: {
                    borderTopWidth: '0px !important',
                    borderBottomWidth: '0px !important'
                },
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [starttime, endtime, , mail_address, mail_per, mail_phone]
            },
            {
                style: {
                    borderTopWidth: '0px !important',
                    borderBottomWidth: '0px !important'
                },
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [sample_time_start, sample_time_end, areacode, isDoubleTube, is_detection]
            },
            {
                style: {
                    borderTopWidth: '0px !important',
                    borderBottomWidth: '1px !important'
                },
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [receiver, case_in_pername, {
                    text: '查询',
                    iconCls: 'Find',
                    handler: me.onSearch
                }]

            },
            {
                xtype: 'toolbar',
                dock: 'top',
                items: [{
                    text: '查看样本信息',
                    iconCls: 'Find',
                    handler: me.onFind
                }, {
                    text: '审核信息',
                    iconCls: 'Bookopenmark',
                    handler: me.onCheck
                }, {
                    text: '查看审核记录',
                    iconCls: 'Camera',
                    handler: me.onSee
                }, {
                    text: '照片管理',
                    iconCls: 'Cog',
                    handler: me.onPhotoManage
                }]
            }
        ];

        me.callParent(arguments);
    },
    onSee: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要查看的记录!");
            return;
        }
        ;
        var win = Ext.create("Ext.window.Window", {
            title: "审核历史（案例条形码：" + selections[0].get("case_code") + "）",
            width: 600,
            iconCls: 'Find',
            height: 400,
            layout: 'border',
            autoScroll: true,
            bodyStyle: "background-color:white;",
            items: [Ext.create('Ext.grid.Panel', {
                renderTo: Ext.getBody(),
                width: 600,
                height: 400,
                frame: false,
                viewConfig: {
                    forceFit: true,
                    stripeRows: true
                    // 在表格中显示斑马线
                },
                store: {// 配置数据源
                    fields: ['verify_id', 'verify_pername', 'verify_mark',
                        'verify_time', 'verify_state'],// 定义字段
                    proxy: {
                        type: 'jsonajax',
                        actionMethods: {
                            read: 'POST'
                        },
                        url: 'alcohol/verify/getVerifyInfo.do',
                        params: {
                            'case_id': selections[0].get("case_id")
                        },
                        reader: {
                            type: 'json',
                            root: 'items',
                            totalProperty: 'count'
                        }
                    },
                    autoLoad: true
                    // 自动加载
                },
                columns: [// 配置表格列
                    {
                        header: "审核人",
                        dataIndex: 'verify_pername',
                        flex: 1,
                        menuDisabled: true
                    }, {
                        header: "审核时间",
                        dataIndex: 'verify_time',
                        flex: 1.5,
                        menuDisabled: true
                    }, {
                        header: "审核状态",
                        dataIndex: 'verify_state',
                        flex: 1,
                        menuDisabled: true,
                        renderer: function (value, cellmeta, record,
                                            rowIndex, columnIndex, store) {
                            if (value == 1) {
                                return "<span style='color:red'>不通过</span>";
                            }
                            if (value == 2) {
                                return "<span style='color:green'>通过</span>";
                            }
                            return "";
                        }
                    }, {
                        header: "备注",
                        dataIndex: 'verify_mark',
                        flex: 2,
                        menuDisabled: true,
                        renderer: function (value, cellmeta, record,
                                            rowIndex, columnIndex, store) {
                            var str = value;
                            if (value.length > 15) {
                                str = value.substring(0, 15) + "...";
                            }
                            return "<span title='" + value + "'>" + str
                                + "</span>";
                        }
                    }]
            })]
        });
        win.show();
    },
    onPhotoManage: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要操作的记录!");
            return;
        }
        ;
        if (selections[0].get("delete") == '2') {
            Ext.Msg.alert("提示", "该记录已作废!");
            return;
        }
        var win = Ext.create("Ext.window.Window", {
            title: "案例照片管理",
            width: 1000,
            iconCls: 'Find',
            height: 300,
            modal: true,
            resizable: false,
            layout: 'border',
            bodyStyle: "background-color:white;",
            items: [Ext.create('Ext.grid.Panel', {
                renderTo: Ext.getBody(),
                width: 1000,
                height: 300,
                frame: false,
                viewConfig: {
                    forceFit: true,
                    stripeRows: true// 在表格中显示斑马线
                },
                store: {// 配置数据源
                    fields: ['att_path', 'case_id', 'att_type'],// 定义字段
                    proxy: {
                        type: 'jsonajax',
                        actionMethods: {
                            read: 'POST'
                        },
                        url: 'alcohol/attachment/getAtt.do',
                        params: {
                            'case_id': selections[0].get("case_id")
                        },
                        reader: {
//							type : 'json',
//							root : 'items',
//							totalProperty : 'count'
                        }
                    },
                    autoLoad: true
                },
                columns: [
                    {
                        text: '附件',
                        dataIndex: 'att_path',
                        width: '30%',
                        menuDisabled: true,
                    },
                    {
                        text: '附件描述',
                        dataIndex: 'att_type',
                        width: '10%',
                        menuDisabled: true,
                    }, {
                        header: "查看照片",
                        dataIndex: '',
                        width: '10%',
                        menuDisabled: true,
                        renderer: function (value, cellmeta,
                                            record, rowIndex, columnIndex,
                                            store) {
                            return "<a href='#'>查看</a>";
                        },
                        listeners: {
                            'click': function () {
                                var me = this.up("gridpanel");
                                var selections = me.getView().getSelectionModel().getSelection();
                                if (selections.length < 1 || selections.length > 1) {
                                    Ext.Msg.alert("提示", "请选择需要查看的一条记录!");
                                    return;
                                }
                                var form = Ext.create(
                                    "Rds.alcohol.form.AlcoholImageShow", {
                                        region: "center",
                                        grid: me
                                    });
                                var win = Ext.create("Ext.window.Window", {
                                    title: '图片查看',
                                    width: 600,
                                    iconCls: 'Pageedit',
                                    height: 600,
                                    maximizable: true,
                                    layout: 'border',
                                    items: [form]
                                });
                                form.loadRecord(selections[0]);
                                win.show();
                            }
                        }
                    }]
            })]
        });
        win.show();
    },
    onFind: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1 || selections.length > 1) {
            Ext.Msg.alert("提示", "请选择需要一条查看的记录!");
            return;
        }
        ;
        Ext.Ajax.request({
            url: "alcohol/register/getSampleInfo.do",
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            jsonData: {
                sample_id: selections[0].get("sample_id")
            },
            success: function (response, options) {
                response = Ext.JSON.decode(response.responseText);
                var sex = "男";
                if (response.sample_sex == 0) {
                    sex = "女"
                }
                var win = Ext.create("Ext.window.Window", {
                    title: "样本信息（案例条形码："
                    + selections[0].get("case_code") + "）",
                    width: 600,
                    iconCls: 'Find',
                    height: 270,
                    autoScroll: true,
                    bodyStyle: "background-color:white;padding-left:10px;padding-top:10px;",
                    items: [{
                        xtype: "container",
                        layout: "column",
                        height: 25,
                        items: [{
                            xtype: "displayfield",
                            columnWidth: .8,
                            labelAlign: 'left',
                            labelWidth: 60,
                            fieldLabel: "委托人",
                            value: selections[0].get("client")
                        }]
                    }, {
                        xtype: "container",
                        layout: "column",
                        height: 25,
                        items: [{
                            xtype: "displayfield",
                            columnWidth: .5,
                            labelAlign: 'left',
                            labelWidth: 60,
                            fieldLabel: "送检人",
                            value: selections[0].get("checkper")
                        }, {
                            xtype: "displayfield",
                            columnWidth: .5,
                            labelAlign: 'left',
                            labelWidth: 60,
                            fieldLabel: "联系电话",
                            value: selections[0].get("checkper_phone")
                        }]
                    }, {
                        xtype: "container",
                        layout: "column",
                        height: 25,
                        items: [{
                            xtype: "displayfield",
                            columnWidth: 1,
                            labelAlign: 'left',
                            labelWidth: 60,
                            fieldLabel: "邮寄地址",
                            value: selections[0].get("mail_address")
                        }]
                    }, {
                        xtype: "container",
                        layout: "column",
                        height: 25,
                        items: [{
                            xtype: "displayfield",
                            columnWidth: .5,
                            labelAlign: 'left',
                            labelWidth: 60,
                            fieldLabel: "收件人",
                            value: selections[0].get("mail_per")
                        }, {
                            xtype: "displayfield",
                            columnWidth: .5,
                            labelAlign: 'left',
                            labelWidth: 60,
                            fieldLabel: "电话",
                            value: selections[0].get("mail_phone")
                        }]
                    }, {
                        xtype: "container",
                        layout: "column",
                        height: 25,
                        items: [{
                            xtype: "displayfield",
                            columnWidth: .5,
                            labelAlign: 'left',
                            labelWidth: 60,
                            fieldLabel: "被检测人",
                            value: response.sample_name
                        }, {
                            xtype: "displayfield",
                            columnWidth: .5,
                            labelAlign: 'left',
                            labelWidth: 60,
                            fieldLabel: "身份证号",
                            value: response.id_number
                        }]
                    }, {
                        xtype: "container",
                        layout: "column",
                        height: 25,
                        items: [
                            {
                                xtype: "displayfield",
                                columnWidth: .5,
                                labelAlign: 'left',
                                labelWidth: 60,
                                fieldLabel: "是否检出",
                                value: selections[0].get("is_detection") == 0 ? "否" : "是"
                            }
                        ]
                    }, {
                        xtype: "container",
                        layout: "column",
                        items: [
                            {
                                xtype: "displayfield",
                                columnWidth: 1,
                                labelAlign: 'left',
                                labelWidth: 60,
                                fieldLabel: "样本描述",
                                value: selections[0].get("sample_remark")
                            }
                        ]
                    }, {
                        xtype: "container",
                        layout: "column",
                        items: [
                            {
                                xtype: "displayfield",
                                columnWidth: 1,
                                labelAlign: 'left',
                                labelWidth: 60,
                                fieldLabel: "检测结果",
                                value: selections[0].get("sample_result")
                            }
                        ]
                    }]
                });
                win.show();
            },
            failure: function () {
                Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
            }
        });
    },
    onCheck: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要审核的记录!");
            return;
        }
        var state = selections[0].get("state");
        if (state != 0) {
            Ext.Msg.alert("提示", "此案例已审核!");
            return;
        }
        var form = Ext.create("Rds.alcohol.form.AlcoholVerifyForm", {
            region: "center",
            autoScroll: true,
            grid: me
        });
        var win = Ext.create("Ext.window.Window", {
            title: '案例审核',
            width: 700,
            iconCls: 'Pageedit',
            height: 600,
            layout: 'border',
            items: [form]
        });
        form.loadRecord(selections[0]);
        win.show();
    },
    onSearch: function () {
        var me = this.up("gridpanel");
        me.getStore().currentPage = 1;
        me.getStore().load();
    },
    listeners: {
        'afterrender': function () {
            this.store.load();
        }
    }
});
