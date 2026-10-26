package com.overit.asseteligibility;

import com.overit.asseteligibility.assetfilters.AssetFilter;
import com.overit.asseteligibility.assetfilters.Filters;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



class NotFilterTest {

	private final Map<String, String> asset = Map.of("isOnline", "true");

	@Test
	void negatesMatchingFilter() {
		AssetFilter filter = Filters.not(Filters.equalTo("isOnline", "true"));

		assertFalse(filter.matches(asset));
	}

	@Test
	void negatesNonMatchingFilter() {
		AssetFilter filter = Filters.not(Filters.equalTo("isOnline", "false"));

		assertTrue(filter.matches(asset));
	}

	@Test
	void rejectsNullFilter() {
		assertThrows(NullPointerException.class, () -> Filters.not(null));
	}

	@Test
	void hasMeaningfulStringRepresentation() {
		AssetFilter filter = Filters.not(Filters.exists("disabled"));

		assertEquals("NOT(EXISTS(disabled))", filter.toString());
	}
}