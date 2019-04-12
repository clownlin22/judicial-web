var FinanceStatistic="";
Ext.define("Rds.statistic.panel.FinanceCaseAllStatisticGridPanel",{
    extend:"Ext.grid.Panel",
    loadMask: true,
    viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    initComponent : function() {
        var me = this;
        var deptname = Ext.create('Ext.data.Store',{
		    fields:['deptname'],
		    autoLoad:true,
		    proxy:{
		    	type: 'jsonajax',
		        actionMethods:{read:'POST'},
		        params:{},
		        url:'statistics/financeConfig/queryUserDept.do',
		        reader:{
		          type:'json',
		        },
		        writer: {
		            type: 'json'
		       }
		      },
		      remoteSort:true
		});
		var case_user = Ext.create('Ext.form.field.Text', {
			name : 'case_user',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '归属人'
		});
		var webchart = Ext.create('Ext.form.field.Text', {
			name : 'webchart',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '人员工号'
		});
		var client = Ext.create('Ext.form.field.Text', {
			name : 'client',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '委托人'
		});
		var case_area = Ext.create('Ext.form.field.Text', {
			name : 'case_area',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '归属地'
		});
		var case_code = Ext.create('Ext.form.field.Text', {
			name : 'case_code',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '案例编号'
		});
		var user_dept_level1 = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			labelWidth : 70,
			width : '20%',
			fieldLabel : '事业部',
		    name:'user_dept_level1',
		    triggerAction: 'all',
		    queryMode: 'local', 
		    emptyText : "请选择",
		    selectOnTab: true,
		    store: deptname,
		    fieldStyle: me.fieldStyle,
		    displayField:'deptname',
		    valueField:'deptname',
		    listClass: 'x-combo-list-small',
		})
//		var user_dept_level1 = Ext.create('Ext.form.field.Text', {
//			name : 'user_dept_level1',
//			labelWidth : 70,
//			width : '20%',
//			fieldLabel : '一级部门'
//		});
		var user_dept_level2 = Ext.create('Ext.form.field.Text', {
			name : 'user_dept_level1',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '二级部门'
		});
		var user_dept_level3 = Ext.create('Ext.form.field.Text', {
			name : 'user_dept_level1',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '三级部门'
		});
		var insideCostUnit =new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			labelWidth : 70,
			width : '20%',
			fieldLabel : '结算费部门',
		    name:'insideCostUnit',
		    triggerAction: 'all',
		    queryMode: 'local', 
		    emptyText : "请选择",
		    selectOnTab: true,
		    store: deptname,
		    fieldStyle: me.fieldStyle,
		    displayField:'deptname',
		    valueField:'deptname',
		    listClass: 'x-combo-list-small',
		})
		var manageCostUnit = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			labelWidth : 70,
			width : '20%',
			fieldLabel : '管理费部门',
		    name:'manageCostUnit',
		    triggerAction: 'all',
		    queryMode: 'local', 
		    emptyText : "请选择",
		    selectOnTab: true,
		    store: deptname,
		    fieldStyle: me.fieldStyle,
		    displayField:'deptname',
		    valueField:'deptname',
		    listClass: 'x-combo-list-small',
		})
		var case_type = Ext.create('Ext.form.field.Text', {
			name : 'case_type',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '案例项目'
		});
		var case_subtype = Ext.create('Ext.form.field.Text', {
			name : 'case_subtype',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '子项目'
		});
		var type=new Ext.form.field.ComboBox({
			fieldLabel : '类型',
			width:'20%',
			labelWidth : 70,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部',''],['服务收入','服务收入' ],['销售收入','销售收入' ],['代理收入','代理收入'],['合作方收入','合作方收入'] ]
					}),
			value : '',
			mode : 'local',
			name : 'type',
			value: ''
		});
		var accept_time_start = new Ext.form.DateField({
			name : 'accept_time_start',
			width:'20%',
			fieldLabel : '受理日期从',
			labelWidth : 70,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-31)
		});
		var accept_time_end = new Ext.form.DateField({
			name : 'accept_time_end',
			width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var confirm_date_start = new Ext.form.DateField({
			name : 'confirm_date_start',
			width:'20%',
			fieldLabel : '确认日期从',
			labelWidth : 70,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-31)
		});
		var confirm_date_end = new Ext.form.DateField({
			name : 'confirm_date_end',
			width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
        me.store = Ext.create('Ext.data.Store',{
            fields:['id','case_id','case_code','case_user','webchart','accept_time', 'case_area','confirm_date','real_sum','return_sum','user_dept_level1',
                    'user_dept_level2','user_dept_level3','user_dept_level4','user_dept_level5','insideCost','insideCostUnit','manageCost','manageCostUnit',
                    'externalCost','aptitudeCost','experimentCost','case_type','case_subtype','type','partner','client','remark','finance_remark','deductible'],
            start:0,
			limit:15,
			pageSize:15,
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                params:{
                },
                api:{
                    read:'statistics/financeConfig/queryCaseDetailAllPage.do'
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
                    		case_user:case_user.getValue().trim(),
                    		case_area:case_area.getValue().trim(),
                    		case_code:case_code.getValue().trim(),
                    		user_dept_level1:(null==user_dept_level1.getValue()?"":user_dept_level1.getValue()),
                    		user_dept_level2:user_dept_level2.getValue().trim(),
                    		user_dept_level3:user_dept_level3.getValue().trim(),
                    		insideCostUnit:(null==insideCostUnit.getValue()?"":insideCostUnit.getValue()),
                    		manageCostUnit:(null==manageCostUnit.getValue()?"":manageCostUnit.getValue()),
                    		case_type:case_type.getValue().trim(),
                    		case_subtype:case_subtype.getValue().trim(),
                    		type:type.getValue(),
                    		confirm_date_start:dateformat(confirm_date_start.getValue()),
                    		confirm_date_end:dateformat(confirm_date_end.getValue()),
                    		accept_time_start:dateformat(accept_time_start.getValue()),
                    		accept_time_end:dateformat(accept_time_end.getValue()),
                    		client:client.getValue().trim(),
                    		webchart:webchart.getValue().trim()
                        });
                    FinanceStatistic= "case_user="
                        + case_user.getValue().trim()
                        + "&case_area="+ case_area.getValue().trim()
                        + "&case_code="+ case_code.getValue().trim()
                        + "&user_dept_level1=" + (null==user_dept_level1.getValue()?"":user_dept_level1.getValue())
                        + "&user_dept_level2=" + user_dept_level2.getValue().trim()
                        + "&user_dept_level3=" + user_dept_level3.getValue().trim()
                        + "&insideCostUnit=" + (null==insideCostUnit.getValue()?"":insideCostUnit.getValue())
                        + "&manageCostUnit=" + (null==manageCostUnit.getValue()?"":manageCostUnit.getValue())
                        + "&case_type=" + case_type.getValue().trim()
                        + "&case_subtype=" + case_type.getValue().trim()
                        + "&type=" + type.getValue()
                        + "&confirm_date_start=" + dateformat(confirm_date_start.getValue())
                        + "&confirm_date_end=" + dateformat(confirm_date_end.getValue())
                        + "&accept_time_start=" + dateformat(accept_time_start.getValue())
                        + "&accept_time_end=" + dateformat(accept_time_end.getValue())
                        + "&webchart=" + webchart.getValue().trim()
                        + "&client=" + client.getValue().trim();
                }
            }
        });

        me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode: 'SINGLE'
		});
		//分页的combobox下拉选择显示条数
	    combo = Ext.create('Ext.form.ComboBox',{
	          name: 'pagesize',
	          hiddenName: 'pagesize',
	          store: new Ext.data.ArrayStore({
	              fields: ['text', 'value'],
	              data: [['15', 15], ['30', 30],['60', 60], ['100', 100], ['500', 500], ['1000', 1000]]
	          }),
	          valueField: 'value',
	          displayField: 'text',
	          emptyText:15,
	          width: 60
	     });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

       //添加下拉显示条数菜单选中事件
       combo.on("select", function (comboBox) {
       var pagingToolbar=Ext.getCmp('pagingbarFinance');
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
			id:'pagingbarFinance',
			store: me.store,
			pageSize:me.pageSize,
			displayInfo: true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录",
	   	 	items: ['-', '每页显示',combo,'条']
		});
        me.columns = [
             {
                 text: '案例回款归属核心部分',//这个就是合并的
                 columns: [{text: '案例归属事业部',dataIndex: 'user_dept_level1',width : 150},
               			{text: '案例归属二级部门',dataIndex: 'user_dept_level2',width : 150},
            			{text: '案例归属三级部门',dataIndex: 'user_dept_level3',width : 150},
            			{text: '案例归属四级部门',dataIndex: 'user_dept_level4',width : 150},
            			{text: '案例归属五级部门',dataIndex: 'user_dept_level5',width : 150},
                        {text: '案例归属人',dataIndex: 'case_user',width : 100},
                        {text: '归属人工号',dataIndex: 'webchart',width : 100}
                        ]
             
             },{
                 text: '收入细节部分',//这个就是合并的
                 columns: [{text: '案例受理日期',dataIndex: 'accept_time',width : 120, renderer: Ext.util.Format.dateRenderer('Y-m-d')},
               			{text: '收入类型',dataIndex: 'type',width : 120},
            			{text: '应收款项',dataIndex: 'real_sum',width : 150},
            			{text: '财务确认到账日期',dataIndex: 'confirm_date',width : 150, renderer: Ext.util.Format.dateRenderer('Y-m-d')},
            			{text: '实际回款金额',dataIndex: 'return_sum',width : 150},
                        {text: '财务备注',dataIndex: 'finance_remark',width : 100}
                        ]
             },{
                 text: '内部结算细节部分',//这个就是合并的
                 columns: [ {text: '内部结算部门',dataIndex: 'insideCostUnit',width : 150},
                            {text: '结算费用',dataIndex: 'insideCost',width : 150},
                            {text: '管理费部门',dataIndex: 'manageCostUnit',width : 150},
                            {text: '管理费',dataIndex: 'manageCost',width : 150},
                            {text: '委外成本',dataIndex: 'externalCost',width : 150},
                            {text: '资质费',dataIndex: 'aptitudeCost',width : 150},
                            {text: '实验费',dataIndex: 'experimentCost',width : 150}
                        ]
             },{
                 text: '其他参数',//这个就是合并的
                 columns: [
                           {text: '案例编号',dataIndex: 'case_code',width : 100},
                           {text: '委托人',dataIndex: 'client',width : 200},
                           {text: '案例归属地',dataIndex: 'case_area',width : 200},
                           {text: '项目',dataIndex: 'case_type',width : 150},
                           {text: '子项目',dataIndex: 'case_subtype',width : 150},
                           {text: '合作方',dataIndex: 'partner',width : 150},{
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
                        ]
             }
        ];

        me.dockedItems = [{
        	style : {
    	        borderTopWidth : '0px !important',
    	        borderBottomWidth : '0px !important'
    	 		},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [case_user,case_area,case_type,confirm_date_start,confirm_date_end]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [ type,user_dept_level1,user_dept_level2,user_dept_level3,insideCostUnit]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [ case_code,case_subtype,manageCostUnit,accept_time_start,accept_time_end]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [ client,webchart,{
                text:'查询',
                iconCls:'Find',
                handler:me.onSearch
            },{
                text : '导出',
                iconCls : 'Pageexcel',
                handler : me.onExport
            }]
		}];
        me.callParent(arguments);
    },
    onExport:function(){
        window.location.href = "statistics/financeConfig/exportCaseDetail.do?"+FinanceStatistic
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
