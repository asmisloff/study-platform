package ru.asmisloff.studyplatform.controller.request.filtering;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class FilteringCriteria {

    private final String searchText;

    public FilteringCriteria(String searchText) {
        this.searchText = preparedForLikeExpression(searchText);
    }

    private String preparedForLikeExpression(@Nullable String s) {
        if (s != null) {
            s = s.trim().toLowerCase();
            return '%' + s + '%';
        }
        return "%";
    }
}
