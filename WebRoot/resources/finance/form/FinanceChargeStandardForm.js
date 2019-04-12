Ext.define("Rds.finance.form.FinanceChargeStandardForm", {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;
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
			        		fieldLabel: '案例来源',
			        		labelWidth : 60,
			        		emptyText : "请选择",
			        		triggerAction: 'all',
			     	        queryMode: 'local',
			     	        selectOnTab: true,
			     	        editable:false,  
			     	        forceSelection: true,
			     	        store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['实体渠道', '0'], ['电子渠道', '1']]
							}),
			                displayField: 'Name',
			                valueField: 'Code',
			                name:'source_type',
			                listeners :{
	                        	"select" : function(combo, record, index){
	                        		  if(combo.getValue()=="1"){
	                        			  Ext.getCmp("specialPirce").setValue("200");
	                        			  Ext.getCmp("specialPirce1").setValue("500");
	                        			  Ext.getCmp("specialPirce2").setValue("700");
	                        			  Ext.getCmp("urgentPrice").setValue("500");
	                        			  Ext.getCmp("urgentPrice1").setValue("1000");
	                        			  Ext.getCmp("urgentPrice2").setValue("2000");
	                        		  }else
	                        		  {
	                        			  Ext.getCmp("specialPirce").setValue("0");
	                        			  Ext.getCmp("specialPirce1").setValue("300");
	                        			  Ext.getCmp("specialPirce2").setValue("300");
	                        			  Ext.getCmp("urgentPrice").setValue("300");
	                        			  Ext.getCmp("urgentPrice1").setValue("300");
	                        			  Ext.getCmp("urgentPrice2").setValue("300");
	                        		  }
	                        	}
	                        }
			            },{
			                xtype: 'combo',
			                autoSelect : true,
			                allowBlank:false,
			        		fieldLabel: '案例项目',
			        		labelWidth : 60,
			        		emptyText : "请选择",
			        		triggerAction: 'all',
			     	        queryMode: 'local',
			     	        selectOnTab: true,
			     	        editable:false,  
			     	        forceSelection: true,
			     	        store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['亲子鉴定', '0'],['亲缘鉴定', '1'],['同胞鉴定', '2']]
							}),
			                displayField: 'Name',
			                valueField: 'Code',
			                name:'program_type',
			                listeners :{
	                        	"select" : function(combo, record, index){
	                        		  if(combo.getValue()=="0"){
	                        			  Ext.getCmp("typeProgram").setRawValue("");
	                        			  Ext.getCmp("typeProgram").setDisabled(false);
	                        		  }else
	                        		  {
//	                        			  Ext.getCmp("typeProgram").setDisabled(true);
	                        		  }
	                        	}
	                        }
			            },{
	                        xtype: 'combo',
	                        autoSelect : true,
	                        allowBlank:false,
	                		fieldLabel: '项目类型',
	                		labelWidth : 60,
	                		emptyText : "请选择",
	                		triggerAction: 'all',
	             	        queryMode: 'local',
	             	        selectOnTab: true,
	             	        editable:false,  
	             	        forceSelection: true,
	             	        store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['司法鉴定', '0'], ['医学鉴定', '1']]
							}),
	                        displayField: 'Name',
	                        valueField: 'Code',
	                        name:'type',
	                        id:'typeProgram'
	                    },{
	        				xtype : 'combo',
	        				fieldLabel : '归属人',
	        				labelWidth : 60,
	        				name : 'userid',
	        				id:"useridCharge",
	        				emptyText:'(人员首字母)：如小明(xm)',
	        				store :Ext.create("Ext.data.Store",{
	        					   fields:[
	        							{name:'key',mapping:'key',type:'string'},
	        							{name:'value',mapping:'value',type:'string'}
	        			          ],
	        					pageSize : 20,
	        					autoLoad: false,
	        					proxy : {
	        						type : "ajax",
	        						url:'judicial/dicvalues/getUsersId.do?userid='+personTemp,
	        						reader : {
	        							type : "json"
	        						}
	        					}
	        				}),
	        				displayField : 'value',
	        				valueField : 'key',
	        				columnWidth : 1,
	        				typeAhead : false,
	        				forceSelection: true,
	        				hideTrigger : true,
	        				minChars : 2,
	        				matchFieldWidth : true,
	        				listConfig : {
	        					loadingText : '正在查找...',
	        					emptyText : '没有找到匹配的数据'
	        				},
	        				listeners: {
	        					'afterrender':function(){
		        						if("" != personTemp)
		        						{
		        							this.store.load();
		        						}
		        					}
	        					}
	        			},{
	        		    	xtype : "combo",
	        				fieldLabel : '归属地区',
	        				labelWidth : 60,
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
									xtype : 'numberfield',
									fieldLabel : '单亲价格',
									labelWidth : 65,
									labelAlign : 'left',
									columnWidth : .33,
									allowBlank : false,
									name : 'singlePrice',
									maxLength : 1000
								},{
									xtype : 'numberfield',
									fieldLabel : '双亲价格',
									labelWidth : 60,
									labelAlign : 'right',
									columnWidth : .33,
									allowBlank : false,
									name : 'doublePrice',
									maxLength : 1000
								},{
									xtype:'numberfield',
									fieldLabel : '加样价格',
									labelWidth : 60,
									labelAlign : 'right',
									columnWidth : .33,
									allowBlank : false,
									name:'samplePrice'
								}]
							},{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ {
									xtype : 'numberfield',
									fieldLabel : '差价',
									labelWidth : 65,
									columnWidth : .33,
									value:'0',
									allowBlank : false,
									labelAlign : 'left',
									name : 'gapPrice',
									maxLength : 1000
								},{
									xtype : 'numberfield',
									fieldLabel : '特殊价格1',
									labelWidth : 88,
									value:'0',
									labelAlign : 'right',
									columnWidth : .50,
									allowBlank : false,
									name : 'specialPirce',
									maxLength : 1000,
									id:'specialPirce'
								}]
							},{

								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ {
									xtype : 'numberfield',
									fieldLabel : '特殊价格2',
									labelWidth : 65,
									value:'300',
									columnWidth : .50,
									allowBlank : false,
									name : 'specialPirce1',
									maxLength : 1000,
									id:'specialPirce1'
								},{
									xtype : 'numberfield',
									fieldLabel : '特殊价格3',
									labelWidth : 88,
									value:'300',
									labelAlign : 'right',
									columnWidth : .50,
									allowBlank : false,
									name : 'specialPirce2',
									maxLength : 1000,
									id:'specialPirce2'
								}]
							
							},{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ {
									xtype : 'numberfield',
									fieldLabel : '48小时',
									labelWidth : 60,
									columnWidth : .33,
									value:'300',
									allowBlank : false,
									labelAlign : 'left',
									name : 'urgentPrice',
									maxLength : 1000,
									id:'urgentPrice'
								},{
									xtype : 'numberfield',
									fieldLabel : '24小时',
									labelWidth : 60,
									columnWidth : .33,
									value:'300',
									allowBlank : false,
									labelAlign : 'right',
									name : 'urgentPrice1',
									maxLength : 1000,
									id:'urgentPrice1'
								},{
									xtype : 'numberfield',
									fieldLabel : '8小时',
									labelWidth : 60,
									columnWidth : .33,
									value:'300',
									allowBlank : false,
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
		values["username"]= Ext.getCmp('useridCharge').getRawValue().split('(')[0];
		values["areaname"]=Ext.getCmp('areanameCharge').getRawValue();
		if("" == values["username"] && ""== values["areaname"])
		{
			Ext.MessageBox.alert("错误信息", "归属人和地区不能一个都不选吧？");
			return;
		}
		if (form.isValid()) {
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({
				url : "finance/chargeStandard/saveStandard.do",
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