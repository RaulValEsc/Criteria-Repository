package com.raulvalesc.repository.shared.persistence.criteria.filters;

import java.util.List;

public class FilterGroup implements BaseFilter {
    private FilterConnector connector;

    private List<BaseFilter> filters;

    public FilterGroup() {}

    public FilterGroup(FilterConnector connector, List<BaseFilter> filters) {
        this.connector = connector;

        this.filters = filters;
    }

    public FilterConnector getConnector() {
        return connector;
    }

    public void setConnector(FilterConnector connector) {
        this.connector = connector;
    }

    public List<BaseFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<BaseFilter> filters) {
        this.filters = filters;
    }
}
