/**
 * 报告类型
 * 
 * @author chenwei
 */
Ext.define("Rds.judicial.panel.JudicialReportTypeGridPanel",{
	extend : 'Ext.tree.Panel',
	collapsible: true,  
    useArrows: true,  
    rootVisible: false,  
    multiSelect: true,  
//    singleExpand: true, 
	initComponent : function() {
		var me = this;
		me.store = Ext.create('Ext.data.TreeStore',{
			fields:['typeid','typename','inputform','displaygrid','identify','parentid','leaf','status','sort'],
			
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'judicial/reporttype/queryall.do',
                autoLoad:true,
                params : {  
                	parentid : "0" 
                },
                reader: {
                    type: 'json'
                }
            },
            listeners:{
				"beforeload":function(ds, operation, opt){
					var parentcode = operation.node.get("typeid");
					if(parentcode==null||parentcode==""){
						parentcode = '0';
					}
					Ext.apply(me.store.proxy.params, {parentid:parentcode});
				}
            }, 

            folderSort: true  
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode: 'SINGLE'
		});
		
		me.columns = [//Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'4%', menuDisabled:true}),
		              { xtype: 'treecolumn', text: '类型名称',	dataIndex: 'typename',width:'10%', menuDisabled:true, flex: 2 },
		              { text: '录入表单',	dataIndex: 'inputform',width:'30%', menuDisabled:true, flex: 3},
		              { text: '展示表格',	dataIndex: 'displaygrid',width:'30%', menuDisabled:true, flex: 3},
		              { text: '报告字样', 	dataIndex: 'identify',width:'8%', menuDisabled:true, flex: 1},
		              { text: '排序', 	dataIndex: 'sort',width:'8%', menuDisabled:true, flex: 1}];

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
		var form = Ext.create("Rds.judicial.panel.JudicialReportTypeFormPanel",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'新增类型',
			width:400,
			iconCls:'Add',
			height:325,
			layout:'border',
			items:[form]
		});
		win.show();
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length>0){
			var record = selections[0];
			form.down("textfield[name=parentid]").setValue(record.get("typeid"));
		}
	},
	onDelete:function(){
		var me = this.up("treepanel");
		var selection =  me.getView().getSelectionModel().getSelection()[0];
		var values = {
				id:"\'"+selection.get("typeid")+"'"
		};
		Ext.Ajax.request({  
			url:"judicial/reporttype/delete.do", 
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
	},
	onUpdate:function(){
		var me = this.up("treepanel");
		var selection =  me.getView().getSelectionModel().getSelection()[0];
		var form = Ext.create("Rds.judicial.panel.JudicialReportTypeFormPanel",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'修改类型',
			width:400,
			iconCls:'Pageedit',
			height:325,
			layout:'border',
			items:[form]
		});
		win.show();
		
		console.log(selection);
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
			layout:'border',
			items:[form]
		});
		win.show();
		form.loadRecord(selection);
	}
	
});