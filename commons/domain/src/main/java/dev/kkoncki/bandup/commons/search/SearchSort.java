package dev.kkoncki.bandup.commons.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchSort {
    private String by;
    private SearchSortOrder order;
}
