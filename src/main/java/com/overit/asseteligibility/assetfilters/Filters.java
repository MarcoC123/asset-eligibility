package com.overit.asseteligibility.assetfilters;

import java.util.Arrays;

import com.overit.asseteligibility.assetfilters.basic.AlwaysFilter;
import com.overit.asseteligibility.assetfilters.basic.NeverFilter;
import com.overit.asseteligibility.assetfilters.basic.PropertyExistsFilter;
import com.overit.asseteligibility.assetfilters.enums.NumericComparisonOperator;
import com.overit.asseteligibility.assetfilters.logical.AndFilter;
import com.overit.asseteligibility.assetfilters.logical.EqualsFilter;
import com.overit.asseteligibility.assetfilters.logical.NotFilter;
import com.overit.asseteligibility.assetfilters.logical.OrFilter;
import com.overit.asseteligibility.assetfilters.numericfilters.NumericComparisonFilter;
import com.overit.asseteligibility.assetfilters.numericfilters.NumericPropertiesComparisonFilter;

/**
 * Provides factory methods for creating and composing asset eligibility
 * filters.
 *
 * <p>An asset is represented as a map of case-sensitive property names to
 * string values. The filters created by this class can evaluate individual
 * properties or combine multiple conditions using logical operators.</p>
 *
 * <p>Example:</p>
 *
 * <pre>{@code
 * AssetFilter filter = Filters.and(
 *     Filters.equalTo("zone", "Lombardy-Plant-A"),
 *     Filters.equalTo("isOnline", "true"),
 *     Filters.numeric(
 *         "batteryLevel",
 *         20,
 *         NumericComparisonOperator.GREATER_THAN_OR_EQUAL
 *     )
 * );
 *
 * boolean eligible = filter.matches(asset);
 * }</pre>
 */
public final class Filters {

	private Filters() {
	}

	// =========== BASE FILTERS ===========
	
	public static AssetFilter always() {
		return new AlwaysFilter();
	}

	public static AssetFilter never() {
		return new NeverFilter();
	}

	public static AssetFilter exists(String propertyName) {
		return new PropertyExistsFilter(propertyName);
	}

	// ========== LOGIC FILTERS - EQUALS, OR, NOT, AND ===========
	
	
    /**
     * Creates a filter that compares a property value with an expected
     * string value.
     *
     * <p>Property names are case-sensitive, while property values are
     * compared case-insensitively.</p>
     *
     * @param propertyName name of the property to evaluate
     * @param expectedValue expected property value
     * @return a filter that matches when the values are equal, ignoring case
     * @throws NullPointerException if either argument is {@code null}
     */
	public static AssetFilter equalTo(String propertyName, String expectedValue) {
		return new EqualsFilter(propertyName, expectedValue);
	}

	  /**
     * Creates a logical OR composition.
     *
     * <p>The resulting filter matches when at least one supplied filter
     * matches. An empty OR composition returns {@code false}.</p>
     *
     * @param filters filters to combine
     * @return the logical OR of the supplied filters
     * @throws NullPointerException if the array or one of its elements is
     *         {@code null}
     */
	public static AssetFilter or(AssetFilter... filters) {
		if (filters == null) {
			throw new NullPointerException("Filters must not be null");
		}

		return new OrFilter(Arrays.asList(filters));
	}

	   /**
     * Creates a filter that negates another filter.
     *
     * @param filter filter whose result must be negated
     * @return a filter returning the opposite result
     * @throws NullPointerException if {@code filter} is {@code null}
     */
	public static AssetFilter not(AssetFilter filter) {
		return new NotFilter(filter);
	}

    /**
     * Creates a logical AND composition.
     *
     * <p>The resulting filter matches only when every supplied filter
     * matches. An empty AND composition returns {@code true}.</p>
     *
     * @param filters filters to combine
     * @return the logical AND of the supplied filters
     * @throws NullPointerException if the array or one of its elements is
     *         {@code null}
     */
	public static AssetFilter and(AssetFilter... filters) {
		if (filters == null) {
			throw new NullPointerException("Filters must not be null");
		}

		return new AndFilter(Arrays.asList(filters));
	}
	
	// =========== NUMERIC FILTERS  ===========
	
    /**
     * Creates a numeric filter that compares an asset property with a fixed
     * reference value.
     *
     * <p>The filter does not match when the property is missing, cannot be
     * parsed as a number, or contains a non-finite value.</p>
     *
     * @param propertyName name of the numeric property to evaluate
     * @param referenceValue fixed value used for the comparison
     * @param operator numeric comparison to perform
     * @return a filter that performs the configured numeric comparison
     * @throws NullPointerException if {@code propertyName} or
     *         {@code operator} is {@code null}
     * @throws IllegalArgumentException if {@code referenceValue} is not finite
     */
	public static AssetFilter numeric(String propertyName, double referenceValue, NumericComparisonOperator operator) {

		return new NumericComparisonFilter(propertyName, referenceValue, operator);
	}

	
    /**
     * Creates a numeric filter that compares the values of two properties
     * belonging to the same asset.
     *
     * <p>The filter does not match when either property is missing, cannot be
     * parsed as a number, or contains a non-finite value.</p>
     *
     * @param propertyName name of the property providing the value to evaluate
     * @param referencePropertyName name of the property providing the
     *        reference value
     * @param operator numeric comparison to perform
     * @return a filter that compares the two property values
     * @throws NullPointerException if any argument is {@code null}
     */
	public static AssetFilter compareNumericProperties(String propertyName, String referencePropertyName, NumericComparisonOperator operator) {

		return new NumericPropertiesComparisonFilter(propertyName, referencePropertyName, operator);
	}
}
