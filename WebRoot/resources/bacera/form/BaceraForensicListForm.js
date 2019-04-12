Ext.define('Rds.bacera.form.BaceraForensicListForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var date = new Ext.form.DateField(
				{
					name : 'date',
					columnWidth : .35,
					fieldLabel : '收案时间<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
					labelWidth : 80,
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
    			xtype:"textfield",
    			fieldLabel: '案例编号<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
    			labelWidth:80,
    			fieldStyle:me.fieldStyle, 
    			name: 'num',
    			allowBlank:false, //不允许为空
    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
    			maxLength: 20
    		},date,{
    			xtype:"textfield",
    			fieldLabel: '姓名<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
    			labelWidth:80,
    			fieldStyle:me.fieldStyle, 
    			name: 'name',
    			maxLength: 20,
    			allowBlank:false, //不允许为空
    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
    		},{
    			xtype:"textfield",
    			fieldLabel: '委托人',
    			labelWidth:80,
    			fieldStyle:me.fieldStyle, 
    			name: 'client',
    			maxLength: 20
    		},
    		{
				xtype : 'combo',
				fieldLabel : '案例归属<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
				labelWidth : 80,
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
			},{
    			xtype:"textfield",
    			fieldLabel: '委托事项',
    			labelWidth:80,
    			fieldStyle:me.fieldStyle, 
    			name: 'commitment',
    			maxLength: 50
    		},
			{
				xtype:"textarea",
    			fieldLabel: '备注要求',
    			labelWidth:80,
    			fieldStyle:me.fieldStyle, 
    			//allowBlank:false,
    			name: 'remark',
    			height:80,
    			maxLength: 100,
    		},
    		{
    			xtype:"numberfield",
    			fieldLabel: '应收款项<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
    			labelWidth:80,
    			fieldStyle:me.fieldStyle, 
    			name: 'receivables', 
    			id: 'receivables',
    			maxLength: 20,
    			allowBlank:false, //不允许为空
    			blankText:"不能为空", //错误提示信息，默认为This field is required!
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
				url:"bacera/Forensic/save.do", 
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