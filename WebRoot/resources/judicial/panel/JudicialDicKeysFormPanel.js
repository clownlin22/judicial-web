Ext.define("Rds.judicial.panel.JudicialDicKeysFormPanel",{
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
    			fieldLabel: '字典编号',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			allowBlank:false,
    			name: 'keycode',
    			maxLength: 20
    		},{
    			xtype:"textfield",
    			fieldLabel: '字典名称',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			allowBlank:false,
    			name: 'keyname',
    			maxLength: 20
    		},{
    			xtype:"textfield",
    			fieldLabel: 'id',
    			labelWidth:100,
    			hidden:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'id'
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
		if(!form.isValid()){
			Ext.MessageBox.alert("提示信息", "请填写完整表单信息!");
			return;
		}
		var values = form.getValues();
		Ext.MessageBox.wait('正在操作','请稍后...');
		Ext.Ajax.request({  
			url:"judicial/dickeys/save.do", 
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