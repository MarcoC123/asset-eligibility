package com.overit.asseteligibility;

import java.util.Map;
import org.junit.jupiter.api.Test;

import com.overit.asseteligibility.assetfilters.AssetFilter;
import com.overit.asseteligibility.assetfilters.Filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrFilterTest {

	private final Map<String, String> asset = Map.of(
			"zone", "Lombardy-Plant-A", 
			"isOnline", "true");

	@Test
	void matchesWhenAtLeastOneFilterMatches() {
		AssetFilter filter = Filters.or(
				Filters.equalTo("zone", "Lombardy-Plant-B"),
				Filters.equalTo("isOnline", "true"));

		assertTrue(filter.matches(asset));
	}

	@Test
	void doesNotMatchWhenNoFilterMatches() {
		AssetFilter filter = Filters.or(
				Filters.equalTo("zone", "Lombardy-Plant-B"),
				Filters.equalTo("isOnline", "false"));

		assertFalse(filter.matches(asset));
	}

	@Test
	void emptyOrDoesNotMatch() {
		AssetFilter filter = Filters.or();

		assertFalse(filter.matches(asset));
	}

	@Test
	void singleFilterDeterminesResult() {
		AssetFilter filter = Filters.or(Filters.exists("zone"));

		assertTrue(filter.matches(asset));
	}

	@Test
	void rejectsNullFilterArray() {
		assertThrows(NullPointerException.class, () -> Filters.or((AssetFilter[]) null));
	}

	@Test
	void rejectsNullFilterElement() {
		assertThrows(NullPointerException.class, () -> Filters.or(Filters.always(), null));
	}

	@Test
	void hasMeaningfulStringRepresentation() {
		AssetFilter filter = Filters.or(
				Filters.exists("zone"), 
				Filters.equalTo("isOnline", "true"));

		assertEquals("OR(EXISTS(zone), isOnline EQUALS \"true\")", filter.toString());
	}
}