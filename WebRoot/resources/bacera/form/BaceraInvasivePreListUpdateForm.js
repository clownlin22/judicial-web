Ext.define('Rds.bacera.form.BaceraInvasivePreListUpdateForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var date = new Ext.form.DateField({
					name : 'date',
					fieldLabel : '日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
					labelWidth : 65,
					columnWidth : .50,
					labelAlign : 'left',
					afterLabelTextTpl : me.required,
					emptyText : '请选择日期',
					format : 'Y-m-d',
					allowBlank:false, //不允许为空
					blankText:"不能为空", //错误提示信息，默认为This field is required! 
					value:new Date()
				});
		var areacode= new Ext.form.field.ComboBox({
			fieldLabel : '归属地区<span style="color:red">*</span>',
			labelWidth : 65,
			columnWidth : .50,
			style:'margin-left:10px',
			id:'areacodeu',
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
					url:"judicial/dicvalues/getAreaInfo.do?area_code="+areacodeTemp,
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
			value:'',
//			listConfig : {
//				loadingText : '正在查找...',
//				emptyText : '没有找到匹配的数据'
//			}, 
			listeners :{
				'afterrender':function(){
					if("" != areacodeTemp)
					{
						this.store.load();
					}
				},
               	"select" : function(combo, record, index){
               		console.log(combo.getRawValue());
               		var areaname=combo.getRawValue();
               		Ext.Ajax.request({
           				url : "bacera/invasivePre/getHospital.do?areaname="+areaname,
           				method : "get",
           				headers : {
           					'Content-Type' : 'application/json'
           				},
//           				jsonData : {report_model:combo.getValue()},
           				success : function(response, options) {
           					response = Ext.JSON.decode(response.responseText);
        					if (response.result == true) {
        						hospital_name.setValue(response.message);
           					}
           				},
           				failure : function() {
           					Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
           				}

           			});
//               		partnerModel.load();
               	}
               },
			     editable:true
			});
	
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
				params : {
					type : '',
				},
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : false,
			remoteSort : true
		});
		var hospital_name = Ext.create('Ext.form.ComboBox', {
			fieldLabel: '所属医院<span style="color:red">*</span>',
            labelWidth : 65,
            editable:true,
            allowBlank:false,
            triggerAction: 'all',
            displayField: 'hospital',
            style:'margin-left:10px',
            columnWidth : .50,
	        emptyText : "没有请选择'无'",
            valueField: 'hospital',
            store: partnerModel,
            mode: 'local',
            name: 'hospital',
            id:'hospital'
		
		});
//		var hospital_name = Ext.create('Ext.form.ComboBox', {
//			autoSelect : true,
//			editable:true,
//			allowBlank:false,
//			labelWidth : 65,
//			columnWidth : .50,
//			fieldLabel : '所属医院',
//			labelAlign : 'right',
//	        name:'hospital',
//	        forceSelection:true,
//	        triggerAction: 'all',
//	        queryMode: 'local', 
//	        emptyText : "请选择",
//	        selectOnTab: true,
//	        store: partnerModel,
//	        style:'margin-left:10px',
//	        fieldStyle: me.fieldStyle,
//	        displayField:'hospital',
//	        valueField:'hospital',
//	        listClass: 'x-combo-list-small'
//		});

		var sampledate = new Ext.form.DateField({
			name : 'sampledate',
			fieldLabel : '采样日期',
			labelWidth : 65,
			columnWidth : .50,
			style:'margin-left:10px',
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			value : Ext.Date.add(new Date(),
					 Ext.Date.DAY),
			allowBlank:false, //不允许为空
			blankText:"不能为空", //错误提示信息，默认为This field is required! 
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
    			xtype:"textfield",
    			fieldLabel: 'id',
    			labelWidth:80,
    			readOnly:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'id',
    			id:'invasicerPerId',
    			hidden:true
    		},{
	        	 xtype:"textfield",
	    			fieldLabel: 'task_id',
	    			labelWidth:60,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'task_id',
	    			readOnly : true,
	    			maxLength: 50,
	    		    hidden:true
	    	},{
	        	 xtype:"textfield",
	    			fieldLabel: 'task_def_key',
	    			labelWidth:60,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'task_def_key',
	    			readOnly : true,
	    			maxLength: 50,
	    		    hidden:true
	    	},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [	
				    		{
				    			xtype:"textfield",
				    			fieldLabel: '案例编号<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
				    			labelWidth:65,
				    			columnWidth : .50,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'num',
				    			readOnly:true,
				    			allowBlank:false, //不允许为空
				    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
				    			maxLength: 20,
				    			validator : function(value) {
				    				if(Ext.getCmp('invasicerPerId').getValue())
				    				{
				    					return true;
			    					}
									var result = false;
									Ext.Ajax.request({
												url : "bacera/invasivePre/exsitCaseCode.do",
												method : "POST",
												async : false,
												headers : {
													'Content-Type' : 'application/json'
												},
												jsonData : {
													num : value
												},
												success : function(
														response,
														options) {
													response = Ext.JSON
															.decode(response.responseText);
													if (!response) {
														result = "此案例编号已存在";
													} else {
														result = true;
													}
												},
												failure : function() {
													Ext.Msg
															.alert(
																	"提示",
																	"网络故障<br>请联系管理员!");
												}
											});
									return result;
								},
				    		}, {
					        	 xtype:"textfield",
					    			fieldLabel: '优惠码',
					    			labelWidth:65,
					    			style:'margin-left:10px',
					    			fieldStyle:me.fieldStyle, 
					    			columnWidth : .50,
					    			name: 'confirm_code',
					    			maxLength: 50
					    			}]
	    	},
	    {
				    			border : false,
				    			style:'margin-top:10px;',
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ {
									xtype : 'combo',
									fieldLabel : '归属人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
									labelWidth : 65,
									columnWidth : .50,
									labelAlign : 'left',
									name : 'ownperson',
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
											url:'judicial/dicvalues/getUsersId.do?userid='+ownpersonTemp,
											reader : {
												type : "json"
											}
										}
									}),
									displayField : 'value',
									valueField : 'key',
									typeAhead : false,
									allowBlank:false, //不允许为空
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
											if("" != ownpersonTemp)
											{
												this.store.load();
											}
										}
										}
								},areacode
								]
					
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [date,sampledate]
    			
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
			    			maxLength: 50,
			    			columnWidth : .50,
			    			allowBlank:false, //不允许为空
			    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    		        },hospital_name]
    			
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [
				         {
				        	 xtype:"textfield",
			    			fieldLabel: '条码<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			    			labelWidth:65,
			    			columnWidth : .50,
			    			fieldStyle:me.fieldStyle, 
			    			name: 'code',
			    			maxLength: 50,
			    			allowBlank:false, //不允许为空
			        		blankText:"不能为空", //错误提示信息，默认为This field is required!
			    			},
			    			{
			    				columnWidth : .50,
			    				xtype: 'combo',
			        			autoSelect : true,
			        			editable:false,
			        			labelWidth : 65,
			        			fieldLabel:'类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			        	        name:'inspectionunit',
			        	        allowBlank:false, //不允许为空
			        			blankText:"不能为空", //错误提示信息，默认为This field is required!
			        	        triggerAction: 'all',
			        	        style:'margin-left:10px',
			        	        queryMode: 'local', 
			        	        emptyText : "请选择",
			        	        selectOnTab: true,
			        	        store: Ext.create('Ext.data.Store', { 
			                    	fields: ['id', 'name'], 
			                    	data : [{"id":"1","name":"NIPT(博奥)"},
			                    	         {"id":"5","name":"NIPT(成都博奥)"},
			                    	         {"id":"2","name":"NIPT(贝瑞)"},
			                    	         {"id":"3","name":"NIPT-plus(博奥)"},
			                    	         {"id":"4","name":"NIPT-plus(贝瑞)"},
			                    	         {"id":"6","name":"NIPT(精科)"},
			                    	         {"id":"7","name":"NIPT-Plus(精科)"},
			                    	]
			                    }),
			        	        fieldStyle: me.fieldStyle,
			        	        displayField:'name',
			        	        valueField:'id',
			        	        listClass: 'x-combo-list-small'
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
    		}]
		}];
		
		me.buttons = [{
			text:'保存并提交审核',
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
		var see=Ext.getCmp('areacodeu').getRawValue();
		var form = me.getForm();
		var values = form.getValues();
		values["areaname"]=see;
		values["cancelif"]=cancelif;
		if(form.isValid())
		{
         	Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:"bacera/invasivePre/update.do", 
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
	}
	
});