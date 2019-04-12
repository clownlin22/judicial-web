onDownload = function(value) {
	window.location.href = "judicial/attachment/download.do?id=" + value;
}
printImg=function(case_code,code,type,path){
	var print_chanel=function(){
		win.close();
	}
	var print_print=function(me){
		Ext.Ajax
		.request({
			url : "judicial/attachment/updateAttachmentPrint.do",
			method : "POST",
			headers : {
				'Content-Type' : 'application/json'
			},
			jsonData : {
				case_code:case_code,
				case_id:code,
				type:type
			},
			success : function(
					response,
					options) {
				response = Ext.JSON
						.decode(response.responseText);
				if (response == true) {
					var iframe = document.getElementById("imgmodel");
					iframe.contentWindow.focus();
					iframe.contentWindow.print();
					win.close();
				}else{
					Ext.Msg
					.alert(
							"提示",
							"打印出错");
				}
			},
			failure : function() {
				Ext.Msg
						.alert(
								"提示",
								"保存失败<br>请联系管理员!");
			}
		});
	}
	
	var onTurnLeft = function() {
	
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
							document.getElementById("imgmodel").src='judicial/attachment/printAttachment.do?type='+type+'&case_code='+case_code+'&path='+path+'&case_id='+code+'&v="'+new Date().getTime()+'"' ;
						} else {
							Ext.Msg.alert("提示", "旋转失败，请重新旋转！");
							return;
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "获取图片失败！");
					}
				});
	}
	var onTurnRight = function() {
		
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
							document.getElementById("imgmodel").src='judicial/attachment/printAttachment.do?type='+type+'&path='+path+'&case_code='+case_code+'&v="'+new Date().getTime()+'"' ;
						} else {
							Ext.Msg.alert("提示", "旋转失败，请重新旋转！");
							return;
						}
					},
					failure : function() {
						Ext.Msg.alert("提示", "获取图片失败！");
					}
				});
	}
	
	var win=Ext.create("Ext.window.Window",{
		title : "打印预览",
		iconCls : 'Find',
		layout:"auto",
		maximized:true,
		maximizable :true,
		modal:true,
		bodyStyle : "background-color:white;",
		html:"<iframe width='100%' height='100%' id='imgmodel' src='judicial/attachment/printAttachment.do?type="+type+"&path="+path+"&case_code="+case_code+"&v="+new Date().getTime()+"'></iframe>",
		buttons:[
					{
						text : '左转',
						iconCls : 'Arrowturnleft',
						handler : onTurnLeft
					}, {
						text : '右转',
						iconCls : 'Arrowturnright',
						handler : onTurnRight
					}, {
						text : '打印',
						iconCls : 'Disk',
						handler:  print_print
					}, {
						text : '取消',
						iconCls : 'Cancel',
						handler: print_chanel
					} 
				]
			});
	win.show(); 
}



Ext.define("Rds.judicial.panel.JudicialAttachmentPrintPanel", {
	extend : "Ext.grid.Panel",
	loadMask : true,
	viewConfig : {
		trackOver : false,
		stripeRows : false
	},
	pageSize : 25,
	initComponent : function() {
		var me = this;
		var search = Ext.create('Ext.form.field.Text', {
					name : 'search',
					labelWidth : 70,
					width : 200,
					regex:/^\w*$/,
					regexText:'请输入英文或数字',
					fieldLabel : '案例条形码'
				});
		var starttime = Ext.create('Ext.form.field.Date', {
				name : 'starttime',
				// id : 'starttime_attachment_grid',
				width : 175,
				fieldLabel : '受理日期从',
				labelWidth : 70,
				labelAlign : 'right',
				emptyText : '请选择日期',
				format : 'Y-m-d',
				value : Ext.Date.add(new Date(), Ext.Date.DAY, -3),
				maxValue : new Date(),
				listeners : {
					'select' : function() {
						var start = starttime.getValue();
						endtime
								.setMinValue(start);
						var endDate = endtime
								.getValue();
						if (start > endDate) {
							endtime
									.setValue(start);
						}
					}
				}
			});
	var endtime = Ext.create('Ext.form.field.Date', {
				//id : 'endtime_attachment_grid',
				name : 'endtime',
				width : 135,
				labelWidth : 20,
				fieldLabel : '到',
				labelAlign : 'right',
				emptyText : '请选择日期',
				format : 'Y-m-d',
				maxValue : new Date(),
				value : Ext.Date.add(new Date(), Ext.Date.DAY),
				listeners : {
					select : function() {
						var start = starttime.getValue();
						var endDate = endtime
								.getValue();
						if (start > endDate) {
							starttime.setValue(endDate);
						}
					}
				}
			})
		me.store = Ext.create('Ext.data.Store', {
					fields : ['id','case_id', 'case_code','cardId', 'id_card_path','id_card_date','pic_path',"pic_print","id_card_print",
							'pic_date','reg_date','reg_path',"reg_print"],
					start:0,
					limit:15,
					pageSize:15,
					proxy : {
						type : 'jsonajax',
						actionMethods : {
							read : 'post'
						},
						url : 'judicial/attachment/getPrintAttachment.do',
						params : {},
						reader : {
							type : 'json',
							root : 'items',
							totalProperty : 'count'
						}
					},
					listeners : {
						'beforeload' : function(ds, operation, opt) {
							Ext.apply(me.store.proxy.params, {
								case_code : search.getValue(),
								starttime : dateformat(starttime
										.getValue()),
								endtime : dateformat(endtime.getValue())
									});
						}
					}
				});

		me.bbar = Ext.create('Ext.PagingToolbar', {
					store : me.store,
					pageSize : me.pageSize,
					displayInfo : true,
					displayMsg : "第 {0} - {1} 条  共 {2} 条",
					emptyMsg : "没有符合条件的记录"
				});
		me.columns = [{
					dataIndex : 'id',
					hidden : true
				}, {
					text : '案例条形码',
					dataIndex : 'case_code',
					width : 200,
					menuDisabled : true,
				}, {
					text : '照片',
					dataIndex : 'pic_path',
					width : 200,
					menuDisabled : true,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						var case_code = record.data["case_code"];
						var case_id = record.data["case_id"];
						var path=record.data["pic_path"];
						if (value == null||value=='') {
							return "<div style=\"color: red;\">尚未上传</div>"
						} else {
							return "<a href='#' onclick='printImg(\""+case_code+"\",\""+case_id+"\",\"3\",\""+path+"\")' >打印照片</a>";
						}
					}
				},{
					text : '照片下载',
					dataIndex : 'pic_path',
					width : 200,
					menuDisabled : true,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						var id = record.data["id"];
						if (value == null||value=='') {
							return "<div style=\"color: red;\">尚未上传</div>"
						} else {
							return "<a href='#' onclick=\"onDownload('" + id
							+ "')\"  >下载";
						}
					}
				},{
					text : '照片是否打印',
					dataIndex : 'pic_print',
					width : 200,
					menuDisabled : true,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						if (value == 0) {
							return "尚未打印"
						} else {
							return "<div style=\"color: green;\">已经打印</div>";
						}
					}
				}, {
					text : '照片上传日期',
					dataIndex : 'pic_date',
					width : 200,
					menuDisabled : true,
				},{
					text : '身份证',
					dataIndex : 'id_card_path',
					width : 200,
					menuDisabled : true,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						var case_code = record.data["case_code"];
						var case_id = record.data["case_id"];
						var path=record.data["id_card_path"];
						if (value == null||value=='') {
							return "<div style=\"color: red;\">尚未上传</div>"
						} else {
							return "<a href='#' onclick=\"printImg('"+case_code+"','"+case_id+"','2','"+path+"')\">打印身份证</a>";
						}
					}
				},{
					text : '身份证下载',
					dataIndex : 'cardId',
					width : 200,
					menuDisabled : true,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						var cardId = record.data["cardId"];
						if (value == null||value=='') {
							return "<div style=\"color: red;\">尚未上传</div>"
						} else {
							return "<a href='#' onclick=\"onDownload('" + cardId
							+ "')\"  >下载";
						}
					}
				
				},{
					text : '身份证是否打印',
					dataIndex : 'id_card_print',
					width : 200,
					menuDisabled : true,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						if (value == 0) {
							return "尚未打印"
						} else {
							return "<div style=\"color: green;\">已经打印</div>";
						}
					}
				},{
					text : '身份证上传日期',
					dataIndex : 'id_card_date',
					width : 200,
					menuDisabled : true,
				},{
					text : '登记表',
					dataIndex : 'reg_path',
					width : 200,
					menuDisabled : true,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						var case_code = record.data["case_code"];
						var case_id = record.data["case_id"];
						var path=record.data["reg_path"];
						if (value == null||value=='') {
							return "<div style=\"color: red;\">尚未上传</div>"
						} else {
							return "<a href='#' onclick=\"printImg('"+case_code+"','"+case_id+"','1','"+path+"')\">打印登记表</a>";
						}
					}
				},{
					text : '登记表是否打印',
					dataIndex : 'reg_print',
					width : 200,
					menuDisabled : true,
					renderer : function(value, cellmeta,
							record, rowIndex, columnIndex,
							store) {
						if (value == 0) {
							return "尚未打印"
						} else {
							return "<div style=\"color: green;\">已经打印</div>";
						}
					}
				},{
					text : '登记表上传日期',
					dataIndex : 'reg_date',
					width : 200,
					menuDisabled : true,
				}];

		me.dockedItems = [{
					xtype : 'toolbar',
					name : 'search',
					dock : 'top',
					items : [search,starttime,endtime, {
								text : '查询',
								iconCls : 'Find',
								handler : me.onSearch
							}]
				}];

		me.callParent(arguments);
	},
	onSearch : function() {
		var me = this.up("gridpanel");
		me.getStore().currentPage=1;
		me.getStore().load();
	},
	onDownload : function() {
		var me = this.up("gridpanel");
		var selections = me.getView().getSelectionModel().getSelection();
		window.location.href = "judicial/attachment/download.do?id="
				+ selections[0].get("id");

	},
	listeners : {
		'afterrender' : function() {
			this.store.load();
		}
	}
});