var documentAppCoo="";
onDownload = function(value) {
	window.location.href = "bacera/documentAppCoo/download.do?attachment_id=" + value;
}
case_canel = function(me) {
	me.up("window").close();
}
Ext.define('Rds.bacera.panel.BaceraDocumentAppCooListGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var num = Ext.create('Ext.form.field.Text',{
			name:'num',
			labelWidth:80,
			width:'20%',
			fieldLabel:'项目编号'
		});
		var client = Ext.create('Ext.form.field.Text',{
			name:'client',
			labelWidth:60,
			width:'20%',
			fieldLabel:'委托人'
		});
		var phone = Ext.create('Ext.form.field.Text',{
			name:'phone',
			labelWidth:80,
			width:'20%',
			fieldLabel:'电话号码'
		});
		var appraisal_pro = Ext.create('Ext.form.field.Text',{
			name:'appraisal_pro',
			labelWidth:80,
			width:'20%',
			fieldLabel:'鉴定项目'
		});
		var ownperson = Ext.create('Ext.form.field.Text',{
			name:'ownperson',
			labelWidth:60,
			width:'20%',
			fieldLabel:'归属人'
		});
		var create_pername = Ext.create('Ext.form.field.Text',{
			name:'ownperson',
			labelWidth:80,
			width:'20%',
			fieldLabel:'创建人'
		});
		var client_starttime = new Ext.form.DateField({
			name : 'client_starttime',
			width : '20%',
			fieldLabel : '委托时间从',
			labelWidth : 80,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var client_endtime = new Ext.form.DateField({
			name : 'client_endtime',
			width : '20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var accept_starttime = new Ext.form.DateField({
			name : 'accept_starttime',
			width : '20%',
			fieldLabel : '受理时间从',
			labelWidth : 80,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var accept_endtime = new Ext.form.DateField({
			name : 'accept_endtime',
			width : '20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var appraisal_end_starttime = new Ext.form.DateField({
			name : 'appraisal_end_starttime',
			width : '20%',
			fieldLabel : '鉴定完成日期',
			labelWidth : 80,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var appraisal_end_endtime = new Ext.form.DateField({
			name : 'appraisal_end_endtime',
			width : '20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var cancelif=new Ext.form.field.ComboBox({
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
						data : [['全部',''],['是','1' ],['否','2' ] ]
					}),
			value : '',
			mode : 'local',
			name : 'cancelif',
		});
		var case_close=new Ext.form.field.ComboBox({
			fieldLabel : '是否结案',
			width : '20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部',''],['是','1' ],['否','2' ] ]
					}),
			value : '',
			mode : 'local',
			name : 'case_close',
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','client','phone','client_date','accept_date','appraisal_pro','basic_case','appraisal_end_date','return_type',
			        'invoice_exp','remark','create_pername','cancelif','ownperson','ownpersonname','areaname','agentname','case_close','receivables','expresstime','confirm_flag'],
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/documentAppCoo/queryallpage.do',
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
            		documentAppCoo = "num=" + num.getValue().trim() + "&client="
							+ client.getValue().trim() + "&phone="
							+ phone.getValue().trim() + "&client_starttime="
							+ dateformat(client_starttime.getValue())
							+ "&client_endtime="
							+ dateformat(client_endtime.getValue())
							+ "&appraisal_pro=" + appraisal_pro.getValue().trim()
							+ "&ownperson=" + ownperson.getValue()
							+ "&create_pername=" + create_pername.getValue().trim()
							+"&accept_starttime="
							+ dateformat(accept_starttime.getValue())
							+ "&accept_endtime="
							+ dateformat(accept_endtime.getValue())
							+"&appraisal_end_starttime="
							+ dateformat(appraisal_end_starttime.getValue())
							+ "&appraisal_end_endtime="
							+ dateformat(appraisal_end_endtime.getValue())
							+"&cancelif="+cancelif.getValue()
							+"&case_close="+case_close.getValue();
            		Ext.apply(me.store.proxy.params, {
            				num:trim(num.getValue()),
            				client:trim(client.getValue()),
            				phone:trim(phone.getValue()),
            				client_starttime:dateformat(client_starttime.getValue()),
            				client_endtime:dateformat(client_endtime.getValue()),
            				appraisal_end_starttime:dateformat(appraisal_end_starttime.getValue()),
            				appraisal_end_starttime:dateformat(appraisal_end_starttime.getValue()),
            				accept_starttime:dateformat(accept_starttime.getValue()),
            				accept_endtime:dateformat(accept_endtime.getValue()),
            				appraisal_pro:appraisal_pro.getValue(),
            				ownperson:ownperson.getValue(),
            				create_pername:create_pername.getValue(),
            				cancelif:cancelif.getValue(),
            				case_close:case_close.getValue()
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
	              { text: '编号',	dataIndex: 'num', menuDisabled:true, width : 100,
                	  renderer : function(value, cellmeta,
								record, rowIndex, columnIndex,
								store) {
							var cancelif= record.data["cancelif"];
							if (cancelif == 1) {
								return "<div style=\"text-decoration: line-through;color: red;\">"
										+ value + "</div>"
							} else {
								return value;
							}

						}
		              },
	                  { text: '是否结案',	dataIndex: 'case_close', menuDisabled:true,width : 80},
	                  { text: '委托人',	dataIndex: 'client', menuDisabled:true,width : 80},
	                  { text: '联系方式',	dataIndex: 'phone', menuDisabled:true, width : 120},
	                  { text: '委托时间',	dataIndex: 'client_date', menuDisabled:true, width : 80},
	                  { text: '受理时间',	dataIndex: 'accept_date', menuDisabled:true, width : 80},
	                  { text: '归属人',	dataIndex: 'ownpersonname', menuDisabled:true, width : 100},
	                  { text: '鉴定项目',	dataIndex: 'appraisal_pro', menuDisabled:true, width : 150},
	                  { text: '收费金额',	dataIndex: 'receivables', menuDisabled:true, width : 150},
	                  { text: '是否缴费',	dataIndex: 'confirm_flag', menuDisabled:true, width : 80,
	                	  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var confirm_flag= record.data["confirm_flag"];
								if (confirm_flag == 1) {
									return "<div style=\"color: red;\">否</div>"
								} else {
									return "是";
								}

							}
	                  },
	                  { text: '开票费用',	dataIndex: 'invoice_exp', menuDisabled:true, width : 100},
	                  { text: '鉴定完成日期',	dataIndex: 'appraisal_end_date', menuDisabled:true, width : 100},
	                  { text: '发送报告日期',	dataIndex: 'expresstime', menuDisabled:true, width : 100},
	                  { text: '创建人',	dataIndex: 'create_pername', menuDisabled:true, width : 80}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[num,client,phone,client_starttime,client_endtime]
	 	},{
	 		style : {
	        borderTopWidth : '0px !important',
	        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[appraisal_pro,ownperson,create_pername,accept_starttime,accept_endtime]
	 	},{
	 		style : {
	        borderTopWidth : '0px !important',
	        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[cancelif,case_close,appraisal_end_starttime,appraisal_end_endtime,{
            	text:'查询',
            	iconCls:'Find',
            	handler:me.onSearch
            }]
	 	
	 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[{
	 			text:'添加',
	 			iconCls:'Add',
	 			handler:me.onInsert
	 		},{
	 			text:'修改',
	 			iconCls:'Pageedit',
	 			handler:me.onUpdate
	 		},{
	 			text:'查看',
	 			iconCls:'Find',
	 			handler:me.onCheck
	 		},{
	 			text:'作废',
	 			iconCls:'Cancel',
	 			handler:me.onCancel
	 		},{
	 			text:'附件管理',
	 			iconCls:'Cog',
	 			handler:me.attachmentManage
	 		},{
	 			text:'导出',
	 			iconCls:'Application',
	 			handler:me.documentAppCooExport
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
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
					Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
					return;
				}
				form.submit({
							url : 'bacera/documentAppCoo/uploadDocumentAppCooAttachment.do',
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
									Ext.MessageBox.alert("提示信息", "上传失败，请联系管理员!");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "上传失败，请联系管理员!");
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
								columnWidth : .60,
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
									data : [['报告', 1], ['原始记录', 2],
											['作业指南', 3], ['其他', 4]]
								}),
								mode : 'local',
								name : 'filetype',
								columnWidth : .4,
								style:'margin-left:10px;'
							})
							]
						},{
							xtype : 'panel',
							layout : 'absolute',
							border : false,
							items : [{
								xtype : 'button',
								text : '增加文件',
								width : 100,
								style:'margin-bottom:10px',
								x : 0,
								handler : function() {
									var me = Ext.getCmp("testFieldset");
									me.add({
										xtype : 'form',
										style : 'margin-top:5px;',
										layout : 'column',
										border : false,
										items : [{
													xtype : 'filefield',
													name : 'files',
													columnWidth : .60,
													fieldLabel : '文件<span style="color:red">*</span>',
													labelWidth : 40,
													msgTarget : 'side',
													allowBlank : false,
													anchor : '100%',
													style:'margin-bottom:10px',
													buttonText : '选择文件'
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
																data : [['报告', 1], ['原始记录', 2],
																		['作业指南', 3], ['其他', 4]]
															}),
													mode : 'local',
													name : 'filetype',
													columnWidth : .4,
													style:'margin-left:10px;'
												}), {
													xtype : 'button',
													style : 'margin-left:15px;',
													text : '删除',
													handler : function() {
														var me = this.up("form");
														me.disable(true);
														me.up("fieldset").remove(me);
													}
												}]
									});
								}
							}]

						}
						]
					
			     }, {
						xtype : 'hiddenfield',
						name : 'id',
						value : selections[0].get("id")
					},
					{
						xtype : 'hiddenfield',
						name : 'num',
						value : selections[0].get("num")
					}]
				}]
			})
			attachment_insert.show();
			
			
		}
		attachment_onDel = function(me) {
			var grid = me.up("gridpanel");
			var selections = grid.getView().getSelectionModel().getSelection();

			if (selections.length < 1) {
				Ext.Msg.alert("提示", "请选择需要删除的记录!");
				return;
			}
			Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
		        if("yes"==btn)
		        {
		        	Ext.MessageBox.wait('正在操作','请稍后...');
		        	Ext.Ajax.request({
						url : "bacera/documentAppCoo/delAttachment.do",
						method : "POST",
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : {
							"attachment_id" : selections[0].get("attachment_id"),
						},
						success : function(response, options) {
							response = Ext.JSON.decode(response.responseText);
							if (response.result == true) {
								Ext.MessageBox.alert("提示信息", "附件删除成功!");
								var grid = me.up("gridpanel");
								grid.getStore().load();
							} else {
								Ext.MessageBox.alert("错误信息", response.message);
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
						}
					});
		        }
			})
		}
		var win = Ext.create("Ext.window.Window", {
			title : "附件管理",
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
					fields : [ 'attachment_id', 'attachment_path', 'attachment_type','appraisal_cpp_id','create_date','create_per' ],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'bacera/documentAppCoo/queryAttacmByDocumentAppCoo.do',
						params : {
							'appraisal_cpp_id' : selections[0].get("id")
						},
						reader : {
							type : 'json',
							root : 'items',
							totalProperty : 'count'
						}
					},
					autoLoad : true
				},
				columns : [// 配置表格列
				{
					header : "附件类型",
					dataIndex : 'attachment_type',
					flex : 0.8,
					menuDisabled : true,
					 renderer : function(value) {
							switch (value) {
							case 1:
								return "报告";
								break;
							case 2:
								return "原始记录";
								break;
							case 3:
								return "作业指南";
								break;
							default:
								return "其他";
							}
						}
				}, {
					header : "附件路径",
					dataIndex : 'attachment_path',
					flex : 1.5,
					menuDisabled : true
				},{
					header : "上传时间",
					dataIndex : 'create_date',
					flex : 1,
					menuDisabled : true
				},{
					header : "上传人员",
					dataIndex : 'create_per',
					flex : 1,
					menuDisabled : true
				},   {
					header : "操作",
					dataIndex : 'attachment_path',
					flex : 0.5,
					menuDisabled : true,
					renderer : function(value, cellmeta, record,
							rowIndex, columnIndex, store) {
						var attachment_id = record.data["attachment_id"];
						return "<a href='#' onclick=\"onDownload('" + attachment_id
						+ "')\"  >下载";
					
					}
				} ]
			}) ]
		});
		win.show();
		
	},
	documentAppCooExport:function(){
		window.location.href = "bacera/documentAppCoo/exportDocumentAppCoo.do?"+documentAppCoo ;
	},
	//新增
	onInsert:function(){
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.bacera.form.BaceraDocumentAppCooListForm",{
			region:"center",
			autoScroll : true,
			grid:me
		});
		confirm_flagTemp = "";
		var win = Ext.create("Ext.window.Window",{
			title:'文书鉴定合作—新增',
			width:800,
			iconCls:'Add',
			height:600,
			modal:true,
			layout:'border',
			maximizable :true,
			items:[form]
		});
		win.show();
	},
	onCheck:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		};
		ownpersonTemp = selections[0].get("ownperson");
		var form = Ext.create("Rds.bacera.form.BaceraDocumentAppCooCheckForm",{
			region:"center",
			modal:true,
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'文书鉴定合作—查看',
			width:800,
			iconCls:'Pageedit',
			height:600,
			modal:true,
			layout:'border',	
			items:[form],
			maximizable :true,
		});
		win.show();
		form.loadRecord(selections[0]);
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
		confirm_flagTemp = selections[0].get("confirm_flag");
		ownpersonTemp = selections[0].get("ownperson");
		var form = Ext.create("Rds.bacera.form.BaceraDocumentAppCooUpdateForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'文书鉴定合作—修改',
			width:800,
			iconCls:'Pageedit',
			height:600,
			layout:'border',	
			modal:true,
			items:[form],
			maximizable :true,
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
				id:selections[0].get("id")
		};
		Ext.MessageBox.confirm("提示", "确认作废选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在保存','请稍后...');
	    		Ext.Ajax.request({  
	    			url:"bacera/documentAppCoo/delete.do", 
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