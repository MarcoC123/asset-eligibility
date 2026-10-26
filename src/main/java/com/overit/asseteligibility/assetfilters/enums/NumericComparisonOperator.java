package com.overit.asseteligibility.assetfilters.enums;

public enum NumericComparisonOperator {

	EQUAL,
	GREATER_THAN, 
	GREATER_THAN_OR_EQUAL, 
	LESS_THAN, 
	LESS_THAN_OR_EQUAL;

	 public boolean evaluate(double actualValue, double referenceValue) {
		return switch (this) {
		case EQUAL ->actualValue == referenceValue;
		 
		case GREATER_THAN -> actualValue > referenceValue;

		case GREATER_THAN_OR_EQUAL -> actualValue >= referenceValue;

		case LESS_THAN -> actualValue < referenceValue;

		case LESS_THAN_OR_EQUAL -> actualValue <= referenceValue;
		};
	}
}
