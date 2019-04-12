Ext.define("Rds.bacera.form.BaceraHospitalAreaNameForm", {
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

		    	xtype : "combo",
				fieldLabel : '归属地区',
				labelWidth : 60,
				allowBlank:false,
				id:'areanameCharge',
				labelAlign : 'left',
				name : 'areacode',
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
						url:"judicial/dicvalues/getAreaInfo.do?area_code="+codeTemp,
						reader : {
							type : "json"
						}
					}
				}),
				displayField : 'value',
				valueField : 'id',
				typeAhead : false,
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
						if("" != codeTemp)
						{
							this.store.load();
						}
					}
				}
			},{
				xtype:'textfield',
				fieldLabel : '所属医院',
				labelWidth : 60,
				style:'margin-top:20px;',
				labelAlign : 'left',
				columnWidth : .50,
				allowBlank : false,
				name:'hospital'},
	        			{
			    			xtype:"textarea",
			    			fieldLabel: '备注',
							style:'margin-top:20px;',
			    			labelWidth:60,
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
		values["areaname"]=Ext.getCmp('areanameCharge').getRawValue();
		if (form.isValid()) {
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({
				url : "bacera/invasivePre/saveHospitalAreaName.do",
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