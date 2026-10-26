package com.overit.asseteligibility;

import com.overit.asseteligibility.assetfilters.AssetFilter;
import com.overit.asseteligibility.assetfilters.Filters;
import com.overit.asseteligibility.assetfilters.enums.NumericComparisonOperator;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumericComparisonFilterTest {
	
	@Test
	void matchesGreaterThanOrEqual() {
		Map<String, String> asset = Map.of("batteryLevel", "85");

		AssetFilter filter = Filters.numeric("batteryLevel", 20, NumericComparisonOperator.GREATER_THAN_OR_EQUAL);

		assertTrue(filter.matches(asset));
	}

	@Test
	void doesNotMatchWhenValueIsBelowThreshold() {
		Map<String, String> asset = Map.of("batteryLevel", "10");

		AssetFilter filter = Filters.numeric("batteryLevel", 20, NumericComparisonOperator.GREATER_THAN_OR_EQUAL);

		assertFalse(filter.matches(asset));
	}

	@Test
	void supportsDecimalValues() {
		Map<String, String> asset = Map.of("temperature", "42.5");

		AssetFilter filter = Filters.numeric("temperature",  50, NumericComparisonOperator.LESS_THAN);

		assertTrue(filter.matches(asset));
	}

	@Test
	void comparesEquivalentNumericRepresentations() {
		Map<String, String> asset = Map.of("batteryLevel", "20.0");

		AssetFilter filter = Filters.numeric("batteryLevel", 20, NumericComparisonOperator.EQUAL);

		assertTrue(filter.matches(asset));
	}

	@Test
	void doesNotMatchMissingProperty() {
		Map<String, String> asset = Map.of();

		AssetFilter filter = Filters.numeric("batteryLevel", 20, NumericComparisonOperator.GREATER_THAN);

		assertFalse(filter.matches(asset));
	}

	@Test
	void doesNotMatchMalformedNumber() {
		Map<String, String> asset = Map.of("batteryLevel", "unknown");

		AssetFilter filter = Filters.numeric("batteryLevel", 20, NumericComparisonOperator.GREATER_THAN);

		assertFalse(filter.matches(asset));
	}

	@Test
	void doesNotMatchNonFiniteNumber() {
		Map<String, String> asset = Map.of("batteryLevel", "NaN");

		AssetFilter filter = Filters.numeric("batteryLevel", 20, NumericComparisonOperator.GREATER_THAN);

		assertFalse(filter.matches(asset));
	}
}
