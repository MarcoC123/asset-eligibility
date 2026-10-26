package com.overit.asseteligibility;

import java.util.Map;
import org.junit.jupiter.api.Test;

import com.overit.asseteligibility.assetfilters.AssetFilter;
import com.overit.asseteligibility.assetfilters.Filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AndFilterTest {

	private final Map<String, String> asset = Map.of("zone", "Lombardy-Plant-A", "isOnline", "true", "batteryLevel",
			"85");

	@Test
	void matchesWhenAllFiltersMatch() {
		AssetFilter filter = Filters.and(
				Filters.equalTo("zone", "Lombardy-Plant-A"), 
				Filters.equalTo("isOnline", "true"),
				Filters.exists("batteryLevel"));

		assertTrue(filter.matches(asset));
	}

	@Test
	void doesNotMatchWhenOneFilterDoesNotMatch() {
		AssetFilter filter = Filters.and(
				Filters.equalTo("zone", "Lombardy-Plant-A"),
				Filters.equalTo("isOnline", "false"), 
				Filters.exists("batteryLevel"));

		assertFalse(filter.matches(asset));
	}

	@Test
	void emptyAndMatches() {
		AssetFilter filter = Filters.and();

		assertTrue(filter.matches(asset));
	}

	@Test
	void singleFilterDeterminesResult() {
		AssetFilter filter = Filters.and(Filters.exists("zone"));

		assertTrue(filter.matches(asset));
	}

	@Test
	void rejectsNullFilterArray() {
		assertThrows(NullPointerException.class, () -> Filters.and((AssetFilter[]) null));
	}

	@Test
	void rejectsNullFilterElement() {
		assertThrows(NullPointerException.class, () -> Filters.and(Filters.always(), null));
	}

	@Test
	void hasMeaningfulStringRepresentation() {
		AssetFilter filter = Filters.and(Filters.exists("zone"), Filters.equalTo("isOnline", "true"));

		assertEquals("AND(EXISTS(zone), isOnline EQUALS \"true\")", filter.toString());
	}
}
