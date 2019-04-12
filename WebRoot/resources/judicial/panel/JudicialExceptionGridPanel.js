Ext.define("Rds.judicial.panel.JudicialExceptionGridPanel", {
    extend: "Ext.grid.Panel",
    loadMask: true,
    viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize: 25,
    initComponent: function () {
        var me = this;
        me.store = Ext.create('Ext.data.Store', {
            storeId:'Rds.judicial.panel.JudicialExceptionGridStore',
            fields: ['uuid', 'exception_id','exception_desc','case_code',
                'sample_code1', 'sample_code2','trans_date', 'state', 'choose_flag','case_id'],
            start:0,
			limit:15,
			pageSize:15,
            proxy: {
                type: 'jsonajax',
                actionMethods: {read: 'POST'},
                params: {
                },
                api: {
                    read: 'judicial/exception/queryAllException.do'
                },
                reader: {
                    type: 'json',
                    root: 'data',
                    totalProperty: 'total'
                },
                autoLoad: false//自动加载
            },
            listeners: {
                'beforeload': function (ds, operation, opt) {
                    Ext.apply(me.store.proxy.params,{
                        starttime: dateformat(me.getDockedItems('toolbar')[0].
                            getComponent('starttime').getValue()),
                        endtime: dateformat(me.getDockedItems('toolbar')[0].
                            getComponent('endtime').getValue()),
                        case_code: trim(me.getDockedItems('toolbar')[0].
                            getComponent('case_code').getValue()),
                        sample_code: trim(me.getDockedItems('toolbar')[0].
                            getComponent('sample_code').getValue()),
                        choose_flag: trim(me.getDockedItems('toolbar')[0].
                            getComponent('choose_flag').getValue())
                    });
                }
            }
        });
        me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode: 'SIMPLE'
		});
		//分页的combobox下拉选择显示条数
	     combo = Ext.create('Ext.form.ComboBox',{
	          name: 'pagesize',
	          hiddenName: 'pagesize',
	          store: new Ext.data.ArrayStore({
	              fields: ['text', 'value'],
	              data: [['15', 15], ['30', 30],['60', 60], ['80', 80], ['100', 100]]
	          }),
	          valueField: 'value',
	          displayField: 'text',
	          emptyText:15,
	          width: 50
	      });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

    	//添加下拉显示条数菜单选中事件
       combo.on("select", function (comboBox) {
       var pagingToolbar=Ext.getCmp('pagingbarJudicialException');
          	pagingToolbar.pageSize = parseInt(comboBox.getValue());
          	itemsPerPage = parseInt(comboBox.getValue());//更改全局变量itemsPerPage
          	me.store.pageSize = itemsPerPage;//设置store的pageSize，可以将工具栏与查询的数据同步。
          	me.store.load({  
          		params:{  
                  start:0,  
                  limit: itemsPerPage
          		}  
          	});//数据源重新加载
          	me.store.loadPage(1);//显示第一页
       });
        
		me.bbar = Ext.create('Ext.PagingToolbar', {
			id:'pagingbarJudicialException',
			store: me.store,
			pageSize:me.pageSize,
			displayInfo: true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录",
	   	 	items: ['-', '每页显示',combo,'条']
      });

//        me.selModel = Ext.create('Ext.selection.CheckboxModel', {
//            mode: 'SIMPLE'
//        });
//
//        me.bbar = Ext.create('Ext.PagingToolbar', {
//            store: me.store,
//            pageSize: me.pageSize,
//            displayInfo: true,
//            displayMsg: "第 {0} - {1} 条  共 {2} 条",
//            emptyMsg: "没有符合条件的记录"
//        });
        me.columns = [
            {text: '涉及案例id', dataIndex: 'case_id', hidden:true},
            {text: '涉及案例编号', dataIndex: 'case_code', width: '20%'},
            {text: '样本编号1', dataIndex: 'sample_code1', width: '15%'},
            {text: '样本编号2', dataIndex: 'sample_code2', width: '15%'},
            {text: '异常发生日期', dataIndex: 'trans_date', width: '15%'},
            {text: '异常说明', dataIndex: 'exception_desc', width: '15%'},
            {text: '异常状态', dataIndex: 'choose_flag', width: '15%',
            renderer:function(value){
                switch(value){
                    case 1:return "未处理";
                           break;
                    case 0:return "已处理";
                           break;
                }
            }}
        ];

        me.dockedItems = [
            {
                xtype: 'toolbar',
                name: 'search',
                dock: 'top',
                items: [
                    {
                        itemId: 'case_code',
                        xtype: 'textfield',
                        name: 'case_code',
                        labelWidth: 80,
                        width: 220,
                        fieldLabel: '案例编号'
                    },
                    {
                        xtype: 'textfield',
                        itemId: 'sample_code',
                        fieldLabel: '样本编号',
                        width: 220,
                        labelWidth: 80,
                        name: 'sample_code'
                    },
                    {   xtype:"combobox",
                        width: 200,
                        fieldLabel: '是否已处理',
                        store:Ext.create('Ext.data.Store', {
                            fields: ['key', 'value'],
                            data : [
                                {"key":"是","value":"0"},
                                {"key":"否","value":"1"}
                            ]
                        }),
                        valueField:'value',
                        displayField:'key',
                        name:'choose_flag',
                        itemId:'choose_flag'
                    },
                    {
                        xtype: 'datefield',
                        name: 'starttime',
                        itemId: 'starttime',
                        width: 220,
                        fieldLabel: '开始时间',
                        labelWidth: 100,
                        labelAlign: 'right',
                        emptyText: '请选择日期',
                        format: 'Y-m-d',
                        maxValue: new Date(),
                        value: Ext.Date.add(new Date(), Ext.Date.DAY,-7),
//                        listeners: {
//                            'select': function () {
//                                var start = me.up('gridpanel').getDockedItems('toolbar')[0].
//                                    getComponent('starttime');
//                                var end = me.up('gridpanel').getDockedItems('toolbar')[0].
//                                    getComponent('endtime');
//                                end.setMinValue(start.getValue);
//                                var endDate = end.getValue();
//                                if (start.getValue() > endDate) {
//                                    end.setValue(start.value());
//                                }
//                            }
//                        }
                    },
                    {
                        xtype: 'datefield',
                        itemId: 'endtime',
                        name: 'endtime',
                        width: 175,
                        labelWidth: 60,
                        fieldLabel: '结束时间',
                        labelAlign: 'right',
                        emptyText: '请选择日期',
                        format: 'Y-m-d',
                        maxValue: new Date(),
                        value: Ext.Date.add(new Date(), Ext.Date.DAY),
                        listeners: {
                            select: function () {
                                var start = me.up('gridpanel').getDockedItems('toolbar')[0].
                                    getComponent('starttime');
                                var end = me.up('gridpanel').getDockedItems('toolbar')[0].
                                    getComponent('endtime');
                                if (start.getValue() > end.getValue()) {
                                    start.setValue(end.getValue());
                                }
                            }
                        }
                    },
                    {
                        text: '查询',
                        iconCls: 'Find',
                        handler: me.onSearch
                    }
                ]
            },
            {
                xtype: 'toolbar',
                dock: 'top',
                items: [
                    {
                        text: '手动处理异常信息',
                        iconCls: 'Pageedit',
                        handler: me.onUpdate
                    },{
                        text : '查看样本信息',
                        iconCls : 'Find',
                        handler : me.onFind
                    },{
						text : '批量处理异常',
						iconCls : 'Printer',
						handler : me.getZip
					}
                ]
            }
        ];

        me.callParent(arguments);
    },
    onUpdate: function () {
        var me = this.up("gridpanel");
        var selections = me.getView().getSelectionModel()
            .getSelection();
        if (selections.length < 1) {
            Ext.Msg.alert("提示", "请选择需要修改的记录!");
            return;
        }
        var values = {
            uuid:selections[0].get('uuid'),
            choose_flag:0
        }
        Ext.MessageBox.confirm('删除确认','是否标识该异常已被处理',function(id){
            if(id=='yes') {
                Ext.Ajax.request({
                    url:"judicial/exception/deleteException.do",
                    method: "POST",
                    headers: { 'Content-Type': 'application/json' },
                    jsonData: values,
                    success: function (response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response.result == true) {
                            me.getStore().load();
                            Ext.MessageBox.alert("提示信息", response.message);
                        }else {
                            me.getStore().load();
                            Ext.MessageBox.alert("错误信息", response.message);
                        }
                    },
                    failure: function () {
                        me.getStore().load();
                        Ext.Msg.alert("提示", "删除失败<br>请联系管理员!");
                    }
                });
            }
        })
    },
    getZip : function(){
						var me = this.up("gridpanel");
						var selections = me.selModel.getSelection();
						if (selections.length < 1) {
							Ext.Msg.alert("提示", "请选择需要修改的记录!");
							return;
						};
						var data = new Array() ;
						for(var i=0;i<selections.length;i++){
							data[i] = selections[i].data;
						}
        Ext.MessageBox.confirm('删除确认','是否标识这些异常已被处理',function(id){
            if(id=='yes') {
                Ext.Ajax.request({
                    url:"judicial/exception/deleteAllException.do",
                    method: "POST",
                    headers: { 'Content-Type': 'application/json' },
                    jsonData: data,
                    success: function (response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response.result == true) {
                            me.getStore().load();
                            Ext.MessageBox.alert("提示信息", response.message);
                        }else {
                            me.getStore().load();
                            Ext.MessageBox.alert("错误信息", response.message);
                        }
                    },
                    failure: function () {
                        me.getStore().load();
                        Ext.Msg.alert("提示", "删除失败<br>请联系管理员!");
                    }
                });
            }
        })
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
            title : "样本信息（案例条形码：" + selections[0].get("case_code") + "）",
            width : 700,
            modal:true,
            iconCls : 'Find',
            height : 400,
            layout : 'border',
            bodyStyle : "background-color:white;",
            items : [ Ext.create('Ext.grid.Panel', {
                renderTo : Ext.getBody(),
                width : 700,
                height : 400,
                frame : false,
                viewConfig : {
                    forceFit : true,
                    stripeRows : true// 在表格中显示斑马线
                },
                store : {// 配置数据源
                    fields : [ 'sample_id', 'sample_code', 'sample_type','sample_typename',
                        'sample_call', 'sample_callname', 'sample_username',
                        'id_number', 'birth_date' ],// 定义字段
                    proxy : {
                        type : 'jsonajax',
                        actionMethods : {
                            read : 'POST'
                        },
                        url : 'judicial/allcaseinfo/getExceptionSampleInfo.do',
                        params : {
                            'sample_code1' : selections[0].get("sample_code1"),
                            'sample_code2' : selections[0].get("sample_code2")
                        },
                        reader : {
                            type : 'json',
                            root : 'items',
                            totalProperty : 'count'
                        }
                    },
                    autoLoad : true
                    // 自动加载
                },
                columns : [// 配置表格列
                    {
                        header : "样本条形码",
                        dataIndex : 'sample_code',
                        width : 150,
                        menuDisabled : true
                    }, {
                        header : "称谓",
                        dataIndex : 'sample_callname',
                        width : 80,
                        menuDisabled : true
                    }, {
                        header : "姓名",
                        dataIndex : 'sample_username',
                        width : 100,
                        menuDisabled : true
                    }, {
                        header : "身份证号",
                        dataIndex : 'id_number',
                        width : 150,
                        menuDisabled : true
                    }, {
                        header : "出生日期",
                        dataIndex : 'birth_date',
                        width : 120,
                        menuDisabled : true
                    }, {
                        header : "样本类型",
                        dataIndex : 'sample_typename',
                        width : 100,
                        menuDisabled : true
                    } ]
            }) ]
        });
        win.show();
    },
    onSearch: function () {
        var me = this.up("gridpanel");
        me.store.currentPage = 1;
        me.getStore().load();
    },
    listeners: {
        'afterrender': function () {
            this.store.load();
        }
    }
});
