Ext.define('Rds.ux.data.proxy.JsonAjaxProxy', {
	extend : 'Ext.data.proxy.Ajax',
	alias : 'proxy.jsonajax',

	actionMethods : {
		create : "POST",
		read : "POST",
		update : "POST",
		destroy : "POST"
	},

	buildRequest : function(operation) {
		var me = this;
		var request = this.callParent(arguments);
		// For documentation on jsonData see Ext.Ajax.request
		var s = {};
		//extend(s, me.params, request.params);
		request.jsonData = extend(request.params, me.params);
		request.headers = {
			'Content-Type' : 'application/json'
		}, request.params = {};

		return request;
	},

	// extend:function(o,n,override){
	// for(var p in n)if(n.hasOwnProperty(p) && (!o.hasOwnProperty(p) ||
	// override))o[p]=n[p];
	// },

	/*
	 * @override Inherit docs. We don't apply any encoding here because all of
	 * the direct requests go out as jsonData
	 */
	applyEncoding : function(value) {
		return value;
	}

});

function extend(o,n) {
//	for ( var p in n)
//		if (n.hasOwnProperty(p) && (!o.hasOwnProperty(p) || override))
//			o[p] = n[p];
	for ( var p in n)
		if (n.hasOwnProperty(p) && !o.hasOwnProperty(p))
			o[p] = n[p];
	return o;
}