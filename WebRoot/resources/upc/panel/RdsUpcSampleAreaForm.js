Ext.define('Rds.upc.panel.RdsUpcSampleAreaForm', {
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
            items: [
//                    {
//				xtype : "combo",
//				fieldLabel : '员工',
//				autoSelect : true,
////				editable:true,
//				labelAlign: 'right',
//				columnWidth : .45,
//				labelWidth : 80,
//				mode: 'local', 
//				name:"userid",
//				allowBlank  : false,//不允许为空  
//	            blankText   : "不能为空",//错误提示信息，默认为This field is required! 
//		        forceSelection: true,   
//		        emptyText:'请选择员工',  
//		        triggerAction: 'all',
//		        queryMode: 'local', 
//				valueField :"key",   
//		        displayField: "value",   
//				store:searchStore
//			},
			{
				xtype : 'combo',
				fieldLabel : '员工',
				labelWidth : 80,
				name : 'userid',
				emptyText:'(人员首字母)：如小明(xm)',
				store :Ext.create("Ext.data.Store",{
					   fields:[
							{name:'key',mapping:'key',type:'string'},
							{name:'value',mapping:'value',type:'string'}
			          ],
					pageSize : 20,
					autoLoad: false,
					proxy : {
						type : "ajax",
						url:'judicial/dicvalues/getUsersId.do?userid='+personTemp,
						reader : {
							type : "json"
						}
					}
				}),
				displayField : 'value',
				valueField : 'key',
				columnWidth : 1,
				typeAhead : false,
				allowBlank:false, //不允许为空
				hideTrigger : true,
				forceSelection: true,
				minChars : 2,
				matchFieldWidth : true,
				listConfig : {
					loadingText : '正在查找...',
					emptyText : '没有找到匹配的数据'
				},
					listeners: {
						'afterrender':function(){
							if("" != personTemp)
							{
								this.store.load();
							}
						}
					}
			},
			{
			xtype : 'combo',
			fieldLabel : '归属地',
			labelWidth : 80,
			name : 'areacode',
			emptyText:'(地区首字母)：如上海市(shs)',
			store :Ext.create("Ext.data.Store",
					{
				 fields:[
		                  {name:'id',mapping:'id',type:'string'},
			              {name:'text',mapping:'text',type:'string'}
		          ],
				pageSize : 10,
				autoLoad: false,
				proxy : {
					type : "ajax",
					 url:'judicial/area/getAreaForPer.do?areacode='+areacodeTemp,
					reader : {
						type : "json"
					}
				}
			}),
			displayField : 'text',
			valueField : 'id',
			columnWidth : 1,
			typeAhead : false,
			allowBlank:false, //不允许为空
			hideTrigger : true,
			forceSelection: true,
			minChars : 2,
			matchFieldWidth : true,
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
				},
			listeners: {
				'afterrender':function(){
					if("" != areacodeTemp)
					{
						this.store.load();
					}
				}
				}
			},
			{
				xtype: 'radiogroup',
	            fieldLabel: '是否需要副本',
	            labelWidth : 100,
	            itemCls: 'x-check-group-alt',
	            columns: 2,
	            vertical: true,
	            items: [
	                {boxLabel: '是', name: 'attach_need', inputValue: 0},
	                {boxLabel: '否', name: 'attach_need', inputValue: 1, checked: true},
	            ]
			},
			{
				// 该列在整行中所占的百分比
				xtype : "textfield",
				labelAlign: 'right',
				fieldLabel : '预支额度',
				labelWidth : 80,
	            maxLength :10,
	            regex: /^\d+$/,
    			regexText: "只能为数字",
    			value:10000,
				name : 'charge_amount'
			},
			{
				// 该列在整行中所占的百分比
				xtype : "textfield",
				labelAlign: 'right',
				fieldLabel : '备注',
				labelWidth : 80,
	            maxLength :50,
				name : 'remark'
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
				url:"judicial/area/saveUpcUserInfo.do", 
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