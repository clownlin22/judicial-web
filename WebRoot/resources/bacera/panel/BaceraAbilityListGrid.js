var ability="";
onDownload = function(value) {
	window.location.href = "bacera/ability/download.do?attachment_id=" + value;
}
case_canel = function(me) {
	me.up("window").close();
}
Ext.define('Rds.bacera.panel.BaceraAbilityListGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
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
						data : [['全部',''],['是','2' ],['否','1' ] ]
					}),
			value : '',
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
			        'payment','paid','account_type','finance_remark','discountPrice','delete'],
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
							+"&isdelete="+isdelete.getValue();
            		Ext.apply(me.store.proxy.params, {
            			ability_num:trim(ability_num.getValue()),
            				ability_name:trim(ability_name.getValue()),
		        			ownperson:trim(ownperson.getValue()),
		        			identify_starttime:dateformat(identify_starttime.getValue()),
		        			identify_endtime:dateformat(identify_endtime.getValue()),
		        			participate_num:participate_num.getValue(),
		    				ability_company:ability_company.getValue(),
		    				isdelete:isdelete.getValue()
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
							if (isnull == 2) {
								return "<div style=\"text-decoration: line-through;color: red;\">"
										+ value + "</div>"
							} else {
								return value;
							}

						}
		              },
	                  { text: '项目名称',	dataIndex: 'ability_name', menuDisabled:true,width : 80},
	                  { text: '参加编号',	dataIndex: 'participate_num', menuDisabled:true, width : 80},
	                  { text: '所属单位',	dataIndex: 'ability_company', menuDisabled:true, width : 80},
	                  { text: '样本类型',	dataIndex: 'sample_type', menuDisabled:true, width : 200},
	                  { text: '归属人',	dataIndex: 'ownperson', menuDisabled:true, width : 100},
	                  { text: '鉴定开始日期',	dataIndex: 'identify_starttime', menuDisabled:true, width : 150},
	                  { text: '鉴定结束',	dataIndex: 'identify_endtime', menuDisabled:true, width : 150},
	                  { text: '发件信息',	dataIndex: 'report_sendinfo', menuDisabled:true, width : 100},
	                  { text: '应收款项',	dataIndex: 'receivables', menuDisabled:true, width : 200},
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
	 			handler:me.abilityExport,
	 			hidden:true
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
		if(selections[0].get("delete") == '2')
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
							url : 'bacera/ability/uploadAbilityAttachment.do',
							method : 'post',
							waitMsg : '正在上传您的文件...',
							success : function(form, action) {
								response = Ext.JSON
										.decode(action.response.responseText);
								console.log(response);
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
//									var me = this.up('form');
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
														console.log(me);
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
						name : 'ability_id',
						value : selections[0].get("ability_id")
					},
					{
						xtype : 'hiddenfield',
						name : 'ability_num',
						value : selections[0].get("ability_num")
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
						url : "bacera/ability/delAttachment.do",
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
					fields : [ 'attachment_id', 'attachment_path', 'attachment_type','ability_id','create_date' ],// 定义字段
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'bacera/ability/queryAttacmByAbility.do',
						params : {
							'ability_id' : selections[0].get("ability_id")
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
				},  {
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
	abilityExport:function(){
		window.location.href = "bacera/ability/exportAbility.do?"+ability ;
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
	onCheck:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要查看的记录!");
			return;
		};
		var form = Ext.create("Rds.bacera.form.BaceraAbilityCheckForm",{
			region:"center",
			modal:true,
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'能力验证—查看',
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
		if(selections[0].get("delete") == '2')
		{
			Ext.Msg.alert("提示", "该记录已作废!");
			return;
		}
		var values = {
				ability_id:selections[0].get("ability_id")
		};
		Ext.MessageBox.confirm("提示", "确认作废选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在保存','请稍后...');
	    		Ext.Ajax.request({  
	    			url:"bacera/ability/delete.do", 
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