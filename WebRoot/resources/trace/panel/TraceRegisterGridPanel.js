/**
 * 案例登记列表
 *
 * @author chenwei
 */
Ext.define(
    "Rds.trace.panel.TraceRegisterGridPanel",
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
            var invoice_number = Ext.create('Ext.form.field.Text',{
    			name:'invoice_number',
    			id:'invoice_number',
    			labelWidth:70,
    			width:'20%',
    			fieldLabel:'发票编号'
    		});
            me.store = Ext.create('Ext.data.Store',
                {   fields : [ 'case_id', 'case_no',
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
                        'identification_date',
                        'verify_baseinfo_state',
                        'dely_reson',
                        'stand_sum','real_sum','return_sum','finance_status','invoice_number'
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
                                    is_delete:me.getDockedItems('toolbar')[0].
                                    getComponent('is_delete').getValue(),
                                    case_no:me.getDockedItems('toolbar')[0].
                                    getComponent('case_no').getValue(),
                                    status:me.getDockedItems('toolbar')[0].
                                    getComponent('status').getValue(),
                                    invoice_number:invoice_number.getValue().trim()
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
                        var dely = record.data["dely_reson"];
                        if (isnull == 1) {
                            return "<div style=\"text-decoration: line-through;color: red;\">"
                                + value + "</div>"
                        } else {
                         	if(dely != '' && dely != null)
                         		return value+"<span style=\"color: blue;\">(延期)</span>";
                         	else
                         		return value;
                        }
                    }
                },
                {
                    text : '委托单位',
                    dataIndex : 'company_name',
                    width : 150,
                    menuDisabled : true
                },
                {
                    text : '受理日期',
                    dataIndex : 'receive_time',
                    menuDisabled : true,
                    width : 100
                },
                {
                    text : '鉴定日期',
                    dataIndex : 'identification_date',
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
                    width : 200
                },
                {
                    text : '鉴定材料',
                    dataIndex : 'case_attachment_desc',
                    menuDisabled : true,
                    width : 200
                },{
                    text: '案例状态',dataIndex: 'status',width : 150,
                    renderer:function(value){
                    switch(value)
                        {
                            case -2:
                                return "<div style=\"color: red;\">"
                                    + "基本信息审核不通过" + "</div>"
                                break;
                            case 0:
                                return "基本信息未审核";
                                break;
                            case 1:
                                return "基本信息已审核";
                                break;
                            case 2:
                                return "报告已生成";
                                break;
                            case 3:
                                return "<div style=\"color: red;\">"
                                    + "审核报告不通过" + "</div>";
                                break;
                            case 4:
                                return "审核报告通过";
                                break;
                            case 5:
                                return "已打印";
                                break;
                        }
                    }
                },{
                    text : '归属人',
                    dataIndex : 'username',
                    menuDisabled : true,
                    width : 100
                },{
                    text : '案例归属地',
                    dataIndex : 'areaname',
                    menuDisabled : true,
                    width : 250
                },{
                    text : '发票编号',
                    dataIndex : 'invoice_number',
                    menuDisabled : true,
                    width : 150
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
                            width : '20%',
                            fieldLabel : '案例序号'
                        },
                        new Ext.form.field.ComboBox(
                            {
                                fieldLabel : '是否废除',
                                width : '20%',
                                labelWidth : 60,
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
                                                '未废除',
                                                0 ],
                                            [
                                                '已废除',
                                                1 ] ]
                                    }),
                                value : -1,
                                mode : 'local',
                                // typeAhead: true,
                                name : 'is_delete',
                                itemId : 'is_delete'
                            }),
                        new Ext.form.field.ComboBox(
                            {
                                fieldLabel : '案例状态',
                                width : '20%',
                                labelWidth : 60,
                                editable : false,
                                triggerAction : 'all',
                                displayField : 'Name',
                                labelAlign : 'right',
                                valueField : 'Code',
                                //0未上传报告 1已上传报告 2审核报告通过 3审核报告不通过
                                //4已打印（可寄送） 5（已寄送）6（已归档）
                                store : new Ext.data.ArrayStore(
                                    {
                                        fields : [
                                            'Name',
                                            'Code' ],
                                        data : [
                                            ['基本信息审核不通过',-2 ],
                                            ['全部',-1 ],
                                            ['基本信息未审核',0],
                                            ['基本信息已审核',1 ],
                                            ['报告已生成',2 ],
                                            ['审核报告不通过',3 ],
                                            ['审核报告通过',4 ]//,
//                                            ['已寄送',5 ],
//                                            ['已归档',6 ]
                                        ]
                                    }),
                                value : -1,
                                mode : 'local',
                                // typeAhead: true,
                                name : 'status',
                                itemId : 'status'
                            }),
                        {
                            xtype : 'datefield',
                            name : 'start_time',
                            itemId : 'start_time',
                            width : '20%',
                            fieldLabel : '受理时间 从',
                            labelWidth : 70,
                            labelAlign : 'right',
                            emptyText : '请选择日期',
                            format : 'Y-m-d',
                            maxValue : new Date(),
                			value : Ext.Date.add(
                					new Date(),
                					Ext.Date.DAY,-30),
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
                            width : '20%',
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
                        }]
                }, {
        	 		style : {
        	        borderTopWidth : '0px !important',
        	        borderBottomWidth : '0px !important'
        	 		},
        	 		xtype:'toolbar',
        	 		name:'search',
        	 		dock:'top',
        	 		items:[invoice_number, {
                        text : '查询',
                        iconCls : 'Find',
                        handler : me.onSearch
                    } ]
        	 	
                },{
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
                    }, {
                        text : '新增',
                        iconCls : 'Pageadd',
                        handler : me.onInsert
                    }, {
                        text : '修改',
                        iconCls : 'Pageedit',
                        handler : me.onUpdate
                    }, {
                        text : '废除',
                        iconCls : 'Delete',
                        handler : me.onDelete
                    },{
                        text : '报告编辑',
                        iconCls : 'Printer',
                        handler : me.onCasePrint,
                        hidden:true
                    },{
                        text : '导出',
                        iconCls : 'Pageexcel',
                        handler : me.onExport
                    },{
                        text : '延期邮寄',
                        iconCls : 'Pageedit',
                        handler : me.onMailDely,
                        hidden:true
                    }]
                }];

            me.callParent(arguments);
        },
        onMailDely:function(){
			 var me = this.up("gridpanel");
			 var selections = me.getView().getSelectionModel()
              .getSelection();
			 if(selections.length>1)
			 {
				 Ext.Msg.alert("提示", "请选择需要一条需要延期的记录!");
				 return;
			 } 
			 if(selections[0].get("is_delete") == 1 )
			 {
				 Ext.Msg.alert("提示", "该记录已删除!");
				 return;
			 }
	         var form = Ext.create("Rds.trace.form.TraceMailDelyForm",
	                {
	                    region : "center",
	                    autoScroll : true,
	                    grid : me
	                });
	         form.loadRecord(selections[0]);
           var win = Ext.create("Ext.window.Window", {
               title : '案例延期',
               width : 400,
               iconCls : 'Pageadd',
               height : 200,
               modal:true,
               layout : 'border',
               items : [ form ]
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
                title : "车辆信息（案例序号：" + selections[0].get("case_no")+" 年份："+
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
        onDelete : function() {
            var me = this.up("gridpanel");
            var selections = me.getView().getSelectionModel()
                .getSelection();
            if (selections.length < 1) {
                Ext.Msg.alert("提示", "请选择需要删除的记录!");
                return;
            };
            if(selections[0].get("status")==4){
                Ext.Msg.alert("提示", "此案例已打印无法废除!");
                return;
            }
            if(selections[0].get("is_delete")!=0){
                Ext.Msg.alert("提示", "此案例已废除!");
                return;
            }
            var values = {
                case_id : selections[0].get("case_id")
            };
            Ext.MessageBox
                .confirm(
                '提示',
                '确定删除此案例吗',
                function(id) {
                    if (id == 'yes') {
                        Ext.Ajax
                            .request({
                                url : "trace/register/delete.do",
                                method : "POST",
                                headers : {
                                    'Content-Type' : 'application/json'
                                },
                                jsonData : values,
                                success : function(
                                    response,
                                    options) {
                                    response = Ext.JSON
                                        .decode(response.responseText);
                                    if (response.result == true) {
                                        Ext.MessageBox
                                            .alert(
                                            "提示信息",
                                            "废除成功！");
                                        me
                                            .getStore()
                                            .load();
                                    } else {
                                        Ext.MessageBox
                                            .alert(
                                            "错误信息",
                                            "废除失败！");
                                    }
                                },
                                failure : function() {
                                    Ext.Msg
                                        .alert(
                                        "提示",
                                        "网络故障<br>请联系管理员!");
                                }
                            });
                    }
                },{
                    xtype:'panel',
                    region:"center",
                    border : false
                });
        },
        onInsert : function() {
            var me = this.up("gridpanel");
            var form = Ext.create(
                "Rds.trace.form.TraceRegisterInsertForm",
                {
                    region : "center",
                    autoScroll : true,
                    operType:"add",
                    grid : me
                });
            var win = Ext.create("Ext.window.Window", {
                title : '案例添加',
                width : 900,
                iconCls : 'Pageadd',
                height : 620,
                modal:true,
                layout : 'border',
                items : [ form ]
            });
            win.show();
        },
        onUpdate : function() {
            var me = this.up("gridpanel");
            var selections = me.getView().getSelectionModel()
                .getSelection();
            if (selections.length < 1) {
                Ext.Msg.alert("提示", "请选择需要修改的记录!");
                return;
            }
            if(selections[0].get("is_delete")==1){
                Ext.Msg.alert("提示", "此案例已删除，无法修改!");
                return;
            }
            if(selections[0].get("verify_baseinfo_state")==1){
                Ext.Msg.alert("提示", "此案例已审核，无法修改!");
                return;
            }
            ownpersonTemp=selections[0].get("receiver_id") ;
            var form = Ext.create(
                "Rds.trace.form.TraceRegisterUpdateForm", {
                    region : "center",
                    grid : me,
                    autoScroll : true,
                    operType:"update",
                    case_id:selections[0].get("case_id"),
                    finance_status:selections[0].get("finance_status")
                });
            var case_type=selections[0].get("case_type")==null?"":selections[0].get("case_type");
            var case_type_array =case_type.toString().split(',');
            selections[0].set("case_type",case_type_array)
            form.loadRecord(selections[0]);
            var case_attachment_desc=selections[0].get("case_attachment_desc")==null?"":selections[0].get("case_attachment_desc");
            var case_attachment_desc_array=case_attachment_desc.split(',');
            var xqCheck = Ext.getCmp('tracecheckbox').items;
            for(var i=0;i<xqCheck.length;i++){
                for(var j=0;j<case_attachment_desc_array.length;j++) {
                    if (xqCheck.get(i).boxLabel==case_attachment_desc_array[j]) {
                        xqCheck.get(i).setValue(true);
                    }
                }
            }

            var win = Ext.create("Ext.window.Window", {
                title : '案例修改',
                width : 900,
                iconCls : 'Pageedit',
                height : 630,
                modal:true,
                layout : 'border',
                items : [ form ]
            });
            win.show();
        },
        onSearch : function() {
            var me = this.up("gridpanel");
            me.getStore().currentPage=1;
            me.getStore().load();
        },
        onCasePrint : function() {
            var me = this.up("gridpanel");
            var selections = me.getView().getSelectionModel()
                .getSelection();
            if (selections.length < 1) {
                Ext.Msg.alert("提示", "请选择需要编辑的模板!");
                return;
            }
            if(selections[0].get("is_delete")!=0){
                Ext.Msg.alert("提示", "此案例已废除!");
                return;
            }
            if(selections[0].get("status")==4){
                Ext.Msg.alert("提示", "此案例报告已通过审核!");
                return;
            }
            if(selections[0].get("status")<1){
                Ext.Msg.alert("提示", "此案例基本信息没有审核，请先审核该案例基本信息!");
                return;
            }
            var case_id = selections[0].get("case_id");
            var src="trace/attachment/getWord.do?year="+
                selections[0].get("year")+"&case_no="+selections[0].get("case_no")
                +"&case_id="+selections[0].get("case_id")+"&firstdownload=Y&template_name=trace.ftl"
            var win = Ext.create("Ext.window.Window", {
                config:{
                    case_id:case_id,
                    gridpanel:me
                },
                title : '报告编写',
                iconCls : 'Find',
                layout:"auto",
                maximized:true,
                maximizable :true,
                modal:true,
                bodyStyle : "background-color:white;",
                html:"<iframe width=100% height=100% id='verifyWord' src='"+src+"'></iframe>",
                buttons:[
                    {
                        text : '确认报告编写完毕',
                        iconCls : 'Disk',
                        handler : me.accept
                    },{
                        text : '取消',
                        iconCls : 'Arrowredo',
                        handler : me.onCancel
                    }
                ]
            });
            win.show();
        },
        onUpload:function(){
                var me = this.up("gridpanel");
                var selections = me.getView().getSelectionModel()
                .getSelection();
                if (selections.length < 1) {
                    Ext.Msg.alert("提示", "请选择需要上传报告的案例!");
                    return;
                }
                if (selections[0].get("status") == 2) {
                    Ext.Msg.alert("提示", "该案例报告已经审核完毕!");
                    return;
                }
                if (selections[0].get("status") > 3) {
                    Ext.Msg.alert("提示", "该案例报告已经打印完毕!");
                    return;
                }
                if (selections[0].get("is_delete") == '1') {
                    Ext.Msg.alert("提示", "该案例已经废除!");
                    return;
                }
                var form = Ext.create(
                    "Rds.trace.form.TraceFileUploadFormPanel", {
                        region: "center",
                        year: selections[0].get('year'),
                        case_no:selections[0].get('case_no'),
                        case_id:selections[0].get('case_id'),
                        grid:me
                    });
                var win = Ext.create("Ext.window.Window", {
                    title: '上传新文件',
                    width: 400,
                    iconCls: 'Add',
                    height: 230,
                    layout: 'border',
                    modal: true,
                    items: [form]
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
        onExport:function(){
            var me = this.up("gridpanel");
            window.location.href = "trace/register/exportInfo.do?start_time="
            + dateformat(me.getDockedItems('toolbar')[0].getComponent('start_time').getValue())
            + "&end_time=" + dateformat(me.getDockedItems('toolbar')[0].getComponent('end_time').getValue())
            + "&is_delete="+ me.getDockedItems('toolbar')[0].getComponent('is_delete').getValue()
            + "&case_no=" + me.getDockedItems('toolbar')[0].getComponent('case_no').getValue()
            + "&status=" + me.getDockedItems('toolbar')[0].getComponent('status').getValue()
            +"&invoice_number="+Ext.getCmp("invoice_number").getValue()
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
                status:2
            };
            // Ext.MessageBox
            //     .confirm(
            //         '提示',
            //         '确定删除此案例吗',
            //         function(id) {
            //             if (id == 'yes') {
            Ext.Ajax.request({
                url:"trace/verify/updateStatus.do",
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                jsonData: values,
                success: function (response, options) {
                    response = Ext.JSON.decode(response.responseText);
                    if (response.result==true) {
                        Ext.MessageBox.alert("提示信息", "编辑报告成功");
                        gridpanel.getStore().load();
                        me.close();
                    }else {
                        Ext.MessageBox.alert("错误信息", "编辑报告失败");
                    }
                },
                failure: function () {
                    Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                }
            });
        // }})
        },
        listeners : {
            'afterrender' : function() {
                this.store.load();
            }
        }
    });
