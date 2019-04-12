var documentAppCoo="";
var rowEditing = Ext.create('Ext.grid.plugin.RowEditing',{
           pluginId:'rowEditing',
           saveBtnText: '保存', 
           cancelBtnText: "取消", 
           autoCancel: false, 
           clicksToEdit:1   //双击进行修改  1-单击   2-双击    0-可取消双击/单击事件
});
Ext.define('Rds.bacera.panel.BaceraDocumentAppCooExpressListGrid', {
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
		var expressnum = Ext.create('Ext.form.field.Text',{
			name:'expressnum',
			labelWidth:80,
			width:'20%',
			fieldLabel:'快递单号'
		});
		var express_starttime = new Ext.form.DateField({
			name : 'express_starttime',
			width:'20%',
			fieldLabel : '快递日期从',
			labelWidth : 80,
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
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','client','phone','client_date','accept_date','appraisal_pro','basic_case','appraisal_end_date','return_type',
			        'invoice_exp','remark','create_pername','cancelif','ownperson','ownpersonname','areaname','agentname','case_close','receivables','expresstime','confirm_flag',
			        'expresstype','expressnum','recive','expresstime','expressremark','finance_remark','paragraphtime'],
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
							+"&cancelif=2"
							+"&case_close="+case_close.getValue()
							+"&express_starttime="
							+ dateformat(express_starttime.getValue())
							+"&express_endtime="
							+ dateformat(express_endtime.getValue())
							+"&reportif="+reportif.getValue()
							+"&expressnum="+expressnum.getValue().trim();
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
            				cancelif:2,
            				case_close:case_close.getValue(),
            				express_starttime:express_starttime.getValue(),
            				express_endtime:express_endtime.getValue(),
            				reportif:reportif.getValue(),
            				expressnum:expressnum.getValue().trim()
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
       var pagingToolbar=Ext.getCmp('pagingbarDocumentAppCooExpress');
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
			id:'pagingbarDocumentAppCooExpress',
			store: me.store,
			pageSize:me.pageSize,
			displayInfo: true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录",
	   	 	items: ['-', '每页显示',combo,'条']
		});
//		me.bbar = Ext.create('Ext.PagingToolbar', {
//            store: me.store,
//            pageSize:me.pageSize,
//            displayInfo: true,
//            displayMsg : "第 {0} - {1} 条  共 {2} 条",
//	   	 	emptyMsg : "没有符合条件的记录"
//        });
		me.columns = [
		              { text: '编号',	dataIndex: 'num', menuDisabled:true, width : 100
		              },
		              { text: '快递单号',	dataIndex: 'expressnum', menuDisabled:true, width : 100,
		            	  editor:new Ext.form.NumberField()
		              },
		              { text: '快递类型',	dataIndex: 'expresstype', menuDisabled:true, width : 100,
		            	  editor:new Ext.form.ComboBox({
		          				autoSelect : true,
		          				editable:true,
		          		        name:'expresstype',
		          		        triggerAction: 'all',
		          		        queryMode: 'local', 
		          		        emptyText : "请选择",
		          		        selectOnTab: true,
		          		        store: mailStore,
		          		        maxLength: 50,
		          		        fieldStyle: me.fieldStyle,
		          		        displayField:'value',
		          		        valueField:'value',
		          		        listClass: 'x-combo-list-small'
		          			})
		              },
	                  { text: '快递日期',	dataIndex: 'expresstime', menuDisabled:true, width : 110,
	                	  renderer:Ext.util.Format.dateRenderer('Y-m-d'),
	                      editor:new Ext.form.DateField({
	                           format: 'Y-m-d'
	                         })
		              },
	                  { text: '收件人',	dataIndex: 'recive', menuDisabled:true, width : 120,
		            	  editor:'textfield'
		              },
		              { text: '快递备注',	dataIndex: 'expressremark', menuDisabled:true, width : 150,
	                	  editor:'textfield'
	                  },
	                  { text: '到款时间',	dataIndex: 'paragraphtime', menuDisabled:true, width : 80},
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
	 		items:[expressnum,case_close,appraisal_end_starttime,appraisal_end_endtime,reportif]
	 	},{
	 		style : {
	        borderTopWidth : '0px !important',
	        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[express_starttime,express_endtime,{
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
	 			text:'导出',
	 			iconCls:'Application',
	 			handler:me.documentAppCooExport
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	documentAppCooExport:function(){
		window.location.href = "bacera/documentAppCoo/exportDocumentAppCoo.do?"+documentAppCoo ;
	},
	caseFeeAdd:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择需要补款的案例!");
			return;
		}
		if(selections[0].get("confirm_flag")!='2')
		{
			Ext.Msg.alert("提示", "该案例未确认!");
			return;
		}
		casefee_save = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["id"]=selections[0].get("id");
			values["num"]=selections[0].get("num");
			values["case_type"]="无创亲子";
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				console.log(values);
				Ext.Ajax.request({
							url : "finance/bacera/insert.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response) {
									Ext.MessageBox.alert("提示信息", "操作成功!");
									me.getStore().load();
									casefee_add.close();
								} else {
									Ext.MessageBox.alert("错误信息", "操作失败，请联系管理员！");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败,查看是否已录过补款<br>请联系管理员!");
							}
						});
			}
		}
		casefee_canel = function(me) {
			me.up("window").close();
		}
		var casefee_add = Ext.create("Ext.window.Window", {
			title : '案例补款',
			width : 300,
			height : 250,
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
					labelAlign : 'right'
				},
				border : false,
				autoHeight : true,
				buttons : [{
							text : '保存',
							iconCls : 'Disk',
							handler : casefee_save
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : casefee_canel
						}],
				items : [{
						xtype : "numberfield",
						fieldLabel : '补款价格',
						labelAlign : 'right',
						maxLength : 200,
						labelWidth : 80,
						name : 'receivables',
		    	        allowBlank:false, //不允许为空
		    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
				}]
			}]
		})
		casefee_add.show();
	},
	inserFinance:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要回款的案例!");
			return;
		};
		var ids="";
		var num="";
		var receivables="";
		for(var i = 0 ; i < selections.length ; i ++)
		{
			if(selections[i].get("confirm_flag")=='2')
			{
				Ext.Msg.alert("提示", "存在已确认案例!");
				return;
			}
			ids += selections[i].get("id")+",";
			num += selections[i].get("test_number")+",";
			receivables+=selections[i].get("price")/100+",";
		}
		ids = ids.substring(0,ids.length-1);
		num = num.substring(0,num.length-1);
		receivables = receivables.substring(0,receivables.length-1);
		casefee_save = function(mei) {
			var form = mei.up("form").getForm();
			var values = form.getValues();
			values["ids"]=ids;
			values["num"]=num;
			values["receivables"]=receivables;
			values["case_type"]="毒品检测";
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
							url : "bacera/boneAge/saveFinanceList.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response.result) {
									Ext.MessageBox.alert("提示信息", "批量操作成功!");
									me.getStore().load();
									casefee_add.close();
								} else {
									Ext.MessageBox.alert("错误信息", "批量操作失败，请联系管理员！");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
						});
			}
		}
		casefee_canel = function(me) {
			me.up("window").close();
		}
		var casefee_add = Ext.create("Ext.window.Window", {
			title : '批量回款',
			width : 400,
			height : 325,
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
					labelAlign : 'right'
				},
				border : false,
				autoHeight : true,
				buttons : [{
							text : '保存',
							iconCls : 'Disk',
							handler : casefee_save
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : casefee_canel
						}],
				items : [{
					xtype : "datefield",
					fieldLabel : '到款时间',
					labelAlign : 'right',
					format : 'Y-m-d',
					value : Ext.Date.add(
							new Date(),
							Ext.Date.DAY),
					maxValue:Ext.Date.add(
							new Date(),
							Ext.Date.DAY),
					labelWidth : 80,
					name : 'paragraphtime',
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This field is required!
				},new Ext.form.ComboBox({
          				autoSelect : true,
          				editable:true,
          		        name:'account_type',
          		        triggerAction: 'all',
    					fieldLabel : '账户类型',
          		        queryMode: 'local', 
          		        emptyText : "请选择",
          		        selectOnTab: true,
          		        store: acounttype,
    					labelWidth : 80,
          		        maxLength: 50,
    					labelAlign : 'right',
          		        fieldStyle: me.fieldStyle,
          		        displayField:'remark',
          		        valueField:'remark',
        				allowBlank : false,// 不允许为空
        				blankText : "不能为空",// 错误提示信息，默认为This field is required!
          			}),new Ext.form.ComboBox({
        				autoSelect : true,
        				editable:true,
    					fieldLabel : '汇款帐户',
        		        name:'remittanceName',
        		        triggerAction: 'all',
    					labelAlign : 'right',
        		        queryMode: 'local', 
    					labelWidth : 80,
        		        emptyText : "请选择",
        		        selectOnTab: true,
        		        store: remittanceAccountStore,
        		        maxLength: 50,
        		        fieldStyle: me.fieldStyle,
        		        displayField:'accountName',
        		        valueField:'accountName',
    					allowBlank : false,// 不允许为空
    					blankText : "不能为空",// 错误提示信息，默认为This field is required!
        			}),
        			{
    					xtype : "datefield",
    					fieldLabel : '汇款时间',
    					labelAlign : 'right',
    					format : 'Y-m-d',
    					value : Ext.Date.add(
    							new Date(),
    							Ext.Date.DAY),
    					maxValue:Ext.Date.add(
    							new Date(),
    							Ext.Date.DAY),
    					labelWidth : 80,
    					name : 'remittanceDate',
    					allowBlank : false,// 不允许为空
    					blankText : "不能为空",// 错误提示信息，默认为This field is required!
    				},{
						xtype : "textarea",
						fieldLabel : '财务备注',
						labelAlign : 'right',
						maxLength : 200,
						labelWidth : 80,
						name : 'remarks'
				}]
			}]
		})
		casefee_add.show();
	},
	confirmFinanceList:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要确认的案例!");
			return;
		};
		var ids='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			ids += selections[i].get("id")+",";
			if(selections[i].get("confirm_flag")=='2'){
				Ext.Msg.alert("提示", "存在已确认案例!");
				return;
			}
		}
		ids = ids.substring(0,ids.length-1);
		Ext.MessageBox.confirm("提示", "确认选中记录？", function (btn) {
	        if("yes"==btn)
	        {
	        	Ext.MessageBox.wait('正在操作','请稍后...'); 	
	    		Ext.Ajax.request({
	    			url : "bacera/boneAge/confirmFinanceList.do",
	    			method : "POST",
	    			headers : {
	    				'Content-Type' : 'application/json'
	    			},
	    			jsonData : {ids:ids},
	    			success : function(response, options) {
	    				response = Ext.JSON
	    						.decode(response.responseText);
	    				if (response.result) {
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
		}
		Ext.Msg.alert("我是案例编号", num);
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	},
	listeners : {
		'beforeedit':function(editor, e,s){
			function afterEdit(e,s){ 
               	Ext.MessageBox.wait('正在保存','请稍后...');
             	Ext.Ajax.request({  
					url:"bacera/boneAge/saveExpress.do", 
					method: "POST",
					headers: { 'Content-Type': 'application/json' },
					jsonData: {
						id:s.record.data.id,
						num:s.record.data.num,
						expressnum:s.record.data.expressnum,
						recive:s.record.data.recive,
						expresstime:Ext.util.Format.date(s.record.data.expresstime, 'Y-m-d'),
						expresstype:s.record.data.expresstype,
						expressremark:s.record.data.expressremark
					}, 
					success: function (response, options) {  
						response = Ext.JSON.decode(response.responseText); 
		                 if (response.result==false) {  
			                 Ext.MessageBox.alert("错误信息", "修改失败，请重试或联系管理员!");
		                 }else{
		                	 Ext.MessageBox.alert("提示信息", "保存成功！");
		                 }
					},  
					failure: function () {
						Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
					}
		      	});
             }
			rowEditing.on('edit',afterEdit);
		}
	}
	
});
