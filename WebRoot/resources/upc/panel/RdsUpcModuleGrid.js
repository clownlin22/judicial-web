/**
 * @description 菜单管理
 * @author yxb
 * @date 20150504
 */
Ext.define('Rds.upc.panel.RdsUpcModuleGrid', {
	extend : 'Ext.tree.Panel',
	collapsible: true,  
    useArrows: true,  
    rootVisible: false,  
    multiSelect: true,  
	initComponent : function() {
		var me = this;
		me.store = Ext.create('Ext.data.TreeStore',{
			fields:['modulecode','modulename','moduleico','moduleurl','moduletype','modulesqe','moduleparentcode','moduledesc','leaf'],
			
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/module/queryAllByParent.do',
                autoLoad:true,
                params : {  
                	parentcode : "0" ,
                	module:"0"
                },
                reader: {
                    type: 'json'
                }
            },
            listeners:{
				"beforeload":function(ds, operation, opt){
					var parentcode = operation.node.get("modulecode");
					if(parentcode==null||parentcode==""){
						parentcode = '0';
					}
					Ext.apply(me.store.proxy.params, {parentcode:parentcode});
				}
            }, 

            folderSort: true  
		});
		
//		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
//		});
		me.columns = [
	              { xtype: 'treecolumn', text: '模版名称',	dataIndex: 'modulename',width:200, menuDisabled:true},
                  { text: '模版URL',	dataIndex: 'moduleurl',width:300, menuDisabled:true},
                  { text: '排序', 	dataIndex: 'modulesqe',width:80, menuDisabled:true},
                  { text: '描述', 	dataIndex: 'moduledesc',width:200, menuDisabled:true}
	              ];

		me.dockedItems = [{
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
		
		me.callParent(arguments);
	},
	onInsert:function(){
		var me = this.up("treepanel");
		var form = Ext.create("Rds.upc.panel.RdsUpcModuleForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'模版——新增',
			width:350,
			iconCls:'Add',
			modal:true,
			height:350,
			layout:'border',
			items:[form]
		});
		win.show();
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length>0){
			var record = selections[0];
			form.down("textfield[name=moduleparentcode]").setValue(record.get("modulecode"));
		}
	},
	onDelete:function(){
		var me = this.up("treepanel");
		var modulecodes='';
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
			return;
		};
		var selection =  me.getView().getSelectionModel().getSelection()[0];
		var values = {
				modulecode:selection.get("modulecode")
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
			if("yes"==btn)
			{
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({  
					url:"upc/module/delete.do", 
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
		var me = this.up("treepanel");
		var selection =  me.getView().getSelectionModel().getSelection()[0];
		var form = Ext.create("Rds.upc.panel.RdsUpcModuleForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'模版——修改',
			width:350,
			iconCls:'Pageedit',
			height:350,
			modal:true,
			layout:'border',
			items:[form]
		});
		win.show();
		form.loadRecord(selection);
	},
	onSearch:function(){
		var me = this;
		var selection =  me.getView().getSelectionModel().getSelection()[0];
		var form = Ext.create("Rds.upc.panel.RdsUpcModuleForm",{
			region:"center"
		});
		var win = Ext.create("Ext.window.Window",{
			title:'模版——修改',
			width:400,
			iconCls:'Pageedit',
			height:500,
			modal:true,
			layout:'border',
			items:[form]
		});
		win.show();
		form.loadRecord(selection);
	}
	
});