var sample_code_length;
var storeCall = Ext.create('Ext.data.Store', {
	model : 'model',
	proxy : {
		type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'judicial/dicvalues/getCustodyCall.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	autoLoad : true,
	remoteSort : true
});
var agentiaStore = Ext.create('Ext.data.Store', {
	fields : ['agentia_id', 'agentia_name'],
	proxy : {
		type : 'jsonajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'children/agentia/getAgentiaCombo.do',
		params : {},
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});
var tariffStore = Ext.create('Ext.data.Store', {
	fields : ['tariff_id', 'tariff_name', 'tariff_price'],
	proxy : {
		type : 'jsonajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'children/register/getTariff.do',
		params : {},
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});

queryMailInfo = function(value, mail_type) {
	var win = Ext.create("Ext.window.Window", {
				title : "快递信息（快递单号：" + value + "）",
				width : 700,
				iconCls : 'Find',
				height : 350,
				layout : 'border',
				modal:true,
				border : false,
				bodyStyle : "background-color:white;",
				items : [Ext.create('Ext.grid.Panel', {
									width : 700,
									height : 350,
									frame : false,
									renderTo : Ext.getBody(),
									viewConfig : {
										forceFit : true,
										stripeRows : true
										,// 在表格中显示斑马线
									},
									store : {// 配置数据源
										fields : ['time', 'content'],// 定义字段
										proxy : {
											type : 'jsonajax',
											actionMethods : {
												read : 'POST'
											},
											url : 'judicial/mail/getMailInfo.do',
											params : {
												'mail_code' : value,
												'mail_type' : mail_type
											},
											reader : {
												type : 'json',
												root : 'items',
												totalProperty : 'count'
											}
										},
										autoLoad : true
										// 自动加载
									},
									columns : [// 配置表格列
									{
												header : "时间",
												dataIndex : 'time',
												flex : 1.5,
												menuDisabled : true
											}, {
												header : "地点",
												dataIndex : 'content',
												flex : 3,
												menuDisabled : true
											}]
								})]
			});
	win.show();
}

var imgs="";
var img_show_index = 0;
var img_count = 0;
Ext.define('Rds.children.form.ChildrenCheckForm', {
	extend : 'Ext.form.Panel',
	layout : "border",
	initComponent : function() {
		var me = this;
		var tariff=Ext.create('Ext.form.ComboBox',{
			name : 'tariff_id',
			fieldLabel : '套餐类型',
			columnWidth : .5,
			labelWidth : 80,
			editable : false,
			mode : 'remote',
			forceSelection : true,
			allowBlank : true,
			triggerAction : 'all',
			displayField : 'tariff_name',
			labelAlign : 'right',
			valueField : 'tariff_id',
			store : tariffStore,
			listeners : {
				change : function(value, newValue, oldValue,
						eOpts) {
					stand_sum.setValue(tariffStore.findRecord(
									"tariff_id", newValue)
									.get("tariff_price"));
				}
			},
			readOnly:true
	  });
	var stand_sum=new Ext.form.TextField({
		// 该列在整行中所占的百分比
		columnWidth : .5,
		xtype : "textfield",
		labelAlign : 'right',
		fieldLabel : "标准金额",
		allowBlank : false,// 不允许为空
		blankText : "不能为空",// 错误提示信息，默认为This field is
		// required!
		labelWidth : 80,
		maxLength : 50,
		name : 'stand_sum',
		readOnly:true
		});
		me.items = [{
			xtype : 'panel',
			region : 'west',
			width : 750,
			autoScroll : true,
			items : [{
				xtype : 'form',
				width : 700,
				style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
				defaults : {
					anchor : '100%',
					labelWidth : 80,
					labelSeparator : "：",
					labelAlign : 'right'
				},
				border : false,
				autoHeight : true,
				items : [{
					border : false,
					xtype : 'fieldcontainer',
					layout : "form",
					items : [{
								xtype : 'hiddenfield',
								name : 'case_id'
							},{
								xtype : 'hiddenfield',
								name : 'gather_id'
							},
							{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
									xtype : "textfield",
									labelAlign : 'right',
									fieldLabel : '案例条形码',
									readOnly : true,
									labelWidth : 80,
									columnWidth : .33,
									maxLength : 50,
									name : 'case_code'
								},{
									xtype : "textfield",
									labelAlign : 'right',
									fieldLabel : '样本条形码',
									readOnly : true,
									labelWidth : 80,
									columnWidth : .33,
									maxLength : 50,
									name : 'sample_code'
								},{
									xtype : 'datefield',
									name : 'gather_time',
									fieldLabel : '采集日期',
									labelWidth : 80,
									columnWidth : .34,
									readOnly : true,
									hideTrigger : true,
									labelAlign : 'right',
									format : 'Y-m-d',
									readOnly:true
								}]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
									xtype : "textfield",
									fieldLabel : '归属人',
									columnWidth : .33,
									labelAlign : 'right',
									labelWidth : 80,
									name : 'case_username',
									readOnly : true
								}, {
									xtype : "textfield",
									fieldLabel : '案例归属地',
									labelAlign : 'right',
									columnWidth : .67,
									labelWidth : 80,
									readOnly : true,
									name : 'case_areaname'
								}]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
											xtype : 'textarea',
											fieldLabel : "详细地址",
											name : 'address',
											width : 500,
											height:30,
											allowBlank : false,// 不允许为空
											blankText : "不能为空",// 错误提示信息，默认为This field
											maxLength : 500,
											columnWidth : 1,
											labelWidth : 80,
											labelAlign : 'right',
											readOnly:true
										}]
							},{

								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [{
											// 该列在整行中所占的百分比
											columnWidth : .5,
											xtype : "textfield",
											labelAlign : 'right',
											fieldLabel : "儿童姓名",
											allowBlank : false,// 不允许为空
											blankText : "不能为空",// 错误提示信息，默认为This field
											labelWidth : 80,
											maxLength : 50,
											name : 'child_name',
											readOnly:true
										}, {
											columnWidth : .5,
											xtype : "textfield",
											fieldLabel : '身份证',
											labelAlign : 'right',
											maxLength : 18,
											labelWidth : 80,
											name : 'id_number',
											readOnly:true
										}]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
											xtype : 'datefield',
											name : 'birth_date',
											columnWidth : .5,
											labelWidth : 80,
											fieldLabel : '出生时间',
											labelAlign : 'right',
											format : 'Y-m-d',
											readOnly:true

										}, new Ext.form.field.ComboBox({
													fieldLabel : '性别',
													columnWidth : .5,
													labelWidth : 80,
													editable : false,
													triggerAction : 'all',
													displayField : 'Name',
													labelAlign : 'right',
													valueField : 'Code',
													store : new Ext.data.ArrayStore({
																fields : ['Name',
																		'Code'],
																data : [['女', 0],
																		['男', 1]]
															}),
													value : 0,
													mode : 'local',
													name : 'child_sex',
													readOnly:true
												})]
							
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
											xtype : 'textarea',
											fieldLabel : '出生医院',
											name : 'birth_hospital',
											width : 500,
											maxLength : 500,
											columnWidth : 0.5,
											labelWidth : 80,
											labelAlign : 'right',
											height:30,
											readOnly:true
										},{
											xtype : 'textarea',
											fieldLabel : '户籍所在地',
											name : 'house_area',
											width : 500,
											height:30,
											maxLength : 500,
											columnWidth : 0.5,
											labelWidth : 80,
											labelAlign : 'right',
											readOnly:true
										}]
							
							},{

								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
											xtype : 'textarea',
											fieldLabel : '生活所在地',
											name : 'life_area',
											width : 500,
											height:30,
											maxLength : 500,
											columnWidth : 0.5,
											labelWidth : 80,
											labelAlign : 'right',
											readOnly:true
										},{
											xtype : 'textarea',
											fieldLabel : '反馈寄送地',
											name : 'mail_area',
											width : 500,
											height:30,
											maxLength : 500,
											columnWidth : 0.5,
											labelWidth : 80,
											labelAlign : 'right',
											readOnly:true
										}]
							
							},{

								xtype : 'fieldset',
								title : '案例财务信息',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [{
									layout : "column",// 从左往右的布局
									xtype : 'fieldcontainer',
									border : false,
									items : [tariff,stand_sum]
								}]
							
							},{

								xtype : 'fieldset',
								title : '案例邮寄信息',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [{
									layout : "column",// 从左往右的布局
									xtype : 'fieldcontainer',
									border : false,
									items : [{
										// 该列在整行中所占的百分比
										columnWidth : .5,
										xtype : "textfield",
										labelAlign : 'right',
										padding : "0 0 5 0",
										fieldLabel : '快递名称',
										allowBlank : true,// 不允许为空
										labelWidth : 80,
										maxLength : 50,
										name : 'mail_name',
										readOnly:true
									},{
										// 该列在整行中所占的百分比
										columnWidth : .5,
										xtype : "textfield",
										padding : "0 0 5 0",
										labelAlign : 'right',
										fieldLabel : '快递编号',
										allowBlank : true,// 不允许为空
										labelWidth : 80,
										maxLength : 50,
										name : 'mail_code',
										readOnly:true
									}]
								}]
							},{

								xtype : 'fieldset',
								title : '案例采集人信息',
								layout : 'anchor',
								defaults : {
									anchor : '100%'
								},
								items : [{
									border : false,
									xtype : 'fieldcontainer',
									layout : "column",
									items : [{
												columnWidth : .45,
												xtype : "textfield",
												fieldLabel : '采集人姓名',
												labelAlign : 'right',
												maxLength : 50,
												labelWidth : 80,
												name : 'gather_name',
												id:'gather_name',
												readOnly:true
											}, {
												columnWidth : .45,
												xtype : "textfield",
												fieldLabel : '采集人身份证',
												labelAlign : 'right',
												maxLength : 18,
												labelWidth : 120,
												name : 'gather_id_number',
												id : 'gather_id_number',
												readOnly:true
											}]
								}, {
									border : false,
									xtype : 'fieldcontainer',
									layout : "column",
									items : [{
												columnWidth : .45,
												xtype : "textfield",
												fieldLabel : '手机号码',
												labelAlign : 'right',
												maxLength : 20,
												labelWidth : 80,
												name : 'gather_phone',
												id : 'gather_phone',
												readOnly:true
											}, {
												columnWidth : .45,
												xtype : "textfield",
												fieldLabel : '所属单位',
												labelAlign : 'right',
												maxLength : 500,
												labelWidth : 120,
												name : 'gather_company_name',
												id : 'gather_company_name',
												readOnly:true
											}]
								}]
							}
							]
				},{
					xtype : 'textarea',
					fieldLabel : '备注',
					name : 'remark',
					height:50,
					readOnly:true
				}]
			}]
		}, {
			xtype : 'panel',
			region : 'center',
			id : 'children_img_show_verify',
			autoScroll : true,
			items : [],
			buttons : [{
						text : '上一张',
						iconCls : 'Arrowleft',
						handler : me.onLast
					}, {
						text : '下一张',
						iconCls : 'Arrowright',
						handler : me.onNext
					}]
		}];

		me.buttons = [{
						text : '取消',
						iconCls : 'Arrowredo',
						handler : me.onCancel
					}]
		me.callParent(arguments);
	},
	onLast : function() {
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var me = this.up("box");
		var box = Ext.getCmp('children_img_box_verify');
		if (box == null) {
			Ext.Msg.alert("提示", "没有图片！");
			return;
		}
		if (img_show_index == 0) {
			Ext.Msg.alert("提示", "没有上一张了！");
			return;
		}
		Ext.getCmp('children_img_show_verify').remove(Ext
				.getCmp('children_photo_type'));
		Ext.getCmp('children_img_show_verify').remove(Ext
				.getCmp('children_img_box_verify'));
		img_show_index -= 1;
		console.log(imgs[img_show_index].photo_type);
		//获取图片高度和宽度
		Ext.getCmp('children_img_show_verify').add({
			id : 'children_photo_type',
			xtype : 'tbtext',
			style:'color:black;margin-left:20px;margin-top:10px;margin-bottom:10px;font-size:20px',
			text : (imgs[img_show_index].photo_type=="1"?"照片":(imgs[img_show_index].photo_type=="2"?"照片":(imgs[img_show_index].photo_type=="5"?"登记表格":"")))
		},{
			xtype : 'box',
			id : 'children_img_box_verify',
			autoEl : {
				tag : 'img',
				src : "children/register/getImg.do?filename="
						+ imgs[img_show_index].photo_path+"&v="+new Date().getTime()
			}
		});
	},
	onNext : function() {
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var me = this.up("box");
		var box = Ext.getCmp('children_img_box_verify');
		if (box == null) {
			Ext.Msg.alert("提示", "没有图片！");
			return;
		}
		if (img_show_index + 1 == img_count) {
			Ext.Msg.alert("提示", "没有下一张了！");
			return;
		}
		Ext.getCmp('children_img_show_verify').remove(Ext
				.getCmp('children_img_box_verify'));
		Ext.getCmp('children_img_show_verify').remove(Ext
				.getCmp('children_photo_type'));
		img_show_index += 1;
		Ext.getCmp('children_img_show_verify').add({
			id : 'children_photo_type',
			xtype : 'tbtext',
			style:'color:black;margin-left:20px;margin-top:10px;margin-bottom:10px;font-size:20px',
			text : (imgs[img_show_index].photo_type=="1"?"照片":(imgs[img_show_index].photo_type=="2"?"照片":(imgs[img_show_index].photo_type=="5"?"登记表格":"")))
		},{
			xtype : 'box',
			id : 'children_img_box_verify',
			autoEl : {
				tag : 'img',
				src : "children/register/getImg.do?filename="
						+ imgs[img_show_index].photo_path+"&v="+new Date().getTime()
			}
		});
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
			var gather_id = values["gather_id"];
			tariffStore.load();
			Ext.Ajax.request({
				url : "children/register/getCustodyInfo.do",
				method : "POST",
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : {
					'case_id' : case_id
				},
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					if (response.length > 0) {
							for (var i = 0; i < response.length; i++) {
								me.down("form").add({
									xtype : 'form',
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
									items : [{
										layout : 'auto',
										xtype : 'panel',
										border : false,
										items : [{
											layout : 'column',
											xtype : 'panel',
											border : false,
											items : [{
												columnWidth : .4,
												xtype : "textfield",
												fieldLabel : "监护人姓名",
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												labelAlign : 'right',
												maxLength : 50,
												value : response[i].custody_name,
												labelWidth : 80,
												name : 'custody_name',
												readOnly:true
											}, {
												columnWidth : .4,
												xtype : "textfield",
												fieldLabel : '身份证',
												labelAlign : 'right',
												maxLength : 18,
												labelWidth : 80,
												value : response[i].id_number,
												name : 'custody_id_number',
												readOnly:true
											}]
										},{
											layout : 'column',
											xtype : 'panel',
											border : false,
											style:'margin-top:10px;',
											items : [{
												columnWidth : .4,
												xtype : "combo",
												mode : 'local',
												labelWidth : 60,
												labelAlign : 'right',
												editable : false,
												blankText : '请选择',
												emptyText : '请选择',
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												valueField : "key",
												displayField : "value",
												store : storeCall,
												value : response[i].custody_call,
												fieldLabel : "称谓",
												maxLength : 50,
												labelWidth : 80,
												name : 'custody_call',
												readOnly:true
											}, {
												columnWidth : .4,
												xtype : "textfield",
												fieldLabel : '手机号码',
												labelAlign : 'right',
												maxLength : 20,
												value : response[i].phone,
												labelWidth : 80,
												name : 'custody_phone',
												readOnly:true
											}]
										
										}]
									}]
								});
							}
					}
				},
				failure : function() {
					return;
				}
			});
			//添加案例图片信息
			Ext.Ajax.request({
						url : "children/register/queryCasePhoto.do",
						method : "POST",
						async: false,
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : {
							'case_id' : case_id
						},
						success : function(response, options) {
							var data = Ext.JSON.decode(response.responseText);
							if(data.length>0){
								imgs = data;
								img_count = data.length;
								Ext.getCmp('children_img_show_verify').add({
									id : 'children_photo_type',
									xtype : 'tbtext',
									style:'color:black;margin-left:20px;margin-top:10px;margin-bottom:10px;font-size:20px',
									text : (imgs[img_show_index].photo_type=="1"?"照片":(imgs[img_show_index].photo_type=="2"?"照片":(imgs[img_show_index].photo_type=="5"?"登记表格":"")))
								},{
									xtype : 'box',
									id : 'children_img_box_verify',
									autoEl : {
										tag : 'img',
										src : "children/register/getImg.do?filename="
												+ imgs[img_show_index].photo_path+"&v="+new Date().getTime()
									}
								});
								
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "获取图片失败<br>请联系管理员!");
						}
					});
			//添加案例采集人信息
			Ext.Ajax.request({
						url : "children/register/getGatherInfo.do",
						method : "POST",
						async: false,
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : {
							'gather_id' : gather_id
						},
						success : function(response, options) {
							if("" != response.responseText){
								var data = Ext.JSON.decode(response.responseText);
								Ext.getCmp("gather_name").setValue(data.gather_name);
								Ext.getCmp("gather_id_number").setValue(data.id_number);
								Ext.getCmp("gather_phone").setValue(data.phone);
								Ext.getCmp("gather_company_name").setValue(data.company_name);
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "获取图片失败<br>请联系管理员!");
						}
					});
		}
	}
});
