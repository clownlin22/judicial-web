function ObjectUtil(){
	/**
	 * join跟src是extjs model
	 */
	this.joinObject = function(join,src){
		if(src==null||join==null){
			throw "输入参数有为null";
		}
		var src_data = src.getData();
		for(var key in src_data){
			src.set(key,join.get(key)+(src.get(key)==undefined?"":src.get(key)));
		}
		return src;
	}
}