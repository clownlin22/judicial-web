Ext.define('Rds.appraisal.panel.AppraisalInputStandardPanel', {
	extend : 'Ext.panel.Panel',
	config : {
		input:null,
		judicialcode:null
	},
	
	judicialtype:null,
	autoScroll:true,
	region:'east',
	split : true,
	width:350,
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
					input = input+store.getAt(i).get("content")+","+store.getAt(i).get("series")+"级;";
				}
				htmlEditor6.html(htmlEditor6.html()+input);
//				var fxsm = me.input.down("textarea[name=analysis_text]");
//				fxsm.setValue(fxsm.getValue()+input);
				this.up("panel").close();
			}
		}];
		
		me.items = [{
			xtype:'form',
			bodyPadding: '5 5 0',
			defaults: {
                anchor: '100%'
            },
			items:[ {
				xtype : "combo",
				fieldLabel : '鉴定类型',
				autoSelect : true,
				labelAlign: 'left',
				labelWidth : 60,
				mode: 'local', 
				name:"type",
//				allowBlank  : false,//不允许为空  
	            blankText   : "不能为空",//错误提示信息，默认为This field is required! 
		        forceSelection: true,   
		        emptyText:'请选择鉴定类型',  
		        triggerAction: 'all',
		        queryMode: 'local', 
				valueField :"key",  
				name:'appraisaltype',
				id:'appraisaltype',
		        displayField: "value",   
				store: Ext.create(
				        'Ext.data.Store',
				        {
				         fields:[
				                      {name:'key',mapping:'key',type:'string'},
				                      {name:'value',mapping:'value',type:'string'}
				          ],
				          proxy: {
				                type: 'jsonajax',
				                actionMethods:{read:'POST'},
				                url:'appraisal/info/queryCaseType.do',
				                params:{
				                	case_id:Ext.getCmp("appraisal_case_id").getValue()
				                },
				                reader: {
				                    type: 'json',
				                    root:'data',
				                }
				            },
				          autoLoad:true
				        }
				   ) 
			},
            {
                xtype: 'combo',
                labelWidth:60,
                name:'appraisalStandard',
                store: Ext.create("Ext.data.Store", {
                	fields : [{
									name : "standard_id",
									type : "string"
								},{
									name : "content",
									type : "string"
								},{
									name : "keyword",
									type : "string"
								},
								{
									name : "series",
									type : "string"
								},
								{
									name : "type_id",
									type : "string"
								}],
                             pageSize: 10,
                             proxy: {
                            	 type: "ajax",
                            	 actionMethods : {
                            		 read : 'POST'
                            	 },
                            	 url : "appraisal/info/queryStandard.do",
                            	 reader: {
                            		 type: "json"
                            	 }
                             },
                             listeners:{
                            	 'beforeload':function ( store, operation, eOpts ){
                            		 var c_appraisaltype = me.down("combo[name=appraisaltype]");
                            		 Ext.apply(store.proxy.extraParams, {
                            			 appraisaltype:c_appraisaltype.getValue(),
                            		 });
                            	 }
                             }
                }),
                fieldLabel: "鉴定标准",
                displayField: 'content',
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
                		var grid = me.down("grid");
                		var store = grid.getStore();
                		var record = records[0];
                		if(store.getCount()==0)
                		{
            				store.insert(0, record);
                		}else
                		{
                			var temp = 0 ;
                    		for(var i = 0 ; i < store.getCount() ;i ++)
                    		{
                    			if(record.get("standard_id")==store.getAt(i).get("standard_id"))
                    			{
                    				temp = 1;
                    				break;
                    				
                    			}
                    		}
                    		if(temp<1)
                    		{
                    			store.insert(0, record);
                    		}
                		}
                		
                	}
                }

            }
			,
            {
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
				         {name: 'standard_id'},
				         {name: 'content'},
				         {name: 'series' }
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
				dataIndex: 'content', 
				flex: 2,
				menuDisabled:true
			},{
				text     : '级数',
				dataIndex: 'series', 
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