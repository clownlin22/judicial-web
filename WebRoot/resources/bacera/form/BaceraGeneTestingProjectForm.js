Ext.define('Rds.bacera.form.BaceraGeneTestingProjectForm', {
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
				    			fieldLabel: '案例编号',
				    			labelWidth:65,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'test_number',
								readOnly : true,
								columnWidth : .50,
				    			maxLength: 20,
				    		}, {
					        	 xtype:"textfield",
					    			fieldLabel: '样本编号',
					    			labelWidth:60,
					    			style:'margin-left:10px;',
					    			fieldStyle:me.fieldStyle, 
					    			columnWidth : .50,
					    			name: 'sample_number',
					    			readOnly:true,
					    			maxLength: 50
					    			}]
    		                    },{
				    			border : false,
				    			style:'margin-top:10px;',
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ {
									xtype : "textfield",
									fieldLabel : '检测套餐',
									columnWidth : .50,
									labelWidth : 65,
									name : 'test_package_name',
									readOnly : true
								},
								
								{
						        	 xtype:"textfield",
						    			fieldLabel: '客户姓名',
						    			labelWidth:60,
						    			style:'margin-left:10px;',
						    			columnWidth : .50,
						    			fieldStyle:me.fieldStyle, 
						    			name: 'consumer_name',
						    			readOnly : true,
						    			maxLength: 50
						    		
									}
								]
    		},{
    			border : false,
    			style:'margin-top:10px;',
				layout : "column",// 从左往右的布局
				xtype : 'fieldcontainer',
				border : false,
				items : [ 
							{
								xtype : "textfield",
								fieldLabel : '登记时间',
								columnWidth : .50,
								labelWidth : 65,
								readOnly : true,
								name : 'add_time'
								},
								{
					    			xtype:"textfield",
					    			fieldLabel: '性别',
					    			labelWidth:60,
					    			columnWidth : 0.5,
					    			style:'margin-left:10px;',
					    			fieldStyle:me.fieldStyle, 
					    			name: 'sconsumer_sex',
					    			readOnly : true,
					    			maxLength: 50
			    		        }
				]
},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
					xtype : "textfield",
					fieldLabel : '客户生日',
					columnWidth : .50,
					labelWidth : 65,
					readOnly : true,
					name : 'consumer_birthday'
					},{
		    			xtype:"textfield",
		    			fieldLabel: '归属人',
		    			labelWidth:60,
		    			style:'margin-left:10px;',
		    			fieldStyle:me.fieldStyle, 
		    			name: 'charge_standard_name',
		    			maxLength: 50,
		    			columnWidth : .50,
		    			readOnly : true,
		    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
    		        }]
    			
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '参考价格',
	    			labelWidth:65,
	    			columnWidth : 0.5,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'sprice',
	    			readOnly : true,
	    			maxLength: 50,
	    		        }]
    			
    		},{

    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    		        xtype:"textarea",
			    			fieldLabel: '检测项',
			    			labelWidth:65,
			    			fieldStyle:me.fieldStyle, 
			    			name: 'test_item_names',
			    			maxLength:200,
			    			columnWidth : 1.0,
			    			readOnly : true,
			    			blankText:"不能为空",}]
    			
    		
    		},{
				xtype:"textarea",
    			fieldLabel: '备注',
    			labelWidth:65,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark',
    			height:80,
    			readOnly : true,
    			maxLength: 200,
    		}]
		}];
		
		me.buttons = [
		{
			text:'关闭',
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