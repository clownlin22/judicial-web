/**
 * 多文件上传组件 
 * for extjs4.0 + struts2.0 + swfupload
 * @author fixed by houniaofei
 * @since 2013-03-15
 */
Ext.define('Ext.ux.uploadPanel.UploadPanel',{
	extend : 'Ext.grid.Panel',
	alias : 'widget.uploadpanel',
//	width : 750,
//	height : 300,
	columns : [
        {xtype: 'rownumberer'},
		{text: '文件名', width: 100,dataIndex: 'wjm'},
		//{text: '自定义文件名', width: 130,dataIndex: 'fileName',editor: {xtype: 'textfield'}},
        //{text: '类型', width: 70,dataIndex: 'type'},
//        {text: '大小', width: 70,dataIndex: 'size',renderer:function(v){
//        	return Ext.util.Format.fileSize(v);
//        }},
        {text: '进度', width: 130,dataIndex: 'percent',renderer:function(v){        	
			var stml =
				'<div>'+
					'<div style="border:1px solid #008000;height:10px;width:115px;margin:2px 0px 1px 0px;float:left;">'+		
						'<div style="float:left;background:green;width:'+v+'%;height:8px;"><div></div></div>'+
					'</div>'+
				//'<div style="text-align:center;float:right;width:40px;margin:3px 0px 1px 0px;height:10px;font-size:12px;">{3}%</div>'+			
			'</div>';
			return stml;
        }},
        {text: '状态', width: 120,dataIndex: 'status',renderer:function(v){
			var status;
			if(v==-1){
				status = "等待上传";
			}else if(v==-2){
				status =  "上传中...";
			}else if(v==-3){
				status =  "<div style='color:red;'>上传失败,请稍后重试</div>";
			}else if(v==-4){
				status =  "<div><img src='uploadPanel/icons/ok.png'/></div>";
			}else if(v==-5){
				status =  "停止上传";
			}else if(v==-6){
				status =  "<div style='color:red;'>上传失败,条形码读取错误</div>";
			}else if(v==-7){
				status =  "<div style='color:red;'>上传失败,保存数据失败!</div>";
			}else if(v==-8){
				status =  "<div style='color:red;'>上传失败,无法解压文件!</div>";
			}else if(v==-9){
				status =  "<div style='color:red;'>上传失败,文件重命名失败!</div>";
			}else if(v==-10){
				status =  "<div style='color:red;'>上传失败,条形码重复上传!</div>";
			}else if(v==-11){
				status =  "<div style='color:red;'>上传失败,内外文件夹不一致!</div>";
			}else{
				status = "无上传文件";
			}
			return status;
		}},
        {
            xtype:'actioncolumn',
            header: "操作",
            width:50,
            items: [{
                icon: 'uploadPanel/icons/delete.gif',
                tooltip: 'Remove',
                handler: function(grid, rowIndex, colIndex) {
                	var id = grid.store.getAt(rowIndex).get('id');
                    grid.store.remove(grid.store.getAt(rowIndex));
                }
            }]
        },
        { text: '条形码', dataIndex: 'txm',width:'10%'},
        { text: '工程名称', dataIndex: 'gcmc',width:'18%'},
        { text: '检验费用', dataIndex: 'jyfy',width:'10%'},
        { text: '上传时间', dataIndex: 'scsj'},
        { text: '上传文件名', dataIndex: 'wjm',width:'10%'}
        
    ],
    viewConfig : {
            getRowClass: function(record, rowIndex, rowParams, store){
                var txm = record.get("txm");
                var ztlx = record.get('ztlx');
                var cls = 'white-row';
                if(ztlx=='2'){
                	cls = 'green-row';
                }else if(ztlx=='8'){
                	cls = 'yellow-row';
                }
                return cls;
            },
            listeners: {
                render: function (view) {
                    view.tip = Ext.create('Ext.tip.ToolTip', {
                        target: view.el,
                        delegate: view.itemSelector,
                        trackMouse: true,
                        listeners: {
                            beforeshow: function (tip) {
                                var record = view.getRecord(tip.triggerElement);
                                var lcbs = record.get("lcbs");
                                var shly = record.get("shly");
                                if(lcbs=='8'){
                                    tip.update("<div style='width:300px;'>" +
                                    		"<div><b>审核回退理由:</b></div>" +
                                    		shly+"<div>");
                                }else{
                                	tip.update("<div width=300></div>");
                                }
                            }
                        }
                    })
                }
            }
        },
    plugins: [
        Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1
        })
    ],    
    store : Ext.create('Ext.data.JsonStore',{
    	autoLoad : false,
    	fields : ['id','name','cwbz','jyss','jyrq','jyfq','jyfh','jywf','jysk','zlfy','zlss','zlfh','zlwf','pjmc','sl','jg','type','size','percent','status','fileName','lxdh','cwsbxh','sydw','cwysbm','applycode','codetype','declarecode','gcmc','ztlx','jccp','sqfs','dz','ssdq','jccpmc','areaname','sqfsmc','txm','wjm','zjry','fjry','sjry','fqrcode','jyfy','shly','lcbs','scsj'],
	    autoload:true,
    	proxy: {
	        type: 'jsonajax',
	        actionMethods:{read:'POST'},
	        url: basepath+'exam/querylist.do',
	        autoload:true,
	        params:{
	        	usercode:usercode
	        },
	        reader: {
	            type: 'json',
	            root:'data',
	            totalProperty:'total'
	        }
	    }
    }),
	addFileBtnText : 'Add File',
	uploadBtnText : 'Upload',
	removeBtnText : 'Remove All',
	cancelBtnText : 'Cancel',
	debug : false,
	file_size_limit : 100,//MB
	file_types : '*.*',
	file_types_description : 'All Files',
	file_upload_limit : 50,
	file_queue_limit : 0,
	post_params : {},
	upload_url : 'test.do',
	flash_url : "swfupload/swfupload.swf",
	flash9_url : "swfupload/swfupload_fp9.swf",
	initComponent : function(){	
		var upload = this;
		this.dockedItems = [{
		    xtype: 'toolbar',
		    dock: 'top',
		    items: [
		        { 
			        xtype:'button',
			        itemId: 'addFileBtn',
			        iconCls : 'add',
			        id : '_btn_for_swf_',
			        text : this.addFileBtnText
		        },{ xtype: 'tbseparator' },{
		        	xtype : 'button',
		        	itemId : 'uploadBtn',
		        	iconCls : 'up',
		        	text : this.uploadBtnText,
		        	scope : this,
		        	handler : this.onUpload
		        },{ xtype: 'tbseparator' },{
		        	xtype : 'button',
		        	itemId : 'removeBtn',
		        	iconCls : 'trash',
		        	text : this.removeBtnText,
		        	scope : this,
		        	handler : this.onRemove
		        },{ xtype: 'tbseparator' },{
		        	xtype : 'button',
		        	itemId : 'cancelBtn',
		        	iconCls : 'cancel',
		        	disabled : true,
		        	text : this.cancelBtnText,
		        	scope : this,
		        	handler : this.onCancelUpload
		        },{
              	  xtype:'button',
                  text: '修改',
                  disabled:true,
                  name:'update',
                  iconCls: 'Bulletedit',
                  handler:function(){
                	  var selects = upload.getView().getSelectionModel().getSelection();
                	  var item = selects[0];
                  	  var jccpmc = item.get("jccpmc");
                  	  var ssdq = item.get("areaname");
                  	  
                	  var win = upload.updatewindow(jccpmc,ssdq);
                  	  var form = win.down("form");
                  	  
                  	  form.getForm().loadRecord(selects[0]);
                  	  win.show();
                  }
              },{
            	  xtype:'button',
                  text: '确认提交',
                  name:'submit',
                  disabled:true,
                  iconCls: 'Applicationgo',
                  handler:function(){
                  	var selections = upload.getView().getSelectionModel().getSelection();
                  	var codes = '';
                  	var samp = "";
                  	var shbs = null;
                  	var ztlx = '4';
                  	var _ztlx = "";
                  	var fqrcode = '';
                  	if (selections.length>0) {
                  		for(var i=0;i<selections.length;i++){
                  			var item = selections[i];
                  			var txm = item.get("txm");
                  			var nztlx = item.get("ztlx");
                  			var status = item.get("status");
                  			if(status!='-4'){
                  				Ext.MessageBox.alert("提示", txm+"没有上传文件没法提交.");
                  				return;
                  			}
                  			if(samp==""){
                  				samp = nztlx;
                  			}else{
                  				if(samp != nztlx){
                  					Ext.MessageBox.alert("提示", "只能提交一种状态任务.");
                  					return;
                  				}
                  			}
                  			if(nztlx=='8'){
                  				_ztlx += '8';
                  				if(_ztlx.indexOf("88")>=0){
                  					Ext.MessageBox.alert("提示", "每次只能提交一个回退的任务!");
                      				return;
                  				}
                  				ztlx = '5';
                  				shbs = '1';
                  				fqrcode = item.get("fqrcode");
                  			}
                  			
                  			if(txm==null||txm==""){
                  				Ext.MessageBox.alert("提示", "确认所有选择记录已经上传资料.");
                  				return;
                  			}
                  			codes = codes +item.get("declarecode");
                  			if(i!=(selections.length-1)){
                  				codes = codes+",";
                  			}
                  		}
                  		Ext.MessageBox.confirm('提示', '您确定要提交一下记录到下一流程吗?<br/>'+codes, function (opt) {
                            if (opt == 'yes') {
	                    		Ext.Ajax.request({  
	                        	       url: basepath+"exam/updatesure.do",  
	                        	       method: "POST",
	                        	       headers: { 'Content-Type': 'application/json' },
	                        	       jsonData: { 
	                        	    	   "ids":codes,
	                        	    	   'ztlx':ztlx,
	                        	    	   'shbs' : shbs,
                             	    	   'fqrcode':usercode,
                             	    	   'jsrcode':fqrcode
	                        	       },
	
	                        	       success: function (response, option) {  
	                        	    	   response = Ext.JSON.decode(response.responseText);  
	                        	    	   if (response.result == true) {  
	                        	    		   Ext.Msg.alert("提示", response.msg);
	                        	    		   upload.getStore().reload();
	                        	    	   }else { 
	                        	    		   Ext.MessageBox.alert("错误信息", "新增"+response.msg);
	                        	    	   } 
	                        	       },  
	                        	       failure: function () { 
	                        	    	   Ext.Msg.alert("提示", "提交失败,请先刷新列表后确认提交!"); 
	                        	       }  
	                          	});
	                        }
                		});
                  	}
                  	return;
                  }
          },{
        	  xtype:'button',
              text: '刷新',
              iconCls: 'Arrowrefresh',
              handler:function(){
            	  upload.getStore().reload();
              }
          },'->',{
              xtype: 'label',
              html: '<span style="color:#99CC99"><img src="uploadPanel/icons/ok.png"/>上传成功</span>、<span style="color:#FBF8BF;" tooltip="ss">回退任务</span>',
              margin: '5 0 0 10'
          }]
		}];
		
		this.selModel = Ext.create('Ext.selection.CheckboxModel',{
			listeners:{
				'selectionchange':function( model, record, index, eOpts ){
					var selects = upload.getView().getSelectionModel().getSelection().length;
					var tbar = upload.down("toolbar");
					var btn_update = tbar.down("button[name=update]");
					var btn_submit = tbar.down("button[name=submit]");
					if(selects==1){
						btn_update.setDisabled(false);
						btn_submit.setDisabled(false);
					}else if(selects>1){
						btn_update.setDisabled(true);
						btn_submit.setDisabled(false);
					}else if(selects<1){
						btn_update.setDisabled(true);
						btn_submit.setDisabled(true);
					}
				}
			}
		});
		
		this.callParent();
		this.down('button[itemId=addFileBtn]').on({	 
			afterrender : function(btn){
				var config = this.getSWFConfig(btn);		
				this.swfupload = new SWFUpload(config);
				Ext.get(this.swfupload.movieName).setStyle({
					position : 'absolute',
					top : 0,
					left : -2
				});	
			},
			scope : this,
			buffer:300
		});
	},
	getSWFConfig : function(btn){
		var me = this;
		var placeHolderId = Ext.id();
		var em = btn.getEl().child('em');
		if(em==null){
			em = Ext.get(btn.getId()+'-btnWrap');
		}		
		em.setStyle({
			position : 'relative',
			display : 'block'
		});
		em.createChild({
			tag : 'div',
			id : placeHolderId
		});
		return {
			debug: me.debug,
			flash_url : me.flash_url,
			flash9_url : me.flash9_url,	
			upload_url: me.upload_url || '',
			post_params: me.post_params||{savePath:'upload\\'},
			file_size_limit : (me.file_size_limit*1024),
			file_types : me.file_types,
			file_types_description : me.file_types_description,
			file_upload_limit : me.file_upload_limit,
			file_queue_limit : me.file_queue_limit,
			button_width: em.getWidth(),
			button_height: em.getHeight(),
			button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
			button_cursor: SWFUpload.CURSOR.HAND,
			button_placeholder_id: placeHolderId,
			custom_settings : {
				scope_handler : me
			},
			swfupload_preload_handler : me.swfupload_preload_handler,
			file_queue_error_handler : me.file_queue_error_handler,
			swfupload_load_failed_handler : me.swfupload_load_failed_handler,
			upload_start_handler : me.upload_start_handler,
			upload_progress_handler : me.upload_progress_handler,
			upload_error_handler : me.upload_error_handler,
			upload_success_handler : me.upload_success_handler,
			upload_complete_handler : me.upload_complete_handler,
			file_queued_handler : me.file_queued_handler/*,
			file_dialog_complete_handler : me.file_dialog_complete_handler*/
		};
	},
	swfupload_preload_handler : function(){
		if (!this.support.loading) {
			Ext.Msg.show({
				title : '提示',
				msg : '浏览器Flash Player版本太低,不能使用该上传功能！',
				width : 250,
				icon : Ext.Msg.ERROR,
				buttons :Ext.Msg.OK
			});
			return false;
		}
	},
	file_queue_error_handler : function(file, errorCode, message){
		switch(errorCode){
			case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED : msg('上传文件列表数量超限,不能选择！');
			break;
			case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT : msg('文件大小超过限制, 不能选择!');
			break;
			case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE : msg('该文件大小为0,不能选择！');
			break;
			case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE : msg('该文件类型不允许上传！');
			break;
			case -140 : msg(message);
			break;
		}
		function msg(info){
			Ext.Msg.show({
				title : '提示',
				msg : info,
				width : 250,
				icon : Ext.Msg.WARNING,
				buttons :Ext.Msg.OK
			});
		}
	},
	swfupload_load_failed_handler : function(){
		Ext.Msg.show({
			title : '提示',
			msg : 'SWFUpload加载失败！',
			width : 180,
			icon : Ext.Msg.ERROR,
			buttons :Ext.Msg.OK
		});
	},
	upload_start_handler : function(file){
		var me = this.settings.custom_settings.scope_handler;
		me.down('#cancelBtn').setDisabled(false);	
		var rec = me.store.getById(file.id);
//		this.addPostParam("newFileName",rec.get('fileName'));
		this.addPostParam("cwsbxh",rec.get('cwsbxh'));
		this.addPostParam("cwysbm",rec.get('cwysbm'));
		this.addPostParam("declarecode",rec.get('declarecode'));
		
		this.addPostParam("djzcode",usercode);
		this.addPostParam("jcclry",usercode);
		this.addPostParam("codetype",rec.get('codetype'));
		this.addPostParam("ssks",dptcode);
		this.addPostParam("dz",rec.get('dz'));
		this.addPostParam("lxdh",rec.get('lxdh'));
		this.addPostParam("djzmc",username);
		this.addPostParam("sydw",rec.get('sydw'));
		
		this.addPostParam("gcmc",rec.get('gcmc'));
		this.addPostParam("jccp",rec.get('jccp'));
		this.addPostParam("sqfs",rec.get('sqfs'));
		this.addPostParam("ssdq",rec.get('ssdq'));
		this.addPostParam("txm",rec.get('txm'));
		this.addPostParam("wjm",rec.get('wjm'));
		this.addPostParam("zjry",rec.get('zjry'));
		this.addPostParam("fjry",rec.get('fjry'));
		this.addPostParam("sjry",rec.get('sjry'));
		this.addPostParam("fqrcode",rec.get('fqrcode'));
		this.addPostParam("jyfy",rec.get('jyfy'));
		this.addPostParam("shly",rec.get('shly'));
		this.addPostParam("lcbs",rec.get('lcbs'));
		this.addPostParam("scsj",rec.get('scsj'));
		
//		this.addPostParam("jyfy",rec.get('jyfy'));
		this.addPostParam("jyss",rec.get('jyss'));
		this.addPostParam("jyfq",rec.get('jyfq'));
		this.addPostParam("jyfh",rec.get('jyfh'));
		this.addPostParam("jywf",rec.get('jywf'));
		this.addPostParam("jysk",rec.get('jysk'));
		this.addPostParam("zlfy",rec.get('zlfy'));
		this.addPostParam("zlss",rec.get('zlss'));
		this.addPostParam("zlfh",rec.get('zlfh'));
		this.addPostParam("zlwf",rec.get('zlwf'));
		this.addPostParam("pjmc",rec.get('pjmc'));
		this.addPostParam("sl",rec.get('sl'));
		this.addPostParam("jg",rec.get('jg'));
		this.addPostParam("cwbz",rec.get('cwbz'));
		this.addPostParam("jyrq",rec.get('jyrq'));
		
	},
	upload_progress_handler : function(file, bytesLoaded, bytesTotal){ 
		var me = this.settings.custom_settings.scope_handler;		
		var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
		percent = percent == 100? 99 : percent;
       	var rec = me.store.getById(file.id);
       	rec.set('percent', percent);
		rec.set('status', file.filestatus);
		rec.commit();
	},
	//每上传一个文件失败触发
	upload_error_handler : function(file, errorCode, message){   
		var me = this.settings.custom_settings.scope_handler;		
		var rec = me.store.getById(file.id);
       	rec.set('percent', 0);
		rec.set('status', file.filestatus);
		rec.commit(); 
		if (this.getStats().files_queued > 0 && this.uploadStopped == false) {
			this.startUpload();
		}else{
			me.showBtn(me,true);
		}
	},
	//每上传一个文件成功后触发
	upload_success_handler : function(file, serverData, responseReceived){
	    var me = this.settings.custom_settings.scope_handler;		
		var rec = me.store.getById(file.id); 
		var data = eval('(' + serverData + ')');
		var viewhtml = "<div><a href='"+data.url+"' target='blank'>查看</a></div>";
		rec.set('percent', 100);
		var status = file.filestatus;
		if(data.result==true){
			status = "-4";
		}else{
			status = data.msg;
		}
		rec.set('status', status);		
		rec.set('view',viewhtml);
		rec.commit();
		if (this.getStats().files_queued > 0 && this.uploadStopped == false) {
			this.startUpload();
		}else{
			me.showBtn(me,true);
		}
	},
	//上传完成之后触发
	upload_complete_handler : function(file){
		//如果调用file_queue_error_handler（）函数，则使用如下方法
		//this.queueEvent("file_queue_error_handler", [file, -140 ,"上传完成"]);
	},
	//这个事件在选定文件后触发
	file_queued_handler : function(file){ 
		var me = this.settings.custom_settings.scope_handler;
		
		var form = me.uploadwindow();
		var newvalue = file.name.replace(".zip","");
		var txm = form.down("textfield[name='txm']");
		txm.setValue(newvalue);
		var wjm = form.down("textfield[name='wjm']");
		wjm.setValue(newvalue);
		var window = Ext.create("Ext.window.Window",{
			width:560,
			height:600,
			title:'检测资料上传',
            modal: true,
            closable: false,
			layout:'border',
			buttons:[{
				text:'保存',
				handler:function(){
					var submitform = form.getForm();
                	if(submitform.isValid()==false){
                		Ext.MessageBox.alert({title:'错误提示',msg:"请填写完整所有信息再次提交!",buttons: Ext.MessageBox.OKCANCEL,icon: Ext.Msg.ERROR});
                		return;
                	}
                	var values = form.getForm().getValues();
                	values.id = file.id;
                	values.name = file.name;
                	values.fileName = file.name;
                	values.size = file.size;
                	values.type = file.type;
                	values.status = file.filestatus;
                	values.percent = 0;
                	me.store.add(values);
                	
                	window.close();
                	
				}
			}/*,{
				text:'取消',
				handler:function(){
					window.close();
				}
			}*/]
		});
		window.add(form);
		window.show();
		
	},
	onUpload : function(){
		if (this.swfupload&&this.store.getCount()>0) {
			if (this.swfupload.getStats().files_queued > 0) {
				this.showBtn(this,false);
				this.swfupload.uploadStopped = false;		
				this.swfupload.startUpload();
			}
		}
	},
	showBtn : function(me,bl){
		me.down('#addFileBtn').setDisabled(!bl);
		me.down('#uploadBtn').setDisabled(!bl);
		me.down('#removeBtn').setDisabled(!bl);
		me.down('#cancelBtn').setDisabled(bl);
		if(bl){
			me.down('actioncolumn').show();
		}else{
			me.down('actioncolumn').hide();
		}		
	},
	onRemove : function(){
		var ds = this.store;
		for(var i=0;i<ds.getCount();i++){
			var record =ds.getAt(i);
			var file_id = record.get('id');
			this.swfupload.cancelUpload(file_id,false);			
		}
		ds.removeAll();
		this.swfupload.uploadStopped = false;
	},
	onCancelUpload : function(){
		if (this.swfupload) {
			this.swfupload.uploadStopped = true;
			this.swfupload.stopUpload();
			this.showBtn(this,true);
		}
	},
	uploadwindow:function(){
		var me = this;

		var form = Ext.create("Ext.form.Panel",{
	        region:'center',
	        bodyPadding: 5,

	        fieldDefaults: {
	            labelAlign: 'left',
	            labelWidth: 90,
	            layout:"hbox",
	            anchor: '100%'
	        },

	        items: [{
                xtype: 'fieldset',
                title: '检测信息',
                //collapsible: true,
                defaults: {
                    labelWidth: 89,
                    anchor: '100%',
                    layout: {
                        type: 'hbox',
                        defaultMargins: {top: 5, right: 5, bottom: 0, left: 0}
                    }
                },
                items: [
                    {
                        xtype: 'fieldcontainer',
                        
                        items: [{
            				xtype: 'combo',
            				autoSelect : true,
            				editable:false,
            				allowBlank:false,
            				fieldLabel:'申请任务',
            				anchor: '100%',
            				name:'applycode',
            				triggerAction: 'all',
            				queryMode: 'local', 
            				selectOnTab: true,
            				store: Ext.create('Ext.data.Store',{
            				fields:['applycode','gcmc','ssdq','areaname','sqsj','dz','lxdh','sqfs','sqfsmc','ztbs','djzmc','sydw'],
                          	autoLoad:true,
                  			proxy: {
                                  type:'jsonajax',
                                  actionMethods:{read:'POST'},
                                  url: basepath+'apply/querylist.do',
                                  params:{
                                      usercode:usercode,
                                      ztbs:'3'
                                  },
                                  render:{
                                      type:'json'
                                  },
                                  writer: {
                                      type: 'json'
                                 }
                              }
                          }),
                          listeners:{
                        	  select : function( combo, records, eOpts ){
                        		  var record = records[0];
                        		  form.loadRecord(record);
                        		  form.down("combo[name=zjry]").getStore().load({params:{applycode:record.get("applycode")}});
                        		  form.down("combo[name=fjry]").getStore().load({params:{applycode:record.get("applycode")}});
                        		  form.down("combo[name=sjry]").getStore().load({params:{applycode:record.get("applycode")}});
                        		  form.down("textfield[name=dz]").setValue(record.get("dz"));
                        	  }
                          },
                          displayField:'gcmc',
                          labelWidth: 60,
                          width:250,
                          valueField:'applycode',
                          listClass: 'x-combo-list-small'
                      },{
            	            xtype: 'textfield',
            	            name: 'sydw',
            	            fieldLabel: 'sydw',
            	            hidden:true,
            	            labelWidth: 60,
                            width:250,
            	            readOnly:true
            	      },{
          	            xtype: 'textfield',
        	            name: 'djzmc',
        	            fieldLabel: 'djzmc',
        	            hidden:true,
        	            readOnly:true
        	        },{
        	            xtype: 'textfield',
        	            name: 'lxdh',
        	            fieldLabel: 'lxdh',
        	            hidden:true,
        	            readOnly:true
        	        },{
        	            xtype: 'textfield',
        	            name: 'dz',
        	            fieldLabel: 'dz',
        	            hidden:true,
        	            readOnly:true
        	        },{
        	            xtype: 'textfield',
        	            name: 'sqfs',
        	            fieldLabel: 'sqfs',
        	            hidden:true,
        	            readOnly:true
        	        },{
        	            xtype: 'textfield',
        	            name: 'declarecode',
        	            fieldLabel: '报告编号',
        	            hidden:true,
        	            readOnly:true
        	        },{
        	            xtype: 'textfield',
        	            name: 'txm',
        	            labelWidth: 60,
                        width:250,
        	            readOnly : true, 
        	            allowBlank:false,
        	            fieldLabel: '条形码',
//        	            hidden:true,
        	            value: ''
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    
                    items: [{
        	            xtype: 'textfield',
        	            name: 'wjm',
        	            readOnly : true, 
        	            hidden:true,
        	            allowBlank:false,
        	            fieldLabel: '文件名'
        	        },{
        	            xtype: 'textfield',
        	            name: 'djzcode',
        	            readOnly : true, 
        	            hidden:true,
        	            allowBlank:false,
        	            value :usercode,
        	            fieldLabel: '登记者'
        	        },{
        	            xtype: 'textfield',
        	            name: 'gcmc',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:505,
        	            fieldLabel: '工程名称',
        	            value: ''
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        				xtype: 'combo',
        				autoSelect : true,
        				editable:false,
        				allowBlank:false,
        				fieldLabel:'所属地区',
        				labelWidth: 60,
                        width:250,
        				anchor: '100%',
        				name:'ssdq',
        				triggerAction: 'all',
        				queryMode: 'local', 
        				selectOnTab: true,
        				store: Ext.create('Ext.data.Store',{
                      	fields:['areacode','areaname'],
                      	autoLoad:true,
              			proxy: {
                              type:'jsonajax',
                              actionMethods:{read:'POST'},
                              url:basepath+'upcarea/querylist.do',
                              params:{
                                  
                              },
                              render:{
                                  type:'json'
                              },
                              writer: {
                                  type: 'json'
                             }
                          }
                      }),
                      displayField:'areaname',
                      valueField:'areacode',
                      listClass: 'x-combo-list-small'
                  },{
      				xtype: 'combo',
//                    typeAhead: true,
                    editable:false,
                    autoSelect : true,
                    allowBlank:false,
                    fieldLabel:'实检人员',
                    name:'sjry',
                    labelWidth: 60,
                    width:250,
                    triggerAction: 'all',
                    queryMode: 'local', 
                    selectOnTab: true,
                    store: Ext.create('Ext.data.Store',{
                        fields:['usercode','username'],
//                        autoLoad:true,
                        proxy:{
                            type:'jsonajax',
                            actionMethods:{read:'POST'},
                            url:basepath+'exam/queryexamuserforallot.do',
                            params:{
//                                code:'1',
//                                dptcode:dptcode
                            },
                            render:{
                                type:'json'
                            },
                            writer: {
                                type: 'json'
                           }
                        }
                    }),
                    displayField:'username',
                    valueField:'usercode',
                    listClass: 'x-combo-list-small'
                }]
                },{
                    xtype: 'fieldcontainer',
                    
                    items: [{
        				xtype: 'combo',
//                        typeAhead: true,
                        autoSelect : true,
                        editable:false,
                        fieldLabel:'主检人员',
                        labelWidth: 60,
                        width:250,
                        allowBlank:false,
                        name:'zjry',
                        triggerAction: 'all',
                        queryMode: 'local', 
                        selectOnTab: true,
                        store: Ext.create('Ext.data.Store',{
                            fields:['usercode','username'],
//                            autoLoad:true,
                            proxy:{
                                type:'jsonajax',
                                actionMethods:{read:'POST'},
                                url:basepath+'exam/queryexamuserforallot.do',
                                params:{
//                                    code:'1',
//                                    dptcode:dptcode
                                },
                                render:{
                                    type:'json'
                                },
                                writer: {
                                    type: 'json'
                               }
                            }
                        }),
                        displayField:'username',
                        valueField:'usercode',
                        listClass: 'x-combo-list-small'
                    },{
        				xtype: 'combo',
//                        typeAhead: true,
                        autoSelect : true,
                        editable:false,
                        allowBlank:false,
                        fieldLabel:'辅检人员',
                        name:'fjry',
                        labelWidth: 60,
                        width:250,
                        triggerAction: 'all',
                        queryMode: 'local', 
                        selectOnTab: true,
                        store: Ext.create('Ext.data.Store',{
                            fields:['usercode','username'],
//                            autoLoad:true,
                            proxy:{
                                type:'jsonajax',
                                actionMethods:{read:'POST'},
                                url:basepath+'exam/queryexamuserforallot.do',
                                params:{
//                                    code:'1',
//                                    dptcode:dptcode
                                },
                                render:{
                                    type:'json'
                                },
                                writer: {
                                    type: 'json'
                               }
                            }
                        }),
                        displayField:'username',
                        valueField:'usercode',
                        listClass: 'x-combo-list-small'
                    }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        				xtype: 'combo',
        				autoSelect : true,
        				fieldLabel:'检验产品',
        				editable:false,
        				allowBlank:false,
        				anchor: '100%',
        				labelWidth: 60,
                        width:250,
        				name:'jccp',
        				triggerAction: 'all',
        				queryMode: 'local', 
        				selectOnTab: true,
        				store: Ext.create('Ext.data.Store',{
        					fields:['typecode','typename','codetype'],
        					autoLoad:true,
        					proxy:{
        						type:'jsonajax',
        						actionMethods:{read:'POST'},
        						url:basepath+'reportclass/querylist.do',
        						params:{
        							lx:'1'
        						},
        						render:{
        							type:'json'
        						},
        						writer: {
        							type: 'json'
        						}
        					}
        				}),
        				displayField:'typename',
        				valueField:'typecode',
        				listClass: 'x-combo-list-small',
        				listeners:{
                        	'select' : function( combo, records, eOpts ){
                        		var record = records[0];
                        		var codetype = form.down("textfield[name=codetype]");
                        		codetype.setValue(record.get("codetype"));
                        		var cmp = Ext.getCmp("test");
                        		if(cmp){
                        			cmp.setDisabled( true);
                        			form.remove("test",true);
                        			form.doLayout( ); 
                        		}
                        		var fieldset = null;
                        		var RawValue = combo.getRawValue();
                        		if(RawValue.indexOf("防坠安全器检验")>-1){
                        			fieldset = me.formFZQ();
                        		}else{
                        			var dq = form.down("combo[name=ssdq]").getRawValue();
                        			
                        			if(dq == '阜阳'){
                        				fieldset = me.formZCForFY();
                        			}else{
                        				fieldset = me.formZC();
                        			}
                        		}
                        		form.add(fieldset);
                        	}
                        }
                  },{
                	  xtype:"datefield",
	                  	format:'Y-m-d',
	                  	fieldLabel: '检验日期',
	                  	allowBlank:false,
	                  	labelWidth: 60,
	                    width:250,
	                  	fieldStyle: me.fieldStyle,
	                  	name: 'jyrq'
    	        },{
                	  xtype:"textfield",
                	  hidden:true,
                	  name:'codetype'
                  },{
                	  xtype:"textfield",
                	  hidden:true,
                	  name:'ssks',
                	  value:dptcode
                  }]
                }]
            }]
	    });
		return form;
	},
	
	formZC:function(){
		var zc  = {
                xtype: 'fieldset',
                id:'test',
                title: '财务信息',
                //collapsible: true,
                defaults: {
                    labelWidth: 89,
                    anchor: '100%',
                    layout: {
                        type: 'hbox',
                        defaultMargins: {top: 5, right: 5, bottom: 0, left: 0}
                    }
                },
                items: [{
                    xtype: 'fieldcontainer',
                    
                    items: [{
        	            xtype: 'textfield',
        	            name: 'cwysbm',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '原始编码'
        	        },{
        	            xtype: 'textfield',
        	            name: 'cwsbxh',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '设备型号'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'jyfy',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '检验费用'
        	        },{
        	            xtype: 'textfield',
        	            name: 'jyss',
        	            labelWidth: 60,
                        width:250,
                        allowBlank:false,
        	            fieldLabel: '实收'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'jyfq',
        	            labelWidth: 60,
                        width:250,
                        allowBlank:false,
        	            fieldLabel: '附墙'
        	        },{
        	            xtype: 'textfield',
        	            name: 'jyfh',
        	            labelWidth: 60,
                        width:250,
                        allowBlank:false,
        	            fieldLabel: '返还状态'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'jywf',
        	            labelWidth: 60,
                        width:250,
                        allowBlank:false,
        	            fieldLabel: '未付'
        	        },{
        	            xtype: 'textfield',
        	            name: 'jysk',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '税款'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'cwbz',
        	            labelWidth: 60,
                        width:505,
        	            fieldLabel: '备注'
        	        }]
                }]
            };
		
		return zc;
	},
	
	formZCForFY:function(){
		var fy = {
                xtype: 'fieldset',
                id:'test',
                title: '财务信息',
                //collapsible: true,
                defaults: {
                    labelWidth: 89,
                    anchor: '100%',
                    layout: {
                        type: 'hbox',
                        defaultMargins: {top: 5, right: 5, bottom: 0, left: 0}
                    }
                },
                items: [{
                    xtype: 'fieldcontainer',
                    
                    items: [{
        	            xtype: 'textfield',
        	            name: 'cwysbm',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '原始编码'
        	        },{
        	            xtype: 'textfield',
        	            name: 'cwsbxh',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '设备型号'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'jyfy',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '检验费用'
        	        },{
        	            xtype: 'textfield',
        	            name: 'jyss',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '检测实收'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'jyfq',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '检测附墙'
        	        },{
        	            xtype: 'textfield',
        	            name: 'jyfh',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '检测返还'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'jywf',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '检测未付'
        	        },{
        	            xtype: 'textfield',
        	            name: 'jysk',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '检测税款'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'zlfy',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '资料费用'
        	        },{
        	            xtype: 'textfield',
        	            name: 'zlss',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '资料实收'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'zlfh',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '资料已返'
        	        },{
        	            xtype: 'textfield',
        	            name: 'zlwf',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '资料未付'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'cwbz',
        	            labelWidth: 60,
                        width:505,
        	            fieldLabel: '备注'
        	        }]
                }]
            };
		return fy;
	},
	
	formFZQ:function(){
		var fzq  = {
                xtype: 'fieldset',
                id:'test',
                title: '财务信息',
                //collapsible: true,
                defaults: {
                    labelWidth: 89,
                    anchor: '100%',
                    layout: {
                        type: 'hbox',
                        defaultMargins: {top: 5, right: 5, bottom: 0, left: 0}
                    }
                },
                items: [{
                    xtype: 'fieldcontainer',
                    
                    items: [{
        	            xtype: 'textfield',
        	            name: 'cwysbm',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '原始编码'
        	        },{
        	            xtype: 'textfield',
        	            name: 'cwsbxh',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '设备型号'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'jyfy',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '检验费用'
        	        },{
        	            xtype: 'textfield',
        	            name: 'jyss',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '实收'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'pjmc',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '配件名称'
        	        },{
        	            xtype: 'textfield',
        	            name: 'sl',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:250,
        	            fieldLabel: '数量'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'jg',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '价格'
        	        },{
        	            xtype: 'textfield',
        	            name: 'jywf',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '未付款'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'jysk',
        	            labelWidth: 60,
        	            allowBlank:false,
                        width:250,
        	            fieldLabel: '税 款'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        	            xtype: 'textfield',
        	            name: 'cwbz',
        	            labelWidth: 60,
                        width:505,
        	            fieldLabel: '备注'
        	        }]
                }]
            };
		return fzq;
	},
	updatewindow:function(jccpmc,ssdq){
		var me = this;
		var window = Ext.create("Ext.window.Window",{
			width:560,
			height:600,
			title:'修改检测资料上传',
            modal: true,
            closable: true,
			layout:'border',
			buttons:[{
                xtype: 'button',
                text: '上传',
                listeners: {
                    click: function() {
                    	var submitform = form.getForm();
                    	if(submitform.isValid()==false){
                    		Ext.MessageBox.alert({title:'错误提示',msg:"请填写完整所有信息再次提交!",buttons: Ext.MessageBox.OKCANCEL,icon: Ext.Msg.ERROR});
                    		return;
                    	}
                    	form.getForm().submit({
                            url:basepath+'exam/uploadfile1.do',
                            method:'POST',
                            waitMsg: '正在上传请稍等。。。',
                            success: function(form, action){
                                var result = Ext.decode(action.response.responseText);
                                if(result.result==true){
                                	me.getStore().load();
                                	window.close();
                                }else{
                                	var v = result.msg;
                                	var status = "";
                                	if(v==-1){
                        				status = "等待上传";
                        			}else if(v==-2){
                        				status =  "上传中...";
                        			}else if(v==-3){
                        				status =  "<div style='color:red;'>上传失败,请稍后重试</div>";
                        			}else if(v==-4){
                        				status =  "<div><img src='uploadPanel/icons/ok.png'/></div>";
                        			}else if(v==-5){
                        				status =  "停止上传";
                        			}else if(v==-6){
                        				status =  "<div style='color:red;'>上传失败,条形码读取错误</div>";
                        			}else if(v==-7){
                        				status =  "<div style='color:red;'>上传失败,保存数据失败!</div>";
                        			}else if(v==-8){
                        				status =  "<div style='color:red;'>上传失败,无法解压文件!</div>";
                        			}else if(v==-9){
                        				status =  "<div style='color:red;'>上传失败,文件重命名失败!</div>";
                        			}else if(v==-10){
                        				status =  "<div style='color:red;'>上传失败,条形码重复上传!</div>";
                        			}else if(v==-11){
                        				status =  "<div style='color:red;'>上传失败,内外文件夹不一致!</div>";
                        			}else{
                        				status = "无上传文件";
                        			}
                                	Ext.MessageBox.alert('提示', status);
                                }
                            },
                            failure: function(form, action){
                                alert(action.result.errormsg);
                            }
                        });
                    	
                    }
                }
            },{
                xtype: 'button',
                text: '取消',
                listeners: {
                    click: function() {
                    	window.close();
                    }
                }
            }]
		});
		var form = Ext.create("Ext.form.Panel",{
	        region:'center',
	        bodyPadding: 5,

	        fieldDefaults: {
	            labelAlign: 'left',
	            labelWidth: 90,
	            layout:"hbox",
	            anchor: '100%'
	        },

	        items: [{
                xtype: 'fieldset',
                title: '检测信息',
                //collapsible: true,
                defaults: {
                    labelWidth: 89,
                    anchor: '100%',
                    layout: {
                        type: 'hbox',
                        defaultMargins: {top: 5, right: 5, bottom: 0, left: 0}
                    }
                },
                items: [
                    {
                        xtype: 'fieldcontainer',
                        
                        items: [{
            				xtype: 'combo',
            				autoSelect : true,
            				editable:false,
            				allowBlank:false,
            				fieldLabel:'申请任务',
            				anchor: '100%',
            				name:'applycode',
            				triggerAction: 'all',
            				queryMode: 'local', 
            				selectOnTab: true,
            				store: Ext.create('Ext.data.Store',{
            				fields:['applycode','gcmc','ssdq','areaname','sqsj','dz','lxdh','sqfs','sqfsmc','ztbs','djzmc','sydw'],
                          	autoLoad:true,
                  			proxy: {
                                  type:'jsonajax',
                                  actionMethods:{read:'POST'},
                                  url: basepath+'apply/querylist.do',
                                  params:{
                                      usercode:usercode,
                                      ztbs:'3'
                                  },
                                  render:{
                                      type:'json'
                                  },
                                  writer: {
                                      type: 'json'
                                 }
                              }
                          }),
                          listeners:{
                        	  select : function( combo, records, eOpts ){
                        		  var record = records[0];
                        		  form.loadRecord(record);
                        		  form.down("combo[name=zjry]").getStore().load({params:{applycode:record.get("applycode")}});
                        		  form.down("combo[name=fjry]").getStore().load({params:{applycode:record.get("applycode")}});
                        		  form.down("combo[name=sjry]").getStore().load({params:{applycode:record.get("applycode")}});
                        		  form.down("textfield[name=dz]").setValue(record.get("dz"));
                        	  }
                          },
                          displayField:'gcmc',
                          labelWidth: 60,
                          width:250,
                          valueField:'applycode',
                          listClass: 'x-combo-list-small'
                      },{
            	            xtype: 'textfield',
            	            name: 'sydw',
            	            fieldLabel: 'sydw',
            	            hidden:true,
            	            labelWidth: 60,
                            width:250,
            	            readOnly:true
            	      },{
          	            xtype: 'textfield',
        	            name: 'djzmc',
        	            fieldLabel: 'djzmc',
        	            hidden:true,
        	            readOnly:true
        	        },{
        	            xtype: 'textfield',
        	            name: 'lxdh',
        	            fieldLabel: 'lxdh',
        	            hidden:true,
        	            readOnly:true
        	        },{
        	            xtype: 'textfield',
        	            name: 'dz',
        	            fieldLabel: 'dz',
        	            hidden:true,
        	            readOnly:true
        	        },{
        	            xtype: 'textfield',
        	            name: 'sqfs',
        	            fieldLabel: 'sqfs',
        	            hidden:true,
        	            readOnly:true
        	        },{
        	            xtype: 'textfield',
        	            name: 'declarecode',
        	            fieldLabel: '报告编号',
        	            hidden:true,
        	            readOnly:true
        	        },{
        	            xtype: 'textfield',
        	            name: 'txm',
        	            labelWidth: 60,
                        width:250,
        	            readOnly : true, 
        	            allowBlank:false,
        	            fieldLabel: '条形码',
//        	            hidden:true,
        	            value: ''
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    
                    items: [{
        	            xtype: 'textfield',
        	            name: 'wjm',
        	            readOnly : true, 
        	            hidden:true,
        	            allowBlank:false,
        	            fieldLabel: '文件名'
        	        },{
        	            xtype: 'textfield',
        	            name: 'djzcode',
        	            readOnly : true, 
        	            hidden:true,
        	            allowBlank:false,
        	            value :usercode,
        	            fieldLabel: '登记者'
        	        },{
        	            xtype: 'textfield',
        	            name: 'gcmc',
        	            allowBlank:false,
        	            labelWidth: 60,
                        width:505,
        	            fieldLabel: '工程名称'
        	        }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        				xtype: 'combo',
        				autoSelect : true,
        				editable:false,
        				allowBlank:false,
        				fieldLabel:'所属地区',
        				labelWidth: 60,
                        width:250,
        				anchor: '100%',
        				name:'ssdq',
        				triggerAction: 'all',
        				queryMode: 'local', 
        				selectOnTab: true,
        				store: Ext.create('Ext.data.Store',{
                      	fields:['areacode','areaname'],
                      	autoLoad:true,
              			proxy: {
                              type:'jsonajax',
                              actionMethods:{read:'POST'},
                              url:basepath+'upcarea/querylist.do',
                              params:{
                                  
                              },
                              render:{
                                  type:'json'
                              },
                              writer: {
                                  type: 'json'
                             }
                          }
                      }),
                      displayField:'areaname',
                      valueField:'areacode',
                      listClass: 'x-combo-list-small'
                  },{
      				xtype: 'combo',
//                    typeAhead: true,
      				editable:false,
                    autoSelect : true,
                    allowBlank:false,
                    fieldLabel:'实检人员',
                    name:'sjry',
                    labelWidth: 60,
                    width:250,
                    triggerAction: 'all',
                    queryMode: 'local', 
                    selectOnTab: true,
                    store: Ext.create('Ext.data.Store',{
                        fields:['usercode','username'],
//                        autoLoad:true,
                        proxy:{
                            type:'jsonajax',
                            actionMethods:{read:'POST'},
                            url:basepath+'exam/queryexamuserforallot.do',
                            params:{
//                                code:'1',
//                                dptcode:dptcode
                            },
                            render:{
                                type:'json'
                            },
                            writer: {
                                type: 'json'
                           }
                        }
                    }),
                    displayField:'username',
                    valueField:'usercode',
                    listClass: 'x-combo-list-small'
                }]
                },{
                    xtype: 'fieldcontainer',
                    
                    items: [{
        				xtype: 'combo',
//                        typeAhead: true,
                        autoSelect : true,
                        editable:false,
                        fieldLabel:'主检人员',
                        labelWidth: 60,
                        width:250,
                        allowBlank:false,
                        name:'zjry',
                        triggerAction: 'all',
                        queryMode: 'local', 
                        selectOnTab: true,
                        store: Ext.create('Ext.data.Store',{
                            fields:['usercode','username'],
//                            autoLoad:true,
                            proxy:{
                                type:'jsonajax',
                                actionMethods:{read:'POST'},
                                url:basepath+'exam/queryexamuserforallot.do',
                                params:{
//                                    code:'1',
//                                    dptcode:dptcode
                                },
                                render:{
                                    type:'json'
                                },
                                writer: {
                                    type: 'json'
                               }
                            }
                        }),
                        displayField:'username',
                        valueField:'usercode',
                        listClass: 'x-combo-list-small'
                    },{
        				xtype: 'combo',
//                        typeAhead: true,
                        autoSelect : true,
                        editable:false,
                        allowBlank:false,
                        fieldLabel:'辅检人员',
                        name:'fjry',
                        labelWidth: 60,
                        width:250,
                        triggerAction: 'all',
                        queryMode: 'local', 
                        selectOnTab: true,
                        store: Ext.create('Ext.data.Store',{
                            fields:['usercode','username'],
//                            autoLoad:true,
                            proxy:{
                                type:'jsonajax',
                                actionMethods:{read:'POST'},
                                url:basepath+'exam/queryexamuserforallot.do',
                                params:{
//                                    code:'1',
//                                    dptcode:dptcode
                                },
                                render:{
                                    type:'json'
                                },
                                writer: {
                                    type: 'json'
                               }
                            }
                        }),
                        displayField:'username',
                        valueField:'usercode',
                        listClass: 'x-combo-list-small'
                    }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
        				xtype: 'combo',
        				autoSelect : true,
        				fieldLabel:'检验产品',
        				editable:false,
        				allowBlank:false,
        				anchor: '100%',
        				labelWidth: 60,
                        width:250,
        				name:'jccp',
        				triggerAction: 'all',
        				queryMode: 'local', 
        				selectOnTab: true,
        				store: Ext.create('Ext.data.Store',{
        					fields:['typecode','typename','codetype'],
        					autoLoad:true,
        					proxy:{
        						type:'jsonajax',
        						actionMethods:{read:'POST'},
        						url:basepath+'reportclass/querylist.do',
        						params:{
        							lx:'1'
        						},
        						render:{
        							type:'json'
        						},
        						writer: {
        							type: 'json'
        						}
        					}
        				}),
        				displayField:'typename',
        				valueField:'typecode',
        				listClass: 'x-combo-list-small',
        				listeners:{
                        	'select' : function( combo, records, eOpts ){
                        		var record = records[0];
                        		var codetype = form.down("textfield[name=codetype]");
                        		codetype.setValue(record.get("codetype"));
                        		var cmp = Ext.getCmp("test");
                        		if(cmp){
//                        			Ext.removeNode(cmp,true);
                        			cmp.setDisabled( true);
                        			form.remove("test",true);
                        			form.doLayout( ); 
                        		}
                        		var fieldset = null;
                        		var RawValue = combo.getRawValue();
                        		if(RawValue.indexOf("防坠安全器检验")>-1){
                        			fieldset = me.formFZQ();
                        		}else{
                        			var dq = form.down("combo[name=ssdq]").getRawValue();
                        			
                        			if(dq == '阜阳'){
                        				fieldset = me.formZCForFY();
                        			}else{
                        				fieldset = me.formZC();
                        			}
                        		}
                        		form.add(fieldset);
                        		form.doLayout();
                        	}
                        }
                  }]
                },{
                    xtype: 'fieldcontainer',
                    items: [{
      	            xtype: 'filefield',
      	            emptyText: '选择上传的文件',
      	            fieldLabel: '选择文件',
      	            allowBlank:false,
      	            id: 'filedata',
	      	          labelWidth: 60,
	                  width:250,
      	            name: 'filedata',
      	            buttonText: '选择',
      	            buttonConfig: {
      	                iconCls: 'upload-icon'
      	            },
      	            listeners:{
      	            	change : function ( field, value, eOpts ){
      	            		var index = value.lastIndexOf('\\');
      	            		var tempvalue = value.substr(index+1);
      	            		var newvalue = tempvalue.replace(".zip","");
      	            		var txm = form.down("textfield[name='txm']");
      	            		txm.setValue(newvalue);
      	            		var wjm = form.down("textfield[name='wjm']");
      	            		wjm.setValue(newvalue);
      	            	}
      	            }
      	        },{
                	  xtype:"textfield",
                	  hidden:true,
                	  name:'codetype'
                  },{
                	  xtype:"textfield",
                	  hidden:true,
                	  name:'ssks',
                	  value:dptcode
                  }]
                }]
            }]
	    });
		
		var fieldset = null;
		if(jccpmc.indexOf("防坠安全器检验")>-1){
			fieldset = me.formFZQ();
		}else{
			var dq = ssdq;//form.down("combo[name=ssdq]").getRawValue();
			
			if(dq == '阜阳'){
				fieldset = me.formZCForFY();
			}else{
				fieldset = me.formZC();
			}
		}
		form.add(fieldset);
		
		window.add(form);
		return window;
	}
});
