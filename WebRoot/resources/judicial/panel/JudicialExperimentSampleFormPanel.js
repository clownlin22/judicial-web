/**
 * Created by lys on 2015/4/8.
 */
Ext.define('Rds.judicial.panel.JudicialExperimentSampleFormPanel', {
    extend:'Ext.form.Panel',
    bodyPadding: 5,
    width: 1000,
    config:{
      uuid:null,
      grid:null,
      experiment_no:null
    },

    // Fields will be arranged vertically, stretched to full width
    layout: {type:'table',columns:12},
    // The fields
    defaults:{
        xtype:'textfield',
        width:120,
        enableKeyEvents:true,
        validateOnChange:false,
        listeners:{
            'specialkey' : function(field, e) {
                if (e.getKey() == Ext.EventObject.TAB) {//or e.getKey() == e.ENTER
                    var me = this.up("form");
                    var form = me.getForm();
                    var values = form.getValues();

                        Ext.Ajax.request({
                            url: "judicial/experiment/savePlaces.do",
                            method: "POST",
                            headers: { 'Content-Type': 'application/json' },
                            jsonData: values,
                            async:true
                        });

                }
            }
        },
        validator : function(value){
            var result=false;
            if(!value ||value=='LADDER'){
                return true;
            }
            Ext.Ajax.request({
                url:"judicial/experiment/verifySampleCode.do",
                method: "POST",
                async : false,
                headers: { 'Content-Type': 'application/json' },
                jsonData: {sample_code:value,
                           experiment_no:this.up("form").experiment_no},
                success: function (response, options) {
//                    console.log(response);
//                    console.log(options);
                    response = Ext.JSON.decode(response.responseText);
                    if(!response){
                        //Ext.Msg.alert("发现样本重复","此条形码"+value+"曾经完成过实验，请确认是否继续");
                        result= "此条形码曾经完成过实验";
                    }else{
                        result= true;
                    }
                },
                failure: function () {
                    Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                }
            });
            return result;
        }
    },
    autoScroll:true,
    initComponent:function(){
        var me = this;
        me.items = [{
            hidden:true,
            name:'uuid',
            value:me.uuid
        },{
            name: 'A1',
            itemId:'A1',
            tabIndex:1,
            fieldStyle:'background-image:url(resources/judicial/imgs/1.png)'
        },{
            name: 'A2',
            itemId:'A2',
            tabIndex:8,
            fieldStyle:'background-image:url(resources/judicial/imgs/9.png)'
        },{
            name: 'A3',
            itemId:'A3',
            tabIndex:16,
            fieldStyle:'background-image:url(resources/judicial/imgs/17.png)'
        },{
            name: 'A4',
            itemId:'A4',
            tabIndex:24,
            fieldStyle:'background-image:url(resources/judicial/imgs/25.png)'
        },{
            name: 'A5',
            itemId:'A5',
            tabIndex:32,
            fieldStyle:'background-image:url(resources/judicial/imgs/33.png)'
        },{
            name: 'A6',
            itemId:'A6',
            tabIndex:40,
            fieldStyle:'background-image:url(resources/judicial/imgs/41.png)'
        },{
            name: 'A7',
            itemId:'A7',
            tabIndex:47,
            fieldStyle:'background-image:url(resources/judicial/imgs/49.png)'
        },{
            name: 'A8',
            itemId:'A8',
            tabIndex:55,
            fieldStyle:'background-image:url(resources/judicial/imgs/57.png)'
        },{
            name: 'A9',
            itemId:'A9',
            tabIndex:63,
            fieldStyle:'background-image:url(resources/judicial/imgs/65.png)'
        },{
            name: 'A10',
            itemId:'A10',
            tabIndex:71,
            fieldStyle:'background-image:url(resources/judicial/imgs/73.png)'
        },{
            name: 'A11',
            itemId:'A11',
            tabIndex:79,
            fieldStyle:'background-image:url(resources/judicial/imgs/81.png)'
        },{
            name: 'A12',
            itemId:'A12',
            value:'LADDER',
            readOnly:true,
            fieldStyle:'background-image:url(resources/judicial/imgs/89.png)'
        },{
            name: 'A13',
            itemId:'A13',
            tabIndex:2,
            fieldStyle:'background-image:url(resources/judicial/imgs/2.png)'
        },{
            name: 'A14',
            itemId:'A14',
            tabIndex:9,
            fieldStyle:'background-image:url(resources/judicial/imgs/10.png)'
        },{
            name: 'A15',
            itemId:'A15',
            tabIndex:17,
            fieldStyle:'background-image:url(resources/judicial/imgs/18.png)'
        },{
            name: 'A16',
            itemId:'A16',
            tabIndex:25,
            fieldStyle:'background-image:url(resources/judicial/imgs/26.png)'
        },{
            name: 'A17',
            itemId:'A17',
            tabIndex:33,
            fieldStyle:'background-image:url(resources/judicial/imgs/34.png)'
        },{
            name: 'A18',
            itemId:'A18',
            tabIndex:41,
            fieldStyle:'background-image:url(resources/judicial/imgs/42.png)'
        },{
            name: 'A19',
            itemId:'A19',
            tabIndex:48,
            fieldStyle:'background-image:url(resources/judicial/imgs/50.png)'
        },{
            name: 'A20',
            itemId:'A20',
            tabIndex:56,
            fieldStyle:'background-image:url(resources/judicial/imgs/58.png)'
        },{
            name: 'A21',
            itemId:'A21',
            tabIndex:64,
            fieldStyle:'background-image:url(resources/judicial/imgs/66.png)'
        },{
            name: 'A22',
            itemId:'A22',
            tabIndex:72,
            fieldStyle:'background-image:url(resources/judicial/imgs/74.png)'
        },{
            name: 'A23',
            itemId:'A23',
            tabIndex:80,
            fieldStyle:'background-image:url(resources/judicial/imgs/82.png)'
        },{
            name: 'A24',
            itemId:'A24',
            tabIndex:87,
            fieldStyle:'background-image:url(resources/judicial/imgs/90.png)'
        },{
            name: 'A25',
            itemId:'A25',
            tabIndex:3,
            fieldStyle:'background-image:url(resources/judicial/imgs/3.png)'
        },{
            name: 'A26',
            itemId:'A26',
            tabIndex:10,
            fieldStyle:'background-image:url(resources/judicial/imgs/11.png)'
        },{
            name: 'A27',
            itemId:'A27',
            tabIndex:18,
            fieldStyle:'background-image:url(resources/judicial/imgs/19.png)'
        },{
            name: 'A28',
            itemId:'A28',
            tabIndex:26,
            fieldStyle:'background-image:url(resources/judicial/imgs/27.png)'
        },{
            name: 'A29',
            itemId:'A29',
            tabIndex:34,
            fieldStyle:'background-image:url(resources/judicial/imgs/35.png)'
        },{
            name: 'A30',
            itemId:'A30',
            tabIndex:42,
            fieldStyle:'background-image:url(resources/judicial/imgs/43.png)'
        },{
            name: 'A31',
            itemId:'A31',
            tabIndex:49,
            fieldStyle:'background-image:url(resources/judicial/imgs/51.png)'
        },{
            name: 'A32',
            itemId:'A32',
            tabIndex:57,
            fieldStyle:'background-image:url(resources/judicial/imgs/59.png)'
        },{
            name: 'A33',
            itemId:'A33',
            tabIndex:65,
            fieldStyle:'background-image:url(resources/judicial/imgs/67.png)'
        },{
            name: 'A34',
            itemId:'A34',
            tabIndex:73,
            fieldStyle:'background-image:url(resources/judicial/imgs/75.png)'
        },{
            name: 'A35',
            itemId:'A35',
            tabIndex:81,
            fieldStyle:'background-image:url(resources/judicial/imgs/83.png)'
        },{
            name: 'A36',
            itemId:'A36',
            tabIndex:88,
            fieldStyle:'background-image:url(resources/judicial/imgs/91.png)'
        },{
            name: 'A37',
            itemId:'A37',
            tabIndex:4,
            fieldStyle:'background-image:url(resources/judicial/imgs/4.png)'
        },{
            name: 'A38',
            itemId:'A38',
            tabIndex:11,
            fieldStyle:'background-image:url(resources/judicial/imgs/12.png)'
        },{
            name: 'A39',
            itemId:'A39',
            tabIndex:19,
            fieldStyle:'background-image:url(resources/judicial/imgs/20.png)'
        },{
            name: 'A40',
            itemId:'A40',
            tabIndex:27,
            fieldStyle:'background-image:url(resources/judicial/imgs/28.png)'
        },{
            name: 'A41',
            itemId:'A41',
            tabIndex:35,
            fieldStyle:'background-image:url(resources/judicial/imgs/36.png)'
        },{
            name: 'A42',
            itemId:'A42',
            tabIndex:43,
            fieldStyle:'background-image:url(resources/judicial/imgs/44.png)'
        },{
            name: 'A43',
            itemId:'A43',
            tabIndex:50,
            fieldStyle:'background-image:url(resources/judicial/imgs/52.png)'
        },{
            name: 'A44',
            itemId:'A44',
            tabIndex:58,
            fieldStyle:'background-image:url(resources/judicial/imgs/60.png)'
        },{
            name: 'A45',
            itemId:'A45',
            tabIndex:66,
            fieldStyle:'background-image:url(resources/judicial/imgs/68.png)'
        },{
            name: 'A46',
            itemId:'A46',
            tabIndex:74,
            fieldStyle:'background-image:url(resources/judicial/imgs/76.png)'
        },{
            name: 'A47',
            itemId:'A47',
            tabIndex:82,
            fieldStyle:'background-image:url(resources/judicial/imgs/84.png)'
        },{
            name: 'A48',
            itemId:'A48',
            tabIndex:89,
            fieldStyle:'background-image:url(resources/judicial/imgs/92.png)'
        },{
            name: 'A49',
            itemId:'A49',
            tabIndex:5,
            fieldStyle:'background-image:url(resources/judicial/imgs/5.png)'
        },{
            name: 'A50',
            itemId:'A50',
            tabIndex:12,
            fieldStyle:'background-image:url(resources/judicial/imgs/13.png)'
        },{
            name: 'A51',
            itemId:'A51',
            tabIndex:20,
            fieldStyle:'background-image:url(resources/judicial/imgs/21.png)'
        },{
            name: 'A52',
            itemId:'A52',
            tabIndex:28,
            fieldStyle:'background-image:url(resources/judicial/imgs/29.png)'
        },{
            name: 'A53',
            itemId:'A53',
            tabIndex:36,
            fieldStyle:'background-image:url(resources/judicial/imgs/37.png)'
        },{
            name: 'A54',
            itemId:'A54',
            tabIndex:44,
            fieldStyle:'background-image:url(resources/judicial/imgs/45.png)'
        },{
            name: 'A55',
            itemId:'A55',
            tabIndex:51,
            fieldStyle:'background-image:url(resources/judicial/imgs/53.png)'
        },{
            name: 'A56',
            itemId:'A56',
            tabIndex:59,
            fieldStyle:'background-image:url(resources/judicial/imgs/61.png)'
        },{
            name: 'A57',
            itemId:'A57',
            tabIndex:67,
            fieldStyle:'background-image:url(resources/judicial/imgs/69.png)'
        },{
            name: 'A58',
            itemId:'A58',
            tabIndex:75,
            fieldStyle:'background-image:url(resources/judicial/imgs/77.png)'
        },{
            name: 'A59',
            itemId:'A59',
            tabIndex:83,
            fieldStyle:'background-image:url(resources/judicial/imgs/85.png)'
        },{
            name: 'A60',
            itemId:'A60',
            tabIndex:90,
            fieldStyle:'background-image:url(resources/judicial/imgs/93.png)'
        },{
            name: 'A61',
            itemId:'A61',
            tabIndex:6,
            fieldStyle:'background-image:url(resources/judicial/imgs/6.png)'
        },{
            name: 'A62',
            itemId:'A62',
            tabIndex:13,
            fieldStyle:'background-image:url(resources/judicial/imgs/14.png)'
        },{
            name: 'A63',
            itemId:'A63',
            tabIndex:21,
            fieldStyle:'background-image:url(resources/judicial/imgs/22.png)'
        },{
            name: 'A64',
            itemId:'A64',
            tabIndex:29,
            fieldStyle:'background-image:url(resources/judicial/imgs/30.png)'
        },{
            name: 'A65',
            itemId:'A65',
            tabIndex:37,
            fieldStyle:'background-image:url(resources/judicial/imgs/38.png)'
        },{
            name: 'A66',
            itemId:'A66',
            tabIndex:45,
            fieldStyle:'background-image:url(resources/judicial/imgs/46.png)'
        },{
            name: 'A67',
            itemId:'A67',
            tabIndex:52,
            fieldStyle:'background-image:url(resources/judicial/imgs/54.png)'
        },{
            name: 'A68',
            itemId:'A68',
            tabIndex:60,
            fieldStyle:'background-image:url(resources/judicial/imgs/62.png)'
        },{
            name: 'A69',
            itemId:'A69',
            tabIndex:68,
            fieldStyle:'background-image:url(resources/judicial/imgs/70.png)'
        },{
            name: 'A70',
            itemId:'A70',
            tabIndex:76,
            fieldStyle:'background-image:url(resources/judicial/imgs/78.png)'
        },{
            name: 'A71',
            itemId:'A71',
            tabIndex:84,
            fieldStyle:'background-image:url(resources/judicial/imgs/86.png)'
        },{
            name: 'A72',
            itemId:'A72',
            tabIndex:91,
            fieldStyle:'background-image:url(resources/judicial/imgs/94.png)'
        },{
            name: 'A73',
            itemId:'A73',
            tabIndex:7,
            fieldStyle:'background-image:url(resources/judicial/imgs/7.png)'
        },{
            name: 'A74',
            itemId:'A74',
            tabIndex:14,
            fieldStyle:'background-image:url(resources/judicial/imgs/15.png)'
        },{
            name: 'A75',
            itemId:'A75',
            tabIndex:22,
            fieldStyle:'background-image:url(resources/judicial/imgs/23.png)'
        },{
            name: 'A76',
            itemId:'A76',
            tabIndex:30,
            fieldStyle:'background-image:url(resources/judicial/imgs/31.png)'
        },{
            name: 'A77',
            itemId:'A77',
            tabIndex:38,
            fieldStyle:'background-image:url(resources/judicial/imgs/39.png)'
        },{
            name: 'A78',
            itemId:'A78',
            tabIndex:46,
            fieldStyle:'background-image:url(resources/judicial/imgs/47.png)'
        },{
            name: 'A79',
            itemId:'A79',
            tabIndex:53,
            fieldStyle:'background-image:url(resources/judicial/imgs/55.png)'
        },{
            name: 'A80',
            itemId:'A80',
            tabIndex:61,
            fieldStyle:'background-image:url(resources/judicial/imgs/63.png)'
        },{
            name: 'A81',
            itemId:'A81',
            tabIndex:69,
            fieldStyle:'background-image:url(resources/judicial/imgs/71.png)'
        },{
            name: 'A82',
            itemId:'A82',
            tabIndex:77,
            fieldStyle:'background-image:url(resources/judicial/imgs/79.png)'
        },{
            name: 'A83',
            itemId:'A83',
            tabIndex:85,
            fieldStyle:'background-image:url(resources/judicial/imgs/87.png)'
        },{
            name: 'A84',
            itemId:'A84',
            tabIndex:92,
            fieldStyle:'background-image:url(resources/judicial/imgs/95.png)'
        },{
            name: 'A85',
            itemId:'A85',
            value:'LADDER',
            readOnly:true,
            fieldStyle:'background-image:url(resources/judicial/imgs/8.png)'
        },{
            name: 'A86',
            itemId:'A86',
            tabIndex:15,
            fieldStyle:'background-image:url(resources/judicial/imgs/16.png)'
        },{
            name: 'A87',
            itemId:'A87',
            tabIndex:23,
            fieldStyle:'background-image:url(resources/judicial/imgs/24.png)'
        },{
            name: 'A88',
            itemId:'A88',
            tabIndex:31,
            fieldStyle:'background-image:url(resources/judicial/imgs/32.png)'
        },{
            name: 'A89',
            itemId:'A89',
            tabIndex:39,
            fieldStyle:'background-image:url(resources/judicial/imgs/40.png)'
        },{
            name: 'A90',
            itemId:'A90',
            value:'LADDER',
            readOnly:true,
            fieldStyle:'background-image:url(resources/judicial/imgs/48.png)'
        },{
            name: 'A91',
            itemId:'A91',
            tabIndex:54,
            fieldStyle:'background-image:url(resources/judicial/imgs/56.png)'
        },{
            name: 'A92',
            itemId:'A92',
            tabIndex:62,
            fieldStyle:'background-image:url(resources/judicial/imgs/64.png)'
        },{
            name: 'A93',
            itemId:'A93',
            tabIndex:70,
            fieldStyle:'background-image:url(resources/judicial/imgs/72.png)'
        },{
            name: 'A94',
            itemId:'A94',
            tabIndex:78,
            fieldStyle:'background-image:url(resources/judicial/imgs/80.png)'
        },{
            name: 'A95',
            itemId:'A95',
            tabIndex:86,
            fieldStyle:'background-image:url(resources/judicial/imgs/88.png)'
        },{
            name: 'A96',
            itemId:'A96',
            tabIndex:93,
            fieldStyle:'background-image:url(resources/judicial/imgs/96.png)'
        }];

        me.buttons = [{
            text:'保存',
            iconCls:'Disk',
            handler:me.onSave
        },{
            text:'取消',
            iconCls:'Cancel',
            handler:me.onCancel
        }];

        me.callParent(arguments);
    },
    onSave:function(){
        var me = this.up("form");
        var form = me.getForm();
        var values = form.getValues();
        //进度条
        var progressBar = new Ext.ProgressBar({
			    width : 200
			   });
			 
			   var progressBarWin = new Ext.Window({
			    title : "执行",
			    modal:true,
			    width : 200,
			    heigth : 100,
			    closable : false,
			    items : progressBar
			   });
        if(!form.isValid()){
            Ext.MessageBox
                .confirm(
                '提示',
                '存在做过实验的样本号，是否保存',
                function(id){
                    if(id=="yes"){
                    	progressBarWin.show();
					      progressBar.wait({
					       text : "正在执行保存操作..."
					      });
                        Ext.Ajax.request({
                            url:"judicial/experiment/savePlaces.do",
                            method: "POST",
                            headers: { 'Content-Type': 'application/json' },
                            jsonData: values,
                            success: function (response, options) {
                                response = Ext.JSON.decode(response.responseText);
                                if (response.result == true) {
                                	 progressBarWin.hide();
                                    Ext.MessageBox.alert("提示信息", response.message);
                                    me.grid.getStore().load();
                                    me.up("window").close();
                                }else {
                                	 progressBarWin.hide();
                                    Ext.MessageBox.alert("错误信息", response.message);
                                }
                            },
                            failure: function () {
                                Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                            }
                        });
                    }
                })
        }else{
			    progressBarWin.show();
					      progressBar.wait({
					       text : "正在执行保存操作..."
					      });
      
            Ext.Ajax.request({
                url:"judicial/experiment/savePlaces.do",
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                jsonData: values,
                success: function (response, options) {
                    response = Ext.JSON.decode(response.responseText);
                    if (response.result == true) {
                    	 progressBarWin.hide();
                    	 progressBar.reset();
                        Ext.MessageBox.alert("提示信息", response.message);
                        me.grid.getStore().load();
                        me.up("window").close();
                    }else {
                        Ext.MessageBox.alert("错误信息", response.message);
                    }
                },
                failure: function () {
                	 progressBarWin.hide();
                    Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                }
            });
        }
    },
    onCancel:function(){
        var me = this;
        me.up("window").close();
    }
});