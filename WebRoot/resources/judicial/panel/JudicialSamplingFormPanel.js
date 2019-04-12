Ext.define('Rds.judicial.panel.JudicialSamplingFormPanel', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		
		var agentstore = Ext.create('Ext.data.Store',{
    	    fields:['key','value'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'judicial/agent/queryUserByType.do',
    	        params:{
    	        	roletype:'103'
    	        },
    	        render:{
    	            type:'json'
    	        },
    	        writer: {
    	            type: 'json'
    	       }
    	    }
    	});
		var marketstore = Ext.create('Ext.data.Store',{
    	    fields:['key','value'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'judicial/agent/queryUserByType.do',
    	        params:{
    	        	roletype:'102'
    	        },
    	        render:{
    	            type:'json'
    	        },
    	        writer: {
    	            type: 'json'
    	       }
    	    }
    	});
		
		var agentCombo = new Ext.form.ComboBox({
			autoSelect : true,
			editable:false,
			allowBlank:false,
			fieldLabel:'市场人员',
	        name:'userid',
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
//	        value:companyid,
//	        readOnly:true,
	        selectOnTab: true,
	        store: agentstore,
	        fieldStyle: me.fieldStyle,
	        displayField:'value',
	        valueField:'key',
	        listClass: 'x-combo-list-small',
		});
		var marketCombo = new Ext.form.ComboBox({
			autoSelect : true,
			editable:false,
			allowBlank:false,
			fieldLabel:'采样员',
	        name:'peruserid',
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
//	        value:companyid,
//	        readOnly:true,
	        selectOnTab: true,
	        store: marketstore,
	        fieldStyle: me.fieldStyle,
	        displayField:'value',
	        valueField:'key',
	        listClass: 'x-combo-list-small',
		})
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
    			fieldLabel: 'id',
    			labelWidth:100,
    			readOnly:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'id',
    			hidden:true
    		},marketCombo,agentCombo,{
    			xtype:"textfield",
    			fieldLabel: '备注',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark',
    			hidden: true, 
    			maxLength:50
    		},{
    			xtype:"textfield",
    			fieldLabel: 'value',
    			labelWidth:100,
    			hidden: true, 
    			fieldStyle:me.fieldStyle, 
    			name: 'flag',
    			value:2
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
			Ext.Ajax.request({  
				url:"judicial/agent/save.do", 
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
		}else{
			Ext.MessageBox.alert("提示", "请正确填写录入信息!");
			return;
		}
	
		
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	}
	
});