Ext.define("Rds.appraisal.panel.AppraisalTypeFormPanel",{
	extend:'Ext.form.Panel',
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
    			fieldLabel: 'type_id',
    			labelWidth:100,
    			labelAgin:'top',
    			fieldStyle:me.fieldStyle, 
    			name: 'type_id',
    			hidden:true
    		},{
    			xtype:"textfield",
    			fieldLabel: '鉴定类型名称',
    			labelWidth:100,
    			labelAgin:'top',
    			fieldStyle:me.fieldStyle, 
    			allowBlank:false,
    			name: 'standard_name',
    			maxLength: 200
    		},{
    			xtype:"textarea",
    			fieldLabel: '鉴定类型说明',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			allowBlank:false,
    			name: 'standard_desc',
    			height:130,
    			maxLength: 2000,
    		},{
    			xtype: 'radiogroup',
	            fieldLabel: '是否有附录',
	            labelWidth : 100,
	            itemCls: 'x-check-group-alt',
	            columns: 2,
	            vertical: true,
	            items: [
	                {boxLabel: '是', name: 'appendix_status', inputValue: 1},
	                {boxLabel: '否', name: 'appendix_status', inputValue: 0, checked: true}
	            ], 
	            listeners:{
	                //通过change触发
	                change: function(g , newValue , oldValue){
	                	if(newValue.appendix_status=='1')
	                		Ext.getCmp("appendix_desc").setDisabled(false);
	                	else
	                		{
	                		Ext.getCmp("appendix_desc").setDisabled(true);
	                		Ext.getCmp("appendix_desc").setRawValue('');
	                		}
	                }
	            }
    		},{
    			xtype:"textarea",
    			fieldLabel: '附录说明',
    			labelWidth:100,
    			labelAgin:'top',
    			fieldStyle:me.fieldStyle, 
    			name: 'appendix_desc',
    			maxLength: 2000,
    			disabled:true,	
    			id:'appendix_desc',
    			height:130,
    			allowBlank:false
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
	onSave:function(){
		var me = this.up("form");
		var form = me.getForm();
		if(!form.isValid()){
			Ext.MessageBox.alert("提示信息", "请正确填写完整表单信息!");
			return;
		}
		var values = form.getValues();
		
		Ext.Ajax.request({  
			url:"appraisal/type/save.do", 
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
                 	Ext.MessageBox.alert("错误信息", response.message);
                 } 
			},  
			failure: function () {
				Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
			}
    	       
      	});
		
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	}
});