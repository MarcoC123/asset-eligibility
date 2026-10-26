package com.overit.asseteligibility.assetfilters.logical;

import java.util.Map;
import java.util.Objects;

import com.overit.asseteligibility.assetfilters.AssetFilter;

public class EqualsFilter implements AssetFilter {

	private final String propertyName;
	private final String expectedValue;

	public EqualsFilter(String propertyName, String expectedValue) {
		this.propertyName = Objects.requireNonNull(propertyName, "Property name must not be null");
		this.expectedValue = Objects.requireNonNull(expectedValue, "Expected value must not be null");
	}

	@Override
	public boolean matches(Map<String, String> asset) {
		Objects.requireNonNull(asset, "Asset must not be null");
		String actualValue = asset.get(propertyName);

		return actualValue != null && actualValue.equalsIgnoreCase(expectedValue);
	}

	@Override
	public String toString() {
		return propertyName + " EQUALS \"" + expectedValue + "\"";
	}

}
