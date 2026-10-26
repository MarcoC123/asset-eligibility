package com.overit.asseteligibility;

import com.overit.asseteligibility.assetfilters.AssetFilter;
import com.overit.asseteligibility.assetfilters.Filters;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.*;

public class BaseFiltersTest {

    @Test
    void alwaysFilterMatchesAnyAsset() {
        Map<String, String> asset = Map.of();
        boolean result = Filters.always().matches(asset);

        assertTrue(result);
    }

    @Test
    void neverFilterDoesNotMatchAnyAsset() {
        Map<String, String> asset = Map.of();
        boolean result = Filters.never().matches(asset);

        assertFalse(result);
    }

    @Test
    void existsMatchesWhenPropertyIsPresent() {
        Map<String, String> asset = Map.of("batteryLevel", "85");
        boolean result = Filters.exists("batteryLevel").matches(asset);

        assertTrue(result);
    }

    @Test
    void existsDoesNotMatchWhenPropertyIsMissing() {
        Map<String, String> asset = Map.of("batteryLevel", "85");
        boolean result = Filters.exists("firmwareVersion").matches(asset);

        assertFalse(result);
    }

    @Test
    void propertyNamesAreCaseSensitive() {
        Map<String, String> asset = Map.of("batteryLevel", "85");
        boolean result = Filters.exists("BatteryLevel").matches(asset);

        assertFalse(result);
    }

    @Test
    void filtersRejectNullAssets() {
        AssetFilter filter = Filters.always();
        
        assertThrows(NullPointerException.class,() -> filter.matches(null));
    }

    @Test
    void filtersHaveMeaningfulStringRepresentations() {
        assertEquals("ALWAYS", Filters.always().toString());
        assertEquals("NEVER", Filters.never().toString());
        assertEquals("EXISTS(batteryLevel)", Filters.exists("batteryLevel").toString());
    }
}
