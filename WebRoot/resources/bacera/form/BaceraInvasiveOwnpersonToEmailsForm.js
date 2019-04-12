Ext.define("Rds.bacera.form.BaceraInvasiveOwnpersonToEmailsForm", {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'form',
			region : 'center',
			name : 'data',
			style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
			labelAlign : "right",
			bodyPadding : 10,
			defaults : {
				anchor : '100%',
				labelWidth : 100
			},
			border : false,
			autoHeight : true,
			items : [{xtype : 'hiddenfield',name:"id"},{
				layout : "column",// 从左往右的布局
				xtype : 'fieldcontainer',
				border : false,
				items : [ {
					xtype : 'combo',
					fieldLabel : '归属人<span style="color:red">*</span>',
					labelWidth : 65,
					columnWidth : 1,
					labelAlign : 'left',
					name : 'ownperson',
                    id:'ownperson',
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
							url:'judicial/dicvalues/getUsersId.do?userid='+ownpersonTemp,
							reader : {
								type : "json"
							}
						}
					}),
					displayField : 'value',
					valueField : 'key',
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
							if("" != ownpersonTemp)
							{
								this.store.load();
							}
						}
						}
				}]
			},{
				xtype:'textfield',
				fieldLabel : '邮箱地址<span style="color:red">*</span>',
				labelWidth : 65,
				style:'margin-top:20px;',
				labelAlign : 'left',
				columnWidth : .50,
				regex: /^(([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6}\;))*([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/  ,
    			regexText: "请注意邮箱格式",
    			emptyText : '多个邮件地址用;隔开',
				allowBlank : false,
				name:'toEmails'},
	        			{
			    			xtype:"textarea",
			    			fieldLabel: '备注',
							style:'margin-top:20px;',
			    			labelWidth:65,
			    			labelAlign : 'left',
			    			fieldStyle:me.fieldStyle, 
			    			name: 'remark',
			    		} ]
		}];

		me.buttons = [{
					text : '保存',
					iconCls : 'Disk',
					handler : me.onSave
				}, {
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	onSave : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		values["ownpersonname"]=Ext.getCmp('ownperson').getRawValue();
		values["ownperson"]=Ext.getCmp('ownperson').getValue();
		if (form.isValid()) {
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({
				url : "bacera/invasivePre/saveOwnpersonToEmails.do",
				method : "POST",
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : values,
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					if (response.result == true) {
						Ext.MessageBox.alert("提示信息", response.message);
						me.grid.getStore().load();
						me.up("window").close();
					} else {
						Ext.MessageBox.alert("错误信息", response.message);
					}
				},
				failure : function() {
					Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
				}

			});
		}

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}
});