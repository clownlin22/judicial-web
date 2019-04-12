Ext.define('Rds.upc.panel.RdsUpcPasswordGrid', {
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
            border:false,
            defaults : {
            	margins : "10"
            },
            autoHeight:true,
            items: [{
    			xtype:"textfield",
    			fieldLabel: '原密码',
    			labelWidth:80,
    			fieldStyle:me.fieldStyle, 
    			name: 'oldPasss',
    			id: 'oldPasss',
    			allowBlank:false, 
    			blankText:"不能为空",
    			inputType: 'password'
    		},{
    			xtype:"textfield",
    			fieldLabel: '新密码',
    			labelWidth:80,
    			fieldStyle:me.fieldStyle, 
    			name: 'newPass',
    			id: 'newPass',
    			allowBlank:false, 
    			blankText:"不能为空",
    			inputType: 'password'
    		},{
    			xtype:"textfield",
    			fieldLabel: '密码确认',
    			labelWidth:80,
    			fieldStyle:me.fieldStyle, 
    			name: 'passConfirm',
    			id: 'passConfirm',
    			allowBlank:false,
    			blankText:"不能为空", 
    			inputType: 'password'
    		},{
    			xtype:"button",
    			text: '保存',
    			margin: '5 15',
    			fieldStyle:me.fieldStyle,
    			listeners:{
    			        click:function(){
    			    		var form = me.getForm();
    			    		var values = form.getValues();
    			    		if(form.isValid())
    			    		{
    			    			if(Ext.getCmp('newPass').getValue().trim()!=Ext.getCmp('passConfirm').getValue().trim())
        			    		{
        			    			Ext.MessageBox.alert("提示信息","确认密码不一致");
        			    			return;
        			    		}
    			    			Ext.Ajax.request({  
        			    			url:"upc/user/updatePass.do", 
        			    			method: "POST",
        			    			headers: { 'Content-Type': 'application/json' },
        			    			jsonData: values, 
        			    			success: function (response, options) {  
        			    				response = Ext.JSON.decode(response.responseText); 
        			                     if (response.result == true) {  
        			                     	Ext.MessageBox.alert("提示信息", response.message,function(){
        			                     		window.location.href = "quit.do";
        			                     	});
        			                     }else { 
        			                     	Ext.MessageBox.alert("错误信息", response.message);
        			                     } 
        			    			},  
        			    			failure: function () {
        			    				Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
        			    			}
        			        	       
        			          	});
    			    		}
    			        	
    			       }
    			    }
    		},{
    			xtype:"button",
    			text: '重置',
    			margin: '5 5 5 5',
    			fieldStyle:me.fieldStyle,
    			 listeners:{
    			        click:function(){
    			        	Ext.getCmp('oldPasss').setValue(); 
    			        	Ext.getCmp('newPass').setValue(); 
    			        	Ext.getCmp('passConfirm').setValue(); 
    			       }
    			    }
    		}]
		}];
		
//		me.buttons = [{
//			text:'保存',
//			iconCls:'Disk',
//			handler:me.onSave
//		},{
//			text:'取消',
//			iconCls:'Cancel',
//			handler:me.onCancel
//		}]
		
		me.callParent(arguments);
	}
});