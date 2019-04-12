Ext.define('Rds.statistic.form.FinanceAptitudeForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var partner_start = new Ext.form.DateField(
			{
				name : 'partner_start',
				fieldLabel : '合作起止日期',
				labelWidth : 100,
				columnWidth : .50,
				afterLabelTextTpl : me.required,
				emptyText : '请选择日期',
				format : 'Y-m-d'
			});
		var partner_end = new Ext.form.DateField(
				{
					name : 'partner_end',
					fieldLabel : '合作终止日期',
					labelWidth : 100,
					columnWidth : .50,
					labelAlign : 'right',
					afterLabelTextTpl : me.required,
					emptyText : '请选择日期',
					format : 'Y-m-d'
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
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: 'config_id',
	    			labelWidth:80,
	    			readOnly:true,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'config_id',
	    			hidden:true
				},{
	    			xtype:"textfield",
	    			fieldLabel: '所属省区',
	    			labelWidth:100,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'provice',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 20,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '合作方',
	    			labelWidth:50,
	    			labelAlign : 'right',
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'partnername',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 20,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"numberfield",
	    			fieldLabel: '资质费(每样本)',
	    			labelWidth:100,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'aptitude_sample',
	    			maxLength: 20,
	    			value:0
	    		},{
	    			xtype:"numberfield",
	    			fieldLabel: '资质费(每案例)',
	    			labelWidth:100,
	    			labelAlign : 'right',
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'aptitude_case',
	    			maxLength: 20,
	    			value:0
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"numberfield",
	    			fieldLabel: '实验费(每样本)',
	    			labelWidth:100,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'experiment_sample',
	    			maxLength: 20,
	    			value:0
	    		},{
	    			xtype:"numberfield",
	    			fieldLabel: '实验费(每案例)',
	    			labelWidth:100,
	    			labelAlign : 'right',
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'experiment_case',
	    			maxLength: 20,
	    			value:0
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [partner_start,partner_end]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"numberfield",
	    			fieldLabel: '月度案例数',
	    			labelWidth:100,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'month_num',
	    			maxLength: 20,
	    			value:0
	    		},{
	    			xtype:"numberfield",
	    			fieldLabel: '月度案例差值',
	    			labelWidth:100,
	    			labelAlign : 'right',
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'month_num_reduce',
	    			maxLength: 20,
	    			value:0
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"numberfield",
	    			fieldLabel: '一类样本加价',
	    			labelWidth:100,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'sample_special1',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 20,
	    			value:0
	    		},{
	    			xtype:"numberfield",
	    			fieldLabel: '二类样本加价',
	    			labelWidth:100,
	    			labelAlign : 'right',
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'sample_special2',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 20,
	    			value:0
	    		}]
    		},{
				xtype:"textarea",
    			fieldLabel: '备注',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark',
    			height:80,
    			maxLength: 200
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
				url:"statistics/financeConfig/saveFinanceAptitude.do", 
				method: "POST",
				headers: { 'Content-Type': 'application/json' },
				jsonData: values, 
				success: function (response, options) {  
					response = Ext.JSON.decode(response.responseText); 
	                 if (response.success == true) {  
	                 	Ext.MessageBox.alert("提示信息", response.msg);
	                 	me.grid.getStore().load();
	                 	me.up("window").close();
	                 }else { 
	                 	Ext.MessageBox.alert("错误信息", response.msg);
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