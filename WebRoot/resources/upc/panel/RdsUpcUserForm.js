Ext.define('Rds.upc.panel.RdsUpcUserForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		
		//公司列表数据源
		var companyStore = Ext.create('Ext.data.Store',{
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
    	});
		var userTypeStore = Ext.create('Ext.data.Store',{
			 fields:['typeid','typecode','typename'],
	    	    autoLoad:true,
	    	    proxy:{
	    	        type:'jsonajax',
	    	        actionMethods:{read:'POST'},
	    	        url:'upc/user/queryusertype.do',
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
		//公司列表combo
		var companyCombo = new Ext.form.ComboBox({
			autoSelect : true,
			editable:false,
			allowBlank:false,
			fieldLabel:'所属企业<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	        name:'companyid',
	        allowBlank:false,
	        labelWidth:65,
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
	        //readOnly:usercode==_super?false:true,
	        selectOnTab: true,
	        store: companyStore,
	        fieldStyle: me.fieldStyle,
	        displayField:'companyname',
	        valueField:'companyid',
	        listClass: 'x-combo-list-small'
		});
		var partnerModel = Ext.create('Ext.data.Store', {
			fields:[
	                  {name:'parnter_name',mapping:'parnter_name',type:'string'},
	          ],
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'upc/partnerConfig/queryall.do',
				params : {
					type : 'dna',
				},
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : false,
			remoteSort : true
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
    			fieldLabel: 'userid',
    			labelWidth:100,
    			hidden:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'userid'
    		},{
    			xtype:"textfield",
    			fieldLabel: 'userid',
    			labelWidth:100,
    			hidden:true,
    			fieldStyle:me.fieldStyle, 
    			name: 'relation_id'
    		},{
            	
            	border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
					xtype:"textfield",
	    			fieldLabel: '登录名<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    			labelWidth:65,
	    			columnWidth : .50,
	    			allowBlank:false,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'usercode',
	    			blankText:"字母数字组成",
	    			regex:/(^\w+$)/,
	    			maxLength: 20
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '真实名<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    			style:'margin-left:5px;',
	    			labelWidth:50,
	    			columnWidth : .50,
	    			allowBlank:false,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'username',
	    			maxLength: 10
	    		}]
            },{
            	border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
					xtype: 'combo',
	    			autoSelect : true,
	    			editable:false,
	    			fieldLabel:'性别<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    	        name:'sex',
	    	        columnWidth : .50,
	    	        labelWidth:65,
	    	        allowBlank:false,
	    	        triggerAction: 'all',
	    	        queryMode: 'local', 
	    	        emptyText : "请选择",
	    	        selectOnTab: true,
	    	        store: Ext.create('Ext.data.Store', { 
	                	fields: ['id', 'name'], 
	                	data : [{"id":"0","name":"女"},
	                	         {"id":"1","name":"男"}]
	                }),
	    	        fieldStyle: me.fieldStyle,
	    	        displayField:'name',
	    	        valueField:'id',
	    	        listClass: 'x-combo-list-small'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '地址',
	    			labelWidth:50,
	    			style:'margin-left:5px;',
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'address',
	    			maxLength: 20
	    		}]
            },companyCombo,{
            	border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
					xtype: 'combo',
	    			autoSelect : true,
//	    			editable:false,
	    			allowBlank:false,
//					forceSelection: true,
	    			fieldLabel:'用户类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    	        name:'usertype',
	    	        columnWidth : .50,
	    	        labelWidth:65,
	    	        emptyText : "请选择",
	    	        allowBlank:false,
	    	        triggerAction: 'all',
	    	        queryMode: 'local', 
	    	        selectOnTab: true,
	    	        store: userTypeStore,
	    	        fieldStyle: me.fieldStyle,
	    	        displayField:'typename',
	    	        valueField:'typename',
	    	        listClass: 'x-combo-list-small'
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '电话',
	    			labelWidth:30,
	    			style:'margin-left:5px;',
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'telphone',
	    			maxLength: 20
	    		}]
            },{
            	border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
					xtype:"textfield",
					fieldLabel: '邮件地址',
					columnWidth : 1,
	    	        labelWidth:65,
					fieldStyle:me.fieldStyle, 
					name: 'email',
					maxLength: 20
	    		}]
            
            },{
            	border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '员工编号',
	    			labelWidth:65,
	    			columnWidth : 1,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'webchart',
	    			maxLength: 20
	    		}]
            },{
            	border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [Ext.create('Ext.form.ComboBox',{
					xtype : 'combo',
					columnWidth : 1,
					fieldLabel : '管理地区',
					labelWidth : 65,
					name : 'areacode',
					id:'areacode',
					emptyText:'检索方式：如朝阳区(cy)',
					store : Ext.create("Ext.data.Store",{
						 fields:[
				                  {name:'key',mapping:'key',type:'string'},
				                  {name:'value',mapping:'value',type:'string'},
				                  {name:'name',mapping:'name',type:'string'},
				                  {name:'id',mapping:'id',type:'string'},
				          ],
						pageSize : 10,
						// autoLoad: true,
						proxy : {
							type : "ajax",
							url:"judicial/dicvalues/getAreaInfo.do?area_code="+ownaddressTemp,
							reader : {
								type : "json"
							}
						}
					}),
					displayField : 'value',
					valueField : 'id',
					forceSelection: true,
					typeAhead : false,
					hideTrigger : true,
					minChars : 2,
					matchFieldWidth : true,
					listConfig : {
						loadingText : '正在查找...',
						emptyText : '没有找到匹配的数据'
					},
					listeners : {
						'afterrender':function(){
							if("" != ownaddressTemp)
							{
								this.store.load();
							}
						}
					}
		          })]
            },{
            	border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [Ext.create('Ext.form.ComboBox', {
					fieldLabel: "合作方",
                    labelWidth : 65,
                    editable:true,
                    triggerAction: 'all',
                    displayField: 'parnter_name',
                    columnWidth : 1,
                    valueField: 'parnter_name',
                    store: partnerModel,
                    mode: 'local',
                    name: 'parnter_name',
				})]
    		},{
            	border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
					xtype:"textfield",
	    			fieldLabel: '资质证号',
	    			columnWidth : .50,
	    	        labelWidth:65,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'certificateno',
	    			maxLength: 20
	    		}]
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
		values["receiver_area"] = Ext.getCmp("areacode").getRawValue();
		if(form.isValid()){
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:"upc/user/save.do", 
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