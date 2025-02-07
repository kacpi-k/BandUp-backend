package dev.kkoncki.bandup.commons.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchForm {
    private List<SearchFormCriteria> criteria;
    private Integer page;
    private Integer size;
    private SearchSort sort;
}
