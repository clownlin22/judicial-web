Ext.define('Rds.bacera.form.BaceraInvasivePreListInsertForm', {
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
			allowBlank:false,
			style:'margin-left:10px',
			id:'areacode11',
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
			value:'',
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
			},  listeners :{
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
        						hospital.setValue(response.message);
           					}
           				},
           				failure : function() {
           					Ext.Msg.alert("提示", "查询失败<br>请联系管理员!");
           				}

           			});
               	}
               },
			     editable:true
			});
	
		var partnerModel = Ext.create('Ext.data.Store', {
			fields:[
	                  {name:'hospital',mapping:'hospital',type:'string'},
	          ],
	          start:0,
				limit:10000,
				pageSize:10000,
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'bacera/invasivePre/queryallpageHospitalAreaName.do',
//				params : {
//					areacode:can
//				},
				reader : {
					type : 'json',
					root : 'data'
				}
			},  
//			listeners: {  
//		        'beforeload': function (store, op, options) {  
//		            var params = {  
//		                areacode:areacode.getValue()
//		               // areaname:Ext.getCmp('areacode11').getRawValue()
//		            };  
//		            Ext.apply(store.proxy.extraParams, params);   
//		        }  
//		    }  ,
			autoLoad : true,
			remoteSort : true
		});
		
		var hospital = Ext.create('Ext.form.ComboBox', {
//			autoSelect : true,
			editable:true,
			allowBlank:false,
			labelWidth : 65,
			columnWidth : .50,
			fieldLabel : '所属医院<span style="color:red">*</span>',
			labelAlign : 'right',
	        name:'hospital',
//	        forceSelection:true,
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "没有请选择'无'",
	        selectOnTab: true,
	        store: partnerModel,
	        style:'margin-left:10px',
	        fieldStyle: me.fieldStyle,
	        displayField:'hospital',
	        valueField:'hospital',
	        listClass: 'x-combo-list-small'
		});

		
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
    			id:'insertId1',
    			hidden:true
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [	
				    		{ xtype:"textfield",
				    			fieldLabel: '案例编号',
				    			labelWidth:65,
				    			style:'margin-left:0px',
				    			columnWidth : .50,
				    			readOnly:true,
				    			allowBlank:true,
				    			emptyText:'案例编号自动生成',
				    			fieldStyle:me.fieldStyle, 
				    			name: 'num',
				    			maxLength: 50},
				    			{
					        	 xtype:"textfield",
					    			fieldLabel: '优惠码',
					    			labelWidth:65,
					    			style:'margin-left:10px',
					    			columnWidth : .50,
					    			fieldStyle:me.fieldStyle, 
					    			name: 'confirm_code',
					    			maxLength: 50
					    			}
				   ]
    		},{
    			border : false,
    			style:'margin-top:10px;',
				layout : "column",// 从左往右的布局
				xtype : 'fieldcontainer',
				border : false,
				items : [ {
					xtype : 'combo',
					fieldLabel : '归属人<span style="color:red">*</span>',
					labelWidth : 65,
					columnWidth : .50,
					name : 'case_userid',
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
							url:'judicial/dicvalues/getUsersId.do',
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
					}
				},areacode]
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
	    		        },hospital
						]
    			
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [
				         {
				        	 xtype:"textfield",
			    			fieldLabel: '条码<span style="color:red">*</span>',
			    			columnWidth : .50,
			    			labelWidth:65,
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
			        			fieldLabel:'类型<span style="color:red">*</span>',
			        	        name:'inspectionunit',
			        	        allowBlank:false, //不允许为空
			        			blankText:"不能为空", //错误提示信息，默认为This field is required!
			        	        triggerAction: 'all',
			        	        queryMode: 'local', 
			        	        style:'margin-left:10px',
			        	        labelAlign: 'right',
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
			xtype : 'fieldset',
			title : '案例照片',
			id:'testFieldset',
			layout : 'anchor',
			defaults : {
				anchor : '100%'
			},
			items : [{
				layout : "column",// 从左往右的布局
				xtype : 'fieldcontainer',
				border : false,
				items : [ 	 {
					xtype : 'filefield',
					name : 'files',
					fieldLabel : '文件<span style="color:red">*</span>',
					msgTarget : 'side',
					allowBlank : false,
					labelWidth : 52,
					anchor : '100%',
					buttonText : '选择文件',
					columnWidth : 0.6,
					validator : function(v) {
						if (!v.endWith(".jpg") &&!v.endWith(".JPG")
								&& !v.endWith(".png") && !v.endWith(".PNG")
								&& !v.endWith(".gif") && !v.endWith(".GIF")
								&& !v.endWith(".jpeg") && !v.endWith(".JPEG") ) {
							return "请选择.jpg .png .gif.jpeg类型的图片";
						}
						return true;
					}
				},new Ext.form.field.ComboBox({
					fieldLabel : '文件类型<span style="color:red">*</span>',
					labelWidth : 65,
					labelAlign : 'right',
					fieldStyle : me.fieldStyle,
					editable : false,
					allowBlank : false,
					triggerAction : 'all',
					displayField : 'Name',
					valueField : 'Code',
					store : new Ext.data.ArrayStore({
								fields : ['Name', 'Code'],
								data : [['登记表格', 5],['照片', 1]]
							}),
					mode : 'local',
					name : 'filetype',
					value:1,
					readOnly:true,
					columnWidth : .4
				})]
			}]},
		{
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
			text:'保存',
			iconCls:'Disk',
			handler:me.onSave
		},{
			text:'保存并提交审核',
			iconCls:'Disk',
			handler:me.onVerifySave
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
			form.submit({  
				url:"bacera/invasivePre/saveCaseInfo.do", 
				method: "POST",
				waitMsg : '正在保存...',
				params:{'areaname':Ext.getCmp('areacode11').getRawValue()
				},
				success : function(form, action) {
					response = Ext.JSON
							.decode(action.response.responseText);
					console.log(response);
					if (response.result) {
						Ext.MessageBox.alert("提示信息", response.message);
						me.grid.getStore().load();
						me.up('window').close();
					} else {
						Ext.MessageBox.alert("提示信息", response.message);
					}
				},
				failure : function() {
					Ext.Msg.alert("提示", "保存失败，请联系管理员!");
				}
	    	       
	      	});
		}
	},
	onVerifySave:function(){
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if(form.isValid())
		{
			form.submit({  
				url:"bacera/invasivePre/saveVerify.do", 
				method: "POST",
				waitMsg : '正在保存...',
				params:{'areaname':Ext.getCmp('areacode11').getRawValue()
				},
				success : function(form, action) {
					response = Ext.JSON
							.decode(action.response.responseText);
					console.log(response);
					if (response.result) {
						Ext.MessageBox.alert("提示信息", response.message);
						me.grid.getStore().load();
						me.up('window').close();
					} else {
						Ext.MessageBox.alert("提示信息", response.message);
					}
				},
				failure : function() {
					Ext.Msg.alert("提示", "保存失败，请联系管理员!");
				}
	    	       
	      	});
		}
	
		
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	}
	
});