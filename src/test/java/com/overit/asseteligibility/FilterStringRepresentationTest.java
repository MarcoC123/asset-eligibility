package com.overit.asseteligibility;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.overit.asseteligibility.assetfilters.Filters;
import com.overit.asseteligibility.assetfilters.enums.NumericComparisonOperator;

public class FilterStringRepresentationTest {

	@Test
	void filtersHaveMeaningfulStringRepresentations() {
		assertAll(() -> assertEquals("ALWAYS", Filters.always().toString()),

				() -> assertEquals("NEVER", Filters.never().toString()),

				() -> assertEquals("EXISTS(batteryLevel)", Filters.exists("batteryLevel").toString()),

				() -> assertEquals("zone EQUALS \"Lombardy-Plant-A\"",
						Filters.equalTo("zone", "Lombardy-Plant-A").toString()),

				() -> assertEquals("NOT(EXISTS(disabled))", Filters.not(Filters.exists("disabled")).toString()),

				() -> assertEquals("OR(EXISTS(isOnline), EXISTS(emergencyMode))",
						Filters.or(Filters.exists("isOnline"), Filters.exists("emergencyMode")).toString()),

				() -> assertEquals("AND(EXISTS(zone), EXISTS(batteryLevel))",
						Filters.and(Filters.exists("zone"), Filters.exists("batteryLevel")).toString()),

				() -> assertEquals("batteryLevel GREATER_THAN_OR_EQUAL 20.0",
						Filters.numeric("batteryLevel", 20, NumericComparisonOperator.GREATER_THAN_OR_EQUAL)
								.toString()),

				() -> assertEquals("currentTemperature LESS_THAN_OR_EQUAL " + "maxAllowedTemperature",
						Filters.compareNumericProperties("currentTemperature", "maxAllowedTemperature",
								NumericComparisonOperator.LESS_THAN_OR_EQUAL).toString()));
	}

	@Test
	void printFilterStringRepresentations() {
		System.out.println(Filters.always());
		System.out.println(Filters.never());
		System.out.println(Filters.exists("batteryLevel"));

		System.out.println(Filters.equalTo("zone", "Lombardy-Plant-A"));

		System.out.println(Filters.not(Filters.exists("disabled")));

		System.out.println(Filters.or(Filters.exists("isOnline"), Filters.exists("emergencyMode")));

		System.out.println(Filters.and(Filters.exists("zone"), Filters.exists("batteryLevel")));

		System.out.println(Filters.numeric("batteryLevel", 20, NumericComparisonOperator.GREATER_THAN_OR_EQUAL));

		System.out.println(Filters.compareNumericProperties("currentTemperature", "maxAllowedTemperature",
				NumericComparisonOperator.LESS_THAN_OR_EQUAL));
	}
}
