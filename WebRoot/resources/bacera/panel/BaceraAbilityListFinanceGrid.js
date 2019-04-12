var ability="";
Ext.define('Rds.bacera.panel.BaceraAbilityListFinanceGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    selType: 'rowmodel',
    plugins: [ Ext.create('Ext.grid.plugin.RowEditing', {
        clicksToEdit: 1,
        listeners:{  
            'edit':function(e,s){  
          	Ext.MessageBox.wait('正在保存','请稍后...');
            	Ext.Ajax.request({  
					url:"bacera/boneAge/saveFinance.do", 
					method: "POST",
					headers: { 'Content-Type': 'application/json' },
					jsonData: {
						id:s.record.data.ability_id,
						num:s.record.data.ability_num,
						receivables:s.record.data.receivables==null?'':s.record.data.receivables,
						payment:s.record.data.payment==null?'':s.record.data.payment,
						paragraphtime:Ext.util.Format.date(s.record.data.paragraphtime, 'Y-m-d'),
						discountPrice:s.record.data.discountPrice==null?'':s.record.data.discountPrice,
						case_type:'能力验证',
						account_type:s.record.data.account_type==null?'':s.record.data.account_type,
						remarks:s.record.data.finance_remark==null?'':s.record.data.finance_remark,
						remittanceName:s.record.data.remittanceName==null?'':s.record.data.remittanceName,
						remittanceDate:Ext.util.Format.date(s.record.data.remittanceDate, 'Y-m-d')
					}, 
					success: function (response, options) {  
						response = Ext.JSON.decode(response.responseText); 
		                 if (response==false) {  
			                 Ext.MessageBox.alert("错误信息", "修改收费失败，请联系管理员！");
		                 }else{
		                	 Ext.MessageBox.alert("提示信息", "保存成功！");
		                 }
					},  
					failure: function () {
						Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
					}
		      	});
            }  
          }  
    })
],
	initComponent : function() {
		var me = this;
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
		var ability_num = Ext.create('Ext.form.field.Text',{
			name:'ability_num',
			labelWidth:80,
			width:'20%',
			fieldLabel:'项目编号'
		});
		var ability_name = Ext.create('Ext.form.field.Text',{
			name:'ability_name',
			labelWidth:60,
			width:'20%',
			fieldLabel:'项目名称'
		});
		var participate_num = Ext.create('Ext.form.field.Text',{
			name:'participate_num',
			labelWidth:80,
			width:'20%',
			fieldLabel:'参加编号'
		});
		var ability_company = Ext.create('Ext.form.field.Text',{
			name:'ability_company',
			labelWidth:80,
			width:'20%',
			fieldLabel:'所属单位'
		});
		var ownperson = Ext.create('Ext.form.field.Text',{
			name:'ownperson',
			labelWidth:80,
			width:'20%',
			fieldLabel:'归属人'
		});
		var contacts_per = Ext.create('Ext.form.field.Text',{
			name:'contacts_per',
			labelWidth:80,
			width:'20%',
			fieldLabel:'联系人'
		});
		var identify_starttime = new Ext.form.DateField({
			name : 'identify_starttime',
			width : '20%',
			fieldLabel : '鉴定时间从',
			labelWidth : 80,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var identify_endtime = new Ext.form.DateField({
			name : 'identify_endtime',
			width : '20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var isdelete=new Ext.form.field.ComboBox({
			fieldLabel : '是否作废',
			width : '20%',
			labelWidth : 80,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部',''],['是','2' ],['否','1'] ]
					}),
			value : '1',
			readOnly:true,
			mode : 'local',
			name : 'delete',
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['ability_id','ability_num','ability_name','participate_num','ability_company',
			        'sample_type','identify_starttime','identify_endtime','contacts_per','contacts_phone',
			        'contacts_mail','ownperson','sample_per','sample_date','sample_express_per',
			        'ability_result','ability_remark','department_concatid','department_name','department_chargename',
			        'experiment_chargename','report_chargename','finished_date','report_sendname','report_concat','report_senddate',
			        'report_sendinfo','report_reciveinfo','report_type','report_num','attachment_id','receivables','paragraphtime',
			        'payment','paid','account_type','finance_remark','discountPrice','remittanceDate','remittanceName'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/ability/queryallpage.do',
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
            		ability = "ability_num=" + ability_num.getValue().trim() + "&ability_name="
							+ ability_name.getValue().trim() + "&ownperson="
							+ ownperson.getValue().trim() + "&identify_starttime="
							+ dateformat(identify_starttime.getValue())
							+ "&identify_endtime="
							+ dateformat(identify_endtime.getValue())
							+ "&participate_num=" + participate_num.getValue().trim()
							+ "&ability_company=" + ability_company.getValue().trim 
							+ "&contacts_per=" + contacts_per.getValue().trim()
							+ "&isdelete="+isdelete;
            		Ext.apply(me.store.proxy.params, {
            			ability_num:trim(ability_num.getValue()),
            				ability_name:trim(ability_name.getValue()),
		        			ownperson:trim(ownperson.getValue()),
		        			identify_starttime:dateformat(identify_starttime.getValue()),
		        			identify_endtime:dateformat(identify_endtime.getValue()),
		        			participate_num:participate_num.getValue(),
		    				ability_company:ability_company.getValue(),
		    				isdelete:isdelete.getValue(),
//		    				finance:flag
    				});
        		}
            }
		});
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode: 'SINGLE'
		});
		me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录"
        });
		me.columns = [
	              { text: '项目编号',	dataIndex: 'ability_num', menuDisabled:true, width : 100,
                	  renderer : function(value, cellmeta,
								record, rowIndex, columnIndex,
								store) {
							var isnull= record.data["delete"];
							if (isnull == 1) {
								return "<div style=\"text-decoration: line-through;color: red;\">"
										+ value + "</div>"
							} else {
								return value;
							}

						}
		              },
	                  { text: '项目名称',	dataIndex: 'ability_name', menuDisabled:true,width : 80},
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
	                  },{ text: '财务备注',	dataIndex: 'finance_remark', menuDisabled:true, width : 120,
	                	  editor:'textfield'
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
	                  { text: '参加编号',	dataIndex: 'participate_num', menuDisabled:true, width : 80},
	                  { text: '所属单位',	dataIndex: 'ability_company', menuDisabled:true, width : 80},
	                  { text: '样本类型',	dataIndex: 'sample_type', menuDisabled:true, width : 200},
	                  { text: '归属人',	dataIndex: 'ownperson', menuDisabled:true, width : 100},
	                  { text: '鉴定开始日期',	dataIndex: 'identify_starttime', menuDisabled:true, width : 150},
	                  { text: '鉴定结束',	dataIndex: 'identify_endtime', menuDisabled:true, width : 150},
	                  { text: '发件信息',	dataIndex: 'report_sendinfo', menuDisabled:true, width : 100},
	                  { text: '备注',	dataIndex: 'ability_remark', menuDisabled:true, width : 200}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[ability_num,ability_name,participate_num,ownperson,ability_company]
	 	},{
	 		style : {
	        borderTopWidth : '0px !important',
	        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[identify_starttime,identify_endtime,isdelete,{
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
	 			handler:me.caseCode,
	 			hidden:true
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	caseCode:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要查看的案例编号!");
			return;
		};
		var ability_num="";
		for(var i = 1 ; i < selections.length+1 ; i ++)
		{
			ability_num += selections[i-1].get("ability_num")+";";
//			if(i%8==0) num +="<br>";
		}
		Ext.Msg.alert("我是案例编号", ability_num);
	},
	//新增
	onInsert:function(){
		var me = this.up("gridpanel");
		ownpersonTemp = "";
		var form = Ext.create("Rds.bacera.form.BaceraAbilityListForm",{
			region:"center",
			autoScroll : true,
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'能力验证—新增',
			width:800,
			iconCls:'Add',
			height:600,
			layout:'border',
			maximizable :true,
			maximized:true,
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
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		};
		if(selections[0].get("delete") == '2')
		{
			Ext.Msg.alert("提示", "该记录已作废，不能修改!");
			return;
		}
		var form = Ext.create("Rds.bacera.form.BaceraAbilityUpdateForm",{
			region:"center",
			modal:true,
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'能力验证—修改',
			width:800,
			iconCls:'Pageedit',
			height:600,
			layout:'border',	
			items:[form],
			maximizable :true,
			maximized:true,
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