var sampleStatistic="";
Ext.define("Rds.statistic.panel.SampleStatisticGridPanel",{
    extend:"Ext.grid.Panel",
    loadMask: true,
    viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    initComponent : function() {
        var me = this;
		var month=Ext.create('Ext.form.DateField', {
			name : 'month',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '统计月份',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format: 'Y-m',
			value:Ext.util.Format.date(new Date(),"Y-m")
		});
		var sample_in_per = Ext.create('Ext.form.field.Text', {
			name : 'sample_in_per',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '采样员'
		});
		var deptname = Ext.create('Ext.form.field.Text', {
			name : 'deptname',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '部门'
		});
        me.store = Ext.create('Ext.data.Store',{
            fields:['sample_in_per','sample_in_per_id','deptcode', 'month','countSample','deptname','money'],
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                params:{
                },
                api:{
                    read:'statistics/sampleStatistics/query.do'
                },
                reader: {
                    type: 'json',
                    root:'data',
                    totalProperty:'total'
                },
                autoLoad: true//自动加载
            },listeners:{
                'beforeload':function(ds, operation, opt){
                    Ext.apply(me.store.proxy.params,
                        {
                            month:Ext.util.Format.date(month.getValue(),"Y-m"),
                            sample_in_per:sample_in_per.getValue().trim(),
                            deptname:deptname.getValue().trim()
                        });
                    sampleStatistic= "month="
                        + Ext.util.Format.date(month.getValue(),"Y-m")
                        + "&sample_in_per="+ sample_in_per.getValue().trim()
                        + "&deptname=" + deptname.getValue().trim();
                }
            }
        });


        me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
            emptyMsg : "没有符合条件的记录"
        });
        me.columns = [
            {text: '案例采样月份',dataIndex: 'month',width : 150},
            {text: '采样员',dataIndex: 'sample_in_per',width : 150},
            {text: '部门',dataIndex: 'deptname',width : 150},
    		{text: '数量',dataIndex: 'countSample',width : 150,
            	renderer : function(value, cellmeta,
						record, rowIndex, columnIndex,
						store) {
						return "<a href='#'>"+ record.data["countSample"]+"</a>";
				},
				listeners:{
					'click':function(){ 
						var me = this.up("gridpanel");
						var selections = me.getView().getSelectionModel().getSelection();
						var month = selections[0].get("month");
						var sample_in_per_id = selections[0].get("sample_in_per_id");
						var sampleStore = Ext.create('Ext.data.Store', {
							fields : ['sample_code', 'sample_username', 'sample_in_per', 'client',
									'accept_time', 'deptname', 'deptcode', 'month'],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'post'
								},
								url : 'statistics/sampleStatistics/querySample.do',
								params : {},
								reader : {
									type : 'json',
									root : 'data',
									totalProperty : 'total'
								}
							},
							listeners : {
								'beforeload' : function(ds, operation, opt) {
									Ext.apply(sampleGrid.store.proxy.params, {
										sample_in_per_id : sample_in_per_id,
										month:month
											});
								}
							}
						});
						var sampleGrid = Ext.create("Ext.grid.Panel", {
							region : 'center',
							store : sampleStore,
							columns : [Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'80', menuDisabled:true}),
							           {
										text : '样本条码',
										dataIndex : 'sample_code',
										width : 120,
										menuDisabled : true
									}, {
										text : '样本姓名',
										dataIndex : 'sample_username',
										menuDisabled : true,
										width : 100
									}, {
										text : '采样人',
										dataIndex : 'sample_in_per',
										menuDisabled : true,
										width : 100
									}, {
										text : '委托人',
										dataIndex : 'client',
										width : 100,
										menuDisabled : true
									}, {
										text : '受理时间',
										dataIndex : 'accept_time',
										width : 100,
										menuDisabled : true
									}, {
										text : '部门',
										dataIndex : 'deptname',
										width : 120,
										menuDisabled : true
									},{
										text : '月份',
										dataIndex : 'month',
										width : 100,
										menuDisabled : true
									}],
							bbar : Ext.create('Ext.PagingToolbar', {
										store : sampleStore,
										pageSize : 15,
										displayInfo : true,
										displayMsg : "第 {0} - {1} 条  共 {2} 条",
										emptyMsg : "没有符合条件的记录"
									}),
							listeners : {
								'afterrender' : {
									fn : function() {
										sampleGrid.store.load();
									}
								}
							}

						});
						var win = Ext.create("Ext.window.Window", {
							title : '样本信息',
							width : 810,
							iconCls : 'Pageedit',
							height : 500,
							layout : 'border',
							modal : true,
							buttons : [{
										text : '导出',
										iconCls : 'Pageexcel',
										handler : function() {
											window.location.href = "statistics/sampleStatistics/exporSampletInfo.do?month="+month+"&sample_in_per_id="+sample_in_per_id;
										}
									
									},{
										text : '确定',
										iconCls : 'Accept',
										handler : function() {
											this.up("window").close();
										}
									}],
							items : [sampleGrid]

						});
						win.show();
						
					}
				}
			},
    		{text: '采样金额',dataIndex: 'money',width : 150}
        ];

        me.dockedItems = [{
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [ sample_in_per,month,deptname,{
                text:'查询',
                iconCls:'Find',
                handler:me.onSearch
            }]
		},{
            xtype:'toolbar',
            dock:'top',
            items:[{
                text : '导出',
                iconCls : 'Pageexcel',
                handler : me.onExport
            }
            ]
        }];

        me.callParent(arguments);
    },
    onExport:function(){
        window.location.href = "statistics/sampleStatistics/exportInfo.do?"+sampleStatistic
    },
    onSearch : function() {
        var me = this.up("gridpanel");
        me.getStore().currentPage=1;
        me.getStore().load();
    },
    listeners:{
        'afterrender':function(){
            this.store.load();
        }
    }
});
