var areacode='';
var dicareatree_store=Ext.create("Ext.data.TreeStore",{
			fields:['id','text','type','leaf','url'],
			proxy : {
				type: 'jsonajax', //获取方式
				url : "judicial/area/getDicAreaInfo.do", //获取树节点的地址
			},
			root:{
				name:"地区",
				parentID:0
			},
			antoLoad:true,
			clearOnLoad : true
		});
Ext.define('Rds.upc.panel.RdsUpcDicAreaPanel', {
	extend : 'Ext.tree.Panel',
	collapsible: true,  
    useArrows : true,//展开按钮图标是箭头还是+-
    rootVisible: false,  
    multiSelect: true,  
	initComponent : function() {
		var me = this;
		me.store = dicareatree_store;
		me.callParent(arguments);
	},
	listeners:{
		"checkchange": function(node, state) {
			areacode='';
			for(var i = 0 ; i < this.getChecked().length ; i ++)
			{
				areacode += this.getChecked()[i].internalId + ',';
			}
        },
		'itemcontextmenu' : function(node, record, item, index, e, eOpts) {
			var id=record.get('id');
            e.preventDefault(); 
            e.stopEvent(); 
            var nodemenu;
            if(id.endWith("0")){
            	 nodemenu=Ext.create('Ext.menu.Menu', {
					    items: [{
					    	iconCls : 'Pageadd',
					        text: '添加',
					        handler:function(){
					        	var win=Ext.create("Ext.window.Window", {
					        		width : 250,
					        		iconCls :'Pageadd',
					        		height : 180,
					        		modal:true,
					        		title:'新增地区',
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
					        							text : '保存',
					        							iconCls : 'Disk',
					        							handler:function(me){
					        								var form  = me.up("form").getForm();
					        								var values = form.getValues();
					        								values["parentId"]=id;
					        								if(form.isValid()){
					        									Ext.MessageBox.wait('正在操作','请稍后...');
					        									Ext.Ajax.request({  
					        										url:"judicial/area/saveDicAreaInfo.do", 
					        										method: "POST",
					        										headers: { 'Content-Type': 'application/json' },
					        										jsonData: values, 
					        										success: function (response, options) {  
					        											response = Ext.JSON.decode(response.responseText); 
					        							                 if (response==true) {  
					        							                 	Ext.MessageBox.alert("提示信息", "添加成功");
					        							                 	dicareatree_store.load();
					        							                 	win.close();
					        							                 }else { 
					        							                 	Ext.MessageBox.alert("错误信息", "添加失败");
					        							                 } 
					        										},  
					        										failure: function () {
					        											Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
					        										}
					        							      	});
					        								}
					        							}
					        					}, {
					        							text : '取消',
					        							iconCls : 'Cancel',
					        							handler:function(){
					        								win.close();
					        							}
					        					} 
					        			],
					        			border : false,
					        			autoHeight : true,
					        			items:[
					        			       {
					        						// 该列在整行中所占的百分比
					        						xtype : "textfield",
					        						labelAlign: 'right',
					        						fieldLabel : '地区编号',
					        						labelWidth : 80,
					        						allowBlank  : false,//不允许为空  
					        			            blankText   : "不能为空",//错误提示信息，默认为This field is required! 
					        			            maxLength :50,
					        			            validator : function(value){
					        			            	var result=false;
					        			            	Ext.Ajax.request({  
					        			    				url:"judicial/area/exsitDicAreaCode.do", 
					        			    				method: "POST",
					        			    				async : false,
					        			    				headers: { 'Content-Type': 'application/json' },
					        			    				jsonData: {areacode:value}, 
					        			    				success: function (response, options) {  
					        			    					response = Ext.JSON.decode(response.responseText); 	
					        			    					if(!response){
					        			    						result= "此编号已存在";
					        			    					}else{
					        			    						result= true;
					        			    					}
					        			    				},  
					        			    				failure: function () {
					        			    					Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
					        			    				}
					        			    	      	});
					        			            	return result;
					        			            },  
					        						name : 'areacode'
					        					},
					        					{
					        						// 该列在整行中所占的百分比
					        						xtype : "textfield",
					        						labelAlign: 'right',
					        						fieldLabel : '地区名称',
					        						labelWidth : 80,
					        			            maxLength :50,
					        						name : 'areaname'
					        					}
					        			]
					        		}]
					        	});
					        	win.show();
					        }
					    },{

					    	iconCls : 'Pageadd',
					        text: '添加收费标准',
					        handler:function(){
					        	var win=Ext.create("Ext.window.Window", {
					        		width : 400,
					        		iconCls :'Add',
					        		modal:true,
					        		title:'添加收费标准',
					        		height : 350,
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
					        							text : '保存',
					        							iconCls : 'Disk',
					        							handler:function(me){
					        								var form  = me.up("form").getForm();
					        								var values = form.getValues();
					        								if(""==areacode)
					        									{Ext.MessageBox.alert("提示信息", "请选择地区！");return;}
					        								if("1" == values["type"])
					        								{
					        									if(""==values["userid"]) 
					        										{Ext.MessageBox.alert("提示信息", "请选择人员！");return;}
					        								}
					        								values["username"]= Ext.getCmp('useridareatemp').getRawValue().split('(')[0];
					        								values["areacode"]=areacode;
					        								if(form.isValid()){
					        									Ext.MessageBox.wait('正在操作','请稍后...');
					        									Ext.Ajax.request({  
					        										url:"finance/chargeStandard/saveStandardOld.do", 
					        										method: "POST",
					        										headers: { 'Content-Type': 'application/json' },
					        										jsonData: values, 
					        										success: function (response, options) {  
					        											response = Ext.JSON.decode(response.responseText);
					        											console.log(response);
					        							                 if (response.result==true) {  
					        							                 	Ext.MessageBox.alert("提示信息", "添加成功！");
					        							                 	dicareatree_store.load();
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
					        							}
					        					}, {
					        							text : '取消',
					        							iconCls : 'Cancel',
					        							handler:function(){
					        								win.close();
					        							}
					        					} 
					        			],
					        			border : false,
					        			autoHeight : true,
					        			items:[{
					                        xtype: 'combo',
					                        autoSelect : true,
					                        allowBlank:false,
					                		fieldLabel: '销售类型',
					                		labelWidth : 80,
					                		emptyText : "请选择",
					                		triggerAction: 'all',
					             	        queryMode: 'local',
					             	        selectOnTab: true,
					             	        store : new Ext.data.ArrayStore({
												fields : ['Name', 'Code'],
												data : [['司法鉴定', '0'], ['医学鉴定', '1'], ['法医临床', '2'], ['酒精检测', '3'], ['儿童基因库', '4'], ['痕迹鉴定', '5']]
											}),
					                        displayField: 'Name',
					                        valueField: 'Code',
					                        name:'type',
					                        value:'0',
					                        listClass: 'x-combo-list-small'
					                    },
					                    {
					        				xtype : 'combo',
					        				fieldLabel : '员工',
					        				labelWidth : 80,
					        				name : 'userid',
					        				id:'useridareatemp',
					        				emptyText:'(人员首字母)：如小明(xm)',
					        				store :Ext.create("Ext.data.Store",{
					        					   fields:[
					        							{name:'key',mapping:'key',type:'string'},
					        							{name:'value',mapping:'value',type:'string'}
					        			          ],
					        					pageSize : 20,
					        					autoLoad: false,
					        					proxy : {
					        						type : "ajax",
					        						url:'judicial/dicvalues/getUsersId.do',
					        						reader : {
					        							type : "json"
					        						}
					        					}
					        				}),
					        				displayField : 'value',
					        				valueField : 'key',
					        				columnWidth : 1,
					        				typeAhead : false,
					        				allowBlank:false, //不允许为空
					        				hideTrigger : true,
					        				forceSelection: true,
					        				minChars : 2,
					        				matchFieldWidth : true,
					        				listConfig : {
					        					loadingText : '正在查找...',
					        					emptyText : '没有找到匹配的数据'
					        				}
					        			},
											{
												xtype : 'textarea',
												fieldLabel : '计算公式',
												labelWidth : 80,
												allowBlank : false,
												name : 'equation',
												value : 'function exe(a,b){\t\n\t}',
												maxLength : 1000
											}, {
												xtype : 'label',
												style:'margin-top:10px;',
												forId : 'myFieldId',
												margin:'30px',
												text : '公式以“function exe(a,b){”开头,以“}”结尾，“a”表示样本数量，“b”表示收费类型,1为单亲,2为双亲，例如：function exe(a,b){if(b=1){return 1000*a}else{return 2000*a}},表示单亲1000元每人，双亲2000元每人.'
											}, {
												xtype : 'textfield',
												fieldLabel : '优惠比例',
												style:'margin-top:10px;',
												labelWidth : 80,
												regex : /^(0\.\d*|[1])$/,
												regexText : '请输入0-1之间的数字',
												allowBlank : false,
												maxLength : 5,
												maxValue : 1,
												name : 'discountrate'
											},{
												xtype: 'radiogroup',
									            fieldLabel: '是否需要副本',
									            labelWidth : 100,
									            itemCls: 'x-check-group-alt',
									            columns: 2,
									            vertical: true,
									            items: [
									                {boxLabel: '是', name: 'attach_need', inputValue: 0},
									                {boxLabel: '否', name: 'attach_need', inputValue: 1, checked: true},
									            ]
											}
					        			]
					        		}]
					        	});
					        	win.show();
					        }
					    
					    },{
					    	iconCls : 'Delete',
					        text: '删除',
					        handler:function(){
					        	Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
					    	        if("yes"==btn)
					    	        {
					    	        	Ext.MessageBox.wait('正在操作','请稍后...');
					    	        	Ext.Ajax.request({  
											url:"judicial/area/delDicAreaInfo.do", 
											method: "POST",
											headers: { 'Content-Type': 'application/json' },
											jsonData: {
												areacode:id
											},
											success: function (response, options) {  
												response = Ext.JSON.decode(response.responseText); 
								                 if (response==true) {  
								                 	Ext.MessageBox.alert("提示信息", "删除成功");
								                 	dicareatree_store.load();
								                 }else { 
								                 	Ext.MessageBox.alert("错误信息", "删除失败(可能存在子区域)");
								                 } 
											},  
											failure: function () {
												Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
											}
								      	});
					    	        }
					        	})
					        	
					        }
					    }]
					});
            }else{
            	 nodemenu=Ext.create('Ext.menu.Menu', {
					    items: [{
					    	iconCls : 'Pageadd',
					        text: '添加收费标准',
					        handler:function(){
					        	var win=Ext.create("Ext.window.Window", {
					        		width : 400,
					        		iconCls :'Add',
					        		modal:true,
					        		title:'添加收费标准',
					        		height : 350,
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
					        							text : '保存',
					        							iconCls : 'Disk',
					        							handler:function(me){
					        								var form  = me.up("form").getForm();
					        								var values = form.getValues();
					        								if(""==areacode)
					        									{Ext.MessageBox.alert("提示信息", "请选择地区！");return;}
					        								if("1" == values["type"])
					        								{
					        									if(""==values["userid"]) 
					        										{Ext.MessageBox.alert("提示信息", "请选择人员！");return;}
					        								}
					        								values["username"]= Ext.getCmp('useridareatemp').getRawValue().split('(')[0];
					        								values["areacode"]=areacode;
					        								if(form.isValid()){
					        									Ext.MessageBox.wait('正在操作','请稍后...');
					        									Ext.Ajax.request({  
					        										url:"finance/chargeStandard/saveStandardOld.do", 
					        										method: "POST",
					        										headers: { 'Content-Type': 'application/json' },
					        										jsonData: values, 
					        										success: function (response, options) {  
					        											response = Ext.JSON.decode(response.responseText);
					        											console.log(response);
					        							                 if (response.result==true) {  
					        							                 	Ext.MessageBox.alert("提示信息", "添加成功！");
					        							                 	dicareatree_store.load();
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
					        							}
					        					}, {
					        							text : '取消',
					        							iconCls : 'Cancel',
					        							handler:function(){
					        								win.close();
					        							}
					        					} 
					        			],
					        			border : false,
					        			autoHeight : true,
					        			items:[{
					                        xtype: 'combo',
					                        autoSelect : true,
					                        allowBlank:false,
					                		fieldLabel: '销售类型',
					                		labelWidth : 80,
					                		emptyText : "请选择",
					                		triggerAction: 'all',
					             	        queryMode: 'local',
					             	        selectOnTab: true,
					             	        store : new Ext.data.ArrayStore({
												fields : ['Name', 'Code'],
												data : [['司法鉴定', '0'], ['医学鉴定', '1'], ['法医临床', '2'], ['酒精检测', '3'], ['儿童基因库', '4'], ['痕迹鉴定', '5']]
											}),
					                        displayField: 'Name',
					                        valueField: 'Code',
					                        name:'type',
					                        value:'0',
					                        listClass: 'x-combo-list-small'
					                    },
					                    {
					        				xtype : 'combo',
					        				fieldLabel : '员工',
					        				labelWidth : 80,
					        				name : 'userid',
					        				id:'useridareatemp',
					        				emptyText:'(人员首字母)：如小明(xm)',
					        				store :Ext.create("Ext.data.Store",{
					        					   fields:[
					        							{name:'key',mapping:'key',type:'string'},
					        							{name:'value',mapping:'value',type:'string'}
					        			          ],
					        					pageSize : 20,
					        					autoLoad: false,
					        					proxy : {
					        						type : "ajax",
					        						url:'judicial/dicvalues/getUsersId.do',
					        						reader : {
					        							type : "json"
					        						}
					        					}
					        				}),
					        				displayField : 'value',
					        				valueField : 'key',
					        				columnWidth : 1,
					        				typeAhead : false,
					        				allowBlank:false, //不允许为空
					        				hideTrigger : true,
					        				forceSelection: true,
					        				minChars : 2,
					        				matchFieldWidth : true,
					        				listConfig : {
					        					loadingText : '正在查找...',
					        					emptyText : '没有找到匹配的数据'
					        				}
					        			},
//					                    {
//											xtype : "combo",
//											fieldLabel : '员工',
//											autoSelect : true,
////											editable:true,
//											id:'useridareatemp',
//											labelAlign: 'right',
//											columnWidth : .45,
//											labelWidth : 80,
//											mode: 'local', 
//											name:"userid",
//									        forceSelection: true,   
//									        emptyText:'请选择员工',  
//									        triggerAction: 'all',
//									        queryMode: 'local', 
//											valueField :"key",   
//									        displayField: "value",   
//											store: Ext.create(
//											        'Ext.data.Store',
//											        {
//											         fields:[
//											                      {name:'key',mapping:'key',type:'string'},
//											                      {name:'value',mapping:'value',type:'string'}
//											          ],
//											          proxy: {
//											                type: 'jsonajax',
//											                actionMethods:{read:'POST'},
//											                url:'judicial/dicvalues/getUsersId.do',
//											                params:{
//											                },
//											                reader: {
//											                    type: 'json',
//											                    root:'data',
//											                }
//											            },
//											          autoLoad:true
//											        }
//											   ),
//										} ,
											{
												xtype : 'textarea',
												fieldLabel : '计算公式',
												labelWidth : 80,
												allowBlank : false,
												name : 'equation',
												value : 'function exe(a,b){\t\n\t}',
												maxLength : 1000
											}, {
												xtype : 'label',
												style:'margin-top:10px;',
												forId : 'myFieldId',
												margin:'30px',
												text : '公式以“function exe(a,b){”开头,以“}”结尾，“a”表示样本数量，“b”表示收费类型,1为单亲,2为双亲，例如：function exe(a,b){if(b=1){return 1000*a}else{return 2000*a}},表示单亲1000元每人，双亲2000元每人.'
											}, {
												xtype : 'textfield',
												fieldLabel : '优惠比例',
												style:'margin-top:10px;',
												labelWidth : 80,
												regex : /^(0\.\d*|[1])$/,
												regexText : '请输入0-1之间的数字',
												allowBlank : false,
												maxLength : 5,
												maxValue : 1,
												name : 'discountrate'
											},{
												xtype: 'radiogroup',
									            fieldLabel: '是否需要副本',
									            labelWidth : 100,
									            itemCls: 'x-check-group-alt',
									            columns: 2,
									            vertical: true,
									            items: [
									                {boxLabel: '是', name: 'attach_need', inputValue: 0},
									                {boxLabel: '否', name: 'attach_need', inputValue: 1, checked: true},
									            ]
											}
					        			]
					        		}]
					        	});
					        	win.show();
					        }
					    },{
					    	iconCls : 'Delete',
					        text: '删除',
					        handler:function(){
					        	Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
					    	        if("yes"==btn)
					    	        {
					    	        	Ext.MessageBox.wait('正在操作','请稍后...');
							        	Ext.Ajax.request({  
											url:"judicial/area/delDicAreaInfo.do", 
											method: "POST",
											headers: { 'Content-Type': 'application/json' },
											jsonData: {
												areacode:id
											},
											success: function (response, options) {  
												response = Ext.JSON.decode(response.responseText); 
								                 if (response==true) {  
								                 	Ext.MessageBox.alert("提示信息", "删除成功");
								                 	dicareatree_store.load();
								                 }else { 
								                 	Ext.MessageBox.alert("错误信息", "删除失败(可能存在子区域)");
								                 } 
											},  
											failure: function () {
												Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
											}
								      	});
					    	        }
					        	})
					        	
					        }
					    }]
					});
            }
            nodemenu.showAt(e.getPoint());
		}
	}
});