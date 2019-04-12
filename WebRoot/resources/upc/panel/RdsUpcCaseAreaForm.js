Ext.define('Rds.upc.panel.RdsUpcCaseAreaForm', {
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
	    	xtype : "combo",
			fieldLabel : '案例地区',
			labelWidth : 80,
			id:'area_add',
			labelAlign : 'right',
			name : 'code',
			emptyText:'检索方式：如朝阳区(cy)',
			store : Ext.create("Ext.data.Store",{
				 fields:[
		                  {name:'key',mapping:'key',type:'string'},
		                  {name:'value',mapping:'value',type:'string'},
		                  {name:'name',mapping:'name',type:'string'},
		                  {name:'id',mapping:'id',type:'string'},
		          ],
				pageSize : 10,
				proxy : {
					type : "ajax",
					url:"judicial/dicvalues/getAreaInfo.do",
					reader : {
						type : "json"
					}
				}
			}),
			displayField : 'value',
			valueField : 'id',
			allowBlank  : false,//不允许为空  
            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
			forceSelection: true,
			typeAhead : false,
			hideTrigger : true,
			minChars : 2,
			matchFieldWidth : true,
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
			}
		},
		{
			xtype : "textfield",
			labelAlign: 'right',
			fieldLabel : '收件人',
			labelWidth : 80,
            maxLength :50,
			name : 'achieve'
		},
		{
			xtype : "textfield",
			labelAlign: 'right',
			fieldLabel : '联系电话',
			labelWidth : 80,
            maxLength :50,
			name : 'phone'
		},
		
		{
			xtype : "textfield",
			labelAlign: 'right',
			fieldLabel : '地址',
			labelWidth : 80,
            maxLength :50,
			name : 'address'
		},
		{
			// 该列在整行中所占的百分比
			xtype : 'numberfield',
			labelAlign: 'right',
			fieldLabel : '份数',
			labelWidth : 80,
            maxLength :11,
            allowBlank  : false,//不允许为空  
            blankText   : "不能为空",
			name : 'print_copies',
			listeners: {  
		        change: function(field, value) {  
		            if(value<0){
		            	 field.setValue(0); 
		            } 
		        }  
		   }
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
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:"judicial/area/saveUpcAreaInfo.do", 
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