var rowEditing = Ext.create('Ext.grid.plugin.CellEditing',{
            pluginId:'CellEditing',
            saveBtnText: '保存', 
            cancelBtnText: "取消", 
            autoCancel: false, 
            clicksToEdit:1   //双击进行修改  1-单击   2-双击    0-可取消双击/单击事件
});
var acounttype=Ext.create(
		'Ext.data.Store',
		{
			fields:['remark'],
			proxy:{
				type: 'jsonajax',
				actionMethods:{read:'POST'},
				url:'judicial/bank/queryallpage.do',
				params:{
					initials:""
				},
				reader:{
					type:'json',
					root:'data'
				}
			},
			autoLoad:true,
			remoteSort:true						
		}
)
var remittanceAccountStore=Ext.create('Ext.data.Store',{
      fields:['accountName'],
      proxy:{
		type: 'jsonajax',
		actionMethods:{read:'POST'},
        url:'judicial/remittance/queryall.do',
        reader:{
          type:'json',
          root:'data'
        }
      },
      autoLoad:true,
      remoteSort:true						
    })
Ext.define('Rds.bacera.panel.BaceraGeneTestingProjectFinanceGrid', {
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
		var remittanceAccountStore=Ext.create('Ext.data.Store',
		        {
		          fields:['accountName'],
		          proxy:{
		    		type: 'jsonajax',
		    		actionMethods:{read:'POST'},
		            url:'judicial/remittance/queryall.do',
		            reader:{
		              type:'json',
		              root:'data'
		            }
		          },
		          autoLoad:true,
		          remoteSort:true						
		        }
		   );
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
			labelWidth : 60,
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
        	width:'19.5%',
	    	fieldLabel:'归属人姓名'
	    });
	    var receivables = Ext.create('Ext.form.field.Text',{
	    	name:'receivables',
	    	labelWidth:60,
        	width:'20%',
	    	fieldLabel:'标准价格'
	    });

		var gene_finance_starttime = new Ext.form.DateField({
			name : 'gene_finance_starttime',
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
					var start = gene_finance_starttime.getValue();
	                var endDate = gene_finance_endtime.getValue();
	                if (start > endDate) {
	                	gene_finance_starttime.setValue(endDate);
	                }
				}
			}
		});
		var gene_finance_endtime = new Ext.form.DateField({
			name : 'gene_finance_endtime',
        	width:'20%',
			labelWidth : 40,
			fieldLabel : '到 ',
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY,1),
			listeners:{
				'select':function(){
					var start = gene_finance_starttime.getValue();
	                var endDate = gene_finance_endtime.getValue();
	                if (start > endDate) {
	                	gene_finance_starttime.setValue(endDate);
	                }
				}
			}
		});
		var acounttype=Ext.create(
				'Ext.data.Store',
				{
					fields:['remark'],
					proxy:{
						type: 'jsonajax',
						actionMethods:{read:'POST'},
						url:'judicial/bank/queryallpage.do',
						params:{
							initials:""
						},
						reader:{
							type:'json',
							root:'data'
						}
					},
					autoLoad:true,
					remoteSort:true						
				}
		);
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','add_time','consumer_name','consumer_sex','consumer_birthday','consumer_phone','sample_number','test_number','report_date','test_package_id','test_package_name',
			        'agency_id','agency_name','test_item_ids','test_item_names', 'charge_standard_id','charge_standard_name','price', 'receivables','payment',
			        'paid','discountPrice','paragraphtime','account_type',
			        'remarks','remittanceName','remittanceDate','expressnum','expresstype','remark','confirm_flag'],
		        start:0,
				limit:15,
				pageSize:15,
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
            			gene_starttime:dateformat(gene_finance_starttime.getValue()),
            			gene_endtime:dateformat(gene_finance_endtime.getValue()),
            			test_number:test_number.getValue(),
            			test_package_name:test_package_name.getValue(),
            			//agency_name:agency_name.getValue().trim(),
            			test_item_names:test_item_names.getValue(),
            			charge_standard_name:charge_standard_name.getValue().trim(),
            			receivables:receivables.getValue(),
        				consumer_sex:consumer_sex.getValue().trim(),
        				//consumer_phone:consumer_phone.getValue().trim()
        				
            		});
        		}
            }
		});
		
//		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
////			mode: 'SINGLE'
//		});
//		me.bbar = Ext.create('Ext.PagingToolbar', {
//			store : me.store,
//			pageSize : me.pageSize,
//			displayInfo : true,
//			displayMsg : "第 {0} - {1} 条  共 {2} 条",
//			emptyMsg : "没有符合条件的记录"
//		});
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
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
	        var pagingToolbar=Ext.getCmp('pagingbarGeneTestFinance');
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
				id:'pagingbarGeneTestFinance',
				store: me.store,
				pageSize:me.pageSize,
				displayInfo: true,
				displayMsg : "第 {0} - {1} 条  共 {2} 条",
		   	 	emptyMsg : "没有符合条件的记录",
		   	 	items: ['-', '每页显示',combo,'条']
	       });
		//me.bbar = {xtype: 'label',id:'totalBbarGene_finance', text: '',style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
		me.columns = [
	                  { text: '案例编号',	dataIndex: 'test_number', menuDisabled:true, width : 100,
	                	  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var confirm_flag = record.data["confirm_flag"];
								if (confirm_flag == 2) {
									return "<div style=\"color: green;\">"
											+ value + "</div>"
								} else {
									return value;
								}
							}
	              },
	                  { text: '样本编号',	dataIndex: 'sample_number', menuDisabled:true, width : 120},
	                  { text: '检测套餐名',	dataIndex: 'test_package_name', menuDisabled:true, width : 170},
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
		              { text: '标准价格',	dataIndex: 'receivables',menuDisabled:true, width : 70,
	                	  editor:new Ext.form.NumberField()
		              },
	                  { text: '所付款项',	dataIndex: 'payment', menuDisabled:true, width : 70,
	                	  editor:new Ext.form.NumberField()
	                  },
	                  { text: '到款时间',	dataIndex: 'paragraphtime', menuDisabled:true, width : 95,
	                	  renderer:Ext.util.Format.dateRenderer('Y-m-d'),
	                      editor:new Ext.form.DateField({
	                           format: 'Y-m-d'
	                         })
	                  },
	                  { text: '账户类型',	dataIndex: 'account_type', menuDisabled:true, width : 70,
	                	  editor:new Ext.form.ComboBox({
	          				autoSelect : true,
	          				editable:true,
	          		        name:'account_type',
	          		        triggerAction: 'all',
	          		        queryMode: 'local', 
	          		        emptyText : "请选择",
	          		        selectOnTab: true,
	          		        store: acounttype,
	          		        maxLength: 50,
	          		        fieldStyle: me.fieldStyle,
	          		        displayField:'remark',
	          		        valueField:'remark',
	          		        listClass: 'x-combo-list-small'
	          			})
	                  },
	                  { text: '财务备注',	dataIndex: 'remarks', menuDisabled:true, width : 120,
	                	  editor:'textfield'
	                  },
	                  { text: '汇款账户',	dataIndex: 'remittanceName', menuDisabled:true, width : 80,
	                	  editor:new Ext.form.ComboBox({
	            				autoSelect : true,
	            				editable:true,
	            		        name:'remittanceName',
	            		        triggerAction: 'all',
	            		        queryMode: 'local', 
	            		        emptyText : "请选择",
	            		        selectOnTab: true,
	            		        store: remittanceAccountStore,
	            		        maxLength: 70,
	            		        fieldStyle: me.fieldStyle,
	            		        displayField:'accountName',
	            		        valueField:'accountName',
	            		        listClass: 'x-combo-list-small'
	            			})
	                    },
	                    { text: '汇款时间',	dataIndex: 'remittanceDate', menuDisabled:true, width : 95,
	                    	renderer:Ext.util.Format.dateRenderer('Y-m-d'),
	                    	editor:new Ext.form.DateField({
	                            format: 'Y-m-d'
	                          })
	                    },
	                    {
	            			header : '优惠价格',
	            			dataIndex : 'discountPrice',
	            			width : 70,
	            			editor:new Ext.form.NumberField()
	            		  },
	                  { text: '登记日期',	dataIndex: 'add_time', menuDisabled:true, width:120,
	            			  renderer:Ext.util.Format.dateRenderer('Y-m-d')
	            		  },
	                  { text: '客户姓名',	dataIndex: 'consumer_name', menuDisabled:true, width:70},
	                  { text: '市场归属人全称',	dataIndex: 'charge_standard_name', menuDisabled:true, width : 225},
	                  { text: '被代理人',	dataIndex: 'agentname', menuDisabled:true, width : 70},
	                  { text: '备注',	dataIndex: 'remark', menuDisabled:true, width : 150},
		              { text: '检测项目名',	dataIndex: 'test_item_names', menuDisabled:true, width:150}, 
	                  { text: '是否发报告',	dataIndex: 'reportif', menuDisabled:true, width : 200,
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
	                  }  ];
		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[test_item_names,test_package_name,sample_number,receivables]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[gene_finance_starttime,gene_finance_endtime,consumer_name,consumer_sex]
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
			 			handler:me.caseCode
		 			},{
			 			text:'批量插入',
			 			iconCls:'Add',
			 			handler:me.inserFinance
			 		},{
			 			text:'案例确认',
			 			iconCls:'Pageedit',
			 			handler:me.confirmFinanceList
			 		},{
			 			text:'补款',
			 			iconCls:'Add',
			 			handler:me.caseFeeAdd
			 		}]
	 	}];
		//me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbarGene_finance').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	caseFeeAdd:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要补款的案例!");
			return;
		}
		if(selections[0].get("confirm_flag")!='2')
		{
			Ext.Msg.alert("提示", "该案例未确认!");
			return;
		}
		casefee_save = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["id"]=selections[0].get("id");
			values["num"]=selections[0].get("num");
			values["case_type"]="健康体检";
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				console.log(values);
				Ext.Ajax.request({
							url : "finance/bacera/insert.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response) {
									Ext.MessageBox.alert("提示信息", "操作成功!");
									me.getStore().load();
									casefee_add.close();
								} else {
									Ext.MessageBox.alert("错误信息", "操作失败，请联系管理员！");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败,查看是否已录过补款<br>请联系管理员!");
							}
						});
			}
		}
		casefee_canel = function(me) {
			me.up("window").close();
		}
		var casefee_add = Ext.create("Ext.window.Window", {
			title : '案例补款',
			width : 300,
			height : 250,
			layout : 'border',
			modal:true,
			items : [{
				xtype : 'form',
				region : 'center',
				style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
				labelAlign : "right",
				bodyPadding : 10,
				defaults : {
					anchor : '100%',
					labelWidth : 80,
					labelAlign : 'right'
				},
				border : false,
				autoHeight : true,
				buttons : [{
							text : '保存',
							iconCls : 'Disk',
							handler : casefee_save
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : casefee_canel
						}],
				items : [{
						xtype : "numberfield",
						fieldLabel : '补款价格',
						labelAlign : 'right',
						maxLength : 200,
						labelWidth : 80,
						name : 'receivables',
		    	        allowBlank:false, //不允许为空
		    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
				}]
			}]
		})
		casefee_add.show();
	},
	inserFinance:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要回款的案例!");
			return;
		};
		var ids="";
		var num="";
		var receivables="";
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if(selections[i].get("confirm_flag")=='2')
			{
				Ext.Msg.alert("提示", "存在已确认案例!");
				return;
			}
			ids += selections[i].get("id")+",";
			num += selections[i].get("test_number")+",";
			receivables+=selections[i].get("price")/100+",";
			console.log(receivables);
		}
		ids = ids.substring(0,ids.length-1);
		num = num.substring(0,num.length-1);
		receivables = receivables.substring(0,receivables.length-1);
		casefee_save = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["ids"]=ids;
			values["num"]=num;
			values["receivables"]=receivables;
			values["case_type"]="健康体检";
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "bacera/boneAge/saveFinanceList.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response.result) {
									Ext.MessageBox.alert("提示信息", "批量操作成功!");
									me.getStore().load();
									casefee_add.close();
								} else {
									Ext.MessageBox.alert("错误信息", "批量操作失败，请联系管理员！");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
			}
		}
		casefee_canel = function(me) {
			me.up("window").close();
		}
		var casefee_add = Ext.create("Ext.window.Window", {
			title : '批量回款',
			width : 400,
			height : 325,
			layout : 'border',
			modal:true,
			items : [{
				xtype : 'form',
				region : 'center',
				style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
				labelAlign : "right",
				bodyPadding : 10,
				defaults : {
					anchor : '100%',
					labelWidth : 80,
					labelAlign : 'right'
				},
				border : false,
				autoHeight : true,
				buttons : [{
							text : '保存',
							iconCls : 'Disk',
							handler : casefee_save
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : casefee_canel
						}],
				items : [{
					xtype : "datefield",
					fieldLabel : '到款时间',
					labelAlign : 'right',
					format : 'Y-m-d',
					value : Ext.Date.add(
							new Date(),
							Ext.Date.DAY),
					maxValue:Ext.Date.add(
							new Date(),
							Ext.Date.DAY),
					labelWidth : 80,
					name : 'paragraphtime',
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
				},new Ext.form.ComboBox({
          				autoSelect : true,
          				editable:true,
          		        name:'account_type',
          		        triggerAction: 'all',
    					fieldLabel : '账户类型',
          		        queryMode: 'local', 
          		        emptyText : "请选择",
          		        selectOnTab: true,
          		        store: acounttype,
    					labelWidth : 80,
          		        maxLength: 50,
    					labelAlign : 'right',
          		        fieldStyle: me.fieldStyle,
          		        displayField:'remark',
          		        valueField:'remark',
        				allowBlank : false,// 不允许为空
        				blankText : "不能为空",// 错误提示信息，默认为This field is required!
          			}),new Ext.form.ComboBox({
        				autoSelect : true,
        				editable:true,
    					fieldLabel : '汇款帐户',
        		        name:'remittanceName',
        		        triggerAction: 'all',
    					labelAlign : 'right',
        		        queryMode: 'local', 
    					labelWidth : 80,
        		        emptyText : "请选择",
        		        selectOnTab: true,
        		        store: remittanceAccountStore,
        		        maxLength: 50,
        		        fieldStyle: me.fieldStyle,
        		        displayField:'accountName',
        		        valueField:'accountName',
    					allowBlank : false,// 不允许为空
    					blankText : "不能为空",// 错误提示信息，默认为This field is required!
        			}),
        			{
    					xtype : "datefield",
    					fieldLabel : '汇款时间',
    					labelAlign : 'right',
    					format : 'Y-m-d',
    					value : Ext.Date.add(
    							new Date(),
    							Ext.Date.DAY),
    					maxValue:Ext.Date.add(
    							new Date(),
    							Ext.Date.DAY),
    					labelWidth : 80,
    					name : 'remittanceDate',
    					allowBlank : false,// 不允许为空
    					blankText : "不能为空",// 错误提示信息，默认为This field is required!
    				},{
						xtype : "textarea",
						fieldLabel : '财务备注',
						labelAlign : 'right',
						maxLength : 200,
						labelWidth : 80,
						name : 'remarks'
				}]
			}]
		})
		casefee_add.show();
	},
	confirmFinanceList:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要确认的案例!");
			return;
		};
		var ids='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			ids += selections[i].get("id")+",";
			if(selections[i].get("confirm_flag")=='2'){
				Ext.Msg.alert("提示", "存在已确认案例!");
				return;
			}
		}
		ids = ids.substring(0,ids.length-1);
		Ext.MessageBox.confirm("提示", "确认选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在操作','请稍后...'); 	
	    		Ext.Ajax.request({
	    			url : "bacera/boneAge/confirmFinanceList.do",
	    			method : "POST",
	    			headers : {
	    				'Content-Type' : 'application/json'
	    			},
	    			jsonData : {ids:ids},
	    			success : function(response, options) {
	    				response = Ext.JSON
	    						.decode(response.responseText);
	    				if (response.result) {
	    					Ext.MessageBox.alert("提示信息", "操作成功!");
	    					me.getStore().load();
	    				} else {
	    					Ext.MessageBox.alert("错误信息", "操作失败，请联系管理员！");
	    				}
	    			},
	    			failure : function() {
	    				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
	    			}
	    		});
	        }
	    });
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
	casef:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择一条记录!");
			return;
		};
			var form = Ext.create("Rds.bacera.form.BaceraGeneTestingProjectFinanceForm",{
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
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	},
	listeners : {
		'beforeedit':function(editor, e,s){
			if(!('masl' == usercode || 'xieh' == usercode  || 'sq_wangyan'==usercode || 'admin'==usercode))
				return false;
			else{
				if(e.record.data.confirm_flag=='2' )
					return false;
			}
			rowEditing.on('edit',afterEdit);
//			if(!('masl' == usercode || 'xieh' == usercode  || 'sq_wangyan'==usercode || 'admin'==usercode))
//				return false;
		},
		'afterrender' : function() {
			this.store.load();
		}
	}
});

function afterEdit(e,s){
 	Ext.Ajax.request({  
		url:"bacera/Gene/saveGeneFinance.do", 
		method: "POST",
		headers: { 'Content-Type': 'application/json' },
		jsonData: {
			id:s.record.data.id,
			num:s.record.data.test_number==null?'':s.record.data.test_number,
			receivables:s.record.data.receivables==null?'':s.record.data.receivables,
			payment:s.record.data.payment==null?'':s.record.data.payment,
			paragraphtime:Ext.util.Format.date(s.record.data.paragraphtime, 'Y-m-d'),
			discountPrice:s.record.data.discountPrice==null?'':s.record.data.discountPrice,
			case_type:s.record.data.test_item_names,
			account_type:s.record.data.account_type==null?'':s.record.data.account_type,
			remarks:s.record.data.remarks==null?'':s.record.data.remarks,
			remittanceName:s.record.data.remittanceName==null?'':s.record.data.remittanceName,
			remittanceDate:Ext.util.Format.date(s.record.data.remittanceDate, 'Y-m-d')
		}, 
		success: function (response, options) {  
			response = Ext.JSON.decode(response.responseText); 
             if (response==false) {  
                 Ext.MessageBox.alert("错误信息", "修改收费失败，请查看");
             }
		},  
		failure: function () {
			Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
		}
  	});
}
