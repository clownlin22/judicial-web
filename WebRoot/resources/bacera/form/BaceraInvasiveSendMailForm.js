Ext.define('Rds.bacera.form.BaceraInvasiveSendMailForm', {
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
                labelWidth: 80
            },
            border:false,
            autoHeight:true,
            items: [{
    			xtype:"textfield",
    			fieldLabel: 'id',
    			labelWidth:80,
    			readOnly:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'id',
    			hidden:true
    		},{
    			xtype:"textfield",
    			fieldLabel: 'emailflag',
    			labelWidth:80,
    			readOnly:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'emailflag',
    			hidden:true
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '邮件主题<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    			labelWidth:70,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			readOnly:true,
	    			name: 'num',
	    			maxLength: 20,
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [
				    		{
				    			xtype:"textfield",
				    			fieldLabel: '收件地址<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
				    			labelWidth:70,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'receiveAddress',
				    			regex: /^(([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6}\;))*([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/  ,
				    			regexText: "请注意邮箱格式",
				    			emptyText : '多个邮件地址用;隔开',
				    			allowBlank:false, //不允许为空
				    			blankText:"不能为空", //错误提示信息，默认为This field is required! 	
				    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [
				    		{
				    			xtype:"textfield",
				    			fieldLabel: '附件<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
				    			labelWidth:70,
				    			readOnly:true,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'attachment_path',
				    		}]
    		},{
				xtype:"textarea",
    			fieldLabel: '邮件内容',
    			labelWidth:70,
    			fieldStyle:me.fieldStyle, 
    			name: 'content',
    			height:80,
    			maxLength: 200,
    			value:" "
    		}]
		}];
		
		me.buttons = [{
			text:'发送',
			iconCls:'Disk',
			handler:me.onSend
		},{
			text:'取消',
			iconCls:'Cancel',
			handler:me.onCancel
		}]
		me.callParent(arguments);
	},
	//验证后保存
	onSend:function(){
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if(form.isValid())
		{
         	Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:'bacera/sendEmail/sendEmail.do', 
				method: "POST",
				timeout: 100000000,
				headers: { 'Content-Type': 'application/json' },
				jsonData: values, 
				success: function (response, options) {  
					response = Ext.JSON.decode(response.responseText); 
	                 if (response.result == true) {  
	                 	Ext.MessageBox.alert("提示信息", response.message);
	                 	me.grid.getStore().load();
	                 	me.up("window").close();
	                 }else { 
	                 	Ext.MessageBox.alert("错误信息", "发送失败！请联系管理员；<br>"+response.message);
	                 } 
				},  
				failure: function () {
					Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
				}
	    	       
	      	});
		}
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	}
});