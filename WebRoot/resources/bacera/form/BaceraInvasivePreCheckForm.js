var imgs="";
var img_show_index = 0;
var img_count = 0;
Ext.define('Rds.bacera.form.BaceraInvasivePreCheckForm', {
	extend : 'Ext.form.Panel',
	layout : "border",
	config:{
		grid:null
	},
	initComponent : function() {
		var me = this;
		var date = new Ext.form.DateField({
					name : 'date',
					fieldLabel : '日期<span style="color:red;font-weight:bold" data-qtip="Required"></span>',
					labelWidth : 65,
					labelAlign : 'left',
					columnWidth : .50,
					afterLabelTextTpl : me.required,
					emptyText : '请选择日期',
					format : 'Y-m-d',
					readOnly : true,
					allowBlank:false, //不允许为空
					blankText:"不能为空", //错误提示信息，默认为This field is required! 
					value:new Date()
				});
		var sampledate = new Ext.form.DateField({
			name : 'sampledate',
			fieldLabel : '采样日期',
			labelWidth : 60,
			columnWidth : .50,
			style:'margin-left:10px;',
			afterLabelTextTpl : me.required,
			emptyText : '请选择日期',
			format : 'Y-m-d',
			readOnly : true,
			value : Ext.Date.add(new Date(),
					 Ext.Date.DAY),
			allowBlank:false, //不允许为空
			blankText:"不能为空", //错误提示信息，默认为This field is required! 
		});
		me.items = [{
		    xtype : 'panel',
		    region : 'west',
		    width : 750,
		    autoScroll : true,
       items:[{
	        xtype:'form',
        	width : 750,
        	name:'data',
        	style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
            labelAlign:"right",
            bodyPadding: 10,
            defaults: {
                anchor: '100%',
                labelWidth: 80,
                labelSeparator : "：",
				labelAlign : 'right'
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
    			hidden:true,
    			id:'vId',
 
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [	
				    		{
				    			xtype:"textfield",
				    			fieldLabel: '案例编号<span style="color:red;font-weight:bold" data-qtip="Required"></span>',
				    			labelWidth:65,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'num',
								readOnly : true,
								columnWidth : .50,
				    			allowBlank:false, //不允许为空
				    			//blankText:"不能为空", //错误提示信息，默认为This field is required! 
				    			maxLength: 20,
				    			validator : function(value) {
				    				if(Ext.getCmp('vId').getValue())
				    				{
				    					return true;
			    					}
									var result = false;
									Ext.Ajax.request({
										url : "bacera/invasivePre/exsitCaseCode.do",
										method : "POST",
										async : false,
										headers : {
											'Content-Type' : 'application/json'
										},
										jsonData : {
											num : value
										},
										success : function(
												response,
												options) {
											response = Ext.JSON
													.decode(response.responseText);
											if (!response) {
												result = "此案例编号已存在";
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
									return result;
								},
				    		}, {
					        	 xtype:"textfield",
					    			fieldLabel: '优惠码',
					    			labelWidth:60,
					    			style:'margin-left:10px;',
					    			fieldStyle:me.fieldStyle, 
					    			columnWidth : .50,
					    			name: 'confirm_code',
					    			readOnly:true,
					    			maxLength: 50
					    			}]},{
				    			border : false,
				    			style:'margin-top:10px;',
								layout : "column",// 从左往右的布局
								xtype : 'fieldcontainer',
								border : false,
								items : [ {
									xtype : "textfield",
									fieldLabel : '归属人',
									columnWidth : .50,
									labelWidth : 65,
									name : 'ownpersonname',
									readOnly : true
//									xtype : 'combo',
//									fieldLabel : '归属人<span style="color:red">*</span>',
//									labelWidth : 65,
//									columnWidth : .50,
//									name : 'ownperson',
//									labelAlign : 'left',
//									emptyText:'(人员首字母)：如小明(xm)',
//									store :Ext.create("Ext.data.Store",{
//										   fields:[
//												{name:'key',mapping:'key',type:'string'},
//												{name:'value',mapping:'value',type:'string'}
//								          ],
//										pageSize : 20,
//										autoLoad: false,
//										proxy : {
//											type : "ajax",
//											url:'judicial/dicvalues/getUsersId.do?userid='+ownpersonTemp,
//											reader : {
//												type : "json"
//											}
//										}
//									}),
//									displayField : 'value',
//									valueField : 'key',
//									typeAhead : false,
//									allowBlank:false, //不允许为空
//									hideTrigger : true,
//									forceSelection: true,
//									minChars : 2,
//									matchFieldWidth : true,
//									listConfig : {
//										loadingText : '正在查找...',
//										emptyText : '没有找到匹配的数据'
//									},	
//									listeners: {
//										'afterrender':function(){
//											if("" != ownpersonTemp)
//											{
//												this.store.load();
//											}
//										}
//										}
								},{
									xtype : "textfield",
									fieldLabel : '归属地区',
									style:'margin-left:10px;',
									columnWidth : .50,
									labelWidth : 60,
									readOnly : true,
									name : 'areaname'
//									xtype : "combo",
//			        				fieldLabel : '归属地区<span style="color:red">*</span>',
//			        				labelWidth : 65,
//			        				columnWidth : .50,
//			        				id:'areacode5',
//			        				style:'margin-left:10px',
//			        				name : 'areacode',
//			        				emptyText:'检索方式：如朝阳区(cy)',
//			        				store : Ext.create("Ext.data.Store",{
//			        					 fields:[
//			        			                  {name:'key',mapping:'key',type:'string'},
//			        			                  {name:'value',mapping:'value',type:'string'},
//			        			                  {name:'name',mapping:'name',type:'string'},
//			        			                  {name:'id',mapping:'id',type:'string'},
//			        			          ],
//			        					pageSize : 10,
//			        					proxy : {
//			        						type : "ajax",
//			        						url:"judicial/dicvalues/getAreaInfo.do?areacode="+areacodeTemp,
//			        						reader : {
//			        							type : "json"
//			        						}
//			        					}
//			        				}),
//			        				displayField : 'value',
//			        				valueField : 'id',
//			        				typeAhead : false,
//			        				hideTrigger : true,
//			        				allowBlank:false, //不允许为空
//			        				forceSelection: true,
//			        				minChars : 2,
//			        				matchFieldWidth : true,
//			        				listConfig : {
//			        					loadingText : '正在查找...',
//			        					emptyText : '没有找到匹配的数据'
//			        				},listeners: {
//										'afterrender':function(){
//											if("" != areacodeTemp)
//											{
//												this.store.load();
//											}
//										}
//										}
									}
								]
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [date,sampledate]
    			
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [{
			    			xtype:"textfield",
			    			fieldLabel: '姓名<span style="color:red;font-weight:bold" data-qtip="Required"></span>',
			    			labelWidth:65,
			    			fieldStyle:me.fieldStyle, 
			    			name: 'name',
			    			maxLength: 10,
			    			columnWidth : .50,
			    			allowBlank:false, //不允许为空
			    			readOnly : true,
			    			blankText:"不能为空", //错误提示信息，默认为This field is required! 
	    		        },{
			    			xtype:"textfield",
			    			fieldLabel: '所属医院',
			    			labelWidth:60,
			    			columnWidth : 0.5,
			    			style:'margin-left:10px;',
			    			fieldStyle:me.fieldStyle, 
			    			name: 'hospital',
			    			readOnly : true,
			    			maxLength: 50,
	    		        }]
    			
    		},{
    			border : false,
				xtype : 'fieldcontainer',
				layout : "column",
				style:'margin-top:10px;',
				items : [
				         {
				        	 xtype:"textfield",
			    			fieldLabel: '条码',
			    			labelWidth:65,
			    			columnWidth : .50,
			    			fieldStyle:me.fieldStyle, 
			    			name: 'code',
			    			readOnly : true,
			    			maxLength: 50
			    		
			    			},
			    			{
					        	 xtype:"textfield",
				    			fieldLabel: 'task_id',
				    			labelWidth:60,
				    			fieldStyle:me.fieldStyle, 
				    			name: 'task_id',
				    			readOnly : true,
				    			maxLength: 50,
				    		    hidden:true
				    			},
				    			{
							    xtype:"textfield",
							  	fieldLabel: 'processInstanceId',
							    labelWidth:60,
							    fieldStyle:me.fieldStyle, 
							    name: 'processInstanceId',
							    readOnly : true,
							    maxLength: 50,
							     hidden:true
							},			
			    			{
			    				columnWidth : .50,
			    				xtype: 'combo',
			        			autoSelect : true,
			        			editable:false,
			        			labelWidth : 60,
			        			fieldLabel:'类型<span style="color:red;font-weight:bold" data-qtip="Required"></span>',
			        	        name:'inspectionunit',
			        	        allowBlank:false, //不允许为空
			        			blankText:"不能为空", //错误提示信息，默认为This field is required!
			        	        triggerAction: 'all',
			        	        queryMode: 'local', 
			        	        style:'margin-left:10px;',
			        	        emptyText : "请选择",
			        	        selectOnTab: true,
			        	    	readOnly : true,
			        	        store: Ext.create('Ext.data.Store', { 
			                    	fields: ['id', 'name'], 
			                    	data : [{"id":"1","name":"NIPT(博奥)"},
			                    	         {"id":"5","name":"NIPT(成都博奥)"},
			                    	         {"id":"2","name":"NIPT(贝瑞)"},
			                    	         {"id":"3","name":"NIPT-plus(博奥)"},
			                    	         {"id":"4","name":"NIPT-plus(贝瑞)"},
			                    	]
			                    }),
			        	        fieldStyle: me.fieldStyle,
			        	        displayField:'name',
			        	        valueField:'id',
			        	        listClass: 'x-combo-list-small'
			    			}
			    			]
    			
    		},{
    			xtype : 'textarea',
    			readOnly : true,
    			fieldLabel : '审核理由',
				labelWidth:65,
				height:80,
				name : 'verifyRemark',
				id : 'check_remark_verify',
				maxLength : 200,
    		}]
       }]},{
			xtype : 'panel',
			region : 'center',
			id : 'invasive_img_show_verify',
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
					}]
		}];
		
		me.buttons = [
		{
			text:'取消',
			iconCls:'Cancel',
			handler:me.onCancel
		}]
		me.callParent(arguments);
	},
	onTurnLeft : function() {
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var path = imgs[0].photo_path;
		Ext.Ajax.request({
					url : "bacera/invasivePre/turnImg.do",
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
							Ext.getCmp('invasive_img_show_verify').remove(Ext
									.getCmp('invasive_img_box_verify'));
							//获取图片高度和宽度
							Ext.Ajax.request({
								url : "bacera/invasivePre/getImgWidth.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {'photo_path' : imgs[0].photo_path},
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
										
										Ext.getCmp('invasive_img_show_verify').add({
											xtype : 'box',
//											style : 'padding:6px;width:100%;',
											id : 'invasive_img_box_verify',
											autoEl : {
												width:Math.ceil(width),
												height:Math.ceil(height),
												tag : 'img',
												src : "bacera/invasivePre/getImg.do?photo_path="
														+ imgs[0].photo_path+"&v="+new Date().getTime()
											}
										});
									}else
										{
										Ext.MessageBox.alert("提示信息","案例照片有问题，请联系管理员！");
										return;
										}
								},
								failure : function() {
									Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
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
		if(imgs==""){
			Ext.Msg.alert("提示", "图片不存在!");
			return;
		}
		var path = imgs[0].photo_path;
		Ext.Ajax.request({
					url : "bacera/invasivePre/turnImg.do",
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
							Ext.getCmp('invasive_img_show_verify').remove(Ext
									.getCmp('invasive_img_box_verify'));
							//获取图片高度和宽度
							Ext.Ajax.request({
								url : "bacera/invasivePre/getImgWidth.do",
								method : "POST",
								headers : {
									'Content-Type' : 'application/json'
								},
								jsonData : {'photo_path' : imgs[0].photo_path},
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
										
										Ext.getCmp('invasive_img_show_verify').add({
											xtype : 'box',
											id : 'invasive_img_box_verify',
											autoEl : {
												width:Math.ceil(width),
												height:Math.ceil(height),
												tag : 'img',
												src : "bacera/invasivePre/getImg.do?photo_path="
														+ imgs[0].photo_path+"&v="+new Date().getTime()
											}
										});
									}else
										{
										Ext.MessageBox.alert("提示信息","案例照片有问题，请联系管理员！");
										return;
										}
								},
								failure : function() {
									Ext.Msg.alert("提示","网络故障<br>请联系管理员!");
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
	
	//验证后保存
	onVerify:function(){
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		if(form.isValid())
		{
         	Ext.MessageBox.wait('正在操作','请稍后...');
			Ext.Ajax.request({  
				url:"bacera/invasivePre/invasivePreYes.do", 
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
	                 	Ext.MessageBox.alert("错误信息", "更新失败！请联系管理员；<br>"+response.message);
	                 } 
				},  
				failure: function () {
					Ext.Msg.alert("提示", "操作失败<br>请联系管理员!");
				}
	    	       
	      	});
		}
	
		
	},
	onNoVerify : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		//values.taskId=me.taskId;
		//values.processInstanceId=me.processInstanceId;
		var check_remark_verify = Ext.getCmp("check_remark_verify").getValue();
		if (check_remark_verify == '' || check_remark_verify == null) {
			Ext.Msg.alert("提示", "请填写审核不通过理由。");
			return;
		}
		if (form.isValid()) {
			Ext.Msg.confirm("提示", "确认该案例不通过?", function (id) {
				if (id == 'yes') {
					Ext.MessageBox.wait('正在操作', '请稍后...');
					Ext.Ajax.request({
						url: "bacera/invasivePre/invasivePreNo.do",
						method: "POST",
						headers: {
							'Content-Type': 'application/json'
						},
						jsonData: values,
						success: function (response, options) {
							response = Ext.JSON.decode(response.responseText);
							console.log(response);
							if (response.success == true) {
								Ext.MessageBox.alert("提示信息", response.message);
								me.grid.getStore().load();
								me.up("window").close();
							} else {
								Ext.MessageBox.alert("错误信息", response.message);
							}
						},
						failure: function () {
							Ext.Msg.alert("提示", "出错<br>请联系管理员!");
						}

					});
				}
			});
		}
	},
	onCancel:function(){
		var me = this;
		me.up("window").close();
	},	
	listeners : {
		'afterrender' : function() {
			var me = this;
			var values = me.getValues();
			console.log(values);
			var id = values["id"];
			//添加案例图片信息
			Ext.Ajax.request({
				url : "bacera/invasivePre/getAttachMent.do",
				method : "POST",
				async: false,
				headers : {
					'Content-Type' : 'application/json'
				},
				jsonData : {
					'id' : id
				},
				success : function(response, options) {
					var data = Ext.JSON.decode(response.responseText);
					console.log(data);
					if(data.length>0){
						imgs = data;
						img_count = data.length;
						//获取图片高度和宽度
						Ext.Ajax.request({
							url : "bacera/invasivePre/getImgWidth.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : {'photo_path' : imgs[0].photo_path},
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
									Ext.getCmp('invasive_img_show_verify').add({
										xtype : 'box',
//										style : 'padding:6px;width:100%;',
										id : 'invasive_img_box_verify',
										autoEl : {
											width:Math.ceil(width),
											height:Math.ceil(height),
											tag : 'img',
											src : "bacera/invasivePre/getImg.do?photo_path="
													+ imgs[0].photo_path+"&v="+new Date().getTime()
										}
									});
								}else
									{
									Ext.MessageBox.alert("提示信息","案例照片有问题，请联系管理员！");
									return;
									}
//								if (response == true) {
//									Ext.MessageBox.alert("提示信息","废除成功！");
//									me.getStore().load();
//								} 
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
	