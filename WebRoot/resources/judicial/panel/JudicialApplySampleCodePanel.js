Ext.define("Rds.judicial.panel.JudicialApplySampleCodePanel",{
	extend:"Ext.grid.Panel",
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:15,
	initComponent : function() {
		var me = this;
		var sample_code = Ext.create('Ext.form.field.Text', {
			name : 'sample_code',
			labelWidth : 80,
			width : 200,
			fieldLabel : '样本条形码'
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','sample_code','create_time','create_per','create_per_name'],
			start:0,
			limit:15,
			pageSize:15,
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'judicial/register/queryApplySampleCode.do',
                params:{
                },
                reader: {
                	type : 'json',
					root : 'items',
					totalProperty : 'count'
                }
            },
            listeners:{
            	'beforeload':function(ds, operation, opt){
        			Ext.apply(me.store.proxy.params, {
        				sample_code:trim(sample_code.getValue())
        			})
        		}
            }
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode: 'SINGLE'
		});
		
		me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录"
        });
		me.columns = [
		              Ext.create("Ext.grid.RowNumberer",{text: '序号',width:'4%', menuDisabled:true}),
	                  { text: '样本条形码',	dataIndex: 'sample_code', menuDisabled:true, flex: .6},
	                  { text: '创建时间',	dataIndex: 'create_time', menuDisabled:true, flex: .6},
	                  { text: '创建人',	dataIndex: 'create_per_name', menuDisabled:true, flex: .8}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[sample_code,{
            	text:'查询',
            	iconCls:'Find',
            	handler:me.onSearch}]
	 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[{
	 			text:'新增',
	 			iconCls:'Add',
	 			handler:me.onInsert
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	onInsert:function(){
		var me = this.up("gridpanel");
		var form = Ext.create("Rds.judicial.form.JudicialApplySampleCodeForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'样本条形码—新增',
			width:250,
			iconCls:'Add',
			height:180,
			layout:'border',
			items:[form]
		});
		win.show();
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
		
	}
});
function dateformat(value) {
	if (value != null) {
		return Ext.Date.format(value, 'Y-m-d');
	} else {
		return '';
	}
}