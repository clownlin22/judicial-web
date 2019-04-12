Ext.define("Rds.finance.form.FinanceRemmitanceUpdateForm", {
	extend : 'Ext.form.Panel',
	config : {
		operType : null
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
			items : [ {xtype:'hidden',name:'remittance_id'},{
				xtype : "textfield",
				fieldLabel : '汇款金额<span style="color:red">*</span>',
				labelAlign : 'right',
				maxLength : 18,
				labelWidth : 80,
				name : 'remittance',
				readOnly:true
			},{
				xtype : "datefield",
				fieldLabel : '汇款时间<span style="color:red">*</span>',
				labelAlign : 'right',
				format : 'Y-m-d',
				labelWidth : 80,
				name : 'remittance_date',
				allowBlank : false,// 不允许为空
				blankText : "不能为空",// 错误提示信息，默认为This field is required!
			},{
				xtype : 'combo',
				fieldLabel : '汇款人<span style="color:red">*</span>',
				labelWidth : 80,
				labelAlign : 'right',
				name : 'remittance_per',
				emptyText:'(人员首字母)：如小明(xm)',
				store :Ext.create("Ext.data.Store",{
					   fields:[
							{name:'key',mapping:'key',type:'string'},
							{name:'value',mapping:'value',type:'string'}
			          ],
					pageSize : 20,
					autoLoad: false,
					proxy : {
						type : "ajax",
						url:'judicial/dicvalues/getUsersId.do?userid='+ownpersonTemp,
						reader : {
							type : "json"
						}
					}
				}),
				displayField : 'value',
				valueField : 'key',
				typeAhead : false,
				allowBlank:false, //不允许为空
				hideTrigger : true,
				forceSelection: true,
				minChars : 2,
				matchFieldWidth : true,
				listConfig : {
					loadingText : '正在查找...',
					emptyText : '没有找到匹配的数据'
				},
				listeners : {
					'afterrender':function(){
						if("" != ownpersonTemp)
						{
							this.store.load();
						}
					}
				}
			},{
				xtype : "textfield",
				fieldLabel : '汇款账户<span style="color:red">*</span>',
				labelAlign : 'right',
				maxLength : 20,
				allowBlank : false,// 不允许为空
				blankText : "不能为空",// 错误提示信息，默认为This field is required!
				labelWidth : 80,
				name : 'remittance_account'
			},
			Ext.create('Ext.form.ComboBox', {
				fieldLabel : '到款账户<span style="color:red">*</span>',
                labelWidth : 80,
                labelAlign : 'right',
                triggerAction: 'all',
				allowBlank : false,// 不允许为空
				blankText : "不能为空",// 错误提示信息，默认为This field is required!
				forceSelection: true,
     	        editable:false,  
                displayField: 'Name',
                valueField: 'Code',
                name : 'arrival_account',
                store: new Ext.data.ArrayStore({
                    fields: ['Name', 'Code'],
                    data: [['苏博支付宝', '苏博支付宝'], ['苏博邮储', '苏博邮储'],
                           ['苏博股份浦发', '苏博股份浦发'], ['苏博股份现金', '苏博股份现金'],
                           ['南有中行','南有中行'],['南有支付宝','南有支付宝'],
                           ['医云南京银行','医云南京银行'], ['医云支付宝','医云支付宝'],
                           ['子渊民丰','子渊民丰'],['子渊农行','子渊农行'],
                           ['子渊现金','子渊现金'],['陈云农行','陈云农行'],
                           ['佟朔工行','佟朔工行'],['孙利华建行','孙利华建行'],
                           ['三和浦发','三和浦发'],['三和建行','三和建行'],
                           ['正泰浦发','正泰浦发'],['毕节鉴定所','毕节鉴定所'],
                           ['永建农行','永建农行'],['检测技术南京银行','检测技术南京银行'],['严权超农行','严权超农行'],
                           ['广西正廉司法鉴定所支付宝','广西正廉司法鉴定所支付宝'],['广西正廉司法鉴定所建行','广西正廉司法鉴定所建行'],
                           ['南京吉量生物科技有限公司高新开发区支行','南京吉量生物科技有限公司高新开发区支行'],['刘勇工行','刘勇工行']]
                })          ,
                mode: 'local',
			}),
//			{
//				xtype : "textfield",
//				fieldLabel : '到款账户<span style="color:red">*</span>',
//				labelAlign : 'right',
//				maxLength : 50,
//				allowBlank : false,// 不允许为空
//				blankText : "不能为空",// 错误提示信息，默认为This field is required!
//				labelWidth : 80,
//				name : 'arrival_account'
//			},
			{
				xtype : "textarea",
				fieldLabel : '汇款备注',
				labelAlign : 'right',
				maxLength : 200,
				labelWidth : 80,
				name : 'remittance_remark'
			},{
                xtype: 'combo',
                autoSelect : true,
                allowBlank:false,
        		fieldLabel: '是否抵扣<span style="color:red">*</span>',
        		labelWidth : 80,
        		emptyText : "请选择",
        		triggerAction: 'all',
     	        queryMode: 'local',
     	        selectOnTab: true,
     	        editable:false,  
     	        forceSelection: true,
     	        store : new Ext.data.ArrayStore({
					fields : ['Name', 'Code'],
					data : [['是', '1'], ['否', '2']]
				}),
                displayField: 'Name',
                valueField: 'Code',
                name:'deductible',
			}]
		}];

		me.buttons = [{
					text : '修改',
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
		values["confirm_state"]=-1;
		if (form.isValid()) {
			Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({
				url : "finance/casefinance/updateRemittance.do",
				method : "POST",
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : values,
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					if (response == true) {
						Ext.MessageBox.alert("提示信息", "修改成功！");
						me.grid.getStore().load();
						me.up("window").close();
					} else {
						Ext.MessageBox.alert("错误信息", "修改失败，请联系管理员！");
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