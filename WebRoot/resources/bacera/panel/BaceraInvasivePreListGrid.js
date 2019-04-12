var invasice=""
	case_canel = function(me) {
	me.up("window").close();
}
Ext.define('Rds.bacera.panel.BaceraInvasivePreListGrid', {
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
		var name = Ext.create('Ext.form.field.Text',{
	    	name:'name',
	    	labelWidth:60,
	    	width:'20%',
	    	fieldLabel:'姓名'
	    });
		var ownperson = Ext.create('Ext.form.field.Text',{
	    	name:'ownperson',
	    	labelWidth:60,
	    	width:'20%',
	    	fieldLabel:'归属人'
	    });
		var agent = Ext.create('Ext.form.field.Text',{
	    	name:'agent',
	    	labelWidth:60,
	    	width:'20%',
	    	fieldLabel:'被代理人'
	    });
		var code = Ext.create('Ext.form.field.Text',{
	    	name:'code',
	    	labelWidth:60,
	    	width:'20%',
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
						data : [['全部',''],['NIPT(博奥)','1' ],['NIPT(成都博奥)','5' ],['NIPT(贝瑞)','2' ],['NIPT-plus(博奥)','3' ],['NIPT-plus(贝瑞)','4' ],['NIPT(精科)','6' ],['NIPT-plus(精科)','7' ] ]
					}),
			value : '',
			mode : 'local',
			name : 'inspectionunit',
			value: ''
		});
//		var inspectionunit = Ext.create('Ext.form.field.Text',{
//	    	name:'inspectionunit',
//	    	labelWidth:60,
//	    	width:'20%',
//	    	fieldLabel:'类型'
//	    });
		var hospital = Ext.create('Ext.form.field.Text',{
			name:'hospital',
			labelWidth:60,
			width:'20%',
			fieldLabel:'所属医院'
		});
		var invasive_starttime = new Ext.form.DateField({
			id:'invasive_starttime',
			name : 'invasive_starttime',
			width:'20%',
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
					var start = Ext.getCmp('invasive_starttime').getValue();
	                var endDate = Ext.getCmp('invasive_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('invasive_starttime').setValue(endDate);
	                }
				}
			}
		});
		var invasive_endtime = new Ext.form.DateField({
			id:'invasive_endtime',
			name : 'invasive_endtime',
			width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('invasive_starttime').getValue();
	                var endDate = Ext.getCmp('invasive_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('invasive_starttime').setValue(endDate);
	                }
				}
			}
		});
		var invasive_sample_starttime = new Ext.form.DateField({
			id:'invasive_sample_starttime',
			name : 'invasive_sample_starttime',
			width:'20%',
			fieldLabel : '采样日期从',
			labelWidth : 70,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
     //		maxValue : new Date(),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('invasive_sample_starttime').getValue();
	                var endDate = Ext.getCmp('invasive_sample_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('invasive_sample_starttime').setValue(endDate);
	                }
				}
			}
		});
		var invasive_sample_endtime = new Ext.form.DateField({
			id:'invasive_sample_endtime',
			name : 'invasive_sample_endtime',
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
					var start = Ext.getCmp('invasive_sample_starttime').getValue();
	                var endDate = Ext.getCmp('invasive_sample_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('invasive_sample_starttime').setValue(endDate);
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
		var verify_state = Ext.create('Ext.form.ComboBox', 
				{
					fieldLabel : '审核状态',
					width:'20%',
					labelWidth : 70,
					editable : false,
					triggerAction : 'all',
					displayField : 'Name',
					valueField : 'Code',
					store : new Ext.data.ArrayStore(
							{fields : ['Name','Code' ],
								data : [
										['全部','' ],
										['未审核','0' ],
										['待审核','1'],
										['审核不通过','2'],
										['审核通过','3'],
//										['待报告上传','4'],
										['待报告确认','5'],
										['邮寄中','6'],
										['归档中','7'],
										['已归档','8']
										]
							}),
					value : '',
					mode : 'local',
					name : 'verify_state',
				});
		var areacode= new Ext.form.field.ComboBox({
			fieldLabel : '归属地',
			labelWidth : 60,
			name : 'areacode',
			width:'20%',
			id:'areacode1',
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
			hideTrigger : true,
			minChars : 2,
			matchFieldWidth : true,
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
			}
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','name','code','hospital','inspectionunit','sampledate','date','ownpersonname','confirm_code',
			 'ownperson','agentname','reportif','remark','verify_state','cancelif','expresstype','expressnum','areaname',
			 'processInstanceId', 'task_id', 'task_def_key', 'areacode',
			 'task_name', 'suspension_state','has_comment','last_task_id','flag','type','emaildate'],
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
            		invasice = "num=" + search.getValue().trim() + "&name="
    				+ name.getValue().trim() + "&ownperson="
    				+ ownperson.getValue().trim() + "&invasive_starttime="
    				+ dateformat(invasive_starttime.getValue())
    				+ "&invasive_endtime="
    				+ dateformat(invasive_endtime.getValue())
    				+ "&reportif=" + reportif.getValue()
    				+ "&cancelif=" + cancelif.getValue() 
    				+ "&agent="+ agent.getValue().trim()+
    				"&areacode="+ areacode.getValue()
    				+"&code="
    				+ code.getValue()+"&check="+2+
    				"&hospital="
    				+ hospital.getValue() +"&inspectionunit="
    				+ inspectionunit.getValue() +"&invasive_sample_starttime="
    				+ dateformat(invasive_sample_starttime.getValue()) +"&invasive_sample_endtime="
    				+ dateformat(invasive_sample_endtime.getValue());
            		Ext.apply(me.store.proxy.params, {num:search.getValue(),name:name.getValue(),ownperson:ownperson.getValue(),
            			invasive_starttime:dateformat(invasive_starttime.getValue()),
            			invasive_endtime:dateformat(invasive_endtime.getValue()),
        				reportif:reportif.getValue(),cancelif:cancelif.getValue(),
        				agent:agent.getValue(),
        				areacode:areacode.getValue(),
        				code:code.getValue(),
        				flag:2,
        				areaname:Ext.getCmp('areacode1').getRawValue(),
        				verify_state:verify_state.getValue(),
        				hospital:hospital.getValue(),
        				inspectionunit:inspectionunit.getValue(),
        				invasive_sample_starttime:dateformat(invasive_sample_starttime.getValue()),
            			invasive_sample_endtime:dateformat(invasive_sample_endtime.getValue()),
        				});
        		}
            }
		});
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
 //		mode: 'SINGLE'
		});
		Ext.override(Ext.selection.Model,{  
		    onStoreLoad:function(store, records, successful, eOpts){  
		        var me = this,  
		            length = me.selected.getCount( );  
		          
		        //如果没有选中的记录，则不需要进行任何的操作  
		        if(length===0)return;  
		          
		        //遍历selected并更新其中的记录  
		        me.selected.eachKey(function(key,item){  
		            var model = store.getById(key);  
		              
		            //如果获取到了model就更新，否则从selected中移除  
		            if(model){  
		                me.selected.add(model);//add时会覆盖掉原来的值  
		            }else{  
		                me.selected.removeAtKey(key);  
		            }  
		        })  
		          
		    }  
		} );
		me.bbar = Ext.create('Ext.PagingToolbar', {
			store : me.store,
			pageSize : me.pageSize,
			displayInfo : true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
			emptyMsg : "没有符合条件的记录"
		});
//          me.bbar = {xtype: 'label',id:'totalBbarInvasive', text: '',style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
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
	              { text: '优惠码',	dataIndex: 'confirm_code', menuDisabled:true, width : 80},
                  { text: '日期',	dataIndex: 'date', menuDisabled:true, width : 80},
                  { text: '姓名',	dataIndex: 'name', menuDisabled:true, width : 80},
                  { text: '条码',	dataIndex: 'code', menuDisabled:true, width : 100},
                  { text: '审核状态',	dataIndex: 'verify_state', menuDisabled:true, width : 150,
                	  renderer:function(value,meta,record){
                		  switch(value){
                		  case "0":
                			  return "<span style='color:red'>未审核</span>";
                			  break;
                		  case "1":
                			    if(record.get("task_def_key")=="taskAudit"){
									return "已提交审核";
								}else{
									return "<span style='color:red'>案例审核未通过</span><a class='lbtnComment' href='#'>查看原因</a>";
								}
								break;
                		  case "2":
                			  if(record.get("task_def_key")=="taskAudit"){
                                  return "已提交审核";
                              }else{
                                 return "<span style='color:red'>案例审核未通过</span><a class='lbtnComment' href='#'>查看原因</a>";
                              }
								break;
                		  case "3":
                			  return "<span style='color:green'>待报告上传</span>";
                			  break;
//                		  case "4":
//                			  return "<span style='color:green'>待报告上传</span>";
//                			  break;
                		  case "5":
                			  return "<span style='color:green'>待报告确认</span>";
                			  break;
                		  case "6":
                			  return "<span style='color:green'>邮寄中</span>";
                			  break;
                		  case "7":
                		      return "<span style='color:green'>归档中</span>";
                		       break;
                		  case "8":
                			  return "<span style='color:red'>已归档</span>";
                			  break;
                		  }
                	  }
                  },
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
							case "6":
								return "NIPT(精科)";
							case "7":
								return "NIPT-plus(精科)";
								break;
							}
						}
                  
                  
                  },
                  { text: '归属地',	dataIndex: 'areaname', menuDisabled:true, width : 150},
                  { text: '市场归属人',	dataIndex: 'ownpersonname', menuDisabled:true, width : 100},
                  { text: '被代理人',	dataIndex: 'agentname', menuDisabled:true, width : 80},
                  { text: '所属医院',	dataIndex: 'hospital', menuDisabled:true, width : 150},
                  { text: 'task_id',	dataIndex: 'task_id', menuDisabled:true, width : 100,hidden:true},
                  { text: 'processInstanceId',	dataIndex: 'processInstanceId', menuDisabled:true, width : 100,hidden:true},
                  { text: '是否发报告',	dataIndex: 'reportif', menuDisabled:true, width : 100,
                	  renderer : function(value, cellmeta,
								record, rowIndex, columnIndex,
								store) {
							var isnull= record.data["emaildate"];
							if ( null !=isnull) {
								return "已发送邮件";
							} else {
								return "<span style='color:red'>否</span>";
							}

						}
              	  
                  },
                  { text: '备注',	dataIndex: 'remark', menuDisabled:true, width:200}
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
	 		items:[code,inspectionunit,hospital,invasive_starttime,invasive_endtime]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[invasive_sample_starttime,invasive_sample_endtime,cancelif,verify_state]
		 	},{
		 		style : {
			        borderTopWidth : '0px !important',
			        borderBottomWidth : '0px !important'
			 		},
			 		xtype:'toolbar',
			 		name:'search',
			 		dock:'top',
			 		items:[reportif,{
		            	text:'查询',
		            	iconCls:'Find',
		            	handler:me.onSearch
		            }]
			 	
		 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[
	 		    {text: '提交审核',
				iconCls: 'Pageedit',
				handler: me.onSubmit
			},{
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
	 			text:'照片管理',
	 			iconCls:'Cog',
	 			handler:me.attachmentManage
	 		},{
	 			text:'作废',
	 			iconCls:'Cancel',
	 			handler:me.onCancel
	 		},{
	 			text:'导出',
	 			iconCls:'Application',
	 			handler:me.invasivePerExport
	 		},{
				text: '查看流程状态',
				iconCls: 'Pageedit',
				handler: me.onTaskHistory
			},{
				text: '汇款生成',
				iconCls: 'User',
				handler: me.createPreFinanceDaily
			},{
				text: '优惠码插入',
				iconCls: 'Add',
				handler: me.confirmCode
			}]
	 	}];
		me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbarInvasive').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	//新增
	onInsert:function(){
		var me = this.up("gridpanel");
		ownpersonTemp="";
		var form = Ext.create("Rds.bacera.form.BaceraInvasivePreListInsertForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'无创产前—新增',
			width:600,
			modal:true,
			iconCls:'Add',
			height:430,
			modal:true,
			layout:'border',
			items:[form]
		});
		win.show();
	},
	confirmCode:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1 ){
			Ext.Msg.alert("提示", "请选择一条需要优惠的记录!");
			return;
		}
		var case_id=selections[0].get("id");
		var num=selections[0].get("num");
		var accept_time=selections[0].get("date");
		var inspectionunit=selections[0].get("inspectionunit")

		confirmCode_confirm = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["case_id"]=case_id;
			values["num"]=num;
			values["case_type"]='inversive';
			values["accept_time"]=accept_time;
			values["finance_type"]=inspectionunit;
			if (form.isValid()) {
				Ext.Msg.show({
					title : '提示',
					msg : '请核实确定插入?',
					width : 300,
					buttons : Ext.Msg.OKCANCEL,
					fn : function(buttonId, text, opt) {
						if (buttonId == 'ok') {
							Ext.MessageBox.wait('正在操作','请稍后...');
							Ext.Ajax.request({
										url : "finance/casefinance/confirmCodeAdd.do",
										method : "POST",
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : values,
										success : function(response, options) {
											response = Ext.JSON
													.decode(response.responseText);
											
											if (response.result) {
												Ext.MessageBox.alert("提示信息", response.message);
												var grid = me.up("gridpanel");
												me.getStore().load();
												confirmCodeadd.close();
											} else {
												Ext.MessageBox.alert("错误信息", response.message);
											}
										},
										failure : function() {
											Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
										}
									});
						} else {
							return;
						}

					},
					animateTarget : 'addAddressBtn',
					icon : Ext.window.MessageBox.INFO
				})
			}
		}
		
		var confirmCodeadd = Ext.create("Ext.window.Window", {
			title : '优惠码信息('+num+')',
			width : 350,
			height : 200,
			iconCls : 'Add',
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
					labelSeparator : "：",
					labelAlign : 'right'
				},
				border : false,
				autoHeight : true,
				buttons : [{
							text : '插入',
							iconCls : 'Disk',
							handler : confirmCode_confirm
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : case_canel
						}],
				items : [{
					xtype : 'tbtext',
					style:'color:red',
					text : '注意：已经生成汇款单的案例，请删除后再操作！'
				},{
					xtype : "textfield",
					fieldLabel : '优惠码',
					allowBlank:false,
					labelAlign : 'right',
					maxLength : 100,
					regex:/^[^\s]*$/,
					regexText:'请输入正确条形码',
					style:"margin-top:20px;",
					labelWidth : 60,
					name : 'confirm_code'
				}]
			}]
		})
		confirmCodeadd.show();
	},
	createPreFinanceDaily:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1 ){
			Ext.Msg.alert("提示", "请选择一条需要优惠的记录!");
			return;
		}
		if(4==selections[0].get("type")){
			Ext.Msg.alert("提示", "月结案例无法手动生成汇款!");
			return;
		}
		var case_id=selections[0].get("id");
		var client=selections[0].get("name");
		Ext.Msg.show({
			title : '提示(委托人：'+client+')',
			msg : '确定生成该案例汇款?',
			width : 300,
			buttons : Ext.Msg.OKCANCEL,
			fn : function(buttonId, text, opt) {
				if (buttonId == 'ok') {
					Ext.MessageBox.wait('正在操作','请稍后...');
					Ext.Ajax.request({
								url : "judicial/finance/createPreFinanceDaily.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {"case_id":case_id},
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									console.log(response.result);
									if (response.result) {
										Ext.MessageBox.alert("提示信息", response.message);
									} else {
										Ext.MessageBox.alert("错误信息", response.message);
									}
								},
								failure : function() {
									Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
								}
							});
				} else {
					return;
				}

			},
			animateTarget : 'addAddressBtn',
			icon : Ext.window.MessageBox.INFO
		})
	},
	onDelete:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
			return;
		};
		if(selections[0].get("cancelif") == 1)
		{
			Ext.Msg.alert("提示", "该案例已作废，不能删除!");
			return;
		}
		if(selections[0].get("verify_state") != '0')
		{
			Ext.Msg.alert("提示", "该案例已提交审核，不能删除!");
			return;
		}
		var case_ids='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if((i+1)==selections.length)
				{
				case_ids += selections[i].get("id")
				}
			else
				{
				case_ids += selections[i].get("id")+",";
				}
		}
		var tempStr = '';
		if(null != selections[0].get("receivables") || null != selections[0].get("expresstype"))
		{
			tempStr='该记录包含财务或快递信息，'
		}
		
		Ext.MessageBox.confirm("提示", tempStr+"确认删除选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在保存','请稍后...');
	    		Ext.Ajax.request({  
	    			url:"bacera/invasivePre/delete.do", 
	    			method: "POST",
	    			headers: { 'Content-Type': 'application/json' },
	    			jsonData: {
						'id':case_ids ,
					},
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
	invasivePerExport:function(){
		window.location.href = "bacera/invasivePre/exportInvasiveInfo.do?"+invasice ;
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
		if(selections[0].get("verify_state") == '0'|| selections[0].get("verify_state") == '2'&& selections[0].get("task_def_key")=="taskRegister")
		{
		ownpersonTemp = selections[0].get("ownperson");
		areacodeTemp=selections[0].get("areacode");
		cancelif=selections[0].get("cancelif");
		var form = Ext.create("Rds.bacera.form.BaceraInvasivePreListUpdateForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'无创产前—修改',
			width:580,
			modal:true,
			iconCls:'Pageedit',
			height:400,
			layout:'border',	
			items:[form]
		});
		win.show();
		form.loadRecord(selections[0]);
		     }else{
			Ext.Msg.alert("提示", "存在提交审核案例，不能修改!");
			return;

		}
	},
	onTaskHistory: function () {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		}
		var values = {
			processInstanceId: selections[0].get("processInstanceId")
		};
		if (values.processInstanceId == null || values.processInstanceId == "") {
			Ext.Msg.alert("提示", "该记录不能进行此项操作!");
			return;
		}
		Ext.Ajax.request({
			url: "activiti/main/taskDetail.do",
			method: "POST",
			headers: {'Content-Type': 'application/json'},
			jsonData: values,
			success: function (response, options) {
				response = Ext.JSON.decode(response.responseText);
//				console.log(response);
				var win = Ext.create("Ext.window.Window", {
					title: '流程步骤',
					width: 700,
					iconCls: 'Add',
					layout: 'fit',
					items: {
						xtype: 'grid',
						border: false,
						columns: [
							{
								text: '步骤ID',
								dataIndex: 'id',
								align: 'center',
								sortable: false,
								menuDisabled: true,
								hidden: true
							},
							{
								text: '步骤名称',
								dataIndex: 'name',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '活动类型',
								dataIndex: 'taskDefinitionKey',
								align: 'center',
								sortable: false,
								menuDisabled: true,
								hidden: true
							},
							{
								text: '办理人',
								dataIndex: 'assignee',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '开始时间',
								dataIndex: 'startTimeString',
								align: 'center',
								width: 120,
								sortable: false,
								menuDisabled: true
							},
							{
								text: '签收时间',
								dataIndex: 'claimTimeString',
								align: 'center',
								width: 120,
								sortable: false,
								hidden: true,
								menuDisabled: true
							},
							{
								text: '结束时间',
								dataIndex: 'endTimeString',
								align: 'center',
								width: 120,
								sortable: false,
								menuDisabled: true
							},
							{
								text: '活动耗时',
								dataIndex: 'durationInMillisString',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '原因',
								dataIndex: 'comment',
								align: 'center',
								flex: 1,
								sortable: false,
								menuDisabled: true,
								 renderer : function(value, cellmeta, record,
											rowIndex, columnIndex, store) {
//										var str = value;
//										if (value.length > 15) {
//											str = value.substring(0, 15) + "...";
//										}
									 if(null == value) return "";
									 else 
										return "<span title='" + value + "'>" + value
												+ "</span>";
									}
							}
						],
						store: Ext.create("Ext.data.Store", {
							fields: ['id', 'name', 'taskDefinitionKey', 'assignee', 'claimTime', 'startTime', 'endTime', 'durationInMillis','comment',
								{
									name: 'claimTimeString', type: 'date',
									convert: function (v, rec) {
										return rec.data.claimTime == null ? "" : Ext.Date.format(new Date(rec.data.claimTime), 'Y-m-d H:i');
									}
								},
								{
									name: 'startTimeString', type: 'date',
									convert: function (v, rec) {
										return rec.data.startTime == null ? "" : Ext.Date.format(new Date(rec.data.startTime), 'Y-m-d H:i');
									}
								},
								{
									name: 'endTimeString',
									convert: function (v, rec) {
										return rec.data.endTime == null ? "" : Ext.Date.format(new Date(rec.data.endTime), 'Y-m-d H:i');
									}
								}, {
									name: 'durationInMillisString',
									convert: function (v, rec) {
										var mills = rec.data.durationInMillis;
										var result = "";
										if (mills == null) {
											result = "";
										}
										else if (mills < 1000) {
											result = "小于1秒";
										}
										else {
											var days = parseInt(mills / (1000 * 60 * 60 * 24));
											mills = mills - days * (1000 * 60 * 60 * 24);
											var hours = parseInt(mills / (1000 * 60 * 60));
											mills = mills - hours * (1000 * 60 * 60);
											var min = parseInt(mills / (1000 * 60));
											mills = mills - min * (1000 * 60);
											var second = parseInt(mills / (1000));

											result += days == 0 ? "" : (days + "天");
											result += hours == 0 ? "" : (hours + "小时");
											result += min == 0 ? "" : (min + "分钟");
											result += second + "秒";
										}
										return result;
									}
								}
							],
//							sorters: [{
////								property: 'id',
////								direction: 'DESC'
//							}],
							data: response
						})
					}
				});
				win.show();
				// response = Ext.JSON.decode(response.responseText);
				// if (response.result == true) {
				//     Ext.MessageBox.alert("提示信息", response.message);
				//     me.getStore().load();
				//     // me.up("window").close();
				// } else {
				//     Ext.MessageBox.alert("错误信息", response.message);
				//     me.getStore().load();
				// }
			},
			failure: function () {
				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
			}
		});
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	},
	onSubmit: function () {
	
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		}

		var taskId='';
		var case_ids='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if(selections[i].get("cancelif")==1){
				Ext.Msg.alert("提示", "该案例已作废，无法提交审核!");
				return;
			}
			if(selections[i].get("verify_state")=='1'){
				Ext.Msg.alert("提示", "存在已提交审核案例，无法重复提交!");
				return;
			}
			if(selections[i].get("verify_state")=='2'||selections[i].get("verify_state")=='6'||selections[i].get("verify_state")=='7'||selections[i].get("verify_state")=='8'){
				Ext.Msg.alert("提示", "存在已审核案例，无法重复提交!");
				return;
			}
			if(selections[i].get("verify_state")=='3'||selections[i].get("verify_state")=='5'){
				Ext.Msg.alert("提示", "存在已审核案例，无法重复提交!");
				return;
			}
		}
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if((i+1)==selections.length)
				{
					taskId +=  selections[i].get("task_id");
					case_ids += selections[i].get("id")
				}
			else
				{

					taskId += selections[i].get("task_id")+",";
					case_ids += selections[i].get("id")+",";
				}
		}
		
//	console.log(taskId)
		
		var values = {
			taskId: taskId
			
		};
	//console.log(values.taskId);
		if (values.taskId == null || values.taskId == "") {
			Ext.Msg.alert("提示", "该记录不能进行此项操作3!");
			return;
		}
		
		Ext.MessageBox.confirm('提示','确定提交审核此案例吗？',
				function(id) {
					if (id == 'yes') {
						Ext.MessageBox.wait('正在操作','请稍后...');
						Ext.Ajax.request({
							url: 'bacera/invasivePre/updateVerifyAll.do',
							method: "POST",
							headers: {
								'Content-Type': 'application/json'
							},
							jsonData: {
								'id':case_ids ,
							},
							success: function (response, options) {
								response = Ext.JSON.decode(response.responseText);
//								console.log(response);
								if (response.result==true) {
									Ext.Msg.alert("提示", response.message);
									me.getStore().load();
								} else {
									Ext.MessageBox.alert("错误信息", response.message);}
							},
							failure: function () {
								Ext.Msg.alert("提示", "发生错误，请联系管理员!");
							}
						});		
					}
				});
		
		
	},
	attachmentManage:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		};
		if(selections[0].get("cancelif") == '1')
		{
			Ext.Msg.alert("提示", "该记录已作废!");
			return;
		}
		attachment_onInsert=function(me)
		{

			attachment_save=function(mei)
			{
				var form = mei.up("form").getForm();
				if (!form.isValid()) {
					Ext.MessageBox.alert("提示信息", "请上传正确格式图片!");
					return;
				}
				form.submit({
							url : 'bacera/invasivePre/upload.do',
							method : 'post',
							waitMsg : '正在上传您的文件...',
							success : function(form, action) {
								response = Ext.JSON
										.decode(action.response.responseText);
								if (response) {
									Ext.MessageBox.alert("提示信息", "上传成功！");
									var grid = me.up("gridpanel");
									grid.getStore().load();
									mei.up("window").close();
								} else {
									Ext.MessageBox.alert("提示信息", "上传失败，不可重复上传!");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "上传失败，检查是否存在照片!");
							}
						});
			}
			var attachment_insert = Ext.create("Ext.window.Window", {
				title : '附件上传',
				width : 500,
				height : 400,
				layout : 'border',
				items : [{
					xtype : 'form',
					region : 'center',
					style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					labelAlign : "right",
					autoScroll : true,
					bodyPadding : 10,
					defaults : {
						anchor : '100%',
						labelWidth : 80,
						labelSeparator : "：",
						labelAlign : 'right'
					},
					border : false,
					autoHeight : true,
					buttons : [{
								text : '保存',
								iconCls : 'Disk',
								handler : attachment_save
							}, {
								text : '取消',
								iconCls : 'Cancel',
								handler : case_canel
							}],
					items : [{
						xtype : 'fieldset',
						title : '案例附件',
						id:'testFieldset',
						layout : 'anchor',
						defaults : {
							anchor : '100%'
						},
						items : [{
							layout : "column",// 从左往右的布局
							xtype : 'fieldcontainer',
							border : false,
							items : [ 	 {
								xtype : 'filefield',
								name : 'files',
								fieldLabel : '文件<span style="color:red">*</span>',
								msgTarget : 'side',
								allowBlank : false,
								labelWidth : 40,
								anchor : '100%',
								buttonText : '选择文件',
								columnWidth : 0.6,
								validator : function(v) {
									if (!v.endWith(".jpg") &&!v.endWith(".JPG")
											&& !v.endWith(".png") && !v.endWith(".PNG")
											&& !v.endWith(".gif") && !v.endWith(".GIF")
											&& !v.endWith(".jpeg") && !v.endWith(".JPEG") ) {
										return "请选择.jpg .png .gif.jpeg类型的图片";
									}
									return true;
								}
							},new Ext.form.field.ComboBox({
								fieldLabel : '文件类型<span style="color:red">*</span>',
								labelWidth : 70,
								labelAlign : 'right',
								fieldStyle : me.fieldStyle,
								editable : false,
								allowBlank : false,
								triggerAction : 'all',
								displayField : 'Name',
								valueField : 'Code',
								store : new Ext.data.ArrayStore({
									fields : ['Name', 'Code'],
									data : [
											['照片', 3]]
								}),
								mode : 'local',
								name : 'filetype',
								columnWidth : .4,
								value:3,
								style:'margin-left:10px;'
							})
							]
						}
						]
					
			     }, {
						xtype : 'hiddenfield',
						name : 'case_id',
						value : selections[0].get("id")
					}]
				}]
			})
			attachment_insert.show();
			
			
		}
		attachment_onDel = function(me) {
			var grid = me.up("gridpanel");
			var selections = grid.getView().getSelectionModel().getSelection();
			if (selections.length < 1) {
				Ext.Msg.alert("提示", "请选择需要修改的记录!");
				return;
			};
//			if(!(selections[0].get("verify_state") == '0' || selections[0].get("verify_state") == '2'))
//			{
//				Ext.MessageBox.alert("提示信息","该案例状态不允许删除！");
//				return;
//			}
			Ext.Msg.show({
						title : '提示',
						msg : '确定删除该记录?',
						width : 300,
						buttons : Ext.Msg.OKCANCEL,
						fn : function(buttonId, text, opt) {
							if (buttonId == 'ok') {
								var values = {
										case_id : selections[0].get("case_id"),
                                        photo_path:selections[0].get("photo_path"),
								};
								Ext.Ajax.request({
											url : "bacera/invasivePre/deleteFile.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : values,
											success : function(response, options) {
												response = Ext.JSON
														.decode(response.responseText);
												if (response.result == true) {
													Ext.MessageBox.alert("提示信息",
															response.msg);
													var grid = me.up("gridpanel");
													grid.getStore().load();
												} else {
													Ext.MessageBox.alert("错误信息",
															response.msg);
												}
											},
											failure : function() {
												Ext.Msg.alert("提示",
														"保存失败<br>请联系管理员!");
											}

										});
							} else {
								return;
							}

						},
						animateTarget : 'addAddressBtn',
						icon : Ext.window.MessageBox.INFO
					})
		}
		var win = Ext.create("Ext.window.Window", {
			title : "照片管理",
			width : 700,
			iconCls : 'Find',
			height : 400,
			modal:true,
			resizable:false,
			layout : 'border',
			bodyStyle : "background-color:white;",
			items : [ Ext.create('Ext.grid.Panel', {
				renderTo : Ext.getBody(),
				width : 690,
				height : 400,
				frame : false,
				viewConfig : {
					forceFit : true,
					stripeRows : true// 在表格中显示斑马线
				},
				dockedItems : [{
					xtype : 'toolbar',
					dock : 'top',
					items : [ {
								xtype : 'button',
								text : '新增',
								iconCls : 'Pageedit',
								handler : attachment_onInsert
							}, {
								xtype : 'button',
								text : '删除',
								iconCls : 'Delete',
								handler : attachment_onDel
							}

					]
				}],
				store : {// 配置数据源
					fields : [ 'photo_id', 'case_id', 'photo_path','upload_time','photo_type','upload_user','upload_username' ],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'bacera/invasivePre/queryAllPhoto.do',
						params : {
							'case_id' : selections[0].get("id")
						},
						reader : {
//							type : 'json',
//							root : 'items',
//							totalProperty : 'count'
						}
					},
					autoLoad : true
				},
				columns : [// 配置表格列
				           {
							text : '类型',
							dataIndex : 'photo_type',
							width : '40%',
							menuDisabled : true,
							flex : 1,
							renderer : function(value) {
								switch (value) {
									case 1 :
										return "照片";
										break;
									default :
										return "照片";
								}
							}
						}, {
							text : '附件',
							dataIndex : 'photo_path',
							width : '40%',
							menuDisabled : true,
							flex : 3
						}, {
							text : '最后上传日期',
							dataIndex : 'upload_time',
							width : '10%',
							menuDisabled : true,
							flex : 1
						},
						{
							text : '上传人员',
							dataIndex : 'upload_username',
							width : '10%',
							menuDisabled : true,
							flex : 1,
							renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var isnull= record.data["upload_username"];
								if (isnull == null) {
									return "系统生成"
								} else
								{
									return value;
								}

							}
						}, {
							header : "操作",
							dataIndex : '',
							flex : 0.5,
							menuDisabled : true,
							renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
									return "<a href='#'>查看</a>";
							},
							listeners:{
								'click':function(){ 
									var me = this.up("gridpanel");
									var selections = me.getView().getSelectionModel().getSelection();
									if (selections.length < 1 || selections.length > 1) {
										Ext.Msg.alert("提示", "请选择需要查看的一条记录!");
										return;
									}
									var form = Ext.create(
											"Rds.bacera.form.BaceraImageShow", {
												region : "center",
												grid : me
											});
									var win = Ext.create("Ext.window.Window", {
												title : '图片查看',
												width : 1600,
												iconCls : 'Pageedit',
												height : 1000,
												maximizable : true,
												maximized : true,
												layout : 'border',
												items : [form]
											});
									form.loadRecord(selections[0]);
									win.show();
								}
							}
						} ]
			}) ]
		});
		win.show();
		
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
	    		Ext.Ajax.request({  
	    			url:"bacera/invasivePre/onCancel.do", 
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
	listeners : {
		'afterrender' : function() {
			this.store.load();
		},
		'cellclick': function (grid, td, cellIndex, record, tr, rowIndex, e) {
			//查看审核不通过原因
			if (e.getTarget('.lbtnComment')) {
				Ext.create("Ext.window.Window", {
					title: '审核不通过原因',
					width: 500,
					height: 250,
					modal: true,
					layout: 'fit',
					items: [{
						xtype: "gridpanel",
						columns: [
							{
								header: "审核时间",
								dataIndex: "timeString",
								align: 'center',
								width: 150,
								sortable: false,
								menuDisabled: true
							},
							{
								header: "审核人",
								dataIndex: "userId",
								align: 'center',
								width: 100,
								sortable: false,
								menuDisabled: true
							},
							{
								header: "审核意见",
								dataIndex: "fullMessage",
								align: 'center',
								flex: 1,
								sortable: false,
								menuDisabled: true,
								 renderer : function(value, cellmeta, record,
											rowIndex, columnIndex, store) {
//										var str = value;
//										if (value.length > 15) {
//											str = value.substring(0, 15) + "...";
//										}
									 if(null == value) return "";
									 else 
										return "<span title='" + value + "'>" + value
												+ "</span>";
									}
							}
						],
						store: Ext.create('Ext.data.Store', {
							fields: ['fullMessage', 'userId', 'time', {
								name: 'timeString', type: 'date',
								convert: function (v, rec) {
									return rec.data.time == null ? "" : Ext.Date.format(new Date(rec.data.time), 'Y-m-d H:i');
								}
							}],
							autoLoad: true,
							proxy: {
								type: 'jsonajax',
								actionMethods: {
									read: 'POST'
								},
								url: 'activiti/main/getProcessInstanceComments.do',
								params: {"processInstanceId": record.data.processInstanceId},
								reader: {
									type: 'json'
								}
							}
						})
					}]
				}).show();
			}
		}
	}
	
});