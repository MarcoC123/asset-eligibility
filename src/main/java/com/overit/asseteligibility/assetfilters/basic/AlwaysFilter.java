package com.overit.asseteligibility.assetfilters.basic;

import java.util.Map;
import java.util.Objects;

import com.overit.asseteligibility.assetfilters.AssetFilter;

public class AlwaysFilter implements AssetFilter {

	  @Override
	    public boolean matches(Map<String, String> asset) {
	        Objects.requireNonNull(asset, "Asset must not be null");
	        return true;
	    }

	    @Override
	    public String toString() {
	        return "ALWAYS";
	    }
}
