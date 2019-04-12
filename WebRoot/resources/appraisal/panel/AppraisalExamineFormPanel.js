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
Ext.define("Rds.appraisal.panel.AppraisalExamineFormPanel",{
	extend:'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
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
						readOnly:true
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
					readOnly:true,
					allowBlank : false,
					// value : Ext.Date.add(new Date(),
					// Ext.Date.DAY, -7),
//					maxValue : new Date()
				});
		var identify_date_start = new Ext.form.DateField(
				{
					name : 'identify_date_start',
					columnWidth : .50,
					fieldLabel : '鉴定日期',
					labelWidth : 80,
					labelAlign : 'left',
					afterLabelTextTpl : me.required,
					emptyText : '请选择日期',
					format : 'Y-m-d',
					readOnly:true,
					allowBlank : false,
//					maxValue : new Date()
				});
		var identify_date_end = new Ext.form.DateField({
			name : 'identify_date_end',
			columnWidth : .50,
			fieldLabel : '到',
			labelWidth : 20,
			labelAlign : 'right',
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			readOnly:true,
			allowBlank : false,
//			maxValue : new Date()
		});
		var identify_per_both = new Ext.form.DateField({
			name : 'identify_per_both',
			columnWidth : .50,
			fieldLabel : '出生年月',
			labelWidth : 70,
			labelAlign : 'right',
			readOnly:true,
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			allowBlank : false,
			maxValue : new Date()
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
						xtype : "textfield",
						labelAlign : 'left',
						labelWidth : 80,
						fieldLabel : '委 托 人',
						maxLength : 50,
						readOnly:true,
						name : 'entrust_per'
					},
					{
						xtype : "textfield",
						labelAlign : 'left',
						labelWidth : 80,
						style:'margin-top:10px;',
						fieldLabel : '委托函号',
						maxLength : 200,
						readOnly:true,
						name : 'entrust_num'
					},
					CheckBoxGroupTypes,{
						xtype : "textfield",
						labelAlign : 'left',
						labelWidth : 80,
						style:'margin-top:10px;',
						fieldLabel : '委托事项',
						maxLength : 200,
						readOnly:true,
						name : 'entrust_matter'
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
									maxLength : 200,
									readOnly:true,
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
						maxLength : 200,
						readOnly:true,
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
							readOnly:true,
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
						    	        triggerAction: 'all',
						    	        queryMode: 'local', 
						    	        emptyText : "请选择",
						    	        readOnly:true,
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
							labelWidth : 45,
							fieldLabel : '住址',
							maxLength : 100,
							readOnly:true,
							name : 'identify_per_address'
						},{
							columnWidth : .25,
							xtype : "textfield",
							labelWidth : 70,
							fieldLabel : '身份证号',
							maxLength : 20,
							readOnly:true,
							name : 'identify_per_idcard'
						}  ]
					}, {
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
					
					},{
						xtype: 'combo',
						autoSelect : true,
						labelWidth : 80,
						fieldLabel:'鉴定机构',
				        name:'judgename',
				        emptyText : "请选择",
				        allowBlank:false,
				        triggerAction: 'all',
				        queryMode: 'local', 
				        selectOnTab: true,
				        store: mechanismStore,
				        fieldStyle: me.fieldStyle,
				        displayField:'name',
				        valueField:'name',
				        readOnly:true,
				        listClass: 'x-combo-list-small'
					}

			        ]}
		            ];
		
		me.buttons = [{
			text:'审核通过',
			iconCls:'Accept',
			handler:me.onSave
		},{
			text:'回退',
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
		var values = form.getValues();
		var values = '{"case_id":"'+values.case_id+'","flag_status":"3"}';
		console.log(values);
		Ext.Ajax.request({  
			url:"appraisal/info/examineBaseInfo.do", 
			method: "POST",
			headers: { 'Content-Type': 'application/json' },
			jsonData: values, 
			success: function (response, options) {  
				response = Ext.JSON.decode(response.responseText); 
                 if (response.result == true) {  
                 	Ext.MessageBox.alert("提示信息", "审核成功");
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
		var me = this.up("form");
		var form = me.getForm();
		if(!form.isValid()){
			Ext.MessageBox.alert("提示信息", "请正确填写完整表单信息!");
			return;
		}
		var values = form.getValues();
		var values = '{"case_id":"'+values.case_id+'","flag_status":"1"}';
		Ext.Ajax.request({  
			url:"appraisal/info/examineBaseInfo.do", 
			method: "POST",
			headers: { 'Content-Type': 'application/json' },
			jsonData: values, 
			success: function (response, options) {  
				response = Ext.JSON.decode(response.responseText); 
                 if (response.result == true) {  
                 	Ext.MessageBox.alert("提示信息", "回退成功");
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
	},listeners : {
		'afterrender' : function() {
			Ext.getCmp('recrive_id_temp').setValue(ownpersonTemp);
		}
	}
});