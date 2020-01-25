package com.hotmail.idiotonastic.plugins.TrulyDynamicShops.Utils;

import org.bukkit.inventory.ItemStack;

public class IngreedientMod {
	ItemStack items;
	 double mod;

    public IngreedientMod(ItemStack items, double mod)
    {
        this.items = items;
        this.mod = mod;
    }
    public ItemStack getItem(){
    	return items;
    }
    public void setItemAmount(int x){
    	this.items.setAmount(x);
    }
    public double getMod(){
    	return mod;
    }
    public boolean compare(IngreedientMod o){
    	if (this.items.isSimilar(o.getItem())){
    		if(this.items.getAmount() == o.getItem().getAmount()){
    			if(this.mod == o.getMod()){
    				return true;
    			}
    		}
    	}
		return false;
    	
    }
}
