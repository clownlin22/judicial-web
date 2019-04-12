Ext.define("Rds.appraisal.panel.AppraisalStandardFormPanel",{
	extend:'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var typeStore = Ext.create('Ext.data.Store',{
    	    fields:['type_id','standard_name'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'appraisal/type/queryAll.do',
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
    			xtype:"textfield",
    			fieldLabel: 'standard_id',
    			labelWidth:100,
    			labelAgin:'top',
    			fieldStyle:me.fieldStyle, 
    			name: 'standard_id',
    			hidden:true
    		},{
    			xtype: 'combo',
    			autoSelect : true,
    			editable:false,
    			allowBlank:false,
    			fieldLabel:'鉴定类型',
    	        name:'type_id',
    	        emptyText : "请选择",
    	        allowBlank:false,
    	        triggerAction: 'all',
    	        queryMode: 'local', 
    	        selectOnTab: true,
    	        store: typeStore,
    	        fieldStyle: me.fieldStyle,
    	        displayField:'standard_name',
    	        valueField:'type_id',
    	        listClass: 'x-combo-list-small'
    		},{
    			xtype:"textarea",
    			fieldLabel: '标准内容',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			allowBlank:false,
    			name: 'content',
    			height:100,
    			maxLength: 500,
    		},{
    			xtype:"textfield",
    			fieldLabel: '级数',
    			labelWidth:100,
    			labelAgin:'top',
    			fieldStyle:me.fieldStyle, 
    			allowBlank:false,
    			name: 'series',
    			maxLength: 20
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
			url:"appraisal/type/saveStandard.do", 
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