Ext.define("Rds.judicial.panel.JudicialRemittanceAccountGridPanel", {
			extend : "Ext.grid.Panel",
			loadMask : true,
			viewConfig : {
				trackOver : false,
				stripeRows : false
			},
			pageSize : 25,
		    selType: 'rowmodel',
		    plugins: [ Ext.create('Ext.grid.plugin.CellEditing', {
		                  clicksToEdit: 1,
		                  listeners:{  
		                      'edit':function(e,s){  
		                    	  Ext.Ajax.request({
		                  			url : "judicial/remittance/queryAccount.do",
		                  			method : "POST",
		                  			async:false,
		                  			headers : {
		                  				'Content-Type' : 'application/json'
		                  			},
		                  			jsonData: {
										id:s.record.data.id,
										remittanceAccount:s.record.data.remittanceAccount.trim(),
										accountName:s.record.data.accountName.trim(),
										remark:s.record.data.remark.trim()
									}, 
		                  			success : function(response, options) {
		                  				response = Ext.JSON.decode(response.responseText);
		                  				console.log(response);
		                  				if(response)
		                  					{
		                  						Ext.Msg.alert("提示", "修改无效，已存在汇款账户或账户名称，请查看!");
		                  					}
		                  				else
		                  					{

		    		                      	Ext.Ajax.request({  
		    									url:"judicial/remittance/save.do", 
		    									method: "POST",
		    									headers: { 'Content-Type': 'application/json' },
		    									jsonData: {
		    										id:s.record.data.id,
		    										remittanceAccount:s.record.data.remittanceAccount.trim(),
		    										accountName:s.record.data.accountName.trim(),
		    										remark:s.record.data.remark.trim()
		    									}, 
		    									success: function (response, options) {  
		    										response = Ext.JSON.decode(response.responseText); 
		    						                 if (response==false) {  
		    							                 Ext.MessageBox.alert("错误信息", "修改失败，请查看");
		    						                 }
		    									},  
		    									failure: function () {
		    										Ext.Msg.alert("提示", "修改失败<br>请联系管理员!");
		    									}
		    						      	});
		                  					}
		                  			}
		                    	  });
		                    	  
		                      }  
		      	          }  
		              })
		          ],
			initComponent : function() {
				var me = this;
				var accountName = Ext.create('Ext.form.field.Text', {
							name : 'accountName',
							labelWidth : 60,
							width : 200,
							fieldLabel : '账户名称'
						});
				var remittanceAccount = Ext.create('Ext.form.field.Text', {
					name : 'remittanceAccount',
					labelWidth : 60,
					width : 200,
					fieldLabel : '汇款账户'
				});
				me.store = Ext.create('Ext.data.Store', {
							fields : ['id', 'remittanceAccount', 'accountName',
									'remark'],
							proxy : {
								type : 'jsonajax',
								actionMethods : {
									read : 'POST'
								},
								url : 'judicial/remittance/queryallpage.do',
								params : {},
								reader : {
									type : 'json',
									root : 'data',
									totalProperty : 'total'
								}
							},
							listeners : {
								'beforeload' : function(ds, operation, opt) {
									me.getSelectionModel().clearSelections();
									Ext.apply(me.store.proxy.params, {
										accountName : accountName.getValue(),
										remittanceAccount:remittanceAccount.getValue()
											});
								}
							}
						});

//				me.selModel = Ext.create('Ext.selection.CheckboxModel', {
//							mode : 'SINGLE'
//						});

				me.bbar = Ext.create('Ext.PagingToolbar', {
							store : me.store,
							pageSize : me.pageSize,
							displayInfo : true,
							displayMsg : "第 {0} - {1} 条  共 {2} 条",
							emptyMsg : "没有符合条件的记录"
						});
				me.columns = [Ext.create("Ext.grid.RowNumberer", {
									text : '序号',
									width : '4%',
									menuDisabled : true
								}), {
							dataIndex : 'id',
							hidden : true
						}, {
							text : '账户名称',
							dataIndex : 'accountName',
							width : '20%',
							menuDisabled : true,
							 editor:'textfield'
						}, {
							text : '汇款账户',
							dataIndex : 'remittanceAccount',
							width : '20%',
							menuDisabled : true,
							editor:'textfield'
						}, {
							text : '备注',
							dataIndex : 'remark',
							width : '50%',
							menuDisabled : true,
							 editor:'textfield'
						}];

				me.dockedItems = [{
							xtype : 'toolbar',
							name : 'search',
							dock : 'top',
							items : [accountName,remittanceAccount, {
										text : '查询',
										iconCls : 'Find',
										handler : me.onSearch
									}]
						}, {
							xtype : 'toolbar',
							dock : 'top',
							items : [{
										text : '添加',
										iconCls : 'Add',
										handler : me.onInsert
									},{
										text : '删除',
										iconCls : 'Delete',
										handler : me.onDelete
									}]
						}];
				me.store.load();
				me.callParent(arguments);
			},
			onInsert : function() {
				var me = this.up("gridpanel");
				var form = Ext.create("Rds.judicial.form.JudicialRemittanceAccountForm", {
							region : "center",
							grid : me
						});
				var win = Ext.create("Ext.window.Window", {
							title : '汇款账户—新增',
							width : 400,
							iconCls : 'Add',
							height : 250,
							layout : 'border',
							items : [form]
						});
				win.show();
			},
			onDelete : function() {
				var me = this.up("gridpanel");
				var selections = me.getView().getSelectionModel()
						.getSelection();
				if (selections.length < 1) {
					Ext.Msg.alert("提示", "请选择需要删除的记录!");
					return;
				};
				var values = {
					id : selections[0].get("id")
				};
				Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
			        if("yes"==btn)
			        {
						Ext.Ajax.request({
									url : "judicial/remittance/delete.do",
									method : "POST",
									headers : {
										'Content-Type' : 'application/json'
									},
									jsonData : values,
									success : function(response, options) {
										response = Ext.JSON
												.decode(response.responseText);
										if (response.result == true) {
											Ext.MessageBox.alert("提示信息",
													response.message);
											me.getStore().load();
										} else {
											Ext.MessageBox.alert("错误信息",
													response.message);
										}
									},
									failure : function() {
										Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
									}
		
								});
			        }
			    });
			},
			onSearch : function() {
				var me = this.up("gridpanel");
				me.getStore().load({
							params : {
								start : 0
							}
						});

			}
		});