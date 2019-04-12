/**
 * 报告录入界面，右侧面板
 */
Ext.define('Rds.judicial.panel.JudicialInputTplPanel', {
	extend : 'Ext.panel.Panel',
	config : {
		input:null
	},
	autoScroll:true,
	region:'east',
	split : true,
	width:300,
	title:'模版',
	initComponent : function() {
		var me = this;
		me.tools = [{
	    	type:'clear',
			tooltip:'清除',
			handler:function(){
				var form = me.down("form");
				form.getForm().reset();
				me.down("grid").getStore().removeAll();
			}
		},{
	    	type:'save',
			tooltip:'保存',
			handler:function(){
				var form = me.down("form");
				var values = form.getForm().getValues();
				var tgjc = me.input.down("htmleditor[name=tgjc]");
				tgjc.setValue(tgjc.getValue()+values.tgjc);
				
				var fxsm = me.input.down("htmleditor[name=fxsm]");
				fxsm.setValue(fxsm.getValue()+values.fxsm);
				
				var jdyj = me.input.down("htmleditor[name=jdyj]");
				jdyj.setValue(jdyj.getValue()+values.jdyj);
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
                name:'tpl',
                store: Ext.create("Ext.data.Store", {
                	fields: [
                	         { name: "tplid", type: "string" },
                             { name: "tplname", type: "string" },
                             { name: "tgjc", type: "string" },
                             { name: "fxsm", type: "string" },
                             { name: "jdyj", type: "string" }],
                             pageSize: 10,
//                             autoLoad: true,
                             proxy: {
//                            	 type: "ajax",
//                            	 actionMethods : {
//                                	 read : 'POST'
//                                 },
                            	 type: 'jsonajax',
                                 actionMethods:{read:'POST'},
                            	 url: "judicial/tpl/queryTplForComplate.do",
                            	 reader: {
                            		 type: "json"
                            	 }
                             }
                }),
                fieldLabel: "关键字",
                displayField: 'tplname',
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
                		var form = me.down("form");
                		//选择
                		var record = records[0];
                		
                		var src = Ext.create('Rds.judicial.model.JudicialTplModel', form.getForm().getValues());
                		var join = Ext.create('Rds.judicial.model.JudicialTplModel', record.getData());
                		var f = new ObjectUtil().joinObject(src,join);
                		form.loadRecord(f);
                		
                		var tplid = record.get("tplid");
                		
                		Ext.Ajax.request({  
                   	       url: 'judicial/tpl/queryTplComplateStandard.do',  
                   	       method: "POST",
                   	       headers: { 'Content-Type': 'application/json' },
                   	       jsonData: {tplid:tplid},

                   	       success: function (response, option) {  
    	               	    	response = Ext.JSON.decode(response.responseText);  
    	                        var store = me.down("grid").getStore();
//    	                        store.removeAll();
    	                        Ext.each(response,function(item){
    	                        	store.add(Ext.create('Rds.judicial.model.JudicialStandardModel', item));
    	                        });
    	                        
                   	       },  
                   	       failure: function () { 
                   	    	   Ext.Msg.alert("提示", "保存失败<br>没有捕获到异常"); 
                   	       }  
                      	});
                	}
                }

            },{
            	xtype:'textarea',
            	fieldLabel:'体格检查',
            	name:'tgjc',
            	labelAlign : 'top'
            },{
            	xtype:'textarea',
            	fieldLabel:'分析说明',
            	name:'fxsm',
            	labelAlign : 'top'
            },{
            	xtype:'textarea',
            	fieldLabel:'鉴定意见',
            	name:'jdyj',
            	labelAlign : 'top'
            }]
		},{
			xtype:'grid',
			store:Ext.create('Ext.data.ArrayStore', {
				fields: [
				         {name: 'stdname'},
				         {name: 'stdid'},
				         {name: 'stdlevel' }
				         ],
//				         autoLoad:true,
				         proxy: {
				        	 type: 'jsonajax',
				        	 actionMethods:{read:'POST'},
				        	 params:{},
				        	 url: 'judicial/tpl/queryTplComplateStandard.do',
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
					tooltip:"删除",
					handler:function(grid,rindex,cindex){
						grid.getStore().removeAt(rindex);
					}
				}], 
				flex: 1,
				menuDisabled:true
			}]
		}];
		me.callParent(arguments);
	}
});