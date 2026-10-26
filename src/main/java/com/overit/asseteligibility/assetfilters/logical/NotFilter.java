package com.overit.asseteligibility.assetfilters.logical;

import java.util.Map;
import java.util.Objects;

import com.overit.asseteligibility.assetfilters.AssetFilter;

public final class NotFilter implements AssetFilter {

    private final AssetFilter filter;

    public NotFilter(AssetFilter filter) {
        this.filter = Objects.requireNonNull(filter,"Filter must not be null");
    }

    @Override
    public boolean matches(Map<String, String> asset) {
        Objects.requireNonNull(asset, "Asset must not be null");
        return !filter.matches(asset);
    }

    @Override
    public String toString() {
        return "NOT(" + filter + ")";
    }
}