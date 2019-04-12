Ext.define('Rds.upc.panel.RdsUpcPartnerConfigForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var storeModel = Ext.create('Ext.data.Store',{
			fields:['key','value'],
			autoLoad:true,
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url : 'judicial/dicvalues/getReportModels.do',
                params:{
					type : 'dna'
                },
                reader: {
                    type: 'json',
                    root:'data',
                    totalProperty:'total'
                }
            }
		});
		var modeltype=Ext.create('Ext.form.ComboBox', 
				{
					xtype : "combo",
					fieldLabel : "模板类型",
					mode: 'local',   
					labelWidth : 60,
					editable:false,
					blankText:'请选择模板',   
			        emptyText:'请选择模板',   
					valueField :"key",   
			        displayField: "value", 
					name : 'report_model',
					store: storeModel
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
            items: [{xtype : 'hiddenfield',name:"id"},{
    			xtype:"textfield",
    			fieldLabel: '合作商',
    			labelWidth:60,
    			fieldStyle:me.fieldStyle, 
    			name: 'parnter_name',
    			allowBlank:false, //不允许为空
    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
    		},{
		    	xtype : "combo",
				fieldLabel : '地区',
				labelWidth : 60,
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
			},
			modeltype,
			{
    			xtype:"textfield",
    			fieldLabel: '资质费用',
    			labelWidth:60,
    			fieldStyle:me.fieldStyle, 
    			name: 'qualificationFee',
    		},{
    			xtype:"textarea",
    			fieldLabel: '备注',
    			labelWidth:60,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark'
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
				url:"upc/partnerConfig/save.do", 
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