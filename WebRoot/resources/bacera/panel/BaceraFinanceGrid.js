var rowEditing = Ext.create('Ext.grid.plugin.CellEditing',{
            pluginId:'rowEditing',
            saveBtnText: '保存', 
            cancelBtnText: "取消", 
            autoCancel: false, 
            clicksToEdit:1   //双击进行修改  1-单击   2-双击    0-可取消双击/单击事件
});
var remittanceAccountStore=Ext.create('Ext.data.Store',{
    fields:['accountName'],
    proxy:{
			type: 'jsonajax',
			actionMethods:{read:'POST'},
		    url:'judicial/remittance/queryall.do',
		    reader:{type:'json',root:'data'}
     },
     autoLoad:true,
     remoteSort:true						
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
});
Ext.define('Rds.bacera.panel.BaceraFinanceGrid', {
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
		var num = Ext.create('Ext.form.field.Text',{
        	name:'num',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'案例编号'
        });
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','case_type','receivables','payment','paid','paragraphtime','discountPrice','fees',
			        'siteFee','account_type','remarks','remittanceName','remittanceDate','confirm_flag'],
	        start:0,
			limit:15,
			pageSize:15,
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'finance/bacera/queryAllPage.do',
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
        			num:num.getValue(),
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
       var pagingToolbar=Ext.getCmp('pagingbarFinance');
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
			id:'pagingbarFinance',
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
								var confirm_flag = record.data["confirm_flag"];
								if (confirm_flag == 2) {
									return "<div style=\"color: green;\">"
											+ value + "</div>"
								} else {
									return value;
								}
							}
					  },
					  { text: '案例类型',	dataIndex: 'case_type', menuDisabled:true, width : 120},
					  { text: '应收款项',	dataIndex: 'receivables', menuDisabled:true, width : 80,
	                	  editor:new Ext.form.NumberField()
		              },
	                  { text: '所付款项',	dataIndex: 'payment', menuDisabled:true, width : 80,
	                	  editor:new Ext.form.NumberField()
	                  },
	                  { text: '到款时间',	dataIndex: 'paragraphtime', menuDisabled:true, width : 110,
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
	                  { text: '汇款账户',	dataIndex: 'remittanceName', menuDisabled:true, width : 150,
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
                    { text: '财务备注',	dataIndex: 'remarks', menuDisabled:true, width : 120, editor:'textfield'}
	              ];
		me.dockedItems = [{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '1px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[num,{
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
	 			text:'财务删除',
	 			iconCls:'Delete',
	 			handler:me.onDelete
	 		},{
	 			text:'财务确认',
	 			iconCls:'Pageedit',
	 			handler:me.confirmFinanceList
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	onDelete:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要删除的财务信息!");
			return;
		}
		if(selections[0].get("confirm_flag")=='2'){
			Ext.Msg.alert("提示", "存在已确认财务案例!");
			return;
		}
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在操作','请稍后...'); 	
	    		Ext.Ajax.request({
	    			url : "finance/bacera/delete.do",
	    			method : "POST",
	    			headers : {
	    				'Content-Type' : 'application/json'
	    			},
	    			jsonData : {id:selections[0].get("id")},
	    			success : function(response, options) {
	    				response = Ext.JSON
	    						.decode(response.responseText);
	    				if (response) {
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
	confirmFinanceList:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 ||selections.length>1){
			Ext.Msg.alert("提示", "请选择需要确认的一条记录!");
			return;
		}
		if(selections[0].get("confirm_flag")=='2'){
			Ext.Msg.alert("提示", "存在已确认财务案例!");
			return;
		}
		Ext.MessageBox.confirm("提示", "确认选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在操作','请稍后...'); 	
	    		Ext.Ajax.request({
	    			url : "finance/bacera/confirmCase.do",
	    			method : "POST",
	    			headers : {
	    				'Content-Type' : 'application/json'
	    			},
	    			jsonData : {id:selections[0].get("id")},
	    			success : function(response, options) {
	    				response = Ext.JSON
	    						.decode(response.responseText);
	    				if (response) {
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
//			if(i>0&&i%8==0) num +="<br>";
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
		}
	}
	
});

function afterEdit(e,s){  
  	Ext.Ajax.request({  
		url:"finance/bacera/update.do", 
		method: "POST",
		headers: { 'Content-Type': 'application/json' },
		jsonData: {
			id:s.record.data.id,
			num:s.record.data.num,
			receivables:s.record.data.receivables==null?'':s.record.data.receivables,
			payment:s.record.data.payment==null?'':s.record.data.payment,
			paragraphtime:Ext.util.Format.date(s.record.data.paragraphtime, 'Y-m-d'),
			discountPrice:s.record.data.discountPrice==null?'':s.record.data.discountPrice,
			fees:s.record.data.fees==null?'':s.record.data.fees,
			siteFee:s.record.data.siteFee==null?'':s.record.data.siteFee,
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
			Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
		}
  	});
}