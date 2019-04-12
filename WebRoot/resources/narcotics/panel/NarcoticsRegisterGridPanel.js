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
Ext.define("Rds.narcotics.panel.NarcoticsRegisterGridPanel", {
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
        var case_num = Ext.create('Ext.form.field.Text', {
            name: 'case_num',
            labelWidth: 70,
            width: '25%',
            fieldLabel: '鉴定号'
        });
        var client = Ext.create('Ext.form.field.Text', {
            name: 'client',
            labelWidth: 70,
            width: '25%',
            fieldLabel: '委托人'
        });
        var person_name = Ext.create('Ext.form.field.Text', {
            name: 'person_name',
            labelWidth: 70,
            width: '25%',
            fieldLabel: '被鉴定人'
        });
        var person_sex = new Ext.form.field.ComboBox({
            fieldLabel: '性别',
            width: '22%',
            labelWidth: 30,
            editable: false,
            triggerAction: 'all',
            mode: 'local',
            name: 'person_sex',
            displayField: 'Name',
            valueField: 'Code',
            store: new Ext.data.ArrayStore({
                fields: ['Name', 'Code'],
                data: [['全部', ''], ['男', '0'], ['女', '1']]
            })
        });
        var person_card = Ext.create('Ext.form.field.Text', {
            name: 'person_card',
            labelWidth: 70,
            width: '25%',
            fieldLabel: '身份证'
        });
        var identification_materials = new Ext.form.field.ComboBox({
            fieldLabel: '鉴定材料',
            width: '25%',
            labelWidth: 70,
            editable: false,
            triggerAction: 'all',
            mode: 'local',
            name: 'identification_materials',
            displayField: 'Name',
            valueField: 'Code',
            store: new Ext.data.ArrayStore({
                fields: ['Name', 'Code'],
                data: [['全部', ''], ['血液', '0'], ['尿液', '1'], ['头发', '2'], ['腋毛', '3'], ['阴毛', '4']]
            })
        });
        var identification_start = new Ext.form.DateField({
            id: 'identification_starts',
            name: 'identification_start',
            width: '25%',
            fieldLabel: '鉴定时间从',
            labelWidth: 70,
            emptyText: '请选择日期',
            vtype: 'daterange',
            maxValue: new Date(),
            endDateField: 'identification_ends',
            format: 'Y-m-d',
            listeners: {
                'change': function (dom, value) {
                    if (value == null) {
                        this.up('toolbar').down('#identification_ends').setMinValue('')
                    }
                }
            }
        });
        var identification_end = new Ext.form.DateField({
            id: 'identification_ends',
            name: 'identification_end',
            width: '22%',
            labelWidth: 30,
            maxValue: new Date(),
            fieldLabel: ' 到 ',
            emptyText: '请选择日期',
            format: 'Y-m-d'
        });
        var case_card = Ext.create('Ext.form.field.Text', {
            name: 'case_card',
            labelWidth: 70,
            width: '25%',
            fieldLabel: '样品编号'
        });
        var client_time_start = new Ext.form.DateField({
            id: 'client_time_starts',
            name: 'client_time_start',
            width: '25%',
            fieldLabel: '受理时间从',
            labelWidth: 70,
            emptyText: '请选择日期',
            vtype: 'daterange',
            endDateField: 'client_time_ends',
            format: 'Y-m-d',
            maxValue: new Date(),
            listeners: {
                'change': function (dom, value) {
                    if (value == null) {
                        this.up('toolbar').down('#client_time_ends').setMinValue('')
                    }
                }
            }
        });
        var client_time_end = new Ext.form.DateField({
            id: 'client_time_ends',
            name: 'client_time_end',
            width: '22%',
            labelWidth: 30,
            fieldLabel: ' 到 ',
            labelAlign: 'left',
            emptyText: '请选择日期',
            format: 'Y-m-d',
            maxValue: new Date()
        });
        var state = new Ext.form.field.ComboBox({
            fieldLabel: '是否废除',
            width: '25%',
            labelWidth: 70,
            editable: false,
            triggerAction: 'all',
            mode: 'local',
            value: '1',
            name: 'state',
            displayField: 'Name',
            valueField: 'Code',
            store: new Ext.data.ArrayStore({
                fields: ['Name', 'Code'],
                data: [['全部', ''], ['已废除', '0'], ['未废除', '1']]
            })
        });

        me.store = Ext.create('Ext.data.Store', {
            fields: [
                "case_id",
                "case_num",
                "client",
                "client_time",
                "person_name",
                "person_sex",
                "person_card",
                "identification_materials",
                "identification_start",
                "identification_end",
                "identification_site",
                "case_card",
                "case_basic",
                "instrument_type",
                "entrusted_matters",
                "materials_totals",
                "materials_color",
                "materials_length",
                "materials_weight",
                "partial_weight",
                "liquid_color",
                "ifleakage",
                "appraisal_opinion",
                "case_in_per",
                "username",
                "case_in_time",
                "state",
                "identification_per",
                "report_time"
            ],
            proxy: {
                type: 'jsonajax',
                actionMethods: {
                    read: 'POST'
                },
                url: 'narcotics/register/getCaseInfo.do',
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
                'beforeload': function () {
                    Ext.apply(me.store.proxy.extraParams, {
                        case_num: trim(case_num.getValue()),
                        client: trim(client.getValue()),
                        person_name: trim(person_name.getValue()),
                        person_sex: trim(person_sex.getValue()),
                        person_card: trim(person_card.getValue()),
                        case_card: trim(case_card.getValue()),
                        identification_materials: trim(identification_materials.getValue()),
                        identification_start: dateformat(identification_start.getValue()),
                        identification_end: dateformat(identification_end.getValue()),
                        client_time_start: dateformat(client_time_start.getValue()),
                        client_time_end: dateformat(client_time_end.getValue()),
                        state: trim(state.getValue())
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
                dataIndex: 'case_id',
                hidden: true
            },
            {
                text: '鉴定号',
                dataIndex: 'case_num',
                menuDisabled: true,
                width: 120,
                renderer: function (value, cellmeta, record) {
                    var state = record.data["state"];
                    if (state == 0) {
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
                menuDisabled: true,
                width: 120
            },
            {
                text: '受理日期',
                dataIndex: 'client_time',
                menuDisabled: true,
                width: 120
            },
            {
                text: '报告时间',
                dataIndex: 'report_time',
                menuDisabled: true,
                width: 120
            },
            {
                text: '被鉴定人',
                dataIndex: 'person_name',
                menuDisabled: true,
                width: 120
            },
            {
                text: '性别',
                dataIndex: 'person_sex',
                menuDisabled: true,
                width: 120,
                renderer: function (value) {
                    if (value == 0) {
                        return "<span style='color:red'>男</span>";
                    }
                    return "<span style='color:blue'>女</span>";
                }
            },
            {
                text: '身份证',
                dataIndex: 'person_card',
                menuDisabled: true,
                width: 120
            },
            {
                text: '鉴定材料',
                dataIndex: 'identification_materials',
                menuDisabled: true,
                width: 120,
                renderer: function (value) {
                    if (value == 0) {
                        return "<span style='color:blue'>血液</span>";
                    } else if (value == 1) {
                        return "<span style='color:blue'>尿液</span>";
                    } else if (value == 2) {
                        return "<span style='color:red'>头发</span>";
                    } else if (value == 3) {
                        return "<span style='color:red'>腋毛</span>";
                    }
                    return "<span style='color:red'>阴毛</span>";
                }
            },
            {
                text: '鉴定时间',
                dataIndex: 'identification_start',
                menuDisabled: true,
                width: 120
            },
            {
                text: '样品编号',
                dataIndex: 'case_card',
                menuDisabled: true,
                width: 120
            },
            {
                text: '仪器型号',
                dataIndex: 'instrument_type',
                menuDisabled: true,
                width: 120
            },
            {
                text: '委托事项',
                dataIndex: 'entrusted_matters',
                menuDisabled: true,
                width: 120,
                renderer: function (value) {
                    if (value == 0) {
                        return "<span style='color:red'>苯丙胺类成分分析</span>";
                    }
                    return "<span style='color:blue'>阿片类成分分析</span>";
                }
            },
            {
                text: '材料总长',
                dataIndex: 'materials_totals',
                menuDisabled: true,
                width: 120
            },
            {
                text: '材料颜色',
                dataIndex: 'materials_color',
                menuDisabled: true,
                width: 120
            },
            {
                text: '距根部距离',
                dataIndex: 'materials_length',
                menuDisabled: true,
                width: 120
            },
            {
                text: '材料重量',
                dataIndex: 'materials_weight',
                menuDisabled: true,
                width: 120
            },
            {
                text: '取样重量',
                dataIndex: 'partial_weight',
                menuDisabled: true,
                width: 120
            },
            {
                text: '液体颜色',
                dataIndex: 'liquid_color',
                menuDisabled: true,
                width: 120
            },
            {
                text: '有无泄漏',
                dataIndex: 'ifleakage',
                menuDisabled: true,
                width: 120,
                renderer: function (value) {
                    if (value == 0) {
                        return "<span style='color:red'>无</span>";
                    } else if (value == 1) {
                        return "<span style='color:blue'>有</span>";
                    }
                }
            },
            {
                text: '登记人',
                dataIndex: 'username',
                menuDisabled: true,
                width: 120
            },
            {
                text: '登记时间',
                dataIndex: 'case_in_time',
                menuDisabled: true,
                width: 120
            }
        ];
        me.startdate = new Date(new Date().getFullYear() + '/01/01');
        me.selModel = Ext.create('Ext.selection.CheckboxModel', {});
        me.dockedItems = [
            {
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [case_num, client, person_name, person_sex]
            },
            {
                style: {
                    borderTopWidth: '0px !important',
                    borderBottomWidth: '0px !important'
                },
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [person_card, identification_materials, identification_start, identification_end]
            },
            {
                style: {
                    borderTopWidth: '0px !important',
                    borderBottomWidth: '0px !important'
                },
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [case_card, state, client_time_start, client_time_end]
            },
            {
                style: {
                    borderTopWidth: '0px !important',
                    borderBottomWidth: '0px !important'
                },
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [{
                    text: '查询',
                    iconCls: 'Find',
                    handler: me.onSearch
                }]
            },
            {
                xtype: 'toolbar',
                dock: 'top',
                items: [
                    {
                        text: '案例登记',
                        iconCls: 'Pageadd',
                        handler: me.onInsert
                    },
                    {
                        text: '修改',
                        iconCls: 'Pageedit',
                        handler: me.onUpdate
                    },
                    {
                        text: '废除',
                        iconCls: 'Delete',
                        handler: me.onDelete
                    },
                    {
                        text: '报告下载',
                        iconCls: 'Pageexcel',
                        handler: me.getAlcoholWord
                    },
                    {
                        text: '批量下载',
                        iconCls: 'Pageexcel',
                        handler: me.getZip
                    }
                ]
            }
        ];

        me.callParent(arguments);
    },
    onSearch: function () {
        var me = this.up("gridpanel");
        me.getStore().currentPage = 1;
        me.getStore().load();
    },
    onInsert: function () {
        var me = this.up("gridpanel");
        var form = Ext.create("Rds.narcotics.form.NarcoticsRegisterInsertForm", {
            region: "center",
            autoScroll: true,
            grid: me,
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
    onUpdate: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1 || selections.length > 1) {
            Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
            return;
        };
        if (selections[0].get("state") == 0) {
            Ext.Msg.alert("提示", "此案例已废除!");
            return;
        }
        var form = Ext.create("Rds.narcotics.form.NarcoticsRegisterUpdateForm", {
            region: "center",
            autoScroll: true,
            grid: me,
            case_id: selections[0].data.case_id,
            identification_materials: selections[0].data.identification_materials,
            identification_per: selections[0].data.identification_per
        });
        var win = Ext.create("Ext.window.Window", {
            title: '案例修改',
            iconCls: 'Pageedit',
            width: 700,
            height: 650,
            modal: true,
            layout: 'border',
            items: [form]
        });
        form.loadRecord(selections[0]);
        win.hide();
        win.show();
    },
    onDelete: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();

        if (selections.length == 0) {
            Ext.Msg.alert("提示", "请至少选择一条需要废除的定价配置!");
            return;
        }
        ;
        var case_ids = "";
        for (var i = 0; i < selections.length; i++) {
            if (i == 0) {
                case_ids += selections[i].get("case_id");
            } else {
                case_ids += ',' + selections[i].get("case_id");
            }
        }
        var values = {
            case_ids: case_ids
        };

        Ext.MessageBox.confirm('提示', '确定废除改案例吗', function (id) {
            if (id == 'yes') {
                Ext.Ajax.request({
                    url: "narcotics/register/deletecaseInfo.do",
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    jsonData: values,
                    success: function (response) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response == true) {
                            Ext.MessageBox.alert("提示信息", "废除成功！");
                            me.getStore().load();
                        } else {
                            Ext.MessageBox.alert("错误信息", "废除失败！");
                        }
                    },
                    failure: function () {
                        Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
                    }
                });
            }
        });
    },
    getAlcoholWord: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1 || selections.length > 1) {
            Ext.Msg.alert("提示", "请选择一条需要下载的记录!");
            return;
        };
        Ext.MessageBox.confirm('提示', '确定下载此案例吗', function (id) {
            if (id == 'yes') {
                window.location.href =
                    "narcotics/register/getNarcoticsWord.do?case_id=" + selections[0].get("case_id")+"&case_num="+selections[0].get("case_num");
            }
        });
    },
    getZip: function () {
        var me = this.up("gridpanel");
        var selections = me.selModel.getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要下载的记录!");
            return;
        }
        ;
        var data = new Array();
        for (var i = 0; i < selections.length; i++) {
            data[i] = selections[i].data;
        }
        Ext.getBody().mask("请稍等，正在下载中...", "x-mask-loading");
        Ext.Ajax.request({
            url: "narcotics/register/getZip.do",
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            jsonData: data,
            success: function (response, options) {
                Ext.getBody().unmask();
                window.location.href = "narcotics/register/getZipAfter.do"
            },
            failure: function () {
                Ext.getBody().unmask();
                Ext.Msg.alert("提示", "下载失败<br>请联系管理员!");
            }
        });
    },
    listeners: {
        'afterrender': function () {
            this.store.load();
        }
    }
});
