Ext.define('Rds.bacera.panel.BaceraInvasivePreFinanceGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    selType: 'rowmodel',
    plugins: [ Ext.create('Ext.grid.plugin.CellEditing', {
                  clicksToEdit: 1,
                  listeners:{  
                      'edit':function(e,s){  
//                    	Ext.MessageBox.wait('正在保存','请稍后...');
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
								case_type:'无创产前',
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
//				                 else{
//				                	 Ext.MessageBox.alert("提示信息", "保存成功！");
//				                 }
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
		var search = Ext.create('Ext.form.field.Text',{
        	name:'num',
        	labelWidth:60,
        	width:160,
        	fieldLabel:'案例编号'
        });
		var name = Ext.create('Ext.form.field.Text',{
	    	name:'name',
	    	labelWidth:60,
	    	width:160,
	    	fieldLabel:'姓名'
	    });
		var ownperson = Ext.create('Ext.form.field.Text',{
	    	name:'ownperson',
	    	labelWidth:60,
	    	width:160,
	    	fieldLabel:'归属人'
	    });
		var agent = Ext.create('Ext.form.field.Text',{
	    	name:'agent',
	    	labelWidth:60,
	    	width:150,
	    	fieldLabel:'被代理人'
	    });
		var code = Ext.create('Ext.form.field.Text',{
	    	name:'code',
	    	labelWidth:60,
	    	width:160,
	    	fieldLabel:'条码'
	    });
		var inspectionunit = new Ext.form.field.ComboBox({
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
						data : [['全部',''],['NIPT(博奥)','1' ],['NIPT(成都博奥)','5' ],['NIPT(贝瑞)','2' ],['NIPT-plus(博奥)','3' ],['NIPT-plus(贝瑞)','4' ] ]
					}),
			value : '',
			mode : 'local',
			name : 'inspectionunit',
			value: ''
		});
//		var inspectionunit = Ext.create('Ext.form.field.Text',{
//	    	name:'inspectionunit',
//	    	labelWidth:40,
//	    	width:120,
//	    	fieldLabel:'类型'
//	    });
		var hospital = Ext.create('Ext.form.field.Text',{
			name:'hospital',
			labelWidth:60,
			width:160,
			fieldLabel:'所属医院'
		});
		var invasive_starttime = new Ext.form.DateField({
			id:'invasive_finance_starttime',
			name : 'invasive_finance_starttime',
			width : 160,
			fieldLabel : '日期从',
			labelWidth : 50,
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('invasive_finance_starttime').getValue();
	                var endDate = Ext.getCmp('invasive_finance_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('invasive_finance_starttime').setValue(endDate);
	                }
				}
			}
		});
		var invasive_endtime = new Ext.form.DateField({
			id:'invasive_finance_endtime',
			name : 'invasive_finance_endtime',
			width : 130,
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
//			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('invasive_finance_starttime').getValue();
	                var endDate = Ext.getCmp('invasive_finance_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('invasive_finance_starttime').setValue(endDate);
	                }
				}
			}
		});
		var paragraphtime_starttime = new Ext.form.DateField({
			id:'invasive_starttime_finance_paragraphtime',
			name : 'paragraphtime_starttime',
			width : 180,
			fieldLabel : '到账日期从',
			labelWidth : 70,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			//		maxValue : new Date(),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('invasive_starttime_finance_paragraphtime').getValue();
					var endDate = Ext.getCmp('invasive_endtime_finance_paragraphtime').getValue();
					if (start > endDate) {
						Ext.getCmp('invasive_starttime_finance_paragraphtime').setValue(endDate);
					}
				}
			}
		});
		var paragraphtime_endtime = new Ext.form.DateField({
			id:'invasive_endtime_finance_paragraphtime',
			name : 'paragraphtime_endtime',
			width : 135,
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			//		maxValue : new Date(),
//			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('invasive_starttime_finance_paragraphtime').getValue();
					var endDate = Ext.getCmp('invasive_endtime_finance_paragraphtime').getValue();
					if (start > endDate) {
						Ext.getCmp('invasive_starttime_finance_paragraphtime').setValue(endDate);
					}
				}
			}
		});
		var invasive_sample_starttime = new Ext.form.DateField({
			id:'invasive_sample_finance_starttime',
			name : 'invasive_sample_finance_starttime',
			width : 180,
			fieldLabel : '采样日期从',
			labelWidth : 70,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
//			maxValue : new Date(),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('invasive_sample_finance_starttime').getValue();
	                var endDate = Ext.getCmp('invasive_sample_finance_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('invasive_sample_finance_starttime').setValue(endDate);
	                }
				}
			}
		});
		var invasive_sample_endtime = new Ext.form.DateField({
			id:'invasive_sample_finance_endtime',
			name : 'invasive_sample_finance_endtime',
			width : 130,
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('invasive_sample_finance_starttime').getValue();
	                var endDate = Ext.getCmp('invasive_sample_finance_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('invasive_sample_finance_starttime').setValue(endDate);
	                }
				}
			}
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
			id:'areacode',
			emptyText:'检索方式：如朝阳区(cy)',
			store : Ext.create("Ext.data.Store",{
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
			fields:['id','num','name','code','hospital','inspectionunit','sampledate','date','ownpersonname',
			        'ownperson','agentname','reportif','remark','cancelif', 'receivables','payment',
			        'paid','discountPrice','paragraphtime','account_type',
			        'areaname','remarks','remittanceName','remittanceDate','expressnum'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/invasivePre/queryallpage.do',
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
            		Ext.apply(me.store.proxy.params, {num:search.getValue(),name:name.getValue(),ownperson:ownperson.getValue(),
            			invasive_starttime:dateformat(invasive_starttime.getValue()),
            			invasive_endtime:dateformat(invasive_endtime.getValue()),
        				reportif:reportif.getValue(),cancelif:2,
        				agent:agent.getValue(),
        				areacode:areacode.getValue(),
        				code:code.getValue(),
        				areaname:Ext.getCmp('areacode').getRawValue(),
        				hospital:hospital.getValue(),
        				inspectionunit:inspectionunit.getValue(),
        				invasive_sample_starttime:dateformat(invasive_sample_starttime.getValue()),
            			invasive_sample_endtime:dateformat(invasive_sample_endtime.getValue()),
            			paragraphtime_starttime:dateformat(paragraphtime_starttime.getValue()),
        				paragraphtime_endtime:dateformat(paragraphtime_endtime.getValue())
        				});
        		}
            }
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
		});
		
		me.bbar = {xtype: 'label',id:'totalBbarInvasive_finance', text: '',style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
		me.columns = [
	              { text: '案例编号',	dataIndex: 'num', menuDisabled:true, width:100,
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
                  { text: '财务备注',	dataIndex: 'remarks', menuDisabled:true, width : 120,
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
                  { text: '日期',	dataIndex: 'date', menuDisabled:true, width:80},
                  { text: '姓名',	dataIndex: 'name', menuDisabled:true, width:80},
                  { text: '条码',	dataIndex: 'code', menuDisabled:true, width : 100},
                  { text: '采样日期',	dataIndex: 'sampledate', menuDisabled:true, width : 100},
                  { text: '类型',	dataIndex: 'inspectionunit', menuDisabled:true, width : 150,
                	  renderer : function(value) {
							switch (value) {
							case "1":
								return "NIPT(博奥)";
								break;
							case "5":
								return "NIPT(成都博奥)";
								break;
							case "2":
								return "NIPT(贝瑞)";
								break;
							case "3":
								return "NIPT-plus(博奥)";
								break;
							case "4":
								return "NIPT-plus(贝瑞)";
								break;
							}
						}
                },
                  { text: '市场归属人',	dataIndex: 'ownpersonname', menuDisabled:true, width : 100},
                  { text: '归属地',	dataIndex: 'areaname', menuDisabled:true, width : 150},
                  { text: '被代理人',	dataIndex: 'agentname', menuDisabled:true, width : 80},
                  { text: '所属医院',	dataIndex: 'hospital', menuDisabled:true, width : 150},
                  { text: '是否发报告',	dataIndex: 'reportif', menuDisabled:true, width : 100,
                	  renderer : function(value, cellmeta,
								record, rowIndex, columnIndex,
								store) {
							var isnull= record.data["expressnum"];
							if ( null !=isnull) {
								return "是";
							} else {
								return "<span style='color:red'>否</span>";
							}

						}
              	  
                  },
                  { text: '备注要求',	dataIndex: 'remark', menuDisabled:true, width : 300}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[search,name,ownperson,areacode,inspectionunit]
	 	},{
	 		style : {
	        borderTopWidth : '0px !important',
	        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[code,hospital,reportif,invasive_starttime,invasive_endtime]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[invasive_sample_starttime,invasive_sample_endtime,paragraphtime_starttime,paragraphtime_endtime,{
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
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
		me.store.on("load",function(){
			Ext.getCmp('totalBbarInvasive_finance').setText("共 "+me.store.getCount()+" 条");
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
	}
	
});