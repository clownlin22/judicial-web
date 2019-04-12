
Ext.define('Rds.bacera.form.BaceraDrugFastDetectorListForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var date = new Ext.form.DateField(
		{
			name : 'date',
			fieldLabel :'发货日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			labelWidth :65,
			columnWidth : .50,
			labelAlign : 'left',
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
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '案例编号<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
	    			labelWidth:65,
	    			columnWidth : .5,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'num',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 20
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '发货地点<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
	    			labelWidth:65,
	    			columnWidth : .5,
	    			labelAlign : 'left',
	    			fieldStyle:me.fieldStyle, 
	    			name: 'address',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			style:'margin-left:5px',
	    			maxLength: 80
	    		}]
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [date,{
	    			xtype:"textfield",
	    			fieldLabel: '负责人',
	    			labelWidth:65,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'person',
	    			style:'margin-left:5px',
	    			maxLength: 20
	    		}]
			
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '试剂/仪器种类',
	    			labelWidth:65,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'reagent_type',
	    			maxLength: 20
				},{
	    			xtype:"textfield",
	    			fieldLabel: '试剂/仪器数量',
	    			labelWidth:65,
	    			columnWidth : .50,
	    			style:'margin-left:5px',
	    			fieldStyle:me.fieldStyle, 
	    			name: 'reagent_count',
	    			regex: /^[1-9]\d*|0$/,
	    			maxLength: 20
				}
				]
			},{

				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
					columnWidth : .50,
					xtype: 'combo',
	    			autoSelect : true,
	    			editable:false,
	    			labelWidth : 65,
	    			fieldLabel:'试用类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    	        name:'trial_type',
	    	       // allowBlank:false,
	    	        triggerAction: 'all',
	    	        queryMode: 'local', 
	    	        emptyText : "请选择",
	    	        allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    	        selectOnTab: true,
	    	        store: Ext.create('Ext.data.Store', { 
	                	fields: ['id', 'name'], 
	                	data : [{"id":"1","name":"采购"},
	                	         {"id":"2","name":"试用"},
	                	         {"id":"3","name":"销售"}]
	                }),
	    	        fieldStyle: me.fieldStyle,
	    	        displayField:'name',
	    	        valueField:'id',
	    	        listClass: 'x-combo-list-small'
				}
				
				]
			
			},
			{
				xtype:"textarea",
    			fieldLabel: '备注要求',
    			labelWidth:65,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark',
    			height:120,
    			maxLength: 200,
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
				url:"bacera/fastDetection/save.do", 
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
		
		'afterrender' : function() {
			if(confirm_flagTemp == '2')
				Ext.getCmp("receivables").readOnly=true;
		}
	}
	
});