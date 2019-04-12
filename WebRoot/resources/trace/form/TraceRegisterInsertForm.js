var storeType=Ext.create(
    'Ext.data.Store',
    {
        fields: ['type_name'],
        proxy:{
            type: 'jsonajax',
            actionMethods:{read:'POST'},
            url:'trace/type/queryType.do',
            reader:{
                type:'json',
                root:'data'
            }
        },
        remoteSort:true
    }
);
Ext
    .define(
    'Rds.trace.form.TraceRegisterInsertForm',
    {
        extend : 'Ext.form.Panel',
        config:{
            operType:null
        },
        initComponent : function() {
            var me = this;
            var items = [];
            Ext.Ajax.request({
                url : 'trace/document/queryDocument.do',
                //params : {case_id:case_id_appraisal.split(',')[0]},
                async:false,
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                jsonData:{abc:1},
                success : function(response, opts) {
                    var res = Ext.JSON
                        .decode(response.responseText);
                    var e = res['data'];
                    for (var i = 0; i < e.length; i++) {
                        var d = e[i];
                        var chk = {
                            boxLabel : d.document_name,
                            name : 'case_attachment_desc',
                            inputValue : d.document_name
                        };
                        items.push(chk);
                    }
                },
                failure : function(response, opts) {
                }
            });

            me.items = [ {
                xtype : 'form',
                style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
                bodyPadding : 10,
                layout:'column',
                border : false,
                autoHeight : true,
                items : [ {
	                    xtype : 'form',
	                    border : false,
	                    style : 'margin-right:20px;',
	                    columnWidth:.4,
	                    defaults : {
	                        anchor : '100%',
	                        labelWidth : 80,
	                        labelSeparator: "：",
	                        labelAlign: 'right'
	                    },
                        items:[{
                            // 该列在整行中所占的百分比
                            columnWidth : .45,
                            xtype : "numberfield",
                            labelAlign: 'left',
                            labelWidth : 100,
                            fieldLabel : '案例编号',
                            allowBlank  : false,//不允许为空
                            blankText   : "不能为空",//错误提示信息，默认为This field is required!
                            maxLength :200,
        					value :Ext.Date.format(new Date(), 'Y'),
                            name : 'case_no'
                        },{
                            // 该列在整行中所占的百分比
                            columnWidth : .45,
                            xtype : "textfield",
                            labelAlign: 'left',
                            labelWidth : 100,
                            fieldLabel : '委托单位',
                            allowBlank  : false,//不允许为空
                            blankText   : "不能为空",//错误提示信息，默认为This field is required!
                            maxLength :200,
                            name : 'company_name'
                        },{
                            // 该列在整行中所占的百分比
                            columnWidth : .45,
                            xtype : "combo",
                            labelAlign: 'left',
                            labelWidth : 100,
                            mode: 'remote',
                            forceSelection: true,
                            fieldLabel : '委托鉴定事项',
                            allowBlank  : false,//不允许为空
                            blankText   : "不能为空",//错误提示信息，默认为This field is required!
                            editable:false,
                            name : 'case_type',
                            valueField :"type_name",
                            displayField: "type_name",
                            store: storeType,
                            multiSelect:true
                        },
                        {
                            // 该列在整行中所占的百分比
                            columnWidth : .45,
                            xtype : "textfield",
                            labelAlign: 'left',
                            fieldLabel : '鉴定地点',
                            allowBlank  : false,//不允许为空
                            blankText   : "不能为空",//错误提示信息，默认为This field is required!
                            labelWidth : 100,
                            maxLength :200,
                            name : 'case_local'
                        },{
                            // 该列在整行中所占的百分比
                            columnWidth : .45,
                            xtype : "textfield",
                            labelAlign: 'left',
                            fieldLabel : '鉴定要求',
                            allowBlank  : false,//不允许为空
                            blankText   : "不能为空",//错误提示信息，默认为This field is required!
                            labelWidth : 100,
                            maxLength :200,
                            name : 'identification_requirements'
                        },{
                                // 该列在整行中所占的百分比
                                columnWidth : .45,
                                xtype : "datefield",
                                labelAlign: 'left',
                                fieldLabel : '受理日期',
                                emptyText : '请选择日期',
                                format : 'Y-m-d',
                                maxValue : new Date(),
                                value : Ext.Date.add(
                                    new Date(),
                                    Ext.Date.DAY),
                                allowBlank  : false,//不允许为空
                                blankText   : "不能为空",//错误提示信息，默认为This field is required!
                                labelWidth : 100,
                                maxLength :200,
                                name : 'receive_time'
                        },{
	    						xtype : 'combo',
	    						fieldLabel : '归属<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
	    						labelWidth : 100,
	                            columnWidth : .45,
	    						name : 'receiver_id',
	    						id : 'receiver_id',
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
	    								url:"finance/chargeStandard/getAscriptionPer.do",
	    								reader : {
	    									type : "json"
	    								}
	    							}
	    						}),
	    						displayField : 'ascription',
	    						valueField : 'id',
	    						typeAhead : false,
	    						hideTrigger : true,
	                            labelAlign: 'left',
	    						forceSelection: true,
	    						minChars : 2,
	    						allowBlank:false, //不允许为空
	    						matchFieldWidth : true,
	    						listConfig : {
	    							loadingText : '正在查找...',
	    							emptyText : '没有找到匹配的数据'
	    						}    					
//                                    xtype : "combo",
//                                    labelSeparator: "：",
//                                    fieldLabel : '归属人',
//                                    columnWidth : .45,
//                                    labelAlign: 'left',
//                                    labelWidth : 100,
//                                    mode: 'remote',
//                                    forceSelection: true,
//                                    allowBlank  : false,//不允许为空
//                                    blankText   : "不能为空",//错误提示信息，默认为This field is required!
//                                    emptyText:'检索方式：如小红(xh)',
//                                    name:'receiver_id',
//                                    valueField :"id",
//                                    displayField: "name",
//                                    store: receiverStore,
//                                    listeners: {
//                                    	'beforequery' : function(e) {  
//							                var combo = e.combo;     
//							                if(!e.forceAll){     
//							                    var value = e.query;     
//							                    combo.store.filterBy(function(record,id){     
//							                        var text = record.get(combo.displayField);     
//							                        return (text.indexOf(value)!=-1);     
//							                    });  
//							                    combo.expand();     
//							                    return false;     
//							                }  
//							            } 
//                                    }
                        },{
                            // 该列在整行中所占的百分比
                            columnWidth : .45,
                            xtype : "datefield",
                            labelAlign: 'left',
                            fieldLabel : '鉴定日期',
                            emptyText : '请选择日期',
                            format : 'Y-m-d',
                            maxValue : new Date(),
                            value : Ext.Date.add(
                                new Date(),
                                Ext.Date.DAY),
                            allowBlank  : false,//不允许为空
                            blankText   : "不能为空",//错误提示信息，默认为This field is required!
                            labelWidth : 100,
                            maxLength :200,
                            name : 'identification_date'
                        },
                        {
                            // 该列在整行中所占的百分比
                            columnWidth : .45,
                            xtype : "numberfield",
                            labelAlign: 'left',
                            fieldLabel : '标准金额',
                            allowBlank  : false,//不允许为空
                            labelWidth : 100,
                            value:0,
						    maxLength :7,
                            name : 'stand_sum',
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
                            columnWidth : .45,
                            xtype : "numberfield",
                            labelAlign: 'left',
                            fieldLabel : '实收金额',
                            allowBlank  : false,//不允许为空
                            labelWidth : 100,
                            value:0,
						    maxLength :7,
                            name : 'real_sum',
                            listeners: {  
						        change: function(field, value) {  
						            if(value<0){
						            	 field.setValue(0); 
						            } 
						        }  
						   }
                        },{
                            // 该列在整行中所占的百分比
                            columnWidth : .45,
                            xtype : "numberfield",
                            labelAlign: 'left',
                            fieldLabel : '回款金额',
                            allowBlank  : false,//不允许为空
                            labelWidth : 100,
                            value:0,
						    maxLength :7,
                            name : 'return_sum',
                            listeners: {  
						        change: function(field, value) {  
						            if(value<0){
						            	 field.setValue(0); 
						            } 
						        }  
						   }
                        },{
                            // 该列在整行中所占的百分比
                            columnWidth : .45,
                            xtype : "textfield",
                            labelAlign: 'left',
                            labelWidth : 100,
                            fieldLabel : '发票编号',
                            maxLength :200,
                            name : 'invoice_number'
                        },
                        {
                            xtype : 'checkboxgroup',
                            labelAlign: 'left',
                            width:260,
                            labelWidth : 100,
                            fieldLabel : '鉴定材料',
                            columns : 1,
                            vertical : true,
                            style : 'margin-bottom:10px;',
                            height : 150,
                            name:'case_attachment_desc',
                            autoScroll : true,
                            allowBlank : false,
                            blankText : "不能为空",
                            items : items
                        },{
                            itemId:'opertype',
                            name:'opertype',
                            xtype:"textfield",
                            value:me.operType,
                            hidden:true
                        }
                ]},{
                    xtype : 'panel',
                    columnWidth:.3,
                    border : false,
                    items:[{
                        xtype : 'button',
                        text : '添加车辆样本',
                        listeners:{
                            click:function(){
                                var me = this.up("panel");
                                me.add({
                                    xtype : 'form',
                                    style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
                                    bodyPadding : 10,
                                    defaults : {
                                        anchor : '100%',
                                        labelWidth : 80,
                                        labelSeparator: "：",
                                        labelAlign: 'right'
                                    },
                                    border : false,
                                    autoHeight : true,
                                    items:[{
                                        layout:'auto',
                                        xtype:'panel',
                                        border:false,
                                        layout:'column',
                                        items:[{
                                            xtype:'panel',
                                            border:false,
                                            items:[{
                                                width : 200,
                                                xtype : "textfield",
                                                fieldLabel : '车牌号',
                                                allowBlank  : false,//不允许为空
                                                blankText   : "不能为空",//错误提示信息，默认为This field is required!
                                                labelAlign: 'left',
                                                maxLength :16,
                                                labelWidth : 80,
                                                name : 'plate_number'
                                            }, {
                                                width : 200,
                                                xtype : "textfield",
                                                fieldLabel : '车架号',
                                                labelAlign: 'left',
                                                maxLength :50,
                                                allowBlank  : false,//不允许为空
                                                blankText   : "不能为空",//错误提示信息，默认为This field is required!
                                                labelWidth : 80,
                                                name : 'vehicle_identification_number'
                                            }, {
                                                width : 200,
                                                xtype : "textfield",
                                                allowBlank  : false,
                                                fieldLabel : '发动机号',
                                                labelAlign: 'left',
                                                blankText   : "不能为空",
                                                maxLength :18,
                                                labelWidth : 80,
                                                name : 'engine_number'
                                            },{
                                                width : 200,
                                                xtype : "textfield",
                                                allowBlank  : false,
                                                fieldLabel : '车辆类型',
                                                labelAlign: 'left',
                                                blankText   : "不能为空",
                                                maxLength :18,
                                                labelWidth : 80,
                                                name : 'vehicle_type'
                                            },{
                                                xtype : 'button',
                                                text : '删除',
                                                listeners:{
                                                    click:function(){
                                                        var mei = this.up("form");
                                                        mei.disable(true);
                                                        mei.up("panel").remove(mei);
                                                    }
                                                }
                                            }]
                                        }]
                                    }]
                                });
                            }
                        }
                    }]
                },{
                    xtype : 'panel',
                    columnWidth:.3,
                    border : false,
                    items:[{
                        xtype : 'button',
                        text : '添加当事人',
                        listeners:{
                            click:function(){
                                var me = this.up("panel");
                                me.add({
                                    xtype : 'form',
                                    style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
                                    bodyPadding : 10,
                                    defaults : {
                                        anchor : '100%',
                                        labelWidth : 80,
                                        labelSeparator: "：",
                                        labelAlign: 'right'
                                    },
                                    border : false,
                                    autoHeight : true,
                                    items:[{
                                        layout:'auto',
                                        xtype:'panel',
                                        border:false,
                                        layout:'column',
                                        items:[{
                                            xtype:'panel',
                                            border:false,
                                            items:[{
                                                width : 200,
                                                xtype : "textfield",
                                                fieldLabel : '姓名',
                                                allowBlank  : false,//不允许为空
                                                blankText   : "不能为空",//错误提示信息，默认为This field is required!
                                                labelAlign: 'left',
                                                maxLength :50,
                                                labelWidth : 80,
                                                name : 'person_name'
                                            }, {
                                                width : 200,
                                                xtype : "textfield",
                                                fieldLabel : '身份证号',
                                                labelAlign: 'left',
                                                maxLength :50,
                                                labelWidth : 80,
                                                name : 'id_number',
                                                validator : function(value){
                                                    var result=false;
                                                    if(value!=null&&value!=""){
                                                        Ext.Ajax.request({
                                                            url:"judicial/register/verifyId_Number.do",
                                                            method: "POST",
                                                            async : false,
                                                            headers: { 'Content-Type': 'application/json' },
                                                            jsonData: {id_number:value},
                                                            success: function (response, options) {
                                                                response = Ext.JSON.decode(response.responseText);
                                                                if(!response){
                                                                    result= "身份证号码不正确";
                                                                }else{
                                                                    result= true;
                                                                }
                                                            },
                                                            failure: function () {
                                                                Ext.Msg.alert("提示", "网络异常<br>请联系管理员!");
                                                            }
                                                        });
                                                    }else{
                                                        result=true;
                                                    }
                                                    return result;
                                                }
                                            }, {
                                                width : 200,
                                                xtype : "textfield",
                                                fieldLabel : '家庭住址',
                                                labelAlign: 'left',
                                                maxLength :18,
                                                labelWidth : 80,
                                                name : 'address'
                                            },{
                                                xtype : 'button',
                                                text : '删除',
                                                listeners:{
                                                    click:function(){
                                                        var mei = this.up("form");
                                                        mei.disable(true);
                                                        mei.up("panel").remove(mei);
                                                    }
                                                }
                                            }]
                                        }]
                                    }]
                                });
                            }
                        }
                    }]
                }]
            }];

            me.buttons = [ {
                text : '保存',
                iconCls : 'Disk',
                handler : me.onSave
            }, {
                text : '取消',
                iconCls : 'Cancel',
                handler : me.onCancel
            } ]
            me.callParent(arguments);
        },
        onSave : function() {
            var me = this.up("form");
            var form = me.getForm();
            var values = form.getValues();
            if(form.isValid()){
        		Ext.MessageBox.wait('正在操作','请稍后...');
                Ext.Ajax.request({
                    url:"trace/register/insertOrUpdate.do",
                    method: "POST",
                    headers: { 'Content-Type': 'application/json' },
                    jsonData: values,
                    success: function (response, options) {
                        response = Ext.JSON.decode(response.responseText);
                        if (response.result==true) {
                            Ext.MessageBox.alert("提示信息", "添加案例成功");
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
            }
        },
        onCancel : function() {
            var me = this;
            me.up("window").close();
        }
    });
