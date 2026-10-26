package com.overit.asseteligibility.assetfilters.basic;

import java.util.Map;
import java.util.Objects;

import com.overit.asseteligibility.assetfilters.AssetFilter;

public class PropertyExistsFilter implements AssetFilter {

	  private final String propertyName;

	    public PropertyExistsFilter(String propertyName) {
	        this.propertyName = Objects.requireNonNull(propertyName,"Property name must not be null");
	    }

	    @Override
	    public boolean matches(Map<String, String> asset) {
	    	Objects.requireNonNull(asset, "Asset must not be null");
	    	return asset.containsKey(propertyName);
	    }

	    @Override
	    public String toString() {
	        return "EXISTS(" + propertyName + ")";
	    }
}
