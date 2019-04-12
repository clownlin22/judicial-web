Ext.define('Rds.upc.panel.RdsUpcModuleForm', {
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
    			fieldLabel: '模版编号',
    			labelWidth:100,
    			readOnly:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'modulecode',
    			hidden:true
    		},{
    			xtype:"textfield",
    			fieldLabel: '模版名称',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'modulename'
    		},{
    			xtype:"textfield",
    			fieldLabel: '模版URL',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'moduleurl'
    		},{
    			xtype:"textfield",
    			fieldLabel: '模版图标',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'moduleico'
    		},{
    			xtype:"textfield",
    			fieldLabel: '模版类型',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'moduletype'
    		},{
    			xtype:"textfield",
    			fieldLabel: '模版序号',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'modulesqe'
    		},{
    			xtype:"textfield",
    			fieldLabel: '父节点',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			hidden:true,
    			value:'0',
    			name: 'moduleparentcode'
    		},{
    			xtype:"textareafield",
    			fieldLabel: '模版描述',
    			labelAgin:'top',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'moduledesc'
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
		Ext.MessageBox.wait('正在操作','请稍后...');
		Ext.Ajax.request({  
			url:"upc/module/insertOrUpdate.do", 
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