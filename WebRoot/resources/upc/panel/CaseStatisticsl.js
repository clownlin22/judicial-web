//初始化grid，分页查询企业信息
Ext.define('Rds.upc.panel.CaseStatisticsl', {
	extend : 'Ext.Panel',
	region:'center',
    closable:true,
    defaults:{autoScroll:true}, 
    layout:'fit',
    title:'流失率', 
    items:[{
    	id:'index1',
    	html:"<iframe scrolling='auto' frameborder='0' width='100%' height='100%' src='upc/statisticsl/queryallprovice.do'> </iframe>"
    }],
    enableTabScroll:true  
});