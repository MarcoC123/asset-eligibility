package com.overit.asseteligibility.assetfilters.logical;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.overit.asseteligibility.assetfilters.AssetFilter;

public class OrFilter implements AssetFilter {

    private final List<AssetFilter> filters;

    public OrFilter(List<AssetFilter> filters) {
        Objects.requireNonNull(filters, "Filters must not be null");
        this.filters = List.copyOf(filters);
    }

    @Override
    public boolean matches(Map<String, String> asset) {
        Objects.requireNonNull(asset, "Asset must not be null");

        for (AssetFilter filter : filters) {
            if (filter.matches(asset)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return filters.stream()
            .map(Object::toString)
            .collect(Collectors.joining(", ", "OR(", ")"));
    }
}
