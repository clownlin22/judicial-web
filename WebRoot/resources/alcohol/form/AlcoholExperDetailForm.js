/**
 * 
 */

Ext.define('Rds.alcohol.form.AlcoholExperDetailForm', {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'form',
			region : 'north',
			labelAlign : "right",
			bodyPadding : 10,
			layout : 'column',
			defaults : {
				anchor : '100%',
				labelWidth : 100,
				border : false,
				style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;'
			},
			border : false,
			autoHeight : true,
			items : [{
						xtype : "textfield",
						name : 'case_id',
						hidden : true
					}, {
						xtype : "displayfield",
						fieldLabel : '案例编号',
						labelWidth : 60,
						columnWidth : 0.5,
						name : 'case_code'
					}, {
						xtype : "displayfield",
						fieldLabel : '当事人',
						labelWidth : 50,
						columnWidth : 0.5,
						name : 'sample_name'
					}, {
						xtype : "displayfield",
						fieldLabel : '身份证',
						labelWidth : 50,
						columnWidth : 1,
						name : 'id_number'
					}]
		}];
		me.callParent(arguments);
	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}
});