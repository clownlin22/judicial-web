/**
 * 图片查看
 */
Ext.define('Rds.finance.form.FinanceImageShow', {
	extend : 'Ext.form.Panel',
	layout : "border",
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'textfield',
			name : 'attachment_path',
			id:'attachment_path'
		}, {
			xtype : 'panel',
			region : 'center',
			id : 'finance_img_show',
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
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	onTurnLeft : function() {
		var path = Ext.getCmp("attachment_path").getValue();
		if(path==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		Ext.Ajax.request({
					url : "finance/casefinance/turnImg.do",
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
							Ext.getCmp('finance_img_show').remove(Ext
									.getCmp('finance_img_box'));
							Ext.getCmp('finance_img_show').add({
								xtype : 'box',
								style : 'margin:6px;',
								id : 'finance_img_box',
								autoEl : {
									tag : 'img',
									src : "finance/casefinance/getImg.do?filename="
											+ path+"&v="+new Date().getTime()
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
		var path = Ext.getCmp("attachment_path").getValue();
		if(path==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		Ext.Ajax.request({
					url : "finance/casefinance/turnImg.do",
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
							Ext.getCmp('finance_img_show').remove(Ext
									.getCmp('finance_img_box'));
							Ext.getCmp('finance_img_show').add({
								xtype : 'box',
								style : 'margin:6px;',
								id : 'finance_img_box',
								autoEl : {
									tag : 'img',
									src : "finance/casefinance/getImg.do?filename="
											+ path+"&v="+new Date().getTime()
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
			var attachment_path = values["attachment_path"];
			if(""==attachment_path || null ==attachment_path){
				attachment_path = finance_attachment;
			}
			Ext.getCmp('finance_img_show').add({
				xtype : 'box',
				style : 'margin:6px;',
				id : 'finance_img_box',
				autoEl : {
					tag : 'img',
					src : "finance/casefinance/getImg.do?filename="
							+ attachment_path+"&v="+new Date().getTime()
				}
			});
		}
	}
});
