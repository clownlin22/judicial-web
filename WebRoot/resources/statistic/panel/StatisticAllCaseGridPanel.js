var exportStatistic="";
Ext.define("Rds.statistic.panel.StatisticAllCaseGridPanel",{
    extend:"Ext.grid.Panel",
    loadMask: true,
    viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    initComponent : function() {
        var me = this;
		var start_time=Ext.create('Ext.form.DateField', {
			name : 'start_time',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '受理时间 从',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")
		});
		var end_time=Ext.create('Ext.form.DateField', {
			name : 'end_time',
			width : '20%',
			labelWidth : 10,
			fieldLabel : '到',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value:Ext.Date.add(
                    new Date(),
                    Ext.Date.DAY)
		});
		var client = Ext.create('Ext.form.field.Text', {
			name : 'client',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '委托人'
		});
        me.store = Ext.create('Ext.data.Store',{
            fields:['accept_time',
        'case_code',
        'real_sum',
        'return_sum',
        'ptype',
        'rtype',
        'receiver',
        'user_dept_level1',
        'user_dept_level2',
        'user_dept_level3',
        'user_dept_level4',
        'user_dept_level5',
        'agent',
        'county',
        'city',
        'province',
        'partner',
        'status',
        'client',
        'deductible'],
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                params:{
                },
                api:{
                    read:'statistics/allCase/query.do'
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
                            ptype:me.getDockedItems('toolbar')[0].
                            	getComponent('ptype').getValue(),
                            receiver:me.getDockedItems('toolbar')[0].
                            	getComponent('receiver').getValue(),
                            deptname:me.getDockedItems('toolbar')[0].
                            	getComponent('deptname').getValue(),
                            agent:me.getDockedItems('toolbar')[0].
                            	getComponent('agent').getValue(),
                            start_time:dateformat(start_time.getValue()),
                            end_time:dateformat(end_time.getValue()),
                            case_code:me.getDockedItems('toolbar')[0].
                                    getComponent('case_code').getValue(),
                            client:client.getValue().trim()
                        });
                    exportStatistic= "start_time="
                        + dateformat(start_time.getValue())
                        + "&end_time=" + dateformat(end_time.getValue())
                        + "&ptype="+ me.getDockedItems('toolbar')[0].getComponent('ptype').getValue()
                        + "&receiver=" + me.getDockedItems('toolbar')[0].getComponent('receiver').getValue()
                        + "&deptname=" + me.getDockedItems('toolbar')[0].getComponent('deptname').getValue()
                        + "&agent=" + me.getDockedItems('toolbar')[0].getComponent('agent').getValue()
                        + "&case_code="+ me.getDockedItems('toolbar')[0].getComponent('case_code').getValue()
                        + "&client="+ client.getValue().trim();
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
            {text: '案例起始时间',dataIndex: 'accept_time'},
            {text: '编号',dataIndex: 'case_code',width:150,
            	renderer : function(value, cellmeta,
    					record, rowIndex, columnIndex,
    					store) {
    				var status= record.data["status"];
    				if (status == 0) {
    					return "<div style=\"color: red;\">"
    							+ value + "</div>"
    				} else {
    					return value;
    				}
    			}},
    		{text: '委托人',dataIndex: 'client'},
            {text: '应收款项',dataIndex: 'real_sum'},
            {text: '实收款项',dataIndex: 'return_sum'},
            {text: '项目类别',dataIndex: 'ptype'},
            {text: '报告种类',dataIndex: 'rtype'},
            {text: '员工名字',dataIndex: 'receiver'},
            {text: '一级部门',dataIndex: 'user_dept_level1'},
            {text: '二级部门',dataIndex: 'user_dept_level2'},
            {text: '三级部门',dataIndex: 'user_dept_level3'},
            {text: '四级部门',dataIndex: 'user_dept_level4'},
            {text: '五级部门',dataIndex: 'user_dept_level5'},
            {text: '代理商名称',dataIndex: 'agent'},
            {text: '省',dataIndex: 'province'},
            {text: '地市',dataIndex: 'city'},
            {text: '区县',dataIndex: 'county'},
            {text: '合作方',dataIndex: 'partner'},{
				text : '是否抵扣',
				dataIndex : 'deductible',
				menuDisabled : true,
				width : 100,
				renderer : function(value,meta,record) {
					switch (value) {
					case '1':
						return "<div style=\"color: red;\">是</div>";
						break;
					case '2':
						return "否"
						break;
					default:
						return "否";
					}
				}
	          }
        ];

        me.dockedItems = [{
            xtype:'toolbar',
            name:'search',
            dock:'top',
            items:[{
                xtype : 'textfield',
                name : 'case_code',
                itemId : 'case_code',
                labelWidth : 70,
                width : '20%',
                fieldLabel : '案例编号'
            },{
                xtype : 'textfield',
                name : 'ptype',
                itemId : 'ptype',
                labelWidth : 70,
                width : '20%',
                fieldLabel : '项目类别'
            },{
                xtype : 'textfield',
                name : 'receiver',
                itemId : 'receiver',
                labelWidth : 70,
                width : '20%',
                fieldLabel : '员工名字'
            },{
                xtype : 'textfield',
                name : 'deptname',
                itemId : 'deptname',
                labelWidth : 70,
                width : '20%',
                fieldLabel : '所属部门'
            },{
                xtype : 'textfield',
                name : 'agent',
                itemId : 'agent',
                labelWidth : 70,
                width : '20%',
                fieldLabel : '代理商名字'
            }
            ]
        },{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 	},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [ client,start_time,  end_time,{
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
            var me = this.up("gridpanel");
            window.location.href = "statistics/allCase/exportInfo.do?"+exportStatistic
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
