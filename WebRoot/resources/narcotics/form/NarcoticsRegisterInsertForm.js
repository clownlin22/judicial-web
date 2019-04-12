var identification_per = Ext.create('Ext.data.Store', {
    model : 'model',
    proxy : {
        type : 'jsonajax',
        actionMethods : {
            read : 'POST'
        },
        url : 'narcotics/register/getIdentificationPer.do',
        reader : {
            type : 'json',
            root : 'data'
        }
    },
    autoLoad : true,
    remoteSort : true
});
Ext.define('Rds.narcotics.form.NarcoticsRegisterInsertForm', {
    extend: 'Ext.form.Panel',
    initComponent: function () {
        var me = this;
        me.items = [{
            xtype: 'form',
            style: 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
            bodyPadding: 10,
            defaults: {
                anchor: '100%',
                labelWidth: 80,
                labelSeparator: "：",
                labelAlign: 'right'
            },
            border: false,
            autoHeight: true,
            items: [
                    {
                        border: false,
                        xtype: 'fieldcontainer',
                        layout: "column",
                        items: [
                            {
                                xtype: 'fieldset',
                                title: '委托信息',
                                layout: 'anchor',
                                defaults: {
                                    anchor: '100%'
                                },
                                items: [{
                                    border: false,
                                    xtype: 'fieldcontainer',
                                    layout: "column",
                                    items: [{
                                        xtype: "textfield",
                                        fieldLabel: '鉴定号',
                                        labelWidth: 80,
                                        allowBlank: false,
                                        regex: /^[1-9]\d*$/,
                                        name: 'case_num',
                                        id: 'case_num',
                                        labelAlign: 'right',
                                        width: 300,
                                        maxLength: 20,
                                        validator: function (value) {
                                            var result = false;
                                            Ext.Ajax.request({
                                                url: "narcotics/register/exsitCaseNum.do",
                                                method: "POST",
                                                async: false,
                                                headers: {
                                                    'Content-Type': 'application/json'
                                                },
                                                jsonData: {
                                                    case_num: value
                                                },
                                                success: function (response) {
                                                    response = Ext.JSON.decode(response.responseText);
                                                    if (!response) {
                                                        result = "此鉴定号已存在";
                                                    } else {
                                                        result = true;
                                                    }
                                                },
                                                failure: function () {
                                                    Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
                                                }
                                            });
                                            return result;
                                        }
                                    },{
                                        xtype: "textfield",
                                        fieldLabel: '委托人',
                                        labelWidth: 80,
                                        allowBlank: false,
                                        name: 'client',
                                        id: 'client',
                                        labelAlign: 'right',
                                        width: 300,
                                        maxLength: 20
                                    }]
                                },{
                                    border: false,
                                    xtype: 'fieldcontainer',
                                    layout: "column",
                                    items: [{
                                        xtype: 'datefield',
                                        name: 'client_time',
                                        id: 'client_time',
                                        labelWidth: 80,
                                        allowBlank: false,
                                        width: 300,
                                        fieldLabel: '受理日期 ',
                                        labelAlign: 'right',
                                        maxValue : new Date(),
                                        format: 'Y-m-d'
                                    },{
                                        xtype: "combo",
                                        fieldLabel: '委托事项',
                                        mode: 'local',
                                        id: 'entrusted_matters',
                                        name: 'entrusted_matters',
                                        labelWidth: 80,
                                        allowBlank: false,
                                        editable: false,
                                        displayField: 'Name',
                                        valueField: 'Code',
                                        labelAlign: 'right',
                                        width: 300,
                                        blankText: "不能为空",
                                        store: new Ext.data.ArrayStore({
                                            fields: ['Name', 'Code'],
                                            data: [['苯丙胺类成分分析', 0], ['阿片类成分分析', 1],['乙醇定性定量分析',3]]
                                        })
                                    }]
                                }]
                            },
                            {
                                xtype: 'fieldset',
                                title: '鉴定信息',
                                layout: 'anchor',
                                defaults: {
                                    anchor: '100%'
                                },
                                items: [
                                    {
                                        border: false,
                                        xtype: 'fieldcontainer',
                                        layout: "column",
                                        items: [{
                                            xtype: "textfield",
                                            fieldLabel: '被鉴定人',
                                            labelWidth: 80,
                                            allowBlank: false,
                                            name: 'person_name',
                                            id: 'person_name',
                                            labelAlign: 'right',
                                            width: 300,
                                            maxLength: 20
                                        },{
                                            xtype: "combo",
                                            fieldLabel: '性别',
                                            mode: 'local',
                                            id: 'person_sex',
                                            name: 'person_sex',
                                            labelWidth: 80,
                                            allowBlank: false,
                                            editable: false,
                                            displayField: 'Name',
                                            valueField: 'Code',
                                            labelAlign: 'right',
                                            width: 300,
                                            blankText: "不能为空",
                                            store: new Ext.data.ArrayStore({
                                                fields: ['Name', 'Code'],
                                                data: [['男', 0], ['女', 1]]
                                            })
                                        }]
                                    },
                                    {
                                        border: false,
                                        xtype: 'fieldcontainer',
                                        layout: "column",
                                        items: [{
                                            xtype: "textfield",
                                            fieldLabel: '样品编号',
                                            allowBlank: false,
                                            labelWidth: 80,
                                            name: 'case_card',
                                            id: 'case_card',
                                            labelAlign: 'right',
                                            width: 300,
                                            maxLength: 20
                                        },{
                                            xtype: "textfield",
                                            fieldLabel: '身份证',
                                            // allowBlank: false,
                                            labelWidth: 80,
                                            name: 'person_card',
                                            id: 'person_card',
                                            labelAlign: 'right',
                                            regex: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
                                            width: 300,
                                        }]
                                    },
                                    {
                                        border: false,
                                        xtype: 'fieldcontainer',
                                        layout: "column",
                                        items: [{
                                            xtype: 'datefield',
                                            width: 300,
                                            name: 'identification_start',
                                            id: 'identification_start',
                                            labelWidth: 80,
                                            fieldLabel: '鉴定时间(初始)',
                                            allowBlank: false,
                                            maxValue : new Date(),
                                            labelAlign: 'right',
                                            format: 'Y-m-d',
                                            labelAlign: 'right'
                                        }, {
                                            xtype: 'datefield',
                                            name: 'identification_end',
                                            id: 'identification_end',
                                            labelWidth: 80,
                                            allowBlank: false,
                                            width: 300,
                                            fieldLabel: '鉴定时间(结束)',
                                            labelAlign: 'right',
                                            maxValue : new Date(),
                                            format: 'Y-m-d',
                                            labelAlign: 'right',
                                            listeners: {
                                                focus: function () {
                                                    var delivery_date_time = this.up('form').down('#identification_start');
                                                    var sampling_date_time = this.up('form').down('#identification_end');
                                                    var start = delivery_date_time
                                                        .getValue();
                                                    sampling_date_time.setMinValue(
                                                        start);
                                                }
                                            }
                                        }]
                                    },
                                    {
                                        border: false,
                                        xtype: 'fieldcontainer',
                                        layout: "column",
                                        items: [{
                                            xtype: "combo",
                                            fieldLabel: '鉴定地点',
                                            mode: 'local',
                                            id: 'identification_site',
                                            name: 'identification_site',
                                            labelWidth: 80,
                                            allowBlank: false,
                                            editable: false,
                                            displayField: 'Name',
                                            valueField: 'Code',
                                            labelAlign: 'right',
                                            width: 300,
                                            blankText: "不能为空",
                                            store: new Ext.data.ArrayStore({
                                                fields: ['Name', 'Code'],
                                                data: [['江苏苏博检测技术有限公司司法鉴定所', 0]]
                                            })
                                        },{
                                            xtype: "textfield",
                                            fieldLabel: '仪器型号',
                                            labelWidth: 80,
                                            allowBlank: false,
                                            name: 'instrument_type',
                                            id: 'instrument_type',
                                            labelAlign: 'right',
                                            width: 300,
                                            maxLength: 20
                                        }]
                                    },
                                    {
                                        border: false,
                                        xtype: 'fieldcontainer',
                                        layout: "column",
                                        items: [{
                                            width: 300,
                                            xtype: "combo",
                                            fieldLabel: '鉴定人(可多选)<span style="color:red">*</span>',
                                            valueField: "key",
                                            displayField: "value",
                                            labelWidth: 100,
                                            forceSelection: true,
                                            allowBlank: false,
                                            editable: false,
                                            blankText: "不能为空",
                                            emptyText: '请选择鉴定人',
                                            name: 'identification_per',
                                            editable: false,
                                            multiSelect: true,
                                            store: identification_per
                                        },{
                                            xtype: 'datefield',
                                            name: 'report_time',
                                            id: 'report_time',
                                            labelWidth: 80,
                                            allowBlank: false,
                                            width: 300,
                                            fieldLabel: '报告时间',
                                            labelAlign: 'right',
                                            maxValue : new Date(),
                                            format: 'Y-m-d'
                                        }]
                                    }
                                ]
                            },
                            {
                                xtype: 'fieldset',
                                title: '材料信息',
                                layout: 'anchor',
                                defaults: {
                                    anchor: '100%'
                                },
                                items: [
                                    {
                                        border: false,
                                        xtype: 'fieldcontainer',
                                        layout: "column",
                                        items: [{
                                            xtype: "combo",
                                            fieldLabel: '鉴定材料 ',
                                            mode: 'local',
                                            id: 'identification_materials',
                                            name: 'identification_materials',
                                            labelWidth: 80,
                                            allowBlank: false,
                                            editable: false,
                                            displayField: 'Name',
                                            valueField: 'Code',
                                            labelAlign: 'right',
                                            width: 300,
                                            blankText: "不能为空",
                                            store: new Ext.data.ArrayStore({
                                                fields: ['Name', 'Code'],
                                                data: [ ['血液', 0],['尿液', 1], ['头发', 2],['腋毛', 3],['阴毛', 4]]
                                            }),
                                            listeners: {
                                                'select': function (a, b) {
                                                    var choose = b[0].data.Code;
                                                    var materials_totals = Ext.getCmp('materials_totals');
                                                    var materials_color = Ext.getCmp('materials_color');
                                                    var materials_length = Ext.getCmp('materials_length');
                                                    var materials_weight = Ext.getCmp('materials_weight');
                                                    var partial_weight = Ext.getCmp('partial_weight');
                                                    var liquid_color = Ext.getCmp('liquid_color');
                                                    var ifleakage = Ext.getCmp('ifleakage');
                                                    if (choose == 1 ||choose == 0 ) {
                                                        materials_totals.hide();
                                                        materials_color.hide();
                                                        materials_length.hide();
                                                        materials_weight.hide();
                                                        partial_weight.hide();
                                                        liquid_color.show();
                                                        ifleakage.show();
                                                        materials_totals.allowBlank = true;
                                                        materials_color.allowBlank = true;
                                                        // materials_length.allowBlank = true;
                                                        materials_weight.allowBlank = true;
                                                        partial_weight.allowBlank = true;
                                                        liquid_color.allowBlank = false;
                                                        ifleakage.allowBlank = false;
                                                    }else if(choose == 2 ||choose == 3 ||choose == 4){
                                                        materials_totals.show();
                                                        materials_color.show();
                                                        materials_length.show();
                                                        materials_weight.show();
                                                        partial_weight.show();
                                                        liquid_color.hide();
                                                        ifleakage.hide();
                                                        materials_totals.allowBlank = false;
                                                        materials_color.allowBlank = false;
                                                        // materials_length.allowBlank = false;
                                                        materials_weight.allowBlank = false;
                                                        partial_weight.allowBlank = false;
                                                        liquid_color.allowBlank = true;
                                                        ifleakage.allowBlank = true;
                                                    }else{
                                                        materials_totals.hide();
                                                        materials_color.hide();
                                                        materials_length.hide();
                                                        materials_weight.hide();
                                                        partial_weight.hide();
                                                        liquid_color.hide();
                                                        ifleakage.hide();
                                                        materials_totals.allowBlank = true;
                                                        materials_color.allowBlank = true;
                                                        // materials_length.allowBlank = true;
                                                        materials_weight.allowBlank = true;
                                                        partial_weight.allowBlank = true;
                                                        liquid_color.allowBlank = true;
                                                        ifleakage.allowBlank = true;
                                                    }
                                                }
                                            }
                                        },{
                                            xtype: "textfield",
                                            fieldLabel: '取样重量',
                                            labelWidth: 80,
                                            hidden: true,
                                            allowBlank: false,
                                            regex: /^[1-9]\d*$/,
                                            name: 'partial_weight',
                                            id: 'partial_weight',
                                            labelAlign: 'right',
                                            width: 300,
                                            maxLength: 20
                                        }]
                                    },
                                    {
                                        border: false,
                                        xtype: 'fieldcontainer',
                                        layout: "column",
                                        items: [{
                                            xtype: "textfield",
                                            fieldLabel: '材料总长',
                                            labelWidth: 80,
                                            hidden: true,
                                            allowBlank: false,
                                            regex: /^[1-9]\d*$/,
                                            name: 'materials_totals',
                                            id: 'materials_totals',
                                            labelAlign: 'right',
                                            width: 300,
                                            maxLength: 20
                                        },{
                                            xtype: "textfield",
                                            fieldLabel: '材料颜色',
                                            labelWidth: 80,
                                            hidden: true,
                                            allowBlank: false,
                                            name: 'materials_color',
                                            id: 'materials_color',
                                            labelAlign: 'right',
                                            width: 300,
                                            maxLength: 20
                                        }]
                                    },
                                    {
                                        border: false,
                                        xtype: 'fieldcontainer',
                                        layout: "column",
                                        items: [{
                                            xtype: "textfield",
                                            fieldLabel: '距根部',
                                            labelWidth: 80,
                                            hidden: true,
                                            // allowBlank: false,
                                            regex: /^[1-9]\d*$/,
                                            name: 'materials_length',
                                            id: 'materials_length',
                                            labelAlign: 'right',
                                            width: 300,
                                            maxLength: 20
                                        },{
                                            xtype: "textfield",
                                            fieldLabel: '材料总重',
                                            labelWidth: 80,
                                            hidden: true,
                                            allowBlank: false,
                                            regex: /^[1-9]\d*$/,
                                            name: 'materials_weight',
                                            id: 'materials_weight',
                                            labelAlign: 'right',
                                            width: 300,
                                            maxLength: 20
                                        }]
                                    },
                                    {
                                        border: false,
                                        xtype: 'fieldcontainer',
                                        layout: "column",
                                        items: [{
                                            xtype: "textfield",
                                            fieldLabel: '液体颜色',
                                            labelWidth: 80,
                                            hidden: true,
                                            allowBlank: false,
                                            name: 'liquid_color',
                                            id: 'liquid_color',
                                            labelAlign: 'right',
                                            width: 300,
                                            maxLength: 20
                                        },{
                                            xtype: "combo",
                                            fieldLabel: '有无泄漏',
                                            mode: 'local',
                                            id: 'ifleakage',
                                            name: 'ifleakage',
                                            labelWidth: 80,
                                            hidden: true,
                                            allowBlank: false,
                                            editable: false,
                                            displayField: 'Name',
                                            valueField: 'Code',
                                            labelAlign: 'right',
                                            width: 300,
                                            blankText: "不能为空",
                                            store: new Ext.data.ArrayStore({
                                                fields: ['Name', 'Code'],
                                                data: [['无', 0], ['有', 1]]
                                            })
                                        }]
                                    }
                                ]
                            },
                            {
                                xtype: 'fieldset',
                                title: '案情信息',
                                layout: 'anchor',
                                defaults: {
                                    anchor: '100%'
                                },
                                items: [
                                    {
                                        border: false,
                                        xtype: 'fieldcontainer',
                                        layout: "column",
                                        items: [
                                            {
                                                xtype: 'textarea',
                                                fieldLabel: '基本案情',
                                                name: 'case_basic',
                                                width: 600,
                                                allowBlank: false,
                                                maxLength: 400,
                                                labelWidth: 80,
                                                labelAlign: 'right'
                                            }
                                        ]
                                    },
                                    {
                                        border: false,
                                        xtype: 'fieldcontainer',
                                        layout: "column",
                                        items: [
                                            {
                                                xtype: 'textarea',
                                                fieldLabel: '鉴定意见',
                                                name: 'appraisal_opinion',
                                                width: 600,
                                                allowBlank: false,
                                                maxLength: 400,
                                                labelWidth: 80,
                                                labelAlign: 'right'
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    },
            ]
        }];
        me.buttons = [{
            text: '保存',
            iconCls: 'Disk',
            handler: me.onSave
        }, {
            text: '取消',
            iconCls: 'Cancel',
            handler: me.onCancel
        }]
        me.callParent(arguments);
    },
    onSave: function () {
        var me = this.up("form");
        var form = me.getForm();
        if (form.isValid()) {
            form.submit({
                url: 'narcotics/register/addCaseInfo.do',
                method: 'post',
                waitMsg: '正在添加数据...',
                success: function (form, action) {
                    response = Ext.JSON
                        .decode(action.response.responseText);
                    if (response) {
                        Ext.MessageBox.alert("提示信息", "保存成功");
                        me.grid.getStore().load();
                        me.up("window").close();
                    } else {
                        Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                    }
                },
                failure: function () {
                    Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                }
            });
        }
    },
    onCancel: function () {
        var me = this;
        me.up("window").close();
    }
});
