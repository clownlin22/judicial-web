var Gene="";
Ext.define('Rds.bacera.panel.BaceraGeneTestingProjectListGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var consumer_name = Ext.create('Ext.form.field.Text',{
        	name:'consumer_name',
        	labelWidth:80,
        	width:'20%',
        	fieldLabel:'客户姓名'
        });
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
		var gene_report_starttime = new Ext.form.DateField({
			//id:'gene_report_starttime',
			name : 'gene_report_starttime',
        	width:'20%',
			fieldLabel : '报告日期从',
			labelWidth : 80,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			listeners:{
				'select':function(){
					var start = gene_report_starttime.getValue();
	                var endDate = gene_report_endtime.getValue();
	                if (start > endDate) {
	                	gene_report_starttime.setValue(endDate);
	                }
				}
			}
		});
		var gene_report_endtime = new Ext.form.DateField({
			//id:'gene_report_endtime',
			name : 'gene_report_endtime',
        	width:'20%',
			labelWidth : 40,
			fieldLabel : '到 ',
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY,1),
			listeners:{
				'select':function(){
					var start = gene_report_starttime.getValue();
	                var endDate = gene_report_endtime.getValue();
	                if (start > endDate) {
	                	gene_report_endtime.setValue(endDate);
	                }
				}
			}
		});
		
		var gene_starttime = new Ext.form.DateField({
			id:'gene_starttime',
			name : 'gene_starttime',
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
					var start = Ext.getCmp('gene_starttime').getValue();
	                var endDate = Ext.getCmp('gene_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('gene_starttime').setValue(endDate);
	                }
				}
			}
		});
		var gene_endtime = new Ext.form.DateField({
			id:'gene_endtime',
			name : 'gene_endtime',
        	width:'20%',
			labelWidth : 40,
			fieldLabel : '到 ',
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY,1),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('gene_starttime').getValue();
	                var endDate = Ext.getCmp('gene_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('gene_starttime').setValue(endDate);
	                }
				}
			}
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','add_time','consumer_name','consumer_sex','consumer_birthday','consumer_phone','sample_number','test_number','report_date','test_package_id','test_package_name',
			        'agency_id','agency_name','sprice','sconsumer_sex','test_item_ids','test_item_names', 'charge_standard_id','charge_standard_name','price','expressnum','expresstype','remark','confirm_flag','age',
		            'hospital', 'office','sample_type', 'admission_num','sample_status','doctor', 'bed_num','genetic_test_id'],
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
            		Gene = "consumer_name=" + consumer_name.getValue().trim() + "&sample_number="
		    				+ sample_number.getValue().trim() + "&gene_starttime="
		    				+ dateformat(gene_starttime.getValue())
		    				+ "&gene_endtime="
		    				+ dateformat(gene_endtime.getValue())
		    				+ "&test_number=" + test_number.getValue()
		    				+ "&test_package_name=" + test_package_name.getValue() + "&test_item_names="
		    				+ test_item_names.getValue() + "&charge_standard_name="
		    				+ charge_standard_name.getValue()+ "&reportif=" + reportif.getValue()
		    				+ "&consumer_sex="+consumer_sex.getValue().trim();
            		Ext.apply(me.store.proxy.params, {
            			consumer_name:consumer_name.getValue().trim(),
            			sample_number:sample_number.getValue().trim(),
            			gene_starttime:dateformat(gene_starttime.getValue()),
            			gene_endtime:dateformat(gene_endtime.getValue()),
            			test_number:test_number.getValue(),
            			test_package_name:test_package_name.getValue(),
            			//agency_name:agency_name.getValue().trim(),
            			test_item_names:test_item_names.getValue(),
            			reportif:reportif.getValue(),
            			charge_standard_name:charge_standard_name.getValue().trim(),
        				consumer_sex:consumer_sex.getValue().trim()
        				//consumer_phone:consumer_phone.getValue().trim()
        				});
        		}
            }
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			//mode: 'SINGLE'
		});
		//分页的combobox下拉选择显示条数
	    combo = Ext.create('Ext.form.ComboBox',{
	          name: 'pagesize',
	          hiddenName: 'pagesize',
	          store: new Ext.data.ArrayStore({
	              fields: ['text', 'value'],
	              data: [['15', 15], ['30', 30],['60', 60], ['80', 80], ['100', 100]]
	          }),
	          valueField: 'value',
	          displayField: 'text',
	          emptyText:15,
	          width: 50
	     });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

       //添加下拉显示条数菜单选中事件
       combo.on("select", function (comboBox) {
       var pagingToolbar=Ext.getCmp('pagingbarGeneTest');
         	pagingToolbar.pageSize = parseInt(comboBox.getValue());
         	itemsPerPage = parseInt(comboBox.getValue());//更改全局变量itemsPerPage
         	me.store.pageSize = itemsPerPage;//设置store的pageSize，可以将工具栏与查询的数据同步。
         	me.store.load({  
         		params:{  
                 start:0,  
                 limit: itemsPerPage
         		}  
         	});//数据源重新加载
         	me.store.loadPage(1);//显示第一页
        });
       
		me.bbar = Ext.create('Ext.PagingToolbar', {
			id:'pagingbarGeneTest',
			store: me.store,
			pageSize:me.pageSize,
			displayInfo: true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录",
	   	 	items: ['-', '每页显示',combo,'条']
		});
//		me.bbar = Ext.create('Ext.PagingToolbar', {
//			store : me.store,
//			pageSize : me.pageSize,
//			displayInfo : true,
//			displayMsg : "第 {0} - {1} 条  共 {2} 条",
//			emptyMsg : "没有符合条件的记录"
//		});
		//me.bbar = {xtype: 'label',id:'totalBbar_GeneTP', text: '',style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
		me.columns = [
                  { text: '案例编号',	dataIndex: 'test_number', menuDisabled:true, width : 120},
                  { text: '样本编号',	dataIndex: 'sample_number', menuDisabled:true, width : 120},
                  { text: '检测套餐名称',	dataIndex: 'test_package_name', menuDisabled:true, width : 170},
                  { text: '登记时间',	dataIndex: 'add_time', menuDisabled:true, width : 120,
                	  renderer:Ext.util.Format.dateRenderer('Y-m-d')
                  },
                  { text: '客户姓名',	dataIndex: 'consumer_name', menuDisabled:true, width : 80},
                  { text: '性别',	dataIndex: 'consumer_sex', menuDisabled:true, width : 60,
                	  renderer:function(value,meta,record){
                		  switch(value){
                		  case 'F':
                			  return "女";
                			  break;
                		  case 'M':
                			  return "男";
								break;
                		  }
                	  }  
                  },
                  { text: '客户生日',	dataIndex: 'consumer_birthday', menuDisabled:true, width : 80},
                  { text: '归属人全称',	dataIndex: 'charge_standard_name', menuDisabled:true, width : 225},
                  { text: '参考价格',	dataIndex: 'price',menuDisabled:true, width : 70, renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						var isnull= record.data["price"];
						if(null !=isnull){
							value=isnull/100;
							return value;
						}
							
						}
					},    
				 { text: '参考价格',	dataIndex: 'sprice',menuDisabled:true, width : 80,hidden:true},
				 { text: '性别',	dataIndex: 'sconsumer_sex',menuDisabled:true, width : 80,hidden:true},
				  { text: '备注',	dataIndex: 'remark', menuDisabled:true, width : 150},
	              { text: '检测项名称',	dataIndex: 'test_item_names', menuDisabled:true, width : 200},
                  { text: '是否发报告',	dataIndex: 'reportif', menuDisabled:true, width : 600,
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
                	  
                  }
	              ];

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
		 		items:[sample_number,gene_starttime,gene_endtime,reportif]
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
		 			text:'导出',
		 			iconCls:'Application',
		 			handler:me.medExamineExport
		 		},{
		 			text:'案例详情',
		 			iconCls:'Find',
		 			handler:me.casef
		 	},{
	 			text:'案例编号',
	 			iconCls:'Find',
	 			handler:me.caseCode
	 	}]
	 	
	 	
	 	}];
		me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbar_GeneTP').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	//新增
	medExamineExport:function(){
		window.location.href = "bacera/Gene/exportGeneInfo.do?"+Gene ;
	},
	casef:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择一条记录!");
			return;
		};
		if(null==selections[0].get("sample_type")){
			var form = Ext.create("Rds.bacera.form.BaceraGeneTestingProjectForm",{
				region:"center",
				grid:me
			});
			form.loadRecord(selections[0]);
			var win = Ext.create("Ext.window.Window",{
				title:'案例详情',
				width:650,
				iconCls:'Pageedit',
				modal:true,
				height:490,
				layout:'border',	
				items:[form]
			});
			win.show();
			}else
			{
				var form = Ext.create("Rds.bacera.form.BaceraGeneTestingProjectAllForm",{
					region:"center",
					grid:me
				});
				form.loadRecord(selections[0]);
				var win = Ext.create("Ext.window.Window",{
					title:'案例详情',
					width:650,
					iconCls:'Pageedit',
					modal:true,
					height:610,
					layout:'border',	
					items:[form]
				});
				win.show();
				
			}
			
		
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
	}
	
});