package dev.kkoncki.bandup.commons.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFormCriteria {
    private String fieldName;
    private Object value;
    private CriteriaOperator operator;
}
