package dev.kkoncki.bandup.commons.search;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SearchSpecification {

    public static <T> Specification<T> buildSpecification(List<SearchFormCriteria> criteriaList) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (SearchFormCriteria criteria : criteriaList) {
                String[] paramField = criteria.getFieldName().split("\\.");
                Path<?> fieldPath = getPath(root, paramField);
                predicates.add(buildPredicateForCriteria(cb, fieldPath, criteria));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    static <T> Path<?> getPath(Root<T> root, String[] paramField) {
        if (paramField.length == 2) {
            return root.join(paramField[0], JoinType.LEFT).get(paramField[1]);
        } else {
            return root.get(paramField[0]);
        }
    }

    static Predicate buildPredicateForCriteria(CriteriaBuilder cb, Path<?> fieldPath, SearchFormCriteria criteria) {
        switch (criteria.getOperator()) {
            case EQUALS:
                Object equalsValue = convertToType(criteria.getValue(), fieldPath.getJavaType());
                if (equalsValue == null) {
                    return cb.isNull(fieldPath);
                } else {
                    return cb.equal(fieldPath, equalsValue);
                }
            case NOT_EQUALS:
                Object notEqualsValue = convertToType(criteria.getValue(), fieldPath.getJavaType());
                if (notEqualsValue == null) {
                    return cb.isNotNull(fieldPath);
                } else {
                    return cb.notEqual(fieldPath, notEqualsValue);
                }
            case LIKE:
                return cb.like(fieldPath.as(String.class), "%" + criteria.getValue() + "%");
            case GR:
                return cb.greaterThan(fieldPath.as(Comparable.class), (Comparable) convertToType(criteria.getValue(), fieldPath.getJavaType()));
            case GRE:
                return cb.greaterThanOrEqualTo(fieldPath.as(Comparable.class), (Comparable) convertToType(criteria.getValue(), fieldPath.getJavaType()));
            case LS:
                return cb.lessThan(fieldPath.as(Comparable.class), (Comparable) convertToType(criteria.getValue(), fieldPath.getJavaType()));
            case LSE:
                return cb.lessThanOrEqualTo(fieldPath.as(Comparable.class), (Comparable) convertToType(criteria.getValue(), fieldPath.getJavaType()));
            default:
                throw new ApplicationException(ErrorCode.UNSUPPORTED_OPERATOR, "Unsupported operator: " + criteria.getOperator());
        }
    }

    static Object convertToType(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        if (targetType.isEnum()) {
            return Enum.valueOf((Class<Enum>) targetType, value.toString());
        } else if (targetType.equals(Instant.class)) {
            return Instant.parse(value.toString());
        } else {
            return value;
        }
    }

    public static PageRequest getPageRequest(SearchForm searchForm) {
        int page = searchForm.getPage() != null ? searchForm.getPage() - 1 : 0;
        int size = searchForm.getSize() != null ? searchForm.getSize() : Integer.MAX_VALUE;
        Sort sort = getSort(searchForm.getSort());
        return PageRequest.of(page, size, sort);
    }

    static Sort getSort(SearchSort searchSort) {
        if (searchSort == null) {
            return Sort.unsorted();
        } else {
            return searchSort.getOrder() == SearchSortOrder.ASC
                    ? Sort.by(searchSort.getBy()).ascending()
                    : Sort.by(searchSort.getBy()).descending();
        }
    }
}
