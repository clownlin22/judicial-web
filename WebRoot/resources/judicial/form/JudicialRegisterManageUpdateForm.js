var storeModel = Ext.create('Ext.data.Store', {
	model : 'model',
	proxy : {
		type : 'jsonajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'judicial/dicvalues/getReportModelByPartner.do',
		params:{
        	 type:'dna',
        	 receiver_id:""
        },
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	autoLoad : true,
	remoteSort : true
});
var storeSampleType = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'ajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getSampleType.do',
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
var storeSampleCall = Ext.create('Ext.data.Store', {
			model : 'model',
			proxy : {
				type : 'ajax',
				actionMethods : {
					read : 'POST'
				},
				url : 'judicial/dicvalues/getSampleCall.do',
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			remoteSort : true
		});
var unitStore=Ext.create(
        'Ext.data.Store',
        {
          model:'model',
          proxy:{
    		type: 'jsonajax',
    		actionMethods:{read:'POST'},
            url:'judicial/dicvalues/getUnitTypes.do',
            params:{
          	  initials:""
            },
            reader:{
              type:'json',
              root:'data'
            }
          },
          autoLoad:true,
          remoteSort:true						
        }
   );
var caseTypeModel = Ext.create('Ext.data.Store',{
    model:'model',
    proxy:{
  	type: 'jsonajax',
      actionMethods:{read:'POST'},
      url:'judicial/dicvalues/getCaseTypes.do',
      reader:{
        type:'json',
        root:'data'
      }
    },
    autoLoad:true,
    remoteSort:true						
  }
);
var imgs="";
var img_count = 0;
var img_show_index = 0;
Ext.define('Rds.judicial.form.JudicialRegisterManageUpdateForm', {
	extend : 'Ext.form.Panel',
	layout : "border",
	initComponent : function() {
		var me = this;
		var receiver=Ext.create('Ext.form.ComboBox', {
				xtype : 'combo',
				columnWidth : .45,
				fieldLabel : '归属人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
				labelWidth : 80,
				labelAlign : 'right',
				name : 'receiver_id_update',
				emptyText:'检索方式(姓名首字母)：如小红(xh)',
				store : Ext.create("Ext.data.Store",{
					 fields:[
			                  {name:'key',mapping:'key',type:'string'},
			                  {name:'value',mapping:'value',type:'string'},
			                  {name:'name',mapping:'name',type:'string'},
			                  {name:'id',mapping:'id',type:'string'},
			          ],
					pageSize : 10,
					proxy : {
						type : "ajax",
						url:"judicial/dicvalues/getUpcUsers.do?area_id="+ownpersonTemp,
						reader : {
							type : "json"
						}
					}
				}),
				displayField : 'name',
				valueField : 'id',allowBlank  : false,//不允许为空  
                blankText   : "不能为空",//错误提示信息，默认为This field is required!  
				forceSelection: true,
				typeAhead : false,
				hideTrigger : true,
				minChars : 2,
				matchFieldWidth : true,
				listConfig : {
					loadingText : '正在查找...',
					emptyText : '没有找到匹配的数据'
				},
				listeners : {
					 select:function(combo,record,opts) {  
							Ext.apply(modeltype.store.proxy.params,{receiver_id:receiver.getValue()});
							modeltype.setValue("");
							modeltype.store.load();
					}, 
					'afterrender':function(){
						if("" != ownpersonTemp)
						{
							this.store.load();
						}
					}
				}
//					fieldLabel : "归属人<span style='color:red'>*</span>",
//					labelAlign: 'right',
//					columnWidth : .45,
//					labelWidth : 80,
//					mode: 'remote',   
//			        forceSelection: true,   
//			        allowBlank  : false,//不允许为空  
//	                blankText   : "不能为空",//错误提示信息，默认为This field is required!  
//			        emptyText:'检索方式：如小红(xh)',  
//					name:'receiver_id_update',
//					valueField :"id",   
//			        displayField: "name",   
//					store: receiverStore,
//					listeners: {
//						 select:function(combo,record,opts) {  
//								Ext.apply(modeltype.store.proxy.params,{receiver_id:receiver.getValue()});
//								modeltype.setValue("");
//								modeltype.store.load();
//						}, 
//						'beforequery' : function(e) {  
//			                var combo = e.combo;     
//			                if(!e.forceAll){     
//			                    var value = e.query;     
//			                    combo.store.filterBy(function(record,id){     
//			                        var text = record.get(combo.displayField);     
//			                        return (text.indexOf(value)!=-1);     
//			                    });  
//			                    combo.expand();     
//			                    return false;     
//			                }  
//			            }   
//			        }
		     });
		var area=Ext.create('Ext.form.ComboBox',{
				columnWidth : .45,
				fieldLabel : '身份证地址',
				labelWidth : 80,
				labelAlign : 'right',
				name : 'area',
				id:'judicial_area_name_manage_update',
				emptyText:'检索方式：如朝阳区(cy)',
				store : Ext.create("Ext.data.Store",{
					 fields:[
			                  {name:'key',mapping:'key',type:'string'},
			                  {name:'value',mapping:'value',type:'string'},
			                  {name:'name',mapping:'name',type:'string'},
			                  {name:'id',mapping:'id',type:'string'},
			          ],
					pageSize : 10,
					// autoLoad: true,
					proxy : {
						type : "ajax",
						url:"judicial/dicvalues/getAreaInfo.do?area_code="+ownaddressTemp,
						reader : {
							type : "json"
						}
					}
				}),
				displayField : 'value',
				valueField : 'id',
				forceSelection: true,
				typeAhead : false,
				hideTrigger : true,
				minChars : 2,
				matchFieldWidth : true,
				listConfig : {
					loadingText : '正在查找...',
					emptyText : '没有找到匹配的数据'
				},
				listeners : {
					'afterrender':function(){
						if("" != ownaddressTemp)
						{
							this.store.load();
						}
					}
				}
//				fieldLabel : '身份证地址',
//				id:'judicial_area_name_manage_update',
//				queryMode: 'remote',   
//		        forceSelection: true,  
//				labelWidth : 80,
//				columnWidth : 0.45,
//				labelAlign: 'right',
//				blankText:'不能为空',   
//		        emptyText:'检索方式：如朝阳区(cy)',  
//				name : 'area',
//				valueField :"id",   
//		        displayField: "value",   
//				store: areaStore,
//				listeners: {
//					'beforequery' : function(e) {  
//		                var combo = e.combo;     
//		                if(!e.forceAll){     
//		                    var value = e.query;     
//		                    combo.store.filterBy(function(record,id){     
//		                        var text = record.get(combo.displayField);     
//		                        return (text.indexOf(value)!=-1);     
//		                    });  
//		                    combo.expand();     
//		                    return false;     
//		                }  
//		            }  
//			    }
	          });
		var attach_need = new Ext.form.CheckboxGroup({
            fieldLabel: '是否需要副本',
            labelWidth : 110,
            columnWidth : .45,
            items: [{
                boxLabel: '需要',
                inputValue: '0',
                name: 'attach_need',
            }]
        });
		var modeltype=Ext.create('Ext.form.ComboBox', 
				{
					columnWidth : .45,
					xtype : "combo",
					fieldLabel : "模板类型<span style='color:red'>*</span>",
					mode: 'local',   
					labelWidth : 80,
					editable:false,
					labelAlign: 'right',
					blankText:'请选择模板',   
			        emptyText:'请选择模板',  
			        allowBlank  : false,//不允许为空  
		            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
					valueField :"key",   
			        displayField: "value", 
					name : 'report_model',
					store: storeModel
		   });
		me.items = [{
			xtype : 'form',
			region : 'west',
			width : 750,
			defaults : {
				anchor : '100%',
				labelWidth : 80,
				labelSeparator : "：",
				labelAlign : 'right'
			},
			style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
			border : false,
			autoHeight : true,
			items : [{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					style : 'margin-top:20px;',
					items : [{
								xtype : 'hiddenfield',
								name : 'case_id'
							}, {
								xtype : 'hiddenfield',
								name : 'case_areacode'
							},{
								xtype : 'hiddenfield',
								name : 'attach_need_case'
							},{
								xtype : 'hiddenfield',
								name : 'sample_in_time'
							},{
								xtype : 'hiddenfield',
								name : 'is_new',
							},{
								// 该列在整行中所占的百分比
								columnWidth : .45,
								xtype : "textfield",
								labelAlign : 'right',
								fieldLabel : "案例条形码<span style='color:red'>*</span>",
								readOnly : true,
								emptyText : '自动生成',
								labelWidth : 80,
//								allowBlank : false,// 不允许为空
//								blankText : "不能为空",// 错误提示信息，默认为This field is
								// required!
								maxLength : 50,
								name : 'case_code'
							}, new Ext.form.field.ComboBox({
										fieldLabel : '紧急程度',
										columnWidth : .45,
										labelWidth : 80,
										editable : false,
										triggerAction : 'all',
										displayField : 'Name',
										labelAlign : 'right',
										valueField : 'Code',
										store : new Ext.data.ArrayStore({
													fields : ['Name', 'Code'],
													data : [['普通', 0],
															['紧急', 1]]
												}),
										mode : 'local',
										// typeAhead: true,
										name : 'urgent_state'
									})]
				}, {
					layout : "column",// 从左往右的布局
					xtype : 'fieldcontainer',
					border : false,
					items : [{
								xtype : 'hiddenfield',
								name : 'receiver_id'
							},  receiver,area]
				}, 		
				{
					layout : "column",// 从左往右的布局
					xtype : 'fieldcontainer',
					border : false,
					items : [ attach_need ]
				},
				{
					layout : "column",// 从左往右的布局
					xtype : 'fieldcontainer',
					border : false,
					    items : [
								{
									// 该列在整行中所占的百分比
									columnWidth : .45,
									xtype : "textfield",
									labelAlign: 'right',
									fieldLabel : '采样人员',
									labelWidth : 80,
								    maxLength :50,
									name : 'sample_in_per',
								},
								{
									columnWidth : .45,
									xtype : "combo",
									fieldLabel : "单位类型<span style='color:red'>*</span>",
									mode: 'local',   
									labelWidth : 80,
									editable:false,
									labelAlign: 'right',
									blankText:'请选择单位',   
							        emptyText:'请选择单位',  
							        allowBlank  : false,//不允许为空  
						            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
									valueField :"key",   
							        displayField: "value",    
									name : 'unit_type',
									store: unitStore
								}
					             ]
				},{
					layout : "column",// 从左往右的布局
					xtype : 'fieldcontainer',
					border : false,
					items : [{
								// 该列在整行中所占的百分比
								columnWidth : .45,
								xtype : "textfield",
								labelAlign : 'right',
								fieldLabel : '电话号码',
								labelWidth : 80,
								maxLength : 50,
								name : 'phone'

							}, modeltype]
				}, {
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					items : [{
								// 该列在整行中所占的百分比
								columnWidth : .45,
								xtype : "textfield",
								labelAlign : 'right',
								labelWidth : 80,
								fieldLabel : '委托人',
								maxLength : 50,
								name : 'client'

							}, Ext.create('Ext.form.ComboBox', {
								fieldLabel: "单双亲<span style='color:red'>*</span>",
			                    labelWidth : 80,
			                    editable:false,
			                    triggerAction: 'all',
			                    displayField: 'Name',
			                    labelAlign: 'right',
			                    columnWidth : .45,
			                    value:1,
			                    valueField: 'Code',
			                    store: new Ext.data.ArrayStore({
			                        fields: ['Name', 'Code'],
			                        data: [['单亲', 1], ['双亲', 2]]
			                    })          ,
			                    mode: 'local',
			                    //typeAhead: true,
			                    name: 'typeid'
							});]
				},{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					items : [{
						xtype : 'hiddenfield',
						name : 'case_type'
					},{
						xtype : "combo",
	                    fieldLabel: "案例类型<span style='color:red'>*</span>",
	                    name: 'case_type_update',
						mode: 'local',   
						labelWidth : 80,
						columnWidth : .45,
						editable:false,
						labelAlign: 'right',
						blankText:'请选择类型',   
				        emptyText:'请选择类型',  
				        allowBlank  : false,//不允许为空  
			            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
						valueField :"key", 
						store: caseTypeModel,
				        displayField: "value"  
	                }, new Ext.form.field.ComboBox({
	                    fieldLabel: "亲属关系<span style='color:red'>*</span>",
	                    columnWidth : .45,
	                    labelWidth : 80,
	                    editable:false,
	                    triggerAction: 'all',
	                    displayField: 'Name',
	                    labelAlign: 'right',
	                    valueField: 'Code',
	                    store: new Ext.data.ArrayStore({
	                        fields: ['Name', 'Code'],
	                        data: [['亲子', 0], ['亲缘', 1],['同胞',2]]
	                    })          ,
	                    mode: 'local',
	                    //typeAhead: true,
	                    name: 'sample_relation'
	                })]
				},{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					items : [{
						xtype : 'textarea',
						fieldLabel : '备注',
						name : 'remark',
						width : 500,
						 maxLength :500,
						columnWidth : 0.9,
						labelWidth : 80,
						labelAlign : 'right'
					}]
				}]
		}, {
			xtype : 'panel', // 或者xtype: 'component',
			region : 'center',
			name:'img',
			autoScroll : true,
			items : [],
			buttons : [{
						text : '打印预览',
						iconCls : 'Printercolor',
						handler : me.onPrint
					}, {
						text : '左转',
						iconCls : 'Arrowturnleft',
						handler : me.onTurnLeft
					}, {
						text : '右转',
						iconCls : 'Arrowturnright',
						handler : me.onTurnRight
					}, {
						text : '上一张',
						iconCls : 'Arrowleft',
						handler : me.onLast
					}, {
						text : '下一张',
						iconCls : 'Arrowright',
						handler : me.onNext
					}]
		}];

		me.buttons = [{
					text : '修改',
					iconCls : 'Disk',
					handler : me.onUpdate
				}, {
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	onPrint : function() {
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		if (!!window.ActiveXObject || "ActiveXObject" in window) {
			var print_chanel = function() {
				win.close();
			}
			var print_print = function(me) {
				var iframe = document.getElementById("dnamodel");
				iframe.contentWindow.focus();
				iframe.contentWindow.print();
				win.close();
			}
			var win = Ext.create("Ext.window.Window", {
				title : "打印预览",
				iconCls : 'Find',
				layout : "auto",
				maximized : true,
				maximizable : true,
				modal : true,
				bodyStyle : "background-color:white;",
				html : "<iframe width=100% height=100% id='dnamodel' src='judicial/attachment/printAttachment.do?type="
						+ 1
						+ "&path="
						+ imgs[img_show_index].attachment_path
						+ "'></iframe>",
				buttons : [{
							text : '打印',
							iconCls : 'Disk',
							handler : print_print
						}, {
							text : '取消',
							iconCls : 'Cancel',
							handler : print_chanel
						}]
			});
			win.show();

		} else {
			Ext.Msg.alert("提示", "该浏览器不支持打印,请使用IE!");
		}
	},
	onTurnLeft : function() {
		var me = this.up("form");
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var path = imgs[img_show_index].attachment_path;
		Ext.Ajax.request({
					url : "judicial/attachment/turnImg.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : {
						'path' : path,
						'direction' : 'left'
					},
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.success == true) {
							me.down("panel[name=img]").remove(me.down("box[name=imgbox]"));
							me.down("panel[name=img]").add({
								xtype : 'box',
								style : 'margin:6px;',
								name:"imgbox",
								autoEl : {
									tag : 'img',
									src : "judicial/attachment/getImg.do?filename="
											+ imgs[img_show_index].attachment_path
											+ "&v=" + new Date().getTime()
								}
							});
						} else {
							Ext.Msg.alert("提示", "旋转失败，请重新旋转！");
							return;
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "获取图片失败！");
					}
				});
	},
	onTurnRight : function() {
		var me = this.up("form");
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var path = imgs[img_show_index].attachment_path;
		Ext.Ajax.request({
					url : "judicial/attachment/turnImg.do",
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : {
						'path' : path,
						'direction' : 'right'
					},
					success : function(response, options) {
						response = Ext.JSON.decode(response.responseText);
						if (response.success == true) {
							me.down("panel[name=img]").remove(me.down("box[name=imgbox]"));
							me.down("panel[name=img]").add({
								xtype : 'box',
								style : 'margin:6px;',
								name:"imgbox",
								autoEl : {
									tag : 'img',
									src : "judicial/attachment/getImg.do?filename="
											+ imgs[img_show_index].attachment_path
											+ "&v=" + new Date().getTime()
								}
							});
						} else {
							Ext.Msg.alert("提示", "旋转失败，请重新旋转！");
							return;
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "获取图片失败！");
					}
				});
	},
	onLast : function() {
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var me = this.up("form");
		var box = me.down("box[name=imgbox]");
		if (box == null) {
			Ext.Msg.alert("提示", "没有图片！");
			return;
		}
		if (img_show_index == 0) {
			Ext.Msg.alert("提示", "没有上一张了！");
			return;
		}
		me.down("panel[name=img]").remove(me.down("box[name=imgbox]"));
		img_show_index -= 1;
		me.down("panel[name=img]").add({
			xtype : 'box',
			style : 'margin:6px;',
			name:"imgbox",
			autoEl : {
				tag : 'img',
				src : "judicial/attachment/getImg.do?filename="
						+ imgs[img_show_index].attachment_path
						+ "&v=" + new Date().getTime()
			}
		});
	},
	onNext : function() {
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var me = this.up("form");
		var box = me.down("box[name=imgbox]");
		if (box == null) {
			Ext.Msg.alert("提示", "没有图片！");
			return;
		}
		if (img_show_index + 1 == img_count) {
			Ext.Msg.alert("提示", "没有下一张了！");
			return;
		}
		me.down("panel[name=img]").remove(me.down("box[name=imgbox]"));
		img_show_index += 1;
		me.down("panel[name=img]").add({
			xtype : 'box',
			style : 'margin:6px;',
			name:"imgbox",
			autoEl : {
				tag : 'img',
				src : "judicial/attachment/getImg.do?filename="
						+ imgs[img_show_index].attachment_path
						+ "&v=" + new Date().getTime()
			}
		});
	},
	onUpdate : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		values["case_area"] = Ext.getCmp("judicial_area_name_manage_update").getRawValue();
		var sample_str = values['sample_code'];
		var id_numbers = values['id_number'];
		var temp='';
		if(values['sample_username'] instanceof Array)
		{
			for(var i = 0 ; i < id_numbers.length ; i ++)
			{
				if(id_numbers[i] != '')
				{
					Ext.Ajax.request({
						url : "judicial/register/verifyId_Number.do",
						method : "POST",
						async : false,
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : {
							id_number : id_numbers[i]
						},
						success : function(response,options) {
							response = Ext.JSON.decode(response.responseText);
							if (!response) {
								temp = Number(i)+1;
							}
						},
						failure : function() {
							Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
						}
					});
					if(temp !='')
					{
						Ext.MessageBox.alert("错误信息","第"+temp+"个身份证号码不正确");
						return;
					}
				}
			}
		}else{
			if(id_numbers !=  undefined && id_numbers != '')
			{
				Ext.Ajax.request({
					url : "judicial/register/verifyId_Number.do",
					method : "POST",
					async : false,
					headers : {
						'Content-Type' : 'application/json'
					},
					jsonData : {
						id_number : id_numbers
					},
					success : function(response,options) {
						response = Ext.JSON.decode(response.responseText);
						if (!response) {
							temp = '1';
						}
					},
					failure : function() {
						Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
					}
				});
				if(temp !='')
				{
					Ext.MessageBox.alert("错误信息","身份证号码不正确");
					return;
				}
			}
		}
		if (form.isValid()) {
				var result = true;
				if (sample_str) {
					if(!((typeof sample_str=='string')&&sample_str.constructor==String)){
						for (var i = 0; i < sample_str.length; i++) {
							for (var j = 0; j < sample_str.length; j++) {
								if (i != j && sample_str[i] == sample_str[j]) {
									result = false;
								}
							}
						}
					}
		}
		if (result == false) {
					Ext.MessageBox.alert("错误信息", "存在条形码输入一样的数据");
		} else {
				var id_result = true;
				var sample_call = values['sample_call'];
				if (sample_call) {
					var id_number = values['id_number'];
					  if(!((typeof sample_str=='string')&&sample_str.constructor==String)){
							for(var i=0;i<sample_call.length;i++){
								for(var j=0;j<id_number.length;j++){
									if(i==j){
										if(Ext.util.Format.trim(id_number[j])!=''){
											var num=id_number[j].substring(16, id_number[j].length-1);
											if(sample_call[i]=='mother'||sample_call[i]=='daughter'){
												if(num%2!=0){
													id_result=false;
												}
											}else if(sample_call[i]=='father'||sample_call[i]=='son'){
												if(num%2==0){
													id_result=false;
												}
											}
										}
									}
								}
							}
					   }else{
						   if(Ext.util.Format.trim(id_number)!=''){
								var num=id_number.substring(16, id_number.length-1);
								if(sample_call=='mother'||sample_call=='daughter'){
									if(num%2!=0){
										id_result=false;
									}
								}else if(sample_call=='father'||sample_call=='son'){
									if(num%2==0){
										id_result=false;
									}
								}
							}
					   }
				}
				if (!id_result) {
					Ext.MessageBox.alert("错误信息", "存在身份证信息不匹配的数据");
					return;
				}
				var father_num=0,mother_num=0;
				if(sample_call){
					if(!((typeof sample_str=='string')&&sample_str.constructor==String)){
						for(var i=0;i<sample_call.length;i++){
							if(sample_call[i]=='father'){
								father_num++;
							}
							if(sample_call[i]=='mother'){
								mother_num++;
							}
						}
					}
				}
//				if(father_num>1||mother_num>1){
//					Ext.MessageBox.alert("错误信息", "家长称谓出现重复");
//					return;
//				}
				if(!values["attach_need"]){
					values["attach_need"]=1;
				}
				values["sign"]="manage";
				Ext.Ajax.request({
							url : "judicial/register/updateCaseInfo.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response == true) {
									Ext.MessageBox.alert("提示信息", "修改案例成功");
									me.grid.getStore().load();
									me.up("window").close();
								} else {
									Ext.MessageBox.alert("错误信息", "修改案例失败");
								}
							},
							failure : function() {
								Ext.Msg.alert("提示", "修改失败<br>请联系管理员!");
							}

						});
			}
		}
	},
	onCancel : function() {
		var me = this;
		me.up("window").close();
	},
	listeners : {
		'afterrender' : function() {
			var me = this;
			var values = me.getValues();
			var case_id = values["case_id"];
			var area_code = values["case_areacode"];
			var case_type = values["case_type"];
			var attach_need_case=values["attach_need_case"];
			caseTypeModel.load();
			var caseType = me.down("combo[name=case_type_update]");
			caseType.setValue(case_type);
			var area = me.down("combo[name=area]");
			area.setValue(area_code);
			var attach_need=me.down("checkbox[name=attach_need]");
			if(attach_need_case==0){
				attach_need.setValue(true);
			}
			var receiver = me.down("combo[name=receiver_id_update]");
			receiver.setValue(values["receiver_id"]);
			Ext.Ajax.request({
				url : 'judicial/register/getSampleInfo.do',
				method : "POST",
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : {
					'case_id' : case_id
				},
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					var items = response["items"];
					for (var i = 0; i < items.length; i++) {
						me.down("form").add({
							xtype : 'form',
							style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
							labelAlign : "right",
							bodyPadding : 10,
							defaults : {
								anchor : '100%',
								labelWidth : 80,
								labelSeparator : "：",
								labelAlign : 'right'
							},
							border : false,
							autoHeight : true,
							items : [{
								layout : 'auto',
								xtype : 'panel',
								border : false,
								items : [{
									layout : 'column',
									xtype : 'panel',
									border : false,
									items : [{
												columnWidth : .28,
												xtype : "textfield",
												fieldLabel : "条形码<span style='color:red'>*</span>",
												emptyText : '自动生成',
//												allowBlank : false,// 不允许为空
//												blankText : "不能为空",// 错误提示信息，默认为This
												regex:/^\w+$/,
												regexText:'请输入正确条形码',
												labelAlign : 'right',
												readOnly : true,
												value : items[i].sample_code,
												maxLength : 50,
												labelWidth : 80,
												name : 'sample_code'

											}, {
												columnWidth : .28,
												xtype : "textfield",
												fieldLabel : "用户名<span style='color:red'>*</span>",
												labelAlign : 'right',
												maxLength : 50,
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												// field is
												// required!
												labelWidth : 80,
												value : items[i].sample_username,
												name : 'sample_username'
											}, {
												columnWidth : .28,
												xtype : "textfield",
												fieldLabel : '身份证',
												labelAlign : 'right',
												maxLength : 18,
												value : items[i].id_number,
												labelWidth : 80,
												validator : function(value) {
													var result = false;
													if (value != null
															&& value != "") {
														Ext.Ajax.request({
															url : "judicial/register/verifyId_Number.do",
															method : "POST",
															async : false,
															headers : {
																'Content-Type' : 'application/json'
															},
															jsonData : {
																id_number : value
															},
															success : function(
																	response,
																	options) {
																response = Ext.JSON
																		.decode(response.responseText);
																if (!response) {
																	result = "身份证号码不正确";
																} else {
																	result = true;
																}
															},
															failure : function() {
																Ext.Msg
																		.alert(
																				"提示",
																				"网络故障<br>请联系管理员!");
															}
														});
													} else {
														result = true;
													}
													return result;
												},
												name : 'id_number'
											}]
								}, {
									layout : 'column',
									xtype : 'panel',
									border : false,
									style : 'margin-top:5px;',
									items : [{
												columnWidth : .28,
												xtype : "combo",
												fieldLabel : "取样类型<span style='color:red'>*</span>",
												mode : 'local',
												labelWidth : 60,
												labelAlign : 'right',
												blankText : '请选择',
												emptyText : '请选择',
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												// field is
												// required!
												valueField : "key",
												displayField : "value",
												value : items[i].sample_type,
												maxLength : 50,
												labelWidth : 80,
												editable:false,
												store : storeSampleType,
												name : 'sample_type'
											}, {
												columnWidth : .28,
												xtype : "combo",
												mode : 'local',
												labelWidth : 60,
												labelAlign : 'right',
												blankText : '请选择',
												emptyText : '请选择',
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												// field is
												// required!
												editable:false,
												readOnly : true,
												valueField : "key",
												displayField : "value",
												value : items[i].sample_call,
												store : storeSampleCall,
												fieldLabel : "称谓<span style='color:red'>*</span>",
												maxLength : 50,
												labelWidth : 80,
												name : 'sample_call'
											}, {
												columnWidth : .28,
												xtype : "textfield",
												fieldLabel : '出生日期',
												emptyText:'yyyy-MM-dd',  
												labelAlign : 'right',
												maxLength : 20,
												value : items[i].birth_date,
												labelWidth : 80,
												name : 'birth_date',
												regex : /^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/,
												regexText : "格式:yyyy-MM-dd！"
											}]
								}]
							}]
						});
					}
				},
				failure : function() {
					return;
				}
			});

			Ext.Ajax.request({
						url : "judicial/attachment/getAttachMent.do",
						method : "POST",
						headers : {
							'Content-Type' : 'application/json'
						},
						jsonData : {
							'case_id' : case_id
						},
						success : function(response, options) {
							var data = Ext.JSON.decode(response.responseText);
							if(data.length>0){
								imgs = data;
								img_count = data.length;
								me.down("panel[name=img]").add({
									xtype : 'box',
									style : 'margin:6px;',
									name:'imgbox',
									autoEl : {
										tag : 'img',
										src : "judicial/attachment/getImg.do?filename="
												+ imgs[img_show_index].attachment_path
												+ "&v=" + new Date().getTime()
									}
								});
							}
						},
						failure : function() {
							return;
						}
					});

		}
	}
});
