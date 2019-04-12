Ext.define('Rds.bacera.panel.BaceraWuHanMedExamineCheckListGrid', {
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
        	width:200,
        	fieldLabel:'案例编号'
        });
		var ownperson = Ext.create('Ext.form.field.Text',{
	    	name:'ownperson',
	    	labelWidth:60,
	    	width:150,
	    	fieldLabel:'归属人'
	    });
		var name = Ext.create('Ext.form.field.Text',{
	    	name:'name',
	    	labelWidth:40,
	    	width:160,
	    	fieldLabel:'姓名'
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
			            params:{ program_type:'医学检测项(武汉)'},
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
		var agent = Ext.create('Ext.form.field.Text',{
	    	name:'agent',
	    	labelWidth:60,
	    	width:150,
	    	fieldLabel:'被代理人'
	    });
		var medExamine_starttime = new Ext.form.DateField({
			name : 'medExamine_starttime',
			width : 200,
			fieldLabel : '日 期  从',
			labelWidth : 60,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var medExamine_endtime = new Ext.form.DateField({
			name : 'medExamine_endtime',
			width : 160,
			labelWidth : 40,
			fieldLabel : ' 到 ',
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var reportif=new Ext.form.field.ComboBox({
			fieldLabel : '是否发报告',
			width : 150,
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
			width : 150,
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
			fields:['id','num','name','date','program','agentname','ownpersonname','ownperson',
			        'remark','cancelif','reportif','expresstype','receivables','areaname'],
	        start:0,
			limit:15,
			pageSize:15,
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/wuHanMedExamine/queryallpage.do',
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
        				name:name.getValue().trim()
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
//		me.bbar = {xtype: 'label',id:'totalBbar_wuHanMedExamineCheck', text: '',style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
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
                  { text: '日期',	dataIndex: 'date', menuDisabled:true, width : 120},
                  { text: '姓名',	dataIndex: 'name', menuDisabled:true, width : 120},
                  { text: '项目',	dataIndex: 'program', menuDisabled:true, width : 120},
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
		 		items:[medExamine_starttime,medExamine_endtime,cancelif,program,{
	            	text:'查询',
	            	iconCls:'Find',
	            	handler:me.onSearch
	            }]
	 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[{
	 			text:'我是案例编号',
	 			iconCls:'Find',
	 			handler:me.onDelete
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbar_wuHanMedExamineCheck').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	onDelete:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要查看的记录!");
			return;
		};
		Ext.Msg.alert("提示", "案例编号："+selections[0].get("num"));
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	}
	
});