/**
 * 
 */
 var screenheight = document.body.clientHeight;
 var imgheight = 2*screenheight/3;
Ext.define("Rds.judicial.form.JudicialCasePhotoForm", {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	region : "center",
	autoScroll : true,
	photoready : false,
	layout : 'anchor',
	bodyPadding : 10,
	defaults : {},
	initComponent : function() {
		var me = this;

		me.items = [{
			xtype : 'fieldset',
			title : '案例信息',
			defaults : {
				anchor : '100%'
			},
			layout : 'anchor',
			items : [{
						xtype : "textfield",
						hidden : true,
						name : 'case_id'
					}, {
						xtype : "textfield",
						labelWidth : 100,
						fieldLabel : '案例编号',
						name : 'case_code',
						listeners : {
							'blur' : function() {
								var formt = this.up("form").getForm();
								if (this.getValue() == ''){
									formt.reset();
									Ext.getCmp("casephoto_file").setDisabled(true);
									Ext.getCmp('case_print_photo').remove(Ext
										.getCmp('casephoto_img_box'));
									return;
								}
									
								var window = this.up("window");
								Ext.Ajax.request({
									url : "judicial/casephoto/getCaseReceiver.do",
									headers : {
										'Content-Type' : 'application/json'
									},
									method : "POST",
									jsonData : {
										'case_code' : this.getValue()
									},
									success : function(response, options) {
										response = Ext.JSON
												.decode(response.responseText);
										if (response.success == true) {
											Ext.getCmp("casephoto_file").setDisabled(false);
											Ext.getCmp('case_print_photo').remove(Ext
										.getCmp('casephoto_img_box'));
											Ext.getCmp("case_receiver_photo")
													.setValue(response.data.case_receiver);
											if (response.data.attachment_path !== ''
													&& response.data.attachment_path !== null
													&& response.data.attachment_path !== 'null') {

//												Ext
//														.getCmp('case_print_photo')
//														.remove(Ext
//																.getCmp('casephoto_img_box'));
												Ext.getCmp('case_print_photo')
														.add({
															xtype : 'box',
															style : 'margin:6px;width:100%;',
															id : 'casephoto_img_box',
															autoEl : {
																tag : 'img',
																src : "judicial/casephoto/getCasePhotoInServer.do?attachment_path="
																		+ response.data.attachment_path
																		+ "&v="
																		+ new Date()
																				.getTime()
															}
														});
											}
										} else {
											Ext.Msg.alert("消息", response.msg);
											window.close();
										}
									},
									failure : function() {
										Ext.Msg.alert("提示", "请求出错了");
									}

								});
							}
						}
					}, {
						xtype : "textfield",
						labelWidth : 100,
						fieldLabel : '所属人',
						id : 'case_receiver_photo',
						readOnly : true,
						name : 'case_receiver'
					}, {
						xtype : 'filefield',
						id:'casephoto_file',
						disabled:true,
						name : 'casephoto',
						fieldLabel : '案例照片',
						labelWidth : 100,
						allowBlank : false,
						anchor : '100%',
						buttonText : '选择照片',
						listeners : {
							'change' : function(cc, value, e) {
								var me = this;
								var myWindow = me.up('window');
								var form = me.up('form').getForm();
								if (!value.endWith(".jpg")&&!value.endWith(".JPG")&&!value.endWith(".png")&&!value.endWith(".PNG")) {
									Ext.MessageBox
											.alert("提示信息", "图片格式只能是jpg,png");
									return;
								}
								form.submit({
									url : 'judicial/casephoto/photoUpload.do',
									method : 'post',
									waitMsg : '正在上传您的文件...',
									success : function(form, action) {
										response = Ext.JSON
												.decode(action.response.responseText);
										if (response.success) {
											Ext
													.getCmp('case_print_photo')
													.remove(Ext
															.getCmp('casephoto_img_box'));
											Ext.getCmp('case_print_photo').add(
													{
														xtype : 'box',
														style : 'margin:6px;width:100%;',
														id : 'casephoto_img_box',
														autoEl : {
															tag : 'img',
															src : "judicial/casephoto/getCasePhoto.do?v="
																	+ new Date()
																			.getTime()
														}
													});
											me.photoready = true;
										} else {
											Ext.MessageBox.alert("提示信息",
													"图片错误，请联系管理员");
										}
									},
									failure : function() {
										Ext.Msg.alert("提示", "上传失败，请联系管理员!");
									}
								});
							}
						}
					}]
		}, {
			xtype : 'fieldset',
			height : imgheight+20,
			title : '图片预览',
			defaults : {
				anchor : '100%'
			},
			layout : 'anchor',
			items : [Ext.create('Ext.form.Panel', {
						region : "center",
						autoScroll : true,
						height : imgheight,
						id : 'case_print_photo',
						items : [{
									xtype : 'box',
									style : 'margin:6px;height:100%;',
									id : 'casephoto_img_box'
								}],
						buttons : [{
									text : '左转',
									iconCls : 'Arrowturnleft',
									handler : me.onTurnLeft
								}, {
									text : '右转',
									iconCls : 'Arrowturnright',
									handler : me.onTurnRight
								}]

					})]
		}];

		me.buttons = [{
			text : '上传并上传下一个案例',
			iconCls : 'Diskupload',
			handler : function() {
				var myWindow = this.up('window');
				var formt = this.up("form").getForm();

				var form = this.up('form').getValues();
				if (me.photoready) {
					Ext.MessageBox.alert("提示信息", "请选择照片!");
					return;
				}
				Ext.Ajax.request({
							url : "judicial/casephoto/savephoto.do",
							headers : {
								'Content-Type' : 'application/json'
							},
							method : "POST",
							jsonData : {
								'case_code' : form.case_code
							},
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response.success == true) {
									Ext.Msg.alert("消息", response.msg);
									
								} else {
									Ext.Msg.alert("消息", response.msg);
								}
								formt.reset();
								Ext.getCmp('case_print_photo').remove(Ext
										.getCmp('casephoto_img_box'));
							},
							failure : function() {
								Ext.Msg.alert("提示", "请求出错了");
							}

						});
			}
		}, {
			text : '上传',
			iconCls : 'Diskupload',
			handler : function() {
				var myWindow = this.up('window');
				var form = this.up('form').getValues();
				if (me.photoready) {
					Ext.MessageBox.alert("提示信息", "请选择照片!");
					return;
				}
				Ext.Ajax.request({
							url : "judicial/casephoto/savephoto.do",
							headers : {
								'Content-Type' : 'application/json'
							},
							method : "POST",
							jsonData : {
								'case_code' : form.case_code
							},
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response.success == true) {
									Ext.Msg.alert("消息", response.msg);
								} else {
									Ext.Msg.alert("消息", response.msg);
								}
								myWindow.close();
							},
							failure : function() {
								Ext.Msg.alert("提示", "请求出错了");
							}

						});
			}
		}, {
			text : '取消',
			iconCls : 'Cancel',
			handler : function() {
				this.up('window').close();
			}
		}
//		, {
//			text : '重置',
//			iconCls : 'Cancel',
//			handler : function() {
//				console.info(this.up('form').getForm());
//				this.up('form').getForm().reset();
//			}
//		}
		]
		me.callParent(arguments);
	},
	onTurnLeft : function() {
		Ext.Ajax.request({
					url : "judicial/casephoto/turnImg.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : {
						'direction' : 'left'
					},
					waitMsg : '正在上传您的文件...',
					async: false,
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.success == true) {
							Ext.getCmp('case_print_photo').remove(Ext
									.getCmp('casephoto_img_box'));
							Ext.getCmp('case_print_photo').add({
								xtype : 'box',
								style : 'margin:6px;width:100%;',
								id : 'casephoto_img_box',
								autoEl : {
									tag : 'img',
									src : "judicial/casephoto/getCasePhoto.do?v="
											+ new Date().getTime()
								}
							});
						} else {
							Ext.Msg.alert("提示", "旋转失败，请重新旋转！");
							return;
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "获取图片失败！");
					}
				});
	},
	onTurnRight : function() {
		Ext.Ajax.request({
					url : "judicial/casephoto/turnImg.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : {
						'direction' : 'right'
					},
					waitMsg : '正在上传您的文件...',
					async: false,
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.success) {
							Ext.getCmp('case_print_photo').remove(Ext
									.getCmp('casephoto_img_box'));
							Ext.getCmp('case_print_photo').add({
								xtype : 'box',
								style : 'margin:6px;width:100%;',
								id : 'casephoto_img_box',
								autoEl : {
									tag : 'img',
									src : "judicial/casephoto/getCasePhoto.do?v="
											+ new Date().getTime()
								}
							});
						} else {
							Ext.Msg.alert("提示", "旋转失败，请重新旋转！");
							return;
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "获取图片失败！");
					}
				});
	}

});