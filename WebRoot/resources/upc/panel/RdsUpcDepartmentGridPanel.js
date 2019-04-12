Ext.define('Rds.upc.panel.RdsUpcDepartmentGridPanel', {
	extend : 'Ext.tree.Panel',
	collapsible: true,  
    useArrows: true,  
    rootVisible: false,  
    multiSelect: true,  
	initComponent : function() {
		var me = this;
		var search = Ext.create('Ext.form.field.Text',{
//        	xtype:'textfield',
        	name:'search',
        	labelWidth:60,
        	width:200,
        	fieldLabel:'部门名称'
        });
		var deptcode = Ext.create('Ext.form.field.Text',{
        	name:'deptcode',
        	labelWidth:60,
        	width:200,
        	fieldLabel:'部门编号'
        });
		//公司列表数据源
		var companyStore = Ext.create('Ext.data.Store',{
    	    fields:['companyid','companyname'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'upc/company/queryall.do',
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
		var companyCombo = new Ext.form.ComboBox({
			autoSelect : true,
			editable:false,
			fieldLabel:'所属企业',
			labelWidth:60,
	        name:'companyid',
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
	        selectOnTab: true,
	        store: companyStore,
	        fieldStyle: me.fieldStyle,
	        displayField:'companyname',
	        valueField:'companyid',
	        listClass: 'x-combo-list-small',
		});
		me.store = Ext.create('Ext.data.TreeStore',{
			fields:['deptid','deptname','parentid','parentname','deptcode','leaf','parentdeptcode','companyname','companyid','islaboratory'],
			
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/department/queryall.do',
                autoLoad:true,
                params : {  
                	parentcode : "0",
//                	companyid:usercode==_super?null:companyid
                },
                reader: {
                    type: 'json'
                }
            },
            listeners:{
				"beforeload":function(ds, operation, opt){
					var parentcode = operation.node.get("deptid");
					if(parentcode==null||parentcode==""){
						parentcode = '0';
					}
					Ext.apply(me.store.proxy.params, {deptname:search.getValue(),
													  parentcode:parentcode,
													  companyid:companyCombo.getValue(),
													  deptcode:deptcode.getValue()
													  });
				}
            }, 
//            folderSort: true  
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			//mode: 'SINGLE'
		});
		
		me.columns = [
		              Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'4%', menuDisabled:true}),
	              { xtype: 'treecolumn', text: '部门名称',	dataIndex: 'deptname',width:'10%', menuDisabled:true, flex: 2 },
	              { text: '直属部门',	dataIndex: 'parentname',width:'10%', menuDisabled:true, flex: 2},
                  { text: '部门编号',	dataIndex: 'deptcode',width:'10%', menuDisabled:true, flex: 1},
                  { text: '所属企业',	dataIndex: 'companyname',width:'10%', menuDisabled:true, flex: 2},
                  { text: '父节点',	dataIndex: 'parentid' ,width:0}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[search,companyCombo,deptcode,{
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
		
		me.callParent(arguments);
	},
	onInsert:function(){
		var me = this.up("treepanel");
		var form = Ext.create("Rds.upc.panel.RdsUpcDepartmentFormPanel",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'新增',
			width:400,
			modal:true,
			iconCls:'Add',
			height:250,
			layout:'border',
			items:[form]
		});
		win.show();
		var selections =  me.getView().getSelectionModel().getSelection();
		var parentid = 0;
		if(selections.length>0){
			var record = selections[0];
			var parentdeptcode = record.get("parentdeptcode");
			var deptcode = record.get("deptcode");
			form.down("textfield[name=parentdeptcode]").setValue(deptcode);
			form.down("textfield[name=companyid]").setValue(record.get("companyname"));
			form.down("textfield[name=companyidtemp]").setValue(record.get("companyid"));
			form.down("textfield[name=companyid]").disabled=true;
			parentid = record.get("deptid");
		}else
		{
			form.down("textfield[name=companyid]").setValue();
		}
		form.down("textfield[name=parentid]").setValue(parentid);
	},
	onDelete:function(){
		var me = this.up("treepanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
			return;
		};
		var ids = '';
		for(var i=0;i<selections.length;i++){
			var record = selections[i];
			ids += record.get("deptid");
			if(i!=selections.length-1){
				ids +=",";
			}
		}
		console.log(ids);
		var values = {
				ids:ids
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在操作','请稍后...');
	        	Ext.Ajax.request({  
	    			url:"upc/department/delete.do", 
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
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.MessageBox.alert("提示", "请选择一条记录修改");
			return ;
		}
		if(selections.length>1){
			Ext.MessageBox.alert("提示", "只能选择一条记录修改");
			return ;
		}
		var form = Ext.create("Rds.upc.panel.RdsUpcDepartmentFormPanel",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'修改',
			width:400,
			iconCls:'Pageedit',
			modal:true,
			layout:'border',
			height:250,
			items:[form]
		});
		win.show();
		form.loadRecord(selections[0]);
		var record = selections[0];
		var parentid = record.get("parentid");
		form.down("textfield[name=companyidtemp]").setValue(record.get("companyid"));
		if(parentid!=0)
		{
			form.down("textfield[name=companyid]").disabled=true;
		}

	},
	onSearch:function(){
		var me = this.up("treepanel");
		me.getStore().load({params:{start:0}});
		
	}
});