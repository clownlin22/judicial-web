/**
 * @author lys
 * @description 样本记录上传表单
 * @date 20150407
 */
var storeReagent=Ext.create(
    'Ext.data.Store',
    {
        fields: ['reagent_name'],
        proxy:{
            type: 'jsonajax',
            actionMethods:{read:'POST'},
            url:'judicial/reagents/queryReagents.do',
            reader:{
                type:'json',
                root:'data'
            },
            params:{enable_flag:'Y'}
        },
        remoteSort:true
    }
);
var storeExperiment=Ext.create(
    'Ext.data.Store',
    {
        storeId:'Rds.judicial.panel.ExperimentStore',
        fields: ['experiment_no'],
        proxy:{
            type: 'jsonajax',
            actionMethods:{read:'POST'},
            url:'judicial/experiment/queryExperimentBySample.do',
            reader:{
                type:'json',
                root:'data'
            },
            params:{datelimite:'Y'}
        },
        remoteSort:true
    }
);
var storeIdentifyPer=Ext.create(
    'Ext.data.Store',
    {
        storeId:'Rds.judicial.panel.IdentifyPerStore',
        fields: ['identify_id','identify_per'],
        proxy:{
            type: 'jsonajax',
            actionMethods:{read:'POST'},
            url:'judicial/experiment/identifyPer.do',
            reader:{
                type:'json',
                root:'data'
            }
        },
        remoteSort:true
    }
);
Ext.define('Rds.judicial.panel.JudicialDataUploadFormPanel', {
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
				labelWidth : 100
			},
			border : false,
			autoHeight : true,
			items : [{
                xtype : "combo",
                fieldLabel : '实验编号',
                labelWidth : 100,
                mode: 'remote',
                forceSelection: true,
                allowBlank  : false,//不允许为空
                blankText   : "不能为空",//错误提示信息，默认为This field is required!
                emptyText:'请选择实验编号',
                name:'experiment_no',
                valueField :"experiment_no",
                displayField: "experiment_no",
                store: storeExperiment,
                editable:false
                },{
                xtype : "combo",
                fieldLabel : '试剂名称',
                labelWidth : 100,
                mode: 'remote',
                forceSelection: true,
                allowBlank  : false,//不允许为空
                blankText   : "不能为空",//错误提示信息，默认为This field is required!
                emptyText:'请选择试剂',
                name:'reagent_name',
                valueField :"reagent_name",
                displayField: "reagent_name",
                store: storeReagent,
                editable:false
            },{
                xtype : "combo",
                fieldLabel : '鉴定人(可多选)',
                labelWidth : 100,
                mode: 'remote',
                forceSelection: true,
                allowBlank  : false,//不允许为空
                blankText   : "不能为空",//错误提示信息，默认为This field is required!
                emptyText:'请选择鉴定人',
                name:'identify_id',
                id:'identify_id',
                valueField :"identify_id",
                displayField: "identify_per",
                store: storeIdentifyPer,
                editable:false,
                multiSelect:true

            },{
                xtype : 'filefield',
                name : 'mfiles',
                fieldLabel : '文件',
                labelWidth : 100,
                msgTarget : 'side',
                allowBlank : false,
                anchor : '100%',
                buttonText : '选择文件'
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
        //storeIdentifyPer.load();
		me.callParent(arguments);
	},
	onSave : function() {
        var me = this;
        var myWindow = me.up('window');
        var formObject = me.up('form');
        var form = me.up('form').getForm();
        if (!form.isValid()) {
            Ext.MessageBox.alert("提示信息", "请填写完整表单信息!");
            return;
        }else{
            Ext.MessageBox.confirm("提示", "请确认导入数据实验编号无误", function (btn) {
                if ("yes" == btn) {
                    form.submit({
                        url: 'judicial/sample/upload.do',
                        method: 'post',
                        waitMsg: '正在上传您的文件...',
                        success: function (form, action) {
                            //console.log(action);
                            var response = Ext.JSON
                                .decode(action.response.responseText);
                            if (response.result == true) {
                                Ext.MessageBox.alert("提示信息", response.message);
                                me.up('form').grid.getStore().load();
                                console.log(Ext.data.StoreManager.lookup('Rds.judicial.panel.ExperimentStore'));
                                Ext.data.StoreManager.lookup('Rds.judicial.panel.ExperimentStore').load();
                                var store1 = Ext.data.StoreManager.lookup('Rds.judicial.panel.JudicialCrossCompareGridStore');
                                var store2 = Ext.data.StoreManager.lookup('Rds.judicial.panel.JudicialMissingDataGridStore');
                                var store3 = Ext.data.StoreManager.lookup('Rds.judicial.panel.JudicialMissingExperimentGridStore');
                                var store4 = Ext.data.StoreManager.lookup('Rds.judicial.panel.JudicialExceptionGridStore');
                                if(store1!=undefined){
                                    store1.load();
                                }
                                if(store2!=undefined){
                                    store1.load();
                                }
                                if(store3!=undefined){
                                    store1.load();
                                }
                                if(store4!=undefined){
                                    store1.load();
                                }

                                myWindow.close();
                            } else {
                                Ext.MessageBox.alert("错误信息", response.message);
//                                myWindow.close();
                            }
                        },
                        failure: function () {
                            Ext.Msg.alert("提示", "上传失败<br>请联系管理员!");
                        }
                    });
                }
            })
        }

	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	}

});