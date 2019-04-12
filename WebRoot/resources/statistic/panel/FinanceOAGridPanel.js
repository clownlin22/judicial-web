var rowEditing = Ext.create('Ext.grid.plugin.RowEditing',{
            pluginId:'rowEditing',
            saveBtnText: '保存', 
            cancelBtnText: "取消", 
            autoCancel: false, 
            clicksToEdit:1   //双击进行修改  1-单击   2-双击    0-可取消双击/单击事件
});
var deptname1 = Ext.create('Ext.data.Store',{
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
var FinanceOA="";
Ext.define("Rds.statistic.panel.FinanceOAGridPanel",{
    extend:"Ext.grid.Panel",
    loadMask: true,
    viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    plugins: [rowEditing],
    initComponent : function() {
        var me = this;
		var kmmc = Ext.create('Ext.form.field.Text', {
			name : 'kmmc',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '费用类型'
		});
		var djbh = Ext.create('Ext.form.field.Text', {
			name : 'djbh',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '流程编号'
		});
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
		var user_dept_level = Ext.create('Ext.form.field.Text', {
			name : 'user_dept_level',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '承担部门'
		});
//		var ztsybmc = Ext.create('Ext.form.field.Text', {
//			name : 'ztsybmc',
//			labelWidth : 70,
//			width : '20%',
//			fieldLabel : '承担部门'
//		});
		var sqrxm = Ext.create('Ext.form.field.Text', {
			name : 'sqrxm',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '申请人姓名'
		});
		var tdrxm = Ext.create('Ext.form.field.Text', {
			name : 'tdrxm',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '提单人姓名'
		});
		var operatedate_start = new Ext.form.DateField({
			name : 'operatedate_start',
			width:'20%',
			fieldLabel : '操作日期从',
			labelWidth : 70,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-31)
		});
		var operatedate_end = new Ext.form.DateField({
			name : 'operatedate_end',
			width:'20%',
			style:'margin-left:10px;',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var sqrq_start = new Ext.form.DateField({
			name : 'sqrq_start',
			width:'20%',
			fieldLabel : '申请日期从',
			labelWidth : 70,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-31)
		});
		var sqrq_end = new Ext.form.DateField({
			name : 'sqrq_end',
			width:'20%',
			style:'margin-left:10px;',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
        me.store = Ext.create('Ext.data.Store',{
            fields:['case_id','djbh','sqrxm','ztsybmc','ztejmc','tdrxm','fycdrxm','sqrworkcode','tdrworkcode','fycdrworkcode','operatedate','operatetime', 
                    'isremark','cwcnyj','bxsm','mx1bxje','sqrq', 'kmmc','user_dept_level1',
                    'user_dept_level2','user_dept_level3','user_dept_level4','user_dept_level5'],
            start:0,
			limit:15,
			pageSize:15,
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                params:{
                },
                api:{
                    read:'statistics/financeOA/queryAllPage.do'
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
                    	    djbh:djbh.getValue().trim(),
                    		kmmc:kmmc.getValue().trim(),
                    		user_dept_level:user_dept_level.getValue().trim(),
                    		tdrxm:tdrxm.getValue().trim(),
                    		sqrxm:sqrxm.getValue().trim(),
                    		sqrq_start:dateformat(sqrq_start.getValue()),
                    		sqrq_end:dateformat(sqrq_end.getValue()),
                    		operatedate_start:dateformat(operatedate_start.getValue()),
                    		operatedate_end:dateformat(operatedate_end.getValue())
                        });
                    FinanceOA= "djbh="+djbh.getValue().trim()+"&kmmc="
                        + kmmc.getValue().trim()
                        + "&user_dept_level="+ user_dept_level.getValue().trim()
                        + "&tdrxm=" + tdrxm.getValue().trim()
                        + "&sqrxm=" + sqrxm.getValue().trim()
                        + "&sqrq_start=" + dateformat(sqrq_start.getValue())
                        + "&sqrq_end=" + dateformat(sqrq_end.getValue())
                        + "&operatedate_start=" + dateformat(operatedate_start.getValue())
                        + "&operatedate_end=" + dateformat(operatedate_end.getValue());
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
	              data: [['15', 15], ['30', 30],['60', 60], ['100', 100]]
	          }),
	          valueField: 'value',
	          displayField: 'text',
	          emptyText:15,
	          width: 60
	     });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

       //添加下拉显示条数菜单选中事件
       combo.on("select", function (comboBox) {
       var pagingToolbar=Ext.getCmp('pagingbarFinanceOA');
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
			id:'pagingbarFinanceOA',
			store: me.store,
			pageSize:me.pageSize,
			displayInfo: true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录",
	   	 	items: ['-', '每页显示',combo,'条']
		});
        me.columns = [
            {text: '流程编号',dataIndex: 'djbh',width : 150},
            {text: '流程申请人',dataIndex: 'sqrxm',width : 150},
            {
                text: '支出成本承担主体划分核心部分',//这个就是合并的
                columns: [{text: '案例归属事业部',dataIndex: 'user_dept_level1',width : 150},
	              		  {text: '案例归属二级部门',dataIndex: 'user_dept_level2',width : 150},
		           		  {text: '案例归属三级部门',dataIndex: 'user_dept_level3',width : 150},
		           		  {text: '案例归属四级部门',dataIndex: 'user_dept_level4',width : 150},
		           		  {text: '案例归属五级部门',dataIndex: 'user_dept_level5',width : 150},
	                      {text: '成本承担人',dataIndex: 'fycdrxm',width : 100},
	                      {text: '承担人工号',dataIndex: 'fycdrworkcode',width : 100}
                       ]
            
            },
    		{text: '费用科目',dataIndex: 'kmmc',width : 150, editor:'textfield'},
            {text: '金额',dataIndex: 'mx1bxje',width : 150},
            {
                text: '财务操作细节部分',//这个就是合并的
                columns: [{text: '财务出纳操作日期',dataIndex: 'operatedate',width : 150,renderer: Ext.util.Format.dateRenderer('Y-m-d')},
	              		  {text: '财务出纳操作情况',dataIndex: 'isremark',width : 150},
		           		  {text: '财务出纳意见',dataIndex: 'cwcnyj',width : 150},
                       ]
            
            },
            {text: '流程申请日期',dataIndex: 'sqrq',width : 150, renderer: Ext.util.Format.dateRenderer('Y-m-d')},
            {text: '说明',dataIndex: 'bxsm',width : 500,
            	renderer : function(value, cellmeta, record,
						rowIndex, columnIndex, store) {
					var str = value;
					if (value.length > 150) {
						str = value.substring(0, 150) + "...";
					}
					return "<span title='" + value + "'>" + str
							+ "</span>";
				}
            },{
                text: '辅助参数',//这个就是合并的
                columns: [{text: '成本承担旧事业部',dataIndex: 'ztejmc',width : 150},
	              		  {text: '提单人',dataIndex: 'tdrxm',width : 150},
		           		  {text: '提单人工号',dataIndex: 'tdrworkcode',width : 150},
                       ]
            
            },
        ];

        me.dockedItems = [{
        	style : {
    	        borderTopWidth : '0px !important',
    	        borderBottomWidth : '0px !important'
    	 		},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [djbh,kmmc,user_dept_level,sqrxm,tdrxm]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [operatedate_start,operatedate_end,sqrq_start,sqrq_end,{
                text:'查询',
                iconCls:'Find',
                handler:me.onSearch
            }]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [{
                text : '导出',
                iconCls : 'Pageexcel',
                handler : me.onExport
            },{
    			xtype : 'label',
    			forId : 'myFieldId',
    			margin:'10px',
    			style:"color:red",
    			text : '注意：备用金费用类型修改无效'
    		}]
		}];
        me.callParent(arguments);
    },
    onExport:function(){
        window.location.href = "statistics/financeOA/exportFinanceOA.do?"+FinanceOA
    },
    onSearch : function() {
        var me = this.up("gridpanel");
        me.getStore().currentPage=1;
        me.getStore().load();
    },
    listeners:{
        'afterrender':function(){
            this.store.load();
        },
        'beforeedit':function(editor, e){
        	if(!('subo_meiy' == usercode || 'subo_wangxuan' == usercode  || 'subo_wudc'==usercode))
				return false;
			rowEditing.on('edit',afterEdit);
		}
    }
});

function afterEdit(e,s){
	Ext.MessageBox.wait('正在操作','请稍后...');
  	Ext.Ajax.request({  
		url:"statistics/financeOA/updateOAdept.do", 
		method: "POST",
		headers: { 'Content-Type': 'application/json' },
		jsonData: {
			case_id:s.record.data.case_id,
			ztsybmc:s.record.data.ztsybmc,
			kmmc:s.record.data.kmmc
		}, 
		success: function (response, options) {  
			response = Ext.JSON.decode(response.responseText); 
             if (response.success==false) {  
                 Ext.MessageBox.alert("错误信息", "修改失败，请重试或联系管理员!");
             }else{
            	 Ext.MessageBox.alert("提示信息", "操作成功!");
             }
		},  
		failure: function () {
			Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
		}
  	});
}