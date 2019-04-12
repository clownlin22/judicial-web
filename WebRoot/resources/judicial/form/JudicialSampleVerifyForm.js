/**
 * @description 样本审核form
 * @author linys
 */
/**
 * 校验条形码是否都相同
 * 
 * @param {}
 *            verifymap
 * @return {}
 */
function setVerifyStatus(verifyMap) {
	var verify_Status = true;
	if (verifyMap == '{}') {
		verify_Status = false;
	} else {
		verifyMap.each(function(key, value, index) {
					if (value == false)
						verify_Status = false;
				});
	}
	return verify_Status;
}
verifyMap = new Map();
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

Ext.define('Rds.judicial.form.JudicialSampleVerifyForm', {
	extend : 'Ext.form.Panel',
	layout : "border",
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'panel',
			region : 'west',
			width : 1000,
			autoScroll : true,
			items : [{
				xtype : 'form',
				id : 'verify_form_display',
				width : 950,
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
									}, {
										xtype : 'hiddenfield',
										name : 'case_areacode'
									}, {
										xtype : "textfield",
										labelAlign : 'right',
										fieldLabel : '案例编号',
//										regex : /^\w*$/,
										regexText : '请输入英文或数字',
										readOnly : true,
										labelWidth : 100,
										allowBlank : false,// 不允许为空
										maxLength : 50,
										name : 'case_code'
									}]
						}]
			}, {
				xtype : 'textarea',
				emptyText : '审核不通过时，请填写审核理由',
				fieldLabel : '审核理由',
				name : 'verify_sampleinfo_remark',
				id : 'check_remark_verify',
				maxLength : 1000,
				width : 500,
				labelAlign : 'right'
			}]
		}];

		me.buttons = [{
					text : '审核通过',
					id : 'sample_verify_yes',
					iconCls : 'Accept',
					handler : me.onYesVerify
				}, {
					text : '审核不通过',
					id : 'sample_verify_no',
					iconCls : 'Delete',
					handler : me.onNoVerify
				}, {
					text : '取消',
					iconCls : 'Arrowredo',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	onYesVerify : function() {
		if (setVerifyStatus(verifyMap) != true) {
			Ext.Msg.alert("提示", "样本条形码不一致");
			return;
		}
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		var loadMarsk = new Ext.LoadMask(me, {
			msg : '正在处理数据，请稍候......',
			removeMask : true
				// 完成后移除
			});
		loadMarsk.show(); // 显示
		if (form.isValid()) {
			Ext.Ajax.request({
						url : "judicial/verify/sampleYes.do",
						method : "POST",
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : values,
						success : function(response, options) {
							loadMarsk.hide(); // 隐藏
							response = Ext.JSON.decode(response.responseText);
							if (response.success == true) {
								Ext.MessageBox.alert("提示信息", response.message);
								me.grid.getStore().load();
								me.up("window").close();
							} else {
								Ext.MessageBox.alert("错误信息", response.message);
							}
						},
						failure : function() {
							loadMarsk.hide(); // 隐藏
							Ext.Msg.alert("提示", "出错<br>请联系管理员!");
						}

					});
		}
	},
	onNoVerify : function() {
		var me = this.up("form");
		var form = me.getForm();
		if (!form.isValid()) {
			Ext.MessageBox.alert("提示信息", "请正确填写表单信息!");
			return;
		}
		if (setVerifyStatus(verifyMap) != false) {
			Ext.Msg.alert("提示", "样本条形码一致<br>不通过失败");
			return;
		}
		var values = form.getValues();
		var check_remark_verify = Ext.getCmp("check_remark_verify").getValue();
		if (check_remark_verify == '' || check_remark_verify == null) {
			Ext.Msg.alert("提示", "请填写不通过理由。");
			return;
		}
		
//		var cnt = Ext.getCmp("check_remark_verify").getValue().length; 
//		 if (cnt > 1000){
//		 	Ext.Msg.alert("提示", "审核理由过长。");
//			return;
//		 }
		var loadMarsk = new Ext.LoadMask(me, {
			msg : '正在处理数据，请稍候......',
			removeMask : true
				// 完成后移除
			});
		if (form.isValid()) {
			Ext.Msg.show({
						title : '提示',
						msg : '确定不通过样本？',
						width : 300,
						buttons : Ext.Msg.OKCANCEL,
						fn : function(buttonId, text, opt) {
							if (buttonId == 'ok') {
								loadMarsk.show(); // 显示
								Ext.Ajax.request({
											url : "judicial/verify/sampleNo.do",
											method : "POST",
											headers : {
												'Content-Type' : 'application/json'
											},
											jsonData : values,
											success : function(response,
													options) {
												loadMarsk.hide(); // 隐藏
												response = Ext.JSON
														.decode(response.responseText);
												if (response.success == true) {
													Ext.MessageBox.alert(
															"提示信息",
															response.message);
													me.grid.getStore().load();
													me.up("window").close();
												} else {
													Ext.MessageBox.alert(
															"错误信息",
															response.message);
												}
											},
											failure : function() {
												loadMarsk.hide(); // 隐藏
												Ext.Msg.alert("提示",
														"出错<br>请联系管理员!");
											}

										});
							} else {
								return;
							}

						},
						animateTarget : 'addAddressBtn',
						icon : Ext.window.MessageBox.INFO
					})

		}
	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	},
	listeners : {
		'afterrender' : function() {
			var me = this;
			verifyMap = new Map();
			var values = me.getValues();
			var case_id = values["case_id"];
			Ext.Ajax.request({
				url : 'judicial/register/getSampleInfo.do',
				method : "POST",
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
							// style :
							// 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
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
												columnWidth : .25,
												xtype : "combo",
												mode : 'local',
												labelWidth : 60,
												labelAlign : 'right',
												allowBlank : false,// 不允许为空
												readOnly : true,
												disabled : true,
												hideTrigger : true,
												valueField : "key",
												displayField : "value",
												value : items[i].sample_call,
												store : storeSampleCall,
												fieldLabel : '称谓',
												maxLength : 50,
												labelWidth : 80,
												name : 'sample_call'
											}, {
												columnWidth : .25,
												xtype : "displayfield",
												fieldLabel : '用户名',
												labelAlign : 'right',
												maxLength : 50,
												allowBlank : false,
												readOnly : true,
												hideTrigger : true,
												labelWidth : 80,
												value : items[i].sample_username,
												name : 'sample_username'
											}]
								}, {
									layout : 'column',
									xtype : 'panel',
									style : 'margin-left:0px;margin-right:0px;margin-top:5px;margin-bottom:5px;',
									border : false,
									items : [{
												columnWidth : 0.5,
												xtype : "displayfield",
												fieldLabel : '条形码',
												allowBlank : false,
												readOnly : true,
												hideTrigger : true,
												labelAlign : 'right',
												value : items[i].sample_code,
												maxLength : 50,
												labelWidth : 80,
												name : 'sample_code',
												id : 'sample_code' + i
											}, {
												columnWidth : 0.5,
												id : 'sample_verify_code' + i,
												xtype : "textfield",
												tabIndex : i + 1,
												fieldLabel : '校验条形码',
												allowBlank : false,
												labelAlign : 'right',
												maxLength : 50,
												labelWidth : 80,
												name : 'verify_sample_code',
												listeners : {
													// focus:function(){
													// alert(this.tabIndex);
													// },
													blur : function() {
														var vsc = trim(this
																.getValue());
														var sc = Ext
																.getCmp("sample_code"
																		+ this.id
																				.substr(
																						18,
																						1))
																.getValue();
														// alert(sc);
														// this
														// .up('form')
														// .getValues()["sample_code"];
														verifyMap.put(this.id,
																vsc == sc);
													},
													render : function() {
														Ext
																.getCmp("sample_verify_code0")
																.focus();
													},
													specialkey : function(
															field, e) {
														if (e.getKey() == e.ENTER) {
															var tempindex = this.id
																	.substr(18,
																			1)
																	/ 1 + 1;
															// alert(this.id
															// .substr(18,
															// 1)
															// / 1 + 1);
															if (tempindex == items.length) {
																Ext
																		.getCmp("sample_verify_yes")
																		.focus();
															} else {
																var tempid = "sample_verify_code"
																		+ tempindex;
																// alert(tempid);
																Ext
																		.getCmp(tempid)
																		.focus();
															}
														}
													}
												}
											}]
								}]
							}]
						});
					}
				},
				failure : function() {
					Ext.Msg.alert("提示", "请求失败<br>可能断网了!");
				}
			});
		}
	}
});