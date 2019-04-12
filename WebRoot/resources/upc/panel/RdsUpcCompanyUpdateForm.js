var laboratory=Ext.create(
    'Ext.data.Store',
    {
        fields: ['laboratory_name','laboratory_no'],
        proxy:{
            type: 'jsonajax',
            actionMethods:{read:'POST'},
            url:'upc/laboratory/queryLaboratory.do',
            reader:{
                type:'json',
                root:'data'
            }
        },
        remoteSort:true
    }
);

Ext.define('Rds.upc.panel.RdsUpcCompanyUpdateForm', {
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
    			fieldLabel: '企业编号',
    			labelWidth:100,
    			readOnly:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'companyid',
    			hidden:true
    		},{
    			xtype:"textfield",
    			fieldLabel: '企业名称',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'companyname',
    			allowBlank:false, //不允许为空
    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
    			maxLength: 20
    		},{
    			xtype:"textfield",
    			fieldLabel: '地址',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'address',
    			allowBlank:false, //不允许为空
    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
    			maxLength: 40
    		},{
    			xtype:"textfield",
    			fieldLabel: '企业代码',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'companycode',
    			allowBlank:false, //不允许为空
    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
    			maxLength: 10,
    			readOnly:true
    		},{
    			xtype:"textfield",
    			fieldLabel: '联系人',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'contact',
    			allowBlank:false, //不允许为空
    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
    			maxLength: 20
    		},{
    			xtype:"textfield",
    			fieldLabel: '联系电话',
    			labelWidth:100,
    			fieldStyle:me.fieldStyle, 
    			name: 'telphone',
    			allowBlank:false, //不允许为空 flex: 20,  
    			maxLength: 20
    			//regex: /(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/
    		},{
                xtype : "combo",
                fieldLabel : '所属实验室',
                labelWidth : 100,
                mode: 'remote',
                emptyText:'请选择实验室',
                name:'laboratory_no',
                valueField :"laboratory_no",
                displayField: "laboratory_name",
                store: laboratory,
                editable:false
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
        laboratory.load();
		me.callParent(arguments);
	},
	//验证后保存
	onSave:function(){
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if(form.isValid())
		{
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:"upc/company/verifyId_code.do", 
				method: "POST",
				async : false,
				headers: { 'Content-Type': 'application/json' },
				jsonData: {companycode:values["companycode"],companyid:values["companyid"]}, 
				success: function (response, options) {  
					response = Ext.JSON.decode(response.responseText); 	
					//判断企业编号是否存在
					if(response){
						Ext.MessageBox.alert("提示信息", "该企业编号存在，请重新输入！");
					}else{
						//保存企业信息
						Ext.Ajax.request({  
							url:"upc/company/save.do", 
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
				failure: function () {
					Ext.Msg.alert("提示", "网络异常<br>请联系管理员!");
				}
	      	});
			
		}
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	}
	
});