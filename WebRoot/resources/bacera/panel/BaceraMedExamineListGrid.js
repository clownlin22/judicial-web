var medExamine="";
Ext.define('Rds.bacera.panel.BaceraMedExamineListGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var search = Ext.create('Ext.form.field.Text',{
        	name:'num',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'案例编号'
        });
		var diagnosis = Ext.create('Ext.form.field.Text',{
        	name:'diagnosis',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'临床诊断'
        });
		var age = Ext.create('Ext.form.field.Text',{
        	name:'age',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'年龄'
        });
		var hospital = Ext.create('Ext.form.field.Text',{
        	name:'hospital',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'送检医院'
        });
		var sex=new Ext.form.field.ComboBox({
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
						data : [['全部',''],['男','男' ],['女','女' ] ]
					}),
			value : '',
			mode : 'local',
			name : 'sex',
		});
		var sampletype = Ext.create('Ext.form.field.Text',{
        	name:'sampletype',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'样本类型'
        });
		var samplecount = Ext.create('Ext.form.field.Text',{
        	name:'samplecount',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'样本数量'
        });
		var ownperson = Ext.create('Ext.form.field.Text',{
	    	name:'ownperson',
	    	labelWidth:60,
        	width:'20%',
	    	fieldLabel:'归属人'
	    });
		var name = Ext.create('Ext.form.field.Text',{
	    	name:'name',
	    	labelWidth:40,
        	width:'20%',
	    	fieldLabel:'姓名'
	    });
		var barcode = Ext.create('Ext.form.field.Text',{
        	name:'barcode',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'物流条码'
        });
		var program = new Ext.form.ComboBox({
			editable:true,
			labelWidth : 60,
	    	width:'20%',
			fieldLabel : '案例类型',
	        name:'program',
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
	        selectOnTab: true,
	        store: Ext.create('Ext.data.Store',{
			        fields:[{name:'program_name',mapping:'program_name',type:'string'}],
			        proxy:{
			        	type: 'jsonajax',
			            actionMethods:{read:'POST'},
			            params:{ program_type:'医学检测项'},
			            url:'bacera/program/queryall.do',
			            reader:{
			              type:'json',
			              root:'data'
			            }
			          },
			          autoLoad:true,
			          remoteSort:true
			     }
			),
	        fieldStyle: me.fieldStyle,
	        displayField:'program_name',
	        valueField:'program_name',
	        listClass: 'x-combo-list-small',
		});
		
//		var program = Ext.create('Ext.form.ComboBox', {
//			fieldLabel : '案例类型',
//        	width:'20%',
//			labelWidth : 60,
//			editable : true,
//			triggerAction : 'all',
//			valueField :"key",  
//	        displayField: "value", 
//			store: Ext.create(
//			        'Ext.data.Store',
//			        {
//			          model:'model',
//			          proxy:{
//			        	type: 'jsonajax',
//			            actionMethods:{read:'POST'},
//			            url:'bacera/medExamine/queryProgram.do',
//			            reader:{
//			              type:'json',
//			              root:'data'
//			            }
//			          },
//			          autoLoad:true,
//			          remoteSort:true
//			        }
//			),
//			mode : 'local',
//			name : 'program'
//		});
		var agent = Ext.create('Ext.form.field.Text',{
	    	name:'agent',
	    	labelWidth:60,
        	width:'20%',
	    	fieldLabel:'被代理人'
	    });
		var medExamine_starttime = new Ext.form.DateField({
			id:'medExamine_starttime',
			name : 'medExamine_starttime',
        	width:'20%',
			fieldLabel : '快递日期',
			labelWidth : 60,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('medExamine_starttime').getValue();
	                var endDate = Ext.getCmp('medExamine_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('medExamine_starttime').setValue(endDate);
	                }
				}
			}
		});
		var medExamine_endtime = new Ext.form.DateField({
			id:'medExamine_endtime',
			name : 'medExamine_endtime',
        	width:'20%',
			labelWidth : 40,
			fieldLabel : ' 到 ',
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('medExamine_starttime').getValue();
	                var endDate = Ext.getCmp('medExamine_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('medExamine_starttime').setValue(endDate);
	                }
				}
			}
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
		var cancelif=new Ext.form.field.ComboBox({
			fieldLabel : '是否作废',
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
						data : [['全部',0],['是',1 ],['否',2 ] ]
					}),
			value : '',
			mode : 'local',
			name : 'cancelif',
			value: 0
		});
		var areacode= new Ext.form.field.ComboBox({
			fieldLabel : '归属地',
			labelWidth : 60,
        	width:'20%',
			name : 'areacode',
			emptyText:'检索方式：如朝阳区(cy)',
			store : Ext.create("Ext.data.Store",
							{
								 fields:[
						                  {name:'key',mapping:'key',type:'string'},
						                  {name:'value',mapping:'value',type:'string'},
						                  {name:'name',mapping:'name',type:'string'},
						                  {name:'id',mapping:'id',type:'string'},
						          ],
								pageSize : 10,
								// autoLoad: true,
								proxy : {
									type : "ajax",
									url:"judicial/dicvalues/getAreaInfo.do",
									reader : {
										type : "json"
									}
								}
							}),
			displayField : 'value',
			valueField : 'id',
			typeAhead : false,
//			forceSelection: true, 
			hideTrigger : true,
			minChars : 2,
			matchFieldWidth : true,
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
			}
		});
		var itemsPerPage = 20;
		var combo;
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','name','date','program','agentname','ownpersonname','ownperson','barcode','diagnosis','hospital',
			        'age','sex','sampletype','samplecount', 'remark','cancelif','reportif','expresstype','entrustment_date','report_date',
			        'receivables','areaname','confirm_flag'],
	        start : 0,
            limit : itemsPerPage,
            pageSize : itemsPerPage,
	        proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/medExamine/queryallpage.do',
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
            		medExamine = "num=" + search.getValue().trim() + "&ownperson="
		    				+ ownperson.getValue().trim() + "&medExamine_starttime="
		    				+ dateformat(medExamine_starttime.getValue())
		    				+ "&medExamine_endtime="
		    				+ dateformat(medExamine_endtime.getValue())
		    				+ "&reportif=" + reportif.getValue()
		    				+ "&cancelif=" + cancelif.getValue() + "&agent="
		    				+ agent.getValue().trim() + "&areacode="
		    				+ areacode.getValue() + "&program="
		    				+ program.getValue()+"&name="+name.getValue().trim()
		    				+ "&barcode="+barcode.getValue().trim()
		    				+ "&diagnosis="+diagnosis.getValue().trim()
		    				+ "&age="+age.getValue().trim()
		    				+ "&sex="+sex.getValue()
		    				+ "&sampletype="+sampletype.getValue().trim()
		    				+ "&samplecount="+samplecount.getValue().trim()
		    				+ "&hospital="+hospital.getValue().trim();
            		Ext.apply(me.store.proxy.params, {
            			num:search.getValue().trim(),
            			ownperson:ownperson.getValue().trim(),
            			medExamine_starttime:dateformat(medExamine_starttime.getValue()),
            			medExamine_endtime:dateformat(medExamine_endtime.getValue()),
        				reportif:reportif.getValue(),
        				cancelif:cancelif.getValue(),
        				agent:agent.getValue().trim(),
        				areacode:areacode.getValue(),
        				name:name.getValue().trim(),
        				program:program.getValue(),
        				name:name.getValue().trim(),
        				barcode:barcode.getValue().trim(),
        				diagnosis:diagnosis.getValue().trim(),
        				age:age.getValue().trim(),
        				sex:sex.getValue(),
        				sampletype:sampletype.getValue().trim(),
        				samplecount:samplecount.getValue().trim(),
        				hospital:hospital.getValue().trim()
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
	              data: [['20', 20], ['40', 40],['60', 60], ['80', 80], ['100', 100]]
	          }),
	          valueField: 'value',
	          displayField: 'text',
	          emptyText:20,
	          width: 50
	      });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

	   //添加下拉显示条数菜单选中事件
	        combo.on("select", function (comboBox) {
	        var pagingToolbar=Ext.getCmp('pagingbar');
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
				id:'pagingbar',
	           store: me.store,
	           pageSize:me.pageSize,
	           displayInfo: true,
	           displayMsg : "第 {0} - {1} 条  共 {2} 条",
		   	 	emptyMsg : "没有符合条件的记录",
		   	 	items: ['-', '每页显示',combo,'条']
	       });
//		me.bbar = Ext.create('Ext.PagingToolbar', {
//            store: me.store,
//            pageSize:me.pageSize,
//            displayInfo: true,
//            displayMsg : "第 {0} - {1} 条  共 {2} 条",
//	   	 	emptyMsg : "没有符合条件的记录"
//        });
//		me.bbar = {xtype: 'label',id:'totalBbar_medExamine', text: '',style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
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
                  { text: '快递日期',	dataIndex: 'date', menuDisabled:true, width : 120},
                  { text: '姓名',	dataIndex: 'name', menuDisabled:true, width : 120},
                  { text: '物流条码',	dataIndex: 'barcode', menuDisabled:true, width : 120},
                  { text: '临床诊断',	dataIndex: 'diagnosis', menuDisabled:true, width : 120},
                  { text: '年龄',	dataIndex: 'age', menuDisabled:true, width : 120},
                  { text: '性别',	dataIndex: 'sex', menuDisabled:true, width : 120},
                  { text: '样本类型',	dataIndex: 'sampletype', menuDisabled:true, width : 120},
                  { text: '样本数量',	dataIndex: 'samplecount', menuDisabled:true, width : 120},
                  { text: '项目',	dataIndex: 'program', menuDisabled:true, width : 120},
                  { text: '送检医院',	dataIndex: 'hospital', menuDisabled:true, width : 120},
                  { text: '归属地',	dataIndex: 'areaname', menuDisabled:true, width : 150},
                  { text: '市场归属人',	dataIndex: 'ownpersonname', menuDisabled:true, width : 100},
                  { text: '被代理人',	dataIndex: 'agentname', menuDisabled:true, width : 80},
                  { text: '是否发报告',	dataIndex: 'reportif', menuDisabled:true, width : 80,
	            	  renderer : function(value) {
							switch (value) {
							case "1":
								return "是";
								break;
							default:
								return "<span style='color:red'>否</span>";
							}
						}
                	  
                  },
                  { text: '应收款项',	dataIndex: 'receivables', menuDisabled:true, width : 100},
                  { text: '备注要求',	dataIndex: 'remark', menuDisabled:true, width : 200}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[search,name,ownperson,areacode,agent]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[medExamine_starttime,medExamine_endtime,cancelif,program,hospital]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[barcode,diagnosis,age,sex,sampletype]
	 	
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[samplecount,{
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
	 			handler:me.medExamineExport
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbar_medExamine').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	//新增
	onInsert:function(){
		var me = this.up("gridpanel");
		ownpersonTemp = "";
		confirm_flagTemp="";
		var form = Ext.create("Rds.bacera.form.BaceraMedExamineListForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'医学检验—新增',
			width:550,
			iconCls:'Add',
			height:500,
			modal:true,
			layout:'border',
			items:[form]
		});
		win.show();
	},
	medExamineExport:function(){
		window.location.href = "bacera/medExamine/exportMedExamine.do?"+medExamine ;
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
	    			url:"bacera/medExamine/delete.do", 
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
		var form = Ext.create("Rds.bacera.form.BaceraMedExamineListForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'医学检测—修改',
			width:550,
			iconCls:'Pageedit',
			height:500,
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
				num:selections[0].get("num"),
				program:selections[0].get("program")
		};
		Ext.MessageBox.confirm("提示", "确认作废选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在保存','请稍后...');
	    		Ext.Ajax.request({  
	    			url:"bacera/medExamine/save.do", 
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