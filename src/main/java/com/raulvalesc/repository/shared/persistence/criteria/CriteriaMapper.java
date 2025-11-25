package com.raulvalesc.repository.shared.persistence.criteria;

import com.fasterxml.jackson.databind.JsonNode;
import com.raulvalesc.repository.shared.persistence.criteria.filters.*;

import java.util.ArrayList;
import java.util.List;

public class CriteriaMapper {
    public static Criteria fromJson(JsonNode node) {
        Criteria criteria = new Criteria();

        if (node.has("orderBy")) {
            criteria.setOrderBy(node.get("orderBy").asText());
        }

        if (node.has("orderDir")) {
            criteria.setOrderDir(OrderDir.valueOf(node.get("orderDir").asText()));
        }

        if (node.has("page")) {
            criteria.setPage(node.get("page").asInt());
        }

        if (node.has("size")) {
            criteria.setSize(node.get("size").asInt());
        }

        if (node.has("filters")) {
            FilterGroup group = parseGroup(node.get("filters"));

            criteria.setFilters(group);
        }

        return criteria;
    }

    private static FilterGroup parseGroup(JsonNode node) {
        FilterGroup group = new FilterGroup();

        group.setConnector(FilterConnector.valueOf(node.get("connector").asText()));

        List<BaseFilter> children = new ArrayList<>();

        for (JsonNode child : node.get("filters")) {
            if (child.has("filters")) {
                children.add(parseGroup(child));
            } else {
                children.add(parseCondition(child));
            }
        }

        group.setFilters(children);

        return group;
    }

    private static FilterCondition parseCondition(JsonNode node) {
        FilterCondition c = new FilterCondition();
        c.setField(node.get("field").asText());
        c.setValue(node.get("value").asText());
        c.setOperator(FilterOperator.valueOf(node.get("operator").asText()));
        return c;
    }
}