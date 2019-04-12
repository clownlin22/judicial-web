var programStore = Ext.create('Ext.data.Store',{
    fields:['program_name'],
    autoLoad:true,
    proxy:{
    	type: 'jsonajax',
        actionMethods:{read:'POST'},
        params:{ program_type:'医学检测项'},
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
Ext.define('Rds.bacera.form.BaceraMedExamineListForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var report_date = new Ext.form.DateField(
		{
			name : 'report_date',
			fieldLabel : '预计出报告日期',
			labelWidth : 100,
			columnWidth : .50,
			style:'margin-left:5px',
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d'
		});
		var date = new Ext.form.DateField(
		{
			name : 'date',
			fieldLabel : '快递日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			labelWidth : 65,
			columnWidth : .50,
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			allowBlank:false, //不允许为空
			blankText:"不能为空", //错误提示信息，默认为This field is required! 
			value:new Date()
		});
		var entrustment_date = new Ext.form.DateField(
		{
			name : 'entrustment_date',
			fieldLabel : '委托日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
			labelWidth : 65,
			columnWidth : .50,
			style:'margin-left:5px',
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			allowBlank:false, //不允许为空
			blankText:"不能为空"
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
	    			id: 'num',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 20
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '姓名',
	    			labelWidth:60,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'name',
	    			style:'margin-left:5px',
	    			maxLength: 20
	    		}]
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [program,
//				         new Ext.form.ComboBox({
//							autoSelect : true,
//							editable:true,
//							allowBlank:false,
//							labelWidth : 65,
//			    			columnWidth : .50,
//							fieldLabel : '案例类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
//						    name:'program',
//						    triggerAction: 'all',
//						    queryMode: 'local', 
//						    emptyText : "请选择",
//						    selectOnTab: true,
//					        forceSelection:true,
//						    store: programStore,
//						    fieldStyle: me.fieldStyle,
//						    displayField:'program_name',
//						    valueField:'program_name',
//						    listClass: 'x-combo-list-small',
//						}),
//				         new Ext.form.ComboBox({
//						autoSelect : true,
//						editable:true,
//						allowBlank:false,
//						labelWidth : 65,
//						columnWidth : .50,
//						fieldLabel : '案例类型<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
//						labelAlign : 'right',
//				        name:'program',
//				        triggerAction: 'all',
//				        queryMode: 'local', 
//				        emptyText : "请选择",
//				        selectOnTab: true,
//				        store: Ext.create(
//						        'Ext.data.Store',
//						        {
//						          model:'model',
//						          proxy:{
//						        	type: 'jsonajax',
//						            actionMethods:{read:'POST'},
//						            url:'bacera/medExamine/queryProgram.do',
//						            reader:{
//						              type:'json',
//						              root:'data'
//						            }
//						          },
//						          autoLoad:true,
//						          remoteSort:true
//						        }
//						),
//				        fieldStyle: me.fieldStyle,
//				        displayField:'value',
//				        valueField:'key',
//				        listClass: 'x-combo-list-small',
//					}),
				
				]
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [date,entrustment_date]
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
					xtype: 'combo',
	    			autoSelect : true,
	    			editable:false,
	    			labelWidth : 65,
	    			fieldLabel:'样本类型',
	    	        name:'sampletype',
	    	        id:'sampletype',
	    	        triggerAction: 'all',
	    	        queryMode: 'local', 
	    	        emptyText : "请选择",
	    	        selectOnTab: true,
	    			columnWidth : 1,
	    	        store: Ext.create('Ext.data.Store', { 
	                	fields: ['id', 'name'], 
	                	data : [ {"id":"外周血","name":"外周血"},
	                	         {"id":"新鲜组织","name":"新鲜组织"},
	                	         {"id":"胸水","name":"胸水"},
	                	         {"id":"蜡块","name":"蜡块"},
	                	         {"id":"切片","name":"切片"},
	                	         {"id":"口腔唾液","name":"口腔唾液"},
	                	         {"id":"血卡","name":"血卡"},
	                	         {"id":"心肌液","name":"心肌液"},
	                	         {"id":"口腔拭子","name":"口腔拭子"},
	                	         {"id":"心肌液","name":"心肌液"},
	                	         {"id":"CTC样本类型","name":"CTC样本类型"},
	                	         {"id":"引产胎儿组织","name":"引产胎儿组织"},
	                	         {"id":"手术组织","name":"手术组织"},
	                	         {"id":"流产胚胎（绒毛）","name":"流产胚胎（绒毛）"},
	                	         {"id":"流产胚胎（胚毛）","name":"流产胚胎（胚毛）"}]
	                }),
	    	        fieldStyle: me.fieldStyle,
	    	        displayField:'name',
	    	        valueField:'id',
	    	        multiSelect:true,
	    	        listClass: 'x-combo-list-small',
	   				listeners :{
	                       	"select" : function(combo, record, index){
	                       		if(Ext.getCmp("num").getValue()==""){
	                       			Ext.MessageBox.alert("提示信息", "请先填写案例编号在选择类型");
	                       			Ext.getCmp("sampletype").setValue("");
	                       			return;
	                       		}
	                       		var type = combo.getValue();
	                       		console.log(type.length);
	                       		var temp = "";
	                       		for(var i = 0 ; i < type.length ; i ++){
	                       			switch(type[i])
	                       			{
	                       			case "外周血":temp += (Ext.getCmp("num").getValue()+"-1A,"+Ext.getCmp("num").getValue()+"-1B,");break; 
	                       			case "新鲜组织":temp += (Ext.getCmp("num").getValue()+"-1C,");break; 
	                       			case "胸水":temp += (Ext.getCmp("num").getValue()+"-1D,");break; 
	                       			case "蜡块":temp += (Ext.getCmp("num").getValue()+"-1E,");break; 
	                       			case "切片":temp += (Ext.getCmp("num").getValue()+"-1E,");break; 
	                       			case "口腔唾液":temp += (Ext.getCmp("num").getValue()+"-1F,");break; 
	                       			case "血卡":temp += (Ext.getCmp("num").getValue()+"-1G,");break; 
	                       			default:"";break; 
	                       			}
	                       		}
	                       		Ext.getCmp("barcode").setValue(temp);
	                       	}
	                }
//	    			xtype:"textfield",
//	    			fieldLabel: '样本类型',
//	    			labelWidth:65,
//	    			columnWidth : 1,
//	    			fieldStyle:me.fieldStyle, 
//	    			name: 'sampletype',
//	    			maxLength: 20
	    		}]
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '物流条码',
	    			labelWidth:65,
					columnWidth : 1,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'barcode',
	    			id:'barcode',
	    			maxLength: 200
	    		}]
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '样本数量',
	    			labelWidth:65,
	    			columnWidth : 1,
	    			fieldStyle:me.fieldStyle, 
	    			regex:/^[^\s]*$/,
					regexText:'请输入正确值',
	    			name: 'samplecount',
	    			maxLength: 20
	    		}]
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '送检医院',
	    			labelWidth:65,
	    			columnWidth : 1,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'hospital',
	    			maxLength: 20
	    		}]
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
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '年龄',
	    			labelWidth:65,
	    			columnWidth : .50,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'age',
	    			regex:/^[^\s]*$/,
					regexText:'请输入正确值',
	    			maxLength: 20
	    		},new Ext.form.field.ComboBox({
	    			fieldLabel : '性别',
	    			columnWidth : .50,
	    			labelWidth:60,
	    			editable : true,
	    			triggerAction : 'all',
	    			style:'margin-left:5px',
	    			displayField : 'Name',
	    			valueField : 'Code',
	    			store : new Ext.data.ArrayStore(
	    					{
	    						fields : ['Name','Code' ],
	    						data : [ ['男','男' ],['女','女' ] ]
	    					}),
	    			mode : 'local',
	    			name : 'sex',
	    		})]
			},{
				border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '临床诊断',
	    			labelWidth:65,
					columnWidth : 1,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'diagnosis',
	    			maxLength: 100
	    		}]
			},
			{
				xtype:"textarea",
    			fieldLabel: '备注要求',
    			labelWidth:65,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark',
    			height:80,
    			maxLength: 500,
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
	    		},report_date]
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
		var sampletype = "";
		for(var i = 0 ; i < values["sampletype"].length; i ++){
			sampletype+=values["sampletype"][i]+","
		}
		if(sampletype.endWith(","))
			values["sampletype"]=sampletype.substring(0,sampletype.length-1);
		if(values["barcode"].endWith(","))
			values["barcode"]=values["barcode"].substring(0,values["barcode"].length-1);
		if(form.isValid())
		{
         	Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:"bacera/medExamine/save.do", 
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