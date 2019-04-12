var alcoholStr='';
Ext.apply(Ext.form.field.VTypes, {  
    daterange: function (val, field) {  
        var date = field.parseDate(val);  
  
        if (!date) {  
            return false;  
        }  
        if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {  
            var start = field.up('grid').down('#' + field.startDateField);  
            start.setMaxValue(date);  
            start.validate();  
            this.dateRangeMax = date;  
        }  
        else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {  
            var end = field.up('grid').down('#' + field.endDateField);  
            end.setMinValue(date);  
            end.validate();  
            this.dateRangeMin = date;  
        }  
        return true;  
    },  
    daterangeText: '开始日期必须小于结束日期'  
});  
Ext.define("Rds.alcohol.panel.AlcoholIdentifyInfoGridPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize :25,
	initComponent : function() {
		var me = this;
		me.store = Ext.create('Ext.data.Store', {
			fields : ['per_id','per_name', 'per_code','per_sys','user_name'],
//			pageSize:2,
			proxy : {
				type : 'jsonajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'alcohol/archive/getidentifyInfo.do',
				params : {
					start : 0,
					limit : 25
				},
				reader : {
					type : 'json',
					root : 'items',
					totalProperty : 'count'
				}
			},
			listeners : {
				'beforeload' : function(ds, operation, opt) {
					Ext.apply(me.store.proxy.extraParams, {
						endtime : dateformat(Ext
								.getCmp('endtime_identify_alcohol').getValue()),
						starttime : dateformat(Ext
								.getCmp('starttime_identify_alcohol').getValue()),
						per_name : trim(Ext
								.getCmp('per_name_identify_alcohol').getValue()),
						per_code : trim(Ext
								.getCmp('per_code_identify_alcohol').getValue())
						
					});
				}
			}
		});

		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : me.pageSize,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
					text : '鉴定人',
					dataIndex : 'per_name',
					menuDisabled : true,
					width : 150
					
				}, {
					text : '执业资格证号',
					dataIndex : 'per_code',
					width : 200,
					menuDisabled : true
				}, {
					text : '添加时间',
					dataIndex : 'per_sys',
					width : 200,
					menuDisabled : true
				}, {
					text : '登录用户',
					dataIndex : 'user_name',
					width : 200,
					menuDisabled : true
				}];

		me.dockedItems = [{
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [{
						xtype : 'textfield',
						name : 'per_name',
						id : 'per_name_identify_alcohol',
						labelWidth : 70,
						width : '20%',
						fieldLabel : '鉴定人'
					}, {
						xtype : 'textfield',
						name : 'per_code',
						id : 'per_code_identify_alcohol',
						labelWidth : 70,
						width : '20%',
						fieldLabel : '资格证号'
					}, {
						xtype : 'datefield',
						id:'starttime_identify_alcohol',
						name : 'starttime',
						fieldLabel : '添加时间 从',
						labelWidth : 70,
						labelAlign : 'left',
						emptyText : '请选择日期',
						format : 'Y-m-d',
						vtype: 'daterange',
						endDateField: 'endtime_identify_alcohol',  
						maxValue : new Date(),
						endDateField: 'endtime_identify_alcohol', 
						listeners:{
							'change' : function(dom,value) {
							if(value == null){
								this.up('toolbar').down('#endtime_identify_alcohol').setMinValue('')
							}
						}
					}
					}, {
						xtype : 'datefield',
						id : 'endtime_identify_alcohol',
						name : 'endtime',
						labelWidth : 20,
						fieldLabel : '到',
						labelAlign : 'right',
						emptyText : '请选择日期',
						format : 'Y-m-d',
						maxValue : new Date()
					}, {
						text : '查询',
						iconCls : 'Find',
						handler : me.onSearch
					}]
		}, {
			xtype : 'toolbar',
			dock : 'top',
			items : [{
						text : '新增',
						iconCls : 'Pageadd',
						handler : me.onInsert
					}, {
						text : '修改',
						iconCls : 'Pageedit',
						handler : me.onUpdate
					}, {
						text : '废除',
						iconCls : 'Delete',
						handler : me.onDelete
					}]
		}];

		me.callParent(arguments);
	},
	 
	onInsert : function() {
		var me = this.up("gridpanel");
		ownpersonTemp="";
		ownaddressTemp="";
		var form = Ext.create("Rds.alcohol.form.AlcoholIdentifyInsert", {
					region : "center",
					autoScroll : true,
					grid : me
				});
		var win = Ext.create("Ext.window.Window", {
					title : '检验信息',
					width : 400,
					iconCls : 'Pageadd',
					height : 150,
					modal:true,
					layout : 'border',
					items : [form]
				});
		win.show();
		
	},	onUpdate : function() {
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1 || selections.length>1){
			Ext.Msg.alert("提示", "请选择一条需要修改的记录!");
			return;
		};
		ownaddressTemp = selections[0].get("per_name");
		var form = Ext.create("Rds.alcohol.form.AlcoholIdentifyInsert",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window", {
			title : '检验信息',
			width : 400,
			iconCls : 'Pageadd',
			height : 150,
			modal:true,
			layout : 'border',
			items : [form]
		});
		win.show();
		form.loadRecord(selections[0]);
	},
	
	onDelete : function() {
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要删除的记录!");
			return;
		};
		var per_id='';
		for(var i = 0 ; i < selections.length ; i ++)
		{
			per_id+="'"+selections[i].get("per_id")+"',";
		}
		per_id = per_id.substring(0,per_id.length-1);
		var values = {
				per_id:per_id
		};
		Ext.MessageBox.confirm("提示", "确认删除选中记录？", function (btn) {
			if("yes" == btn)
			{
				Ext.Ajax.request({  
					url:"alcohol/archive/delete.do", 
					method: "POST",
					headers: { 'Content-Type': 'application/json' },
					jsonData: values, 
					success: function (response, options) {  
						response = Ext.JSON.decode(response.responseText); 
		                 if (response) {  
		                 	Ext.MessageBox.alert("提示信息", "成功");
		                 	me.getStore().load();
		                 }else { 
		                 	Ext.MessageBox.alert("错误信息","失败");
		                 } 
					},  
					failure: function () {
						Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
					}
		    	       
		      	});
			}
	    });
		
	},
	
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage = 1;
		me.getStore().load();
	},
	listeners : {
		'afterrender' : function() {
			this.store.load();
		}
	}
});
