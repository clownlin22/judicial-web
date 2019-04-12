var programStore = Ext.create('Ext.data.Store',{
    fields:['program_name'],
    autoLoad:true,
    proxy:{
    	type: 'jsonajax',
        actionMethods:{read:'POST'},
        params:{ program_type:'地中海贫血'},
        url:'bacera/program/queryall.do',
        reader:{
          type:'json',
        },
        writer: {
            type: 'json'
       }
      },
      remoteSort:true
});
Ext.define('Rds.bacera.form.BaceraMedThalassemiaListForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var date = new Ext.form.DateField(
		{
			name : 'date',
			fieldLabel : '日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			labelWidth : 65,
			columnWidth : .50,
			labelAlign : 'left',
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			allowBlank:false, //不允许为空
			blankText:"不能为空", //错误提示信息，默认为This field is required! 
			value:new Date()
		});
		var program = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			allowBlank:false,
			labelWidth : 65,
			columnWidth : 1,
			fieldLabel : '案例类型<span style="color:red">*</span>',
		    name:'program',
		    forceSelection:true,
		    triggerAction: 'all',
		    queryMode: 'local', 
		    emptyText : "请选择",
		    selectOnTab: true,
		    store: programStore,
		    fieldStyle: me.fieldStyle,
		    displayField:'program_name',
		    valueField:'program_name',
		    listClass: 'x-combo-list-small',
		})
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
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '案例编号<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
	    			labelWidth:65,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'num',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 20
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '物流条码',
	                style:'margin-left:5px',
	    			labelWidth:60,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'code',
	    			maxLength: 20
	    		}]
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '姓名',
	    			labelWidth:65,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'name',
	    			maxLength: 20
	    		},{
	                xtype : "combo",
	                fieldLabel : '送检人',
	                labelWidth : 60,
	                mode: 'remote',
	                columnWidth : .50,
	                editable : true,
	                emptyText:'请选择鉴定人',
	                name:'inspection',
//	                id:'identify_id',
	                style:'margin-left:5px',
	                valueField :"Name",
	                displayField: "Name",
	                store: new Ext.data.ArrayStore(
							{
								fields : [
										'Name',
										'Code' ],
								data : [[
											'袁其镇',
											'袁其镇' ],
									[
											'樊伟涛',
											'樊伟涛' ],
									[
											'胡少江',
											'胡少江' ],
									[
											'叶田鹏',
											'叶田鹏' ],
									[
											'刘志强',
											'刘志强' ],
									[
											'迟淑晶',
											'迟淑晶' ],
									[
											'姚明清',
											'姚明清' ]
]
							}),
//	                multiSelect:true
//	    			xtype:"textfield",
//	    			fieldLabel: '送检人',
//	    			labelWidth:60,
//	    			columnWidth : .50,
//	    			fieldStyle:me.fieldStyle, 
//	    			name: 'inspection',
//	    			style:'margin-left:5px',
//	    			maxLength: 20
	    		}]
			
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [program,
//				         Ext.create('Ext.form.ComboBox', {
//					fieldLabel : '案例类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
//					width : 200,
//					labelWidth : 65,
//					autoSelect : true,
//					editable : true,
//					triggerAction: 'all',
//			        queryMode: 'local', 
//			        emptyText : "请选择",
//			        selectOnTab: true,
//					valueField :"key",  
//					columnWidth : .50,
//			        displayField: "value", 
//					store: Ext.create(
//					        'Ext.data.Store',
//					        {
//					          model:'model',
//					          proxy:{
//					        	type: 'jsonajax',
//					            actionMethods:{read:'POST'},
//					            url:'bacera/medThalassemia/queryProgram.do',
//					            reader:{
//					              type:'json',
//					              root:'data'
//					            }
//					          },
//					          autoLoad:true,
//					          remoteSort:true
//					        }
//					),
//					mode : 'local',
//					name : 'program',
//			        listClass: 'x-combo-list-small',
//				}),
				]
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [date]
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
					xtype : 'combo',
					fieldLabel : '案例归属<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
					labelWidth : 65,
					name : 'ownperson',
					emptyText:'(姓名/地区首字母)：如小红(xh)',
					store :Ext.create("Ext.data.Store",
							{
						 fields:[
				                  {name:'id',mapping:'id',type:'string'},
				                  {name:'ascription',mapping:'ascription',type:'string'}
				          ],
						pageSize : 10,
						autoLoad: false,
						proxy : {
							type : "ajax",
							url:"finance/chargeStandard/getAscriptionPer.do?id="+ownpersonTemp,
							reader : {
								type : "json"
							}
						}
					}),
					displayField : 'ascription',
					valueField : 'id',
					typeAhead : false,
					hideTrigger : true,
					forceSelection: true,
					columnWidth : 1,
					minChars : 2,
					allowBlank:false, //不允许为空
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
			},
			{
				xtype:"textarea",
    			fieldLabel: '备注要求',
    			labelWidth:65,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark',
    			height:80,
    			maxLength: 100,
    		},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"numberfield",
	    			fieldLabel: '应收款项<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    			labelWidth:65,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'receivables',
	    			id: 'receivables',
	    			maxLength: 20,
	    			allowBlank:false, //不允许为空
        			blankText:"不能为空", //错误提示信息，默认为This field is required!
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
	//验证后保存
	onSave:function(){
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if(form.isValid())
		{
         	Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:"bacera/medThalassemia/save.do", 
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
	                 	Ext.MessageBox.alert("错误信息", "更新失败！请联系管理员；<br>"+response.message);
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
	},
	listeners : {
		'beforerender' : function() {
			programStore.load();
		},
		'afterrender' : function() {
			if(confirm_flagTemp == '2')
				Ext.getCmp("receivables").readOnly=true;
		}
	}
	
});