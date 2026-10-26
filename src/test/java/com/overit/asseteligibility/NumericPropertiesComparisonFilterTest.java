package com.overit.asseteligibility;

import com.overit.asseteligibility.assetfilters.AssetFilter;
import com.overit.asseteligibility.assetfilters.Filters;
import com.overit.asseteligibility.assetfilters.enums.NumericComparisonOperator;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumericPropertiesComparisonFilterTest {
	@Test
	void matchesWhenFirstPropertyIsLessThanSecondProperty() {
		Map<String, String> asset = Map.of("currentTemperature", "42.5", "maxAllowedTemperature", "80");

		AssetFilter filter = Filters.compareNumericProperties("currentTemperature", "maxAllowedTemperature",NumericComparisonOperator.LESS_THAN);

		assertTrue(filter.matches(asset));
	}

	@Test
	void matchesWhenPropertiesAreNumericallyEqual() {
		Map<String, String> asset = Map.of("currentTemperature", "80.0", "maxAllowedTemperature", "80");

		AssetFilter filter = Filters.compareNumericProperties("currentTemperature", "maxAllowedTemperature",NumericComparisonOperator.EQUAL);

		assertTrue(filter.matches(asset));
	}

	@Test
	void doesNotMatchWhenComparisonFails() {
		Map<String, String> asset = Map.of("currentTemperature", "95", "maxAllowedTemperature", "80");

		AssetFilter filter = Filters.compareNumericProperties("currentTemperature","maxAllowedTemperature", NumericComparisonOperator.LESS_THAN_OR_EQUAL);

		assertFalse(filter.matches(asset));
	}

	@Test
	void doesNotMatchWhenFirstPropertyIsMissing() {
		Map<String, String> asset = Map.of("maxAllowedTemperature", "80");

		AssetFilter filter = Filters.compareNumericProperties("currentTemperature", "maxAllowedTemperature",
				NumericComparisonOperator.LESS_THAN);

		assertFalse(filter.matches(asset));
	}

	@Test
	void doesNotMatchWhenReferencePropertyIsMissing() {
		Map<String, String> asset = Map.of("currentTemperature", "42.5");

		AssetFilter filter = Filters.compareNumericProperties("currentTemperature", "maxAllowedTemperature",
				NumericComparisonOperator.LESS_THAN);

		assertFalse(filter.matches(asset));
	}

	@Test
	void doesNotMatchWhenFirstPropertyIsNotNumeric() {
		Map<String, String> asset = Map.of("currentTemperature", "unknown", "maxAllowedTemperature", "80");

		AssetFilter filter = Filters.compareNumericProperties("currentTemperature", "maxAllowedTemperature",
				NumericComparisonOperator.LESS_THAN);

		assertFalse(filter.matches(asset));
	}

	@Test
	void doesNotMatchWhenReferencePropertyIsNotNumeric() {
		Map<String, String> asset = Map.of("currentTemperature", "42.5", "maxAllowedTemperature", "unknown");

		AssetFilter filter = Filters.compareNumericProperties("currentTemperature", "maxAllowedTemperature",
				NumericComparisonOperator.LESS_THAN);

		assertFalse(filter.matches(asset));
	}

	@Test
	void propertyNamesRemainCaseSensitive() {
		Map<String, String> asset = Map.of("currentTemperature", "42.5", "maxAllowedTemperature", "80");

		AssetFilter filter = Filters.compareNumericProperties("CurrentTemperature", "maxAllowedTemperature",
				NumericComparisonOperator.LESS_THAN);

		assertFalse(filter.matches(asset));
	}

	@Test
	void doesNotMatchNonFiniteValues() {
		Map<String, String> asset = Map.of("currentTemperature", "NaN", "maxAllowedTemperature", "80");

		AssetFilter filter = Filters.compareNumericProperties("currentTemperature", "maxAllowedTemperature",
				NumericComparisonOperator.LESS_THAN);

		assertFalse(filter.matches(asset));
	}
}
