/**
 * @description 角色管理
 * @author yxb
 * @date 20150423
 */

Ext.define('Rds.upc.panel.RdsUpcRoleGrid', {
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
//            	xtype:'textfield',
            	name:'search',
            	labelWidth:60,
            	width:200,
            	fieldLabel:'角色名称'
            });
		me.store = Ext.create('Ext.data.Store',{
			fields:['roleid','rolename','roletype','permitname'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/role/queryallpage.do',
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
        			Ext.apply(me.store.proxy.params, {rolename:search.getValue()});
        		}
            }
		});
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
		});
		me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录"
        });
		me.columns = [
		              Ext.create("Ext.grid.RowNumberer",{text: '序号',width : 40, menuDisabled:true}),
	              { text: '角色名称',	dataIndex: 'rolename', menuDisabled:true, width : 120 },
                  { text: '角色编码',	dataIndex: 'roletype', menuDisabled:true, width : 70},
                  { text: '角色权限', 	dataIndex: 'permitname', menuDisabled:true,flex:1}
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
	 		},{
	 			text:'权限配置',
	 			iconCls:'Cog',
	 			handler:me.onPermitConfig
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	onPermitConfig:function(){
		var me = this.up("gridpanel");
		var ss =  this.up("gridpanel").getView().getSelectionModel().getSelection();
		if(ss.length<1){
			Ext.MessageBox.alert("提示信息", '请选择需要角色!');
			return;
		}
		else if(ss.length>1)
		{
			Ext.MessageBox.alert("提示信息", '配置不过来啦!');
			return;
		}
		var roletypes = ss[0].get("roletype");
		var selModel = Ext.create('Ext.selection.CheckboxModel',{
		});
		var columns = [Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'8%', menuDisabled:true}),
		               { text: '菜单名称',	dataIndex: 'modulename',flex:1, menuDisabled:true},
		               { text: '菜单URL',	dataIndex: 'moduleurl',flex:2, menuDisabled:true}
		              ];
		var store = Ext.create('Ext.data.TreeStore', {  
			fields:['id','modulecode','modulename','moduleico','moduleurl','moduletype','modulesqe','moduleparentcode','moduledesc','leaf','checked'],
			proxy:{
				type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/module/queryAllByParent.do',
                reader:'json',
                autoLoad:true,
                params:{
                	parentcode : "0" ,
                	roletypes:roletypes
                },
                clearOnLoad : true
			}
		}); 
		var grid=Ext.create("Ext.tree.Panel",{
			region:'center',
			columns:[{
				xtype:'treecolumn',
				text:'权限名字',
				dataIndex:'modulename',
				width:150,
				sortable:true
			},{
				text:'权限url',
				dataIndex:'moduleurl',
				width:250,
				flex:1,
				sortable:true
			}],
			store:store,
			rootVisible: false,
			 listeners: {
			      //监听复选框的选中属性改变事件
			         checkchange : function(node,checked){
			        //获得父节点
			        pNode = node.parentNode;
			        //改变当前节点的选中状态
			        node.checked = checked;
			        //判断当前节点是否为叶子节点
			        var isLeaf = node.isLeaf();
			        //当该节点有子节点时,将所有子节点的选中状态同化
			        if (!isLeaf){
			          //cascade是指从当前节点node开始逐层下报，即遍历当前节点的每一个节点(无论有多少层级结构,详参API)
			          node.cascade(function(node){
			            node.set("checked", checked);
			          });
			        }
			        //如果当前节点是选中状态
			        if (checked == true) {
			          //将当前节点的所有未选中的父节点选中
			          for (;pNode != null && !pNode.get("checked");pNode = pNode.parentNode) {
			            pNode.set("checked", true);
			          }
			        }else{
			          //取消当前节点的所有不包含选中的子节点的父节点的选中状态
			          for (;pNode != null;pNode = pNode.parentNode) {	
			            //如果当前的父节点包含选中的子节点，则终止搜索过程
			            if(hasCheckedNode(pNode)){
			              break;
			            }else{
			              //否则取消当前父节点的选中状态
			              pNode.set("checked", false);
			            }
			          }
			        }
			      }
			        }
		});
		 function hasCheckedNode(node){
			    var has = false;
			    //当前节点是否含有子节点
			    if(node.childNodes){
			      //遍历子节点
			      Ext.each(node.childNodes, function(item){
			        //如果当前节点是叶子节点
			        if(item.isLeaf()){
			          //遇到选中的子节点时终止搜索过程
			          if(has = item.get("checked")){
			            return false;
			          }
			        //遇到含有选中的子节点的父节点时终止搜索过程
			        }else if(has = hasCheckedNode(item)){
			          return false;
			        }
			      });
			    }
			    return has;
			  };
			  grid.expandAll();
		var win = Ext.create("Ext.window.Window",{
			title:'权限配置',
			width:600,
			height:410,
			modal:true,
			layout:'border',
			items:[grid],
			buttons:[{
				text:'刷新',
				handler:function(){
					store.reload();
				}
			},{
				text:'保存',
				handler:function(){
					var modulecodes='';
					var selNodes = grid.getChecked();
					Ext.each(selNodes, function(node){
						var id = node.id.split('-')[3];
						if(id!='root')
						{
							modulecodes += id+",";
						}
					});
					modulecodes = modulecodes.substring(0,(modulecodes.length-1));
					Ext.MessageBox.wait('正在操作','请稍后...');
					Ext.Ajax.request({  
						url:"upc/role/saveRole.do", 
						method: "POST",
						headers: { 'Content-Type': 'application/json' },
						jsonData: {
							roletypes:roletypes,
							modulecodes:modulecodes
						}, 
						success: function (response, options) {  
							response = Ext.JSON.decode(response.responseText); 
			                 if (response.result == true) {  
			                 	Ext.MessageBox.alert("提示信息", response.message);
			                 	me.getStore().load();
			                 	win.close();
			                 }else { 
			                 	Ext.MessageBox.alert("错误信息", response.message);
			                 } 
						},  
						failure: function () {
							Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
						}
			    	       
			      	});
				}
			}]
		});
		win.show();
	},
	//新增
	onInsert:function(){
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.upc.panel.RdsUpcRoleForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'角色—新增',
			width:300,
			iconCls:'Add',
			height:180,
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
		var roleids = '';
		var roletype='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			roleids += "'" + selections[i].get("roleid") +"',"
			roletype += "'" + selections[i].get("roletype") +"',"
			
		}
		roleids = roleids.substring(0,(roleids.length-1));
		roletype = roletype.substring(0,(roletype.length-1));
		var values = {
				roleid:roleids,
				roletype:roletype
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
			if("yes" == btn)
			{
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({  
					url:"upc/role/delete.do", 
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
		var form = Ext.create("Rds.upc.panel.RdsUpcRoleForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'角色—修改',
			width:300,
			iconCls:'Pageedit',
			height:180,
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