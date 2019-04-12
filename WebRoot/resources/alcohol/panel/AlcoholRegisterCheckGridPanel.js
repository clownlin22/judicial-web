var alcoholStr='';
Ext.define("Rds.alcohol.panel.AlcoholRegisterCheckGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	region : 'center',
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var case_code =Ext.create('Ext.form.field.Text',{
        	name:'case_code',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'案例条形码'
        });
		var state = new Ext.form.field.ComboBox({
			fieldLabel : '状态信息',
        	width:'20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'right',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name', 'Code'],
						data : [['全部', -1], ['未审核', 0],
								['未通过', 1], ['已废除', 7],['未废除', -7]]
					}),
			value : -1,
			mode : 'local',
			// typeAhead: true,
			name : 'state'
		});
		var starttime = new Ext.form.DateField({
			name : 'starttime',
        	width:'20%',
			fieldLabel : '受理时间从',
			labelWidth : 70,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-30),
		});
		var endtime = new Ext.form.DateField({
			name : 'endtime',
        	width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : new Date(),
			maxValue : new Date()
		});
		var client =Ext.create('Ext.form.field.Text',{
        	name:'client',
        	labelWidth:40,
        	width:'20%',
        	fieldLabel:'委托人'
        });
		var checkper =Ext.create('Ext.form.field.Text',{
        	name:'checkper',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'送检人'
        });
		var checkper_phone =Ext.create('Ext.form.field.Text',{
        	name:'checkper_phone',
        	labelWidth:65,
        	width:'20%',
        	fieldLabel:'送检人电话'
        });
		var receiver =Ext.create('Ext.form.field.Text',{
        	name:'receiver',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'归属人'
        });
		var areacode= new Ext.form.field.ComboBox({
			fieldLabel : '所属地区',
			labelWidth : 60,
			name : 'area_code',
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
			forceSelection: true, 
        	width:'20%',
			typeAhead : false,
			hideTrigger : true,
			minChars : 2,
			matchFieldWidth : true,
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
			}
		});
		var mail_address =Ext.create('Ext.form.field.Text',{
        	name:'mail_address',
        	labelWidth:60,
        	width:'20%',
        	fieldLabel:'邮寄地址'
        });
		var mail_per =Ext.create('Ext.form.field.Text',{
        	name:'mail_per',
        	labelWidth:65,
        	width:'20%',
        	fieldLabel:'邮件接收人'
        });
		var mail_phone =Ext.create('Ext.form.field.Text',{
        	name:'mail_phone',
        	labelWidth:60,
        	width:'20%',
			labelAlign : 'right',
        	fieldLabel:'联系电话'
        });
		var isDoubleTube = new Ext.form.field.ComboBox({
			fieldLabel : 'AB管',
        	width:'20%',
			labelWidth : 65,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'right',
			valueField : 'Code',
			store : new Ext.data.ArrayStore({
						fields : ['Name', 'Code'],
						data : [['全部', ''], ['是', '1'],
								['否', '0']]
					}),
			value : '',
			mode : 'local',
			name : 'isDoubleTube'
		});
		var case_in_pername =Ext.create('Ext.form.field.Text',{
        	name:'case_in_pername',
        	labelWidth:60,
        	width:'20%',
			labelAlign : 'right',
        	fieldLabel:'登记人'
        });
		var sample_time_start = new Ext.form.DateField({
			id:'alcohol_sample_starttime',
			name : 'sample_time_start',
        	width:'20%',
			fieldLabel : '登记日期从',
			labelWidth : 70,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			listeners:{
				'select':function(){
					var start = Ext.getCmp('alcohol_sample_starttime').getValue();
	                var endDate = Ext.getCmp('alcohol_sample_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('alcohol_sample_starttime').setValue(endDate);
	                }
				}
			}
		});
		var sample_time_end = new Ext.form.DateField({
			id:'alcohol_sample_endtime',
			name : 'sample_time_start',
        	width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
//			maxValue : new Date(),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('alcohol_sample_starttime').getValue();
	                var endDate = Ext.getCmp('alcohol_sample_endtime').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('alcohol_sample_starttime').setValue(endDate);
	                }
				}
			}
		});
		me.store = Ext.create('Ext.data.Store', {
					fields : ['case_id', 'case_code', "client", "checkper","checkper_phone",
							"area_code", "event_desc", "area_name", "remark",
							"case_in_pername", "report_model",
							"report_modelname", "accept_time", "sample_id",
							"mail_address", 'state', "mail_per", "stateStr",
							"mail_phone", 'receiver', 'receiver_area',
							"attachment", "receiver_id", "case_in_per",
							"client_time","close_time", "sample_remark", "sample_result",
							"isDoubleTube","is_detection","sample_remark2","bloodnumA","bloodnumB",
					        "sample_time"],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'alcohol/register/getCaseInfo.do',
						params : {
							start : 0,
							limit : 25
						},
						reader : {
							type : 'json',
							root : 'items',
							totalProperty : 'count'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							alcoholStr = "case_code="+trim(case_code.getValue()) + 
										 "&state="+state.getValue()+
										 "&starttime="+dateformat(starttime.getValue())+
										 "&endtime="+dateformat(endtime.getValue())+
										 "&client="+trim(client.getValue())+
										 "&sample_time_start="+dateformat(sample_time_start.getValue())+
										 "&sample_time_end="+dateformat(sample_time_end.getValue()) +
										 "&checkper=" + trim(checkper.getValue())+
										 "&checkper_phone=" + trim(checkper_phone.getValue())+
										 "&receiver=" + trim(receiver.getValue()) + 
										 "&areacode="+ areacode.getValue()+
										 "&mail_address="+trim(mail_address.getValue()) +
										 "&mail_per=" + trim(mail_per.getValue()) + 
										 "&mail_phone=" + trim(mail_phone.getValue())+
										 "&isDoubleTube=" + isDoubleTube.getValue()+
										 "&case_in_pername = " + trim(case_in_pername.getValue());
							Ext.apply(me.store.proxy.extraParams, {
										endtime : dateformat(endtime.getValue()),
										starttime : dateformat(starttime.getValue()),
										case_code : trim(case_code.getValue()),
										state : state.getValue(),
										client : trim(client.getValue()),
										sample_time_start:dateformat(sample_time_start.getValue()),
										sample_time_end:dateformat(sample_time_end.getValue()),
										checkper : trim(checkper.getValue()),
										checkper_phone : trim(checkper_phone.getValue()),
										receiver : trim(receiver.getValue()),
										areacode : areacode.getValue(),
										mail_address : trim(mail_address.getValue()),
										mail_per : trim(mail_per.getValue()),
										mail_phone : trim(mail_phone.getValue()),
										isDoubleTube : isDoubleTube.getValue(),
										case_in_pername : trim(case_in_pername.getValue())
									});
						}
					}
				});

		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : me.pageSize,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
			text : '案例条形码',
			dataIndex : 'case_code',
			menuDisabled : true,
			width : 120,
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				var state = record.data["state"];
				if (state == 7) {
					return "<div style=\"text-decoration: line-through;color: red;\">"
							+ value + "</div>"
				} else {
					return value;
				}
			}
		}, {
			text : '委托人',
			dataIndex : 'client',
			width : 220,
			menuDisabled : true
		}, {
			text : '送检人',
			dataIndex : 'checkper',
			menuDisabled : true,
			width : 100
		}, {
			text : '送检人电话',
			dataIndex : 'checkper_phone',
			menuDisabled : true,
			width : 120
		}, {
			text : '案例归属地',
			dataIndex : 'receiver_area',
			menuDisabled : true,
			width : 200
		}, {
			text : '归属人',
			dataIndex : 'receiver',
			menuDisabled : true,
			width : 100
		}, {
			text : '所属地区',
			dataIndex : 'area_name',
			menuDisabled : true,
			width : 200
		},{
			text : '状态',
			dataIndex : 'stateStr',
			menuDisabled : true,
			width : 100,
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				var state = record.data["state"];
				if (state == 1) {
					return "<span style='color:red'>" + value + "</span>";
				}
				return value;
			}
		}, {
			hidden : true,
			text : '事件描述',
			dataIndex : 'event_desc',
			menuDisabled : true
				// width : 500
		},{
			text : '邮寄地址',
			dataIndex : 'mail_address',
			menuDisabled : true,
			width : 100
		}, {
			text : '邮件接收人',
			dataIndex : 'mail_per',
			menuDisabled : true,
			width : 100
		}, {
			text : '联系电话',
			dataIndex : 'mail_phone',
			menuDisabled : true,
			width : 100
		}, {
			text : '模板名称',
			dataIndex : 'report_modelname',
			menuDisabled : true,
			width : 120
		}, {
			text : '受理日期',
			dataIndex : 'accept_time',
			menuDisabled : true,
			width : 100
		}, {
			text : '登记日期',
			dataIndex : 'sample_time',
			menuDisabled : true,
			width : 100
		}, {
			text : '登记人',
			dataIndex : 'case_in_pername',
			menuDisabled : true,
			width : 120
		},
//		{
//			text : '样本描述',
//			dataIndex : 'sample_remark',
//			menuDisabled : true,
//			width : 500
//		}, {
//			text : '检验描述',
//			dataIndex : 'sample_remark2',
//			menuDisabled : true,
//			width : 500
//		}, {
//			text : '检验结果',
//			dataIndex : 'sample_result',
//			menuDisabled : true,
//			width : 120
//		}, 
			{
			text : 'AB管',
			dataIndex : 'isDoubleTube',
			menuDisabled : true,
			width : 80,
			renderer : function(value) {
				if (value == 0) {
					return "<span style='color:red'>否</span>";
				}
				return "<span style='color:blue'>是</span>";
			}
		},
//		 {
//			text : '是否检出',
//			dataIndex : 'is_detection',
//			menuDisabled : true,
//			width : 120,
//			renderer : function(value) {
//				if (value == 0) {
//					return "<span style='color:red'>否</span>";
//				}
//				return "<span style='color:blue'>是</span>";
//			}
//		}, 
			{
			text : '备注',
			dataIndex : 'remark',
			menuDisabled : true,
			width : 300
		}];
		me.startdate = new Date(new Date().getFullYear()+'/01/01')
		me.dockedItems = [{
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [case_code, client,checkper,checkper_phone, state]
		},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[starttime, endtime,mail_address,mail_per,mail_phone]
		},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[sample_time_start,sample_time_end,areacode,isDoubleTube,case_in_pername]
		},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '1px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[receiver,{
	            	text:'查询',
	            	iconCls:'Find',
	            	handler:me.onSearch
	            }]
		
		},{
			xtype : 'toolbar',
			dock : 'top',
			items : [{
						text : '查看样本信息',
						iconCls : 'Find',
						handler : me.onFind
					},
					{
			 			text:'导出',
			 			iconCls:'Application',
			 			handler:me.alcoholExport
			 		}
					]
		}];

		me.callParent(arguments);
	},
	alcoholExport:function(){
		window.location.href = "alcohol/register/caseInfoExport.do?"+alcoholStr ;
	},
	onFind : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		if (selections.length < 1 || selections.length > 1) {
			Ext.Msg.alert("提示", "请选择需要一条查看的记录!");
			return;
		};
		Ext.Ajax.request({
			url : "alcohol/register/getSampleInfo.do",
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : {
				sample_id : selections[0].get("sample_id")
			},
			success : function(response, options) {
				response = Ext.JSON.decode(response.responseText);
				var sex = "男";
				if (response.sample_sex == 0) {
					sex = "女"
				}
				var win = Ext.create("Ext.window.Window", {
							title : "样本信息（案例条形码："
									+ selections[0].get("case_code") + "）",
							width : 600,
							iconCls : 'Find',
							height : 270,
							bodyStyle : "background-color:white;padding-left:10px;padding-top:10px;",
							items : [{
										xtype : "container",
										layout : "column",
										height : 25,
										items : [{
													xtype : "displayfield",
													columnWidth : .8,
													labelAlign : 'left',
													labelWidth : 60,
													fieldLabel : "委托人",
													value : selections[0].get("client")
												}]
									},{
										xtype : "container",
										layout : "column",
										height : 25,
										items : [{
													xtype : "displayfield",
													columnWidth : .5,
													labelAlign : 'left',
													labelWidth : 60,
													fieldLabel : "送检人",
													value : selections[0].get("checkper")
												},{
													xtype : "displayfield",
													columnWidth : .5,
													labelAlign : 'left',
													labelWidth : 60,
													fieldLabel : "联系电话",
													value : selections[0].get("checkper_phone")
												}]
									},{
										xtype : "container",
										layout : "column",
										height : 25,
										items : [{
													xtype : "displayfield",
													columnWidth : 1,
													labelAlign : 'left',
													labelWidth : 60,
													fieldLabel : "邮寄地址",
													value : selections[0].get("mail_address")
												}]
									}, {
										xtype : "container",
										layout : "column",
										height : 25,
										items : [{
													xtype : "displayfield",
													columnWidth : .5,
													labelAlign : 'left',
													labelWidth : 60,
													fieldLabel : "收件人",
													value : selections[0].get("mail_per")
												},{
													xtype : "displayfield",
													columnWidth : .5,
													labelAlign : 'left',
													labelWidth : 60,
													fieldLabel : "电话",
													value : selections[0].get("mail_phone")
												}]
									}, {
										xtype : "container",
										layout : "column",
										height : 25,
										items : [{
											xtype : "displayfield",
											columnWidth : .5,
											labelAlign : 'left',
											labelWidth : 60,
											fieldLabel : "被检测人",
											value : response.sample_name
										},{
													xtype : "displayfield",
													columnWidth : .5,
													labelAlign : 'left',
													labelWidth : 60,
													fieldLabel : "身份证号",
													value : response.id_number
										}]
									}, {
										xtype : "container",
										layout : "column",
										height : 25,
										items : [
										         {
													xtype : "displayfield",
													columnWidth : .5,
													labelAlign : 'left',
													labelWidth : 60,
													fieldLabel : "是否检出",
													value : selections[0].get("is_detection")==0?"否":"是"
												}
												]
									}, {
										xtype : "container",
										layout : "column",
										height : 25,
										items : [
										         {
													xtype : "displayfield",
													columnWidth : 1,
													labelAlign : 'left',
													labelWidth : 60,
													fieldLabel : "样本描述",
													value : selections[0].get("sample_remark")
												}
												]
									},{
										xtype : "container",
										layout : "column",
										height : 25,
										items : [
										         {
													xtype : "displayfield",
													columnWidth : 1,
													labelAlign : 'left',
													labelWidth : 60,
													fieldLabel : "检测结果",
													value : selections[0].get("sample_result")
												}
												]
									}]
						});
				win.show();
			},
			failure : function() {
				Ext.Msg.alert("提示", "网络故障<br>请联系管理员!");
			}
		});
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage = 1;
		me.getStore().load();
	},
	onExport : function() {
		var me = this.up("gridpanel");
		var params = me.store.proxy.extraParams;
		window.location.href = "alcohol/register/exportCaseInfo.do?case_code=" + params["case_code"] 
								+ "&client=" + params[client]
								+ "&state=" + params["state"]
								+ "&starttime=" + params["starttime"] 
								+ "&endtime=" + params["endtime"]
								+ "&checkper=" + params["checkper"]
								+ "&checkper_phone=" + params["checkper_phone"]
		 						+ "&receiver=" + params["receiver"]
								+ "&areacode=" + params["areacode"]
								+ "&mail_address=" + params["mail_address"]
								+ "&mail_per=" + params["mail_per"]
								+ "&mail_phone=" + params["mail_phone"]
								+ "&client_starttime" + params["client_starttime"]
								+ "&client_endtime" + params["client_endtime"]
								+ "&isDoubleTube" + params["isDoubleTube"]
								+ "&case_in_pername" + params["case_in_pername"];
	},
	listeners : {
		'afterrender' : function() {
			this.store.load();
		}
	}
});
