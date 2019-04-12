Ext.define('Rds.bacera.form.BaceraTumorPreListForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
//		var items = [];
//        Ext.Ajax.request({
//            url : 'upc/tumorPre/queryAllTumorPerItems.do',
//            //params : {case_id:case_id_appraisal.split(',')[0]},
//            async:false,
//            method: "POST",
//            headers: { 'Content-Type': 'application/json' },
//            jsonData:{abc:1},
//            success : function(response, opts) {
//                var res = Ext.JSON.decode(response.responseText);
//                for (var i = 0; i < res.length; i++) {
//                    var d = res[i];
//                    var chk = {
//                        boxLabel : d.items_name,
//                        name : 'testitems',
//                        inputValue : d.items_name
//                    };
//                    items.push(chk);
//                }
//            },
//            failure : function(response, opts) {
//            }
//        });
		var items = [];
		
		Ext.Ajax.request({
			url : 'bacera/program/queryAllAjax.do',
			params : {program_type:'肿瘤个体'},
			async : false,
			success : function(response, opts) {
				var res = Ext.JSON
						.decode(response.responseText);
				var resLen = res.length;
				for (var i = 0; i < res.length; i++) {
					var d = res[i];
					var chk = {
						boxLabel : d.program_name,
						name : 'entrust_type',
						listeners:{
			                //通过change触发
			                change: function(obj, ischecked){
			                	var temp = Ext.getCmp("testitems").getValue();
			                	if(ischecked)
			                		Ext.getCmp("testitems").setValue(temp+obj.boxLabel+";");
			                	else
			                		Ext.getCmp("testitems").setValue(temp.replace(obj.boxLabel+";",""));
//			                	if(newValue.appendix_status=='1')
//			                		alert(g);
////			                		Ext.getCmp("appendix_desc").setDisabled(false);
//			                	else{
//			                		alert(1);
////			                		Ext.getCmp("appendix_desc").setDisabled(true);
////			                		Ext.getCmp("appendix_desc").setRawValue('');
//			                		}
			                }
			            }
					};
					items.push(chk);
				}
			},
			failure : function(response, opts) {
			}
		});
		var CheckBoxGroupTypes = new Ext.form.CheckboxGroup(
				{
					xtype : 'checkboxgroup',
					labelWidth : 80,
					fieldLabel : '鉴定类型',
					columns : 4,
	    			columnWidth : 1,
					vertical : true,
					style : 'margin-bottom:10px;',
					height : 200,
					name:'entrust_type',
					autoScroll : true,
					//allowBlank : false,
					//blankText : "不能为空",
					items : items
				});
		var date = new Ext.form.DateField(
				{
					name : 'date',
					fieldLabel : '日期<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
					labelWidth : 70,
					labelAlign : 'left',
	    			columnWidth : .5,
					afterLabelTextTpl : me.required,
					emptyText : '请选择日期',
					format : 'Y-m-d',
					allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required!
	    			value:new Date()
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
				style:'margin-top:10px;',
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '案例编号<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
	    			labelWidth:70,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'num',
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    			maxLength: 20
	    		},
//	    		{
//    				xtype: 'combo',
//        			autoSelect : true,
//        			editable:false,
//        			labelWidth : 70,
//        			fieldLabel:'检测项目',
//        	        name:'testitems',
//	                editable : true,
//        	        triggerAction: 'all',
//        	        queryMode: 'local', 
//        	        labelAlign: 'right',
//        	        emptyText : "请选择",
//        	        selectOnTab: true,
//        	        store: Ext.create('Ext.data.Store', { 
//                    	fields: ['id', 'name'], 
//                    	data : [{"id":"EGFR","name":"EGFR"},
//                    	         {"id":"BRAF","name":"BRAF"},
//                    	         {"id":"EGFR ERCC1 TUBB3 TYMS","name":"EGFR ERCC1 TUBB3 TYMS"}
//                    	]
//                    }),
//        	        fieldStyle: me.fieldStyle,
//        	        displayField:'name',
//        	        valueField:'id',
//        	        listClass: 'x-combo-list-small'
//                }
	    		]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [CheckBoxGroupTypes]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
					xtype : "textarea",
					labelAlign : 'left',
					labelWidth : 70,
					style:'margin-top:10px;',
					fieldLabel : '检测项目',
	    			columnWidth : 1,
					allowBlank : false,// 不允许为空
					blankText : "不能为空",// 错误提示信息，默认为This
					maxLength : 2000,
					name : 'testitems',
					id:'testitems',
					readOnly:true,
					listeners : { 
						render: function(p) { 
						      // Append the Panel to the click handler's argument list. 
						     p.getEl().on('dblclick', function(p){ 
						    	 Ext.getCmp("testitems").setValue("");
						     }); 
						  }, 
					}
				}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [date,{
    				xtype: 'combo',
        			autoSelect : true,
        			editable:true,
	    			columnWidth : .5,
        			labelWidth : 70,
        			fieldLabel:'送检人',
        	        name:'checkper',
        	        triggerAction: 'all',
        	        queryMode: 'local', 
        	        labelAlign: 'right',
        	        emptyText : "请选择",
        	        selectOnTab: true,
        	        sytle:'margin-left:2px',
        	        store: Ext.create('Ext.data.Store', { 
                    	fields: ['id', 'name'], 
                    	data : [{"id":"武汉","name":"武汉"},
                    	         {"id":"魏云军","name":"魏云军"},
                    	         {"id":"姚楠","name":"姚楠"},
                    	         {"id":"贾立先","name":"贾立先"},
                    	         {"id":"李丽玲","name":"李丽玲"},
                    	         {"id":"张明书","name":"张明书"},
                    	         {"id":"丁文杰","name":"丁文杰"},
                    	         {"id":"张鑫钢","name":"张鑫钢"},
                    	         {"id":"唐嘉麒","name":"唐嘉麒"}
                    	]
                    }),
        	        fieldStyle: me.fieldStyle,
        	        displayField:'name',
        	        valueField:'id',
        	        listClass: 'x-combo-list-small'
					
//	    			xtype:"textfield",
//	    			fieldLabel: '送检人',
//	    			labelWidth:60,
//	    			labelAlign:'right',
//	    			maxLength: 20,
//	    			sytle:'margin-left:2px',
//	    			fieldStyle:me.fieldStyle, 
//	    			name: 'checkper'
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
	    			xtype:"textfield",
	    			fieldLabel: '姓名<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    			labelWidth:70,
	    			columnWidth : .5,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'name',
	    			maxLength: 20,
	    			allowBlank:false, //不允许为空
	    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    		},{
	    			xtype:"textfield",
	    			fieldLabel: '送检医院',
	    			labelWidth:70,
	    			labelAlign:'right',
	    			maxLength: 20,
	    			columnWidth : .5,
	    			fieldStyle:me.fieldStyle, 
	    			name: 'hospital'
	    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [
				    		{
				    			xtype:"textfield",
				    			fieldLabel: '年龄',
				    			labelWidth:70,
				    			maxLength: 3,
				    			columnWidth : .5,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'age',
				    			regex: /^\d+$/,
				    			regexText: "只能为数字",
				    		},{
				    			xtype:"textfield",
				    			fieldLabel: '电话',
				    			labelWidth:70,
				    			columnWidth : .5,
				    			maxLength: 20,
				    			labelAlign:'right',
				    			fieldStyle:me.fieldStyle, 
				    			name: 'phonenum'
				    		}]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{

					xtype : 'combo',
					fieldLabel : '案例归属<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
					labelWidth : 70,
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
	    			columnWidth : 1,
					typeAhead : false,
					hideTrigger : true,
					allowBlank:false, //不允许为空
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
    			xtype:"textarea",
    			fieldLabel: '病理诊断',
    			labelWidth:70,
    			fieldStyle:me.fieldStyle, 
    			name: 'diagnosis',
    			height:80,
    			maxLength: 200
    			},{
    				xtype:"textarea",
        			fieldLabel: '备注要求',
        			labelWidth:70,
        			fieldStyle:me.fieldStyle, 
        			name: 'remark',
        			height:80,
        			maxLength: 200,
        		},
        		{
    				border : false,
    				xtype : 'fieldcontainer',
    				layout : "column",
    				items : [{
    	    			xtype:"numberfield",
    	    			fieldLabel: '应收款项<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
    	    			labelWidth:70,
    	    			columnWidth : .50,
    	    			fieldStyle:me.fieldStyle, 
    	    			name: 'receivables',
    	    			id: 'receivables',
    	    			maxLength: 20,
    	    			allowBlank:false, //不允许为空
            			blankText:"不能为空", //错误提示信息，默认为This field is required!
    	    		}]
    			}
			]
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
				url:"bacera/tumorPre/save.do", 
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
		'afterrender' : function() {
			if(confirm_flagTemp == '2')
				Ext.getCmp("receivables").readOnly=true;
		}
	}	
	
});