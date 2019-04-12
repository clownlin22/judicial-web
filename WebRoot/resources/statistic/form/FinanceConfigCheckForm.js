Ext.define('Rds.statistic.form.FinanceConfigCheckForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
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
	    			fieldLabel: '总类型',
	    			labelWidth:100,
	    			columnWidth : .33,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'program',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 20,
	    			readOnly:true,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '项目',
	    			labelWidth:40,
	    			labelAlign : 'right',
	    			columnWidth : .33,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'finance_program',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 20,
	    			readOnly:true,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '子项目',
	    			labelWidth:50,
	    			labelAlign : 'right',
	    			columnWidth : .34,
	    			fieldStyle:me.fieldStyle, 
	    			readOnly:true,
	    			name: 'finance_program_type',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 50,
//	    			regex:/^[^\s]*$/,
//					regexText:'我不喜欢空格'
	    		}]
    		},{
				xtype:"textarea",
    			fieldLabel: '前端结算公式',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'front_settlement',
    			height:80,
    			maxLength: 200,
    			readOnly:true,
				emptyText:'公式举例：function exe(real_sum,case_num){ return real_sum-500*case_num;};其中real_sum为应收，case_num为案例数量',
    		},{
				xtype:"textarea",
    			fieldLabel: '管理费公式',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'finance_manage',
    			height:80,
    			readOnly:true,
    			maxLength: 200,
    			emptyText:'公式举例：function exe(real_sum,case_num,aptitude_price){ return (400*case_num+aptitude_price*0.5*0.3);};其中real_sum为应收，case_num为案例数量,aptitude_price为资质费，需要用到其他合作方资质时使用！',
    		},{
				xtype:"textarea",
    			fieldLabel: '后端结算公式',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'back_settlement',
    			height:80,
    			readOnly:true,
    			maxLength: 200,
    			emptyText:'公式举例：function exe(real_sum,case_num){ return 147*case_num;};其中real_sum为应收，case_num为案例数量',
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '后端部门分配',
	    			labelWidth:100,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'back_remark',
	    			maxLength: 20,
	    			readOnly:true,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '业务支撑中心',
	    			labelWidth:100,
	    			labelAlign : 'right',
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'business_support',
	    			maxLength: 20,
	    			readOnly:true,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '代理价',
	    			labelWidth:100,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'agency_price',
	    			maxLength: 20,
	    			readOnly:true,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '实验成本价',
	    			labelWidth:100,
	    			labelAlign : 'right',
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'experiment_price',
	    			maxLength: 20,
	    			readOnly:true,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '定价',
	    			labelWidth:100,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'price',
	    			maxLength: 20,
	    			readOnly:true,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '管理费部门',
	    			labelWidth:100,
	    			labelAlign : 'right',
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'finance_manage_dept',
	    			maxLength: 50,
	    			readOnly:true,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '后端结算部门',
	    			labelWidth:100,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'back_settlement_dept',
	    			maxLength: 50,
	    			readOnly:true,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '后端结算部门2',
	    			labelWidth:100,
	    			labelAlign : 'right',
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'back_settlement_dept1',
	    			maxLength: 50,
	    			readOnly:true,
	    			regex:/^[^\s]*$/,
					regexText:'我不喜欢空格'
	    		}]
    		},{
				xtype:"textarea",
    			fieldLabel: '备注',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark',
    			readOnly:true,
    			height:80,
    			maxLength: 200
    		}]
		}];
		
		me.buttons = [{
			text:'取消',
			iconCls:'Cancel',
			handler:me.onCancel
		}]
		me.callParent(arguments);
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	}
});