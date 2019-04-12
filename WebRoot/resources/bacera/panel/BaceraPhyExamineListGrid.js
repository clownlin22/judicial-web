var phyExamine="";
Ext.define('Rds.bacera.panel.BaceraPhyExamineListGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var num = Ext.create('Ext.form.field.Text',{
			name:'num',
			labelWidth:70,
			width:'20%',
			fieldLabel:'案例编号'
		});
		var name = Ext.create('Ext.form.field.Text',{
			name:'name',
			labelWidth:50,
			width:'20%',
			fieldLabel:'姓名'
		});
		var ownperson = Ext.create('Ext.form.field.Text',{
			name:'ownperson',
			labelWidth:50,
			width:'20%',
			fieldLabel:'归属人'
		});
		var agent = Ext.create('Ext.form.field.Text',{
			name:'agent',
			labelWidth:60,
			width:'20%',
			fieldLabel:'被代理人'
		});
		var phyExamine_starttime = new Ext.form.DateField({
			id:'phyExamine_starttime',
			name : 'phyExamine_starttime',
			width:'20%',
			fieldLabel : '日 期  从',
			labelWidth : 50,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('phyExamine_starttime').getValue();
		            var endDate = Ext.getCmp('phyExamine_endtime').getValue();
		            if (start > endDate) {
		                Ext.getCmp('phyExamine_starttime').setValue(endDate);
		            }
				}
			}
		});
		var phyExamine_endtime = new Ext.form.DateField({
			id:'phyExamine_endtime',
			name : 'phyExamine_endtime',
			width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('phyExamine_starttime').getValue();
		            var endDate = Ext.getCmp('phyExamine_endtime').getValue();
		            if (start > endDate) {
		                Ext.getCmp('phyExamine_starttime').setValue(endDate);
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
		var sex=new Ext.form.field.ComboBox({
			fieldLabel : '性别',
			width:'20%',
			labelWidth : 40,
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
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','sex','name','model_type',
			        'date','ownpersonname','ownperson','agentname','reportif','remark','cancelif',
			        'expresstype','receivables','areaname','confirm_flag'],
	        start:0,
			limit:15,
			pageSize:15,
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/phyExamine/queryallpage.do',
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
            		phyExamine = "num=" + num.getValue().trim() + "&name="
							+ name.getValue().trim() + "&ownperson="
							+ ownperson.getValue().trim() + "&phyExamine_starttime="
							+ dateformat(phyExamine_starttime.getValue())
							+ "&phyExamine_endtime="
							+ dateformat(phyExamine_endtime.getValue())
							+ "&reportif=" + reportif.getValue()
							+ "&cancelif=" + cancelif.getValue() + "&agent="
							+ agent.getValue().trim() + "&areacode="
							+ areacode.getValue() +"&sex="
							+ sex.getValue();
            		Ext.apply(me.store.proxy.params, {
		        			num:trim(num.getValue()),
		        			name:trim(name.getValue()),
		        			ownperson:trim(ownperson.getValue()),
		        			phyExamine_starttime:dateformat(phyExamine_starttime.getValue()),
		        			phyExamine_endtime:dateformat(phyExamine_endtime.getValue()),
		    				reportif:reportif.getValue(),
		    				cancelif:cancelif.getValue(),
		    				agent:trim(agent.getValue()),
		    				areacode:areacode.getValue(),
		    				sex:sex.getValue()
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
	        var pagingToolbar=Ext.getCmp('pagingbarPhyExamine');
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
				id:'pagingbarPhyExamine',
				store: me.store,
				pageSize:me.pageSize,
				displayInfo: true,
				displayMsg : "第 {0} - {1} 条  共 {2} 条",
		   	 	emptyMsg : "没有符合条件的记录",
		   	 	items: ['-', '每页显示',combo,'条']
	       });
//		me.bbar = {xtype: 'label',id:'totalBbarPhyExamine', text: '',
//				style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
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
	                  { text: '日期',	dataIndex: 'date', menuDisabled:true,width : 80},
	                  { text: '姓名',	dataIndex: 'name', menuDisabled:true, width : 80},
	                  { text: '性别',	dataIndex: 'sex', menuDisabled:true, width : 80},
	                  { text: '套餐类型',	dataIndex: 'model_type', menuDisabled:true, width : 200},
	                  { text: '市场归属人',	dataIndex: 'ownpersonname', menuDisabled:true, width : 100},
	                  { text: '被代理人',	dataIndex: 'agentname', menuDisabled:true, width : 80},
	                  { text: '归属地',	dataIndex: 'areaname', menuDisabled:true, width : 150},
	                  { text: '是否发报告',	dataIndex: 'reportif', menuDisabled:true, width : 100,
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
	                  { text: '应收款项',	dataIndex: 'receivables', menuDisabled:true, width : 200},
	                  { text: '备注',	dataIndex: 'remark', menuDisabled:true, width : 200}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[num,name,ownperson,areacode,agent]
	 	},{
	 		style : {
	        borderTopWidth : '0px !important',
	        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[reportif,phyExamine_starttime,phyExamine_endtime,cancelif,sex,{
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
	 			handler:me.phyExamineExport
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbarPhyExamine').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	phyExamineExport:function(){
		window.location.href = "bacera/phyExamine/exportPhyExamine.do?"+phyExamine ;
	},
	//新增
	onInsert:function(){
		var me = this.up("gridpanel");
		ownpersonTemp = "";
		confirm_flagTemp = "";
		var form = Ext.create("Rds.bacera.form.BaceraPhyExamineListForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'健康体检—新增',
			width:560,
			iconCls:'Add',
			modal:true,
			height:400,
			layout:'border',
			items:[form]
		});
		win.show();
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
	    			url:"bacera/phyExamine/delete.do", 
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
			Ext.Msg.alert("提示", "请选择需要更新的记录!");
			return;
		};
		if(selections[0].get("cancelif") == '1')
		{
			Ext.Msg.alert("提示", "该记录已作废，不能修改!");
			return;
		}
		ownpersonTemp = selections[0].get("ownperson");
		confirm_flagTemp = selections[0].get("confirm_flag");
		var form = Ext.create("Rds.bacera.form.BaceraPhyExamineListForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'健康体检—修改',
			width:560,
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
				num:selections[0].get("num")
		};
		Ext.MessageBox.confirm("提示", "确认作废选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在保存','请稍后...');
	    		Ext.Ajax.request({  
	    			url:"bacera/phyExamine/save.do", 
	    			method: "POST",
	    			headers: { 'Content-Type': 'application/json' },
	    			jsonData: values, 
	    			success: function (response, options) {  
	    				response = Ext.JSON.decode(response.responseText); 
	                     if (response.result == true) {  
	                     	Ext.MessageBox.alert("提示信息", "作废成功！");
	                     	me.getStore().load();
	                     }else { 
	                     	Ext.MessageBox.alert("错误信息", "操作失败<br>请联系管理员!");
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