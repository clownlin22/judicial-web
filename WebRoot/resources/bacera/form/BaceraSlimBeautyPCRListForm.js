Ext.define('Rds.bacera.form.BaceraSlimBeautyPCRListForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var date = new Ext.form.DateField(
		{
			name : 'date',
			fieldLabel : '日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			labelWidth : 60,
			labelAlign : 'right',
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
				items : [{
					columnWidth : .50,
					xtype: 'combo',
	    			autoSelect : true,
	    			editable:false,
	    			labelWidth : 70,
	    			fieldLabel:'案例类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    	        name:'case_type',
	    	        triggerAction: 'all',
	    	        queryMode: 'local', 
	    	        emptyText : "请选择",
	    	        allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    	        selectOnTab: true,
	    	        store: Ext.create('Ext.data.Store', { 
	                	fields: ['id', 'name'], 
	                	data : [{"id":"1","name":"减肥美容基因检测"},
	                	         {"id":"2","name":"个体特征基因检测"},
	                	         {"id":"3","name":"重大慢病基因检测"}
	                	         ]
	                }),
	    	        fieldStyle: me.fieldStyle,
	    	        displayField:'name',
	    	        valueField:'id',
	    	        listClass: 'x-combo-list-small'
				},{
	    			xtype:"textfield",
	    			fieldLabel: '案例编号<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
	    			labelWidth:70,columnWidth : .50,
	    			labelAlign:'right',
	    			fieldStyle:me.fieldStyle, 
	    			name: 'num',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 20
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
					xtype : 'combo',
					fieldLabel : '案例归属<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
					labelWidth : 70,
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
	    			columnWidth :.50,
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
				},date]
    		},{
    			xtype:"textarea",
    			fieldLabel: '检测项目',
    			labelWidth:70,
    			labelAlign:'left',
    			fieldStyle:me.fieldStyle, 
    			maxLength: 100,
    			height:50,
    			name: 'testitems'
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '送检人',
	    			labelWidth:70,
	    			maxLength: 20,
	    			sytle:'margin-left:2px',
	    			fieldStyle:me.fieldStyle, 
	    			name: 'checkper'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '送检单位',
	    			labelWidth:60,
	    			labelAlign:'right',
	    			maxLength: 20,
	    			sytle:'margin-left:2px',
	    			fieldStyle:me.fieldStyle, 
	    			name: 'inspectionUnits'
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '姓名<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    			labelWidth:70,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'name',
	    			maxLength: 20,
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    		},new Ext.form.field.ComboBox({
	    			fieldLabel : '性别',
	    			width : 140,
	    			labelWidth:60,
	    			editable : false,
	    			triggerAction : 'all',
	    			displayField : 'Name',
	    			labelAlign : 'right',
	    			valueField : 'Code',
	    			store : new Ext.data.ArrayStore(
	    					{
	    						fields : ['Name','Code' ],
	    						data : [ ['男','男' ],['女','女' ] ]
	    					}),
	    			mode : 'local',
	    			name : 'gender',
	    		})]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [
				    		{
				    			xtype:"textfield",
				    			fieldLabel: '年龄',
				    			labelWidth:70,
				    			maxLength: 3,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'age',
				    			regex: /^\d+$/,
				    			regexText: "只能为数字",
				    		},{
				    			xtype:"textfield",
				    			fieldLabel: '电话',
				    			labelWidth:60,
				    			maxLength: 20,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'phonenum',
				    			labelAlign:'right',
				    			regex: /(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,11}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/,
								regexText:"电话格式有误"
				    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '报告种类',
	    			labelWidth:70,
	    			maxLength: 20,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'reportType'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '接单平台',
	    			labelWidth:60,
	    			labelAlign:'right',
	    			maxLength: 20,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'orderPlatform'
	    		}]
    		},{
				xtype:"textarea",
    			fieldLabel: '备注要求',
    			labelWidth:70,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark',
    			height:80,
    			maxLength: 200,
    		},
    		{
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
	    			maxLength: 10,
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
				url:"bacera/slimBeautyPCR/save.do", 
				method: "POST",
				headers: { 'Content-Type': 'application/json' },
				jsonData: values, 
				success: function (response, options) {  
					response = Ext.JSON.decode(response.responseText); 
	                 if (response.result == true) {  
	                 	Ext.MessageBox.alert("提示信息", response.message);
	                 	me.up("window").close();
	                 	me.grid.getStore().load();
	                 }else { 
	                 	Ext.MessageBox.alert("错误信息", response.message);
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