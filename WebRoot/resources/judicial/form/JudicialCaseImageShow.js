/**
 * @description 案例图片展示
 * @author yxb
 */

var imgs="";
var img_show_index = 0;
var img_count = 0;
var attachmentid=new Array();
var attachment_type = new Array();
Ext.define('Rds.judicial.form.JudicialCaseImageShow', {
	extend : 'Ext.form.Panel',
	layout : "border",
	initComponent : function() {
		var me = this;
		img_show_index = 0;
		me.items = [{
			xtype : 'panel',
			region : 'west',
			width : 200,
			hidden:true,
			autoScroll : true,
			items : [{
				xtype : 'form',
				width : 200,
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
								xtype : 'textfield',
								name : 'id',
								id:'attachmentid'
							}
							]
				}]
			}]
		},{
			xtype : 'panel',
			region : 'center',
			id : 'judicial_img_show_verify',
			autoScroll : true,
			items : [{
				xtype : 'tbtext',
				id : 'imageTitle',
				style:'font-size:20px;margin-left:15px;margin-top:15px;margin-bottom:15px;'
			},],
			buttons : [{
						text : '左转',
						iconCls : 'Arrowturnleft',
						handler : me.onTurnLeft
					}, {
						text : '右转',
						iconCls : 'Arrowturnright',
						handler : me.onTurnRight
					}, {
						text : '上一张',
						iconCls : 'Arrowleft',
						handler : me.onLast,
						hidden:true
					}, {
						text : '下一张',
						iconCls : 'Arrowright',
						handler : me.onNext
					}]
		}];

//		me.buttons = [{
//					text : '审核通过',
//					iconCls : 'Accept',
//					handler : me.onVerify
//				}, {
//					text : '审核不通过',
//					iconCls : 'Cancel',
//					handler : me.onNoVerify
//				}, {
//					text : '取消',
//					iconCls : 'Arrowredo',
//					handler : me.onCancel
//				}]
		me.callParent(arguments);
	},
	onTurnLeft : function() {
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var path = imgs[img_show_index].attachment_path;
		Ext.Ajax.request({
					url : "judicial/attachment/turnImg.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : {
						'path' : path,
						'direction' : 'left'
					},
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.success == true) {
							Ext.getCmp('judicial_img_show_verify').remove(Ext
									.getCmp('judicial_img_box_verify'));
							Ext.getCmp('judicial_img_show_verify').add({
								xtype : 'box',
								style : 'margin:6px;',
								id : 'judicial_img_box_verify',
								autoEl : {
									tag : 'img',
									src : "judicial/attachment/getImg.do?filename="
											+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
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
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var path = imgs[img_show_index].attachment_path;
		Ext.Ajax.request({
					url : "judicial/attachment/turnImg.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : {
						'path' : path,
						'direction' : 'right'
					},
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.success == true) {
							Ext.getCmp('judicial_img_show_verify').remove(Ext
									.getCmp('judicial_img_box_verify'));
							Ext.getCmp('judicial_img_show_verify').add({
								xtype : 'box',
								style : 'margin:6px;',
								id : 'judicial_img_box_verify',
								autoEl : {
									tag : 'img',
									src : "judicial/attachment/getImg.do?filename="
											+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
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
	onLast : function() {
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var me = this.up("box");
		var box = Ext.getCmp('judicial_img_box_verify');
		if (box == null) {
			Ext.Msg.alert("提示", "没有图片！");
			return;
		}
		if (img_show_index == 0) {
			Ext.Msg.alert("提示", "没有上一张了！");
			return;
		}
		Ext.getCmp('judicial_img_show_verify').remove(Ext
				.getCmp('judicial_img_box_verify'));

		img_show_index -= 1;
		Ext.getCmp('attachmentid').setValue(attachmentid[img_show_index]);
		Ext.getCmp('judicial_img_show_verify').add({
			xtype : 'box',
//			style : 'padding:6px;width:100%;',
			id : 'judicial_img_box_verify',
			autoEl : {
				tag : 'img',
				src : "judicial/attachment/getImg.do?filename="
						+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
			}
		});
	},
	onNext : function() {
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var me = this.up("box");
		var box = Ext.getCmp('judicial_img_box_verify');
		if (box == null) {
			Ext.Msg.alert("提示", "没有图片！");
			return;
		}
		if (img_show_index + 1 == img_count) {
			img_show_index = 0;
//			Ext.Msg.alert("提示", "没有下一张了！");
//			return;
		}else
		{
			img_show_index += 1;
		}
		Ext.getCmp("imageTitle").setText("共"+img_count+"张,第"+(Number(img_show_index)+Number(1))+"张<div align='center'>"+attachment_type[img_show_index]+"</div>");
		Ext.getCmp('judicial_img_show_verify').remove(Ext
				.getCmp('judicial_img_box_verify'));
		Ext.getCmp('attachmentid').setValue(attachmentid[img_show_index]);
		
		Ext.getCmp('judicial_img_show_verify').add({
			xtype : 'box',
//			style : 'padding:6px;width:100%;',
			id : 'judicial_img_box_verify',
			autoEl : {
				tag : 'img',
				src : "judicial/attachment/getImg.do?filename="
						+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
			}
		});
		// alert("next");
	},
	onVerify : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		values.taskId=me.taskId;
		if (form.isValid()) {
			Ext.Msg.confirm("提示", "确认该案例通过?", function (id) {
				if (id == 'yes') {
					Ext.MessageBox.wait('正在操作', '请稍后...');
					Ext.Ajax.request({
						url: "judicial/verify/yes.do",
						method: "POST",
						headers: {
							'Content-Type': 'application/json'
						},
						jsonData: values,
						success: function (response, options) {
							response = Ext.JSON.decode(response.responseText);
							if (response.success == true) {
								Ext.MessageBox.alert("提示信息", response.message);
								me.grid.getStore().load();
								me.up("window").close();
							} else {
								Ext.MessageBox.alert("错误信息", response.message);
							}
						},
						failure: function () {
							Ext.Msg.alert("提示", "出错<br>请联系管理员!");
						}

					});
				}
			});
		}
	},

	onNoVerify : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		values.taskId=me.taskId;
		values.processInstanceId=me.processInstanceId;
		var check_remark_verify = Ext.getCmp("check_remark_verify").getValue();
		if (check_remark_verify == '' || check_remark_verify == null) {
			Ext.Msg.alert("提示", "请填写审核不通过理由。");
			return;
		}
		if (form.isValid()) {
			Ext.Msg.confirm("提示", "确认该案例不通过?", function (id) {
				if (id == 'yes') {
					Ext.MessageBox.wait('正在操作', '请稍后...');
					Ext.Ajax.request({
						url: "judicial/verify/no.do",
						method: "POST",
						headers: {
							'Content-Type': 'application/json'
						},
						jsonData: values,
						success: function (response, options) {
							response = Ext.JSON.decode(response.responseText);
							if (response.success == true) {
								Ext.MessageBox.alert("提示信息", response.message);
								me.grid.getStore().load();
								me.up("window").close();
							} else {
								Ext.MessageBox.alert("错误信息", response.message);
							}
						},
						failure: function () {
							Ext.Msg.alert("提示", "出错<br>请联系管理员!");
						}

					});
				}
			});
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
			//添加案例图片信息
			Ext.Ajax.request({
						url : "judicial/attachment/getAttachMent.do",
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
								Ext.getCmp('attachmentid').setValue(data[0].id);
								for(var i = 0 ; i <data.length;i++)
								{
									attachmentid[i]=data[i].id;
									switch(data[i].attachment_type)
									{
									case 1:
										attachment_type[i]="登记表格";
									  break;
									case 2:
										attachment_type[i]="身份证";
									  break;
									case 3:
										attachment_type[i]="照片";
									  break;
									default:
										attachment_type[i]="其他";
									}
								}
								imgs = data;
								img_count = data.length;
								Ext.getCmp("imageTitle").setText("共"+img_count+"张,第"+(Number(img_show_index)+Number(1))+"张 <div align='center'>"+attachment_type[img_show_index]+"</div>");
								console.log(imgs[img_show_index])
								if(undefined != imgs[img_show_index].attachment_path)
								{
									Ext.getCmp('judicial_img_show_verify').add({
										xtype : 'box',
//										style : 'padding:6px;width:100%;',
										id : 'judicial_img_box_verify',
										autoEl : {
											tag : 'img',
											src : "judicial/attachment/getImg.do?filename="
													+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
										}
									});
								}
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "获取图片失败<br>请联系管理员!");
						}
					});
		}
	}
});
