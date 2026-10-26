package com.overit.asseteligibility.assetfilters.numericfilters;

import com.overit.asseteligibility.assetfilters.AssetFilter;
import com.overit.asseteligibility.assetfilters.enums.NumericComparisonOperator;

import java.util.Map;
import java.util.Objects;

public final class NumericComparisonFilter implements AssetFilter {

	private final String propertyName;
	private final double referenceValue;
	private final NumericComparisonOperator operator;

	public NumericComparisonFilter(String propertyName, double referenceValue, NumericComparisonOperator operator) {

		this.propertyName = Objects.requireNonNull(propertyName, "Property name must not be null");

		this.operator = Objects.requireNonNull(operator, "Operator must not be null");

		if (!Double.isFinite(referenceValue)) {
			throw new IllegalArgumentException("Reference value must be finite");
		}

		this.referenceValue = referenceValue;
	}

	@Override
	public boolean matches(Map<String, String> asset) {
		Objects.requireNonNull(asset, "Asset must not be null");

		String value = asset.get(propertyName);

		if (value == null) {
			return false;
		}

		try {
			double actualValue = Double.parseDouble(value);

			if (!Double.isFinite(actualValue)) {
				return false;
			}

			return operator.evaluate(actualValue, referenceValue);
		} catch (NumberFormatException exception) {
			return false;
		}
	}

	@Override
	public String toString() {
		return propertyName + " " + operator + " " + referenceValue;
	}
}
