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

Ext.define("Rds.alcohol.panel.AlcoholPrintGridPanel", {
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
        var starttime = new Ext.form.DateField({
            id: 'alcohol_sample_starttime33',
            name: 'starttime',
            width: '20%',
            fieldLabel: '受理时间从',
            labelWidth: 70,
            labelAlign: 'left',
            emptyText: '请选择日期',
            vtype: 'daterange',
            endDateField: 'alcohol_sample_endtime33',
            maxValue: new Date(),
            format: 'Y-m-d',
            listeners: {
                'change': function (dom, value) {
                    if (value == null) {
                        this.up('toolbar').down('#alcohol_sample_endtime33').setMinValue('')
                    }
                }
            }
        });
        var endtime = new Ext.form.DateField({
            id: 'alcohol_sample_endtime33',
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
            labelWidth: 60,
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
            fieldLabel: '联系电话'
        });
        var isDoubleTube = new Ext.form.field.ComboBox({
            fieldLabel: '血管类型',
            width: '20%',
            labelWidth: 60,
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
        var isPrint = new Ext.form.field.ComboBox({
            fieldLabel: '是否打印',
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
                    ['否', '3']]
            }),
            value: '3',
            mode: 'local',
            name: 'isPrint'
        });
        var case_in_pername = Ext.create('Ext.form.field.Text', {
            name: 'case_in_pername',
            labelWidth: 70,
            width: '20%',
            fieldLabel: '登记人'
        });
        var sample_time_start = new Ext.form.DateField({
            id: 'alcohol_sample_starttime3',
            name: 'sample_time_start',
            width: '20%',
            fieldLabel: '登记日期从',
            labelWidth: 70,
            labelAlign: 'left',
            emptyText: '请选择日期',
            vtype: 'daterange',
            endDateField: 'alcohol_sample_endtime3',
            maxValue: new Date(),
            format: 'Y-m-d',
            listeners: {
                'change': function (dom, value) {
                    if (value == null) {
                        this.up('toolbar').down('#alcohol_sample_endtime3').setMinValue('')
                    }
                }
            }
        });
        var sample_time_end = new Ext.form.DateField({
            id: 'alcohol_sample_endtime3',
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
                "report_modelname", "accept_time", "sample_id",
                "mail_address", "close_time", 'state', "mail_per", "stateStr",
                'receiver', 'receiver_area', "mail_phone", "receiver_id",
                "report_url", "attachment", "case_in_per", "client_time",
                "sample_remark", "sample_result", "isDoubleTube", "sample_remark2", "is_detection"
                , "bloodnumA", "bloodnumB", "sample_time"],
//							   pageSize:2,
            proxy: {
                type: 'jsonajax',
                actionMethods: {
                    read: 'POST'
                },
                url: 'alcohol/print/getPrintCaseInfo.do',
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
                        case_in_pername: trim(case_in_pername.getValue()),
                        isPrint: isPrint.getValue()
                    });
                }
            }
        });
        me.selModel = Ext.create('Ext.selection.CheckboxModel', {
//			mode: 'SINGLE'
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
                text: '打印状态',
                dataIndex: 'state',
                menuDisabled: true,
                width: 100,
                renderer: function (value) {
                    if (value == 3) {
                        return "未打印"
                    } else {
                        return "<span style='color:green'>已打印</span>";
                    }
                }
            },
            {
                text: '模板名称',
                dataIndex: 'report_modelname',
                menuDisabled: true,
                width: 120
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
                    if (value == 0) {
                        return "<span style='color:red'>促凝管</span>";
                    }
                    return "<span style='color:blue'>真空采血管</span>";
                }
            },
            {
                text: '备注',
                dataIndex: 'remark',
                menuDisabled: true,
                width: 300
            }
        ];
        me.dockedItems = [
            {
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [case_code, client, checkper, checkper_phone, receiver]
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
                items: [sample_time_start, sample_time_end, areacode, isDoubleTube]
            },
            {
                style: {
                    borderTopWidth: '0px !important',
                    borderBottomWidth: '1px !important'
                },
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [case_in_pername, isPrint, {
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
                    text: '打印案例',
                    iconCls: 'Printer',
                    handler: me.onPrint
                }, {
                    text: '报告下载',
                    iconCls: 'Pageexcel',
                    handler: me.getAlcoholWord
                }, {
                    text: '批量下载',
                    iconCls: 'Pageexcel',
                    handler: me.getZip
                }]
            }
        ];

        me.callParent(arguments);
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
            url: "alcohol/print/getZip.do",
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            jsonData: data,
            success: function (response, options) {
                Ext.getBody().unmask();
                window.location.href = "alcohol/print/getZipAfter.do"
            },
            failure: function () {
                Ext.getBody().unmask();
                Ext.Msg.alert("提示", "下载失败<br>请联系管理员!");
            }
        });

    },
    getAlcoholWord: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel()
            .getSelection();
        if (selections.length < 1 || selections.length > 1) {
            Ext.Msg.alert("提示", "请选择一条需要下载的记录!");
            return;
        }
        ;
        Ext.MessageBox
            .confirm(
                '提示',
                '确定下载此案例吗',
                function (id) {
                    if (id == 'yes') {
                        window.location.href = "alcohol/print/getAlcoholWord.do?case_code="
                            + selections[0].get("case_code")
                            + "&case_id="
                            + selections[0].get("case_id") + "&report_model=" + selections[0].get("report_model")
                            + "&type=jjjymodel";
                    }
                });
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
    onPrint: function () {
//		if (!!window.ActiveXObject || "ActiveXObject" in window) {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel().getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要打印的记录!");
            return;
        }
//			var case_code = selections[0].get("case_code");
        var case_code = '';
        var case_codes = '';
        for (var i = 0; i < selections.length; i++) {
            case_code += selections[i].get("case_code") + ',';
            case_codes += "'" + selections[i].get("case_code") + "',"
        }
        case_code = case_code.substring(0, (case_code.length - 1));
//			Ext.MessageBox.confirm('提示','是否小白鼠案例？',function(id) {
//						if (id == 'yes') {

        var print_print = function (me) {
            Ext.MessageBox.confirm('提示', '确定打印吗？', function (id) {
                if (id == 'yes') {


                    Ext.Ajax.request({
                        url: "alcohol/print/printCase.do",
                        method: "POST",
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        jsonData: {
                            case_code: case_codes.substring(0, (case_codes.length - 1))
                        },
                        success: function (response, options) {
                            response = Ext.JSON
                                .decode(response.responseText);
                            if (response == true) {
                                var iframe = document.getElementById("alcoholmodel");
                                iframe.contentWindow.focus();
                                iframe.contentWindow.print();
                                win.close();
                            }
                        },
                        failure: function () {
                            Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
                        }
                    });


                }
            });
        }

        var print_chanel = function () {
            win.close();
        }
        var src = "alcohol/print/getPrintModel.do?type=jjjymodel&web=alcohol/print_xbs&case_code=" + case_code;
        var win = Ext.create("Ext.window.Window", {
            title: '案例打印',
            maximized: true,
            maximizable: true,
            iconCls: 'Printer',
            modal: true,
            bodyStyle: "background-color:white;",
            html: "<iframe width=100% height=100% id='alcoholmodel' src='"
            + src + "'></iframe>",
            buttons: [{
                text: '打印',
                iconCls: 'Disk',
                handler: print_print
            }, {
                text: '取消',
                iconCls: 'Cancel',
                handler: print_chanel
            }]
        });
        win.show();
//						}
//						else{
//
//							var print_print = function(me) {
//								Ext.MessageBox.confirm('提示', '确定打印吗？', function(id) {
//									if (id == 'yes') {
//										Ext.Ajax.request({
//													url : "alcohol/print/printCase.do",
//													method : "POST",
//													headers : {
//														'Content-Type' : 'application/json'
//													},
//													jsonData : {
//														case_code : case_codes.substring(0,(case_codes.length-1))
//													},
//													success : function(response, options) {
//														response = Ext.JSON
//																.decode(response.responseText);
//														if (response == true) {
//															var iframe = document.getElementById("alcoholmodel");
//															iframe.contentWindow.focus();
//															iframe.contentWindow.print();
//															win.close();
//														}
//													},
//													failure : function() {
//														Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
//													}
//												});
//									}
//								});
//							}
//							var print_chanel = function() {
//								win.close();
//							}
//							var src ="alcohol/print/getPrintModel.do?type=jjjymodel&web=alcohol/print_zyjd&case_code=" + case_code;
//							var win = Ext.create("Ext.window.Window", {
//										title : '案例打印',
//										maximized : true,
//										maximizable : true,
//										iconCls : 'Printer',
//										modal : true,
//										bodyStyle : "background-color:white;",
//										html : "<iframe width=100% height=100% id='alcoholmodel' src='"
//												+ src + "'></iframe>",
//										buttons : [{
//													text : '打印',
//													iconCls : 'Disk',
//													handler : print_print
//												},{
//													text : '取消',
//													iconCls : 'Cancel',
//													handler : print_chanel
//												}]
//									});
//							win.show();
//						}
//			})
//			return;


//		} else {
//			Ext.Msg.alert("提示", "该浏览器不支持打印,请使用IE!");
//		}
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
