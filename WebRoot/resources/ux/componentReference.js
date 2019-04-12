Ext.AbstractComponent.prototype.onAdded = Ext.Function.createSequence(Ext.AbstractComponent.prototype.onAdded, function(){
	
    if(this.refO && this.itemId ){ 
        this.refO[this.itemId] = this; 
    }else if(this.ownerCt && this.ownerCt.refO ){
		
        if(this.itemId ){ 
            this.ownerCt.refO[this.itemId] = this; 
        }
        if( !this.refO ){ 
            this.refO = this.ownerCt.refO; 
        }
    }
});