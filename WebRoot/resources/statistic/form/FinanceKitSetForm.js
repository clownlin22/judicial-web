var deptname = Ext.create('Ext.data.Store',{
    fields:['deptname'],
    autoLoad:true,
    proxy:{
    	type: 'jsonajax',
        actionMethods:{read:'POST'},
        params:{},
        url:'statistics/kitSet/queryDeptList.do',
        reader:{
          type:'json',
        },
        writer: {
            type: 'json'
       }
      },
      remoteSort:true
});
Ext.define("Rds.statistic.form.FinanceKitSetForm", {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;
		var user_dept_level1 = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			allowBlank:false,
			labelWidth : 100,
			columnWidth : 1,
			fieldLabel : '申请部门<span style="color:red">*</span>',
		    name:'user_dept_level1',
		    forceSelection:true,
		    triggerAction: 'all',
		    queryMode: 'local', 
		    emptyText : "请选择",
			labelAlign : 'right',
		    selectOnTab: true,
		    store: deptname,
		    fieldStyle: me.fieldStyle,
		    displayField:'deptname',
		    valueField:'deptname',
		    listClass: 'x-combo-list-small',
		})
		var apply_date = new Ext.form.DateField(
		{
			name : 'apply_date',
			fieldLabel : '日期<span style="color:red">*</span>',
			labelAlign : 'right',
			labelWidth : 100,
			columnWidth : 1,
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d'
		});
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
			items : [{
		    			xtype:"textfield",
		    			fieldLabel: 'kit_id',
		    			labelWidth:80,
		    			labelAlign : 'right',
		    			readOnly:true,
		    			fieldStyle:me.fieldStyle, 
		    			name: 'kit_id',
		    			hidden:true
		    		},user_dept_level1,
		    		{
						xtype : "textfield",
						fieldLabel : "申请人<span style='color:red'>*</span>",
						mode : 'local',
						labelAlign : 'right',
						allowBlank : false,// 不允许为空
						maxLength : 50,
						labelWidth : 100,
						name : 'apply_per',
					},{
						xtype : "textfield",
						fieldLabel : "泛薇流程编号<span style='color:red'>*</span>",
						mode : 'local',
						labelAlign : 'right',
						allowBlank : false,// 不允许为空
						maxLength : 50,
						labelWidth : 100,
						name : 'apply_num',
					},apply_date,
					{
		    			xtype:"textfield",
		    			fieldLabel: "试剂名称<span style='color:red'>*</span>",
		    			labelWidth:100,
		    			labelAlign : 'right',
		    			columnWidth : .50,
		    			fieldStyle:me.fieldStyle, 
		    			name: 'kit_name',
		    			allowBlank:false, //不允许为空
		    			maxLength: 50
		    		},{
		    			xtype:"textfield",
		    			fieldLabel: "规格(RXN)<span style='color:red'>*</span>",
		    			labelWidth:100,
		    			labelAlign : 'right',
		    			columnWidth : .50,
		    			fieldStyle:me.fieldStyle, 
		    			name: 'kit_spec',
		    			allowBlank:false, //不允许为空
		    			maxLength: 50
		    		},{
		    			xtype:"textfield",
		    			fieldLabel: "数量(盒)<span style='color:red'>*</span>",
		    			labelWidth:100,
		    			labelAlign : 'right',
		    			columnWidth : .50,
		    			fieldStyle:me.fieldStyle, 
		    			name: 'kit_count',
		    			allowBlank:false, //不允许为空
		    			maxLength: 50
		    		},{
		    			xtype:"textfield",
		    			fieldLabel: "批次号<span style='color:red'>*</span>",
		    			labelWidth:100,
		    			labelAlign : 'right',
		    			columnWidth : .50,
		    			fieldStyle:me.fieldStyle, 
		    			name: 'kit_bathc_num',
		    			allowBlank:false, //不允许为空
		    			maxLength: 50
		    		},{
		    			xtype:"textfield",
		    			fieldLabel: "目的地<span style='color:red'>*</span>",
		    			labelWidth:100,
		    			labelAlign : 'right',
		    			columnWidth : .50,
		    			fieldStyle:me.fieldStyle, 
		    			name: 'kit_dest',
		    			allowBlank:false, //不允许为空
		    			maxLength: 50
		    		},{
		    			xtype:"textfield",
		    			fieldLabel: "顺丰单号<span style='color:red'>*</span>",
		    			labelWidth:100,
		    			labelAlign : 'right',
		    			columnWidth : .50,
		    			fieldStyle:me.fieldStyle, 
		    			name: 'kit_express_num',
		    			allowBlank:false, //不允许为空
		    			maxLength: 50
		    		},{
		    			xtype:"textfield",
		    			fieldLabel: "收件人<span style='color:red'>*</span>",
		    			labelWidth:100,
		    			labelAlign : 'right',
		    			columnWidth : .50,
		    			fieldStyle:me.fieldStyle, 
		    			name: 'kit_receive_per',
		    			allowBlank:false, //不允许为空
		    			maxLength: 50
		    		},{
						xtype : "textarea",
						fieldLabel : "备注",
						mode : 'local',
						labelAlign : 'right',
						maxLength : 500,
						labelWidth : 100,
						name : 'remark'
					},{
						xtype : 'tbtext',
						text : '<div style="margin-left:20px;font-weight:bold">注意事项:</div><div style="margin-left:20px;">1. 按实际发生情况填写上述参数；</div><div style="margin-left:20px;">2. 申请部门选择请确定组织结构，尽量选择真实准确的部门参数；</div>'
					}
					]
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
					url : "statistics/kitSet/insertKitSet.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : values,
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.success) {
							Ext.MessageBox.alert("提示信息", "操作成功！");
							me.grid.getStore().load();
							me.up("window").close();
						} else {
							Ext.MessageBox.alert("错误信息", response.msg);
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
//				deptname.load();
			}
		}
});