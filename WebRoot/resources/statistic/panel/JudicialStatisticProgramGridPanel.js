Ext.define("Rds.statistic.panel.JudicialStatisticProgramGridPanel",{
					extend : "Ext.grid.Panel",
					loadMask : true,
					viewConfig : {
						trackOver : false,
						stripeRows : false
					},
					region:'center',
					pageSize : 25,
					initComponent : function() {
						var me = this;
//						var case_area = Ext.create('Ext.form.field.Text', {
//							name : 'case_area',
//							labelWidth : 60,
//							width : '20%',
//							fieldLabel : '案例省区',
//							value:'江苏'
//						});
						var case_area = new Ext.form.ComboBox({
							autoSelect : true,
							editable:true,
							labelWidth : 60,
							width : '20%',
							fieldLabel : '案例省区',
							labelAlign : 'right',
					        name:'case_area',
					        forceSelection:true,
					        triggerAction: 'all',
					        queryMode: 'local', 
					        emptyText : "请选择",
					        selectOnTab: true,
					        store : new Ext.data.ArrayStore({
								fields : ['key','value' ],
								data : [['全部','' ],
										['上海市','上海市' ],
										['云南省','云南省' ],
										['内蒙古','内蒙古' ],
										['北京市','北京市' ],
										['吉林省','吉林省' ],
										['四川省','四川省' ],
										['天津市','天津市' ],
										['宁夏','宁夏' ],
										['安徽','安徽' ],
										['山东省','山东省' ],
										['山西省','山西省' ],
										['广东省','广东省' ],
										['广西','广西' ],
										['新疆','新疆' ],
										['江苏省','江苏省' ],
										['江西省','江西省' ],
										['河北省','河北省' ],
										['河南省','河南省' ],
										['浙江省','浙江省' ],
										['海南省','海南省' ],
										['湖北省','湖北省' ],
										['湖南省','湖南省' ],
										['甘肃省','甘肃省' ],
										['福建省','福建省' ],
										['贵州省','贵州省' ],
										['西藏','西藏' ],
										['辽宁省','辽宁省' ],
										['重庆市','重庆市' ],
										['陕西省','陕西省' ],
										['青海省','青海省' ],
										['黑龙江','黑龙江' ],
										['无地区','无地区' ]]
							}),
					        fieldStyle: me.fieldStyle,
					        displayField:'value',
					        valueField:'value',
					        listClass: 'x-combo-list-small',
						});
						var case_user = Ext.create('Ext.form.ComboBox', {
							xtype : 'combo',
							fieldLabel : '业务员',
							labelWidth : 60,
							width : '20%',
							name : 'case_user',
							emptyText:'(人员首字母)：如小明(xm)',
							store :Ext.create("Ext.data.Store",{
								   fields:[
										{name:'key',mapping:'key',type:'string'},
										{name:'value',mapping:'value',type:'string'}
						          ],
								pageSize : 20,
								autoLoad: false,
								proxy : {
									type : "ajax",
									url:'judicial/dicvalues/getUsersId.do',
									reader : {
										type : "json"
									}
								}
							}),
							displayField : 'value',
							valueField : 'key',
							typeAhead : false,
							hideTrigger : true,
							minChars : 2,
							matchFieldWidth : true,
							listConfig : {
								loadingText : '正在查找...',
								emptyText : '没有找到匹配的数据'
							}
						});
						
//						var case_user = Ext.create('Ext.form.field.Text', {
//							name : 'case_user',
//							labelWidth : 60,
//							width : 200,
//							fieldLabel : '业务员'
//						});
//						var user_dept_level1 = Ext.create('Ext.form.field.Text', {
//							name : 'user_dept_level1',
//							labelWidth : 60,
//							width : 200,
//							fieldLabel : '事业部',
//							value:'公司总部'
//						});
						var user_dept_level1 = new Ext.form.ComboBox({
							autoSelect : true,
							editable:true,
							labelWidth : 60,
							width : '20%',
							fieldLabel : '事业部',
							labelAlign : 'right',
					        name:'user_dept_level1',
					        forceSelection:true,
					        triggerAction: 'all',
					        queryMode: 'local', 
					        emptyText : "请选择",
					        selectOnTab: true,
					        store : new Ext.data.ArrayStore({
								fields : ['key','value' ],
								data : [['全部','' ],
										['渠道事业部','渠道事业部' ],
										['仪器设备事业部','仪器设备事业部' ],
										['企管中心','企管中心' ],
										['公司总部','公司总部' ],
										['医学检验事业部','医学检验事业部' ],
										['司法鉴定事业部','司法鉴定事业部' ],
										['大健康事业部','大健康事业部' ],
										['大客户事业部','大客户事业部' ],
										['检测事业部','检测事业部' ],
										['武汉医学检验事业部','武汉医学检验事业部' ],
										['江苏宿迁子渊实验室','江苏宿迁子渊实验室' ],
										['电商部','电商部' ],
										['研发','研发' ],
										['综合管理部','综合管理部' ],
										['设备维护部','设备维护部' ],
										['转化医学事业部','转化医学事业部' ]]
							}),
					        fieldStyle: me.fieldStyle,
					        displayField:'value',
					        valueField:'value',
					        listClass: 'x-combo-list-small'
						});
						
						
						var case_type = Ext.create('Ext.form.field.Text', {
							name : 'case_type',
							labelWidth : 60,
							width : 200,
							fieldLabel : '项目类型'
						});
						var accept_time=new Ext.form.ComboBox({
							autoSelect : true,
							editable:true,
							labelWidth : 60,
							width : '20%',
							fieldLabel : '年份',
					        name:'accept_time',
					        forceSelection:true,
					        triggerAction: 'all',
					        queryMode: 'local', 
					        emptyText : "请选择",
					        selectOnTab: true,
					        store : new Ext.data.ArrayStore({
								fields : ['key','value' ],
								data : [['全部','' ],
										['2010','2010' ],
										['2011','2011' ],
										['2012','2012' ],
										['2013','2013' ],
										['2014','2014' ],
										['2015','2015' ],
										['2016','2016' ],
										['2017','2017' ],
										['2018','2018' ]]
							}),
					        fieldStyle: me.fieldStyle,
					        displayField:'value',
					        valueField:'value',
					        listClass: 'x-combo-list-small',
					        value:'2018'
						});
						me.store = Ext.create(
										'Ext.data.Store',
										{
											fields : ['case_area','case_user',"case_agentuser","user_dept_level1","case_type"],
											proxy : {
												type : 'jsonajax',
												actionMethods : {
													read : 'POST'
												},
												url : 'statistics/queryProgramProvice.do',
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
												'beforeload' : function(ds,
														operation, opt) {
													Ext.apply(
															me.store.proxy.extraParams,{								
																accept_time :  trim(accept_time.getValue()),	
																case_area : trim(case_area.getValue()),
																case_user : case_user.getRawValue().split("(")[0],
																user_dept_level1 : trim(user_dept_level1.getValue()),
																case_type : trim(case_type.getValue()),
																});
												}
											}
										});

//						me.bbar = Ext.create('Ext.PagingToolbar', {
//							store : me.store,
//							pageSize : me.pageSize,
//							displayInfo : true,
//							displayMsg : "第 {0} - {1} 条  共 {2} 条",
//							emptyMsg : "没有符合条件的记录"
//						});
						me.bbar = {xtype: 'label',id:'totalBbarProgram', text: '',
								style:'height:25px;line-height:25px;text-align:right;margin-right:10px;'};
						me.columns = [
								{
									text : '所属省区',
									dataIndex : 'case_area',
									menuDisabled : true,
									width : 150,
								},{
									text : '事业部',
									dataIndex : 'user_dept_level1',
									menuDisabled : true,
									width : 150
								},{
									text : '项目类型',
									dataIndex : 'case_type',
									menuDisabled : true,
									width : 150,
								},
								{
									text : '所属业务员',
									dataIndex : 'case_user',
									menuDisabled : true,
									width : 150
								},
								{
									text : '代理',
									dataIndex : 'case_agentuser',
									menuDisabled : true,
									width : 150,
								}
							 ];

						me.dockedItems = [
								{
									xtype : 'toolbar',
									name : 'search',
									dock : 'top',
									items : [accept_time,case_area,case_user,user_dept_level1,case_type, {
												text : '查询',
												iconCls : 'Find',
												handler : me.onSearch
											}]
								},{
							 		xtype:'toolbar',
							 		dock:'top', 		
							 		items:[{
							 			text:'格式化',
							 			iconCls:'Add',
							 			handler:me.onFormat
							 		}]
							 	
								}];

						me.callParent(arguments);
						me.store.on("load",function(){
							Ext.getCmp('totalBbarProgram').setText("共 "+me.store.getCount()+" 条");
						});
					},
					onFormat:function(){
						var me = this.up("gridpanel");
						mergeGrid(me,1,false);
					},
					onSearch : function() {
						var me = this.up("gridpanel");
						me.getStore().currentPage=1;
						me.getStore().load();
					},
					listeners : {
						'afterrender' : function() {
							this.store.load();
						}
					}
				});
function mergeGrid(grid, colIndexArray, isAllSome) {
    isAllSome = isAllSome == undefined ? true : isAllSome; // 默认为true
 
    // 1.是否含有数据
    var gridView = document.getElementById(grid.getView().getId() + '-body');
    if (gridView == null) {
        return;
    }
 
    // 2.获取Grid的所有tr
    var trArray = [];
    if (grid.layout.type == 'table') { // 若是table部署方式，获取的tr方式如下
        trArray = gridView.childNodes;
    } else {
        trArray = gridView.getElementsByTagName('tr');
    }
    var trLength = trArray.length
    for(var i = 0; i < trLength; i++){
    	var temp0 = 1;
    	var temp1 = 1;
    	var temp2 = 1;
    	var temp3 = 1;
    	var temp4 = 1;
    	for(var j = i+1; j < trLength; j++){
    		var thisTr = trArray[i];
    		var nextTr = trArray[j];
    		
			var thisTd = thisTr.getElementsByTagName('td')[0];
			var nextTd = nextTr.getElementsByTagName('td')[0];
			if(thisTd.innerText.trim() == nextTd.innerText.trim()){
				thisTd.setAttribute('rowspan', ++temp0);
				nextTd.style.display = 'none';
			}else{
				temp0=1;
				break;
			}
    	}
    	console.log(temp0);
    	for(var j = i+1; j < trLength; j++){
    		var thisTr = trArray[i];
    		var nextTr = trArray[j];
			var thisTd = thisTr.getElementsByTagName('td')[1];
			var nextTd = nextTr.getElementsByTagName('td')[1];
			if(thisTd.innerText.trim() == nextTd.innerText.trim()){
				thisTd.setAttribute('rowspan', ++temp1);
				nextTd.style.display = 'none';
			}else{
				temp1=1;
				break;
			}
    	}
    	
    	for(var j = i+1; j < trLength; j++){
    		var thisTr = trArray[i];
    		var nextTr = trArray[j];
			var thisTd = thisTr.getElementsByTagName('td')[2];
			var nextTd = nextTr.getElementsByTagName('td')[2];
			if(thisTd.innerText.trim() == nextTd.innerText.trim()){
				thisTd.setAttribute('rowspan', ++temp2);
				nextTd.style.display = 'none';
			}else{
				temp2=1;
				break;
			}
		

    	}
    	
    	for(var j = i+1; j < trLength; j++){
    		var thisTr = trArray[i];
    		var nextTr = trArray[j];
			var thisTd = thisTr.getElementsByTagName('td')[3];
			var nextTd = nextTr.getElementsByTagName('td')[3];
			console.log(thisTd.innerText);
			console.log(nextTd.innerText);
			if(thisTd.innerText.trim() == nextTd.innerText.trim()){
				thisTd.setAttribute('rowspan', ++temp3);
				nextTd.style.display = 'none';
			}else{
				temp3=1;
				break;
			}
    	}
    	
    	for(var j = i+1; j < trLength; j++){
    		var thisTr = trArray[i];
    		var nextTr = trArray[j];
			var thisTd = thisTr.getElementsByTagName('td')[4];
			var nextTd = nextTr.getElementsByTagName('td')[4];
			if(thisTd.innerText == nextTd.innerText){
				thisTd.setAttribute('rowspan', ++temp4);
				nextTd.style.display = 'none';
			}else{
				temp4=1;
				break;
			}
    	}
    	
    }
    return;
    // 3.进行合并操作
    if (isAllSome) { // 3.1 全部列合并：只有相邻tr所指定的td都相同才会进行合并
        var lastTr = trArray[0]; // 指向第一行
        // 1)遍历grid的tr，从第二个数据行开始
        for (var i = 1, trLength = trArray.length; i < trLength; i++) {
            var thisTr = trArray[i];
            console.log(thisTr.getElementsByTagName('td').length);return;
            var isPass = true; // 是否验证通过
            // 2)遍历需要合并的列
            for (var j = 0, colArrayLength = colIndexArray.length; j < colArrayLength; j++) {
                var colIndex = colIndexArray[j];
                // 3)比较2个td的列是否匹配，若不匹配，就把last指向当前列
                if (lastTr.childNodes[colIndex].innerText != thisTr.childNodes[colIndex].innerText) {
                    lastTr = thisTr;
                    isPass = false;
                    break;
                }
            }
 
            // 4)若colIndexArray验证通过，就把当前行合并到'合并行'
            if (isPass) {
                for (var j = 0, colArrayLength = colIndexArray.length; j < colArrayLength; j++) {
                    var colIndex = colIndexArray[j];
                    // 5)设置合并行的td rowspan属性
                    if (lastTr.childNodes[colIndex].hasAttribute('rowspan')) {
                        var rowspan = lastTr.childNodes[colIndex].getAttribute('rowspan') - 0;
                        rowspan++;
                        lastTr.childNodes[colIndex].setAttribute('rowspan', rowspan);
                    } else {
                        lastTr.childNodes[colIndex].setAttribute('rowspan', '2');
                    }
                    // lastTr.childNodes[colIndex].style['text-align'] = 'center';; // 水平居中
                    lastTr.childNodes[colIndex].style['vertical-align'] = 'middle';; // 纵向居中
                    thisTr.childNodes[colIndex].style.display = 'none';
                }
            }
        }
    } else { // 3.2 逐个列合并：每个列在前面列合并的前提下可分别合并
        // 1)遍历列的序号数组
        for (var i = 0, colArrayLength = colIndexArray.length; i < colArrayLength; i++) {
            var colIndex = colIndexArray[i];
            var lastTr = trArray[0]; // 合并tr，默认为第一行数据
            // 2)遍历grid的tr，从第二个数据行开始
            for (var j = 1, trLength = trArray.length; j < trLength; j++) {
                var thisTr = trArray[j];
                // 3)2个tr的td内容一样
                if (lastTr.childNodes[colIndex].innerText == thisTr.childNodes[colIndex].innerText) {
                    // 4)若前面的td未合并，后面的td都不进行合并操作
                    if (i > 0 && thisTr.childNodes[colIndexArray[i - 1]].style.display != 'none') {
                        lastTr = thisTr;
                        continue;
                    } else {
                        // 5)符合条件合并td
                        if (lastTr.childNodes[colIndex].hasAttribute('rowspan')) {
                            var rowspan = lastTr.childNodes[colIndex].getAttribute('rowspan') - 0;
                            rowspan++;
                            lastTr.childNodes[colIndex].setAttribute('rowspan', rowspan);
                        } else {
                            lastTr.childNodes[colIndex].setAttribute('rowspan', '2');
                        }
                       // lastTr.childNodes[colIndex].style['text-align'] = 'center';; // 水平居中
                        lastTr.childNodes[colIndex].style['vertical-align'] = 'middle';; // 纵向居中
                        thisTr.childNodes[colIndex].style.display = 'none'; // 当前行隐藏
                    }
                } else {
                    // 5)2个tr的td内容不一样
                    lastTr = thisTr;
                }
            }
        }
    }
}