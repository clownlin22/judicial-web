//初始化grid，分页查询企业信息
Ext.define('Rds.upc.panel.StatisticsTotal', {
	extend : 'Ext.Panel',
	region:'center',
    closable:true,
    defaults:{autoScroll:true}, 
    layout:'fit',
    title:'汇总', 
    items:[{
    	html:"<iframe scrolling='auto' frameborder='0' width='100%' height='100%' src='statistics/totalinfo.do'> </iframe>"
    }],
    enableTabScroll:true  
});