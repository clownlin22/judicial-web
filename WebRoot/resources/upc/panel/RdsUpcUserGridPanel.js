/**
 * @description 人员管理
 * @author yxb
 * @date 20150420
 */

Ext.define('Rds.upc.panel.RdsUpcUserGridPanel', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    //初始化
	initComponent : function() {
		var me = this;
		//搜索栏用户名
		var username = Ext.create('Ext.form.field.Text',{
            	name:'username',
            	labelWidth:60,
            	emptyText : "登录名或真实名",
            	width:'20%',
            	fieldLabel:'用户名'
            });
		var receiver_area = Ext.create('Ext.form.field.Text',{
        	name:'receiver_area',
        	labelWidth:60,
        	emptyText : "管理地区",
        	width:'20%',
        	fieldLabel:'管理地区'
        });
		var parnter_name = Ext.create('Ext.form.field.Text',{
        	name:'parnter_name',
        	labelWidth:60,
        	emptyText : "合作商名",
        	width:'20%',
        	fieldLabel:'合作商'
        });
		var dept_name = Ext.create('Ext.form.field.Text',{
        	name:'dept_name',
        	labelWidth:40,
        	emptyText : "部门名称",
        	width:'20%',
        	fieldLabel:'部门'
        });
		var userTypeStore = Ext.create('Ext.data.Store',{
			 fields:['typeid','typecode','typename'],
	    	    autoLoad:true,
	    	    proxy:{
	    	        type:'jsonajax',
	    	        actionMethods:{read:'POST'},
	    	        url:'upc/user/queryusertype.do',
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
		//搜索栏用户类型
		var usertype = new Ext.form.ComboBox({
    			autoSelect : true,
    			labelWidth:60,
            	width:'20%',
    			fieldLabel:'用户类型',
    	        name:'usertype',
    	        emptyText : "请选择",
    	        editable:true,
    	        triggerAction: 'all',
    	        queryMode: 'local', 
    	        selectOnTab: true,
    	        store: userTypeStore,
    	        fieldStyle: me.fieldStyle,
    	        displayField:'typename',
    	        valueField:'typecode',
    	        listClass: 'x-combo-list-small'
    	   
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
			fieldLabel:'所属企业',
			labelWidth:60,
	        name:'companyid',
	        editable:true,
        	width:'20%',
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
		//角色sotre
		var roleStore = Ext.create('Ext.data.Store',{
    	    fields:['roletype','rolename'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'upc/user/queryRoleType.do',
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
		//角色列表
		var roleSerach = new Ext.form.ComboBox({
			autoSelect : true,
			fieldLabel:'角色',
	        name:'roletype',
	        id:'roletype',
	        labelWidth:40,
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
	        selectOnTab: true,
        	width:'20%',
	        store: roleStore,
	        fieldStyle: me.fieldStyle,
	        displayField:'rolename',
	        valueField:'roletype',
	        editable:true
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['userid','usercode','rolename','roletype','username','deptcode','deptid','receiver_area',
			        'usertypename','companyid','companyname','deptname','usertype','typename',
			        'sex','telphone','areacode','parnter_name','webchart','email','address','certificateno','relation_id','parentDeptname'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/user/queryallpage.do',
                params:{
//                	companyid:usercode==_super?null:companyid
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
        								username:trim(username.getValue()),
        								roletype:roleSerach.getValue(),
        								usertype:usertype.getValue(),
        								companyid:companyCombo.getValue(),
        								receiver_area:trim(receiver_area.getValue()),
        								parnter_name:trim(parnter_name.getValue()),
        								dept_name:trim(dept_name.getValue())
        					});
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
		
		//性别
		var sex = new Map();
		sex.put('0','女');   
		sex.put('1','男');  
		
		me.columns = [Ext.create("Ext.grid.RowNumberer",
				  {text: '序号', menuDisabled:true,width:40}),
		          { text: '登录名',dataIndex: 'usercode',menuDisabled:true,width:100 },
	              { text: '真实名',dataIndex: 'username',menuDisabled:true,width:100 },
	              { text: '角色名称',dataIndex: 'rolename',menuDisabled:true,width:100 },
	              { text: '所属公司',	dataIndex: 'companyname', menuDisabled:true,width:120},
                  { text: '部门',	dataIndex: 'deptname',menuDisabled:true,width:100},
                  { text: '性别',	dataIndex: 'sex', menuDisabled:true, width:60,
	            	  renderer:function(value){
	            		  return sex.get(value)
	            	  }
	              },
                  { text: '用户类型',dataIndex: 'typename',menuDisabled:true,width:100},
                  { text: '联系电话',dataIndex: 'telphone',menuDisabled:true,width:100},
                  { text: '邮件',	dataIndex: 'email',menuDisabled:true, width:100},
	              { text: '地址',	dataIndex: 'address',menuDisabled:true,width:120},
                  { text: '管理地区',dataIndex: 'receiver_area',menuDisabled:true,width:100},
                  { text: '合作方',dataIndex: 'parnter_name',menuDisabled:true,width:100},
                  { text: '员工编号',dataIndex: 'webchart',menuDisabled:true,width:100},
                  { text: '证号',dataIndex: 'certificateno',menuDisabled:true,width:100}
	              ];
		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[username,dept_name,parnter_name,receiver_area,usertype]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[companyCombo,roleSerach,{
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
	 			text:'部门配置',
	 			iconCls:'Cog',
	 			handler:me.onDeptConfig
	 		},{
	 			text:'角色配置',
	 			iconCls:'Cog',
	 			handler:me.onPermitConfig
	 		},{
	 			text:'模版配置',
	 			iconCls:'Cog',
	 			handler:me.onPartnerConfig
	 		},{
	 			text:'密码重置',
	 			iconCls:'Cog',
	 			handler:me.onPassReset
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	onPartnerConfig:function(){
		var me = this.up("gridpanel");
		var ss =  this.up("gridpanel").getView().getSelectionModel().getSelection();
		if(ss.length<1){
			Ext.MessageBox.alert("提示信息", '请选择需要配置的人员!');
			return;
		}
		if(ss.length>1){
			Ext.MessageBox.alert("提示信息", '请选择一个人员进行配置哦!');
			return;
		}
		
		var userid = ss[0].get("userid");
		var store = Ext.create('Ext.data.Store',{
			fields:['key','value','checked'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url : 'judicial/dicvalues/getReportModels.do',
                params:{
					type : 'dna',
					userid:userid
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
			model:'SINGLE',
			showHeaderCheckbox: false
		});
		var columns = [Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'8%', menuDisabled:true}),
		               { text: '模版类型',	dataIndex: 'value',flex:1, menuDisabled:true}
		              ];
		
		var grid = Ext.create("Ext.grid.Panel",{
			store:store,
			selModel:selModel,
			columns:columns,
			region:'center',
			multiSelect: true
		});
		store.addListener('load',function(){  
				for(var i = 0 ; i < store.getCount();i++)
				{
					if(store.getAt(i).get("checked"))
					{
	
						grid.getSelectionModel().select(i,true,false);
					}
				}
			  }); 
		var win = Ext.create("Ext.window.Window",{
			title:'模版配置',
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
					var selections =  grid.getView().getSelectionModel().getSelection();
					if(selections.length<=0){
						Ext.MessageBox.alert("提示信息", '请选择模版');
						return;
					};

					var code='';
					for(var i = 0 ; i < selections.length ; i ++)
					{
						code+=selections[i].get("key")+",";
					}
					code = code.substring(0,code.length-1);
					Ext.MessageBox.wait('正在操作','请稍后...');
					Ext.Ajax.request({  
						url:"upc/partnerConfig/saveReportModel.do", 
						method: "POST",
						headers: { 'Content-Type': 'application/json' },
						jsonData: {
							userid:userid,
							code:code
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
	onDeptConfig:function(){
		var me = this.up("gridpanel");
		var ss =  this.up("gridpanel").getView().getSelectionModel().getSelection();
		if(ss.length<1 || ss.length>1){
			Ext.MessageBox.alert("提示信息", '请选择一条需要配置的人员!');
			return;
		}
		var usercode = ss[0].get("usercode");
		var companyid = ss[0].get("companyid");
		var selModel = Ext.create('Ext.selection.CheckboxModel',{
		});
		var columns = [Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'8%', menuDisabled:true}),
		               { text: '部门名称',	dataIndex: 'text',flex:1, menuDisabled:true}
		              ];
		var store = Ext.create('Ext.data.TreeStore', {  
			fields:['id','text','leaf','checked'],
			proxy:{
				type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/department/queryDepartmentList.do',
                reader:'json',
                autoLoad:true,
                params:{
                	parentcode : "0" ,
                	usercode:usercode ,
                	companyid:companyid
                },
                clearOnLoad : true
			}
		}); 
		var grid=Ext.create("Ext.tree.Panel",{
			region:'center',
			columns:[{
				xtype:'treecolumn',
				text:'部门名称',
				dataIndex:'text',
				width:'100%',
				sortable:true
			}],
			store:store,
			rootVisible: false
		});
		grid.on('checkchange', function(node, checked,obj){
			   var checkedNodes = this.getChecked();
			    for(var i=0;i<checkedNodes.length;i++){
			                      var n = checkedNodes[i];
			                      if(node.get("id") != n.get("id")){
			                          n.set("checked" , false);
			                      }
			               }

			});
	    grid.expandAll();
		var win = Ext.create("Ext.window.Window",{
			title:'部门配置',
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
					var deptid=selNodes[0].internalId;
					Ext.MessageBox.wait('正在操作','请稍后...');
					Ext.Ajax.request({  
						url:"upc/user/saveDept.do", 
						method: "POST",
						headers: { 'Content-Type': 'application/json' },
						jsonData: {
							usercode:usercode,
							deptid:deptid
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
	onCaseConfig:function(){
		var me = this.up("gridpanel");
		var ss =  this.up("gridpanel").getView().getSelectionModel().getSelection();
		if(ss.length<1){
			Ext.MessageBox.alert("提示信息", '请选择需要配置的人员!');
			return;
		}else if(ss.length>1)
		{
			Ext.MessageBox.alert("提示信息", '配置不过来啦!');
			return;
		}
		var usercode = ss[0].get("usercode");
		var selModel = Ext.create('Ext.selection.CheckboxModel',{
			model:'SINGLE'
		});
		var store = Ext.create('Ext.data.TreeStore', {  
			fields:['id','typeid','typename','inputform','displaygrid','identify',
			        'status','sort','parentid','leaf','checked'],
			proxy:{
				type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'judicial/reporttype/queryall.do',
                reader:'json',
                autoLoad:true,
                params:{
                	parentid : "0" ,
                	usercode:usercode
                },
                clearOnLoad : true
			}
		}); 
		var grid=Ext.create("Ext.tree.Panel",{
			region:'center',
			columns:[{
				xtype:'treecolumn',
				text:'案例登记名称',
				dataIndex:'typename',
				width:150,
				sortable:true
			},{
				text:'案例登记url',
				dataIndex:'displaygrid',
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
			title:'案例登记配置',
			width:600,
			height:410,
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
					var typeids='';
					var selNodes = grid.getChecked();
					Ext.each(selNodes, function(node){
						var id = node.id.split('-')[3];
						if(id!='root')
						{
							typeids += id+",";
						}
					});
					Ext.Ajax.request({  
						url:"upc/user/saveUserReport.do", 
						method: "POST",
						headers: { 'Content-Type': 'application/json' },
						jsonData: {
							usercode:usercode,
							typeids:typeids
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
	onPermitConfig:function(){
		var me = this.up("gridpanel");
		var ss =  this.up("gridpanel").getView().getSelectionModel().getSelection();
		if(ss.length<1){
			Ext.MessageBox.alert("提示信息", '请选择需要配置人员!');
			return;
		}
		if(ss.length>1){
			Ext.MessageBox.alert("提示信息", '请选择一个人员进行配置哦!');
			return;
		}
		var usercode = '';
		for(var i = 0 ; i < ss.length ; i ++)
		{
			usercode += "'"+ss[i].get("usercode")+"',";
		}
		usercode = usercode.substring(0,(usercode.length-1));
		var userid = ss[0].get("userid");
		var store = Ext.create('Ext.data.Store',{
			fields:['roleid','roletype','rolename','permitname','checked'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'upc/role/queryall.do',
                params:{
                	userid:userid
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
			model:'SINGLE',
			showHeaderCheckbox: false
		});
		var columns = [Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'8%', menuDisabled:true}),
		               { text: '角色名称',	dataIndex: 'rolename',flex:1, menuDisabled:true},
		               { text: '角色权限',	dataIndex: 'permitname',flex:2, menuDisabled:true}
		              ];
		
		var grid = Ext.create("Ext.grid.Panel",{
			store:store,
			selModel:selModel,
			columns:columns,
			region:'center',
			multiSelect: true
		});
		store.addListener('load',function(){  
				for(var i = 0 ; i < store.getCount();i++)
				{
					if(store.getAt(i).get("checked"))
					{
	
						grid.getSelectionModel().select(i,true,false);
					}
				}
			  }); 
		var win = Ext.create("Ext.window.Window",{
			title:'角色配置',
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
					var selections =  grid.getView().getSelectionModel().getSelection();
					if(selections.length<=0){
						Ext.MessageBox.alert("提示信息", '请选择角色');
						return;
					};
					if(selections.length>1){
						Ext.MessageBox.alert("提示信息", '只能选择一条记录');
						return;
					};
					Ext.MessageBox.wait('正在操作','请稍后...');
					Ext.Ajax.request({  
						url:"upc/user/saveRole.do", 
						method: "POST",
						headers: { 'Content-Type': 'application/json' },
						jsonData: {
							usercode:usercode,
							roletype:selections[0].get("roletype")
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
	onPassReset:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要重置密码的人员!");
			return;
		};
		var userids='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			userids+="'"+selections[i].get("userid")+"',";
		}
		userids = userids.substring(0,userids.length-1);
		var values = {
				userid:userids
		};
		Ext.MessageBox.confirm("标题", "确认重置选中人员密码？", function (btn) {
			if("yes" == btn)
			{
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({  
					url:"upc/user/resetUserPass.do", 
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
	onInsert:function(){
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.upc.panel.RdsUpcUserForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'人员—新增',
			width:450,
			modal:true,
			iconCls:'Add',
			height:370,
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
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
			return;
		};
		ownaddressTemp = selections[0].get("areacode");
		var form = Ext.create("Rds.upc.panel.RdsUpcUserForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'人员—修改',
			width:450,
			modal:true,
			iconCls:'Pageedit',
			height:370,
			layout:'border',	
			items:[form]
		});
		win.show();
		form.loadRecord(selections[0]);
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	}
	
});

var upcUser_update=function(me){
	me = me.up("gridpanel");
	var selections = me.getView().getSelectionModel().getSelection();
	if (selections.length < 1) {
		Ext.Msg.alert("提示", "请选择需要修改的记录!");
		return;
	};
	var usercodes='';
	for(var i = 0 ; i < selections.length ; i ++)
	{
		usercodes+=selections[i].get("usercode")+",";
	}
	usercodes = usercodes.substring(0,usercodes.length-1);
	console.log(usercodes);
	
	upc_user_company_canel=function(){
		win.close();
	}
	upc_user_company_save=function(mei){
		var form  = mei.up("form").getForm();
		var values = form.getValues();
		values["usercodes"]=usercodes;
		console.log(values);
		if(form.isValid()){
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:"upc/user/userCompanyAdd.do", 
				method: "POST",
				headers: { 'Content-Type': 'application/json' },
				jsonData: values, 
				success: function (response, options) {  
					response = Ext.JSON.decode(response.responseText); 
					console.log(response);
	                 if (response.result==true) {  
	                 	Ext.MessageBox.alert("提示信息", response.message);
	                 	me.getStore().load();
	                 	win.close();
	                 }else { 
	                 	Ext.MessageBox.alert("提示信息", response.message);
	                 } 
				},  
				failure: function () {
					Ext.Msg.alert("提示", "修改失败<br>请联系管理员!");
				}
	      	});
		}
	}
	
	var name_plus = Ext.create('Ext.form.field.Text', {
		fieldLabel: '姓名前缀',
		width:180,
        labelWidth:65,
		fieldStyle:me.fieldStyle, 
		allowBlank:false,
		name: 'name_plus',
		maxLength: 20
	});
	
	var usercode_plus = Ext.create('Ext.form.field.Text', {
		fieldLabel: '登录名前缀',
		labelWidth:70,
		style:'margin-left:5px;',
		allowBlank:false,
		width:180,
		fieldStyle:me.fieldStyle, 
		name: 'usercode_plus',
		maxLength: 20
	});
	
	//部门列表store
	var dept_tree_store=Ext.create("Ext.data.TreeStore",{
		fields:['id','text','leaf'],
		proxy : {
			type: 'jsonajax', //获取方式
			actionMethods:{read:'POST'},
			url : "upc/department/queryDepartmentList.do",
			autoLoad:true,
            params:{
            	parentcode : "0" 
            },
            clearOnLoad : true
		},
		root: {
	        expanded: true,
	        text : '部门'
		}
	});
	var deptList = new Rds.ux.TreeCombo({
		name: 'deptid', 
	    fieldLabel: '所属部门',
	    anchor:'100%',
	    allowBlank:false,
	    maxPickerHeight:160,
	    labelWidth:65,
	    editable:false,
	    emptyText : "请选择",
		allowBlank:false,
	    readOnly:true,
	    store: dept_tree_store
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
	//公司列表combo
	var companyCombo = new Ext.form.ComboBox({
		autoSelect : true,
		editable:false,
		allowBlank:false,
		fieldLabel:'所属企业',
        name:'companyid',
        allowBlank:false,
        labelWidth:65,
        triggerAction: 'all',
        queryMode: 'local', 
        emptyText : "请选择",
        selectOnTab: true,
        store: companyStore,
        fieldStyle: me.fieldStyle,
        displayField:'companyname',
        valueField:'companyid',
        listClass: 'x-combo-list-small',
        listeners: {
            select : function(combo,records,options){ 
            	deptList.setValue('');
            	deptList.readOnly=false;
            	dept_tree_store.load({  
                    params: {  
                    	companyid: combo.value,
                        parentcode:'0'
                    }  
                }); 
            }    
        }  
	});
	var win=Ext.create("Ext.window.Window", {
		width : 400,
		iconCls :'Pageadd',
		height : 200,
		modal:true,
		title:'人员新增公司',
		layout : 'border',
		bodyStyle : "background-color:white;",
		items : [{
			xtype:"form",
			region : 'center',
			bodyPadding : 10,
			defaults : {
				anchor : '100%',
				labelWidth : 80,
				labelSeparator: "：",
				labelAlign: 'right'
			},
			buttons:[
					{
							text : '修改',
							iconCls : 'Disk',
							handler:upc_user_company_save
					}, {
							text : '取消',
							iconCls : 'Cancel',
							handler:upc_user_company_canel
					} 
			],
			border : false,
			autoHeight : true,
			items:[companyCombo , deptList,{
	            	border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					items : [name_plus,usercode_plus]
            	}
			]
		}]
	});
	win.show();
};
		