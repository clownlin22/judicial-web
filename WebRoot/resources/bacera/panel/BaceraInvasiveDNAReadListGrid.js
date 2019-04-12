var invasiceDNA=""
Ext.define('Rds.bacera.panel.BaceraInvasiveDNAReadListGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
	initComponent : function() {
		var me = this;
		var search = Ext.create('Ext.form.field.Text',{
        	name:'num',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'案例编号'
        });
		var fatherName = Ext.create('Ext.form.field.Text',{
	    	name:'fatherName',
	    	labelWidth:70,
        	width:'20%',
	    	fieldLabel:'父亲姓名'
	    });
		var motherName = Ext.create('Ext.form.field.Text',{
	    	name:'motherName',
	    	labelWidth:60,
        	width:'20%',
	    	fieldLabel:'母亲姓名'
	    });
		var ownperson = Ext.create('Ext.form.field.Text',{
	    	name:'ownperson',
	    	labelWidth:60,
        	width:'20%',
	    	fieldLabel:'归属人'
	    });
		var agent = Ext.create('Ext.form.field.Text',{
	    	name:'agent',
	    	labelWidth:60,
        	width:'20%',
	    	fieldLabel:'被代理人'
	    });
		var fatherType = Ext.create('Ext.form.field.Text',{
	    	name:'fatherType',
	    	labelWidth:60,
        	width:'20%',
	    	fieldLabel:'父本类型'
	    });
		var client = Ext.create('Ext.form.field.Text',{
	    	name:'client',
	    	labelWidth:60,
        	width:'20%',
	    	fieldLabel:'委托方'
	    });
		var invasive_starttime = new Ext.form.DateField({
			name : 'invasiveDNA_starttime',
        	width:'20%',
			fieldLabel : '受理日期从',
			labelWidth : 70,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7)
		});
		var invasive_endtime = new Ext.form.DateField({
			name : 'invasiveDNA_endtime',
        	width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			maxValue : new Date(),
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var consigning_starttime = new Ext.form.DateField({
			name : 'consigning_starttime',
        	width:'20%',
			fieldLabel : '委托日期从',
			labelWidth : 70,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d'
		});
		var consigning_endtime = new Ext.form.DateField({
			name : 'consigning_endtime',
        	width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY)
		});
		var reportif=new Ext.form.field.ComboBox({
			fieldLabel : '是否发报告',
        	width:'20%',
			labelWidth : 70,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部',0],['是',1 ],['否',2 ] ]
					}),
			value : '',
			mode : 'local',
			name : 'reportif',
			value: 0
		});
		var cancelif=new Ext.form.field.ComboBox({
			fieldLabel : '是否作废',
        	width:'20%',
			labelWidth : 60,
			editable : false,
			triggerAction : 'all',
			displayField : 'Name',
			labelAlign : 'left',
			valueField : 'Code',
			store : new Ext.data.ArrayStore(
					{
						fields : ['Name','Code' ],
						data : [['全部',0],['是',1 ],['否',2 ] ]
					}),
			value : '',
			mode : 'local',
			name : 'cancelif',
			value: 0
		});
		var areacode= new Ext.form.field.ComboBox({
			fieldLabel : '归属地',
			labelWidth : 60,
        	width:'20%',
			name : 'areacode',
			emptyText:'检索方式：如朝阳区(cy)',
			store : Ext.create("Ext.data.Store",{
				 fields:[
		                  {name:'key',mapping:'key',type:'string'},
		                  {name:'value',mapping:'value',type:'string'},
		                  {name:'name',mapping:'name',type:'string'},
		                  {name:'id',mapping:'id',type:'string'},
		          ],
				pageSize : 10,
				// autoLoad: true,
				proxy : {
					type : "ajax",
					url:"judicial/dicvalues/getAreaInfo.do",
					reader : {
						type : "json"
					}
				}
			}),
			displayField : 'value',
			valueField : 'id',
			typeAhead : false,
			hideTrigger : true,
			minChars : 2,
			matchFieldWidth : true,
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
			}
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','consigningDate','fatherName','motherName','fatherType','gestational','client','date','ownpersonname',
			        'ownperson','agentname','reportif','remark','cancelif','expresstype','receivables',
			        'areaname'],
	        start:0,
			limit:15,
			pageSize:15,
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/invasiveDNA/queryallpage.do',
                params:{
                },
                reader: {
                    type: 'json',
                    root:'data',
                    totalProperty:'total'
                }
            },
            listeners:{
            	'beforeload':function(ds, operation, opt){
            		me.getSelectionModel().clearSelections();
            		invasiceDNA = "num=" + search.getValue().trim() +"&ownperson="
    				+ ownperson.getValue().trim() + "&invasive_starttime="
    				+ dateformat(invasive_starttime.getValue())
    				+ "&invasive_endtime="
    				+ dateformat(invasive_endtime.getValue())
    				+ "&reportif=" + reportif.getValue()
    				+ "&cancelif=" + cancelif.getValue() + "&agent="
    				+ agent.getValue().trim() + "&areacode="
    				+ areacode.getValue()+"&fatherName="
    				+ fatherName.getValue()+"&motherName="
    				+ motherName.getValue()+"&fatherType="
    				+ fatherType.getValue() +"&consigning_starttime="
    				+ dateformat(consigning_starttime.getValue()) +"&consigning_endtime="
    				+ dateformat(consigning_endtime.getValue())+"&client="
    				+ client.getValue().trim();
            		Ext.apply(me.store.proxy.params, {num:search.getValue(),ownperson:ownperson.getValue(),
            			invasive_starttime:dateformat(invasive_starttime.getValue()),
            			invasive_endtime:dateformat(invasive_endtime.getValue()),
        				reportif:reportif.getValue(),cancelif:cancelif.getValue(),
        				agent:agent.getValue(),
        				areacode:areacode.getValue(),
        				fatherName:fatherName.getValue(),
        				motherName:motherName.getValue(),
        				fatherType:fatherType.getValue(),
        				consigning_starttime:dateformat(consigning_starttime.getValue()),
        				consigning_endtime:dateformat(consigning_endtime.getValue()),
        				client:client.getValue().trim()
        				});
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
	              { text: '案例编号',	dataIndex: 'num', menuDisabled:true, width : 100,
	                	  renderer : function(value, cellmeta,
									record, rowIndex, columnIndex,
									store) {
								var isnull= record.data["cancelif"];
								if (isnull == 1) {
									return "<div style=\"text-decoration: line-through;color: red;\">"
											+ value + "</div>"
								} else {
									return value;
								}

							}
	              },
                  { text: '受理日期',	dataIndex: 'date', menuDisabled:true, width : 80},
                  { text: '父亲姓名',	dataIndex: 'fatherName', menuDisabled:true, width : 80},
                  { text: '母亲姓名',	dataIndex: 'motherName', menuDisabled:true, width : 80},
                  { text: '父本类型',	dataIndex: 'fatherType', menuDisabled:true, width : 80},
                  { text: '母本孕周',	dataIndex: 'gestational', menuDisabled:true, width : 80},
                  { text: '委托方',	dataIndex: 'client', menuDisabled:true, width : 80},
                  { text: '委托日期',	dataIndex: 'consigningDate', menuDisabled:true, width : 100},
                  { text: '归属地',	dataIndex: 'areaname', menuDisabled:true, width : 150},
                  { text: '市场归属人',	dataIndex: 'ownpersonname', menuDisabled:true, width : 100},
                  { text: '被代理人',	dataIndex: 'agentname', menuDisabled:true, width : 80},
                  { text: '是否发报告',	dataIndex: 'reportif', menuDisabled:true, width : 100,
	            	  renderer : function(value) {
							switch (value) {
							case "1":
								return "是";
								break;
							default:
								return "<span style='color:red'>否</span>";
							}
						}
                	  
                  },
                  { text: '应收款项',	dataIndex: 'receivables', menuDisabled:true, width : 100},
                  { text: '备注',	dataIndex: 'remark', menuDisabled:true, width:300,
						renderer: function(value, meta, record) {
                            meta.style = 'overflow:auto;padding: 3px 6px;text-overflow: ellipsis;white-space: nowrap;white-space:normal;line-height:20px;';   
                            return value;   
                       }
                  }
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[search,client,ownperson,areacode,agent]
	 	},{
	 		style : {
	        borderTopWidth : '0px !important',
	        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[fatherName,motherName,fatherType,reportif,cancelif,]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[invasive_starttime,invasive_endtime,consigning_starttime,consigning_endtime,{
	            	text:'查询',
	            	iconCls:'Find',
	            	handler:me.onSearch
	            }]
		 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[{
	 			text:'导出',
	 			iconCls:'Application',
	 			handler:me.invasiveDNAExport
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	invasiveDNAExport:function(){
		window.location.href = "bacera/invasiveDNA/exportInvasiveInfo.do?"+invasiceDNA ;
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	}
	
});