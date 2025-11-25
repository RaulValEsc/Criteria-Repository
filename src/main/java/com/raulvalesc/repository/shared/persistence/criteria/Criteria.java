package com.raulvalesc.repository.shared.persistence.criteria;

import com.raulvalesc.repository.shared.persistence.criteria.filters.FilterGroup;
import com.raulvalesc.repository.shared.persistence.criteria.filters.OrderDir;

public class Criteria {
    private FilterGroup filters;

    private String orderBy;

    private OrderDir orderDir;

    private int page;

    private int size;

    public Criteria() {}

    public Criteria(FilterGroup filters, String orderBy, OrderDir orderDir, int page, int size) {
        this.filters = filters;

        this.orderBy = orderBy;

        this.orderDir = orderDir;

        this.page = page;

        this.size = size;
    }

    public FilterGroup getFilters() {
        return filters;
    }

    public void setFilters(FilterGroup filters) {
        this.filters = filters;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public OrderDir getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(OrderDir orderDir) {
        this.orderDir = orderDir;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
