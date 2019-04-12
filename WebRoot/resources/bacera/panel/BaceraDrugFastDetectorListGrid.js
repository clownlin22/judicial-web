var drugDetection="";
Ext.define('Rds.bacera.panel.BaceraDrugFastDetectorListGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var address = Ext.create('Ext.form.field.Text',{
        	name:'address',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'发货地址'
        });
		var num = Ext.create('Ext.form.field.Text',{
        	name:'num',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'案例编号'
        });
		var person = Ext.create('Ext.form.field.Text',{
	    	name:'person',
	    	labelWidth:60,
	    	width:'20%',
	    	fieldLabel:'负责人'
	    });
		var reagent_type = Ext.create('Ext.form.field.Text',{
	    	name:'reagent_type',
	    	labelWidth:65,
	    	width:'20%',
	    	fieldLabel:'试剂种类'
	    });
		
		var reagent_count = Ext.create('Ext.form.field.Text',{
	    	name:'reagent_count',
	    	labelWidth:60,
	    	width:'20%',
	    	fieldLabel:'试剂数量'
	    });


		var fastDetection_starttime = new Ext.form.DateField({
			name : 'fastDetection_starttime',
			width:'20%',
			fieldLabel : '日 期  从',
			labelWidth : 60,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
		});
		var fastDetection_endtime = new Ext.form.DateField({
			name : 'fastDetection_endtime',
			width:'20%',
			
			labelWidth : 60,
			fieldLabel : ' 到 ',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value :Ext.Date.add(
					new Date(),
					Ext.Date.DAY,1),
		});
		var cancelif=new Ext.form.field.ComboBox({
			fieldLabel : '是否作废',
			width:'20%',
			labelWidth : 65,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部',0],['是',1 ],['否',2 ] ]
					}),
			value : '',
			mode : 'local',
			name : 'cancelif',
			value: 0
		});
		var trial_type=new Ext.form.field.ComboBox({
							columnWidth : .50,
							xtype: 'combo',
							autoSelect : true,
							editable:false,
							labelWidth : 65,
							fieldLabel:'试用类型',
					        name:'trial_type',
					        triggerAction: 'all',
					        queryMode: 'local', 
					        emptyText : "请选择",
					        allowBlank:true, //不允许为空
							blankText:"不能为空", //错误提示信息，默认为This field is required! 
					        selectOnTab: true,
					        store: Ext.create('Ext.data.Store', { 
					        	fields: ['id', 'name'], 
					        	data : [{"id":"0","name":"全部"},
					        	        {"id":"1","name":"采购"},
					        	         {"id":"2","name":"试用"},
					        	         {"id":"3","name":"销售"}]
					        }),
					        fieldStyle: me.fieldStyle,
					        displayField:'name',
					        valueField:'id',
					        value:'0',
					        listClass: 'x-combo-list-small'
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','address','person','date','reagent_type','cancelif','reagent_count','trial_type','input','remark'],
	        start:0,
			limit:15,
			pageSize:15,
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/fastDetection/queryallpage.do',
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
            		drugDetection = "num="+num.getValue().trim() + 
            			            "&address="+address.getValue().trim() + 
            						"&person="+person.getValue().trim() + 
            						"&fastDetection_starttime="+dateformat(fastDetection_starttime.getValue())+
            						"&fastDetection_endtime="+dateformat(fastDetection_endtime.getValue())+ 
            						"&cancelif="+cancelif.getValue() + 
            						"&trial_type="+trial_type.getValue()+
            						"&reagent_type="+reagent_type.getValue().trim();
            		Ext.apply(me.store.proxy.params, {
            			address:address.getValue().trim(),
            			person:person.getValue().trim(),
            			fastDetection_starttime:dateformat(fastDetection_starttime.getValue()),
            			fastDetection_endtime:dateformat(fastDetection_endtime.getValue()),
        				cancelif:cancelif.getValue(),
        				reagent_type:reagent_type.getValue().trim(),
        				trial_type:trial_type.getValue(),
        				num:num.getValue()
        				});
        		}
            }
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode: 'SINGLE'
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
	          width: 60
	     });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

       //添加下拉显示条数菜单选中事件
       combo.on("select", function (comboBox) {
       var pagingToolbar=Ext.getCmp('pagingbarFastDetection');
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
			id:'pagingbarFastDetection',
			store: me.store,
			pageSize:me.pageSize,
			displayInfo: true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录",
	   	 	items: ['-', '每页显示',combo,'条']
		});

		me.columns = [
		              { text: '案例编号',	dataIndex: 'num', menuDisabled:true, width : 100,
		                	  renderer : function(value, cellmeta,
										record, rowIndex, columnIndex,
										store) {
									var isnull= record.data["cancelif"];
									if (isnull == 1) {
										return "<div style=\"text-decoration: line-through;color: red;\">"
												+ value + "</div>"
									} else {
										return value;
									}

								}
		              },
		              { text: '负责人',	dataIndex: 'person', menuDisabled:true, width : 100},
		              { text: '发货地址',	dataIndex: 'address', menuDisabled:true, width : 220},
	                  { text: '发货日期',	dataIndex: 'date', menuDisabled:true, width : 120},
	                  { text: '试剂种类',	dataIndex: 'reagent_type', menuDisabled:true, width : 150},
	                  { text: '试剂数量',	dataIndex: 'reagent_count', menuDisabled:true, width : 120},
	                  { text: '试用类型',	dataIndex: 'trial_type', menuDisabled:true, width : 150,
	                	  renderer : function(value) {
								switch (value) {
								case "1":
									return "采购";
									break;
								case "2":
									return "试用";
									break;
								case "3":
									return "销售";
									break;
								}
							}},
	                  { text: '登记人',	  dataIndex: 'input', menuDisabled:true, width : 100},
	                  { text: '备注要求',	dataIndex: 'remark', menuDisabled:true, width : 2000}
		              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[num,address,person,reagent_type,trial_type]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[fastDetection_starttime,fastDetection_endtime,cancelif,{
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
	 			text:'作废',
	 			iconCls:'Cancel',
	 			handler:me.onCancel
	 		},{
	 			text:'导出',
	 			iconCls:'Application',
	 			handler:me.drugDetectionExport
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbar_drugDetection').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	//新增
	onInsert:function(){
		var me = this.up("gridpanel");
		ownpersonTemp = "";
		confirm_flagTemp="";
		var form = Ext.create("Rds.bacera.form.BaceraDrugFastDetectorListForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'毒品快检仪—登记',
			width:680,
			iconCls:'Add',
			height:400,
			modal:true,
			layout:'border',
			items:[form]
		});
		win.show();
	},
	drugDetectionExport:function(){
		window.location.href = "bacera/fastDetection/exportDrugDetection.do?"+drugDetection ;
	},
	onDelete:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
			return;
		};
		var values = {
				id:selections[0].get("id")
		};
		var tempStr = '';
		if(null != selections[0].get("receivables") || null != selections[0].get("expresstype"))
		{
			tempStr='该条记录包含财务或快递信息，'
		}
		Ext.MessageBox.confirm("提示", tempStr+"确认删除选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在保存','请稍后...');
	    		Ext.Ajax.request({  
	    			url:"bacera/fastDetection/delete.do", 
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
	    				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
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
		if(selections[0].get("cancelif") == '1')
		{
			Ext.Msg.alert("提示", "该记录已作废，不能修改!");
			return;
		}
		ownpersonTemp = selections[0].get("ownperson");
		confirm_flagTemp = selections[0].get("confirm_flag");
		var form = Ext.create("Rds.bacera.form.BaceraDrugFastDetectorListForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'毒品快检仪—修改',
			width:550,
			iconCls:'Pageedit',
			height:400,
			modal:true,
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
	onCancel:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要作废的记录!");
			return;
		};
		if(selections[0].get("cancelif") == '1')
		{
			Ext.Msg.alert("提示", "该记录已作废!");
			return;
		}
		var values = {
				id:selections[0].get("id"),
				cancelif:"1",
				date:selections[0].get("date"),
		};
		Ext.MessageBox.confirm("提示", "确认作废选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在保存','请稍后...');
	    		Ext.Ajax.request({  
	    			url:"bacera/fastDetection/save.do", 
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
	    				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
	    			}
	        	       
	          	});
	        }
	    });
	}
	
});