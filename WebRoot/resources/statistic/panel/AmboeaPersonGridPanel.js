var FinanceStatistic="";
Ext.define("Rds.statistic.panel.AmboeaPersonGridPanel",{
    extend:"Ext.grid.Panel",
    loadMask: true,
    viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    initComponent : function() {
        var me = this;
		var username = Ext.create('Ext.form.field.Text', {
			name : 'username',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '员工姓名'
		});
		var webchart = Ext.create('Ext.form.field.Text', {
			name : 'webchart',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '员工工号'
		});
		var usertype = Ext.create('Ext.form.field.Text', {
			name : 'usertype',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '任职岗位'
		});
		var confirm_date_start = Ext.create('Ext.ux.form.MonthField', {
			fieldLabel: '月份起', 
            width: '20%', 
			editable:false,
            labelWidth: 50, 
            name : 'confirm_date_start',
            format: 'Y-m',
			value : Ext.Date.add(new Date(), Ext.Date.Month)
		});
//		var confirm_date_start = new Ext.form.DateField({
//			name : 'confirm_date_start',
//			width:'20%',
//			fieldLabel : '确认日期从',
//			labelWidth : 70,
//			emptyText : '请选择日期',
//			format : 'Y-m-d',
//			value : Ext.Date.add(new Date(), Ext.Date.DAY,-31)
//		});
		var confirm_date_end = Ext.create('Ext.ux.form.MonthField', {
			fieldLabel: '月份起', 
            width: '20%', 
			editable:false,
            labelWidth: 50, 
            name : 'confirm_date_end',
            format: 'Y-m',
			value : Ext.Date.add(new Date(), Ext.Date.Month)
		});
//		var confirm_date_end = new Ext.form.DateField({
//			name : 'confirm_date_end',
//			width:'20%',
//			labelWidth : 20,
//			fieldLabel : ' 到 ',
//			labelAlign : 'right',
//			emptyText : '请选择日期',
//			format : 'Y-m-d',
//			value : Ext.Date.add(new Date(), Ext.Date.DAY)
//		});
		var user_dept_level = Ext.create('Ext.form.field.Text', {
			name : 'user_dept_level',
			labelWidth : 70,
			width : '20%',
			fieldLabel : '所属部门'
		});
        me.store = Ext.create('Ext.data.Store',{
            fields:['case_id','username','webchart','user_dept_level1','user_dept_level2','user_dept_level3','user_dept_level4',
                    'user_dept_level5','confirm_date','serviceIncome','sellIncome','partnerIncome','totalIncome','totalTax',
                    'totalIncomeOutTax','laborCost','laborCost_float','laborCost_fixed','materials','purchasingMaterials','consumables','externam','externalInspection',
                    'agent','manage','office','spareGold','travelExpenses','welfareFunds','advertisement','business','lease','other',
                    'otherPay','otherProcurement','otherPayAll','instrumentBuy','engine','instrumentEquipment','outboundInvestment',
                    'renovation','paySum','internalSettlement','operatingProfit','taxIncome','virtualProfit'],
            start:0,
			limit:15,
			pageSize:15,
            proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                params:{
                },
                api:{
                    read:'statistics/personAmboea/queryAmboeaPerson.do'
                },
                reader: {
                    type: 'json',
                    root:'data',
                    totalProperty:'total'
                },
                autoLoad: true//自动加载
            },listeners:{
                'beforeload':function(ds, operation, opt){
                    FinanceStatistic= "username="
                        + username.getValue().trim()
                        + "&webchart="+ webchart.getValue().trim()
                        + "&usertype="+ usertype.getValue().trim()
                        + "&user_dept_level="+user_dept_level.getValue().trim()
                        + "&confirm_date_start=" + Ext.util.Format.date(confirm_date_start.getValue(),"Y-m")+"-01"
                        + "&confirm_date_end=" + Ext.util.Format.date(confirm_date_end.getValue(),"Y-m")+"-31";
                    Ext.apply(me.store.proxy.params,
                        {
	                    	username:username.getValue().trim(),
	                    	webchart:webchart.getValue().trim(),
	                    	usertype:usertype.getValue().trim(),
                    		confirm_date_start:Ext.util.Format.date(confirm_date_start.getValue(),"Y-m")+"-01",
                    		confirm_date_end:Ext.util.Format.date(confirm_date_end.getValue(),"Y-m")+"-31",
                    		user_dept_level:user_dept_level.getValue().trim()
                        });
                }
            }
        });

        me.selModel = Ext.create('Ext.selection.CheckboxModel',{
			mode: 'SINGLE'
		});
		//分页的combobox下拉选择显示条数
	    combo = Ext.create('Ext.form.ComboBox',{
	          name: 'pagesize',
	          hiddenName: 'pagesize',
	          store: new Ext.data.ArrayStore({
	              fields: ['text', 'value'],
	              data: [['15', 15], ['30', 30],['60', 60], ['100', 100], ['500', 500], ['1000', 1000]]
	          }),
	          valueField: 'value',
	          displayField: 'text',
	          emptyText:15,
	          width: 60
	     });//若要“永久性”更改全局变量itemsPerPage，此combox要放在Ext.onReady(）;中

       //添加下拉显示条数菜单选中事件
       combo.on("select", function (comboBox) {
       var pagingToolbar=Ext.getCmp('pagingbarAmboeaPerson');
         	pagingToolbar.pageSize = parseInt(comboBox.getValue());
         	itemsPerPage = parseInt(comboBox.getValue());//更改全局变量itemsPerPage
         	me.store.pageSize = itemsPerPage;//设置store的pageSize，可以将工具栏与查询的数据同步。
         	me.store.load({  
         		params:{  
                 start:0,  
                 limit: itemsPerPage
         		}  
         	});//数据源重新加载
         	me.store.loadPage(1);//显示第一页
        });
       
		me.bbar = Ext.create('Ext.PagingToolbar', {
			id:'pagingbarAmboeaPerson',
			store: me.store,
			pageSize:me.pageSize,
			displayInfo: true,
			displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录",
	   	 	items: ['-', '每页显示',combo,'条']
		});
        me.columns = [
                      {
                          text: '阿米巴数据表（前端个人）',//这个就是合并的
                          columns: [
                                    {text: '案例归属人',dataIndex: 'username',width : 100},
                                    {text: '归属人工号',dataIndex: 'webchart',width : 100,
                                    	renderer : function(value, cellmeta,
											record, rowIndex, columnIndex,
											store) {
											return "<a href='#'>"+ record.data["webchart"]+"</a>";
									},
    									listeners:{
	    										'click':function(){ 
		    										var me = this.up("gridpanel");
		    										Ext.MessageBox.wait('正在操作','请稍后...');
		    										var selections = me.getView().getSelectionModel().getSelection();
													var showMsg="<table style='margin:20px 20px 20px 20px;' border='1' cellpadding='4' cellspacing='0' ><tr><td colspan='9' style='text-align:center;font-weight:bold;' >苏博医学阿米巴损益表1.0</td></tr>";
													showMsg += "<tr><td colspan='9' style='text-align:right'>单元：元</td></tr>";
													showMsg += "<tr><td width='200' colspan='3' rowspan='2' style='text-align:center;font-size:20px;font-weight:bold;'>项   目</td><td width='300' style='text-align:center;font-weight:bold;' colspan='2'>计划</td><td width='300' style='text-align:center;font-weight:bold;' colspan='2'>实际</td><td width='300' style='text-align:center;font-weight:bold;' colspan='2'>差异</td></tr>";
													showMsg += "<tr><td width='500' style='text-align:center;font-weight:bold;'>计划</td><td width='500' style='text-align:center;font-weight:bold;'>占销售净额</td><td width='500' style='text-align:center;font-weight:bold;'>实际</td><td width='500' style='text-align:center;font-weight:bold;'>占销售净额</td><td width='500' style='text-align:center;font-weight:bold;'>金额</td><td width='500' style='text-align:center;font-weight:bold;'>比率</td></tr>";
													showMsg += "<tr><td width='200' rowspan='6' style='text-align:center;font-weight:bold;font-size:13px;'>收<br/>入</td><td width='1300' colspan='2' style='text-align:center;font-size:13px;'>服务收入</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("serviceIncome")==0?"":selections[0].get("serviceIncome"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1300' colspan='2' style='text-align:center;font-size:13px;'>销售收入</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("sellIncome")==0?"":selections[0].get("sellIncome"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1300' colspan='2' style='text-align:center;font-size:13px;'>合作方（实收）收入</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("partnerIncome")==0?"":selections[0].get("partnerIncome"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1300' colspan='2' style='text-align:center;font-size:13px;'>收入小计（含税）</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("totalIncome")==0?"":selections[0].get("totalIncome"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1300' colspan='2' style='text-align:center;font-size:13px;'>税额合计</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("totalTax")==0?"":selections[0].get("totalTax"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1300' colspan='2' style='text-align:center;font-weight:bold;'>收入小计（不含税）</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("totalIncomeOutTax")==0?"":selections[0].get("totalIncomeOutTax"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='200' rowspan='16' style='text-align:center;font-weight:bold;font-size:13px;'>变<br/>动<br/>费<br/>用</td><td width='400' rowspan='3' style='text-align:center;font-size:13px;'>材料成本</td><td width='1000' style='text-align:center;font-size:13px;'>采购材料付款</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("purchasingMaterials")==0?"":selections[0].get("purchasingMaterials"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-size:13px;'>耗材</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("consumables")==0?"":selections[0].get("consumables"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-weight:bold;font-size:13px;'>小计</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("materials")==0?"":selections[0].get("materials"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='400' rowspan='4' style='text-align:center;font-size:13px;'>委外费用</td><td width='1000' style='text-align:center;font-size:13px;'>委外检测成本</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("externalInspection")==0?"":selections[0].get("externalInspection"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-size:13px;'>代理费</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("agent")==0?"":selections[0].get("agent"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-size:13px;'>资质费</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'></td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-weight:bold;font-size:13px;'>小计</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("externam")==0?"":selections[0].get("externam"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='400' rowspan='6' style='text-align:center;font-size:13px;'>销售费用</td><td width='1000' style='text-align:center;font-size:13px;'>广告宣传</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("advertisement")==0?"":selections[0].get("advertisement"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-size:13px;'>业务招待费</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("business")==0?"":selections[0].get("business"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-size:13px;'>其他费用</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("other")==0?"":selections[0].get("other"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-size:13px;'>其他付款</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("otherPay")==0?"":selections[0].get("otherPay"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-size:13px;'>其他采购</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("otherProcurement")==0?"":selections[0].get("otherProcurement"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-weight:bold;font-size:13px;'>小计</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+((selections[0].get("advertisement")+selections[0].get("business")+selections[0].get("other")+selections[0].get("otherPay")+selections[0].get("otherProcurement"))==0?"":(selections[0].get("advertisement")+selections[0].get("business")+selections[0].get("other")+selections[0].get("otherPay")+selections[0].get("otherProcurement")))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='400' rowspan='2' style='text-align:center;font-size:13px;color:red;'>人工成本<br/>（绩效提成）</td><td width='1000' style='text-align:center;font-size:13px;color:red;'>业绩提成（计件工资）</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("laborCost_float")==0?"":selections[0].get("laborCost_float"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-weight:bold;font-size:13px;'>小计</td><td width='500'></td><td width='500'></td><td style='text-align:center;font-size:13px;'>"+(selections[0].get("laborCost_float")==0?"":selections[0].get("laborCost_float"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1300' colspan='2' style='text-align:center;font-weight:bold;font-size:13px;'>变动费用合计</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+((selections[0].get("purchasingMaterials")+selections[0].get("consumables")+selections[0].get("externalInspection")+selections[0].get("agent")+selections[0].get("advertisement")+selections[0].get("business")+selections[0].get("other")+selections[0].get("otherPay")+selections[0].get("otherProcurement")+selections[0].get("laborCost_float"))==0?"":(selections[0].get("purchasingMaterials")+selections[0].get("consumables")+selections[0].get("externalInspection")+selections[0].get("agent")+selections[0].get("advertisement")+selections[0].get("business")+selections[0].get("other")+selections[0].get("otherPay")+selections[0].get("otherProcurement")+selections[0].get("laborCost_float")))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='1300' colspan='3' style='text-align:center;font-weight:bold;font-size:15px;'>边界利益</td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='200' rowspan='15' style='text-align:center;font-weight:bold;font-size:13px;'>固<br/>定<br/>费<br/>用</td><td width='400' rowspan='3' style='text-align:center;font-size:13px;color:red;'>人工成本<br/>(固定薪资)</td><td width='1000' style='text-align:center;font-size:13px;color:red;'>固定薪资</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("laborCost_fixed")==0?"":selections[0].get("laborCost_fixed"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-size:13px;color:red;'>福利费（报销)</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("welfareFunds")==0?"":selections[0].get("welfareFunds"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-weight:bold;font-size:13px;'>小计</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+((selections[0].get("welfareFunds")+selections[0].get("laborCost_fixed"))==0?"":(selections[0].get("welfareFunds")+selections[0].get("laborCost_fixed")))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='400' rowspan='2' style='text-align:center;font-size:13px;'>设备费</td><td width='1000' style='text-align:center;font-size:13px;'>设备折旧</td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-weight:bold;font-size:13px;'>小计</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'></td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='400' rowspan='2' style='text-align:center;font-size:13px;'>销售管理费用</td><td width='1000' style='text-align:center;font-size:13px;'>租赁费</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("lease")==0?"":selections[0].get("lease"))+"</td></td><td width='500'><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-weight:bold;font-size:13px;'>小计</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("lease")==0?"":selections[0].get("lease"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='400' rowspan='3' style='text-align:center;font-size:13px;'>其他支出</td><td width='1000' style='text-align:center;font-size:13px;'>工程</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("engine")==0?"":selections[0].get("engine"))+"</td><td width='500'><td width='500'></td></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-size:13px;'>装修付款</td><td width='500'><td width='500'></td></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("renovation")==0?"":selections[0].get("renovation"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-weight:bold;font-size:13px;'>小计</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+((selections[0].get("renovation")+selections[0].get("engine"))==0?"":(selections[0].get("renovation")+selections[0].get("engine")))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='400' rowspan='4' style='text-align:center;font-size:13px;'>办公费用</td><td width='1000' style='text-align:center;font-size:13px;'>差旅费</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("travelExpenses")==0?"":selections[0].get("travelExpenses"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-size:13px;'>办公费</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("office")==0?"":selections[0].get("office"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-size:13px;'>备用金</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+(selections[0].get("spareGold")==0?"":selections[0].get("spareGold"))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1000' style='text-align:center;font-weight:bold;font-size:13px;'>小计</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+((selections[0].get("spareGold")+selections[0].get("office")+selections[0].get("travelExpenses"))==0?"":(selections[0].get("spareGold")+selections[0].get("office")+selections[0].get("travelExpenses")))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='1300' colspan='2' style='text-align:center;font-weight:bold;font-size:13px;'>固定费用合计</td><td width='500'></td><td width='500'></td><td width='500' style='text-align:center;font-size:13px;'>"+((selections[0].get("engine")+selections[0].get("renovation")+selections[0].get("lease")+selections[0].get("welfareFunds")+selections[0].get("laborCost_fixed")+selections[0].get("spareGold")+selections[0].get("office")+selections[0].get("travelExpenses"))==0?"":(selections[0].get("engine")+selections[0].get("renovation")+selections[0].get("lease")+selections[0].get("welfareFunds")+selections[0].get("laborCost_fixed")+selections[0].get("spareGold")+selections[0].get("office")+selections[0].get("travelExpenses")))+"</td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													
													showMsg += "<tr><td width='1300' colspan='3' style='text-align:center;font-weight:bold;font-size:15px;'>经营利益</td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1300' colspan='3' style='text-align:center;font-weight:bold;font-size:15px;'>投入人员数</td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg += "<tr><td width='1300' colspan='3' style='text-align:center;font-weight:bold;font-size:15px;'>人·月劳动生产力</td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td><td width='500'></td></tr>";
													showMsg+="</table>";
													
													var win=Ext.create("Ext.window.Window", {
														width : 1000,
														iconCls :'Pageadd',
														height : 700,
														modal:true,
														autoScroll:true,
														title:'案例信息',
														layout : 'border',
														maximizable :true,
														maximized:true,
														html:showMsg,
														bodyStyle : "background-color:white;font-size:15px;font-family:'微软雅黑'"
													});
													win.show();
													Ext.MessageBox.close();
	    									}  
    									} 
                                    },
                                    {text: '事业部',dataIndex: 'user_dept_level1',width : 150},
                        			{text: '二级部门',dataIndex: 'user_dept_level2',width : 150},
                        			{text: '三级部门',dataIndex: 'user_dept_level3',width : 150},
                        			{text: '四级部门',dataIndex: 'user_dept_level4',width : 150},
                        			{text: '五级部门',dataIndex: 'user_dept_level5',width : 150}
                                 ]
                      
                      },{
                          text: '收入',//这个就是合并的
                          columns: [
                                    {text: '服务收入',dataIndex: 'serviceIncome',width : 100},
                                    {text: '销售收入',dataIndex: 'sellIncome',width : 100},
                                    {text: '合作方收入',dataIndex: 'partnerIncome',width : 150},
                        			{text: '收入小计（含税）',dataIndex: 'totalIncome',width : 150},
                        			{text: '税额合计',dataIndex: 'totalTax',width : 150},
                        			{text: '收入小计（不含税）',dataIndex: 'totalIncomeOutTax',width : 150}
                                 ]
                      },{

                          text: '支出',//这个就是合并的
                          columns: [
                                    {text: '人工成本',dataIndex: 'laborCost',width : 100},
                                    {text: '材料成本',dataIndex: 'materials',width : 100},{
                                        text: '1',//这个就是合并的
                                        columns: [
                                                  {text: '采购材料付款',dataIndex: 'purchasingMaterials',width : 100},
                                               ]
                                    
                                    },{
                                        text: '2',//这个就是合并的
                                        columns: [
                                                  {text: '耗材',dataIndex: 'consumables',width : 100},
                                               ]
                                    
                                    },
                                    {text: '委外费用',dataIndex: 'externam',width : 100},
                                    {
                                        text: '1',//这个就是合并的
                                        columns: [
                                                  {text: '委外检测成本',dataIndex: 'externalInspection',width : 100},
                                               ]
                                    
                                    },{
                                        text: '2',//这个就是合并的
                                        columns: [
                                                  {text: '代理费',dataIndex: 'agent',width : 100},
                                               ]
                                    
                                    },
                                    {text: '销售管理费用',dataIndex: 'manage',width : 100},
                                    {
                                        text: '1',//这个就是合并的
                                        columns: [
                                                  {text: '办公费',dataIndex: 'office',width : 100},
                                               ]
                                    
                                    },{
                                        text: '2',//这个就是合并的
                                        columns: [
                                                  {text: '备用金',dataIndex: 'spareGold',width : 100},
                                               ]
                                    
                                    },{
                                        text: '3',//这个就是合并的
                                        columns: [
                                                  {text: '差旅费',dataIndex: 'travelExpenses',width : 100},
                                               ]
                                    
                                    },{
                                        text: '4',//这个就是合并的
                                        columns: [
                                                  {text: '福利费',dataIndex: 'welfareFunds',width : 100},
                                               ]
                                    
                                    },{
                                        text: '5',//这个就是合并的
                                        columns: [
                                                  {text: '广告宣传',dataIndex: 'advertisement',width : 100},
                                               ]
                                    
                                    },{
                                        text: '6',//这个就是合并的
                                        columns: [
                                                  {text: '业务招待费',dataIndex: 'business',width : 100},
                                               ]
                                    
                                    },{
                                        text: '7',//这个就是合并的
                                        columns: [
                                                  {text: '租赁费',dataIndex: 'lease',width : 100},
                                               ]
                                    
                                    },{
                                        text: '8',//这个就是合并的
                                        columns: [
                                                  {text: '其他费用',dataIndex: 'other',width : 100},
                                               ]
                                    
                                    },{
                                        text: '9',//这个就是合并的
                                        columns: [
                                                  {text: '其他付款',dataIndex: 'otherPay',width : 100},
                                               ]
                                    
                                    },{
                                        text: '10',//这个就是合并的
                                        columns: [
                                                  {text: '其它采购',dataIndex: 'otherProcurement',width : 100},
                                               ]
                                    
                                    },
                                    {text: '其他支出',dataIndex: 'otherPayAll',width : 100},{
                                        text: '1',//这个就是合并的
                                        columns: [
                                                  {text: '采购仪器设备付款',dataIndex: 'instrumentBuy',width : 100},
                                               ]
                                    
                                    },{
                                        text: '2',//这个就是合并的
                                        columns: [
                                                  {text: '工程',dataIndex: 'engine',width : 100},
                                               ]
                                    
                                    },{
                                        text: '3',//这个就是合并的
                                        columns: [
                                                  {text: '仪器设备',dataIndex: 'instrumentEquipment',width : 100},
                                               ]
                                    
                                    },{
                                        text: '4',//这个就是合并的
                                        columns: [
                                                  {text: '对外投资',dataIndex: 'outboundInvestment',width : 100},
                                               ]
                                    
                                    },{
                                        text: '5',//这个就是合并的
                                        columns: [
                                                  {text: '装修付款',dataIndex: 'renovation',width : 100},
                                               ]
                                    
                                    },
                                    {text: '支出费用小计',dataIndex: 'paySum',width : 100},
                                 ]
                      },
                      {text: '案例内部结算（成本）',dataIndex: 'internalSettlement',width : 150},
                      {
                          text: '运营情况',//这个就是合并的
                          columns: [
                                    {text: '运营利润额',dataIndex: 'operatingProfit',width : 100},
                                    {text: '所得税',dataIndex: 'taxIncome',width : 100},
                                    {text: '虚拟运营净利润合计',dataIndex: 'virtualProfit',width : 150}
                                 ]
                      
                      }
                 ];

        me.dockedItems = [{
        	style : {
    	        borderTopWidth : '0px !important',
    	        borderBottomWidth : '0px !important'
    	 		},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [ username,webchart,usertype,confirm_date_start,confirm_date_end]
		},{
			style : {
		        borderTopWidth : '0px !important',
		        borderBottomWidth : '0px !important'
		 		},
			xtype : 'toolbar',
			name : 'search',
			dock : 'top',
			items : [user_dept_level,{
                text:'查询',
                iconCls:'Find',
                handler:me.onSearch
            },{
                text : '导出',
                iconCls : 'Pageexcel',
                handler : me.onExport
            }]
		
		}];
        me.callParent(arguments);
    },
    onExport:function(){
        window.location.href = "statistics/personAmboea/exportAmboeaPerson.do?"+FinanceStatistic
    },
    onSearch : function() {
        var me = this.up("gridpanel");
        me.getStore().currentPage=1;
        me.getStore().load();
    },
    listeners:{
        'afterrender':function(){
            this.store.load();
        }
    }
});
