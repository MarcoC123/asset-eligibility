package com.overit.asseteligibility.assetfilters.logical;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.overit.asseteligibility.assetfilters.AssetFilter;

public class AndFilter implements AssetFilter {

	private final List<AssetFilter> filters;

	public AndFilter(List<AssetFilter> filters) {
		Objects.requireNonNull(filters, "Filters must not be null");
		this.filters = List.copyOf(filters);
	}

	@Override
	public boolean matches(Map<String, String> asset) {
		Objects.requireNonNull(asset, "Asset must not be null");

		for (AssetFilter filter : filters) {
			if (!filter.matches(asset)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return filters.stream().map(Object::toString).collect(Collectors.joining(", ", "AND(", ")"));
	}
}
