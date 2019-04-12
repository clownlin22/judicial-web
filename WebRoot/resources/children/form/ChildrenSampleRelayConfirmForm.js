Ext.define('Rds.children.form.ChildrenSampleRelayConfirmForm',
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
						me.items = [{
							xtype : 'hiddenfield',
							name : 'receive_id'
						},{
					        xtype     : 'textareafield',
					        grow      : true,
					        name      : 'receive_remark',
					        fieldLabel: '确认备注原因',
					        anchor    : '100%',
					        maxLength : 500,
					        height:80
						}];
						me.buttons = [ {
							text : '确认',
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
						var values = form.getValues();
						if(form.isValid()){
							var samples='';
							var grid = me.down("treepanel[name=grid]");
							var selNodes = grid.getChecked();
							var i=0;
							Ext.each(selNodes, function(node){
								var id = node.id.split('-')[3];
								if(id!=0)
								{
									samples += id+",";
									i++;
								}
							});
							if(i>0){
								samples = samples.substring(0,(samples.length-1));
							}
							values["sample_codeids"] = samples;
							Ext.MessageBox.confirm('提示','确认此次交接吗,没打勾即确认没不通过?',
									function(id) {
										if (id == 'yes') {
											Ext.MessageBox.wait('正在操作','请稍后...');
											Ext.Ajax.request({  
												url:"children/sampleRelay/confirmReceiveSampleInfo.do", 
												method: "POST",
												headers: { 'Content-Type': 'application/json' },
												jsonData: values, 
												success: function (response, options) {  
													response = Ext.JSON.decode(response.responseText); 
									                 if (response.result==true) {  
									                 	Ext.MessageBox.alert("提示信息", response.message);
									                 	me.grid.getStore().load();
									                 	me.up("window").close();
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
						}
					},
					onCancel : function() {
						var me = this;
						me.up("window").close();
					},
					listeners : {
						'afterrender' : function() {
							var me = this;
							var form = me.getForm();
							var values = form.getValues();
							var store=Ext.create("Ext.data.TreeStore",{
								fields:['id','text','type','leaf','url','checked'],
								proxy : {
									type: 'jsonajax', //获取方式
									url: 'children/sampleRelay/getSampleInfo.do?receive_id='+values["receive_id"],
								},
								antoLoad:true,
								clearOnLoad : true
							});
							var grid=Ext.create("Ext.tree.Panel",{
								name:'grid',
								height:330,
								style : 'margin-left:20px;',
								columns:[{
									text:'样本编号（确认打√）（右击全选）',
									xtype:'treecolumn',
									dataIndex:'text',
									width:250,
									flex:1,
									sortable:true
								}],
								store:store,
								rootVisible: false,
								listeners:{  
							        'itemcontextmenu':function(menutree,record,items,index,e){  
							            e.preventDefault();  
							            e.stopEvent();  
							            console.log(record.parentNode.childNodes.length);
							            //判断是否为叶子结点  
							            if(record.data.leaf==true){  
							            var nodemenu = new Ext.menu.Menu({  
							                floating:true,  
							                items:[{  
							                    text:'全选',  
							                    handler:function(){  
							                        for( var i =0;i<record.parentNode.childNodes.length;++i){  
							                            //设置结点checked属性为true  
							                        	record.parentNode.childNodes[i].set('checked',true);     
							                        }  
							                    }  
							                },{  
							                    text:'反选',  
							                    handler:function(){  
							                        for( var i =0;i<record.parentNode.childNodes.length;++i){  
							                            if(record.parentNode.childNodes[i].data.checked == false) {  
							                                //设置结点checked属性为true  
							                            	record.parentNode.childNodes[i].set('checked',true)}  
							                            else {  
							                                //设置结点checked属性为true  
							                            	record.parentNode.childNodes[i].set('checked',false);    
							                            }     
							                        }  
							                    }  
							                },{  
							                    text:'撤销全部',  
							                    handler:function(){  
							                        for( var i =0;i<record.parentNode.childNodes.length;++i){  
							                            //设置结点checked属性为false  
							                        	record.parentNode.childNodes[i].set('checked',false);    
							                        }  
							                    }  
							                }]  
							                  
							            });  
							            nodemenu.showAt(e.getXY());  
							            }  
							        }  
							    }  
							});
							me.add(grid);
						}
					}
				});
