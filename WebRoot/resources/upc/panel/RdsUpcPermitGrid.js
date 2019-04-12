Ext.define('Rds.upc.panel.RdsUpcPermitGrid', {
	extend : 'Ext.grid.Panel',
	initComponent : function() {
		var me = this;
		me.store = Ext.create('Ext.data.Store',{
			fields:['companyid','companyname','status','companycode','adress','telphone','contact','cratetime'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/company/querypage.do',
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
		me.bbar = new Ext.PagingToolbar({
			pageSize : 25,  // 一页显示25行
			store : me.store,  // 前面定义的store
	   	 	displayInfo : true, // 是否显示总体信息
	   	 	displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录"
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode: 'SINGLE'
		});
		
		me.columns = [
		              Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'4%', menuDisabled:true}),
		          { text: '模版编号', 	dataIndex: 'modulecode',width:'10%', menuDisabled:true},
	              { text: '模版名称',	dataIndex: 'modulename',width:'10%', menuDisabled:true},
	              { text: '父节点编号',	dataIndex: 'moduleparentcode',width:'10%', menuDisabled:true},
	              { text: '父节点',	dataIndex: 'moduleparentname',width:'10%', menuDisabled:true},
	              { text: '模版图标', 	dataIndex: 'moduleico',width:'8%', menuDisabled:true},
                  { text: '模版URL',	dataIndex: 'moduleurl',width:'30%', menuDisabled:true},
                  { text: '模版类型', 	dataIndex: 'moduletype', menuDisabled:true},
                  { text: '排序', 	dataIndex: 'modulesqe', menuDisabled:true},
                  { text: '描述', 	dataIndex: 'moduledesc',width:'18%', menuDisabled:true},
                  { text: '根节点',	dataIndex: 'isleaf',width:'14%', menuDisabled:true}
	              ];
		me.dockedItems = [{
	 		xtype:'toolbar',
	 		dock:'top',
	 		items:[{
            	xtype:'textfield',
            	name:'txm',
            	labelWidth:60,
            	width:200,
            	fieldLabel:'模版名称'
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
		
		me.callParent(arguments);
	},
	onInsert:function(){
		var me = this;
		var win = Ext.create("Ext.window.Window",{
			title:'模版——新增',
			width:400,
			iconCls:'Add',
			height:500,
			layout:'border',
			items:[Ext.create("Rds.upc.panel.RdsUpcModuleForm",{
				region:"center"
			})]
		});
		win.show();
	},
	onDelete:function(){
		var me = this;
		alert("delete");
	},
	onUpdate:function(){
		var me = this;
		var win = Ext.create("Ext.window.Window",{
			title:'模版——修改',
			width:400,
			iconCls:'Pageedit',
			height:500,
			layout:'border',
			items:[Ext.create("Rds.upc.panel.RdsUpcModuleForm",{
				region:"center"
			})]
		});
		win.show();
	},
	onSearch:function(){
		alert("search")
	}
	
});