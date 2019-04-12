
onDownload = function(value)
{
	window.location.href = "statistics/report/download.do?uuid=" +value;
}
ondelete = 	function (value) {
	Ext.Ajax.request({  
		url : "statistics/report/delete.do?uuid="+value,
		
		success: function (response, options) {  
			response = Ext.JSON.decode(response.responseText); 
             if (response.result == true) {  
             	Ext.MessageBox.alert("提示信息", response.message);
             	detailStore.load();
             }else { 
             	Ext.MessageBox.alert("错误信息", response.message);
             } 
		},  
		failure: function () {
			Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
		}
	       
  	});
}

var financeStore = Ext.create('Ext.data.Store', {
	fields : ['id','subject','flag'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'post'
				},
				url : 'statistics/report/queryAllSubject.do',
				params : {},
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'total'
				}
			},
			listeners : {
				'beforeload' : function(ds, operation, opt) {
					Ext.apply(financeStore.proxy.params, {
					//	subject : subject.getValue()
						subject:Ext.getCmp('sub').getValue()
							});
				}
			}
		});
var detailStore = Ext.create('Ext.data.Store', {
	fields : ['uuid','report','upload_username',
				'upload_date','times'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'post'
				},
				url : 'statistics/report/queryAllReport.do',
				params : {
					
				},
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'total'
				}
			}
		});

Ext.define("Rds.statistic.panel.ExperimentalReportGridPanel", {
			extend : "Ext.panel.Panel",
			layout : 'border',
			items : [{
				xtype : 'gridpanel',
				region : 'center',
				//width:'100%',
				columns : [
							  {
									text : '工作任务',
									dataIndex : 'subject',
									width : 250,
									menuDisabled : true
								},
								 {
									text : 'id',
									dataIndex : 'id',
									width : 200,
									menuDisabled : true,
									hidden:true
								}],
				store : financeStore,
				dockedItems : [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [{
						xtype : 'toolbar',
						dock : 'top',
						items : [{
							text : '添加',
							iconCls : 'Add',
							handler : function() {
								var me = this.up("gridpanel");
								var form = Ext
										.create(
												"Rds.statistic.panel.ExperimentalReportInsertForm",
												{
													region : "center",
													grid : me
												});
								var win = Ext.create("Ext.window.Window", {
											title : '新增',
											width : 400,
											iconCls : 'Add',
											height : 200,
											layout : 'border',
											items : [form]
										});
								win.show();
							}
						},{ text : '上传附件',
			                    iconCls : 'Add',
			                    handler : function(){
			                    	var me = this.up("gridpanel");
			                		var selections =  me.getView().getSelectionModel().getSelection();
			                		if (selections.length < 1) {
			                			Ext.Msg.alert("提示", "请选择一条记录!");
			                			return;
			                		};
			                		var form = Ext.create("Rds.statistic.panel.ExperimentalReportForm",
			            					{
			            						region : "center",
			            						grid : me
			            					});
			            			var win = Ext.create("Ext.window.Window", {
			            						title : '上传新文件',
			            						width : 400,
			            						iconCls : 'Add',
			            						height : 200,
			            						layout : 'border',
			            						modal : true,
			            						items : [form],
			            						listeners : {
			            							close : {
			            								fn : function() {
			            									detailStore.load();
			            								}
			            							}
			            						}
			            					});
			            			form.loadRecord(selections[0]);
			            			win.show();
			                    }
			               
						},{
							text : '删除',
	                    iconCls : 'Delete',
	                    handler : function() {
			                var me = this.up("gridpanel");
				            var selections = me.getView().getSelectionModel()
											.getSelection();
									if (selections.length < 1) {
										Ext.Msg.alert("提示", "请选择需要删除的记录!");
										return;
									};
										var id=selections[0].get("id");
										Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
								        if("yes"==btn)
								        {
								        	
								        	//Ext.MessageBox.wait('正在删除','请稍后...');
								    		Ext.Ajax.request({  
								    			url : "statistics/report/deleteSubject.do",
								    			method: "POST",
								    			headers: { 'Content-Type': 'application/json' },
								    			jsonData: {
													'id':id,
										
												},
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
								    				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
								    			}
								        	       
								          	});
								        }
								    });
	
	                    }					
											
	                    },{
							 xtype:"textfield",
							   name : 'subject',
								labelWidth : 60,
								width : 150,
								id:'sub',
								fieldLabel : '工作任务'
							
						}, {
							text : '查询',
							iconCls : 'Find',
							handler : function() {
								var me = this.up("gridpanel");
								financeStore.load({
											params : {
												start : 0
											}
										});
							}
						}]
					}]
				}],
				listeners : {
					'afterrender' : function() {
						financeStore.load();
					},
					'select' : function(value, record) {
						//console.log(record.isModel);
						Ext.apply(detailStore.proxy.params, {
							id : record.get('id')
								});
						detailStore.load();
						Ext.getCmp('sgrid').show();
					}
				}

			}, { xtype : 'grid',
				//floating:true,
				width:700,
				height:436,
				style : 'margin:51px 00px 0px 250px;',	
				//style:'padding:20px 0px 0px 0px',
				position:'right',
				store : detailStore,
				id:'sgrid',
				hidden:true,
				columns : [ {
					text : '附件',
					dataIndex : 'report',
					width : 180,
					menuDisabled : true
				}, {
					text : '上传日期',
					dataIndex : 'upload_date',
					width : 150,
					menuDisabled : true
				},
				{
					text : '上传人员',
					dataIndex : 'upload_username',
					width : 100,
					menuDisabled : true
				},
				{
					text : '下载次数',
					dataIndex : 'times',
					width : 70,
					menuDisabled : true
				},
				{
					header : "操作",
					dataIndex : 'uuid',
					width : 50,
					menuDisabled : true,
					renderer : function(value, cellmeta, record,
							rowIndex, columnIndex, store) {
						var uuid = record.data["uuid"];
						if(null!=uuid){
							return "<a href='#' onclick=\"onDownload('"
							+ uuid
							+ "')\"  >下载";
						}
						

					}
			
			},{
				header : "删除",
				dataIndex : 'uuid',
				width : 50,
				menuDisabled : true,
				renderer : function(value, cellmeta, record,
						rowIndex, columnIndex, store) {
					var uuid = record.data["uuid"];
					if(null!=uuid){
						return "<a href='javascript:void(0)' onclick=\"ondelete('"
						+ uuid
						+ "')\"  >删除";
					}
					

				}
		
		}]
}]  

		});
