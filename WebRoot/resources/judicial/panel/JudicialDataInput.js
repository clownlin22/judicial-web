Ext.define('Rds.judicial.panel.JudicialDataInput', {
	extend : 'Ext.panel.Panel',
//	autoScroll:true,
	layout:'border',
	required:'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',

	buttonAlign:'right',
	
	initComponent : function() {
		var me = this;
		
		var menu = Ext.create('Ext.menu.Menu', {
		    id: 'mainMenu',
		    style: {
		        overflow: 'visible'
		    },
		    items: [{
		            text: '另存为模版',
		            iconCls:'Pagesave',
		            handler:function(){
		            	var win = Ext.create("Ext.window.Window",{
		            		title:'另存为模版',
		            		height:600,
		            		width:800,
		            		layout:'border',
		            		buttons:[{
		            			text:'保存',
		            			handler:function(){
		            				Ext.MessageBox.prompt("模版关键字","请输入模版关键字",function(id,msg){
//		            					alert(id+":"+msg);
		            					Ext.MessageBox.alert("消息", "另存为模版成功");
		    		            	});
		            				
		            			}
		            		}]
		            	});
		            	var tgjc = me.down("htmleditor[name=tgjc]");
		            	var fxsm = me.down("htmleditor[name=fxsm]");
		            	var jdyj = me.down("htmleditor[name=jdyj]");
		            	
		            	var panel = Ext.create("Rds.judicial.panel.JudicialTplPanel",{
		            		region:'center'
		            	});
		            	
		            	win.add(panel);
		            	win.show();
		            	//设置values
		            	panel.loadRecord(Ext.create("Rds.judicial.model.JudicialTplModel",{
	            			tgjc:tgjc.getValue(),
	            			fxsm:fxsm.getValue(),
	            			jdyj:jdyj.getValue()
	            		}));
		            	//加载store
		            	var store = me.down("grid").getStore();
		            	var p_store = panel.down("grid").getStore();
		            	for(var i=0;i<store.getCount();i++){
		            		p_store.add(store.getAt(i));
		            	}
		            	
		            }
		        },{
		            text: '保存全部',
		            iconCls:'Scriptsave',
		            handler:function(){
		            	Ext.MessageBox.alert("消息", "保存成功");
		            	
		            }
		        }
		    ]

		}); 
		me.items = [
		            me.centerPanel()
		        ];
		me.tbar = [{ 
			text: "菜单", 
			iconCls:'Folderpage',
			menu: menu
		},{
			text:'模版',
			iconCls:'Diskmagnify',
			handler:function(){
				me.addRight("tpl");
			}
		},{
			text:'鉴定标准',
			iconCls:'Diskedit',
			handler:function(){
				me.addRight("judicial");
			}
		},'历史报告',{
            xtype: 'combo',
            labelWidth:60,
            hideLabel: true,
            name: 'history',
            store: Ext.create("Ext.data.Store", {
            	fields: [
            	         { name: "key", type: "key" },
                         { name: "value", type: "value" }],
                         pageSize: 10,
//                         autoLoad: true,
                         proxy: {
                        	 type: "ajax",
                        	 url: "judicial/process/queryJudicialByCodeForComplate.do",
                        	 reader: {
                        		 type: "json"
                        	 }
                         }
            }),
            fieldLabel: "关键字",
            displayField: 'value',
            valueField:'key',
            typeAhead: false,
            hideTrigger: true,
            minChars: 1,
            matchFieldWidth: true,
            listConfig: {
                loadingText: '正在查找...',
                emptyText: '没有找到匹配的数据'
            }
        }, {
           xtype: 'button',
           text: '导入',
           iconCls:'Add',
           handler:me.onImport,
           scope: me
       }];
		
		me.callParent(arguments);
	},
	
	onImport:function(){
		var me = this;
		var history = me.down("combo[name=history]");
		var result = {
				tgjc:'被鉴定人步行入检查室，右下肢跛行，语言清晰，对答切题，查体合作，左额部见长6㎝条状皮肤疤痕。右小腿外固定支架在位，右小腿外侧下段见长12㎝纵形疤痕，右胫前下段见长8.5㎝纵形疤痕，右膝关节下方见长14㎝横形软组织疤痕。右下肢活动受限，双上肢活动好。',
				fxsm:'根据送检的鉴定资料记载，被鉴定人章路峰于2013年7月6日因交通事故致右胫腓骨折、多发性肋骨骨折、右外踝骨折，伤后一年余，临床体征稳定。根据体格检查及阅片所见，被鉴定人章路峰右侧第4-6及左侧第3-7肋骨骨折（共8根）经治疗后，目前恢复可；右胫腓骨骨折合并右外踝骨折经治疗后目前外固定支架在位，遗留右下肢跛行，右下肢行走、承重功能受限，右下肢丧失功能10%以上，依据《道路交通事故受伤人员伤残评定》（GB18667-2002）标准第4.9.5b、附则5.1及第4.10.10i条规定，被鉴定人章路峰8肋以上骨折构成道路交通事故九级伤残；右胫腓骨骨折合并右外踝骨折构成道路交通事故十级伤残。被鉴定人章路峰损伤后，因治疗和康复的需要而无法参加工作，需要休息、存在误工；生活自理能力下降，需要他人帮助；并且需加强营养作为辅助治疗措施，以利于损伤恢复和身体康复。按照《人身损害受伤人员误工损失日评定准则》（GA/T521-2004）及江苏省关于《人身损害受伤人员休息期、营养期、护理期评定标准》等相关规定，结合治疗与恢复情况，被鉴定人章路峰的误工期限以受伤之日起至评残前一日止为宜；护理期限以三个月为宜。',
				jdyj:'1被鉴定人章路峰8肋以上骨折构成道路交通事故九级伤残；右胫腓骨骨折合并右外踝骨折构成道路交通事故十级伤残。'
		};
		me.down("htmleditor[name=tgjc]").setValue(result.tgjc);
		me.down("htmleditor[name=fxsm]").setValue(result.fxsm);
		me.down("htmleditor[name=jdyj]").setValue(result.jdyj);
//		alert(history.getValue());
	},
	
	
	tplPanel:function(){
		var me = this;
		
		return Ext.create("Rds.judicial.panel.JudicialInputTplPanel",{
			input : me
		});
	},
	
	judicialStandardPanel:function(){
		var me = this;
		
		return Ext.create("Rds.judicial.panel.JudicialInputStandardPanel",{
			input:me,
			title:'鉴定标准'
		});
	},
	
	addRight:function(type){
		var me = this;
		var right = me.down("panel[region=east]");
		if(right){
			me.remove(right);
		}
		if(type=="judicial"){
			me.add(me.judicialStandardPanel());
		}else if(type='tpl'){
			me.add(me.tplPanel());
		}
		
	},
	
	centerPanel:function(){
		var me = this;
		return {
			xtype:'panel',
			autoScroll:true,
			region:'center',
			items:[
			       me.baseContentPanel(),
			       me.abstractPanel(),
			       me.checkProPanel(),
			       me.explainPanel(),
			       me.expertPanel()
			]
		};
	},
	
	baseContentPanel:function(){
		var me = this;
		return {
			xtype:'form',
			title:'一、基础内容',
			defaultType: 'textfield',
			bodyPadding: '5 5 0',
            defaults: {
                anchor: '100%'
            },
			items:[{
                fieldLabel: '委 托 人',
                afterLabelTextTpl: me.required,
                name: 'wtr',
                value:'宿迁市宿城区人民法院',
                allowBlank:false
            },{
                fieldLabel: '委托函号',
                afterLabelTextTpl: me.required,
                name: 'wthh',
                value:'（2014）宿城法鉴委字第335号',
                allowBlank:false
            },{
                fieldLabel: '委托事项',
                afterLabelTextTpl: me.required,
                name: 'wtsx',
                value:'章路峰的伤残等级；误工及护理期限进行鉴定。',
                allowBlank:false
            }, {
                fieldLabel: '受理日期',
                afterLabelTextTpl: me.required,
                name: 'sjrq',
                value:'2014年8月18日',
                allowBlank:false
            }, {
                fieldLabel: '鉴定材料',
                afterLabelTextTpl: me.required,
                name: 'jdzl',
                value:'卷宗一册（含鉴定委托书、病史资料复印件等）',
                allowBlank:false
            }, {
                fieldLabel: '鉴定日期',
                afterLabelTextTpl: me.required,
                name: 'jdrq',
                value:'2014年8月18日～2014年8月25日。',
                allowBlank:false
            }, {
                fieldLabel: '鉴定地点',
                afterLabelTextTpl: me.required,
                name: 'jddd',
                value:'宿迁子渊司法鉴定所 ',
                allowBlank:false
            }, {
                fieldLabel: '被鉴定人',
                afterLabelTextTpl: me.required,
                name: 'bjdr',
                value:'章路峰，男，1970年1月25日生，住址：江苏省沭阳县沭城镇宣义二巷15号，身份证号：320823197001250533。 ',
                allowBlank:false
            }]
		};
	},
	abstractPanel:function(){
		var me = this;
		return {
			xtype:'form',
			title:'二、检案摘要',
	        bodyPadding: '5 5 0',
	        fieldDefaults: {
	            msgTarget: 'side',
	            labelWidth: 75
	        },
	        defaults: {
	            anchor: '100%'
	        },
	        items: [{
	            xtype:'fieldset',
	            title: '（一）案情摘要',
	            defaultType: 'htmleditor',
	            layout: 'anchor',
	            defaults: {
	                anchor: '100%'
	            },
	            items :[{
	                fieldLabel: '案情摘要',
	                afterLabelTextTpl: me.required,
	                grow: true,  
	                hideLabel:true,
	                name: 'first',
	                value:'据送检的鉴定资料记载：被鉴定人章路峰于2013年7月6日因交通事故受伤，伤后经沭阳县中心医院治疗。',
	                allowBlank:false
	            }]
	        },{
	            xtype:'fieldset',
	            title: '（二）病历摘要',
	            defaultType: 'htmleditor',
	            layout: 'anchor',
	            defaults: {
	                anchor: '100%'
	            },
	            items :[{
	                fieldLabel: '病历摘要',
	                afterLabelTextTpl: me.required,
	                grow: true,  
	                hideLabel:true,
	                name: 'first',
	                value:'沭阳县中心医院（住院号：79443）记载：'+
	                	'入院情况：患者因“车祸致右小腿出血疼痛16小时”入院。查体：头颅五官无畸形，头顶部见长约15㎝深达颅骨，边缘不齐，挫伤严重，少许渗血纵行创口，压痛，叩击痛，左侧胸壁广泛青紫肿胀，呼吸动度减弱，左侧9-11肋骨前缘压痛，叩击痛，可及骨擦感，右小腿中下段皮肤软组织广泛挫伤，胫前见长约5.0㎝皮肤创口，少许渗血，边缘不齐，深达皮下，重度污染，挫伤严重，压痛，叩击痛，可及骨擦感，右下肢活动受限，肢体纵向叩击痛阳性，右根部见长约3.0㎝不规则皮肤创口，边缘不齐，重度污染，深达皮下，压痛叩击痛，右上臂中段外侧见长约2.0㎝不规则皮肤创口，出血不止，压痛叩击痛，左小腿皮肤软组织广泛挫伤，青紫肿胀，末梢血运良好。X线示：右胫腓骨、右外踝骨折，CT示：左侧肋骨骨折，右侧肩胛骨骨折，两肺挫伤，双侧胸腔积液。'+
	                	'住院经过：入院后完善相关辅助检查，于急诊下患肢“清创缝合+外固定支架外固定术+右腓骨切开复位内固定术”术后予以抗炎，消肿，止痛等治疗，于2013年7月23日好转出院。'+
	                	'出院诊断：右胫前开放性粉碎性骨折；右外踝骨折；左侧多发性肋骨骨折；左小腿软组织挫伤；右上臂皮肤软组织挫裂伤；右跟部皮肤挫裂伤；轻型颅脑损伤、头皮挫裂伤。',
	                allowBlank:false
	            }]
	        }]
		};
	},
	
	checkProPanel:function(){
		var me = this;
		return {
			xtype:'form',
			title:'三、检验过程',
			titlePosition: 2,
			bodyPadding: '5 5 0',
	        fieldDefaults: {
	            msgTarget: 'side',
	            labelWidth: 75
	        },
	        defaults: {
	            anchor: '100%'
	        },
	        items: [{
	            xtype:'fieldset',
	            title: '（一）检验方法',
	            defaultType: 'htmleditor',
	            layout: 'anchor',
	            defaults: {
	                anchor: '100%'
	            },
	            items :[{
	                fieldLabel: '检验方法',
	                afterLabelTextTpl: me.required,
	                grow: true,  
	                hideLabel:true,
	                value:'根据《法医临床检验规范》（SF/Z JD0103003-2011）对被鉴定人章路峰进行法医学检验。',
	                name: 'first',
	                allowBlank:false
	            }]
	        },{
	            xtype:'fieldset',
	            title: '（二）体格检查（2014年8月21日）',
	            defaultType: 'htmleditor',
	            layout: 'anchor',
	            defaults: {
	                anchor: '100%'
	            },
	            items :[{
	                fieldLabel: '体格检查',
	                afterLabelTextTpl: me.required,
	                grow: true,  
	                hideLabel:true,
	                name: 'tgjc',
	                allowBlank:false
	            }]
	        },{
	            xtype:'fieldset',
	            title: '（三）阅片所见',
	            checkboxToggle:true,
	            collapsed: true,
	            defaultType: 'htmleditor',
	            layout: 'anchor',
	            defaults: {
	                anchor: '100%'
	            },
	            items :[{
	                fieldLabel: '阅片所见',
	                afterLabelTextTpl: me.required,
	                grow: true,  
	                hideLabel:true,
	                name: 'first',
	                allowBlank:false
	            }]
	        }]
		};
	},
	explainPanel:function(){
		var me = this;
		return {
			xtype:'panel',
			title:'四、分析说明',
			bodyPadding: '5 5 0',
	        fieldDefaults: {
	            msgTarget: 'side',
	            labelWidth: 75
	        },
	        defaults: {
	            anchor: '100%'
	        },
	        items: [{
	            xtype:'fieldset',
	            title: '（一）分析说明',
	            defaultType: 'htmleditor',
	            layout: 'anchor',
	            defaults: {
	                anchor: '100%'
	            },
	            items :[{
	                fieldLabel: '分析说明',
	                afterLabelTextTpl: me.required,
	                grow: true,  
	                hideLabel:true,
	                name: 'fxsm',
	                allowBlank:false
	            }]
	        }]
		};
	},
	expertPanel:function(){
		var me = this;
		return {
			xtype:'panel',
			title:'五、鉴定意见',
			bodyPadding: '5 5 0',
	        fieldDefaults: {
	            msgTarget: 'side',
	            labelWidth: 75
	        },
	        defaults: {
	            anchor: '100%'
	        },
	        items: [{
	            xtype:'fieldset',
	            title: '（一）鉴定意见',
	            defaultType: 'htmleditor',
	            layout: 'anchor',
	            defaults: {
	                anchor: '100%'
	            },
	            items :[{
	                fieldLabel: '鉴定意见',
	                afterLabelTextTpl: me.required,
	                grow: true,  
	                hideLabel:true,
	                name: 'jdyj',
	                allowBlank:false
	            }]
	        }]
		};
	}
});