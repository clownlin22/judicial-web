Ext.define('Rds.bacera.form.BaceraProgramListForm', {
	extend : 'Ext.form.Panel',
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var program_type = new Ext.form.ComboBox({
			autoSelect : true,
			editable:true,
			allowBlank:false,
			labelWidth : 80,
			columnWidth : .25,
			fieldLabel : '项目类型<span style="color:red">*</span>',
	        name:'program_type',
	        forceSelection:true,
	        triggerAction: 'all',
	        queryMode: 'local', 
	        emptyText : "请选择",
	        selectOnTab: true,
	        store: new Ext.data.ArrayStore({
                fields: ['Name', 'Code'],
                data: [['医学检测项', '医学检测项'], ['医学检测项(武汉)', '医学检测项(武汉)'], ['医学检测项(转化医学)', '医学检测项(转化医学)'],
                       ['血清学筛查', '血清学筛查'],['肿瘤标志物', '肿瘤标志物'],['毒品检测', '毒品检测'],['HPV', 'HPV'],['叶酸代谢能力检测', '叶酸代谢能力检测'],['健康体检', '健康体检'],['地中海贫血', '地中海贫血'],
                       ['文书鉴定', '文书鉴定'],['儿童基因检测', '儿童基因检测'],['肿瘤个体', '肿瘤个体'],['安全用药', '安全用药'],['肿瘤易感基因', '肿瘤易感基因']]
            }),
	        fieldStyle: me.fieldStyle,
	        displayField:'Name',
	        valueField:'Code',
	        listClass: 'x-combo-list-small',
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
    		},program_type,{
    			xtype:"textfield",
    			fieldLabel: '项目名称<span style="color:red;font-weight:bold" data-qtip="Required">*</span>&nbsp;',
    			labelWidth:80,
    			fieldStyle:me.fieldStyle, 
    			name: 'program_name',
//				regex:/^[^\s]*$/,
//				regexText:'请输入项目名称',
    			allowBlank:false, //不允许为空
    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
    			maxLength: 50
    		},{
    			xtype:"textfield",
    			fieldLabel: '项目编码',
    			labelWidth:80,
    			maxLength: 20,
				regex:/^[^\s]*$/,
				regexText:'请输入项目编码',
    			fieldStyle:me.fieldStyle, 
    			name: 'program_code',
    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
    		},{
				xtype:"textarea",
    			fieldLabel: '备注',
    			labelWidth:80,
    			fieldStyle:me.fieldStyle, 
    			name: 'remark',
    			height:80,
    			maxLength: 100
    		}]
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
				url:"bacera/program/save.do", 
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
	                 	Ext.MessageBox.alert("错误信息", "操作失败！请联系管理员；<br>"+response.message);
	                 } 
				},  
				failure: function () {
					Ext.Msg.alert("错误信息", "操作失败<br>请联系管理员!");
				}
	    	       
	      	});
		}
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	}
});