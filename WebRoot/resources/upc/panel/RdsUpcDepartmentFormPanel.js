Ext.define('Rds.upc.panel.RdsUpcDepartmentFormPanel', {
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
    			fieldLabel: '部门编号',
    			labelWidth:100,
    			readOnly:true,
    			fieldStyle:me.fieldStyle, 
    			hidden:true,
    			name: 'deptid'
    		},{
    			xtype:"textfield",
    			fieldLabel: '企业id',
    			hidden:true,
    			labelWidth:100,
    			readOnly:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'companyidtemp'
    		},{
    			xtype:"textfield",
    			fieldLabel: '部门名称',
    			allowBlank:false,
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'deptname',
    			maxLength: 20
    		},{
    			xtype: 'combo',
    			autoSelect : true,
    			editable:false,
    			//disabled:true,
    			hidden:false,
    			allowBlank:false,
    			fieldLabel:'所属企业',
    	        name:'companyid',
    	        triggerAction: 'all',
    	        queryMode: 'local', 
    	        emptyText : "请选择",
//    	        value:companyid,
//    	        readOnly:usercode==_super?false:true,
    	        selectOnTab: true,
    	        store: Ext.create('Ext.data.Store',{
    	    	    fields:['companyid','companyname'],
    	    	    autoLoad:true,
    	    	    proxy:{
    	    	        type:'jsonajax',
    	    	        actionMethods:{read:'POST'},
    	    	        url:'upc/company/queryall.do',
    	    	        params:{
    	    	        },
    	    	        render:{
    	    	            type:'json'
    	    	        },
    	    	        writer: {
    	    	            type: 'json'
    	    	       }
    	    	    }
    	    	}),
    	        fieldStyle: me.fieldStyle,
    	        displayField:'companyname',
    	        valueField:'companyid',
    	        listClass: 'x-combo-list-small'
    	   },{
    		   xtype:"textfield",
    		   fieldLabel: '上级部门',
    		   readOnly:true,
    		   labelWidth:100,
    		   fieldStyle:me.fieldStyle, 
    		   name: 'parentid',
    		   hidden: true
    	   },{
    		   xtype:"textfield",
    		   fieldLabel: '上级部门编号',
    		   hidden:true,
    		   labelWidth:100,
    		   fieldStyle:me.fieldStyle, 
    		   name: 'parentdeptcode',
    	   },{
    			xtype:"textfield",
    			fieldLabel: '部门编号',
    			allowBlank:false,
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'deptcode',
    			maxLength: 45
    		},{
                xtype:"combobox",
                labelWidth:100,
                fieldLabel: '是否为实验室',
                store:Ext.create('Ext.data.Store', {
                    fields: ['key', 'value'],
                    data : [
                        {"key":"是","value":"Y"},
                        {"key":"否","value":"N"}
                    ]
                }),
                valueField:'value',
                displayField:'key',
                name:'islaboratory',
                itemId:'islaboratory'
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
		if(form.isValid())
		{
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:"upc/department/save.do", 
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
		}
		
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	}
	
});