Ext.define("Rds.judicial.panel.JudicialSamplingManageGridPanel",{
	extend:"Ext.grid.Panel",
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var agentstore = Ext.create('Ext.data.Store',{
    	    fields:['key','value'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'judicial/agent/queryUserByType.do',
    	        params:{
    	        	roletype:'103'
    	        },
    	        render:{
    	            type:'json'
    	        },
    	        writer: {
    	            type: 'json'
    	       }
    	    }
    	});
		var marketstore = Ext.create('Ext.data.Store',{
    	    fields:['key','value'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'judicial/agent/queryUserByType.do',
    	        params:{
    	        	roletype:'102'
    	        },
    	        render:{
    	            type:'json'
    	        },
    	        writer: {
    	            type: 'json'
    	       }
    	    }
    	});
		var agentCombo = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			fieldLabel:'市场人员',
	        name:'userid',
	        triggerAction: 'all',
	        labelWidth:70,
	        queryMode: 'local', 
	        emptyText : "请选择",
	        selectOnTab: true,
	        store: agentstore,
	        fieldStyle: me.fieldStyle,
	        displayField:'value',
	        valueField:'key',
	        listClass: 'x-combo-list-small',
		});
		var marketCombo = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			labelWidth:60,
			fieldLabel:'采样员',
	        name:'peruserid',
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
	        selectOnTab: true,
	        store: marketstore,
	        fieldStyle: me.fieldStyle,
	        displayField:'value',
	        valueField:'key',
	        listClass: 'x-combo-list-small',
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','userid','peruserid','remark','username','perusername'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'judicial/agent/queryAgent.do',
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
        					userid:agentCombo.getValue(),
        					peruserid:marketCombo.getValue(),
        					flag:2
						})
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
	                  { text: '采样员',	dataIndex: 'perusername', menuDisabled:true, flex: .6},
	                  { text: '市场人员',	dataIndex: 'username', menuDisabled:true, flex: .6},
	                //  { text: '备注',	dataIndex: 'remark', menuDisabled:true, flex: .6},
	                
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[marketCombo,agentCombo,{
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
	 		}
	 		,{
	 			text:'修改',
	 			iconCls:'Pageedit',
	 			handler:me.onUpdate
	 		}
	 		,{
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
		var form = Ext.create("Rds.judicial.panel.JudicialSamplingFormPanel",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'采样员管理—新增',
			width:400,
			iconCls:'Add',
			height:180,
			layout:'border',
			items:[form]
		});
		win.show();
	},
	onDelete:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		};
		var values = {
				id:selections[0].get("id")
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.Ajax.request({  
	    			url:"judicial/agent/delete.do", 
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
		var form = Ext.create("Rds.judicial.panel.JudicialSamplingFormPanel",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'采样员管理—修改',
			width:400,
			iconCls:'Pageedit',
			height:180,
			layout:'border',	
			items:[form]
		});
		win.show();
		form.loadRecord(selections[0]);
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.getStore().load();
		
	}
});
function dateformat(value) {
	if (value != null) {
		return Ext.Date.format(value, 'Y-m-d');
	} else {
		return '';
	}
}