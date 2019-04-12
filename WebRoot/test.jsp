<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link href="resources/extjs/resources/css/ext-all.css"
	rel="stylesheet" />
<link href="resources/extjs/resources/css/icon.css"
	rel="stylesheet" />	
	
	
<script type="text/javascript" src="resources/extjs/bootstrap.js"></script>
<script type="text/javascript" src="resources/extjs/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
</head>
<body>

<script type="text/javascript">
Ext.Loader.setConfig({
    enabled: true,
    paths: {
    	"Rds.view": "resources/view",
        "Rds.upc": "resources/upc",
        "Rds.judicial": "resources/judicial",
        'Ext.ux': 'UPC/ux'
    }
});
Ext.require('Rds.view.ExtjsTest');
Ext.onReady(function() {
	Ext.define('Task', {  
        extend: 'Ext.data.Model',  
        fields: [  
            {name: 'task',     type: 'string'},  
            {name: 'user',     type: 'string'},  
            {name: 'duration', type: 'string'}  
        ]  
    });  
    var store = Ext.create('Ext.data.TreeStore', {  
        model: 'Task',  
        proxy: {  
            type: 'ajax',  
            //the store will get the content from the .json file  
            url: 'treegrid.json'  
        },  
        folderSort: true  
    });  
  
    //Ext.ux.tree.TreeGrid is no longer a Ux. You can simply use a tree.TreePanel  
    var tree = Ext.create('Ext.tree.Panel', {  
        title: 'Core Team Projects',  
        width: 500,  
        height: 300,  
        renderTo: 'tree-example',//2B的官方和SV党们，这里竟然是getbody，bo你妹啊。  
        collapsible: true,  
        useArrows: true,  
        rootVisible: false,  
        store: store,  
        multiSelect: true,  
        singleExpand: true,  
        //the 'columns' property is now 'headers'  
        columns: [{  
            xtype: 'treecolumn', //this is so we know which column will show the tree  
            text: 'Task',  
            flex: 2,  
            sortable: true,  
            dataIndex: 'task'  
        },{  
            //we must use the templateheader component so we can use a custom tpl  
            xtype: 'templatecolumn',  
            text: 'Duration',  
            flex: 1,  
            sortable: true,  
            dataIndex: 'duration',  
            align: 'center',  
            //add in the custom tpl for the rows  
            tpl: Ext.create('Ext.XTemplate', '{duration:this.formatHours}', {  
                formatHours: function(v) {  
                    if (v < 1) {  
                        return Math.round(v * 60) + ' mins';  
                    } else if (Math.floor(v) !== v) {  
                        var min = v - Math.floor(v);  
                        return Math.floor(v) + 'h ' + Math.round(min * 60) + 'm';  
                    } else {  
                        return v + ' hour' + (v === 1 ? '' : 's');  
                    }  
                }  
            })  
        },{  
            text: 'Assigned To',  
            flex: 1,  
            dataIndex: 'user',  
            sortable: true  
        }]  
    });
});
</script>
<div id="tree-example"></div>
</body>
</html>