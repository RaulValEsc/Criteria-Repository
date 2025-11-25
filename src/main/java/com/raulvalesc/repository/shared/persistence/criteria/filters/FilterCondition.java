package com.raulvalesc.repository.shared.persistence.criteria.filters;

public class FilterCondition implements BaseFilter {
    private String field;

    private FilterOperator operator;

    private String value;

    public FilterCondition() {}

    public FilterCondition(String field, FilterOperator operator, String value) {
        this.field = field;

        this.operator = operator;

        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public FilterOperator getOperator() {
        return operator;
    }

    public void setOperator(FilterOperator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
