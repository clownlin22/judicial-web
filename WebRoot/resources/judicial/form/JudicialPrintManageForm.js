var companyStore = Ext.create(
        'Ext.data.Store',
        {
          model:'model',
          proxy:{
        	type: 'jsonajax',
            actionMethods:{read:'POST'},
            url:'judicial/printmanage/getCompany.do',
            reader:{
              type:'json',
              root:'data'
            }
          },
          autoLoad:true,
          remoteSort:true						
        }
      );
var store=Ext.create("Ext.data.TreeStore",{
	fields:['id','text','type','leaf','url','checked'],
	proxy : {
		type: 'jsonajax', //获取方式
		url: 'judicial/printmanage/getArea.do',
	},
	root:{
		name:"地区",
		id:0
	},
	antoLoad:true,
	clearOnLoad : true
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
	
Ext.define('Rds.judicial.form.JudicialPrintManageForm',
				{
					extend : 'Ext.form.Panel',
					style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					bodyPadding : 10,
					defaults : {
						anchor : '100%',
						labelWidth : 80,
						labelSeparator: "：",
						labelAlign: 'right'
					},
					border : false,
					autoHeight : true,
					initComponent : function() {
						var me = this;
						var grid=Ext.create("Ext.tree.Panel",{
							name:'grid',
							region:'center',
							height:400,
							style : 'margin-left:20px;',
							columns:[{
								xtype:'treecolumn',
								text:'编号',
								dataIndex:'id',
								width:150,
								sortable:true
							},{
								text:'名称',
								dataIndex:'text',
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
						
						me.items = [ {
											xtype : 'hiddenfield',
											name : 'print_code'
									  },
									  {
											xtype : "combo",
											fieldLabel : '单位类型',
											mode: 'local',   
											labelWidth : 80,
											editable:false,
											labelAlign: 'right',
											blankText:'请选择公司',   
										    emptyText:'请选择公司',  
										    allowBlank  : false,//不允许为空  
										    blankText   : "不能为空",//错误提示信息，默认为This field is required!  
											valueField :"key",   
										    displayField: "value",    
											name : 'companyid',
											store: companyStore,
											listeners: {
												select:function(combo,record,opts) {  
		   											var me = this.up("form");
		   											var form = me.getForm();
		   											var values = form.getValues();
													Ext.apply(grid.store.proxy.extraParams,{companyid:values["companyid"],pmodel_code:values["print_code"]});
													grid.store.load();
		   									    }
											} 
										},
										grid
						   ];

						me.buttons = [ {
							text : '保存',
							iconCls : 'Disk',
							handler : me.onSave
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : me.onCancel
						} ]
						me.callParent(arguments);
					},
					onSave : function() {
						var me = this.up("form");
						var form = me.getForm();
						if (form.isValid()) {
							var values = form.getValues();
							var area_code='';
							var grid = me.down("treepanel[name=grid]");
							var selNodes = grid.getChecked();
							var i=0;
							Ext.each(selNodes, function(node){
								var id = node.id.split('-')[3];
								if(id!=0)
								{
									area_code += id+",";
									i++;
								}
							});
							if(i==0){
								Ext.Msg.alert("提示", "请选择需要配置的地区!");
								return;
							}
							area_code = area_code.substring(0,(area_code.length-1));
							Ext.Ajax.request({  
								url:"judicial/printmanage/saveArea.do", 
								method: "POST",
								headers: { 'Content-Type': 'application/json' },
								jsonData: {
									area_code:area_code,
									companyid:values["companyid"],
									pmodel_code:values["print_code"]
								}, 
								success: function (response, options) {  
									response = Ext.JSON.decode(response.responseText); 
					                 if (response == true) {  
					                 	Ext.MessageBox.alert("提示信息", "配置成功");
					                 	me.up("window").close();
					                 }else { 
					                 	Ext.MessageBox.alert("错误信息", "配置失败");
					                 } 
								},  
								failure: function () {
									Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
								}
					    	       
					      	});
						}
					},
					onCancel : function() {
						var me = this;
						me.up("window").close();
					}
				});
