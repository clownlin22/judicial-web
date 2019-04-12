/**
 * 图片查看
 */
Ext.define('Rds.alcohol.form.AlcoholImageShow', {
	extend : 'Ext.form.Panel',
	layout : "border",
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'hiddenfield',
			name : 'att_path'
		}, {
			xtype : 'panel',
			region : 'center',
			id : 'children_img_show',
			autoScroll : true,
		}];
		me.callParent(arguments);
	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	},
	listeners : {
		'afterrender' : function() {
			var me = this;
			var values = me.getValues();
			var photo_path = values["att_path"];
			//添加案例图片信息
			Ext.getCmp('children_img_show').add({
				xtype : 'box',
				style : 'margin:6px;',
				autoEl : {
					tag : 'img',
					src : "alcohol/register/getImg.do?filename="+photo_path+"&v="+new Date().getTime()
				}
			});
		}
	}
});
