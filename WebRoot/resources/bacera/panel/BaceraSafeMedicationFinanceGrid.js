var rowEditing = Ext.create('Ext.grid.plugin.CellEditing',{
            pluginId:'rowEditing',
            saveBtnText: '保存', 
            cancelBtnText: "取消", 
            autoCancel: false, 
            clicksToEdit:1   //双击进行修改  1-单击   2-双击    0-可取消双击/单击事件
});
var acounttype=Ext.create('Ext.data.Store',{
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
})
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
Ext.define('Rds.bacera.panel.BaceraSafeMedicationFinanceGrid', {
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
		var search = Ext.create('Ext.form.field.Text',{
        	name:'num',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'案例编号'
        });
		var name = Ext.create('Ext.form.field.Text',{
	    	name:'name',
	    	labelWidth:40,
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
		var testitems = Ext.create('Ext.form.field.Text',{
			name:'testitems',
			labelWidth:60,
        	width:'20%',
			fieldLabel:'检测项目'
		});
		var safeMed_starttime = new Ext.form.DateField({
			id:'safeMed_starttime_finance',
			name : 'safeMed_starttime_finance',
        	width:'20%',
			fieldLabel : '日期从',
			labelWidth : 50,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('safeMed_starttime_finance').getValue();
	                var endDate = Ext.getCmp('safeMed_endtime_finance').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('safeMed_starttime_finance').setValue(endDate);
	                }
				}
			}
		});
		var safeMed_endtime = new Ext.form.DateField({
			id:'safeMed_endtime_finance',
			name : 'safeMed_endtime_finance',
        	width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
//			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('safeMed_starttime_finance').getValue();
	                var endDate = Ext.getCmp('safeMed_endtime_finance').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('safeMed_starttime_finance').setValue(endDate);
	                }
				}
			}
		});
		var paragraphtime_starttime = new Ext.form.DateField({
			id:'safeMed_starttime_finance_paragraphtime',
			name : 'paragraphtime_starttime',
        	width:'20%',
			fieldLabel : '到账日期从',
			labelWidth : 70,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			//		maxValue : new Date(),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('safeMed_starttime_finance_paragraphtime').getValue();
					var endDate = Ext.getCmp('safeMed_endtime_finance_paragraphtime').getValue();
					if (start > endDate) {
						Ext.getCmp('safeMed_starttime_finance_paragraphtime').setValue(endDate);
					}
				}
			}
		});
		var paragraphtime_endtime = new Ext.form.DateField({
			id:'safeMed_endtime_finance_paragraphtime',
			name : 'paragraphtime_endtime',
        	width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			listeners:{
				'select':function(){
					var start = Ext.getCmp('safeMed_starttime_finance_paragraphtime').getValue();
					var endDate = Ext.getCmp('safeMed_endtime_finance_paragraphtime').getValue();
					if (start > endDate) {
						Ext.getCmp('safeMed_starttime_finance_paragraphtime').setValue(endDate);
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
		var type=new Ext.form.field.ComboBox({
			fieldLabel : '类型',
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
						data : [['全部',''],['成人','audlt' ],['儿童','child' ] ]
					}),
			mode : 'local',
			name : 'type',
			value: ''
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
			store : Ext.create("Ext.data.Store",{
				 fields:[
		                  {name:'key',mapping:'key',type:'string'},
		                  {name:'value',mapping:'value',type:'string'},
		                  {name:'name',mapping:'name',type:'string'},
		                  {name:'id',mapping:'id',type:'string'},
		          ],
				pageSize : 10,
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
			hideTrigger : true,
			minChars : 2,
			matchFieldWidth : true,
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
			}
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','name','date','type','testitems','ownpersonname','ownperson','agentname',
			        'reportif','remark','cancelif','receivables','payment','paid','paragraphtime',
			        'discountPrice','account_type','confirm_flag',
			        'expressnum','expresstype','recive','remarks','remittanceName','remittanceDate'],
	        start:0,
			limit:15,
			pageSize:15,
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/safeMedication/queryallpage.do',
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
            			num:search.getValue(),
            			name:name.getValue(),
            			ownperson:ownperson.getValue(),
            			safeMed_starttime:dateformat(safeMed_starttime.getValue()),
            			safeMed_endtime:dateformat(safeMed_endtime.getValue()),
        				reportif:reportif.getValue(),
        				agent:agent.getValue(),
        				areacode:areacode.getValue(),
        				testitems:testitems.getValue(),
        				type:type.getValue(),
        				cancelif:2,
        				paragraphtime_starttime:dateformat(paragraphtime_starttime.getValue()),
        				paragraphtime_endtime:dateformat(paragraphtime_endtime.getValue())
        				});
        		}
            }
		});
		
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
	          width: 60
	      });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

	     	//添加下拉显示条数菜单选中事件
	        combo.on("select", function (comboBox) {
	        var pagingToolbar=Ext.getCmp('pagingbarSafeMedicationFinance');
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
				id:'pagingbarSafeMedicationFinance',
				store: me.store,
				pageSize:me.pageSize,
				displayInfo: true,
				displayMsg : "第 {0} - {1} 条  共 {2} 条",
		   	 	emptyMsg : "没有符合条件的记录",
		   	 	items: ['-', '每页显示',combo,'条']
	       });
//		me.bbar = {xtype: 'label',id:'totalBbarSafeMed_finance', text: '',
//				style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
		me.columns = [
						{ text: '案例编号',	dataIndex: 'num', menuDisabled:true, width : 100,
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
			              { text: '应收款项',	dataIndex: 'receivables', menuDisabled:true, width : 80,
		                	  editor:new Ext.form.NumberField()
			              },
		                  { text: '所付款项',	dataIndex: 'payment', menuDisabled:true, width : 80,
		                	  editor:new Ext.form.NumberField()
		                  },
		                  { text: '到款时间',	dataIndex: 'paragraphtime', menuDisabled:true, width :110,
		                	  renderer:Ext.util.Format.dateRenderer('Y-m-d'),
		                      editor:new Ext.form.DateField({
		                           format: 'Y-m-d'
		                         })
		                  },
		                  { text: '账户类型',	dataIndex: 'account_type', menuDisabled:true, width : 150,
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
		                  { text: '财务备注',	dataIndex: 'remarks', menuDisabled:true, width : 200,
		                	  editor:'textfield'
		                  },{ text: '汇款账户',	dataIndex: 'remittanceName', menuDisabled:true, width : 150,
		                	  editor:new Ext.form.ComboBox({
		            				autoSelect : true,
		            				editable:true,
		            		        name:'remittanceName',
		            		        triggerAction: 'all',
		            		        queryMode: 'local', 
		            		        emptyText : "请选择",
		            		        selectOnTab: true,
		            		        store: remittanceAccountStore,
		            		        maxLength: 50,
		            		        fieldStyle: me.fieldStyle,
		            		        displayField:'accountName',
		            		        valueField:'accountName',
		            		        listClass: 'x-combo-list-small'
		            			})
		                    },
		                    { text: '汇款时间',	dataIndex: 'remittanceDate', menuDisabled:true, width : 110,
		                  	  renderer:Ext.util.Format.dateRenderer('Y-m-d'),
		                        editor:new Ext.form.DateField({
		                             format: 'Y-m-d'
		                           })
		                    },
		                    {
		            			header : '优惠价格',
		            			dataIndex : 'discountPrice',
		            			width : 80,
		            			editor:new Ext.form.NumberField()
		            		  },
						 { text: '类型',	dataIndex: 'type', menuDisabled:true, width : 60,
			            	  renderer : function(value) {
									switch (value) {
									case "audlt":
										return "成人";
										break;
									default:
										return "儿童";
									}
								}
		                	  
		                  },
						{ text: '日期',	dataIndex: 'date', menuDisabled:true, width : 80},
						{ text: '姓名',	dataIndex: 'name', menuDisabled:true, width : 80},
						{ text: '市场归属人',	dataIndex: 'ownpersonname', menuDisabled:true, width : 100},
		                { text: '归属地',	dataIndex: 'areaname', menuDisabled:true, width : 150},
						{ text: '被代理人',	dataIndex: 'agentname', menuDisabled:true, width : 80},
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
						{ text: '检测项目',	dataIndex: 'testitems', menuDisabled:true, width : 200},
	                    { text: '备注要求',	dataIndex: 'remark', menuDisabled:true, width : 300}];
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
	 		items:[reportif,safeMed_starttime,safeMed_endtime,testitems,type]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '1px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[paragraphtime_starttime,paragraphtime_endtime,{
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
	 			handler:me.caseCode
	 		},{
	 			text:'批量插入',
	 			iconCls:'Add',
	 			handler:me.inserFinance
	 		},{
	 			text:'案例确认',
	 			iconCls:'Pageedit',
	 			handler:me.confirmFinanceList
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbarSafeMed_finance').setText("共 "+me.store.getCount()+" 条");
//		});
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
			values["case_type"]="法医病理";
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
		for(var i = 0 ; i < selections.length ; i ++)
		{
			num += selections[i].get("num")+";";
		}
		Ext.Msg.alert("我是案例编号", num);
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	},
	listeners : {
		'beforeedit':function(editor, e){
			if(!('masl' == usercode || 'xieh' == usercode  || 'sq_wangyan'==usercode || 'admin'==usercode))
				return false;
			else{
				if(e.record.data.confirm_flag=='2' )
					return false;
			}
			rowEditing.on('edit',afterEdit);
//			if(!('masl' == usercode || 'xieh' == usercode  || 'sq_wangyan'==usercode || 'admin'==usercode))
//				return false;
		}
	}
});

function afterEdit(e,s){  
  	Ext.Ajax.request({  
		url:"bacera/boneAge/saveFinance.do", 
		method: "POST",
		headers: { 'Content-Type': 'application/json' },
		jsonData: {
			id:s.record.data.id,
			num:s.record.data.num,
			receivables:s.record.data.receivables==null?'':s.record.data.receivables,
			payment:s.record.data.payment==null?'':s.record.data.payment,
			paragraphtime:Ext.util.Format.date(s.record.data.paragraphtime, 'Y-m-d'),
			discountPrice:s.record.data.discountPrice==null?'':s.record.data.discountPrice,
			case_type:s.record.data.type+"安全用药",
			account_type:s.record.data.account_type==null?'':s.record.data.account_type,
			remarks:s.record.data.remarks==null?'':s.record.data.remarks,
			remittanceName:s.record.data.remittanceName==null?'':s.record.data.remittanceName,
			remittanceDate:Ext.util.Format.date(s.record.data.remittanceDate, 'Y-m-d')
		}, 
		success: function (response, options) {  
			response = Ext.JSON.decode(response.responseText); 
             if (response.result==false) {  
                 Ext.MessageBox.alert("错误信息", "修改失败，请重试或联系管理员!");
             }
		},  
		failure: function () {
			Ext.MessageBox.alert("提示", "保存失败<br>请联系管理员!");
		}
  	});
}