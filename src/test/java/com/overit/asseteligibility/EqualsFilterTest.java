package com.overit.asseteligibility;

import org.junit.jupiter.api.Test;

import com.overit.asseteligibility.assetfilters.AssetFilter;
import com.overit.asseteligibility.assetfilters.Filters;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class EqualsFilterTest {

	@Test
	void matchesWhenPropertyHasExpectedValue() {
		Map<String, String> asset = Map.of("zone", "Lombardy-Plant-A");
		AssetFilter filter = Filters.equalTo("zone", "Lombardy-Plant-A");

		assertTrue(filter.matches(asset));
	}

	@Test
	void comparesValuesIgnoringCase() {
		Map<String, String> asset = Map.of("zone", "Lombardy-Plant-A");
		AssetFilter filter = Filters.equalTo("zone", "lombardy-plant-a");

		assertTrue(filter.matches(asset));
	}

	@Test
	void doesNotMatchDifferentValue() {
		Map<String, String> asset = Map.of("zone", "Lombardy-Plant-B");
		AssetFilter filter = Filters.equalTo("zone", "Lombardy-Plant-A");

		assertFalse(filter.matches(asset));
	}

	@Test
	void doesNotMatchMissingProperty() {
		Map<String, String> asset = Map.of();
		AssetFilter filter = Filters.equalTo("zone", "Lombardy-Plant-A");

		assertFalse(filter.matches(asset));
	}

	@Test
	void propertyNameIsCaseSensitive() {
		Map<String, String> asset = Map.of("zone", "Lombardy-Plant-A");
		AssetFilter filter = Filters.equalTo("Zone", "Lombardy-Plant-A");

		assertFalse(filter.matches(asset));
	}

	@Test
	void doesNotMatchNullPropertyValue() {
		Map<String, String> asset = new HashMap<>();
		asset.put("zone", null);

		AssetFilter filter = Filters.equalTo("zone", "Lombardy-Plant-A");

		assertFalse(filter.matches(asset));
	}

	@Test
	void rejectsNullPropertyName() {
		assertThrows(NullPointerException.class, () -> Filters.equalTo(null, "value"));
	}

	@Test
	void rejectsNullExpectedValue() {
		assertThrows(NullPointerException.class, () -> Filters.equalTo("zone", null));
	}
}