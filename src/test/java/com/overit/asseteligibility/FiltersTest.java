package com.overit.asseteligibility;

import com.overit.asseteligibility.assetfilters.Filters;
import com.overit.asseteligibility.assetfilters.enums.NumericComparisonOperator;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FiltersTest {

	private Map<String, String> sensor;

	@BeforeEach
	void setUp() {
		sensor = new HashMap<>();

		sensor.put("assetId", "SENSOR-001");
		sensor.put("assetType", "temperatureSensor");
		sensor.put("zone", "Lombardy-Plant-A");
		sensor.put("expectedZone", "Lombardy-Plant-A");
		sensor.put("isOnline", "true");

		sensor.put("batteryLevel", "85");
		sensor.put("minimumBatteryLevel", "20");

		sensor.put("currentTemperature", "42.5");
		sensor.put("maxAllowedTemperature", "80");

		sensor.put("firmwareVersion", "2.4.1");
		sensor.put("requiredFirmwareVersion", "2.4.1");
	}

	@Test
	void sensorEligibilityChangesWhenBatteryLevelDrops() {
		var eligibilityFilter = Filters.and(
				Filters.equalTo("zone", "Lombardy-Plant-A"),
				Filters.equalTo("isOnline", "true"),
				Filters.numeric("batteryLevel", 20, NumericComparisonOperator.GREATER_THAN_OR_EQUAL));

		assertTrue(eligibilityFilter.matches(sensor));

		sensor.put("batteryLevel", "10");

		assertFalse(eligibilityFilter.matches(sensor));
	}

	@Test
	void sensorMatchesNestedOperationalCriteria() {
		var eligibilityFilter = Filters.and(
				Filters.equalTo("zone", "Lombardy-Plant-A"),
				Filters.or(
						Filters.equalTo("isOnline", "true"), 
						Filters.equalTo("emergencyMode", "true")),
				Filters.not(
						Filters.equalTo("disabled", "true")), 
				Filters.compareNumericProperties("currentTemperature",
						"maxAllowedTemperature", NumericComparisonOperator.LESS_THAN_OR_EQUAL));

		assertTrue(eligibilityFilter.matches(sensor));
	}
	
	@Test
	void emergencyModeCanSatisfyOperationalConditionWhenSensorIsOffline() {
	    var eligibilityFilter = Filters.or(
	        Filters.equalTo(
	            "isOnline",
	            "true"
	        ),
	        Filters.equalTo(
	            "emergencyMode",
	            "true"
	        )
	    );

	    sensor.put("isOnline", "false");
	    sensor.put("emergencyMode", "true");

	    assertTrue(eligibilityFilter.matches(sensor));
	}
	
	@Test
	void sensorBecomesIneligibleWhenTemperatureExceedsItsLimit() {
	    var temperatureFilter =
	        Filters.compareNumericProperties(
	            "currentTemperature",
	            "maxAllowedTemperature",
	            NumericComparisonOperator.LESS_THAN_OR_EQUAL
	        );

	    assertTrue(temperatureFilter.matches(sensor));

	    sensor.put("currentTemperature", "95");

	    assertFalse(temperatureFilter.matches(sensor));
	}
}