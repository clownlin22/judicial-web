var chengdanbumen = Ext.create('Ext.data.Store', {
	model : 'model',
	proxy : {
		type : 'jsonajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'statistics/financeOA/queryUserDept1.do',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	autoLoad : true,
	remoteSort : true
});
var deptId= Ext.getCmp('user_dept_level1');

var aaa;
var bbb;
var ccc;
var ddd;
var eee;
var zzz;
Ext.define('Rds.statistic.form.FinanceOAGridForm', {
	extend : 'Ext.form.Panel',
	initComponent : function() {
		var me = this;

		me.items = [{
			id:'items',
			xtype : 'form',
			defaults: {
				anchor: '100%',
				labelWidth: 80
			},		
			border : false,
			autoHeight : true,
			items : [{
				layout : "column",// 从左往右的布局
				xtype : 'fieldcontainer',
				border : false,
				//流程编号  承担部门名称  费用类型   申请人姓名   提单人姓名  金额   操作日期  申请日期  说明
				items : [{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					style : 'margin-left:25px;margin-right:5px;margin-top:40px;margin-bottom:5px;',
					items : [{
						xtype : "hiddenfield",
						name : "case_id"
					},{
						xtype:"textfield",
						fieldLabel: '流程编号',
						labelWidth:70,
						readOnly : true,
						fieldStyle:me.fieldStyle, 
						name: 'djbh',
						id: 'djbh',
						labelAlign : 'right',
						maxLength: 20
					},{
						xtype: 'combo',
						autoSelect : true,
						editable:false,
						labelWidth : 70,
						fieldLabel:'旧事业部',
						name:'ztsybmc',
						id:'ztsybmc',
						triggerAction: 'all',
						emptyText : "请选择",
						allowBlank:false, //不允许为空
						blankText:"不能为空", //错误提示信息，默认为This field is required! 
						fieldStyle: me.fieldStyle,
						displayField: "key",
						valueField :"key",
						labelAlign : 'right',
						store: chengdanbumen 
					}]
				},{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					style : 'margin-left:25px;margin-right:5px;margin-top:10px;margin-bottom:5px;',
					items : [{
						xtype:"textfield",
						fieldLabel: '申请人',
						labelWidth:70,
						fieldStyle:me.fieldStyle, 
						name: 'sqrxm',
						id: 'sqrxm',
						width:255,
						labelAlign : 'right',
						maxLength: 20
					},{
						xtype:"textfield",
						fieldLabel: '成本承担人',
						labelWidth:70,
						fieldStyle:me.fieldStyle, 
						name: 'tdrxm',
						id: 'tdrxm',
						width:255,
						labelAlign : 'right',
						maxLength: 20
					}]
				},{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					style : 'margin-left:25px;margin-right:5px;margin-top:10px;margin-bottom:5px;',
					items : [{
						xtype:"textfield",
						fieldLabel: '承担人工号',
						labelWidth:70,
						fieldStyle:me.fieldStyle, 
						name: 'tdrworkcode',
						id: 'tdrworkcode',
						width:255,
						labelAlign : 'right',
						maxLength: 20
					},{
						xtype:"textfield",
						fieldLabel: '费用科目',
						labelWidth:70,
						fieldStyle:me.fieldStyle, 
						name: 'kmmc',
						id: 'kmmc',
						width:255,
						labelAlign : 'right',
						maxLength: 20
					}]
				},{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					style : 'margin-left:25px;margin-right:5px;margin-top:10px;margin-bottom:5px;',
					items : [{
						xtype : 'datefield',
						name : 'operatedate',
						id : 'operatedate',
						labelWidth : 70,
						fieldLabel : '操作日期 ',
						labelAlign : 'right',
						format : 'Y-m-d',
						allowBlank : false,// 不允许为空
						maxValue : new Date(),
						labelAlign : 'right',
						value : Ext.Date.add(new Date(), Ext.Date.DAY)
					},{
						xtype : 'datefield',
						name : 'sqrq',
						id : 'sqrq',
						labelWidth : 70,
						fieldLabel : '申请日期 ',
						labelAlign : 'right',
						format : 'Y-m-d',
						allowBlank : false,// 不允许为空
						maxValue : new Date(),
						labelAlign : 'right',
						value : Ext.Date.add(new Date(), Ext.Date.DAY)
					}]
				},{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					style : 'margin-left:25px;margin-right:5px;margin-top:10px;margin-bottom:5px;',
					items : [{
						xtype:"textfield",
						fieldLabel: '金额',
						labelWidth:70,
						fieldStyle:me.fieldStyle, 
						name: 'mx1bxje',
						id: 'mx1bxje',
						width:255,
						labelAlign : 'right',
						maxLength: 20
					},{
						xtype:"combo",
						fieldLabel: '操作情况',
						labelWidth:70,
						editable:false,
						fieldStyle:me.fieldStyle, 
						name: 'isremark',
						id: 'isremark',
						displayField: "key",
						valueField :"key",
						width:255,
						labelAlign : 'right',
						store: new Ext.data.ArrayStore({
							fields : ['key'],
							data : [['已操作'], ['未操作']]
						}),
					}]
				},{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					style : 'margin-left:25px;margin-right:5px;margin-top:10px;margin-bottom:5px;',
					items : [{
						xtype:"textfield",
						fieldLabel: '事业部',
						labelWidth:70,
						fieldStyle:me.fieldStyle, 
						name: 'user_dept_level1',
						id: 'user_dept_level1',
						width:255,
						labelAlign : 'right',
						maxLength: 20
					},{
						xtype:"textfield",
						fieldLabel: '二级部门',
						labelWidth:70,
						fieldStyle:me.fieldStyle, 
						name: 'user_dept_level2',
						id: 'user_dept_level2',
						width:255,
						labelAlign : 'right',
						maxLength: 20
					}]
				},{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					style : 'margin-left:25px;margin-right:5px;margin-top:10px;margin-bottom:5px;',
					items : [{
						xtype:"textfield",
						fieldLabel: '三级部门',
						labelWidth:70,
						fieldStyle:me.fieldStyle, 
						name: 'user_dept_level3',
						id: 'user_dept_level3',
						width:255,
						labelAlign : 'right',
						maxLength: 20
					},{
						xtype:"textfield",
						fieldLabel: '四级部门',
						labelWidth:70,
						fieldStyle:me.fieldStyle, 
						name: 'user_dept_level4',
						id: 'user_dept_level4',
						width:255,
						labelAlign : 'right',
						maxLength: 20
					}]
				},{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					style : 'margin-left:25px;margin-right:5px;margin-top:10px;margin-bottom:5px;',
					items : [{
						xtype:"textfield",
						fieldLabel: '五级部门',
						labelWidth:70,
						fieldStyle:me.fieldStyle, 
						name: 'user_dept_level5',
						id: 'user_dept_level5',
						width:255,
						labelAlign : 'right',
						maxLength: 20
					}]
				},{
					style : 'margin-left:25px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					xtype:"textarea",
					fieldLabel: '出纳意见',
					labelWidth:70,
					fieldStyle:me.fieldStyle, 
					name: 'cwcnyj',
					id: 'cwcnyj',
					height:80,
					labelAlign : 'right',
					width:510,
					maxLength: 400
				},{
					style : 'margin-left:25px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
					xtype:"textarea",
					fieldLabel: '说明',
					labelWidth:70,
					fieldStyle:me.fieldStyle, 
					name: 'bxsm',
					id: 'bxsm',
					height:80,
					labelAlign : 'right',
					width:510,
					maxLength: 1000
				}]
			}]
		}];



		me.buttons = [{
			text : '修改',
			iconCls : 'Disk',
			handler : me.onUpdate
		},{
			text : '取消',
			iconCls : 'Cancel',
			handler : me.onCancel
		}]
		me.callParent(arguments);
	},

	onUpdate:function(){
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if(form.isValid()){
			Ext.Ajax.request({  
				url:"statistics/financeOA/updateOAInfo.do", 
				method: "POST",
				headers: { 'Content-Type': 'application/json' },
				jsonData: values, 
				success: function (response, options) {  
					response = Ext.JSON.decode(response.responseText); 
					if (response.result) {  
						me.up("window").close();
						me.grid.getStore().load();
						Ext.MessageBox.alert("信息", response.message);
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
	onCancel : function() {
		var me = this;
		me.up("window").close();
	},
	listeners : {
		'afterrender' : function() {
			 
		}
	} 
	
});
