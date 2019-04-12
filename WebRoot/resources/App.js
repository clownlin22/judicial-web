Ext.Loader.setConfig({
    enabled: true,
    paths: {
    	"Rds.view": "resources/view",
        "Rds.upc": "resources/upc",
        "Rds.judicial": "resources/judicial",
        "Rds.narcotics": "resources/narcotics",
        "Rds.alcohol": "resources/alcohol",
        "Rds.mail" : "resources/mail",
        "Rds.appraisal": "resources/appraisal",
        "Rds.trace": "resources/trace",
        "Rds.ux": "resources/ux",
        "Rds.children": "resources/children",
        "Rds.crm":"resources/crm",
        "Rds.statistic":"resources/statistic",
        "Rds.bacera": "resources/bacera",
        "Rds.finance": "resources/finance"
    }
});
Ext.require(['Rds.view.ViewPart']);
Ext.onReady(function() {
	Ext.tip.QuickTipManager.init();
    var proto = Ext.picker.Date.prototype, date = Ext.Date;
    proto.monthNames = date.monthNames;
    proto.dayNames = date.dayNames;
    proto.format = date.defaultFormat;

	Ext.create('Rds.view.ViewPart');
});
