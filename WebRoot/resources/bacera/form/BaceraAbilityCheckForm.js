/**
 * @author yuanxiaobo
 * @description 案例新增
 * @date 20170427
 */

Ext.define('Rds.bacera.form.BaceraAbilityCheckForm', {
	extend : 'Ext.form.Panel',
	config : {
		grid : null
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
				labelWidth : 70
			},
			border : false,
			autoHeight : true,
			items : [{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [{
										xtype : 'hiddenfield',
										name : 'ability_id'
									},
									{
										xtype : 'hiddenfield',
										name : 'department_concatid'
									},
									{
										// 该列在整行中所占的百分比
										columnWidth : .50,
										xtype : "textfield",
										labelAlign : 'right',
										labelWidth : 80,
										allowBlank:false, //不允许为空
										fieldLabel : "项目名称<span style='color:red'>*</span>",
										name : 'ability_name',
										readOnly:true
									},{
										// 该列在整行中所占的百分比
										columnWidth : .50,
										xtype : "textfield",
										labelAlign : 'right',
										labelWidth : 80,
										allowBlank:false, //不允许为空
										fieldLabel : "项目编号<span style='color:red'>*</span>",
										name : 'ability_num',
										readOnly:true
									} ]
				     },
				     {
							layout : "column",// 从左往右的布局
							xtype : 'fieldcontainer',
							border : false,
							items : [ {
								// 该列在整行中所占的百分比
								columnWidth : .50,
								xtype : "textfield",
								labelAlign : 'right',
								labelWidth : 80,
								allowBlank:false, //不允许为空
								fieldLabel : "参加编号<span style='color:red'>*</span>",
								name : 'participate_num',
								readOnly:true
							},{
								// 该列在整行中所占的百分比
								columnWidth : .50,
								xtype : "textfield",
								labelAlign : 'right',
								labelWidth : 80,
								allowBlank:false, //不允许为空
								fieldLabel : "所属单位<span style='color:red'>*</span>",
								name : 'ability_company',
								readOnly:true
							}
							]
				     },
				     {
							layout : "column",// 从左往右的布局
							xtype : 'fieldcontainer',
							border : false,
							items : [ {
								// 该列在整行中所占的百分比
								columnWidth : .50,
								xtype : "textfield",
								labelAlign : 'right',
								fieldLabel : '样本类型<span style="color:red">*</span>',
								labelWidth : 80,
								allowBlank:false, //不允许为空
								maxLength : 50,
								name : 'sample_type',
								readOnly:true
							}, {
								// 该列在整行中所占的百分比
								columnWidth : .50,
								xtype : "textfield",
								labelAlign : 'right',
								fieldLabel : '归属人<span style="color:red">*</span>',
								labelWidth : 80,
								allowBlank:false, //不允许为空
								maxLength : 50,
								name : 'ownperson',
								readOnly:true
							} ]
				     },{
							layout : "column",// 从左往右的布局
							xtype : 'fieldcontainer',
							border : false,
							items : [ {
								xtype : 'datefield',
								name : 'identify_starttime',
								columnWidth : .50,
								labelWidth : 90,
								fieldLabel : "鉴定开始时间",
								format : 'Y-m-d',
								allowBlank : false,// 不允许为空
								maxValue : new Date(),
								readOnly:true
							}, {
								xtype : 'datefield',
								name : 'identify_endtime',
								columnWidth : .50,
								labelWidth : 90,
								fieldLabel : "鉴定结束时间",
								labelAlign : 'right',
								format : 'Y-m-d',
								readOnly:true
							} ]
				     },{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [
									{
										// 该列在整行中所占的百分比
										columnWidth : .33,
										xtype : "textfield",
										labelAlign : 'right',
										labelWidth : 80,
										fieldLabel : '联系人',
										maxLength : 50,
										name : 'contacts_per',
										readOnly:true
									},
									{
										// 该列在整行中所占的百分比
										columnWidth : .33,
										xtype : "textfield",
										labelAlign : 'right',
										labelWidth : 80,
										fieldLabel : '联系电话',
										maxLength : 50,
										name : 'contacts_phone',
										readOnly:true
									},{
										// 该列在整行中所占的百分比
										columnWidth : .33,
										xtype : "textfield",
										labelAlign : 'right',
										labelWidth : 80,
										fieldLabel : '联系邮箱',
										maxLength : 50,
										name : 'contacts_mail',
										readOnly:true
									
									}]
				     },{
							layout : "column",// 从左往右的布局
							xtype : 'fieldcontainer',
							border : false,
							items : [ {
								// 该列在整行中所占的百分比
								columnWidth : .33,
								xtype : "textfield",
								labelAlign : 'right',
								labelWidth : 80,
								fieldLabel : '接样人',
								maxLength : 50,
								name : 'sample_per',
								readOnly:true
							
							}, {
								xtype : 'datefield',
								columnWidth : .33,
								labelWidth : 80,
								fieldLabel : "接样日期",
								labelAlign : 'right',
								format : 'Y-m-d',
								name : 'sample_date',
								readOnly:true
							},{
								// 该列在整行中所占的百分比
								columnWidth : .33,
								xtype : "textfield",
								labelAlign : 'right',
								labelWidth : 80,
								fieldLabel : '接样快递人',
								maxLength : 50,
								name : 'sample_express_per',
								readOnly:true
							
							}]
				     },{
							xtype : 'fieldset',
							title : '科室信息',
							layout : 'anchor',
							defaults : {
								anchor : '100%'
							},
							items : [{
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ 	{
									columnWidth : .20,
									xtype : "textfield",
									fieldLabel : "科室名称",
									labelAlign : 'right',
									maxLength : 50,
									labelWidth : 70,
									allowBlank : false,
									name : 'department_name',
									readOnly:true
								},{
									columnWidth : .20,
									xtype : "textfield",
									fieldLabel : "科室负责人",
									labelAlign : 'right',
									maxLength : 50,
									labelWidth : 70,
									allowBlank : false,
									name : 'department_chargename',
									style:'margin-left:5px;',
									readOnly:true
								},{
									columnWidth : .20,
									xtype : "textfield",
									fieldLabel : "实验负责人",
									labelAlign : 'right',
									maxLength : 50,
									labelWidth : 70,
									allowBlank : false,
									name : 'experiment_chargename',
									style:'margin-left:5px;',
									readOnly:true
								},{
									columnWidth : .20,
									xtype : "textfield",
									fieldLabel : "报告负责人",
									labelAlign : 'right',
									maxLength : 50,
									labelWidth : 70,
									allowBlank : false,
									name : 'report_chargename',
									style:'margin-left:5px;',
									readOnly:true
								}]
							},{

								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ 	
							   {
									xtype : 'datefield',
									columnWidth : .20,
									labelWidth : 70,
									fieldLabel : "完成日期",
									labelAlign : 'right',
									format : 'Y-m-d',
									name : 'finished_date',
									readOnly:true
								},{
									columnWidth : .20,
									xtype : "textfield",
									fieldLabel : "报告发送人",
									labelAlign : 'right',
									maxLength : 50,
									labelWidth : 70,
									name : 'report_sendname',
									style:'margin-left:5px;',
									readOnly:true
								},{
									columnWidth : .20,
									xtype : "textfield",
									fieldLabel : "联系方式",
									labelAlign : 'right',
									maxLength : 50,
									labelWidth : 70,
									name : 'report_concat',
									style:'margin-left:5px;',
									readOnly:true
								},{
									xtype : 'datefield',
									columnWidth : .20,
									labelWidth : 70,
									fieldLabel : "发送日期",
									labelAlign : 'right',
									format : 'Y-m-d',
									name : 'report_senddate',
									readOnly:true
								}]
							
							},{


								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ 	{
									columnWidth : .20,
									xtype : "textfield",
									fieldLabel : "发件信息",
									labelAlign : 'right',
									maxLength : 50,
									labelWidth : 70,
									name : 'report_sendinfo',
									readOnly:true
								},{
									columnWidth : .20,
									xtype : "textfield",
									fieldLabel : "收件信息",
									labelAlign : 'right',
									maxLength : 50,
									labelWidth : 70,
									name : 'report_reciveinfo',
									style:'margin-left:5px;',
									readOnly:true
								},{
									columnWidth : .20,
									xtype : "textfield",
									fieldLabel : "快递类型",
									labelAlign : 'right',
									maxLength : 50,
									labelWidth : 70,
									name : 'report_type',
									style:'margin-left:5px;',
									readOnly:true
								},{
									columnWidth : .20,
									xtype : "textfield",
									fieldLabel : "快递单号",
									labelAlign : 'right',
									maxLength : 50,
									labelWidth : 70,
									name : 'report_num',
									readOnly:true
								},{
									columnWidth : .20,
									xtype : "textfield",
									fieldLabel : "快递状态",
									labelAlign : 'right',
									maxLength : 50,
									labelWidth : 70,
									name : 'report_status',
									readOnly:true
								}]
							}
							]
				     },{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [{
				    			xtype:"numberfield",
				    			fieldLabel: '应收款项<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
				    			labelWidth:80,
				    			columnWidth : .20,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'receivables',
				    			maxLength: 20,
				    			allowBlank:false, //不允许为空
			        			blankText:"不能为空", //错误提示信息，默认为This field is required!
								readOnly:true
				    		}]
			    		},{
							border : false,
							xtype : 'fieldcontainer',
							layout : "column",
							items : [ {
								xtype : 'textarea',
								fieldLabel : '案例备注',
								name : 'ability_remark',
								width : 500,
								maxLength : 500,
								columnWidth : 1,
								labelWidth : 80,
								readOnly:true
							} ]
				     }]
		}];

		me.buttons = [ {
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}

});
