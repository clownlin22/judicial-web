var rowEditing = Ext.create('Ext.grid.plugin.RowEditing',{
            pluginId:'rowEditing',
            saveBtnText: '保存', 
            cancelBtnText: "取消", 
            autoCancel: false, 
            clicksToEdit:2   //双击进行修改  1-单击   2-双击    0-可取消双击/单击事件
});
Ext.define('Rds.bacera.panel.BaceraGeneTestingProjectExpressGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    selType: 'rowmodel',
    plugins: [rowEditing],
	initComponent : function() {
		var me = this;
		var reportif=new Ext.form.field.ComboBox({
			fieldLabel : '是否发报告',
			width:'20%',
			labelWidth : 70,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部',0],['是',1 ],['否',2 ] ]
					}),
			value : '',
			mode : 'local',
			name : 'reportif',
			value: 0
		});
		var consumer_name = Ext.create('Ext.form.field.Text',{
        	name:'consumer_name',
        	labelWidth:80,
        	width:'20%',
        	fieldLabel:'客户姓名'
        });
		var sample_number = Ext.create('Ext.form.field.Text',{
        	name:'sample_number',
        	labelWidth:80,
        	width:'20%',
        	fieldLabel:'样本编号'
        });
		var consumer_sex=new Ext.form.field.ComboBox({
			fieldLabel : '性别',
			width:'20%',
			labelWidth : 70,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部',''],['男','M' ],['女','F' ] ]
					}),
			value : '',
			mode : 'local',
			name : 'sex',
		});
		var test_number = Ext.create('Ext.form.field.Text',{
        	name:'test_number',
        	labelWidth:80,
        	width:'20%',
        	fieldLabel:'案例编号'
        });
		var test_package_name = Ext.create('Ext.form.field.Text',{
	    	name:'test_package_name',
	    	labelWidth:80,
        	width:'20%',
	    	fieldLabel:'检测套餐名称'
	    });
		var agency_name = Ext.create('Ext.form.field.Text',{
        	name:'agency_name',
        	labelWidth:80,
        	width:'20%',
        	fieldLabel:'代理商名称'
        });
	    var test_item_names = Ext.create('Ext.form.field.Text',{
	    	name:'test_item_names',
	    	labelWidth:80,
        	width:'20%',
	    	fieldLabel:'检测项目名称'
	    });
	    var charge_standard_id = Ext.create('Ext.form.field.Text',{
	    	name:'charge_standard_id',
	    	labelWidth:80,
        	width:'20%',
	    	fieldLabel:'归属人id'
	    });
	    var charge_standard_name = Ext.create('Ext.form.field.Text',{
	    	name:'charge_standard_name',
	    	labelWidth:80,
        	width:'20%',
	    	fieldLabel:'归属人姓名'
	    });
		var mailStore = Ext.create('Ext.data.Store', {
			fields:['key','value'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getMailModels.do',
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
		var gene_express_starttime = new Ext.form.DateField({
			id:'gene_express_starttime',
			name : 'gene_express_starttime',
        	width:'20%',
			fieldLabel : '添加日期从',
			labelWidth : 80,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('gene_express_starttime').getValue();
	                var endDate = Ext.getCmp('gene_express_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('gene_express_starttime').setValue(endDate);
	                }
				}
			}
		});
		var gene_express_endtime = new Ext.form.DateField({
			id:'gene_express_endtime',
			name : 'gene_express_endtime',
        	width:'20%',
			labelWidth : 40,
			fieldLabel : '到 ',
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY,1),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('gene_express_starttime').getValue();
	                var endDate = Ext.getCmp('gene_express_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('gene_express_starttime').setValue(endDate);
	                }
				}
			}
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','add_time','consumer_name','consumer_sex','consumer_birthday','consumer_phone','sample_number','test_number','report_date','test_package_id','test_package_name',
			        'agency_id','agency_name','test_item_ids','test_item_names','expressnum','expresstype','recive','expresstime','expressremark','paragraphtime','account_type','remarks','remark','charge_standard_name'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/Gene/queryallpage.do',
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
            		me.getSelectionModel().clearSelections();
            		Ext.apply(me.store.proxy.params, {
            			consumer_name:consumer_name.getValue().trim(),
            			sample_number:sample_number.getValue().trim(),
            			gene_starttime:dateformat(gene_express_starttime.getValue()),
            			gene_endtime:dateformat(gene_express_endtime.getValue()),
            			test_number:test_number.getValue(),
            			test_package_name:test_package_name.getValue(),
            			reportif:reportif.getValue(),
            		//	agency_name:agency_name.getValue().trim(),
            			test_item_names:test_item_names.getValue(),
            			charge_standard_name:charge_standard_name.getValue().trim(),
        				consumer_sex:consumer_sex.getValue().trim()
        				//consumer_phone:consumer_phone.getValue().trim()
        				});
        		}
            }
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
		});
		me.bbar = Ext.create('Ext.PagingToolbar', {
			store : me.store,
			pageSize : me.pageSize,
			displayInfo : true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
			emptyMsg : "没有符合条件的记录"
		});
		//me.bbar = {xtype: 'label',id:'totalBbarGene_express', text: '',style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
		me.columns = [
	              { text: '案例编号',	dataIndex: 'test_number', menuDisabled:true, width : 120},
                  { text: '样本编号',	dataIndex: 'sample_number', menuDisabled:true, width : 120},
                  { text: '检测套餐名',	dataIndex: 'test_package_name', menuDisabled:true, width : 170},
	              { text: '快递单号',	dataIndex: 'expressnum', menuDisabled:true, width : 100,
                	  editor:'textfield'
	              },
	              { text: '快递类型',	dataIndex: 'expresstype', menuDisabled:true, width : 110,
	            	  editor:new Ext.form.ComboBox({
	          				autoSelect : true,
	          				editable:true,
	          		        name:'expresstype',
	          		        triggerAction: 'all',
	          		        queryMode: 'local', 
	          		        emptyText : "请选择",
	          		        selectOnTab: true,
	          		        store: mailStore,
	          		        maxLength: 50,
	          		        fieldStyle: me.fieldStyle,
	          		        displayField:'value',
	          		        valueField:'value',
	          		        listClass: 'x-combo-list-small'
	          			})
	              },
                  { text: '快递日期',	dataIndex: 'expresstime', menuDisabled:true, width : 110    },
                  { text: '收件人',	dataIndex: 'recive', menuDisabled:true, width : 120,
	            	  editor:'textfield'
	              },{ text: '快递备注',	dataIndex: 'expressremark', menuDisabled:true, width : 150,
                	  editor:'textfield'
                  },

                  { text: '账户类型',	dataIndex: 'account_type', menuDisabled:true, width : 150},
                  { text: '到款日期',	dataIndex: 'paragraphtime', menuDisabled:true, width : 95},
                  { text: '登记时间',	dataIndex: 'add_time', menuDisabled:true, width:120,
                	  renderer:Ext.util.Format.dateRenderer('Y-m-d')
                  },
                  { text: '客户姓名',	dataIndex: 'consumer_name', menuDisabled:true, width:80},
                  { text: '归属人全称',	dataIndex: 'charge_standard_name', menuDisabled:true, width : 225},
                  { text: '备注',	dataIndex: 'remark', menuDisabled:true, width : 200},
                  { text: '被代理人',	dataIndex: 'agentname', menuDisabled:true, width : 80},
                  { text: '财务备注',	dataIndex: 'remarks', menuDisabled:true, width : 150},
		          { text: '检测项目名',	dataIndex: 'test_item_names', menuDisabled:true, width:200}, 
                  { text: '是否发报告',	dataIndex: 'reportif', menuDisabled:true, width : 100,
                	  renderer : function(value, cellmeta,
								record, rowIndex, columnIndex,
								store) {
							var isnull= record.data["expresstype"];
							var isll= record.data["expressnum"];
							if ( null !=isnull||null!=isll) {
								return "是";
							} else {
								return "<span style='color:red'>否</span>";
							}

						}
                	  
                  }      ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[test_item_names,test_package_name,consumer_name,consumer_sex]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[sample_number,gene_express_starttime,gene_express_endtime,reportif]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[test_number,charge_standard_name,{
	            	text:'查询',
	            	iconCls:'Find',
	            	handler:me.onSearch
	            }]
	 	
	 	},{

	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[{
			 			text:'案例详情',
			 			iconCls:'Find',
			 			handler:me.casef
			 	},{
		 			text:'案例编号',
		 			iconCls:'Find',
		 			handler:me.casecode
		 	}]
	 	
	 	
	 	}];
		me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbarGene_express').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	casef:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择一条记录!");
			return;
		};
			var form = Ext.create("Rds.bacera.form.BaceraGeneTestingProjectExpressForm",{
				region:"center",
				grid:me
			});
			var win = Ext.create("Ext.window.Window",{
				title:'案例详情',
				width:580,
				iconCls:'Pageedit',
				modal:true,
				height:400,
				layout:'border',	
				items:[form]
			});
			win.show();
			form.loadRecord(selections[0]);
		
	},
	caseCode:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要查看的案例编号!");
			return;
		};
		var num="";
		for(var i = 1 ; i < selections.length+1 ; i ++)
		{
			num += selections[i-1].get("test_number")+";";
		}
		Ext.Msg.alert("我是案例编号", num);
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	},
	listeners : {
		'beforeedit':function(editor, e,s){
			function afterEdit(e,s){
             	Ext.Ajax.request({  
    				url:"bacera/Gene/saveGeneExpress.do", 
    				method: "POST",
    				headers: { 'Content-Type': 'application/json' },
    				jsonData: {
    					id:s.record.data.id,
    					num:s.record.data.test_number,
    					expressnum:s.record.data.expressnum,
    					recive:s.record.data.recive,
    					expresstime:(Ext.Date.format(new Date(), 'Y-m-d')),
    					case_type:s.record.data.test_item_names,
    					expresstype:s.record.data.expresstype,
    					expressremark:s.record.data.expressremark
    				}, 
    				success: function (response, options) {  
    					response = Ext.JSON.decode(response.responseText); 
    	                 if (response==false) {  
    		                 Ext.MessageBox.alert("错误信息", "修改快递失败，请查看");
    	                 }
    				},  
    				failure: function () {
    					Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
    				}
    	      	});
			}
			rowEditing.on('edit',afterEdit);
		},
		'afterrender' : function() {
			this.store.load();
		}
	}
	
});




