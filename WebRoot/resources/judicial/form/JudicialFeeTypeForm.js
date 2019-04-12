/**
 * 
 */
var pernum = 1;
Ext.define("Rds.judicial.form.JudicialFeeTypeForm", {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
	},
	initComponent : function() {
		var me = this;
		var saleStore = Ext.create('Ext.form.ComboBox', {
			xtype : 'combo',
			columnWidth : .45,
			fieldLabel : '销售经理',
			labelWidth : 100,
			labelAlign : 'left',
			name : 'area_id',
//			id:'case_receiver_update_alcohol',
			emptyText:'检索方式(姓名首字母)：如小红(xh)',
			store : Ext.create("Ext.data.Store",{
				 fields:[
		                  {name:'key',mapping:'key',type:'string'},
		                  {name:'value',mapping:'value',type:'string'}
		          ],
				pageSize : 10,
				proxy : {
					type : "ajax",
					url : "judicial/dicvalues/getAllManager.do",
					reader : {
						type : "json"
					}
				}
			}),
			displayField : 'value',
			valueField : 'key',
			allowBlank  : false,//不允许为空  
            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
			forceSelection: true,
			typeAhead : false,
			hideTrigger : true,
			minChars : 2,
			matchFieldWidth : true,
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
			}
		   });
//		var saleStore=Ext.create('Ext.data.Store', {
//			model : 'model',
//			proxy : {
//				type : 'jsonajax',
//				actionMethods : {
//					read : 'POST'
//				},
//				params:{
//					type:0
//				},
//				url : 'judicial/dicvalues/getAllManager.do',
//				reader : {
//					type : 'json',
//					root : 'data'
//				}
//			},
//			autoLoad : true,
//			remoteSort : true
//		});
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
			items : [saleStore
//			         {
//						xtype : "combo",
//						fieldLabel : '销售经理',
//						autoSelect : true,
//						columnWidth : .45,
//						labelWidth : 100,
//						mode : 'local',
//						name : "area_id",
//						allowBlank : false,// 不允许为空
//						blankText : "不能为空",// 错误提示信息，默认为This
//						forceSelection : true,
//						emptyText : '请选择员工',
//						queryMode : 'local',
//						valueField : "key",
//						displayField : "value",
//						triggerAction : 'all',
//						store : saleStore
//					}
			, new Ext.form.field.ComboBox({
								fieldLabel : '销售类型',
//								columnWidth : .45,
								labelWidth : 100,
								editable : false,
								triggerAction : 'all',
								displayField : 'Name',
								valueField : 'Code',
								store : new Ext.data.ArrayStore({
											fields : ['Name', 'Code'],
											data : [['司法鉴定', 0], ['医学鉴定', 1]]
										}),
								value : 0,
								mode : 'local',
								// typeAhead: true,
								name : 'feetype'
							}), {
						xtype : 'textarea',
						fieldLabel : '标准价格计算公式',
						labelWidth : 100,
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						name : 'equation',
						value : 'function exe(a,b){\t\n\t}',
						maxLength : 1000
					}, {
						xtype : 'label',
						forId : 'myFieldId',
						text : '公式以“function exe(a,b){”开头,以“}”结尾，“a”表示样本数量，“b”表示收费类型,1为单亲,2为双亲，例如：function exe(a,b){if(b=1){return 1000*a}else{return 2000*a}},表示单亲1000元每人，双亲2000元每人'
					}, {
						xtype : 'textfield',
						fieldLabel : '回款优惠比例',
						labelWidth : 100,
						regex : /^(0\.\d*|[1])$/,
						regexText : '请输入0-1之间的数字',
						fieldStyle : me.fieldStyle,
						allowBlank : false,
						maxLength : 5,
						maxValue : 1,
						name : 'discountrate'
					}]
		}];

		me.buttons = [{
					text : '保存',
					iconCls : 'Disk',
					handler : me.onSave,
					listeners : {
						click : {
							fn : function(items, e, eOpts) {
								items.disable(true);
							}
						}
					}
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
		if (!form.isValid()) {
			Ext.MessageBox.alert("提示信息", "请填写完整表单信息!");
			return;
		}
		var values = form.getValues();

		Ext.Ajax.request({
					url : "judicial/feestandard/save.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : values,
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.result == true) {
							Ext.MessageBox.alert("提示信息", response.message);
							me.grid.getStore().load();
							me.up("window").close();
						} else {
							Ext.MessageBox.alert("错误信息", response.message);
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
					}

				});

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}

});