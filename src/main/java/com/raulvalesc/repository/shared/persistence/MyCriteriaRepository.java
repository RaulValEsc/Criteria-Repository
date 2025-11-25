package com.raulvalesc.repository.shared.persistence;

import com.raulvalesc.repository.shared.persistence.criteria.Criteria;
import com.raulvalesc.repository.shared.persistence.criteria.filters.FilterCondition;
import com.raulvalesc.repository.shared.persistence.criteria.filters.FilterGroup;
import com.raulvalesc.repository.shared.persistence.criteria.filters.OrderDir;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class MyCriteriaRepository<Entity, EntityId> {
    private final Class<Entity> entityClass;

    @Autowired
    private EntityManager entityManager;

    protected MyCriteriaRepository(Class<Entity> entityClass) {
        this.entityClass = entityClass;
    }

    @Transactional
    protected Entity _findById(EntityId id) {
        return entityManager.find(entityClass, id);
    }

    @Transactional
    protected void _remove(Entity entity) {
        entityManager.remove(entity);
    }

    @Transactional
    protected void _save(Entity entity) {
        entityManager.persist(entity);
    }

    @Transactional
    protected SearchResponse<Entity> _searchByCriteria(Criteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(entityClass);

        Root<Entity> root = criteriaQuery.from(entityClass);

        if (OrderDir.ASC.equals(criteria.getOrderDir())) {
            criteriaQuery.orderBy(criteriaBuilder.asc(generateVariableRootPath(criteria.getOrderBy(), root)));
        } else if (OrderDir.DESC.equals(criteria.getOrderDir())) {
            criteriaQuery.orderBy(criteriaBuilder.desc(generateVariableRootPath(criteria.getOrderBy(), root)));
        }

        Predicate criteriaFilter = this.processFilterGroup(criteria.getFilters(), criteriaBuilder, root);

        criteriaQuery.where(criteriaFilter);

        criteriaQuery.select(root);

        TypedQuery<Entity> query = entityManager.createQuery(criteriaQuery);

        int total = query.getResultList().size();

        query.setFirstResult(criteria.getPage() * criteria.getSize());

        query.setMaxResults(criteria.getSize());

        List<Entity> list = query.getResultList();

        return new SearchResponse<Entity>(total, list);
    }

    @Transactional
    public void _update(Entity entity) {
        this.entityManager.merge(entity);
    }

    private Predicate processFilterGroup(FilterGroup filterGroup, CriteriaBuilder criteriaBuilder, Root<Entity> root) {
        List<Predicate> predicates = filterGroup.getFilters().stream().map((filter) -> {
            if (filter instanceof FilterCondition) {
                return this.processFilterCondition((FilterCondition) filter, criteriaBuilder, root);
            }

            if (filter instanceof FilterGroup) {
                return this.processFilterGroup((FilterGroup) filter, criteriaBuilder, root);
            }

            return null;
        }).toList();

        return switch (filterGroup.getConnector()) {
            case AND ->
                criteriaBuilder.and(predicates.toArray(new Predicate[0]));

            case OR ->
                criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    private Predicate processFilterCondition(FilterCondition filterCondition, CriteriaBuilder criteriaBuilder, Root<Entity> root) {
        Path<?> path = generateVariableRootPath(filterCondition.getField(), root);

        Object value = filterCondition.getValue();

        Class<?> type = path.getJavaType();

        return switch (filterCondition.getOperator()) {
            case EQUALS ->
                    criteriaBuilder.equal(path, value);

            case NOT_EQUALS ->
                    criteriaBuilder.notEqual(path, value);

            case CONTAINS ->
                    criteriaBuilder.like(path.as(String.class), "%" + value + "%");

            case STARTS_WITH ->
                    criteriaBuilder.like(path.as(String.class), value + "%");

            case ENDS_WITH ->
                    criteriaBuilder.like(path.as(String.class), "%" + value);

            case GREATER_THAN -> {
                if (!Comparable.class.isAssignableFrom(type)) {
                    throw new IllegalArgumentException("Field " + filterCondition.getField() + " is not comparable");
                }

                @SuppressWarnings("unchecked")
                Expression<? extends Comparable> exp = (Expression<? extends Comparable>) path;

                yield criteriaBuilder.greaterThan(exp, (Comparable) value);
            }

            case LESS_THAN -> {
                if (!Comparable.class.isAssignableFrom(type)) {
                    throw new IllegalArgumentException("Field " + filterCondition.getField() + " is not comparable");
                }

                @SuppressWarnings("unchecked")
                Expression<? extends Comparable> exp = (Expression<? extends Comparable>) path;

                yield criteriaBuilder.lessThan(exp, (Comparable) value);
            }

            default ->
                    throw new IllegalArgumentException("Invalid filter operator: " + filterCondition.getOperator());
        };
    }

    private Path<?> generateVariableRootPath(String variableName, Root<Entity> root) {
        Path<?> path = root;

        for (String variable : variableName.split("\\.")) {
            path = path.get(variable);
        }

        return path;
    }
}
