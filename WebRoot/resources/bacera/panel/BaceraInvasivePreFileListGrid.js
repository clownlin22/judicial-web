Ext.define('Rds.bacera.panel.BaceraInvasivePreFileListGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    selType: 'rowmodel',

	initComponent : function() {
		var me = this;
		var search = Ext.create('Ext.form.field.Text',{
        	name:'num',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'案例编号'
        });
		var expressnum = Ext.create('Ext.form.field.Text',{
			name:'expressnum',
			labelWidth:60,
        	width:'20%',
			fieldLabel:'快递单号'
		});
		var name = Ext.create('Ext.form.field.Text',{
	    	name:'name',
	    	labelWidth:60,
        	width:'20%',
	    	fieldLabel:'姓名'
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
					value : '7',
					mode : 'local',
					name : 'verify_state',
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
						data : [['全部',''],['NIPT(博奥)','1' ],['NIPT(成都博奥)','5' ],['NIPT(贝瑞)','2' ],['NIPT-plus(博奥)','3' ],['NIPT-plus(贝瑞)','4' ] ]
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
		var invasive_starttime = new Ext.form.DateField({
			name : 'invasive_starttime',
        	width:'20%',
			fieldLabel : '日期从',
			labelWidth : 70,
			emptyText : '请选择日期',
			format : 'Y-m-d'
		});
		var invasive_endtime = new Ext.form.DateField({
			name : 'invasive_endtime',
        	width:'20%',
			fieldLabel : '到',
			labelWidth : 20,
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d'
		});
		var express_starttime = new Ext.form.DateField({
			name : 'express_starttime',
        	width:'20%',
			fieldLabel : '快递日期从',
			labelWidth : 70,
			emptyText : '请选择日期',
			format : 'Y-m-d'
		});
		var express_endtime = new Ext.form.DateField({
			name : 'express_endtime',
        	width:'20%',
			fieldLabel : ' 到 ',
			labelWidth : 20,
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d'
		});
		
		var areacode= new Ext.form.field.ComboBox({
			fieldLabel : '归属地',
			labelWidth : 60,
        	width:'20%',
			name : 'areacode',
			id:'areacode3',
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
		var mailStore = Ext.create('Ext.data.Store', {
			fields:['key','value'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getMailModels.do',
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','name','code','hospital','inspectionunit','sampledate','date','ownpersonname',
			        'ownperson','agentname','remark','remarks','cancelif', 'verify_state','receivables',
			        'payment','paid','paragraphtime','account_type','areaname',
			        'expressnum','expresstype','recive','expresstime','expressremark','task_id','task_def_key','processInstanceId','task_name',
			        'suspension_state','has_comment','last_task_id','attachment_path','uuid'],
			    proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/invasivePre/queryAllExpress.do',
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
        				agent:agent.getValue(),
        				areacode:areacode.getValue(),
        				code:code.getValue(),
        				areaname:Ext.getCmp('areacode3').getRawValue(),
        				hospital:hospital.getValue(),
        				inspectionunit:inspectionunit.getValue(),
            			expressnum:expressnum.getValue(),
            			express_starttime:dateformat(express_starttime.getValue()),
            			express_endtime:dateformat(express_endtime.getValue()),
            			verify_state:verify_state.getValue()
        				});
        		}
            }
		});
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			//mode: 'SINGLE'
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
//		me.bbar = {xtype: 'label',id:'totalBbarInvasiveFile', text: '',
//				   style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
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
	              { text: '审核状态',	dataIndex: 'verify_state', menuDisabled:true, width : 120,
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
//	            		  case "3":
//	            			  return "<span style='color:green'>审核通过</span>";
//	            			  break;
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
					
	              { text: '快递单号',	dataIndex: 'expressnum', menuDisabled:true, width : 100,
	            	
	              },
	              { text: '快递类型',	dataIndex: 'expresstype', menuDisabled:true, width : 110,
	              },
                  { text: '快递日期',	dataIndex: 'expresstime', menuDisabled:true, width : 110,
	            	  
	              },
                  { text: '收件人',	dataIndex: 'recive', menuDisabled:true, width : 120,

	              },{ text: '快递备注',	dataIndex: 'expressremark', menuDisabled:true, width : 80,
              
                  },
                  { text: '日期',	dataIndex: 'date', menuDisabled:true, width:80},

                  { text: '到款日期',	dataIndex: 'paragraphtime', menuDisabled:true, width : 80},

                  { text: '财务备注',	dataIndex: 'remarks', menuDisabled:true, width : 80},
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
                  { text: '备注要求',	dataIndex: 'remark', menuDisabled:true, width : 300}
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
		        borderBottomWidth : '1px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[expressnum,express_starttime,express_endtime,verify_state,{
	            	text:'查询',
	            	iconCls:'Find',
	            	handler:me.onSearch
	            }]
	 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[
	 		    {text: '确认归档',
				iconCls: 'Pageedit',
				handler: me.onFile
			},{
				text: '查看流程状态',
				iconCls: 'Pageedit',
				handler: me.onTaskHistory
			}]
	 	}];
//		me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbarInvasiveFile').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	},
	onDownload : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		window.location.href = "Bacera/InvasivePreAttachment/download.do?uuid=" +
				selections[0].get("uuid");

	},
	onFile : function() {
        var me = this.up("gridpanel");
    var selections = me.getView().getSelectionModel()
					.getSelection();
			if (selections.length < 1) {
				Ext.Msg.alert("提示", "请选择确认归档的案例!");
				return;
			};
			if(selections[0].get("cancelif") == 1)
			{
				Ext.Msg.alert("提示", "该案例已作废，不能归档操作!");
				return;
			}
			if ('8'==selections[0].get("verify_state")) {
				Ext.Msg.alert("提示", "该案例已归档!");
				return;
			};
			if ('7'!=selections[0].get("verify_state")) {
				Ext.Msg.alert("提示", "该案例不在确认归档状态!");
				return;
			};
				var id=selections[0].get("id");
				var task_id=selections[0].get("task_id");
				var process_instance_id =selections[0].get("process_instance_id");
				var attachment_path = selections[0].get("attachment_path");
				Ext.MessageBox.confirm("提示", "确认归档？", function (btn) {
		        if("yes"==btn)
		        {
		        	
		        	Ext.MessageBox.wait('正在处理','请稍后...');
		    		Ext.Ajax.request({  
		    			url : "Bacera/InvasivePreAttachment/onFile.do",
		    			method: "POST",
		    			headers: { 'Content-Type': 'application/json' },
		    			jsonData: {
							'id':id ,
							'task_id':task_id,
							'process_instance_id':process_instance_id,
							'attachment_path':attachment_path,
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
							data: response
						})
					}
				});
				win.show();

			},
			failure: function () {
				Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
			}
		});
	},listeners : {
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