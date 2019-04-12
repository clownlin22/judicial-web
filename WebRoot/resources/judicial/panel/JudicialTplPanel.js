Ext.define('Rds.judicial.panel.JudicialTplPanel', {
	extend :'Ext.form.Panel',
	config:{
		f_data:null,
		store:null
	},
	autoScroll:true,
	initComponent : function() {
		var me = this;
		
		me.items = [{
            xtype:'fieldset',
            title: '体格检查',
            defaultType: 'htmleditor',
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            items :[{
                fieldLabel: '体格检查',
                afterLabelTextTpl: me.required,
                grow: true,  
                hideLabel:true,
                name: 'tgjc',
                allowBlank:false
            }]
        },{
            xtype:'fieldset',
            title: '分析说明',
            defaultType: 'htmleditor',
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            items :[{
                fieldLabel: '分析说明',
                afterLabelTextTpl: me.required,
                grow: true,  
                hideLabel:true,
                name: 'fxsm',
                allowBlank:false
            }]
        },{
            xtype:'fieldset',
            title: '鉴定意见',
            defaultType: 'htmleditor',
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            items :[{
                fieldLabel: '鉴定意见',
                afterLabelTextTpl: me.required,
                grow: true,  
                hideLabel:true,
                name: 'jdyj',
                allowBlank:false
            }]
        },{
			xtype:'grid',
			width:'90%',
			height:100,
			store:Ext.create('Ext.data.ArrayStore', {
				fields: [
				         {name: 'stdid'},
				         {name: 'stdname'},
				         {name: 'stdlevel' }
				         ],
//				         autoLoad:true,
				         proxy: {
				        	 type: 'jsonajax',
				        	 actionMethods:{read:'POST'},
				        	 params:{},
//				        	 url: 'declare/queryScore.do',
				        	 reader: {
				        		 type: 'json'
				        	 }
				         }
			}),
			columns:[{
				text     : '标准内容',
				dataIndex: 'stdname',
				width:400, 
				flex: 1,
				menuDisabled:true
			},{
				text     : '级数',
				dataIndex: 'stdlevel',
				width:100, 
				flex: 1,
				menuDisabled:true
			}]
		}];
		
		me.callParent(arguments);
	}
});