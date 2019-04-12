var experimentalData="";
Ext.define("Rds.statistic.panel.StatisticExperimentalDataGridPanel",{
    extend:"Ext.grid.Panel",
    loadMask: true,
    viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    initComponent : function() {
        var me = this;
		var accpet_start_time=Ext.create('Ext.form.DateField', {
			name : 'accpet_start_time',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '受理时间 从',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")
		});
		var accpet_end_time=Ext.create('Ext.form.DateField', {
			name : 'accpet_end_time',
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
	
		var consignment_start_time=Ext.create('Ext.form.DateField', {
			name : 'consignment_start_time',
			width : '20%',
			labelWidth : 70,
			fieldLabel : '采样时间 从',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")
		});
		var consignment_end_time=Ext.create('Ext.form.DateField', {
			name : 'consignment_end_time',
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
            fields:[
					 'case_code',
					'sample_codes',
					'consignment_time',
					'accept_time',
					'experiment_no',
					'fandm',
					'case_id',
					'id_card',
					'child',
					'birth_date',
					'identify_per',
					'compare_date',
					'ext_flag',
					'final_result_flag',
					'close_time',
					'mail_time',
					'mail_address',
					'mail_phone',
					'sample_count',
					'experiment_date'
                    ],
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                params:{
                },
                api:{
                    read:'statistics/experimental/queryallpage.do'
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
                    	case_code:me.getDockedItems('toolbar')[0].
                            getComponent('case_code').getValue(),
                        sample_codes:me.getDockedItems('toolbar')[0].
                            getComponent('sample_codes').getValue(),
                        experiment_no:me.getDockedItems('toolbar')[0].
                            getComponent('experiment_no').getValue(),
                        mail_phone:me.getDockedItems('toolbar')[0].
                            getComponent('mail_phone').getValue(),
                            accpet_start_time:dateformat(accpet_start_time.getValue()),
                            accpet_end_time:dateformat(accpet_end_time.getValue()),
                            consignment_start_time:dateformat(consignment_start_time.getValue()),
                            consignment_end_time:dateformat(consignment_end_time.getValue()),
                           
                        });
                    experimentalData= 
                    	"accpet_start_time="  + dateformat(accpet_start_time.getValue())
                        + "&accpet_end_time=" + dateformat(accpet_end_time.getValue())
                        +"&consignment_start_time="  + dateformat(consignment_start_time.getValue())
                        + "&consignment_end_time=" + dateformat(consignment_end_time.getValue())
                        + "&case_code="+ me.getDockedItems('toolbar')[0].getComponent('case_code').getValue()
                        + "&experiment_no=" + me.getDockedItems('toolbar')[0].getComponent('experiment_no').getValue()
                        + "&mail_phone=" + me.getDockedItems('toolbar')[0].getComponent('mail_phone').getValue()
                        + "&sample_codes=" + me.getDockedItems('toolbar')[0].getComponent('sample_codes').getValue()
                     
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
            {text: '案例编号',dataIndex: 'case_code'},
            {text: '采样编号',dataIndex: 'sample_codes'},
    		{text: '采样日期',dataIndex: 'consignment_time'},
            {text: '受理日期',dataIndex: 'accept_time'},
            {text: '实验编号',dataIndex: 'experiment_no'},
            {text: '父母亲',dataIndex: 'fandm'},
            {text: '身份证',dataIndex: 'id_card'},
            {text: '孩子',dataIndex: 'child'},
            {text: '出生日期',dataIndex: 'birth_date'},
            {text: '司法鉴定人',dataIndex: 'identify_per'},
            {text: '比对日期',dataIndex: 'compare_date'},
            {text: '是否添加位点',dataIndex: 'ext_flag',
            	 renderer : function(value) {
						switch (value) {
						case "Y":
							return "是";
							break;
						case "N":
							return "否";
							break;
						default:
							return "";
						}
					}	
            
            },
            {text: '位点最终结果',dataIndex: 'final_result_flag',
            	 renderer : function(value) {
						switch (value) {
						case "passed":
							return "肯定";
							break;
						case "failed":
							return "否定";
							break;
						default:
							return "";
						}
					}		
            },
            {text: '报告打印日期',dataIndex: 'close_time'},
            {text: '邮寄地址',dataIndex: 'mail_address'},
            {text: '电话',dataIndex: 'mail_phone'},
            {text: '样本数',dataIndex: 'sample_count'},
            {text: '实验日期',dataIndex: 'experiment_date'}
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
                name : 'sample_codes',
                itemId : 'sample_codes',
                labelWidth : 70,
                width : '20%',
                fieldLabel : '采样编号'
            },{
                xtype : 'textfield',
                name : 'experiment_no',
                itemId : 'experiment_no',
                labelWidth : 70,
                width : '20%',
                fieldLabel : '实验编号'
            },{
                xtype : 'textfield',
                name : 'mail_phone',
                itemId : 'mail_phone',
                labelWidth : 70,
                width : '20%',
                fieldLabel : '电话号码'
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
			items : [ accpet_start_time,  accpet_end_time,]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 	},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [ consignment_start_time,  consignment_end_time,{
                text:'查询',
                iconCls:'Find',
                handler:me.onSearch
            }]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 	}, 
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
            window.location.href = "statistics/experimental/exportexperimentaldata.do?"+experimentalData
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
