var rowEditing = Ext.create('Ext.grid.plugin.RowEditing',{
            pluginId:'rowEditing',
            saveBtnText: '保存', 
            cancelBtnText: "取消", 
            autoCancel: false, 
            clicksToEdit:1   //双击进行修改  1-单击   2-双击    0-可取消双击/单击事件
});
Ext.define('Rds.bacera.panel.BaceraMedScreenExpressGrid', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    selType: 'rowmodel',
    plugins: [rowEditing],
	initComponent : function() {
		var me = this;
		var search = Ext.create('Ext.form.field.Text',{
        	name:'num',
        	labelWidth:70,
        	width:'20%',
        	fieldLabel:'案例编号'
        });
		var mailStore = Ext.create('Ext.data.Store', {
			fields:['key','value'],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getMailModels.do',
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
		var name = Ext.create('Ext.form.field.Text',{
	    	name:'name',
	    	labelWidth:40,
        	width:'20%',
	    	fieldLabel:'姓名'
	    });
		var inspection = Ext.create('Ext.form.field.Text',{
	    	name:'inspection',
	    	labelWidth:40,
        	width:'20%',
	    	fieldLabel:'送检人'
	    });
		var expressnum = Ext.create('Ext.form.field.Text',{
			name:'expressnum',
			labelWidth:60,
        	width:'20%',
			fieldLabel:'快递单号'
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
		var program = Ext.create('Ext.form.ComboBox', {
			fieldLabel : '案例类型',
        	width:'20%',
			labelWidth : 60,
			editable : true,
			triggerAction : 'all',
			valueField :"key",  
	        displayField: "value", 
			store: Ext.create(
			        'Ext.data.Store',
			        {
			          model:'model',
			          proxy:{
			        	type: 'jsonajax',
			            actionMethods:{read:'POST'},
			            url:'bacera/medScreen/queryProgram.do',
			            reader:{
			              type:'json',
			              root:'data'
			            }
			          },
			          autoLoad:true,
			          remoteSort:true
			        }
			),
			mode : 'local',
			name : 'program'
		});
		var medScreen_starttime = new Ext.form.DateField({
			id:'medScreen_starttime_express',
			name : 'medScreen_starttime',
        	width:'20%',
			fieldLabel : '日 期  从',
			labelWidth : 70,
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('medScreen_starttime_express').getValue();
	                var endDate = Ext.getCmp('medScreen_endtime_express').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('medScreen_starttime_express').setValue(endDate);
	                }
				}
			}
		});
		var medScreen_endtime = new Ext.form.DateField({
			id:'medScreen_endtime_express',
			name : 'medScreen_endtime',
        	width:'20%',
			labelWidth : 40,
			fieldLabel : ' 到 ',
			labelAlign : 'left',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('medScreen_starttime_express').getValue();
	                var endDate = Ext.getCmp('medScreen_endtime_express').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('medScreen_starttime_express').setValue(endDate);
	                }
				}
			}
		});
		var express_starttime = new Ext.form.DateField({
			name : 'express_starttime',
        	width:'20%',
			fieldLabel : '快递日期从',
			labelWidth : 70,
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d'
		});
		var express_endtime = new Ext.form.DateField({
			name : 'express_endtime',
        	width:'20%',
			fieldLabel : ' 到 ',
			labelWidth : 20,
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d'
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
			labelWidth : 70,
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
//			forceSelection: true, 
			hideTrigger : true,
			minChars : 2,
			matchFieldWidth : true,
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
			}
		});
		me.store = Ext.create('Ext.data.Store',{
			fields:['id','num','name','program','date','ownpersonname','inspection',
			        'ownperson','agentname','reportif','remark','cancelif', 'receivables','payment',
			        'paid','discountPrice','paragraphtime','account_type',
			        'areaname','remarks','remittanceName','remittanceDate','expressnum','expresstype','recive','expresstime','expressremark'],
	        start:0,
			limit:15,
			pageSize:15,
	        proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/medScreen/queryallpage.do',
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
            		Ext.apply(me.store.proxy.params, {
            			num:search.getValue(),
            			name:name.getValue(),
            			ownperson:ownperson.getValue(),
            			medScreen_starttime:dateformat(medScreen_starttime.getValue()),
            			medScreen_endtime:dateformat(medScreen_endtime.getValue()),
        				reportif:reportif.getValue(),
        				cancelif:2,
        				agent:agent.getValue(),
        				areacode:areacode.getValue(),
        				program:program.getValue(),
        				expressnum:expressnum.getValue(),
        				express_starttime:dateformat(express_starttime.getValue()),
            			express_endtime:dateformat(express_endtime.getValue()),
            			inspection:inspection.getValue().trim()
        				});
        		}
            }
		});
		
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
		});
		me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录"
        });
//		me.bbar = {xtype: 'label',id:'totalBbarMedScreen_express', text: '',style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
		me.columns = [
	              { text: '案例编号',	dataIndex: 'num', menuDisabled:true, width:100,
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
	              { text: '快递单号',	dataIndex: 'expressnum', menuDisabled:true, width : 100,
	            	  editor:new Ext.form.NumberField()
	              },
	              { text: '快递类型',	dataIndex: 'expresstype', menuDisabled:true, width : 110,
	            	  editor:new Ext.form.ComboBox({
	          				autoSelect : true,
	          				editable:true,
	          		        name:'expresstype',
	          		        triggerAction: 'all',
	          		        queryMode: 'local', 
	          		        emptyText : "请选择",
	          		        selectOnTab: true,
	          		        store: mailStore,
	          		        maxLength: 50,
	          		        fieldStyle: me.fieldStyle,
	          		        displayField:'value',
	          		        valueField:'value',
	          		        listClass: 'x-combo-list-small'
	          			})
	              },
                  { text: '快递日期',	dataIndex: 'expresstime', menuDisabled:true, width : 110,
                	  renderer:Ext.util.Format.dateRenderer('Y-m-d'),
                      editor:new Ext.form.DateField({
                           format: 'Y-m-d'
                         })
	              },
                  { text: '收件人',	dataIndex: 'recive', menuDisabled:true, width : 120,
	            	  editor:'textfield'
	              },{ text: '快递备注',	dataIndex: 'expressremark', menuDisabled:true, width : 150,
                	  editor:'textfield'
                  },
                  { text: '到款日期',	dataIndex: 'paragraphtime', menuDisabled:true, width : 80},
                  { text: '财务备注',	dataIndex: 'remarks', menuDisabled:true, width : 80},
                  { text: '日期',	dataIndex: 'date', menuDisabled:true, width:80},
                  { text: '姓名',	dataIndex: 'name', menuDisabled:true, width:80},
                  { text: '送检人',	dataIndex: 'inspection', menuDisabled:true, width:80},
                  { text: '项目',	dataIndex: 'program', menuDisabled:true, width : 120},
                  { text: '市场归属人',	dataIndex: 'ownpersonname', menuDisabled:true, width : 100},
                  { text: '归属地',	dataIndex: 'areaname', menuDisabled:true, width : 200},
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
                  { text: '备注要求',	dataIndex: 'remark', menuDisabled:true, width : 300}
	              ];

		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[search,name,ownperson,areacode,inspection]
	 	},{
	 		style : {
	        borderTopWidth : '0px !important',
	        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[medScreen_starttime,medScreen_endtime,program,reportif,expressnum]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[express_starttime,express_endtime,{
	            	text:'查询',
	            	iconCls:'Find',
	            	handler:me.onSearch
	            }]
	 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[{
	 			text:'我是案例编号',
	 			iconCls:'Find',
	 			handler:me.caseCode
	 		}]
	 	}];
		me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbarMedScreen_express').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	caseCode:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要查看的案例编号!");
			return;
		};
		var num="";
		for(var i = 0 ; i < selections.length ; i ++)
		{
			num += selections[i].get("num")+";";
//			if(i>0&&i%8==0) num +="<br>";
		}
		Ext.Msg.alert("我是案例编号", num);
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	},
	listeners : {

		'beforeedit':function(editor, e,s){
			function afterEdit(e,s){  
          	  Ext.MessageBox.wait('正在保存','请稍后...');
           	Ext.Ajax.request({  
					url:"bacera/boneAge/saveExpress.do", 
					method: "POST",
					headers: { 'Content-Type': 'application/json' },
					jsonData: {
						id:s.record.data.id,
						num:s.record.data.num,
						expressnum:s.record.data.expressnum,
						recive:s.record.data.recive,
						expresstime:Ext.util.Format.date(s.record.data.expresstime, 'Y-m-d'),
						case_type:s.record.data.program,
						expresstype:s.record.data.expresstype,
						expressremark:s.record.data.expressremark
					}, 
					success: function (response, options) {  
						response = Ext.JSON.decode(response.responseText); 
		                 if (response.result==false) {  
			                 Ext.MessageBox.alert("错误信息", "修改失败，请重试或联系管理员!");
		                 }else{
		                	 Ext.MessageBox.alert("提示信息", "保存成功！");
		                 }
					},  
					failure: function () {
						Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
					}
		      	});
            }
			rowEditing.on('edit',afterEdit);
		}
	}
	
});