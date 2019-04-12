Ext.define('Rds.judicial.panel.JudicialInputStandardPanel', {
	extend : 'Ext.panel.Panel',
	config : {
		input:null,
		judicialcode:null
	},
	
	judicialtype:null,
	autoScroll:true,
	region:'east',
	split : true,
	width:300,
	title:'模版',
	initComponent : function() {
		var me = this;
		
		me.tools = [{
	    	type:'save',
			tooltip:'保存',
			handler:function(){
				var store = me.down("grid").getStore();
				var input = "";
				for(var i=0;i<store.getCount();i++){
					input = input+store.getAt(i).get("stdname");
				}
				var fxsm = me.input.down("htmleditor[name=fxsm]");
				fxsm.setValue(fxsm.getValue()+input);
				
				this.setDisabled(true);
			}
		}];
		
		me.items = [{
			xtype:'form',
			bodyPadding: '5 5 0',
			defaults: {
                anchor: '100%'
            },
			items:[{
                xtype: 'combo',
                labelWidth:60,
                name:'judicialtype',
                store: Ext.create("Ext.data.Store", {
                	fields: [
                	         { name: "judicialTypeCode", type: "string" },
                             { name: "judicialTypeName", type: "string" }],
                             pageSize: 10,
                             autoLoad: true,
                             proxy: {
                            	 type: "jsonajax",
                            	 url: "judicial/process/queryJudicialType.do",
                            	 reader: {
                            		 type: "json"
                            	 }
                             }
                }),
                fieldLabel: "鉴定类型",
                displayField: 'judicialTypeName',
                valueField:	'judicialTypeCode',
                typeAhead: false,
                listConfig: {
                    loadingText: '正在查找...',
                    emptyText: '没有找到匹配的数据'
                }

            },{
                xtype: 'combo',
                labelWidth:60,
                name:'judicialstandard',
                store: Ext.create("Ext.data.Store", {
                	fields: [
                	         { name: "stdid", type: "string" },
                             { name: "stdname", type: "string" },
                             { name: "stdlevel", type: "string" }],
                             pageSize: 10,
                             proxy: {
                            	 type: "ajax",
                            	 actionMethods : {
                            		 read : 'POST'
                            	 },
                            	 url: "judicial/standard/queryJudicialStandardComplate.do",
                            	 reader: {
                            		 type: "json"
                            	 }
                             },
                             listeners:{
                            	 'beforeload':function ( store, operation, eOpts ){
                            		 var c_judicialtype = me.down("combo[name=judicialtype]");
                            		 Ext.apply(store.proxy.extraParams, {
                            			 appraisalType:c_judicialtype.getValue(),
                            		 });
                            	 }
                             }
                }),
                fieldLabel: "鉴定标准",
                displayField: 'stdname',
                typeAhead: false,
                hideLabel: false,
                hideTrigger: true,
                minChars: 1,
                matchFieldWidth: true,
                listConfig: {
                    loadingText: '正在查找...',
                    emptyText: '没有找到匹配的数据'
                },
                listeners:{
                	'select':function( combo, records, eOpts ){
                		var record = records[0];
                		var grid = me.down("grid");
                		var store = grid.getStore();
                		store.insert(0, record);
                	}
                }

            },{
                xtype: 'fieldcontainer',
                fieldLabel: '附录',
                defaultType: 'checkboxfield',
                items: [{
                	boxLabel  : '有',
                	name      : 'topping',
                	inputValue: '1'
                }]
            }]
		},{
			xtype:'grid',
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
				flex: 1,
				menuDisabled:true
			},{
				text     : '级数',
				dataIndex: 'stdlevel', 
				flex: 1,
				menuDisabled:true
			},{
				xtype:"actioncolumn",
				text     : '操作',
				items:[{
					iconCls:'Delete',
					text:'删除',
					tooltip:"删除",
					handler:function(grid,rindex,cindex){
						grid.getStore().removeAt(rindex);
//				        var id=record.get("ID");
//				        download(id);
					}
				}], 
				flex: 1,
				menuDisabled:true
			}]
		}];
		
		me.callParent(arguments);
	}
});