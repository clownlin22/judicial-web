var alcoholStr = '';
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
Ext.define("Rds.alcohol.panel.AlcoholRegisterGridPanel", {
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
            displayField: 'Name',
            labelAlign: 'right',
            valueField: 'Code',
            store: new Ext.data.ArrayStore({
                fields: ['Name', 'Code'],
                data: [['全部', -1], ['未审核', 0],
                    ['未通过', 1], ['已废除', 7], ['未废除', -7]]
            }),
            value: -1,
            mode: 'local',
            // typeAhead: true,
            name: 'state'
        });
        var starttime = new Ext.form.DateField({
            id: 'alcohol_sample_starttime11',
            name: 'starttime',
            width: '20%',
            fieldLabel: '受理时间从',
            labelWidth: 70,
            labelAlign: 'left',
            emptyText: '请选择日期',
            vtype: 'daterange',
            endDateField: 'alcohol_sample_endtime11',
            maxValue: new Date(),
            format: 'Y-m-d',
            listeners: {
                'change': function (dom, value) {
                    if (value == null) {
                        this.up('toolbar').down('#alcohol_sample_endtime11').setMinValue('')
                    }
                }
            }
        });
        var endtime = new Ext.form.DateField({
            id: 'alcohol_sample_endtime11',
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
            width: '20%',
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
        var case_in_pername = Ext.create('Ext.form.field.Text', {
            name: 'case_in_pername',
            labelWidth: 60,
            width: '20%',
            labelAlign: 'right',
            fieldLabel: '登记人'
        });
        var sample_time_start = new Ext.form.DateField({
            id: 'alcohol_sample_starttime',
            name: 'sample_time_start',
            width: '20%',
            fieldLabel: '登记日期从',
            labelWidth: 70,
            labelAlign: 'left',
            emptyText: '请选择日期',
            format: 'Y-m-d',
            vtype: 'daterange',
            endDateField: 'alcohol_sample_endtime',
            maxValue: new Date(),
            listeners: {
                'change': function (dom, value) {
                    if (value == null) {
                        this.up('toolbar').down('#alcohol_sample_endtime').setMinValue('')
                    }
                }
            }
        });
        var sample_time_end = new Ext.form.DateField({
            id: 'alcohol_sample_endtime',
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
            fields: [
                "case_id", "case_code", "client", "checkper", "checkper_phone", "area_code",
                "event_desc", "area_name", "remark", "case_in_pername", "report_model",
                "report_modelname", "accept_time", "sample_id", "mail_address", 'state', "mail_per", "stateStr",
                "mail_phone", 'receiver', 'receiver_area', "attachment", "receiver_id", "case_in_per",
                "client_time", "close_time", "sample_remark", "sample_result", "case_checkper_id",
                "isDoubleTube", "is_detection", "sample_remark2", "bloodnumA", "bloodnumB", "case_checkper",
                "sample_time", "case_intr", "case_det", "stand_sum", "real_sum", "is_check"
            ],
            proxy: {
                type: 'jsonajax',
                actionMethods: {
                    read: 'POST'
                },
                url: 'alcohol/register/getCaseInfo.do',
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
                    alcoholStr = "case_code=" + trim(case_code.getValue()) +
                        "&state=" + state.getValue() +
                        "&starttime=" + dateformat(starttime.getValue()) +
                        "&endtime=" + dateformat(endtime.getValue()) +
                        "&client=" + trim(client.getValue()) +
                        "&sample_time_start=" + dateformat(sample_time_start.getValue()) +
                        "&sample_time_end=" + dateformat(sample_time_end.getValue()) +
                        "&checkper=" + trim(checkper.getValue()) +
                        "&checkper_phone=" + trim(checkper_phone.getValue()) +
                        "&receiver=" + trim(receiver.getValue()) +
                        "&areacode=" + areacode.getValue() +
                        "&mail_address=" + trim(mail_address.getValue()) +
                        "&mail_per=" + trim(mail_per.getValue()) +
                        "&mail_phone=" + trim(mail_phone.getValue()) +
                        "&isDoubleTube=" + isDoubleTube.getValue() +
                        "&case_in_pername=" + trim(case_in_pername.getValue());
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
                width: 120,
                renderer: function (value, cellmeta, record, rowIndex, columnIndex,
                                    store) {
                    var state = record.data["state"];
                    if (state == 7) {
                        return "<div style=\"text-decoration: line-through;color: red;\">"
                            + value + "</div>"
                    } else {
                        return value;
                    }
                }
            },
            {
                text: '委托人',
                dataIndex: 'client',
                width: 220,
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
                text: '状态',
                dataIndex: 'stateStr',
                menuDisabled: true,
                width: 100,
                renderer: function (value, cellmeta, record, rowIndex, columnIndex,
                                    store) {
                    var state = record.data["state"];
                    if (state == 1) {
                        return "<span style='color:red'>" + value + "</span>";
                    }
                    return value;
                }
            },
            {
                hidden: true,
                text: '事件描述',
                dataIndex: 'event_desc',
                menuDisabled: true
                // width : 500
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
                text: '模板名称',
                dataIndex: 'report_modelname',
                menuDisabled: true,
                width: 120
            },
            {
                text: '受理日期',
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
                text: '打印日期',
                dataIndex: 'close_time',
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
                text: '血管类型',
                dataIndex: 'isDoubleTube',
                menuDisabled: true,
                width: 80,
                renderer: function (value) {
                    if (value == 1) {
                        return "<span style='color:red'>真空采血管</span>";
                    }
                    return "<span style='color:blue'>促凝管</span>";
                }
            },
            {
                text: '鉴定人',
                dataIndex: 'case_checkper',
                menuDisabled: true,
                width: 120
            },
            {
                text: '是否检出',
                dataIndex: 'is_detection',
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
                text: '备注',
                dataIndex: 'remark',
                menuDisabled: true,
                width: 300
            }];
        me.startdate = new Date(new Date().getFullYear() + '/01/01')
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
                items: [starttime, endtime, mail_address, mail_per, mail_phone]
            },
            {
                style: {
                    borderTopWidth: '0px !important',
                    borderBottomWidth: '0px !important'
                },
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [sample_time_start, sample_time_end, areacode, isDoubleTube, case_in_pername]
            },
            {
                style: {
                    borderTopWidth: '0px !important',
                    borderBottomWidth: '1px !important'
                },
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [receiver, {
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
                    text: '案例登记',
                    iconCls: 'Pageadd',
                    handler: me.onInsert
                }, {
                    text: '案例结果登记',
                    iconCls: 'Pageedit',
                    handler: me.onResultInsert
                }, {
                    text: '附件管理',
                    iconCls: 'Cog',
                    handler: me.onPhotoManage
                }, {
                    text: '修改',
                    iconCls: 'Pageedit',
                    handler: me.onUpdate
                }, {
                    text: '废除',
                    iconCls: 'Delete',
                    handler: me.onDelete
                },
                    {
                        text: '导出',
                        iconCls: 'Application',
                        handler: me.alcoholExport
                    }
                ]
            }
        ];

        me.callParent(arguments);
    },
    alcoholExport: function () {
        var me = this.up("gridpanel");
        me.getStore().currentPage = 1;
        me.getStore().load();
        window.location.href = "alcohol/register/caseInfoExport.do?" + alcoholStr;
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
    onDelete: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要废除的记录!");
            return;
        }
        ;
        if (selections[0].get("state") == 7) {
            Ext.Msg.alert("提示", "此案例已废除!");
            return;
        }
        if (selections[0].get("state") != 1 && selections[0].get("state") != 0) {
            Ext.Msg.alert("提示", "此案例已审核，无法废除!");
            return;
        }
        var values = {
            case_id: selections[0].get("case_id")
        };
        Ext.MessageBox.confirm('提示', '确定废除此案例吗', function (id) {
            if (id == 'yes') {
                Ext.Ajax.request({
                    url: "alcohol/register/deleteCaseInfo.do",
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    jsonData: values,
                    success: function (response, options) {
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
                    failure: function () {
                        Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
                    }
                });
            }
        });
    },
    onInsert: function () {
        var me = this.up("gridpanel");
        ownpersonTemp = "";
        ownaddressTemp = "";
        var form = Ext.create("Rds.alcohol.form.AlcoholRegisterInsertForm", {
            region: "center",
            autoScroll: true,
            grid: me
        });
        var win = Ext.create("Ext.window.Window", {
            title: '案例登记',
            width: 700,
            iconCls: 'Pageadd',
            height: 600,
            modal: true,
            layout: 'border',
            items: [form]
        });
        win.show();
    },
    onResultInsert: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要修改的记录!");
            return;
        }
        if (selections[0].get("state") == 7) {
            Ext.Msg.alert("提示", "此案例已废除!");
            return;
        }
        if (selections[0].get("state") != 1 && selections[0].get("state") != 0) {
            Ext.Msg.alert("提示", "此案例已审核，无法修改!");
            return;
        }
        var form = Ext.create("Rds.alcohol.form.AlcoholRegisterInsertResultForm", {
            region: "center",
            autoScroll: true,
            grid: me
        });
        form.loadRecord(selections[0]);
        var win = Ext.create("Ext.window.Window", {
            title: '案例修改',
            iconCls: 'Pageedit',
            width: 600,
            modal: true,
            height: 400,
            layout: 'border',
            items: [form]
        });
        win.show();
    },
    onPhotoManage: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要修改的记录!");
            return;
        }
        if (selections[0].get("state") == 7) {
            Ext.Msg.alert("提示", "此案例已废除!");
            return;
        }
        if (selections[0].get("state") != 1 && selections[0].get("state") != 0) {
            Ext.Msg.alert("提示", "此案例已审核，无法修改!");
            return;
        }
        if (selections[0].get("delete") == '2') {
            Ext.Msg.alert("提示", "该记录已作废!");
            return;
        }

        var win = Ext.create("Ext.window.Window", {
            title: "案例附件管理",
            width: 1000,
            iconCls: 'Find',
            height: 300,
            modal: true,
            resizable: false,
            layout: 'border',
            autoScroll: true,
            tbar: [
                {
                    text: '添加',
                    iconCls: 'Pageadd',
                    handler: function () {
                        var father = this.up('window');

                        var form = Ext.create('Ext.form.Panel', {
                            region: "center",
                            autoScroll: true,
                            layout: 'anchor',
                            bodyPadding: 10,
                            defaults: {
                                anchor: '100%'
                            },
                            defaultType: 'textfield',
                            items: [{
                                xtype: "hiddenfield",
                                name: 'case_id',
                                value: selections[0].data.case_id
                            }, {
                                xtype: "hiddenfield",
                                name: 'att_id',
                                value: selections[0].data.att_id
                            },

                                {
                                    // 该列在整行中所占的百分比
                                    xtype: "textfield",
//				 								labelAlign : 'right',
//				 								labelWidth : 80,
                                    fieldLabel: '附件描述',
                                    name: 'att_type',
                                }
                                , {
                                    xtype: "hiddenfield",
                                    name: 'att_path'
                                }, {
                                    xtype: 'filefield',
                                    name: 'headPhoto',
                                    fieldLabel: '附件',
//				 								msgTarget : 'side',
//				 								labelWidth : 80,
                                    allowBlank: false,
                                    buttonText: '选择附件',
                                    validator: function (v) {
                                        if (!v.endWith(".jpg") && !v.endWith(".JPG")
                                            && !v.endWith(".png") && !v.endWith(".PNG")
                                            && !v.endWith(".gif") && !v.endWith(".GIF")
                                            && !v.endWith(".jpeg") && !v.endWith(".JPEG")) {
                                            return "请选择.jpg .png .gif.jpeg类型的图片";
                                        }
                                        return true;
                                    }
                                }],

                            buttons: [{
                                text: '上传',
                                iconCls: 'Diskupload',
                                handler: function () {
                                    var grid = this.up('window').father.down('gridpanel');
                                    var myWindow = this.up('window');
                                    var form = this.up('form').getForm();
                                    //myWindow.down('[name=case_id]').setValue(selections[0].data.case_id);
                                    if (!form.isValid()) {
                                        Ext.MessageBox.alert("提示信息", "请选择附件!");
                                        return;
                                    }
                                    form.submit({
                                        url: 'alcohol/attachment/uploadtt.do',
                                        method: 'post',
                                        waitMsg: '正在上传您的文件...',
                                        success: function (form, action) {
                                            Ext.Msg.alert("提示", "上传成功!");
                                            grid.getStore().reload();
                                            myWindow.close();
                                        },
                                        failure: function () {
                                            Ext.Msg.alert("提示", "上传失败，请联系管理员!");
                                            myWindow.close();
                                        }
                                    });
                                    //alert(selections[0].data.case_id);


                                }
                            }, {
                                text: '取消',
                                iconCls: 'Cancel',
                                handler: function () {
                                    this.up('window').close();
                                }
                            }]
                        });

                        var win = Ext.create("Ext.window.Window", {
                            title: '案例添加',
                            iconCls: 'Pageedit',
                            width: 400,
                            modal: true,
                            height: 300,
                            layout: 'border',
                            items: [form],
                            father: father
                        });
                        win.show();
                    }

                },
                {
                    text: '删除',
                    iconCls: 'Delete',
                    handler: function () {

                        var grid = this.up("window").down('gridpanel');
                        var selections = grid.getView().getSelectionModel().getSelection();
                        if (selections.length < 1) {
                            Ext.Msg.alert("提示", "请选择需要删除的记录!");
                            return;
                        }
                        ;

                        Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
                            if ("yes" == btn) {
                                Ext.Ajax.request({
                                    url: "alcohol/attachment/deletAttInfo.do",
                                    method: "POST",
                                    headers: {'Content-Type': 'application/json'},
                                    jsonData: {
                                        params: selections[0].data.att_id
                                    },
                                    success: function (response, options) {
                                        response = Ext.JSON.decode(response.responseText);
                                        if (response) {
                                            Ext.MessageBox.alert("提示信息", "成功");
                                            grid.getStore().reload();
                                        } else {
                                            Ext.MessageBox.alert("错误信息", "失败");
                                        }
                                    },
                                    failure: function () {
                                        Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                                    }

                                });
                            }
                        });

                    }
                }

            ],
            bodyStyle: "background-color:white;",
            items: [
                Ext.create('Ext.grid.Panel', {
                    renderTo: Ext.getBody(),
                    width: 1000,
                    height: 300,
                    frame: false,
                    viewConfig: {
                        forceFit: true,
                        stripeRows: true// 在表格中显示斑马线
                    },
                    store: {// 配置数据源
                        fields: ['att_id', 'att_path', 'case_id', 'att_type'],// 定义字段
                        proxy: {
                            type: 'jsonajax',
                            actionMethods: {
                                read: 'POST'
                            },
                            url: 'alcohol/attachment/getAtt.do',
                            params: {
                                'case_id': selections[0].get("case_id"),
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
                            text: '主键',
                            dataIndex: 'att_id',
                            hidden: true
                        }
                        ,
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


                            header: "操作",
                            dataIndex: '',
                            width: '10%',
                            menuDisabled: true,
                            renderer: function (value, cellmeta,
                                                record, rowIndex, columnIndex,
                                                store) {
                                if (record.data["photo_type"] == 1 || record.data["photo_type"] == 5 || record.data["photo_type"] == null || record.data["photo_type"] == 0) {
                                    return "<a href='#'>修改</a>";
                                }
                            },
                            listeners: {
                                'click': function () {
                                    var mei = this.up("gridpanel");
                                    var selections = mei.getView().getSelectionModel().getSelection();
                                    if (selections.length < 1) {
                                        Ext.Msg.alert("提示", "请选择案例!");
                                        return;
                                    }
                                    var form = Ext.create('Ext.form.Panel', {
                                        region: "center",
//											autoScroll : true,
                                        layout: 'anchor',
                                        bodyPadding: 10,
                                        defaults: {
                                            anchor: '100%'
                                        },
                                        defaultType: 'textfield',
                                        items: [{
                                            xtype: "hiddenfield",
                                            name: 'case_id'
                                        }, {
                                            xtype: "hiddenfield",
                                            name: 'att_id'
                                        }, {
                                            xtype: "textfield",
                                            name: 'att_type',
                                            fieldLabel: '附件描述'
                                        }, {
                                            xtype: "hiddenfield",
                                            name: 'att_path'
                                        }],

                                        buttons: [{
                                            text: '上传',
                                            iconCls: 'Diskupload',
                                            handler: function () {
                                                var me = this;
                                                var myWindow = me.up('window');
                                                var form = me.up('form').getForm();

                                                form.submit({
                                                    url: 'alcohol/attachment/uploadtd.do',
                                                    method: 'post',
                                                    waitMsg: '正在上传您的文件...',
                                                    success: function (form, action) {
                                                        Ext.Msg.alert("提示", "上传成功!");
                                                        var grid = mei.up("gridpanel");
                                                        mei.getStore().load();
                                                        myWindow.close();
                                                    },
                                                    failure: function () {
                                                        Ext.Msg.alert("提示", "上传失败，请联系管理员!");
                                                        myWindow.close();
                                                    }
                                                });

                                            }
                                        }, {
                                            text: '取消',
                                            iconCls: 'Cancel',
                                            handler: function () {
                                                this.up('window').close();
                                            }
                                        }]
                                    });
                                    var win = Ext.create("Ext.window.Window", {
                                        title: '案例描述修改',
                                        width: 400,
                                        iconCls: 'Pageadd',
                                        height: 200,
                                        modal: true,
                                        layout: 'border',
                                        items: [form]
                                    });
                                    form.loadRecord(selections[0]);
                                    win.show();
                                }
                            }


                        }, {
                            header: "查看附件",
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
                        }, {
                            header: "操作",
                            dataIndex: '',
                            width: '10%',
                            menuDisabled: true,
                            renderer: function (value, cellmeta,
                                                record, rowIndex, columnIndex,
                                                store) {
                                if (record.data["photo_type"] == 1 || record.data["photo_type"] == 5 || record.data["photo_type"] == null || record.data["photo_type"] == 0) {
                                    return "<a href='#'>修改</a>";
                                }
                            },
                            listeners: {
                                'click': function () {
                                    var mei = this.up("gridpanel");
                                    var selections = mei.getView().getSelectionModel().getSelection();
                                    if (selections.length < 1) {
                                        Ext.Msg.alert("提示", "请选择案例!");
                                        return;
                                    }
                                    var form = Ext.create('Ext.form.Panel', {
                                        region: "center",
//											autoScroll : true,
                                        layout: 'anchor',
                                        bodyPadding: 10,
                                        defaults: {
                                            anchor: '100%'
                                        },
                                        defaultType: 'textfield',
                                        items: [{
                                            xtype: "hiddenfield",
                                            name: 'case_id'
                                        }, {
                                            xtype: "hiddenfield",
                                            name: 'att_id'
                                        }, {
                                            xtype: "hiddenfield",
                                            name: 'att_type'
                                        }, {
                                            xtype: "hiddenfield",
                                            name: 'att_path'
                                        }, {
                                            xtype: 'filefield',
                                            name: 'headPhoto',
//														emptyText: selections[0].data.att_path,
                                            fieldLabel: '附件',
                                            msgTarget: 'side',
//														labelWidth : 80,
                                            allowBlank: false,
                                            anchor: '100%',
                                            buttonText: '选择附件',
                                            validator: function (v) {
                                                if (!v.endWith(".jpg") && !v.endWith(".JPG")
                                                    && !v.endWith(".png") && !v.endWith(".PNG")
                                                    && !v.endWith(".gif") && !v.endWith(".GIF")
                                                    && !v.endWith(".jpeg") && !v.endWith(".JPEG")) {
                                                    return "请选择.jpg .png .gif.jpeg类型的图片";
                                                }
                                                return true;
                                            }
                                        }],

                                        buttons: [{
                                            text: '上传',
                                            iconCls: 'Diskupload',
                                            handler: function () {
                                                var me = this;
                                                var myWindow = me.up('window');
                                                var form = me.up('form').getForm();
                                                if (!form.isValid()) {
                                                    Ext.MessageBox.alert("提示信息", "请选择附件!");
                                                    return;
                                                }
                                                form.submit({
                                                    url: 'alcohol/attachment/uploadt.do',
                                                    method: 'post',
                                                    waitMsg: '正在上传您的文件...',
                                                    success: function (form, action) {
                                                        Ext.Msg.alert("提示", "上传成功!");
                                                        var grid = mei.up("gridpanel");
                                                        mei.getStore().load();
                                                        myWindow.close();
                                                    },
                                                    failure: function () {
                                                        Ext.Msg.alert("提示", "上传失败，请联系管理员!");
                                                        myWindow.close();
                                                    }
                                                });

                                            }
                                        }, {
                                            text: '取消',
                                            iconCls: 'Cancel',
                                            handler: function () {
                                                this.up('window').close();
                                            }
                                        }]
                                    });
                                    var win = Ext.create("Ext.window.Window", {
                                        title: '案例附件修改',
                                        width: 400,
                                        iconCls: 'Pageadd',
                                        height: 200,
                                        modal: true,
                                        layout: 'border',
                                        items: [form]
                                    });
                                    form.loadRecord(selections[0]);
                                    win.show();
                                }
                            }

                        }


                    ]
                })]
        });
        win.show();
    },
    onUpdate: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        console.log(selections[0])
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要修改的记录!");
            return;
        }
        if (selections[0].get("state") == 7) {
            Ext.Msg.alert("提示", "此案例已废除!");
            return;
        }
        if (selections[0].get("state") != 1 && selections[0].get("state") != 0) {
            Ext.Msg.alert("提示", "此案例已审核，无法修改!");
            return;
        }
        ownpersonTemp = selections[0].get("receiver_id");
        ownaddressTemp = selections[0].get("area_code");
        var form = Ext.create("Rds.alcohol.form.AlcoholRegisterUpdateForm", {
            region: "center",
            autoScroll: true,
            grid: me
        });
        form.loadRecord(selections[0]);
        var win = Ext.create("Ext.window.Window", {
            title: '案例修改',
            iconCls: 'Pageedit',
            width: 700,
            height: 650,
            modal: true,
            layout: 'border',
            items: [form]
        });
        win.show();
        // form.get("province");
    },
    onSearch: function () {
        var me = this.up("gridpanel");
        me.getStore().currentPage = 1;
        me.getStore().load();
    },
    onExport: function () {
        var me = this.up("gridpanel");
        var params = me.store.proxy.extraParams;
        window.location.href = "alcohol/register/exportCaseInfo.do?case_code=" + params["case_code"]
            + "&client=" + params[client]
            + "&state=" + params["state"]
            + "&starttime=" + params["starttime"]
            + "&endtime=" + params["endtime"]
            + "&checkper=" + params["checkper"]
            + "&checkper_phone=" + params["checkper_phone"]
            + "&receiver=" + params["receiver"]
            + "&areacode=" + params["areacode"]
            + "&mail_address=" + params["mail_address"]
            + "&mail_per=" + params["mail_per"]
            + "&mail_phone=" + params["mail_phone"]
            + "&client_starttime" + fparams["client_starttime"]
            + "&client_endtime" + params["client_endtime"]
            + "&isDoubleTube" + params["isDoubleTube"]
            + "&case_in_pername" + params["case_in_pername"];
    },
    listeners: {
        'afterrender': function () {
            this.store.load();
        }
    }
});
