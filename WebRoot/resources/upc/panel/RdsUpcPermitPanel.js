/**
 * @description 权限管理
 * @author yxb
 * @date 20150427
 */

Ext.define('Rds.upc.panel.RdsUpcPermitPanel', {
	extend : 'Ext.panel.Panel',
	name:'permit',
	layout:'border',
	initComponent : function() {
		var me = this;
		me.items = [me.centerPanel(),me.westPanel()];
		
		me.callParent(arguments);
	},
	centerPanel:function(){
		var me = this;
		var dockedItems = [{
	 		xtype:'toolbar',
	 		dock:'top',
	 		items:[{
            	xtype:'textfield',
            	name:'search',
            	labelWidth:60,
            	width:200,
            	fieldLabel:'权限名称'
            },{
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
		var store = Ext.create('Ext.data.Store',{
			fields:['permitcode','permitname','permitdesc'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/permit/queryall.do',
                params:{
                	companyid:companyid
                },
                reader: {
                    type: 'json',
                    root:'data',
                    totalProperty:'total'
                }
            }
		});
		
		store.load();
		var selModel = Ext.create('Ext.selection.CheckboxModel',{
			model:'single'
		});
		
		var columns = [Ext.create("Ext.grid.RowNumberer",{text: '序号',flex:.2, menuDisabled:true}),
		               { text: '编号', 	dataIndex: 'permitcode',flex:1, menuDisabled:true},
		               { text: '名称',	dataIndex: 'permitname',flex:2, menuDisabled:true},
		               { text: '描述',	dataIndex: 'permitdesc',flex:1, menuDisabled:true}
		              ];
		
		var grid = Ext.create("Ext.grid.Panel",{
			title:'权限管理',
			store:store,
			dockedItems:dockedItems,
			selModel:selModel,
			columns:columns,
			region:'center',
			listeners:{
				'itemclick':function(grid,record, item, index, e, eOpts){
					var permitcode = record.get("permitcode");
					var treepanel = me.down("treepanel");
					var store = treepanel.getStore();
					Ext.apply(store.proxy.params, {parentcode:'0',permitcode:permitcode});
					store.load();
				}
			}
		});
		
		store.on("beforeload",function(ds, operation, opt){
			var value = grid.down("textfield[name=search]").getValue();
			Ext.apply(store.proxy.params, {permitname:value});
		});
		
		return grid;
	},
	onSearch : function(){
		var grid = this.up("gridpanel");
		grid.getStore().load({params:{start:0}});
	},
	onDelete:function(){
		var grid = this.up("gridpanel");
		var selections =  grid.getView().getSelectionModel().getSelection();
		var length = selections.length;
		if(length<1){
			Ext.MessageBox.alert("提示","请选择需要删除的行");
			return;
		}
		var permitcode = "";
		Ext.each(selections,function(item,index){
			permitcode += "'";
			permitcode += item.get("permitcode");
			permitcode += "'";
			if(index<length-1){
				permitcode += ',';
			}
		});
		Ext.Ajax.request({  
			url:"upc/permit/delete.do", 
			method: "POST",
			headers: { 'Content-Type': 'application/json' },
			jsonData: {
				permitcode:permitcode
			}, 
			success: function (response, options) {  
				response = Ext.JSON.decode(response.responseText); 
                 if (response.result == true) {  
                 	Ext.MessageBox.alert("提示信息", response.message);
                 	grid.getStore().load();
                 	grid.up("window").close();
                 }else { 
                 	Ext.MessageBox.alert("错误信息", response.message);
                 } 
			},  
			failure: function () {
				Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
			}
    	       
      	});
	},
	onInsert:function(){
		var grid = this.up("gridpanel");
		var win = Ext.create("Ext.window.Window",{
			title:'权限-添加',
			width:400,
			height:180,
			layout:'border'
		});
		var form = Ext.create("Rds.upc.panel.RdsUpcPermitForm",{
			region:'center',
			grid:grid
		});
		win.add(form);
		win.show();
	},
	
	onUpdate:function(){
		var grid = this.up("gridpanel");
		var selections =  grid.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.MessageBox.alert("提示","请选择需要修改的记录");
			return;
		}
		var win = Ext.create("Ext.window.Window",{
			title:'权限-修改',
			width:400,
			height:300,
			layout:'border'
		});
		var form = Ext.create("Rds.upc.panel.RdsUpcPermitForm",{
			region:'center',
			grid:grid
		});
		form.getForm().loadRecord(selections[0]);
		win.add(form);
		win.show();
	},
	westPanel:function(){
		var me = this;
		var store = Ext.create('Ext.data.TreeStore',{
			fields:['id','text','checked','leaf'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/permit/queryPermitModelByParentCode1.do',
                params : {  
                	parentcode : "0" ,
                	permitcode : ''
                },
                reader: {
                    type: 'json'
                }
            },
            listeners:{
				"beforeload":function(ds, operation, opt){
					var parentcode = operation.node.get("id");
					if(parentcode==null||parentcode==""||parentcode=="root"){
						parentcode = '0';
					}
					Ext.apply(store.proxy.params, {parentcode:parentcode});
				}
            }, 
            folderSort: true  
		});
		
		store.load();
		var selModel = Ext.create('Ext.selection.CheckboxModel',{
		});
		
		var columns = [
	              { xtype: 'treecolumn', text: '模版名称',	dataIndex: 'text', menuDisabled:true, flex: 2 }
	              ];

		var dockedItems = [{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[{
	 			text:'保存',
	 			iconCls:'Pagesave',
	 			handler:me.onSave
	 		}]
	 	}];
		var tree =  Ext.create("Ext.tree.Panel",{
			width:300,
			title:'资源配置',
			region:'east',
			store:store,
			collapsible: true,  
		    useArrows: true,  
		    rootVisible: false,  
		    multiSelect: true,  
			dockedItems:dockedItems,
			columns:columns
		});
		return tree
	},
	onSave:function(){
		var tree = this.up("treepanel");
		var checked = tree.getChecked();
		var grid = this.up("panel[name=permit]").down("gridpanel");
		var selections =  grid.getView().getSelectionModel().getSelection();
		if(selections.length==0){
			Ext.MessageBox.alert("提示","请选择需要权限记录");
			return;
		}
		var permitcode = selections[0].get("permitcode");
		
		var values = new Array();
		Ext.each(checked,function(item){
			if(item.data.leaf){
				values.push({
					permitcode:permitcode,
					modulecode:item.data.id
				});
			}
		});
		
		Ext.Ajax.request({  
			url:"upc/permit/savepermitmodule.do", 
			method: "POST",
			headers: { 'Content-Type': 'application/json' },
			jsonData: {
				permitcode:permitcode,
				data:values
			}, 
			success: function (response, options) {  
				response = Ext.JSON.decode(response.responseText); 
                 if (response.result == true) {  
                 	Ext.MessageBox.alert("提示信息", response.message);
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
