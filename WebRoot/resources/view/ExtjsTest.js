Ext.define('Rds.view.ExtjsTest', {
	extend : 'Ext.container.Viewport',
	layout : 'border',
	required:'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	initComponent : function() {
		var me = this;
		me.items = [Ext.create("Rds.judicial.panel.JudicialDataInput",{
			region:'center'
		})];
		me.callParent(arguments);
	}
});

