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
var storeModel = Ext.create('Ext.data.Store', {
	model : 'model',
	proxy : {
		type : 'jsonajax',
		actionMethods : {
			read : 'POST'
		},
		url : 'judicial/dicvalues/getReportModelByPartner.do',
		params : {
			type : 'dna',
			receiver_id : ""
		},
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
Ext.define('Rds.judicial.form.JudicialRegisterChangeForm', {
//	extend : 'Ext.form.Panel',
//	layout : "border",
	extend : 'Ext.form.Panel',
	style : 'margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:5px;',
	bodyPadding : 10,
	defaults : {
		anchor : '100%',
		labelWidth : 80,
		labelSeparator : "：",
		labelAlign : 'right'
	},
	border : false,
	autoHeight : true,
	initComponent : function() {
		        var me = this;
				var case_userid = Ext.create('Ext.form.ComboBox', {
					xtype : 'combo',
					columnWidth : .50,
					fieldLabel : '归属人<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
					readOnly:true,
					labelWidth : 80,
					labelAlign : 'right',
					name : 'case_userid_update',
					id:'case_userid_update',
					emptyText:'检索方式(姓名首字母)：如小红(xh)',
					store : Ext.create("Ext.data.Store",{
						 fields:[
									{name:'key',mapping:'key',type:'string'},
									{name:'value',mapping:'value',type:'string'}
					          ],
						pageSize : 10,
						proxy : {
							type : "ajax",
							url:'judicial/dicvalues/getUsersId.do?userid='+ownpersonTemp,
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
					},
					listeners : {
						'afterrender':function(){
							if("" != ownpersonTemp)
							{
								this.store.load();
							}
						}
					}
				});
				var case_areacode=Ext.create('Ext.form.ComboBox',{
						xtype : 'combo',
						columnWidth : .50,
						fieldLabel : '归属地区<span style="color:red">*</span>',
						readOnly:true,
						labelWidth : 80,
						labelAlign : 'right',
						name : 'case_areacode_update',
						id:'case_areacode_update',
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
							},
							"select" : function(combo, record, index){
                        		Ext.Ajax.request({
                    				url : "upc/partnerConfig/queryPartnerName.do",
                    				method : "POST",
                    				headers : {
                    					'Content-Type' : 'application/json'
                    				},
                    				jsonData : {case_areacode:combo.getValue()},
                    				success : function(response, options) {
                    					response = Ext.JSON.decode(response.responseText);
                    					if (response.result == true) {
                    						
                    						Ext.getCmp("parnter_name").setValue(response.message);
                    					}
                    				},
                    				failure : function() {
                    					Ext.Msg.alert("提示", "保存失败<br>请联系管理员!");
                    				}

                    			});
                        		
                        	}
						}
			          });
				var modeltype=Ext.create('Ext.form.ComboBox', {
							columnWidth : .50,
							xtype : "combo",
							fieldLabel : "模板类型<span style='color:red'>*</span>",
							readOnly:true,
							mode: 'local',   
							labelWidth : 80,
							editable:false,
							labelAlign: 'right',
							blankText:'请选择模板',   
					        emptyText:'请选择模板',  
//					        allowBlank  : false,//不允许为空  
//				            blankText   : "不能为空",//错误提示信息，默认为This field is required!  
							valueField :"key",   
					        displayField: "value", 
							name : 'report_model',
							store: storeModel
				   });
				var partnerModel = Ext.create('Ext.data.Store', {
					fields:[
			                  {name:'parnter_name',mapping:'parnter_name',type:'string'},
			          ],
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'POST'
						},
						url : 'upc/partnerConfig/queryall.do',
						params : {
							type : 'dna',
						},
						reader : {
							type : 'json',
							root : 'data'
						}
					},
					autoLoad : false,
					remoteSort : true
				});
				var partner = Ext.create('Ext.form.ComboBox', {
					fieldLabel: "合作方",
					readOnly:true,
                    labelWidth : 80,
                    editable:true,
                    triggerAction: 'all',
                    displayField: 'parnter_name',
                    labelAlign: 'right',
                    columnWidth : .50,
                    valueField: 'parnter_name',
                    store: partnerModel,
                    mode: 'local',
                    name: 'parnter_name',
                    id:'parnter_name'
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
				items : [{
					xtype : 'hiddenfield',
					name : 'case_id'
				},{
					xtype : 'hiddenfield',
					name : 'is_new',
				}, {
					xtype : 'hiddenfield',
					name : 'case_areacode'
				},{
					xtype : 'hiddenfield',
					name : 'attach_need_case'
				}, {
					xtype : 'hiddenfield',
					name : 'case_userid'
				},	{
					xtype : 'datefield',
					name : 'accept_time',
					columnWidth : .50,
					labelWidth : 80,
					fieldLabel : "受理时间<span style='color:red'>*</span>",
					labelAlign : 'right',
					readOnly:true,
					format : 'Y-m-d',
					allowBlank : false,// 不允许为空
					maxValue : new Date(),
					value : Ext.Date.add(
							new Date(),
							Ext.Date.DAY),
				},new Ext.form.field.ComboBox(
						{
							fieldLabel : '案例来源',
							readOnly:true,
							columnWidth : .50,
							labelWidth : 80,
							editable : false,
							triggerAction : 'all',
							displayField : 'Name',
							labelAlign : 'right',
							valueField : 'Code',
							store : new Ext.data.ArrayStore(
									{
										fields : [
												'Name',
												'Code' ],
										data : [
												[
														'实体渠道',
														'0' ],
												[
														'电子渠道',
														'1' ] ]
									}),
							mode : 'local',
							name : 'source_type'
						})
				         ]},{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					items : [ {
								// 该列在整行中所占的百分比
								columnWidth : .50,
								xtype : "textfield",
								labelAlign : 'right',
								fieldLabel : "案例条形码<span style='color:red'>*</span>",
								readOnly : true,
								labelWidth : 80,
								maxLength : 50,
								name : 'case_code'
							}, new Ext.form.field.ComboBox({
										fieldLabel : '紧急程度',
										readOnly:true,
										columnWidth : .50,
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
										name : 'urgent_state'
									})]
				}, {
					layout : "column",// 从左往右的布局
					xtype : 'fieldcontainer',
					border : false,
					items : [ case_userid,case_areacode]
				}, 	
				{
					layout : "column",// 从左往右的布局
					xtype : 'fieldcontainer',
					border : false,
					hidden:true,
					items : [ attach_need ]
				},
				{
					layout : "column",// 从左往右的布局
					xtype : 'fieldcontainer',
					border : false,
					    items : [
								{
									// 该列在整行中所占的百分比
									columnWidth : .50,
									xtype : "textfield",
									labelAlign: 'right',
									fieldLabel : "采样人员<span style='color:red'>*</span>",
									readOnly:true,
									allowBlank  : false,//不允许为空  
									labelWidth : 80,
								    maxLength :50,
									name : 'sample_in_per',
								},
								{
									columnWidth : .50,
									xtype : "combo",
									fieldLabel : "单位类型<span style='color:red'>*</span>",
									mode: 'local',   
									labelWidth : 80,
									editable:false,
									labelAlign: 'right',
									readOnly:true,
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
								columnWidth : .50,
								xtype : "textfield",
								labelAlign : 'right',
								readOnly:true,
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
								columnWidth : .50,
								xtype : "textfield",
								labelAlign : 'right',
								labelWidth : 80,
//								readOnly:true,
								fieldLabel : '委托人',
								maxLength : 50,
								name : 'client'
							},{
								xtype : 'datefield',
								name : 'consignment_time',
								columnWidth : .50,
								labelWidth : 80,
								fieldLabel : "委托时间<span style='color:red'>*</span>",
								readOnly:true,
								labelAlign : 'right',
								format : 'Y-m-d',
								allowBlank : false,// 不允许为空
								maxValue : new Date()
							} ]
				},{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					items : [{xtype:"hiddenfield",name:"case_type",value:"0"},
					         Ext.create('Ext.form.field.Number',{
					    fieldLabel: "打印份数<span style='color:red'>*</span>",
					    name: 'copies',
					    labelWidth : 80,
					    value: 2,
					    maxValue: 10,
					    minValue: 0,
					    columnWidth : .50,
					    labelAlign: 'right',
					    allowBlank:false
					}), new Ext.form.field.ComboBox({
	                    fieldLabel: "亲属关系<span style='color:red'>*</span>",
	                    columnWidth : .50,
	                    labelWidth : 80,
	                    editable:false,
	                    triggerAction: 'all',
	                    displayField: 'Name',
	                    labelAlign: 'right',
	                    readOnly:true,
	                    valueField: 'Code',
	                    store: new Ext.data.ArrayStore({
	                        fields: ['Name', 'Code'],
	                        data: [['亲子', 0], ['亲缘', 1],['同胞',2]]
	                    })          ,
	                    mode: 'local',
	                    //typeAhead: true,
	                    name: 'sample_relation'
	                })]
				},
				{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					items : [
					         Ext.create('Ext.form.ComboBox', {
						fieldLabel: "单双亲<span style='color:red'>*</span>",
	                    labelWidth : 80,
	                    editable:false,
	                    triggerAction: 'all',
	                    displayField: 'Name',
	                    labelAlign: 'right',
	                    readOnly:true,
	                    blankText:'请选择类型',   
				        emptyText:'请选择类型',  
	                    allowBlank  : false,//不允许为空  
	                    columnWidth : .50,
	                    valueField: 'Code',
	                    store: new Ext.data.ArrayStore({
	                        fields: ['Name', 'Code'],
	                        data: [['单亲', '1'], ['双亲', '2']]
	                    })          ,
	                    mode: 'local',
	                    name: 'typeid',
	                    id:'typeid'
					}),Ext.create('Ext.form.field.Number',{
					    fieldLabel: "样本数<span style='color:red'>*</span>",
					    readOnly:true,
					    name: 'per_num',
					    id:'per_num',
					    labelWidth : 80,
					    value: 0,
					    maxValue: 9999,
					    minValue: 0,
					    columnWidth : .50,
					    labelAlign: 'right',
					    allowBlank:false,
					    readOnly:true
					})]
				},{
					layout : "column",// 从左往右的布局
					xtype : 'fieldcontainer',
					border : false,
					items : [partner,{
						// 该列在整行中所占的百分比
						columnWidth : .50,
						xtype : "textfield",
						labelAlign : 'right',
						labelWidth : 80,
						fieldLabel : '优惠码',
						readOnly:true,
						maxLength : 50,
						name : 'confirm_code',
					}]
				},
				{
					border : false,
					xtype : 'fieldcontainer',
					layout : "column",
					items : [{
						xtype : 'textarea',
						fieldLabel : '备注',
						name : 'remark',
						width : 500,
						 maxLength :500,
						columnWidth : 1,
						labelWidth : 80,
						labelAlign : 'right'
					}]
				}]
		}
		];

		me.buttons = [{
					text : '修改',
					iconCls : 'Disk',
					handler : me.onUpdate
				},{
					text : '取消',
					iconCls : 'Cancel',
					handler : me.onCancel
				}]
		me.callParent(arguments);
	},
	onUpdate : function() {
		var me = this.up("form");
		var form = me.getForm();
		var values = form.getValues();
		values["case_areacode"] = Ext.getCmp("case_areacode_update").getValue();
		values["receiver_area"]= Ext.getCmp("case_areacode_update").getRawValue();
		values["case_userid"] = Ext.getCmp("case_userid_update").getValue();
		var sample_str = values['sample_code'];
		var id_numbers = values['id_number'];
		var temp='';

		if(!(sample_str instanceof Array))
		{
			Ext.MessageBox.alert("提示信息","样本填写错误！");
			return;
		}else
		{
//			var sample_str_temp = sample_str;
//			var nary=sample_str_temp.sort(); 
			console.log(sample_str);
			for(var i=0;i<sample_str.length-1;i++){ 
				for(var j = i+1; j < sample_str.length; j ++)
				{
					console.log(sample_str[i]+"-----"+sample_str[j])
					if (sample_str[i]==sample_str[j]){ 
						Ext.MessageBox.alert("提示信息","重复样本条形码："+sample_str[i]);
						return;
					} 
				}
			} 
		}

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
			Ext.MessageBox.wait('正在操作','请稍后...');
//				if (sample_str) {
//					if(!((typeof sample_str=='string')&&sample_str.constructor==String)){
//						for (var i = 0; i < sample_str.length; i++) {
//							for (var j = 0; j < sample_str.length; j++) {
//								if (i != j && sample_str[i] == sample_str[j]) {
//									result = false;
//								}
//							}
//						}
//					}
//		}
		var id_result = true;
		var sample_call = values['sample_call'];
		if(undefined == sample_call)
		{
			Ext.MessageBox.alert("错误信息", "样本信息还没填呢！");
			return;
		}
		
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
				if(father_num>1||mother_num>1){
					Ext.MessageBox.alert("错误信息", "家长称谓出现重复");
					return;
				}
				if(father_num == 1 && mother_num == 1 && values["typeid"] != "2")
				{
					Ext.MessageBox.alert("错误信息", "单双亲类型和样本类型不符！");
					return;
				}
				if(!values["attach_need"]){
					values["attach_need"]=1;
				}
				Ext.Ajax.request({
							url : "judicial/register/updateChangeCaseInfo.do",
							method : "POST",
							headers : {
								'Content-Type' : 'application/json'
							},
							jsonData : values,
							success : function(response, options) {
								response = Ext.JSON
										.decode(response.responseText);
								if (response.result == true) {
									Ext.MessageBox.alert("提示信息", response.message);
									me.grid.getStore().load();
									me.up("window").close();
								} else {
									Ext.MessageBox.alert("错误信息",  response.message);
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
			var attach_need_case=values["attach_need_case"];
			var area = me.down("combo[name=case_areacode_update]");
			area.setValue(area_code);
			var attach_need=me.down("checkbox[name=attach_need]");
			if(attach_need_case==0){
				attach_need.setValue(true);
			}
			//归属人
			var case_userid = me.down("combo[name=case_userid_update]");
			case_userid.setValue(values["case_userid"]);
			
			//添加样本信息
			Ext.Ajax.request({
				url : 'judicial/register/getSampleInfo.do',
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
					var items = response["items"];
					Ext.getCmp("per_num").setValue(items.length);
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
												columnWidth : .27,
												xtype : "textfield",
												fieldLabel : "条形码<span style='color:red'>*</span>",
												regex:/^\w+$/,
												regexText:'请输入正确条形码',
												labelAlign : 'right',
												value : items[i].sample_code,
												maxLength : 50,
												labelWidth : 80,
												name : 'sample_code',
//												readOnly:true,
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
											}, {
												columnWidth : .24,
												xtype : "textfield",
												fieldLabel : "用户名<span style='color:red'>*</span>",
												labelAlign : 'right',
												maxLength : 50,
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												labelWidth : 80,
												value : items[i].sample_username,
												name : 'sample_username'
											}, {
												columnWidth : .26,
												xtype : "textfield",
												fieldLabel : '身份证',
												labelAlign : 'right',
												maxLength : 18,
												value : items[i].id_number,
												labelWidth : 80,
												name : 'id_number',
												listeners:
												{
													"blur":function(combo, record, index){
														if(""!=combo.getValue())
														{
															var mei = this.up("form");
															mei.getForm().findField('birth_date').setValue(getBirthdatByIdNo(combo.getValue()));
															console.log(getBirthdatByIdNo(combo.getValue()));
														}
														
													}
												}
											},{
												layout : 'absolute',// 从左往右的布局
												xtype : 'panel',
												border : false,
												columnWidth : .23,
												items : [Ext.create('Ext.form.ComboBox', {
													fieldLabel: "特殊样本",
								                    labelWidth : 60,
								                    editable:false,
								                    width:140,
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
								                    name: 'special'
												}) ]
											} ]
								}, {
									layout : 'column',
									xtype : 'panel',
									border : false,
									style : 'margin-top:5px;',
									items : [{
												columnWidth : .27,
												xtype : "combo",
												fieldLabel : "取样类型<span style='color:red'>*</span>",
												readOnly:true,
												mode : 'local',
												labelWidth : 60,
												labelAlign : 'right',
												blankText : '请选择',
												emptyText : '请选择',
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												valueField : "key",
												displayField : "value",
												value : items[i].sample_type,
												maxLength : 50,
												labelWidth : 80,
												editable:false,
												store : storeSampleType,
												name : 'sample_type',
												 listeners :{
							                        	"select" : function(combo, record, index){
							                        		var mei = this.up("form");
							                        		if( combo.getValue() == 'cy01' || combo.getValue()== 'cy02' || combo.getValue()== 'cy03' )
								                        		mei.getForm().findField('special').setValue('0');
							                        		else
								                        		mei.getForm().findField('special').setValue('1');
							                        	}
							                        }
											}, {
												columnWidth : .24,
												xtype : "combo",
												mode : 'local',
												labelWidth : 60,
												labelAlign : 'right',
												blankText : '请选择',
												emptyText : '请选择',
												allowBlank : false,// 不允许为空
												blankText : "不能为空",// 错误提示信息，默认为This
												editable:false,
												valueField : "key",
												displayField : "value",
												value : items[i].sample_call,
												store : storeSampleCall,
												fieldLabel : "称谓<span style='color:red'>*</span>",
//												readOnly:true,
												maxLength : 50,
												labelWidth : 80,
												name : 'sample_call'
											}, {
												columnWidth : .26,
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

		}
	}
});
