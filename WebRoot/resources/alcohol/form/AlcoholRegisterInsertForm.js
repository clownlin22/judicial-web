var identification_per = Ext.create('Ext.data.Store', {
    model : 'model',
    proxy : {
        type : 'jsonajax',
        actionMethods : {
            read : 'POST'
        },
        url : 'alcohol/register/getIdentificationPer.do',
        reader : {
            type : 'json',
            root : 'data'
        }
    },
    autoLoad : true,
    remoteSort : true
});
Ext.define('Rds.alcohol.form.AlcoholRegisterInsertForm', {
    extend: 'Ext.form.Panel',
    initComponent: function () {
        var me = this;
        var storeModel = Ext.create('Ext.data.Store', {
            model: 'model',
            proxy: {
                type: 'jsonajax',
                actionMethods: {
                    read: 'POST'
                },
                url: 'judicial/dicvalues/getReportModels.do',
                params: {
                    type: 'alcohol',
                    receiver_id: ""
                },
                reader: {
                    type: 'json',
                    root: 'data'
                }
            },
            autoLoad: true,
            remoteSort: true
        });
        var clientStore = Ext.create('Ext.data.Store', {
            model: 'model',
            proxy: {
                type: 'jsonajax',
                actionMethods: {
                    read: 'POST'
                },
                url: 'alcohol/register/getClient.do',
                params: {},
                reader: {
                    type: 'json',
                    root: 'data'
                }
            },
            autoLoad: true,
            remoteSort: true
        });
        var clientStore2 = Ext.create('Ext.data.Store', {
            model: 'model',
            proxy: {
                type: 'jsonajax',
                actionMethods: {
                    read: 'POST'
                },
                url: 'alcohol/register/getClient2.do',
                params: {},
                reader: {
                    type: 'json',
                    root: 'data'
                }
            },
            autoLoad: true,
            remoteSort: true
        });
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
                    items: [{
                        // 该列在整行中所占的百分比
                        columnWidth: .5,
                        xtype: "textfield",
                        labelAlign: 'right',
                        labelWidth: 80,
                        fieldLabel: '案例条形码<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
                        allowBlank: false,// 不允许为空
                        blankText: "不能为空",// 错误提示信息，默认为This field is required!
                        maxLength: 49,
                        regex: /^[A-Za-z0-9]+$/,
                        regexText: "只能输入数字和字母",
                        name: 'case_code',
                        value: 'D' + Ext.Date.format(new Date(), 'Ym'),
                        validator: function (value) {
                            var result = false;
                            Ext.Ajax.request({
                                url: "alcohol/register/exsitCaseCode.do",
                                method: "POST",
                                async: false,
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                jsonData: {
                                    case_code: value
                                },
                                success: function (response, options) {
                                    response = Ext.JSON
                                        .decode(response.responseText);
                                    if (!response) {
                                        result = "此条形码已存在";
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
                    }, {
                        xtype: 'datefield',
                        name: 'close_time',
                        columnWidth: .5,
                        labelWidth: 80,
                        fieldLabel: '打印日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
                        labelAlign: 'right',
                        format: 'Y-m-d',
                        allowBlank: false,// 不允许为空
//					maxValue : new Date(),
                        value: Ext.Date.add(new Date(), Ext.Date.DAY)

                    }]
                },
                {
                    layout: "column",// 从左往右的布局
                    xtype: 'fieldcontainer',
                    border: false,
                    items: [{
                        xtype: 'datefield',
                        name: 'client_time',
                        columnWidth: .5,
                        labelWidth: 80,
                        fieldLabel: '委托日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
                        labelAlign: 'right',
                        format: 'Y-m-d',
                        allowBlank: false,// 不允许为空
                        maxValue: new Date(),
                        value: Ext.Date.add(new Date(), Ext.Date.DAY)

                    }, {
                        xtype: 'datefield',
                        name: 'accept_time',
                        columnWidth: .5,
                        labelWidth: 80,
                        fieldLabel: '受理日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
                        labelAlign: 'right',
                        format: 'Y-m-d',
                        allowBlank: false,// 不允许为空
                        maxValue: new Date(),
                        value: Ext.Date.add(new Date(), Ext.Date.DAY)

                    }]
                },
                {
                    layout: "column",// 从左往右的布局
                    xtype: 'fieldcontainer',
                    border: false,
                    items: [
                        // {
                        // // 该列在整行中所占的百分比
                        // columnWidth : .5,
                        // xtype : "textfield",
                        // labelAlign : 'right',
                        // fieldLabel : '委托人',
                        // allowBlank : false,// 不允许为空
                        // labelWidth : 80,
                        // maxLength : 50,
                        // name : 'client'
                        //
                        // },
                        {
                            columnWidth: .5,
                            xtype: "combo",
                            fieldLabel: '委托人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
                            triggerAction: 'all',
                            queryMode: 'local',
                            labelWidth: 80,
                            editable: true,
                            labelAlign: 'right',
                            allowBlank: false,// 不允许为空
                            valueField: "key",
                            displayField: "key",
                            name: 'client',
                            maxLength: 20,
                            store: clientStore
                        },
                        {
                            xtype: 'combo',
                            columnWidth: .5,
                            fieldLabel: '案例所属地',
                            labelWidth: 80,
                            labelAlign: 'right',
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
                            typeAhead: false,
                            hideTrigger: true,
                            forceSelection: true,
                            minChars: 2,
                            matchFieldWidth: true,
                            listConfig: {
                                loadingText: '正在查找...',
                                emptyText: '没有找到匹配的数据'
                            }
                        }

                    ]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [{
                        // 该列在整行中所占的百分比
                        columnWidth: .5,
                        xtype: "textfield",
                        labelAlign: 'right',
                        labelWidth: 80,
                        fieldLabel: '送检人',
//							maxLength : 50,
                        name: 'checkper',
                        validator: function (value) {
                            var len = value.replace(/[^\x00-\xff]/g, "xx").length;
                            if (len > 40) {
                                return "长度不能超过40个字符或20个汉字";
                            }
                            return true;
                        }

                    }, {
                        // 该列在整行中所占的百分比
                        columnWidth: .5,
                        xtype: "textfield",
                        labelAlign: 'right',
                        labelWidth: 80,
                        fieldLabel: '送检人电话',
                        regex: /(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,11}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/,
                        name: 'checkper_phone',
                        regexText: "电话格式有误"
                    }]
                },
                {
                    layout: "column",// 从左往右的布局
                    xtype: 'fieldcontainer',
                    border: false,
                    items: [
                        {
                            xtype: 'combo',
                            fieldLabel: '案例归属<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
                            labelWidth: 80,
                            columnWidth: 1,
                            labelAlign: 'right',
                            name: 'receiver_id',
                            emptyText: '(姓名/地区首字母)：如小红(xh)',
                            store: Ext.create("Ext.data.Store",
                                {
                                    fields: [
                                        {name: 'id', mapping: 'id', type: 'string'},
                                        {name: 'ascription', mapping: 'ascription', type: 'string'}
                                    ],
                                    pageSize: 10,
                                    autoLoad: false,
                                    proxy: {
                                        type: "ajax",
                                        url: "finance/chargeStandard/getAscriptionPer.do?id=" + ownpersonTemp,
                                        reader: {
                                            type: "json"
                                        }
                                    }
                                }),
                            displayField: 'ascription',
                            valueField: 'id',
                            typeAhead: false,
                            hideTrigger: true,
                            forceSelection: true,
                            minChars: 2,
                            allowBlank: false, //不允许为空
                            matchFieldWidth: true,
                            listConfig: {
                                loadingText: '正在查找...',
                                emptyText: '没有找到匹配的数据'
                            },
                            listeners: {
                                'afterrender': function () {
                                    if ("" != ownpersonTemp) {
                                        this.store.load();
                                    }
                                }
                            }
                        }]
                },
                {
                    layout: "column",// 从左往右的布局
                    xtype: 'fieldcontainer',
                    border: false,
                    items: [
                        {
                            xtype: "combo",
                            columnWidth: 1,
                            fieldLabel: '模板类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
                            mode: 'local',
                            labelWidth: 80,
                            editable: false,
                            labelAlign: 'right',
                            blankText: '请选择模板',
                            emptyText: '请选择模板',
                            allowBlank: false,// 不允许为空
                            blankText: "不能为空",// 错误提示信息，默认为This field is required!
                            valueField: "key",
                            displayField: "value",
//					id : "report_model_add_alcohol",
                            name: 'report_model',
                            store: storeModel
                        }]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [{
                        // 该列在整行中所占的百分比
                        columnWidth: .5,
                        xtype: "textfield",
                        labelAlign: 'right',
                        labelWidth: 80,
                        fieldLabel: '邮寄地址',
                        name: 'mail_address',
                        validator: function (value) {
                            var len = value.replace(/[^\x00-\xff]/g, "xx").length;
                            if (len > 50) {
                                return "长度不能超过50个字符或25个汉字";
                            }
                            return true;
                        }

                    }, {
                        // 该列在整行中所占的百分比
                        columnWidth: .5,
                        xtype: "textfield",
                        labelAlign: 'right',
                        labelWidth: 80,
                        fieldLabel: '邮件接收人',
                        name: 'mail_per',
                        validator: function (value) {
                            var len = value.replace(/[^\x00-\xff]/g, "xx").length;
                            if (len > 40) {
                                return "长度不能超过40个字符或20个汉字";
                            }
                            return true;
                        }
                    }]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [


                        {
                            // 该列在整行中所占的百分比
//							columnWidth : .5,
                            xtype: "textfield",
                            labelAlign: 'right',
                            labelWidth: 80,
                            fieldLabel: '联系电话',
                            regex: /(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,11}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/,
                            name: 'mail_phone',
                            regexText: "电话格式有误"
                        }]
                },
                {
                    xtype: 'fieldset',
                    title: '案例照片附件',
                    id: 'testFieldset',
                    items: [{
                                style: 'margin-left:25px;',
                                layout: "column",// 从左往右的布局
                                xtype: 'fieldcontainer',
                                border: false,
                                items: [
                                    {
                                        xtype: 'filefield',
                                        name: 'files',
                                        fieldLabel: '附件<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
                                        msgTarget: 'side',
                                        allowBlank: false,
                                        labelWidth: 40,
                                        buttonText: '选择附件',
                                        columnWidth: .5,
                                        validator: function (v) {
                                            if (!v.endWith(".jpg") && !v.endWith(".JPG")
                                                && !v.endWith(".png") && !v.endWith(".PNG")
                                                && !v.endWith(".gif") && !v.endWith(".GIF")
                                                && !v.endWith(".jpeg") && !v.endWith(".JPEG")) {
                                                return "请选择.jpg .png .gif.jpeg类型的图片";
                                            }
                                            return true;
                                        }
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'att_type',
                                        fieldLabel: '描述<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
                                        labelWidth: 40,
                                        anchor: '100%',
                                        allowBlank: false,
                                        columnWidth: .5,
                                        labelAlign: 'right',
                                        maxLength: 40,
                                    }
                                ]
                            }, {
                                xtype: 'panel',
                                layout: 'absolute',
                                border: false,
                                items: [{
                                    xtype: 'button',
                                    text: '增加照片',
                                    width: 100,
                                    style: 'margin-bottom:10px',
                                    x: 10,
                                    handler: function () {
                                        var me = Ext.getCmp("testFieldset");
                                        me.add({
                                            xtype: 'form',
                                            style: 'margin-top:5px;',
                                            layout: 'column',
                                            border: false,
                                            items: [
                                                {
                                                    xtype: 'filefield',
                                                    name: 'files',
                                                    columnWidth: .5,
                                                    fieldLabel: '附件<span style="color:red">*</span>',
                                                    labelWidth: 40,
                                                    msgTarget: 'side',
                                                    allowBlank: false,
                                                    anchor: '100%',
                                                    style: 'margin-bottom:10px;margin-left:25px;',
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
                                                },
                                                {
                                                    xtype: 'button',
                                                    style: 'margin-left:15px;',
                                                    text: '删除',
                                                    handler: function () {
                                                        var me = this.up("form");
                                                        console.log(me);
                                                        me.disable(true);
                                                        me.up("fieldset").remove(me);
                                                    }
                                                },
                                                {
                                                    xtype: 'textfield',
                                                    name: 'att_type',
                                                    fieldLabel: '描述<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
                                                    labelWidth: 40,
                                                    anchor: '100%',
                                                    columnWidth: .5,
                                                    labelAlign: 'right',
                                                    maxLength: 40,
                                                }
                                            ]
                                        });
                                    }
                                }]
                                }
                    ]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [{
                        xtype: 'textarea',
                        fieldLabel: '备注',
                        name: 'remark',
                        columnWidth: 1,
                        maxLength: 400,
                        labelWidth: 80,
                        labelAlign: 'right'
                    }]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [{
                        // 该列在整行中所占的百分比
                        columnWidth: .5,
                        xtype: "textfield",
                        labelAlign: 'right',
                        labelWidth: 80,
                        allowBlank: false,// 不允许为空
                        fieldLabel: '被检验人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
                        maxLength: 20,
                        name: 'sample_name',
                        regex: /^[\u4E00-\u9FA5]+$/,
                        regexText: '只能输入汉字'
                    }, {
                        columnWidth: .5,
                        xtype: "textfield",
                        labelAlign: 'right',
                        labelWidth: 80,
                        fieldLabel: '身份证号',
                        maxLength: 18,
                        name: 'id_number',
                        validator: function (value) {
                            var result = false;
                            if (value != null && value != "") {
                                Ext.Ajax.request({
                                    url: "judicial/register/verifyId_Number.do",
                                    method: "POST",
                                    async: false,
                                    headers: {
                                        'Content-Type': 'application/json'
                                    },
                                    jsonData: {
                                        id_number: value
                                    },
                                    success: function (response,
                                                       options) {
                                        response = Ext.JSON
                                            .decode(response.responseText);
                                        if (!response) {
                                            result = "身份证号码不正确";
                                        } else {
                                            result = true;
                                        }
                                    },
                                    failure: function () {
                                        Ext.Msg.alert("提示",
                                            "网络故障<br>请联系管理员!");
                                    }
                                });
                            } else {
                                result = true;
                            }
                            return result;
                        }
                    }]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    hidden: true,
                    layout: "column",
                    items: [{
                        // 该列在整行中所占的百分比
                        columnWidth: .5,
                        xtype: 'numberfield',
                        labelAlign: 'right',
                        labelWidth: 80,
                        // allowBlank : false,// 不允许为空
                        fieldLabel: '采样剂量(mL)',
                        maxLength: 5,
                        allowDecimals: false,
                        name: 'sample_ml',
                        value: 0,
                        listeners: {
                            change: function (field, value) {
                                // if (value < 0) {
                                // field.setValue(0);
                                // }
                            }
                        }
                    }]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [

                        new Ext.form.field.ComboBox({
                            fieldLabel: '血管类型',
                            columnWidth: .5,
                            labelWidth: 80,
                            editable: false,
                            triggerAction: 'all',
                            displayField: 'Name',
                            labelAlign: 'right',
                            valueField: 'Code',
                            store: new Ext.data.ArrayStore({
                                fields: ['Name', 'Code'],
                                data: [['真空采血管', 1], ['促凝管', 0]]
                            }),
                            value: 1,
                            mode: 'local',
                            // typeAhead: true,
                            name: 'isDoubleTube'
                        })
                        ,

                        new Ext.form.field.ComboBox({
                            fieldLabel: '是否检出',
                            columnWidth: .5,
                            id: 'alcohol_is_detection',
                            labelWidth: 80,
                            editable: false,
                            triggerAction: 'all',
                            displayField: 'Name',
                            labelAlign: 'right',
                            valueField: 'Code',
                            store: new Ext.data.ArrayStore({
                                fields: ['Name', 'Code'],
                                data: [['是', 1], ['否', 0]]
                            }),
                            value: 0,
                            mode: 'local',
                            hidden: true,
                            // typeAhead: true,
                            name: 'is_detection',
                            listeners: {
                                'change': function (value, newValue,
                                                    oldValue) {
                                    switch (newValue) {
                                        case 0 :
                                            Ext.getCmp("alcohol_sample_remark2").setValue("未出现乙醇的特征色谱峰。");
                                            Ext.getCmp("alcohol_sample_remark2").setReadOnly(true);
                                            Ext.getCmp("alcohol_sample_result").setValue("血液中未检出乙醇成分。");
                                            Ext.getCmp("alcohol_sample_result").setReadOnly(true);
                                            break;
                                        case 1 :
                                            Ext.getCmp("alcohol_sample_remark2").setValue("血液中乙醇含量为mg/mL。");
                                            Ext.getCmp("alcohol_sample_remark2").setReadOnly(false);
                                            Ext.getCmp("alcohol_sample_result").setValue("血液中乙醇含量为mg/mL。");
                                            Ext.getCmp("alcohol_sample_result").setReadOnly(false);
                                            break;
                                    }
                                }
                            }
                        })
                        ,
                        // {
                        //     columnWidth: .5,
                        //     xtype: "combo",
                        //     fieldLabel: '鉴定人(可多选)<span style="color:red">*</span>',
                        //     valueField: "key",
                        //     displayField: "key",
                        //     labelWidth: 100,
                        //     forceSelection: true,
                        //     allowBlank: false,//不允许为空
                        //     blankText: "不能为空",//错误提示信息，默认为This field is required!
                        //     emptyText: '请选择鉴定人',
                        //     name: 'case_checkper',
                        //     editable: false,
                        //     multiSelect: true,
                        //     store: clientStore2
                        // }
                        {
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
                            name: 'case_checkper',
                            editable: false,
                            multiSelect: true,
                            store: identification_per
                        }
//						         ,{
//											columnWidth : .5,
//											xtype : "combo",
//											fieldLabel : '鉴定人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
//											triggerAction : 'all',
//											queryMode : 'local',
//											labelWidth : 80,
//											editable : true,
//											labelAlign : 'right',
//											allowBlank : false,// 不允许为空
//											valueField : "key",
//											displayField : "key",
//											name : 'case_checkper',
//											store : clientStore2
//						         }
                    ]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [{
                        // 该列在整行中所占的百分比
                        columnWidth: .5,
                        id: 'bloodnumAid',
                        xtype: "textfield",
                        labelAlign: 'right',
                        labelWidth: 80,
                        fieldLabel: '血液编号',
                        maxLength: 20,
                        name: 'bloodnumA'

                    }, {
                        // 该列在整行中所占的百分比
                        columnWidth: .5,
                        xtype: "textfield",
                        labelAlign: 'right',
                        labelWidth: 80,
                        fieldLabel: '血液容量',
                        regex: /^\d+(\.\d{1,4})?$/,
                        maxLength: 20,
                        name: 'bloodnumB',
                        validator: function (value) {
                            if ("" != Ext.getCmp("bloodnumAid").getValue().trim()) {
                                return true;
                            } else if ("" == Ext.getCmp("bloodnumAid").getValue().trim() && "" == value.trim()) {
                                return true;
                            } else {
                                return "血液编号需先填写，谢谢！";
                            }
                        }

                    }]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [{
                        xtype: 'textfield',
                        fieldLabel: '案例简介',
                        name: 'case_intr',
                        columnWidth: 1,
                        labelWidth: 80,
                        labelAlign: 'right',
                        validator: function (value) {
                            var len = value.replace(/[^\x00-\xff]/g, "xx").length;
                            if (len > 100) {
                                return "长度不能超过100个字符或50个汉字";
                            }
                            return true;
                        }
                    }]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [{
                        xtype: 'textarea',
                        fieldLabel: '案例详情',
                        name: 'case_det',
                        columnWidth: 1,
                        maxLength: 500,
                        labelWidth: 80,
                        labelAlign: 'right',
                        validator: function (value) {
                            var len = value.replace(/[^\x00-\xff]/g, "xx").length;
                            if (len > 300) {
                                return "长度不能超过300个字符或150个汉字";
                            }
                            return true;
                        }
                    }]
                },
                {

                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    style: 'margin-left:0px;margin-right:0px;margin-top:0px;margin-bottom:0px;',
                    items: [new Ext.form.field.ComboBox({
                        fieldLabel: '重新鉴定',
                        columnWidth: .5,
                        labelWidth: 80,
                        editable: false,
                        triggerAction: 'all',
                        displayField: 'Name',
                        labelAlign: 'right',
                        valueField: 'Code',
                        store: new Ext.data.ArrayStore({
                            fields: ['Name', 'Code'],
                            data: [['否', 0], ['是', 1]]
                        }),
                        value: 0,
                        mode: 'local',
                        // typeAhead: true,
                        name: 'is_check',
                        id: 'is_check'
                    })]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    style: 'margin-left:0px;margin-right:0px;margin-top:0px;margin-bottom:0px;',
                    items: [{
                        xtype: 'textarea',
                        fieldLabel: '样本描述',
                        name: 'sample_remark',
                        allowBlank: false,
                        hidden: true,
                        width: 500,
                        maxLength: 500,
                        columnWidth: 0.9,
                        labelWidth: 80,
                        labelAlign: 'right',
                        value: '无凝块，包装无渗漏。'
                    }]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [{
                        xtype: 'textarea',
                        id: 'alcohol_sample_remark2',
                        fieldLabel: '检验描述',
                        name: 'sample_remark2',
                        readOnly: true,
                        allowBlank: false,
                        hidden: true,
                        width: 500,
                        maxLength: 500,
                        columnWidth: 0.9,
                        labelWidth: 80,
                        labelAlign: 'right',
                        value: '未出现乙醇的特征色谱峰。',
                        listeners: {
                            'change': function (value, newValue, oldValue) {
                                if (Ext.getCmp("alcohol_is_detection").getValue() == 1) {
                                    Ext.getCmp("alcohol_sample_result").setValue(newValue);
                                }
                            }
                        }

                    }]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    style: 'margin-left:0px;margin-right:0px;margin-top:0px;margin-bottom:0px;',
                    items: [{
                        columnWidth: 0.9,
                        xtype: 'textarea',
                        id: 'alcohol_sample_result',
                        fieldLabel: '检验结果',
                        name: 'sample_result',
                        readOnly: true,
                        hidden: true,
                        allowBlank: false,
                        labelWidth: 80,
                        labelAlign: 'right',
                        value: '血液中未检出乙醇成分。',
                        listeners: {
                            'change': function (value, newValue, oldValue) {
                                if (Ext.getCmp("alcohol_is_detection").getValue() == 1) {
                                    Ext.getCmp("alcohol_sample_remark2").setValue(newValue);
                                }
                            }
                        }
                    }]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [
                        {
                            // 该列在整行中所占的百分比
                            columnWidth: .5,
                            xtype: "numberfield",
                            value: 0,
                            maxLength: 7,
                            labelAlign: 'right',
                            labelWidth: 80,
                            allowBlank: false,// 不允许为空
                            fieldLabel: '标准金额',
                            maxLength: 50,
                            name: 'stand_sum',
                            value: 350.0
                        }, {
                            // 该列在整行中所占的百分比
                            columnWidth: .5,
                            xtype: "numberfield",
                            value: 0,
                            maxLength: 7,
                            labelAlign: 'right',
                            labelWidth: 80,
                            allowBlank: false,// 不允许为空
                            fieldLabel: '实收金额',
                            maxLength: 50,
                            name: 'real_sum',
                            value: 350
//			 listeners : {
//			 change : function(field, value) {
//			 if (value < 0) {
//			 field.setValue(0);
//			 }
//			 }
//			 }
                        }]
                },
                {
                    border: false,
                    xtype: 'fieldcontainer',
                    layout: "column",
                    items: [{
                        // 该列在整行中所占的百分比
                        columnWidth: .5,
                        xtype: "numberfield",
                        value: 0,
                        hidden: true,
                        maxLength: 7,
                        labelAlign: 'right',
                        labelWidth: 80,
                        allowBlank: false,// 不允许为空
                        fieldLabel: '回款金额',
                        maxLength: 50,
                        name: 'return_sum',
//			 listeners : {
//			 change : function(field, value) {
//			 if (value < 0) {
//			 field.setValue(0);
//			 }
//			 }
//			 }
                    }]
                }
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
        console.log(form)
        if (form.isValid()) {
            form.submit({
                url: 'alcohol/register/addCaseInfo.do',
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
//								me.up("window").close();
                    }
                },
                failure: function () {
                    Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
//							me.up("window").close();
                }
            });
        }
    },
    onCancel: function () {
        var me = this;
        me.up("window").close();
    }
});
