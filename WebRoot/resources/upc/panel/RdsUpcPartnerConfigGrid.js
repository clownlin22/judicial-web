//初始化grid，分页查询企业信息
Ext.define('Rds.upc.panel.RdsUpcPartnerConfigGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var parnter_name = Ext.create('Ext.form.field.Text',{
            	name:'parnter_name',
            	labelWidth:60,
            	width:200,
            	fieldLabel:'合作商'
            });
		var areaname = Ext.create('Ext.form.field.Text',{
        	name:'areaname',
        	labelWidth:60,
        	width:200,
        	fieldLabel:'地区'
        });
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','parnter_name','areaname','areacode','qualificationFee','remark','report_model','text'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/partnerConfig/queryallpage.do',
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
        				parnter_name:trim(parnter_name.getValue()),
        				areaname:trim(areaname.getValue())
        				});
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
	              { text: '地区',	dataIndex: 'areaname',width:'10%', menuDisabled:true, flex: 1 },
	              { text: '模版',	dataIndex: 'text',width:'10%', menuDisabled:true, flex: 1 },
	              { text: '合作商',	dataIndex: 'parnter_name',width:'10%', menuDisabled:true, flex:1 },
	              { text: '资质费',	dataIndex: 'qualificationFee',width:'10%', menuDisabled:true, flex:1 },
                  { text: '备注',	dataIndex: 'remark',width:'30%', menuDisabled:true, flex:2 }
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[areaname,parnter_name,{
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
		codeTemp='';
		var form = Ext.create("Rds.upc.panel.RdsUpcPartnerConfigForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'合作商—新增',
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
		if(selections.length < 1){
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
			return;
		};
		var ids = "";
		for(var i = 0 ; i < selections.length ; i ++)
		{
			ids+="'"+selections[i].get("id")+"',";
		}
		ids = ids.substring(0,ids.length-1);
		
		var values = {
				id:ids
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	    		Ext.Ajax.request({  
	    			url:"upc/partnerConfig/delete.do", 
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
		codeTemp= selections[0].get("areacode");
		var form = Ext.create("Rds.upc.panel.RdsUpcPartnerConfigForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'合作商—修改',
			width:400,
			iconCls:'Pageedit',
			height:300,
			modal:true,
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