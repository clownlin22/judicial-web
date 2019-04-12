Ext
		.define(
				'Rds.mail.form.MailCaseUpdateForm',
				{
					extend : 'Ext.form.Panel',
					style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					bodyPadding : 10,
					defaults : {
						anchor : '100%',
						labelWidth : 80,
						labelSeparator : "：",
						labelAlign : 'right'
					},
					border : false,
					autoHeight : true,
					initComponent : function() {
						var me = this;
						var mailStore = Ext.create('Ext.data.Store', {
							model : 'model',
							proxy : {
								type : 'ajax',
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
						var storeModel = Ext.create(
						        'Ext.data.Store',
						        {
						          model:'model',
						          proxy:{
						        	type: 'jsonajax',
						            actionMethods:{read:'POST'},
						            url:'mail/dicvalues/getMailTypes.do',
						            reader:{
						              type:'json',
						              root:'data'
						            }
						          },
						          autoLoad:true,
						          remoteSort:true,
						          listeners : {
										'load' : function() {
											case_type.select(this.getAt(0));
										}
									}
						        }
						);
						var case_code = Ext.create('Ext.form.field.Text', {
							name : 'casecode',
							labelWidth : 60,
							width : 200,
							regexText : '请输入案例编号',
							fieldLabel : '案例编号'
						});
						var mail_code=Ext.create('Ext.form.field.Text', {
							name : 'casecode',
							labelWidth : 60,
							columnWidth : .75,
							width : 200,
							fieldLabel : '邮寄编号'
						});
						var mail_id=Ext.create('Ext.form.field.Hidden', {
							name : 'mail_id',
						});
						var addressee=Ext.create('Ext.form.field.Text', {
							fieldLabel : '收件人',
							labelAlign : 'right',
							maxLength : 18,
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field is required!
							labelWidth : 80,
							name : 'addressee'
						});
						var case_type = Ext.create('Ext.form.ComboBox', {
							fieldLabel : '案例类型',
							width : 200,
							labelWidth : 60,
							editable : false,
							triggerAction : 'all',
							valueField :"key",  
					        displayField: "value", 
							store: storeModel,
							mode : 'local',
							value:'alcohol',
							// typeAhead: true,
							name : 'case_type',
						}); 
						var mail_type=new Ext.form.field.ComboBox({
							fieldLabel : '快递类型',
							labelWidth : 80,
							editable : false,
							triggerAction : 'all',
							labelAlign : 'right',
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field is
							valueField : "key",
							displayField : "value",
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This field is
												// required!
							store : mailStore,
							mode : 'local',
							// typeAhead: true,
							name : 'mail_type'
						});
						var search=new Ext.Button({
                            text:'查询',
                            iconCls : 'Find',
                            disabled:true,
                            handler : addCaseInfo
                        })
						 Ext.define('Record', { 
							 extend: 'Ext.data.Model', 
							 fields : [ 'case_id', 'case_code',"mail_codes",
										"client","fee_remark","fee_status","fee_date","fee_type","sample_str","receiver_name","areaname","case_time"
										,"mail_state","mail_count","case_type","case_type_str"]
						});
						function addCaseInfo(){
							Ext.Ajax
							.request({
								url : "mail/getMailCase.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {
									"case_code":trim(case_code.getValue()),
									"case_type":case_type.getValue()
								},
								success : function(
										response,
										options) {
									if(response.responseText==""){
										Ext.Msg
										.alert(
												"提示",
												"该类型下未找到此案例！");
									}else{
										response = Ext.JSON
										.decode(response.responseText);
										var data=grid.getStore().data;
										var result=true;
										for(var i=0;i<data.length;i++){
											if(data.get(i).get("case_id")==response["case_id"]&&data.get(i).get("case_type")==response["case_type"]){
												result=false;
											}
										}
										if(result){
											if(response["fee_type"]==1||response["fee_status"] ==0||response["fee_status"]==2){
												var record  = new Record(response); 
												grid.getStore().insert(grid.getStore().data.length, record );
											}else{
												Ext.MessageBox
												.confirm(
														'提示',
														'此案例未付款，确定加入此案例吗？',
														function(id) {
															if (id == 'yes') {
																var record  = new Record(response); 
																grid.getStore().insert(grid.getStore().data.length, record );  
														}
												});
											}
										}else{
											Ext.Msg
											.alert(
													"提示",
													"此案例列表已存在！");
										}
									}
								},
								failure : function() {
									Ext.Msg
											.alert(
													"提示",
													"网络故障<br>请联系管理员!");
								}
							});
						};
						loadCaseInfo=function(){
							Ext.Ajax.request({
								url : "mail/getMailInfo.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {
									mail_code:mail_code.getValue()
								},
								success : function(response, options) {
									response = Ext.JSON
											.decode(response.responseText);
									grid.getStore().removeAll();
									mail_type.setValue("");
									mail_id.setValue("");
									addressee.setValue("");
									if(response.mail_id==""){
										Ext.Msg.alert("提示", "此快递单号不存在!");
										search.setDisabled(true);
									}else{
										search.setDisabled(false);
										mail_type.setValue(response.mail_type);
										mail_id.setValue(response.mail_id);
										addressee.setValue(response.addressee);
										for(var i=0;i<response.caseInfos.length;i++){
											var record  = new Record(response.caseInfos[i]); 
											grid.getStore().insert(grid.getStore().data.length, record);
										}
									}
								},
								failure : function() {
									Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
								}
							});
						};
						delCaseInfo=function(case_id,case_type){
							var data=grid.getStore().data;
							var insertdata=[];
							for(var i=0;i<data.length;i++){
								if(data.get(i).get("case_id")!=case_id||data.get(i).get("case_type")!=case_type){
									insertdata.push(data.get(i));
								}
							}
							grid.getStore().removeAll();
							for(var i=0;i<insertdata.length;i++){
								grid.getStore().insert(i, insertdata[i]);
							}
						}
						var grid = Ext.create("Ext.grid.Panel", {
						    xtype: "grid",
						    name:"mail_case",
						    columnLines: true,
						    renderTo: Ext.getBody(),
						    plugins: [{
					            ptype: 'rowexpander',
					            rowBodyTpl : [
					                '<p><b>样本信息:</b> {sample_str}</p>',
					                '<p><b>录入时间:</b> {case_time} &nbsp;<b>付款时间:</b> {fee_date}</p>',
					                '<p><b>快递单号:</b> {mail_codes}</p>'
					            ]
						    }],
						    columns: [
									{
										text : '案例条形码',
										dataIndex : 'case_code',
										menuDisabled : true,
										width : 100
									},
									{
										text : '委托人',
										dataIndex : 'client',
										menuDisabled : true,
										width : 100
									},
									{
										text : '归属人',
										dataIndex : 'receiver_name',
										menuDisabled : true,
										width : 100
									},
									{
										text : '案例归属地',
										dataIndex : 'areaname',
										menuDisabled : true,
										width : 120
									},
									{
										text : '付款信息',
										dataIndex : 'fee_status',
										menuDisabled : true,
										width : 100,
										renderer : function(value, cellmeta, record, rowIndex, columnIndex,
												store) {
											var fee_type = record.data["fee_type"];
											var type_remark="正常";
											if(fee_type==1){
												type_remark="先出报告后付款";
											}
											if(fee_type==2){
												type_remark="免单";
											}
											if (value ==0||value==2) {
												return "<span style='color:green'>已付款("+type_remark+")</span>";
											} else {
												return "<span style='color:red'>未付款("+type_remark+")</span>";
											}
										}
									},
									{
										text : '案例类型',
										dataIndex : 'case_type_str',
										menuDisabled : true,
										width : 100
									},
									{
										text : '操作',
										dataIndex : 'case_id',
										menuDisabled : true,
										width : 100,
										renderer : function(value, cellmeta, record, rowIndex, columnIndex,
												store) {
											var case_type = record.data["case_type"];
											return "<a href=\"javascript:delCaseInfo('"+value+"','"+case_type+"')\">删除</a>"
										}
									},
						    ],
						    tbar :[case_type,case_code,search]
						});
						me.items = [{
										border : false,
										xtype : 'fieldcontainer',
										layout : "column",
										items : [mail_id,mail_code, new Ext.Button({
		                                    text:'加载已有快递',
		                                    style:'margin-left:10px;',
		                                    handler : loadCaseInfo
		                                })]
									  },	
									{
										xtype:'fieldset', 
										autoHeight:true,  
								        title:'邮寄信息',  
									    items:[{
											border : false,
											xtype : 'fieldcontainer',
											layout : "column",
											items : [
													mail_type,addressee]
										}]
									},
									grid
								];

						me.buttons = [ {
							text : '修改',
							iconCls : 'Disk',
							handler : me.onSave
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : me.onCancel
						} ]
						me.callParent(arguments);
					},
					onSave : function() {
						var me = this.up("form");
						var form = me.getForm();
						var values = form.getValues();
						if (form.isValid()) {
							var data=me.down("gridpanel[name=mail_case]").getStore().data;
							if(data.length>0){
								var case_str="";
								for(var i=0;i<data.length;i++){
									case_str+=data.get(i).get("case_id")+","+data.get(i).get("case_type")+";";
								}
								case_str=case_str.substring(0, case_str.length-1);
								values["case_str"]=case_str;
								Ext.Ajax.request({
									url : "mail/updateMailInfo.do",
									method : "POST",
									headers : {
										'Content-Type' : 'application/json'
									},
									jsonData : values,
									success : function(response, options) {
										response = Ext.JSON
												.decode(response.responseText);
										if (response == true) {
											Ext.MessageBox.alert("提示信息",
													"更改邮寄成功");
											me.grid.getStore().load();
											me.up("window").close();
										} else {
											Ext.MessageBox.alert("错误信息",
													"更改邮寄失败");
										}
									},
									failure : function() {
										Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
									}
								});
							}else{
								Ext.Msg
								.alert(
										"提示",
										"请选择需要邮寄的案例");
							}
						}
					},
					onCancel : function() {
						var me = this;
						me.up("window").close();
					},
					listeners : {
						'afterrender' : function() {
							var me = this;
							me.down("gridpanel[name=mail_case]").getStore().removeAll();
						}
					}
				});
