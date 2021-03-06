var verify=""
Ext.define('Rds.bacera.panel.BaceraInvasiveVerifyListGrid', {
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
//        	width:'20%',
//	    	fieldLabel:'类型'
//	    });
		var hospital = Ext.create('Ext.form.field.Text',{
			name:'hospital',
			labelWidth:60,
        	width:'20%',
			fieldLabel:'所属医院'
		});
		var verify_starttime = new Ext.form.DateField({
			id:'verify_starttime',
			name : 'verify_starttime',
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
					var start = Ext.getCmp('verify_starttime').getValue();
	                var endDate = Ext.getCmp('verify_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('verify_starttime').setValue(endDate);
	                }
				}
			}
		});
		var verify_endtime = new Ext.form.DateField({
			id:'verify_endtime',
			name : 'verify_endtime',
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
					var start = Ext.getCmp('verify_starttime').getValue();
	                var endDate = Ext.getCmp('verify_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('verify_starttime').setValue(endDate);
	                }
				}
			}
		});
		var verify_sample_starttime = new Ext.form.DateField({
			id:'verify_sample_starttime',
			name : 'verify_sample_starttime',
        	width:'20%',
			fieldLabel : '采样日期从',
			labelWidth : 70,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
//			maxValue : new Date(),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('verify_sample_starttime').getValue();
	                var endDate = Ext.getCmp('verify_sample_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('verify_sample_starttime').setValue(endDate);
	                }
				}
			}
		});
		var verify_sample_endtime = new Ext.form.DateField({
			id:'verify_sample_endtime',
			name : 'verify_sample_endtime',
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
					var start = Ext.getCmp('verify_sample_starttime').getValue();
	                var endDate = Ext.getCmp('verify_sample_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('verify_sample_starttime').setValue(endDate);
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
					//					['待报告上传','3'],
										['待报告确认','5'],
										['邮寄中','6'],
										['归档中','7'],
										['已归档','8']
										]
							}),
					value : '1',
					mode : 'local',
					name : 'verify_state',
				});
		var areacode= new Ext.form.field.ComboBox({
			fieldLabel : '归属地',
			labelWidth : 60,
        	width:'20%',
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
			hideTrigger : true,
			minChars : 2,
			matchFieldWidth : true,
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
			}
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','name','code','hospital','inspectionunit','sampledate','date','ownpersonname',
					 'ownperson','agentname','reportif','remark','verify_state','cancelif','expresstype','expressnum','receivables','areaname',
					 'processInstanceId', 'task_id', 'task_def_key', 'areacode',
					 'task_name', 'suspension_state','has_comment','last_task_id','flag'],
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
            		verify = "num=" + search.getValue().trim() + "&name="
    				+ name.getValue().trim() + "&ownperson="
    				+ ownperson.getValue().trim() + "&invasive_starttime="
    				+ dateformat(verify_starttime.getValue())
    				+ "&invasive_endtime="
    				+ dateformat(verify_endtime.getValue())+"&check="+1
    				+ "&reportif=" + reportif.getValue()
    				+ "&cancelif=" + cancelif.getValue() +"&verify_state="+verify_state.getValue()+ "&agent="
    				+ agent.getValue().trim() + "&areacode="
    				+ areacode.getValue()+"&code="
    				+ code.getValue()+"&hospital="
    				+ hospital.getValue() +"&inspectionunit="
    				+ inspectionunit.getValue() +"&invasive_sample_starttime="
    				+ dateformat(verify_sample_starttime.getValue()) +"&invasive_sample_endtime="
    				+ dateformat(verify_sample_endtime.getValue());
            		Ext.apply(me.store.proxy.params, {num:search.getValue(),name:name.getValue(),ownperson:ownperson.getValue(),
            			verify_starttime:dateformat(verify_starttime.getValue()),
            			verify_endtime:dateformat(verify_endtime.getValue()),
        				reportif:reportif.getValue(),cancelif:cancelif.getValue(),
        				agent:agent.getValue(),
        				areacode:areacode.getValue(),
        				code:code.getValue(),
        				verify_state:verify_state.getValue(),
        				flag:1,
        				areaname:Ext.getCmp('areacode').getRawValue(),
        				hospital:hospital.getValue(),
        				inspectionunit:inspectionunit.getValue(),
        				verify_sample_starttime:dateformat(verify_sample_starttime.getValue()),
            			verify_sample_endtime:dateformat(verify_sample_endtime.getValue()),
        				});
        		}
            }
		});
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode: 'SINGLE'
		});
		me.bbar = Ext.create('Ext.PagingToolbar', {
			store : me.store,
			pageSize : me.pageSize,
			displayInfo : true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
			emptyMsg : "没有符合条件的记录"
		});
//		me.bbar = {xtype: 'label',id:'totalBbarVerify', text: '',style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
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
									return "<span style='color:#FF00FF'>待审核</span>";
								}else{
									return "<span style='color:red'>报告审核未通过</span><a class='lbtnComment' href='#'>查看原因</a>";
								}
								break;
                		  case "2":
                			  if(record.get("task_def_key")=="taskAudit"){
                                  return "<span style='color:#FF00FF'>待审核</span>";
                              }else{
                                 return "<span style='color:red'>报告审核未通过</span><a class='lbtnComment' href='#'>查看原因</a>";
                              }
								break;
//                		  case "3":
//                			  return "<span style='color:green'>审核通过</span>";
//                			  break;
                		  case "3":
                			  return "<span style='color:green'>待报告上传</span>";
                			  break;
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
                  { text: 'task_id',	dataIndex: 'task_id', menuDisabled:true, width : 100,hidden:true},
                  { text: 'processInstanceId',	dataIndex: 'processInstanceId', menuDisabled:true, width : 100,hidden:true},
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
	 		items:[code,inspectionunit,hospital,verify_starttime,verify_endtime]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[verify_sample_starttime,verify_sample_endtime,verify_state,reportif,{
	            	text:'查询',
	            	iconCls:'Find',
	            	handler:me.onSearch
	            }]
		 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[
	 		   {
	 			text:'报告审核',
	 			iconCls:'Pageedit',
	 			handler:me.onSubmit
	 		},
	 		{
	 			text:'导出',
	 			iconCls:'Application',
	 			handler:me.invasivePerExport
	 		},{
				text: '查看流程状态',
				iconCls: 'Pageedit',
				handler: me.onTaskHistory
			},{
		 			text:'案例详情',
		 			iconCls:'Find',
		 			handler:me.onCheck
		 	}]
	 	}];
//     	me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbarVerify').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	
	invasivePerExport:function(){
		window.location.href = "bacera/invasivePre/exportInvasiveInfo.do?"+verify ;
	},
	onCheck:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		};
			ownpersonTemp = selections[0].get("ownperson");
			areacodeTemp=selections[0].get("areacode");			
			var form = Ext.create("Rds.bacera.form.BaceraInvasivePreCheckForm",{
				region:"center",
				grid:me
			});
			var win = Ext.create("Ext.window.Window",{
				title:'无创产前—查看',
				width:1600,
				iconCls:'Pageedit',
				modal:true,
				height:1000,
				maximizable : true,
				maximized : true,
				layout:'border',	
				items:[form]
			});
			form.loadRecord(selections[0]);
			win.show();
		
	},
	onSubmit:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要审核的记录!");
			return;
		};
		if(selections[0].get("cancelif") == '1')
		{
			Ext.Msg.alert("提示", "该记录已作废，不能修改!");
			return;
		}if(selections[0].get("verify_state") == '2'&& selections[0].get("task_def_key")=="taskAudit"||selections[0].get("verify_state")== '1')
		{
			ownpersonTemp = selections[0].get("ownperson");
			areacodeTemp=selections[0].get("areacode");			
			var form = Ext.create("Rds.bacera.form.BaceraInvasivePreListForm",{
				region:"center",
				grid:me
			});
			var win = Ext.create("Ext.window.Window",{
				title:'无创产前—审核',
				width:1600,
				iconCls:'Pageedit',
				modal:true,
				height:1000,
				maximizable : true,
				maximized : true,
				layout:'border',	
				items:[form]
			});
			form.loadRecord(selections[0]);
			win.show();
			form.loadRecord(selections[0]);
			
		}else{
			Ext.Msg.alert("提示", "该记录不在待审核状态，不能审核!");
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
			Ext.Msg.alert("提示", "该记录不能进行此项操作2!");
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