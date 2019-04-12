Ext.define("Rds.appraisal.panel.AppraisalBaseInfoFormPanel",{
	extend:'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		
		var entrustStore = Ext.create('Ext.data.Store',{
    	    fields:['id','name'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'appraisal/entrust/queryAll.do',
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
		var items = [];
		Ext.Ajax.request({
			url : 'appraisal/type/queryAll.do',
			params : {case_id:case_id_appraisal.split(',')[0]},
			async : false,
			success : function(response, opts) {
				var res = Ext.JSON
						.decode(response.responseText);
				var resLen = res.length;
				for (var i = 0; i < res.length; i++) {
					var d = res[i];
					var chk = {
						boxLabel : d.standard_name,
						name : 'entrust_type',
						inputValue : d.type_id,
						checked:d.checked,
						listeners:{
			                //通过change触发
			                change: function(obj, ischecked){
			                	var temp = Ext.getCmp("entrust_matter").getValue();
			                	if(ischecked)
			                		Ext.getCmp("entrust_matter").setValue(temp+obj.boxLabel+";");
			                	else
			                		Ext.getCmp("entrust_matter").setValue(temp.replace(obj.boxLabel+";",""));
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
		// 定义多选组
		var CheckBoxGroupTypes = new Ext.form.CheckboxGroup(
				{
					xtype : 'checkboxgroup',
					labelWidth : 80,
					fieldLabel : '鉴定类型',
					columns : 4,
					vertical : true,
					style : 'margin-bottom:10px;',
					height : 100,
					name:'entrust_type',
					autoScroll : true,
					//allowBlank : false,
					//blankText : "不能为空",
					items : items
				});
		var accept_date = new Ext.form.DateField(
				{
					name : 'accept_date',
					columnWidth : .35,
					fieldLabel : '受理日期',
					labelWidth : 80,
					labelAlign : 'left',
					afterLabelTextTpl : me.required,
					emptyText : '请选择日期',
					format : 'Y-m-d',
//					allowBlank : false,
					// value : Ext.Date.add(new Date(),
					// Ext.Date.DAY, -7),
//					maxValue : new Date()
				});
		var identify_date_start = new Ext.form.DateField(
		{
			id:'identify_date_start',
			name : 'identify_date_start',
			columnWidth : .50,
			fieldLabel : '鉴定日期',
			labelWidth : 80,
			labelAlign : 'left',
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d',
//			allowBlank : false,
			listeners:{
				'select':function(){
					var start = Ext.getCmp('identify_date_start').getValue();
                    var endDate = Ext.getCmp('identify_date_end').getValue();
                    if(null != endDate)
                    {
                        if (start > endDate) {
                            Ext.getCmp('identify_date_start').setValue(endDate);
                        }
                    }
				}
			}
		});
		var identify_date_end = new Ext.form.DateField({
			id:'identify_date_end',
			name : 'identify_date_end',
			columnWidth : .50,
			fieldLabel : '到',
			labelWidth : 20,
			labelAlign : 'right',
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d',
//			allowBlank : false,
			listeners:{
				'select':function(){
					var start = Ext.getCmp('identify_date_start').getValue();
                    var endDate = Ext.getCmp('identify_date_end').getValue();
                    if (start > endDate) {
                        Ext.getCmp('identify_date_start').setValue(endDate);
                    }
				}
			}
//			maxValue : new Date()
		});
		var identify_per_both = new Ext.form.DateField({
			name : 'identify_per_both',
			columnWidth : .50,
			fieldLabel : '出生年月',
			labelWidth : 70,
			labelAlign : 'right',
			afterLabelTextTpl : me.required,
//			emptyText : '请选择日期',
			format : 'Y-m-d',
//			allowBlank : false,
			maxValue : new Date()
		});
		var mechanismStore = Ext.create('Ext.data.Store',{
    	    fields:['id','name'],
    	    autoLoad:true,
    	    proxy:{
    	        type:'jsonajax',
    	        actionMethods:{read:'POST'},
    	        url:'appraisal/entrust/queryAllMechanism.do',
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
					{
						xtype : "textfield",
						labelAlign : 'left',
						labelWidth : 80,
						fieldLabel : 'case_id',
						name : 'case_id',
						hidden:true
					},
					{
						xtype: 'combo',
		    			autoSelect : true,
		    			editable:true,
		    			//allowBlank:false,
		    			labelWidth : 80,
						fieldLabel : '委 托 人',
		    	        name:'entrust_per',
		    	        emptyText : "请选择",
		    	        triggerAction: 'all',
		    	        queryMode: 'local', 
		    	        selectOnTab: true,
		    	        store: entrustStore,
		    	        fieldStyle: me.fieldStyle,
		    	        displayField:'name',
		    	        valueField:'name',
		    	        maxLength : 20,
		    	        listClass: 'x-combo-list-small'
					},
					{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [
								{
						xtype : "textfield",
						labelAlign : 'left',
						labelWidth : 80,
						style:'margin-top:10px;',
						fieldLabel : '案例编号',
//						allowBlank : false,// 不允许为空
//						blankText : "不能为空",// 错误提示信息，默认为This
						maxLength : 50,
						name : 'case_number'
					},{
						xtype : "textfield",
						labelAlign : 'right',
						labelWidth : 80,
						style:'margin-top:10px;',
						fieldLabel : '委托函号',
//						allowBlank : false,// 不允许为空
//						blankText : "不能为空",// 错误提示信息，默认为This
						maxLength : 50,
						name : 'entrust_num'
					}]
					},
					CheckBoxGroupTypes,{
						xtype : "textfield",
						labelAlign : 'left',
						labelWidth : 80,
						style:'margin-top:10px;',
						fieldLabel : '委托事项',
//						allowBlank : false,// 不允许为空
//						blankText : "不能为空",// 错误提示信息，默认为This
						maxLength : 50,
						name : 'entrust_matter',
						id:'entrust_matter'
					},
					{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						items : [
								accept_date,
								{
									columnWidth : .65,
									xtype : "textfield",
									labelAlign : 'right',
									afterLabelTextTpl : me.required,
									labelWidth : 80,
									fieldLabel : '鉴定材料',
//									allowBlank : false,// 不允许为空
//									blankText : "不能为空",// 错误提示信息，默认为This
									maxLength : 60,
									name : 'identify_stuff'
								} ]
					},
					{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						style:'margin-top:10px;',
						items : [
								identify_date_start,
								identify_date_end,
								]
					},{
						xtype : "textfield",
						labelAlign : 'left',
						labelWidth : 80,
						fieldLabel : '鉴定地点',
//						allowBlank : false,// 不允许为空
//						blankText : "不能为空",// 错误提示信息，默认为This
						maxLength : 200,
						name : 'identify_place'
					} ,
					{
						xtype : 'fieldset',
						title : '被鉴定人',
						name : 'identify_per',
						style:'margin-top:10px;',
						layout : 'anchor',
						defaults : {
							anchor : '100%'
						},
						items : [ {
							columnWidth : .1,
							xtype : "textfield",
							labelAlign : 'left',
							labelWidth : 40,
							fieldLabel : '姓名',
//							allowBlank : false,// 不允许为空
//							blankText : "不能为空",// 错误提示信息，默认为This
							maxLength : 50,
							name : 'identify_per_name'
						},{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [
									{
										columnWidth : .50,
										xtype: 'combo',
						    			autoSelect : true,
						    			editable:false,
						    			labelWidth : 40,
						    			fieldLabel:'性别',
						    	        name:'identify_per_sex',
//						    	        allowBlank:false,
						    	        triggerAction: 'all',
						    	        queryMode: 'local', 
						    	        emptyText : "请选择",
						    	        selectOnTab: true,
						    	        store: Ext.create('Ext.data.Store', { 
						                	fields: ['id', 'name'], 
						                	data : [{"id":"女","name":"女"},
						                	         {"id":"男","name":"男"}]
						                }),
						    	        fieldStyle: me.fieldStyle,
						    	        displayField:'name',
						    	        valueField:'id',
						    	        listClass: 'x-combo-list-small'
									},
									identify_per_both
									]
						},{
							columnWidth : .35,
							xtype : "textfield",
							labelWidth : 40,
							fieldLabel : '住址',
//							allowBlank : false,// 不允许为空
//							blankText : "不能为空",// 错误提示信息，默认为This
							maxLength : 50,
							name : 'identify_per_address'
						},{
							columnWidth : .25,
							xtype : "textfield",
							labelWidth : 70,
							fieldLabel : '身份证号',
//							allowBlank : false,// 不允许为空
//							blankText : "不能为空",// 错误提示信息，默认为This
							maxLength : 20,
							name : 'identify_per_idcard'
						}  ]
					},{
						xtype : 'hiddenfield',
						name : 'recrive_id'
					},
					{
						xtype : 'combo',
						fieldLabel : '案例归属<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
						labelWidth : 80,
						name : 'recrive_id_temp',
						id : 'recrive_id_temp',
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
					
					}
					,
//					{
//						xtype : 'combo',
//						columnWidth : .45,
//						fieldLabel : '归属人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
//						labelWidth : 80,
//						labelAlign : 'left',
//						name : 'recrive_id',
//						id:'case_receiver_add',
//						emptyText:'检索方式(姓名首字母)：如小红(xh)',
//						store : Ext.create("Ext.data.Store",
//										{
//											 fields:[
//									                  {name:'key',mapping:'key',type:'string'},
//									                  {name:'value',mapping:'value',type:'string'},
//									                  {name:'name',mapping:'name',type:'string'},
//									                  {name:'id',mapping:'id',type:'string'},
//									          ],
//											pageSize : 10,
//											// autoLoad: true,
//											proxy : {
//												type : "ajax",
//												url:"judicial/dicvalues/getUpcUsers.do?area_id="+ownpersonTemp,
//												reader : {
//													type : "json"
//												}
//											}
//										}),
//						displayField : 'name',
//						valueField : 'id',
//						allowBlank  : false,//不允许为空  
//						blankText   : "不能为空",//错误提示信息，默认为This field is required!
//						typeAhead : false,
//						hideTrigger : true,
//						forceSelection: true,
//						minChars : 2,
//						matchFieldWidth : true,
//						listConfig : {
//							loadingText : '正在查找...',
//							emptyText : '没有找到匹配的数据'
//						},
//						listeners:{
//							'afterrender':function(){
//								if("" != ownpersonTemp)
//								{
//									this.store.load();
//								}
//							}
//						}
					
//						xtype : "combo",
//						fieldLabel : '归属人',
//						labelAlign: 'left',
//						columnWidth : .45,
//						labelWidth : 80,
//						mode: 'remote',   
//					    forceSelection: true,   
////					    allowBlank  : false,//不允许为空  
////					    blankText   : "不能为空",//错误提示信息，默认为This field is required!  
//					    emptyText:'检索方式：如小红(xh)',  
//						id:'case_receiver_add',
//						name:'recrive_id',
//						valueField :"id",   
//					    displayField: "name",   
//						store: receiverStore,
//						listeners: {
//							'beforequery' : function(e) {  
//				                var combo = e.combo;     
//				                if(!e.forceAll){     
//				                    var value = e.query;     
//				                    combo.store.filterBy(function(record,id){     
//				                        var text = record.get(combo.displayField);     
//				                        return (text.indexOf(value)!=-1);     
//				                    });  
//				                    combo.expand();     
//				                    return false;     
//				                }  
//				            }  
//					    }
//					},
					{
						xtype: 'combo',
						autoSelect : true,
						//allowBlank:false,
						labelWidth : 80,
						fieldLabel:'鉴定机构',
				        name:'judgename',
				        emptyText : "请选择",
//				        allowBlank:false,
				        triggerAction: 'all',
				        queryMode: 'local', 
				        selectOnTab: true,
				        store: mechanismStore,
				        fieldStyle: me.fieldStyle,
				        displayField:'name',
				        valueField:'name',
				        listClass: 'x-combo-list-small'
					},{
						border : false,
						xtype : 'fieldcontainer',
						layout : "column",
						hidden:true,
						style:'margin-top:10px;',
						items : [{
										// 该列在整行中所占的百分比
										columnWidth : .33,
										xtype : "numberfield",
										value:0,
									    maxLength :7,
										labelWidth : 80,
										allowBlank : false,// 不允许为空
										fieldLabel : '标准金额<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
										maxLength : 50,
										name : 'stand_sum',
										listeners: {  
									        change: function(field, value) {  
									            if(value<0){
									            	 field.setValue(0); 
									            } 
									        }  
									   }
									}, {
										// 该列在整行中所占的百分比
										columnWidth : .33,
										xtype : "numberfield",
										value:0,
									    maxLength :7,
										labelWidth : 80,
										allowBlank : false,// 不允许为空
										fieldLabel : '实收金额<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
										maxLength : 50,
										name : 'real_sum',
										listeners: {  
									        change: function(field, value) {  
									            if(value<0){
									            	 field.setValue(0); 
									            } 
									        }  
									   }
									},
									{
										// 该列在整行中所占的百分比
										columnWidth : .33,
										xtype : "numberfield",
										value:0,
									    maxLength :7,
										labelWidth : 80,
										allowBlank : false,// 不允许为空
										fieldLabel : '回款金额<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
										maxLength : 50,
										name : 'return_sum',
										listeners: {  
									        change: function(field, value) {  
									            if(value<0){
									            	 field.setValue(0); 
									            } 
									        }  
									   }
									}
								]
					}
			        ]}
		            ];
		
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
		if(!form.isValid()){
			Ext.MessageBox.alert("提示信息", "请正确填写完整表单信息!");
			return;
		}
		Ext.MessageBox.wait('正在操作','请稍后...');
		var values = form.getValues();
		values["recrive_id"] = values["recrive_id_temp"];
		Ext.Ajax.request({  
			url:"appraisal/info/saveBaseInfo.do", 
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
		
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	},
	listeners : {
		'afterrender' : function() {
			var me = this;
			var values = me.getValues();
			var recrive_id = values["recrive_id"];
			Ext.getCmp('recrive_id_temp').setValue(recrive_id);
		}
	},
});