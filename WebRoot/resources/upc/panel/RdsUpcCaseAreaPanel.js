Ext.define('Rds.upc.panel.RdsUpcCaseAreaPanel', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    selType: 'rowmodel',
    plugins: [ Ext.create('Ext.grid.plugin.RowEditing', {
        clicksToEdit: 1,
        listeners:{  
            'edit':function(e,s){  
          	Ext.MessageBox.wait('正在保存','请稍后...');
            	Ext.Ajax.request({  
					url:"judicial/area/updateUpcAreaInfo.do", 
					method: "POST",
					headers: { 'Content-Type': 'application/json' },
					jsonData: {
						achieve:s.record.data.achieve,
						address:s.record.data.address,
						print_copies:s.record.data.print_copies,
						phone:s.record.data.phone,
						areaname:s.record.data.areaname,
						areacode:s.record.data.areacode
					}, 
					success: function (response, options) {  
						response = Ext.JSON.decode(response.responseText); 
		                 if (response.result==false) {  
			                 Ext.MessageBox.alert("错误信息", response.message+"，修改无效");
		                 }else{
		                	 Ext.MessageBox.alert("提示信息", response.message);
		                 }
					},  
					failure: function () {
						Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
					}
		      	});
            }  
          }  
    })
],
    //初始化
	initComponent : function() {
		var me = this;
		var areaname = Ext.create('Ext.form.field.Text',{
        	name:'areaname',
        	labelWidth:60,
        	width:200,
        	fieldLabel:'地区名称'
        });
		var achieve = Ext.create('Ext.form.field.Text',{
        	name:'achieve',
        	labelWidth:60,
        	width:200,
        	fieldLabel:'收件人'
        });
		var address = Ext.create('Ext.form.field.Text',{
        	name:'address',
        	labelWidth:60,
        	width:200,
        	fieldLabel:'地址'
        });
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','areaname','print_copies','address','areacode','achieve','phone'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'judicial/area/getUpcAreaInfo.do',
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
        			Ext.apply(me.store.proxy.params, {
        				areaname:trim(areaname.getValue()),
        				achieve:trim(achieve.getValue()),
        				address:trim(address.getValue())});
        		}
            }
		});
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
		});
		me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录"
        });
		
		me.columns = [Ext.create("Ext.grid.RowNumberer",
				  {text: '序号', menuDisabled:true,width:40}),
		          { text: '地区',dataIndex: 'areaname',menuDisabled:true,width:300 },
	              { text: '收件人',dataIndex: 'achieve',menuDisabled:true,width:100,editor:'textfield' },
	              { text: '联系电话',dataIndex: 'phone',menuDisabled:true,width:100,editor:'textfield' },
	              { text: '地址',	dataIndex: 'address', menuDisabled:true,width:120,editor:'textfield'},
	              { text: '可打印份数',	dataIndex: 'print_copies', menuDisabled:true,width:120,editor:new Ext.form.NumberField()}
	              ];
		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[areaname,achieve,address,{
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
		var form = Ext.create("Rds.upc.panel.RdsUpcCaseAreaForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'案例归属地—新增',
			width:410,
			modal:true,
			iconCls:'Add',
			height:320,
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
		var usercode='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			usercode+="'"+selections[i].get("usercode")+"',";
		}
		usercode = usercode.substring(0,usercode.length-1);
		var values = {
				usercode:usercode
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
			if("yes" == btn)
			{
				Ext.Ajax.request({  
					url:"upc/user/delete.do", 
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
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	}
	
});
