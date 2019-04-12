Ext.define("Rds.judicial.panel.JudicialDicValuesFormPanel",{
	extend:'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var store = Ext.create('Ext.data.Store',{
			 fields:['id','keycode','keyname','status','sort'],
	    	    autoLoad:true,
	    	    proxy:{
	    	        type:'jsonajax',
	    	        actionMethods:{read:'POST'},
	    	        url:'judicial/dickeys/queryall.do',
	    	        params:{
	    	        },
	    	        render:{
	    	            type:'json'
	    	        },
	    	        writer: {
	    	            type: 'json'
	    	       }
	    	    }
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
                labelWidth: 100
            },
            border:false,
            autoHeight:true,
            items: [{
                xtype: 'combo',
                autoSelect : true,
                editable:true,
                allowBlank:false,
        		fieldLabel: '字典编码',
        		emptyText : "请选择",
        		triggerAction: 'all',
     	        queryMode: 'local',
     	        selectOnTab: true,
                store: store,
        		fieldStyle: me.fieldStyle,
                displayField: 'keyname',
                valueField: 'keycode',
                name:'keycode',
                listClass: 'x-combo-list-small'
            },{
    			xtype:"textfield",
    			fieldLabel: '字典key',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			allowBlank:false,
    			name: 'keyid',
    			maxLength: 20
    		},{
    			xtype:"textfield",
    			fieldLabel: '字典值',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			allowBlank:false,
    			name: 'keyvalue',
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
			url:"judicial/dicvalues/save.do", 
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