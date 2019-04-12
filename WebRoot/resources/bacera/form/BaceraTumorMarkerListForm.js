var programStore = Ext.create('Ext.data.Store',{
    fields:['program_name'],
    autoLoad:true,
    proxy:{
    	type: 'jsonajax',
        actionMethods:{read:'POST'},
        params:{ program_type:'肿瘤标志物'},
        url:'bacera/program/queryall.do',
        reader:{
          type:'json',
        },
        writer: {
            type: 'json'
       }
      },
      remoteSort:true
});
Ext.define('Rds.bacera.form.BaceraTumorMarkerListForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var date = new Ext.form.DateField({
					name : 'date',
					fieldLabel : '日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
					labelWidth : 50,
					style:'margin-left:10px',
					labelAlign : 'left',
					columnWidth : .50,
					afterLabelTextTpl : me.required,
					emptyText : '请选择日期',
					format : 'Y-m-d',
					allowBlank:false, //不允许为空
					blankText:"不能为空", //错误提示信息，默认为This field is required! 
					value:new Date()
				});
		me.items = [{
        	xtype:'form',
        	region:'center',
        	name:'data',
            style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
            labelAlign:"right",
            bodyPadding: 10,
            defaults: {
                anchor: '100%',
                labelWidth: 80
            },
            border:false,
            autoHeight:true,
            items: [{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [	
				    		{
				    			xtype:"textfield",
				    			fieldLabel: 'id',
				    			labelWidth:80,
				    			readOnly:true,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'id',
				    			id:'tumorId',
				    			hidden:true
				    		},{
				    			xtype:"textfield",
				    			fieldLabel: '案例编号<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
				    			labelWidth:65,
				    			columnWidth : .50,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'num',
				    			allowBlank:false, //不允许为空
				    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
				    			maxLength: 18
				    		},date
				   ]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
					xtype : 'combo',
					fieldLabel : '案例归属<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
					labelWidth : 65,
					name : 'ownperson',
					emptyText:'(姓名/地区首字母)：如小红(xh)',
					store :Ext.create("Ext.data.Store",
							{
						 fields:[
				                  {name:'id',mapping:'id',type:'string'},
				                  {name:'ascription',mapping:'ascription',type:'string'}
				          ],
						pageSize : 10,
						autoLoad: false,
						proxy : {
							type : "ajax",
							url:"finance/chargeStandard/getAscriptionPer.do?id="+ownpersonTemp,
							reader : {
								type : "json"
							}
						}
					}),
					displayField : 'ascription',
					valueField : 'id',
	    			columnWidth : 1,
					typeAhead : false,
					hideTrigger : true,
					forceSelection: true,
					allowBlank:false, //不允许为空
					minChars : 2,
					matchFieldWidth : true,
					listConfig : {
						loadingText : '正在查找...',
						emptyText : '没有找到匹配的数据'
					},
				listeners: {
					'afterrender':function(){
						if("" != ownpersonTemp)
						{
							this.store.load();
						}
					}
					}
				} ]
    			
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
			    			xtype:"textfield",
			    			fieldLabel: '姓名<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			    			labelWidth:65,
			    			fieldStyle:me.fieldStyle, 
			    			name: 'name',
			    			columnWidth : .50,
			    			maxLength: 10,
			    			allowBlank:false, //不允许为空
			    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    		        },
		    			{
	    		        	columnWidth : .50,
		    				xtype: 'combo',
		        			autoSelect : true,
		        			editable:false,
		        			labelWidth : 50,
		        			style:'margin-left:10px',
		        			fieldLabel:'性别',
		        	        name:'sex',
		        	        triggerAction: 'all',
		        	        queryMode: 'local', 
		        	        emptyText : "请选择",
		        	        selectOnTab: true,
		        	        store: Ext.create('Ext.data.Store', { 
		                    	fields: ['id', 'name'], 
		                    	data : [{"id":"1","name":"男"},
		                    	         {"id":"2","name":"女"}
		                    	      
		                    	]
		                    }),
		        	        fieldStyle: me.fieldStyle,
		        	        displayField:'name',
		        	        valueField:'id',
		        	        listClass: 'x-combo-list-small'
		    			}]
    			
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [ new Ext.form.ComboBox({
					autoSelect : true,
					editable:true,
					allowBlank:false,
					labelWidth : 65,
	    			columnWidth : .50,
					fieldLabel : '检测项目<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
				    name:'program',
				    triggerAction: 'all',
				    queryMode: 'local', 
				    emptyText : "请选择",
				    selectOnTab: true,
			        forceSelection:true,
				    store: programStore,
				    fieldStyle: me.fieldStyle,
				    displayField:'program_name',
				    valueField:'program_name',
				    listClass: 'x-combo-list-small',
				}),
//				{
//	                xtype : "combo",
//	                fieldLabel : '检测项目<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
//	                labelWidth : 65,
//	                mode: 'remote',
//	                columnWidth : .50,
//	                editable : true,
//	                emptyText:'请选择检测项目',
//	                name:'program',
//	                valueField :"Name",
//	                displayField: "Name",
//	    			allowBlank:false, //不允许为空
//	    			blankText:"不能为空",
//	                store: new Ext.data.ArrayStore(
//							{
//								fields : [
//										'Name',
//										'Code' ],
//								data : [
//										[
//												'CEA',
//												'CEA' ]]
//							}),
//					    			},
							         {
						                xtype : "combo",
						                fieldLabel : '送检人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
						                labelWidth : 50,
						                mode: 'remote',
						                columnWidth : .50,
						                editable : true,
						                emptyText:'请选择送检人',
						                name:'inspection',
						        		style:'margin-left:10px',
						                valueField :"Name",
						                displayField: "Name",
						    			allowBlank:false, //不允许为空
						    			blankText:"不能为空",
						                store: new Ext.data.ArrayStore(
												{
													fields : [
															'Name',
															'Code' ],
													data : [
															[
																	'匡氪衡',
																	'匡氪衡' ]]
												}),
					    				
					    				
					    				
					    				
//							        	 xtype:"textfield",
//						        		style:'margin-left:10px',
//						    			fieldLabel: '送检人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
//						    			labelWidth:50,
//						    			columnWidth : .50,
//						    			fieldStyle:me.fieldStyle, 
//						    			name: 'inspection',
//						    			maxLength: 50,
//						    			allowBlank:false, //不允许为空
//						    			blankText:"不能为空"
						    			}
			    			]
    			
    		},{
				xtype:"textarea",
    			fieldLabel: '备注要求',
    			labelWidth:65,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark',
    			height:80,
    			maxLength: 200,
    		},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"numberfield",
	    			fieldLabel: '应收款项<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
	    			labelWidth:70,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'receivables',
	    			id: 'receivables',
	    			maxLength: 20,
	    			allowBlank:false, //不允许为空
        			blankText:"不能为空", //错误提示信息，默认为This field is required!
	    		}]
			}]
		}];
		
		me.buttons = [{
			text:'保存',
			iconCls:'Disk',
			handler:me.onSave
		},{
			text:'取消',
			iconCls:'Cancel',
			handler:me.onCancel
		}]
		me.callParent(arguments);
	},
	//验证后保存
	onSave:function(){
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if(form.isValid())
		{
         	Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:"bacera/tumorMarker/save.do", 
				method: "POST",
				headers: { 'Content-Type': 'application/json' },
				jsonData: values, 
				success: function (response, options) {  
					response = Ext.JSON.decode(response.responseText); 
	                 if (response.result == true) {  
	                 	Ext.MessageBox.alert("提示信息", response.message);
	                 	me.grid.getStore().load();
	                 	me.up("window").close();
	                 }else { 
	                 	Ext.MessageBox.alert("错误信息", "更新失败！请联系管理员；<br>"+response.message);
	                 } 
				},  
				failure: function () {
					Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
				}
	    	       
	      	});
		}
	
		
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	},
	listeners : {
		'beforerender' : function() {
			programStore.load();
		},
		'afterrender' : function() {
			if(confirm_flagTemp == '2')
				Ext.getCmp("receivables").readOnly=true;
		}
	}
	
});