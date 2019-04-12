/**
 * @description 案例审核form
 * @author fushaoming
 */
var sample_code_length;
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
var mailStore = Ext.create('Ext.data.Store', {
	fields:['key','value'],
	proxy : {
		type : 'jsonajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'judicial/dicvalues/getMailModels.do',
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


queryMailInfo = function(value, mail_type) {
	var win = Ext.create("Ext.window.Window", {
				title : "快递信息（快递单号：" + value + "）",
				width : 700,
				iconCls : 'Find',
				height : 350,
				layout : 'border',
				modal:true,
				border : false,
				bodyStyle : "background-color:white;",
				items : [Ext.create('Ext.grid.Panel', {
									width : 700,
									height : 350,
									frame : false,
									renderTo : Ext.getBody(),
									viewConfig : {
										forceFit : true,
										stripeRows : true
										,// 在表格中显示斑马线
									},
									store : {// 配置数据源
										fields : ['time', 'content'],// 定义字段
										proxy : {
											type : 'jsonajax',
											actionMethods : {
												read : 'POST'
											},
											url : 'judicial/mail/getMailInfo.do',
											params : {
												'mail_code' : value,
												'mail_type' : mail_type
											},
											reader : {
												type : 'json',
												root : 'items',
												totalProperty : 'count'
											}
										},
										autoLoad : true
										// 自动加载
									},
									columns : [// 配置表格列
									{
												header : "时间",
												dataIndex : 'time',
												flex : 1.5,
												menuDisabled : true
											}, {
												header : "地点",
												dataIndex : 'content',
												flex : 3,
												menuDisabled : true
											}]
								})]
			});
	win.show();
}

var imgs="";
var img_show_index = 0;
var img_count = 0;
Ext.define('Rds.judicial.form.JudicialVerifyFormCheck', {
	extend : 'Ext.form.Panel',
	layout : "border",
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'panel',
			region : 'west',
			width : 750,
			autoScroll : true,
			items : [{
				xtype : 'form',
				width : 700,
				style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
				defaults : {
					anchor : '100%',
					labelWidth : 80,
					labelSeparator : "：",
					labelAlign : 'right'
				},
				border : false,
				autoHeight : true,
				items : [{
					border : false,
					xtype : 'fieldcontainer',
					layout : "form",
					items : [{
								xtype : 'hiddenfield',
								name : 'case_id'
							}, {
								xtype : 'hiddenfield',
								name : 'case_areacode'
							},
							{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [
										{
											xtype : 'datefield',
											name : 'accept_time',
											fieldLabel : '鉴定日期',
											labelWidth : 80,
											columnWidth : .33,
											readOnly : true,
											hideTrigger : true,
											labelAlign : 'right',
											format : 'Y-m-d'
										},{
											xtype : "hiddenfield",
											name : 'case_state'
										}]
							},
							{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
									xtype : "textfield",
									labelAlign : 'right',
									fieldLabel : '案例条形码',
									readOnly : true,
									labelWidth : 80,
									columnWidth : .33,
									maxLength : 50,
									name : 'case_code'
								},new Ext.form.field.ComboBox(
										{
											fieldLabel : '案例来源',
											columnWidth : .33,
											labelWidth : 80,
											editable : false,
											triggerAction : 'all',
											displayField : 'Name',
											labelAlign : 'right',
											value : 0,
											valueField : 'Code',
											store : new Ext.data.ArrayStore(
													{
														fields : [
																'Name',
																'Code' ],
														data : [
																[
																		'市场',
																		'0' ],
																[
																		'网络',
																		'1' ] ]
													}),
											mode : 'local',
											name : 'source_type',
											readOnly : true,
										}), new Ext.form.field.ComboBox({
											fieldLabel : '紧急程度',
											labelWidth : 80,
											editable : false,
											columnWidth : .33,
											readOnly : true,
											hideTrigger : true,
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
											name : 'urgent_state'
										})]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
									xtype : "textfield",
									fieldLabel : '归属人',
									columnWidth : .33,
									labelAlign : 'right',
									labelWidth : 80,
									name : 'case_receiver',
									readOnly : true
								}, {
									xtype : "hiddenfield",
									name : 'case_userid'
								}, {
									xtype : "textfield",
									fieldLabel : '案例归属地',
									labelAlign : 'right',
									columnWidth : .66,
									labelWidth : 80,
									readOnly : true,
									name : 'receiver_area'
								}]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
									// 该列在整行中所占的百分比
									columnWidth : .33,
									xtype : "textfield",
									labelAlign: 'right',
									fieldLabel : '采样人员',
									labelWidth : 80,
								    maxLength :50,
								    readOnly : true,
									name : 'sample_in_per',
								},
								{
									columnWidth : .33,
									xtype : "combo",
									fieldLabel : "单位类型",
									mode: 'local',   
									labelWidth : 80,
									editable:false,
									labelAlign: 'right',
									blankText:'请选择单位',   
							        emptyText:'请选择单位',  
									valueField :"key",   
							        displayField: "value",    
									name : 'unit_type',
									readOnly : true,
									store: unitStore
								},{
									xtype : "textfield",
									labelAlign : 'right',
									readOnly : true,
									columnWidth : .33,
									hideTrigger : true,
									fieldLabel : '电话号码',
									labelWidth : 80,
									maxLength : 50,
									name : 'phone'
								}]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [ {
									xtype : "textfield",
									labelAlign : 'right',
									readOnly : true,
									columnWidth : .33,
									hideTrigger : true,
									fieldLabel : '模板类型',
									labelWidth : 80,
									name : 'report_modelname'
								},{
									xtype : "hiddenfield",
									name : 'report_model'
								},{
									xtype : "textfield",
									labelAlign : 'right',
									readOnly : true,
									hideTrigger : true,
									fieldLabel : '委托人',
									labelWidth : 80,
									columnWidth : .33,
									name : 'client'
								},{
									xtype : 'datefield',
									name : 'consignment_time',
									fieldLabel : '委托时间',
									labelWidth : 80,
									columnWidth : .33,
									readOnly : true,
									hideTrigger : true,
									labelAlign : 'right',
									format : 'Y-m-d'
								}]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [  Ext.create('Ext.form.field.Number',{
								    fieldLabel: "打印份数",
								    name: 'copies',
								    labelWidth : 80,
								    columnWidth : .33,
								    labelAlign: 'right',
								    readOnly : true
								}), new Ext.form.field.ComboBox({
				                    fieldLabel: "亲属关系",
				                    columnWidth : .33,
				                    labelWidth : 80,
				                    readOnly : true,
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
				                }),Ext.create('Ext.form.ComboBox', {
									fieldLabel: "单双亲<span style='color:red'>*</span>",
				                    labelWidth : 80,
				                    editable:false,
				                    triggerAction: 'all',
				                    displayField: 'Name',
				                    labelAlign: 'right',
				                    columnWidth : .33,
				                    valueField: 'Code',
				                    store: new Ext.data.ArrayStore({
				                        fields: ['Name', 'Code'],
				                        data: [['单亲', '1'], ['双亲', '2']]
				                    })          ,
				                    mode: 'local',
				                    name: 'typeid',
				                    readOnly : true
								})]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [  Ext.create('Ext.form.field.Number',{
								    fieldLabel: "样本数",
								    name: 'per_num',
								    id:'per_num',
								    labelWidth : 80,
								    value: 0,
								    maxValue: 9999,
								    minValue: 0,
								    columnWidth : .33,
								    labelAlign: 'right',
								    readOnly:true
								}), Ext.create('Ext.form.ComboBox', {
									fieldLabel: "合作方",
				                    labelWidth : 80,
				                    editable:true,
				                    triggerAction: 'all',
				                    displayField: 'parnter_name',
				                    labelAlign: 'right',
				                    columnWidth : .33,
				                    valueField: 'parnter_name',
				                    mode: 'local',
				                    name: 'parnter_name',
				                    readOnly:true
								}),{
									// 该列在整行中所占的百分比
									columnWidth : .33,
									xtype : "textfield",
									labelAlign : 'right',
									labelWidth : 80,
									fieldLabel : '优惠码',
									maxLength : 50,
									name : 'confirm_code',
									readOnly:true
								}]
							},{
								border : false,
								xtype : 'fieldcontainer',
								layout : "column",
								items : [{
									columnWidth : .33,
									xtype : "textfield",
									labelAlign: 'right',
									fieldLabel : '鉴定目的',
									labelWidth : 80,
								    maxLength :50,
								    readOnly : true,
									name : 'purpose'
								}]
							}
							]
				},{
					xtype : 'textarea',
					fieldLabel : '备注',
					name : 'remark',
					readOnly:true
				}]
			}]
		}, {
			xtype : 'panel',
			region : 'center',
			id : 'judicial_img_show_verify',
			autoScroll : true,
			items : [],
			buttons : [{
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
					text : '取消',
					iconCls : 'Arrowredo',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	onTurnLeft : function() {
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var me = this.up("box");
		var box = Ext.getCmp('judicial_img_box_verify');
		if (box == null) {
			Ext.Msg.alert("提示", "没有图片！");
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
							Ext.getCmp('judicial_img_show_verify').remove(Ext
									.getCmp('judicial_img_box_verify'));
							//获取图片高度和宽度
							Ext.Ajax.request({
								url : "judicial/attachment/getImgWidth.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {'filename' : imgs[img_show_index].attachment_path},
								success : function(response,options) {
									response = Ext.JSON.decode(response.responseText);
									if(response.height>0){
										var height = response.height;
										var width =response.width;
										console.log(height+"----"+width);
										if(height>800 && width >1000)
										{
											if(height/800 > width/800)
											{
												width = width / (height/800);
												height = 800;
											}else
											{
												height = height /(width/1000);
												width = 1000;

												console.log(height+"----"+width);
											}
										}else if(height<800 && width >1000)
										{
											height = height /(width/1000);
											width = 1000;
										}else if(height>800 && width <1000)
										{
											width = width / (height/800);
											height = 800;
										}
										
										Ext.getCmp('judicial_img_show_verify').add({
											xtype : 'box',
//											style : 'padding:6px;width:100%;',
											id : 'judicial_img_box_verify',
											autoEl : {
												width:Math.ceil(width),
												height:Math.ceil(height),
												tag : 'img',
												src : "judicial/attachment/getImg.do?filename="
														+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
											}
										});
									}else
										{
										Ext.MessageBox.alert("提示信息","案例照片有问题，请联系管理员！");
										return;
										}
//									if (response == true) {
//										Ext.MessageBox.alert("提示信息","废除成功！");
//										me.getStore().load();
//									} 
								},
								failure : function() {
									Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
								}
							});
//							Ext.getCmp('judicial_img_show_verify').add({
//								xtype : 'box',
//								style : 'margin:6px;',
//								id : 'judicial_img_box_verify',
//								autoEl : {
//									tag : 'img',
//									src : "judicial/attachment/getImg.do?filename="
//											+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
//								}
//							});
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
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var me = this.up("box");
		var box = Ext.getCmp('judicial_img_box_verify');
		if (box == null) {
			Ext.Msg.alert("提示", "没有图片！");
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
							Ext.getCmp('judicial_img_show_verify').remove(Ext
									.getCmp('judicial_img_box_verify'));
							//获取图片高度和宽度
							Ext.Ajax.request({
								url : "judicial/attachment/getImgWidth.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {'filename' : imgs[img_show_index].attachment_path},
								success : function(response,options) {
									response = Ext.JSON.decode(response.responseText);
									if(response.height>0){
										var height = response.height;
										var width =response.width;
										console.log(height+"----"+width);
										if(height>800 && width >1000)
										{
											if(height/800 > width/800)
											{
												width = width / (height/800);
												height = 800;
											}else
											{
												height = height /(width/1000);
												width = 1000;

												console.log(height+"----"+width);
											}
										}else if(height<800 && width >1000)
										{
											height = height /(width/1000);
											width = 1000;
										}else if(height>800 && width <1000)
										{
											width = width / (height/800);
											height = 800;
										}
										
										Ext.getCmp('judicial_img_show_verify').add({
											xtype : 'box',
//											style : 'padding:6px;width:100%;',
											id : 'judicial_img_box_verify',
											autoEl : {
												width:Math.ceil(width),
												height:Math.ceil(height),
												tag : 'img',
												src : "judicial/attachment/getImg.do?filename="
														+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
											}
										});
									}else
										{
										Ext.MessageBox.alert("提示信息","案例照片有问题，请联系管理员！");
										return;
										}
//									if (response == true) {
//										Ext.MessageBox.alert("提示信息","废除成功！");
//										me.getStore().load();
//									} 
								},
								failure : function() {
									Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
								}
							});
							
//							Ext.getCmp('judicial_img_show_verify').add({
//								xtype : 'box',
//								style : 'margin:6px;',
//								id : 'judicial_img_box_verify',
//								autoEl : {
//									tag : 'img',
//									src : "judicial/attachment/getImg.do?filename="
//											+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
//								}
//							});
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
		var me = this.up("box");
		var box = Ext.getCmp('judicial_img_box_verify');
		if (box == null) {
			Ext.Msg.alert("提示", "没有图片！");
			return;
		}
		if (img_show_index == 0) {
			Ext.Msg.alert("提示", "没有上一张了！");
			return;
		}
		Ext.getCmp('judicial_img_show_verify').remove(Ext
				.getCmp('judicial_img_box_verify'));

		img_show_index -= 1;
		
		//获取图片高度和宽度
		Ext.Ajax.request({
			url : "judicial/attachment/getImgWidth.do",
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : {'filename' : imgs[img_show_index].attachment_path},
			success : function(response,options) {
				response = Ext.JSON.decode(response.responseText);
				if(response.height>0){
					var height = response.height;
					var width =response.width;
					console.log(height+"----"+width);
					if(height>800 && width >1000)
					{
						if(height/800 > width/800)
						{
							width = width / (height/800);
							height = 800;
						}else
						{
							height = height /(width/1000);
							width = 1000;

							console.log(height+"----"+width);
						}
					}else if(height<800 && width >1000)
					{
						height = height /(width/1000);
						width = 1000;
					}else if(height>800 && width <1000)
					{
						width = width / (height/800);
						height = 800;
					}
					
					Ext.getCmp('judicial_img_show_verify').add({
						xtype : 'box',
//						style : 'padding:6px;width:100%;',
						id : 'judicial_img_box_verify',
						autoEl : {
							width:Math.ceil(width),
							height:Math.ceil(height),
							tag : 'img',
							src : "judicial/attachment/getImg.do?filename="
									+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
						}
					});
				}else
					{
					Ext.MessageBox.alert("提示信息","案例照片有问题，请联系管理员！");
					return;
					}
//				if (response == true) {
//					Ext.MessageBox.alert("提示信息","废除成功！");
//					me.getStore().load();
//				} 
			},
			failure : function() {
				Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
			}
		});
//		Ext.getCmp('judicial_img_show_verify').add({
//			xtype : 'box',
////			style : 'padding:6px;width:100%;',
//			id : 'judicial_img_box_verify',
//			autoEl : {
//				tag : 'img',
//				src : "judicial/attachment/getImg.do?filename="
//						+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
//			}
//		});
	},
	onNext : function() {
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var me = this.up("box");
		var box = Ext.getCmp('judicial_img_box_verify');
		if (box == null) {
			Ext.Msg.alert("提示", "没有图片！");
			return;
		}
		if (img_show_index + 1 == img_count) {
			Ext.Msg.alert("提示", "没有下一张了！");
			return;
		}
		Ext.getCmp('judicial_img_show_verify').remove(Ext
				.getCmp('judicial_img_box_verify'));
		img_show_index += 1;
		
		//获取图片高度和宽度
		Ext.Ajax.request({
			url : "judicial/attachment/getImgWidth.do",
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : {'filename' : imgs[img_show_index].attachment_path},
			success : function(response,options) {
				response = Ext.JSON.decode(response.responseText);
				if(response.height>0){
					var height = response.height;
					var width =response.width;
					console.log(height+"----"+width);
					if(height>800 && width >1000)
					{
						if(height/800 > width/800)
						{
							width = width / (height/800);
							height = 800;
						}else
						{
							height = height /(width/1000);
							width = 1000;

							console.log(height+"----"+width);
						}
					}else if(height<800 && width >1000)
					{
						height = height /(width/1000);
						width = 1000;
					}else if(height>800 && width <1000)
					{
						width = width / (height/800);
						height = 800;
					}
					
					Ext.getCmp('judicial_img_show_verify').add({
						xtype : 'box',
//						style : 'padding:6px;width:100%;',
						id : 'judicial_img_box_verify',
						autoEl : {
							width:Math.ceil(width),
							height:Math.ceil(height),
							tag : 'img',
							src : "judicial/attachment/getImg.do?filename="
									+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
						}
					});
				}else
					{
					Ext.MessageBox.alert("提示信息","案例照片有问题，请联系管理员！");
					return;
					}
//				if (response == true) {
//					Ext.MessageBox.alert("提示信息","废除成功！");
//					me.getStore().load();
//				} 
			},
			failure : function() {
				Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
			}
		});
		
		
//		Ext.getCmp('judicial_img_show_verify').add({
//			xtype : 'box',
////			style : 'padding:6px;width:100%;',
//			id : 'judicial_img_box_verify',
//			autoEl : {
//				tag : 'img',
//				src : "judicial/attachment/getImg.do?filename="
//						+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
//			}
//		});
		// alert("next");
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
					Ext.getCmp("per_num").setValue(items.length);
					//用作校验条形码
		            sample_code_length = items.length;
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
												columnWidth : .30,
												xtype : "textfield",
												fieldLabel : '条形码',
												readOnly : true,
												hideTrigger : true,
												labelAlign : 'right',
												value : items[i].sample_code,
												maxLength : 50,
												labelWidth : 50,
												name : 'sample_code'
											}, {
												columnWidth : .18,
												xtype : "textfield",
												fieldLabel : '用户名',
												labelAlign : 'right',
												maxLength : 50,
												readOnly : true,
												hideTrigger : true,
												labelWidth : 50,
												value : items[i].sample_username,
												name : 'sample_username'
											}, {
												columnWidth : .32,
												xtype : "textfield",
												fieldLabel : '身份证',
												labelAlign : 'right',
												maxLength : 18,
												value : items[i].id_number,
												labelWidth : 50,
												readOnly : true,
												hideTrigger : true,
												name : 'id_number'
											},{
												layout : 'absolute',// 从左往右的布局
												xtype : 'panel',
												border : false,
												columnWidth : .20,
												items : [Ext.create('Ext.form.ComboBox', {
													fieldLabel: "特殊样本",
								                    labelWidth : 60,
								                    editable:false,
								                    width:130,
								                    triggerAction: 'all',
								                    displayField: 'Name',
								                    labelAlign: 'right',
								                    valueField: 'Code',
								                    store: new Ext.data.ArrayStore({
								                        fields: ['Name', 'Code'],
								                        data: [['否', '0'], ['是', '1']]
								                    }),
								                    value:items[i].special,
								                    mode: 'local',
								                    name: 'special',
								                    readOnly : true,
												}) ]
											}]
								}, {
									layout : 'column',
									xtype : 'panel',
									border : false,
									items : [{
												columnWidth : .30,
												xtype : "combo",
												fieldLabel : '取样类型',
												mode : 'local',
												labelWidth : 60,
												labelAlign : 'right',
												blankText : '请选择',
												emptyText : '请选择',
												readOnly : true,
												hideTrigger : true,
												valueField : "key",
												displayField : "value",
												value : items[i].sample_type,
												maxLength : 50,
												store : storeSampleType,
												name : 'sample_type',
												style:'margin-top:5px'
											}, {
												columnWidth : .18,
												xtype : "combo",
												mode : 'local',
												labelWidth : 50,
												labelAlign : 'right',
												readOnly : true,
												hideTrigger : true,
												valueField : "key",
												displayField : "value",
												value : items[i].sample_call,
												store : storeSampleCall,
												fieldLabel : '称谓',
												maxLength : 50,
												name : 'sample_call',
												style:'margin-top:5px'
											}, {
												columnWidth : .32,
												xtype : "textfield",
												fieldLabel : '出生日期',
												labelAlign : 'right',
												maxLength : 20,
												value : items[i].birth_date,
												labelWidth : 60,
												readOnly : true,
												hideTrigger : true,
												name : 'birth_date',
												style:'margin-top:5px'
											}]
								}]
							}]
						});
					}
				}
			});
			//添加采样快递信息
			Ext.Ajax.request({
				url : 'judicial/register/querySampleExpress.do',
				method : "POST",
				async: false,
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : {
					'case_id' : case_id
				},
				success : function(response, options) {
					response = Ext.JSON.decode(response.responseText);
					var items = response;
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
										columnWidth : .30,
										xtype : "textfield",
										fieldLabel : "快递编号",
										labelAlign : 'right',
										maxLength : 50,
										readOnly : true,
										labelWidth : 80,
										value : items[i].express_num,
										name : 'express_num',
									},new Ext.form.field.ComboBox({
											fieldLabel : '快递类型',
											labelWidth : 80,
											columnWidth : .30,
											triggerAction : 'all',
											labelAlign : 'right',
											// required!
											valueField : "key",
											displayField : "value",
											value : items[i].express_type,
											store : mailStore,
											readOnly : true,
											mode : 'local',
											name : 'express_type'
										}),{
										columnWidth : .30,
										xtype : "textfield",
										fieldLabel : "快递时间",
										labelAlign : 'right',
										maxLength : 50,
										labelWidth : 80,
										readOnly : true,
										value : items[i].express_time,
										name : 'express_time'
									}
									]
								}, {
									layout : 'column',
									xtype : 'panel',
									border : false,
									style : 'margin-top:5px;',
									items : [{
										columnWidth : .30,
										xtype : "textfield",
										fieldLabel : '收件人',
										labelAlign : 'right',
										value : items[i].express_recive,
										maxLength : 18,
										readOnly : true,
										labelWidth : 80,
										name : 'express_recive'
									} ,{
										columnWidth : .30,
										xtype : "textfield",
										fieldLabel : '联系人',
										labelAlign : 'right',
										value : items[i].express_concat,
										maxLength : 18,
										readOnly : true,
										labelWidth : 80,
										name : 'express_concat'
									},
									{
										xtype : 'textfield',
										fieldLabel : '快递地址',
										name : 'express_remark',
										maxLength : 500,
										columnWidth : 0.30,
										labelWidth : 80,
										readOnly : true,
										value : items[i].express_remark,
										labelAlign : 'right'
									},{
										xtype : 'tbtext',
										columnWidth : 0.1,
										text : "<a href='#' style='margin-left:5px;' onclick=\"queryMailInfo('"
											+  items[i].express_num
											+ "','"
											+ items[i].express_type
											+ "')\"  >查询"
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
			//添加案例图片信息
			Ext.Ajax.request({
						url : "judicial/attachment/getAttachMent.do",
						method : "POST",
						async: false,
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
								//获取图片高度和宽度
								Ext.Ajax.request({
									url : "judicial/attachment/getImgWidth.do",
									method : "POST",
									headers : {
										'Content-Type' : 'application/json'
									},
									jsonData : {'filename' : imgs[img_show_index].attachment_path},
									success : function(response,options) {
										response = Ext.JSON.decode(response.responseText);
										if(response.height>0){
											var height = response.height;
											var width =response.width;
											console.log(height+"----"+width);
											if(height>800 && width >1000)
											{
												if(height/800 > width/1000)
												{
													width = width / (height/800);
													height = 800;
												}else
												{
													height = height /(width/1000);
													width = 1000;

													console.log(height+"----"+width);
												}
											}else if(height<800 && width >1000)
											{
												height = height /(width/1000);
												width = 1000;
											}else if(height>800 && width <1000)
											{
												width = width / (height/800);
												height = 800;
											}
											
											Ext.getCmp('judicial_img_show_verify').add({
												xtype : 'box',
//												style : 'padding:6px;width:100%;',
												id : 'judicial_img_box_verify',
												autoEl : {
													width:Math.ceil(width),
													height:Math.ceil(height),
													tag : 'img',
													src : "judicial/attachment/getImg.do?filename="
															+ imgs[img_show_index].attachment_path+"&v="+new Date().getTime()
												}
											});
										}else
											{
											Ext.MessageBox.alert("提示信息","案例照片有问题，请联系管理员！");
											return;
											}
//										if (response == true) {
//											Ext.MessageBox.alert("提示信息","废除成功！");
//											me.getStore().load();
//										} 
									},
									failure : function() {
										Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
									}
								});
								
							}
						},
						failure : function() {
							Ext.Msg.alert("提示", "获取图片失败<br>请联系管理员!");
						}
					});

		}
	}
});
