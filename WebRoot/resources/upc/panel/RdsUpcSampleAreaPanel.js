/**
 * @description 采样人员管理
 * @author yxb
 * @date 20150420
 */

Ext.define('Rds.upc.panel.RdsUpcSampleAreaPanel', {
	extend : 'Ext.grid.Panel',
	loadMask: true,
	viewConfig: {
        trackOver: false,
        stripeRows: false
    },
    pageSize:25,
    //初始化
	initComponent : function() {
		var me = this;
		var check = new Map();
		check.put('0','是');   
		check.put('1','否');  
		var userid = new Ext.form.ComboBox({
			fieldLabel : '员工',
			labelWidth : 40,
			name : 'userid',
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
			columnWidth : 1,
			typeAhead : false,
			forceSelection: true,   
			hideTrigger : true,
			minChars : 2,
			matchFieldWidth : true,
			listConfig : {
				loadingText : '正在查找...',
				emptyText : '没有找到匹配的数据'
				}
	    });
		var areaname =Ext.create('Ext.form.field.Text',{
			fieldLabel : '地区名称',
			labelWidth : 60,
	        maxLength :50,
			name : 'areaname',
	    });
		var usercode =Ext.create('Ext.form.field.Text',{
			fieldLabel : '用户名',
			labelWidth : 55,
	        maxLength :50,
			name : 'usercode',
	    });
		me.store = Ext.create('Ext.data.Store',{
			fields : ['area_id','usercode','areacode','username','areaname','address','remark','attach_need','achieve','userid','charge_amount'],// 定义字段
			proxy: {
                type: 'jsonajax',
                actionMethods:{read:'POST'},
                url : 'judicial/area/getUpcUserInfo.do',
                params:{
                },
                reader: {
        			type : 'json',
        			root : 'items',
        			totalProperty : 'count'
                }
            },
            listeners:{
            	'beforeload':function(ds, operation, opt){
        			Ext.apply(me.store.proxy.params, {
        						userid:trim(userid.getValue()),
        						areaname:trim(areaname.getValue()),
        						usercode:trim(usercode.getValue())});
        		}
            }
		});
		me.selModel = Ext.create('Ext.selection.CheckboxModel',{
//			mode: 'SINGLE'
		});
		me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store,
            pageSize:me.pageSize,
            displayInfo: true,
            displayMsg : "第 {0} - {1} 条  共 {2} 条",
	   	 	emptyMsg : "没有符合条件的记录"
        });
		me.columns = [Ext.create("Ext.grid.RowNumberer",
				  {text: '序号', menuDisabled:true,width:40}),
				  {
						header : "ID",
						dataIndex : 'area_id',
						flex : 0,
						width:0,
						menuDisabled : true
					},
					{
						header : "userid",
						dataIndex : 'userid',
						flex : 0,
						width:0,
						menuDisabled : true
					},
		           {
						header : "用户名",
						dataIndex : 'usercode',
						width:80,
						menuDisabled : true
					},
					{
						header : "姓名",
						dataIndex : 'username',
						width:80,
						menuDisabled : true
					}, {
						header : "采样地区",
						dataIndex : 'areaname',
						width:220,
						menuDisabled : true
					},{
						header : "预支额度",
						dataIndex : 'charge_amount',
						width:80,
						menuDisabled : true
					},{
						header : "是否需要副本",
						dataIndex : 'attach_need',
						width:100,
						menuDisabled : true,
						renderer:function(value){
		            		  return check.get(value)
		            	  }
					}, {
						header : "收件人",
						dataIndex : 'achieve',
						width:80,
						menuDisabled : true
					},{
						header : "地址",
						dataIndex : 'address',
						width:200,
						menuDisabled : true
					},{
						header : "备注",
						dataIndex : 'remark',
						width:300,
						menuDisabled : true
					}
	              ];
		me.dockedItems = [{
	 		xtype:'toolbar',
	 		name:'search',
	 		dock:'top',
	 		items:[userid,areaname,usercode,{
            	text:'查询',
            	iconCls:'Find',
            	handler:me.onSearch
            }]
	 	},{
	 		xtype:'toolbar',
	 		dock:'top', 		
	 		items:[
	 		      { xtype: 'button',text : '新增',iconCls : 'Pageadd', handler:me.onInsert},
			        { xtype: 'button',text : '修改',iconCls : 'Pageedit',handler:me.onUpdate},
			        { xtype: 'button',text : '删除',iconCls : 'Delete', handler:me.onDelete}
	 		       ]
	 	}];
		me.store.load();
		me.callParent(arguments);
	},
	onInsert:function(){
		var me = this.up("gridpanel");
		areacodeTemp = "";
		personTemp  = "";
		var form = Ext.create("Rds.upc.panel.RdsUpcSampleAreaForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'采样人员—新增',
			width:400,
			iconCls:'Add',
			height:250,
			modal:true,
			layout:'border',
			items:[form]
		});
		win.show();
		
	},
	onUpdate:function(){
		var me = this.up("gridpanel");
		var selections =  me.getView().getSelectionModel().getSelection();
		if(selections.length<1){
			Ext.Msg.alert("提示", "请选择需要修改的记录!");
			return;
		};
		areacodeTemp = selections[0].get("areacode");
		personTemp  = selections[0].get("userid");
		var form = Ext.create("Rds.upc.panel.RdsUpcSampleAreaForm",{
			region:"center",
			grid:me
		});
		var win = Ext.create("Ext.window.Window",{
			title:'采样人员—修改',
			width:400,
			iconCls:'Pageedit',
			height:250,
			modal:true,
			layout:'border',	
			items:[form]
		});
		win.show();
		form.loadRecord(selections[0]);
	},
	onDelete:function(){

	},
	onSearch:function(){
		var me = this.up("gridpanel");
		me.store.currentPage = 1;
		me.getStore().load();
	}
	
});
