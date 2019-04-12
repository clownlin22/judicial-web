var imgs="";
var img_show_index = 0;
var img_count = 0;
Ext
    .define(
    'Rds.trace.form.TraceVerifyForm',
    {
        extend : 'Ext.form.Panel',
        layout : "border",
        config:{
            operType:null,
            case_id:null
        },
        initComponent : function() {
            var me = this;
            me.items = [{
                xtype : 'panel',
                region : 'west',
                width : 750,
                autoScroll : true,
                items : [
                {
                    xtype: 'form',
                    width: 700,
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
                            // 该列在整行中所占的百分比
                            columnWidth: .45,
                            xtype: "textfield",
                            labelAlign: 'left',
                            labelWidth: 100,
                            fieldLabel: '案例序号',
                            allowBlank: false,//不允许为空
                            blankText: "不能为空",//错误提示信息，默认为This field is required!
                            name: 'case_no',
                            readOnly: true
                        },
                        {
                            // 该列在整行中所占的百分比
                            columnWidth: .45,
                            xtype: "textfield",
                            labelAlign: 'left',
                            labelWidth: 100,
                            fieldLabel: '委托单位',
                            allowBlank: false,//不允许为空
                            blankText: "不能为空",//错误提示信息，默认为This field is required!
                            maxLength: 200,
                            name: 'company_name',
                            readOnly: true
                        },
                        {
                            // 该列在整行中所占的百分比
                            columnWidth: .45,
                            xtype: "textfield",
                            labelAlign: 'left',
                            labelWidth: 100,
                            fieldLabel: '委托鉴定事项',
                            allowBlank: false,//不允许为空
                            blankText: "不能为空",//错误提示信息，默认为This field is required!
                            maxLength: 200,
                            name: 'case_type',
                            readOnly: true
                        },
                        {
                            xtype: 'hiddenfield',
                            name: 'case_id'
                        },
                        {
                            // 该列在整行中所占的百分比
                            columnWidth: .45,
                            xtype: "textfield",
                            labelAlign: 'left',
                            labelWidth: 100,
                            fieldLabel: '鉴定材料',
                            allowBlank: false,//不允许为空
                            blankText: "不能为空",//错误提示信息，默认为This field is required!
                            maxLength: 200,
                            name: 'case_attachment_desc',
                            readOnly: true
                        },
                        {
                            // 该列在整行中所占的百分比
                            columnWidth: .45,
                            xtype: "textfield",
                            labelAlign: 'left',
                            fieldLabel: '鉴定地点',
                            labelWidth: 100,
                            maxLength: 200,
                            name: 'case_local',
                            readOnly: true
                        },
                        {
                            // 该列在整行中所占的百分比
                            columnWidth: .45,
                            xtype: "textfield",
                            labelAlign: 'left',
                            fieldLabel: '鉴定要求',
                            labelWidth: 100,
                            maxLength: 200,
                            name: 'identification_requirements',
                            readOnly: true
                        },
                        {
                            itemId: 'opertype',
                            name: 'opertype',
                            xtype: "textfield",
                            value: me.operType,
                            hidden: true
                        },
                        {
                            xtype: 'textarea',
                            fieldLabel: '审核备注',
                            name: 'verify_baseinfo_remark',
                            itemId: 'verify_baseinfo_remark',
                            width: 500,
                            columnWidth: 0.9,
                            labelWidth: 100,
                            labelAlign: 'left'
                        }
                    ]
                }
            ]
        },{
                xtype: 'panel',
                region: 'center',
                id: 'trace_img_show_verify',
                autoScroll: true,
                items: [],
                buttons: [
                    {
                        text: '左转',
                        iconCls: 'Arrowturnleft',
                        handler: me.onTurnLeft
                    },
                    {
                        text: '右转',
                        iconCls: 'Arrowturnright',
                        handler: me.onTurnRight
                    },
                    {
                        text: '上一张',
                        iconCls: 'Arrowleft',
                        handler: me.onLast
                    },
                    {
                        text: '下一张',
                        iconCls: 'Arrowright',
                        handler: me.onNext
                    }
                ]
            }];

            me.buttons = [ {
                text : '审核通过',
                iconCls : 'Disk',
                handler : me.accept
            },{
                text : '审核不通过',
                iconCls : 'Cancel',
                handler : me.refuse
            },{
                text : '取消',
                iconCls : 'Arrowredo',
                handler : me.onCancel
            } ]
            me.callParent(arguments);
        },
        onTurnLeft : function() {
            if(imgs==""){
                Ext.Msg.alert("提示", "图片不存在!");
                return;
            }
            var path = imgs[img_show_index].attachment_path;
            Ext.Ajax.request({
                url : "trace/attachment/turnImg.do",
                method : "POST",
                headers : {
                    'Content-Type' : 'application/json'
                },
                jsonData : {
                    'path' : path,
                    'direction' : 'left'
                },
                success : function(response, options) {
                    response = Ext.JSON.decode(response.responseText);
                    if (response.success == true) {
                        Ext.getCmp('trace_img_show_verify').remove(Ext
                            .getCmp('trace_img_box_verify'));
                        Ext.getCmp('trace_img_show_verify').add({
                            xtype : 'box',
                            style : 'margin:6px;',
                            id : 'trace_img_box_verify',
                            autoEl : {
                                tag : 'img',
                                src : "trace/attachment/getImg.do?filename="
                                    + imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
                            }
                        });
                    } else {
                        Ext.Msg.alert("提示", "旋转失败，请重新旋转！");
                        return;
                    }
                },
                failure : function() {
                    Ext.Msg.alert("提示", "获取图片失败！");
                }
            });
        },
        onTurnRight : function() {
            if(imgs==""){
                Ext.Msg.alert("提示", "图片不存在!");
                return;
            }
            var path = imgs[img_show_index].attachment_path;
            Ext.Ajax.request({
                url : "trace/attachment/turnImg.do",
                method : "POST",
                headers : {
                    'Content-Type' : 'application/json'
                },
                jsonData : {
                    'path' : path,
                    'direction' : 'right'
                },
                success : function(response, options) {
                    response = Ext.JSON.decode(response.responseText);
                    if (response.success == true) {
                        Ext.getCmp('trace_img_show_verify').remove(Ext
                            .getCmp('trace_img_box_verify'));
                        Ext.getCmp('trace_img_show_verify').add({
                            xtype : 'box',
                            style : 'margin:6px;',
                            id : 'trace_img_box_verify',
                            autoEl : {
                                tag : 'img',
                                src : "trace/attachment/getImg.do?filename="
                                    + imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
                            }
                        });
                    } else {
                        Ext.Msg.alert("提示", "旋转失败，请重新旋转！");
                        return;
                    }
                },
                failure : function() {
                    Ext.Msg.alert("提示", "获取图片失败！");
                }
            });
        },
        onLast : function() {
            if(imgs==""){
                Ext.Msg.alert("提示", "图片不存在!");
                return;
            }
            var me = this.up("box");
            var box = Ext.getCmp('trace_img_box_verify');
            if (box == null) {
                Ext.Msg.alert("提示", "没有图片！");
                return;
            }
            if (img_show_index == 0) {
                Ext.Msg.alert("提示", "没有上一张了！");
                return;
            }
            Ext.getCmp('trace_img_show_verify').remove(Ext
                .getCmp('trace_img_box_verify'));

            img_show_index -= 1;
            Ext.getCmp('trace_img_show_verify').add({
                xtype : 'box',
                style : 'margin:6px;',
                id : 'trace_img_box_verify',
                autoEl : {
                    tag : 'img',
                    src : "trace/attachment/getImg.do?filename="
                        + imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
                }
            });
        },
        onNext : function() {
            if(imgs==""){
                Ext.Msg.alert("提示", "图片不存在!");
                return;
            }
            var me = this.up("box");
            var box = Ext.getCmp('trace_img_box_verify');
            if (box == null) {
                Ext.Msg.alert("提示", "没有图片！");
                return;
            }
            if (img_show_index + 1 == img_count) {
                Ext.Msg.alert("提示", "没有下一张了！");
                return;
            }
            Ext.getCmp('trace_img_show_verify').remove(Ext
                .getCmp('trace_img_box_verify'));
            img_show_index += 1;
            Ext.getCmp('trace_img_show_verify').add({
                xtype : 'box',
                style : 'margin:6px;',
                id : 'trace_img_box_verify',
                autoEl : {
                    tag : 'img',
                    src : "trace/attachment/getImg.do?filename="
                        + imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
                }
            });
            // alert("next");
        },
        accept : function() {
            var me = this.up("form");
            var form = me.getForm();
//            console.log(me.items.get(0).items.get(0).items.get(8));
//            console.log(me.items.get(0).items.get(0).items.get(8).getValue());
            var verify_baseinfo_remark = me.items.get(0).items.get(0).items.get(8).getValue();
            var values = {
                case_id:me.case_id,
                verify_baseinfo_state:1,
                verify_baseinfo_remark:verify_baseinfo_remark
            };

            if(form.isValid()){
                Ext.Ajax.request({
                    url:"trace/verify/insertVerify.do",
                    method: "POST",
                    headers: { 'Content-Type': 'application/json' },
                    jsonData: values,
                    success: function (response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response.result==true) {
                            Ext.MessageBox.alert("提示信息", "审核成功");
                            me.grid.getStore().load();
                            me.up("window").close();
                        }else {
                            Ext.MessageBox.alert("错误信息", "审核失败");
                        }
                    },
                    failure: function () {
                        Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                    }
                });
            }
        },

        refuse : function() {
            var me = this.up("form");
            var form = me.getForm();
//            console.log(me.items.get(0).items.get(0).items.get(8));
//            console.log(me.items.get(0).items.get(0).items.get(8).getValue());
            var verify_baseinfo_remark = me.items.get(0).items.get(0).items.get(8).getValue();
            var values = {
                case_id:me.case_id,
                verify_baseinfo_state:2,
                verify_baseinfo_remark:verify_baseinfo_remark
            };

            if(form.isValid()){
                Ext.Ajax.request({
                    url:"trace/verify/insertVerify.do",
                    method: "POST",
                    headers: { 'Content-Type': 'application/json' },
                    jsonData: values,
                    success: function (response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response.result==true) {
                            Ext.MessageBox.alert("提示信息", "审核成功");
                            me.grid.getStore().load();
                            me.up("window").close();
                        }else {
                            Ext.MessageBox.alert("错误信息", "审核失败");
                        }
                    },
                    failure: function () {
                        Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                    }
                });
            }
        },

        onCancel : function() {
            var me = this;
            me.up("window").close();
        },
        listeners : {
            'afterrender' : function() {
                var me = this;
                var values = me.getValues();
                var case_id = me.case_id;
                Ext.Ajax.request({
                    url : 'trace/vehicle/queryVehicle.do',
                    method : "POST",
                    headers : {
                        'Content-Type' : 'application/json'
                    },
                    jsonData : {
                        'case_id' : case_id
                    },
                    success : function(response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        var data = response["data"];
                        for (var i = 0; i < data.length; i++) {
                            me.down("form").add({
                                xtype : 'form',
                                style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
                                bodyPadding : 10,
                                defaults : {
                                    anchor : '100%',
                                    labelWidth : 80,
                                    labelSeparator: "：",
                                    labelAlign: 'right'
                                },
                                border : false,
                                autoHeight : true,
                                items:[{
                                    layout:'auto',
                                    xtype:'panel',
                                    border:false,
                                    items:[{
                                        layout:'column',
                                        xtype:'panel',
                                        border:false,
                                        items:[{
                                            columnWidth : .28,
                                            xtype : "textfield",
                                            fieldLabel : '车牌号',
                                            allowBlank  : false,//不允许为空
                                            blankText   : "不能为空",//错误提示信息，默认为This field is required!
                                            labelAlign: 'right',
                                            maxLength :50,
                                            value:data[i].plate_number,
                                            labelWidth : 80,
                                            name : 'plate_number',
                                            readOnly:true
                                        }, {
                                            columnWidth : .28,
                                            xtype : "textfield",
                                            fieldLabel : '车架号',
                                            labelAlign: 'right',
                                            maxLength :50,
                                            allowBlank  : false,//不允许为空
                                            blankText   : "不能为空",//错误提示信息，默认为This field is required!
                                            labelWidth : 80,
                                            value:data[i].vehicle_identification_number,
                                            name : 'vehicle_identification_number',
                                            readOnly:true
                                        }, {
                                            columnWidth : .28,
                                            xtype : "textfield",
                                            fieldLabel : '发动机号',
                                            labelAlign: 'right',
                                            maxLength :18,
                                            labelWidth : 80,
                                            value:data[i].engine_number,
                                            name : 'engine_number',
                                            readOnly:true
                                        }]
                                    },{
                                        layout:'column',
                                        xtype:'panel',
                                        border:false,
                                        items:[{
                                            width : 192,
                                            xtype : "textfield",
                                            fieldLabel : '车辆类型',
                                            labelAlign: 'right',
                                            maxLength :18,
                                            labelWidth : 80,
                                            value:data[i].vehicle_type,
                                            name : 'vehicle_type',
                                            readOnly:true
                                        }]
                                    }]
                                }]
                            });
                        }
                    },
                    failure : function() {
                        return;
                    }
                });

                Ext.Ajax.request({
                    url : 'trace/person/queryPerson.do',
                    method : "POST",
                    headers : {
                        'Content-Type' : 'application/json'
                    },
                    jsonData : {
                        'case_id' : case_id
                    },
                    success : function(response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        var data = response["data"];
                        for (var i = 0; i < data.length; i++) {
                            me.down("form").add({
                                xtype : 'form',
                                style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
                                bodyPadding : 10,
                                defaults : {
                                    anchor : '100%',
                                    labelWidth : 80,
                                    labelSeparator: "：",
                                    labelAlign: 'right'
                                },
                                border : false,
                                autoHeight : true,
                                items:[{
                                    layout:'auto',
                                    xtype:'panel',
                                    border:false,
                                    items:[{
                                        layout:'column',
                                        xtype:'panel',
                                        border:false,
                                        items:[{
                                            columnWidth : .28,
                                            xtype : "textfield",
                                            fieldLabel : '姓名',
                                            allowBlank  : false,//不允许为空
                                            blankText   : "不能为空",//错误提示信息，默认为This field is required!
                                            labelAlign: 'right',
                                            maxLength :50,
                                            value:data[i].person_name,
                                            labelWidth : 80,
                                            name : 'person_name',
                                            readOnly:true
                                        }, {
                                            columnWidth : .28,
                                            xtype : "textfield",
                                            fieldLabel : '身份证号',
                                            labelAlign: 'right',
                                            maxLength :50,
                                            labelWidth : 80,
                                            value:data[i].id_number,
                                            name : 'id_number',
                                            readOnly:true
                                        }, {
                                            columnWidth : .28,
                                            xtype : "textfield",
                                            fieldLabel : '家庭住址',
                                            labelAlign: 'right',
                                            maxLength :18,
                                            labelWidth : 80,
                                            value:data[i].address,
                                            name : 'address',
                                            readOnly:true
                                        }]
                                    }]
                                }]
                            });
                        }
                    },
                    failure : function() {
                        return;
                    }
                });

                Ext.Ajax.request({
                    url : "trace/attachment/getAttachMent.do",
                    method : "POST",
                    headers : {
                        'Content-Type' : 'application/json'
                    },
                    jsonData : {
                        'case_id' : case_id
                    },
                    success : function(response, options) {
                        var data = Ext.JSON.decode(response.responseText);
                        if(data.length>0){
                            imgs = data;
                            img_count = data.length;
                            Ext.getCmp('trace_img_show_verify').add({
                                xtype : 'box',
                                style : 'margin:6px;',
                                id : 'trace_img_box_verify',
                                autoEl : {
                                    tag : 'img',
                                    src : "trace/attachment/getImg.do?filename="
                                        + imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
                                }
                            });
                        }
                    },
                    failure : function() {
                        Ext.Msg.alert("提示", "获取图片失败<br>请联系管理员!");
                    }
                });

            }
        }
    });
