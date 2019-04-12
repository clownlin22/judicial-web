Ext.define('Rds.statistic.panel.StatisticsAmoebaNew', {
	extend : 'Ext.Panel',
	region:'center',
    closable:true,
    defaults:{autoScroll:true}, 
    layout:'fit',
    title:'苏博核算', 
    items:[{
    	html:"<iframe scrolling='auto' frameborder='0' width='100%' height='100%' src='statistics/financeConfigNew/queryFinanceConfig.do'> </iframe>"
    }],
    enableTabScroll:true  
});