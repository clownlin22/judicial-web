Ext.define('Rds.bacera.form.BaceraInvasiveDNAListForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var date = new Ext.form.DateField({
			name : 'date',
			fieldLabel : '日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			labelWidth : 60,
			labelAlign : 'right',
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			allowBlank:false, //不允许为空
			blankText:"不能为空", //错误提示信息，默认为This field is required! 
			value:new Date()
		});
		var consigningDate = new Ext.form.DateField({
			name : 'consigningDate',
			fieldLabel : '委托日期',
			labelWidth : 60,
			labelAlign : 'right',
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
				    			fieldStyle:me.fieldStyle, 
				    			name: 'num',
				    			allowBlank:false, //不允许为空
				    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
				    			maxLength: 20
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
	    			labelAlign: 'left',
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
				}]
    			
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
			    			xtype:"textfield",
			    			fieldLabel: '父亲姓名<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			    			labelWidth:65,
			    			fieldStyle:me.fieldStyle, 
			    			name: 'fatherName',
			    			maxLength: 50,
			    			allowBlank:false, //不允许为空
			    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    		        },{
			    			xtype:"textfield",
			    			fieldLabel: '母亲姓名',
			    			labelWidth:60,
			    			columnWidth : 1,
			    			labelAlign: 'right',
			    			fieldStyle:me.fieldStyle, 
			    			name: 'motherName',
			    			maxLength: 50,
	    		        }]
    			
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [ {
							xtype: 'combo',
			    			autoSelect : true,
			    			editable:false,
			    			labelWidth : 65,
			    			fieldLabel:'父本类型',
			    	        name:'fatherType',
			    	        triggerAction: 'all',
			    	        queryMode: 'local', 
			    	        emptyText : "请选择",
			    	        selectOnTab: true,
			    	        store: Ext.create('Ext.data.Store', { 
			                	fields: ['id', 'name'], 
			                	data : [{"id":"静脉血","name":"静脉血"},
			                	         {"id":"指甲","name":"指甲"},
			                	         {"id":"精斑","name":"精斑"},
			                	         {"id":"血痕","name":"血痕"},
			                	         {"id":"毛发","name":"毛发"}]
			                }),
			    	        fieldStyle: me.fieldStyle,
			    	        displayField:'name',
			    	        valueField:'id',
			    	        multiSelect:true,
			    	        listClass: 'x-combo-list-small'
						},{
			    			xtype:"textfield",
			    			fieldLabel: '母本孕周',
			    			labelWidth:60,
			    			columnWidth : 1,
			    			labelAlign: 'right',
			    			fieldStyle:me.fieldStyle, 
			    			name: 'gestational',
			    			maxLength: 50,
	    		        }
			    		]
    			
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
			    			xtype:"textfield",
			    			fieldLabel: '委托方',
			    			labelWidth:65,
			    			fieldStyle:me.fieldStyle, 
			    			name: 'client',
			    			maxLength: 50
	    		        },consigningDate]
    			
    		},{
				xtype:"textarea",
    			fieldLabel: '备注要求',
    			labelWidth:65,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark',
    			height:80,
    			maxLength: 1000,
    		},
    		{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"numberfield",
	    			fieldLabel: '应收款项<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    			labelWidth:65,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'receivables',
	    			id: 'receivables',
	    			maxLength: 20,
	    			allowBlank:false, //不允许为空
        			blankText:"不能为空", //错误提示信息，默认为This field is required!
	    		}]
    		}
    		]
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
		values["fatherType"]=values["fatherType"].toString()
		if(form.isValid())
		{
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:"bacera/invasiveDNA/save.do", 
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
	                 	Ext.MessageBox.alert("提示信息",response.message);
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
		'afterrender' : function() {
			if(confirm_flagTemp == '2')
				Ext.getCmp("receivables").readOnly=true;
		}
	}
	
	
});