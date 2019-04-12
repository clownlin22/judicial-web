Ext.define("Rds.judicial.panel.JudicialReportTypeFormPanel",{
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
    			fieldLabel: '类型编号',
    			labelWidth:100,
    			readOnly:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'typeid'
    		},{
    			xtype:"textfield",
    			fieldLabel: '类型名称',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'typename'
    		},{
    			xtype:"textfield",
    			fieldLabel: '录入表单',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'inputform'
    		},{
    			xtype:"textfield",
    			fieldLabel: '展示表格',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'displaygrid'
    		},{
    			xtype:"textfield",
    			fieldLabel: '报告字样',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'identify'
    		},{
    			xtype:"textfield",
    			fieldLabel: '父类型节点',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
//    			hidden:true,
    			value:'0',
    			name: 'parentid'
    		},{
    			xtype:"numberfield",
    			fieldLabel: '序号',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'sort'
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
		Ext.Ajax.request({  
			url:"judicial/reporttype/save.do", 
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