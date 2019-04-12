//初始化grid，分页查询企业信息
Ext.define('Rds.bacera.panel.BaceraProgramListGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var program_name = Ext.create('Ext.form.field.Text',{
        	name:'program_name',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'项目名称'
        });
		var is_delete=new Ext.form.field.ComboBox({
			fieldLabel : '是否作废',
	    	width:'20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部',''],['是',2 ],['否',1 ] ]
					}),
			value : '',
			mode : 'local',
			name : 'is_delete'
		});
		var program_type = new Ext.form.ComboBox({
			editable:true,
			labelWidth : 80,
	    	width:'20%',
			fieldLabel : '项目类型',
	        name:'program_type',
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
	        selectOnTab: true,
	        store: new Ext.data.ArrayStore({
                fields: ['Name', 'Code'],
                data: [['医学检测项', '医学检测项'], ['医学检测项(武汉)', '医学检测项(武汉)'], ['医学检测项(转化医学)', '医学检测项(转化医学)'],
                       ['血清学筛查', '血清学筛查'],['肿瘤标志物', '肿瘤标志物'],['毒品检测', '毒品检测'],['地中海贫血', '地中海贫血'],
                       ['文书鉴定', '文书鉴定'],['肿瘤个体', '肿瘤个体'],['肿瘤易感基因', '肿瘤易感基因']]
            }),
	        fieldStyle: me.fieldStyle,
	        displayField:'Name',
	        valueField:'Code',
	        listClass: 'x-combo-list-small',
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','program_name','program_code','program_type','is_delete','create_time','create_per','create_pername','remark'],
	        start:0,
			limit:15,
			pageSize:15,
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/program/queryallpage.do',
                params:{
                },
                reader: {
                    type: 'json',
                    root:'data',
                    totalProperty:'total'
                }
            },
            listeners:{
            	'beforeload':function(ds, operation, opt){
            		me.getSelectionModel().clearSelections();
        			Ext.apply(me.store.proxy.params, {
        				program_name:program_name.getValue().trim(),
        				program_type:program_type.getValue(),
        				is_delete:is_delete.getValue()
        		})
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
	              data: [['15', 15], ['30', 30],['60', 60], ['80', 80], ['100', 100]]
	          }),
	          valueField: 'value',
	          displayField: 'text',
	          emptyText:15,
	          width: 50
	      });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

    	//添加下拉显示条数菜单选中事件
       combo.on("select", function (comboBox) {
       var pagingToolbar=Ext.getCmp('pagingbarProgram');
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
			id:'pagingbarProgram',
			store: me.store,
			pageSize:me.pageSize,
			displayInfo: true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录",
	   	 	items: ['-', '每页显示',combo,'条']
      });
		me.columns = [
	              { text: '项目类型',	dataIndex: 'program_type', menuDisabled:true, width : 150,
	                	  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var is_delete= record.data["is_delete"];
								if (is_delete == 2) {
									return "<div style=\"text-decoration: line-through;color: red;\">"
											+ value + "</div>"
								} else {
									return value;
								}

							}
	              },
                  { text: '项目名称',	dataIndex: 'program_name', menuDisabled:true, width : 120},
                  { text: '项目编码',	dataIndex: 'program_code', menuDisabled:true, width : 70},
                  { text: '创建时间',	dataIndex: 'create_time', menuDisabled:true, width : 150},
                  { text: '创建人员',	dataIndex: 'create_pername', menuDisabled:true, width : 80},
                  { text: '备注',	dataIndex: 'remark', menuDisabled:true, width : 500}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[program_name,program_type,is_delete,{
            	text:'查询',
            	iconCls:'Find',
            	handler:me.onSearch
            }]
	 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[{
	 			text:'添加',
	 			iconCls:'Add',
	 			handler:me.onInsert
	 		},{
	 			text:'修改',
	 			iconCls:'Pageedit',
	 			handler:me.onUpdate
	 		},{
	 			text:'作废',
	 			iconCls:'Cancel',
	 			handler:me.onCancel
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	//新增
	onInsert:function(){
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.bacera.form.BaceraProgramListForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'医学项目—新增',
			width:350,
			iconCls:'Add',
			height:300,
			modal:true,
			layout:'border',
			items:[form]
		});
		win.show();
	},
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
			return;
		};
		if(selections[0].get("is_delete") == '2')
		{
			Ext.Msg.alert("提示", "该记录已作废，不能修改!");
			return;
		}
		var form = Ext.create("Rds.bacera.form.BaceraProgramListForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'医学项目—修改',
			width:350,
			modal:true,
			iconCls:'Pageedit',
			height:300,
			layout:'border',	
			items:[form]
		});
		form.loadRecord(selections[0]);
		win.show();
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	},
	onCancel:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要作废的记录!");
			return;
		};
		if(selections[0].get("is_delete") == '2')
		{
			Ext.Msg.alert("提示", "该记录已作废!");
			return;
		}
		var values = {
				id:selections[0].get("id")
		};
		Ext.MessageBox.confirm("提示", "确认作废选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	    		Ext.Ajax.request({  
	    			url:"bacera/program/delete.do", 
	    			method: "POST",
	    			headers: { 'Content-Type': 'application/json' },
	    			jsonData: values, 
	    			success: function (response, options) {  
	    				response = Ext.JSON.decode(response.responseText); 
	                     if (response.result == true) {  
	                     	Ext.MessageBox.alert("提示信息", "作废成功！");
	                     	me.getStore().load();
	                     }else { 
	                     	Ext.MessageBox.alert("错误信息", "作废失败，请重试或联系管理员！");
	                     } 
	    			},  
	    			failure: function () {
	    				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
	    			}
	        	       
	          	});
	        }
	    });
	}
	
});
