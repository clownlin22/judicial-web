onDownload = function(value)
{
	window.location.href = "Bacera/InvasivePreAttachment/download.do?uuid=" +value;
}
var invasiceAttach=""
Ext.define("Rds.bacera.panel.BaceraInvasivePreAttachmentGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var invasive_starttime = new Ext.form.DateField({
			name : 'invasive_starttime',
			width:'20%',
			fieldLabel : '日期从',
			labelWidth : 70,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var invasive_endtime = new Ext.form.DateField({
			name : 'invasive_endtime',
			width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var upload_username = Ext.create('Ext.form.field.Text', {
			name : 'upload_username',
			labelWidth : 80,
			width : '20%',
			fieldLabel : '上传人员'
		});
		var verify_state = Ext.create('Ext.form.ComboBox', 
				{
					fieldLabel : '审核状态',
					width : '20%',
					labelWidth : 60,
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
										['待报告上传','3'],
//										['待报告上传','4'],
										['待报告确认','5'],
										['邮寄中','6'],
										['归档中','7'],
										['已归档','8']
										]
							}),
					value : '3',
					mode : 'local',
					name : 'verify_state',
				});
		var confirm_state=Ext.create('Ext.form.ComboBox', 
				{
			fieldLabel : '财务状态',
			width : '19.5%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{fields : ['Name','Code' ],
						data : [
								['全部',null ],
								['未汇款','' ],
								['未确认','-1'],
								['汇款确认通过','1'],
								['汇款确认不通过','2']

								]
					}),
			value : null,
			mode : 'local',
			name : 'confirm_state',
		});
		var num = Ext.create('Ext.form.field.Text', {
			name : 'num',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '案例编号'
		});
		var name = Ext.create('Ext.form.field.Text',{
	    	name:'name',
	    	labelWidth:60,
	    	width:'20%',
	    	fieldLabel:'姓名'
	    });
		var emailflag=new Ext.form.field.ComboBox({
			fieldLabel : '是否发送邮件',
			width:'20%',
			labelWidth : 80,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部',''],['未发送','0' ],['已发送','1'] ]
					}),
			value : '',
			mode : 'local',
			name : 'emailflag',
		});
		var download_username = Ext.create('Ext.form.field.Text', {
			name : 'download_username',
			labelWidth : 60,
			width : '20%',
			fieldLabel : '下载人员'
		});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['uuid','attachment_path','name','upload_username','id',
							'attachment_date','download_time','download_username',
							'num','verify_state','process_instance_id','task_id'
							,'task_def_key','task_name','suspension_state','cancelif','emailflag','confirm_state','type','receiveAddress','ownpersonname'],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'Bacera/InvasivePreAttachment/queryAttachMent.do',
						params : {},
						reader : {
							type : 'json',
							root : 'data',
							totalProperty : 'total'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							invasiceAttach="start_time="+dateformat(me.getDockedItems('toolbar')[0].
                                    getComponent('start_time').getValue())+	"&end_time="+dateformat(me.getDockedItems('toolbar')[0].
                                            getComponent('end_time').getValue())+
                                            "&upload_username="+trim(upload_username.getValue())+
                                            "&download_username="+trim(download_username.getValue())+
                                            "&num="+trim(num.getValue())+
                                            "&name="+trim(name.getValue())+"&confirm_state"+confirm_state.getValue()+
                                            "&verify_state="+verify_state.getValue()+
                                            "&invasive_starttime="+dateformat(invasive_starttime.getValue())+
                                            "&invasive_endtime="+dateformat(invasive_endtime.getValue())+"&emailflag="+emailflag.getValue();
							Ext.apply(me.store.proxy.params, {
                                start_time:dateformat(me.getDockedItems('toolbar')[0].
                                    getComponent('start_time').getValue()),
                                end_time:dateformat(me.getDockedItems('toolbar')[0].
                                    getComponent('end_time').getValue()),
                                upload_username:trim(upload_username.getValue()),
                                download_username:trim(download_username.getValue()),
                                num:trim(num.getValue()),
                                confirm_state:confirm_state.getValue(),
                                name:trim(name.getValue()),
                                verify_state:verify_state.getValue(),
                                invasive_starttime:dateformat(invasive_starttime.getValue()),
                    			invasive_endtime:dateformat(invasive_endtime.getValue()),
                    			emailflag:emailflag.getValue()
									});
						}
					}
				});

		me.selModel = Ext.create('Ext.selection.CheckboxModel', {
					mode : 'SINGLE'
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
                  { text: '姓名',	dataIndex: 'name', menuDisabled:true, width : 80},
                  { text: '邮箱地址',	dataIndex: 'receiveAddress', menuDisabled:true, width : 80,hidden:true},
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
            			  return "<span style='color:red'>待报告上传</span>";
            			  break;
//            		  case "4":
//            			  return "<span style='color:red'>待报告上传</span>";
//            			  break;
            		  case "5":
            			  return "<span style='color:green'>待报告确认</span>";
            			  break;
            		  case "6":
            			  return "<span style='color:red'>邮寄中</span>";
            			  break;
            		  case "7":
            		      return "<span style='color:green'>归档中</span>";
            		       break;
            		  case "8":
            			  return "<span style='color:red'>已归档</span>";
            			  break;
            		  }
            	  }
              },   { text: '市场归属人',	dataIndex: 'ownpersonname', menuDisabled:true, width : 80},

              {
					text : '附件',
					dataIndex : 'attachment_path',
					width : 200,
					menuDisabled : true
				}, {
					text : '案例状态',
					dataIndex : 'type',
					width : 100,
					menuDisabled : true,
					renderer:function(value,meta,record){
	            		  switch(value){
	            		  case "0":
	            			  return "正常";
	            			  break;
	            		  case "1":
							  return "<span style='color:green'>先出报告后付款</span>";
							  break;
	            		  case "2":
							  return "<span style='color:green'>免单</span>";
							  break;
	            		  case "3":
							  return "<span style='color:red'>优惠</span>";
							  break;
	            		  case "4":
							  return "<span style='color:green'>月结</span>";
							  break;
	            		  case "5":
							  return "<span style='color:red'>二次采样</span>";
							  break;
	            		  case "6":
							  return "<span style='color:red'>补样</span>";
							  break;
				  }
					}
		
					
				},
				{
					text : '最后上传日期',
					dataIndex : 'attachment_date',
					width : 150,
					menuDisabled : true
				},
				 {
					text : 'id',
					dataIndex : 'id',
					width : 150,
					menuDisabled : true,
					hidden:true
				},
				{
					text : 'task_id',
					dataIndex : 'task_id',
					width : 150,
					menuDisabled : true,
					hidden:true
				},
				{
					text : 'uuid',
					dataIndex : 'uuid',
					width : 150,
					menuDisabled : true,
					hidden:true
				},
				{
					text : 'task_def_key',
					dataIndex : 'task_def_key',
					width : 150,
					menuDisabled : true,
					hidden:true
				},
				{
					text : '上传人员',
					dataIndex : 'upload_username',
					width : 100,
					menuDisabled : true
				},
				{
					text : '下载人员',
					dataIndex : 'download_username',
					width : 100,
					menuDisabled : true
				},
				{
					text : '最后下载日期',
					dataIndex : 'download_time',
					width : 150,
					menuDisabled : true
				},
				{
					header : "操作",
					dataIndex : 'uuid',
					width : 60,
					menuDisabled : true,
					renderer : function(value, cellmeta, record,
							rowIndex, columnIndex, store) {
						var uuid = record.data["uuid"];
						if(null!=uuid){
							return "<a href='#' onclick=\"onDownload('"
							+ uuid
							+ "')\"  >下载";
						}
						

					}
				},
				{
					text : '是否发送邮件',
					dataIndex : 'emailflag',
					width : 100,
					menuDisabled : true,
					renderer:function(value,meta,record){
	            		  switch(value){
	            		  case "0":
	            			  return "<span style='color:red'>未发送</span>";
	            			  break;
	            		  case "1":
							  return "<span style='color:green'>已发送</span>";
							  break;
				  }
					}
				  },
				  {
						text : '财务确认状态',
						dataIndex : 'confirm_state',
						width : 100,
						menuDisabled : true,
						renderer:function(value,meta,record){
		            		  switch(value){
		            		  case "-1":
		            			  return "<span style='color:red'>未确认</span>";
		            			  break;
		            		  case "1":
								  return "<span style='color:green'>汇款确认通过</span>";
								  break;
		            		  case "2":
								  return "<span style='color:red'>汇款确认不通过</span>";
								  break;
		            		  case null:
								  return "<span style='color:'>未汇款</span>";
								  break;
					  }
						}
					  }
				];
        me.dockedItems = [
            {
                xtype : 'toolbar',
                name : 'search',
                dock : 'top',
                items : [num,name,
                    {
                        xtype : 'datefield',
                        name : 'start_time',
                        itemId : 'start_time',
                        width : '20%',
                        fieldLabel : '上传时间从',
                        labelWidth : 70,
                        emptyText : '请选择日期',
                        format : 'Y-m-d',
                        maxValue : new Date(),
                        listeners : {
                            'select' : function() {
                                var start = me.getDockedItems('toolbar')[0].
                                    getComponent('start_time').getValue();
                                me.getDockedItems('toolbar')[0].
                                    getComponent('end_time')
                                    .setMinValue(
                                    start);
                                var endDate = me.getDockedItems('toolbar')[0].
                                    getComponent('end_time').getValue()
                                if (start > endDate) {
                                    me.getDockedItems('toolbar')[0].
                                        getComponent('start_time').setValue(start);
                                }
                            }
                        }
                    },
                    {
                        xtype : 'datefield',
                        itemId : 'end_time',
                        name : 'end_time',
                        width : '20%',
                        labelWidth : 20,
                        fieldLabel : '到',
                        labelAlign : 'right',
                        emptyText : '请选择日期',
                        format : 'Y-m-d',
                        maxValue : new Date(),
//                        value : Ext.Date.add(
//                            new Date(),
//                            Ext.Date.DAY),
                        listeners : {
                            select : function() {
                                var start = me.getDockedItems('toolbar')[0].
                                    getComponent('start_time').getValue();
                                var endDate = me.getDockedItems('toolbar')[0].
                                    getComponent('end_time').getValue();
                                if (start > endDate) {
                                    me.getDockedItems('toolbar')[0].
                                        getComponent('start_time').
                                        setValue(endDate);
                                }
                            }
                        }
                    }, upload_username]
            },{
		 		style : {
			        borderTopWidth : '0px !important',
			        borderBottomWidth : '0px !important'
			 		},
			 		xtype:'toolbar',
			 		name:'search',
			 		dock:'top',
			 		items:[download_username,verify_state,invasive_starttime,invasive_endtime,emailflag]
			 	
		 	},{
		 		style : {
			        borderTopWidth : '0px !important',
			        borderBottomWidth : '0px !important'
			 		},
			 		xtype:'toolbar',
			 		name:'search',
			 		dock:'top',
			 		items:[confirm_state,{
		            	text:'查询',
		            	iconCls:'Find',
		            	handler:me.onSearch
		            }]
			 	
		 	}
            , {
                xtype : 'toolbar',
                dock : 'top',
                items : [{
                    text : '上传报告',
                    iconCls : 'Add',
                    handler : me.onInsert
                }, {
                    text : '删除报告',
                    iconCls : 'Delete',
                    handler : me.onDelete
                },
                {
                    text : '确认邮寄',
                    iconCls : 'Mail',
                    handler : me.onMail
                },{
    				text: '查看流程状态',
    				iconCls: 'Pageedit',
    				handler: me.onTaskHistory
    			},    {
                    text : '发送邮件',
                    iconCls : 'Mail',
                    handler : me.sendMail
                }, {
                    text : '邮件详情',
                    iconCls : 'Mail',
                    handler : me.EMail
                },{
    	 			text:'导出',
    	 			iconCls:'Application',
    	 			handler:me.invasivePerExport
    	 		}]
            }];
		me.callParent(arguments);
	},
	invasivePerExport:function(){
		window.location.href = "Bacera/InvasivePreAttachment/exportInvasivePreAttachment.do?"+invasiceAttach ;
	},
	onTaskHistory: function () {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		}
		var values = {
			processInstanceId: selections[0].get("process_instance_id")
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
	
	EMail: function () {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1) {
			Ext.Msg.alert("提示", "请选择需要操作的记录!");
			return;
		}
		var values = {
				id: selections[0].get("id")
			};
		Ext.Ajax.request({
			url: 'bacera/sendEmail/queryEmail.do',
			method: "POST",
			headers: {'Content-Type': 'application/json'},
			jsonData: values,
			success: function (response, options) {
				response = Ext.JSON.decode(response.responseText);
//				console.log(response);
				var win = Ext.create("Ext.window.Window", {
					title: '发送邮件记录',
					width: 700,
					iconCls: 'Add',
					layout: 'fit',
					items: {
						xtype: 'grid',
						border: false,
						columns: [
							{
								text: 'id',
								dataIndex: 'id',
								align: 'center',
								sortable: false,
								menuDisabled: true,
								hidden: true
							},
							{
								text: '邮件主题',
								dataIndex: 'subject',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '发件箱',
								dataIndex: 'emailFrom',
								align: 'center',
								sortable: false,
								menuDisabled: true,
								
							},
							{
								text: '发件人',//操作人
								dataIndex: 'emailUserName',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '收件箱',
								dataIndex: 'toEmails',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '附件',
								dataIndex: 'attachFileNames',
								align: 'center',
								sortable: false,
								menuDisabled: true
							},
							{
								text: '发送时间',
								dataIndex: 'emaildateString',
								align: 'center',
								sortable: false,
								width: 170,
								menuDisabled: true
								
							}
						],
						store: Ext.create("Ext.data.Store", {
							fields: ['id', 'subject', 'emailFrom', 'emailUserName', 'toEmails', 'attachFileNames', 'emaildate',
							         {
								name: 'emaildateString',type: 'date',
								convert: function (v, rec) {
									return rec.data.emaildate == null ? "" : Ext.Date.format(new Date(rec.data.emaildate), 'Y-m-d H:i');
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
	},
	onInsert : function() {
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要上传报告的案例!");
			return;
		};
		if (1==selections[0].get("cancelif")){
			Ext.Msg.alert("提示", "该案例已作废,无法操作!");
			return;
		};
		if (null!=selections[0].get("attachment_path")){
			Ext.Msg.alert("提示", "该案例已上传报告!");
			return;
		};
		if (3!=selections[0].get("verify_state")){
			Ext.Msg.alert("提示", "该案例不在报告上传状态,无法操作!");
			return;
		};
			var form = Ext.create("Rds.bacera.form.BaceraInvasivePreAttachmentForm",
					{
						region : "center",
						grid : me
					});
			var win = Ext.create("Ext.window.Window", {
						title : '上传新文件',
						width : 400,
						iconCls : 'Add',
						height : 200,
						layout : 'border',
						modal : true,
						items : [form],
						listeners : {
							close : {
								fn : function() {
									me.getStore().load();
								}
							}
						}
					});
			win.show();
			form.loadRecord(selections[0]);

	},
	onDelete : function() {
		                var me = this.up("gridpanel");
		            var selections = me.getView().getSelectionModel()
									.getSelection();
							if (selections.length < 1) {
								Ext.Msg.alert("提示", "请选择需要删除的记录!");
								return;
							};
							if(null == selections[0].get("attachment_path"))
							{
								Ext.Msg.alert("提示", "该案例未上传报告,无法操作!");
								return;
							}
							if (1==selections[0].get("cancelif")){
								Ext.Msg.alert("提示", "该案例已作废,无法操作!");
								return;
							};
							if (8==selections[0].get("verify_state")){
								Ext.Msg.alert("提示", "该案例已归档,无法操作!");
								return;
							};
								var id=selections[0].get("id");
								var task_id=selections[0].get("task_id");
								var process_instance_id =selections[0].get("process_instance_id");
								var attachment_path = selections[0].get("attachment_path");
								var task_def_key=selections[0].get("task_def_key");
								Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
						        if("yes"==btn)
						        {
						        	
						        	Ext.MessageBox.wait('正在删除','请稍后...');
						    		Ext.Ajax.request({  
						    			url : "Bacera/InvasivePreAttachment/delete.do",
						    			method: "POST",
						    			headers: { 'Content-Type': 'application/json' },
						    			jsonData: {
											'id':id ,
											'task_id':task_id,
											'process_instance_id':process_instance_id,
											'attachment_path':attachment_path,
											'task_def_key':task_def_key,
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
	onMail : function() {
        var me = this.up("gridpanel");
    var selections = me.getView().getSelectionModel()
					.getSelection();
			if (selections.length < 1) {
				Ext.Msg.alert("提示", "请选择确认邮寄的案例!");
				return;
			};
			if(selections[0].get("verify_state")=='6'){
				Ext.Msg.alert("提示", "该案例已确认邮寄!");
				return;
			};
			if (selections[0].get("verify_state")=='5') {
				
				var id=selections[0].get("id");
				var task_id=selections[0].get("task_id");
				var process_instance_id =selections[0].get("process_instance_id");
				var attachment_path = selections[0].get("attachment_path");
				Ext.MessageBox.confirm("提示", "确认邮寄？", function (btn) {
		        if("yes"==btn)
		        {
		        	
		        	Ext.MessageBox.wait('正在处理','请稍后...');
		    		Ext.Ajax.request({  
		    			url : "Bacera/InvasivePreAttachment/Mail.do",
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
				
			} else if(selections[0].get("verify_state")=='3') {
				Ext.Msg.alert("提示", "请先上传报告!");
				return;
			}else{
				Ext.Msg.alert("提示", "该案例不在确认邮寄状态!");
				return;
			};
			
						
					
},
sendMail:function(){
	var me = this.up("gridpanel");
	var selections =  me.getView().getSelectionModel().getSelection();
	if(selections.length<1){
		Ext.Msg.alert("提示", "请选择需要发送邮件的案例!");
		return;
	};
	if(selections[0].get("cancelif") == '1')
	{
		Ext.Msg.alert("提示", "该记录已作废!");
		return;
	};
	if (null==selections[0].get("attachment_path")){
		Ext.Msg.alert("提示", "请先上传报告!");
		return;
	};
	if(selections[0].get("confirm_state") != '1'&& selections[0].get("type")!=1&& selections[0].get("type")!=2 
			&& selections[0].get("type")!=4)
	{
		Ext.Msg.alert("提示", "该案例需要先汇款，待财务确认!");
		return;
	};
	ownpersonTemp = "";
	var form = Ext.create("Rds.bacera.form.BaceraInvasiveSendMailForm",{
		region:"center",
		grid:me
	});
	var win = Ext.create("Ext.window.Window",{
		title:'发送邮件',
		width:560,
		modal:true,
		iconCls:'Mail',
		height:400,
		layout:'border',
		items:[form]
	});
	win.show();
	form.loadRecord(selections[0]);
},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage=1;
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
								params: {"processInstanceId": record.data.process_instance_id},
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




