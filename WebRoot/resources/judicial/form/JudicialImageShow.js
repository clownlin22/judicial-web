/**
 * 图片查看
 */
var imgs="";
var img_show_index = 0;
var img_count = 0;
Ext.define('Rds.judicial.form.JudicialImageShow', {
	extend : 'Ext.form.Panel',
	layout : "border",
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'hiddenfield',
			name : 'id'
		}, {
			xtype : 'panel',
			region : 'center',
			id : 'judicial_img_show',
			autoScroll : true,
			items : [],
			buttons : [{
						text : '左转',
						iconCls : 'Arrowturnleft',
						handler : me.onTurnLeft
					}, {
						text : '右转',
						iconCls : 'Arrowturnright',
						handler : me.onTurnRight
					}]
		}];

		me.buttons = [{
					text : '取消',
					iconCls : 'Arrowredo',
					handler : me.onCancel
				}]
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
							Ext.getCmp('judicial_img_show').remove(Ext
									.getCmp('judicial_img_box'));
							Ext.getCmp('judicial_img_show').add({
								xtype : 'box',
								style : 'margin:6px;',
								id : 'judicial_img_box',
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
							Ext.getCmp('judicial_img_show').remove(Ext
									.getCmp('judicial_img_box'));
							Ext.getCmp('judicial_img_show').add({
								xtype : 'box',
								style : 'margin:6px;',
								id : 'judicial_img_box',
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
	onCancel : function() {
		var me = this;
		me.up("window").close();
	},
	listeners : {
		'afterrender' : function() {
			var me = this;
			var values = me.getValues();
			var id = values["id"];
			//添加案例图片信息
			Ext.Ajax.request({
						url : "judicial/attachment/getAttachMent.do",
						method : "POST",
						async: false,
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : {
							'id' : id
						},
						success : function(response, options) {
							var data = Ext.JSON.decode(response.responseText);
							if(data.length>0){
								imgs = data;
								img_count = data.length;
								Ext.getCmp('judicial_img_show').add({
									xtype : 'box',
									style : 'margin:6px;',
									id : 'judicial_img_box',
									autoEl : {
										tag : 'img',
										src : "judicial/attachment/getImg.do?filename="
												+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
									}
								});
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "获取图片失败<br>请联系管理员!");
						}
					});
		}
	}
});
