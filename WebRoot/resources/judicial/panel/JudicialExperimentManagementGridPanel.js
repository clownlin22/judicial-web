
Ext.define("Rds.judicial.panel.JudicialExperimentManagementGridPanel",{
    extend:"Ext.grid.Panel",
    loadMask: true,
    viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    initComponent : function() {
        var me = this;
        me.store = Ext.create('Ext.data.Store',{
            fields:['uuid','experiment_date','experimenter','experiment_no','enable_flag','places','reagent_name'],
            start:0,
			limit:15,
			pageSize:15,
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                //url: 'judicial/experiment/getCaseInfo.do',
                params:{
                },
                api:{
                    read:'judicial/experiment/queryExperiment.do'
                },
                reader: {
                    type: 'json',
                    root:'data',
                    totalProperty:'total'
                },
                autoLoad: true//自动加载
            },
            listeners:{
                'beforeload':function(ds, operation, opt){
                    Ext.apply(me.store.proxy.params,
                        {starttime: dateformat(Ext.getCmp('ExpStarttime').getValue()),
                         experimenter: trim(Ext.getCmp('experimenter').getValue()),
                         endtime: dateformat(Ext.getCmp('ExpEndtime').getValue()),
                         experiment_no:me.getDockedItems('toolbar')[0].
                             getComponent('experiment_no').getValue()});
                }
            }
        });

        me.selModel = Ext.create('Ext.selection.CheckboxModel',{
            mode: 'SINGLE'
        });

        me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
            emptyMsg : "没有符合条件的记录"
        });
        me.columns = [
            {text: '实验编号',dataIndex: 'experiment_no',width:'25%'},
            {text: '实验日期',dataIndex: 'experiment_date',width:'25%'},
            {text: '实验执行人',dataIndex: 'experimenter',width:'25%'},
            {text: '试剂名称',dataIndex: 'reagent_name',width:'25%'}
        ];

        me.dockedItems = [{
            xtype:'toolbar',
            name:'search',
            dock:'top',
            items:[{
                xtype:'textfield',
                name:'experiment_no',
                itemId:'experiment_no',
                labelWidth:80,
                width:220,
                fieldLabel:'实验编号'
            },{
                xtype:'textfield',
                fieldLabel:'实验执行人',
                width: 175,
                labelWidth: 80,
                id:'experimenter',
                name:'experimenter'
            },
                {
                    xtype:'datefield',
                    name:'starttime',
                    id: 'ExpStarttime',
                    width: 220,
                    fieldLabel: '开始时间',
                    labelWidth: 100,
                    labelAlign: 'right',
                    emptyText: '请选择日期',
                    format: 'Y-m-d',
                    maxValue: new Date(),
                    value: Ext.Date.add(new Date(), Ext.Date.DAY,-7),
                    listeners: {
                        'select': function () {
                            var start = Ext.getCmp('ExpStarttime').getValue();
                            Ext.getCmp('ExpEndtime').setMinValue(start);
                            var endDate = Ext.getCmp('ExpEndtime').getValue();
                            if (start > endDate) {
                                Ext.getCmp('ExpEndtime').setValue(start);
                            }
                        }
                    }
                }, {
                    xtype: 'datefield',
                    id: 'ExpEndtime',
                    name:'endtime',
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
                            var start = Ext.getCmp('ExpStarttime').getValue();
                            var endDate = Ext.getCmp('ExpEndtime').getValue();
                            if (start > endDate) {
                                Ext.getCmp('ExpStarttime').setValue('ExpEndDate');
                            }
                        }
                    }
                },
                {
                    text:'查询',
                    iconCls:'Find',
                    handler:me.onSearch
                }]
        },{
            xtype:'toolbar',
            dock:'top',
            items:[{
                text:'新增',
                iconCls:'Pageadd',
                handler:me.onInsert
            },{
                text:'修改',
                iconCls:'Pageedit',
                handler:me.onUpdate
            },{
                text:'添加修改实验样本模板',
                handler:me.addSample
            },{
                text:'下载实验模板',
                handler:me.onDownload
            },{
                text:'打印实验模板',
                handler:me.onPrint
            }]
        }];

        me.callParent(arguments);
    },
    onDownload : function() {
        var me = this.up("gridpanel");
        me.getStore().load();
        var selections = me.getView().getSelectionModel().getSelection();
        if(selections.length<1){
            Ext.Msg.alert("提示", "请选择下载实验的记录");
            return;
        };
        if(selections[0].get("places")==null ||selections[0].get("places")==''){
            Ext.Msg.alert("提示", "请先添加样本模板信息");
            return;
        }
        window.location.href = "judicial/experiment/ExportTxt.do?experiment_no=" +
            selections[0].get("experiment_no");
    },
    addSample:function(){
        var me = this.up("gridpanel");
        var selections =  me.getView().getSelectionModel().getSelection();
        if(selections.length<1){
            Ext.Msg.alert("提示", "请选择添加实验样本的记录");
            return;
        };
        var form = Ext.create("Rds.judicial.panel.JudicialExperimentSampleFormPanel",{
            region:"center",
            uuid:selections[0].get("uuid"),
            grid:me,
            experiment_no:selections[0].get("experiment_no")
        });
        var values = {
            uuid:selections[0].get("uuid")
        }
        Ext.Ajax.request({
            url:"judicial/experiment/queryPlaces.do",
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                jsonData: values,
                success: function (response, options) {
                    //console.log(response.responseText);
                var str = response.responseText;
                    str = str.substring(1,str.length-2);
                var strs = str.split(',');
                    //console.log(response.responseText.split(','));
                for(var i=1;i<=96;i++){
                	if(!(strs[i-1]==undefined || strs[i-1] == ''))
                    form.getComponent(strs[i-1].split('=')[0]).setValue(strs[i-1].split('=')[1]);
                }
            },
            failure: function () {
                Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
            }
        })

        var win = Ext.create("Ext.window.Window",{
            title:'新增修改样本托盘位置信息',
            width:1000,
            height:400,
            layout:'border',
            items:[form]
        });
        win.show();
        form.loadRecord(selections[0]);
    },
    onDelete:function(){
        var me = this.up("gridpanel");
        var selections =  me.getView().getSelectionModel().getSelection();
        console.log(selections[0].get("experiment_no"));
        if(selections.length<1){
            Ext.Msg.alert("提示", "请选择需要删除的记录!");
            return;
        };
        var values = {
            uuid:selections[0].get("uuid")
        };
        console.log(values);
        Ext.MessageBox.confirm('提示','确定删除此案例吗',function(id){
            if(id=='yes'){
                Ext.Ajax.request({
                    url:"judicial/experiment/deleteExperiment.do",
                    method: "POST",
                    headers: { 'Content-Type': 'application/json' },
                    jsonData: values,
                    success: function (response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response.result == true) {
                            Ext.MessageBox.alert("提示信息", "删除成功！");
                            me.getStore().load();
                        }else {
                            Ext.MessageBox.alert("错误信息", "删除失败！");
                        }
                    },
                    failure: function () {
                        Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                    }
                });
            }
        });
    },
    onInsert:function(){
        var me = this.up("gridpanel");

        var form = Ext.create("Rds.judicial.panel.JudicialExperimentManagementFormPanel",{
            region:"center",
            operType:'add',
            grid:me
        });

        var win = Ext.create("Ext.window.Window",{
            title:'添加实验',
            width:400,
            iconCls:'Pageadd',
            height:300,
            layout:'border',
            items:[form]
        });
        win.show();
    },
    onUpdate:function(){
        var me = this.up("gridpanel");
        var selections =  me.getView().getSelectionModel().getSelection();
        if(selections.length<1){
            Ext.Msg.alert("提示", "请选择需要修改的记录!");
            return;
        };
        var form = Ext.create("Rds.judicial.panel.JudicialExperimentManagementFormPanel",{
            region:"center",
            operType:"update",
            grid:me
        });
        var win = Ext.create("Ext.window.Window",{
            title:'实验——修改',
            width:400,
            iconCls:'Pageedit',
            height:300,
            layout:'border',
            items:[form]
        });
        win.show();
        form.loadRecord(selections[0]);
    },
    onSearch:function(){
        var me = this.up("gridpanel");
        me.store.currentPage = 1;
        me.getStore().load();
    },
    onPrint:function(){
        var me = this.up("gridpanel");
        var selections =  me.getView().getSelectionModel().getSelection();
        if(selections.length<1){
            Ext.Msg.alert("提示", "请选择需要打印的记录!");
            return;
        };
        if(selections[0].get("places")==null ||selections[0].get("places")==''){
            Ext.Msg.alert("提示", "请先添加样本模板信息");
            return;
        }
        var experiment_no = selections[0].get("experiment_no");
        var src="judicial/experiment/printModel.do?experiment_no="+experiment_no;
        var print_chanel=function(){
            win.close();
        }
        var print_print=function(me){
            var iframe = document.getElementById("experimentModel");
            iframe.contentWindow.focus();
            iframe.contentWindow.print();
            win.close();
        }
    var win=Ext.create("Ext.window.Window",{
        title : "打印预览",
        iconCls : 'Find',
        layout:"auto",
        maximized:true,
        maximizable :true,
        modal:true,
        bodyStyle : "background-color:white;",
        html:"<iframe width=100% height=100% id='experimentModel' src='"+src+"'></iframe>",
        buttons:[
            {
                text : '打印',
                iconCls : 'Disk',
                handler:  print_print
            }, {
                text : '取消',
                iconCls : 'Cancel',
                handler: print_chanel
            }
        ]
    });
win.show();
},
    listeners:{
        'afterrender':function(){
            this.store.load();
        }
    }
});
