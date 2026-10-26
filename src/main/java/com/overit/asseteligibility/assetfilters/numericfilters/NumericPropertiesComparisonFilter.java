package com.overit.asseteligibility.assetfilters.numericfilters;

import com.overit.asseteligibility.assetfilters.AssetFilter;
import com.overit.asseteligibility.assetfilters.enums.NumericComparisonOperator;
import java.util.Map;
import java.util.Objects;

public class NumericPropertiesComparisonFilter implements AssetFilter {

	private final String propertyName;
	private final String referencePropertyName;
	private final NumericComparisonOperator operator;

	public NumericPropertiesComparisonFilter(String propertyName, String referencePropertyName,  NumericComparisonOperator operator) {

		this.propertyName = Objects.requireNonNull(propertyName, "Property name must not be null");
		this.operator = Objects.requireNonNull(operator, "Operator must not be null");
		this.referencePropertyName = Objects.requireNonNull(referencePropertyName,"Reference property name must not be null");
	}

	@Override
	public boolean matches(Map<String, String> asset) {
		Objects.requireNonNull(asset, "Asset must not be null");

		String actualValue = asset.get(propertyName);
		String referenceValue = asset.get(referencePropertyName);

		if (actualValue == null || referenceValue == null) {
			return false;
		}

		try {
			double actualNumber = Double.parseDouble(actualValue);
			double referenceNumber = Double.parseDouble(referenceValue);

			if (!Double.isFinite(actualNumber) || !Double.isFinite(referenceNumber)) {
				return false;
			}

			return operator.evaluate(actualNumber, referenceNumber);
		} catch (NumberFormatException exception) {
			return false;
		}
	}

	@Override
	public String toString() {
		return propertyName + " " + operator + " " + referencePropertyName;
	}
}
