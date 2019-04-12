/**
 * 字典名称对应值列表
 * 
 * @author chenwei
 */
Ext.define("Rds.judicial.panel.JudicialDicValuesGridPanel",{
	extend:"Ext.grid.Panel",
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var store = Ext.create('Ext.data.Store',{
			 fields:['id','keycode','keyname','status','sort'],
	    	    autoLoad:true,
	    	    proxy:{
	    	        type:'jsonajax',
	    	        actionMethods:{read:'POST'},
	    	        url:'judicial/dickeys/queryall.do',
	    	        params:{
	    	        },
	    	        render:{
	    	            type:'json'
	    	        },
	    	        writer: {
	    	            type: 'json'
	    	       }
	    	    }
		});
		var search = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			fieldLabel:'字典名称',
	        name:'search',
	        labelWidth:70,
	        width:200,
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
	        //readOnly:usercode==_super?false:true,
	        selectOnTab: true,
	        store: store,
	        fieldStyle: me.fieldStyle,
	        displayField:'keyname',
	        valueField:'keycode'
		});
//		var search = Ext.create("Ext.form.field.ComboBox",{
//			autoSelect : true,
//			editable:true,
//			store: store,
//        	name:'search',
//        	labelWidth:60,
//        	width:100,
//        	fieldLabel:'字典名称',
//        	displayField: 'keyname',
//        	valueField: 'keycode',
//        	fieldStyle: me.fieldStyle,
//        	selectOnTab: true,
//        	labelWidth:40,
//	        triggerAction: 'all',
//	        queryMode: 'local', 
//	        emptyText : "请选择"
//        });
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','keyid','keyvalue','keyname','keycode','status','sort'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'judicial/dicvalues/queryallpage.do',
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
        			Ext.apply(me.store.proxy.params, {search:search.getValue()});
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
//	              { text: 'id',	dataIndex: 'id',width:'10%', menuDisabled:true, flex: 2 },
                  { text: '字典key',	dataIndex: 'keyid',width:'30%', menuDisabled:true, flex: 2},
                  { text: '字典编码', 	dataIndex: 'keyname',width:'8%', menuDisabled:true, flex: 3},
                  { text: '字典值', 	dataIndex: 'keyvalue',width:'8%', menuDisabled:true, flex: 3},
                  { text: '当前状态', 	dataIndex: 'status',width:'18%', menuDisabled:true, flex: 1,
                	  renderer:function(value){
                		  switch(value)
                		  {
	                		  case '0':
	                			  return "正常";
	                			  break;
	                		  default:
	                			  return "废弃";
                		  }
                	  }
                  }
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[search,{
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
	onInsert:function(){
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.judicial.panel.JudicialDicValuesFormPanel",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'系统字典——新增',
			width:400,
			iconCls:'Add',
			height:190,
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
				id:selections[0].get("id")
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在操作','请稍后...');
	        	Ext.Ajax.request({  
	    			url:"judicial/dicvalues/delete.do", 
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
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		};
		var form = Ext.create("Rds.judicial.panel.JudicialDicValuesFormPanel",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'系统字典——修改',
			width:400,
			iconCls:'Pageedit',
			height:190,
			modal:true,
			layout:'border',	
			items:[form]
		});
		win.show();
		form.loadRecord(selections[0]);
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.getStore().load({params:{start:0}});
		
	}
});