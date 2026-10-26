package com.overit.asseteligibility.assetfilters;

import java.util.Map;

/**
 * Represents a criterion used to determine whether an asset is eligible.
 *
 * <p>An asset is represented as a map containing property or telemetry
 * attribute names and their corresponding string values. Property names are
 * case-sensitive.</p>
 *
 * <p>Implementations may evaluate a single property or combine multiple
 * filters into a more complex eligibility expression.</p>
 */

@FunctionalInterface
public interface AssetFilter {
	  boolean matches(Map<String, String> asset);
}
