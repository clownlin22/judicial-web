//初始化grid，分页查询企业信息
Ext.define('Rds.upc.panel.RdsUpcCompanyGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var search = Ext.create('Ext.form.field.Text',{
            	name:'search',
            	labelWidth:60,
            	width:200,
            	fieldLabel:'企业名称'
            });
		var companycode = Ext.create('Ext.form.field.Text',{
        	name:'companycode',
        	labelWidth:60,
        	width:200,
        	fieldLabel:'企业编号'
        });
		me.store = Ext.create('Ext.data.Store',{
			fields:['companyid','companyname','status','companycode','address','telphone','contact','cratetime','laboratory_name','laboratory_no'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/company/queryallpage.do',
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
        			Ext.apply(me.store.proxy.params, {companyname:trim(search.getValue()),companycode:trim(companycode.getValue())});
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
		              Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'4%', menuDisabled:true}),
	              { text: '企业名称',	dataIndex: 'companyname',width:'10%', menuDisabled:true, flex: 2 },
                  { text: '企业编码',	dataIndex: 'companycode',width:'30%', menuDisabled:true, flex: 1},
                  { text: '地址', 	dataIndex: 'address',width:'8%', menuDisabled:true, flex: 3},
                  { text: '联系电话', 	dataIndex: 'telphone', menuDisabled:true, flex: 1},
                  { text: '联系人', 	dataIndex: 'contact', menuDisabled:true, flex: 1},
                  { text: '所属实验室', 	dataIndex: 'laboratory_name', menuDisabled:true, flex: 1},
                  { text: '当前状态', 	dataIndex: 'status',width:'18%', menuDisabled:true, flex: 1,
                	  renderer:function(value){
                		  switch(value)
                		  {
	                		  case '1':
	                			  return "正常";
	                			  break;
	                		  default:
	                			  return "异常";
                		  }
                	  }
                  }
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[search,companycode,{
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
	 			text:'删除',
	 			iconCls:'Delete',
	 			handler:me.onDelete
	 		}]
	 	}];
		
		me.store.load();
		
		me.callParent(arguments);
	},
	//新增
	onInsert:function(){
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.upc.panel.RdsUpcCompanyForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'企业—新增',
			width:400,
			iconCls:'Add',
			height:300,
			modal:true,
			layout:'border',
			items:[form]
		});
		win.show();
	},
	onDelete:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
			return;
		};
		var values = {
				companyid:selections[0].get("companyid")
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在操作','请稍后...');
	    		Ext.Ajax.request({  
	    			url:"upc/company/delete.do", 
	    			method: "POST",
	    			headers: { 'Content-Type': 'application/json' },
	    			jsonData: values, 
	    			success: function (response, options) {  
	    				response = Ext.JSON.decode(response.responseText); 
	                     if (response.result == true) {  
	                     	Ext.MessageBox.alert("提示信息", response.message);
	                     	me.getStore().load();
	                     }else { 
	                     	Ext.MessageBox.alert("错误信息", response.message);
	                     } 
	    			},  
	    			failure: function () {
	    				Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
	    			}
	        	       
	          	});
	        }
	    });
	},
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
			return;
		};
		var form = Ext.create("Rds.upc.panel.RdsUpcCompanyUpdateForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'企业—修改',
			width:400,
			modal:true,
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
	}
	
});