var rowEditing = Ext.create('Ext.grid.plugin.RowEditing',{
            pluginId:'rowEditing',
            saveBtnText: '保存', 
            cancelBtnText: "取消", 
            autoCancel: false, 
            clicksToEdit:1   //双击进行修改  1-单击   2-双击    0-可取消双击/单击事件
});
Ext.define('Rds.bacera.panel.BaceraFolicAcidExpressGrid', {
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
	    	labelWidth:50,
        	width:'20%',
	    	fieldLabel:'姓名'
	    });
		var ownperson = Ext.create('Ext.form.field.Text',{
	    	name:'ownperson',
	    	labelWidth:50,
        	width:'20%',
	    	fieldLabel:'归属人'
	    });
		var agent = Ext.create('Ext.form.field.Text',{
	    	name:'agent',
	    	labelWidth:60,
        	width:'20%',
	    	fieldLabel:'被代理人'
	    });
		var checkper = Ext.create('Ext.form.field.Text',{
			name:'checkper',
			labelWidth:50,
        	width:'20%',
			fieldLabel:'送检人'
		});
		var hospital = Ext.create('Ext.form.field.Text',{
			name:'hospital',
			labelWidth:60,
        	width:'20%',
			fieldLabel:'送检医院'
		});
		var folicAcid_starttime = new Ext.form.DateField({
			id:'folicAcid_starttime_express',
			name : 'folicAcid_starttime_express',
        	width:'20%',
			fieldLabel : '日 期  从',
			labelWidth : 50,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(
					new Date(),
					Ext.Date.DAY,-7),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('folicAcid_starttime_express').getValue();
	                var endDate = Ext.getCmp('folicAcid_endtime_express').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('folicAcid_starttime_express').setValue(endDate);
	                }
				}
			}
		});
		var expressnum = Ext.create('Ext.form.field.Text',{
			name:'expressnum',
			labelWidth:70,
        	width:'20%',
			fieldLabel:'快递单号'
		});
		var folicAcid_endtime = new Ext.form.DateField({
			id:'folicAcid_endtime_express',
			name : 'folicAcid_endtime_express',
        	width:'20%',
			labelWidth : 20,
			fieldLabel : ' 到 ',
			labelAlign : 'right',
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(), Ext.Date.DAY),
			listeners:{
				'select':function(){
					var start = Ext.getCmp('folicAcid_starttime_express').getValue();
	                var endDate = Ext.getCmp('folicAcid_endtime_express').getValue();
	                if (start > endDate) {
	                    Ext.getCmp('folicAcid_starttime_express').setValue(endDate);
	                }
				}
			}
		});
		var express_starttime = new Ext.form.DateField({
			name : 'express_starttime',
        	width:'20%',
			fieldLabel : '快递日期从',
			labelWidth : 70,
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
			fields:['id','num','name','age','testitems','diagnosis','checkper','agentname','ownpersonname',
			        'ownperson','reportif','payment','paragraphtime','account_type','reportif','hospital',
			        'expresstype','expressnum','recive','phonenum','date','remark','cancelif','expresstime',
			        'expressremark'],
	        start:0,
			limit:15,
			pageSize:15,
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url: 'bacera/folicAcid/queryallpage.do',
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
            			checkper:checkper.getValue(),
            			hospital:hospital.getValue(),
            			ownperson:ownperson.getValue(),
            			folicAcid_starttime:dateformat(folicAcid_starttime.getValue()),
            			folicAcid_endtime:dateformat(folicAcid_endtime.getValue()),
        				reportif:reportif.getValue(),
        				cancelif:2,
        				agent:agent.getValue(),
        				areacode:areacode.getValue(),
        				expressnum:expressnum.getValue(),
        				express_starttime:dateformat(express_starttime.getValue()),
        				express_endtime:dateformat(express_endtime.getValue())
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
//		me.bbar = {xtype: 'label',id:'totalBbarfolicAcid_express', text: '',
//				style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
		me.columns = [
		              { text: '案例编号',	dataIndex: 'num', menuDisabled:true, width : 100
	              },
	              { text: '快递单号',	dataIndex: 'expressnum', menuDisabled:true, width : 100,
	            	  editor:new Ext.form.NumberField()
	              },
	              { text: '快递类型',	dataIndex: 'expresstype', menuDisabled:true, width : 100,
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
	              },
	              { text: '快递备注',	dataIndex: 'expressremark', menuDisabled:true, width : 150,
                	  editor:'textfield'
                  },
                  { text: '账户类型',	dataIndex: 'account_type', menuDisabled:true, width : 150},
                  { text: '到款时间',	dataIndex: 'paragraphtime', menuDisabled:true, width : 80},
                  { text: '日期',	dataIndex: 'date', menuDisabled:true,width : 80},
                  { text: '姓名',	dataIndex: 'name', menuDisabled:true, width : 80},
                  { text: '年龄',	dataIndex: 'age', menuDisabled:true, width : 80},
                  { text: '病理诊断',	dataIndex: 'diagnosis', menuDisabled:true, width : 150},
                  { text: '检测项目',	dataIndex: 'testitems', menuDisabled:true, width : 150},
                  { text: '电话',	dataIndex: 'phonenum', menuDisabled:true, width : 80},
                  { text: '送检人',	dataIndex: 'checkper', menuDisabled:true, width : 80},
                  { text: '送检医院',	dataIndex: 'hospital', menuDisabled:true, width : 120},
                  { text: '市场归属人',	dataIndex: 'ownpersonname', menuDisabled:true, width : 100},
                  { text: '被代理人',	dataIndex: 'agentname', menuDisabled:true, width : 80},
                  { text: '归属地',	dataIndex: 'areaname', menuDisabled:true, width : 150},
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
	 		items:[search,name,ownperson,areacode,agent]
	 	},{
	 		style : {
	        borderTopWidth : '0px !important',
	        borderBottomWidth : '0px !important'
	 		},
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[reportif,folicAcid_starttime,folicAcid_endtime,checkper,hospital]
	 	},{
	 		style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '1px !important'
		 		},
		 		xtype:'toolbar',
		 		name:'search',
		 		dock:'top',
		 		items:[expressnum,express_starttime,express_endtime,{
	            	text:'查询',
	            	iconCls:'Find',
	            	handler:me.onSearch
	            }]
		 	}
		 	];
		me.store.load();
		me.callParent(arguments);
//		me.store.on("load",function(){
//			Ext.getCmp('totalBbarfolicAcid_express').setText("共 "+me.store.getCount()+" 条");
//		});
	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	},listeners : {
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
					case_type:'叶酸代谢能力检测',
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