var deptname1 = Ext.create('Ext.data.Store',{
    fields:['deptname'],
    autoLoad:true,
    proxy:{
    	type: 'jsonajax',
        actionMethods:{read:'POST'},
        params:{},
        url:'statistics/financeConfig/queryUserDept.do',
        reader:{
          type:'json',
        },
        writer: {
            type: 'json'
       }
      },
      remoteSort:true
});
var deptname2 = Ext.create('Ext.data.Store',{
    fields:['deptname'],
    autoLoad:true,
    proxy:{
    	type: 'jsonajax',
        actionMethods:{read:'POST'},
        params:{},
        url:'statistics/financeConfig/queryUserDept.do',
        reader:{
          type:'json',
        },
        writer: {
            type: 'json'
       }
      },
      remoteSort:true
});
Ext.define("Rds.finance.form.FinanceRemittanceInsertForm", {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;
		var insideCostUnit = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			allowBlank:true,
			labelWidth : 90,
			fieldLabel : '内部结算部门',
		    name:'insideCostUnit',
//		    forceSelection:true,
		    triggerAction: 'all',
		    queryMode: 'local', 
		    emptyText : "请选择",
		    selectOnTab: true,
			labelAlign : 'right',
		    store: deptname1,
		    fieldStyle: me.fieldStyle,
		    displayField:'deptname',
		    valueField:'deptname',
		    listClass: 'x-combo-list-small',
		})
		var manageUnit = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			allowBlank:true,
			labelWidth : 90,
			fieldLabel : '管理费部门',
		    name:'manageUnit',
//		    forceSelection:true,
		    triggerAction: 'all',
			labelAlign : 'right',
		    queryMode: 'local', 
		    emptyText : "请选择",
		    selectOnTab: true,
		    store: deptname2,
		    fieldStyle: me.fieldStyle,
		    displayField:'deptname',
		    valueField:'deptname',
		    listClass: 'x-combo-list-small',
		})
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
			items : [	{xtype : 'hiddenfield',name : 'contract_id'},
			         	{xtype : 'hiddenfield',name : 'contract_remittance_planid'},
						{
							xtype : "datefield",
							fieldLabel : "汇款时间<span style='color:red'>*</span>",
							mode : 'local',
							labelAlign : 'right',
							blankText : '请选择',
							emptyText : '请选择',
							allowBlank : false,// 不允许为空
							blankText : "不能为空",// 错误提示信息，默认为This
							maxLength : 50,
							labelWidth : 90,
							format : 'Y-m-d',
							name : 'remittance_date'
						},{
							xtype : "numberfield",
							fieldLabel : "回款金额<span style='color:red'>*</span>",
							mode : 'local',
							labelAlign : 'right',
							allowBlank : false,// 不允许为空
							maxLength : 50,
							labelWidth : 90,
							name : 'remittance',
							value:0.0
						},insideCostUnit,
						{
							xtype : "numberfield",
							fieldLabel : "内部结算价<span style='color:red'>*</span>",
							mode : 'local',
							labelAlign : 'right',
							allowBlank : false,// 不允许为空
							maxLength : 50,
							labelWidth : 90,
							labelAlign : 'right',
							name : 'insideCost',
							value:0.0
						},manageUnit,
						{
							xtype : "numberfield",
							fieldLabel : "管理费<span style='color:red'>*</span>",
							mode : 'local',
							labelAlign : 'right',
							allowBlank : false,// 不允许为空
							maxLength : 50,
							labelWidth : 90,
							name : 'manageCost',
							value:0.0
						},{
							xtype : "numberfield",
							fieldLabel : "委外成本<span style='color:red'>*</span>",
							mode : 'local',
							labelAlign : 'right',
							allowBlank : false,// 不允许为空
							maxLength : 50,
							labelWidth : 90,
							name : 'externalCost',
							value:0.0
						}]
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
			if (form.isValid()) {
				Ext.MessageBox.wait('正在操作','请稍后...');
				Ext.Ajax.request({
					url : "finance/casefinance/insertContractRemittance.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : values,
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response) {
							Ext.MessageBox.alert("提示信息", "操作成功！");
							me.grid.getStore().load();
							me.up("window").close();
						} else {
							Ext.MessageBox.alert("错误信息", "操作失败，请联系管理员！");
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
		},
		listeners : {
			'beforerender' : function() {
				deptname1.load();
				deptname2.load();
			}
		}
});