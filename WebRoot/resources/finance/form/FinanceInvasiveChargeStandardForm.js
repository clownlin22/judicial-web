Ext.define("Rds.finance.form.FinanceInvasiveChargeStandardForm", {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;
		var partnerModel = Ext.create('Ext.data.Store', {
			fields:[
	                  {name:'hospital',mapping:'hospital',type:'string'},
	          ],
	          start:0,
				limit:100,
				pageSize:100,
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'bacera/invasivePre/queryallpageHospitalAreaName.do',
				reader : {
					type : 'json',
					root : 'data'
				}
			},  
			autoLoad : true,
			remoteSort : true
		});

		var hospital = Ext.create('Ext.form.ComboBox', {
//			autoSelect : true,
			editable:true,
			allowBlank:false,
			labelWidth : 65,
			columnWidth : 1,
			fieldLabel : '所属医院<span style="color:red">*</span>',
	        name:'hospital',
//	        forceSelection:true,
	        id:'hospital',
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "没有请选择'无'",
	        selectOnTab: true,
	        store: partnerModel,
	        fieldStyle: me.fieldStyle,
	        displayField:'hospital',
	        valueField:'hospital',
	        listClass: 'x-combo-list-small'
		});

		me.items = [{
			xtype : 'form',
			region : 'center',
			name : 'data',
			style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
			labelAlign : "right",
			bodyPadding : 10,
			defaults : {
				anchor : '100%',
				labelWidth : 100
			},
			border : false,
			autoHeight : true,
			items : [{xtype : 'hiddenfield',name:"id"},{
			                xtype: 'combo',
			                autoSelect : true,
			                allowBlank:false,
			        		fieldLabel: '案例项目<span style="color:red">*</span>',
			        		labelWidth : 65,
			        		emptyText : "请选择",
			        		triggerAction: 'all',
			     	        queryMode: 'local',
			     	        selectOnTab: true,
			     	        editable:false,  
			     	        forceSelection: true,
			     	        store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['NIPT(博奥)', '1'],['NIPT(成都博奥)', '5'],['NIPT(贝瑞)', '2'],['NIPT-plus(博奥)', '3'],['NIPT-plus(贝瑞)', '4'],['NIPT(精科)','6'],
		                    	         ['NIPT-Plus(精科)','7']]
							}),
			                displayField: 'Name',
			                valueField: 'Code',
			                name:'program_type',
			                listeners :{
	                        	"select" : function(combo, record, index){
	                        		  if(combo.getValue()=="6"){
	                        			  Ext.getCmp("areanameCharge").setVisible(false);
	                        			  Ext.getCmp("areanameCharge").allowBlank = true;
	                        		  }
	                        	}
	                        }
			            },{
	        		    	xtype : "combo",
	        				fieldLabel : '归属地区<span style="color:red">*</span>',
	        				labelWidth : 65,
	        				id:'areanameCharge',
	        				labelAlign : 'left',
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
	        						url:"judicial/dicvalues/getAreaInfo.do?area_code="+codeTemp,
	        						reader : {
	        							type : "json"
	        						}
	        					}
	        				}),
	        				displayField : 'value',
	        				valueField : 'id',
	        				typeAhead : false,
	        				hideTrigger : true,
	        				forceSelection: true,
	        				minChars : 2,
	        				matchFieldWidth : true,
	        				listConfig : {
	        					loadingText : '正在查找...',
	        					emptyText : '没有找到匹配的数据'
	        				},
	        				listeners: {
	        					'afterrender':function(){
	        						if("" != codeTemp)
	        						{
	        							this.store.load();
	        						}
	        					}
        					}
	        			},{border : false,
	        				xtype : 'fieldcontainer',
	        				layout : "column",
	        				items : [hospital
	        						]
	            			},
	        			{
							xtype : 'fieldset',
							title : '价格标准',
							layout : 'anchor',
							defaults : {
								anchor : '100%'
							},
							items : [{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ {
									xtype:'numberfield',
									fieldLabel : '样本价格',
									labelWidth : 65,
									labelAlign : 'right',
									columnWidth : .50,
									allowBlank : false,
									name:'samplePrice'
								},{
									xtype : 'numberfield',
									fieldLabel : '48小时',
									labelWidth : 65,
									columnWidth : .50,
									allowBlank : false,
									value:0,
									labelAlign : 'right',
									name : 'urgentPrice',
									maxLength : 1000,
									id:'urgentPrice'
								}]
							},{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ {
									xtype : 'numberfield',
									fieldLabel : '24小时',
									labelWidth : 65,
									value:0,
									columnWidth : .50,
									allowBlank : false,
									labelAlign : 'right',
									name : 'urgentPrice1',
									maxLength : 1000,
									id:'urgentPrice1'
								},{
									xtype : 'numberfield',
									fieldLabel : '8小时',
									labelWidth : 65,
									columnWidth : .50,
									allowBlank : false,
									value:0,
									labelAlign : 'right',
									name : 'urgentPrice2',
									maxLength : 1000,
									id:'urgentPrice2'
								}]
							}
							]
						},{
			    			xtype:"textarea",
			    			fieldLabel: '备注',
			    			labelWidth:100,
			    			fieldStyle:me.fieldStyle, 
			    			name: 'remark',
			    		} ]
		}];

		me.buttons = [{
					text : '保存',
					iconCls : 'Disk',
					handler : me.onSave
				}, {
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	onSave : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		values["areaname"]=Ext.getCmp('areanameCharge').getRawValue();
//		values["hospital"]=Ext.getCmp('hospital').getValue();
		if (form.isValid()) {
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({
				url : "finance/chargeStandard/saveInvasiveStandard.do",
				method : "POST",
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : values,
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					if (response.result == true) {
						Ext.MessageBox.alert("提示信息", response.message);
						me.grid.getStore().load();
						me.up("window").close();
					} else {
						Ext.MessageBox.alert("错误信息", response.message);
					}
				},
				failure : function() {
					Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
				}

			});
		}

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}
});