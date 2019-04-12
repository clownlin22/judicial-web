var storeSampleType = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'ajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getSampleType.do',
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
var storeSampleCall = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'ajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getSampleCall.do',
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
Ext.define('Rds.judicial.form.JudicialSampleUpdateForm', {
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
				
		me.items = [{
			xtype : 'form',
			region : 'west',
			width : 500,
			defaults : {
				anchor : '100%',
				labelWidth : 80,
				labelSeparator : "：",
				labelAlign : 'right'
			},
			style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
			border : false,
			autoHeight : true,
			items : [{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
						xtype : 'hiddenfield',
						name : 'case_id'
					},{
						// 该列在整行中所占的百分比
						columnWidth : .27,
						xtype : "textfield",
						labelAlign : 'right',
						fieldLabel : "案例条形码<span style='color:red'>*</span>",
						labelWidth : 80,
						maxLength : 50,
						name : 'case_code',
					},{
						xtype : 'hiddenfield',
						name : 'case_code_old',
						id:'case_code_old'
					}
				]}]
		}
		];

		me.buttons = [{
					text : '修改',
					iconCls : 'Disk',
					handler : me.onUpdate
				}, {
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	onUpdate : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		var sample_str = values['sample_code'];
		var id_numbers = values['id_number'];
		var temp='';
		if(!(sample_str instanceof Array))
		{
			Ext.MessageBox.alert("提示信息","样本填写错误！");
			return;
		}else
		{
			for(var i=0;i<sample_str.length-1;i++){ 
				for(var j = i+1; j < sample_str.length; j ++)
				{
					if (sample_str[i]==sample_str[j]){ 
						Ext.MessageBox.alert("提示信息","重复样本条形码："+sample_str[i]);
						return;
					} 
				}
			} 
		}

			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				var id_result = true;
				var sample_call = values['sample_call'];
				if(undefined == sample_call)
				{
					Ext.MessageBox.alert("错误信息", "样本信息还没填呢！");
					return;
				}
				
				if (sample_call) {
					var id_number = values['id_number'];
						var father_num=0,mother_num=0;
						if(sample_call){
							if(!((typeof sample_str=='string')&&sample_str.constructor==String)){
								for(var i=0;i<sample_call.length;i++){
									if(sample_call[i]=='father'){
										father_num++;
									}
									if(sample_call[i]=='mother'){
										mother_num++;
									}
								}
							}
						}
						if(father_num>1||mother_num>1){
							Ext.MessageBox.alert("错误信息", "家长称谓出现重复");
							return;
						}
						Ext.Ajax.request({
									url : "judicial/verify/updateSampleCaseInfo.do",
									method : "POST",
									headers : {
										'Content-Type' : 'application/json'
									},
									jsonData : values,
									success : function(response, options) {
										response = Ext.JSON
												.decode(response.responseText);
										if (response.result == true) {
											Ext.MessageBox.alert("提示信息", response.msg);
											me.grid.getStore().load();
											me.up("window").close();
										} else {
											Ext.MessageBox.alert("错误信息",  response.msg);
										}
									},
									failure : function() {
										Ext.Msg.alert("提示", "修改失败<br>请联系管理员!");
									}
		
								});
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
			var values = me.getValues();
			var case_id = values["case_id"];
			var case_code_old = values["case_code"];
			Ext.getCmp("case_code_old").setValue(case_code_old);
			//添加样本信息
			Ext.Ajax.request({
				url : 'judicial/register/getSampleInfo.do',
				method : "POST",
				async: false,
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : {
					'case_id' : case_id
				},
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					var items = response["items"];
					for (var i = 0; i < items.length; i++) {
						me.down("form").add({
							xtype : 'form',
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
							items : [{
								layout : 'auto',
								xtype : 'panel',
								border : false,
								items : [{
									layout : 'column',
									xtype : 'panel',
									border : false,
									items : [{
													xtype : "hiddenfield",
													value : items[i].sample_code_sys,
													name : 'sample_code_sys'
											},{

												xtype : "hiddenfield",
												value : items[i].sample_code,
												name : 'sample_code_old'
										
											},{
												columnWidth : .27,
												xtype : "textfield",
												fieldLabel : "条形码<span style='color:red'>*</span>",
												//regex:/^\w+$/,
												//regexText:'请输入正确条形码',
												labelAlign : 'right',
												value : items[i].sample_code,
												maxLength : 50,
												labelWidth : 65,
												name : 'sample_code',
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												listeners:{
											        blur: function(combo, record, index){
											        	var sample_code= trim(combo.getValue());
											        	Ext.Ajax.request({
															url : "judicial/register/exsitSampleCode.do",
															method : "POST",
															async : false,
															headers : {
																'Content-Type' : 'application/json'
															},
															jsonData : {
																sample_code : sample_code
															},
															success : function(response,options) {
																response = Ext.JSON.decode(response.responseText);
																if (!response) {
																	Ext.Msg.alert("提示","此"+sample_code+"条形码系统已存在，请核实无误后录入！");
																}
															},
															failure : function() {
																Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
															}
														});
											        }
											    }
											}, {
												columnWidth : .24,
												xtype : "textfield",
												fieldLabel : "用户名<span style='color:red'>*</span>",
												labelAlign : 'right',
												maxLength : 50,
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												labelWidth : 65,
												value : items[i].sample_username,
												name : 'sample_username'
											},{
												columnWidth : .24,
												xtype : "textfield",
												fieldLabel : "地址",
												labelAlign : 'right',
												maxLength : 50,
												blankText : "不能为空",// 错误提示信息，默认为This
												labelWidth : 65,
												hidden:true,
												value : items[i].address,
												name : 'address'
											}, {
												columnWidth : .26,
												xtype : "textfield",
												fieldLabel : '身份证',
												labelAlign : 'right',
												maxLength : 18,
												value : items[i].id_number,
												labelWidth : 65,
												regex:/^[^\s]*$/,
												regexText:'请输入正确身份证号',
												name : 'id_number',
												listeners:
												{
													"blur":function(combo, record, index){
														if(""!=combo.getValue())
														{
															var mei = this.up("form");
															mei.getForm().findField('birth_date').setValue(getBirthdatByIdNo(combo.getValue()));
															console.log(getBirthdatByIdNo(combo.getValue()));
														}
														
													}
												}
											},{
												layout : 'absolute',// 从左往右的布局
												xtype : 'panel',
												border : false,
												columnWidth : .23,
												items : [Ext.create('Ext.form.ComboBox', {
													fieldLabel: "特殊样本",
								                    labelWidth : 60,
								                    editable:false,
								                    width:140,
								                    triggerAction: 'all',
								                    displayField: 'Name',
								                    labelAlign: 'right',
								                    valueField: 'Code',
								                    readOnly:true,
								                    store: new Ext.data.ArrayStore({
								                        fields: ['Name', 'Code'],
								                        data: [['否', '0'], ['是', '1']]
								                    }),
								                    value:items[i].special,
								                    mode: 'local',
								                    name: 'special'
												}) ]
											} ]
								}, {
									layout : 'column',
									xtype : 'panel',
									border : false,
									style : 'margin-top:5px;',
									items : [{
												columnWidth : .27,
												xtype : "combo",
												fieldLabel : "取样类型<span style='color:red'>*</span>",
												mode : 'local',
												labelWidth : 60,
												labelAlign : 'right',
												blankText : '请选择',
												emptyText : '请选择',
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												valueField : "key",
												displayField : "value",
												value : items[i].sample_type,
												maxLength : 50,
												labelWidth : 65,
												editable:false,
												store : storeSampleType,
												name : 'sample_type',
												readOnly:true,
												 listeners :{
							                        	"select" : function(combo, record, index){
							                        		var mei = this.up("form");
							                        		if( combo.getValue() == 'cy01' || combo.getValue()== 'cy02' || combo.getValue()== 'cy03' )
								                        		mei.getForm().findField('special').setValue('0');
							                        		else
								                        		mei.getForm().findField('special').setValue('1');
							                        	}
							                        }
											}, {
												columnWidth : .24,
												xtype : "combo",
												mode : 'local',
												labelWidth : 60,
												labelAlign : 'right',
												blankText : '请选择',
												emptyText : '请选择',
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												editable:false,
												valueField : "key",
												displayField : "value",
												value : items[i].sample_call,
												store : storeSampleCall,
												fieldLabel : "称谓<span style='color:red'>*</span>",
												maxLength : 50,
												labelWidth : 65,
												name : 'sample_call'
											}, {
												columnWidth : .26,
												xtype : "textfield",
												fieldLabel : '出生日期',
												emptyText:'yyyy-MM-dd',  
												labelAlign : 'right',
												maxLength : 20,
												value : items[i].birth_date,
												labelWidth : 65,
												name : 'birth_date',
												regex : /^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/,
												regexText : "格式:yyyy-MM-dd！"
											}]
								}]
							}]
						});
					}
				},
				failure : function() {
					return;
				}
			});

		}
	}
});
