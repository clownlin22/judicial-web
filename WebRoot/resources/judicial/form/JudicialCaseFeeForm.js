Ext
		.define(
				'Rds.judicial.form.JudicialCaseFeeForm',
				{
					extend : 'Ext.form.Panel',
					style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					bodyPadding : 10,
					defaults : {
						anchor : '100%',
						labelWidth : 80,
						labelSeparator: "：",
						labelAlign: 'right'
					},
					border : false,
					autoHeight : true,
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
						var feeid = Ext.create('Ext.form.Hidden', {
							name : 'fee_id',
						});
						var case_code=Ext.create("Ext.form.DisplayField",{
							fieldLabel : '案例编号',
							labelWidth : 80,
							columnWidth : .45,
							name:"case_code",
						});
						var client=Ext.create("Ext.form.DisplayField",{
							fieldLabel : '委托人',
							labelWidth : 80,
							columnWidth : .45,
							name:"client",
						});
						var receive_name=Ext.create("Ext.form.DisplayField",{
							fieldLabel : '归属人',
							labelWidth : 80,
							columnWidth : .45,
							name:"receive_name",
						});
						var areaname=Ext.create("Ext.form.DisplayField",{
							fieldLabel : '地区',
							labelWidth : 80,
							columnWidth : .45,
							name:"areaname",
						});
						var sample_str=Ext.create("Ext.form.DisplayField",{
							fieldLabel : '样本信息',
							labelWidth : 80,
							columnWidth : .45,
							name:"sample_str",
						});
						var date=Ext.create("Ext.form.DisplayField",{
							fieldLabel : '登记日期',
							labelWidth : 80,
							columnWidth : .45,
							name:"date",
						});
						var standSum = Ext.create('Ext.form.field.Text', {
							name : 'stand_sum',
							fieldLabel : '标准金额',
							allowBlank  : false,//不允许为空  
							blankText   : "请配置归属人收费标准",
							readOnly : true,
							labelWidth : 80,
						});
						var real_sum=Ext.create('Ext.form.field.Number',{
						    fieldLabel: "实收金额<span style='color:red'>*</span>",
						    name: 'real_sum',
						    labelWidth : 80,
						    value: 0,
						    maxValue: 999999,
						    nanText:"请输入有效数字", //无效数字提示
						    allowNegative:false ,      //不允许输入负数
						    minValue: 0    ,
						    allowBlank:false,
						});
						var return_sum=Ext.create('Ext.form.field.Number',{
						    fieldLabel: "回款金额<span style='color:red'>*</span>",
						    name: 'return_sum',
						    labelWidth : 80,
						    nanText:"请输入有效数字", //无效数字提示
						    allowNegative:false  ,     //不允许输入负数
						    value: 0,
						    maxValue: 999999,
						    minValue: 0    ,
						    allowBlank:false                         
						});
						var fee_type = Ext.create('Ext.form.ComboBox', {
							fieldLabel: "付款类型<span style='color:red'>*</span>",
		                    labelWidth : 80,
		                    editable:false,
		                    triggerAction: 'all',
		                    displayField: 'Name',
		                    value:0,
		                    valueField: 'Code',
		                    store: new Ext.data.ArrayStore({
		                        fields: ['Name', 'Code'],
		                        data: [['正常', 0], ['先出报告后付款', 1]]
		                    })          ,
		                    mode: 'local',
		                    //typeAhead: true,
		                    name: 'fee_type'
						});
						var remark=Ext.create('Ext.form.field.TextArea',{
						    name:"remark",
						    grow: true,
						    fieldLabel: '备注',
						    labelWidth : 80,
						    anchor    : '100%'
						});
						var paragraphtime = new Ext.form.DateField(
								{
									name : 'paragraphtime',
									fieldLabel : '到款日期',
									labelWidth : 80,
									labelAlign : 'left',
									columnWidth : .4,
									afterLabelTextTpl : me.required,
									emptyText : '请选择日期',
									format : 'Y-m-d',
//									allowBlank:false, //不允许为空
//									blankText:"不能为空", //错误提示信息，默认为This field is required! 
								});
						var account = new Ext.form.ComboBox({
							autoSelect : true,
							fieldLabel:'账户类型',
					        name:'account',
					        triggerAction: 'all',
					        editable:true,
					        queryMode: 'local', 
							labelWidth : 80,
							labelAlign : 'right',
					        emptyText : "请选择",
					        columnWidth : .6,
					        selectOnTab: true,
					        maxLength: 50,
					        store: acounttype,
					        fieldStyle: me.fieldStyle,
					        displayField:'remark',
					        valueField:'remark',
					        listClass: 'x-combo-list-small',
					        style:'margin-left:2px'
						});
						me.items = [{
			    			xtype : 'fieldset',
							title : '信息&nbsp;',
							style:'margin-top:10px;',
							layout : 'anchor',
							defaults : {
								anchor : '100%'
							},
							items : [{
						    			border : false,
										xtype : 'fieldcontainer',
										layout : "column",
										style:'margin-top:10px;',
										items : [case_code,client]
							         },{
							    			border : false,
											xtype : 'fieldcontainer',
											layout : "column",
											style:'margin-top:10px;',
											items : [receive_name,date]
								    },areaname,sample_str
							]},
						feeid,standSum,real_sum,return_sum,fee_type,{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [paragraphtime,account
									]
						},remark];

						me.buttons = [ {
							text : '保存',
							iconCls : 'Disk',
							handler : me.onSave
						}, {
							text : '保存并确认',
							iconCls : 'Pageadd',
							handler : me.onConfirm
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : me.onCancel
						} ]
						me.callParent(arguments);
					},
					onConfirm:function(){
						var me = this.up("form");
						var form = me.getForm();
						var values = form.getValues();
						values["status"] = 0;
						Ext.MessageBox
						.confirm(
								'提示',
								'确定保存并确认此收费吗',
								function(id) {
									if (id == 'yes') {
										Ext.Ajax.request({  
											url:"judicial/casefee/saveCaseFee.do", 
											method: "POST",
											headers: { 'Content-Type': 'application/json' },
											jsonData: values, 
											success: function (response, options) {  
												response = Ext.JSON.decode(response.responseText); 
								                 if (response==true) {  
								                 	Ext.MessageBox.alert("提示信息", "确认收费成功");
								                 	me.grid.getStore().load();
								                 	me.up("window").close();
								                 }else { 
								                 	Ext.MessageBox.alert("错误信息", "确认收费失败");
								                 } 
											},  
											failure: function () {
												Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
											}
								      	});
									}
								});	
					},
					onSave : function() {
						var me = this.up("form");
						var form = me.getForm();
						var values = form.getValues();
						Ext.Ajax.request({  
							url:"judicial/casefee/saveCaseFee.do", 
							method: "POST",
							headers: { 'Content-Type': 'application/json' },
							jsonData: values, 
							success: function (response, options) {  
								response = Ext.JSON.decode(response.responseText); 
				                 if (response==true) {  
				                 	Ext.MessageBox.alert("提示信息", "修改收费成功");
				                 	me.grid.getStore().load();
				                 	me.up("window").close();
				                 }else { 
				                 	Ext.MessageBox.alert("错误信息", "修改收费失败");
				                 } 
							},  
							failure: function () {
								Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
							}
				      	});
					},
					onCancel : function() {
						var me = this;
						me.up("window").close();
					}
				});