Ext.define('Rds.narcotics.form.NarcoticsIdentifyInsert', {
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
                labelWidth: 100
            },
            border:false,
            autoHeight:true,
            items: [{
    			xtype:"textfield",
    			fieldLabel: 'per_id',
    			labelWidth:100,
    			hidden:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'per_id'
    		},{
            	border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
					xtype:"textfield",
	    			fieldLabel: '鉴定人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    			labelWidth:65,
	    			columnWidth : .50,
	    			allowBlank:false,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'per_name',
	    			regex: /^[\u4E00-\u9FA5]+$/, 
	    			regexText:'只能输入汉字',  
	    			maxLength: 20,
	    			anchor: '100%'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '证号<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    			style:'margin-left:5px;',
	    			labelWidth:50,
	    			columnWidth : .50,
	    			allowBlank:false,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'per_code',
	    			maxLength: 12,
	    			anchor: '100%'
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
	onSave:function(){
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if(form.isValid()){
			Ext.Ajax.request({  
				url:"narcotics/archive/save.do",
				method: "POST",
				headers: { 'Content-Type': 'application/json' },
				jsonData: values, 
				success: function (response, options) {  
					response = Ext.JSON.decode(response.responseText); 
	                 if (response.result) {  
	                 	me.up("window").close();
	                 	me.grid.getStore().load();
	                 	Ext.MessageBox.alert("信息", response.message);
	                 }else { 
	                	 Ext.MessageBox.alert("错误信息", response.message);
	                 } 
				},  
				failure: function () {
					Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
				}
	      	});
		}
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	}
});